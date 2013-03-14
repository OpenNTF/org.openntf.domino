package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class DateRange extends Base<org.openntf.domino.DateRange, lotus.domino.DateRange> implements org.openntf.domino.DateRange,
		lotus.domino.DateRange {

	public DateRange(lotus.domino.DateRange delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, (parent instanceof org.openntf.domino.Session) ? parent : Factory.getSession(parent));
	}

	@Override
	public DateTime getEndDateTime() {
		try {
			return Factory.fromLotus(getDelegate().getEndDateTime(), DateTime.class, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Session getParent() {
		return (org.openntf.domino.Session) super.getParent();
	}

	@Override
	public DateTime getStartDateTime() {
		try {
			return Factory.fromLotus(getDelegate().getStartDateTime(), DateTime.class, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public String getText() {
		try {
			return getDelegate().getText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public void setEndDateTime(lotus.domino.DateTime end) {
		try {
			getDelegate().setEndDateTime(end);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}

	}

	@Override
	public void setStartDateTime(lotus.domino.DateTime start) {
		try {
			getDelegate().setStartDateTime(start);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void setText(String text) {
		try {
			getDelegate().setText(text);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

}