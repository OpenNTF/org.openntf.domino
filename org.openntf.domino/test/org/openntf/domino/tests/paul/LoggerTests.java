package org.openntf.domino.tests.paul;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.logging.LogUtils;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;

public enum LoggerTests {
	INSTANCE;

	private LoggerTests() {

	}

	static class LoggingTest implements Runnable {
		private final static Logger log_ = Logger.getLogger("org.openntf.domino");

		@Override
		public void run() {
			LogUtils.loadLoggerConfig(false, "");
			Handler consoleHandler = new org.openntf.domino.logging.DefaultConsoleHandler();
			ArrayList<Handler> handlers = new ArrayList<Handler>();
			handlers.add(consoleHandler);
			if (LogUtils.setupLogger("org.openntf.domino", handlers, false, Level.FINER)) {
				log_.log(Level.FINE, "", new Throwable());
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int delay = 500;
		DominoThread dt1 = new DominoThread(new LoggingTest(), "Logging Test");
		dt1.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
	}
}
