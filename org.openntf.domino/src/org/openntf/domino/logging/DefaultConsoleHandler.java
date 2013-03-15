package org.openntf.domino.logging;

import java.util.logging.ConsoleHandler;
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
			super.publish(logRecord);
		}
		if (debugLevel > 1) {
			if (logRecord.getThrown() != null && logRecord.getThrown() instanceof Exception) {
				Exception ee = (Exception) logRecord.getThrown();
				ee.printStackTrace();
			}
		}
	}

}
