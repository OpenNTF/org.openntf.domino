package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class ViewNavigator extends Base<org.openntf.domino.ViewNavigator, lotus.domino.ViewNavigator> implements
		org.openntf.domino.ViewNavigator {

	public ViewNavigator(lotus.domino.ViewNavigator delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public int getBufferMaxEntries() {
		return getDelegate().getBufferMaxEntries();
	}

	@Override
	public int getCacheSize() {
		return getDelegate().getCacheSize();
	}

	@Override
	public ViewEntry getChild() {
		try {
			return Factory.fromLotus(getDelegate().getChild(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getChild(lotus.domino.ViewEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getChild((lotus.domino.ViewEntry) toLotus(entry)), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getCount() {
		return getDelegate().getCount();
	}

	@Override
	public ViewEntry getCurrent() {
		try {
			return Factory.fromLotus(getDelegate().getCurrent(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getEntryOptions() {
		return getDelegate().getEntryOptions();
	}

	@Override
	public ViewEntry getFirst() {
		try {
			return Factory.fromLotus(getDelegate().getFirst(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getFirstDocument() {
		try {
			return Factory.fromLotus(getDelegate().getFirstDocument(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getLast() {
		try {
			return Factory.fromLotus(getDelegate().getLast(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getLastDocument() {
		try {
			return Factory.fromLotus(getDelegate().getLastDocument(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getMaxLevel() {
		return getDelegate().getMaxLevel();
	}

	@Override
	public ViewEntry getNext() {
		try {
			return Factory.fromLotus(getDelegate().getNext(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getNext(lotus.domino.ViewEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getNext((lotus.domino.ViewEntry) toLotus(entry)), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getNextCategory() {
		try {
			return Factory.fromLotus(getDelegate().getNextCategory(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getNextDocument() {
		try {
			return Factory.fromLotus(getDelegate().getNextDocument(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getNextSibling() {
		try {
			return Factory.fromLotus(getDelegate().getNextSibling(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getNextSibling(lotus.domino.ViewEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getNextSibling((lotus.domino.ViewEntry) toLotus(entry)), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getNth(int n) {
		try {
			return Factory.fromLotus(getDelegate().getNth(n), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getParent() {
		try {
			return Factory.fromLotus(getDelegate().getParent(), ViewEntry.class, this);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	@Override
	public ViewEntry getParent(lotus.domino.ViewEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getParent((lotus.domino.ViewEntry) toLotus(entry)), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public View getParentView() {
		return (View) super.getParent();
	}

	@Override
	public ViewEntry getPos(String pos, char separator) {
		try {
			return Factory.fromLotus(getDelegate().getPos(pos, separator), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getPrev() {
		try {
			return Factory.fromLotus(getDelegate().getPrev(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getPrev(lotus.domino.ViewEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getPrev((lotus.domino.ViewEntry) toLotus(entry)), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getPrevCategory() {
		try {
			return Factory.fromLotus(getDelegate().getPrevCategory(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getPrevDocument() {
		try {
			return Factory.fromLotus(getDelegate().getPrevDocument(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getPrevSibling() {
		try {
			return Factory.fromLotus(getDelegate().getPrevSibling(), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ViewEntry getPrevSibling(lotus.domino.ViewEntry entry) {
		try {
			return Factory.fromLotus(getDelegate().getPrevSibling((lotus.domino.ViewEntry) entry), ViewEntry.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean gotoChild() {
		try {
			return getDelegate().gotoChild();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoChild(lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoChild((lotus.domino.ViewEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoEntry(Object entry) {
		try {
			return getDelegate().gotoEntry(toLotus((lotus.domino.Base) entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoFirst() {
		try {
			return getDelegate().gotoFirst();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoFirstDocument() {
		try {
			return getDelegate().gotoFirstDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoLast() {
		try {
			return getDelegate().gotoLast();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoLastDocument() {
		try {
			return getDelegate().gotoLastDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoNext() {
		try {
			return getDelegate().gotoNext();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoNext(lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoNext((lotus.domino.ViewEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoNextCategory() {
		try {
			return getDelegate().gotoNextCategory();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoNextDocument() {
		try {
			return getDelegate().gotoNextDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoNextSibling() {
		try {
			return getDelegate().gotoNextSibling();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoNextSibling(lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoNextSibling((lotus.domino.ViewEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoParent() {
		try {
			return getDelegate().gotoParent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoParent(lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoParent((lotus.domino.ViewEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoPos(String pos, char separator) {
		try {
			return getDelegate().gotoPos(pos, separator);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoPrev() {
		try {
			return getDelegate().gotoPrev();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoPrev(lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoPrev((lotus.domino.ViewEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoPrevCategory() {
		try {
			return getDelegate().gotoPrevCategory();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoPrevDocument() {
		try {
			return getDelegate().gotoPrevDocument();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoPrevSibling() {
		try {
			return getDelegate().gotoPrevSibling();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean gotoPrevSibling(lotus.domino.ViewEntry entry) {
		try {
			return getDelegate().gotoPrevSibling((lotus.domino.ViewEntry) toLotus(entry));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public void markAllRead() {
		try {
			getDelegate().markAllRead();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void markAllRead(String userName) {
		try {
			getDelegate().markAllRead(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void markAllUnread() {
		try {
			getDelegate().markAllUnread();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void markAllUnread(String userName) {
		try {
			getDelegate().markAllUnread(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setAutoExpandGuidance(int maxEntries, int[] collapsedNoteIds, int[] expandedNoteIds) {
		try {
			getDelegate().setAutoExpandGuidance(maxEntries, collapsedNoteIds, expandedNoteIds);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

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

	@Override
	public void setBufferMaxEntries(int entryCount) {
		try {
			getDelegate().setBufferMaxEntries(entryCount);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCacheGuidance(int maxEntries) {
		try {
			getDelegate().setCacheGuidance(maxEntries);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCacheGuidance(int maxEntries, int readMode) {
		try {
			getDelegate().setCacheGuidance(maxEntries, readMode);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCacheSize(int size) {
		try {
			getDelegate().setCacheSize(size);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setEntryOptions(int options) {
		try {
			getDelegate().setEntryOptions(options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setMaxLevel(int maxLevel) {
		try {
			getDelegate().setMaxLevel(maxLevel);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

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
