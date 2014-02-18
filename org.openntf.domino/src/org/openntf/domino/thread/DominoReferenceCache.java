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
package org.openntf.domino.thread;

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
	/** The delegate map contains the value wrapped in phantomReferences) **/
	private Map<Long, DominoReference> map = new HashMap<Long, DominoReference>(16, 0.75F);

	/** This is the queue with unreachable refs **/
	private ReferenceQueue<Object> queue = new ReferenceQueue<Object>();

	/** Needed for objects that should not get recycled, like sessions **/
	private boolean autorecycle_;

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
	public DominoReferenceCache(final boolean autorecycle) {
		super();
		autorecycle_ = autorecycle;
	}

	/**
	 * Gets the value for the given key.
	 * <p>
	 * 
	 * @param key
	 *            key whose associated value, if any, is to be returned
	 * @return the value to which this map maps the specified key.
	 */
	public Object get(final long key, final long parent_key) {
		//		processQueue(key, parent_key);
		// We don't need to remove garbage collected values here;
		// if they are garbage collected, the get() method returns null;
		// the next put() call with the same key removes the old value
		// automatically so that it can be completely garbage collected
		if (key == 0) {
			return null;
		} else {
			return getReferenceObject(map.get(key));
		}
	}

	/**
	 * returns a object of Type T
	 * 
	 * @param key
	 *            the cpp-id
	 * @param t
	 *            the class to return
	 * @return the object
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final long key, final Class<T> t, final long parent_key) {
		Object o = get(key, parent_key);
		if (o != null) {
			return (T) o;
		}
		return null;
	}

	/**
	 * Puts a new (key,value) into the map.
	 * <p>
	 * 
	 * @param key
	 *            key with which the specified value is to be associated.
	 * @param value
	 *            value to be associated with the specified key.
	 */
	public void put(final long key, final Object value, final lotus.domino.Base delegate, final long parent_key) {
		// If the map already contains an equivalent key, the new key
		// of a (key, value) pair is NOT stored in the map but the new
		// value only. But as the key is strongly referenced by the
		// map, it can not be removed from the garbage collector, even
		// if the key becomes weakly reachable due to the old
		// value. So, it isn't necessary to remove all garbage
		// collected values with their keys from the map before the
		// new entry is made. We only clean up here to distribute
		// clean up calls on different operations.
		processQueue(key, parent_key);
		if (value == null) {
			return;
		}
		// create and enqueue a reference that tracks lifetime of value
		DominoReference ref = new DominoReference(key, value, delegate, queue);
		if (key == 0) {
			throw new IllegalArgumentException("key cannot be 0");
		}
		map.put(key, ref);
		if (autorecycle_) {
			Factory.countLotus(delegate.getClass());
		}
	}

	/**
	 * Removes all garbage collected values with their keys from the map.
	 * 
	 */
	public void processQueue(final long curKey, final long parent_key) {

		int counter = cache_counter.incrementAndGet();
		if (counter % 1024 == 0) {
			// We have to run GC from time to time, otherwise objects will die very late :(
			System.gc();
		}

		DominoReference ref = null;

		while ((ref = (DominoReference) queue.poll()) != null) {
			long key = ref.getKey();

			if (map.remove(key) == null) {
				System.out.println("Removed dead element");
			}
			if (autorecycle_) {
				if (curKey == key || parent_key == key) {
					// System.out.println("Current object dropped out of the queue");

					// RPr: This happens definitely, if you access the same document in series
					// Was reproduceable by iterating over several NoteIds and doing this in the loop (odd number required)
					//		doc1 = d.getDocumentByID(id);
					//		doc1 = null;
					//		doc2 = d.getDocumentByID(id);
					//		doc2 = null;
					//		doc3 = d.getDocumentByID(id);
					//		doc3 = null;

					// ref is not used any more, but the delegate must be protected from recycling
					ref.setNoRecycle(true);
				}
				ref.recycle();
			}

		}
	}

	/**
	 * A convenience method to return the object held by the weak reference or <code>null</code> if it does not exist.
	 */
	protected final Object getReferenceObject(final DominoReference ref) {

		if (ref == null) {
			return null;
		}

		Object result = ref.get();
		//if (result == null) {
		//	// this is the wrapper. If there is no wrapper inside (don't know how this should work) we do not return it
		//	System.out.println("Wrapper lost! " + ref.isEnqueued());
		//	return null;
		//}

		// check if the delegate is still alive. (not recycled or null)
		if (result == null || ref.isDead() || ref.isEnqueued()) {

			// For debug
			//			if (result == null) {
			//				System.out.println("NULL");
			//			}
			//			if (ref.isDead()) {
			//				System.out.println("DEAD");
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

			if (ref.isEnqueued()) {
				// TODO: I'm not sure, when we must setNoRecycle to true
				// ref.setNoRecycle(true);
			}
			map.remove(ref.getKey());
			return null;
		} else {
			//			if (ref.isEnqueued()) {
			//				// if it's enqueued, we definitely can't use the existing reference, because it's going to be recycled for sure
			//				ref = ref.resurrect(queue); // so we resurrect this one. processQueue MUST NOT remove the key from the map
			//				map.put(ref.getKey(), ref); // and replace the new one in the map
			//				System.out.println("Ressurect for dominoReference occured");
			//			}
			return result; // the wrapper.
		}
	}
}
