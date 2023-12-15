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

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Handles formatting of output to Domino console
 *
 * @author withersp
 *
 */
@SuppressWarnings("nls")
public class ConsoleFormatter extends Formatter {

	/** The UT c_ format. */
	private boolean UTC_Format = false;

	/**
	 * Constructor
	 *
	 * @since org.openntf.domino 1.0.0
	 */
	public ConsoleFormatter() {
	}

	/**
	 * Checks whether dates should be outputted in UTC format
	 *
	 * @return boolean, if is UTC format
	 * @since org.openntf.domino 1.0.0
	 */
	public boolean isUTC_Format() {
		return UTC_Format;
	}

	/**
	 * Sets whether dates should be outputted in UTC format
	 *
	 * @param uTC_Format
	 *            boolean, whether UTC format
	 * @since org.openntf.domino 1.0.0
	 */
	public void setUTC_Format(final boolean uTC_Format) {
		UTC_Format = uTC_Format;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(final LogRecord logRecord) {
		//		Date recordDate = new Date(logRecord.getMillis());
		StringBuffer sb = new StringBuffer();
		try {
			// TIME and LEVEL is already at the console
			//			sb.append(LogUtils.dateToString(recordDate, UTC_Format));
			//			sb.append(" [");
			//			sb.append(logRecord.getLevel().getName());
			//			sb.append("]: ");

			//
			sb.append(logRecord.getMessage());

			sb.append("\n   (source:");
			sb.append(logRecord.getSourceClassName());
			sb.append('.');
			sb.append(logRecord.getSourceMethodName());
			if (logRecord.getThrown() != null) {
				sb.append(" - See IBM_TECHNICAL_SUPPORT\\org.openntf.log.X.Y.txt for full stack trace.");
			}
			sb.append(")");
		} catch (Throwable t) {
			System.out.println("Error trying to format output for error handling. Resorting to standard stack trace...");
			t.printStackTrace();
		}
		return sb.toString();
	}
}
