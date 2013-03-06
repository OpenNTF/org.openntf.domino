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
}
