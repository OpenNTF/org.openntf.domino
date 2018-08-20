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
 * Allows lookups of a specific associated Notes directory on a specific server or local computer.
 * <h3>Create</h3>
 * <p>
 * To create a new NotesDirectoryNavigator object you can use the following:
 * </p>
 * <ul>
 * <li>{@link Directory#createNavigator()}</li>
 * </ul>
 *
 * <p>
 * Lookups performed against a Directory replace the existing DirectoryNavigator for that directory with a new navigator. To replace a
 * DirectoryNavigator object you can use the following:
 * </p>
 * <ul>
 * <li>{@link Directory#lookupAllNames(String, String)}</li>
 * <li>{@link Directory#lookupNames(String, String, String)}</li>
 * </ul>
 *
 */
public interface DirectoryNavigator extends Base<lotus.domino.DirectoryNavigator>, lotus.domino.DirectoryNavigator,
		org.openntf.domino.ext.DirectoryNavigator, SessionDescendant {

	public static class Schema extends FactorySchema<DirectoryNavigator, lotus.domino.DirectoryNavigator, Directory> {
		@Override
		public Class<DirectoryNavigator> typeClass() {
			return DirectoryNavigator.class;
		}

		@Override
		public Class<lotus.domino.DirectoryNavigator> delegateClass() {
			return lotus.domino.DirectoryNavigator.class;
		}

		@Override
		public Class<Directory> parentClass() {
			return Directory.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Positions navigator to first match of the current name in the navigator.
	 * <p>
	 * Positions the directory navigator at the first match, and sets the {@link #getCurrentName() CurrentName} property with the name found
	 * in that match. Lookups automatically position the navigator at the first match of the first name.
	 * </p>
	 *
	 * @return boolean True if a match was found in the navigator. False if no match was found.
	 */
	@Override
	public boolean findFirstMatch();

	/**
	 * Positions navigator to the first name in the navigator and returns number of matches.
	 * <p>
	 * Positions the directory navigator at the first match of the first name, and sets the NameLocated property to true if the method
	 * succeeds. If the method fails, the {@link #isNameLocated() NameLocated} property is set to false. Does not change the current match.
	 * </p>
	 * <p>
	 * Lookups automatically position the navigator at the first match of the first name.
	 * </p>
	 *
	 * @return long The number of matches found in the navigator.
	 */
	@Override
	public long findFirstName();

	/**
	 * Positions navigator to next match of the current name in the navigator.
	 *
	 * @return boolean True if a match was found in the navigator. False if no match was found.
	 */
	@Override
	public boolean findNextMatch();

	/**
	 * Positions navigator to the next name in the namelist and returns number of matches.
	 * <p>
	 * Positions the directory navigator at the next name in the namelist, and sets the NameLocated property to true if the method succeeds.
	 * If the method fails, the {@link #isNameLocated() NameLocated} property is set to false. Does not change the current match.
	 * </p>
	 *
	 * @return long The number of matches found in the navigator.
	 */
	@Override
	public long findNextName();

	/**
	 * Positions navigator to nth match of the current name in the navigator.
	 *
	 * @param n
	 *            The number of the match at which to position the navigator, starting with 1.
	 * @return boolean True if a match was found in the navigator. False if no match was found.
	 */
	@Override
	public boolean findNthMatch(final long n);

	/**
	 * Positions navigator to the nth name in the namelist and returns number of matches.
	 * <p>
	 * Positions the directory navigator at the <em>n</em>th name in the namelist, and sets the {@link #isNameLocated() NameLocated}
	 * property to true if the method succeeds. If the method fails, the {@link #isNameLocated() NameLocated} property is set to false. Does
	 * not change the current match.
	 * </p>
	 *
	 * @param n
	 *            The number of the name at which to position the navigator, starting with 1.
	 * @return long The number of matches found in the navigator.
	 */
	@Override
	public long findNthName(final int n);

	/**
	 * The name of the current item in the current match to which the navigator is pointing.
	 * <p>
	 * This property will contain the name of the current item in the current match to which the directory navigator is pointing. It is
	 * updated by the {@link #getNextItemValue()} and {@link #getNthItemValue(int)} methods.
	 * </p>
	 *
	 * @return The name of the current item in the current match to which the navigator is pointing.
	 *
	 */
	@Override
	public String getCurrentItem();

	/**
	 * The iteration index into the internal match list of the current match to which the navigator is pointing.
	 * <p>
	 * This property will contain the iteration index into the internal match list of all matches for a given Name to which the directory
	 * navigator is pointing. It is updated by the {@link #findFirstMatch()}, {@link #findNextMatch()} and {@link #findNthMatch(long)}
	 * methods.
	 * </p>
	 *
	 * @return The iteration index into the internal match list of the current match to which the navigator is pointing.
	 *
	 */
	@Override
	public long getCurrentMatch();

	/**
	 * The number of matches found for the current name.
	 * <p>
	 * This property will contain the number of matches found for the CurrentName in the directory navigator. If the navigator was created
	 * by the {@link Directory#lookupAllNames(String, String)} method, so that CurrentName is blank, CurrentMatches will contain the number
	 * of matches returned from the {@link #findFirstName()} call.
	 * </p>
	 *
	 * @return The number of matches found for the current name.
	 *
	 */
	@Override
	public long getCurrentMatches();

	/**
	 * The name of the current match to which the navigator is pointing
	 * <p>
	 * This property will contain the name of the current match to which the directory navigator is pointing. It is updated by the
	 * {@link #findFirstMatch()}, {@link #findNextMatch()} and {@link #findNthMatch(long)} methods.
	 * </p>
	 * <p>
	 * If the directory navigator was created with the {@link Directory#lookupAllNames(String, String)} method, this property will be an
	 * empty string.
	 * </p>
	 *
	 * @return The name of the current match to which the navigator is pointing
	 *
	 */
	@Override
	public String getCurrentName();

	/**
	 * The name of the cached view.
	 * <p>
	 * This property will contain the name of the cached directory view used to create the directory navigator. It is updated by directory
	 * lookups.
	 * </p>
	 *
	 * @return The name of the cached view.
	 *
	 */
	@Override
	public String getCurrentView();

	/**
	 * Returns the value of the first item of the current match.
	 * <p>
	 * Lookups automatically position the navigator at the first match of the first name.
	 * </p>
	 *
	 * @return Vector The value of the first item of the current match.
	 */
	@Override
	public Vector<Object> getFirstItemValue();

	/**
	 * Returns the value of the next item of the current match.
	 * <p>
	 * Positions navigator to next item within current match, and updates the CurrentItem property with the item name.
	 * </p>
	 * <p>
	 * Lookups automatically position the navigator at the first match of the first name.
	 * </p>
	 *
	 * @return Vector The value of the next item of the current match.
	 */
	@Override
	public Vector<Object> getNextItemValue();

	/**
	 * Positions navigator to nth match of the current name in the navigator and returns the value of the current item.
	 * <p>
	 * Positions navigator to the <em>nth</em> match of the current name, and updates the CurrentItem property with the item name.
	 * </p>
	 * <p>
	 * The only way to advance the current item is by use of the {@link #getNextItemValue()} method.
	 * </p>
	 *
	 * @param n
	 *            The number of the match at which to position the navigator, starting with 1.
	 * @return Vector The value of the current item of the nth match.
	 */
	@Override
	public Vector<Object> getNthItemValue(final int n);

	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	@Override
	public Directory getParent();

	/**
	 * Indicates success of most recent match.
	 * <p>
	 * This property is set by the {@link #findFirstMatch()}, {@link #findNextMatch()}, and {@link #findNthMatch(long)} methods.
	 * </p>
	 *
	 * @return True if a match was successful, false if unsuccessful.
	 */
	@Override
	public boolean isMatchLocated();

	/**
	 * Indicates success of finding a name.
	 * <p>
	 * This property is set by the {@link #findFirstMatch()}, {@link #findNextMatch()}, and {@link #findNthMatch(long)} methods.
	 * </p>
	 *
	 * @return True if a name was successfully located, false if not.
	 */
	@Override
	public boolean isNameLocated();

}
