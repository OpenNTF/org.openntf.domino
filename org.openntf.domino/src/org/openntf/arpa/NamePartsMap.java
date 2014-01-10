package org.openntf.arpa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Logger;

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

	public static enum CanonicalKey {
		CN("Common Name"), OU("Organizational Unit"), O("Organization"), C("Country Code");
		private String _label;

		public String getLabel() {
			return this._label;
		}

		private void setLabel(final String label) {
			this._label = label;
		}

		private CanonicalKey(final String label) {
			this.setLabel(label);
		}

	}

	private static final Logger log_ = Logger.getLogger(NamePartsMap.class.getName());
	private static final long serialVersionUID = 1L;
	private RFC822name _rfc822name;

	/**
	 * * Zero-Argument Constructor
	 */
	public NamePartsMap() {
		super();
	}

	/**
	 * Default Constructor
	 * 
	 * @param source
	 *            String from which to construct the object
	 */
	public NamePartsMap(final String source) {
		super();
		this.parse(source);
	}

	/**
	 * Optional Constructor
	 * 
	 * @param source
	 *            String from which to construct the object
	 * @param rfc822name
	 *            RFC822name for the object.
	 */
	public NamePartsMap(final String source, final RFC822name rfc822name) {
		super();
		this.parse(source);
		this.setRFC822name(rfc822name);
	}

	/**
	 * Default Constructor
	 * 
	 * @param source
	 *            String from which to construct the object
	 * @param rfc822string
	 *            String from which to construct the RFC822name for the object.
	 */
	public NamePartsMap(final String source, final String rfc822string) {
		super();
		this.parse(source);
		this.parseRFC82xContent(rfc822string);
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(NamePartsMap.class.getName());
		sb.append(" [");
		for (Key key : Key.values()) {
			String s = this.get(key);
			if (!Strings.isBlankString(s)) {
				sb.append(key.name() + "=" + s);
			}
		}

		sb.append("]");

		return sb.toString();
	}

	public String get(final NamePartsMap.Key key) {
		if (null != key) {
			switch (key) {
			case Abbreviated: {
				String common = this.get(NamePartsMap.Key.Common);
				String ou1 = this.get(NamePartsMap.Key.OrgUnit1);
				String ou2 = this.get(NamePartsMap.Key.OrgUnit2);
				String ou3 = this.get(NamePartsMap.Key.OrgUnit3);
				String ou4 = this.get(NamePartsMap.Key.OrgUnit4);
				String organization = this.get(NamePartsMap.Key.Organization);
				String country = this.get(NamePartsMap.Key.Country);

				StringBuffer sb = new StringBuffer("");
				if (!ISO.isBlankString(common)) {
					sb.append(common);
				}
				if (!ISO.isBlankString(ou4)) {
					sb.append("/OU=" + ou4);
				}
				if (!ISO.isBlankString(ou3)) {
					sb.append("/" + ou3);
				}
				if (!ISO.isBlankString(ou2)) {
					sb.append("/" + ou2);
				}
				if (!ISO.isBlankString(ou1)) {
					sb.append("/" + ou1);
				}
				if (!ISO.isBlankString(organization)) {
					sb.append("/" + organization);
				}
				if (!ISO.isBlankString(country)) {
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
				String ou4 = this.get(NamePartsMap.Key.OrgUnit4);
				String organization = this.get(NamePartsMap.Key.Organization);
				String country = this.get(NamePartsMap.Key.Country);

				StringBuffer sb = new StringBuffer("");
				if (!ISO.isBlankString(common)) {
					sb.append("CN=" + common);
				}
				if (!ISO.isBlankString(ou4)) {
					sb.append("/OU=" + ou4);
				}
				if (!ISO.isBlankString(ou3)) {
					sb.append("/OU=" + ou3);
				}
				if (!ISO.isBlankString(ou2)) {
					sb.append("/OU=" + ou2);
				}
				if (!ISO.isBlankString(ou1)) {
					sb.append("/OU=" + ou1);
				}
				if (!ISO.isBlankString(organization)) {
					sb.append("/O=" + organization);
				}
				if (!ISO.isBlankString(country)) {
					sb.append("/C=" + country);
				}

				return sb.toString();
			}

			default:
				final String result = super.get(key);
				return (null == result) ? "" : result;
			}
		}

		return "";
	}

	@Override
	public String put(final NamePartsMap.Key key, final String value) {
		if (null != key) {
			if (ISO.isBlankString(value)) {
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

			case IDprefix:
				return this.getIDprefix();

			default:
				final String result = super.put(key, value);
				return (null == result) ? "" : result;
			}
		}

		return "";
	}

	public String getIDprefix() {
		String result = super.get(Key.IDprefix);
		if (Strings.isBlankString(result)) {

			String common = this.get(Key.Common);
			if (null != common) {

				final String alphanumericandspacacesonly = common.trim().toUpperCase().replaceAll("[^A-Za-z0-9 ]", "");
				final int idx = alphanumericandspacacesonly.indexOf(" ");
				String firstname = alphanumericandspacacesonly;
				String lastname = alphanumericandspacacesonly;

				if (idx > 0) {
					final String[] chunks = alphanumericandspacacesonly.split(" ");
					firstname = chunks[0].trim().replaceAll("[^A-Za-z0-9]", "");
					lastname = chunks[chunks.length - 1].replaceAll("[^A-Za-z0-9]", "");
				}

				final StringBuilder sb = new StringBuilder(firstname.substring(0, 1));
				sb.append(lastname.substring(0, 2));
				sb.append(lastname.substring(lastname.length() - 1));
				while (sb.length() < 4) {
					sb.append("X");
				}

				result = sb.toString();
				this.put(Key.IDprefix, result);
			}
		}

		return result;
	}

	public void parseRFC82xContent(final String source) {
		this.getRFC822name().parseRFC82xContent(source);
	}

	public boolean isHasRFC82xContent() {
		return (null == this._rfc822name) ? false : this.getRFC822name().isHasRFC82xContent();
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
		String common = "";
		String[] ous = new String[] { "", "", "", "" };
		String organization = "";
		String country = "";

		if ((!ISO.isBlankString(source)) && (source.indexOf('/') > 0)) {
			// break the source into it's component words and parse them

			String[] words = source.split("/");
			int length = words.length;
			if (length > 0) {
				int idx = 0;

				if (source.indexOf('=') > 0) {
					// use canonical logic
					for (int i = words.length - 1; i >= 0; i--) {
						String[] nibbles = words[i].trim().split("=");
						String key = nibbles[0];
						String value = nibbles[1];

						if (CanonicalKey.C.name().equals(key)) {
							country = value;
						} else if (CanonicalKey.O.name().equals(key)) {
							organization = value;
						} else if (CanonicalKey.OU.name().equals(key)) {
							ous[idx] = value;
							idx++;
						} else if (CanonicalKey.CN.name().equals(key)) {
							common = value;
						}
					}

				} else {
					// use abbreviated logic
					common = words[0].trim();
					if (length > 1) {
						int orgpos = length;
						organization = words[orgpos];
						if (ISO.isCountryCode(organization)) {
							// organization could be a country code, 
							if (orgpos > 1) {
								// Treat organization as a country code and re-aquire the organization
								country = organization;
								orgpos--;
								organization = words[orgpos];
							}
						}

						int oupos = orgpos - 1;
						while (oupos > 0) {
							ous[idx] = words[oupos];

							oupos--;
							idx++;
							if (idx > 3) {
								break;
							}
						}
					}
				}
			}
		}

		this.put(Key.Common, common);
		this.put(Key.OrgUnit1, ous[0]);
		this.put(Key.OrgUnit2, ous[1]);
		this.put(Key.OrgUnit3, ous[2]);
		this.put(Key.OrgUnit4, ous[3]);
		this.put(Key.Organization, organization);
		this.put(Key.Country, country);
	}
}
