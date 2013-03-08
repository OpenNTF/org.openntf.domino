package org.openntf.domino;

import lotus.domino.View;
import lotus.domino.ViewEntry;

import org.openntf.domino.annotations.Legacy;

public interface ViewEntryCollection extends Base<lotus.domino.ViewEntryCollection>, lotus.domino.ViewEntryCollection,
		Iterable<lotus.domino.ViewEntry> {
	@Override
	public void addEntry(Object arg0);

	@Override
	public void addEntry(Object arg0, boolean arg1);

	@Override
	public lotus.domino.ViewEntryCollection cloneCollection();

	@Override
	public boolean contains(lotus.domino.Base arg0);

	@Override
	public boolean contains(int arg0);

	@Override
	public boolean contains(String arg0);

	@Override
	public void deleteEntry(ViewEntry arg0);

	@Override
	public void FTSearch(String arg0);

	@Override
	public void FTSearch(String arg0, int arg1);

	@Override
	public int getCount();

	@Override
	public ViewEntry getEntry(Object arg0);

	@Override
	public ViewEntry getFirstEntry();

	@Override
	public ViewEntry getLastEntry();

	@Override
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public ViewEntry getNextEntry();

	@Override
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public ViewEntry getNextEntry(ViewEntry arg0);

	@Override
	@Deprecated
	@Legacy(Legacy.ITERATION_WARNING)
	public ViewEntry getNthEntry(int arg0);

	@Override
	public View getParent();

	@Override
	public ViewEntry getPrevEntry();

	@Override
	public ViewEntry getPrevEntry(ViewEntry arg0);

	@Override
	public String getQuery();

	@Override
	public void intersect(lotus.domino.Base arg0);

	@Override
	public void intersect(int arg0);

	@Override
	public void intersect(String arg0);

	@Override
	public void markAllRead();

	@Override
	public void markAllRead(String arg0);

	@Override
	public void markAllUnread();

	@Override
	public void markAllUnread(String arg0);

	@Override
	public void merge(lotus.domino.Base arg0);

	@Override
	public void merge(int arg0);

	@Override
	public void merge(String arg0);

	@Override
	public void putAllInFolder(String arg0);

	@Override
	public void putAllInFolder(String arg0, boolean arg1);

	@Override
	public void removeAll(boolean arg0);

	@Override
	public void removeAllFromFolder(String arg0);

	@Override
	public void stampAll(String arg0, Object arg1);

	@Override
	public void subtract(lotus.domino.Base arg0);

	@Override
	public void subtract(int arg0);

	@Override
	public void subtract(String arg0);

	@Override
	public void updateAll();
}
