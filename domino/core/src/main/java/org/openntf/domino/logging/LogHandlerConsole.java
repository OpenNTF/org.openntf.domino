package org.openntf.domino.logging;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Handler for writing log messages to the Domino server console.
 * <p>
 * This class can be used as an handler in the configuration file. This handler does not use any custom properties. You should use this
 * handler together with some other handler which writes log messages to a file or to the OpenLog database (as it does not by default print
 * full stack trace if there is one attached to the LogRecord).
 * </p>
 * <h5>Example configuration</h5>
 * <p>
 * The example defines a handler named HConsole (the name is arbitrary).
 * </p>
 *
 * <pre>
 * Handlers=HConsole
 * Handler.HConsole.Class=org.openntf.domino.logging.LogHandlerConsole
 * </pre>
 */
public class LogHandlerConsole extends ConsoleHandler implements LogHandlerUpdateIF {

	private static class LHCConfig implements LogHandlerConfigIF {
		private static LHCConfig _theLHC = new LHCConfig();

		@Override
		public boolean isEqual(final LogHandlerConfigIF other) {
			return other instanceof LHCConfig;
		}
	}

	public static LogHandlerConfigIF configFromProps(final String props) {
		return LHCConfig._theLHC;
	}

	public static LogHandlerConsole getInstance(final LogHandlerConfigIF config, final boolean useDefaultFormatter) {
		if (!(config instanceof LHCConfig)) {
			throw new IllegalArgumentException("Invalid call to LogHandlerConsole.getInstance");
		}
		LogHandlerConsole ret = new LogHandlerConsole();
		if (useDefaultFormatter) {
			ret.setFormatter(new LogFormatterConsoleDefault());
		}
		return ret;
	}

	@Override
	public boolean mayUpdateYourself(final LogHandlerConfigIF newHandlerConfig, final LogHandlerConfigIF oldHandlerConfig) {
		return newHandlerConfig.isEqual(oldHandlerConfig);
	}

	@Override
	public void doUpdateYourself(final LogHandlerConfigIF newhandlerConfig, final LogHandlerConfigIF oldHandlerConfig,
			final boolean useDefaultFormatter, final Formatter newFormatter) {
		if (newFormatter != null) {
			setFormatter(newFormatter);
		} else if (useDefaultFormatter && !(getFormatter() instanceof LogFormatterConsoleDefault)) {
			setFormatter(new LogFormatterConsoleDefault());
		}
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
		if (publishing_.get() == Boolean.TRUE) {
			return;
		}
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
