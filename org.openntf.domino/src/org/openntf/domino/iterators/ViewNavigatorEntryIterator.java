/**
 * 
 */
package org.openntf.domino.iterators;

import java.util.logging.Logger;

import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author Nathan T. Freeman
 * 
 */
public class ViewNavigatorEntryIterator extends AbstractDominoIterator<ViewEntry> {
	private static final Logger log_ = Logger.getLogger(ViewNavigatorEntryIterator.class.getName());
	private static final long serialVersionUID = 1L;

	private transient ViewNavigator navigator_;

	/** The current entry_. */
	private transient ViewEntry previousEntry_;
	private transient ViewEntry currentEntry_;
	private transient ViewEntry nextEntry_;

	/** The started_. */
	private boolean started_;

	private boolean nextChecked_ = false;

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
	public ViewNavigatorEntryIterator(final ViewNavigator navigator) {
		super(navigator);
		navigator_ = navigator;
		//		// TODO replace this with a less-expensive operation
	}

	public ViewNavigator getNavigator() {
		return navigator_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		if (currentEntry_ != null) {	//TODO NTF need check to see if we're done...
			nextEntry_ = getNavigator().getNext(getCurrentEntry());
		} else {
			nextEntry_ = getNavigator().getFirst();
		}
		nextChecked_ = true;
		return nextEntry_ != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	public ViewEntry next() {
		ViewEntry result = null;
		//		if (hasNext()) {
		//			previousEntry_ = currentEntry_;	//TODO NTF use if we want to implement a ListIterator
		//			currentEntry_ = nextEntry_;
		//			nextEntry_ = null;
		//			return currentEntry_;
		//		} else {
		//			return null;
		//		}

		ViewEntry currentEntry = getCurrentEntry();
		try {
			if (nextChecked_) {
				result = getCurrentEntry();
			} else {
				if (hasNext()) {
					result = getCurrentEntry();
				}
			}
			currentIndex_++;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		} finally {
			setCurrentEntry(result);
		}
		nextChecked_ = false;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		// NOOP
	}

	/**
	 * Gets the current entry.
	 * 
	 * @return the current entry
	 */
	public ViewEntry getCurrentEntry() {
		return currentEntry_;
	}

	/**
	 * Sets the current entry.
	 * 
	 * @param currentEntry
	 *            the new current entry
	 */
	public void setCurrentEntry(final ViewEntry currentEntry) {
		currentEntry_ = currentEntry;
		setStarted(currentEntry != null);
		setDone(currentEntry == null);
	}

	/**
	 * Sets the done.
	 * 
	 * @param done
	 *            the new done
	 */
	public void setDone(final boolean done) {
		done_ = done;
	}

	/**
	 * Sets the started.
	 * 
	 * @param started
	 *            the new started
	 */
	public void setStarted(final boolean started) {
		started_ = started;
	}

}
