/*
 * Copyright OpenNTF 2013
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import lotus.domino.Base;

import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoReferenceSet.
 * 
 * @author nfreeman
 */
public class DominoReferenceSet extends ThreadLocal<Collection<Base>> implements Collection<Base> {
	
	/** The object set. */
	private final Collection<Base> objectSet = new ArrayList<Base>();
	
	/** The lock collection. */
	private final Collection<Base> lockCollection = new ArrayList<Base>();

	/**
	 * Instantiates a new domino reference set.
	 */
	public DominoReferenceSet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Lock.
	 * 
	 * @param base
	 *            the base
	 */
	public void lock(Base base) {
		lockCollection.add(base);
	}

	/**
	 * Lock.
	 * 
	 * @param bases
	 *            the bases
	 */
	public void lock(Base... bases) {
		for (Base base : bases) {
			lockCollection.add(base);
		}
	}

	/**
	 * Checks if is locked.
	 * 
	 * @param base
	 *            the base
	 * @return true, if is locked
	 */
	public boolean isLocked(Base base) {
		return lockCollection.contains(base);
	}

	/* (non-Javadoc)
	 * @see java.lang.ThreadLocal#initialValue()
	 */
	@Override
	protected Collection<Base> initialValue() {
		return objectSet;
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(Base paramE) {
		return objectSet.add(paramE);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends Base> paramCollection) {
		return objectSet.addAll(paramCollection);
	}

	/**
	 * Vapourise.
	 * 
	 * @param b
	 *            the b
	 */
	private void vapourise(Base b) {
		if (!lockCollection.contains(b)) {
			try {
				b.recycle();
			} catch (Throwable t) {
				DominoUtils.handleException(t);
			}
		}
	}

	/**
	 * Vapourise.
	 */
	private void vapourise() {
		for (Base b : objectSet) {
			vapourise(b);
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		vapourise();
		objectSet.clear();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	public boolean contains(Object paramObject) {
		return objectSet.contains(paramObject);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> paramCollection) {
		return objectSet.containsAll(paramCollection);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object paramObject) {
		return objectSet.equals(paramObject);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return objectSet.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty() {
		return objectSet.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#iterator()
	 */
	public Iterator<Base> iterator() {
		return objectSet.iterator();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(Object paramObject) {
		if (paramObject instanceof Base) {
			vapourise((Base) paramObject);
		}
		return objectSet.remove(paramObject);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> paramCollection) {
		for (Object o : paramCollection) {
			if (o instanceof Base) {
				vapourise((Base) o);
			}
		}
		return objectSet.removeAll(paramCollection);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> paramCollection) {
		return objectSet.retainAll(paramCollection);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#size()
	 */
	public int size() {
		return objectSet.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray()
	 */
	public Object[] toArray() {
		return objectSet.toArray();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray(T[])
	 */
	public <T> T[] toArray(T[] paramArrayOfT) {
		return objectSet.toArray(paramArrayOfT);
	}
}
