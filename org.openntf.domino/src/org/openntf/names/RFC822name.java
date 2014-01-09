/*
 * Copyright 2014
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
package org.openntf.names;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.openntf.arpa.RFC822name.Addr82xParts;
import org.openntf.domino.utils.Strings;

/**
 * Carrier and parsing object for various RFC822 name parts.
 * 
 * @author Devin S. Olsonm (dolson@czarnowski.com)
 * 
 * @see "RFC822: Standard for ARPA Internet Text Messages" http://www.w3.org/Protocols/rfc822/
 * 
 */
public class RFC822name extends HashMap<RFC822name.Key, String> implements Serializable {

	private static final Logger log_ = Logger.getLogger(RFC822name.class.getName());
	private static final long serialVersionUID = 1L;

	public static enum Key {
		Comment1, Comment2, Comment3, Local, Domain, Phrase;

		@Override
		public String toString() {
			return this.name();
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
	public RFC822name(final String source) {
		super();
		this.parse(source);
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
	public String getAddr821() {
		String local = this.getAddr822LocalPart();
		if (local.length() > 0) {
			String domain = this.getAddr822Domain();
			return (domain.length() > 0) ? local + "@" + domain : "";
		}

		return "";
	}

	public String getAddr822Comment(final int commentnumber) {
		Addr82xParts key = null;

		switch (commentnumber) {
		case 1:
			key = Addr82xParts.Comment1;
			break;
		case 2:
			key = Addr82xParts.Comment2;
			break;
		case 3:
			key = Addr82xParts.Comment3;
			break;
		default:
			return "";
		}

		String comment = this.get(key);
		return (comment.length() < 1) ? "" : "(" + comment + ")";

	}

	public String getAddr822Comment1() {
		return this.getAddr822Comment(1);
	}

	public String getAddr822Comment2() {
		return this.getAddr822Comment(2);
	}

	public String getAddr822Comment3() {
		return this.getAddr822Comment(3);
	}

	public String getAddr822Domain() {
		return this.get(Addr82xParts.Domain);
	}

	public String getAddr822LocalPart() {
		return this.get(Addr82xParts.Local);
	}

	public String getAddr822Full() {
		StringBuilder sb = new StringBuilder(this.getAddr822Simple());
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

	public String getAddr822FullFirstLast() {
		StringBuilder sb = new StringBuilder(this.getAddr822SimpleFirstLast());
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

	public String getAddr822Simple() {
		String addr821 = this.getAddr821();
		if (addr821.length() > 0) {
			StringBuilder sb = new StringBuilder("");
			String phrase = this.getAddr822Phrase();
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

	public String getAddr822SimpleFirstLast() {
		String addr821 = this.getAddr821();
		if (addr821.length() > 0) {
			StringBuilder sb = new StringBuilder("");
			String phrase = this.getAddr822PhraseFirstLast();
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

	public String getAddr822Phrase() {
		return this.get(Addr82xParts.Phrase);
	}

	public String getAddr822PhraseFirstLast() {
		String phrase = this.getAddr822Phrase();
		if (phrase.indexOf(',') > 0) {
			// assume name format of Lastname, Firstname and reverse
			// to format of Firstname Lastname
			final String[] chunks = phrase.split(",");
			final StringBuilder sb = new StringBuilder();
			for (int i = chunks.length - 1; i > -1; i--) {
				sb.append(Strings.toProperCase(chunks[i].trim()));
				sb.append(" ");
			}

			return sb.toString();

		} else if (phrase.indexOf('.') > 0) {
			// assume name format of Firstname.Lastname and strip
			// out the period
			final String[] chunks = phrase.split("\\.");
			final StringBuilder sb = new StringBuilder();
			for (final String s : chunks) {
				sb.append(Strings.toProperCase(s.trim()));
				sb.append(" ");
			}

			return sb.toString();
		}

		return phrase;
	}

	public boolean isHasRFC82xContent() {
		if (this.size() > 0) {
			for (String s : this.values()) {
				if ((null != s) && (s.trim().length() > 0)) {
					return true;
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
		String result = (null == key) ? "" : super.get(key);
		return (null == result) ? "" : result;
	}

	@Override
	public String put(final RFC822name.Key key, final String value) {
		return "";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("RFC822name");
		if (this.size() < 1) {
			return sb.toString();
		}

		sb.append(" [");
		Iterator<Map.Entry<RFC822name.Key, String>> it = this.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<RFC822name.Key, String> entry = it.next();
			sb.append(entry.getKey() + "=" + entry.getValue());
			if (it.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("]");
		return sb.toString();
	}

	public void parseRFC82xContent(final String source) {
		this.clear();
		this.parse(source);
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

	/**
	 * Retrieves and sets the various content values by parsing an input source string.
	 * 
	 * @param source
	 *            String from which to parse the content values.
	 */
	private void parse(final String source) {
		if ((null != source) && (source.length() > 0)) {
			final String pattern = "^.*<.*>.*$";
			/*
			 * Match Pattern: anytext<anytext>anytext
			 * 
			 * pattern definition:
			 * 
			 * ^ match the beginning of the string
			 * 
			 * . match any single character
			 * 
			 * * match the preceding match character zero or more times.
			 * 
			 * < match a less than character
			 * 
			 * . match any single character
			 * 
			 * * match the preceding match character zero or more times.
			 * 
			 * > match a greater than character
			 * 
			 * . match any single character
			 * 
			 * * match the preceding match character zero or more times.
			 * 
			 * $ match the preceding match instructions against the end of the string.
			 */
			if (source.matches(pattern)) {
				// test matches anytext<anytext>anytext
				// get the three primary chunks as phrase<internetaddress>comments from the source

				int idxLT = source.indexOf('<');
				int idxGT = source.indexOf('>', idxLT);

				// parse the phrase part
				String phrase = (idxLT > 0) ? source.substring(0, idxLT).trim() : "";
				if (phrase.length() > 0) {
					this.put(Key.Phrase, phrase.replaceAll("\"", "").trim());
				}

				// parse the internetaddress part
				String internetaddress = (idxGT > (idxLT + 1)) ? source.substring(idxLT + 1, idxGT).trim() : "";
				if ((internetaddress.length() > 0) && (internetaddress.indexOf('@') >= 0)) {
					String[] chunks = internetaddress.split("@");
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
				String comments = (idxGT < source.length()) ? source.substring(idxGT).trim() : "";
				if (comments.length() > 0) {
					int idxParenOpen = comments.indexOf('(');
					int idxParenClose = comments.indexOf(')');
					if ((idxParenOpen < 0) || (idxParenClose < 0) || (idxParenClose < idxParenOpen)) {
						// treat the entire comments string as a single comment.
						this.setAddr822Comment(1, comments.replaceAll("(", "").replaceAll(")", "").trim());
					} else {
						for (int commentnumber = 1; commentnumber < 4; commentnumber++) {
							String comment = comments.substring(idxParenOpen, idxParenClose).trim();
							this.setAddr822Comment(commentnumber, comment);

							idxParenOpen = comments.indexOf('(', idxParenClose);
							if (idxParenOpen < idxParenClose) {
								break;
							}
							idxParenClose = comments.indexOf(')', idxParenOpen);
							if (idxParenClose < idxParenOpen) {
								break;
							}
						}
					}
				}
			}
		}
	}
} // RFC822name
