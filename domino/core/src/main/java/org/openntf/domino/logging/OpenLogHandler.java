/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

/**
 * OpenLogHandler class
 * 
 * Handles outputting error log to OpenLog
 */
public class OpenLogHandler extends Handler {

	/** The log db path. */
	//	private String logDbPath;

	/** The ol_. */
	private DominoOpenLogItem ol_;

	/**
	 * Instantiates a new open log handler.
	 * 
	 * @since org.openntf.domino 1.0.0
	 */
	public OpenLogHandler() {
		ol_ = new DominoOpenLogItem();
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

	/**
	 * Sets the OpenLog database path for the OpenLogItem.
	 * 
	 * @param logDbPath
	 *            the new log db path
	 * @since org.openntf.domino 1.0.0
	 */
	public void setLogDbPath(final String logDbPath) {
		ol_.setLogDbName(logDbPath);
	}

	private boolean publishing_ = false;

	/*
	 * (non-Javadoc)
	 * 
	 * Creates an OpenLog entry in designated database
	 * 
	 * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public void publish(final LogRecord record) {
		if (publishing_)
			return;
		publishing_ = true;
		try {
			Throwable t = record.getThrown();
			if (t != null) {
				for (StackTraceElement elem : t.getStackTrace()) {
					if (elem.getClassName().equals(getClass().getName())) {
						// NTF - we are by definition in a loop
						System.out.println(t.toString());
						t.printStackTrace();
						return;
					}
				}
			}
			org.openntf.domino.Session session = Factory.getSession_unchecked(SessionType.CURRENT);
			if (session != null) {
				ol_.logError(session, t, record.getMessage(), record.getLevel(), null);
			}
		} finally {
			publishing_ = false;
		}
	}

}
