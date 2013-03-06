package org.openntf.domino.impl;

import lotus.domino.DateTime;
import lotus.domino.NotesException;
import lotus.domino.Session;

public class DateRange extends Base<org.openntf.domino.DateRange, lotus.domino.DateRange> implements org.openntf.domino.DateRange,
		lotus.domino.DateRange {

	public DateRange() {

	}

	public DateRange(lotus.domino.DateRange delegate) {
		super(delegate);
	}

	@Override
	public DateTime getEndDateTime() throws NotesException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session getParent() throws NotesException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DateTime getStartDateTime() throws NotesException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() throws NotesException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEndDateTime(DateTime arg0) throws NotesException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStartDateTime(DateTime arg0) throws NotesException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setText(String arg0) throws NotesException {
		// TODO Auto-generated method stub

	}

}
