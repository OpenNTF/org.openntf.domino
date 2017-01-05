/*
 * Copyright 2013
 * 
 * @author Devin S. Olson (dolson@czarnowski.com)
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
package org.openntf.arpa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openntf.domino.utils.Strings;

/**
 * Carrier and parsing object for various RFC822 name parts.
 * 
 * @author Devin S. Olsonm (dolson@czarnowski.com)
 * 
 * @see "RFC822: Standard for ARPA Internet Text Messages" http://www.w3.org/Protocols/rfc822/
 * 
 */
@SuppressWarnings("javadoc")
public class RFC822name extends HashMap<RFC822name.Key, String> implements Serializable {

	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(RFC822name.class.getName());
	private static final long serialVersionUID = 1L;

	public static enum Key {
		Comment1, Comment2, Comment3, Local, Domain, Phrase;

		@Override
		public String toString() {
			return Key.class.getName() + ": " + this.name();
		}

		public String getInfo() {
			return this.getDeclaringClass() + "." + this.getClass() + ":" + this.name();
		}
	};

	/**
	 * Zero Argument Constructor
	 */
	public RFC822name() {
		super();
	}

	/**
	 * Default Constructor
	 * 
	 * @param source
	 *            String from which to construct the object
	 */
	public RFC822name(final String string) {
		super();
		this.parse(string);
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * RFC82x getters
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	/**
	 * Gets the ARPA Internet Name Addr821 component of the object
	 * 
	 * @return the Addr821 component
	 */
	public String getAddr821() {
		final String local = this.getAddr822LocalPart();
		if (local.length() > 0) {
			final String domain = this.getAddr822Domain();
			return (domain.length() > 0) ? local + "@" + domain : "";
		}

		return "";
	}

	/**
	 * Gets the ARPA Internet Name getAddr822Comment for the specified comment number (1 - 3)
	 * 
	 * @param commentnumber
	 *            Number of the comment (1 - 3) component to retrieve.
	 * 
	 * @return the Addr822Comment component specified by the comment number
	 */
	public String getAddr822Comment(final int commentnumber) {
		Key key = null;

		switch (commentnumber) {
		case 1:
			key = Key.Comment1;
			break;
		case 2:
			key = Key.Comment2;
			break;
		case 3:
			key = Key.Comment3;
			break;
		default:
			return "";
		}

		final String comment = this.get(key);
		return (comment.length() < 1) ? "" : "(" + comment + ")";

	}

	/**
	 * Gets the ARPA Internet Name Addr822Comment1 component of the object
	 * 
	 * @return the Addr822Comment1 component
	 */
	public String getAddr822Comment1() {
		return this.getAddr822Comment(1);
	}

	/**
	 * Gets the ARPA Internet Name Addr822Comment2 component of the object
	 * 
	 * @return the Addr822Comment2 component
	 */
	public String getAddr822Comment2() {
		return this.getAddr822Comment(2);
	}

	/**
	 * Gets the ARPA Internet Name Addr822Comment3 component of the object
	 * 
	 * @return the Addr822Comment3 component
	 */
	public String getAddr822Comment3() {
		return this.getAddr822Comment(3);
	}

	/**
	 * Gets the ARPA Internet Name Addr822Domain component of the object
	 * 
	 * @return the Addr822Domain component
	 */
	public String getAddr822Domain() {
		return this.get(Key.Domain);
	}

	/**
	 * Gets the ARPA Internet Name Addr822LocalPart component of the object
	 * 
	 * @return the Addr822LocalPart component
	 */
	public String getAddr822LocalPart() {
		return this.get(Key.Local);
	}

	/**
	 * Gets the ARPA Internet Name Addr822Full component of the object
	 * 
	 * @return the Addr822Full component
	 */
	public String getAddr822Full() {
		final StringBuilder sb = new StringBuilder(this.getAddr822Simple());
		if (sb.length() > 0) {
			if (this.getAddr822Comment1().length() > 0) {
				sb.append(" " + this.getAddr822Comment1());
			}
			if (this.getAddr822Comment2().length() > 0) {
				sb.append(" " + this.getAddr822Comment2());
			}
			if (this.getAddr822Comment3().length() > 0) {
				sb.append(" " + this.getAddr822Comment3());
			}
		}

		return sb.toString();
	}

	/**
	 * Gets the ARPA Internet Name Addr822Full component of the object, formatted with the Phrase component in FIRST LAST order
	 * 
	 * @return the Addr822Full component
	 */
	public String getAddr822FullFirstLast() {
		final StringBuilder sb = new StringBuilder(this.getAddr822SimpleFirstLast());
		if (sb.length() > 0) {
			if (this.getAddr822Comment1().length() > 0) {
				sb.append(" " + this.getAddr822Comment1());
			}
			if (this.getAddr822Comment2().length() > 0) {
				sb.append(" " + this.getAddr822Comment2());
			}
			if (this.getAddr822Comment3().length() > 0) {
				sb.append(" " + this.getAddr822Comment3());
			}
		}

		return sb.toString();
	}

	/**
	 * Gets the ARPA Internet Name Addr822Simple component of the object
	 * 
	 * @return the Addr822Simple component
	 */
	public String getAddr822Simple() {
		final String addr821 = this.getAddr821();
		if (addr821.length() > 0) {
			final StringBuilder sb = new StringBuilder("");
			final String phrase = this.getAddr822Phrase();
			if (phrase.length() > 0) {
				sb.append("\"");
				sb.append(phrase);
				sb.append("\" ");
			}

			sb.append("<");
			sb.append(addr821);
			sb.append(">");

			return sb.toString();
		}

		return "";
	}

	/**
	 * Gets the ARPA Internet Name Addr822Simple component of the object, formatted with the Phrase component in FIRST LAST order
	 * 
	 * @return the Addr822Simple component
	 */
	public String getAddr822SimpleFirstLast() {
		final String addr821 = this.getAddr821();
		if (addr821.length() > 0) {
			final StringBuilder sb = new StringBuilder("");
			final String phrase = this.getAddr822PhraseFirstLast();
			if (phrase.length() > 0) {
				sb.append("\"");
				sb.append(phrase);
				sb.append("\" ");
			}

			sb.append("<");
			sb.append(addr821);
			sb.append(">");

			return sb.toString();
		}

		return "";
	}

	/**
	 * Gets the ARPA Internet Name Addr822Phrase component of the object
	 * 
	 * @return the Addr822Phrase component
	 */
	public String getAddr822Phrase() {
		return this.get(Key.Phrase);
	}

	/**
	 * Gets the ARPA Internet Name Addr822Phrase component of the object, formatted with the Phrase component in FIRST LAST order
	 * 
	 * @return the Addr822Phrase component
	 */
	public String getAddr822PhraseFirstLast() {
		final StringBuilder sb = new StringBuilder("");
		final String phrase = this.getAddr822Phrase();
		if (phrase.indexOf(',') > 0) {
			// assume name format of Lastname, Firstname and reverse
			// to format of Firstname Lastname
			final String[] chunks = phrase.split(",");
			for (int i = chunks.length - 1; i > -1; i--) {
				sb.append(ISO.toProperCase(chunks[i].trim()));
				sb.append(" ");
			}

		} else if (phrase.indexOf('.') > 0) {
			// assume name format of Firstname.Lastname and strip
			// out the period
			final String[] chunks = phrase.split("\\.");
			for (final String s : chunks) {
				sb.append(ISO.toProperCase(s.trim()));
				sb.append(" ");
			}

		} else {
			sb.append(phrase);
		}

		return sb.toString();
	}

	/**
	 * Indicates whether the object has RFC82xContent
	 * 
	 * @return Flag indicating if the object has RFC82xContent
	 */

	public boolean isHasRFC82xContent() {
		if (this.size() > 0) {
			for (final String s : this.values()) {
				if ((null != s) && (s.trim().length() > 0)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Determines if any of the mapped values are equal to the passed in string.
	 * 
	 * Performs a case-insensitive search.
	 * 
	 * @param string
	 *            String tom compare values against
	 * 
	 * @return Flag indicating if any of the values are equal to the string.
	 */
	public boolean equalsIgnoreCase(final String string) {
		if (!ISO.isBlankString(string)) {
			for (final String s : this.values()) {
				if (string.equalsIgnoreCase(s)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Determines if any of the mapped values begin with the prefix.
	 * 
	 * @param prefix
	 *            Value to compare to the mapped values.
	 * 
	 * @param casesensitive
	 *            Flag indicating if Case-Sensitive comparisons should be enforced.
	 * 
	 * @return Flag indicating if any of the mapped values begin with the prefix
	 */
	public boolean startsWith(final String prefix, final boolean casesensitive) {
		if (!ISO.isBlankString(prefix)) {
			if (casesensitive) {
				for (final String s : this.values()) {
					if ((null != s) && s.startsWith(prefix)) {
						return true;
					}
				}

			} else {
				for (final String s : this.values()) {
					if (Strings.startsWithIgnoreCase(s, prefix)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * other methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	@Override
	public String get(final Object key) {
		return (key instanceof RFC822name.Key) ? this.get((RFC822name.Key) key) : "";
	}

	public String get(final RFC822name.Key key) {
		final String result = (null == key) ? "" : super.get(key);
		return (null == result) ? "" : result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("RFC822name");
		if (this.size() < 1) {
			return sb.toString();
		}

		sb.append(" [");
		final Iterator<Map.Entry<RFC822name.Key, String>> it = this.entrySet().iterator();
		while (it.hasNext()) {
			final Map.Entry<RFC822name.Key, String> entry = it.next();
			sb.append(entry.getKey() + "=" + entry.getValue());
			if (it.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("]");
		return sb.toString();
	}

	/**
	 * Parses the source string and sets the appropriate RFC822 values.
	 * 
	 * @param string
	 *            RFC822 source string from which to set the appropriate RFC822 values.
	 */
	public void parseRFC82xContent(final String string) {
		this.clear();
		this.parse(string);
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * PUBLIC STATIC methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */
	/**
	 * Generates an RFC822 Addr822Full Address String from the specified component parts.
	 * 
	 * @param phrase
	 *            Addr822Phrase part used to construct the result.
	 * @param addr821
	 *            Addr821 part used to construct the result.
	 * @param comments
	 *            Addr822Comment1, Addr822Comment2, and Addr822Comment2 parts used to construct the result.
	 * @return properly formatted RFC822 Addr822Full string generated from the component parts. Empty string on error or no value for
	 *         addr821.
	 */
	public static String buildAddr822Full(final String phrase, final String addr821, final String... comments) {

		if ((null != addr821) && (addr821.trim().length() > 0)) {
			final StringBuilder sb = new StringBuilder((null == phrase) ? "" : phrase.trim());
			sb.append("<");
			sb.append(addr821);
			sb.append(">");

			if (null != comments) {
				int idx = 0;
				for (final String comment : comments) {
					if (!ISO.isBlankString(comment)) {
						sb.append("(");
						sb.append(comment);
						sb.append(")");
						idx++;
						if (idx > 2) {
							break;
						}
					}
				}

			}

			return sb.toString();
		}

		return "";
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * private methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	public void setAddr822Comment(final int commentnumber, final String comment) {
		Key key = null;

		switch (commentnumber) {
		case 1:
			key = Key.Comment1;
			break;
		case 2:
			key = Key.Comment2;
			break;
		case 3:
			key = Key.Comment3;
			break;
		default:
			return;
		}

		this.put(key, comment);
	}

	public static Pattern PARENS_MATCH = Pattern.compile("\\(.+?\\)");
	public static Pattern INPARENS_MATCH = Pattern.compile("/\\(([^()]+)\\)");

	/**
	 * Retrieves and sets the various content values by parsing an input source string.
	 * 
	 * @param string
	 *            String from which to parse the content values.
	 */
	private void parse(final String string) {
		if (!ISO.isBlankString(string)) {
			final Matcher matcher = ISO.PatternRFC822.matcher(string);
			if (matcher.matches()) {
				/*
				 * test matches anytext<anytext>anytext
				 * 
				 * get the three primary chunks as
				 * phrase<internetaddress>comments from the source
				 */

				final int idxLT = string.indexOf('<');
				final int idxGT = string.indexOf('>', idxLT);

				// parse the phrase part
				final String phrase = (idxLT > 0) ? string.substring(0, idxLT).trim() : "";
				if (phrase.length() > 0) {
					this.put(Key.Phrase, phrase.replaceAll("\"", "").trim());
				}

				// parse the internetaddress part
				final String internetaddress = (idxGT > (idxLT + 1)) ? string.substring(idxLT + 1, idxGT).trim() : "";
				if ((internetaddress.length() > 0) && (internetaddress.indexOf('@') >= 0)) {
					final String[] chunks = internetaddress.split("@");
					if (null != chunks) {
						if (null != chunks[0]) {
							this.put(Key.Local, chunks[0].trim());
							if ((2 <= chunks.length) && (null != chunks[1])) {
								this.put(Key.Domain, chunks[1].trim());
							}
						}
					}
				}

				// parse the comments part
				final String comments = (idxGT < string.length()) ? string.substring(idxGT).trim() : "";
				if (comments.length() > 0) {
					if (!comments.startsWith("(")) {
						this.setAddr822Comment(1, comments.replaceAll("\\(", " ").replaceAll("\\)", " ").trim());
					} else {
						final String[] commentSet = RFC822name.INPARENS_MATCH.split(comments);
						for (int i = 0; i < commentSet.length; i++) {
							if (i < 3) {
								this.setAddr822Comment(i + 1, commentSet[i]);
							} else {
								break;
							}
						}
					}
				}
			}
		}
	}
}
