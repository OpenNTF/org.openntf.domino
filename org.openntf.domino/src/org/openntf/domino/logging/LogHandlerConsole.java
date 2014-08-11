package org.openntf.domino.logging;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

public class LogHandlerConsole extends ConsoleHandler {

	public static Object configFromProps(final String props) {
		return Boolean.FALSE;
	}

	public static LogHandlerConsole getInstance(final Object config, final boolean useDefaultFormatter) {
		LogHandlerConsole ret = new LogHandlerConsole();
		if (useDefaultFormatter)
			ret.setFormatter(new LogFormatterConsoleDefault());
		return ret;
	}

	/**
	 * Calls the publish method of the parent ConsoleHandler class
	 * 
	 * Called from publish method via a PrivilegedAction to avoid access issues
	 * 
	 * @param record
	 *            LogRecord to be outputted
	 * @since org.openntf.domino 1.0.0
	 */
	private void superPub(final LogRecord record) {
		super.publish(record);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.ConsoleHandler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public synchronized void publish(final LogRecord record) {
		if (publishing_.get() == Boolean.TRUE)
			return;
		publishing_.set(Boolean.TRUE);
		try {
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				public Object run() {
					LogHandlerConsole.this.superPub(record);
					return null;
				}
			});
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			publishing_.set(Boolean.FALSE);
		}
	}

	private static ThreadLocal<Boolean> publishing_ = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
	};

}
