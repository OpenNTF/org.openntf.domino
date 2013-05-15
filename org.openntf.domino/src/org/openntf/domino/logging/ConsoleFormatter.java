package org.openntf.domino.logging;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {

	private boolean UTC_Format = false;

	public ConsoleFormatter() {
		// TODO Auto-generated constructor stub
	}

	public boolean isUTC_Format() {
		return UTC_Format;
	}

	public void setUTC_Format(boolean uTC_Format) {
		UTC_Format = uTC_Format;
	}

	@Override
	public String format(LogRecord logRecord) {
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
		sb.append("***See IBM_TECHNICAL_SUPPORT\\org.openntf.log.X.Y.txt for full stack trace***");
		sb.append("\n");
		return sb.toString();
	}
}
