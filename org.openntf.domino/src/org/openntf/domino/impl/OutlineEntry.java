package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class OutlineEntry extends Base<org.openntf.domino.OutlineEntry, lotus.domino.OutlineEntry> implements
		org.openntf.domino.OutlineEntry {

	public OutlineEntry(lotus.domino.OutlineEntry delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public String getAlias() {
		try {
			return getDelegate().getAlias();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Database getDatabase() {
		try {
			return Factory.fromLotus(getDelegate().getDatabase(), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Document getDocument() {
		try {
			return Factory.fromLotus(getDelegate().getDocument(), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getEntryClass() {
		try {
			return getDelegate().getEntryClass();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getFormula() {
		try {
			return getDelegate().getFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getFrameText() {
		try {
			return getDelegate().getFrameText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getHideFormula() {
		try {
			return getDelegate().getHideFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getImagesText() {
		try {
			return getDelegate().getImagesText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean getKeepSelectionFocus() {
		try {
			return getDelegate().getKeepSelectionFocus();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public String getLabel() {
		try {
			return getDelegate().getLabel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getLevel() {
		try {
			return getDelegate().getLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getNamedElement() {
		try {
			return getDelegate().getNamedElement();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Outline getParent() {
		try {
			return Factory.fromLotus(getDelegate().getParent(), Outline.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getType() {
		try {
			return getDelegate().getType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean getUseHideFormula() {
		try {
			return getDelegate().getUseHideFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public View getView() {
		try {
			return Factory.fromLotus(getDelegate().getView(), View.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean hasChildren() {
		try {
			return getDelegate().hasChildren();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isHidden() {
		try {
			return getDelegate().isHidden();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isHiddenFromNotes() {
		try {
			return getDelegate().isHiddenFromNotes();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isHiddenFromWeb() {
		try {
			return getDelegate().isHiddenFromWeb();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isInThisDB() {
		try {
			return getDelegate().isInThisDB();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isPrivate() {
		try {
			return getDelegate().isPrivate();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean setAction(String action) {
		try {
			return getDelegate().setAction(action);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public void setAlias(String alias) {
		try {
			getDelegate().setAlias(alias);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setFrameText(String frameText) {
		try {
			getDelegate().setFrameText(frameText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHidden(boolean flag) {
		try {
			getDelegate().setHidden(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHiddenFromNotes(boolean flag) {
		try {
			getDelegate().setHiddenFromNotes(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHiddenFromWeb(boolean flag) {
		try {
			getDelegate().setHiddenFromWeb(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setHideFormula(String formula) {
		try {
			getDelegate().setHideFormula(formula);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setImagesText(String imagesText) {
		try {
			getDelegate().setImagesText(imagesText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setKeepSelectionFocus(boolean flag) {
		try {
			getDelegate().setKeepSelectionFocus(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setLabel(String label) {
		try {
			getDelegate().setLabel(label);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public boolean setNamedElement(lotus.domino.Database db, String elementName, int entryClass) {
		try {
			return getDelegate().setNamedElement((lotus.domino.Database) toLotus(db), elementName, entryClass);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean setNoteLink(lotus.domino.Database db) {
		try {
			return getDelegate().setNoteLink((lotus.domino.Database) toLotus(db));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean setNoteLink(lotus.domino.Document doc) {
		try {
			return getDelegate().setNoteLink((lotus.domino.Document) toLotus(doc));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean setNoteLink(lotus.domino.View view) {
		try {
			return getDelegate().setNoteLink((lotus.domino.View) toLotus(view));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean setURL(String url) {
		try {
			return getDelegate().setURL(url);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public void setUseHideFormula(boolean flag) {
		try {
			getDelegate().setUseHideFormula(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
