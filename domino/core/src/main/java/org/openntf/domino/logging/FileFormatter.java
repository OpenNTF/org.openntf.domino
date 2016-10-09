package org.openntf.domino.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FileFormatter extends Formatter {

	private boolean UTC_Format = false;

	/**
	 * Constructor
	 * 
	 * @since org.openntf.domino 1.0.0
	 */
	public FileFormatter() {
	}

	/**
	 * Checks whether dates should be outputted in UTC format
	 * 
	 * @return boolean, if is UTC format
	 * @since org.openntf.domino 1.0.0
	 */
	public boolean isUTC_Format() {
		return UTC_Format;
	}

	/**
	 * Sets whether dates should be outputted in UTC format
	 * 
	 * @param uTC_Format
	 *            boolean, whether UTC format
	 * @since org.openntf.domino 1.0.0
	 */
	public void setUTC_Format(final boolean uTC_Format) {
		UTC_Format = uTC_Format;
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(final LogRecord logRecord) {
		Date recordDate = new Date(logRecord.getMillis());
		StringBuffer sb = new StringBuffer();
		Throwable t = logRecord.getThrown();
		StackTraceElement ste = null;
		if (t != null) {
			ste = t.getStackTrace()[0];
		}
		sb.append(LogUtils.dateToString(recordDate, UTC_Format));
		sb.append(" [");
		sb.append(logRecord.getLevel().getName());
		sb.append("]: ");
		if (null == ste) {
			sb.append("***NO STACK TRACE***");
		} else {
			sb.append(ste.getClassName() + "." + ste.getMethodName());
		}
		sb.append(" - ");
		sb.append(logRecord.getMessage());
		sb.append("\n");
		if (null != ste) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			logRecord.getThrown().printStackTrace(pw);
			sb.append(sw.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
