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
package org.openntf.arpa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Logger;

import org.openntf.domino.utils.Strings;

/**
 * Carrier and parsing object for various RFC822 name parts.
 * 
 * @author Devin S. Olsonm (dolson@czarnowski.com)
 * 
 * @see "RFC822: Standard for ARPA Internet Text Messages" http://www.w3.org/Protocols/rfc822/
 * 
 */
public class RFC822name implements Serializable {
	private static final Logger log_ = Logger.getLogger(RFC822name.class.getName());
	private static final long serialVersionUID = 1L;

	public static enum Addr82xParts {
		Comment1, Comment2, Comment3, Local, Domain, Phrase;

		@Override
		public String toString() {
			return this.name();
		}

		public String getInfo() {
			return this.getDeclaringClass() + "." + this.getClass() + ":" + this.name();
		}
	};

	private HashMap<Addr82xParts, String> _content;

	/**
	 * Zero Argument Constructor
	 */
	public RFC822name() {
	}

	/**
	 * Default Constructor
	 * 
	 * @param source
	 *            String from which to construct the object
	 */
	public RFC822name(final String source) {
		this.parseContent(source);
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * Serializable getters & setters
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	public HashMap<Addr82xParts, String> getContent() {
		if (null == this._content) {
			this._content = new HashMap<Addr82xParts, String>();
		}

		return this._content;
	}

	public void setContent(final HashMap<Addr82xParts, String> content) {
		this._content = content;
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

		String comment = this.getPart(key);
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
		return this.getPart(Addr82xParts.Domain);
	}

	public String getAddr822LocalPart() {
		return this.getPart(Addr82xParts.Local);
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
		return this.getPart(Addr82xParts.Phrase);
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

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * other methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */
	public String getPart(final Addr82xParts key) {
		String result = (null == key) ? "" : this.getContent().get(key);
		return (null == result) ? "" : result;
	}

	public void setPart(final Addr82xParts key, final String value) {
		this.getContent().put(key, value);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RFC822name [content=" + _content + "]";
	}

	public void clear() {
		if (null != this._content) {
			this._content.clear();
		}
	}

	public void parseRFC82xContent(final String source) {
		this.clear();
		this.parseContent(source);
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
			return;
		}

		this.setPart(key, comment);
	}

	/**
	 * Retrieves and sets the various content values by parsing an input source string.
	 * 
	 * @param source
	 *            String from which to parse the content values.
	 */
	private void parseContent(final String source) {
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
					this.setPart(Addr82xParts.Phrase, phrase.replaceAll("\"", "").trim());
				}

				// parse the internetaddress part
				String internetaddress = (idxGT > (idxLT + 1)) ? source.substring(idxLT + 1, idxGT).trim() : "";
				if ((internetaddress.length() > 0) && (internetaddress.indexOf('@') >= 0)) {
					String[] chunks = internetaddress.split("@");
					if (null != chunks) {
						if (null != chunks[0]) {
							this.setPart(Addr82xParts.Local, chunks[0].trim());
							if ((2 <= chunks.length) && (null != chunks[1])) {
								this.setPart(Addr82xParts.Domain, chunks[1].trim());
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

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * hashCode and equals
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_content == null) ? 0 : _content.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RFC822name)) {
			return false;
		}
		RFC822name other = (RFC822name) obj;
		if (_content == null) {
			if (other._content != null) {
				return false;
			}
		} else if (!_content.equals(other._content)) {
			return false;
		}
		return true;
	}

} // RFC822name
