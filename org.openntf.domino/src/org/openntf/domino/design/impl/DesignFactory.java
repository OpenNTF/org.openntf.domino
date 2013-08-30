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
	public static <T> T fromDocument(final Document doc, final Class<? extends org.openntf.domino.design.DesignBase> T) {
		if (T == ACLNote.class) {
			return (T) (new ACLNote(doc));
		} else if (T == AboutDocument.class) {
			return (T) (new AboutDocument(doc));
		} else if (T == FileResource.class) {
			return (T) (new FileResource(doc));
		} else if (T == DesignForm.class) {
			return (T) (new DesignForm(doc));
		} else if (T == IconNote.class) {
			return (T) (new IconNote(doc));
		} else if (T == ReplicationFormula.class) {
			return (T) (new ReplicationFormula(doc));
		} else if (T == UsingDocument.class) {
			return (T) (new UsingDocument(doc));
		} else if (T == DesignView.class) {
			return (T) (new DesignView(doc));
		} else if (T == JavaResource.class) {
			return (T) (new JavaResource(doc));
		} else if (T == JarResource.class) {
			return (T) (new JarResource(doc));
		} else if (T == XPage.class) {
			return (T) (new XPage(doc));
		}
		return null;
	}
}
