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
package org.openntf.domino.logging;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

// TODO: Auto-generated Javadoc
/**
 * The Class DefaultConsoleHandler.
 */
public class DefaultConsoleHandler extends ConsoleHandler {

	/**
	 * Instantiates a new default console handler.
	 */
	public DefaultConsoleHandler() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.util.logging.ConsoleHandler#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();
	}

	/* (non-Javadoc)
	 * @see java.util.logging.ConsoleHandler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public void publish(LogRecord arg0) {
		// TODO Auto-generated method stub
		super.publish(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.logging.StreamHandler#flush()
	 */
	@Override
	public void flush() {
		// TODO Auto-generated method stub
		super.flush();
	}

	/* (non-Javadoc)
	 * @see java.util.logging.StreamHandler#setOutputStream(java.io.OutputStream)
	 */
	@Override
	protected void setOutputStream(OutputStream arg0) {
		// TODO Auto-generated method stub
		super.setOutputStream(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Handler#getFormatter()
	 */
	@Override
	public Formatter getFormatter() {
		// TODO Auto-generated method stub
		return super.getFormatter();
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Handler#reportError(java.lang.String, java.lang.Exception, int)
	 */
	@Override
	protected void reportError(String msg, Exception ex, int code) {
		// TODO Auto-generated method stub
		super.reportError(msg, ex, code);
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Handler#setFormatter(java.util.logging.Formatter)
	 */
	@Override
	public void setFormatter(Formatter newFormatter) {
		// TODO Auto-generated method stub
		super.setFormatter(newFormatter);
	}

}
