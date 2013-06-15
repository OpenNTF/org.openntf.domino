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
	 * @see org.openntf.domino.design.DesignBase#setAlias(java.lang.String)
	 */
	@Override
	public void setAlias(String alias) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setAliases(java.lang.Iterable)
	 */
	@Override
	public void setAliases(Iterable<String> aliases) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.design.DesignBase#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub

	}
}
