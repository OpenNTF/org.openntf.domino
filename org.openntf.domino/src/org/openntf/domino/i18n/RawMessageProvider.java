package org.openntf.domino.i18n;

import java.util.Locale;

public interface RawMessageProvider {
	public String getRawText(final String bundleName, final String key, final Locale loc);
}
