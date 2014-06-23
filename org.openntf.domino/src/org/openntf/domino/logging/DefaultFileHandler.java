/*
 * Copyright 2013
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
package org.openntf.domino.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * DefaultFileHandler class
 * 
 * Handles outputting error log to IBM_TECHNICAL_SUPPORT\\org.openntf.log.X.Y.txt, where X and Y are integers incremented when the previous
 * file is full
 */
public class DefaultFileHandler extends FileHandler {

	/**
	 * Instantiates a new default file handler.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @since org.openntf.domino 1.0.0
	 */
	public DefaultFileHandler() throws IOException {
	}

	/**
	 * Instantiates a new default file handler with pattern for file name
	 * 
	 * @param pattern
	 *            String predefined pattern for file names for the log file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @since org.openntf.domino 1.0.0
	 */
	public DefaultFileHandler(final String pattern) throws IOException {
		super(pattern);
	}

	/**
	 * Instantiates a new default file handler with pattern for filename and whether or not to append to an existing file
	 * 
	 * @param pattern
	 *            String predefined pattern for file names for the log file
	 * @param append
	 *            boolean whether to append to the existing file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @since org.openntf.domino 1.0.0
	 */
	public DefaultFileHandler(final String pattern, final boolean append) throws IOException {
		super(pattern, append);
	}

	/**
	 * Instantiates a new default file handler with pattern for filename, size limit and max file count
	 * 
	 * @param pattern
	 *            String pattern for file names
	 * @param limit
	 *            int size limit in bytes
	 * @param count
	 *            int maximum number of files to use
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @since org.openntf.domino 1.0.0
	 */
	public DefaultFileHandler(final String pattern, final int limit, final int count) throws IOException {
		super(pattern, limit, count);
	}

	/**
	 * Instantiates a new default file handler with pattern for filename, size limit, max file count and whether or not to append to an
	 * existing file
	 * 
	 * @param pattern
	 *            String pattern for file names
	 * @param limit
	 *            int size limit in bytes
	 * @param count
	 *            int maximum number of files to use
	 * @param append
	 *            boolean whether to append to the existing file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @since org.openntf.domino 1.0.0
	 */
	public DefaultFileHandler(final String pattern, final int limit, final int count, final boolean append) throws IOException {
		super(pattern, limit, count, append);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.FileHandler#close()
	 */
	@Override
	public void close() {
		super.close();
	}

	/**
	 * 
	 * Calls the publish method of the parent FileHandler class
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
	 * @see java.util.logging.FileHandler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public synchronized void publish(final LogRecord record) {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					DefaultFileHandler.this.superPub(record);
					flush();
					return null;
				}
			});
			// super.publish(record);
			// flush();
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.StreamHandler#flush()
	 */
	@Override
	public void flush() {
		super.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.StreamHandler#setOutputStream(java.io.OutputStream)
	 */
	@Override
	protected void setOutputStream(final OutputStream out) {
		super.setOutputStream(out);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Handler#getFormatter()
	 */
	@Override
	public Formatter getFormatter() {
		return super.getFormatter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Handler#reportError(java.lang.String, java.lang.Exception, int)
	 */
	@Override
	protected void reportError(final String msg, final Exception ex, final int code) {
		super.reportError(msg, ex, code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Handler#setFormatter(java.util.logging.Formatter)
	 */
	@Override
	public void setFormatter(final Formatter newFormatter) {
		super.setFormatter(newFormatter);
	}

}
