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

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.Design;
import org.openntf.domino.types.FactorySchema;

/**
 * Represents an outline in a database.
 * <p>
 * An outline supports a hierarchy of outline entries and provides methods for their navigation and manipulation.
 * </p>
 */
public interface Outline
		extends Base<lotus.domino.Outline>, lotus.domino.Outline, org.openntf.domino.ext.Outline, Design, DatabaseDescendant {

	public static class Schema extends FactorySchema<Outline, lotus.domino.Outline, Database> {
		@Override
		public Class<Outline> typeClass() {
			return Outline.class;
		}

		@Override
		public Class<lotus.domino.Outline> delegateClass() {
			return lotus.domino.Outline.class;
		}

		@Override
		public Class<Database> parentClass() {
			return Database.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Adds an entry to an outline after the referenceEntry and as a child of the referenceEntry
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 *
	 * @param entry
	 *            The entry being added.
	 * @param referenceEntry
	 *            The name of an existing entry. Specify null to add the first entry to an empty outline.
	 * @deprecated Use {@link #createEntry} instead (this method is obsolete as of Release 5.0.2)
	 */
	@Override
	@Deprecated
	public void addEntry(final lotus.domino.OutlineEntry entry, final lotus.domino.OutlineEntry referenceEntry);

	/**
	 * Adds an entry to an outline and makes it a child entry.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 *
	 * @param entry
	 *            The entry being added.
	 * @param referenceEntry
	 *            The name of an existing entry. Specify null to add the first entry to an empty outline.
	 * @param after
	 *            true to put the entry after the existing entry; false to put the entry before the existing entry.
	 * @deprecated Use {@link #createEntry} instead (this method is obsolete as of Release 5.0.2)
	 */
	@Override
	@Deprecated
	public void addEntry(final lotus.domino.OutlineEntry entry, final lotus.domino.OutlineEntry referenceEntry, final boolean after);

	/**
	 * Adds an entry to an outline.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 *
	 * @param entry
	 *            The entry being added.
	 * @param referenceEntry
	 *            The name of an existing entry. Specify null to add the first entry to an empty outline.
	 * @param after
	 *            true to put the entry after the existing entry; false to put the entry before the existing entry.
	 * @param asChild
	 *            true to make the entry a child of the preceding entry. If boolean after is false and boolean asChild is true, an exception
	 *            is raised.
	 * @deprecated Use {@link #createEntry} instead (this method is obsolete as of Release 5.0.2)
	 */
	@Override
	@Deprecated
	public void addEntry(final lotus.domino.OutlineEntry entry, final lotus.domino.OutlineEntry referenceEntry, final boolean after,
			final boolean asChild);

	/**
	 * Creates a copy of an existing outline entry and adds it to the outline.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 *
	 * @param fromEntry
	 *            The entry to be copied.
	 * @return the new entry
	 */
	@Override
	public OutlineEntry createEntry(final lotus.domino.OutlineEntry fromEntry);

	/**
	 * Creates a copy of an existing outline entry and adds it to the outline.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 *
	 * @param fromEntry
	 *            The entry to be copied.
	 * @param referenceEntry
	 *            The entry after which the entry is being placed.
	 * @return the new entry
	 */
	@Override
	public OutlineEntry createEntry(final lotus.domino.OutlineEntry fromEntry, final lotus.domino.OutlineEntry referenceEntry);

	/**
	 * Creates a copy of an existing outline entry and adds it to the outline.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 *
	 * @param fromEntry
	 *            The entry to be copied.
	 * @param referenceEntry
	 *            The entry after which the entry is being placed.
	 * @param after
	 *            true to place the entry after the specified reference entry.
	 * @return the new entry
	 */
	@Override
	public OutlineEntry createEntry(final lotus.domino.OutlineEntry fromEntry, final lotus.domino.OutlineEntry referenceEntry,
			final boolean after);

	/**
	 * Creates a copy of an existing outline entry and adds it to the outline.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 *
	 * @param fromEntry
	 *            The entry to be copied.
	 * @param referenceEntry
	 *            The entry after which the entry is being placed.
	 * @param after
	 *            true to place the entry after the specified reference entry.
	 * @param asChild
	 *            true to place the entry as a child of the specified reference entry.
	 * @return the new entry
	 */
	@Override
	public OutlineEntry createEntry(final lotus.domino.OutlineEntry fromEntry, final lotus.domino.OutlineEntry referenceEntry,
			final boolean after, final boolean asChild);

	/**
	 * Creates an outline entry and adds it to the outline.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 *
	 * @param entryName
	 *            The name of the new entry
	 *
	 * @return the new entry
	 */
	@Override
	public OutlineEntry createEntry(final String entryName);

	/**
	 * Creates an outline entry and adds it to the outline.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 *
	 * @param entryName
	 *            The name of the new entry
	 * @param referenceEntry
	 *            The entry after which the entry is being placed.
	 * @return the new entry
	 */
	@Override
	public OutlineEntry createEntry(final String entryName, final lotus.domino.OutlineEntry referenceEntry);

	/**
	 * Creates an outline entry and adds it to the outline.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 *
	 * @param entryName
	 *            The name of the new entry
	 * @param referenceEntry
	 *            The entry after which the entry is being placed.
	 * @param after
	 *            true to place the entry after the specified reference entry.
	 * @return the new entry
	 */
	@Override
	public OutlineEntry createEntry(final String entryName, final lotus.domino.OutlineEntry referenceEntry, final boolean after);

	/**
	 * Creates an outline entry and adds it to the outline.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 *
	 * @param entryName
	 *            The name of the new entry
	 * @param referenceEntry
	 *            The entry after which the entry is being placed.
	 * @param after
	 *            true to place the entry after the specified reference entry.
	 * @param asChild
	 *            true to place the entry as a child of the specified reference entry
	 * @return the new entry
	 */
	@Override
	public OutlineEntry createEntry(final String entryName, final lotus.domino.OutlineEntry referenceEntry, final boolean after,
			final boolean asChild);

	/**
	 * Alias name of an outline for programmatic access.
	 *
	 */
	@Override
	public String getAlias();

	/**
	 * Gets the child of the specified outline entry.
	 *
	 * @param entry
	 *            The parent entry.
	 * @return The child entry.
	 */
	@Override
	public OutlineEntry getChild(final lotus.domino.OutlineEntry entry);

	/**
	 * An informational description of an outline.
	 *
	 */
	@Override
	public String getComment();

	/**
	 * Gets the first entry of an outline.
	 *
	 * @return The first entry of the outline, or null if there are no entries.
	 */
	@Override
	public OutlineEntry getFirst();

	/**
	 * Gets the last entry of an outline.
	 *
	 * @return The last entry of the outline, or null if there are no entries.
	 */
	@Override
	public OutlineEntry getLast();

	/**
	 * Name of an outline.
	 */
	@Override
	public String getName();

	/**
	 * Gets the entry immediately following a specified entry of an outline.
	 *
	 * @param entry
	 *            An entry in the outline.
	 * @return The entry following the one specified as the parameter, or null if there is no next entry.
	 */
	@Override
	public OutlineEntry getNext(final lotus.domino.OutlineEntry entry);

	/**
	 * Gets the entry at the same level following a specified entry of an outline.
	 *
	 * @param entry
	 *            An entry in the outline.
	 * @return The entry at the same level following the one specified as the parameter, or null if there is no next sibling.
	 */
	@Override
	public OutlineEntry getNextSibling(final lotus.domino.OutlineEntry entry);

	/**
	 * Returns the parent of a specified entry.
	 *
	 * @param entry
	 *            An entry in the outline.
	 * @return The parent entry of the one specified as the parameter, or null if there is no parent.
	 */
	@Override
	public OutlineEntry getParent(final lotus.domino.OutlineEntry entry);

	/**
	 * The parent database of the outline.
	 *
	 */
	@Override
	public Database getParentDatabase();

	/**
	 * Gets the entry immediately preceding a specified entry of an outline.
	 *
	 * @param entry
	 *            An entry in the outline.
	 * @return The entry preceding the one specified as the parameter, or null if there is no next entry.
	 */
	@Override
	public OutlineEntry getPrev(final lotus.domino.OutlineEntry entry);

	/**
	 * Gets the entry at the same level preceding a specified entry of an outline.
	 *
	 * @param entry
	 *            An entry in the outline.
	 * @return The entry at the same level preceding the one specified as the parameter, or null if there is no next sibling.
	 */
	@Override
	public OutlineEntry getPrevSibling(final lotus.domino.OutlineEntry entry);

	/**
	 * Moves an outline entry and subentries from one location to another.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 * <p>
	 * Moving an entry to the beginning of the outline makes it a top-level (level 0) entry.
	 * </p>
	 *
	 * @param entry
	 *            The entry being moved.
	 * @param referenceEntry
	 *            The entry after which the entry being moved is placed.
	 */
	@Override
	public void moveEntry(final lotus.domino.OutlineEntry entry, final lotus.domino.OutlineEntry referenceEntry);

	/**
	 * Moves an outline entry and subentries from one location to another.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 * <p>
	 * Moving an entry to the beginning of the outline makes it a top-level (level 0) entry.
	 * </p>
	 *
	 * @param entry
	 *            The entry being moved.
	 * @param referenceEntry
	 *            The entry after or before which the entry being moved is placed.
	 * @param after
	 *            true to move the entry after the reference entry false to move the entry before the reference entry, as a sibling
	 */
	@Override
	public void moveEntry(final lotus.domino.OutlineEntry entry, final lotus.domino.OutlineEntry referenceEntry, final boolean after);

	/**
	 * Moves an outline entry and subentries from one location to another.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 * <p>
	 * Moving an entry to the beginning of the outline makes it a top-level (level 0) entry.
	 * </p>
	 *
	 * @param entry
	 *            The entry being moved.
	 * @param referenceEntry
	 *            The entry after or before which the entry being moved is placed.
	 * @param after
	 *            true to move the entry after the reference entry false to move the entry before the reference entry, as a sibling
	 * @param asChild
	 *            true to make the entry a child of the preceding entry or false to not make the entry a child
	 */
	@Override
	public void moveEntry(final lotus.domino.OutlineEntry entry, final lotus.domino.OutlineEntry referenceEntry, final boolean after,
			final boolean asChild);

	/**
	 * Deletes an entry and its subentries from an outline.
	 * <p>
	 * You must {@link #save()} the outline or the effect of this method is lost when the program exits.
	 * </p>
	 * <p>
	 * An attempt to access entries removed with this method throws an exception.
	 * </p>
	 *
	 * @param entry
	 *            The entry being removed. Cannot be null.
	 */
	@Override
	public void removeEntry(final lotus.domino.OutlineEntry entry);

	/**
	 * Saves any changes made to the outline.
	 * <p>
	 * You must save the outline before the program exits or the effect of a method that changes the outline is lost. These methods include
	 * {@link #addEntry()}, {@link #createEntry(lotus.domino.OutlineEntry)},
	 * {@link #moveEntry(lotus.domino.OutlineEntry, lotus.domino.OutlineEntry)}, {@link #removeEntry(lotus.domino.OutlineEntry)}.
	 * </p>
	 *
	 * @return
	 */
	@Override
	public int save();

	/**
	 * Sets an alias name of an outline for programmatic access.
	 *
	 * @param alias
	 *            the new alias
	 */
	@Override
	public void setAlias(final String alias);

	/**
	 * Sets an informational description of an outline.
	 *
	 * @param comment
	 *            the new description
	 */
	@Override
	public void setComment(final String comment);

	/**
	 * Sets the name of an outline.
	 * 
	 * @param name
	 *            the new name of the outline
	 */
	@Override
	public void setName(final String name);

}
