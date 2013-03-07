/**
 * 
 */
package org.openntf.domino.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lotus.domino.Base;

/**
 * @author nfreeman
 * 
 */
public class DominoReferenceSet extends ThreadLocal<Set<Base>> implements Set<Base> {
	private final Set<Base> objectSet = new HashSet<Base>();
	private final Collection<Base> lockCollection = new ArrayList<Base>();

	public DominoReferenceSet() {
		// TODO Auto-generated constructor stub
	}

	public void lock(Base base) {
		lockCollection.add(base);
	}

	public boolean isLocked(Base base) {
		return lockCollection.contains(base);
	}

	@Override
	protected Set<Base> initialValue() {
		return objectSet;
	}

	public boolean add(Base paramE) {
		return objectSet.add(paramE);
	}

	public boolean addAll(Collection<? extends Base> paramCollection) {
		return objectSet.addAll(paramCollection);
	}

	private void vapourise(Base b) {
		if (!lockCollection.contains(b)) {
			try {
				b.recycle();
			} catch (Throwable t) {
				// IGNORE
			}
		}
	}

	private void vapourise() {
		for (Base b : objectSet) {
			vapourise(b);
		}
	}

	public void clear() {
		vapourise();
		objectSet.clear();
	}

	public boolean contains(Object paramObject) {
		return objectSet.contains(paramObject);
	}

	public boolean containsAll(Collection<?> paramCollection) {
		return objectSet.containsAll(paramCollection);
	}

	@Override
	public boolean equals(Object paramObject) {
		return objectSet.equals(paramObject);
	}

	@Override
	public int hashCode() {
		return objectSet.hashCode();
	}

	public boolean isEmpty() {
		return objectSet.isEmpty();
	}

	public Iterator<Base> iterator() {
		return objectSet.iterator();
	}

	public boolean remove(Object paramObject) {
		if (paramObject instanceof Base) {
			vapourise((Base) paramObject);
		}
		return objectSet.remove(paramObject);
	}

	public boolean removeAll(Collection<?> paramCollection) {
		for (Object o : paramCollection) {
			if (o instanceof Base) {
				vapourise((Base) o);
			}
		}
		return objectSet.removeAll(paramCollection);
	}

	public boolean retainAll(Collection<?> paramCollection) {
		return objectSet.retainAll(paramCollection);
	}

	public int size() {
		return objectSet.size();
	}

	public Object[] toArray() {
		return objectSet.toArray();
	}

	public <T> T[] toArray(T[] paramArrayOfT) {
		return objectSet.toArray(paramArrayOfT);
	}
}
