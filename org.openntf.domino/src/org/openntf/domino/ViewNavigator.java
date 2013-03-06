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
	public ViewEntry getChild(ViewEntry arg0);

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
	public ViewEntry getNext(ViewEntry arg0);

	@Override
	public ViewEntry getNextCategory();

	@Override
	public ViewEntry getNextDocument();

	@Override
	public ViewEntry getNextSibling();

	@Override
	public ViewEntry getNextSibling(ViewEntry arg0);

	@Override
	public ViewEntry getNth(int arg0);

	@Override
	public ViewEntry getParent();

	@Override
	public ViewEntry getParent(ViewEntry arg0);

	@Override
	public View getParentView();

	@Override
	public ViewEntry getPos(String arg0, char arg1);

	@Override
	public ViewEntry getPrev();

	@Override
	public ViewEntry getPrev(ViewEntry arg0);

	@Override
	public ViewEntry getPrevCategory();

	@Override
	public ViewEntry getPrevDocument();

	@Override
	public ViewEntry getPrevSibling();

	@Override
	public ViewEntry getPrevSibling(ViewEntry arg0);

	@Override
	public boolean gotoChild();

	@Override
	public boolean gotoChild(ViewEntry arg0);

	@Override
	public boolean gotoEntry(Object arg0);

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
	public boolean gotoNext(ViewEntry arg0);

	@Override
	public boolean gotoNextCategory();

	@Override
	public boolean gotoNextDocument();

	@Override
	public boolean gotoNextSibling();

	@Override
	public boolean gotoNextSibling(ViewEntry arg0);

	@Override
	public boolean gotoParent();

	@Override
	public boolean gotoParent(ViewEntry arg0);

	@Override
	public boolean gotoPos(String arg0, char arg1);

	@Override
	public boolean gotoPrev();

	@Override
	public boolean gotoPrev(ViewEntry arg0);

	@Override
	public boolean gotoPrevCategory();

	@Override
	public boolean gotoPrevDocument();

	@Override
	public boolean gotoPrevSibling();

	@Override
	public boolean gotoPrevSibling(ViewEntry arg0);

	@Override
	public void markAllRead();

	@Override
	public void markAllRead(String arg0);

	@Override
	public void markAllUnread();

	@Override
	public void markAllUnread(String arg0);

	@Override
	public void setAutoExpandGuidance(int arg0, int[] arg1, int[] arg2);

	@Override
	public void setAutoExpandGuidance(int arg0, NoteCollection arg1, NoteCollection arg2);

	@Override
	public void setBufferMaxEntries(int arg0);

	@Override
	public void setCacheGuidance(int arg0);

	@Override
	public void setCacheGuidance(int arg0, int arg1);

	@Override
	public void setCacheSize(int arg0);

	@Override
	public void setEntryOptions(int arg0);

	@Override
	public void setMaxLevel(int arg0);

	@Override
	public int skip(int arg0);
}
