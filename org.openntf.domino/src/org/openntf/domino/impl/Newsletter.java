package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class Newsletter extends Base<org.openntf.domino.Newsletter, lotus.domino.Newsletter> implements org.openntf.domino.Newsletter {

	public Newsletter(lotus.domino.Newsletter delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public Document formatDocument(lotus.domino.Database db, int index) {
		try {
			return Factory.fromLotus(getDelegate().formatDocument((lotus.domino.Database) toLotus(db), index), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Document formatMsgWithDoclinks(lotus.domino.Database db) {
		try {
			return Factory.fromLotus(getDelegate().formatMsgWithDoclinks((lotus.domino.Database) toLotus(db)), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Session getParent() {
		try {
			return Factory.fromLotus(getDelegate().getParent(), Session.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getSubjectItemName() {
		try {
			return getDelegate().getSubjectItemName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean isDoScore() {
		try {
			return getDelegate().isDoScore();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isDoSubject() {
		try {
			return getDelegate().isDoSubject();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public void setDoScore(boolean flag) {
		try {
			getDelegate().setDoScore(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setDoSubject(boolean flag) {
		try {
			getDelegate().setDoSubject(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSubjectItemName(String name) {
		try {
			getDelegate().setSubjectItemName(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
