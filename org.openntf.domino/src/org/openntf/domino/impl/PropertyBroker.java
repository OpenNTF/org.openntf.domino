package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class PropertyBroker extends Base<org.openntf.domino.PropertyBroker, lotus.domino.PropertyBroker> implements
		org.openntf.domino.PropertyBroker {

	public PropertyBroker(lotus.domino.PropertyBroker delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public void clearProperty(String propertyName) {
		try {
			getDelegate().clearProperty(propertyName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public Vector<org.openntf.domino.NotesProperty> getInputPropertyContext() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getInputPropertyContext(), org.openntf.domino.NotesProperty.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public NotesProperty getProperty(String propertyName) {
		try {
			return Factory.fromLotus(getDelegate().getProperty(propertyName), NotesProperty.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<Object> getPropertyValue(String propertyName) {
		try {
			return getDelegate().getPropertyValue(propertyName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getPropertyValueString(String propertyName) {
		try {
			return getDelegate().getPropertyValueString(propertyName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean hasProperty(String propertyName) {
		try {
			return getDelegate().hasProperty(propertyName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public NotesProperty setPropertyValue(String propertyName, Object propertyValue) {
		try {
			return Factory.fromLotus(getDelegate().setPropertyValue(propertyName, propertyValue), NotesProperty.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return (Session) this.getParent();
	}
}
