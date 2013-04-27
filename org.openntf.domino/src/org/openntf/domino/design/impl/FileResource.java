package org.openntf.domino.design.impl;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.impl.Database;

public class FileResource implements org.openntf.domino.design.FileResource {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(FileResource.class.getName());
	private Document document_;
	private Database parent_;

	public FileResource(final Document document, final Database parent) {
		parent_ = parent;
		document_ = document;
	}

	@Override
	public Document getDocument() {
		// I don't know if it'll matter whether or not I create a new Document instance or wrap the delegate
		return document_;
	}

	public InputStream getInputStream() {
		return document_.getFirstItem("$FileData").getInputStream();
	}

	public String getMimeType() {
		return document_.getItemValueString("$MimeType");
	}

	@Override
	public String getName() {
		return document_.getItemValueString("$TITLE");
	}

	@Override
	public String getNoteID() {
		return document_.getNoteID();
	}

	@Override
	public String getUniversalID() {
		return document_.getUniversalID();
	}

	public boolean isHideFromWeb() {
		return getFlags().contains("w");
	}

	public boolean isHideFromNotes() {
		return getFlags().contains("n");
	}

	@Override
	public boolean isNeedsRefresh() {
		return getFlags().contains("$");
	}

	@Override
	public boolean isPreventChanges() {
		return getFlags().contains("P");
	}

	@Override
	public boolean isPropagatePreventChanges() {
		return getFlags().contains("r");
	}

	@Override
	public boolean isReadOnly() {
		return getFlags().contains("&");
	}

	private String getFlags() {
		return document_.getItemValueString("$Flags");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public Database getAncestorDatabase() {
		return parent_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getAncestorDatabase().getAncestorSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#getAliases()
	 */
	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return null;
	}
}
