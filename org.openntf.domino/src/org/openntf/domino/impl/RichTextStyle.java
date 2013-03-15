package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class RichTextStyle extends Base<org.openntf.domino.RichTextStyle, lotus.domino.RichTextStyle> implements
		org.openntf.domino.RichTextStyle {

	public RichTextStyle(lotus.domino.RichTextStyle delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public int getBold() {
		try {
			return getDelegate().getBold();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	@Override
	public int getColor() {
		try {
			return getDelegate().getColor();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	@Override
	public int getEffects() {
		try {
			return getDelegate().getEffects();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	@Override
	public int getFont() {
		try {
			return getDelegate().getFont();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	@Override
	public int getFontSize() {
		try {
			return getDelegate().getFontSize();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	@Override
	public int getItalic() {
		try {
			return getDelegate().getItalic();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	@Override
	public Session getParent() {
		try {
			return Factory.fromLotus(getDelegate().getParent(), Session.class, this);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	@Override
	public int getPassThruHTML() {
		try {
			return getDelegate().getPassThruHTML();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	@Override
	public int getStrikeThrough() {
		try {
			return getDelegate().getStrikeThrough();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	@Override
	public int getUnderline() {
		try {
			return getDelegate().getUnderline();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	@Override
	public boolean isDefault() {
		try {
			return getDelegate().isDefault();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}

	@Override
	public void setBold(int value) {
		try {
			getDelegate().setBold(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	@Override
	public void setColor(int value) {
		try {
			getDelegate().setColor(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	@Override
	public void setEffects(int value) {
		try {
			getDelegate().setEffects(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	@Override
	public void setFont(int value) {
		try {
			getDelegate().setFont(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	@Override
	public void setFontSize(int value) {
		try {
			getDelegate().setFontSize(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	@Override
	public void setItalic(int value) {
		try {
			getDelegate().setItalic(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	@Override
	public void setPassThruHTML(int value) {
		try {
			getDelegate().setPassThruHTML(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	@Override
	public void setStrikeThrough(int value) {
		try {
			getDelegate().setStrikeThrough(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	@Override
	public void setUnderline(int value) {
		try {
			getDelegate().setUnderline(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}
}
