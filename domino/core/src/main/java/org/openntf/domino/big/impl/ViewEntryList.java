package org.openntf.domino.big.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.big.ViewEntryCoordinate;
import org.openntf.domino.exceptions.UnimplementedException;

public class ViewEntryList implements List<org.openntf.domino.big.ViewEntryCoordinate> {
	private View parentView_;
	private ViewNavigator navigator_;

	public static class ViewEntryListIterator implements ListIterator<ViewEntryCoordinate> {
		private ViewNavigator navigator_;
		private ViewEntry cur_;
		private Boolean hasNextCache_;
		private ViewEntry next_;
		private Boolean hasPrevCache_;
		private ViewEntry prev_;
		private int index_;

		ViewEntryListIterator(final ViewNavigator navigator) {
			navigator_ = navigator;
		}

		@Override
		public void add(final ViewEntryCoordinate arg0) {
			throw new UnsupportedOperationException("ViewEntryListIterator is not modifiable");
		}

		@Override
		public boolean hasNext() {
			if (hasNextCache_ == null) {
				next_ = navigator_.getNextSibling(cur_);
				hasNextCache_ = next_ != null;
			}
			return hasNextCache_;
		}

		@Override
		public boolean hasPrevious() {
			if (hasPrevCache_ == null) {
				prev_ = navigator_.getPrevSibling(cur_);
				hasPrevCache_ = prev_ != null;
			}
			return hasPrevCache_;
		}

		@Override
		public ViewEntryCoordinate next() {
			hasPrevCache_ = null;
			hasNextCache_ = null;
			prev_ = cur_;
			if (next_ == null) {
				ViewEntry newEntry = navigator_.getNextSibling(cur_);
				cur_ = newEntry;
			} else {
				cur_ = next_;
			}
			next_ = null;
			index_++;
			return new org.openntf.domino.big.impl.ViewEntryCoordinate(cur_);
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
			return new org.openntf.domino.big.impl.ViewEntryCoordinate(cur_);
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
		parentView_ = navigator.getParentView();
	}

	public ViewNavigator getNavigator() {
		return navigator_;
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
			ViewEntry current = navigator_.getCurrent();
			boolean result = navigator_.gotoPos(vec.getPosition(), org.openntf.domino.ViewNavigator.DEFAULT_SEPARATOR);
			navigator_.gotoEntry(current);
			return result;
		}
		if (arg0 instanceof ViewEntry) {
			ViewEntry ve = (ViewEntry) arg0;
			ViewEntry current = navigator_.getCurrent();
			boolean result = navigator_.gotoEntry(ve);
			navigator_.gotoEntry(current);
			return result;
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
		ViewEntry entry = getNavigator().getNth(arg0);
		return new org.openntf.domino.big.impl.ViewEntryCoordinate(entry);
	}

	@Override
	public int indexOf(final Object arg0) {
		throw new UnimplementedException();
	}

	private Boolean isEmpty_ = null;

	@Override
	public boolean isEmpty() {
		if (isEmpty_ == null) {
			isEmpty_ = navigator_.getCount() > 0;
		}
		return isEmpty_;
	}

	@Override
	public Iterator<org.openntf.domino.big.ViewEntryCoordinate> iterator() {
		return new ViewEntryListIterator(navigator_);
	}

	@Override
	public int lastIndexOf(final Object arg0) {
		throw new UnimplementedException();
	}

	@Override
	public ListIterator<org.openntf.domino.big.ViewEntryCoordinate> listIterator() {
		return new ViewEntryListIterator(navigator_);

	}

	@Override
	public ListIterator<org.openntf.domino.big.ViewEntryCoordinate> listIterator(final int arg0) {
		// TODO Auto-generated method stub
		return null;
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
		return navigator_.getCount();
	}

	@Override
	public List<ViewEntryCoordinate> subList(final int arg0, final int arg1) {
		throw new UnimplementedException();
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
