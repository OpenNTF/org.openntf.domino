package org.openntf.domino.design.sync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public enum OnDiskUtil {
	;
	/**
	 * Returns, if the resourceName must be encoded
	 * 
	 * @param resName
	 *            the resourceName
	 * @return true if the resourceName contains invalid characters
	 */
	public static boolean mustEncode(final String resName) {
		for (int i = 0; i < resName.length(); i++) {
			char ch = resName.charAt(i);
			switch (ch) {
			case '/':
			case '\\':
			case ':':
			case '*':
			case '?':
			case '<':
			case '>':
			case '|':
			case '"':
				return true;
			}
		}
		return false;
	}

	/**
	 * Encodes the resource name, so that it is ODP-compatible
	 * 
	 * @param resName
	 *            the resource name
	 * @return the encoded version (replaces / \ : * &gt; &lt; | " )
	 */
	public static String encodeResourceName(final String resName) {
		if (resName == null)
			return null;
		if (!mustEncode(resName))
			return resName;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < resName.length(); i++) {
			char ch = resName.charAt(i);
			switch (ch) {
			case '_':
			case '/':
			case '\\':
			case ':':
			case '*':
			case '?':
			case '<':
			case '>':
			case '|':
			case '"':
				sb.append('_');
				sb.append(Integer.toHexString(ch));
				break;
			default:
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	public static PrintStream getLogStream(final File odpDir) throws FileNotFoundException {
		File logDir = new File(odpDir, SyncTask.LOG_DIR);
		logDir.mkdirs();
		File logFile = new File(logDir, "sync-10.log");
		if (logFile.exists())
			logFile.delete();
		for (int i = 9; i > 0; i--) {
			logFile = new File(logDir, "sync-" + i + ".log");
			if (logFile.exists())
				logFile.renameTo(new File(logDir, "sync-" + (i + 1) + ".log"));
		}
		logFile = new File(logDir, "sync-1.log");
		return new PrintStream(logFile);

	}
}
