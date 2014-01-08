package org.openntf.domino.thread;

/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 * Original implementation
 * http://svn.apache.org/repos/asf/db/jdo/branches/2.0.1/core20/src/java/org/apache/jdo/util/WeakValueHashMap.java
 */

import java.lang.ref.ReferenceQueue;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import lotus.domino.local.NotesBase;

import org.openntf.domino.Base;

/**
 * A WeakValueHashMap is implemented as a HashMap that maps keys to WeakValues. Because we don't have access to the innards of the HashMap,
 * we have to wrap/unwrap value objects with WeakValues on every operation. Fortunately WeakValues are small, short-lived objects, so the
 * added allocation overhead is tolerable. This implementation delegates to java.util.HashMap.
 * 
 * @author Markus Fuchs
 * @see java.util.HashMap
 * @see java.lang.ref.WeakReference
 * 
 * @author Roland Praml: Added genrics
 */

public class DominoReferenceHashMap<T> implements Map<T, Base> {

	private Map<T, DominoReference<T>> delegate = new HashMap<T, DominoReference<T>>();

	/* Reference queue for cleared refs */
	// TODO: Check if this should be threadLocal!
	//  private ReferenceQueue<Base> queue = new ReferenceQueue<Base>();

	private ThreadLocal<ReferenceQueue<Base>> queue = new ThreadLocal<ReferenceQueue<Base>>() {
		@Override
		protected ReferenceQueue<Base> initialValue() {
			return new ReferenceQueue<Base>();
		}
	};

	private boolean autorecycle_;

	/**
	 * @param autorecycle
	 */
	public DominoReferenceHashMap(final boolean autorecycle) {
		super();
		autorecycle_ = autorecycle;
	}

	/**
	 * Returns the number of key-value mappings in this map.
	 * <p>
	 * 
	 * @return the number of key-value mappings in this map.
	 */
	@Override
	public int size() {
		// delegate to entrySet, as super.size() also counts WeakValues
		processQueue();
		return entrySet().size();
	}

	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings.
	 * <p>
	 * 
	 * @return <tt>true</tt> if this map contains no key-value mappings.
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns <tt>true</tt> if this map contains a mapping for the specified key.
	 * <p>
	 * 
	 * @param key
	 *            key whose presence in this map is to be tested
	 * @return <tt>true</tt> if this map contains a mapping for the specified key.
	 */
	@Override
	public boolean containsKey(final Object key) {
		// need to clean up gc'ed values before invoking super method
		processQueue();
		return delegate.containsKey(key);
	}

	/**
	 * Returns <tt>true</tt> if this map maps one or more keys to the specified value.
	 * <p>
	 * 
	 * @param value
	 *            value whose presence in this map is to be tested
	 * @return <tt>true</tt> if this map maps one or more keys to this value.
	 */
	@Override
	public boolean containsValue(final Object value) {
		if (value instanceof Base) {
			// we must wrap the incoming object in a reference, null key means - no recycling is done!
			DominoReference<T> ref = new DominoReference((Base) value, getQueue(), null);
			return delegate.containsValue(ref);
		} else {
			return false;
		}
	}

	/**
	 * Gets the value for the given key.
	 * <p>
	 * 
	 * @param key
	 *            key whose associated value, if any, is to be returned
	 * @return the value to which this map maps the specified key.
	 */
	@Override
	public Base get(final Object key) {
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
	@Override
	public Base put(final T key, final Base value) {
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
		DominoReference<T> ref = new DominoReference((Base) value, getQueue(), key);
		return getReferenceObject(delegate.put(key, ref));
	}

	/**
	 * @return
	 */
	protected ReferenceQueue<Base> getQueue() {
		return queue.get();
	}

	/**
	 * Removes key and value for the given key.
	 * <p>
	 * 
	 * @param key
	 *            key whose mapping is to be removed from the map.
	 * @return previous value associated with specified key, or null if there was no mapping for key or the value has been garbage collected
	 *         by the garbage collector.
	 */
	@Override
	public Base remove(final Object key) {
		return getReferenceObject(delegate.remove(key));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractMap#entrySet()
	 */

	@Override
	public Set<java.util.Map.Entry<T, Base>> entrySet() {
		processQueue();
		Set<Map.Entry<T, Base>> entries = new LinkedHashSet<Map.Entry<T, Base>>();
		for (Map.Entry<T, DominoReference<T>> entry : delegate.entrySet()) {
			Base value = getReferenceObject(entry.getValue());
			if (value != null) { // take only referenced values
				entries.add(new AbstractMap.SimpleEntry<T, Base>(entry.getKey(), value));
			}
		}
		return entries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		processQueue();
		delegate.clear();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<T> keySet() {
		processQueue();
		return delegate.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(final Map<? extends T, ? extends Base> paramMap) {
		for (Map.Entry<? extends T, ? extends Base> entry : paramMap.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<Base> values() {
		processQueue();

		Collection<Base> values = new ArrayList<Base>();
		for (DominoReference<T> valueRef : delegate.values()) {
			Base value = getReferenceObject(valueRef);
			if (value != null) {
				values.add(value);
			}
		}
		return values;
	}

	/**
	 * Removes all garbage collected values with their keys from the map. Since we don't know how much the ReferenceQueue.poll() operation
	 * costs, we should not call it every map operation.
	 */
	@SuppressWarnings("unchecked")
	public void processQueue() {
		// System.gc(); // TODO TODO
		DominoReference<T> ref = null;
		ReferenceQueue<Base> queue = getQueue();
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
