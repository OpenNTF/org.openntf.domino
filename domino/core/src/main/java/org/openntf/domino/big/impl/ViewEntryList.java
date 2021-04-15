/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.big.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.big.ViewEntryCoordinate;
import org.openntf.domino.exceptions.UnimplementedException;

@SuppressWarnings("nls")
public class ViewEntryList implements List<org.openntf.domino.big.ViewEntryCoordinate> {
	//	private View parentView_;
	private ViewNavigator navigator_;
	private ViewEntryCollection collection_;
	protected ViewEntry startEntry_;
	protected ViewEntry stopEntry_;
	protected Integer fromIndex_ = null;
	protected Integer toIndex_ = null;
	protected boolean emptyIterator_ = false;
	protected final boolean flatIterator_;

	public static void debug(final String... messages) {
		for (String message : messages) {
			System.out.println("TEMP DEBUG " + message);
		}
	}

	protected static interface Indexable {
		public void setFromIndex(final int fromIndex);

		public void setToIndex(final int toIndex);
	}

	public static class ViewEntryCollectionIterator implements ListIterator<ViewEntryCoordinate>, Indexable {
		private ViewEntryList parentList_;
		private ViewEntryCollection collection_;
		private ViewEntry cur_;
		private Boolean hasNextCache_;
		private ViewEntry next_;
		private Boolean hasPrevCache_;
		private ViewEntry prev_;
		private int index_ = 0;
		private int fromIndex_ = 0;
		private int toIndex_ = Integer.MAX_VALUE;

		ViewEntryCollectionIterator(final ViewEntryCollection collection, final ViewEntryList parent) {
			collection_ = collection;
			parentList_ = parent;

		}

		@Override
		public void setFromIndex(final int fromIndex) {
			fromIndex_ = fromIndex;
			next_ = collection_.getNthEntry(fromIndex);
			hasNextCache_ = true;
		}

		@Override
		public void setToIndex(final int toIndex) {
			toIndex_ = toIndex;
		}

		@Override
		public void add(final ViewEntryCoordinate arg0) {
			throw new UnsupportedOperationException("ViewEntryListIterator is not modifiable");
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean hasNext() {
			if (parentList_.emptyIterator_) {
				parentList_.emptyIterator_ = false;
				return false;
			}
			if (index_ >= (toIndex_ - fromIndex_)) {
				return false;
			}
			if (hasNextCache_ == null) {
				if (cur_ == null) {
					if (collection_.getCount() == 0) {
						hasNextCache_ = false;
					} else {
						hasNextCache_ = true;
					}
				} else {
					next_ = collection_.getNextEntry(cur_);
					hasNextCache_ = (next_ != null);
				}
			}
			return hasNextCache_;
		}

		@Override
		public boolean hasPrevious() {
			if (parentList_.emptyIterator_) {
				parentList_.emptyIterator_ = false;
				return false;
			}
			if (index_ == 0) {
				return false;
			}
			if (hasPrevCache_ == null) {
				if (cur_ == null) {
					if (collection_.getCount() == 0) {
						hasPrevCache_ = false;
					} else {
						hasPrevCache_ = true;
					}
				} else {
					prev_ = collection_.getPrevEntry(cur_);
					hasPrevCache_ = prev_ != null;
				}
			}
			return hasPrevCache_;
		}

		@SuppressWarnings("deprecation")
		@Override
		public ViewEntryCoordinate next() {
			hasPrevCache_ = null;
			hasNextCache_ = null;
			prev_ = cur_;
			if (cur_ == null && next_ != null) {
				cur_ = next_;
			}
			if (cur_ == null) {
				ViewEntry newEntry = collection_.getFirstEntry();
				cur_ = newEntry;
			} else if (next_ == null) {
				ViewEntry newEntry = collection_.getNextEntry(cur_);
				cur_ = newEntry;
			} else {
				cur_ = next_;
			}
			next_ = null;
			index_++;
			if (cur_ != null) {
				ViewEntryCoordinate vec = new org.openntf.domino.big.impl.ViewEntryCoordinate(cur_);
				vec.setSourceColl(collection_);
				return vec;
			} else {
				return null;
			}
		}

		@Override
		public int nextIndex() {
			if (hasNext()) {
				return index_ + 1;
			}
			return index_;
		}

		@Override
		public ViewEntryCoordinate previous() {
			hasPrevCache_ = null;
			hasNextCache_ = null;
			next_ = cur_;
			if (prev_ == null) {
				ViewEntry newEntry = collection_.getPrevEntry(cur_);
				cur_ = newEntry;
			} else {
				cur_ = prev_;
			}
			prev_ = null;
			index_--;
			ViewEntryCoordinate vec = new org.openntf.domino.big.impl.ViewEntryCoordinate(cur_);
			vec.setSourceColl(collection_);
			return vec;
		}

		@Override
		public int previousIndex() {
			if (hasPrevious()) {
				return index_ - 1;
			}
			return index_;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("ViewEntryListIterator is not modifiable");
		}

		@Override
		public void set(final ViewEntryCoordinate arg0) {
			throw new UnsupportedOperationException("ViewEntryListIterator is not modifiable");
		}
	}

	public static class ViewEntryFlatListIterator implements ListIterator<ViewEntryCoordinate>, Indexable {
		private ViewEntryList parentList_;
		private ViewNavigator navigator_;
		private ViewEntry cur_;
		private Boolean hasNextCache_;
		private ViewEntry next_;
		private Boolean hasPrevCache_;
		private ViewEntry prev_;
		private int index_ = 0;
		private int fromIndex_ = 0;
		private int toIndex_ = Integer.MAX_VALUE;

		ViewEntryFlatListIterator(final ViewNavigator navigator, final ViewEntryList parent) {
			navigator_ = navigator;
			parentList_ = parent;
			//			System.out.println("TEMP DEBUG Created a ViewEntryFlatListIterator for " + navigator.getParentView().getName());
		}

		@Override
		public void setFromIndex(final int fromIndex) {
			fromIndex_ = fromIndex + 1;
			next_ = navigator_.getNthDocument(fromIndex_);
		}

		@Override
		public void setToIndex(final int toIndex) {
			toIndex_ = toIndex + 1;
		}

		@Override
		public void add(final ViewEntryCoordinate arg0) {
			throw new UnsupportedOperationException("ViewEntryListIterator is not modifiable");
		}

		@Override
		public boolean hasNext() {
			if (parentList_.emptyIterator_) {
				parentList_.emptyIterator_ = false;
				hasNextCache_ = false;
			}
			if (index_ >= (toIndex_ - fromIndex_)) {
				hasNextCache_ = false;
			}

			if (hasNextCache_ == null) {
				if (cur_ == null) {
					if (navigator_.getCount() == 0) {
						hasNextCache_ = false;
					} else {
						hasNextCache_ = true;
					}
				} else {
					next_ = navigator_.getNextDocument();
					hasNextCache_ = (next_ != null);
				}
			}

			return hasNextCache_;
		}

		@Override
		public boolean hasPrevious() {
			if (parentList_.emptyIterator_) {
				parentList_.emptyIterator_ = false;
				return false;
			}
			if (index_ >= 0) {
				return false;
			}
			if (hasPrevCache_ == null) {
				if (cur_ == null) {
					if (navigator_.getCount() == 0) {
						hasPrevCache_ = false;
					} else {
						hasPrevCache_ = true;
					}
				} else {
					prev_ = navigator_.getPrevDocument();
					hasPrevCache_ = prev_ != null;
				}
			}
			return hasPrevCache_;
		}

		@Override
		public ViewEntryCoordinate next() {
			hasPrevCache_ = null;
			hasNextCache_ = null;
			prev_ = cur_;
			if (cur_ == null && next_ != null) {
				cur_ = next_;
			}
			if (cur_ == null) {
				ViewEntry newEntry = navigator_.getFirstDocument();
				cur_ = newEntry;
			} else if (next_ == null) {
				ViewEntry newEntry = navigator_.getNextDocument();
				cur_ = newEntry;
			} else {
				cur_ = next_;
			}
			next_ = null;
			index_++;
			if (cur_ != null) {
				ViewEntryCoordinate vec = new org.openntf.domino.big.impl.ViewEntryCoordinate(cur_);
				vec.setSourceNav(navigator_);
				return vec;
			} else {
				return null;
			}
		}

		@Override
		public int nextIndex() {
			if (hasNext()) {
				return index_ + 1;
			}
			return index_;
		}

		@Override
		public ViewEntryCoordinate previous() {
			hasPrevCache_ = null;
			hasNextCache_ = null;
			next_ = cur_;
			if (prev_ == null) {
				ViewEntry newEntry = navigator_.getPrevDocument();
				cur_ = newEntry;
			} else {
				cur_ = prev_;
			}
			prev_ = null;
			index_--;
			ViewEntryCoordinate vec = new org.openntf.domino.big.impl.ViewEntryCoordinate(cur_);
			vec.setSourceNav(navigator_);
			return vec;
		}

		@Override
		public int previousIndex() {
			if (hasPrevious()) {
				return index_ - 1;
			}
			return index_;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("ViewEntryListIterator is not modifiable");
		}

		@Override
		public void set(final ViewEntryCoordinate arg0) {
			throw new UnsupportedOperationException("ViewEntryListIterator is not modifiable");
		}
	}

	public static class ViewEntryListIterator implements ListIterator<ViewEntryCoordinate>, Indexable {
		private ViewEntryList parentList_;
		private ViewNavigator navigator_;
		private ViewEntry cur_;
		private Boolean hasNextCache_;
		private ViewEntry next_;
		private Boolean hasPrevCache_;
		private ViewEntry prev_;
		private int index_ = 0;
		private int fromIndex_ = 0;
		private int toIndex_ = Integer.MAX_VALUE;

		ViewEntryListIterator(final ViewNavigator navigator, final ViewEntryList parent) {
			navigator_ = navigator;
			parentList_ = parent;
			//			System.out.println("TEMP DEBUG Created a ViewEntryListIterator for " + navigator.getParentView().getName());
		}

		@Override
		public void setFromIndex(final int fromIndex) {
			fromIndex_ = fromIndex;
			next_ = navigator_.getNth(fromIndex);
		}

		@Override
		public void setToIndex(final int toIndex) {
			toIndex_ = toIndex;
		}

		@Override
		public void add(final ViewEntryCoordinate arg0) {
			throw new UnsupportedOperationException("ViewEntryListIterator is not modifiable");
		}

		@Override
		public boolean hasNext() {
			if (parentList_.emptyIterator_) {
				parentList_.emptyIterator_ = false;
				hasNextCache_ = false;
			}
			if (index_ >= (toIndex_ - fromIndex_)) {
				hasNextCache_ = false;
			}
			if (hasNextCache_ == null) {
				if (cur_ == null) {
					if (navigator_.getCount() == 0) {
						hasNextCache_ = false;
					} else {
						hasNextCache_ = true;
					}
				} else {
					next_ = navigator_.getNextSibling(cur_);
					hasNextCache_ = (next_ != null);
				}
				//				if (!hasNextCache_) {
				//					String pos = (next_ == null ? "null" : next_.getPosition());
				//					System.out.println("TEMP DEBUG Navigator for " + navigator_.getParentView().getName()
				//							+ " no longer has another entry. The index is " + index_ + " while the toIndex is " + String.valueOf(toIndex_)
				//							+ " and the next position is " + pos);
				//					Throwable t = new Throwable();
				//					t.printStackTrace();
				//				}
			}
			return hasNextCache_;
		}

		@Override
		public boolean hasPrevious() {
			if (parentList_.emptyIterator_) {
				parentList_.emptyIterator_ = false;
				return false;
			}
			if (index_ >= 0) {
				return false;
			}
			if (hasPrevCache_ == null) {
				if (cur_ == null) {
					if (navigator_.getCount() == 0) {
						hasPrevCache_ = false;
					} else {
						hasPrevCache_ = true;
					}
				} else {
					prev_ = navigator_.getPrevSibling(cur_);
					hasPrevCache_ = prev_ != null;
				}
			}
			return hasPrevCache_;
		}

		@Override
		public ViewEntryCoordinate next() {
			hasPrevCache_ = null;
			hasNextCache_ = null;
			prev_ = cur_;
			if (cur_ == null && next_ != null) {
				cur_ = next_;
			}
			if (cur_ == null) {
				ViewEntry newEntry = navigator_.getFirst();
				cur_ = newEntry;
			} else if (next_ == null) {
				ViewEntry newEntry = navigator_.getNextSibling(cur_);
				cur_ = newEntry;
			} else {
				cur_ = next_;
			}
			next_ = null;
			index_++;
			if (cur_ != null) {
				ViewEntryCoordinate vec = new org.openntf.domino.big.impl.ViewEntryCoordinate(cur_);
				vec.setSourceNav(navigator_);
				return vec;
			} else {
				return null;
			}
		}

		@Override
		public int nextIndex() {
			if (hasNext()) {
				return index_ + 1;
			}
			return index_;
		}

		@Override
		public ViewEntryCoordinate previous() {
			hasPrevCache_ = null;
			hasNextCache_ = null;
			next_ = cur_;
			if (prev_ == null) {
				ViewEntry newEntry = navigator_.getPrevSibling(cur_);
				cur_ = newEntry;
			} else {
				cur_ = prev_;
			}
			prev_ = null;
			index_--;
			ViewEntryCoordinate vec = new org.openntf.domino.big.impl.ViewEntryCoordinate(cur_);
			vec.setSourceNav(navigator_);
			return vec;
		}

		@Override
		public int previousIndex() {
			if (hasPrevious()) {
				return index_ - 1;
			}
			return index_;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("ViewEntryListIterator is not modifiable");
		}

		@Override
		public void set(final ViewEntryCoordinate arg0) {
			throw new UnsupportedOperationException("ViewEntryListIterator is not modifiable");
		}

	}

	public ViewEntryList(final ViewNavigator navigator) {
		navigator_ = navigator;
		//		parentView_ = navigator.getParentView();
		flatIterator_ = false;
	}

	public ViewEntryList(final ViewNavigator navigator, final boolean isFlat) {
		navigator_ = navigator;
		//		parentView_ = navigator.getParentView();
		flatIterator_ = isFlat;
		if (isFlat) {
			//			System.out.println("TEMP DEBUG constructing a flat ViewEntryList");
		}
	}

	public ViewEntryList(final ViewEntryCollection collection) {
		collection_ = collection;
		//		parentView_ = collection.getParent();
		flatIterator_ = false;
	}

	public ViewNavigator getNavigator() {
		return navigator_;
	}

	public ViewEntryCollection getCollection() {
		return collection_;
	}

	@Override
	public boolean add(final ViewEntryCoordinate arg0) {
		throw new UnsupportedOperationException("ViewEntryList is not modifiable");
	}

	@Override
	public void add(final int arg0, final ViewEntryCoordinate arg1) {
		throw new UnsupportedOperationException("ViewEntryList is not modifiable");
	}

	@Override
	public boolean addAll(final Collection<? extends ViewEntryCoordinate> arg0) {
		throw new UnsupportedOperationException("ViewEntryList is not modifiable");
	}

	@Override
	public boolean addAll(final int arg0, final Collection<? extends ViewEntryCoordinate> arg1) {
		throw new UnsupportedOperationException("ViewEntryList is not modifiable");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("ViewEntryList is not modifiable");
	}

	@Override
	public boolean contains(final Object arg0) {
		if (arg0 instanceof ViewEntryCoordinate) {
			ViewEntryCoordinate vec = (ViewEntryCoordinate) arg0;
			if (navigator_ != null) {
				ViewEntry current = getNavigator().getCurrent();
				boolean result = getNavigator().gotoPos(vec.getPosition(), org.openntf.domino.ViewNavigator.DEFAULT_SEPARATOR);
				getNavigator().gotoEntry(current);
				return result;
			} else {
				//TODO implement
			}
		}
		if (arg0 instanceof ViewEntry) {
			ViewEntry ve = (ViewEntry) arg0;
			if (navigator_ != null) {
				ViewEntry current = getNavigator().getCurrent();
				boolean result = getNavigator().gotoEntry(ve);
				getNavigator().gotoEntry(current);
				return result;
			} else {
				return getCollection().contains(ve);
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(final Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ViewEntryCoordinate get(final int arg0) {
		if (navigator_ != null) {
			ViewEntry entry = getNavigator().getNth(arg0);
			ViewEntryCoordinate vec = new org.openntf.domino.big.impl.ViewEntryCoordinate(entry);
			vec.setSourceNav(getNavigator());
			return vec;
		} else {
			ViewEntry entry = getCollection().getNthEntry(arg0);
			ViewEntryCoordinate vec = new org.openntf.domino.big.impl.ViewEntryCoordinate(entry);
			vec.setSourceColl(getCollection());
			return vec;
		}
	}

	@Override
	public int indexOf(final Object arg0) {
		throw new UnimplementedException();
	}

	private Boolean isEmpty_ = null;

	@Override
	public boolean isEmpty() {
		if (isEmpty_ == null) {
			if (navigator_ != null) {
				isEmpty_ = getNavigator().getCount() > 0;
			} else {
				isEmpty_ = getCollection().getCount() > 0;
			}
		}
		return isEmpty_;
	}

	@Override
	public Iterator<org.openntf.domino.big.ViewEntryCoordinate> iterator() {
		ListIterator<org.openntf.domino.big.ViewEntryCoordinate> result = null;
		if (navigator_ != null) {
			if (flatIterator_) {
				result = new ViewEntryFlatListIterator(getNavigator(), this);
			} else {
				result = new ViewEntryListIterator(getNavigator(), this);
			}
		} else {
			result = new ViewEntryCollectionIterator(getCollection(), this);
		}
		if (fromIndex_ != null) {
			((Indexable) result).setFromIndex(fromIndex_);
		}
		if (toIndex_ != null) {
			((Indexable) result).setToIndex(toIndex_);
		}
		return result;
	}

	@Override
	public int lastIndexOf(final Object arg0) {
		throw new UnimplementedException();
	}

	@Override
	public ListIterator<org.openntf.domino.big.ViewEntryCoordinate> listIterator() {
		ListIterator<org.openntf.domino.big.ViewEntryCoordinate> result = null;
		if (navigator_ != null) {
			if (flatIterator_) {
				result = new ViewEntryFlatListIterator(getNavigator(), this);
			} else {
				result = new ViewEntryListIterator(getNavigator(), this);
			}
		} else {
			result = new ViewEntryCollectionIterator(getCollection(), this);
		}
		if (fromIndex_ != null) {
			((Indexable) result).setFromIndex(fromIndex_);
		}
		if (toIndex_ != null) {
			((Indexable) result).setToIndex(toIndex_);
		}
		return result;
	}

	@Override
	public ListIterator<org.openntf.domino.big.ViewEntryCoordinate> listIterator(final int arg0) {
		throw new UnimplementedException("ViewEntryList.listIterator with an index is not yet implemented");
	}

	@Override
	public ViewEntryCoordinate remove(final int arg0) {
		throw new UnsupportedOperationException("ViewEntryList is not modifiable");
	}

	@Override
	public boolean remove(final Object arg0) {
		throw new UnsupportedOperationException("ViewEntryList is not modifiable");
	}

	@Override
	public boolean removeAll(final Collection<?> arg0) {
		throw new UnsupportedOperationException("ViewEntryList is not modifiable");
	}

	@Override
	public boolean retainAll(final Collection<?> arg0) {
		throw new UnsupportedOperationException("ViewEntryList is not modifiable");
	}

	@Override
	public ViewEntryCoordinate set(final int arg0, final ViewEntryCoordinate arg1) {
		throw new UnsupportedOperationException("ViewEntryList is not modifiable");
	}

	@Override
	public int size() {
		if (navigator_ != null) {
			return getNavigator().getCount();
		} else {
			return getCollection().getCount();
		}
	}

	Integer getFromIndex() {
		return fromIndex_;
	}

	Integer getToIndex() {
		return toIndex_;
	}

	@Override
	public List<ViewEntryCoordinate> subList(final int fromIndex, final int toIndex) {
		//		System.out.println("Getting entry from " + arg0 + " position");
		//		System.out.println("TEMP DEBUG sublisting a ViewEntryList from " + fromIndex + " to " + toIndex);
		if (fromIndex < 0 || toIndex > size() || fromIndex > toIndex) {
			throw new IndexOutOfBoundsException();
		}

		ViewEntryList result = null;
		if (collection_ != null) {
			result = new ViewEntryList(collection_);
		} else {
			if (flatIterator_) {
				result = new ViewEntryList(navigator_, flatIterator_);
			} else {
				result = new ViewEntryList(navigator_);
			}
		}
		result.fromIndex_ = fromIndex;	//BECAUSE VIEWS ARE FRACKING 1-BASED INSTEAD OF 0-BASED -- NTF
		result.toIndex_ = toIndex;
		return result;
	}

	@Override
	public Object[] toArray() {
		throw new UnimplementedException();
	}

	@Override
	public <T> T[] toArray(final T[] arg0) {
		throw new UnimplementedException();
	}

}
