package org.openntf.domino.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.openntf.domino.HasExceptionDetails;

public class OpenNTFNotesException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OpenNTFNotesException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public OpenNTFNotesException(final Throwable cause) {
		super(cause);
	}

	private transient HasExceptionDetails myHED;

	public HasExceptionDetails getHED() {
		return myHED;
	}

	public void setHED(final HasExceptionDetails hed) {
		this.myHED = hed;
	}

	public OpenNTFNotesException(final String message, final Throwable cause, final HasExceptionDetails hed) {
		this(message, cause);
		myHED = hed;
	}

	public OpenNTFNotesException(final Throwable cause, final HasExceptionDetails hed) {
		this(cause);
		myHED = hed;
	}

	public List<String> getExceptionDetails() {
		if (myHED == null)
			return null;
		ArrayList<String> ret = new ArrayList<String>();
		myHED.fillExceptionDetails(ret);
		return ret;
	}

}