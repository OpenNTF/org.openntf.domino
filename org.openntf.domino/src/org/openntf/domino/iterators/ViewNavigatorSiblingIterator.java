/**
 * 
 */
package org.openntf.domino.iterators;

import java.util.logging.Logger;

import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;

/**
 * @author Nathan T. Freeman
 * 
 */
public class ViewNavigatorSiblingIterator extends AbstractDominoIterator<ViewEntry> {
	private static final Logger log_ = Logger.getLogger(ViewNavigatorSiblingIterator.class.getName());
	private static final long serialVersionUID = 1L;

	private transient ViewNavigator navigator_;

	/** The current entry_. */
	private transient ViewEntry previousEntry_;
	private transient ViewEntry currentEntry_;
	private transient ViewEntry nextEntry_;

	/** The started_. */
	private boolean started_;

	/** The done_. */
	private boolean done_;

	/** The count_. */
	private int count_ = 0;

	/** The current index_. */
	private int currentIndex_ = 0;

	/**
	 * Instantiates a new view entry iterator.
	 * 
	 * @param navigator
	 *            the navigator
	 */
	public ViewNavigatorSiblingIterator(final ViewNavigator navigator) {
		super(navigator);
		navigator_ = navigator;
		//		// TODO replace this with a less-expensive operation
		//		count_ = navigator.getCount();
	}

	public ViewNavigator getNavigator() {
		return navigator_;
	}

	//	/**
	//	 * Gets the current entry.
	//	 * 
	//	 * @return the current entry
	//	 */
	//	public ViewEntry getCurrentEntry() {
	//		return currentEntry_;
	//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		if (currentEntry_ != null) {
			nextEntry_ = getNavigator().getNextSibling(currentEntry_);
		} else {
			nextEntry_ = getNavigator().getFirst();
		}
		return nextEntry_ != null;
	}

	//	/**
	//	 * Checks if is done.
	//	 * 
	//	 * @return true, if is done
	//	 */
	//	public boolean isDone() {
	//		return done_;
	//	}
	//
	//	/**
	//	 * Checks if is started.
	//	 * 
	//	 * @return true, if is started
	//	 */
	//	public boolean isStarted() {
	//		return started_;
	//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	public ViewEntry next() {
		//		started_ = true;
		ViewEntry result = null;
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
	public void remove() {
		// NOOP
	}

	//	/**
	//	 * Sets the current entry.
	//	 * 
	//	 * @param currentEntry
	//	 *            the new current entry
	//	 */
	//	public void setCurrentEntry(final ViewEntry currentEntry) {
	//		currentEntry_ = currentEntry;
	//		setStarted(currentEntry != null);
	//		setDone(currentEntry == null);
	//	}
	//
	//	/**
	//	 * Sets the done.
	//	 * 
	//	 * @param done
	//	 *            the new done
	//	 */
	//	public void setDone(final boolean done) {
	//		done_ = done;
	//	}
	//
	//	/**
	//	 * Sets the started.
	//	 * 
	//	 * @param started
	//	 *            the new started
	//	 */
	//	public void setStarted(final boolean started) {
	//		started_ = started;
	//	}
}
