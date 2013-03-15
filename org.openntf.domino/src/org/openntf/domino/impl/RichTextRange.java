package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class RichTextRange extends Base<org.openntf.domino.RichTextRange, lotus.domino.RichTextRange> implements
		org.openntf.domino.RichTextRange {

	public RichTextRange(lotus.domino.RichTextRange delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public RichTextRange Clone() {
		try {
			return Factory.fromLotus(getDelegate().Clone(), RichTextRange.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int findandReplace(String target, String replacement) {
		try {
			return getDelegate().findandReplace(target, replacement);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int findandReplace(String target, String replacement, long options) {
		try {
			return getDelegate().findandReplace(target, replacement, options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public RichTextNavigator getNavigator() {
		try {
			return Factory.fromLotus(getDelegate().getNavigator(), RichTextNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public RichTextStyle getStyle() {
		try {
			return Factory.fromLotus(getDelegate().getStyle(), RichTextStyle.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getTextParagraph() {
		try {
			return getDelegate().getTextParagraph();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getTextRun() {
		try {
			return getDelegate().getTextRun();
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
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void reset(boolean begin, boolean end) {
		try {
			getDelegate().reset(begin, end);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setBegin(lotus.domino.Base element) {
		try {
			getDelegate().setBegin(toLotus(element));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setEnd(lotus.domino.Base element) {
		try {
			getDelegate().setEnd(toLotus(element));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setStyle(lotus.domino.RichTextStyle style) {
		try {
			getDelegate().setStyle((lotus.domino.RichTextStyle) toLotus(style));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
