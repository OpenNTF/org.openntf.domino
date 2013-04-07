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
package org.openntf.domino.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.openntf.domino.iterators.VectorIterator;

// TODO: Auto-generated Javadoc
/**
 * The Class Vector.
 * 
 * @param <E>
 *            the element type
 */
public class VectorSingle<E> extends Vector<E> implements Collection<E> {

	private E value_;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new vector.
	 */
	public VectorSingle() {

	}

	public VectorSingle(E value) {
		value_ = value;
	}

	/**
	 * Instantiates a new vector.
	 * 
	 * @param source
	 *            the source
	 */
	public VectorSingle(Collection<E> source) {
		for (E e : source) {
			value_ = e;
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#add(java.lang.Object)
	 */
	@Override
	public boolean add(E arg0) {
		if (value_ == null) {
			value_ = arg0;
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#add(int, java.lang.Object)
	 */
	@Override
	public void add(int arg0, E arg1) {
		if (value_ == null) {
			value_ = arg1;

		} else {
			// TODO NTF - Exception?
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		boolean result = false;
		if (value_ == null) {
			for (E e : arg0) {
				value_ = e;
				result = true;
				break;
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int arg0, Collection<? extends E> arg1) {
		boolean result = false;
		if (value_ == null) {
			for (E e : arg1) {
				value_ = e;
				result = true;
				break;
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#addElement(java.lang.Object)
	 */
	@Override
	@Deprecated
	public void addElement(E arg0) {
		if (value_ == null)
			value_ = arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#capacity()
	 */
	@Override
	public int capacity() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#clear()
	 */
	@Override
	public void clear() {
		value_ = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#clone()
	 */
	@Override
	public Object clone() {
		return new VectorSingle<E>(value_);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object arg0) {
		if (arg0 == null)
			return false;
		return arg0.equals(value_);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> arg0) {
		if (arg0.size() > 1)
			return false;
		for (Object o : arg0) {
			return o.equals(value_); // yes, it's a return because we know there's only one item
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#copyInto(java.lang.Object[])
	 */
	@Override
	@Deprecated
	public void copyInto(Object[] arg0) {
		System.arraycopy(this.toArray(), 0, arg0, 0, this.size());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#elementAt(int)
	 */
	@Override
	@Deprecated
	public E elementAt(int arg0) {
		if (arg0 == 0)
			return value_;
		return null;
		// TODO NTF - Exception?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#elements()
	 */
	@Override
	@Deprecated
	public Enumeration<E> elements() {
		return Collections.enumeration(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#ensureCapacity(int)
	 */
	@Override
	public void ensureCapacity(int arg0) {
		// what would this even mean?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null)
			return false;
		return arg0.equals(value_);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#firstElement()
	 */
	@Override
	public E firstElement() {
		return value_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#get(int)
	 */
	@Override
	public E get(int arg0) {
		if (arg0 == 0)
			return value_;
		return null;
		// TODO Exception?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#hashCode()
	 */
	@Override
	public int hashCode() {
		return value_.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#indexOf(java.lang.Object, int)
	 */
	@Override
	@Deprecated
	public int indexOf(Object arg0, int arg1) {
		// TODO NTF - come up with an elegant way to express this rather brutal set of conditions...
		if (arg1 > 0)
			return -1;
		if (arg0 == null && value_ == null)
			return 0;
		if (arg0 != null && arg0.equals(value_))
			return 0;
		return -1;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(Object arg0) {
		if (arg0 == null && value_ == null)
			return 0;
		if (arg0 != null && arg0.equals(value_))
			return 0;
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#insertElementAt(java.lang.Object, int)
	 */
	@Override
	@Deprecated
	public void insertElementAt(E arg0, int arg1) {
		if (arg1 > 0)
			return;
		value_ = arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return value_ == null;
	}

	/**
	 * Gets the mod count.
	 * 
	 * @return the mod count
	 */
	@Override
	public int getModCount() {
		return modCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractList#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		return new VectorIterator(this, modCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#lastElement()
	 */
	@Override
	@Deprecated
	public E lastElement() {
		return value_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#lastIndexOf(java.lang.Object, int)
	 */
	@Override
	@Deprecated
	public int lastIndexOf(Object arg0, int arg1) {
		// TODO NTF - come up with an elegant way to express this rather brutal set of conditions...
		if (arg1 > 0)
			return -1;
		if (arg0 == null && value_ == null)
			return 0;
		if (arg0 != null && arg0.equals(value_))
			return 0;
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#lastIndexOf(java.lang.Object)
	 */
	@Override
	public int lastIndexOf(Object arg0) {
		if (arg0 == null && value_ == null)
			return 0;
		if (arg0 != null && arg0.equals(value_))
			return 0;
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#remove(int)
	 */
	@Override
	public E remove(int arg0) {
		if (arg0 == 0) {
			E result = value_;
			value_ = null;
			return result;
		}
		return null; // TODO Exception?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object arg0) {
		if (arg0 != null && arg0.equals(value_)) {
			value_ = null;
			return true;
		}
		return false; // TODO Exception?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> arg0) {
		if (arg0.contains(value_)) {
			value_ = null;
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#removeAllElements()
	 */
	@Override
	public void removeAllElements() {
		value_ = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#removeElement(java.lang.Object)
	 */
	@Override
	public boolean removeElement(Object arg0) {
		return remove(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#removeElementAt(int)
	 */
	@Override
	public void removeElementAt(int arg0) {
		remove(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> arg0) {
		if (!arg0.contains(value_)) {
			value_ = null;
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#set(int, java.lang.Object)
	 */
	@Override
	public E set(int arg0, E arg1) {
		if (arg0 == 0) {
			E result = value_;
			value_ = arg1;
			return result;
		}
		return null; // TODO Exception?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#setElementAt(java.lang.Object, int)
	 */
	@Override
	@Deprecated
	public void setElementAt(E arg0, int arg1) {
		if (arg1 == 0) {
			E result = value_;
			value_ = arg0;
		}
		// TODO Exception?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#setSize(int)
	 */
	@Override
	@Deprecated
	public void setSize(int arg0) {
		if (arg0 == 1) {
			// so what?
		} else {
			// TODO Exception?
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#size()
	 */
	@Override
	public int size() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#subList(int, int)
	 */
	@Override
	public List<E> subList(int arg0, int arg1) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#toArray()
	 */
	@Override
	public Object[] toArray() {
		Object[] result = new Object[1];
		result[0] = value_;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#toArray(T[])
	 */
	@Override
	public <T> T[] toArray(T[] arg0) {
		T[] result = null;
		if (arg0.length == 1) {
			result = arg0;
		} else {
			result = Arrays.copyOf(arg0, 1);
		}
		result[0] = (T) value_;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#toString()
	 */
	@Override
	public String toString() {
		return String.valueOf(value_);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Vector#trimToSize()
	 */
	@Override
	public void trimToSize() {
		// can't get any smaller, dude
	}

}
