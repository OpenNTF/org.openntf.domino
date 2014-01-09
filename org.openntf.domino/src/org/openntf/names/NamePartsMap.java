package org.openntf.names;

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Logger;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Strings;

/**
 * NamePartsMap carries the various component string values that make up a name.
 * 
 * @author Devin S. Olson (dolson@czarnowski.com)
 * 
 */
public class NamePartsMap extends HashMap<NamePartsMap.Key, String> implements Serializable {

	public static enum Key {
		Abbreviated, Addr821, Addr822Comment1, Addr822Comment2, Addr822Comment3, Addr822LocalPart, Addr822Phrase, ADMD, Canonical, Common, Country, Generation, Given, Initials, Keyword, Language, Organization, OrgUnit1, OrgUnit2, OrgUnit3, OrgUnit4, PRMD, Surname, IDprefix, SourceString;

		@Override
		public String toString() {
			return this.name();
		}

		public String getInfo() {
			return this.getDeclaringClass() + "." + this.getClass() + ":" + this.name();
		}
	};

	private static final Logger log_ = Logger.getLogger(NamePartsMap.class.getName());
	private static final long serialVersionUID = 1L;
	private RFC822name _rfc822name;

	/**
	 * * Zero-Argument Default Constructor
	 */
	public NamePartsMap() {
		super();
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

	public RFC822name getRFC822name() {
		if (null == this._rfc822name) {
			this._rfc822name = new RFC822name();
		}
		return this._rfc822name;
	}

	public void setRFC822name(final RFC822name rfc822name) {
		this._rfc822name = rfc822name;
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * other public methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	/**
	 * Clears the object.
	 */
	@Override
	public void clear() {
		super.clear();
		if (null != this._rfc822name) {
			this._rfc822name.clear();
		}
	}

	@Override
	public String get(final Object key) {
		return (key instanceof NamePartsMap.Key) ? this.get((NamePartsMap.Key) key) : "";
	}

	public String get(final NamePartsMap.Key key) {
		try {
			if (null == key) {
				throw new IllegalArgumentException("Key is null");
			}

			switch (key) {
			case Abbreviated: {
				String common = this.get(NamePartsMap.Key.Common);
				String ou1 = this.get(NamePartsMap.Key.OrgUnit1);
				String ou2 = this.get(NamePartsMap.Key.OrgUnit2);
				String ou3 = this.get(NamePartsMap.Key.OrgUnit3);
				String organization = this.get(NamePartsMap.Key.Organization);
				String country = this.get(NamePartsMap.Key.Country);

				StringBuffer sb = new StringBuffer("");
				if (!Strings.isBlankString(common)) {
					sb.append(common);
				}
				if (!Strings.isBlankString(ou3)) {
					sb.append("/" + ou3);
				}
				if (!Strings.isBlankString(ou2)) {
					sb.append("/" + ou2);
				}
				if (!Strings.isBlankString(ou1)) {
					sb.append("/" + ou1);
				}
				if (!Strings.isBlankString(organization)) {
					sb.append("/" + organization);
				}
				if (!Strings.isBlankString(country)) {
					sb.append("/" + country);
				}

				return sb.toString();
			}

			case Addr821:
				return this.getRFC822name().getAddr821();

			case Addr822Comment1:
				return this.getRFC822name().getAddr822Comment1();

			case Addr822Comment2:
				return this.getRFC822name().getAddr822Comment2();

			case Addr822Comment3:
				return this.getRFC822name().getAddr822Comment3();

			case Addr822LocalPart:
				return this.getRFC822name().getAddr822LocalPart();

			case Addr822Phrase:
				return this.getRFC822name().getAddr822Phrase();

			case Canonical: {
				String common = this.get(NamePartsMap.Key.Common);
				String ou1 = this.get(NamePartsMap.Key.OrgUnit1);
				String ou2 = this.get(NamePartsMap.Key.OrgUnit2);
				String ou3 = this.get(NamePartsMap.Key.OrgUnit3);
				String organization = this.get(NamePartsMap.Key.Organization);
				String country = this.get(NamePartsMap.Key.Country);

				StringBuffer sb = new StringBuffer("");
				if (!Strings.isBlankString(common)) {
					sb.append("CN=" + common);
				}
				if (!Strings.isBlankString(ou3)) {
					sb.append("/OU=" + ou3);
				}
				if (!Strings.isBlankString(ou2)) {
					sb.append("/OU=" + ou2);
				}
				if (!Strings.isBlankString(ou1)) {
					sb.append("/OU=" + ou1);
				}
				if (!Strings.isBlankString(organization)) {
					sb.append("/O=" + organization);
				}
				if (!Strings.isBlankString(country)) {
					sb.append("/C=" + country);
				}

				return sb.toString();
			}

			default:
				final String result = super.get(key);
				return (null == result) ? "" : result;
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	@Override
	public String put(final NamePartsMap.Key key, final String value) {
		try {
			if (null == key) {
				throw new IllegalArgumentException("Key is null");
			}
			if (Strings.isBlankString(value)) {
				if (this.containsKey(key)) {
					this.remove(key);
				}

				return "";
			}

			switch (key) {
			case Abbreviated: {
				this.parse(value);
				return this.get(Key.Abbreviated);
			}

			case Addr821:
				return this.getRFC822name().getAddr821();

			case Addr822Comment1:
				return this.getRFC822name().getAddr822Comment1();

			case Addr822Comment2:
				return this.getRFC822name().getAddr822Comment2();

			case Addr822Comment3:
				return this.getRFC822name().getAddr822Comment3();

			case Addr822LocalPart:
				return this.getRFC822name().getAddr822LocalPart();

			case Addr822Phrase:
				return this.getRFC822name().getAddr822Phrase();

			case Canonical: {
				this.parse(value);
				return this.get(Key.Canonical);
			}

			default:
				final String result = super.put(key, value);
				return (null == result) ? "" : result;
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
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

	/**
	 * Retrieves and sets the various name values by parsing an input source string.
	 * 
	 * @param source
	 *            String from which to parse the name values.
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

}
