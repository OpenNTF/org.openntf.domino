/*
 * Copyright OpenNTF 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.tests.paul;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.logging.LogUtils;
import org.openntf.domino.thread.DominoThread;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Enum LoggerTests.
 */
public enum LoggerTests {
	
	/** The instance. */
	INSTANCE;

	/**
	 * Instantiates a new logger tests.
	 */
	private LoggerTests() {

	}

	/**
	 * The Class LoggingConsoleOnly.
	 */
	static class LoggingConsoleOnly implements Runnable {
		
		/** The Constant log_. */
		private final static Logger log_ = Logger.getLogger("org.openntf.domino");

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			LogUtils.loadLoggerConfig(false, "");
			Handler consoleHandler = new org.openntf.domino.logging.DefaultConsoleHandler();
			ArrayList<Handler> handlers = new ArrayList<Handler>();
			handlers.add(consoleHandler);
			if (LogUtils.setupLoggerEx("org.openntf.domino", handlers, false, Level.FINER)) {
				log_.log(Level.FINE, "This is a test just to the console", new Throwable());
			}
		}
	}

	/**
	 * The Class DominoException.
	 */
	static class DominoException implements Runnable {
		
		/** The Constant log_. */
		private final static Logger log_ = Logger.getLogger("org.openntf.domino");

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
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
	public static void main(String[] args) {
		int delay = 500;
		DominoThread dt1 = new DominoThread(new DominoException(), "Logging Test");
		dt1.start();
		DominoThread dt2 = new DominoThread(new LoggingConsoleOnly(), "Logging Test");
		dt2.start();
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			DominoUtils.handleException(e1);

		}
	}
}
