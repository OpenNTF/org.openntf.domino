package org.openntf.domino.design.sync;

import java.util.Date;

import org.openntf.domino.Document;

public class DocumentWrapper {
	Document document_;

	public DocumentWrapper(final Document document) {
		document_ = document;
	}

	public Document getDocument() {
		return document_;
	}

	public void setDocument(final Document document) {
		document_ = document;
	}

	public Date getLastModifiedDate() {
		return document_.getLastModifiedDate();
	}

	@Override
	public String toString() {
		if (document_ == null) {
			return "null";
		}
		return "Form='" + document_.getFormName() + "' [" + document_.getUniversalID() + "]";
	}
}
