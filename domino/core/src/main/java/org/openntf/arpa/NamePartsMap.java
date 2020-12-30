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
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.openntf.domino.ext.Name.NameFormat;
import org.openntf.domino.ext.Name.NamePartKey;
import org.openntf.domino.utils.DominoUtils;

/**
 * NamePartsMap carries the various component string values that make up a name.
 *
 * @author Devin S. Olson (dolson@czarnowski.com)
 *
 */
@SuppressWarnings("nls")
public class NamePartsMap extends EnumMap<NamePartKey, String> implements Serializable {

	// Enum Key moved to Interface ext-Name (org.openntf.domino.ext.Name.NamePartKey)

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

	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(NamePartsMap.class.getName());
	private static final long serialVersionUID = 1L;
	private RFC822name _rfc822name;
	private NameFormat _nameFormat = NameFormat.FLAT;

	/**
	 * * Zero-Argument Constructor
	 */
	public NamePartsMap() {
		super(NamePartKey.class);
	}

	/**
	 * Default Constructor
	 *
	 * @param source
	 *            String from which to construct the object
	 */
	public NamePartsMap(final String string) {
		super(NamePartKey.class);
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
		super(NamePartKey.class);
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
		super(NamePartKey.class);
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
		return (key instanceof NamePartKey) ? this.get((NamePartKey) key) : "";
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(NamePartsMap.class.getName());
		sb.append(" [");
		for (final NamePartKey key : NamePartKey.values()) {
			final String s = this.get(key);
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
	public String get(final NamePartKey key) {
		if (null != key) {
			switch (key) {
			case Abbreviated: {
				final String common = this.get(NamePartKey.Common);
				final String ou1 = this.get(NamePartKey.OrgUnit1);
				final String ou2 = this.get(NamePartKey.OrgUnit2);
				final String ou3 = this.get(NamePartKey.OrgUnit3);
				final String ou4 = this.get(NamePartKey.OrgUnit4);
				final String organization = this.get(NamePartKey.Organization);
				String dc1 = this.get(NamePartKey.DomainComponent1);
				String dc2 = this.get(NamePartKey.DomainComponent2);
				String dc3 = this.get(NamePartKey.DomainComponent3);
				String dc4 = this.get(NamePartKey.DomainComponent4);
				final String country = this.get(NamePartKey.Country);

				final StringBuffer sb = new StringBuffer("");
				if (!ISO.isBlankString(common)) {
					sb.append(common);
				}

				if (!ISO.isBlankString(ou4)) {
					sb.append("/" + ou4);
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

				if (!ISO.isBlankString(dc1)) {
					sb.append("/" + dc1);
				}
				if (!ISO.isBlankString(dc2)) {
					sb.append("/" + dc2);
				}
				if (!ISO.isBlankString(dc3)) {
					sb.append("/" + dc3);
				}
				if (!ISO.isBlankString(dc4)) {
					sb.append("/" + dc4);
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
				if (_nameFormat.equals(NameFormat.HIERARCHICAL)) {
					return this.get(NamePartKey.SourceString);
				}
				final String common = this.get(NamePartKey.Common);
				final String ou1 = this.get(NamePartKey.OrgUnit1);
				final String ou2 = this.get(NamePartKey.OrgUnit2);
				final String ou3 = this.get(NamePartKey.OrgUnit3);
				final String ou4 = this.get(NamePartKey.OrgUnit4);
				final String organization = this.get(NamePartKey.Organization);
				String dc1 = this.get(NamePartKey.DomainComponent1);
				String dc2 = this.get(NamePartKey.DomainComponent2);
				String dc3 = this.get(NamePartKey.DomainComponent3);
				String dc4 = this.get(NamePartKey.DomainComponent4);
				final String country = this.get(NamePartKey.Country);

				final StringBuffer sb = new StringBuffer("");
				if (!ISO.isBlankString(common)) {
					sb.append("*".equals(common) ? "*" : "CN=" + common);
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

				if (!ISO.isBlankString(dc1)) {
					sb.append("/DC=" + dc1);
				}
				if (!ISO.isBlankString(dc2)) {
					sb.append("/DC=" + dc2);
				}
				if (!ISO.isBlankString(dc3)) {
					sb.append("/DC=" + dc3);
				}
				if (!ISO.isBlankString(dc4)) {
					sb.append("/DC=" + dc4);
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
	public String put(final NamePartKey key, final String value) {
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
				return this.get(NamePartKey.Abbreviated);
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
				return this.get(NamePartKey.Canonical);
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
		String result = super.get(NamePartKey.IDprefix);
		if (ISO.isBlankString(result)) {

			final String common = this.get(NamePartKey.Common);
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

				// use super.put() to avoid endless loop!
				super.put(NamePartKey.IDprefix, result);
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
		return this.parse(string, true);
	}

	/**
	 * Retrieves and sets the various name values by parsing an input source string.
	 *
	 * @param string
	 *            String from which to parse the name values.
	 *
	 * @param allowRecursion
	 *            Flag indicating if this method is allowed to recursively call itself.
	 */
	private boolean parse(final String string, final boolean allowRecursion) {
		if (DominoUtils.isHierarchicalName(string)) {
			_nameFormat = NameFormat.DOMINO;
			DominoUtils.parseNamesPartMap(string, this);	//TODO NTF should replace later with just a thrown exception if it's not hierarchical
		} else {
			try {
				String common = "";
				final String[] ous = new String[] { "", "", "", "" };
				String organization = "";
				String country = "";

				if (!ISO.isBlankString(string)) {
					if (ISO.PatternRFC822.matcher(string).matches()) {
						this.parseRFC82xContent(string);
						if (allowRecursion) {
							final String phrase = this.getRFC822name().getAddr822Phrase();
							return this.parse((phrase.indexOf('/') < 0) ? this.getRFC822name().getAddr822PhraseFirstLast() : phrase, false);
						}

						return false;
					}

					if (string.indexOf('/') < 0) {
						common = string;

					} else {
						// break the source into component words and parse them
						final String[] words = string.split("/");
						if (words.length > 0) {
							int idx = 0;

							if (string.indexOf('=') > 0) {
								// use canonical logic
								try {
									TreeMap<Integer, String> undefinedValues = null;
									int Oidx = -1;
									int Cidx = -1;
									for (int i = (words.length - 1); i >= 0; i--) {
										final String word = words[i].trim();
										if (word.indexOf('=') > 0) {
											final String[] nibbles = word.split("=");
											if (nibbles.length > 1) {
												final String key = nibbles[0];
												final String value = nibbles[1];

												if (CanonicalKey.C.name().equalsIgnoreCase(key)) {
													country = value;
													Cidx = i;

												} else if (CanonicalKey.O.name().equalsIgnoreCase(key)) {
													organization = value;
													Oidx = i;

												} else if (CanonicalKey.OU.name().equalsIgnoreCase(key)) {
													if (idx < 4) {
														ous[idx] = value;
													}
													idx++;

												} else if (CanonicalKey.CN.name().equalsIgnoreCase(key)) {
													common = value;
												}
											} else {
												throw new RuntimeException(
														"Cannot Parse Word: \"" + word + "\", Source String: \"" + string + "\"");
											}

										} else {
											// no '=' in word
											if (null == undefinedValues) {
												undefinedValues = new TreeMap<Integer, String>();
											}
											undefinedValues.put(new Integer(i), word);
										}
									}

									if (null != undefinedValues) {
										// at least one undefined value (such as a
										// wildcard) exists.
										final Iterator<Map.Entry<Integer, String>> it = undefinedValues.entrySet().iterator();
										while (it.hasNext()) {
											final Map.Entry<Integer, String> entry = it.next();
											final int idxEntry = entry.getKey().intValue();
											if (0 == idxEntry) {
												if (ISO.isBlankString(common)) {
													common = entry.getValue();
												} else {
													for (String s : ous) {
														if (ISO.isBlankString(s)) {
															s = entry.getValue();
															break;
														}
													}
												}

											} else if ((Cidx < 0) && (idxEntry == (words.length - 1))) {
												if (ISO.isBlankString(organization)) {
													organization = entry.getValue();
												} else {
													country = entry.getValue();
												}

											} else if (idxEntry == Cidx) {
												organization = entry.getValue();

											} else if (idxEntry == Oidx) {
												for (String orgunit : ous) {
													if (ISO.isBlankString(orgunit)) {
														orgunit = entry.getValue();
														break;
													}
												}
											}
										}
									}

								} catch (final Exception e) {
									DominoUtils.handleException(e, "Source String: \"" + string + "\"");
								}

							} else {
								// use abbreviated logic
								common = words[0].trim();
								if (words.length > 1) {
									int orgpos = (words.length - 1);
									organization = words[orgpos];
									if (ISO.isCountryCode2(organization)) {
										// organization could be a country code,
										if (orgpos > 1) {
											// Treat organization as a country code
											// and
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
				}

				this.put(NamePartKey.Common, common);
				this.put(NamePartKey.OrgUnit1, ous[0]);
				this.put(NamePartKey.OrgUnit2, ous[1]);
				this.put(NamePartKey.OrgUnit3, ous[2]);
				this.put(NamePartKey.OrgUnit4, ous[3]);
				this.put(NamePartKey.Organization, organization);
				this.put(NamePartKey.Country, country);

				return true;

			} catch (final Exception e) {
				DominoUtils.handleException(e, "Source String: \"" + string + "\"");
			}
		}
		return false;
	}

	public boolean isHiearchical() {
		return _nameFormat == NameFormat.DOMINO || _nameFormat == NameFormat.HIERARCHICAL;
	}

	public NameFormat getNameFormat() {
		return _nameFormat;
	}
}
