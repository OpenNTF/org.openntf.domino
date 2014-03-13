/**
 * 
 */
package org.openntf.domino.exceptions;

import java.util.logging.Logger;

/**
 * @author Nathan T. Freeman
 * 
 */
public class IHaveNoIdeaHowThisHappenedException extends RuntimeException {
	private static final Logger log_ = Logger.getLogger(IHaveNoIdeaHowThisHappenedException.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public IHaveNoIdeaHowThisHappenedException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public IHaveNoIdeaHowThisHappenedException(final String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public IHaveNoIdeaHowThisHappenedException(final Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public IHaveNoIdeaHowThisHappenedException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
}
