package org.openntf.domino.impl;

import java.util.Iterator;

import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;

import org.openntf.domino.iterators.ViewEntryIterator;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class ViewEntryCollection extends Base<org.openntf.domino.ViewEntryCollection, lotus.domino.ViewEntryCollection> implements
		org.openntf.domino.ViewEntryCollection {

	protected ViewEntryCollection(lotus.domino.ViewEntryCollection delegate) {
		super(delegate);
	}

	@Override
	public void addEntry(Object obj) {
		try {
			getDelegate().addEntry(obj);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addEntry(Object obj, boolean checkDups) {
		try {
			getDelegate().addEntry(obj, checkDups);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public lotus.domino.ViewEntryCollection cloneCollection() {
		try {
			org.openntf.domino.ViewEntryCollection result = Factory.fromLotus(getDelegate().cloneCollection(), ViewEntryCollection.class);
			return result;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return null;
		}
	}

	@Override
	public Iterator<ViewEntry> iterator() {
		return new ViewEntryIterator(this);
	}

	@Override
	public boolean contains(lotus.domino.Base entries) {
		try {
			boolean result = getDelegate().contains(entries);
			return result;
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return false;
		}
	}

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

	@Override
	public void deleteEntry(ViewEntry entry) {
		try {
			getDelegate().deleteEntry(entry);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void FTSearch(String query) {
		try {
			getDelegate().FTSearch(query);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void FTSearch(String query, int maxDocs) {
		try {
			getDelegate().FTSearch(query, maxDocs);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public int getCount() {
		try {
			return getDelegate().getCount();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
			return 0;
		}
	}

	@Override
	public ViewEntry getEntry(Object entry) {
		try {
			return Factory.fromLotus(getDelegate().getEntry(entry), org.openntf.domino.ViewEntry.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getFirstEntry() {
		try {
			return Factory.fromLotus(getDelegate().getFirstEntry(), org.openntf.domino.ViewEntry.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getLastEntry() {
		try {
			return Factory.fromLotus(getDelegate().getLastEntry(), org.openntf.domino.ViewEntry.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getNextEntry() {
		try {
			return Factory.fromLotus(getDelegate().getNextEntry(), org.openntf.domino.ViewEntry.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getNextEntry(ViewEntry entry) {
		try {
			ViewEntry result = Factory.fromLotus(getDelegate().getNextEntry(entry), org.openntf.domino.ViewEntry.class);
			entry.recycle();
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getNthEntry(int n) {
		try {
			return Factory.fromLotus(getDelegate().getNthEntry(n), org.openntf.domino.ViewEntry.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public View getParent() {
		try {
			return Factory.fromLotus(getDelegate().getParent(), org.openntf.domino.View.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getPrevEntry() {
		try {
			return Factory.fromLotus(getDelegate().getPrevEntry(), org.openntf.domino.ViewEntry.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getPrevEntry(ViewEntry entry) {
		try {
			ViewEntry result = Factory.fromLotus(getDelegate().getPrevEntry(entry), org.openntf.domino.ViewEntry.class);
			entry.recycle();
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getQuery() {
		try {
			return getDelegate().getQuery();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void intersect(lotus.domino.Base entries) {
		try {
			getDelegate().intersect(entries);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void intersect(int noteId) {
		try {
			getDelegate().intersect(noteId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void intersect(String noteId) {
		try {
			getDelegate().intersect(noteId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void markAllRead() {
		try {
			getDelegate().markAllRead();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void markAllRead(String userName) {
		try {
			getDelegate().markAllUnread(userName);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void markAllUnread() {
		try {
			getDelegate().markAllUnread();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void markAllUnread(String userName) {
		try {
			getDelegate().markAllUnread(userName);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void merge(lotus.domino.Base entries) {
		try {
			getDelegate().merge(entries);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void merge(int noteId) {
		try {
			getDelegate().merge(noteId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void merge(String noteId) {
		try {
			getDelegate().merge(noteId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void putAllInFolder(String folderName) {
		try {
			getDelegate().putAllInFolder(folderName);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void putAllInFolder(String folderName, boolean createOnFail) {
		try {
			getDelegate().putAllInFolder(folderName, createOnFail);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void removeAll(boolean force) {
		try {
			getDelegate().removeAll(force);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void removeAllFromFolder(String folderName) {
		try {
			getDelegate().removeAllFromFolder(folderName);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void stampAll(String itemName, Object value) {
		try {
			getDelegate().stampAll(itemName, value);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void subtract(lotus.domino.Base entries) {
		try {
			getDelegate().subtract(entries);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void subtract(int noteId) {
		try {
			getDelegate().subtract(noteId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void subtract(String noteId) {
		try {
			getDelegate().subtract(noteId);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

	@Override
	public void updateAll() {
		try {
			getDelegate().updateAll();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
	}

}
