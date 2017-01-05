package org.openntf.domino.big;

import java.util.Date;

import org.openntf.domino.ext.Database;
import org.openntf.domino.ext.Document;
import org.openntf.domino.ext.Session;

public interface NoteMeta extends BaseMeta {
	public java.util.List<ItemMeta> getItemMetaList();

	public ItemMeta getItemMeta(String itemname);

	public String getForm();

	public NoteType getType();

	public String getUnid();

	public String getReplid();

	public int getNoteid();

	public boolean hasLocalItems();

	public Document getDocument(Database db);

	public Document getDocument(Session session, String server);

	public Date getCreated();

	public Date getLastModified();

	public boolean hasReaders();

	public boolean hasAuthors();

	public void setDocument(Document doc);

	public void setUnid(String unid);

	public void setReplid(String replid);

	public void setNoteid(int noteid);

	public void setCreated(Date created);

	public void setLastModified(Date lastModified);

	public void setReaders(boolean readers);

	public void setAuthors(boolean authors);
}
