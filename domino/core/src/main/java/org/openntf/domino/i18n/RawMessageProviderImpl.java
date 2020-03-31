/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.openntf.domino.utils.Factory;

public class RawMessageProviderImpl extends RawMessageProvider {

	/*
	 * In the first version, we used the standard-no-fallback-ResourceBundle.Control, given by
	 * ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES).
	 * However, when resources sit in an NSF, the method Control.needsReload returns "true" very often
	 * (perhaps always?), and performance gets dreadful. Hence:
	 */
	private static class MyNoFallbackControl extends ResourceBundle.Control {
		static final ResourceBundle.Control _instance = new MyNoFallbackControl();
		private List<String> _format;

		public MyNoFallbackControl() {
			(_format = new ArrayList<String>()).add("java.properties");
		}

		@Override
		public List<String> getFormats(final String s) {
			return _format;
		}

		@Override
		public Locale getFallbackLocale(final String s, final Locale loc) {
			return null;
		}

		@Override
		public boolean needsReload(final String s1, final Locale loc, final String s2, final ClassLoader cl, final ResourceBundle rb,
				final long l) {
			return false;
		}
	}

	protected ClassLoader provClassLoader;

	@Override
	public String getRawText(final String bundleName, final String key, final Locale loc) {
		try {
			ClassLoader cl = provClassLoader != null ? provClassLoader : Factory.getClassLoader();
			ResourceBundle rb = ResourceBundle.getBundle(bundleName, loc, cl, MyNoFallbackControl._instance);
			//					ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
			return rb.getString(key);
		} catch (MissingResourceException mre) {
			return null;
		}
	}

	/**
	 * this is the last provider
	 */
	@Override
	protected int getPriority() {
		return 99;
	}
}
