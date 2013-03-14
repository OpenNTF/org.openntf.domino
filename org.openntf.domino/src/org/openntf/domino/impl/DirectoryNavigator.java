package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public class DirectoryNavigator extends Base<org.openntf.domino.DirectoryNavigator, lotus.domino.DirectoryNavigator> implements
		org.openntf.domino.DirectoryNavigator {

	protected DirectoryNavigator(lotus.domino.DirectoryNavigator delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public boolean findFirstMatch() {
		try {
			return getDelegate().findFirstMatch();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public long findFirstName() {
		try {
			return getDelegate().findFirstName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public boolean findNextMatch() {
		try {
			return getDelegate().findNextMatch();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public long findNextName() {
		try {
			return getDelegate().findNextName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public boolean findNthMatch(long n) {
		try {
			return getDelegate().findNthMatch(n);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public long findNthName(int n) {
		try {
			return getDelegate().findNthName(n);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getCurrentItem() {
		try {
			return getDelegate().getCurrentItem();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public long getCurrentMatch() {
		try {
			return getDelegate().getCurrentMatch();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public long getCurrentMatches() {
		try {
			return getDelegate().getCurrentMatches();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public String getCurrentName() {
		try {
			return getDelegate().getCurrentName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getCurrentView() {
		try {
			return getDelegate().getCurrentView();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<Object> getFirstItemValue() {
		try {
			return getDelegate().getFirstItemValue();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<Object> getNextItemValue() {
		try {
			return getDelegate().getNextItemValue();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<Object> getNthItemValue(int n) {
		try {
			return getDelegate().getNthItemValue(n);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean isMatchLocated() {
		try {
			return getDelegate().isMatchLocated();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isNameLocated() {
		try {
			return getDelegate().isNameLocated();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

}
