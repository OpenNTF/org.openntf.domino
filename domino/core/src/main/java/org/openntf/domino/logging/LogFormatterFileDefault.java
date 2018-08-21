package org.openntf.domino.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.openntf.domino.exceptions.OpenNTFNotesException;

/**
 * Default formatter for writing log messages to a file when no formatter is specified in the configuration file. Writes every message on at
 * least two lines (optionally with a stack trace). Date and time is written as 'yyyy-MM-dd HH:mm:ss'.
 */
public class LogFormatterFileDefault extends Formatter {

	public static LogFormatterFileDefault getInstance() {
		return new LogFormatterFileDefault();
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
		sb.append("Log from " + logRecord.getLoggerName());
		sb.append('\n');
		Throwable t = logRecord.getThrown();
		StackTraceElement ste = null;
		if (t != null) {
			StackTraceElement[] stes = t.getStackTrace();
			if (stes != null && stes.length > 0) {
				ste = stes[0];
			}
		}
		sb.append("      ");
		sb.append(logRecord.getMessage());
		boolean levelSevere = (logRecord.getLevel().intValue() >= Level.SEVERE.intValue());
		if (ste != null || levelSevere) {
			sb.append(" - ");
		}
		if (ste != null) {
			sb.append(ste.getClassName() + "." + ste.getMethodName());
		} else if (levelSevere) {
			sb.append("***NO STACK TRACE***");
		}
		sb.append('\n');
		if (logRecord.getThrown() instanceof OpenNTFNotesException) {
			LogRecordAdditionalInfo lrai = new LogRecordAdditionalInfo(logRecord);
			lrai.writeToLog(sb);
		}
		if (t != null && ste != null) { // Of course superfluous: t!=null, but otherwise we get a warning
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			sb.append("    ");
			sb.append(sw.toString().replace("\r\n", "\n"));
			sb.append("\n");
		}
		return sb.toString();
	}
}
