/**
 * 
 */
package org.openntf.domino.thread.deprecated;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * @author nfreeman
 * 
 *         Experimental class to test whether a primitive-based reference counter would improve performance of iteration and auto-recycling.
 *         No apparent difference occured.
 * 
 */
public class DominoReferenceMap {
	private static final Logger log_ = Logger.getLogger(DominoReferenceMap.class.getName());
	private static final long serialVersionUID = 1L;

	public static final int NO_VALUE = Integer.MIN_VALUE;
	private int[] keys = new int[1024];
	{
		Arrays.fill(keys, NO_VALUE);
	}

	private int[] values = new int[1024];
	private int lastKey = keys.length / 2 - 1;

	public int get(final int key) {
		int hash = key & lastKey;
		if (keys[hash] == key)
			return values[hash];
		return NO_VALUE;
	}

	public int incrementAndGet(final int key) {
		int cur = get(key);
		put(key, ++cur);
		return cur;
	}

	public int decrementAndGet(final int key) {
		int cur = get(key);
		if (cur == NO_VALUE) {
			put(key, 0);
			return 0;
		} else {
			put(key, --cur);
			return cur;
		}
	}

	public void put(final int key, final int value) {
		int hash = key & lastKey;
		if (keys[hash] != NO_VALUE && keys[hash] != key) {
			resize();
			hash = key & lastKey;
			if (keys[hash] != NO_VALUE && keys[hash] != key)
				throw new UnsupportedOperationException("Unable to handle collision.");
		}
		keys[hash] = key;
		values[hash] = value;
	}

	private void resize() {
		int len2 = keys.length * 2;
		int[] keys2 = new int[len2];
		Arrays.fill(keys2, NO_VALUE);
		int[] values2 = new int[len2];
		lastKey = len2 - 1;
		for (int i = 0; i < keys.length; i++) {
			int key = keys[i];
			int value = values[i];
			if (key == NO_VALUE)
				continue;
			int hash = key & lastKey;
			if (keys2[hash] != NO_VALUE)
				throw new UnsupportedOperationException("Unable to handle collision.");
			keys2[hash] = key;
			values2[hash] = value;
		}
		keys = keys2;
		values = values2;
	}

	public void clear() {
		Arrays.fill(keys, NO_VALUE);
	}
}
