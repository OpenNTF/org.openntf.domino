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
import org.openntf.domino.types.FactorySchema;

/**
 * Represents a collection of Domino design and data elements in a database.
 * <h3>Creation and access</h3>
 * <p>
 * Use {@link Database#createNoteCollection(boolean)} to create a <code>NoteCollection</code> object. The object represents the notes in the
 * containing database. Use <code>createNoteCollection</code> to initialize all Select properties to true or false depending on the
 * parameter specification.
 * </p>
 *
 * <p>
 * Change the Select properties as desired to specify the content of the collection. You can set the Select properties directly, or in
 * groups by calling the Select methods. Further modify the collection as desired with the {@link #setSelectionFormula(String)
 * SelectionFormula} and {@link #setSinceTime(java.util.Date) SinceTime} properties.
 * </p>
 *
 * <p>
 * Use {@link #buildCollection()} to compile the collection. You must build before the collection can be used.
 * </p>
 *
 * <h3>Process the notes in the collection</h3>
 * <p>
 * To process all notes in the collection, use the enhanced for loop:
 *
 * <pre>
 * Session session = Factory.getSession();
 * Database db = session.getCurrentDatabase();
 *
 * NoteCollection notes = db.createNoteCollection(false);
 * notes.setSelectForms(true);
 *
 * notes.buildCollection();
 *
 * for (String noteID : notes) {
 * 	//work with the noteID
 * }
 * </pre>
 * </p>
 */
public interface NoteCollection extends Base<lotus.domino.NoteCollection>, lotus.domino.NoteCollection,
		org.openntf.domino.ext.NoteCollection, Iterable<String>, DatabaseDescendant {

	public static class Schema extends FactorySchema<NoteCollection, lotus.domino.NoteCollection, Database> {
		@Override
		public Class<NoteCollection> typeClass() {
			return NoteCollection.class;
		}

		@Override
		public Class<lotus.domino.NoteCollection> delegateClass() {
			return lotus.domino.NoteCollection.class;
		}

		@Override
		public Class<Database> parentClass() {
			return Database.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Enum to allow easy access to note types for building a Collection
	 *
	 * @since org.openntf.domino 2.5.0
	 */
	public static enum SelectOption {
		ACL, ACTIONS, AGENTS, DATABASE_SCRIPT, DATA_CONNECTIONS, DOCUMENTS, FOLDERS, FORMS, FRAMESETS, HELP_ABOUT, HELP_INDEX, HELP_USING,
		ICON, IMAGE_RESOURCES, JAVA_RESOURCES, MISC_CODE, MISC_FORMAT, MISC_INDEX, NAVIGATORS, OUTLINES, PAGES, PROFILES,
		REPLICATION_FORMULAS, SCRIPT_LIBRARIES, SHARED_FIELDS, STYLESHEETS, SUBFORMS, VIEWS, ALL_BUT_NOT;
	}

	/**
	 * Adds one or more notes to a note collection.
	 * <p>
	 * The {@link #getParent() Parent} database of the note(s) to add must be the same as the note collection.
	 * </p>
	 *
	 * @param additionspecifier
	 *            A DocumentCollection object representing the note to be added.
	 */
	@Override
	public void add(final lotus.domino.DocumentCollection additionSpecifier);

	/**
	 * Adds a note to a note collection.
	 * <p>
	 * The {@link #getParent() Parent} database of the note(s) to add must be the same as the note collection.
	 * </p>
	 *
	 * @param additionspecifier
	 *            A Form object representing the note to be added.
	 */
	@Override
	public void add(final lotus.domino.Form additionSpecifier);

	/**
	 * Adds a note to a note collection.
	 * <p>
	 * The {@link #getParent() Parent} database of the note(s) to add must be the same as the note collection.
	 * </p>
	 *
	 * @param additionspecifier
	 *            The note ID of the note to be added.
	 */
	@Override
	public void add(final int additionSpecifier);

	/**
	 * Adds one or more notes to a note collection.
	 * <p>
	 * The {@link #getParent() Parent} database of the note(s) to add must be the same as the note collection.
	 * </p>
	 *
	 * @param additionspecifier
	 *            The note ID of the notes to be added.
	 */
	@Override
	public void add(final int[] additionSpecifier);

	/**
	 * Adds a note to a note collection.
	 * <p>
	 * The {@link #getParent() Parent} database of the note(s) to add must be the same as the note collection.
	 * </p>
	 *
	 * @param additionspecifier
	 *            An Agent object representing the note to be added.
	 */
	@Override
	public void add(final lotus.domino.Agent additionSpecifier);

	/**
	 * Adds a note to a note collection.
	 * <p>
	 * The {@link #getParent() Parent} database of the note(s) to add must be the same as the note collection.
	 * </p>
	 *
	 * @param additionspecifier
	 *            A Document object representing the note to be added.
	 */
	@Override
	public void add(final lotus.domino.Document additionSpecifier);

	/**
	 * Adds one or more notes to a note collection.
	 * <p>
	 * The {@link #getParent() Parent} database of the note(s) to add must be the same as the note collection.
	 * </p>
	 *
	 * @param additionspecifier
	 *            A NoteCollection object representing the notes to be added.
	 */
	@Override
	public void add(final lotus.domino.NoteCollection additionSpecifier);

	/**
	 * Adds a note to a note collection.
	 * <p>
	 * The {@link #getParent() Parent} database of the note(s) to add must be the same as the note collection.
	 * </p>
	 *
	 * @param additionspecifier
	 *            The note ID of the note to be added.
	 */
	@Override
	public void add(final String noteid);

	/**
	 * Adds a note to a note collection.
	 * <p>
	 * The {@link #getParent() Parent} database of the note(s) to add must be the same as the note collection.
	 * </p>
	 *
	 * @param additionspecifier
	 *            A View object representing the note to be added.
	 */
	@Override
	public void add(final lotus.domino.View additionSpecifier);

	/**
	 * Builds (compiles) the note collection.
	 * <p>
	 * You must build the collection before it is usable.
	 * </p>
	 * <p>
	 * The built collection contains the database notes determined by the current values of {@link #getSelectionFormula() SelectionFormula},
	 * {@link #getSinceTime() SinceTime} and the Select properties.
	 * </p>
	 * <p>
	 * Initially <code>SelectionFormula</code> and <code>SinceTime</code> select all notes.
	 * </p>
	 * <p>
	 * Initially the Select properties are all true or all false as determined by {@link Database#createNoteCollection()}. You can reset
	 * them all true or all false with {@link #selectAllNotes(boolean)}.
	 * </p>
	 * <p>
	 * You can set the Select properties individually or in groups. The following list arranges the Select properties under the selectAll
	 * methods that set them:
	 * </p>
	 * <ol>
	 * <li>Not in a selectAll group:</li>
	 * <ul>
	 * <li>{@link #setSelectDocuments(boolean) SelectDocuments}</li>
	 * <li>{@link #setSelectProfiles(boolean) SelectProfiles}</li>
	 * </ul>
	 * </li>
	 * <li>Set by {@link #selectAllAdminNotes(boolean) SelectAllAdminNotes}:</li>
	 * <ul>
	 * <li>{@link #setSelectAcl(boolean) SelectAcl}</a></li>
	 * <li>{@link #setSelectReplicationFormulas(boolean) SelectReplicationFormulas}</li>
	 * </ul>
	 * </li>
	 * <li>Set by {@link #selectAllDesignElements(boolean) selectAllDesignElements} and {@link #selectAllCodeElements(boolean)
	 * selectAllCodeElements}:
	 * <ul>
	 * <li>{@link #setSelectAgents(boolean) SelectAgents}</li>
	 * <li>{@link #setSelectDatabaseScript(boolean) SelectDatabaseScript}</li>
	 * <li>{@link #setSelectDataConnections(boolean) SelectDataConnections}</li>
	 * <li>{@link #setSelectMiscCodeElements(boolean) SelectMiscCodeElements}</li>
	 * <li>{@link #setSelectOutlines(boolean) SelectOutlines}</li>
	 * <li>{@link #setSelectScriptLibraries(boolean) SelectScriptLibraries}</li>
	 * </ul>
	 * </li>
	 * <li>Set by {@link #selectAllDesignElements(boolean) selectAllDesignElements} and {@link #selectAllFormatElements(boolean)
	 * selectAllFormatElements}:
	 * <ul>
	 * <li>{@link #setSelectActions(boolean) SelectActions}</li>
	 * <li>{@link #setSelectForms(boolean) SelectForms}</li>
	 * <li>{@link #setSelectFramesets(boolean) SelectFramesets}</li>
	 * <li>{@link #setSelectImageResources(boolean) SelectImageResources}</li>
	 * <li>{@link #setSelectJavaResources(boolean) SelectJavaResources}</li>
	 * <li>{@link #setSelectMiscFormatElements(boolean) SelectMiscFormatElements}</li>
	 * <li>{@link #setSelectPages(boolean) SelectPages}</li>
	 * <li>{@link #setSelectStylesheetResources(boolean) SelectStylesheetResources}</li>
	 * <li>{@link #setSelectSubforms(boolean) SelectSubforms}</li>
	 * </ul>
	 * </li>
	 * <li>Set by {@link #selectAllDesignElements(boolean) selectAllDesignElements} and {@link #selectAllIndexElements(boolean)
	 * selectAllIndexElements}:
	 * <ul>
	 * <li>{@link #setSelectFolders(boolean) SelectFolders}</li>
	 * <li>{@link #setSelectMiscIndexElements(boolean) SelectMiscIndexElements}</li>
	 * <li>{@link #setSelectNavigators(boolean) SelectNavigators}</li>
	 * <li>{@link #setSelectViews(boolean) SelectViews}</li>
	 * </ul>
	 * </li>
	 * <li>Set by {@link #selectAllDesignElements(boolean) selectAllDesignElements}:
	 * <ul>
	 * <li>{@link #setSelectHelpAbout(boolean) SelectHelpAbout}</li>
	 * <li>{@link #setSelectHelpIndex(boolean) SelectHelpIndex}</li>
	 * <li>{@link #setSelectHelpUsing(boolean) SelectHelpUsing}</li>
	 * <li>{@link #setSelectIcon(boolean) SelectIcon}</li>
	 * <li>{@link #setSelectSharedFields(boolean) SelectSharedFields}</li>
	 * </ul>
	 * </li>
	 * </ol>
	 * <p>
	 * Use {@link #getLastBuildTime()} to get the time of the last build.
	 * </p>
	 *
	 */
	@Override
	public void buildCollection();

	/**
	 * Clears (invalidates) a note collection build.
	 * <p>
	 * Clearing renders a built note collection unusable. The collection must be {@link #buildCollection() built} again.
	 * </p>
	 * <p>
	 * Clearing a note collection does not change {@link #getSelectionFormula() SelectionFormula}, {@link #getSinceTime() SinceTime}, and
	 * the Select properties. Use {@link #selectAllNotes(boolean)} to reinitialize the Select properties. Reinitialize
	 * {@link #setSelectionFormula(String) SelectionFormula} and {@link #setSinceTime(java.util.Date) SinceTime} individually.
	 * </p>
	 *
	 */
	@Override
	public void clearCollection();

	/**
	 * The number of notes in the collection.
	 */
	@Override
	public int getCount();

	/**
	 * Gets the first note in a collection.
	 */
	@Override
	public String getFirstNoteID();

	/**
	 * Date and time of the last build for this note collection.
	 * <p>
	 * The {@link #buildCollection()} method sets this time.
	 * </p>
	 * <p>
	 * By default (prior to a build) this property is 12:00:00 AM without a date setting.
	 * </p>
	 *
	 */
	@Override
	public DateTime getLastBuildTime();

	/**
	 * Gets the last note in a collection.
	 */
	@Override
	public String getLastNoteID();

	/**
	 * Given a note, finds the note immediately following it in a collection.
	 * <p>
	 * If no next note exists, this method returns a string of length 0.
	 * </p>
	 * <p>
	 * This method throws an exception if the parameter is an invalid note ID.
	 * </p>
	 *
	 * @param noteId
	 *            A valid note ID.
	 * @return The ID of the note following the specified note.
	 */
	@Override
	public String getNextNoteID(final String noteId);

	/**
	 * Returns the NoteID of all notes in the collection as an array of integers
	 */
	@Override
	public int[] getNoteIDs();

	/**
	 * The database that contains the note collection.
	 */
	@Override
	public Database getParent();

	/**
	 * Given a note, finds the note immediately preceding it in a collection.
	 * <p>
	 * If no next note exists, this method returns a string of length 0.
	 * </p>
	 * <p>
	 * This method throws an exception if the parameter is an invalid note ID.
	 * </p>
	 *
	 * @param noteId
	 *            A valid note ID.
	 * @return The ID of the note preceding the specified note.
	 */
	@Override
	public String getPrevNoteID(final String noteId);

	/**
	 * Indicates whether the collection contains an ACL note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllAdminNotes(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains an ACL note.
	 */
	@Override
	public boolean getSelectAcl();

	/**
	 * Indicates whether the collection contains notes for actions.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for actions.
	 */
	@Override
	public boolean getSelectActions();

	/**
	 * Indicates whether the collection contains notes for agents.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllCodeElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for agents.
	 */
	@Override
	public boolean getSelectAgents();

	/**
	 * Indicates whether the collection contains a database script note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllCodeElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains a database script note.
	 */
	@Override
	public boolean getSelectDatabaseScript();

	/**
	 * Indicates whether the collection contains a data connection note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllCodeElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains a data connection note.
	 */
	@Override
	public boolean getSelectDataConnections();

	/**
	 * Indicates whether the collection contains the data documents.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDataNotes(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains the data documents.
	 */
	@Override
	public boolean getSelectDocuments();

	/**
	 * Indicates whether the collection contains notes for folders.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllIndexElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for folders.
	 */
	@Override
	public boolean getSelectFolders();

	/**
	 * Indicates whether the collection contains notes for forms.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for forms.
	 */
	@Override
	public boolean getSelectForms();

	/**
	 * Indicates whether the collection contains notes for frame sets.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for frame sets.
	 */
	@Override
	public boolean getSelectFramesets();

	/**
	 * Indicates whether the collection contains an "About Database" note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains an "About Database" note.
	 */
	@Override
	public boolean getSelectHelpAbout();

	/**
	 * Indicates whether the collection contains a help index note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains a help index note.
	 */
	@Override
	public boolean getSelectHelpIndex();

	/**
	 * Indicates whether the collection contains a "Using Database" note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains a "Using Database" note.
	 */
	@Override
	public boolean getSelectHelpUsing();

	/**
	 * Indicates whether the collection contains an icon note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains an icon note.
	 */
	@Override
	public boolean getSelectIcon();

	/**
	 * Indicates whether the collection contains notes for image resources.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for image resources.
	 */
	@Override
	public boolean getSelectImageResources();

	/**
	 * Formula that selects notes for inclusion in the collection.
	 * <p>
	 * This property must be a valid Domino formula that evaluates to True (includes the note) or False (excludes the note) for each note.
	 * </p>
	 * <p>
	 * This property is initially an empty string, which is equivalent to selecting all notes.
	 * </p>
	 * <p>
	 * This property is not a stand-alone specification. It intersects the other selection criteria.
	 * </p>
	 *
	 * @return Formula that selects notes for inclusion in the collection.
	 */
	@Override
	public String getSelectionFormula();

	/**
	 * Indicates whether the collection contains notes for Java resources.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for Java resources.
	 */
	@Override
	public boolean getSelectJavaResources();

	/**
	 * Indicates whether the collection contains notes for miscellaneous code elements.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllCodeElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</a></li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for miscellaneous code elements.
	 */
	@Override
	public boolean getSelectMiscCodeElements();

	/**
	 * Indicates whether the collection contains notes for miscellaneous format elements.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for miscellaneous format elements.
	 */
	@Override
	public boolean getSelectMiscFormatElements();

	/**
	 * Indicates whether the collection contains notes for miscellaneous index elements.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllIndexElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for miscellaneous index elements.
	 */
	@Override
	public boolean getSelectMiscIndexElements();

	/**
	 * Indicates whether the collection contains notes for navigators.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllIndexElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for navigators.
	 */
	@Override
	public boolean getSelectNavigators();

	/**
	 * Indicates whether the collection contains notes for outlines.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllCodeElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for outlines.
	 */
	@Override
	public boolean getSelectOutlines();

	/**
	 * Indicates whether the collection contains notes for pages.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for pages.
	 */
	@Override
	public boolean getSelectPages();

	/**
	 * Indicates whether the collection contains profile documents.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDataNotes(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains profile documents.
	 */
	@Override
	public boolean getSelectProfiles();

	/**
	 * Indicates whether the collection contains replication formulas.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllAdminNotes(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains replication formulas.
	 */
	@Override
	public boolean getSelectReplicationFormulas();

	/**
	 * Indicates whether the collection contains notes for script libraries.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllCodeElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for script libraries.
	 */
	@Override
	public boolean getSelectScriptLibraries();

	/**
	 * Indicates whether the collection contains notes for shared fields.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for shared fields.
	 */
	@Override
	public boolean getSelectSharedFields();

	/**
	 * Indicates whether the collection contains notes for style sheet resources.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for style sheet resources.
	 */
	@Override
	public boolean getSelectStylesheetResources();

	/**
	 * Indicates whether the collection contains notes for subforms.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for subforms.
	 */
	@Override
	public boolean getSelectSubforms();

	/**
	 * Indicates whether the collection contains notes for views.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllIndexElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @return True if the collection contains notes for views.
	 */
	@Override
	public boolean getSelectViews();

	/**
	 * Indicates the earliest note creation time in the collection.
	 * <p>
	 * {@link #buildCollection()} selects notes from the time specified by this property to the present time, excluding notes created prior
	 * to this time.
	 * </p>
	 * <p>
	 * By default this property is 12:00:00 AM without a date setting, which means that creation time is not a factor unless you explicitly
	 * set this property to a date.
	 * </p>
	 * <p>
	 * This property is not a stand-alone specification. It intersects the other selection criteria.
	 * </p>
	 *
	 * @return The date and time of the earliest note creation time in the collection.
	 *
	 */
	@Override
	public DateTime getSinceTime();

	/**
	 * Returns a Universal ID of a note given its noteid
	 * <p>
	 * The noteid parameter must be in hexadecimal format. Use <code>Integer.toHexString()</code> to convert a noteid returned by the
	 * {@link #getNoteIDs()} method to get a noteid suitable for this method.
	 * </p>
	 *
	 * @param noteid
	 *            Note ID in a hexadecimal format
	 * @return Universal ID of a note with corresponding noteid
	 */
	@Override
	public String getUNID(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.NoteCollection#getUntilTime()
	 */
	@Override
	public DateTime getUntilTime();

	/**
	 * Creates a note collection containing the notes common to the original collection and the note or notes specified in the intersection
	 * parameter.
	 * <p>
	 * The {@link #getParent() Parent } databases of the note collections must be the same.
	 * </p>
	 *
	 * @param intersectionspecifier
	 *            A Document object representing the note to be combined.
	 */
	@Override
	public void intersect(final lotus.domino.Document intersectionSpecifier);

	/**
	 * Creates a note collection containing the notes common to the original collection and the note or notes specified in the intersection
	 * parameter.
	 * <p>
	 * The {@link #getParent() Parent } databases of the note collections must be the same.
	 * </p>
	 *
	 * @param intersectionspecifier
	 *            A DocumentCollection object representing the notes to be combined.
	 */
	@Override
	public void intersect(final lotus.domino.DocumentCollection intersectionSpecifier);

	/**
	 * Creates a note collection containing the notes common to the original collection and the note or notes specified in the intersection
	 * parameter.
	 * <p>
	 * The {@link #getParent() Parent } databases of the note collections must be the same.
	 * </p>
	 *
	 * @param intersectionspecifier
	 *            A Form object representing the note to be combined.
	 */
	@Override
	public void intersect(final lotus.domino.Form intersectionSpecifier);

	/**
	 * Creates a note collection containing the notes common to the original collection and the note or notes specified in the intersection
	 * parameter.
	 * <p>
	 * The {@link #getParent() Parent } databases of the note collections must be the same.
	 * </p>
	 *
	 * @param intersectionspecifier
	 *            The note ID of the note to be combined.
	 */
	@Override
	public void intersect(final int intersectionSpecifier);

	/**
	 * Creates a note collection containing the notes common to the original collection and the note or notes specified in the intersection
	 * parameter.
	 * <p>
	 * The {@link #getParent() Parent } databases of the note collections must be the same.
	 * </p>
	 *
	 * @param intersectionspecifier
	 *            An Agent object representing the note to be combined.
	 */
	@Override
	public void intersect(final lotus.domino.Agent intersectionSpecifier);

	/**
	 * Creates a note collection containing the notes common to the original collection and the note or notes specified in the intersection
	 * parameter.
	 * <p>
	 * The {@link #getParent() Parent } databases of the note collections must be the same.
	 * </p>
	 *
	 * @param intersectionspecifier
	 *            A NoteCollection object representing the note to be combined.
	 */
	@Override
	public void intersect(final lotus.domino.NoteCollection intersectionSpecifier);

	/**
	 * Creates a note collection containing the notes common to the original collection and the note or notes specified in the intersection
	 * parameter.
	 * <p>
	 * The {@link #getParent() Parent } databases of the note collections must be the same.
	 * </p>
	 *
	 * @param intersectionspecifier
	 *            The note ID of the note to be combined.
	 */
	@Override
	public void intersect(final String noteid);

	/**
	 * Creates a note collection containing the notes common to the original collection and the note or notes specified in the intersection
	 * parameter.
	 * <p>
	 * The {@link #getParent() Parent } databases of the note collections must be the same.
	 * </p>
	 *
	 * @param intersectionspecifier
	 *            A View object representing the note to be combined.
	 */
	@Override
	public void intersect(final lotus.domino.View intersectionSpecifier);

	/**
	 * Removes a note from a note collection.
	 *
	 * @param removalspecifier
	 *            A Document object representing the note to be removed.
	 */
	@Override
	public void remove(final lotus.domino.Document removalSpecifier);

	/**
	 * Removes one or more notes from a note collection.
	 *
	 * @param removalspecifier
	 *            A DocumentCollection object representing the notes to be removed.
	 */
	@Override
	public void remove(final lotus.domino.DocumentCollection removalSpecifier);

	/**
	 * Removes a note from a note collection.
	 *
	 * @param removalspecifier
	 *            A Form object representing the note to be removed.
	 */
	@Override
	public void remove(final lotus.domino.Form removalSpecifier);

	/**
	 * Removes a note from a note collection.
	 *
	 * @param removalspecifier
	 *            The note ID of the note to be removed.
	 */
	@Override
	public void remove(final int removalSpecifier);

	/**
	 * Removes a note from a note collection.
	 *
	 * @param removalspecifier
	 *            An Agent object representing the note to be removed.
	 */
	@Override
	public void remove(final lotus.domino.Agent removalSpecifier);

	/**
	 * Removes one or more notes from a note collection.
	 *
	 * @param removalspecifier
	 *            A NoteCollection object representing the notes to be removed.
	 */
	@Override
	public void remove(final lotus.domino.NoteCollection removalSpecifier);

	/**
	 * Removes a note from a note collection.
	 *
	 * @param removalspecifier
	 *            The note ID of the note to be removed.
	 */
	@Override
	public void remove(final String noteid);

	/**
	 * Removes a note from a note collection.
	 *
	 * @param removalspecifier
	 *            A View object representing the note to be removed.
	 */
	@Override
	public void remove(final lotus.domino.View removalSpecifier);

	/**
	 * Selects or deselects the administration notes for inclusion in the collection.
	 * <p>
	 * The administration notes are those regulated by the following properties. This method sets these properties true or false as
	 * indicated by the selector value.
	 * </p>
	 * <ul>
	 * <li>{@link #setSelectAcl(boolean)}</li>
	 * <li>{@link #setSelectReplicationFormulas(boolean)}</li>
	 * </ul>
	 *
	 * @param selectorValue
	 *            true includes the administration notes, false excludes the administration notes
	 */
	@Override
	public void selectAllAdminNotes(final boolean selectorValue);

	/**
	 * Selects or deselects the notes for code elements for inclusion in the collection.
	 * <p>
	 * The notes for code elements are those regulated by the following properties. This method sets these properties true or false as
	 * indicated by the selector value.
	 * </p>
	 * <ul>
	 * <li>{@link #setSelectAgents(boolean)}</li>
	 * <li>{@link #setSelectDatabaseScript(boolean)}</li>
	 * <li>{@link #setSelectDataConnections(boolean)}</li>
	 * <li>{@link #setSelectMiscCodeElements(boolean)}</li>
	 * <li>{@link #setSelectOutlines(boolean)}</li>
	 * <li>{@link #setSelectScriptLibraries(boolean)}</li>
	 * </ul>
	 *
	 * @param selectorValue
	 *            true includes all code elements, false excludes all code elements
	 */
	@Override
	public void selectAllCodeElements(final boolean selectorValue);

	/**
	 * Selects or deselects the data notes for inclusion in the collection.
	 * <p>
	 * The data notes are those regulated by the following properties. This method sets these properties true or false as indicated by the
	 * selector value.
	 * </p>
	 * <ul>
	 * <li>{@link #setSelectDocuments(boolean)}</li>
	 * <li>{@link #setSelectProfiles(boolean)}</li>
	 * </ul>
	 *
	 * @param selectorValue
	 *            true includes the data notes false excludes the data notes
	 */
	@Override
	public void selectAllDataNotes(final boolean selectorValue);

	/**
	 * Selects or deselects the notes for design elements for inclusion in the collection.
	 * <p>
	 * The notes for design elements are those regulated by the following properties. This method sets these properties true or false as
	 * indicated by the selector value.
	 * </p>
	 * <ul>
	 * <li>Also set by {@link #selectAllCodeElements(boolean)}
	 * <ul>
	 * <li>{@link #setSelectAgents(boolean)}</li>
	 * <li>{@link #setSelectDatabaseScript(boolean)}</li>
	 * <li>{@link #setSelectMiscCodeElements(boolean)}</li>
	 * <li>{@link #setSelectOutlines(boolean)}</li>
	 * <li>{@link #setSelectScriptLibraries(boolean)}</li>
	 * </ul>
	 * </li>
	 * <li>Also set by {@link #selectAllFormatElements(boolean)}
	 * <ul>
	 * <li>{@link #setSelectActions(boolean)}</li>
	 * <li>{@link #setSelectForms(boolean)}</li>
	 * <li>{@link #setSelectFramesets(boolean)}</li>
	 * <li>{@link #setSelectImageResources(boolean)}</li>
	 * <li>{@link #setSelectJavaResources(boolean)}</li>
	 * <li>{@link #setSelectMiscFormatElements(boolean)}</li>
	 * <li>{@link #setSelectPages(boolean)}</li>
	 * <li>{@link #setSelectStylesheetResources(boolean)}</li> {@link #setSelectSubforms(boolean)}</li>
	 * </ul>
	 * </li>
	 * <li>Also set by {@link #selectAllIndexElements(boolean)}
	 * <ul>
	 * <li>{@link #setSelectFolders(boolean)}</li>
	 * <li>{@link #setSelectMiscIndexElements(boolean)}</li>
	 * <li>{@link #setSelectNavigators(boolean)}</li>
	 * <li>{@link #setSelectViews(boolean)}</li>
	 * </ul>
	 * </li>
	 * <li>Other:
	 * <ul>
	 * <li>{@link #setSelectHelpAbout(boolean)}</li>
	 * <li>{@link #setSelectHelpIndex(boolean)}</li>
	 * <li>{@link #setSelectHelpUsing(boolean)}</li>
	 * <li>{@link #setSelectIcon(boolean)}</li>
	 * <li>{@link #setSelectSharedFields(boolean)}</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * @param selectorValue
	 *            true includes all design elements, false excludes all design elements
	 */
	@Override
	public void selectAllDesignElements(final boolean selectorValue);

	/**
	 * Selects or deselects the notes for format elements for inclusion in the collection.
	 * <p>
	 * The notes for format elements are those regulated by the following properties. This method sets these properties true or false as
	 * indicated by the selector value.
	 * </p>
	 * <ul>
	 * <li>{@link #setSelectActions(boolean)}</li>
	 * <li>{@link #setSelectForms(boolean)}</li>
	 * <li>{@link #setSelectFramesets(boolean)}</li>
	 * <li>{@link #setSelectImageResources(boolean)}</li>
	 * <li>{@link #setSelectJavaResources(boolean)}</li>
	 * <li>{@link #setSelectMiscFormatElements(boolean)}</li>
	 * <li>{@link #setSelectPages(boolean)}</li>
	 * <li>{@link #setSelectStylesheetResources(boolean)}</li>
	 * <li>{@link #setSelectSubforms(boolean)}</li>
	 * </ul>
	 *
	 * @param selectorValue
	 *            true includes all format elements false excludes all format elements
	 */
	@Override
	public void selectAllFormatElements(final boolean selectorValue);

	/**
	 * Selects or deselects the notes for index elements for inclusion in the collection.
	 * <p>
	 * The notes for index elements are those regulated by the following properties. This method sets these properties true or false as
	 * indicated by the selector value.
	 * </p>
	 * <ul>
	 * <li>{@link #setSelectFolders(boolean)}</li>
	 * <li>{@link #setSelectMiscIndexElements(boolean)}</li>
	 * <li>{@link #setSelectNavigators(boolean)}</li>
	 * <li>{@link #setSelectViews(boolean)}</li>
	 * </ul>
	 *
	 * @param selectorValue
	 *            true includes all index elements, false excludes all index elements
	 */
	@Override
	public void selectAllIndexElements(final boolean selectorValue);

	/**
	 * Selects or deselects all notes for inclusion in the collection.
	 *
	 * @param selectorValue
	 *            true includes all notes, false excludes all notes
	 */
	@Override
	public void selectAllNotes(final boolean selectorValue);

	/**
	 * Sets whether the collection contains an ACL note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllAdminNotes(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain an ACL note.
	 */
	@Override
	public void setSelectAcl(final boolean flag);

	/**
	 * Sets whether the collection contains notes for actions.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for actions.
	 */
	@Override
	public void setSelectActions(final boolean flag);

	/**
	 * Sets whether the collection contains notes for agents.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllCodeElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for agents.
	 */
	@Override
	public void setSelectAgents(final boolean flag);

	/**
	 * Sets whether the collection contains a database script note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllCodeElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain a database script note.
	 */
	@Override
	public void setSelectDatabaseScript(final boolean flag);

	/**
	 * Sets whether the collection contains a data connection note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllCodeElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain a data connection note.
	 */
	@Override
	public void setSelectDataConnections(final boolean flag);

	/**
	 * Sets whether the collection contains the data documents.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDataNotes(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain the data documents.
	 */
	@Override
	public void setSelectDocuments(final boolean flag);

	/**
	 * Sets whether the collection contains notes for folders.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllIndexElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for folders.
	 */

	@Override
	public void setSelectFolders(final boolean flag);

	/**
	 * Sets whether the collection contains notes for forms.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for forms.
	 */

	@Override
	public void setSelectForms(final boolean flag);

	/**
	 * Sets whether the collection contains notes for frame sets.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for frame sets.
	 */
	@Override
	public void setSelectFramesets(final boolean flag);

	/**
	 * Sets whether the collection contains an "About Database" note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain an "About Database" note.
	 */
	@Override
	public void setSelectHelpAbout(final boolean flag);

	/**
	 * Sets whether the collection contains a help index note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain a help index note.
	 */
	@Override
	public void setSelectHelpIndex(final boolean flag);

	/**
	 * Sets whether the collection contains a "Using Database" note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain a "Using Database" note.
	 */
	@Override
	public void setSelectHelpUsing(final boolean flag);

	/**
	 * Sets whether the collection contains an icon note.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain an icon note.
	 */
	@Override
	public void setSelectIcon(final boolean flag);

	/**
	 * Sets whether the collection contains notes for image resources.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for image resources.
	 */
	@Override
	public void setSelectImageResources(final boolean flag);

	/**
	 * Set the formula that selects notes for inclusion in the collection.
	 * <p>
	 * This property must be a valid Domino formula that evaluates to True (includes the note) or False (excludes the note) for each note.
	 * </p>
	 * <p>
	 * This property is initially an empty string, which is equivalent to selecting all notes.
	 * </p>
	 * <p>
	 * This property is not a stand-alone specification. It intersects the other selection criteria.
	 * </p>
	 *
	 * @param formula
	 *            Formula that selects notes for inclusion in the collection.
	 */
	@Override
	public void setSelectionFormula(final String formula);

	/**
	 * Sets whether the collection contains notes for Java resources.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for Java resources.
	 */
	@Override
	public void setSelectJavaResources(final boolean flag);

	/**
	 * Sets whether the collection contains notes for miscellaneous code elements.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllCodeElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</a></li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for miscellaneous code elements.
	 */
	@Override
	public void setSelectMiscCodeElements(final boolean flag);

	/**
	 * Sets whether the collection contains notes for miscellaneous format elements.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for miscellaneous format elements.
	 */
	@Override
	public void setSelectMiscFormatElements(final boolean flag);

	/**
	 * Sets whether the collection contains notes for miscellaneous index elements.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllIndexElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for miscellaneous index elements.
	 */
	@Override
	public void setSelectMiscIndexElements(final boolean flag);

	/**
	 * Sets whether the collection contains notes for navigators.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllIndexElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for navigators.
	 */
	@Override
	public void setSelectNavigators(final boolean flag);

	/**
	 * Sets whether the collection contains notes for outlines.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllCodeElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for outlines.
	 */
	@Override
	public void setSelectOutlines(final boolean flag);

	/**
	 * Sets whether the collection contains notes for pages.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for pages.
	 */
	@Override
	public void setSelectPages(final boolean flag);

	/**
	 * Sets whether the collection contains profile documents.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDataNotes(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain profile documents.
	 */
	@Override
	public void setSelectProfiles(final boolean flag);

	/**
	 * Sets whether the collection contains replication formulas.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllAdminNotes(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection contains replication formulas.
	 */
	@Override
	public void setSelectReplicationFormulas(final boolean flag);

	/**
	 * Sets whether the collection contains notes for script libraries.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllCodeElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for script libraries.
	 */
	@Override
	public void setSelectScriptLibraries(final boolean flag);

	/**
	 * Sets whether the collection contains notes for shared fields.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for shared fields.
	 */
	@Override
	public void setSelectSharedFields(final boolean flag);

	/**
	 * Sets whether the collection contains notes for style sheet resources.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for style sheet resources.
	 */
	@Override
	public void setSelectStylesheetResources(final boolean flag);

	/**
	 * Sets whether the collection contains notes for subforms.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllFormatElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for subforms.
	 */
	@Override
	public void setSelectSubforms(final boolean flag);

	/**
	 * Sets whether the collection contains notes for views.
	 * <p>
	 * The following methods set this property:
	 * </p>
	 * <ul>
	 * <li>{@link Database#createNoteCollection(boolean)}</li>
	 * <li>{@link #selectAllIndexElements(boolean)}</li>
	 * <li>{@link #selectAllDesignElements(boolean)}</li>
	 * <li>{@link #selectAllNotes(boolean)}</li>
	 * </ul>
	 *
	 * @param flag
	 *            True if the collection should contain notes for views.
	 */
	@Override
	public void setSelectViews(final boolean flag);

	/**
	 * Sets the earliest note creation time in the collection.
	 * <p>
	 * {@link #buildCollection()} selects notes from the time specified by this property to the present time, excluding notes created prior
	 * to this time.
	 * </p>
	 * <p>
	 * By default this property is 12:00:00 AM without a date setting, which means that creation time is not a factor unless you explicitly
	 * set this property to a date.
	 * </p>
	 * <p>
	 * This property is not a stand-alone specification. It intersects the other selection criteria.
	 * </p>
	 *
	 * @param sinceTime
	 */
	@Override
	public void setSinceTime(final lotus.domino.DateTime sinceTime);

	/**
	 * A string representation of the instance.
	 */
	@Override
	public String toString();

}
