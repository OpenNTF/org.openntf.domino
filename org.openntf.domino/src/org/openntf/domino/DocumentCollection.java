package org.openntf.domino;

import lotus.domino.Database;
import lotus.domino.Document;

import org.openntf.domino.annotations.Legacy;

public interface DocumentCollection extends lotus.domino.DocumentCollection, org.openntf.domino.Base<lotus.domino.DocumentCollection> {
	public abstract int getCount();

	public abstract String getQuery();

	public abstract Database getParent();

	public abstract Document getFirstDocument();

	public abstract Document getLastDocument();

	@Legacy(Legacy.ITERATION_WARNING)
	public abstract Document getNextDocument(Document doc);

	public abstract Document getPrevDocument(Document doc);

	@Legacy(Legacy.ITERATION_WARNING)
	public abstract Document getNthDocument(int n);

	@Legacy(Legacy.ITERATION_WARNING)
	public abstract Document getNextDocument();

	public abstract Document getPrevDocument();

	public abstract Document getDocument(Document doc);

	public abstract void addDocument(Document doc);

	public abstract void addDocument(Document doc, boolean checkDups);

	public abstract void deleteDocument(Document doc);

	public abstract void FTSearch(String query);

	public abstract void FTSearch(String query, int maxDocs);

	public abstract boolean isSorted();

	public abstract void putAllInFolder(String folderName);

	public abstract void putAllInFolder(String folderName, boolean createOnFail);

	public abstract void removeAll(boolean force);

	public abstract void removeAllFromFolder(String folderName);

	public abstract void stampAll(String itemName, Object value);

	public abstract void updateAll();

	public abstract DateTime getUntilTime();

	public abstract void markAllRead(String userName);

	public abstract void markAllUnread(String userName);

	public abstract void markAllRead();

	public abstract void markAllUnread();

	public abstract void intersect(int noteid);

	public abstract void intersect(String noteid);

	public abstract void intersect(lotus.domino.Base doc);

	public abstract void merge(int noteid);

	public abstract void merge(String noteid);

	public abstract void merge(lotus.domino.Base doc);

	public abstract void subtract(int noteid);

	public abstract void subtract(String noteid);

	public abstract void subtract(lotus.domino.Base doc);

	public abstract boolean contains(int noteid);

	public abstract boolean contains(String noteid);

	public abstract boolean contains(lotus.domino.Base doc);

	public abstract DocumentCollection cloneCollection();

}
