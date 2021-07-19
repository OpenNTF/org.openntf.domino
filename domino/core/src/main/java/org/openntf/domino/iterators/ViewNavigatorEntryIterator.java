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
			nextEntry_ = getNavigator().getNext(currentEntry_);
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
