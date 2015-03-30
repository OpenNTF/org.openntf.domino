package org.openntf.domino.types;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;

import javolution.util.FastMap;

public class CaseInsensitiveHashSet extends AbstractSet<String> {
	private Map<String, String> delegate = new FastMap<String, String>();

	@Override
	public boolean add(final String e) {
		if (delegate.containsKey(e.toLowerCase()))
			return false;
		delegate.put(e.toLowerCase(), e);
		return true;
	}

	@Override
	public void clear() {
		delegate.clear();

	}

	@Override
	public boolean contains(final Object arg0) {
		if (arg0 instanceof String) {
			return delegate.containsKey(((String) arg0).toLowerCase());
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public boolean remove(final Object arg0) {
		if (arg0 instanceof String) {
			return delegate.remove(((String) arg0).toLowerCase()) != null;
		}
		return false;
	}

	@Override
	public Iterator<String> iterator() {
		return delegate.values().iterator();
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public Object[] toArray() {
		return delegate.values().toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return delegate.values().toArray(a);
	}

	@Override
	public String toString() {
		return delegate.values().toString();
	}

}
