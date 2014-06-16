package org.openntf.domino.i18n;

import java.io.Serializable;
import java.util.Locale;

/**
 * This
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public class Messages implements Serializable {

	private static final String BUNDLE_NAME = "de.foconis.lib.app.messages"; //$NON-NLS-1$

	public static String getString(final String key, final Object... args) {
		return MessageProvider.sGetString(BUNDLE_NAME, key, args);
	}

	public static String getInternalString(final String key, final Object... args) {
		return MessageProvider.sGetInternalString(BUNDLE_NAME, key, args);
	}

	public static String getRawString(final String key, final Locale loc) {
		return MessageProvider.sGetRawText(BUNDLE_NAME, key, loc);
	}

}
