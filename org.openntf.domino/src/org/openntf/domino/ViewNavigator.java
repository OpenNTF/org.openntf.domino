package org.openntf.domino;

import lotus.domino.NoteCollection;
import lotus.domino.View;
import lotus.domino.ViewEntry;

public interface ViewNavigator extends Base<lotus.domino.ViewNavigator>, lotus.domino.ViewNavigator {
	@Override
	public int getBufferMaxEntries();

	@Override
	public int getCacheSize();

	@Override
	public ViewEntry getChild();

	@Override
	public ViewEntry getChild(ViewEntry entry);

	@Override
	public int getCount();

	@Override
	public ViewEntry getCurrent();

	@Override
	public int getEntryOptions();

	@Override
	public ViewEntry getFirst();

	@Override
	public ViewEntry getFirstDocument();

	@Override
	public ViewEntry getLast();

	@Override
	public ViewEntry getLastDocument();

	@Override
	public int getMaxLevel();

	@Override
	public ViewEntry getNext();

	@Override
	public ViewEntry getNext(ViewEntry entry);

	@Override
	public ViewEntry getNextCategory();

	@Override
	public ViewEntry getNextDocument();

	@Override
	public ViewEntry getNextSibling();

	@Override
	public ViewEntry getNextSibling(ViewEntry entry);

	@Override
	public ViewEntry getNth(int n);

	@Override
	public ViewEntry getParent();

	@Override
	public ViewEntry getParent(ViewEntry entry);

	@Override
	public View getParentView();

	@Override
	public ViewEntry getPos(String pos, char separator);

	@Override
	public ViewEntry getPrev();

	@Override
	public ViewEntry getPrev(ViewEntry entry);

	@Override
	public ViewEntry getPrevCategory();

	@Override
	public ViewEntry getPrevDocument();

	@Override
	public ViewEntry getPrevSibling();

	@Override
	public ViewEntry getPrevSibling(ViewEntry entry);

	@Override
	public boolean gotoChild();

	@Override
	public boolean gotoChild(ViewEntry entry);

	@Override
	public boolean gotoEntry(Object entry);

	@Override
	public boolean gotoFirst();

	@Override
	public boolean gotoFirstDocument();

	@Override
	public boolean gotoLast();

	@Override
	public boolean gotoLastDocument();

	@Override
	public boolean gotoNext();

	@Override
	public boolean gotoNext(ViewEntry entry);

	@Override
	public boolean gotoNextCategory();

	@Override
	public boolean gotoNextDocument();

	@Override
	public boolean gotoNextSibling();

	@Override
	public boolean gotoNextSibling(ViewEntry entry);

	@Override
	public boolean gotoParent();

	@Override
	public boolean gotoParent(ViewEntry entry);

	@Override
	public boolean gotoPos(String pos, char separator);

	@Override
	public boolean gotoPrev();

	@Override
	public boolean gotoPrev(ViewEntry entry);

	@Override
	public boolean gotoPrevCategory();

	@Override
	public boolean gotoPrevDocument();

	@Override
	public boolean gotoPrevSibling();

	@Override
	public boolean gotoPrevSibling(ViewEntry entry);

	@Override
	public void markAllRead();

	@Override
	public void markAllRead(String userName);

	@Override
	public void markAllUnread();

	@Override
	public void markAllUnread(String userName);

	@Override
	public void setAutoExpandGuidance(int maxEntries, int[] collapsedNoteIds, int[] expandedNoteIds);

	@Override
	public void setAutoExpandGuidance(int maxEntries, NoteCollection collapsedNoteIds, NoteCollection expandedNoteIds);

	@Override
	public void setBufferMaxEntries(int entryCount);

	@Override
	public void setCacheGuidance(int maxEntries);

	@Override
	public void setCacheGuidance(int maxEntries, int readMode);

	@Override
	public void setCacheSize(int size);

	@Override
	public void setEntryOptions(int options);

	@Override
	public void setMaxLevel(int maxLevel);

	@Override
	public int skip(int count);
}
