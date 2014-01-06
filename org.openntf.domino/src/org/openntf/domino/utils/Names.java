/**
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

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.impl.Name.NamePart;

/**
 * Name handling utilities
 * 
 * @author Devin S. Olson (dolson@czarnowski.com)
 * 
 */
public enum Names {
	;

	public static enum DIRECTORY_SEARCH_RESULT_TYPE {
		Person("P"), Group("G"), Unknown("U");

		private String Key;

		private DIRECTORY_SEARCH_RESULT_TYPE(final String key) {
			this.setKey(key);
		}

		@Override
		public String toString() {
			return this.name();
		}

		public String getInfo() {
			return this.getDeclaringClass() + "." + this.getClass() + ":" + this.name();
		}

		public String getKey() {
			return this.Key;
		}

		public void setKey(final String key) {
			this.Key = key;
		}
	};

	/**
	 * Formats a String as a role (begins with "[", ends with "]")
	 * 
	 * @param string
	 *            String to be formatted
	 * 
	 * @return source string formatted as a role.
	 */
	public static String formatAsRole(final String string) {
		return (Strings.isBlankString(string)) ? ""
				: ((string.indexOf('[') == 0) && (string.indexOf(']') == (string.length() - 1))) ? string : "[" + string + "]";
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
	 * @param part
	 *            Part of name string to return
	 * 
	 * @return source string converted to the appropriate name format
	 */
	public static String getNamePart(final Session session, final String source, final NamePart part) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}
			if (null == part) {
				throw new IllegalArgumentException("NamePart is null");
			}

			final String seed = (Strings.isBlankString(source)) ? session.getEffectiveUserName() : source;
			org.openntf.domino.impl.Name name = (org.openntf.domino.impl.Name) Names.createName(session, seed);
			return name.getNamePart(part);

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
	 * @param part
	 *            Part of name string to return
	 * 
	 * @return source strings converted to the appropriate name format, in the order in which they exist in source.
	 */
	public static String[] getNameParts(final Session session, final String[] source, final NamePart part) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}

			if (null == source) {
				throw new IllegalArgumentException("Source Array is null");
			}

			if (null == part) {
				throw new IllegalArgumentException("NamePart is null");
			}

			if (source.length < 1) {
				return null;
			}

			final List<String> values = new ArrayList<String>();
			for (final String temp : source) {
				if (!Strings.isBlankString(temp)) {
					final String name = Names.getNamePart(session, temp, part);

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
		return Names.getNameParts(session, source, NamePart.Abbreviated);
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
		return Names.getNamePart(session, source, NamePart.Abbreviated);
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
		return Names.getNameParts(session, source, NamePart.Canonical);
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
		return Names.getNamePart(session, source, NamePart.Canonical);
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
		return Names.getNameParts(session, source, NamePart.Common);
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
		return Names.getNamePart(session, source, NamePart.Common);
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

	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	public static HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> expandNamesList(final Session session, final TreeSet<String> searchfor,
			final HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> searched) {

		HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> result = new HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE>();

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
							final HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> found = Names.expandNamesList(session, database, searchfor,
									result);
							if ((null != found) && (found.size() > 0)) {
								final Iterator<Map.Entry<String, DIRECTORY_SEARCH_RESULT_TYPE>> it = found.entrySet().iterator();
								while (it.hasNext()) {
									final Map.Entry<String, DIRECTORY_SEARCH_RESULT_TYPE> entry = it.next();
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

	public static TreeSet<String> expandNamesList(final Session session, final Object source, final DIRECTORY_SEARCH_RESULT_TYPE... filters) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}

			final TreeSet<String> result = new TreeSet<String>();
			if ((null != filters) && (filters.length > 0)) {
				final TreeSet<String> searchfor = CollectionUtils.getTreeSetStrings(source);
				if ((null != searchfor) && (searchfor.size() > 0)) {
					HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> found = Names.expandNamesList(session, searchfor,
							new HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE>());

					// add the searchfor values to found
					if (null == found) {
						found = new HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE>();
					}
					for (final String s : searchfor) {
						if (!found.containsKey(s)) {
							final String key = Names.getAbbreviated(session, s);
							if (!found.containsKey(key)) {
								found.put(key, DIRECTORY_SEARCH_RESULT_TYPE.Unknown);
							}
						}
					}

					if (found.size() > 0) {
						final HashMap<String, String> temp = new HashMap<String, String>();
						final Iterator<Map.Entry<String, DIRECTORY_SEARCH_RESULT_TYPE>> it = found.entrySet().iterator();
						while (it.hasNext()) {
							boolean include = false;
							final Map.Entry<String, DIRECTORY_SEARCH_RESULT_TYPE> entry = it.next();
							final String key = entry.getKey();
							final DIRECTORY_SEARCH_RESULT_TYPE value = entry.getValue();
							for (final DIRECTORY_SEARCH_RESULT_TYPE filter : filters) {
								if (filter.equals(value)) {
									include = true;
									break;
								}
							}
							if (include) {
								if (DIRECTORY_SEARCH_RESULT_TYPE.Person.equals(value)) {
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

	public static TreeSet<String> expandNamesList(final Session session, final Object source) {
		return Names.expandNamesList(session, source, DIRECTORY_SEARCH_RESULT_TYPE.values());
	}

	public static Name createName(final Session session, final String source) {
		try {
			return new org.openntf.domino.impl.Name(session, source);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	public static Name createName(final Session session, final Document document, final String itemname) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (Strings.isBlankString(itemname)) {
				throw new IllegalArgumentException("Item Name is blank or null");
			}

			final String string = document.getItemValueString(itemname);
			return (Strings.isBlankString(string)) ? null : Names.createName(session, string);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	public static TreeSet<Name> getNamesMissingRoles(final Session session, final Database database, final TreeSet<String> sourcenames,
			final TreeSet<String> sourceroles) {

		final TreeSet<Name> result = new TreeSet<Name>();
		try {
			if ((null != sourcenames) && (sourcenames.size() > 0) && (null != sourceroles) && (sourceroles.size() > 0)) {
				final TreeSet<String> checkroles = new TreeSet<String>();
				for (final String s : sourceroles) {
					if (!Strings.isBlankString(s)) {
						checkroles.add(Names.formatAsRole(s));
					}
				}

				if (checkroles.size() > 0) {
					for (final String s : sourcenames) {
						if (!Strings.isBlankString(s)) {
							final Name name = new org.openntf.domino.impl.Name(session, s);
							if (!result.contains(name)) {
								final TreeSet<String> roles = CollectionUtils.getTreeSetStrings(database.queryAccessRoles(name
										.getCanonical()));
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

	public static TreeSet<Name> getNamesMissingRoles(final Session session, final Database database, final Object sourcenames,
			final Object sourceroles) {
		return Names.getNamesMissingRoles(session, database, CollectionUtils.getTreeSetStrings(sourcenames),
				CollectionUtils.getTreeSetStrings(sourceroles));
	}

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

	private static HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> expandNamesList(final Session session, final View view,
			final TreeSet<String> searchfor, HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> searched) {

		HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> result = new HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE>();

		try {
			if (null == searched) {
				searched = new HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE>();
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
						ViewEntry vent = view.getEntryByKey(key);
						if (null == vent) {
							key = Names.getAbbreviated(session, s);
							if ((!result.containsKey(key)) && (!searched.containsKey(key))) {
								vent = view.getEntryByKey(key);
							}
						}

						if (null != vent) {
							final String tag = (String) vent.getColumnValues().get(0);
							if (DIRECTORY_SEARCH_RESULT_TYPE.Person.getKey().equalsIgnoreCase(tag)) {
								result.put(key, DIRECTORY_SEARCH_RESULT_TYPE.Person);

							} else if (DIRECTORY_SEARCH_RESULT_TYPE.Group.getKey().equalsIgnoreCase(tag)) {
								result.put(key, DIRECTORY_SEARCH_RESULT_TYPE.Group);
								Document document = vent.getDocument();

								final TreeSet<String> ts = CollectionUtils.getTreeSetStrings(document
										.getItemValue(DominoUtils.ITEMNAME_MEMBERS));
								if (null != ts) {
									final HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> found = Names.expandNamesList(session, view, ts,
											result);
									if ((null != found) && (found.size() > 0)) {
										final Iterator<Map.Entry<String, DIRECTORY_SEARCH_RESULT_TYPE>> it = found.entrySet().iterator();
										while (it.hasNext()) {
											final Map.Entry<String, DIRECTORY_SEARCH_RESULT_TYPE> entry = it.next();
											if (!result.containsKey(entry.getKey())) {
												result.put(entry.getKey(), entry.getValue());
											}
										}
									}
								}

							} else {
								result.put(key, DIRECTORY_SEARCH_RESULT_TYPE.Unknown);
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

	private static HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> expandNamesList(final Session session, final Database database,
			final TreeSet<String> searchfor, final HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> searched) {

		View view = null;
		HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> result = new HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE>();

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
				final HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> found = Names.expandNamesList(session, view, searchfor, result);
				if ((null != found) && (found.size() > 0)) {
					final Iterator<Map.Entry<String, DIRECTORY_SEARCH_RESULT_TYPE>> it = found.entrySet().iterator();
					while (it.hasNext()) {
						final Map.Entry<String, DIRECTORY_SEARCH_RESULT_TYPE> entry = it.next();
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
