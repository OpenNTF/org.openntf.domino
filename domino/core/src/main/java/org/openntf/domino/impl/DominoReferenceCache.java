/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.impl;

import java.lang.ref.ReferenceQueue;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.Base;

import org.openntf.domino.utils.Factory;

/**
 * Class to cache OpenNTF-Domino-wrapper objects. The wrapper and its delegate is stored in a phantomReference. This reference is queued if
 * the wrapper Object is GC. Then the delegate gets recycled.
 * 
 * We don't optimize the map key any more (shifting unused bits out), because there was no measurable performance gain
 * 
 * 
 * The DominoReference tracks the wrapper lifetime and caches the delegate and key, so that it can do the cleanup if the wrapper dies!
 * 
 * @author Roland Praml, Foconis AG
 */

public class DominoReferenceCache {
	private static final Logger log_ = Logger.getLogger(DominoReferenceCache.class.getName());

	/** The delegate map contains the value wrapped in phantomReferences) **/
	//	private Map<Long, DominoReference> map = new HashMap<Long, DominoReference>(16, 0.75F);
	private Map<lotus.domino.Base, DominoReference> map = new IdentityHashMap<lotus.domino.Base, DominoReference>(2048);

	/** This is the queue with unreachable refs **/
	private ReferenceQueue<Object> queue = new ReferenceQueue<Object>();

	/**
	 * needed to call GC periodically. The optimum is somewhere ~1500. 1024 seems to be a good value
	 */
	private static AtomicInteger cache_counter = new AtomicInteger();
	public static int GARBAGE_INTERVAL = 1024;

	/**
	 * Creates a new DominoReferencCache
	 * 
	 * @param autorecycle
	 *            true if the cache should recycle objects if they are weakly reachable
	 * 
	 */
	public DominoReferenceCache() {
		super();
		//autorecycle_ = autorecycle;
	}

	/**
	 * Gets the value for the given key.
	 * <p>
	 * 
	 * @param key
	 *            key whose associated value, if any, is to be returned
	 * @return the value to which this map maps the specified key.
	 */
	public org.openntf.domino.Base<?> get(final lotus.domino.Base key) {
		//		processQueue(key, parent_key);
		// We don't need to remove garbage collected values here;
		// if they are garbage collected, the get() method returns null;
		// the next put() call with the same key removes the old value
		// automatically so that it can be completely garbage collected
		if (key == null) {
			return null;
		} else {
			return getReferenceObject(map.get(key));
		}
	}

	public void setNoRecycle(final lotus.domino.Base key, final boolean value) {
		DominoReference ref = map.get(key);
		if (ref == null) {
			log_.log(Level.WARNING, "Attemped to set noRecycle on a DominoReference id " + key + " that's not in the local reference cache");
		} else {
			ref.setNoRecycle(value);
		}
	}

	/**
	 * Puts a new (key,value) into the map.
	 * <p>
	 * 
	 * @param key
	 *            key with which the specified value is to be associated.
	 * @param value
	 *            value to be associated with the specified key.
	 * @return The previous value associated with the delegate, if any
	 */
	public org.openntf.domino.Base<?> put(final lotus.domino.Base delegate, final org.openntf.domino.Base<?> value) {
		// If the map already contains an equivalent key, the new key
		// of a (key, value) pair is NOT stored in the map but the new
		// value only. But as the key is strongly referenced by the
		// map, it can not be removed from the garbage collector, even
		// if the key becomes weakly reachable due to the old
		// value. So, it isn't necessary to remove all garbage
		// collected values with their keys from the map before the
		// new entry is made. We only clean up here to distribute
		// clean up calls on different operations.

		if (value == null) {
			return null;
		}
		// create and enqueue a reference that tracks lifetime of value
		DominoReference ref = new DominoReference(value, queue, delegate);
		if (delegate == null) {
			throw new IllegalArgumentException("key cannot be 0");
		}
		Factory.countLotus(delegate.getClass());
		return getReferenceObject(map.put(delegate, ref));
	}

	/**
	 * Removes all garbage collected values with their keys from the map.
	 * 
	 */
	public long processQueue(final lotus.domino.Base current, final Collection<lotus.domino.Base> prevent_recycling) {
		long result = 0;
		if (current instanceof lotus.domino.Item) {
			// do not count items, as we have many of them
		} else if (current instanceof lotus.domino.MIMEEntity) {
			// Mimeentities are recycled on closeMimeEntities
		} else if (current instanceof lotus.domino.MIMEHeader) {
			// same for header
		} else {
			// count only "heavy" objects (list above not yet complete)
			int counter = cache_counter.incrementAndGet();
			if (counter % GARBAGE_INTERVAL == 0) {
				// We have to run GC from time to time, otherwise objects will die very late :(
				System.gc();
			}
		}
		DominoReference ref = null;

		while ((ref = (DominoReference) queue.poll()) != null) {
			Base unrefLotus = ref.getDelegate();

			map.remove(unrefLotus);
			if (unrefLotus == null) {
				// should never happen
			} else if (unrefLotus == current) {
				ref.setNoRecycle(true);
			} else if (prevent_recycling != null) {
				for (Base curKey : prevent_recycling) {
					if (curKey == unrefLotus) {

						// TODO: This case does not handle the counters correctly
						// System.out.println("Parent object dropped out of the queue ---");

						// RPr: This happens definitely, if you access the same document in series
						// Was reproduceable by iterating over several NoteIds and doing this in the loop (odd number required)
						//		doc1 = d.getDocumentByID(id);
						//		doc1 = null;
						//		doc2 = d.getDocumentByID(id);
						//		doc2 = null;
						//		doc3 = d.getDocumentByID(id);
						//		doc3 = null;

						// ref is not used any more, but this object must be protected from recycling, because it will be reused in the next step
						// Q RPr: Who sets this back to False?
						// A RPr: That is not needed, because this "ref" is not used any more and the lotus object gets wrapped in a new DominoReference.
						ref.setNoRecycle(true);
						break;
					}
				}
			}

			if (ref.recycle())
				result++;
		}
		return result;
	}

	public long finishThreadSafes() {
		long ret = 0;
		//NTF can't remove values from a map while iterating over them...
		Object[] raws = map.values().toArray();
		for (Object raw : raws) {
			DominoReference ref = (DominoReference) raw;
			if (!(ref.get() instanceof BaseThreadSafe))
				continue;
			if (ref.recycle())
				ret++;
			map.remove(ref.getDelegate());
		}
		return ret;
	}

	/**
	 * A convenience method to return the object held by the weak reference or <code>null</code> if it does not exist.
	 */
	protected final org.openntf.domino.Base<?> getReferenceObject(final DominoReference ref) {

		if (ref == null) {
			return null;
		}

		org.openntf.domino.Base<?> result = ref.get();
		//if (result == null) {
		//	// this is the wrapper. If there is no wrapper inside (don't know how this should work) we do not return it
		//	System.out.println("Wrapper lost! " + ref.isEnqueued());
		//	return null;
		//}

		// check if the delegate is still alive. (not recycled or null)
		if (result == null || ref.isEnqueued()) {

			// For debug
			//			if (result == null) {
			//				System.out.println("NULL"); // happens, if there is no strong ref to this wrapper.
			//			}
			//			if (ref.isDead()) {
			//				System.out.println("DEAD"); // happens, if you call recycle on the parent()
			//			}
			//			if (ref.isEnqueued()) {
			//				// result is also NULL if the reference is enqueued (makes sense, because result (=wrapper) is no longer reachable 
			//				System.out.println("ENQUEUED");
			//			}

			// 	we get dead elements if we do this in a loop
			//		d = s.getDatabase("", "names.nsf");
			//		doc1 = d.getDocumentByID(id);
			//		d = null;
			//		doc1 = null;

			// So, these objects can be removed from the cache
			map.remove(ref.getDelegate());
			return null;
		} else {
			return result; // the wrapper.
		}
	}

}
