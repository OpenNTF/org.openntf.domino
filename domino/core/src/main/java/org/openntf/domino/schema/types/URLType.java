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
/**
 * 
 */
package org.openntf.domino.schema.types;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.openntf.domino.schema.exceptions.ItemException;

/**
 * @author nfreeman
 * 
 */
public class URLType extends StringType {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(URLType.class.getName());
	//URL Regex pattern from http://mathiasbynens.be/demo/url-regex
	//MIT licensed at https://gist.github.com/dperini/729294
	//Copyright (c) 2010-2013 Diego Perini (http://www.iport.it)
	public static final String URL_REGEX_SOURCE = "^" //$NON-NLS-1$
			+ "(?:(?:https?|ftp)://)" //$NON-NLS-1$
			// protocol identifier
			+ "(?:\\\\S+(?::\\\\S*)?@)?" //$NON-NLS-1$
			// user:pass authentication
			+ "(?:" + "(?!10(?:\\\\.\\\\d{1,3}){3})" + "(?!127(?:\\\\.\\\\d{1,3}){3})" + "(?!169\\\\.254(?:\\\\.\\\\d{1,3}){2})" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			+ "(?!192\\\\.168(?:\\\\.\\\\d{1,3}){2})" //$NON-NLS-1$
			+ "(?!172\\\\.(?:1[6-9]|2\\\\d|3[0-1])(?:\\\\.\\\\d{1,3}){2})" //$NON-NLS-1$
			// IP address exclusion
			// private & local networks
			+ "(?:[1-9]\\\\d?|1\\\\d\\\\d|2[01]\\\\d|22[0-3])" + "(?:\\\\.(?:1?\\\\d{1,2}|2[0-4]\\\\d|25[0-5])){2}" //$NON-NLS-1$ //$NON-NLS-2$
			+ "(?:\\\\.(?:[1-9]\\\\d?|1\\\\d\\\\d|2[0-4]\\\\d|25[0-4]))" + "|" //$NON-NLS-1$ //$NON-NLS-2$
			// IP address dotted notation octets
			// excludes loopback network 0.0.0.0
			// excludes reserved space >= 224.0.0.0
			// excludes network & broacast addresses
			// (first & last IP address of each class)
			+ "(?:(?:[a-z\\\\u00a1-\\\\uffff0-9]+-?)*[a-z\\\\u00a1-\\\\uffff0-9]+)" //$NON-NLS-1$
			// host name
			+ "(?:\\\\.(?:[a-z\\\\u00a1-\\\\uffff0-9]+-?)*[a-z\\\\u00a1-\\\\uffff0-9]+)*" //$NON-NLS-1$
			// domain name
			+ "(?:\\\\.(?:[a-z\\\\u00a1-\\\\uffff]{2,}))" + ")" //$NON-NLS-1$ //$NON-NLS-2$
			// TLD identifier
			+ "(?::\\\\d{2,5})?" //$NON-NLS-1$
			// port number
			+ "(?:/[^\\\\s]*)?" //$NON-NLS-1$
			// resource path
			+ "$"; //$NON-NLS-1$
	public static final Pattern URL_REGEX = Pattern.compile(URL_REGEX_SOURCE, Pattern.CASE_INSENSITIVE);

	/**
	 * 
	 */
	public URLType() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.schema.types.IDominoType#getUITypeName()
	 */
	@Override
	public String getUITypeName() {
		return "URL (Web Address)"; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.schema.types.StringType#validateValue(java.lang.Object)
	 */
	@Override
	public boolean validateValue(final Object value) throws ItemException {
		if (super.validateValue(value)) {
			//TODO further validation
			return true;
		}
		return false;
	}
}
