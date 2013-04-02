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

import lotus.domino.NotesException;

import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class ViewNavigator.
 */
public class ViewNavigator extends Base<org.openntf.domino.ViewNavigator, lotus.domino.ViewNavigator> implements
		org.openntf.domino.ViewNavigator {

	/**
	 * Instantiates a new view navigator.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public ViewNavigator(lotus.domino.ViewNavigator delegate, org.openntf.domino.View parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getBufferMaxEntries()
	 */
	@Override
	public int getBufferMaxEntries() {
		return getDelegate().getBufferMaxEntries();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getCacheSize()
	 */
	@Override
	public int getCacheSize() {
		return getDelegate().getCacheSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getChild()
	 */
	@Override
	public ViewEntry getChild() {
		try {
			return Factory.fromLotus(getDelegate().getChild(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getChild(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getChild(lotus.domino.ViewEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getChild((lotus.domino.ViewEntry) toLotus(entry)), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getCount()
	 */
	@Override
	public int getCount() {
		return getDelegate().getCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getCurrent()
	 */
	@Override
	public ViewEntry getCurrent() {
		try {
			return Factory.fromLotus(getDelegate().getCurrent(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getEntryOptions()
	 */
	@Override
	public int getEntryOptions() {
		return getDelegate().getEntryOptions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getFirst()
	 */
	@Override
	public ViewEntry getFirst() {
		try {
			return Factory.fromLotus(getDelegate().getFirst(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getFirstDocument()
	 */
	@Override
	public ViewEntry getFirstDocument() {
		try {
			return Factory.fromLotus(getDelegate().getFirstDocument(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getLast()
	 */
	@Override
	public ViewEntry getLast() {
		try {
			return Factory.fromLotus(getDelegate().getLast(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getLastDocument()
	 */
	@Override
	public ViewEntry getLastDocument() {
		try {
			return Factory.fromLotus(getDelegate().getLastDocument(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getMaxLevel()
	 */
	@Override
	public int getMaxLevel() {
		return getDelegate().getMaxLevel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getNext()
	 */
	@Override
	public ViewEntry getNext() {
		try {
			return Factory.fromLotus(getDelegate().getNext(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getNext(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getNext(lotus.domino.ViewEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getNext((lotus.domino.ViewEntry) toLotus(entry)), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getNextCategory()
	 */
	@Override
	public ViewEntry getNextCategory() {
		try {
			return Factory.fromLotus(getDelegate().getNextCategory(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getNextDocument()
	 */
	@Override
	public ViewEntry getNextDocument() {
		try {
			return Factory.fromLotus(getDelegate().getNextDocument(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getNextSibling()
	 */
	@Override
	public ViewEntry getNextSibling() {
		try {
			return Factory.fromLotus(getDelegate().getNextSibling(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getNextSibling(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getNextSibling(lotus.domino.ViewEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getNextSibling((lotus.domino.ViewEntry) toLotus(entry)), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getNth(int)
	 */
	@Override
	public ViewEntry getNth(int n) {
		try {
			return Factory.fromLotus(getDelegate().getNth(n), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public ViewEntry getParent() {
		try {
			return Factory.fromLotus(getDelegate().getParent(), ViewEntry.class, this);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getParent(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getParent(lotus.domino.ViewEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getParent((lotus.domino.ViewEntry) toLotus(entry)), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getParentView()
	 */
	@Override
	public View getParentView() {
		return (View) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getPos(java.lang.String, char)
	 */
	@Override
	public ViewEntry getPos(String pos, char separator) {
		try {
			return Factory.fromLotus(getDelegate().getPos(pos, separator), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getPrev()
	 */
	@Override
	public ViewEntry getPrev() {
		try {
			return Factory.fromLotus(getDelegate().getPrev(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getPrev(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getPrev(lotus.domino.ViewEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getPrev((lotus.domino.ViewEntry) toLotus(entry)), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getPrevCategory()
	 */
	@Override
	public ViewEntry getPrevCategory() {
		try {
			return Factory.fromLotus(getDelegate().getPrevCategory(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getPrevDocument()
	 */
	@Override
	public ViewEntry getPrevDocument() {
		try {
			return Factory.fromLotus(getDelegate().getPrevDocument(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getPrevSibling()
	 */
	@Override
	public ViewEntry getPrevSibling() {
		try {
			return Factory.fromLotus(getDelegate().getPrevSibling(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getPrevSibling(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getPrevSibling(lotus.domino.ViewEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getPrevSibling((lotus.domino.ViewEntry) entry), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoChild()
	 */
	@Override
	public boolean gotoChild() {
		try {
			return getDelegate().gotoChild();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoChild(lotus.domino.ViewEntry)
	 */
	@Override
	public boolean gotoChild(lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoChild((lotus.domino.ViewEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoEntry(java.lang.Object)
	 */
	@Override
	public boolean gotoEntry(Object entry) {
		try {
			return getDelegate().gotoEntry(toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ViewNavigator#gotoEntry(org.openntf.domino.Document)
	 */
	public boolean gotoEntry(Document document) {
		return gotoEntry((Object) document);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ViewNavigator#gotoEntry(org.openntf.domino.ViewEntry)
	 */
	public boolean gotoEntry(ViewEntry entry) {
		return gotoEntry((Object) entry);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoFirst()
	 */
	@Override
	public boolean gotoFirst() {
		try {
			return getDelegate().gotoFirst();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoFirstDocument()
	 */
	@Override
	public boolean gotoFirstDocument() {
		try {
			return getDelegate().gotoFirstDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoLast()
	 */
	@Override
	public boolean gotoLast() {
		try {
			return getDelegate().gotoLast();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoLastDocument()
	 */
	@Override
	public boolean gotoLastDocument() {
		try {
			return getDelegate().gotoLastDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoNext()
	 */
	@Override
	public boolean gotoNext() {
		try {
			return getDelegate().gotoNext();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoNext(lotus.domino.ViewEntry)
	 */
	@Override
	public boolean gotoNext(lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoNext((lotus.domino.ViewEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoNextCategory()
	 */
	@Override
	public boolean gotoNextCategory() {
		try {
			return getDelegate().gotoNextCategory();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoNextDocument()
	 */
	@Override
	public boolean gotoNextDocument() {
		try {
			return getDelegate().gotoNextDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoNextSibling()
	 */
	@Override
	public boolean gotoNextSibling() {
		try {
			return getDelegate().gotoNextSibling();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoNextSibling(lotus.domino.ViewEntry)
	 */
	@Override
	public boolean gotoNextSibling(lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoNextSibling((lotus.domino.ViewEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoParent()
	 */
	@Override
	public boolean gotoParent() {
		try {
			return getDelegate().gotoParent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoParent(lotus.domino.ViewEntry)
	 */
	@Override
	public boolean gotoParent(lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoParent((lotus.domino.ViewEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoPos(java.lang.String, char)
	 */
	@Override
	public boolean gotoPos(String pos, char separator) {
		try {
			return getDelegate().gotoPos(pos, separator);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoPrev()
	 */
	@Override
	public boolean gotoPrev() {
		try {
			return getDelegate().gotoPrev();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoPrev(lotus.domino.ViewEntry)
	 */
	@Override
	public boolean gotoPrev(lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoPrev((lotus.domino.ViewEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoPrevCategory()
	 */
	@Override
	public boolean gotoPrevCategory() {
		try {
			return getDelegate().gotoPrevCategory();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoPrevDocument()
	 */
	@Override
	public boolean gotoPrevDocument() {
		try {
			return getDelegate().gotoPrevDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoPrevSibling()
	 */
	@Override
	public boolean gotoPrevSibling() {
		try {
			return getDelegate().gotoPrevSibling();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#gotoPrevSibling(lotus.domino.ViewEntry)
	 */
	@Override
	public boolean gotoPrevSibling(lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoPrevSibling((lotus.domino.ViewEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#markAllRead()
	 */
	@Override
	public void markAllRead() {
		try {
			getDelegate().markAllRead();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#markAllRead(java.lang.String)
	 */
	@Override
	public void markAllRead(String userName) {
		try {
			getDelegate().markAllRead(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#markAllUnread()
	 */
	@Override
	public void markAllUnread() {
		try {
			getDelegate().markAllUnread();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#markAllUnread(java.lang.String)
	 */
	@Override
	public void markAllUnread(String userName) {
		try {
			getDelegate().markAllUnread(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#setAutoExpandGuidance(int, int[], int[])
	 */
	@Override
	public void setAutoExpandGuidance(int maxEntries, int[] collapsedNoteIds, int[] expandedNoteIds) {
		try {
			getDelegate().setAutoExpandGuidance(maxEntries, collapsedNoteIds, expandedNoteIds);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#setAutoExpandGuidance(int, lotus.domino.NoteCollection, lotus.domino.NoteCollection)
	 */
	@Override
	public void setAutoExpandGuidance(int maxEntries, lotus.domino.NoteCollection collapsedNoteIds,
			lotus.domino.NoteCollection expandedNoteIds) {
		try {
			getDelegate().setAutoExpandGuidance(maxEntries, (lotus.domino.NoteCollection) toLotus(collapsedNoteIds),
					(lotus.domino.NoteCollection) toLotus(expandedNoteIds));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#setBufferMaxEntries(int)
	 */
	@Override
	public void setBufferMaxEntries(int entryCount) {
		try {
			getDelegate().setBufferMaxEntries(entryCount);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#setCacheGuidance(int)
	 */
	@Override
	public void setCacheGuidance(int maxEntries) {
		try {
			getDelegate().setCacheGuidance(maxEntries);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#setCacheGuidance(int, int)
	 */
	@Override
	public void setCacheGuidance(int maxEntries, int readMode) {
		try {
			getDelegate().setCacheGuidance(maxEntries, readMode);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#setCacheSize(int)
	 */
	@Override
	public void setCacheSize(int size) {
		try {
			getDelegate().setCacheSize(size);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#setEntryOptions(int)
	 */
	@Override
	public void setEntryOptions(int options) {
		try {
			getDelegate().setEntryOptions(options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#setMaxLevel(int)
	 */
	@Override
	public void setMaxLevel(int maxLevel) {
		try {
			getDelegate().setMaxLevel(maxLevel);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#skip(int)
	 */
	@Override
	public int skip(int count) {
		try {
			return getDelegate().skip(count);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}
}
