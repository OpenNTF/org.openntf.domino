/**
 * 
 */
package org.openntf.domino.design.impl;

import org.openntf.domino.Document;

/**
 * @author jgallagher
 * 
 */
enum DesignFactory {
	INSTANCE;

	private DesignFactory() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromDocument(Document doc, Class<? extends org.openntf.domino.design.DesignBase> T) {
		if (T == ACL.class) {
			return (T) (new ACL(doc));
		} else if (T == AboutDocument.class) {
			return (T) (new AboutDocument(doc));
		} else if (T == FileResource.class) {
			return (T) (new FileResource(doc));
		} else if (T == Form.class) {
			return (T) (new Form(doc));
		} else if (T == IconNote.class) {
			return (T) (new IconNote(doc));
		} else if (T == ReplicationFormula.class) {
			return (T) (new ReplicationFormula(doc));
		} else if (T == UsingDocument.class) {
			return (T) (new UsingDocument(doc));
		} else if (T == View.class) {
			return (T) (new View(doc));
		}
		return null;
	}
}
