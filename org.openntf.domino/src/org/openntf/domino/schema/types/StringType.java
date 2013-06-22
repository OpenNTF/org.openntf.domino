/**
 * 
 */
package org.openntf.domino.schema.types;

import java.util.logging.Logger;

/**
 * @author nfreeman
 * 
 */
public class StringType extends AbstractDominoType {
	private static final Logger log_ = Logger.getLogger(StringType.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public StringType() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.schema.types.IDominoType#getUITypeName()
	 */
	@Override
	public String getUITypeName() {
		// TODO allow for i18n translation via properties files
		return "Text";
	}
}
