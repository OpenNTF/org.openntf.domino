/**
 *
 */
package org.openntf.domino.iterators;

import java.util.Iterator;
import java.util.logging.Logger;

import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;

/**
 * @author Nathan T. Freeman
 *
 */
public class ViewNavigatorEntryIterator implements Iterator<ViewEntry> {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(ViewNavigatorEntryIterator.class.getName());

	private transient ViewNavigator navigator_;

	@SuppressWarnings("unused")
	private transient ViewEntry previousEntry_;
	private transient ViewEntry currentEntry_;
	private transient ViewEntry nextEntry_;

	/**
	 * Instantiates a new view entry iterator.
	 *
	 * @param navigator
	 *            the navigator
	 */
	public ViewNavigatorEntryIterator(final ViewNavigator navigator) {
		navigator_ = navigator;
	}

	public ViewNavigator getNavigator() {
		return navigator_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		if (currentEntry_ != null) {
			nextEntry_ = getNavigator().getNext();
		} else {
			nextEntry_ = getNavigator().getFirst();
		}
		return nextEntry_ != null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Iterator#next()
	 */
	@Override
	public ViewEntry next() {
		if (hasNext()) {
			previousEntry_ = currentEntry_;	//TODO NTF use if we want to implement a ListIterator
			currentEntry_ = nextEntry_;
			nextEntry_ = null;
			return currentEntry_;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		// NOOP
	}

}
