package org.openntf.domino.utils;

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
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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

public class WeakValueHashMap<K, V> implements Map<K, V> {
	private static final long serialVersionUID = 1L;

	/* Reference queue for cleared WeakValues */
	private ReferenceQueue queue = new ReferenceQueue<V>();
	private Map<K, WeakValue<K, V>> delegate = new HashMap<K, WeakValue<K, V>>();

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
		return delegate.containsValue(WeakValue.create(value));
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
	public V get(final Object key) {
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
	public V put(final K key, final V value) {
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
		WeakValue<K, V> oldValue = delegate.put(key, WeakValue.create(key, value, queue));
		return getReferenceObject(oldValue);
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
	public V remove(final Object key) {
		return getReferenceObject(delegate.remove(key));
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#entrySet()
	 */

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		processQueue();
		Set<Map.Entry<K, V>> entries = new LinkedHashSet<Map.Entry<K, V>>();
		for (Map.Entry<K, WeakValue<K, V>> entry : delegate.entrySet()) {
			V value = getReferenceObject(entry.getValue());
			if (value != null) { // take only referenced values
				entries.add(new AbstractMap.SimpleEntry<K, V>(entry.getKey(), value));
			}
		}
		return entries;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		processQueue();
		delegate.clear();

	}

	/* (non-Javadoc)
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<K> keySet() {
		processQueue();
		return delegate.keySet();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(final Map<? extends K, ? extends V> paramMap) {
		for (Map.Entry<? extends K, ? extends V> entry : paramMap.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<V> values() {
		processQueue();
		AbstractMap A;
		Collection<V> values = new ArrayList<V>();
		for (WeakValue<K, V> valueRef : delegate.values()) {
			V value = getReferenceObject(valueRef);
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
	private void processQueue() {
		WeakValue<K, V> wv = null;

		while ((wv = (WeakValue<K, V>) this.queue.poll()) != null) {
			// "super" is not really necessary but use it
			// to be on the safe side
			delegate.remove(wv.key);
		}
	}

	/**
	 * A convenience method to return the object held by the weak reference or <code>null</code> if it does not exist.
	 */
	private final V getReferenceObject(final WeakValue<K, V> ref) {
		return (ref == null) ? null : ref.get();
	}

	/**
	 * We need this special class to keep the backward reference from the value to the key, so that we are able to remove the key if the
	 * value is garbage collected.
	 */
	private static class WeakValue<K, V> extends WeakReference<V> {
		/**
		 * It's the same as the key in the map. We need the key to remove the value if it is garbage collected.
		 */
		private K key;

		private WeakValue(final V value) {
			super(value);
		}

		/**
		 * Creates a new weak reference without adding it to a ReferenceQueue.
		 */
		static WeakValue create(final Object value) {
			if (value == null)
				return null;
			else
				return new WeakValue(value);
		}

		private WeakValue(final K key, final V value, final ReferenceQueue queue) {
			super(value, queue);
			this.key = key;
		}

		/**
		 * Creates a new weak reference and adds it to the given queue.
		 */
		static WeakValue create(final Object key, final Object value, final ReferenceQueue queue) {
			if (value == null)
				return null;
			else
				return new WeakValue(key, value, queue);
		}

		/**
		 * A WeakValue is equal to another WeakValue iff they both refer to objects that are, in turn, equal according to their own equals
		 * methods.
		 */
		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;

			if (!(obj instanceof WeakValue))
				return false;

			Object ref1 = this.get();
			Object ref2 = ((WeakValue) obj).get();

			if (ref1 == ref2)
				return true;

			if ((ref1 == null) || (ref2 == null))
				return false;

			return ref1.equals(ref2);
		}

		/**
	     *
	     */
		@Override
		public int hashCode() {
			Object ref = this.get();

			return (ref == null) ? 0 : ref.hashCode();
		}
	}

}
