package org.openntf.domino.logging;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class OutputFormatter extends Formatter {

	private boolean UTC_Format = false;

	public OutputFormatter() {
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
		sb.append(LogUtils.dateToString(recordDate, UTC_Format));
		sb.append(" [");
		sb.append(logRecord.getLevel().getName());
		sb.append("]: ");
		sb.append(logRecord.getSourceClassName());
		sb.append(".");
		sb.append(logRecord.getSourceMethodName());
		sb.append("() - ");
		sb.append(logRecord.getMessage());
		sb.append("\n");
		return sb.toString();
	}
}
