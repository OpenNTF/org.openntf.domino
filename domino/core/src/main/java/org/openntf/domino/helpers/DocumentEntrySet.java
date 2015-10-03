package org.openntf.domino.helpers;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.openntf.domino.Document;

public class DocumentEntrySet extends AbstractSet<Entry<String, Object>> {
	private Document doc;

	class DocumentEntrySetIterator implements Iterator<Entry<String, Object>> {
		private Iterator<String> keys;

		public DocumentEntrySetIterator(final Set<String> keySet) {
			keys = keySet.iterator();
		}

		@Override
		public boolean hasNext() {
			return keys.hasNext();
		}

		@Override
		public Entry<String, Object> next() {
			final String key = keys.next();

			return new Entry<String, Object>() {
				@Override
				public String getKey() {
					return key;
				}

				@Override
				public Object getValue() {
					return doc.get(key);
				}

				@Override
				public Object setValue(final Object value) {
					return doc.put(key, value);
				}

				@Override
				public String toString() {
					return key + "=" + getValue();
				}
			};
		}

		@Override
		public void remove() {
			keys.remove();
		}

	}

	public DocumentEntrySet(final Document document) {
		doc = document;
	}

	@Override
	public boolean add(final Entry<String, Object> entry) {
		return doc.put(entry.getKey(), entry.getValue()) != null;
	}

	@Override
	public void clear() {
		doc.clear();
	}

	@Override
	public boolean isEmpty() {
		return doc.isEmpty();
	}

	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return new DocumentEntrySetIterator(doc.keySet());
	}

	@Override
	public boolean remove(final Object arg0) {
		if (arg0 instanceof Entry) {
			@SuppressWarnings("unchecked")
			final Entry<String, Object> entry = (Entry<String, Object>) arg0;
			return doc.remove(entry.getKey()) != null;
		}
		return false;
	}

	@Override
	public int size() {
		return doc.size();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((doc == null) ? 0 : doc.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DocumentEntrySet)) {
			return false;
		}
		DocumentEntrySet other = (DocumentEntrySet) obj;
		Set<String> myKeySet = doc.keySet();
		Object otherKeySet = other.doc.keySet();
		if (!myKeySet.equals(otherKeySet))
			return false;
		for (String key : myKeySet) {
			Vector myVal = doc.getItemValue(key);
			Vector otherVal = other.doc.getItemValue(key);
			if (myVal == null && otherVal == null) {
				// nop
			} else if (myVal == null || otherVal == null) {
				return false;
			} else if (!myVal.equals(otherVal)) {
				return false;
			}
		}
		return true;

	}

}
