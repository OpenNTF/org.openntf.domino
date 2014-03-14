package org.openntf.domino.helpers;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.openntf.domino.Document;

public class DocumentEntrySet extends AbstractSet<Entry<String, Object>> {
	private Document doc;

	class DocumentEntrySetIterator implements Iterator<Entry<String, Object>> {
		private Iterator<String> keys;

		public DocumentEntrySetIterator(final Set<String> keySet) {
			keys = keySet.iterator();
		}

		public boolean hasNext() {
			// TODO Auto-generated method stub
			return keys.hasNext();
		}

		public Entry<String, Object> next() {
			// TODO Auto-generated method stub
			final String key = keys.next();

			return new Entry<String, Object>() {
				public String getKey() {
					return key;
				}

				public Object getValue() {
					return doc.get(key);
				}

				public Object setValue(final Object value) {
					return doc.put(key, value);
				}

				@Override
				public String toString() {
					return key + "=" + getValue();
				}
			};
		}

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
			final Entry<String, Object> entry = (Entry<String, Object>) arg0;
			return doc.remove(entry.getKey()) != null;
		}
		return false;
	}

	@Override
	public int size() {
		return doc.size();
	}

}
