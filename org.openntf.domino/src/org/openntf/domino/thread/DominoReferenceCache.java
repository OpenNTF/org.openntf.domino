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

/**
 * Class to cache OpenNTF-Domino-wrapper objects. The wrapper and its delegate is stored in a phantomReference. This reference is queued if
 * the wrapper Object is GC. Then the delegate gets recycled.
 * 
 * K: the Key Type (Integer or Long)
 * 
 * V: the OpenNTF Wrapper
 * 
 * The DominoReference unwraps the Wrapper and caches the delegate and key, so that it can do the cleanup if the wrapper dies!
 * 
 * @author Roland Praml, Foconis AG
 */

public class DominoReferenceCache<K, V extends org.openntf.domino.Base> {
	/** The delegate map contains the value wrapped in phantomReferences) **/
	private Map<K, DominoReference<K, V, lotus.domino.Base>> map = new HashMap<K, DominoReference<K, V, lotus.domino.Base>>(16, 0.75F);

	/** This is the queue with unreachable refs **/
	private ReferenceQueue<V> queue = new ReferenceQueue<V>();

	/** Needed for objects that should not get recycled, like sessions **/
	private boolean autorecycle_;

	/**
	 * needed to call GC periodically. The optimum is somwhere ~1500. 1024 seems to be a good value
	 */
	private static AtomicInteger cache_counter = new AtomicInteger();
	public static int GARBAGE_INTERVAL = 1024;

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
	public V get(final K key) {
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
	public void put(final K key, final V value, final lotus.domino.Base delegate) {
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
		if (value == null)
			return;
		DominoReference<K, V, lotus.domino.Base> ref = new DominoReference<K, V, lotus.domino.Base>(key, value, delegate, queue);
		if (key == null)
			return;
		map.put(key, ref);
	}

	/**
	 * Removes all garbage collected values with their keys from the map. Since we don't know how much the ReferenceQueue.poll() operation
	 * costs, we should not call it every map operation.
	 * 
	 * @param b
	 */
	@SuppressWarnings("unchecked")
	public void processQueue() {

		int counter = cache_counter.incrementAndGet();
		if (counter % GARBAGE_INTERVAL == 0) {
			System.gc();
		}

		DominoReference<K, V, lotus.domino.Base> ref = null;

		while ((ref = (DominoReference<K, V, lotus.domino.Base>) queue.poll()) != null) {
			K key = ref.getKey();
			if (key != null) {
				map.remove(key);
			}
			if (autorecycle_) {
				ref.recycle();
			}
		}

	}

	/**
	 * A convenience method to return the object held by the weak reference or <code>null</code> if it does not exist.
	 */
	protected final V getReferenceObject(final DominoReference<K, V, lotus.domino.Base> ref) {

		if (ref != null) {
			V result = ref.get();
			if (result != null) {
				lotus.domino.Base delegate = org.openntf.domino.impl.Base.getDelegate(result);
				if (org.openntf.domino.impl.Base.isInvalid(delegate)) {
					// check if it is not yet recycled
					map.remove(ref.getKey());
				} else {
					return result;
				}
			}
		}
		return null;
	}
}
