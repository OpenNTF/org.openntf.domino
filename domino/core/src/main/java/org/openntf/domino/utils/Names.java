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
package org.openntf.domino.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openntf.arpa.RFC822name;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ext.Name.NamePartKey;

/**
 * Name handling utilities
 *
 * @author Devin S. Olson (dolson@czarnowski.com)
 *
 */
public enum Names {
	;

	public static Pattern IS_HIERARCHICAL_MATCH = Pattern.compile("^((CN=)|(O=)|(OU=)|(C=))[^/]+", Pattern.CASE_INSENSITIVE);

	public static Pattern CN_MATCH = Pattern.compile("^(CN=)[^/]+", Pattern.CASE_INSENSITIVE);

	public static Pattern OU_MATCH = Pattern.compile("\\b(OU=)[^/]+", Pattern.CASE_INSENSITIVE);

	public static Pattern O_MATCH = Pattern.compile("\\b(O=)[^/]+", Pattern.CASE_INSENSITIVE);

	public static Pattern C_MATCH = Pattern.compile("\\b(C=)[^/]+", Pattern.CASE_INSENSITIVE);

	public static Pattern DC_MATCH = Pattern.compile("\\b(DC=)[^/]+", Pattern.CASE_INSENSITIVE);

	public static enum LookupType {
		Person("P"), Group("G"), Unknown("U");

		private String _code;

		/**
		 * Instance Constructor
		 *
		 * @param code
		 *            Code for the Key
		 */
		private LookupType(final String code) {
			this.setCode(code);
		}

		@Override
		public String toString() {
			return LookupType.class.getName() + ": " + this.name() + "{\"" + this.getCode() + "\"}";
		}

		/**
		 * Gets the Code for the Key.
		 *
		 * @return Key's Code.
		 */
		public String getCode() {
			return this._code;
		}

		/**
		 * Sets the Code for the Key.
		 *
		 * @param code
		 *            Key's Code.
		 */
		public void setCode(final String code) {
			this._code = code;
		}
	};

	/*
	 * **************************************************************************
	 * **************************************************************************
	 *
	 * PUBLIC STATIC Properties and Methods
	 *
	 * **************************************************************************
	 * **************************************************************************
	 */

	/**
	 * Formats a String as a role (begins with "[", ends with "]")
	 *
	 * @param string
	 *            String to be formatted
	 *
	 * @return source string formatted as a role.
	 */
	public static String formatAsRole(final String string) {
		//		return (Strings.isBlankString(string)) ? ""
		//				: ((string.indexOf('[') == 0) && (string.indexOf(']') == (string.length() - 1))) ? string : "[" + string + "]";

		if (!Strings.isBlankString(string)) {
			final String result = "[" + string.replace("[", "").replace("]", "").trim() + "]";
			return ("[]".equals(result)) ? "" : result;
		}

		return "";
	}

	/**
	 * Gets the approprite formatted name string for the given source.
	 *
	 * @param session
	 *            Session in effect for generating the name.
	 *
	 * @param source
	 *            Source string from which to generate the name
	 *
	 * @param key
	 *            Part of name string to return
	 *
	 * @return source string converted to the appropriate name format
	 */
	public static String getNamePart(final Session session, final String source, final NamePartKey key) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}
			if (null == key) {
				throw new IllegalArgumentException("NamePartsMap.Key is null");
			}

			final String seed = (Strings.isBlankString(source)) ? session.getEffectiveUserName() : source;
			org.openntf.domino.Name name = session.createName(seed);
			return name.getNamePart(key);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Gets the approprite formatted name strings for the given source. The order of values is preserved, however duplicates are removed and
	 * no values will be included for null or blank entries in the source.
	 *
	 * @param session
	 *            Session to use
	 *
	 * @param source
	 *            Source strings from which to generate the names.
	 *
	 * @param key
	 *            Part of name string to return
	 *
	 * @return source strings converted to the appropriate name format, in the order in which they exist in source.
	 */
	public static String[] getNameParts(final Session session, final String[] source, final NamePartKey key) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}

			if (null == source) {
				throw new IllegalArgumentException("Source Array is null");
			}

			if (null == key) {
				throw new IllegalArgumentException("NamePartsMap.Key is null");
			}

			if (source.length < 1) {
				return null;
			}

			final List<String> values = new ArrayList<String>();
			for (final String temp : source) {
				if (!Strings.isBlankString(temp)) {
					final String name = Names.getNamePart(session, temp, key);

					if (!Strings.isBlankString(name)) {
						values.add(name);
					}
				}
			}

			return (values.size() < 1) ? null : CollectionUtils.getStringArray(values);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Generates an array of Strings containing Abbreviated names.
	 *
	 * @param session
	 *            Session to use
	 *
	 * @param source
	 *            Source strings from which to generate the names.
	 *
	 * @return source strings converted to the appropriate name format, in the order in which they exist in source.
	 */
	public static String[] getAbbreviated(final Session session, final String[] source) {
		return Names.getNameParts(session, source, NamePartKey.Abbreviated);
	}

	/**
	 * Generates an Abbreviated Name from the input.
	 *
	 * @param session
	 *            Session to use
	 *
	 * @param source
	 *            String from which to generate the names.
	 *
	 * @return source String converted to the appropriate name format
	 */
	public static String getAbbreviated(final Session session, final String source) {
		return Names.getNamePart(session, source, NamePartKey.Abbreviated);
	}

	/**
	 * Generates an Abbreviated Name for the current user.
	 *
	 * @param session
	 *            Session to use
	 *
	 * @return Abbreviated name for the current user.
	 */
	public static String getAbbreviated(final Session session) {
		return Names.getAbbreviated(session, "");
	}

	/**
	 * Generates an array of Strings containing Canonical names.
	 *
	 * @param session
	 *            Session to use
	 *
	 * @param source
	 *            Source strings from which to generate the names.
	 *
	 * @return source strings converted to the appropriate name format, in the order in which they exist in source.
	 */
	public static String[] getCanonical(final Session session, final String[] source) {
		return Names.getNameParts(session, source, NamePartKey.Canonical);
	}

	/**
	 * Generates an Canonical Name from the input.
	 *
	 * @param session
	 *            Session to use
	 *
	 * @param source
	 *            String from which to generate the names.
	 *
	 * @return source String converted to the appropriate name format
	 */
	public static String getCanonical(final Session session, final String source) {
		return Names.getNamePart(session, source, NamePartKey.Canonical);
	}

	/**
	 * Generates an Canonical Name for the current user.
	 *
	 * @param session
	 *            Session to use
	 *
	 * @return Canonical name for the current user.
	 */
	public static String getCanonical(final Session session) {
		return Names.getCanonical(session, "");
	}

	/**
	 * Generates an array of Strings containing Common names.
	 *
	 * @param session
	 *            Session to use
	 *
	 * @param source
	 *            Source strings from which to generate the names.
	 *
	 * @return source strings converted to the appropriate name format, in the order in which they exist in source.
	 */
	public static String[] getCommon(final Session session, final String[] source) {
		return Names.getNameParts(session, source, NamePartKey.Common);
	}

	/**
	 * Generates an Common Name from the input.
	 *
	 * @param session
	 *            Session to use
	 *
	 * @param source
	 *            String from which to generate the names.
	 *
	 * @return source String converted to the appropriate name format
	 */
	public static String getCommon(final Session session, final String source) {
		return Names.getNamePart(session, source, NamePartKey.Common);
	}

	/**
	 * Generates an Common Name for the current user.
	 *
	 * @param session
	 *            Session to use
	 *
	 * @return Common name for the current user.
	 */
	public static String getCommon(final Session session) {
		return Names.getCommon(session, "");
	}

	/**
	 * Gets all the roles the current user has for the Database.
	 *
	 * @param session
	 *            Session in effect.
	 * @param database
	 *            Database for which to check the roles.
	 * @return All roles the current user has for the Database. Null on exception or no roles found.
	 */
	public static TreeSet<String> getRoles(final Session session, final Database database) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}
			if (null == database) {
				throw new IllegalArgumentException("Database is null");
			}

			return Names.getRoles(database, Names.getCanonical(session));

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Gets all the roles the specified user has for the Database.
	 *
	 * @param database
	 *            Database for which to check the roles.
	 * @param name
	 *            Name for which to get the roles.
	 * @return All roles the specified user has for the Database. Null on exception or no roles found.
	 */
	public static TreeSet<String> getRoles(final Database database, final Name name) {
		try {
			if (null == database) {
				throw new IllegalArgumentException("Database is null");
			}
			if (null == name) {
				throw new IllegalArgumentException("Name is null");
			}

			return Names.getRoles(database, name.getCanonical());

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Fully expands a list of name strings.
	 *
	 * Searches all public address books available to the specified session for person or group entries. For every group found the members
	 * of the group will be checked against the searched set. If not in the searched list they will also be searched for (and added to the
	 * searched list). Found group members will be included in the results.
	 *
	 * @param session
	 *            Session in effect for the search.
	 * @param searchfor
	 *            Set of name strings for which to search.
	 * @param searched
	 *            Set of name strings which have already been searched.
	 * @return Set of all expanded found names & group members. K = name string, V = Result type (Person, Group, Unknown)
	 */
	public static HashMap<String, Names.LookupType> expandNamesList(final Session session, final TreeSet<String> searchfor,
			final HashMap<String, Names.LookupType> searched) {

		HashMap<String, Names.LookupType> result = new HashMap<String, Names.LookupType>();

		try {
			if (null != searched) {
				result = searched;
			}
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}

			if ((null != searchfor) && (searchfor.size() > 0)) {
				final Collection<Database> books = session.getAddressBookCollection();
				for (Database database : books) {
					if (database.isPublicAddressBook()) {
						database.open();
						if (database.isOpen()) {
							final HashMap<String, Names.LookupType> found = Names.expandNamesList(session, database, searchfor, result);
							if ((null != found) && (found.size() > 0)) {
								final Iterator<Map.Entry<String, Names.LookupType>> it = found.entrySet().iterator();
								while (it.hasNext()) {
									final Map.Entry<String, Names.LookupType> entry = it.next();
									if (!result.containsKey(entry.getKey())) {
										result.put(entry.getKey(), entry.getValue());
									}
								}
							}
						}
					}
				}

			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return result;
	}

	/**
	 * Fully expands a list of name strings.
	 *
	 * Searches all public address books available to the specified session for person or group entries. For every group found the members
	 * of the group will also be searched for and included in the results.
	 *
	 * @param session
	 *            Session in effect for the search.
	 * @param source
	 *            Object which can be converted to a TreeSet of Strings for which to search.
	 * @param filters
	 *            Names.LookupType (Person, Group, Unknown) for which to limit the results.
	 * @return Set of all expanded found names & group members whose result type is included in filters. K = name string, V = Result type
	 *         (Person, Group, Unknown)
	 */
	public static TreeSet<String> expandNamesList(final Session session, final Object source, final Names.LookupType... filters) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}

			final TreeSet<String> result = new TreeSet<String>();
			if ((null != filters) && (filters.length > 0)) {
				final TreeSet<String> searchfor = CollectionUtils.getTreeSetStrings(source);
				if ((null != searchfor) && (searchfor.size() > 0)) {
					HashMap<String, Names.LookupType> found = Names.expandNamesList(session, searchfor,
							new HashMap<String, Names.LookupType>());

					// add the searchfor values to found
					if (null == found) {
						found = new HashMap<String, Names.LookupType>();
					}
					for (final String s : searchfor) {
						if (!found.containsKey(s)) {
							final String key = Names.getAbbreviated(session, s);
							if (!found.containsKey(key)) {
								found.put(key, Names.LookupType.Unknown);
							}
						}
					}

					if (found.size() > 0) {
						final HashMap<String, String> temp = new HashMap<String, String>();
						final Iterator<Map.Entry<String, Names.LookupType>> it = found.entrySet().iterator();
						while (it.hasNext()) {
							boolean include = false;
							final Map.Entry<String, Names.LookupType> entry = it.next();
							final String key = entry.getKey();
							final Names.LookupType value = entry.getValue();
							for (final Names.LookupType filter : filters) {
								if (filter.equals(value)) {
									include = true;
									break;
								}
							}
							if (include) {
								if (Names.LookupType.Person.equals(value)) {
									String abbrev = Names.getAbbreviated(session, key);
									if (!Strings.isBlankString(key)) {
										temp.put(abbrev.toLowerCase(), abbrev);
									}
								} else {
									temp.put(key.toLowerCase(), key);
								}
							}

							result.addAll(temp.values());
						}
					}
				}
			}

			return (result.size() > 0) ? result : null;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Fully expands a list of name strings.
	 *
	 * Searches all public address books available to the specified session for person or group entries. For every group found the members
	 * of the group will also be searched for and included in the results.
	 *
	 * @param session
	 *            Session in effect for the search.
	 * @param source
	 *            Object which can be converted to a TreeSet of Strings for which to search.
	 * @return Set of all expanded found names & group members. K = name string, V = Result type (Person, Group, Unknown)
	 */
	public static TreeSet<String> expandNamesList(final Session session, final Object source) {
		return Names.expandNamesList(session, source, Names.LookupType.values());
	}

	/**
	 * Creates a new Name object from the specified source.
	 *
	 * @param session
	 *            Session in effect.
	 * @param source
	 *            String from which to create a new Name object.
	 * @return Name created from the source string. Null on error.
	 * @deprecated use {@link Session#createName(String)}
	 */

	@Deprecated
	public static Name createName(final Session session, final String source) {
		return session.createName(source);
	}

	/**
	 * Creates a new Name object using the value of a specified item on the Document as the source.
	 *
	 * @param session
	 *            Session in effect.
	 * @param document
	 *            Document from which to get the item value string.
	 * @param itemname
	 *            Name of the item from which the value string will be used to create the new Name object.
	 * @return Name created from the specified item's value. Null on error.
	 *
	 * @deprecated use {@link Document#getItemValueName(String)}
	 */
	@Deprecated
	public static Name createName(final Session session, final Document document, final String itemname) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (Strings.isBlankString(itemname)) {
				throw new IllegalArgumentException("Item Name is blank or null");
			}

			final String string = document.getItemValueString(itemname);
			return (Strings.isBlankString(string)) ? null : session.createName(string);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Creates a new Name object using the specified Name object as it's source.
	 *
	 * @param name
	 *            Name object from which to construct a new Name object.
	 *
	 * @return Name created from the specified Name. Null on error.
	 * @deprecated use {@link Name#clone()} instead.
	 */

	@Deprecated
	public static Name createName(final Name name) {
		try {
			if (null == name) {
				throw new IllegalArgumentException("Name is null");
			}

			return name.clone();

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Creates a new Name object using the specified Name object as it's source.
	 *
	 * @param name
	 *            Name object from which to construct a new Name object.
	 *
	 * @return Name created from the specified Name. Null on error.
	 *
	 * @deprecated use {@link Factory#fromLotus}(name, Name.SCHEMA, null) instead
	 */
	@Deprecated
	public static Name createName(final lotus.domino.Name name) {
		try {
			if (null == name) {
				throw new IllegalArgumentException("Name is null");
			}
			return Factory.fromLotus(name, Name.SCHEMA, null);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Creates a new RFC822name object using the specified Name object as it's source.
	 *
	 * @param name
	 *            Name object from which to construct a new Name object.
	 *
	 * @return RFC822name created from the specified Name. Null on error.
	 */
	public static RFC822name createRFC822name(final Name name) {
		try {

			if (null == name) {
				throw new IllegalArgumentException("Name is null");
			}

			String addr822 = Names.buildAddr822Full(name);
			return (Strings.isBlankString(addr822)) ? new RFC822name() : new RFC822name(addr822);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Creates a new RFC822name object using the specified Name object as it's source.
	 *
	 * @param name
	 *            Name object from which to construct a new Name object.
	 *
	 * @return RFC822name created from the specified Name. Null on error.
	 */
	public static RFC822name createRFC822name(final lotus.domino.Name name) {
		try {

			if (null == name) {
				throw new IllegalArgumentException("Name is null");
			}

			String addr822 = Names.buildAddr822Full(name);
			return (Strings.isBlankString(addr822)) ? new RFC822name() : new RFC822name(addr822);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Generates an RFC822 Addr822 Full Address String from the specified Name.
	 *
	 * @param name
	 *            Name from which to construct the result.
	 * @return properly formatted RFC822 Addr822Full string generated from the specified Name. Empty string on error or no value for
	 *         name.getAddr821().
	 */
	public static String buildAddr822Full(final Name name) {
		try {
			if (null == name) {
				throw new IllegalArgumentException("Name is null");
			}

			return RFC822name.buildAddr822Full(name.getAddr822Phrase(), name.getAddr821(), name.getAddr822Comment1(),
					name.getAddr822Comment2(), name.getAddr822Comment3());
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	/**
	 * Generates an RFC822 Addr822 Full Address String from the specified Name.
	 *
	 * @param name
	 *            Name from which to construct the result.
	 * @return properly formatted RFC822 Addr822Full string generated from the specified Name. Empty string on error or no value for
	 *         name.getAddr821().
	 */
	public static String buildAddr822Full(final lotus.domino.Name name) {
		try {
			String addr821 = name.getAddr821();
			if (Strings.isBlankString(addr821)) {
				return ""; // fast exit, if there is no Addr821
			}
			return RFC822name.buildAddr822Full(name.getAddr822Phrase(), addr821, name.getAddr822Comment1(), name.getAddr822Comment2(),
					name.getAddr822Comment3());
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	/**
	 * Gets alll names from a specified source which are missing the specified roles for a specified database.
	 *
	 * @param session
	 *            Session in effect.
	 * @param database
	 *            Database from which to query the ACL roles.
	 * @param sourcenames
	 *            Name strings for which to check roles in the Database.
	 * @param sourceroles
	 *            Roles to check for in the Database.
	 * @return Set of Name objects constructed from sourcenames which do NOT have any of the specified roles in the Database.
	 */
	public static TreeSet<Name> getNamesMissingRoles(final Session session, final Database database, final TreeSet<String> sourcenames,
			final TreeSet<String> sourceroles) {

		final TreeSet<Name> result = new TreeSet<Name>();
		try {
			if ((null != sourcenames) && !sourcenames.isEmpty() && (null != sourceroles) && !sourceroles.isEmpty()) {
				final TreeSet<String> checkroles = new TreeSet<String>();
				for (final String s : sourceroles) {
					if (!Strings.isBlankString(s)) {
						checkroles.add(Names.formatAsRole(s));
					}
				}

				if (checkroles.size() > 0) {
					for (final String s : sourcenames) {
						if (!Strings.isBlankString(s)) {
							final Name name = session.createName(s);
							if (!result.contains(name)) {
								final TreeSet<String> roles = CollectionUtils
										.getTreeSetStrings(database.queryAccessRoles(name.getCanonical()));
								if (null == roles) {
									result.add(name);
								} else {
									for (final String role : checkroles) {
										if (!roles.contains(role)) {
											result.add(name);
										}
									}
								}
							}
						}
					}
				}
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return result;
	}

	/**
	 * Gets alll names from a specified source which are missing the specified roles for a specified database.
	 *
	 * @param session
	 *            Session in effect.
	 * @param database
	 *            Database from which to query the ACL roles.
	 * @param sourcenames
	 *            Object from which a TreeSet of Name strings can be constructed for which to check roles in the Database.
	 * @param sourceroles
	 *            Object from which a TreeSet of Role strings can be constructed to check for in the Database.
	 * @return Set of Name objects constructed from sourcenames which do NOT have any of the specified roles in the Database.
	 */
	public static TreeSet<Name> getNamesMissingRoles(final Session session, final Database database, final Object sourcenames,
			final Object sourceroles) {
		return Names.getNamesMissingRoles(session, database, CollectionUtils.getTreeSetStrings(sourcenames),
				CollectionUtils.getTreeSetStrings(sourceroles));
	}

	/**
	 * Determines if a specified Name is a member of a set of name strings.
	 *
	 * @param session
	 *            Session in effect.
	 * @param names
	 *            Name strings for which to determine if name is a member.
	 * @param name
	 *            Name to search for in names.
	 *
	 * @return Flag indicating if name is a member of names.
	 */
	public static boolean isNamesListMember(final Session session, final TreeSet<String> names, final Name name) {

		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}
			if (null == names) {
				throw new IllegalArgumentException("Names is null");
			}
			if (null == name) {
				throw new IllegalArgumentException("Name is null");
			}

			if (names.size() < 1) {
				return false;

			}

			final String abbreviated = name.getAbbreviated();
			final String common = name.getCommon();
			final String canonical = name.getCanonical();

			// do an initial check for membership
			if (names.contains(canonical) || names.contains(abbreviated) || names.contains(common)) {
				return true;
			}

			// do a case-insensitive check for membership
			for (final String t : names) {
				if (canonical.equalsIgnoreCase(t) || abbreviated.equalsIgnoreCase(t) || common.equalsIgnoreCase(t)) {
					return true;
				}
			}

			final Pattern pattern = DACL.getPatternForRoles();

			// do a case insensitive check for constructed name matches
			for (final String entry : names) {
				final Matcher matcher = pattern.matcher(entry);
				if (!matcher.matches()) {
					// entry is not a role
					Name entryname = Names.createName(session, entry);
					if (entryname.getAbbreviated().equalsIgnoreCase(abbreviated)) {
						return true;
					}
				}
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Determines if a specified name string is a member of a set of name strings.
	 *
	 * @param session
	 *            Session in effect.
	 * @param names
	 *            Name strings for which to determine if checkname is a member.
	 * @param checkname
	 *            Name string for which to construct a Name object which is searched for in names.
	 *
	 * @return Flag indicating if checkname is a member of names.
	 */
	public static boolean isNamesListMember(final Session session, final TreeSet<String> names, final String checkname) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}
			if (null == names) {
				throw new IllegalArgumentException("Names is null");
			}
			if (Strings.isBlankString(checkname)) {
				throw new IllegalArgumentException("CheckName is null or blank");
			}
			if (names.size() < 1) {
				return false;
			}

			return Names.isNamesListMember(session, names, Names.createName(session, checkname));

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/*
	 * ************************************************************************
	 * ************************************************************************
	 *
	 * PRIVATE methods
	 *
	 * ************************************************************************
	 * ************************************************************************
	 */
	/**
	 * Gets the ACL Roles associated with a specified canonical name for a specified Database.
	 *
	 * @param database
	 *            Database from which to get ACL Roles for canonical.
	 * @param canonical
	 *            Canonical string for which to search for roles in database.
	 *
	 * @return Set of roles for the specified canonical name. Null on error or none found.
	 */
	private static TreeSet<String> getRoles(final Database database, final String canonical) {
		try {
			if (null == database) {
				throw new IllegalArgumentException("Database is null");
			}
			if (Strings.isBlankString(canonical)) {
				throw new IllegalArgumentException("Canonical  is null");
			}

			final List<String> roles = database.queryAccessRoles(canonical);
			return (roles.size() > 0) ? new TreeSet<String>(roles) : null;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Fully expands a list of name strings.
	 *
	 * Searches a specified view for person or group entries. For every group found the members of the group will be checked against the
	 * searched set. If not in the searched list they will also be searched for (and added to the searched list). Found group members will
	 * be included in the results.
	 *
	 * @param session
	 *            Session in effect for the search.
	 * @param view
	 *            View within which to search for entries.
	 * @param searchfor
	 *            Set of name strings for which to search.
	 * @param searched
	 *            Set of name strings which have already been searched.
	 * @return Set of all expanded found names & group members. K = name string, V = Result type (Person, Group, Unknown)
	 */
	private static HashMap<String, Names.LookupType> expandNamesList(final Session session, final View view,
			final TreeSet<String> searchfor, HashMap<String, Names.LookupType> searched) {

		HashMap<String, Names.LookupType> result = new HashMap<String, Names.LookupType>();

		try {
			if (null == searched) {
				searched = new HashMap<String, Names.LookupType>();
			} else {
				result = searched;
			}
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}
			if (null == view) {
				throw new IllegalArgumentException("View is null");
			}

			if ((null != searchfor) && (searchfor.size() > 0)) {
				for (final String s : searchfor) {
					if ((!Strings.isBlankString(s)) && (!result.containsKey(s)) && (!searched.containsKey(s))) {
						String key = s;
						ViewEntry vent = view.getFirstEntryByKey(key);
						if (null == vent) {
							key = Names.getAbbreviated(session, s);
							if ((!result.containsKey(key)) && (!searched.containsKey(key))) {
								vent = view.getFirstEntryByKey(key);
							}
						}

						if (null != vent) {
							final String tag = (String) vent.getColumnValues().get(0);
							if (Names.LookupType.Person.getCode().equalsIgnoreCase(tag)) {
								result.put(key, Names.LookupType.Person);

							} else if (Names.LookupType.Group.getCode().equalsIgnoreCase(tag)) {
								result.put(key, Names.LookupType.Group);
								Document document = vent.getDocument();

								final TreeSet<String> ts = CollectionUtils
										.getTreeSetStrings(document.getItemValue(Documents.ITEMNAME_MEMBERS));
								if (null != ts) {
									final HashMap<String, Names.LookupType> found = Names.expandNamesList(session, view, ts, result);
									if ((null != found) && (found.size() > 0)) {
										final Iterator<Map.Entry<String, Names.LookupType>> it = found.entrySet().iterator();
										while (it.hasNext()) {
											final Map.Entry<String, Names.LookupType> entry = it.next();
											if (!result.containsKey(entry.getKey())) {
												result.put(entry.getKey(), entry.getValue());
											}
										}
									}
								}

							} else {
								result.put(key, Names.LookupType.Unknown);
							}
						}
					}
				}
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return result;
	}

	/**
	 * Fully expands a list of name strings.
	 *
	 * Searches a specified database for person or group entries. For every group found the members of the group will be checked against the
	 * searched set. If not in the searched list they will also be searched for (and added to the searched list). Found group members will
	 * be included in the results.
	 *
	 * NOTE: NO CHECKS ARE PERFORMED TO DETERMINE IF DATABASE IS PUBLIC ADDRESS BOOK
	 *
	 * @param session
	 *            Session in effect for the search.
	 * @param database
	 *            Database within which to search for entries.
	 * @param searchfor
	 *            Set of name strings for which to search.
	 * @param searched
	 *            Set of name strings which have already been searched.
	 * @return Set of all expanded found names & group members. K = name string, V = Result type (Person, Group, Unknown)
	 */
	private static HashMap<String, Names.LookupType> expandNamesList(final Session session, final Database database,
			final TreeSet<String> searchfor, final HashMap<String, Names.LookupType> searched) {

		View view = null;
		HashMap<String, Names.LookupType> result = new HashMap<String, Names.LookupType>();

		try {
			if (null != searched) {
				result = searched;
			}
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}
			if (null == database) {
				throw new IllegalArgumentException("Database is null");
			}

			if ((null != searchfor) && (searchfor.size() > 0)) {
				view = database.getView(DominoUtils.VIEWNAME_VIM_PEOPLE_AND_GROUPS);
				view.setAutoUpdate(false);
				final HashMap<String, Names.LookupType> found = Names.expandNamesList(session, view, searchfor, result);
				if ((null != found) && (found.size() > 0)) {
					final Iterator<Map.Entry<String, Names.LookupType>> it = found.entrySet().iterator();
					while (it.hasNext()) {
						final Map.Entry<String, Names.LookupType> entry = it.next();
						if (!result.containsKey(entry.getKey())) {
							result.put(entry.getKey(), entry.getValue());
						}
					}
				}
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return result;
	}

}
