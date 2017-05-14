/*
 * Copyright 2013
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
 */
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * Represents the Notes directories on a specific server or local computer, each of which is associated with one or more directory
 * navigators to allow directory lookups.
 *
 * <h3>Creation</h3>
 * <p>
 * You create a new <code>Directory</code> object using the name of the server you want to access. You can use the
 * {@link Session#getDirectory(String)} method.
 * </p>
 * <h3>Usage</h3>
 * <p>
 * Use the <code>Directory</code> class to lookup people, groups and other data in Domino Directories. If you have multiple directories,
 * this class searches them all at once. You can search for multiple names at once and return multiple information (like internet address,
 * shortname, mail file).
 * </p>
 * <p>
 * Use the {@link #lookupNames(String, Vector, Vector, boolean)} method to lookup the information you need. You can specify multiple names
 * to lookup and multiple items to retrieve.
 * </p>
 * <p>
 * For every name you search for, you will receive zero or more matches. For every match you will receive one or more item values you asked
 * for. The returned {@link DirectoryNavigator} lets you navigate through all the returned names, matches and item values (see example
 * below).
 * </p>
 * <ul>
 * <li>Name 1
 * <ul>
 * <li>Match 1
 * <ul>
 * <li>Item value 1</li>
 * <li>Item value 2</li>
 * <li>Item value 3</li>
 * </ul>
 * </li>
 * <li>Match 2
 * <ul>
 * <li>Item value 1</li>
 * <li>Item value 2</li>
 * <li>Item value 3</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 *
 * <li>Name 2
 * <ul>
 * <li>Match 1
 * <ul>
 * <li>Item value 1</li>
 * <li>Item value 2</li>
 * <li>Item value 3</li>
 * </ul>
 * </li>
 * <li>Match 2
 * <ul>
 * <li>Item value 1</li>
 * <li>Item value 2</li>
 * <li>Item value 3</li>
 * </ul>
 * </li>
 * <li>Match 3
 * <ul>
 * <li>Item value 1</li>
 * <li>Item value 2</li>
 * <li>Item value 3</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * </ul>
 * </p>
 * <h3>Example</h3>
 *
 * <pre>
 * </pre>
 *
 */
public interface Directory
		extends Base<lotus.domino.Directory>, lotus.domino.Directory, org.openntf.domino.ext.Directory, SessionDescendant {

	public static class Schema extends FactorySchema<Directory, lotus.domino.Directory, Session> {
		@Override
		public Class<Directory> typeClass() {
			return Directory.class;
		}

		@Override
		public Class<lotus.domino.Directory> delegateClass() {
			return lotus.domino.Directory.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Creates a new directory navigator.
	 * <p>
	 * Calls to Lookup against a Directory return a resident {@link DirectoryNavigator}, with successive Lookups resetting the cache and
	 * associated navigator. The <code>createNavigator</code> method is used to create additional <code>DirectoryNavigators</code>, allowing
	 * multiple simultaneous information retrievals.
	 * </p>
	 *
	 * @return The newly created directory navigator.
	 */
	@Override
	public DirectoryNavigator createNavigator();

	/**
	 * Frees memory in the current lookup buffer. Resets all Navigators.
	 * <p>
	 * This method is used for memory management, since the namelookup buffer may be large.
	 * </p>
	 *
	 */
	@Override
	public void freeLookupBuffer();

	/**
	 * Array of summary data items which have been looked up and cached.
	 * <p>
	 * This property is set by the variant <em>Items</em> in methods LookupNames and LookupAllNames. If a <code>Directory</code> has been
	 * created but no lookups have been performed against it, this property is null.
	 * </p>
	 *
	 * @return Array of summary data items which have been looked up and cached.
	 */
	@Override
	public Vector<String> getAvailableItems();

	/**
	 * Array of names which have been looked up and cached.
	 * <p>
	 * If the most recent lookup was LookupAllNames, this property will contain all names in the specified view. If the most recent lookup
	 * was LookupNames, this property will contain the variant <em>Names</em> specified in that lookup. If a Directory has been created but
	 * no lookups have been performed against it, this property is null.
	 * </p>
	 *
	 * @return Array of names which have been looked up and cached.
	 *
	 */
	@Override
	public Vector<String> getAvailableNames();

	/**
	 * The view name which has been looked up and cached.
	 * <p>
	 * This property will contain the view name specified in the most recent LookupAllNames or LookupNames. If a Directory has been created
	 * but no lookups have been performed against it, this property is null.
	 * </p>
	 *
	 * @return The view name which has been looked up and cached.
	 */
	@Override
	public String getAvailableView();

	/**
	 * Returns mail data for a specified person.
	 * <p>
	 * For directory lookups of mail information, the resident server specified for the directory class instance will be used, if present.
	 * If that fails, bootstrap information for the method will be gleaned from the user's current operating environment.
	 * </p>
	 * <p>
	 * If no server responds to the lookup requests, the error code that is thrown is error code 4749 "Unable to access server."
	 * </p>
	 * <p>
	 * If username was not found in the directory, the error code that is thrown is error code 4731 "User not found in Directory."
	 * </p>
	 * <p>
	 * If the method fails for any other reason, the error code that is thrown is error code 4730 "GetMailInfo failed."
	 * </p>
	 *
	 * @param userName
	 *            The name of the user for whom mail information is requested.
	 * @return The vector contains the following elements:
	 *         <ul>
	 *         <li>Mail server - Home mail server for the specified person.</li>
	 *         <li>Build number - an empty string (use {@link #getMailInfo(String, boolean, boolean)} to get the build number).</li>
	 *         <li>Domino version - a null string (use {@link #getMailInfo(String, boolean, boolean)} to get the Domino version).</li>
	 *         <li>Mail file - Mail file for the specified person.</li>
	 *         <li>Short name - Short form of the specified person's name.</li>
	 *         <li>Mail domain - Notes Domain of the specified person's mail address.</li>
	 *         <li>User name - First entry in the list of user names honored for the specified person.</li>
	 *         <li>Internet mail address - Internet mail address for the specified person.</li>
	 *         <li>Out of Office - Out of Office service type. "1" indicates Agent, "2" indicates Service.</li>
	 *         </ul>
	 */
	@Override
	public Vector<String> getMailInfo(final String userName);

	/**
	 * Returns mail data for a specified person.
	 * <p>
	 * For directory lookups of mail information, the resident server specified for the directory class instance will be used, if present.
	 * If that fails, bootstrap information for the method will be gleaned from the user's current operating environment.
	 * </p>
	 * <p>
	 * If errorOnMultipleMatches is true, and multiple matches for the same name are found, the error code that is thrown is error code 4751
	 * "Directory contains multiple entries for this user."
	 * </p>
	 * <p>
	 * If no server responds to the lookup requests, the error code that is thrown is error code 4749 "Unable to access server."
	 * </p>
	 * <p>
	 * If userName was not found in the directory, the error code that is thrown is error code 4731 "User not found in Directory."
	 * </p>
	 * <p>
	 * If the method fails for any other reason, the error code that is thrown is error code 4730 "GetMailInfo failed."
	 * </p>
	 *
	 * @param userName
	 *            The name of the user for whom mail information is requested.
	 * @param getVersion
	 *            If true, requests build number and version information of the user's home mail server.
	 * @param errorOnMultipleMatches
	 *            Indicate how to handle multiple matches for the user name. If true, throws an exception. If s false, uses the first match.
	 *
	 * @return The vector contains the following elements:
	 *         <ul>
	 *         <li>Mail server - Home mail server for the specified person.</li>
	 *         <li>Build number - If <code>getVersion</code> is true, a string representation of the build number of the specified person's
	 *         mail server, for example, 303. If <code>getVersion</code> is false, an empty string.</li>
	 *         <li>Domino version - If <code>getVersion</code> is true, a string representation of the Domino version of the specified
	 *         person's mail server, for example, Build V80_07042006NP. If <code>getVersion</code> is false, a null string.</li>
	 *         <li>Mail file - Mail file for the specified person.</li>
	 *         <li>Short name - Short form of the specified person's name.</li>
	 *         <li>Mail domain - Notes Domain of the specified person's mail address.</li>
	 *         <li>User name - First entry in the list of user names honored for the specified person.</li>
	 *         <li>Internet mail address - Internet mail address for the specified person.</li>
	 *         <li>Out of Office - Out of Office service type. "1" indicates Agent, "2" indicates Service.</li>
	 *         </ul>
	 */
	@Override
	public Vector<String> getMailInfo(final String userName, final boolean getVersion, final boolean errorOnMultipleMatches);

	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	@Override
	public Session getParent();

	/**
	 * The name of the server represented by the Directory.
	 *
	 */
	@Override
	public String getServer();

	/**
	 * Indicates if only those directories enabled for group authorization will be searched by lookups.
	 * <p>
	 * This property works with the SearchAllDirectories and TrustedOnly properties to limit which directories are searched in a lookup.
	 * </p>
	 *
	 * @return True if lookups will only search directories marked "Enable for Group Authorization", false if lookups will search all
	 *         directories.
	 */
	@Override
	public boolean isGroupAuthorizationOnly();

	/**
	 * Indicates if the results of lookups are limited to fifty entries.
	 * <p>
	 * This property works with the PartialMatches property to control the number of results returned by a lookup.
	 * </p>
	 *
	 * @return True if lookups will only return the first fifty matches, false (default) if lookups will return all matches.
	 */
	@Override
	public boolean isLimitMatches();

	/**
	 * Indicates if lookups match on partial names.
	 * <p>
	 * This property is set by the optional PartialMatches parameter in the {@link #lookupNames(String, Vector, Vector, boolean)} method. It
	 * works with the LimitMatches property to control the number of results returned by a lookup.
	 * </p>
	 *
	 * @return True if lookups will match on partial names, false (default) if lookups will not match on partial names.
	 */
	@Override
	public boolean isPartialMatches();

	/**
	 * Indicates if all directories will be searched by lookups.
	 * <p>
	 * This property works with the GroupAuthorizationOnly and TrustedOnly properties to limit which directories are searched in a lookup.
	 * </p>
	 *
	 * @return True (default) if lookups will search all directories, False if lookups will stop searching after the first directory
	 *         containing the specified view name.
	 */
	@Override
	public boolean isSearchAllDirectories();

	/**
	 * Indicates if only directories that contain trusted information will be searched by lookups.
	 * <p>
	 * This property works with the GroupAuthorizationOnly and SearchAllDirectories properties to limit which directories are searched in a
	 * lookup.
	 * </p>
	 *
	 * @return True if lookups will search only directories containing trust information, false (default) if lookups will search all
	 *         directories.
	 */
	@Override
	public boolean isTrustedOnly();

	/**
	 * Indicates if the server of the context database will be used.
	 *
	 * @return True if the server of the context database will be used, or local if the context database is local, false (default) if the
	 *         server specified in lookup methods will be used.
	 */
	@Override
	public boolean isUseContextServer();

	/**
	 * Looks up designated Items for all Names contained in the specified View.
	 *
	 * <p>
	 * This method will flush the Directory cache and reset all child navigators.
	 * </p>
	 * <p>
	 * The items specified in this method can be:
	 * </p>
	 * <ol>
	 * <li>the programmatic name of a column (the programmatic name is not necessarily the display name)</li>
	 * <li>the programmatic name of a field in the note (names which are not columns in the specified view)</li>
	 * <li>a computed item provided by NAMELookup (such as $$DbName)</li>
	 * </ol>
	 * <p>
	 * For best performance, use column names from the specified view or computed items.
	 * </p>
	 * <p>
	 * Since no names are furnished as input arguments, the {@link DirectoryNavigator#getCurrentName()} property will be empty.
	 * </p>
	 * <h3>CAUTION</h3>
	 * <p>
	 * Large results will be limited by the system, according to settings in Notes®.ini. This method is intended for views with limited
	 * scopes.
	 * </p>
	 *
	 * @param view
	 *            The name of the view in which to look up names.
	 * @param item
	 *            Summary data items whose values will be returned by the lookup.
	 *
	 * @return The newly created directory navigator, containing values of the specified lookup items for all names in the specified view.
	 */
	@Override
	public DirectoryNavigator lookupAllNames(final String view, final String item);

	/**
	 * Looks up designated Items for all Names contained in the specified View.
	 *
	 * <p>
	 * This method will flush the Directory cache and reset all child navigators.
	 * </p>
	 * <p>
	 * The items specified in this method can be:
	 * </p>
	 * <ol>
	 * <li>the programmatic name of a column (the programmatic name is not necessarily the display name)</li>
	 * <li>the programmatic name of a field in the note (names which are not columns in the specified view)</li>
	 * <li>a computed item provided by NAMELookup (such as $$DbName)</li>
	 * </ol>
	 * <p>
	 * For best performance, use column names from the specified view or computed items.
	 * </p>
	 * <p>
	 * Since no names are furnished as input arguments, the {@link DirectoryNavigator#getCurrentName()} property will be empty.
	 * </p>
	 * <h3>CAUTION</h3>
	 * <p>
	 * Large results will be limited by the system, according to settings in Notes®.ini. This method is intended for views with limited
	 * scopes.
	 * </p>
	 *
	 * @param view
	 *            The name of the view in which to look up names.
	 * @param items
	 *            Summary data items whose values will be returned by the lookup.
	 *
	 * @return The newly created directory navigator, containing values of the specified lookup items for all names in the specified view.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public DirectoryNavigator lookupAllNames(final String view, final Vector items);

	/**
	 * Looks up designated Items for specified Names contained in the specified View.
	 * <p>
	 * This method will flush the Directory cache and reset all child navigators.
	 * </p>
	 * <p>
	 * The items specified in this method can be:
	 * </p>
	 * <ol>
	 * <li>the programmatic name of a column (the programmatic name is not necessarily the display name)</li>
	 * <li>the programmatic name of a field in the note (names which are not columns in the specified view)</li>
	 * <li>a computed item provided by NAMELookup (such as $$DbName)</li>
	 * </ol>
	 * <p>
	 * For best performance, use column names from the specified view or computed items.
	 * </p>
	 *
	 *
	 * @param view
	 *            The name of the view in which to look up names.
	 * @param name
	 *            The names to be searched in the view. Entries must match one of these names to be searched for items.
	 * @param item
	 *            Summary data items whose values will be returned by the lookup.
	 *
	 * @return The newly created directory navigator, containing the values in items of entries whose name matches <code>name</code>.
	 */
	@Override
	public DirectoryNavigator lookupNames(final String view, final String name, final String item);

	/**
	 * Looks up designated Items for specified Names contained in the specified View.
	 * <p>
	 * This method will flush the Directory cache and reset all child navigators.
	 * </p>
	 * <p>
	 * The items specified in this method can be:
	 * </p>
	 * <ol>
	 * <li>the programmatic name of a column (the programmatic name is not necessarily the display name)</li>
	 * <li>the programmatic name of a field in the note (names which are not columns in the specified view)</li>
	 * <li>a computed item provided by NAMELookup (such as $$DbName)</li>
	 * </ol>
	 * <p>
	 * For best performance, use column names from the specified view or computed items.
	 * </p>
	 *
	 *
	 * @param view
	 *            The name of the view in which to look up names.
	 * @param names
	 *            The names to be searched in the view. Entries must match one of these names to be searched for items.
	 * @param items
	 *            Summary data items whose values will be returned by the lookup.
	 * @param partialMatches
	 *            true if partial matches of names are allowed.
	 *
	 * @return The newly created directory navigator, containing the values in items of entries whose name matches (or partially matches, if
	 *         partialMatches is true) <code>name</code>.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public DirectoryNavigator lookupNames(final String view, final Vector names, final Vector items, final boolean partialMatches);

	/**
	 * Sets if only those directories enabled for group authorization will be searched by lookups.
	 * <p>
	 * This property works with the SearchAllDirectories and TrustedOnly properties to limit which directories are searched in a lookup.
	 * </p>
	 *
	 * @param flag
	 *            true if only those directories enabled for group authorization will be searched by lookups.
	 */
	@Override
	public void setGroupAuthorizationOnly(final boolean flag);

	/**
	 * Sets if the results of lookups are limited to fifty entries.
	 * <p>
	 * This property works with the PartialMatches property to control the number of results returned by a lookup.
	 * </p>
	 *
	 * @param flag
	 *            True if lookups will only return the first fifty matches, false if lookups will return all matches.
	 */
	@Override
	public void setLimitMatches(final boolean flag);

	/**
	 * Sets if all directories will be searched by lookups.
	 * <p>
	 * This property works with the GroupAuthorizationOnly and TrustedOnly properties to limit which directories are searched in a lookup.
	 * </p>
	 *
	 *
	 * @param flag
	 *            True if lookups will search all directories, false indicates lookups will stop searching after the first directory
	 *            containing the specified view name.
	 */
	@Override
	public void setSearchAllDirectories(final boolean flag);

	/**
	 * Indicates if only directories that contain trusted information will be searched by lookups.
	 * <p>
	 * This property works with the GroupAuthorizationOnly and SearchAllDirectories properties to limit which directories are searched in a
	 * lookup.
	 * </p>
	 *
	 * @param flag
	 *            True if lookups will search only directories containing trust information, False if lookups will search all directories.
	 */
	@Override
	public void setTrustedOnly(final boolean flag);

	/**
	 * Indicates if the server of the context database will be used.
	 * <p>
	 * If true, this property will override the server specified in a lookup method with the server of the context database.
	 * </p>
	 *
	 *
	 * @param True
	 *            if the server of the context database will be used, or local if the context database is local, false (default) if the
	 *            server specified in lookup methods will be used.
	 */
	@Override
	public void setUseContextServer(final boolean flag);

}
