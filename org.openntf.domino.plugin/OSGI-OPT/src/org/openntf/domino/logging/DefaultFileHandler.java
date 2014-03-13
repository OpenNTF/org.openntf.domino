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

// TODO: Auto-generated Javadoc
/**
 * The Class DefaultFileHandler.
 */
public class DefaultFileHandler extends FileHandler {

	/**
	 * Instantiates a new default file handler.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public DefaultFileHandler() throws IOException {
	}

	/**
	 * Instantiates a new default file handler.
	 * 
	 * @param arg0
	 *            the arg0
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public DefaultFileHandler(final String arg0) throws IOException {
		super(arg0);
	}

	/**
	 * Instantiates a new default file handler.
	 * 
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public DefaultFileHandler(final String arg0, final boolean arg1) throws IOException {
		super(arg0, arg1);
	}

	/**
	 * Instantiates a new default file handler.
	 * 
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public DefaultFileHandler(final String arg0, final int arg1, final int arg2) throws IOException {
		super(arg0, arg1, arg2);
	}

	/**
	 * Instantiates a new default file handler.
	 * 
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 * @param arg3
	 *            the arg3
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public DefaultFileHandler(final String arg0, final int arg1, final int arg2, final boolean arg3) throws IOException {
		super(arg0, arg1, arg2, arg3);
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
