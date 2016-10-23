package org.openntf.domino.logging;

import java.util.List;
import java.util.logging.LogRecord;

import org.openntf.domino.ExceptionDetails;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.utils.Factory;

public class LogRecordAdditionalInfo {

	private List<ExceptionDetails.Entry> exceptionDetails;
	private String[] lastWrappedDocs;

	public LogRecordAdditionalInfo(final LogRecord logRecord) {
		Throwable t = logRecord.getThrown();
		if (t != null && t instanceof OpenNTFNotesException)
			exceptionDetails = ((OpenNTFNotesException) t).getExceptionDetails();

		WrapperFactory wf = Factory.getWrapperFactory_unchecked();
		if (wf != null)
			lastWrappedDocs = wf.getLastWrappedDocsInThread();

	}

	public List<ExceptionDetails.Entry> getExceptionDetails() {
		return exceptionDetails;
	}

	public String[] getLastWrappedDocs() {
		return lastWrappedDocs;
	}

	public void writeToLog(final StringBuffer sb) {
		if (exceptionDetails != null) {
			sb.append("    Details where exception was thrown:\n");
			for (ExceptionDetails.Entry exEntry : exceptionDetails)
				sb.append("      " + exEntry.toString() + "\n");
		}
		if (lastWrappedDocs != null) {
			sb.append("    Last wrapped docs in thread:\n");
			for (int i = 0; i < lastWrappedDocs.length; i++)
				sb.append("      " + lastWrappedDocs[i] + "\n");
		}
	}

}
