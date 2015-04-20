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

@SuppressWarnings("unused")
public class NoteMeta implements org.openntf.domino.big.NoteMeta {
	private long created_;
	private long lastModified_;
	private String form_;
	private final byte[] unid_ = new byte[16];
	private byte dbRef_;
	private int noteid_;
	private short formRef_;

	public NoteMeta() {
	}

	@Override
	public void readExternal(final ObjectInput arg0) throws IOException, ClassNotFoundException {

	}

	@Override
	public void writeExternal(final ObjectOutput arg0) throws IOException {
	}

	@Override
	public List<ItemMeta> getItemMetaList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemMeta getItemMeta(final String itemname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NoteType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUnid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReplid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNoteid() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasLocalItems() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Document getDocument(final Database db) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getDocument(final Session session, final String server) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getCreated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLastModified() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasReaders() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAuthors() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDocument(final Document doc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUnid(final String unid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setReplid(final String replid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNoteid(final int noteid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCreated(final Date created) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLastModified(final Date lastModified) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setReaders(final boolean readers) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAuthors(final boolean authors) {
		// TODO Auto-generated method stub

	}

}
