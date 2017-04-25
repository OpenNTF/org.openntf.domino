package org.openntf.domino.logging;

import java.util.List;
import java.util.logging.LogRecord;

import org.openntf.domino.ExceptionDetails;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.utils.Factory;

/**
 * Used when there is a throwable attached to the LogRecord being published which is an instance of a
 * {@link org.openntf.domino.exceptions.OpenNTFNotesException}. Such exceptions contain details about the environment (Notes user name,
 * current database and so on).
 */
public class LogRecordAdditionalInfo {

	private List<ExceptionDetails.Entry> exceptionDetails;
	private String[] lastWrappedDocs;

	/**
	 * Initializes instance of this class.
	 *
	 * @param logRecord
	 *            LogRecord containing a <code>Throwable</code> which is an instance of
	 *            {@link org.openntf.domino.exceptions.OpenNTFNotesException}
	 */
	public LogRecordAdditionalInfo(final LogRecord logRecord) {
		Throwable t = logRecord.getThrown();
		if (t != null && t instanceof OpenNTFNotesException) {
			exceptionDetails = ((OpenNTFNotesException) t).getExceptionDetails();
		}

		WrapperFactory wf = Factory.getWrapperFactory_unchecked();
		if (wf != null) {
			lastWrappedDocs = wf.getLastWrappedDocsInThread();
		}

	}

	/**
	 * @return All exception details provided by the OpenNTFNotesException attached to the LogRecord used to initialize this instance.
	 */
	public List<ExceptionDetails.Entry> getExceptionDetails() {
		return exceptionDetails;
	}

	public String[] getLastWrappedDocs() {
		return lastWrappedDocs;
	}

	/**
	 * Appends further exception details to the given <code>StringBuffer</code>. Used by the {@link LogFormatterConsoleDefault} and
	 * {@link LogFormatterFileDefault}.
	 *
	 * @param sb
	 *            StringBuffer to which to append all exception details and last wrapped documents.
	 */
	public void writeToLog(final StringBuffer sb) {
		if (exceptionDetails != null) {
			sb.append("    Details where exception was thrown:\n");
			for (ExceptionDetails.Entry exEntry : exceptionDetails) {
				sb.append("      " + exEntry.toString() + "\n");
			}
		}
		if (lastWrappedDocs != null) {
			sb.append("    Last wrapped docs in thread:\n");
			for (String lastWrappedDoc : lastWrappedDocs) {
				sb.append("      " + lastWrappedDoc + "\n");
			}
		}
	}

}
