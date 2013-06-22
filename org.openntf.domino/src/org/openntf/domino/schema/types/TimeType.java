/**
 * 
 */
package org.openntf.domino.schema.types;

import java.util.logging.Logger;

/**
 * @author nfreeman
 * 
 */
public class TimeType extends AbstractDominoType {
	private static final Logger log_ = Logger.getLogger(TimeType.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public TimeType() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.schema.types.IDominoType#getUITypeName()
	 */
	@Override
	public String getUITypeName() {
		return "Time";
	}
}
