/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.utils;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openntf.domino.ACL;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.Name;
import org.openntf.domino.Session;

/**
 * Document Access Control List Utilities
 * 
 * @author Devin S. Olson (dolson@czarnowski.com)
 * 
 */
@SuppressWarnings("nls")
public enum DACL {
	AUTHORS(DACL.ITEMNAME_DACL_AUTHORS, DACL.DEFAULT_DACL_AUTHORS), READERS(DACL.ITEMNAME_DACL_READERS, DACL.DEFAULT_DACL_READERS);

	private final String _itemname;
	private final String _role;

	public String getItemname() {
		return this._itemname;
	}

	public String getRole() {
		return this._role;
	}

	private DACL(final String itemname, final String role) {
		this._itemname = itemname;
		this._role = role;
	}

	@Override
	public String toString() {
		return DACL.class.getName() + ": " + this.name() + "{\"" + this.getItemname() + "\", \"" + this.getRole() + "\"}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	/**
	 * Gets the DACL Member values from the source document.
	 * 
	 * Retreives values ONLY from the item defined by {@link DACL#getItemname()}.
	 * 
	 * @param document
	 *            Document from which to retreive the Member values.
	 * 
	 * @return DACL Member values. Null if not present or empty.
	 */
	public TreeSet<String> values(final Document document) {
		return DACL.values(document, this, false);
	}

	/**
	 * Gets the DACL Member values from the source document.
	 * 
	 * Conditionally retreives values from ALL DACL items or ONLY from the item defined by {@link DACL#getItemname()}.
	 * 
	 * @param document
	 *            Document from which to retreive the Member values.
	 * 
	 * @param checkAllItems
	 *            Flag indicating if all items should be checked. If false then only the {@link DACL#getItemname()} will be checked.
	 * 
	 * @return DACL Member values. Null if not present or empty.
	 */
	public TreeSet<String> values(final Document document, final boolean checkAllItems) {
		return DACL.values(document, this, checkAllItems);
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
	 * @return Flag indicating if the name (in any form) is a member of the dacl set, or if any intersection exists between dacl and roles.
	 */
	public boolean contains(final Session session, final TreeSet<String> dacl, final TreeSet<String> roles, final Name name) {
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
	 * Conditionally checks values from ALL DACL items or ONLY from the item defined by {@link DACL#getItemname()}.
	 * 
	 * @param session
	 *            Current Session
	 * @param document
	 *            Document to search for DACL
	 * @param name
	 *            Name to check
	 * 
	 * @param checkAllItems
	 *            Flag indicating if all items should be checked. If false then only the {@link DACL#getItemname()} will be checked.
	 * 
	 * @return Flag indicating if the name is a member of the specified DACL for the document.
	 */
	public boolean contains(final Session session, final Document document, final Name name, final boolean checkAllItems) {
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
			final TreeSet<String> dacl = this.values(document, checkAllItems);

			return this.contains(session, dacl, roles, name);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Determines if the passed in name is a member of the specified DACL for the document.
	 * 
	 * Checks values ONLY from the item defined by {@link DACL#getItemname()}.
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
	public boolean contains(final Session session, final Document document, final Name name) {
		return this.contains(session, document, name, false);
	}

	/**
	 * Determines if the current user is a member of the specified DACL for the document.
	 * 
	 * Conditionally checks values from ALL DACL items or ONLY from the item defined by {@link DACL#getItemname()}.
	 * 
	 * @param session
	 *            Current Session
	 * @param document
	 *            Document to search for DACL
	 * 
	 * @param checkAllItems
	 *            Flag indicating if all items should be checked. If false then only the {@link DACL#getItemname()} will be checked.
	 * 
	 * @return Flag indicating if the name is a member of the specified DACL for the document.
	 */
	public boolean contains(final Session session, final Document document, final boolean checkAllItems) {
		return this.contains(session, document, session.createName(""), checkAllItems);
	}

	/**
	 * Determines if the current user is a member of the specified DACL for the document.
	 * 
	 * Checks values ONLY from the item defined by {@link DACL#getItemname()}.
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
	public boolean contains(final Session session, final Document document) {
		return this.contains(session, document, false);
	}

	/**
	 * Sets the DACL item on the source document. Does NOT save the document.
	 * 
	 * Conditionally overrides the item name.
	 * 
	 * Conditionally adds the Default role {@link DACL#getRole()}
	 * 
	 * @param document
	 *            Document upon which to set the DACL item.
	 * 
	 * @param members
	 *            Names or roles to be set as the DACL value.
	 * 
	 * @param override
	 *            Name of DACL item to be added. Overrides {@link DACL#getItemname()}. If null or blank then {@link DACL#getItemname()} will
	 *            be used.
	 * 
	 * @param doNotAddRole
	 *            Flag indicating if the {@link DACL#getRole()} should be explicitly added. If True, the role will not be added NOTE: if the
	 *            Role is a member of members it will NOT be excluded, regardless of doNotAddRole
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public boolean set(final Document document, final TreeSet<String> members, final String override, final boolean doNotAddRole) {
		String itemname = (Strings.isBlankString(override)) ? this.getItemname() : override;
		return DACL.set(document, members, itemname, this, doNotAddRole);
	}

	/**
	 * Sets the DACL item on the source document. Does NOT save the document.
	 * 
	 * Conditionally adds the Default role {@link DACL#getRole()}
	 * 
	 * @param document
	 *            Document upon which to set the DACL item.
	 * 
	 * @param members
	 *            Names or roles to be set as the DACL value.
	 * 
	 * @param doNotAddRole
	 *            Flag indicating if the {@link DACL#getRole()} should be explicitly added. If True, the role will not be added NOTE: if the
	 *            Role is a member of members it will NOT be excluded, regardless of doNotAddRole
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public boolean set(final Document document, final TreeSet<String> members, final boolean doNotAddRole) {
		return this.set(document, members, null, doNotAddRole);
	}

	/**
	 * Sets the DACL item on the source document. Does NOT save the document.
	 * 
	 * Adds the Default role {@link DACL#getRole()}
	 * 
	 * @param document
	 *            Document upon which to set the DACL item.
	 * 
	 * @param members
	 *            Names or roles to be set as the DACL value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public boolean set(final Document document, final TreeSet<String> members) {
		return this.set(document, members, null, false);
	}

	/**
	 * Sets the DACL item on the source document. Does NOT save the document.
	 * 
	 * Adds the Default role {@link DACL#getRole()}
	 * 
	 * @param document
	 *            Document upon which to set the DACL item.
	 * 
	 * @param values
	 *            Names or roles (String or object convertable to a TreeSet of Strings) to be set as the DACL value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public boolean set(final Document document, final Object values) {
		return this.set(document, CollectionUtils.getTreeSetStrings(values));
	}

	/**
	 * Adds a members to the DACL for the document.
	 * 
	 * Existing DACL members are not removed. Adds the Default role {@link DACL#getRole()}. Does NOT save the document.
	 * 
	 * @param document
	 *            Document upon which to update the .
	 * 
	 * @param members
	 *            Names or roles to be added to the DACL value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public boolean add(final Document document, final TreeSet<String> members) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (null == members) {
				throw new IllegalArgumentException("Members is null");
			}

			TreeSet<String> dacl = this.values(document);
			if (null == dacl) {
				dacl = new TreeSet<String>();
			}

			dacl.addAll(members);
			return this.set(document, dacl);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Adds a members to the DACL for the document.
	 * 
	 * Existing DACL members are not removed. Adds the Default role {@link DACL#getRole()}. Does NOT save the document.
	 * 
	 * @param document
	 *            Document upon which to update the .
	 * 
	 * @param values
	 *            Names or roles (String or object convertable to a TreeSet of Strings) to be added.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public boolean add(final Document document, final Object values) {
		return this.add(document, CollectionUtils.getTreeSetStrings(values));
	}

	/**
	 * Adds a Role to the appropriate DACL item on a document.
	 * 
	 * @param document
	 *            Document to which the Role shall be added.
	 * 
	 * @param string
	 *            Role to be added to the document.
	 * 
	 * @return Flag indicating if the Role was added to the item.
	 */
	public boolean addRole(final Document document, final String string) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}

			final String role = Names.formatAsRole(string);
			if (Strings.isBlankString(role)) {
				throw new IllegalArgumentException("Role is null or blank: " + string);
			}

			return this.add(document, role);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Removes the DACL item from the source document.
	 * 
	 * Conditionally removes ALL DACL items or ONLY the item defined by {@link DACL#getItemname()}.
	 * 
	 * Does NOT save the document.
	 * 
	 * @param document
	 *            Document from which to remove the DACL item(s).
	 * 
	 * @param removeAll
	 *            Flag indicating if ALL DACL items found on the document should be removed.
	 * 
	 * @return Flag indicating if the item was successfully removed.
	 */
	public boolean remove(final Document document, final boolean removeAll) {
		return DACL.remove(document, this, removeAll);
	}

	/**
	 * Removes the DACL item from the source document.
	 * 
	 * Removed ONLY the item defined by {@link DACL#getItemname()}.
	 * 
	 * Does NOT save the document.
	 * 
	 * @param document
	 *            Document from which to remove the DACL item(s).
	 * 
	 * @return Flag indicating if the item was successfully removed.
	 */
	public boolean remove(final Document document) {
		return DACL.remove(document, this, false);
	}

	/**
	 * Removes an explicit to the DACL for the document.
	 * 
	 * Does NOT save the document.
	 * 
	 * @param document
	 *            Document upon which to update the .
	 * 
	 * @param member
	 *            Names or roles to be removed from the DACL value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public boolean remove(final Document document, final String member) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (Strings.isBlankString(member)) {
				throw new IllegalArgumentException("Members is null");
			}

			TreeSet<String> dacl = this.values(document);
			if (null == dacl) {
				dacl = new TreeSet<String>();
			}

			return (dacl.remove(member)) ? this.set(document, dacl) : false;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Removes members to the DACL for the document.
	 * 
	 * Does NOT save the document.
	 * 
	 * @param document
	 *            Document upon which to update the .
	 * 
	 * @param members
	 *            Names or roles to be removed from the DACL value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public boolean remove(final Document document, final TreeSet<String> members) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (null == members) {
				throw new IllegalArgumentException("Members is null");
			}

			TreeSet<String> dacl = this.values(document);
			if (null == dacl) {
				dacl = new TreeSet<String>();
			}

			return (dacl.removeAll(members)) ? this.set(document, dacl) : false;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Removes a Role from the appropriate DACL on a document.
	 * 
	 * NOTE: This will NOT remove the DEFAULT_DACL_READERS or DEFAULT_DACL_AUTHORS roles.
	 * 
	 * @param document
	 *            Document from which the Role shall be removed.
	 * @param string
	 *            Role to be removed from the document.
	 * @return Flag indicating if the Role was removed from the item.
	 */
	public boolean removeRole(final Document document, final String string) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}

			final String role = Names.formatAsRole(string);
			if (Strings.isBlankString(role)) {
				throw new IllegalArgumentException("Role is null or blank: " + string);
			}

			return this.remove(document, role);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Replaces a Role on the appropriate DACL on a document.
	 * 
	 * NOTE: This will NOT remove the DEFAULT_DACL_READERS or DEFAULT_DACL_AUTHORS roles.
	 * 
	 * @param document
	 *            Document from which the Role shall be removed.
	 * @param oldrole
	 *            Role to be removed from the document.
	 * @param newrole
	 *            Role to be added from the document.
	 * @return Flag indicating if the Role was replaced on the item.
	 */
	public boolean replaceRole(final Document document, final String oldrole, final String newrole) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (Strings.isBlankString(newrole)) {
				throw new IllegalArgumentException("New Role is null or blank");
			}

			if (!Strings.isBlankString(oldrole)) {
				this.removeRole(document, oldrole);
			}

			return this.addRole(document, newrole);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	};

	/*
	 * **************************************************************************
	 * **************************************************************************
	 * 
	 * STATIC Properties and Methods
	 * 
	 * **************************************************************************
	 * **************************************************************************
	 */
	public static final String ITEMNAME_DACL_READERS = "$DACL_Readers";
	public static final String ITEMNAME_DACL_AUTHORS = "$DACL_Authors";
	public static final String DEFAULT_DACL_READERS = "[ReadAll]";
	public static final String DEFAULT_DACL_AUTHORS = "[EditAll]";

	// starts with "[", has any number of letters, spaces, hyphens, or underscores, ends with "]"
	public static final String PATTERNTEXT_ROLES = "(?i)^\\[[a-z0-9 -_]+\\]$";

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
	 *            DACL (AUTHOR or READER) for which to search. If null then returns a combination of all AUTHOR and READER types.
	 * 
	 * @param checkAllItems
	 *            Flag indicating if all items should be checked. If false then only the {@link DACL#getItemname()} will be checked.
	 * 
	 * @return DACL Member values. Null if not present or empty.
	 */
	public static TreeSet<String> values(final Document document, final DACL daclType, final boolean checkAllItems) {

		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}

			TreeSet<String> members = null;

			if (null == daclType) {
				final TreeSet<String> result = new TreeSet<String>();
				for (DACL type : DACL.values()) {
					members = DACL.values(document, type, checkAllItems);
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
					if ((item.isAuthors() && DACL.AUTHORS.equals(daclType)) || (item.isReaders() && DACL.READERS.equals(daclType))) {
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

	/**
	 * Sets the DACL item on the source document. Does NOT save the document.
	 * 
	 * Conditionally overrides the item name.
	 * 
	 * Conditionally adds the Default role {@link DACL#getRole()}
	 * 
	 * @param document
	 *            Document upon which to set the DACL_AUTHORS item.
	 * 
	 * @param values
	 *            Names or roles to be set as the DACL_AUTHORS value. If null or empty AND doNotAddRole is true, then the item will be
	 *            REMOVED from the document.
	 * 
	 * @param itemname
	 *            Name of DACL item to be added.
	 * 
	 * @param daclType
	 *            DACL of item to be added.
	 * 
	 * @param doNotAddRole
	 *            Flag indicating if the {@link DACL#getRole()} should be explicitly added. If True, the role will not be added NOTE: if the
	 *            Role is a member of values it will NOT be excluded, regardless of doNotAddRole
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean set(final Document document, final TreeSet<String> values, final String itemname, final DACL daclType,
			final boolean doNotAddRole) {

		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (null == daclType) {
				throw new IllegalArgumentException("DACL is null");
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

	/**
	 * Removes the DACL item from the source document.
	 * 
	 * Conditionally removes ALL DACL items or ONLY the item defined by {@link DACL#getItemname()}.
	 * 
	 * Does NOT save the document.
	 * 
	 * @param document
	 *            Document from which to remove the DACL item(s).
	 * 
	 * @param daclType
	 *            DACL (AUTHOR or READER) for which to search. If null then removes both AUTHOR and READER types.
	 * 
	 * @param removeAll
	 *            Flag indicating if ALL DACL items found on the document should be removed.
	 * 
	 * @return Flag indicating if the item was successfully removed.
	 */
	public static boolean remove(final Document document, final DACL daclType, final boolean removeAll) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}

			if (null == daclType) {
				boolean resultAuthors = DACL.remove(document, DACL.AUTHORS, removeAll);
				boolean resultReaders = DACL.remove(document, DACL.READERS, removeAll);
				return (resultAuthors || resultReaders);
			}

			if (removeAll) {
				TreeSet<String> itemnames = new TreeSet<String>();
				for (Item item : document.getItems()) {
					if ((item.isAuthors() && DACL.AUTHORS.equals(daclType)) || (item.isReaders() && DACL.READERS.equals(daclType))) {
						itemnames.add(item.getName());
					}
				}

				if (!itemnames.isEmpty()) {
					for (String s : itemnames) {
						document.removeItem(s);
					}

					return true;
				}

			} else {
				if (document.hasItem(daclType.getItemname())) {
					document.removeItem(daclType.getItemname());
					return true;
				}
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/**
	 * Removes all ACL Roles from a set of strings.
	 * 
	 * Removes all bracketed (beginning with "[" and ending with "]") entries from an input set. Optionally comares all entries to all roles
	 * from the passed in database and removes any matching entries.
	 * 
	 * @param source
	 *            Set of strings to process
	 * 
	 * @param database
	 *            Optional database from which to check ACL roles
	 * 
	 * @return source with all roles removed.
	 */
	public static TreeSet<String> removeRoles(final TreeSet<String> source, final Database database) {
		try {
			if (null == source) {
				throw new IllegalArgumentException("Source TreeSet is null");
			}

			final TreeSet<String> result = new TreeSet<String>();
			final Pattern pattern = DACL.getPatternForRoles();

			for (final String entry : source) {
				final Matcher matcher = pattern.matcher(entry);
				if (!matcher.matches()) {
					// entry is not a role
					result.add(entry);
				}
			}

			if (null != database) {
				ACL acl = database.getACL();
				final TreeSet<String> roles = CollectionUtils.getTreeSetStrings(acl.getRoles());
				if (null != roles) {
					final TreeSet<String> remove = new TreeSet<String>();
					for (final String role : roles) {
						for (final String string : result) {
							if (role.equalsIgnoreCase(string)) {
								remove.add(string);
							}
						}
					}

					if (remove.size() > 0) {
						result.removeAll(remove);
					}
				}
			}

			return result;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return null;
	}

	/**
	 * Determines if a specified role is contained within a set of roles.
	 * 
	 * Performs a case-insensitive comparison of all entries to check for match. Strips any leading "[" or trailing "]" characters prior to
	 * comparison.
	 * 
	 * @param roles
	 *            Roles to check for match
	 * 
	 * @param role
	 *            Role to check for existence in Roles
	 * 
	 * @return Flag indicating if the specified Role is contained in the set of Roles.
	 */
	public static boolean containsRole(final TreeSet<String> roles, final String role) {
		try {
			if (null == roles) {
				throw new IllegalArgumentException("Roles is null");
			}
			if (Strings.isBlankString(role)) {
				throw new IllegalArgumentException("Role is null or blank");
			}

			if (roles.size() > 0) {
				if (roles.contains(role)) {
					return true;
				}

				final Pattern pattern = DACL.getPatternForRoles();
				final TreeSet<String> checkroles = new TreeSet<String>();
				for (final String string : roles) {
					final Matcher matcher = pattern.matcher(string);
					if (matcher.matches()) {
						checkroles.add(string.substring(string.indexOf('[') + 1, string.indexOf(']')).toLowerCase());
					} else {
						checkroles.add(string.toLowerCase());
					}
				}

				final Matcher matcher = pattern.matcher(role);
				final String checkrole = (matcher.matches()) ? role.substring(role.indexOf('[') + 1, role.indexOf(']')).toLowerCase()
						: role.toLowerCase();

				return checkroles.contains(checkrole);

			} // if (roles.size() > 0)

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/*
	 * **************************************************************************
	 * **************************************************************************
	 * 
	 * AUTHORS wrapper methods
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
	public static boolean isAuthor(final Session session, final Document document, final Name name) {
		return DACL.AUTHORS.contains(session, document, name);
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
	public static boolean isAuthor(final Session session, final Document document) {
		return DACL.AUTHORS.contains(session, document);
	}

	/**
	 * Gets the DACL_AUTHORS values from the source document.
	 * 
	 * @param document
	 *            Document from which to retreive the DACL_AUTHORS values.
	 * 
	 * @return DACL_AUTHORS values. null if not present or empty.
	 */
	public static TreeSet<String> getAuthors(final Document document) {
		return DACL.AUTHORS.values(document);
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
	 *            Names or roles (convertable to a TreeSet) to be set as the DACL_AUTHORS value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean setAuthors(final Document document, final Object values) {
		return DACL.AUTHORS.set(document, values);
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
	 *            Names or roles (String or object convertable to a TreeSet of Strings) to be added.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean addAuthors(final Document document, final Object values) {
		return DACL.AUTHORS.add(document, values);
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
	public static boolean removeAuthors(final Document document) {
		return DACL.AUTHORS.remove(document);
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
	public static boolean removeAuthor(final Document document, final String value) {
		return DACL.AUTHORS.remove(document, value);
	}

	/*
	 * **************************************************************************
	 * **************************************************************************
	 * 
	 * READERS wrapper methods
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
	public static boolean isReader(final Session session, final Document document, final Name name) {
		return DACL.READERS.contains(session, document, name);
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
	public static boolean isReader(final Session session, final Document document) {
		return DACL.READERS.contains(session, document);
	}

	/**
	 * Gets the DACL_READERS values from the source document.
	 * 
	 * @param document
	 *            Document from which to retreive the DACL_READERS values.
	 * 
	 * @return DACL_READERS values. null if not present or empty.
	 */
	public static TreeSet<String> getReaders(final Document document) {
		return DACL.READERS.values(document);
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
	 *            Names or roles (convertable to a TreeSet) to be set as the DACL_READERS value.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean setReaders(final Document document, final Object values) {
		return DACL.READERS.set(document, values);
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
	 *            Names or roles (String or object convertable to a TreeSet of Strings) to be added.
	 * 
	 * @return Flag indicating the success / failure of the operation.
	 */
	public static boolean addReaders(final Document document, final Object values) {
		return DACL.READERS.add(document, values);
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
	public static boolean removeReaders(final Document document) {
		return DACL.READERS.remove(document);
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
	public static boolean removeReader(final Document document, final String value) {
		return DACL.READERS.remove(document, value);
	}

}
