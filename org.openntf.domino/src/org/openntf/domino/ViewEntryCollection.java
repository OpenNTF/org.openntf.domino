package org.openntf.domino;

import lotus.domino.View;
import lotus.domino.ViewEntry;

public interface ViewEntryCollection extends Base<lotus.domino.ViewEntryCollection>, lotus.domino.ViewEntryCollection {
	@Override
	public void addEntry(Object obh);

	@Override
	public void addEntry(Object obj, boolean checkDups);

	@Override
	public lotus.domino.ViewEntryCollection cloneCollection();

	@Override
	public boolean contains(lotus.domino.Base obj);

	@Override
	public boolean contains(int noteid);

	@Override
	public boolean contains(String noteid);

	@Override
	public void deleteEntry(ViewEntry entry);

	@Override
	public void FTSearch(String query);

	@Override
	public void FTSearch(String query, int maxDocs);

	@Override
	public int getCount();

	@Override
	public ViewEntry getEntry(Object entry);

	@Override
	public ViewEntry getFirstEntry();

	@Override
	public ViewEntry getLastEntry();

	@Override
	public ViewEntry getNextEntry();

	@Override
	public ViewEntry getNextEntry(ViewEntry entry);

	@Override
	public ViewEntry getNthEntry(int n);

	@Override
	public View getParent();

	@Override
	public ViewEntry getPrevEntry();

	@Override
	public ViewEntry getPrevEntry(ViewEntry entry);

	@Override
	public String getQuery();

	@Override
	public void intersect(lotus.domino.Base obj);

	@Override
	public void intersect(int noteid);

	@Override
	public void intersect(String noteid);

	@Override
	public void markAllRead();

	@Override
	public void markAllRead(String userName);

	@Override
	public void markAllUnread();

	@Override
	public void markAllUnread(String userName);

	@Override
	public void merge(lotus.domino.Base obj);

	@Override
	public void merge(int noteid);

	@Override
	public void merge(String noteid);

	@Override
	public void putAllInFolder(String folderName);

	@Override
	public void putAllInFolder(String folderName, boolean createOnFail);

	@Override
	public void removeAll(boolean force);

	@Override
	public void removeAllFromFolder(String folderName);

	@Override
	public void stampAll(String itemName, Object value);

	@Override
	public void subtract(lotus.domino.Base obj);

	@Override
	public void subtract(int noteid);

	@Override
	public void subtract(String noteid);

	@Override
	public void updateAll();
}
