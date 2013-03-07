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
	public abstract Document getNextDocument(Document paramDocument);

	public abstract Document getPrevDocument(Document paramDocument);

	@Legacy(Legacy.ITERATION_WARNING)
	public abstract Document getNthDocument(int paramInt);

	@Legacy(Legacy.ITERATION_WARNING)
	public abstract Document getNextDocument();

	public abstract Document getPrevDocument();

	public abstract Document getDocument(Document paramDocument);

	public abstract void addDocument(Document paramDocument);

	public abstract void addDocument(Document paramDocument, boolean paramBoolean);

	public abstract void deleteDocument(Document paramDocument);

	public abstract void FTSearch(String paramString);

	public abstract void FTSearch(String paramString, int paramInt);

	public abstract boolean isSorted();

	public abstract void putAllInFolder(String paramString);

	public abstract void putAllInFolder(String paramString, boolean paramBoolean);

	public abstract void removeAll(boolean paramBoolean);

	public abstract void removeAllFromFolder(String paramString);

	public abstract void stampAll(String paramString, Object paramObject);

	public abstract void updateAll();

	public abstract DateTime getUntilTime();

	public abstract void markAllRead(String paramString);

	public abstract void markAllUnread(String paramString);

	public abstract void markAllRead();

	public abstract void markAllUnread();

	public abstract void intersect(int paramInt);

	public abstract void intersect(String paramString);

	public abstract void intersect(lotus.domino.Base paramBase);

	public abstract void merge(int paramInt);

	public abstract void merge(String paramString);

	public abstract void merge(lotus.domino.Base paramBase);

	public abstract void subtract(int paramInt);

	public abstract void subtract(String paramString);

	public abstract void subtract(lotus.domino.Base paramBase);

	public abstract boolean contains(int paramInt);

	public abstract boolean contains(String paramString);

	public abstract boolean contains(lotus.domino.Base paramBase);

	public abstract DocumentCollection cloneCollection();

}
