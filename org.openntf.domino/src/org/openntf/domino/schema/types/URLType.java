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
	private static final Logger log_ = Logger.getLogger(URLType.class.getName());
	private static final long serialVersionUID = 1L;
	//URL Regex pattern from http://mathiasbynens.be/demo/url-regex
	//MIT licensed at https://gist.github.com/dperini/729294
	//Copyright (c) 2010-2013 Diego Perini (http://www.iport.it)

	public static final String URL_REGEX_SOURCE = "^"
			+ "(?:(?:https?|ftp)://)"
			// protocol identifier
			+ "(?:\\\\S+(?::\\\\S*)?@)?"
			// user:pass authentication
			+ "(?:" + "(?!10(?:\\\\.\\\\d{1,3}){3})" + "(?!127(?:\\\\.\\\\d{1,3}){3})" + "(?!169\\\\.254(?:\\\\.\\\\d{1,3}){2})"
			+ "(?!192\\\\.168(?:\\\\.\\\\d{1,3}){2})"
			+ "(?!172\\\\.(?:1[6-9]|2\\\\d|3[0-1])(?:\\\\.\\\\d{1,3}){2})"
			// IP address exclusion
			// private & local networks
			+ "(?:[1-9]\\\\d?|1\\\\d\\\\d|2[01]\\\\d|22[0-3])" + "(?:\\\\.(?:1?\\\\d{1,2}|2[0-4]\\\\d|25[0-5])){2}"
			+ "(?:\\\\.(?:[1-9]\\\\d?|1\\\\d\\\\d|2[0-4]\\\\d|25[0-4]))" + "|"
			// IP address dotted notation octets
			// excludes loopback network 0.0.0.0
			// excludes reserved space >= 224.0.0.0
			// excludes network & broacast addresses
			// (first & last IP address of each class)
			+ "(?:(?:[a-z\\\\u00a1-\\\\uffff0-9]+-?)*[a-z\\\\u00a1-\\\\uffff0-9]+)"
			// host name
			+ "(?:\\\\.(?:[a-z\\\\u00a1-\\\\uffff0-9]+-?)*[a-z\\\\u00a1-\\\\uffff0-9]+)*"
			// domain name
			+ "(?:\\\\.(?:[a-z\\\\u00a1-\\\\uffff]{2,}))" + ")"
			// TLD identifier
			+ "(?::\\\\d{2,5})?"
			// port number
			+ "(?:/[^\\\\s]*)?"
			// resource path
			+ "$";
	public static final Pattern URL_REGEX = Pattern.compile(URL_REGEX_SOURCE, Pattern.CASE_INSENSITIVE);

	/**
	 * 
	 */
	public URLType() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.schema.types.IDominoType#getUITypeName()
	 */
	@Override
	public String getUITypeName() {
		return "URL (Web Address)";
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
