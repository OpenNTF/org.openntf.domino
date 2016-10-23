package org.openntf.domino.logging;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.openntf.domino.utils.Factory;

public class LogHandlerFile extends FileHandler implements LogHandlerUpdateIF {

	private static class LHFConfig implements LogHandlerConfigIF {
		String pattern;
		int limit;
		int count;
		boolean append;

		LHFConfig() {
			pattern = null;
			limit = 100 * 1000 * 1000;
			count = 1;
			append = false;
		}

		@Override
		public boolean isEqual(final LogHandlerConfigIF o) {
			if (!(o instanceof LHFConfig))
				return false;
			LHFConfig other = (LHFConfig) o;
			return append == other.append && limit == other.limit && count == other.count && pattern.equals(other.pattern);
		}
	}

	public static LogHandlerConfigIF configFromProps(final String props) {
		if (props == null)
			cfpError(props, "At least Pattern has to be specified");
		LHFConfig ret = new LHFConfig();
		String[] propArr = LogConfig.splitAlongComma(props);
		for (int i = 0; i < propArr.length; i++) {
			int ind = propArr[i].indexOf('=');
			while (ind > 0) {
				String propKey = propArr[i].substring(0, ind).trim();
				String propValue = propArr[i].substring(ind + 1).trim();
				if (propKey.isEmpty() || propValue.isEmpty()) {
					ind = -1;
					break;
				}
				if (propKey.equals("Limit")) {
					if ((ind = parsePosNum(propValue, 1000)) > 0)
						ret.limit = ind;
					break;
				}
				if (propKey.equals("Count")) {
					if ((ind = parsePosNum(propValue, 1)) > 0)
						ret.count = ind;
					break;
				}
				if (propKey.equals("Append")) {
					if (propValue.equals("true"))
						ret.append = true;
					else if (propValue.equals("false"))
						ret.append = false;
					else
						ind = -1;
					break;
				}
				if (propKey.equals("Pattern")) {
					ret.pattern = propValue.replace("<notesdata>", Factory.getDataPath());
					if (ret.pattern.length() < 3)
						ind = -1;
					break;
				}
				ind = -1;
				break;
			}
			if (ind <= 0)
				cfpError(props, "Invalid Entry '" + propArr[i] + "'");
		}
		if (ret.pattern == null)
			cfpError(props, "Missing entry 'Pattern'");
		return ret;
	}

	private static int parsePosNum(final String propValue, final int min) {
		int ret = -1;
		try {
			ret = Integer.parseInt(propValue);
			if (ret < min)
				ret = -1;
		} catch (Exception e) {
		}
		return ret;
	}

	private static void cfpError(final String props, final String detail) {
		throw new IllegalArgumentException("Invalid Props-Property for LogHandlerFile (" + props + "): " + detail);
	}

	public static LogHandlerFile getInstance(final LogHandlerConfigIF config, final boolean useDefaultFormatter) throws IOException {
		if (!(config instanceof LHFConfig))
			throw new IllegalArgumentException("Invalid call to LogHandlerFile.getInstance");
		LogHandlerFile ret = new LogHandlerFile((LHFConfig) config);
		if (useDefaultFormatter)
			ret.setFormatter(new LogFormatterFileDefault());
		return ret;
	}

	public LogHandlerFile(final LHFConfig config) throws IOException {
		super(config.pattern, config.limit, config.count, config.append);
	}

	@Override
	public boolean mayUpdateYourself(final LogHandlerConfigIF newHandlerConfig, final LogHandlerConfigIF oldHandlerConfig) {
		return newHandlerConfig.isEqual(oldHandlerConfig);
	}

	@Override
	public void doUpdateYourself(final LogHandlerConfigIF newhandlerConfig, final LogHandlerConfigIF oldHandlerConfig,
			final boolean useDefaultFormatter, final Formatter newFormatter) {
		if (newFormatter != null)
			setFormatter(newFormatter);
		else if (useDefaultFormatter && !(getFormatter() instanceof LogFormatterFileDefault))
			setFormatter(new LogFormatterFileDefault());
	}

	/**
	 * 
	 * Calls the publish method of the parent FileHandler class
	 * 
	 * Called from publish method via a PrivilegedAction to avoid access issues
	 * 
	 * @param record
	 *            LogRecord to be output
	 * @since org.openntf.domino 1.0.0
	 */
	private void superPub(final LogRecord record) {
		super.publish(record);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.FileHandler#publish(java.util.logging.LogRecord)
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
					LogHandlerFile.this.superPub(record);
					LogHandlerFile.this.flush();
					return null;
				}
			});
		} catch (Throwable e) {
			System.err.println(e.toString());
			e.printStackTrace();
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
