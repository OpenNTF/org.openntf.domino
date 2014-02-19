/**
 * 
 */
package org.openntf.domino.schema.types;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.openntf.domino.schema.exceptions.ItemException;

/**
 * @author nfreeman
 * 
 */
public class EmailType extends StringType {
	private static final Logger log_ = Logger.getLogger(EmailType.class.getName());
	private static final long serialVersionUID = 1L;
	public static final Pattern EMAIL_REGEX = Pattern.compile("\\\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,4}\\\\b",
			Pattern.CASE_INSENSITIVE);

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

	/* (non-Javadoc)
	 * @see org.openntf.domino.schema.types.AbstractDominoType#validateValue(java.lang.Object)
	 */
	@Override
	public boolean validateValue(final Object value) throws ItemException {
		if (super.validateValue(value)) {
			return EMAIL_REGEX.matcher((CharSequence) value).matches();
		}
		return false;
	}
}
