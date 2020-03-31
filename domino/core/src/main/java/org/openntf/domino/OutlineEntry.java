/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino;

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.Design;
import org.openntf.domino.types.FactorySchema;

/**
 * Represents an entry in an outline.
 */
public interface OutlineEntry extends Base<lotus.domino.OutlineEntry>, lotus.domino.OutlineEntry, org.openntf.domino.ext.OutlineEntry,
		Design, DatabaseDescendant {

	public static class Schema extends FactorySchema<OutlineEntry, lotus.domino.OutlineEntry, Outline> {
		@Override
		public Class<OutlineEntry> typeClass() {
			return OutlineEntry.class;
		}

		@Override
		public Class<lotus.domino.OutlineEntry> delegateClass() {
			return lotus.domino.OutlineEntry.class;
		}

		@Override
		public Class<Outline> parentClass() {
			return Outline.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/**
	 * Name of an outline entry for programmatic access.
	 */
	@Override
	public String getAlias();

	/**
	 * Database that is the resource link for an outline entry, or null.
	 * <p>
	 * This property applies if the outline entry type is OUTLINE_TYPE_NOTELINK or OUTLINE_TYPE_NAMEDELEMENT and the entry class is
	 * OUTLINE_CLASS_DATABASE, OUTLINE_CLASS_DOCUMENT, or OUTLINE_CLASS_VIEW.
	 * </p>
	 *
	 * @return Database that is the resource link for an outline entry, or null.
	 */
	@Override
	public Database getDatabase();

	/**
	 * Document that is the resource link for an outline entry, or null.
	 * <p>
	 * This property applies if the outline entry type is OUTLINE_TYPE_NOTELINK and the entry class is OUTLINE_CLASS_DOCUMENT.
	 * </p>
	 *
	 * @return Document that is the resource link for an outline entry, or null.
	 */
	@Override
	public Document getDocument();

	/**
	 * Class of the outline entry.
	 *
	 * @return One of
	 *         <ul>
	 *         <li>OutlineEntry.OUTLINE_CLASS_DATABASE</li>
	 *         <li>OutlineEntry.OUTLINE_CLASS_DOCUMENT</li>
	 *         <li>OutlineEntry.OUTLINE_CLASS_FORM</li>
	 *         <li>OutlineEntry.OUTLINE_CLASS_FOLDER</li>
	 *         <li>OutlineEntry.OUTLINE_CLASS_FRAMESET</li>
	 *         <li>OutlineEntry.OUTLINE_CLASS_NAVIGATOR</li>
	 *         <li>OutlineEntry.OUTLINE_CLASS_PAGE</li>
	 *         <li>OutlineEntry.OUTLINE_CLASS_UNKNOWN</li>
	 *         <li>OutlineEntry.OUTLINE_CLASS_VIEW</li>
	 *         </ul>
	 */
	@Override
	public int getEntryClass();

	/**
	 * Formula of an action outline, or an empty string.
	 * <p>
	 * This property applies if the outline entry type is OUTLINE_TYPE_ACTION.
	 * </p>
	 *
	 * @return Formula of an action outline, or an empty string.
	 */
	@Override
	public String getFormula();

	/**
	 * The name of the target frame for the entry's OnClick event.
	 *
	 */
	@Override
	public String getFrameText();

	/**
	 * A formula that determines when an entry is hidden, or an empty string.
	 * <p>
	 * The hide formula must be a valid Notes formula that evaluates to a boolean value.
	 * </p>
	 * <p>
	 * {@link #getUseHideFormula()} determines if the hide formula is used.
	 * </p>
	 *
	 * @return A formula that determines when an entry is hidden, or an empty string.
	 */
	@Override
	public String getHideFormula();

	/**
	 * The name of the image file used to add an icon to an entry.
	 *
	 */
	@Override
	public String getImagesText();

	/**
	 * Indicates whether an entry keeps selection focus.
	 *
	 */
	@Override
	public boolean getKeepSelectionFocus();

	/**
	 * Label (display text) for an entry.
	 *
	 */
	@Override
	public String getLabel();

	/**
	 * The level of this entry (0 is the top level).
	 *
	 */
	@Override
	public int getLevel();

	/**
	 * Named element referenced by an outline entry, or an empty string.
	 * <p>
	 * This property applies if the outline entry type is OUTLINE_TYPE_NAMEDELEMENT.
	 * </p>
	 *
	 * @return Named element referenced by an outline entry, or an empty string.
	 */
	@Override
	public String getNamedElement();

	/**
	 * The parent outline of an outline entry.
	 *
	 */
	@Override
	public Outline getParent();

	/**
	 * Type of outline entry.
	 *
	 * @return one of:
	 *         <ul>
	 *         <li>OutlineEntry.OUTLINE_OTHER_FOLDERS_TYPE</li>
	 *         <li>OutlineEntry.OUTLINE_OTHER_UNKNOWN_TYPE</li>
	 *         <li>OutlineEntry.OUTLINE_OTHER_VIEWS_TYPE</li>
	 *         <li>OutlineEntry.OUTLINE_TYPE_ACTION</li>
	 *         <li>OutlineEntry.OUTLINE_TYPE_NAMEDELEMENT</li>
	 *         <li>OutlineEntry.OUTLINE_TYPE_NOTELINK</li>
	 *         <li>OutlineEntry.OUTLINE_TYPE_URL</li>
	 *         </ul>
	 */
	@Override
	public int getType();

	/**
	 * URL of an outline entry, or an empty string.
	 * <p>
	 * This property applies if the outline entry type is OUTLINE_TYPE_URL.
	 * </p>
	 *
	 * @return URL of an outline entry, or an empty string.
	 */
	@Override
	public String getURL();

	/**
	 * Indicates whether an entry is hidden when the hide formula is true.
	 * <p>
	 * See {@link #getHideFormula()} for the text of the hide formula.
	 * </p>
	 *
	 * @return Indicates whether an entry is hidden when the hide formula is true.
	 */
	@Override
	public boolean getUseHideFormula();

	/**
	 * View that is the resource link for an outline entry, or null.
	 * <p>
	 * This property applies if the outline entry type is OUTLINE_TYPE_NOTELINK and the entry class is OUTLINE_CLASS_DOCUMENT or
	 * OUTLINE_CLASS_VIEW.
	 * </p>
	 *
	 * @return View that is the resource link for an outline entry, or null.
	 */
	@Override
	public View getView();

	/**
	 * Indicates whether an entry contains child entries.
	 *
	 * @return true if the entry contains child entries, false if not
	 */
	@Override
	public boolean hasChildren();

	/**
	 * Indicates whether an entry is hidden in the current user context (read) or hides the entry in all contexts (write).
	 * <p>
	 * The current user context can be the Notes client or a Web browser.
	 * </p>
	 * <p>
	 * To read and write the hide properties with precision, see {@link #isHiddenFromNotes()}, {@link #isHiddenFromWeb()},
	 * {@link #getHideFormula()}, and {@link #getUseHideFormula()}.
	 * </p>
	 *
	 * @return Indicates whether an entry is hidden in the current user context (read) or hides the entry in all contexts (write).
	 */
	@Override
	public boolean isHidden();

	/**
	 * Indicates whether an entry is hidden in the context of the Notes client.
	 */
	@Override
	public boolean isHiddenFromNotes();

	/**
	 * Indicates whether an entry is hidden in the context of a Web browser.
	 */
	@Override
	public boolean isHiddenFromWeb();

	/**
	 * Indicates whether an entry refers to an element in the current database.
	 *
	 */
	@Override
	public boolean isInThisDB();

	/**
	 * Indicates whether an entry is specific to an individual.
	 */
	@Override
	public boolean isPrivate();

	/**
	 * Sets a formula for an action outline entry.
	 * <p>
	 * This method sets the outline entry type to OUTLINE_TYPE_ACTION.
	 * </p>
	 *
	 * @param formula
	 *            A Domino formula.
	 * @return true if the action is set successfully.
	 */
	@Override
	public boolean setAction(final String action);

	/**
	 * Sets the name of an outline entry for programmatic access.
	 *
	 * @param alias
	 *            New name for programmatic access.
	 */
	@Override
	public void setAlias(final String alias);

	/**
	 * The name of the target frame for the entry's OnClick event.
	 *
	 * @param frameText
	 *            the new name of the target fram
	 */
	@Override
	public void setFrameText(final String frameText);

	/**
	 * Sets whether an entry is hidden in the current user context (read) or hides the entry in all contexts (write).
	 * <p>
	 * The current user context can be the Notes client or a Web browser.
	 * </p>
	 * <p>
	 * To read and write the hide properties with precision, see {@link #isHiddenFromNotes()}, {@link #isHiddenFromWeb()},
	 * </p>
	 *
	 */
	@Override
	public void setHidden(final boolean flag);

	/**
	 * Sets whether an entry is hidden in the context of the Notes client.
	 *
	 * @param flag
	 *            true if the entry should be hidden in the Notes client
	 */
	@Override
	public void setHiddenFromNotes(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.OutlineEntry#setHiddenFromWeb(boolean)
	 */
	@Override
	public void setHiddenFromWeb(final boolean flag);

	/**
	 * Sets a formula that determines when an entry is hidden, or an empty string.
	 * <p>
	 * The hide formula must be a valid Notes formula that evaluates to a boolean value.
	 * </p>
	 * <p>
	 * {@link #setUseHideFormula(boolean)} sets if the hide formula is used.
	 * </p>
	 *
	 */
	@Override
	public void setHideFormula(final String formula);

	/**
	 * Sets the name of the image file used to add an icon to an entry.
	 *
	 * @param imagesText
	 *            the new name of the image file
	 */
	@Override
	public void setImagesText(final String imagesText);

	/**
	 * Sets whether an entry keeps selection focus.
	 *
	 * @param flag
	 *            true if the entry keeps selection focus
	 */
	@Override
	public void setKeepSelectionFocus(final boolean flag);

	/**
	 * Sets the label (display text) for an entry.
	 *
	 * @param label
	 *            the new label
	 */
	@Override
	public void setLabel(final String label);

	/**
	 * Specifies a named element for an outline entry.
	 * <p>
	 * This method sets the outline entry {@link #getType()} to OUTLINE_TYPE_NAMEDELEMENT and the entry class as specified.
	 * </p>
	 *
	 * @param db
	 *            The database containing the named element.
	 * @param elementName
	 *            The name of a database element.
	 * @param entryClass
	 *            The entry class to be assigned to the database element. See {@link #getEntryClass()} for the legal values.
	 * @return true if the named element is set successfully.
	 */
	@Override
	public boolean setNamedElement(final lotus.domino.Database db, final String elementName, final int entryClass);

	/**
	 * Specifies a resource link for an outline entry.
	 * <p>
	 * This method sets the outline entry type to OUTLINE_TYPE_NOTELINK and the entry class to OUTLINE_CLASS_DATABASE.
	 * </p>
	 *
	 * @param db
	 *            A database that is the resource link.
	 * @return true if the link is set successfully.
	 */
	@Override
	public boolean setNoteLink(final lotus.domino.Database db);

	/**
	 * Specifies a resource link for an outline entry.
	 * <p>
	 * This method sets the outline entry type to OUTLINE_TYPE_NOTELINK and the entry class to OUTLINE_CLASS_DOCUMENT.
	 * </p>
	 *
	 * @param doc
	 *            A document that is a resource link.
	 * @return true if the link is set successfully.
	 */
	@Override
	public boolean setNoteLink(final lotus.domino.Document doc);

	/**
	 * Specifies a resource link for an outline entry.
	 * <p>
	 * This method sets the outline entry type to OUTLINE_TYPE_NOTELINK and the entry class to OUTLINE_CLASS_VIEW.
	 * </p>
	 *
	 * @param view
	 *            A view that is a resource link.
	 * @return true if the link is set successfully.
	 */
	@Override
	public boolean setNoteLink(final lotus.domino.View view);

	/**
	 * Sets a URL as the resource link for an outline entry.
	 * <p>
	 * This method sets the outline entry type to OUTLINE_TYPE_URL.
	 * </p>
	 *
	 * @param url
	 *            A URL.
	 * @return true if the URL is set successfully.
	 */
	@Override
	public boolean setURL(final String url);

	/**
	 * Sets whether an entry is hidden when the hide formula is true.
	 * <p>
	 * See {@link #getHideFormula()} for the text of the hide formula.
	 * </p>
	 *
	 */
	@Override
	public void setUseHideFormula(final boolean flag);
}
