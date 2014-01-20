package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.NotesProperty;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class PropertyBroker extends Base<org.openntf.domino.PropertyBroker, lotus.domino.PropertyBroker, Session> implements
		org.openntf.domino.PropertyBroker {

	@Deprecated
	public PropertyBroker(final lotus.domino.PropertyBroker delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getSession(parent));
	}

	/**
	 * Instantiates a new outline.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 * @param wf
	 *            the wrapperfactory
	 * @param cppId
	 *            the cpp-id
	 */
	public PropertyBroker(final lotus.domino.PropertyBroker delegate, final Session parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_PROPERTYBROKER);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected Session findParent(final lotus.domino.PropertyBroker delegate) {
		return fromLotus(Base.getSession(delegate), Session.SCHEMA, null);
	}

	@Override
	public void clearProperty(final String propertyName) {
		try {
			getDelegate().clearProperty(propertyName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public Vector<org.openntf.domino.NotesProperty> getInputPropertyContext() {
		try {
			return fromLotusAsVector(getDelegate().getInputPropertyContext(), org.openntf.domino.NotesProperty.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public NotesProperty getProperty(final String propertyName) {
		try {
			return fromLotus(getDelegate().getProperty(propertyName), NotesProperty.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.PropertyBroker#getPropertyValue(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<Object> getPropertyValue(final String propertyName) {
		try {
			return getDelegate().getPropertyValue(propertyName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.PropertyBroker#getPropertyValueString(java.lang.String)
	 */
	@Override
	public String getPropertyValueString(final String propertyName) {
		try {
			return getDelegate().getPropertyValueString(propertyName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.PropertyBroker#hasProperty(java.lang.String)
	 */
	@Override
	public boolean hasProperty(final String propertyName) {
		try {
			return getDelegate().hasProperty(propertyName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.PropertyBroker#setPropertyValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public NotesProperty setPropertyValue(final String propertyName, final Object propertyValue) {
		try {
			return fromLotus(getDelegate().setPropertyValue(propertyName, propertyValue), NotesProperty.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.PropertyBroker#publish()
	 */
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
		return getAncestor();
	}
}
