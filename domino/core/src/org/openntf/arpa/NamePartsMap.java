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
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.openntf.domino.Name.NameFormat;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Strings;

/**
 * NamePartsMap carries the various component string values that make up a name.
 * 
 * @author Devin S. Olson (dolson@czarnowski.com)
 * 
 */
public class NamePartsMap extends EnumMap<NamePartsMap.Key, String> implements Serializable {

	static NamePartsMap DOMAIN_PARTS = new NamePartsMap(Factory.getLocalServerName()); // don't know if this is correct

	public static enum Key {
		Abbreviated, Addr821, Addr822Comment1, Addr822Comment2, Addr822Comment3, Addr822LocalPart, Addr822Phrase, ADMD, Canonical, Common,
		Country, Generation, Given, Initials, Keyword, Language, Organization, OrgUnit1, OrgUnit2, OrgUnit3, OrgUnit4, PRMD, Surname,
		IDprefix, SourceString;

		@Override
		public String toString() {
			return Key.class.getName() + ": " + this.name();
		}

		public String getInfo() {
			return this.getDeclaringClass() + "." + this.getClass() + ":" + this.name();
		}
	};

	//	public static enum CanonicalKey {
	//		CN("Common Name"), OU("Organizational Unit"), O("Organization"), C("Country Code"), A("Administration Management Domain"),
	//		Q("Generation"), S("Surname"), G("Given"), I("Initials"), P("Private Management Domain Name");
	//		private String _label;
	//
	//		@Override
	//		public String toString() {
	//			return CanonicalKey.class.getName() + ": " + this.name() + "(\"" + this.getLabel() + "\")";
	//		}
	//
	//		/**
	//		 * Gets the Label for the Canonical Key
	//		 * 
	//		 * @return Label for the Canonical Key
	//		 */
	//		public String getLabel() {
	//			return this._label;
	//		}
	//
	//		/**
	//		 * Instance Constructor
	//		 * 
	//		 * @param label
	//		 *            Label for the Canonical Key
	//		 */
	//		private CanonicalKey(final String label) {
	//			this._label = label;
	//		}
	//
	//	}

	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(NamePartsMap.class.getName());
	private static final long serialVersionUID = 1L;
	private RFC822name _rfc822name;
	private NameFormat _nameFormat = NameFormat.FLAT;

	/**
	 * * Zero-Argument Constructor
	 */
	public NamePartsMap() {
		super(Key.class);
	}

	/**
	 * Default Constructor
	 * 
	 * @param source
	 *            String from which to construct the object
	 */
	public NamePartsMap(final String string) {
		super(Key.class);
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
		super(Key.class);
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
		super(Key.class);
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

	public void setName(final String name) {
		parse(name);
	}

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

	public NameFormat getNameFormat() {
		return _nameFormat;
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
		final StringBuilder sb = new StringBuilder(NamePartsMap.class.getName());
		sb.append(" [");
		boolean comma = false;
		for (final Key key : Key.values()) {
			final String s = this.get(key);
			if (!ISO.isBlankString(s)) {
				if (comma)
					sb.append(", ");
				sb.append(key.name() + "=" + s);
				comma = true;
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
				if (_nameFormat == NameFormat.HIERARCHICALEX || _nameFormat == NameFormat.HIERARCHICALUNKNOWN)
					return this.get(Key.SourceString);
				final String common = this.get(NamePartsMap.Key.Common);
				final String ou1 = this.get(NamePartsMap.Key.OrgUnit1);
				final String ou2 = this.get(NamePartsMap.Key.OrgUnit2);
				final String ou3 = this.get(NamePartsMap.Key.OrgUnit3);
				final String ou4 = this.get(NamePartsMap.Key.OrgUnit4);
				final String organization = this.get(NamePartsMap.Key.Organization);
				final String country = this.get(NamePartsMap.Key.Country);

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
			case Keyword:
				String keyword = super.get(Key.Keyword);
				if (keyword == null) {
					StringBuilder sb = new StringBuilder();
					String tmp;
					if (!Strings.isBlankString(tmp = get(Key.Country))) {
						sb.append(tmp);
					}
					if (!Strings.isBlankString(tmp = get(Key.Organization))) {
						if (sb.length() > 0)
							sb.append('\\');
						sb.append(tmp);
					}
					if (!Strings.isBlankString(tmp = get(Key.OrgUnit1))) {
						if (sb.length() > 0)
							sb.append('\\');
						sb.append(tmp);
					}
					if (!Strings.isBlankString(tmp = get(Key.OrgUnit2))) {
						if (sb.length() > 0)
							sb.append('\\');
						sb.append(tmp);
					}
					if (!Strings.isBlankString(tmp = get(Key.OrgUnit3))) {
						if (sb.length() > 0)
							sb.append('\\');
						sb.append(tmp);
					}
					if (!Strings.isBlankString(tmp = get(Key.OrgUnit4))) {
						if (sb.length() > 0)
							sb.append('\\');
						sb.append(tmp);
					}
					this.put(Key.Keyword, sb.toString());
					return sb.toString();
				} else {
					return keyword;
				}
			case Canonical: {
				if (_nameFormat.equals(NameFormat.CANONICAL))
					return this.get(Key.SourceString);
				if (_nameFormat == NameFormat.HIERARCHICALUNKNOWN)
					return this.get(Key.SourceString);
				final String generation = this.get(NamePartsMap.Key.Generation);
				final String given = this.get(NamePartsMap.Key.Given);
				final String common = this.get(NamePartsMap.Key.Common);
				final String ou1 = this.get(NamePartsMap.Key.OrgUnit1);
				final String ou2 = this.get(NamePartsMap.Key.OrgUnit2);
				final String ou3 = this.get(NamePartsMap.Key.OrgUnit3);
				final String ou4 = this.get(NamePartsMap.Key.OrgUnit4);
				final String organization = this.get(NamePartsMap.Key.Organization);
				final String country = this.get(NamePartsMap.Key.Country);

				final StringBuffer sb = new StringBuffer("");

				if (!ISO.isBlankString(generation)) {
					sb.append("Q=" + generation + "/");
				}

				if (!ISO.isBlankString(given)) {
					sb.append("G=" + given + "/");
				}

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

				if (!ISO.isBlankString(country)) {
					sb.append("/C=" + country);
				}

				return sb.toString();
			}

			default:
				final String result = super.get(key);
				return (result == null) ? "" : result;
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

			final String common = this.get(Key.Common);
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
				super.put(Key.IDprefix, result);
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
		this.put(Key.SourceString, string);
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
			_nameFormat = NameFormat.CANONICAL;
			DominoUtils.parseNamesPartMap(string, this);	//TODO NTF should replace later with just a thrown exception if it's not hierarchical
		} else {
			try {
				String common = "";
				final String[] ous = new String[] { "", "", "", "" };
				String organization = "";
				String country = "";

				if (!ISO.isBlankString(string)) {
					if (ISO.PatternRFC822.matcher(string).matches()) { // --------- RFC822 name --------------------
						_nameFormat = NameFormat.RFC822;
						this.parseRFC82xContent(string);
						if (allowRecursion) {
							final String phrase = this.getRFC822name().getAddr822Phrase();
							return this.parse((phrase.indexOf('/') < 0) ? this.getRFC822name().getAddr822PhraseFirstLast() : phrase, false);
						}

						return false;
					}

					if (string.indexOf('/') < 0) { // ------------------- flat name ---------------------------------
						common = string;
						_nameFormat = NameFormat.FLAT;

					} else { // ----------------------------------------- hierarchical Name -------------------------
						_nameFormat = NameFormat.HIERARCHICAL;
						// break the source into component words and parse them
						final String[] words = string.split("/");

						if (words.length > 0) {
							int idx = 0;

							if (string.indexOf('=') > 0) { // ---------- use canonical logic------------------------
								try {
									TreeMap<Integer, String> undefinedValues = null;
									int Oidx = -1;
									int Cidx = -1;

									for (int i = (words.length - 1); i >= 0; i--) {
										final String word = words[i].trim();
										int sep;
										if ((sep = word.indexOf('=')) > 0) {

											if (word.indexOf('=', sep + 1) == -1) { // more than one ==

												final String key = word.substring(0, sep).toUpperCase(Locale.ENGLISH);
												final String value = word.substring(sep + 1);
												if (key.length() == 1) {
													switch (key.charAt(0)) {

													case 'C': // Country
														country = value;
														Cidx = i;
														break;

													case 'O': // Organitation
														organization = value;
														Oidx = i;
														break;

													case 'A':
														//if (_nameFormat != NameFormat.HIERARCHICALUNKNOWN)
														_nameFormat = NameFormat.HIERARCHICALUNKNOWN;
														this.put(Key.ADMD, value);
														break;
													case 'G':
														if (_nameFormat != NameFormat.HIERARCHICALUNKNOWN)
															_nameFormat = NameFormat.HIERARCHICALEX;
														this.put(NamePartsMap.Key.Given, value);
														break;
													case 'I':
														if (_nameFormat != NameFormat.HIERARCHICALUNKNOWN)
															_nameFormat = NameFormat.HIERARCHICALEX;
														this.put(NamePartsMap.Key.Initials, value);
														break;
													case 'P':
														//if (_nameFormat != NameFormat.HIERARCHICALUNKNOWN)
														_nameFormat = NameFormat.HIERARCHICALUNKNOWN;
														this.put(NamePartsMap.Key.PRMD, value);
														break;
													case 'S':
														if (_nameFormat != NameFormat.HIERARCHICALUNKNOWN)
															_nameFormat = NameFormat.HIERARCHICALEX;
														this.put(NamePartsMap.Key.Surname, value);
														break;
													case 'Q':
														if (_nameFormat != NameFormat.HIERARCHICALUNKNOWN)
															_nameFormat = NameFormat.HIERARCHICALEX;
														this.put(NamePartsMap.Key.Generation, value);
														break;
													default:
														_nameFormat = NameFormat.HIERARCHICALUNKNOWN;
														// TODO: Should we save unknown parts in the map?
													}

												} else if ("CN".equals(key)) {
													common = value;

												} else if ("OU".equals(key)) {
													if (idx < 4) {
														ous[idx] = value;
													}
													idx++;
												} else {
													_nameFormat = NameFormat.HIERARCHICALUNKNOWN;
													// TODO: Should we save unknown parts in the map?
												}

											} else {
												throw new RuntimeException("Cannot Parse Word: \"" + word + "\", Source String: \""
														+ string + "\"");
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

							} else { // ----------------------------------- use abbreviated logic ------------------------------
								common = words[0].trim();
								if (words.length > 1) {
									int orgpos = (words.length - 1);
									organization = words[orgpos];
									if (ISO.isCountryCode2(organization)) {
										// organization could be a country code,
										country = organization;
										orgpos--;
										if (orgpos > 0) {
											// Treat organization as a country code
											// and
											// re-aquire the organization
											organization = words[orgpos];
										} else {
											organization = "";
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

				if (DOMAIN_PARTS != null && (_nameFormat == NameFormat.HIERARCHICAL || _nameFormat == NameFormat.HIERARCHICALEX)) {
					do {
						// fill missing parts from current domain
						if (!ISO.isBlankString(country))
							break;
						country = DOMAIN_PARTS.get(Key.Country);

						if (!ISO.isBlankString(organization))
							break;
						organization = DOMAIN_PARTS.get(Key.Organization);

						if (!ISO.isBlankString(ous[3]))
							break;
						ous[3] = DOMAIN_PARTS.get(Key.OrgUnit4);

						if (!ISO.isBlankString(ous[2]))
							break;
						ous[2] = DOMAIN_PARTS.get(Key.OrgUnit3);

						if (!ISO.isBlankString(ous[1]))
							break;
						ous[1] = DOMAIN_PARTS.get(Key.OrgUnit2);

						if (!ISO.isBlankString(ous[0]))
							break;
						ous[0] = DOMAIN_PARTS.get(Key.OrgUnit1);
					} while (false);
				}
				this.put(Key.Common, common);
				this.put(Key.OrgUnit1, ous[0]);
				this.put(Key.OrgUnit2, ous[1]);
				this.put(Key.OrgUnit3, ous[2]);
				this.put(Key.OrgUnit4, ous[3]);
				this.put(Key.Organization, organization);
				this.put(Key.Country, country);
				/**
				 * From Designer help:
				 * 
				 * the following components of a hierarchical name in the order shown separated by backslashes: country or
				 * region\organization\organizational unit 1\organizational unit 2\organizational unit 3\organizational unit 4. Returns an
				 * empty string if the property is undefined.
				 */
				//FIXME NTF This should really be in the get() so it's done on-demand
				StringBuilder sb = new StringBuilder();
				String tmp;
				if (!Strings.isBlankString(tmp = get(Key.Country))) {
					sb.append(tmp);
				}
				if (!Strings.isBlankString(tmp = get(Key.Organization))) {
					if (sb.length() > 0)
						sb.append('\\');
					sb.append(tmp);
				}
				if (!Strings.isBlankString(tmp = get(Key.OrgUnit1))) {
					if (sb.length() > 0)
						sb.append('\\');
					sb.append(tmp);
				}
				if (!Strings.isBlankString(tmp = get(Key.OrgUnit2))) {
					if (sb.length() > 0)
						sb.append('\\');
					sb.append(tmp);
				}
				if (!Strings.isBlankString(tmp = get(Key.OrgUnit3))) {
					if (sb.length() > 0)
						sb.append('\\');
					sb.append(tmp);
				}
				if (!Strings.isBlankString(tmp = get(Key.OrgUnit4))) {
					if (sb.length() > 0)
						sb.append('\\');
					sb.append(tmp);
				}
				this.put(Key.Keyword, sb.toString());

				return true;

			} catch (final Exception e) {
				DominoUtils.handleException(e, "Source String: \"" + string + "\"");
			}
		}
		return false;
	}
}
