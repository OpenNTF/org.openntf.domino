package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class RichTextSection extends Base<org.openntf.domino.RichTextSection, lotus.domino.RichTextSection> implements
		org.openntf.domino.RichTextSection {

	public RichTextSection(lotus.domino.RichTextSection delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public ColorObject getBarColor() {
		try {
			return Factory.fromLotus(getDelegate().getBarColor(), ColorObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getTitle() {
		try {
			return getDelegate().getTitle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public RichTextStyle getTitleStyle() {
		try {
			return Factory.fromLotus(getDelegate().getTitleStyle(), RichTextStyle.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean isExpanded() {
		try {
			return getDelegate().isExpanded();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
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
	public void setBarColor(lotus.domino.ColorObject color) {
		try {
			getDelegate().setBarColor((lotus.domino.ColorObject) toLotus(color));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setExpanded(boolean flag) {
		try {
			getDelegate().setExpanded(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setTitle(String title) {
		try {
			getDelegate().setTitle(title);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setTitleStyle(lotus.domino.RichTextStyle style) {
		try {
			getDelegate().setTitleStyle((lotus.domino.RichTextStyle) toLotus(style));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
