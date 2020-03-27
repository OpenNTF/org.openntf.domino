/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.NotesProperty;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

public class PropertyBroker extends BaseThreadSafe<org.openntf.domino.PropertyBroker, lotus.domino.PropertyBroker, Session> implements
org.openntf.domino.PropertyBroker {

	/**
	 * Instantiates a new outline.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected PropertyBroker(final lotus.domino.PropertyBroker delegate, final Session parent) {
		super(delegate, parent, NOTES_PROPERTYBROKER);
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
	public final Session getAncestorSession() {
		return parent;
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getFactory();
	}

}
