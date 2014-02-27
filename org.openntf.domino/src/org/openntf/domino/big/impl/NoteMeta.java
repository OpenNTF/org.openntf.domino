package org.openntf.domino.big.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.List;

import org.openntf.domino.big.ItemMeta;
import org.openntf.domino.big.NoteType;
import org.openntf.domino.ext.Database;
import org.openntf.domino.ext.Document;
import org.openntf.domino.ext.Session;

public class NoteMeta implements org.openntf.domino.big.NoteMeta {
	private long created_;
	private long lastModified_;
	private String form_;
	private final byte[] unid_ = new byte[16];
	private byte dbRef_;
	private int noteid_;
	private short formRef_;

	public NoteMeta() {
		// TODO Auto-generated constructor stub
	}

	public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	public void writeExternal(final ObjectOutput arg0) throws IOException {
		// TODO Auto-generated method stub
	}

	public List<ItemMeta> getItemMetaList() {
		// TODO Auto-generated method stub
		return null;
	}

	public ItemMeta getItemMeta(final String itemname) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getForm() {
		// TODO Auto-generated method stub
		return null;
	}

	public NoteType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUnid() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getReplid() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getNoteid() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean hasLocalItems() {
		// TODO Auto-generated method stub
		return false;
	}

	public Document getDocument(final Database db) {
		// TODO Auto-generated method stub
		return null;
	}

	public Document getDocument(final Session session, final String server) {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getCreated() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getLastModified() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasReaders() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasAuthors() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setDocument(final Document doc) {
		// TODO Auto-generated method stub

	}

	public void setUnid(final String unid) {
		// TODO Auto-generated method stub

	}

	public void setReplid(final String replid) {
		// TODO Auto-generated method stub

	}

	public void setNoteid(final int noteid) {
		// TODO Auto-generated method stub

	}

	public void setCreated(final Date created) {
		// TODO Auto-generated method stub

	}

	public void setLastModified(final Date lastModified) {
		// TODO Auto-generated method stub

	}

	public void setReaders(final boolean readers) {
		// TODO Auto-generated method stub

	}

	public void setAuthors(final boolean authors) {
		// TODO Auto-generated method stub

	}

}
