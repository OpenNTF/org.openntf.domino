package org.openntf.domino.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FileFormatter extends Formatter {

	private boolean UTC_Format = false;

	public FileFormatter() {
		// TODO Auto-generated constructor stub
	}

	public boolean isUTC_Format() {
		return UTC_Format;
	}

	public void setUTC_Format(final boolean uTC_Format) {
		UTC_Format = uTC_Format;
	}

	@Override
	public String format(final LogRecord logRecord) {
		Date recordDate = new Date(logRecord.getMillis());
		StringBuffer sb = new StringBuffer();
		StackTraceElement ste = logRecord.getThrown().getStackTrace()[0];
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
