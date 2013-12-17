/*
 * © Copyright IBM Corp. 2012-2013
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

//import com.ibm.commons.Platform;

/**
 * System cache using a LRU mechanism.
 * <p>
 * This cache is used to hold a certain number of entries that can be reused. For example, it can be used to cache compiled version of
 * JavaScript snippets of code, or XPath definition.
 * </p>
 * 
 * @ibm-api
 */
public final class SystemCache {

	private String name;
	private int maxSize;
	private int size;
	private MapEntry listStart;
	private MapEntry listEnd;
	private MapEntry[] entries;

	// Some performance tuning information
	private long accessed;	// Number of times it had been accessed
	private long incache;	// Number of times 
	private long added;		// Number of times an entry had been added to the cache 
	private long discarded;	// Number of items discarded 

	private static class MapEntry {
		boolean inCache;
		// List in the Hash table
		MapEntry prevHash;
		MapEntry nextHash;
		// List in the Linked list
		MapEntry prevList;
		MapEntry nextList;
		int hashCode;
		String key;
		Object value;

		MapEntry(final String key, final Object value) {
			this.key = key;
			this.value = value;
			this.hashCode = key.hashCode();
		}
	}

	/**
	 * @ibm-api
	 */
	public SystemCache(final String name, final int maxSize) {
		this.name = name;
		this.maxSize = maxSize;
		this.entries = new MapEntry[211];
	}

	/**
	 * @ibm-api
	 */
	public SystemCache(final String name, int maxSize, final String propertyName) {
		this.name = name;
		if (StringUtil.isNotEmpty(propertyName)) {
			try {
				String value = System.getProperty(propertyName);
				//        		String value = Platform.getInstance().getProperty(propertyName);
				if (StringUtil.isNotEmpty(value)) {
					int iv = Integer.parseInt(value);
					if (iv > 0) {
						maxSize = iv;
					}
				}
			} catch (Throwable ex) {
			}
		}
		this.maxSize = maxSize;
		this.entries = new MapEntry[211];
	}

	/**
	 * @ibm-api
	 */
	public String getName() {
		return name;
	}

	/**
	 * @ibm-api
	 */
	public long getAccessedTimes() {
		return accessed;
	}

	/**
	 * @ibm-api
	 */
	public long getInCacheTimes() {
		return incache;
	}

	/**
	 * @ibm-api
	 */
	public long getAddedTimes() {
		return added;
	}

	/**
	 * @ibm-api
	 */
	public long getDiscardedTimes() {
		return discarded;
	}

	/**
	 * @ibm-api
	 */
	public int size() {
		return size;
	}

	/**
	 * @ibm-api
	 */
	public int getCapacity() {
		return maxSize;
	}

	/**
	 * @ibm-api
	 */
	public synchronized void clear() {
		this.entries = new MapEntry[211];
		this.listStart = null;
		this.listEnd = null;
		this.size = 0;
	}

	/**
	 * @ibm-api
	 */
	public void remove(final String key) {
		// Tuning info
		accessed++;

		//TDiag.trace( "Getting '{0}'", key );
		MapEntry e = getEntry(key);
		if (e != null) {
			incache++;
			synchronized (this) {
				// The entry might have been removed when it was not synchronized
				// In this case, we should just re-add it
				if (e.inCache) {
					removeEntry(e);
				}
			}
		}
	}

	/**
	 * @ibm-api
	 */
	public Object get(final String key) {
		// Tuning info
		accessed++;

		//TDiag.trace( "Getting '{0}'", key );
		MapEntry e = getEntry(key);
		if (e != null) {
			incache++;
			synchronized (this) {
				// The entry might have been removed when it was not synchronized
				// In this case, we should just re-add it
				if (e.inCache) {
					moveToStart(e);
				} else {
					put(e);
				}
			}
			return e.value;
		}
		return null;
	}

	/**
	 * @ibm-api
	 */
	public synchronized void put(final String key, final Object value) {
		if (maxSize > 0) {
			MapEntry e = getEntry(key);
			if (e != null) {
				e.value = value;
				moveToStart(e);
			} else {
				e = new MapEntry(key, value);
				put(e);
			}
		}
	}

	private void put(final MapEntry e) {
		e.inCache = true;

		// Remove the oldest one?
		if (size == maxSize) {
			removeEntry(listEnd);
		}

		// Insert the new entry in the HashTable
		int slot = getSlot(e.hashCode);
		e.nextHash = entries[slot];
		if (e.nextHash != null) {
			e.nextHash.prevHash = e;
		}
		entries[slot] = e;

		// And in the list
		if (listStart != null) {
			listStart.prevList = e;
		}
		e.nextList = listStart;
		listStart = e;
		if (listEnd == null) {
			listEnd = e;
		}
		size++;

		// Tuning info
		added++;
	}

	private final int getSlot(final int hashCode) {
		return (hashCode & 0x7FFFFFFF) % entries.length;
	}

	private final MapEntry getEntry(final String key) {
		int hashCode = key.hashCode();
		for (MapEntry e = entries[getSlot(hashCode)]; e != null; e = e.nextHash) {
			if (e.hashCode == hashCode && e.key.equals(key)) {
				return e;
			}
		}
		return null;
	}

	private final void moveToStart(final MapEntry e) {
		if (e != listStart) {
			// Remove it from the list
			e.prevList.nextList = e.nextList;
			if (e.nextList != null) {
				e.nextList.prevList = e.prevList;
			} else {
				listEnd = e.prevList;
			}
			// And add it a the top
			listStart.prevList = e;
			e.nextList = listStart;
			listStart = e;
		}
	}

	private final void removeEntry(final MapEntry e) {
		e.inCache = false;
		// Remove the entry from the hashtable
		if (e.prevHash != null) {
			e.prevHash.nextHash = e.nextHash;
		} else {
			entries[getSlot(e.hashCode)] = e.nextHash;
		}
		if (e.nextHash != null) {
			e.nextHash.prevHash = e.prevHash;
		}
		// Remove the entry from the linked list
		if (e.prevList != null) {
			e.prevList.nextList = e.nextList;
		} else {
			listStart = e.nextList;
		}
		if (e.nextList != null) {
			e.nextList.prevList = e.prevList;
		} else {
			listEnd = e.prevList;
		}
		// Decrease the map count
		size--;

		// Tuning information
		discarded++;
	}

	// FOR DEBUG ONLY
	//    private void dumpList() {
	//        TDiag.trace( "list({0}), start={1}, end={2}", TString.toString(size), listStart!=null ? listStart.key : "<null>", listEnd!=null ? listEnd.key : "<null>" );
	//        for( MapEntry e=listStart; e!=null; e=e.nextList ) {
	//            TDiag.trace( "{0}, prev={1}, next ={2}", e.key, e.prevList!=null ? e.prevList.key : "<null>", e.nextList!=null ? e.nextList.key : "<null>" );
	//        }
	//    }
}
