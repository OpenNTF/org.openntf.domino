/**
 * 
 */
package org.openntf.domino.xsp;

import java.util.logging.Level;

import org.openntf.domino.Document;
import org.openntf.domino.Session;

/**
 * @author withersp
 * 
 */
public class XspOpenLogUtil {

	private transient static XspOpenLogItem oli_;

	/**
	 * 
	 */
	public XspOpenLogUtil() {

	}

	public static XspOpenLogItem getXspOpenLogItem() {
		if (null == oli_) {
			oli_ = new XspOpenLogItem();
		}
		return oli_;
	}

	public static String logError(final Throwable ee) {
		return getXspOpenLogItem().logError(ee);
	}

	public static void logError(final Session s, final Throwable ee) {
		getXspOpenLogItem().logError(s, ee);
	}

	public static void logError(final Session s, final Throwable ee, final String msg, final Level severityType, final Document doc) {
		getXspOpenLogItem().logError(s, ee, msg, severityType, doc);
	}

	public static String logErrorEx(final Throwable ee, final String msg, final Level severityType, final Document doc) {
		return getXspOpenLogItem().logErrorEx(ee, msg, severityType, doc);
	}

	public static void logEvent(final Session s, final Throwable ee, final String msg, final Level severityType, final Document doc) {
		getXspOpenLogItem().logEvent(s, ee, msg, severityType, doc);
	}

	public static String logEvent(final Throwable ee, final String msg, final Level severityType, final Document doc) {
		return getXspOpenLogItem().logEvent(ee, msg, severityType, doc);
	}

}
