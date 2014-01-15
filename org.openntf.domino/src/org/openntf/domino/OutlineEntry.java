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
 * The Interface OutlineEntry.
 */
public interface OutlineEntry extends Base<lotus.domino.OutlineEntry>, lotus.domino.OutlineEntry, org.openntf.domino.ext.OutlineEntry,
		Design, DatabaseDescendant {

	public static class Schema extends FactorySchema<ColorObject, lotus.domino.ColorObject, Outline> {
		@Override
		public Class<ColorObject> typeClass() {
			return ColorObject.class;
		}

		@Override
		public Class<lotus.domino.ColorObject> delegateClass() {
			return lotus.domino.ColorObject.class;
		}

		@Override
		public Class<Outline> parentClass() {
			return Outline.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getAlias()
	 */
	@Override
	public String getAlias();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getDatabase()
	 */
	@Override
	public Database getDatabase();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getDocument()
	 */
	@Override
	public Document getDocument();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getEntryClass()
	 */
	@Override
	public int getEntryClass();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getFormula()
	 */
	@Override
	public String getFormula();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getFrameText()
	 */
	@Override
	public String getFrameText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getHideFormula()
	 */
	@Override
	public String getHideFormula();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getImagesText()
	 */
	@Override
	public String getImagesText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getKeepSelectionFocus()
	 */
	@Override
	public boolean getKeepSelectionFocus();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getLabel()
	 */
	@Override
	public String getLabel();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getLevel()
	 */
	@Override
	public int getLevel();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getNamedElement()
	 */
	@Override
	public String getNamedElement();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getParent()
	 */
	@Override
	public Outline getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getType()
	 */
	@Override
	public int getType();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getURL()
	 */
	@Override
	public String getURL();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getUseHideFormula()
	 */
	@Override
	public boolean getUseHideFormula();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#getView()
	 */
	@Override
	public View getView();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#hasChildren()
	 */
	@Override
	public boolean hasChildren();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#isHidden()
	 */
	@Override
	public boolean isHidden();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#isHiddenFromNotes()
	 */
	@Override
	public boolean isHiddenFromNotes();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#isHiddenFromWeb()
	 */
	@Override
	public boolean isHiddenFromWeb();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#isInThisDB()
	 */
	@Override
	public boolean isInThisDB();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#isPrivate()
	 */
	@Override
	public boolean isPrivate();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setAction(java.lang.String)
	 */
	@Override
	public boolean setAction(final String action);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setAlias(java.lang.String)
	 */
	@Override
	public void setAlias(final String alias);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setFrameText(java.lang.String)
	 */
	@Override
	public void setFrameText(final String frameText);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setHidden(boolean)
	 */
	@Override
	public void setHidden(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setHiddenFromNotes(boolean)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setHideFormula(java.lang.String)
	 */
	@Override
	public void setHideFormula(final String formula);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setImagesText(java.lang.String)
	 */
	@Override
	public void setImagesText(final String imagesText);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setKeepSelectionFocus(boolean)
	 */
	@Override
	public void setKeepSelectionFocus(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(final String label);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setNamedElement(lotus.domino.Database, java.lang.String, int)
	 */
	@Override
	public boolean setNamedElement(final lotus.domino.Database db, final String elementName, final int entryClass);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setNoteLink(lotus.domino.Database)
	 */
	@Override
	public boolean setNoteLink(final lotus.domino.Database db);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setNoteLink(lotus.domino.Document)
	 */
	@Override
	public boolean setNoteLink(final lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setNoteLink(lotus.domino.View)
	 */
	@Override
	public boolean setNoteLink(final lotus.domino.View view);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setURL(java.lang.String)
	 */
	@Override
	public boolean setURL(final String url);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.OutlineEntry#setUseHideFormula(boolean)
	 */
	@Override
	public void setUseHideFormula(final boolean flag);
}
