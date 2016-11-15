package org.openntf.domino.exceptions;

import org.openntf.domino.Document;

public class DocumentWriteAccessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DocumentWriteAccessException(final Document doc) {
		super("User " + doc.getAncestorSession().getEffectiveUserName() + " not authorized to write to document " + doc.getUniversalID()
				+ " in database " + doc.getAncestorDatabase().getApiPath());
	}

}
