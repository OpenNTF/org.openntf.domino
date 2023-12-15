/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.logging;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.openntf.domino.utils.Factory;

/**
 * Handler for writing log messages to a text file. This handler writes either to a single file or it can write to a rotating set of files.
 * For a rotating set of files, as each file reaches a given size limit, it is closed, rotated out, and a new file is opened. Successively
 * older files are named by adding "0", "1", "2", etc. into the base filename.
 * <p>
 * This class can be used as an handler in the configuration file. This handler uses the following properties (specified in the
 * configuration file on the <code>Handler.[handler name].Props</code> line:
 * <dl>
 * <dt>Pattern</dt>
 * <dd>Name of the output file using java.util.logging patterns (see list below). Use &lt;notedata&gt for a Domino data directory.
 * Required.</dd>
 * <dt>Limit</dt>
 * <dd>An approximate maximum amount to write (in bytes) to a file. Default is 100 MB. Optional.</dd>
 * <dt>Count</dt>
 * <dd>Specifies how many output files to cycle through. Default is 1. Optional.</dd>
 * <dt>Append</dt>
 * <dd>Either true if new messages should be appended to an existing file or false if an existing file should be overwritten. Default is
 * false. Optional.</dd>
 * </dl>
 * </p>
 * <p>
 * A pattern consists of a string that includes the following special components that will be replaced at runtime:
 * <ul>
 * <li>"/" the local pathname separator</li>
 * <li>"%t" the system temporary directory</li>
 * <li>"%h" the value of the "user.home" system property</li>
 * <li>"%g" the generation number to distinguish rotated logs</li>
 * <li>"%u" a unique number to resolve conflicts</li>
 * <li>"%%" translates to a single percent sign "%"</li>
 * <li>&lt;notedata&gt Domino data directory</li>
 * </ul>
 * </p>
 * <h5>Example configuration</h5>
 * <p>
 * The example defines a handler named HFile (the name is arbitrary) which writes log messages to a series of text files.
 * </p>
 *
 * <pre>
 * Handlers=HFile
 * Handler.HFile.Class=org.openntf.domino.logging.LogHandlerFile
 * Handler.HFile.Props=	Limit=50000000,	\
			Count=10, \
			Append=true, \
			Pattern=<notesdata>/IBM_TECHNICAL_SUPPORT/org.openntf.log.%u.%g.txt
 * </pre>
 *
 */
@SuppressWarnings("nls")
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
			if (!(o instanceof LHFConfig)) {
				return false;
			}
			LHFConfig other = (LHFConfig) o;
			return append == other.append && limit == other.limit && count == other.count && pattern.equals(other.pattern);
		}
	}

	public static LogHandlerConfigIF configFromProps(final String props) {
		if (props == null) {
			cfpError(props, "At least Pattern has to be specified");
		}
		LHFConfig ret = new LHFConfig();
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
				if (propKey.equals("Limit")) { //$NON-NLS-1$
					if ((ind = parsePosNum(propValue, 1000)) > 0) {
						ret.limit = ind;
					}
					break;
				}
				if (propKey.equals("Count")) { //$NON-NLS-1$
					if ((ind = parsePosNum(propValue, 1)) > 0) {
						ret.count = ind;
					}
					break;
				}
				if (propKey.equals("Append")) { //$NON-NLS-1$
					if (propValue.equals("true")) { //$NON-NLS-1$
						ret.append = true;
					} else if (propValue.equals("false")) { //$NON-NLS-1$
						ret.append = false;
					} else {
						ind = -1;
					}
					break;
				}
				if (propKey.equals("Pattern")) { //$NON-NLS-1$
					ret.pattern = propValue.replace("<notesdata>", Factory.getDataPath()); //$NON-NLS-1$
					if (ret.pattern.length() < 3) {
						ind = -1;
					}
					break;
				}
				ind = -1;
				break;
			}
			if (ind <= 0) {
				cfpError(props, "Invalid Entry '" + element + "'");
			}
		}
		if (ret.pattern == null) {
			cfpError(props, "Missing entry 'Pattern'");
		}
		return ret;
	}

	private static int parsePosNum(final String propValue, final int min) {
		int ret = -1;
		try {
			ret = Integer.parseInt(propValue);
			if (ret < min) {
				ret = -1;
			}
		} catch (Exception e) {
		}
		return ret;
	}

	private static void cfpError(final String props, final String detail) {
		throw new IllegalArgumentException("Invalid Props-Property for LogHandlerFile (" + props + "): " + detail);
	}

	public static LogHandlerFile getInstance(final LogHandlerConfigIF config, final boolean useDefaultFormatter) throws IOException {
		if (!(config instanceof LHFConfig)) {
			throw new IllegalArgumentException("Invalid call to LogHandlerFile.getInstance");
		}
		LogHandlerFile ret = new LogHandlerFile((LHFConfig) config);
		if (useDefaultFormatter) {
			ret.setFormatter(new LogFormatterFileDefault());
		}
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
		if (newFormatter != null) {
			setFormatter(newFormatter);
		} else if (useDefaultFormatter && !(getFormatter() instanceof LogFormatterFileDefault)) {
			setFormatter(new LogFormatterFileDefault());
		}
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
		if (publishing_.get() == Boolean.TRUE) {
			return;
		}
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
