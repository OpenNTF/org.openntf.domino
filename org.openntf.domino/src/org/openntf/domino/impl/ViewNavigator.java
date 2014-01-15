/*
 * Copyright 2013
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

import java.util.Iterator;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.iterators.ViewNavigatorEntryIterator;
import org.openntf.domino.utils.DominoUtils;

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
	@Deprecated
	public ViewNavigator(final lotus.domino.ViewNavigator delegate, final org.openntf.domino.View parent) {
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
			return fromLotus(getDelegate().getChild(), ViewEntry.SCHEMA, getParentView());
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
	public ViewEntry getChild(final lotus.domino.ViewEntry entry) {
		try {
			return fromLotus(getDelegate().getChild(toLotus(entry)), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getCurrent(), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getFirst(), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getFirstDocument(), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getLast(), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getLastDocument(), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getNext(), ViewEntry.SCHEMA, getParentView());
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
	public ViewEntry getNext(final lotus.domino.ViewEntry entry) {
		try {
			return fromLotus(getDelegate().getNext(toLotus(entry)), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getNextCategory(), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getNextDocument(), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getNextSibling(), ViewEntry.SCHEMA, getParentView());
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
	public ViewEntry getNextSibling(final lotus.domino.ViewEntry entry) {
		try {
			return fromLotus(getDelegate().getNextSibling(toLotus(entry)), ViewEntry.SCHEMA, getParentView());
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
	public ViewEntry getNth(final int n) {
		try {
			return fromLotus(getDelegate().getNth(n), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getParent(), ViewEntry.SCHEMA, getParentView());
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
	public ViewEntry getParent(final lotus.domino.ViewEntry entry) {
		try {
			return fromLotus(getDelegate().getParent(toLotus(entry)), ViewEntry.SCHEMA, getParentView());
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
	public ViewEntry getPos(final String pos, final char separator) {
		try {
			return fromLotus(getDelegate().getPos(pos, separator), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getPrev(), ViewEntry.SCHEMA, getParentView());
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
	public ViewEntry getPrev(final lotus.domino.ViewEntry entry) {
		try {
			return fromLotus(getDelegate().getPrev(toLotus(entry)), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getPrevCategory(), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getPrevDocument(), ViewEntry.SCHEMA, getParentView());
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
			return fromLotus(getDelegate().getPrevSibling(), ViewEntry.SCHEMA, getParentView());
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
	public ViewEntry getPrevSibling(final lotus.domino.ViewEntry entry) {
		try {
			return fromLotus(getDelegate().getPrevSibling(entry), ViewEntry.SCHEMA, getParentView());
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
	public boolean gotoChild(final lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoChild(toLotus(entry));
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
	public boolean gotoEntry(final Object entry) {
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
	public boolean gotoEntry(final Document document) {
		return gotoEntry((Object) document);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ViewNavigator#gotoEntry(org.openntf.domino.ViewEntry)
	 */
	public boolean gotoEntry(final ViewEntry entry) {
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
	public boolean gotoNext(final lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoNext(toLotus(entry));
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
	public boolean gotoNextSibling(final lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoNextSibling(toLotus(entry));
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
	public boolean gotoParent(final lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoParent(toLotus(entry));
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
	public boolean gotoPos(final String pos, final char separator) {
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
	public boolean gotoPrev(final lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoPrev(toLotus(entry));
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
	public boolean gotoPrevSibling(final lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoPrevSibling(toLotus(entry));
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
	public void markAllRead(final String userName) {
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
	public void markAllUnread(final String userName) {
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
	public void setAutoExpandGuidance(final int maxEntries, final int[] collapsedNoteIds, final int[] expandedNoteIds) {
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
	public void setAutoExpandGuidance(final int maxEntries, final lotus.domino.NoteCollection collapsedNoteIds,
			final lotus.domino.NoteCollection expandedNoteIds) {
		try {
			getDelegate().setAutoExpandGuidance(maxEntries, toLotus(collapsedNoteIds), toLotus(expandedNoteIds));
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
	public void setBufferMaxEntries(final int entryCount) {
		try {
			getParentView().setAutoUpdate(false);
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
	public void setCacheGuidance(final int maxEntries) {
		try {
			getParentView().setAutoUpdate(false);
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
	public void setCacheGuidance(final int maxEntries, final int readMode) {
		try {
			getParentView().setAutoUpdate(false);
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
	public void setCacheSize(final int size) {
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
	public void setEntryOptions(final int options) {
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
	public void setMaxLevel(final int maxLevel) {
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
	public int skip(final int count) {
		try {
			return getDelegate().skip(count);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public Database getAncestorDatabase() {
		return this.getParentView().getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getAncestorDatabase().getAncestorSession();
	}

	public Iterator<org.openntf.domino.ViewEntry> iterator() {
		return new ViewNavigatorEntryIterator(this);
	}
}
