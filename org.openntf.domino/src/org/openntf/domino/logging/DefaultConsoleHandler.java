package org.openntf.domino.logging;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author withersp
 * 
 */
public class DefaultConsoleHandler extends ConsoleHandler {

	// this variable sets the "debug level" of all the methods. Right now
	// the valid debug levels are:
	// 0 -- internal errors are discarded
	// 1 -- Exception messages from internal errors are printed
	// 2 -- stack traces from internal errors are also printed
	private static String olDebugLevel = "1";

	public DefaultConsoleHandler() {

	}

	public static String getOlDebugLevel() {
		return olDebugLevel;
	}

	public static void setOlDebugLevel(String olDebugLevel) {
		DefaultConsoleHandler.olDebugLevel = olDebugLevel;
	}

	// Date formatter
	private static ThreadLocal<DateFormat> formatter = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
		}
	};

	@Override
	public void close() {
		super.close();
	}

	@Override
	public void publish(LogRecord logRecord) {
		int debugLevel = 0;
		try {
			debugLevel = Integer.parseInt(olDebugLevel);
		} catch (Exception e) {
			System.out.println(this.getClass().getName() + ": Error getting debug level - non-numeric");
		}
		if (debugLevel > 0) {
			Date date = new Date(logRecord.getMillis());
			StringBuffer sb = new StringBuffer();
			sb.append(formatter.get().format(date));
			sb.append(" [");
			sb.append(logRecord.getLevel().getName());
			sb.append("]: ");
			sb.append(logRecord.getSourceClassName());
			sb.append(".");
			sb.append(logRecord.getSourceMethodName());
			sb.append("() - ");
			sb.append(logRecord.getMessage());
			System.out.println(sb.toString());
		}
		if (debugLevel > 1) {
			if (logRecord.getThrown() != null && logRecord.getThrown() instanceof Exception) {
				Exception ee = (Exception) logRecord.getThrown();
				ee.printStackTrace();
			}
		}
	}

	@Override
	public void flush() {
		super.flush();
	}

	@Override
	protected void setOutputStream(OutputStream stream) {
		super.setOutputStream(stream);
	}

	@Override
	public Formatter getFormatter() {
		return super.getFormatter();
	}

	@Override
	protected void reportError(String msg, Exception ex, int code) {
		super.reportError(msg, ex, code);
	}

	@Override
	public void setFormatter(Formatter newFormatter) {
		super.setFormatter(newFormatter);
	}

}
