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

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.Name;
import org.openntf.domino.Session;

/**
 * Document Access Control List Tools & Utilities
 * 
 * @author Devin S. Olson (dolson@czarnowski.com)
 * 
 */
public enum DACL {
	;

	public static final String ITEMNAME_DACL_READERS = "$DACL_Readers";
	public static final String ITEMNAME_DACL_AUTHORS = "$DACL_Authors";
	public static final String DEFAULT_DACL_READERS = "[ReadAll]";
	public static final String DEFAULT_DACL_AUTHORS = "[EditAll]";

	// starts with "[", has any number of letters, spaces, hyphens, or underscores, ends with "]"
	public static final String PATTERNTEXT_ROLES = "(?i)^\\[[a-z0-9 -_]+\\]$";

	public static enum TYPE {
		AUTHORS(DACL.ITEMNAME_DACL_AUTHORS, DACL.DEFAULT_DACL_AUTHORS), READERS(DACL.ITEMNAME_DACL_AUTHORS, DACL.DEFAULT_DACL_READERS);

		private final String _itemname;
		private final String _role;

		public String getItemname() {
			return this._itemname;
		}

		public String getRole() {
			return this._role;
		}

		private TYPE(final String itemname, final String role) {
			this._itemname = itemname;
			this._role = role;
		}

		@Override
		public String toString() {
			return TYPE.class.getName() + ": " + this.name() + "{\"" + this.getItemname() + "\", \"" + this.getRole() + "\"}";
		}

		/**
		 * Gets the DACL Member values from the source document.
		 * 
		 * Retreives values ONLY from the item defined by {@link DACL.TYPE#getItemname()}.
		 * 
		 * @param document
		 *            Document from which to retreive the Member values.
		 * 
		 * @return DACL Member values. Null if not present or empty.
		 */
		public TreeSet<String> getMembers(final Document document) {
			return DACL.getMembers(document, this, false);
		}

		/**
		 * Gets the DACL Member values from the source document.
		 * 
		 * Conditionally retreives values from ALL DACL.TYPE items or ONLY from the item defined by {@link DACL.TYPE#getItemname()}.
		 * 
		 * @param document
		 *            Document from which to retreive the Member values.
		 * 
		 * @param checkAllItems
		 *            Flag indicating if all items should be checked. If false then only the {@link DACL.TYPE#getItemname()} will be
		 *            checked.
		 * 
		 * @return DACL Member values. Null if not present or empty.
		 */
		public TreeSet<String> getMembers(final Document document, final boolean checkAllItems) {
			return DACL.getMembers(document, this, checkAllItems);
		}

		/**
		 * Determines if the passed in name is a member of the dacl entries.
		 * 
		 * Returns true if the name (in any name format), or any member of roles is a member of the dacl entries.
		 * 
		 * @param session
		 *            Current Session
		 * 
		 * @param dacl
		 *            DACL entries to check
		 * 
		 * @param roles
		 *            ACL Roles belonging to the name to check. (No verification is performed)
		 * 
		 * @param name
		 *            Name to check
		 * 
		 * @return Flag indicating if the name (in any form) is a member of the dacl set, or if any intersection exists between dacl and
		 *         roles.
		 */
		public boolean isMember(final Session session, final TreeSet<String> dacl, final TreeSet<String> roles, final Name name) {
			try {
				if (null == session) {
					throw new IllegalArgumentException("Session is null");
				}
				if (null == name) {
					throw new IllegalArgumentException("Name is null");
				}

				if ((null != dacl) && (dacl.size() > 0)) {
					// do an initial check on the name
					if (dacl.contains(name.getCanonical()) || dacl.contains(name.getAbbreviated())) {
						return true;
					}

					// do an initial check for matching roles
					if (null != roles) {
						for (final String role : roles) {
							if (dacl.contains(role)) {
								return true;
							}
						}
					}

					// do a more thorough check for roles
					final Pattern pattern = DACL.getPatternForRoles();
					if (null != roles) {
						for (final String entry : dacl) {
							final Matcher matcher = pattern.matcher(entry);
							if (matcher.matches()) {
								// entry represents a role
								if (roles.contains(entry)) {
									return true;
								}
							}
						}

						// do a case-insensitive check for roles
						for (final String entry : dacl) {
							final Matcher matcher = pattern.matcher(entry);
							if (matcher.matches()) {
								// entry represents a role
								for (final String role : roles) {
									if (role.equalsIgnoreCase(entry)) {
										return true;
									}
								}
							}
						}
					}

					return Names.isNamesListMember(session, dacl, name);
				}

			} catch (final Exception e) {
				DominoUtils.handleException(e);
			}

			return false;
		}

		/**
		 * Determines if the passed in name is a member of the specified DACL for the document.
		 * 
		 * Conditionally checks values from ALL DACL.TYPE items or ONLY from the item defined by {@link DACL.TYPE#getItemname()}.
		 * 
		 * @param session
		 *            Current Session
		 * @param document
		 *            Document to search for DACL
		 * @param name
		 *            Name to check
		 * 
		 * @param checkAllItems
		 *            Flag indicating if all items should be checked. If false then only the {@link DACL.TYPE#getItemname()} will be
		 *            checked.
		 * 
		 * @return Flag indicating if the name is a member of the specified DACL for the document.
		 */
		public boolean isMember(final Session session, final Document document, final Name name, final boolean checkAllItems) {
			try {
				if (null == session) {
					throw new IllegalArgumentException("Session is null");
				}
				if (null == document) {
					throw new IllegalArgumentException("Document is null");
				}
				if (null == name) {
					throw new IllegalArgumentException("Name is null");
				}

				final TreeSet<String> roles = Names.getRoles(document.getParentDatabase(), name);
				final TreeSet<String> dacl = this.getMembers(document, checkAllItems);

				return this.isMember(session, dacl, roles, name);

			} catch (final Exception e) {
				DominoUtils.handleException(e);
			}

			return false;
		}

		/**
		 * Determines if the passed in name is a member of the specified DACL for the document.
		 * 
		 * Checks values ONLY from the item defined by {@link DACL.TYPE#getItemname()}.
		 * 
		 * @param session
		 *            Current Session
		 * @param document
		 *            Document to search for DACL
		 * @param name
		 *            Name to check
		 * 
		 * @return Flag indicating if the name is a member of the specified DACL for the document.
		 */
		public boolean isMember(final Session session, final Document document, final Name name) {
			return this.isMember(session, document, name, false);
		}

		/**
		 * Determines if the current user is a member of the specified DACL for the document.
		 * 
		 * Conditionally checks values from ALL DACL.TYPE items or ONLY from the item defined by {@link DACL.TYPE#getItemname()}.
		 * 
		 * @param session
		 *            Current Session
		 * @param document
		 *            Document to search for DACL
		 * 
		 * @param checkAllItems
		 *            Flag indicating if all items should be checked. If false then only the {@link DACL.TYPE#getItemname()} will be
		 *            checked.
		 * 
		 * @return Flag indicating if the name is a member of the specified DACL for the document.
		 */
		public boolean isMember(final Session session, final Document document, final boolean checkAllItems) {
			return this.isMember(session, document, Names.createName(session, ""), checkAllItems);
		}

		/**
		 * Determines if the current user is a member of the specified DACL for the document.
		 * 
		 * Checks values ONLY from the item defined by {@link DACL.TYPE#getItemname()}.
		 * 
		 * @param session
		 *            Current Session
		 * @param document
		 *            Document to search for DACL
		 * @param name
		 *            Name to check
		 * 
		 * @return Flag indicating if the name is a member of the specified DACL for the document.
		 */
		public boolean isMember(final Session session, final Document document) {
			return this.isMember(session, document, false);
		}

		/**
		 * Sets the DACL.TYPE item on the source document. Does NOT save the document.
		 * 
		 * Conditionally overrides the item name.
		 * 
		 * Conditionally adds the Default role {@link DACL.TYPE#getRole()}
		 * 
		 * @param document
		 *            Document upon which to set the DACL_AUTHORS item.
		 * 
		 * @param members
		 *            Person names or roles to be set as the DACL_AUTHORS value.
		 * 
		 * @param itemname
		 *            Name of DACL item to be added. Overrides {@link DACL.TYPE#getItemname()}. If null or blank then
		 *            {@link DACL.TYPE#getItemname()} will be used.
		 * 
		 * @param doNotAddRole
		 *            Flag indicating if the {@link DACL.TYPE#getRole()} should be explicitly added. If True, the role will not be added
		 *            NOTE: if the Role is a member of members it will NOT be excluded, regardless of doNotAddRole
		 * 
		 * @return Flag indicating the success / failure of the operation.
		 */
		public boolean setMembers(final Document document, final TreeSet<String> members, final String itemname, final boolean doNotAddRole) {
			return DACL
					.setMembers(document, members, (Strings.isBlankString(itemname)) ? this.getItemname() : itemname, this, doNotAddRole);
		}

		/**
		 * Sets the DACL.TYPE item on the source document. Does NOT save the document.
		 * 
		 * Conditionally adds the Default role {@link DACL.TYPE#getRole()}
		 * 
		 * @param document
		 *            Document upon which to set the DACL_AUTHORS item.
		 * 
		 * @param members
		 *            Person names or roles to be set as the DACL_AUTHORS value.
		 * 
		 * @param doNotAddRole
		 *            Flag indicating if the {@link DACL.TYPE#getRole()} should be explicitly added. If True, the role will not be added
		 *            NOTE: if the Role is a member of members it will NOT be excluded, regardless of doNotAddRole
		 * 
		 * @return Flag indicating the success / failure of the operation.
		 */
		public boolean setMembers(final Document document, final TreeSet<String> members, final boolean doNotAddRole) {
			return this.setMembers(document, members, null, doNotAddRole);
		}

		/**
		 * Sets the DACL.TYPE item on the source document. Does NOT save the document.
		 * 
		 * Adds the Default role {@link DACL.TYPE#getRole()}
		 * 
		 * @param document
		 *            Document upon which to set the DACL_AUTHORS item.
		 * 
		 * @param members
		 *            Person names or roles to be set as the DACL_AUTHORS value.
		 * 
		 * @return Flag indicating the success / failure of the operation.
		 */
		public boolean setMembers(final Document document, final TreeSet<String> members) {
			return this.setMembers(document, members, null, false);
		}

		/**
		 * Sets the DACL.TYPE item on the source document. Does NOT save the document.
		 * 
		 * Adds the Default role {@link DACL.TYPE#getRole()}
		 * 
		 * @param document
		 *            Document upon which to set the DACL_AUTHORS item.
		 * 
		 * @param values
		 *            Person names or roles (convertable to a TreeSet) to be set as the DACL_AUTHORS value.
		 * 
		 * @return Flag indicating the success / failure of the operation.
		 */
		public boolean setMembers(final Document document, final Object values) {
			return this.setMembers(document, CollectionUtils.getTreeSetStrings(values));
		}

	};

	/*
	 * **************************************************************************
	 * **************************************************************************
	 * 
	 * AUTHOR Methods
	 * 
	 * **************************************************************************
	 * **************************************************************************
	 */
	/**
	 * Determines if the passed in name is a member of the DACL_AUTHORS for the document.
	 * 
	 * @param session
	 *            Current Session
	 * @param document
	 *            Document to search for DACL
	 * @param name
	 *            Name to check
	 * 
	 * @return Flag indicating if the name is a member of the DACL_AUTHORS for the document.
	 */
	public static boolean isDACLauthor(final Session session, final Document document, final Name name) {
		return DACL.TYPE.AUTHORS.isMember(session, document, name);
	}

	/**
	 * Determines if the current user is a member of the DACL_AUTHORS for the document.
	 * 
	 * @param session
	 *            Current Session
	 * @param document
	 *            Document to search for DACL
	 * 
	 * @return Flag indicating if the current user is a member of the DACL_AUTHORS for the document.
	 */
	public static boolean isDACLauthor(final Session session, final Document document) {
		return DACL.TYPE.AUTHORS.isMember(session, document);
	}

	/**
	 * Gets the DACL_AUTHORS values from the source document.
	 * 
	 * @param document
	 *            Document from which to retreive the DACL_AUTHORS values.
	 * 
	 * @return DACL_AUTHORS values. null if not present or empty.
	 */
	public static TreeSet<String> getDACLauthors(final Document document) {
		return DACL.TYPE.AUTHORS.getMembers(document);
	}

	/**
	 * Sets the DACL_AUTHORS item on the source document.
	 * 
	 * The value for DEFAULT_DACL_AUTHORS is included. Does NOT save the document.
	 * 
	 * @param document
	 *            Document upon which to set the DACL_AUTHORS item.
	 * 
	 * @param values
	 *            Person names or roles to be set as the DACL_AUTHORS value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean setDACLauthors(final Document document, final TreeSet<String> values) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (null == values) {
				throw new IllegalArgumentException("Values is null");
			}

			final TreeSet<String> dacl = new TreeSet<String>();
			dacl.add(DACL.DEFAULT_DACL_AUTHORS);
			for (final String entry : values) {
				if (!Strings.isBlankString(entry)) {
					dacl.add(entry);
				}
			}

			final Item item = document.replaceItemValue(DACL.ITEMNAME_DACL_AUTHORS, Strings.getVectorizedStrings(dacl));
			item.setAuthors(true);
			return true;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Sets the DACL_AUTHORS item on the source document.
	 * 
	 * The value for DEFAULT_DACL_AUTHORS is included. Does NOT save the document.
	 * 
	 * @param document
	 *            Document upon which to set the DACL_AUTHORS item.
	 * 
	 * @param values
	 *            Person names or roles (convertable to a TreeSet) to be set as the DACL_AUTHORS value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean setDACLauthors(final Document document, final Object values) {
		return DACL.setDACLauthors(document, CollectionUtils.getTreeSetStrings(values));
	}

	/**
	 * Adds a value to the DACL_AUTHORS for the document.
	 * 
	 * Existing DACL_AUTHORS are not removed. The value for DEFAULT_DACL_AUTHORS is included. Does NOT save the document.
	 * 
	 * @param document
	 *            Document upon which to update the DACL_AUTHORS.
	 * 
	 * @param values
	 *            Person names or roles to be added to the DACL_AUTHORS value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean addDACLauthors(final Document document, final TreeSet<String> values) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (null == values) {
				throw new IllegalArgumentException("Values is null");
			}

			TreeSet<String> dacl = DACL.getDACLauthors(document);
			if (null == dacl) {
				dacl = new TreeSet<String>();
			}
			for (final String entry : values) {
				if (!Strings.isBlankString(entry)) {
					dacl.add(entry);
				}
			}

			return DACL.setDACLauthors(document, dacl);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/**
	 * Adds a value to the DACL_AUTHORS for the document.
	 * 
	 * Existing DACL_AUTHORS are not removed. The value for DEFAULT_DACL_AUTHORS is included. Does NOT save the document.
	 * 
	 * @param document
	 *            Document upon which to update the DACL_AUTHORS.
	 * 
	 * @param value
	 *            Person name or role to be added to the DACL_AUTHORS value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean addDACLauthor(final Document document, final String value) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}

			final TreeSet<String> values = new TreeSet<String>();
			if (!Strings.isBlankString(value)) {
				values.add(value);
			}

			return DACL.addDACLauthors(document, values);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/**
	 * Removes the DACL_AUTHORS item from the source document.
	 * 
	 * Does NOT save the document.
	 * 
	 * @param document
	 *            Document from which to remove the DACL_AUTHORS item.
	 * 
	 * @return Flag indicating if the item was successfully removed.
	 */
	public static boolean removeDACLauthors(final Document document) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}

			if (document.hasItem(DACL.ITEMNAME_DACL_AUTHORS)) {
				document.removeItem(DACL.ITEMNAME_DACL_AUTHORS);
				return true;
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Removes a value from the DACL_AUTHORS item on the source document.
	 * 
	 * Does NOT save the document.
	 * 
	 * @param document
	 *            Document from which to remove the DACL_AUTHORS item.
	 * 
	 * @param value
	 *            Value to be removed from the DACL_AUTHORS
	 * 
	 * @return Flag indicating if the value was successfully removed from the DACL_AUTHORS
	 */
	public static boolean removeDACLauthor(final Document document, final String value) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (Strings.isBlankString(value)) {
				throw new IllegalArgumentException("Value to remove is blank or null");
			}

			final TreeSet<String> dacl = DACL.getDACLauthors(document);
			return (null == dacl) ? false : (dacl.remove(value)) ? DACL.setDACLauthors(document, dacl) : false;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/*
	 * **************************************************************************
	 * **************************************************************************
	 * 
	 * READER Methods
	 * 
	 * **************************************************************************
	 * **************************************************************************
	 */

	/**
	 * Determines if the passed in name is a member of the DACL_READERS for the document.
	 * 
	 * @param session
	 *            Current Session
	 * @param document
	 *            Document to search for DACL
	 * @param name
	 *            Name to check
	 * 
	 * @return Flag indicating if the name is a member of the DACL_READERS for the document.
	 */
	public static boolean isDACLreader(final Session session, final Document document, final Name name) {
		return DACL.TYPE.READERS.isMember(session, document, name);
	}

	/**
	 * Determines if the current user is a member of the DACL_READERS for the document.
	 * 
	 * @param session
	 *            Current Session
	 * @param document
	 *            Document to search for DACL
	 * 
	 * @return Flag indicating if the current user is a member of the DACL_READERS for the document.
	 */
	public static boolean isDACLreader(final Session session, final Document document) {
		return DACL.TYPE.READERS.isMember(session, document);
	}

	/**
	 * Gets the DACL_READERS values from the source document.
	 * 
	 * @param document
	 *            Document from which to retreive the DACL_READERS values.
	 * 
	 * @return DACL_READERS values. null if not present or empty.
	 */
	public static TreeSet<String> getDACLreaders(final Document document) {
		return DACL.TYPE.READERS.getMembers(document);
	}

	/**
	 * Sets the DACL_READERS item on the source document.
	 * 
	 * The value for DEFAULT_DACL_AUTHORS is included. Does NOT save the document.
	 * 
	 * @param document
	 *            Document upon which to set the DACL_READERS item.
	 * 
	 * @param values
	 *            Person names or roles to be set as the DACL_READERS value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean setDACLreaders(final Document document, final TreeSet<String> values) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (null == values) {
				throw new IllegalArgumentException("Values is null");
			}

			final TreeSet<String> dacl = new TreeSet<String>();
			dacl.add(DACL.DEFAULT_DACL_READERS);
			for (final String entry : values) {
				if (!Strings.isBlankString(entry)) {
					dacl.add(entry);
				}
			}
			final Item item = document.replaceItemValue(DACL.ITEMNAME_DACL_READERS, Strings.getVectorizedStrings(dacl));
			item.setReaders(true);
			return true;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Sets the DACL_READERS item on the source document.
	 * 
	 * The value for DEFAULT_DACL_READERS is included. Does NOT save the document.
	 * 
	 * @param document
	 *            Document upon which to set the DACL_READERS item.
	 * 
	 * @param values
	 *            Person names or roles (convertable to a TreeSet) to be set as the DACL_READERS value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean setDACLreaders(final Document document, final Object values) {
		return DACL.setDACLreaders(document, CollectionUtils.getTreeSetStrings(values));
	}

	/**
	 * Adds a value to the DACL_READERS for the document.
	 * 
	 * Existing DACL_READERS are not removed. The value for DEFAULT_DACL_READERS is included. Does NOT save the document.
	 * 
	 * @param document
	 *            Document upon which to update the DACL_READERS.
	 * 
	 * @param values
	 *            Person names or roles to be added to the DACL_READERS value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean addDACLreaders(final Document document, final TreeSet<String> values) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (null == values) {
				throw new IllegalArgumentException("Values is null");
			}

			TreeSet<String> dacl = DACL.getDACLreaders(document);
			if (null == dacl) {
				dacl = new TreeSet<String>();
			}
			for (final String entry : values) {
				if (!Strings.isBlankString(entry)) {
					dacl.add(entry);
				}
			}

			return DACL.setDACLreaders(document, dacl);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Adds a value to the DACL_READERS for the document.
	 * 
	 * Existing DACL_READERS are not removed. The value for DEFAULT_DACL_READERS is included. Does NOT save the document.
	 * 
	 * @param document
	 *            Document upon which to update the DACL_READERS.
	 * 
	 * @param value
	 *            Person name or role to be added to the DACL_READERS value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean addDACLreader(final Document document, final String value) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}

			final TreeSet<String> values = new TreeSet<String>();
			if (!Strings.isBlankString(value)) {
				values.add(value);
			}

			return DACL.addDACLreaders(document, values);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Removes the DACL_READERS item from the source document.
	 * 
	 * Does NOT save the document.
	 * 
	 * @param document
	 *            Document from which to remove the DACL_READERS item.
	 * 
	 * @return Flag indicating if the item was successfully removed.
	 */
	public static boolean removeDACLreaders(final Document document) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}

			if (document.hasItem(DACL.ITEMNAME_DACL_READERS)) {
				document.removeItem(DACL.ITEMNAME_DACL_READERS);
				return true;
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/**
	 * Removes a value from the DACL_READERS item on the source document.
	 * 
	 * Does NOT save the document.
	 * 
	 * @param document
	 *            Document from which to remove the DACL_READERS item.
	 * 
	 * @param value
	 *            Value to be removed from the DACL_READERS
	 * 
	 * @return Flag indicating if the value was successfully removed from the DACL_READERS
	 */
	public static boolean removeDACLreader(final Document document, final String value) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (Strings.isBlankString(value)) {
				throw new IllegalArgumentException("Value to remove is blank or null");
			}

			final TreeSet<String> dacl = DACL.getDACLreaders(document);
			return (null == dacl) ? false : (dacl.remove(value)) ? DACL.setDACLreaders(document, dacl) : false;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/*
	 * **************************************************************************
	 * **************************************************************************
	 * 
	 * OTHER Methods
	 * 
	 * **************************************************************************
	 * **************************************************************************
	 */

	/**
	 * Gets the Pattern that a string must match in order to be considered as an ACL Role.
	 * 
	 * @return Pattern for a role
	 */
	public static final Pattern getPatternForRoles() {
		return Pattern.compile(DACL.PATTERNTEXT_ROLES, Pattern.CASE_INSENSITIVE);
	}

	/**
	 * Gets the DACL Member values from the source document.
	 * 
	 * Returns the values of all AUTHOR or READER fields found on the document.
	 * 
	 * @param document
	 *            Document from which to retreive the DACL Member values.
	 * 
	 * @param daclType
	 *            DACL.TYPE (AUTHOR or READER) for which to search. If null then returns a combination of all AUTHOR and READER types.
	 * 
	 * @param checkAllItems
	 *            Flag indicating if all items should be checked. If false then only the {@link DACL.TYPE#getItemname()} will be checked.
	 * 
	 * @return DACL Member values. Null if not present or empty.
	 */
	public static TreeSet<String> getMembers(final Document document, final DACL.TYPE daclType, final boolean checkAllItems) {

		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}

			TreeSet<String> members = null;

			if (null == daclType) {
				final TreeSet<String> result = new TreeSet<String>();
				for (DACL.TYPE type : DACL.TYPE.values()) {
					members = DACL.getMembers(document, type, checkAllItems);
					if ((null != members) && (members.size() > 0)) {
						result.addAll(members);
					}
				}
				return ((null == result) || (result.size() < 1)) ? null : result;

			} else {
				if (checkAllItems) {
					// check every item on the document
					switch (daclType) {
					case AUTHORS: {
						final TreeSet<String> result = new TreeSet<String>();
						for (Item item : document.getItems()) {
							if (item.isAuthors()) {
								members = CollectionUtils.getTreeSetStrings(item.getValues(String.class));
								if ((null != members) && members.size() > 0) {
									result.addAll(members);
								}
							}
						}
						return ((null == result) || (result.size() < 1)) ? null : result;
					}

					case READERS: {
						final TreeSet<String> result = new TreeSet<String>();
						for (Item item : document.getItems()) {
							if (item.isAuthors()) {
								members = CollectionUtils.getTreeSetStrings(item.getValues(String.class));
								if ((null != members) && members.size() > 0) {
									result.addAll(members);
								}
							}
						}
						return ((null == result) || (result.size() < 1)) ? null : result;
					}

					} // switch (daclType)

				} else if (document.hasItem(daclType.getItemname())) {
					// get the members from the explicitly named item
					Item item = document.getFirstItem(daclType.getItemname());
					// verify the item is of the correct type
					if ((item.isAuthors() && DACL.TYPE.AUTHORS.equals(daclType))
							|| (item.isReaders() && DACL.TYPE.READERS.equals(daclType))) {
						final TreeSet<String> result = CollectionUtils.getTreeSetStrings(item.getValues(String.class));
						return ((null == result) || (result.size() < 1)) ? null : result;
					}
				}
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	//	/**
	//	 * Determines if the passed in name is a member of the set.
	//	 * 
	//	 * Returns true if the name (in any name format), or any member of roles is a member of the dacl set.
	//	 * 
	//	 * @param session
	//	 *            Current Session
	//	 * 
	//	 * @param dacl
	//	 *            DACL entries to check
	//	 * 
	//	 * @param roles
	//	 *            ACL Roles belonging to the name to check. (No verification is performed)
	//	 * 
	//	 * @param name
	//	 *            Name to check
	//	 * 
	//	 * @return Flag indicating if the name (in any form) is a member of the dacl set, or if any intersection exists between dacl and roles.
	//	 */
	//	public static boolean isDACLmember(final Session session, final TreeSet<String> dacl, final TreeSet<String> roles, final Name name) {
	//		try {
	//			if (null == session) {
	//				throw new IllegalArgumentException("Session is null");
	//			}
	//			if (null == name) {
	//				throw new IllegalArgumentException("Name is null");
	//			}
	//
	//			if ((null != dacl) && (dacl.size() > 0)) {
	//				// do an initial check on the name
	//				if (dacl.contains(name.getCanonical()) || dacl.contains(name.getAbbreviated())) {
	//					return true;
	//				}
	//
	//				// do an initial check for matching roles
	//				if (null != roles) {
	//					for (final String role : roles) {
	//						if (dacl.contains(role)) {
	//							return true;
	//						}
	//					}
	//				}
	//
	//				// do a more thorough check for roles
	//				final Pattern pattern = DACL.getPatternForRoles();
	//				if (null != roles) {
	//					for (final String entry : dacl) {
	//						final Matcher matcher = pattern.matcher(entry);
	//						if (matcher.matches()) {
	//							// entry represents a role
	//							if (roles.contains(entry)) {
	//								return true;
	//							}
	//						}
	//					}
	//
	//					// do a case-insensitive check for roles
	//					for (final String entry : dacl) {
	//						final Matcher matcher = pattern.matcher(entry);
	//						if (matcher.matches()) {
	//							// entry represents a role
	//							for (final String role : roles) {
	//								if (role.equalsIgnoreCase(entry)) {
	//									return true;
	//								}
	//							}
	//						}
	//					}
	//				}
	//
	//				return Names.isNamesListMember(session, dacl, name);
	//			}
	//
	//		} catch (final Exception e) {
	//			DominoUtils.handleException(e);
	//		}
	//
	//		return false;
	//	}
	//
	//	/**
	//	 * Determines if the passed in name is a member of the specified DACL for the document.
	//	 * 
	//	 * @param session
	//	 *            Current Session
	//	 * @param document
	//	 *            Document to search for DACL
	//	 * @param name
	//	 *            Name to check
	//	 * @param dacltype
	//	 *            DACL.TYPE to check
	//	 * 
	//	 * @return Flag indicating if the name is a member of the specified DACL for the document.
	//	 */
	//	public static boolean isDACLmember(final Session session, final Document document, final Name name, final DACL.TYPE dacltype) {
	//		try {
	//			if (null == session) {
	//				throw new IllegalArgumentException("Session is null");
	//			}
	//			if (null == name) {
	//				throw new IllegalArgumentException("Name is null");
	//			}
	//			if (null == document) {
	//				throw new IllegalArgumentException("Document is null");
	//			}
	//			if (null == dacltype) {
	//				throw new IllegalArgumentException("DACLtype is null");
	//			}
	//
	//			return dacltype.isMember(session, document, name);
	//
	//		} catch (final Exception e) {
	//			DominoUtils.handleException(e);
	//		}
	//
	//		return false;
	//	}
	//
	//	/**
	//	 * Determines if the current user is a member of the specified DACL for the document.
	//	 * 
	//	 * @param session
	//	 *            Current Session
	//	 * @param document
	//	 *            Document to search for DACL
	//	 * @param dacltype
	//	 *            DACL.TYPE to check
	//	 * 
	//	 * @return Flag indicating if the current user is a member of the specified DACL for the document.
	//	 */
	//	public static boolean isDACLmember(final Session session, final Document document, final DACL.TYPE dacltype) {
	//		return DACL.isDACLmember(session, document, Names.createName(session, ""), dacltype);
	//	}
	//
	/**
	 * Sets the DACL.TYPE item on the source document. Does NOT save the document.
	 * 
	 * Conditionally overrides the item name.
	 * 
	 * Conditionally adds the Default role {@link DACL.TYPE#getRole()}
	 * 
	 * @param document
	 *            Document upon which to set the DACL_AUTHORS item.
	 * 
	 * @param values
	 *            Person names or roles to be set as the DACL_AUTHORS value. If null or empty AND doNotAddRole is true, then the item will
	 *            be REMOVED from the document.
	 * 
	 * @param itemname
	 *            Name of DACL item to be added.
	 * 
	 * @param daclType
	 *            DACL.TYPE of item to be added.
	 * 
	 * @param doNotAddRole
	 *            Flag indicating if the {@link DACL.TYPE#getRole()} should be explicitly added. If True, the role will not be added NOTE:
	 *            if the Role is a member of values it will NOT be excluded, regardless of doNotAddRole
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean setMembers(final Document document, final TreeSet<String> values, final String itemname,
			final DACL.TYPE daclType, final boolean doNotAddRole) {

		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (null == daclType) {
				throw new IllegalArgumentException("DACL.TYPE is null");
			}
			if (Strings.isBlankString(itemname)) {
				throw new IllegalArgumentException("Item Name is blank or null");
			}

			final TreeSet<String> dacl = new TreeSet<String>();
			if (!doNotAddRole) {
				dacl.add(daclType.getRole());
			}

			if (null != values) {
				for (final String entry : values) {
					if (!Strings.isBlankString(entry)) {
						dacl.add(entry);
					}
				}
			}

			if (dacl.isEmpty()) {
				if (document.hasItem(itemname)) {
					document.removeItem(itemname);
				}

			} else {

				final Item item = document.replaceItemValue(itemname, Strings.getVectorizedStrings(dacl));
				switch (daclType) {
				case AUTHORS: {
					item.setAuthors(true);
					break;
				}
				case READERS: {
					item.setReaders(true);
					break;
				}
				} // switch(this)

				return true;
			}
		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}
	//	/**
	//	 * Adds a Role to the appropriate DACL on a document if it does not already exist.
	//	 * 
	//	 * @param document
	//	 *            Document to which the Role shall be added.
	//	 * @param role
	//	 *            Role to be added to the document.
	//	 * @param dacltype
	//	 *            DACL.TYPE that should be updated.
	//	 * @return Flag indicating if the Role was added to the item.
	//	 */
	//	public static boolean addRole(final Document document, final String role, final DACL.TYPE dacltype) {
	//		try {
	//			if (null == document) {
	//				throw new IllegalArgumentException("Document is null");
	//			}
	//			if (Strings.isBlankString(role)) {
	//				throw new IllegalArgumentException("Role is null or blank");
	//			}
	//			if (null == dacltype) {
	//				throw new IllegalArgumentException("DACLtype is null");
	//			}
	//
	//			final String newrole = "[" + role.replace("[", "").replace("]", "").trim() + "]";
	//			if ("[]".equals(newrole)) {
	//				throw new IllegalArgumentException("Role is null or blank");
	//			}
	//
	//			return (DACL.TYPE.AUTHORS.equals(dacltype)) ? DACL.addDACLauthor(document, newrole)
	//					: (DACL.TYPE.READERS.equals(dacltype)) ? DACL.addDACLreader(document, newrole) : false;
	//
	//		} catch (final Exception e) {
	//			DominoUtils.handleException(e);
	//		}
	//
	//		return false;
	//	}
	//
	//	/**
	//	 * Removes a Role from the appropriate DACL on a document.
	//	 * 
	//	 * NOTE: This will NOT remove the DEFAULT_DACL_READERS or DEFAULT_DACL_AUTHORS roles.
	//	 * 
	//	 * @param document
	//	 *            Document from which the Role shall be removed.
	//	 * @param role
	//	 *            Role to be removed from the document.
	//	 * @param dacltype
	//	 *            DACL.TYPE that should be updated.
	//	 * @return Flag indicating if the Role was removed from the item.
	//	 */
	//	public static boolean removeRole(final Document document, final String role, final DACL.TYPE dacltype) {
	//		try {
	//			if (null == document) {
	//				throw new IllegalArgumentException("Document is null");
	//			}
	//			if (Strings.isBlankString(role)) {
	//				throw new IllegalArgumentException("Role is null or blank");
	//			}
	//			if (null == dacltype) {
	//				throw new IllegalArgumentException("DACLtype is null");
	//			}
	//
	//			final String newrole = "[" + role.replace("[", "").replace("]", "").trim() + "]";
	//			if ("[]".equals(newrole)) {
	//				throw new IllegalArgumentException("Role is null or blank");
	//			}
	//			if (DACL.DEFAULT_DACL_AUTHORS.equalsIgnoreCase(newrole)) {
	//				throw new IllegalArgumentException(DACL.DEFAULT_DACL_AUTHORS + " may not be removed");
	//			}
	//			if (DACL.DEFAULT_DACL_READERS.equalsIgnoreCase(newrole)) {
	//				throw new IllegalArgumentException(DACL.DEFAULT_DACL_READERS + " may not be removed");
	//			}
	//
	//			return (TYPE.AUTHORS.equals(dacltype)) ? DACL.removeDACLauthor(document, newrole) : (TYPE.READERS.equals(dacltype)) ? DACL
	//					.removeDACLreader(document, newrole) : false;
	//
	//		} catch (final Exception e) {
	//			DominoUtils.handleException(e);
	//		}
	//
	//		return false;
	//	}
	//
	//	/**
	//	 * Removes all ACL Roles from a set of strings.
	//	 * 
	//	 * Removes all bracketed (beginning with "[" and ending with "]") entries from an input set. Optionally comares all entries to all roles
	//	 * from the passed in database and removes any matching entries.
	//	 * 
	//	 * 
	//	 * @param source
	//	 *            Set of strings to process
	//	 * 
	//	 * @param database
	//	 *            Optional database from which to check ACL roles
	//	 * 
	//	 * @return source with all roles removed.
	//	 */
	//	public static TreeSet<String> removeRoles(final TreeSet<String> source, final Database database) {
	//		ACL acl = null;
	//		try {
	//			if (null == source) {
	//				throw new IllegalArgumentException("Source TreeSet is null");
	//			}
	//
	//			final TreeSet<String> result = new TreeSet<String>();
	//			final Pattern pattern = DACL.getPatternForRoles();
	//
	//			for (final String entry : source) {
	//				final Matcher matcher = pattern.matcher(entry);
	//				if (!matcher.matches()) {
	//					// entry is not a role
	//					result.add(entry);
	//				}
	//			}
	//
	//			if (null != database) {
	//
	//				acl = database.getACL();
	//				final TreeSet<String> roles = CollectionUtils.getTreeSetStrings(acl.getRoles());
	//				if (null != roles) {
	//					final TreeSet<String> remove = new TreeSet<String>();
	//					for (final String role : roles) {
	//						for (final String string : result) {
	//							if (role.equalsIgnoreCase(string)) {
	//								remove.add(string);
	//							}
	//						}
	//					}
	//
	//					if (remove.size() > 0) {
	//						result.removeAll(remove);
	//					}
	//				}
	//			}
	//
	//			return result;
	//
	//		} catch (final Exception e) {
	//			DominoUtils.handleException(e);
	//		} finally {
	//			DominoUtils.incinerate(acl);
	//		}
	//
	//		return null;
	//	}
	//
	//	/**
	//	 * Replaces a Role on the appropriate DACL on a document.
	//	 * 
	//	 * NOTE: This will NOT remove the DEFAULT_DACL_READERS or DEFAULT_DACL_AUTHORS roles.
	//	 * 
	//	 * @param document
	//	 *            Document from which the Role shall be removed.
	//	 * @param oldrole
	//	 *            Role to be removed from the document.
	//	 * @param newrole
	//	 *            Role to be added from the document.
	//	 * @param dacltype
	//	 *            DACL.TYPE that should be updated.
	//	 * @return Flag indicating if the Role was replaced on the item.
	//	 */
	//	public static boolean replaceRole(final Document document, final String oldrole, final String newrole, final DACL.TYPE dacltype) {
	//		try {
	//			if (null == document) {
	//				throw new IllegalArgumentException("Document is null");
	//			}
	//			if (Strings.isBlankString(newrole)) {
	//				throw new IllegalArgumentException("New Role is null or blank");
	//			}
	//			if (null == dacltype) {
	//				throw new IllegalArgumentException("DACLtype is null");
	//			}
	//
	//			if (!Strings.isBlankString(oldrole)) {
	//				DACL.removeRole(document, oldrole, dacltype);
	//			}
	//
	//			return DACL.addRole(document, newrole, dacltype);
	//
	//		} catch (final Exception e) {
	//			DominoUtils.handleException(e);
	//		}
	//
	//		return false;
	//	}
	//
	//	/**
	//	 * Determines if a specified role is contained within a set of roles.
	//	 * 
	//	 * Performs a case-insensitive comparison of all entries to check for match. Strips any leading "[" or trailing "]" characters prior to
	//	 * comparison.
	//	 * 
	//	 * @param roles
	//	 *            Roles to check for match
	//	 * 
	//	 * @param role
	//	 *            Role to check for existence in Roles
	//	 * 
	//	 * @return Flag indicating if the specified Role is contained in the set of Roles.
	//	 */
	//	public static boolean containsRole(final TreeSet<String> roles, final String role) {
	//		try {
	//			if (null == roles) {
	//				throw new IllegalArgumentException("Roles is null");
	//			}
	//			if (Strings.isBlankString(role)) {
	//				throw new IllegalArgumentException("Role is null or blank");
	//			}
	//
	//			if (roles.size() > 0) {
	//				if (roles.contains(role)) {
	//					return true;
	//				}
	//
	//				final Pattern pattern = DACL.getPatternForRoles();
	//				final TreeSet<String> checkroles = new TreeSet<String>();
	//				for (final String string : roles) {
	//					final Matcher matcher = pattern.matcher(string);
	//					if (matcher.matches()) {
	//						checkroles.add(string.substring(string.indexOf('[') + 1, string.indexOf(']')).toLowerCase());
	//					} else {
	//						checkroles.add(string.toLowerCase());
	//					}
	//				}
	//
	//				final Matcher matcher = pattern.matcher(role);
	//				final String checkrole = (matcher.matches()) ? role.substring(role.indexOf('[') + 1, role.indexOf(']')).toLowerCase()
	//						: role.toLowerCase();
	//
	//				return checkroles.contains(checkrole);
	//
	//			} // if (roles.size() > 0)
	//
	//		} catch (final Exception e) {
	//			DominoUtils.handleException(e);
	//		}
	//
	//		return false;
	//	}

}
