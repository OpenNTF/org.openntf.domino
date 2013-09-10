/*
 * © Copyright IBM Corp. 2010
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

package com.ibm.commons.util;


/**
 * Map that can efficiently be accessed by either the key or the value.
 * <p>
 * </p>
 */
public class DoubleMap<K,V> {

	private int slotCount;
	private int size;

	// Hash map entries
	private MapEntry[] keyEntries;
	private MapEntry[] valueEntries;

	private static final class MapEntry {
		// List in the Hash table
		MapEntry keyPrevHash;
		MapEntry keyNextHash;
		MapEntry valuePrevHash;
		MapEntry valueNextHash;

		int keyHashCode;
		int valueHashCode;

		Object key;
		Object value;

		MapEntry(Object key, Object value) {
			this.key = key;
			this.value = value;
			this.keyHashCode = key.hashCode();
			this.valueHashCode = value.hashCode();
		}
	}

	public DoubleMap() {
		this(13); // See Aho for good values here...
	}

	public DoubleMap(int slotCount) {
		this.slotCount = slotCount;
		this.keyEntries = new MapEntry[slotCount];
		this.valueEntries = new MapEntry[slotCount];
	}

	public int size() {
		return size;
	}

	public void clear() {
		this.size = 0;
		this.keyEntries = new MapEntry[slotCount];
		this.valueEntries = new MapEntry[slotCount];
	}
	
	public void put(K key, V value) {
		MapEntry e = new MapEntry(key, value);
		put(e);
	}

	public void removeEntryByKey(K key) {
		MapEntry e = getEntryByKey(key);
		if (e != null) {
			removeEntry(e);
		}
	}

	public void removeEntryByValue(V value) {
		MapEntry e = getEntryByValue(value);
		if (e != null) {
			removeEntry(e);
		}
	}


	public V getNameByKey(K key) {
		MapEntry e = getEntryByKey(key);
		if (e != null) {
			return (V)e.value;
		}
		return null;
	}

	public K getIdByValue(V value) {
		MapEntry e = getEntryByValue(value);
		if (e != null) {
			return (K)e.key;
		}
		return null;
	}

	private final int getSlot(int hashCode) {
		return (hashCode & 0x7FFFFFFF) % slotCount;
	}

	private final MapEntry getEntryByKey(K key) {
		int hashCode = key.hashCode();
		for (MapEntry e = keyEntries[getSlot(hashCode)]; e != null; e = e.keyNextHash) {
			if (e.keyHashCode == hashCode && e.key.equals(key)) {
				return e;
			}
		}
		return null;
	}

	private final MapEntry getEntryByValue(V value) {
		int hashCode = value.hashCode();
		for (MapEntry e = valueEntries[getSlot(hashCode)]; e != null; e = e.valueNextHash) {
			if (e.valueHashCode == hashCode && e.value.equals(value)) {
				return e;
			}
		}
		return null;
	}


	private void put(MapEntry e) {
		// Insert the new entry in the ID HashTable
		int keySlot = getSlot(e.keyHashCode);
		e.keyNextHash = keyEntries[keySlot];
		if (e.keyNextHash != null) {
			e.keyNextHash.keyPrevHash = e;
		}
		keyEntries[keySlot] = e;

		// Insert the new entry in the Name HashTable
		int valueSlot = getSlot(e.valueHashCode);
		e.valueNextHash = valueEntries[valueSlot];
		if (e.valueNextHash != null) {
			e.valueNextHash.valuePrevHash = e;
		}
		valueEntries[valueSlot] = e;

		size++;
	}

	private final void removeEntry(MapEntry e) {
		// Remove the entry from the ID hashtable
		if (e.keyPrevHash != null) {
			e.keyPrevHash.keyNextHash = e.keyNextHash;
		} else {
			keyEntries[getSlot(e.keyHashCode)] = e.keyNextHash;
		}
		if (e.keyNextHash != null) {
			e.keyNextHash.keyPrevHash = e.keyPrevHash;
		}
		// Remove the entry from the Name hashtable
		if (e.valuePrevHash != null) {
			e.valuePrevHash.valueNextHash = e.valueNextHash;
		} else {
			valueEntries[getSlot(e.valueHashCode)] = e.valueNextHash;
		}
		if (e.valueNextHash != null) {
			e.valueNextHash.valuePrevHash = e.valuePrevHash;
		}
		// Decrease the map count
		size--;
	}
}
