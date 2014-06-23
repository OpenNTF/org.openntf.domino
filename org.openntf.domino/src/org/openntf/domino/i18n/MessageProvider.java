/*
 * © Copyright FOCONIS AG, 2014
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
 * 
 */
package org.openntf.domino.i18n;

import java.util.List;
import java.util.Locale;

import org.openntf.domino.utils.Factory;

public class MessageProvider {

	/**
	 * returns the raw text
	 * 
	 * @param bundleName
	 *            the bundleName
	 * @param key
	 *            the key
	 * @param loc
	 *            the locale to use for lookup
	 * @return the raw text
	 */
	public String getRawText(final String bundleName, final String key, final Locale loc) {
		List<RawMessageProvider> provs = Factory.findApplicationServices(RawMessageProvider.class);
		for (RawMessageProvider prov : provs) {
			String ret = prov.getRawText(bundleName, key, loc);
			if (ret != null)
				return ret;

		}
		return getDefaultString(bundleName, key, loc);
	}

	public static String sGetRawText(final String bundleName, final String key, final Locale loc) {
		return getCurrentInstance().getRawText(bundleName, key, loc);
	}

	/**
	 * Returns a default string, if no text is found
	 */
	protected String getDefaultString(final String bundleName, final String key, final Locale loc) {
		return "[&]Invalid TextID '" + bundleName + "/" + key + "'";
	}

	/**
	 * Returns the cooked text, based on the external locale (= browser locale)
	 * 
	 * @param bundleName
	 *            the bundleName
	 * @param key
	 *            the key
	 * @param args
	 *            a list of arguments. Depends on implementation
	 * @return the text
	 */
	public String getString(final String bundleName, final String key, final Object... args) {
		return getCookedText(bundleName, key, Factory.getExternalLocale(), args);
	}

	public static String sGetString(final String bundleName, final String key, final Object... args) {
		return getCurrentInstance().getString(bundleName, key, args);
	}

	/**
	 * Returns the cooked text, based on the internal locale (= db/os locale)
	 * 
	 * @param bundleName
	 *            the bundleName
	 * @param key
	 *            the key
	 * @param args
	 *            a list of arguments. Depends on implementation
	 * @return the text
	 */
	public String getInternalString(final String bundleName, final String key, final Object... args) {
		return getCookedText(bundleName, key, Factory.getInternalLocale(), args);
	}

	public static String sGetInternalString(final String bundleName, final String key, final Object... args) {
		return getCurrentInstance().getString(bundleName, key, args);
	}

	/**
	 * returns the same as getRawText. This method may be overwritten.
	 */
	protected String getCookedText(final String bundleName, final String key, final Locale loc, final Object... args) {
		return getRawText(bundleName, key, loc);
	}

	public static MessageProvider getCurrentInstance() {
		List<MessageProvider> msgProv = Factory.findApplicationServices(MessageProvider.class);
		if (msgProv.size() == 0)
			throw new IllegalStateException("No MessageProvider service found");
		return msgProv.get(0); // we take the first one.
	}

	/**
	 * Needed to reset a DB message cache
	 */
	public void resetCache() {
		List<RawMessageProvider> provs = Factory.findApplicationServices(RawMessageProvider.class);
		for (RawMessageProvider prov : provs)
			prov.resetCache();
	}

	public static void sResetCache() {
		getCurrentInstance().resetCache();
	}

}
