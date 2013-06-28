/**
 * 
 */
package org.openntf.domino.design.impl;

import java.util.logging.Logger;

import org.openntf.domino.Document;

/**
 * @author jgallagher
 * 
 */
public class DesignForm extends AbstractDesignBaseNamed implements org.openntf.domino.design.DesignForm {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DesignForm.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @param document
	 */
	protected DesignForm(final Document document) {
		super(document);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignForm#getFields()
	 */
	@Override
	public FormFieldList getFields() {
		return new FormFieldList(this, "//field");
	}
}
