package org.openntf.domino.logging;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.openntf.domino.exceptions.OpenNTFNotesException;

/**
 * Default formatter for writing log messages to the Domino console when no formatter is specified in the configuration file. Date and time
 * is written as 'yyyy-MM-dd HH:mm:ss'. Does not write the full stack trace if there is one.
 */
public class LogFormatterConsoleDefault extends Formatter {

	public static LogFormatterConsoleDefault getInstance() {
		return new LogFormatterConsoleDefault();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(final LogRecord logRecord) {
		StringBuffer sb = new StringBuffer();
		sb.append(Logging.dateToString(new Date(logRecord.getMillis())));
		sb.append(" [");
		sb.append(logRecord.getLevel().getName());
		sb.append("]: ");
		Throwable t = logRecord.getThrown();
		StackTraceElement ste = null;
		if (t != null) {
			StackTraceElement[] stes = t.getStackTrace();
			if (stes != null && stes.length > 0) {
				ste = stes[0];
			}
		}
		if (ste != null) {
			sb.append(ste.getClassName() + "." + ste.getMethodName());
		} else {
			sb.append("***NO STACK TRACE***");
		}
		sb.append(" - ");
		sb.append(logRecord.getMessage());
		sb.append('\n');
		if (logRecord.getThrown() instanceof OpenNTFNotesException) {
			LogRecordAdditionalInfo lrai = new LogRecordAdditionalInfo(logRecord);
			lrai.writeToLog(sb);
		}
		if (ste != null) {
			sb.append("\n");
			sb.append("***See configured log file for full stack trace***");
			sb.append("\n");
		}
		return sb.toString();
	}
}
