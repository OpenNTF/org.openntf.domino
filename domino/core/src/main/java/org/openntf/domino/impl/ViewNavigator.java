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
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.iterators.ViewNavigatorEntryIterator;
import org.openntf.domino.iterators.ViewNavigatorSiblingIterator;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ViewNavigator.
 */
public class ViewNavigator extends BaseThreadSafe<org.openntf.domino.ViewNavigator, lotus.domino.ViewNavigator, View> implements
org.openntf.domino.ViewNavigator {

	private boolean forceJavaDates_ = false;
	private int cacheSize_ = -1;
	private int maxLevel_ = -1;
	private int maxEntries_ = -1;
	private int entryOptions_ = -1;
	private int readMode_ = -1;
	private int[] collapsedNoteIds_ = null;
	private int[] expandedNoteIds_ = null;
	private String curPosition_ = null;
	private String curNoteid_ = null;
	private String startingPosition_ = null;
	private String startingNoteid_ = null;
	private String startingCategory_ = null;
	private String unreadUsername_;
	private Types navType_;

	/**
	 * Instantiates a new outline.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 * @param wf
	 *            the wrapperfactory
	 * @param cppId
	 *            the cpp-id
	 */
	protected ViewNavigator(final lotus.domino.ViewNavigator delegate, final View parent) {
		super(delegate, parent, Base.NOTES_VIEWNAV);
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
			lotus.domino.ViewEntry newEntry = getDelegate().getChild();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getChild(toLotus(entry));
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getFirst();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getFirstDocument();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getLast();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getLastDocument();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getNext();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getNext(toLotus(entry));
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getNextCategory();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getNextDocument();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getNextSibling();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getNextSibling(toLotus(entry));
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getNth(n);
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
	public final ViewEntry getParent() {
		try {
			lotus.domino.ViewEntry newEntry = getDelegate().getParent();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getParent(toLotus(entry));
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
	public final View getParentView() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#getPos(java.lang.String, char)
	 */
	@Override
	public ViewEntry getPos(final String pos, final char separator) {
		try {
			lotus.domino.ViewEntry newEntry = getDelegate().getPos(pos, separator);
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getPrev();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getPrev(toLotus(entry));
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getPrevCategory();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getPrevDocument();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getPrevSibling();
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			lotus.domino.ViewEntry newEntry = getDelegate().getPrevSibling(toLotus(entry));
			if (newEntry != null) {
				curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
				if (forceJavaDates_)
					newEntry.setPreferJavaDates(true);
			}
			return fromLotus(newEntry, ViewEntry.SCHEMA, getParentView());
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
			boolean result = getDelegate().gotoChild();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
				//TODO NTF - recycle? I think probably not, but need a testing plan
			}
			return result;
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
			boolean result = getDelegate().gotoChild(toLotus(entry));
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoEntry(toLotus(entry));
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ViewNavigator#gotoEntry(org.openntf.domino.Document)
	 */
	@Override
	public boolean gotoEntry(final Document document) {
		return gotoEntry((Object) document);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ViewNavigator#gotoEntry(org.openntf.domino.ViewEntry)
	 */
	@Override
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
			boolean result = getDelegate().gotoFirst();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoFirstDocument();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoLast();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoLastDocument();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoNext();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoNext(toLotus(entry));
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoNextCategory();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoNextDocument();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoNextSibling();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoNextSibling(toLotus(entry));
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoParent();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoParent(toLotus(entry));
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoPos(pos, separator);
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoPrev();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoPrev(toLotus(entry));
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoPrevCategory();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoPrevDocument();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoPrevSibling();
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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
			boolean result = getDelegate().gotoPrevSibling(toLotus(entry));
			if (result) {
				lotus.domino.ViewEntry newEntry = getDelegate().getCurrent();
				if (newEntry != null) {
					curPosition_ = newEntry.getPosition(DEFAULT_SEPARATOR);
					if (forceJavaDates_)
						newEntry.setPreferJavaDates(true);
				}
			}
			return result;
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

	@Override
	protected void resurrect() { // should only happen if the delegate has been destroyed somehow.
		try {
			lotus.domino.ViewNavigator newDelegate = null;
			lotus.domino.ViewEntry entry = null;
			lotus.domino.View rawView = toLotus(getParentView());
			switch (this.navType_) {
			case NONE:
				if (this.cacheSize_ > -1) {
					newDelegate = rawView.createViewNav(cacheSize_);
				} else {
					newDelegate = rawView.createViewNav();
				}
				break;
			case FROM:
				entry = toLotus(getParentView().getEntryAtPosition(startingPosition_, '.'));
				if (this.cacheSize_ > -1) {
					newDelegate = rawView.createViewNavFrom(toLotus(entry), cacheSize_);
				} else {
					newDelegate = rawView.createViewNavFrom(toLotus(entry));
				}
				break;
			case CATEGORY:
				if (this.cacheSize_ > -1) {
					newDelegate = rawView.createViewNavFromCategory(startingCategory_, cacheSize_);
				} else {
					newDelegate = rawView.createViewNavFromCategory(startingCategory_);
				}
				break;
			case CHILDREN:
				entry = toLotus(getParentView().getEntryAtPosition(startingPosition_, '.'));
				if (this.cacheSize_ > -1) {
					newDelegate = rawView.createViewNavFromChildren(entry, cacheSize_);
				} else {
					newDelegate = rawView.createViewNavFromChildren(entry);
				}
				break;
			case DESCENDANTS:
				entry = toLotus(getParentView().getEntryAtPosition(startingPosition_, '.'));
				if (this.cacheSize_ > -1) {
					newDelegate = rawView.createViewNavFromDescendants(entry, cacheSize_);
				} else {
					newDelegate = rawView.createViewNavFromDescendants(entry);
				}
				break;
			case UNREAD:
				if (this.unreadUsername_ == null) {
					newDelegate = rawView.createViewNavFromAllUnread();
				} else {
					newDelegate = rawView.createViewNavFromAllUnread(unreadUsername_);
				}
				break;
			case MAXLEVEL:
				if (this.cacheSize_ > -1) {
					newDelegate = rawView.createViewNavMaxLevel(maxLevel_, cacheSize_);
				} else {
					newDelegate = rawView.createViewNavMaxLevel(maxLevel_);
				}
			}
			if (entryOptions_ > -1)
				newDelegate.setEntryOptions(entryOptions_);
			if (maxLevel_ > -1)
				newDelegate.setMaxLevel(maxLevel_);
			if (readMode_ > -1)
				newDelegate.setCacheGuidance(cacheSize_, readMode_);
			if (collapsedNoteIds_ != null || expandedNoteIds_ != null) {
				newDelegate.setAutoExpandGuidance(maxEntries_, collapsedNoteIds_, expandedNoteIds_);
			}
			if (newDelegate.gotoPos(curPosition_, DEFAULT_SEPARATOR)) {

			}
			setDelegate(newDelegate, true);
		} catch (Exception e) {
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
			this.maxEntries_ = maxEntries;
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
			this.maxEntries_ = maxEntries;
			if (collapsedNoteIds != null)
				this.collapsedNoteIds_ = collapsedNoteIds.getNoteIDs();
			if (expandedNoteIds != null)
				this.expandedNoteIds_ = expandedNoteIds.getNoteIDs();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#setBufferMaxEntries(int)
	 * @deprecated
	 * @use org.openntf.domino.ViewNavigator#setCacheGuidance(int) instead
	 */
	@Override
	@Deprecated
	public void setBufferMaxEntries(final int entryCount) {
		try {
			getDelegate().setCacheGuidance(entryCount);
			this.maxEntries_ = entryCount;
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
			this.maxEntries_ = maxEntries;
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
			this.readMode_ = readMode;
			this.maxEntries_ = maxEntries;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewNavigator#setCacheSize(int)
	 * @deprecated
	 * @use org.openntf.domino.ViewNavigator#setCacheGuidance(int) instead
	 */
	@Override
	@Deprecated
	public void setCacheSize(final int size) {
		try {
			getDelegate().setCacheGuidance(size);
			this.cacheSize_ = size;
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
			this.entryOptions_ = options;
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
			this.maxLevel_ = maxLevel;
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
	public final Database getAncestorDatabase() {
		return this.getParentView().getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return this.getAncestorDatabase().getAncestorSession();
	}

	@Override
	public Iterator<org.openntf.domino.ViewEntry> iterator() {
		return new ViewNavigatorEntryIterator(this);
	}

	protected Iterator<org.openntf.domino.ViewEntry> siblingIterator() {
		return new ViewNavigatorSiblingIterator(this);
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

	void registerStartPosition() {
		ViewEntry ve = this.getCurrent();
		if (ve != null) {
			startingPosition_ = ve.getPosition();
		} else {
			startingPosition_ = "";
		}
	}

	void setType(final Types type) {
		navType_ = type;
		registerStartPosition();
	}

	void setStartCategory(final String category) {
		startingCategory_ = category;
	}

	void setUnreadUser(final String username) {
		unreadUsername_ = username;
	}

}
