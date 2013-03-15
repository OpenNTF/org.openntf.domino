package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class RichTextParagraphStyle extends Base<org.openntf.domino.RichTextParagraphStyle, lotus.domino.RichTextParagraphStyle> implements
		org.openntf.domino.RichTextParagraphStyle {

	public RichTextParagraphStyle(lotus.domino.RichTextParagraphStyle delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public void clearAllTabs() {
		try {
			getDelegate().clearAllTabs();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public int getAlignment() {
		try {
			return getDelegate().getAlignment();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getFirstLineLeftMargin() {
		try {
			return getDelegate().getFirstLineLeftMargin();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getInterLineSpacing() {
		try {
			return getDelegate().getInterLineSpacing();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getLeftMargin() {
		try {
			return getDelegate().getLeftMargin();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getPagination() {
		try {
			return getDelegate().getPagination();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getRightMargin() {
		try {
			return getDelegate().getRightMargin();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getSpacingAbove() {
		try {
			return getDelegate().getSpacingAbove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getSpacingBelow() {
		try {
			return getDelegate().getSpacingBelow();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public Vector<org.openntf.domino.RichTextTab> getTabs() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getTabs(), org.openntf.domino.RichTextTab.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void setAlignment(int value) {
		try {
			getDelegate().setAlignment(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setFirstLineLeftMargin(int value) {
		try {
			getDelegate().setFirstLineLeftMargin(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setInterLineSpacing(int value) {
		try {
			getDelegate().setInterLineSpacing(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setLeftMargin(int value) {
		try {
			getDelegate().setLeftMargin(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPagination(int value) {
		try {
			getDelegate().setPagination(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setRightMargin(int value) {
		try {
			getDelegate().setRightMargin(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSpacingAbove(int value) {
		try {
			getDelegate().setSpacingAbove(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSpacingBelow(int value) {
		try {
			getDelegate().setSpacingBelow(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setTab(int position, int type) {
		try {
			getDelegate().setTab(position, type);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setTabs(int count, int startPos, int interval) {
		try {
			getDelegate().setTabs(count, startPos, interval);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setTabs(int count, int startPos, int interval, int type) {
		try {
			getDelegate().setTabs(count, startPos, interval, type);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
