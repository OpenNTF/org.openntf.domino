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

import java.util.Iterator;

import lotus.domino.NotesException;

import org.openntf.domino.iterators.ViewEntryIterator;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class ViewEntryCollection.
 */
public class ViewEntryCollection extends Base<org.openntf.domino.ViewEntryCollection, lotus.domino.ViewEntryCollection> implements
		org.openntf.domino.ViewEntryCollection {

	/**
	 * Instantiates a new view entry collection.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public ViewEntryCollection(lotus.domino.ViewEntryCollection delegate, org.openntf.domino.View parent) {
		super(delegate, parent);
	}

	// FIXME NTF -- all method that return a ViewEntry probably need to parent to the View rather than the Collection. Someone should verify
	// the defined behavior.
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#addEntry(java.lang.Object)
	 */
	@Override
	public void addEntry(Object obj) {
		try {
			getDelegate().addEntry(toLotus(obj));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#addEntry(java.lang.Object, boolean)
	 */
	@Override
	public void addEntry(Object obj, boolean checkDups) {
		try {
			getDelegate().addEntry(toLotus(obj), checkDups);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#cloneCollection()
	 */
	@Override
	public ViewEntryCollection cloneCollection() {
		try {
			return Factory.fromLotus(getDelegate().cloneCollection(), ViewEntryCollection.class, getParent());
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<org.openntf.domino.ViewEntry> iterator() {
		return new ViewEntryIterator(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#contains(lotus.domino.Base)
	 */
	@Override
	public boolean contains(lotus.domino.Base entries) {
		try {
			return getDelegate().contains(toLotus(entries));
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#contains(int)
	 */
	@Override
	public boolean contains(int noteId) {
		try {
			boolean result = getDelegate().contains(noteId);
			return result;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#contains(java.lang.String)
	 */
	@Override
	public boolean contains(String noteId) {
		try {
			boolean result = getDelegate().contains(noteId);
			return result;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#deleteEntry(lotus.domino.ViewEntry)
	 */
	@Override
	public void deleteEntry(lotus.domino.ViewEntry entry) {
		try {
			getDelegate().deleteEntry((lotus.domino.ViewEntry) toLotus(entry));
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#FTSearch(java.lang.String)
	 */
	@Override
	public void FTSearch(String query) {
		try {
			getDelegate().FTSearch(query);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#FTSearch(java.lang.String, int)
	 */
	@Override
	public void FTSearch(String query, int maxDocs) {
		try {
			getDelegate().FTSearch(query, maxDocs);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#getCount()
	 */
	@Override
	public int getCount() {
		try {
			return getDelegate().getCount();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#getEntry(java.lang.Object)
	 */
	@Override
	public ViewEntry getEntry(Object entry) {
		try {
			return Factory.fromLotus(getDelegate().getEntry(toLotus(entry)), org.openntf.domino.ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#getFirstEntry()
	 */
	@Override
	public ViewEntry getFirstEntry() {
		try {
			return Factory.fromLotus(getDelegate().getFirstEntry(), org.openntf.domino.ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#getLastEntry()
	 */
	@Override
	public ViewEntry getLastEntry() {
		try {
			return Factory.fromLotus(getDelegate().getLastEntry(), org.openntf.domino.ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#getNextEntry()
	 */
	@Override
	public ViewEntry getNextEntry() {
		try {
			return Factory.fromLotus(getDelegate().getNextEntry(), org.openntf.domino.ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#getNextEntry(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getNextEntry(lotus.domino.ViewEntry entry) {
		try {
			ViewEntry result = Factory.fromLotus(getDelegate().getNextEntry((lotus.domino.ViewEntry) toLotus(entry)),
					org.openntf.domino.ViewEntry.class, this);
			entry.recycle();
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#getNthEntry(int)
	 */
	@Override
	public ViewEntry getNthEntry(int n) {
		try {
			return Factory.fromLotus(getDelegate().getNthEntry(n), org.openntf.domino.ViewEntry.class, this);
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
	public org.openntf.domino.View getParent() {
		return (org.openntf.domino.View) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#getPrevEntry()
	 */
	@Override
	public ViewEntry getPrevEntry() {
		try {
			return Factory.fromLotus(getDelegate().getPrevEntry(), org.openntf.domino.ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#getPrevEntry(lotus.domino.ViewEntry)
	 */
	@Override
	public ViewEntry getPrevEntry(lotus.domino.ViewEntry entry) {
		try {
			ViewEntry result = Factory.fromLotus(getDelegate().getPrevEntry((lotus.domino.ViewEntry) toLotus(entry)),
					org.openntf.domino.ViewEntry.class, this);
			entry.recycle();
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#getQuery()
	 */
	@Override
	public String getQuery() {
		try {
			return getDelegate().getQuery();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#intersect(lotus.domino.Base)
	 */
	@Override
	public void intersect(lotus.domino.Base entries) {
		try {
			getDelegate().intersect(toLotus(entries));
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#intersect(int)
	 */
	@Override
	public void intersect(int noteId) {
		try {
			getDelegate().intersect(noteId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#intersect(java.lang.String)
	 */
	@Override
	public void intersect(String noteId) {
		try {
			getDelegate().intersect(noteId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#markAllRead()
	 */
	@Override
	public void markAllRead() {
		try {
			getDelegate().markAllRead();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#markAllRead(java.lang.String)
	 */
	@Override
	public void markAllRead(String userName) {
		try {
			getDelegate().markAllUnread(userName);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#markAllUnread()
	 */
	@Override
	public void markAllUnread() {
		try {
			getDelegate().markAllUnread();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#markAllUnread(java.lang.String)
	 */
	@Override
	public void markAllUnread(String userName) {
		try {
			getDelegate().markAllUnread(userName);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#merge(lotus.domino.Base)
	 */
	@Override
	public void merge(lotus.domino.Base entries) {
		try {
			getDelegate().merge(toLotus(entries));
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#merge(int)
	 */
	@Override
	public void merge(int noteId) {
		try {
			getDelegate().merge(noteId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#merge(java.lang.String)
	 */
	@Override
	public void merge(String noteId) {
		try {
			getDelegate().merge(noteId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#putAllInFolder(java.lang.String)
	 */
	@Override
	public void putAllInFolder(String folderName) {
		try {
			getDelegate().putAllInFolder(folderName);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#putAllInFolder(java.lang.String, boolean)
	 */
	@Override
	public void putAllInFolder(String folderName, boolean createOnFail) {
		try {
			getDelegate().putAllInFolder(folderName, createOnFail);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#removeAll(boolean)
	 */
	@Override
	public void removeAll(boolean force) {
		try {
			getDelegate().removeAll(force);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#removeAllFromFolder(java.lang.String)
	 */
	@Override
	public void removeAllFromFolder(String folderName) {
		try {
			getDelegate().removeAllFromFolder(folderName);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#stampAll(java.lang.String, java.lang.Object)
	 */
	@Override
	public void stampAll(String itemName, Object value) {
		try {
			getDelegate().stampAll(itemName, value);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#subtract(lotus.domino.Base)
	 */
	@Override
	public void subtract(lotus.domino.Base entries) {
		try {
			getDelegate().subtract(toLotus(entries));
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#subtract(int)
	 */
	@Override
	public void subtract(int noteId) {
		try {
			getDelegate().subtract(noteId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#subtract(java.lang.String)
	 */
	@Override
	public void subtract(String noteId) {
		try {
			getDelegate().subtract(noteId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ViewEntryCollection#updateAll()
	 */
	@Override
	public void updateAll() {
		try {
			getDelegate().updateAll();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	public org.openntf.domino.Database getParentDatabase() {
		return getParent().getParentDatabase();
	}
}
