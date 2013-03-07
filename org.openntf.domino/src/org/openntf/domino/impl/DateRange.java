package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class DateRange extends Base<org.openntf.domino.DateRange, lotus.domino.DateRange> implements org.openntf.domino.DateRange,
		lotus.domino.DateRange {

	public DateRange(lotus.domino.DateRange delegate) {
		super(delegate);
	}

	@Override
	public DateTime getEndDateTime() {
		try {
			return Factory.fromLotus(getDelegate().getEndDateTime(), DateTime.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public Session getParent() {
		try {
			return Factory.fromLotus(getDelegate().getParent(), Session.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Override
	public DateTime getStartDateTime() {
		try {
			return Factory.fromLotus(getDelegate().getStartDateTime(), DateTime.class);
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
	public void setEndDateTime(lotus.domino.DateTime arg0) {
		try {
			getDelegate().setEndDateTime(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}

	}

	@Override
	public void setStartDateTime(lotus.domino.DateTime arg0) {
		try {
			getDelegate().setStartDateTime(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	@Override
	public void setText(String arg0) {
		try {
			getDelegate().setText(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

}
