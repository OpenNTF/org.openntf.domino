/**
 * 
 */
package org.openntf.domino.iterators;

import java.util.logging.Logger;

import org.openntf.domino.Base;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.utils.DominoUtils;

/**
 * @author Nathan T. Freeman
 * 
 */
public class ViewNavigatorEntryIterator extends AbstractDominoIterator<ViewEntry> {
	private static final Logger log_ = Logger.getLogger(ViewNavigatorEntryIterator.class.getName());
	private static final long serialVersionUID = 1L;

	/** The current entry_. */
	private transient ViewEntry currentEntry_;

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
	 * @param collection
	 *            the collection
	 */
	public ViewNavigatorEntryIterator(final ViewNavigator navigator) {
		super(navigator);

		// TODO replace this with a less-expensive operation
		count_ = navigator.getCount();
	}

	public ViewNavigator getNavigator() {
		ViewNavigator result = null;
		Base<?> collection = super.getCollection();
		if (collection instanceof ViewEntryCollection) {
			result = (ViewNavigator) collection;
		}
		return result;
	}

	/**
	 * Gets the current entry.
	 * 
	 * @return the current entry
	 */
	public ViewEntry getCurrentEntry() {
		return currentEntry_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return currentIndex_ < count_;
	}

	/**
	 * Checks if is done.
	 * 
	 * @return true, if is done
	 */
	public boolean isDone() {
		return done_;
	}

	/**
	 * Checks if is started.
	 * 
	 * @return true, if is started
	 */
	public boolean isStarted() {
		return started_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	@SuppressWarnings("deprecation")
	public ViewEntry next() {
		ViewEntry result = null;
		ViewEntry currentEntry = getCurrentEntry();
		try {
			result = ((currentEntry == null) ? getNavigator().getFirst() : getNavigator().getNext(currentEntry));
			currentIndex_++;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		} finally {
			setCurrentEntry(result);
		}
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
