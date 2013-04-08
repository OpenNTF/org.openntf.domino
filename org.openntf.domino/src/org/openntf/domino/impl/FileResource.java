package org.openntf.domino.impl;

import java.io.InputStream;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;

public class FileResource extends Base<org.openntf.domino.FileResource, lotus.domino.Base> implements org.openntf.domino.FileResource {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(FileResource.class.getName());
	private Document document_;

	protected FileResource(lotus.domino.Document delegate, Database parent) {
		super(delegate, parent);
		document_ = Factory.fromLotus(delegate, Document.class, parent);
	}

	@Override
	public Document getDocument() {
		// I don't know if it'll matter whether or not I create a new Document instance or wrap the delegate
		return this.getParent().getDocumentByUNID(document_.getUniversalID());
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
	public Database getParent() {
		return (Database) super.getParent();
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
		return this.getParent();
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
}
