/**
 * 
 */
package org.openntf.domino.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;

import org.openntf.domino.impl.NameHandle;

/**
 * Name handling utilities
 * 
 * @author Devin S. Olson (dolson@czarnowski.com)
 * 
 */
public enum Names {
	;

	public static enum NAMETYPE {
		Abbreviated, Canonical, Common;

		@Override
		public String toString() {
			return this.getDeclaringClass() + "." + this.getClass() + ":" + this.name();
		}
	};

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
	 * @param nametype
	 *            Type of name string to return
	 * 
	 * @return source string converted to the appropriate name format
	 */
	public static String getNameString(final Session session, final String source, final Names.NAMETYPE nametype) {
		lotus.domino.Name name = null;
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}
			if (null == nametype) {
				throw new IllegalArgumentException("NameType is null");
			}

			final String seed = (Strings.isBlankString(source)) ? Names.getEffectiveUserName(session) : source;
			name = Names.createName(session, seed);
			final String result = (nametype.equals(Names.NAMETYPE.Abbreviated)) ? name.getAbbreviated() : (nametype
					.equals(Names.NAMETYPE.Canonical)) ? name.getCanonical() : (nametype.equals(Names.NAMETYPE.Common)) ? name.getCommon()
					: null;
			name.recycle();
			return Strings.toProperCase(result);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		} finally {
			DominoUtils.incinerate(name);
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
	 * @param nametype
	 *            Type of name string to return
	 * 
	 * @return source strings converted to the appropriate name format, in the order in which they exist in source.
	 */
	public static String[] getNameStrings(final Session session, final String[] source, final Names.NAMETYPE nametype) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}

			if (null == source) {
				throw new IllegalArgumentException("Source Array is null");
			}

			if (null == nametype) {
				throw new IllegalArgumentException("NameType is null");
			}

			if (source.length < 0) {
				return null;
			}

			final List<String> values = new ArrayList<String>();
			for (final String temp : source) {
				if (!Strings.isBlankString(temp)) {
					final String name = Names.getNameString(session, temp, nametype);

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
		return Names.getNameStrings(session, source, Names.NAMETYPE.Abbreviated);
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
		return Names.getNameString(session, source, Names.NAMETYPE.Abbreviated);
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
		return Names.getNameStrings(session, source, Names.NAMETYPE.Canonical);
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
		return Names.getNameString(session, source, Names.NAMETYPE.Canonical);
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
		return Names.getNameStrings(session, source, Names.NAMETYPE.Common);
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
		return Names.getNameString(session, source, Names.NAMETYPE.Common);
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

	public static String getEffectiveUserName(final Session session) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}

			return session.getEffectiveUserName();

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	public static lotus.domino.Name createName(final Session session, final String source) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}

			return session.createName((Strings.isBlankString(source)) ? session.getEffectiveUserName() : source);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	public static TreeSet<String> getRoles(final Session session, final Database database) {
		lotus.domino.Name name = null;
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}
			if (null == database) {
				throw new IllegalArgumentException("Database is null");
			}

			name = Names.createName(session, Names.getEffectiveUserName(session));
			return Names.getRoles(database, name);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		} finally {
			DominoUtils.incinerate(name);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static TreeSet<String> getRoles(final Database database, final lotus.domino.Name name) {
		try {
			if (null == database) {
				throw new IllegalArgumentException("Database is null");
			}
			if (null == name) {
				throw new IllegalArgumentException("Name is null");
			}

			final List<String> roles = database.queryAccessRoles(name.getCanonical());
			return (roles.size() > 0) ? new TreeSet<String>(roles) : null;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> expandNamesList(final Session session, final TreeSet<String> searchfor,
			final HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> searched) {

		Database database = null;

		HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> result = new HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE>();

		try {
			if (null != searched) {
				result = searched;
			}
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}

			if ((null != searchfor) && (searchfor.size() > 0)) {
				final Vector<Database> books = session.getAddressBooks();
				final Enumeration<Database> e = books.elements();
				while (e.hasMoreElements()) {
					database = e.nextElement();
					if (database.isPublicAddressBook()) {
						try {
							database.open();
							if (database.isOpen()) {
								final HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> found = Names.expandNamesList(session, database,
										searchfor, result);
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

						} catch (final NotesException ne) {
							DominoUtils.handleException(ne);
						}
					}

					DominoUtils.incinerate(database);
				}
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		} finally {
			DominoUtils.incinerate(database);
		}

		return result;
	}

	public static TreeSet<String> expandNamesList(final Session session, final Object source, final DIRECTORY_SEARCH_RESULT_TYPE... filters) {
		lotus.domino.Name name = null;
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
							name = session.createName(s);
							final String key = name.getAbbreviated();
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
									name = session.createName(key);
									temp.put(name.getAbbreviated().toLowerCase(), name.getAbbreviated());
									DominoUtils.incinerate(name);
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
		} finally {
			DominoUtils.incinerate(name);
		}

		return null;
	}

	public static TreeSet<String> expandNamesList(final Session session, final Object source) {
		return Names.expandNamesList(session, source, DIRECTORY_SEARCH_RESULT_TYPE.values());
	}

	/*
	 * ************************************************************************
	 * ************************************************************************
	 * 
	 * NAMEHANDLE methods
	 * 
	 * ************************************************************************
	 * ************************************************************************
	 */
	public static lotus.domino.Name createName(final Session session, final NameHandle namehandle) {
		return Names.createName(session, namehandle.getCanonical());
	}

	public static NameHandle getNameHandleFromString(final Session session, final String string) {
		try {
			return new NameHandle(session, string);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	public static NameHandle getNameHandleFromDocumentItem(final Session session, final Document document, final String itemname) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (Strings.isBlankString(itemname)) {
				throw new IllegalArgumentException("Item Name is blank or null");
			}

			final String string = document.getItemValueString(itemname);
			return (Strings.isBlankString(string)) ? null : Names.getNameHandleFromString(session, string);

			// return new NameHandle(session, string);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	public static TreeSet<NameHandle> getNameHandlesMissingRoles(final Session session, final Database database,
			final TreeSet<String> sourcenames, final TreeSet<String> sourceroles) {

		final TreeSet<NameHandle> result = new TreeSet<NameHandle>();
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
							final NameHandle namehandle = Names.getNameHandleFromString(session, s);
							if (!result.contains(namehandle)) {
								final TreeSet<String> roles = CollectionUtils.getTreeSetStrings(database.queryAccessRoles(namehandle
										.getCanonical()));
								if (null == roles) {
									result.add(namehandle);
								} else {
									for (final String role : checkroles) {
										if (!roles.contains(role)) {
											result.add(namehandle);
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

	public static TreeSet<NameHandle> getNameHandlesMissingRoles(final Session session, final Database database, final Object sourcenames,
			final Object sourceroles) {
		return Names.getNameHandlesMissingRoles(session, database, CollectionUtils.getTreeSetStrings(sourcenames),
				CollectionUtils.getTreeSetStrings(sourceroles));
	}

	public static boolean isNamesListMember(final Session session, final TreeSet<String> names, final NameHandle namehandle) {

		lotus.domino.Name entryname = null;
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}
			if (null == names) {
				throw new IllegalArgumentException("Names is null");
			}
			if (null == namehandle) {
				throw new IllegalArgumentException("NameHandle is null");
			}

			if (names.size() < 1) {
				return false;

			}

			final String abbreviated = namehandle.getAbbreviated();
			final String common = namehandle.getCommon();
			final String canonical = namehandle.getCanonical();

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

			final Pattern pattern = CzarDACL.getPatternForRoles();

			// do a case insensitive check for constructed name matches
			for (final String entry : names) {
				final Matcher matcher = pattern.matcher(entry);
				if (!matcher.matches()) {
					// entry is not a role
					entryname = Names.createName(session, entry);
					if (entryname.getAbbreviated().equalsIgnoreCase(abbreviated)) {
						return true;
					}

					DominoUtils.incinerate(entryname);
				}
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		} finally {
			DominoUtils.incinerate(entryname);
		}

		return false;
	}

	public static boolean isNamesListMember(final Session session, final TreeSet<String> names, final lotus.domino.Name checkname) {

		try {
			if (null == checkname) {
				throw new IllegalArgumentException("Name is null");
			}

			return Names.isNamesListMember(session, names, new NameHandle(checkname));

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	public static boolean isNamesListMember(final Session session, final TreeSet<String> names, final String checkname) {
		lotus.domino.Name name = null;
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

			name = Names.createName(session, checkname);
			final boolean result = Names.isNamesListMember(session, names, name);
			name.recycle();
			return result;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		} finally {
			DominoUtils.incinerate(name);
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public static TreeSet<String> getRoles(final Database database, final NameHandle namehandle) {
		try {
			if (null == database) {
				throw new IllegalArgumentException("Database is null");
			}
			if (null == namehandle) {
				throw new IllegalArgumentException("NameHandle is null");
			}

			final List<String> roles = database.queryAccessRoles(namehandle.getCanonical());
			return (roles.size() > 0) ? new TreeSet<String>(roles) : null;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
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
	private static HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> expandNamesList(final Session session, final View view,
			final TreeSet<String> searchfor, HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> searched) {

		lotus.domino.Name name = null;
		Document document = null;
		ViewEntry vent = null;
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
						vent = view.getEntryByKey(key);
						if (null == vent) {
							name = session.createName(s);
							key = name.getAbbreviated();
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
								document = vent.getDocument();
								final TreeSet<String> treeset = CollectionUtils.getTreeSetStrings(document
										.getItemValue(DominoUtils.ITEMNAME_MEMBERS));
								if (null != treeset) {
									final HashMap<String, DIRECTORY_SEARCH_RESULT_TYPE> found = Names.expandNamesList(session, view,
											treeset, result);
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
		} finally {
			DominoUtils.incinerate(name, document, vent);
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
		} finally {
			DominoUtils.incinerate(view);
		}

		return result;
	}

}
