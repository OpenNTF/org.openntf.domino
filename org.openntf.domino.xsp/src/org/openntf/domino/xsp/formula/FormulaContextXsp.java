package org.openntf.domino.xsp.formula;

import java.util.logging.Logger;

import javax.faces.component.UIComponent;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.formula.FormulaContextNotes;
import org.openntf.domino.utils.Factory;

import com.ibm.xsp.model.domino.wrapped.DominoDocument;

public class FormulaContextXsp extends FormulaContextNotes {
	private static final Logger log_ = Logger.getLogger(FormulaContextNotes.class.getName());
	UIComponent component;

	@Override
	public Database getDatabase() {
		if (dataMap instanceof DominoDocument) {
			getDocument().getAncestorDatabase();
		}
		return super.getDatabase();
	}

	@Override
	public Document getDocument() {
		if (dataMap instanceof DominoDocument) {
			return Factory.fromLotus(getXspDocument().getDocument(), Document.SCHEMA, null);
		}
		return super.getDocument();
	}

	public DominoDocument getXspDocument() {
		if (dataMap instanceof DominoDocument) {
			return (DominoDocument) dataMap;
		}
		return null;
	}

	public void setComponent(final UIComponent component) {
		this.component = component;

	}

	public UIComponent getComponent() {
		return component;
	}
}
