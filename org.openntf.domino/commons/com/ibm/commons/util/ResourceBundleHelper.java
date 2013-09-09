/*
 * © Copyright IBM Corp. 2012-2013
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
 */

package com.ibm.commons.util;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

//import com.ibm.commons.log.LogException;

/**
 * A convenience class for dealing with resource bundles and formatting messages
 * 
 * @ibm-not_published
 */
public class ResourceBundleHelper {
	private ResourceBundle _bundle;
	private MessageFormat _formatter;
	private String _bundleName = "NULL"; //$NON-NLS-1$
	private final static int ERROR_CODE_LENGTH = 10;

	private static class StackCrawler extends SecurityManager {

		@Override
		public Class[] getClassContext() {
			return super.getClassContext();
		}

		public ClassLoader getCallingLoader() {
			//Class[] stack = getClassContext();
			return getClassContext()[3].getClassLoader();
		}
	}

	private static StackCrawler crawler = new StackCrawler();

	/**
	 * Constructor for ResourceBundleHelper.
	 * 
	 * @param bundleName
	 *            the fully-qualified bundle name
	 */
	public ResourceBundleHelper(final String bundleName) {
		ResourceBundle bundle = null;
		try {
			Class[] cls = crawler.getClassContext();
			//ClassLoader cl = crawler.getCallingLoader();
			Locale locale = Locale.getDefault();
			Throwable th = null;
			for (int i = 0; i < cls.length; i++) {
				try {
					bundle = ResourceBundle.getBundle(bundleName, locale, cls[i].getClassLoader());
					break;
				} catch (Throwable t) {
					th = t;
				}
			}
			_bundleName = bundleName;

			if (bundle == null) {
				throw th;
			}
		} catch (Throwable t) {
			System.out.println("Unable to load bundle " + bundleName); //$NON-NLS-1$
			System.out.println("Continuing with bundle=null"); //$NON-NLS-1$
			System.out.println("Printing StackTrace from ResourceBundleHelper(String bundleName)"); //$NON-NLS-1$
			t.printStackTrace();
		}

		init(bundle);
	}

	/**
	 * Constructor for ResourceBundleHelper.
	 * 
	 * @param bundleName
	 *            the fully-qualified bundle name
	 * @param locale
	 *            the Locale
	 */
	public ResourceBundleHelper(final String bundleName, final Locale locale) {
		ResourceBundle bundle = null;
		try {
			ClassLoader cl = crawler.getCallingLoader();
			bundle = ResourceBundle.getBundle(bundleName, locale, cl);
			_bundleName = bundleName;
		} catch (Throwable t) {
			System.out.println("Unable to load bundle " + bundleName); //$NON-NLS-1$
			System.out.println("Continuing with bundle=null"); //$NON-NLS-1$
			System.out.println("Printing StackTrace from ResourceBundleHelper(String bundleName, Locale locale)"); //$NON-NLS-1$
			t.printStackTrace();
		}
		_bundleName = bundleName;
		init(bundle);
	}

	/**
	 * Constructor for ResourceBundleHelper.
	 * 
	 * @param bundleName
	 *            the fully-qualified bundle name
	 * @param locale
	 *            the Locale
	 * @param cl
	 *            the class loader
	 */
	public ResourceBundleHelper(final String bundleName, final Locale locale, final ClassLoader cl) {
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, cl);
		_bundleName = bundleName;
		init(bundle);
	}

	/**
	 * Constructor for ResourceBundleHelper.
	 * 
	 * @param bundleName
	 *            the fully-qualified bundle name
	 * @param locales
	 *            the array of Locale
	 * @param cl
	 *            the class loader
	 */
	public ResourceBundleHelper(final String bundleName, final Locale[] locales, final ClassLoader cl) {
		int i = 0;
		ResourceBundle bundle;

		do {
			bundle = ResourceBundle.getBundle(bundleName, locales[i], cl);

			if (locales[i].equals(bundle.getLocale()))
				break;
			else
				i++;
		} while (locales[i] != null);

		_bundleName = bundleName;
		init(bundle);
	}

	/**
	 * Constructor for ResourceBundleHelper.
	 * 
	 * @param bundleName
	 *            the fully-qualified bundle name
	 * @param locales
	 *            the array of Locale
	 */
	public ResourceBundleHelper(final String bundleName, final Locale[] locales) {
		int i = 0;
		ResourceBundle bundle;

		do {
			ClassLoader cl = crawler.getCallingLoader();
			bundle = ResourceBundle.getBundle(bundleName, locales[i], cl);
			if (locales[i].equals(bundle.getLocale())) {
				break;
			} else {
				i++;
			}
		} while (locales[i] != null);

		_bundleName = bundleName;
		init(bundle);
	}

	/**
	 * Constructor for ResourceBundleHelper.
	 * 
	 * @param bundleName
	 *            the fully-qualified bundle name
	 * @param cl
	 *            the class loader
	 */
	public ResourceBundleHelper(final String bundleName, final ClassLoader cl) {
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, Locale.getDefault(), cl);
		_bundleName = bundleName;
		init(bundle);
	}

	/**
	 * Constructor for ResourceBundleHelper.
	 * 
	 * @param bundle
	 *            the ResourceBundle we're helping
	 */
	public ResourceBundleHelper(final ResourceBundle bundle) {
		init(bundle);
		_bundleName = bundle.getClass().getName();
	}

	private void init(final ResourceBundle bundle) {
		if (null != bundle) {
			_bundle = bundle;
			_formatter = new MessageFormat(""); //$NON-NLS-1$
			_formatter.setLocale(_bundle.getLocale());
		} else {
			_bundle = null;
			_formatter = new MessageFormat(""); //$NON-NLS-1$
		}
	}

	/**
	 * Constructor for ResourceBundleHelper.
	 */
	private ResourceBundleHelper() {
		super();
	}

	/**
	 * Get the name of this bundle
	 * 
	 * @return BundleName
	 */

	public String getBundleName() {
		return _bundleName;
	}

	/**
	 * Allows the _bundleName to be set if the ResourceBundleHelper(ResourceBundle) <br>
	 * constructor is used since the ResourceBundle.name is not accessible
	 */

	public void setBundleName(final String bn) {
		if (_bundleName == null) {
			_bundleName = bn;
		}
	}

	public Locale getLocale() {
		return _bundle.getLocale();
	}

	public Enumeration getKeys() {
		return _bundle.getKeys();
	}

	/**
	 * Get the error code for this resource
	 * 
	 * @param the
	 *            resource key
	 * @exception LogException
	 *                The error code is a ten digit string that starts after the equal sign and identifies a specific error. The format is:
	 *                key=0123456789My Error Text.
	 * 
	 */
	public String getErrorCode(final String key) {
		String msg = _bundle.getString(key);
		int startPos = msg.indexOf("=") + 1; //We start one position to the //$NON-NLS-1$
												// left of the equal sign
		int endPos = startPos + ERROR_CODE_LENGTH;
		return msg.substring(startPos, endPos);

	}

	/**
	 * Convenience method to return a string with argument replacement
	 * 
	 * @param key
	 *            the resource key
	 * @return String
	 */
	public String getString(final String key) {
		try {
			return _bundle.getString(key);
		} catch (Throwable t) {
			System.err.println(t.getLocalizedMessage());
			return key;
		}
	}

	/**
	 * Convenience method to return a string with argument replacement
	 * 
	 * @param key
	 *            the resource key
	 * @param params
	 *            an array of arguments
	 * @return String
	 */
	public String getString(final String key, final Object[] params) {
		return MessageFormat.format(_bundle.getString(key), params);
	}

	/**
	 * Convenience method to return a string with argument replacement
	 * 
	 * @param key
	 *            the resource key
	 * @param param1
	 * @return String
	 */
	public String getString(final String key, final String param1) {
		String params[] = new String[] { param1 };
		return getString(key, (Object[]) params);
	}

	/**
	 * Convenience method to return a string with argument replacement
	 * 
	 * @param key
	 *            the resource key
	 * @param param1
	 * @param param2
	 * @return String
	 */
	public String getString(final String key, final String param1, final String param2) {
		String params[] = new String[] { param1, param2 };
		return getString(key, (Object[]) params);
	}

	/**
	 * Convenience method to return a string with argument replacement
	 * 
	 * @param key
	 *            the resource key
	 * @param param1
	 * @param param2
	 * @param param3
	 * @return String
	 */
	public String getString(final String key, final String param1, final String param2, final String param3) {
		String params[] = new String[] { param1, param2, param3 };
		return getString(key, (Object[]) params);
	}

	/**
	 * Convenience method to return a string with argument replacement
	 * 
	 * @param key
	 *            the resource key
	 * @param param1
	 * @param param2
	 * @param param3
	 * @param param4
	 * @return String
	 */
	public String getString(final String key, final String param1, final String param2, final String param3, final String param4) {
		String params[] = new String[] { param1, param2, param3, param4 };
		return getString(key, (Object[]) params);
	}

	/**
	 * Convenience method to return a string with argument replacement
	 * 
	 * @param key
	 *            the resource key
	 * @param params
	 * @return String
	 */
	public String getString(final String key, final String[] params) {
		return getString(key, (Object[]) params);
	}
}
