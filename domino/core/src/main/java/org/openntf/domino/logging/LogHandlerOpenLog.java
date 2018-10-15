package org.openntf.domino.logging;

import java.io.IOException;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * Handler for writing log messages to an OpenLog database.
 * <p>
 * This class can be used as an handler in the configuration file. This handler uses the following properties (specified in the
 * configuration file on the <code>Handler.[handler name].Props</code> line:
 * <dl>
 * <dt>LogDB</dt>
 * <dd>Path to the OpenLog database where the log messages will be saved. The handler will create on document for each log message.
 * Required.</dd>
 * </dl>
 * </p>
 * <h5>Example configuration</h5>
 * <p>
 * The example defines a handler named HLogDB (the name is arbitrary) which writes log messages to a database OpenLog.nsf. The file name and
 * path to the OpenLog database is also arbitrary.
 * </p>
 *
 * <pre>
 * Handlers=HLogDB
 * Handler.HLogDB.Class=org.openntf.domino.logging.LogHandlerOpenLog
 * Handler.HLogDB.MinimalLevel=INFO
 * Handler.HLogDB.Props=LogDB=OpenLog.nsf
 * </pre>
 *
 */
public class LogHandlerOpenLog extends Handler implements LogHandlerUpdateIF {

	private static class LHOLConfig implements LogHandlerConfigIF {
		String _logDB;

		LHOLConfig() {
			_logDB = null;
		}

		@Override
		public boolean isEqual(final LogHandlerConfigIF other) {
			if (!(other instanceof LHOLConfig)) {
				return false;
			}
			return _logDB.equals(((LHOLConfig) other)._logDB);
		}
	}

	public static LogHandlerConfigIF configFromProps(final String props) {
		if (props == null) {
			cfpError(props, "LogDB has to be specified");
		}
		LHOLConfig ret = new LHOLConfig();
		String[] propArr = LogConfig.splitAlongComma(props);
		for (String element : propArr) {
			int ind = element.indexOf('=');
			while (ind > 0) {
				String propKey = element.substring(0, ind).trim();
				String propValue = element.substring(ind + 1).trim();
				if (propKey.isEmpty() || propValue.isEmpty()) {
					ind = -1;
					break;
				}
				if (propKey.equals("LogDB")) {
					ret._logDB = propValue;
					break;
				}
				ind = -1;
				break;
			}
			if (ind <= 0) {
				cfpError(props, "Invalid Entry '" + element + "'");
			}
		}
		if (ret._logDB == null) {
			cfpError(props, "Missing entry 'LogDB'");
		}
		return ret;
	}

	private static void cfpError(final String props, final String detail) {
		throw new IllegalArgumentException("Invalid Props-Property for LogHandlerFile (" + props + "): " + detail);
	}

	public static LogHandlerOpenLog getInstance(final LogHandlerConfigIF config, final boolean useDefaultFormatter) throws IOException {
		if (!(config instanceof LHOLConfig)) {
			throw new IllegalArgumentException("Invalid call to LogHandlerOpenLog.getInstance");
		}
		return new LogHandlerOpenLog((LHOLConfig) config);
	}

	@Override
	public boolean mayUpdateYourself(final LogHandlerConfigIF newHandlerConfig, final LogHandlerConfigIF oldHandlerConfig) {
		return newHandlerConfig.isEqual(oldHandlerConfig);
	}

	@Override
	public void doUpdateYourself(final LogHandlerConfigIF newhandlerConfig, final LogHandlerConfigIF oldHandlerConfig,
			final boolean useDefaultFormatter, final Formatter newFormatter) {
		// Nothing to do here
	}

	/** The ol_. */
	private LogGeneratorOpenLog _olGenerator;

	/**
	 * Instantiates a new open log handler.
	 *
	 * @param config
	 *            configured properties
	 *
	 * @since org.openntf.domino 1.0.0
	 */
	public LogHandlerOpenLog(final LHOLConfig config) {
		_olGenerator = new LogGeneratorOpenLog(config._logDB);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.logging.Handler#close()
	 */
	@Override
	public void close() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.logging.Handler#flush()
	 */
	@Override
	public void flush() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * Creates an OpenLog entry in designated database
	 *
	 * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public synchronized void publish(final LogRecord record) {
		if (publishing_.get() == Boolean.TRUE) {
			return;
		}
		publishing_.set(Boolean.TRUE);
		try {
			Session session = Factory.getSession_unchecked(SessionType.CURRENT);
			if (session != null) {
				_olGenerator.log(session, record, new LogRecordAdditionalInfo(record));
			}
		} catch (Exception e) {
			System.err.println("Exception " + e.getClass().getName() + " in LogHandlerOpenLog.publish:");
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
