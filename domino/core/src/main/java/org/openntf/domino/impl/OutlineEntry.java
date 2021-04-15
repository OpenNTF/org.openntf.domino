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
package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Outline;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class OutlineEntry.
 */
public class OutlineEntry extends BaseThreadSafe<org.openntf.domino.OutlineEntry, lotus.domino.OutlineEntry, Outline>
		implements org.openntf.domino.OutlineEntry {

	/**
	 * Instantiates a new outline entry.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected OutlineEntry(final lotus.domino.OutlineEntry delegate, final Outline parent) {
		super(delegate, parent, NOTES_OUTLINEENTRY);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getAlias()
	 */
	@Override
	public String getAlias() {
		try {
			return getDelegate().getAlias();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getDatabase()
	 */
	@Override
	public Database getDatabase() {
		try {
			return fromLotus(getDelegate().getDatabase(), Database.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getDocument()
	 */
	@Override
	public Document getDocument() {
		try {
			return fromLotus(getDelegate().getDocument(), Document.SCHEMA, parent.getParentDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getEntryClass()
	 */
	@Override
	public int getEntryClass() {
		try {
			return getDelegate().getEntryClass();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getFormula()
	 */
	@Override
	public String getFormula() {
		try {
			return getDelegate().getFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getFrameText()
	 */
	@Override
	public String getFrameText() {
		try {
			return getDelegate().getFrameText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getHideFormula()
	 */
	@Override
	public String getHideFormula() {
		try {
			return getDelegate().getHideFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getImagesText()
	 */
	@Override
	public String getImagesText() {
		try {
			return getDelegate().getImagesText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getKeepSelectionFocus()
	 */
	@Override
	public boolean getKeepSelectionFocus() {
		try {
			return getDelegate().getKeepSelectionFocus();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getLabel()
	 */
	@Override
	public String getLabel() {
		try {
			return getDelegate().getLabel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getLevel()
	 */
	@Override
	public int getLevel() {
		try {
			return getDelegate().getLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getNamedElement()
	 */
	@Override
	public String getNamedElement() {
		try {
			return getDelegate().getNamedElement();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.types.Design#getNoteID()
	 */
	@Override
	public String getNoteID() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final Outline getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getType()
	 */
	@Override
	public int getType() {
		try {
			return getDelegate().getType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.types.Design#getUniversalID()
	 */
	@Override
	public String getUniversalID() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getURL()
	 */
	@Override
	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getUseHideFormula()
	 */
	@Override
	public boolean getUseHideFormula() {
		try {
			return getDelegate().getUseHideFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#getView()
	 */
	@Override
	public View getView() {
		try {
			return fromLotus(getDelegate().getView(), View.SCHEMA, getAncestorDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		try {
			return getDelegate().hasChildren();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#isHidden()
	 */
	@Override
	public boolean isHidden() {
		try {
			return getDelegate().isHidden();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#isHiddenFromNotes()
	 */
	@Override
	public boolean isHiddenFromNotes() {
		try {
			return getDelegate().isHiddenFromNotes();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#isHiddenFromWeb()
	 */
	@Override
	public boolean isHiddenFromWeb() {
		try {
			return getDelegate().isHiddenFromWeb();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#isInThisDB()
	 */
	@Override
	public boolean isInThisDB() {
		try {
			return getDelegate().isInThisDB();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#isPrivate()
	 */
	@Override
	public boolean isPrivate() {
		try {
			return getDelegate().isPrivate();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setAction(java.lang.String)
	 */
	@Override
	public boolean setAction(final String action) {
		try {
			return getDelegate().setAction(action);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setAlias(java.lang.String)
	 */
	@Override
	public void setAlias(final String alias) {
		try {
			getDelegate().setAlias(alias);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setFrameText(java.lang.String)
	 */
	@Override
	public void setFrameText(final String frameText) {
		try {
			getDelegate().setFrameText(frameText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setHidden(boolean)
	 */
	@Override
	public void setHidden(final boolean flag) {
		try {
			getDelegate().setHidden(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setHiddenFromNotes(boolean)
	 */
	@Override
	public void setHiddenFromNotes(final boolean flag) {
		try {
			getDelegate().setHiddenFromNotes(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setHiddenFromWeb(boolean)
	 */
	@Override
	public void setHiddenFromWeb(final boolean flag) {
		try {
			getDelegate().setHiddenFromWeb(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setHideFormula(java.lang.String)
	 */
	@Override
	public void setHideFormula(final String formula) {
		try {
			getDelegate().setHideFormula(formula);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setImagesText(java.lang.String)
	 */
	@Override
	public void setImagesText(final String imagesText) {
		try {
			getDelegate().setImagesText(imagesText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setKeepSelectionFocus(boolean)
	 */
	@Override
	public void setKeepSelectionFocus(final boolean flag) {
		try {
			getDelegate().setKeepSelectionFocus(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(final String label) {
		try {
			getDelegate().setLabel(label);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setNamedElement(lotus.domino.Database, java.lang.String, int)
	 */
	@Override
	public boolean setNamedElement(final lotus.domino.Database db, final String elementName, final int entryClass) {
		try {
			return getDelegate().setNamedElement(toLotus(db), elementName, entryClass);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setNoteLink(lotus.domino.Database)
	 */
	@Override
	public boolean setNoteLink(final lotus.domino.Database db) {
		try {
			return getDelegate().setNoteLink(toLotus(db));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setNoteLink(lotus.domino.Document)
	 */
	@Override
	public boolean setNoteLink(final lotus.domino.Document doc) {
		try {
			return getDelegate().setNoteLink(toLotus(doc));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setNoteLink(lotus.domino.View)
	 */
	@Override
	public boolean setNoteLink(final lotus.domino.View view) {
		try {
			return getDelegate().setNoteLink(toLotus(view));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setURL(java.lang.String)
	 */
	@Override
	public boolean setURL(final String url) {
		try {
			return getDelegate().setURL(url);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.OutlineEntry#setUseHideFormula(boolean)
	 */
	@Override
	public void setUseHideFormula(final boolean flag) {
		try {
			getDelegate().setUseHideFormula(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public final Database getAncestorDatabase() {
		return parent.getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return this.getAncestorDatabase().getAncestorSession();
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}
