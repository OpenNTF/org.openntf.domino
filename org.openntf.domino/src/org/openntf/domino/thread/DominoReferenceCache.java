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

import lotus.domino.local.NotesBase;

import org.openntf.domino.Base;

/**
 * Class to cache OpenNTF-Domino-wrapper objects. The wrapper and its delegate is stored in a phantomReference. This reference is queued if
 * the wrapper Object is GC. Then the delegate gets recycled.
 * 
 * @author Roland Praml, Foconis AG
 */

public class DominoReferenceCache<T> {
	/** The delegate map contains the value wrapped in phantomReferences) **/
	private Map<T, DominoReference<T>> delegate = new HashMap<T, DominoReference<T>>(16, 0.75F);

	/** This is the queue with unreachable refs **/
	private ReferenceQueue<Base> queue = new ReferenceQueue<Base>();

	/** Needed for objects that should not get recycled, like sessions **/
	private boolean autorecycle_;

	/**
	 * @param autorecycle
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
	public Base get(final T key) {
		// We don't need to remove garbage collected values here;
		// if they are garbage collected, the get() method returns null;
		// the next put() call with the same key removes the old value
		// automatically so that it can be completely garbage collected
		return getReferenceObject(delegate.get(key));
	}

	/**
	 * Puts a new (key,value) into the map.
	 * <p>
	 * 
	 * @param key
	 *            key with which the specified value is to be associated.
	 * @param value
	 *            value to be associated with the specified key.
	 * @return previous value associated with specified key, or null if there was no mapping for key or the value has been garbage collected
	 *         by the garbage collector.
	 */
	public void put(final T key, final Base value) {
		// If the map already contains an equivalent key, the new key
		// of a (key, value) pair is NOT stored in the map but the new
		// value only. But as the key is strongly referenced by the
		// map, it can not be removed from the garbage collector, even
		// if the key becomes weakly reachable due to the old
		// value. So, it isn't necessary to remove all garbage
		// collected values with their keys from the map before the
		// new entry is made. We only clean up here to distribute
		// clean up calls on different operations.
		processQueue();
		DominoReference<T> ref = new DominoReference((Base) value, queue, key);
		delegate.put(key, ref);
	}

	/**
	 * Removes all garbage collected values with their keys from the map. Since we don't know how much the ReferenceQueue.poll() operation
	 * costs, we should not call it every map operation.
	 */
	@SuppressWarnings("unchecked")
	public void processQueue() {
		// System.gc(); // TODO TODO
		DominoReference<T> ref = null;
		while ((ref = (DominoReference<T>) queue.poll()) != null) {
			T key = ref.getKey();
			if (key != null) {
				// only recycle refs that have a key
				delegate.remove(key);
				if (autorecycle_) {
					// System.out.println("- " + key);
					ref.recycle();
				}
			}
		}
	}

	/**
	 * A convenience method to return the object held by the weak reference or <code>null</code> if it does not exist.
	 */
	protected final Base getReferenceObject(final DominoReference<T> ref) {

		if (ref != null) {

			Base result = ref.get();
			if (result != null) {
				lotus.domino.Base delegate = org.openntf.domino.impl.Base.getDelegate((org.openntf.domino.Base) result);
				if (delegate != null) {
					// check if it is not yet recycled
					if (!org.openntf.domino.impl.Base.isRecycled((NotesBase) delegate)) {
						return result;
					}
				}
			}
		}
		return null;
	}
}
