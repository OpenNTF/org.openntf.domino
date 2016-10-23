package org.openntf.domino.tests.paul;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;

public enum LoggerTests {
	INSTANCE;

	private LoggerTests() {

	}

	// static class LoggingConsoleOnly implements Runnable {
	// private final static Logger log_ = Logger.getLogger("org.openntf.domino");
	//
	// @Override
	// public void run() {
	// LogUtils.loadLoggerConfig(false, "");
	// Handler consoleHandler = new org.openntf.domino.logging.DefaultConsoleHandler();
	// ArrayList<Handler> handlers = new ArrayList<Handler>();
	// handlers.add(consoleHandler);
	// if (LogUtils.setupLoggerEx("org.openntf.domino", handlers, false, Level.FINER)) {
	// log_.log(Level.FINE, "This is a test just to our console", new Throwable());
	// }
	// }
	// }

	static class DominoException implements Runnable {
		private final static Logger log_ = Logger.getLogger("org.openntf.domino");

		@Override
		public void run() {
			log_.log(Level.FINE, "This should not get thrown", new Throwable());
			DominoUtils.handleException(new Throwable("Thrown Error2"));
		}

	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		int delay = 500;
		DominoThread dt1 = new DominoThread(new DominoException(), "Logging Test");
		dt1.start();
		// DominoThread dt2 = new DominoThread(new LoggingConsoleOnly(), "Logging Test");
		// dt2.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);
		}
	}
}
