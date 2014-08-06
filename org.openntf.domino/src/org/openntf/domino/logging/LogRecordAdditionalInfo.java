package org.openntf.domino.logging;

import java.util.List;
import java.util.logging.LogRecord;

import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.utils.Factory;

public class LogRecordAdditionalInfo {

	private List<String> exceptionDetails;
	private String[] lastWrappedDocs;

	public LogRecordAdditionalInfo(final LogRecord logRecord) {
		Throwable t = logRecord.getThrown();
		if (t != null && t instanceof OpenNTFNotesException)
			exceptionDetails = ((OpenNTFNotesException) t).getExceptionDetails();
		lastWrappedDocs = Factory.getWrapperFactory().getLastWrappedDocsInThread();
	}

	public List<String> getExceptionDetails() {
		return exceptionDetails;
	}

	public String[] getLastWrappedDocs() {
		return lastWrappedDocs;
	}

	public void writeToLog(final StringBuffer sb) {
		if (exceptionDetails != null) {
			sb.append("    Details where exception was thrown:\n");
			for (String s : exceptionDetails)
				sb.append("      " + s + "\n");
		} else if (lastWrappedDocs != null) {
			sb.append("    Last wrapped docs in thread:\n");
			for (int i = 0; i < lastWrappedDocs.length; i++)
				sb.append("      " + lastWrappedDocs[i] + "\n");
		}
	}

}
