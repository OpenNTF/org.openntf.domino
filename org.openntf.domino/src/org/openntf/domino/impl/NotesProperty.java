package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class NotesProperty extends Base<org.openntf.domino.NotesProperty, lotus.domino.NotesProperty> implements
		org.openntf.domino.NotesProperty {

	public NotesProperty(lotus.domino.NotesProperty delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public void clear() {
		try {
			getDelegate().clear();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public String getDescription() {
		try {
			return getDelegate().getDescription();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getNamespace() {
		try {
			return getDelegate().getNamespace();
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
	public String getTypeName() {
		try {
			return getDelegate().getTypeName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getValueString() {
		try {
			return getDelegate().getValueString();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector<Object> getValues() {
		try {
			// Does this even use DateTime? Who knows?
			return Factory.wrapColumnValues(getDelegate().getValues());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean isInput() {
		try {
			return getDelegate().isInput();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public void publish() {
		try {
			getDelegate().publish();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
