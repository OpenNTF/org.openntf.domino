/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
 */
package org.openntf.domino.graph2.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.openntf.domino.big.NoteCoordinate;

import com.tinkerpop.blueprints.Element;

@SuppressWarnings("nls")
public class DElementIterable implements org.openntf.domino.graph2.DElementIterable, List<Element> {

	public static class DElementIterator implements org.openntf.domino.graph2.DElementIterable.DElementIterator {
		protected final DElementStore elementStore_;
		protected final List<NoteCoordinate> index_;
		protected ListIterator<NoteCoordinate> iterator_;

		public DElementIterator(final DElementStore store, final List<NoteCoordinate> index) {
			elementStore_ = store;
			index_ = index;
		}

		private ListIterator<NoteCoordinate> getIterator() {
			if (iterator_ == null) {
				iterator_ = index_.listIterator();
			}
			return iterator_;
		}

		@Override
		public boolean hasNext() {
			return getIterator().hasNext();
		}

		@Override
		public Element next() {
			Element result = null;
			NoteCoordinate nc = getIterator().next();
			if (nc != null) {
				result = elementStore_.getElement(nc);
			}
			return result;
		}

		@Override
		public void remove() {
			getIterator().remove();
		}

		@Override
		public void add(final Element e) {
			Object rawid = e.getId();
			if (rawid instanceof String) {
				getIterator().add(NoteCoordinate.Utils.getNoteCoordinate((String) rawid));
			} else if (rawid instanceof NoteCoordinate) {
				getIterator().add((NoteCoordinate) rawid);
			} else {
				throw new IllegalStateException("Cannot process an element with an id of type " + rawid.getClass().getName());
			}
		}

		@Override
		public boolean hasPrevious() {
			return getIterator().hasPrevious();
		}

		@Override
		public int nextIndex() {
			return getIterator().nextIndex();
		}

		@Override
		public Element previous() {
			Element result = null;
			NoteCoordinate nc = getIterator().previous();
			if (nc != null) {
				result = elementStore_.getElement(nc);
			}
			return result;
		}

		@Override
		public int previousIndex() {
			return getIterator().previousIndex();
		}

		@Override
		public void set(final Element e) {
			Object rawid = e.getId();
			if (rawid instanceof String) {
				getIterator().set(NoteCoordinate.Utils.getNoteCoordinate((String) rawid));
			} else if (rawid instanceof NoteCoordinate) {
				getIterator().set((NoteCoordinate) rawid);
			} else {
				throw new IllegalStateException("Cannot process an element with an id of type " + rawid.getClass().getName());
			}

		}
	}

	protected final List<NoteCoordinate> index_;
	protected final DElementStore store_;

	public DElementIterable(final DElementStore store, final List<NoteCoordinate> index) {
		store_ = store;
		index_ = index;
	}

	@Override
	public Iterator<Element> iterator() {
		return new DElementIterator(store_, index_);
	}

	@Override
	public boolean add(final Element e) {
		Object rawid = e.getId();
		if (rawid instanceof NoteCoordinate) {
			return index_.add((NoteCoordinate) rawid);
		}
		return false;
	}

	@Override
	public void add(final int index, final Element element) {
		Object rawid = element.getId();
		if (rawid instanceof NoteCoordinate) {
			index_.add(index, (NoteCoordinate) rawid);
		}
	}

	@Override
	public boolean addAll(final Collection<? extends Element> c) {
		List<NoteCoordinate> nclist = new ArrayList<NoteCoordinate>();
		for (Element e : c) {
			Object rawid = e.getId();
			if (rawid instanceof NoteCoordinate) {
				nclist.add((NoteCoordinate) rawid);
			}
		}
		return index_.addAll(nclist);
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends Element> c) {
		List<NoteCoordinate> nclist = new ArrayList<NoteCoordinate>();
		for (Element e : c) {
			Object rawid = e.getId();
			if (rawid instanceof NoteCoordinate) {
				nclist.add((NoteCoordinate) rawid);
			}
		}
		return index_.addAll(index, nclist);
	}

	@Override
	public void clear() {
		index_.clear();
	}

	@Override
	public boolean contains(final Object o) {
		if (o instanceof Element) {
			Object rawid = ((Element) o).getId();
			if (rawid instanceof NoteCoordinate) {
				return index_.contains(rawid);
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		List<NoteCoordinate> nclist = new ArrayList<NoteCoordinate>();
		for (Object raw : c) {
			if (raw instanceof Element) {
				Element e = (Element) raw;
				Object rawid = e.getId();
				if (rawid instanceof NoteCoordinate) {
					nclist.add((NoteCoordinate) rawid);
				}
			}
		}
		return index_.containsAll(nclist);
	}

	@Override
	public Element get(final int index) {
		return store_.getElement(index_.get(index));
	}

	@Override
	public int indexOf(final Object o) {
		if (o instanceof Element) {
			Object rawid = ((Element) o).getId();
			if (rawid instanceof NoteCoordinate) {
				return index_.indexOf(rawid);
			}
		}
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return index_.isEmpty();
	}

	@Override
	public int lastIndexOf(final Object o) {
		if (o instanceof Element) {
			Object rawid = ((Element) o).getId();
			if (rawid instanceof NoteCoordinate) {
				return index_.lastIndexOf(rawid);
			}
		}
		return 0;
	}

	@Override
	public ListIterator<Element> listIterator() {
		return new DElementIterator(store_, index_);
	}

	@Override
	public ListIterator<Element> listIterator(final int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element remove(final int index) {
		Element result = store_.getElement(index_.remove(index));
		return result;
	}

	@Override
	public boolean remove(final Object o) {
		if (o instanceof Element) {
			Object rawid = ((Element) o).getId();
			if (rawid instanceof NoteCoordinate) {
				return index_.remove(rawid);
			}
		}
		return false;
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		List<NoteCoordinate> nclist = new ArrayList<NoteCoordinate>();
		for (Object raw : c) {
			if (raw instanceof Element) {
				Element e = (Element) raw;
				Object rawid = e.getId();
				if (rawid instanceof NoteCoordinate) {
					nclist.add((NoteCoordinate) rawid);
				}
			}
		}
		return index_.removeAll(nclist);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		List<NoteCoordinate> nclist = new ArrayList<NoteCoordinate>();
		for (Object raw : c) {
			if (raw instanceof Element) {
				Element e = (Element) raw;
				Object rawid = e.getId();
				if (rawid instanceof NoteCoordinate) {
					nclist.add((NoteCoordinate) rawid);
				}
			}
		}
		return index_.retainAll(nclist);
	}

	@Override
	public Element set(final int index, final Element element) {
		Object rawid = element.getId();
		if (rawid instanceof NoteCoordinate) {
			index_.set(index, (NoteCoordinate) rawid);
		}
		return element;
	}

	@Override
	public int size() {
		return index_.size();
	}

	@Override
	public List<Element> subList(final int fromIndex, final int toIndex) {
		List<NoteCoordinate> subindex = index_.subList(fromIndex, toIndex);
		return new DElementIterable(store_, subindex);
	}

	@Override
	public Object[] toArray() {
		int size = index_.size();
		Object[] result = new Object[size];
		for (int i = 0; i < size; i++) {
			NoteCoordinate e = index_.get(i);
			result[i] = store_.getElement(e);
		}
		return result;
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		System.err.println("ALERT: toArray is called on a DElementIterable with an argument of " + a.getClass().getName());
		return null;
	}

}
