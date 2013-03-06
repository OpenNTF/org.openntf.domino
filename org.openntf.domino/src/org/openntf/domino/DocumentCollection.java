package org.openntf.domino;

import lotus.domino.Database;
import org.openntf.domino.DateTime;
import lotus.domino.Document;

public interface DocumentCollection extends Base<lotus.domino.DocumentCollection>, lotus.domino.DocumentCollection {

	@Override
	public void addDocument(Document arg0);

	@Override
	public void addDocument(Document arg0, boolean arg1);

	@Override
	public lotus.domino.DocumentCollection cloneCollection();

	@Override
	public boolean contains(lotus.domino.Base arg0);

	@Override
	public boolean contains(int arg0);

	@Override
	public boolean contains(String arg0);

	@Override
	public void deleteDocument(Document arg0);

	@Override
	public void FTSearch(String arg0);

	@Override
	public void FTSearch(String arg0, int arg1);

	@Override
	public int getCount();

	@Override
	public Document getDocument(Document arg0);

	@Override
	public Document getFirstDocument();

	@Override
	public Document getLastDocument();

	@Override
	public Document getNextDocument();

	@Override
	public Document getNextDocument(Document arg0);

	@Override
	public Document getNthDocument(int arg0);

	@Override
	public Database getParent();

	@Override
	public Document getPrevDocument();

	@Override
	public Document getPrevDocument(Document arg0);

	@Override
	public String getQuery();

	@Override
	public DateTime getUntilTime();

	@Override
	public void intersect(lotus.domino.Base arg0);

	@Override
	public void intersect(int arg0);

	@Override
	public void intersect(String arg0);

	@Override
	public boolean isSorted();

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
=======
import lotus.domino.Base;
import lotus.domino.Database;
import lotus.domino.DateTime;
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

	public abstract void intersect(Base paramBase);

	public abstract void merge(int paramInt);

	public abstract void merge(String paramString);

	public abstract void merge(Base paramBase);

	public abstract void subtract(int paramInt);

	public abstract void subtract(String paramString);

	public abstract void subtract(Base paramBase);

	public abstract boolean contains(int paramInt);

	public abstract boolean contains(String paramString);

	public abstract boolean contains(Base paramBase);

	public abstract DocumentCollection cloneCollection();

}
