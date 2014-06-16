package org.openntf.domino.i18n;

import java.util.Locale;

public abstract class RawMessageProvider implements Comparable<RawMessageProvider> {
	public abstract String getRawText(final String bundleName, final String key, final Locale loc);

	protected int getPriority() {
		return 50;
	}

	@Override
	public int compareTo(final RawMessageProvider paramT) {
		return getPriority() - paramT.getPriority();
	}
}
