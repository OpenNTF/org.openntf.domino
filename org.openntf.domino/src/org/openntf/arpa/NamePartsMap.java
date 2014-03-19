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
import java.util.logging.Logger;

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
			return Key.class.getName() + ": " + this.name();
		}

		public String getInfo() {
			return this.getDeclaringClass() + "." + this.getClass() + ":" + this.name();
		}
	};

	public static enum CanonicalKey {
		CN("Common Name"), OU("Organizational Unit"), O("Organization"), C("Country Code");
		private String _label;

		@Override
		public String toString() {
			return CanonicalKey.class.getName() + ": " + this.name() + "(\"" + this.getLabel() + "\")";
		}

		/**
		 * Gets the Label for the Canonical Key
		 * 
		 * @return Label for the Canonical Key
		 */
		public String getLabel() {
			return this._label;
		}

		/**
		 * Sets the Label for the Canonical Key
		 * 
		 * @param label
		 *            the Label for the Canonical Key
		 */
		private void setLabel(final String label) {
			this._label = label;
		}

		/**
		 * Instance Constructor
		 * 
		 * @param label
		 *            Label for the Canonical Key
		 */
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
	public NamePartsMap(final String string) {
		super();
		this.parse(string);
	}

	/**
	 * Optional Constructor
	 * 
	 * @param source
	 *            String from which to construct the object
	 * 
	 * @param rfc822name
	 *            RFC822name for the object.
	 */
	public NamePartsMap(final String string, final RFC822name rfc822name) {
		super();
		this.parse(string);
		this.setRFC822name(rfc822name);
	}

	/**
	 * Default Constructor
	 * 
	 * @param source
	 *            String from which to construct the object
	 * 
	 * @param rfc822string
	 *            String from which to construct the RFC822name for the object.
	 */
	public NamePartsMap(final String string, final String rfc822string) {
		super();
		this.parse(string);
		this.parseRFC82xContent(rfc822string);
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * public methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */
	/**
	 * Gets the RFC822name for the object
	 * 
	 * @return the RFC822name
	 */
	public RFC822name getRFC822name() {
		if (null == this._rfc822name) {
			this._rfc822name = new RFC822name();
		}
		return this._rfc822name;
	}

	/**
	 * Sets the RFC822name for the object
	 * 
	 * @param rfc822name
	 *            the RFC822name
	 */
	private void setRFC822name(final RFC822name rfc822name) {
		this._rfc822name = rfc822name;
	}

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

	/**
	 * Gets the String for the key.
	 * 
	 * @param key
	 *            Key for the mapped String
	 * 
	 * @return Mapped String for the key. Empty string "" if no mapping exists.
	 * 
	 * @see java.util.HashMap#get(Object)
	 */
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
			if (!ISO.isBlankString(s)) {
				sb.append(key.name() + "=" + s);
			}
		}

		sb.append("]");

		return sb.toString();
	}

	/**
	 * Gets the String for the key.
	 * 
	 * @param key
	 *            Key for the mapped String
	 * 
	 * @return Mapped String for the key. Empty string "" if no mapping exists.
	 * 
	 * @see java.util.HashMap#get(Object)
	 */
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

	/**
	 * Associates the specified String with the specified key in the map.
	 * 
	 * @param key
	 *            Key for the mapped String
	 * 
	 * @param value
	 *            String to be associated with the key.
	 * 
	 * @return Previous value associated with the key. Empty string "" if no mapping exists.
	 * 
	 * @see java.util.HashMap#put(Object, Object)
	 */
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

	/**
	 * Gets the IDprefix for the Name.
	 * 
	 * If an IDprefix does not exist one will be created.
	 * 
	 * @return IDprefix for the Name
	 */
	public String getIDprefix() {
		String result = super.get(Key.IDprefix);
		if (ISO.isBlankString(result)) {

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

	/**
	 * Parses the source string and sets the appropriate RFC822 values.
	 * 
	 * @param string
	 *            RFC822 source string from which to set the appropriate RFC822 values.
	 */
	public void parseRFC82xContent(final String string) {
		this.getRFC822name().parseRFC82xContent(string);
	}

	/**
	 * Indicates whether the object has RFC82xContent
	 * 
	 * @return Flag indicating if the object has RFC82xContent
	 */
	public boolean isHasRFC82xContent() {
		return (null == this._rfc822name) ? false : this.getRFC822name().isHasRFC82xContent();
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

			return this.getRFC822name().equalsIgnoreCase(string);
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
					if (ISO.startsWithIgnoreCase(s, prefix)) {
						return true;
					}
				}
			}
		}

		return false;

	} /*
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
	 * @param string
	 *            String from which to parse the name values.
	 */
	private boolean parse(final String string) {
		try {
			String common = "";
			final String[] ous = new String[] { "", "", "", "" };
			String organization = "";
			String country = "";

			if ((!ISO.isBlankString(string)) && (string.indexOf('/') > 0)) {

				// break the source into it's component words and parse them
				final String[] words = string.split("/");
				final int length = words.length;
				if (length > 0) {
					int idx = 0;

					if (string.indexOf('=') > 0) {
						// use canonical logic
						try {
							for (int i = (words.length - 1); i >= 0; i--) {
								final String word = words[i].trim();
								try {
									// TODO Need to handle case where word = "*"   DSO 20140319

									if (word.indexOf('=') > 0) {
										final String[] nibbles = word.split("=");
										if (nibbles.length > 1) {
											final String key = nibbles[0];
											final String value = nibbles[1];

											if (CanonicalKey.C.name().equalsIgnoreCase(key)) {
												country = value;
											} else if (CanonicalKey.O.name().equalsIgnoreCase(key)) {
												organization = value;
											} else if (CanonicalKey.OU.name().equalsIgnoreCase(key)) {
												ous[idx] = value;
												idx++;
											} else if (CanonicalKey.CN.name().equalsIgnoreCase(key)) {
												common = value;
											}
										} else {
											throw new RuntimeException("Cannot Parse Word: \"" + word + "\", Source String: \"" + string
													+ "\"");
										}
									}

								} catch (final Exception e) {
									ISO.handleException(e, "Source String: \"" + string + "\"");
								}
							}
						} catch (final Exception e) {
							ISO.handleException(e, "Source String: \"" + string + "\"");
						}

					} else {
						// use abbreviated logic
						common = words[0].trim();
						if (length > 1) {
							int orgpos = length;
							organization = words[orgpos];
							if (ISO.isCountryCode2(organization)) {
								// organization could be a country code,
								if (orgpos > 1) {
									// Treat organization as a country code and
									// re-aquire the organization
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

			return true;

		} catch (final Exception e) {
			ISO.handleException(e, "Source String: \"" + string + "\"");
		}

		return false;
	}
}
