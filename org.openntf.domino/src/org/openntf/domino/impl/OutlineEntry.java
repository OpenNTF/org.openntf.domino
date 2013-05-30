/*
 * Copyright OpenNTF 2013
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
package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class OutlineEntry.
 */
public class OutlineEntry extends Base<org.openntf.domino.OutlineEntry, lotus.domino.OutlineEntry> implements
		org.openntf.domino.OutlineEntry {

	/**
	 * Instantiates a new outline entry.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public OutlineEntry(lotus.domino.OutlineEntry delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
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
			return Factory.fromLotus(getDelegate().getDatabase(), Database.class, this);
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
			return Factory.fromLotus(getDelegate().getDocument(), Document.class, this);
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
	public Outline getParent() {
		return (Outline) super.getParent();
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
			return Factory.fromLotus(getDelegate().getView(), View.class, this);
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
	public boolean setAction(String action) {
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
	public void setAlias(String alias) {
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
	public void setFrameText(String frameText) {
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
	public void setHidden(boolean flag) {
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
	public void setHiddenFromNotes(boolean flag) {
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
	public void setHiddenFromWeb(boolean flag) {
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
	public void setHideFormula(String formula) {
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
	public void setImagesText(String imagesText) {
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
	public void setKeepSelectionFocus(boolean flag) {
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
	public void setLabel(String label) {
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
	public boolean setNamedElement(lotus.domino.Database db, String elementName, int entryClass) {
		try {
			return getDelegate().setNamedElement((lotus.domino.Database) toLotus(db), elementName, entryClass);
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
	public boolean setNoteLink(lotus.domino.Database db) {
		try {
			return getDelegate().setNoteLink((lotus.domino.Database) toLotus(db));
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
	public boolean setNoteLink(lotus.domino.Document doc) {
		try {
			return getDelegate().setNoteLink((lotus.domino.Document) toLotus(doc));
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
	public boolean setNoteLink(lotus.domino.View view) {
		try {
			return getDelegate().setNoteLink((lotus.domino.View) toLotus(view));
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
	public boolean setURL(String url) {
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
	public void setUseHideFormula(boolean flag) {
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
	public Database getAncestorDatabase() {
		return this.getParent().getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getAncestorDatabase().getAncestorSession();
	}

}
