package org.openntf.domino.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.openntf.domino.utils.Factory;

public class RawMessageProviderImpl implements RawMessageProvider {

	protected ClassLoader provClassLoader;

	@Override
	public String getRawText(final String bundleName, final String key, final Locale loc) {
		try {
			ClassLoader cl = provClassLoader != null ? provClassLoader : Factory.getClassLoader();
			ResourceBundle rb = ResourceBundle.getBundle(bundleName, loc, cl,
					ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
			return rb.getString(key);
		} catch (MissingResourceException mre) {
			return null;
		}
	}
}
