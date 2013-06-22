/**
 * 
 */
package org.openntf.domino.schema.types;

import java.util.logging.Logger;

/**
 * @author nfreeman
 * 
 */
public class EmailType extends AbstractDominoType {
	private static final Logger log_ = Logger.getLogger(EmailType.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public EmailType() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.schema.types.IDominoType#getUITypeName()
	 */
	@Override
	public String getUITypeName() {
		return "Email Address";
	}
}
