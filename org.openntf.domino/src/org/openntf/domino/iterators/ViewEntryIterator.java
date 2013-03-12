/*
 * © Copyright OpenNTF 2013
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
package org.openntf.domino.iterators;

import lotus.domino.ViewEntry;

import org.openntf.domino.Base;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.utils.DominoUtils;

public class ViewEntryIterator extends AbstractDominoIterator<ViewEntry> {
	private transient ViewEntry currentEntry_;
	private boolean started_;
	private boolean done_;

	public ViewEntryIterator(ViewEntryCollection collection) {
		super(collection);
	}

	@Override
	public ViewEntryCollection getCollection() {
		ViewEntryCollection result = null;
		Base<?> collection = super.getCollection();
		if (collection instanceof ViewEntryCollection) {
			result = (ViewEntryCollection) collection;
		}
		return result;
	}

	public ViewEntry getCurrentEntry() {
		return currentEntry_;
	}

	public boolean hasNext() {
		boolean result = false;
		ViewEntry currentEntry = getCurrentEntry();
		ViewEntry nextEntry = null;
		try {
			nextEntry = ((currentEntry == null) ? (isDone() ? null : getCollection().getFirstEntry()) : getCollection().getNextEntry(
					currentEntry));
			result = (nextEntry != null);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		} finally {
			DominoUtils.incinerate(nextEntry);
		}
		return result;
	}

	public boolean isDone() {
		return done_;
	}

	public boolean isStarted() {
		return started_;
	}

	public ViewEntry next() {
		ViewEntry result = null;
		ViewEntry currentEntry = getCurrentEntry();
		try {
			result = ((currentEntry == null) ? getCollection().getFirstEntry() : getCollection().getNextEntry(currentEntry));
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		} finally {
			DominoUtils.incinerate(currentEntry);
			setCurrentEntry(result);
		}
		return result;
	}

	public void remove() {
		// NOOP
	}

	public void setCurrentEntry(ViewEntry currentEntry) {
		currentEntry_ = currentEntry;
		setStarted(currentEntry != null);
		setDone(currentEntry == null);
	}

	public void setDone(boolean done) {
		done_ = done;
	}

	public void setStarted(boolean started) {
		started_ = started;
	}
}
