package org.openntf.domino;

public interface ViewNavigator extends Base<lotus.domino.ViewNavigator>, lotus.domino.ViewNavigator {
	@Override
	public int getBufferMaxEntries();

	@Override
	public int getCacheSize();

	@Override
	public ViewEntry getChild();

	@Override
	public ViewEntry getChild(lotus.domino.ViewEntry entry);

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
	public ViewEntry getNext(lotus.domino.ViewEntry entry);

	@Override
	public ViewEntry getNextCategory();

	@Override
	public ViewEntry getNextDocument();

	@Override
	public ViewEntry getNextSibling();

	@Override
	public ViewEntry getNextSibling(lotus.domino.ViewEntry entry);

	@Override
	public ViewEntry getNth(int n);

	@Override
	public ViewEntry getParent();

	@Override
	public ViewEntry getParent(lotus.domino.ViewEntry entry);

	@Override
	public View getParentView();

	@Override
	public ViewEntry getPos(String pos, char separator);

	@Override
	public ViewEntry getPrev();

	@Override
	public ViewEntry getPrev(lotus.domino.ViewEntry entry);

	@Override
	public ViewEntry getPrevCategory();

	@Override
	public ViewEntry getPrevDocument();

	@Override
	public ViewEntry getPrevSibling();

	@Override
	public ViewEntry getPrevSibling(lotus.domino.ViewEntry entry);

	@Override
	public boolean gotoChild();

	@Override
	public boolean gotoChild(lotus.domino.ViewEntry entry);

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
	public boolean gotoNext(lotus.domino.ViewEntry entry);

	@Override
	public boolean gotoNextCategory();

	@Override
	public boolean gotoNextDocument();

	@Override
	public boolean gotoNextSibling();

	@Override
	public boolean gotoNextSibling(lotus.domino.ViewEntry entry);

	@Override
	public boolean gotoParent();

	@Override
	public boolean gotoParent(lotus.domino.ViewEntry entry);

	@Override
	public boolean gotoPos(String pos, char separator);

	@Override
	public boolean gotoPrev();

	@Override
	public boolean gotoPrev(lotus.domino.ViewEntry entry);

	@Override
	public boolean gotoPrevCategory();

	@Override
	public boolean gotoPrevDocument();

	@Override
	public boolean gotoPrevSibling();

	@Override
	public boolean gotoPrevSibling(lotus.domino.ViewEntry entry);

	@Override
	public void markAllRead();

	@Override
	public void markAllRead(String userName);

	@Override
	public void markAllUnread();

	@Override
	public void markAllUnread(String userName);

	@Override
	// TODO Figure out what these parameters are
	public void setAutoExpandGuidance(int arg0, int[] arg1, int[] arg2);

	@Override
	public void setAutoExpandGuidance(int maxEntries, lotus.domino.NoteCollection collapsedNoteIds,
			lotus.domino.NoteCollection expandedNoteIds);

	@Override
	public void setBufferMaxEntries(int entryCount);

	@Override
	// TODO Figure out what these parameters are
	public void setCacheGuidance(int arg0);

	@Override
	// TODO Figure out what these parameters are
	public void setCacheGuidance(int arg0, int arg1);

	@Override
	public void setCacheSize(int size);

	@Override
	public void setEntryOptions(int options);

	@Override
	public void setMaxLevel(int maxLevel);

	@Override
	public int skip(int count);
}
