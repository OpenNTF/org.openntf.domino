package org.openntf.domino.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.openntf.domino.ExceptionDetails;

public class OpenNTFNotesException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * @param cause
	 *            the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and
	 *            indicates that the cause is nonexistent or unknown.)
	 */

	public OpenNTFNotesException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 * @param cause
	 *            the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and
	 *            indicates that the cause is nonexistent or unknown.)
	 */
	public OpenNTFNotesException(final String message, final Throwable cause) {
		super(message, cause);
	}

	// in the most cases we will have only one exDetail.
	private transient ExceptionDetails exceptionDetails_;
	// the List is only used if we have more than once ExceptionDetalis
	private transient List<ExceptionDetails> moreExceptionDetails_;

	/**
	 * This method is called if we bubble up and reach an other object that implements also ExceptionDetails
	 * 
	 */
	public void addExceptionDetails(final ExceptionDetails ed) {
		if (exceptionDetails_ == null) {
			exceptionDetails_ = ed;
			return;
		}
		if (moreExceptionDetails_ == null) {
			moreExceptionDetails_ = new ArrayList<ExceptionDetails>();
		}
		moreExceptionDetails_.add(ed);

	}

	//public ExceptionDetails getExceptionDetails() {
	//	return exceptionDetails_;
	//}

	//public void setExceptionDetails(final ExceptionDetails ed) {
	//	this.exceptionDetails_ = ed;
	//}

	public OpenNTFNotesException(final String message, final Throwable cause, final ExceptionDetails ed) {
		this(message, cause);
		exceptionDetails_ = ed;
	}

	public OpenNTFNotesException(final Throwable cause, final ExceptionDetails ed) {
		this(cause);
		exceptionDetails_ = ed;
	}

	public List<ExceptionDetails.Entry> getExceptionDetails() {
		if (exceptionDetails_ == null)
			return null;
		ArrayList<ExceptionDetails.Entry> ret = new ArrayList<ExceptionDetails.Entry>();
		try {
			exceptionDetails_.fillExceptionDetails(ret);
		} catch (Throwable t) {
			// we will ignore ANY error that occurs while gathering more information (it could happen that the DB is closed now and you get stuck in a loop)
		}

		if (moreExceptionDetails_ != null) {
			for (ExceptionDetails med : moreExceptionDetails_) {
				try {
					med.fillExceptionDetails(ret);
				} catch (Throwable t) {

				}
			}
		}
		return ret;
	}

}