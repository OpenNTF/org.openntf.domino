/*
 * Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.PropertyBroker;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class NotesProperty.
 */
public class NotesProperty extends Base<org.openntf.domino.NotesProperty, lotus.domino.NotesProperty, PropertyBroker> implements
		org.openntf.domino.NotesProperty {

	/**
	 * Instantiates a new notes property.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public NotesProperty(final lotus.domino.NotesProperty delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, (PropertyBroker) parent);
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
	public NotesProperty(final lotus.domino.NotesProperty delegate, final PropertyBroker parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_OUTLINE);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected PropertyBroker findParent(final lotus.domino.NotesProperty delegate) throws NotesException {
		throw new IllegalArgumentException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NotesProperty#clear()
	 */
	@Override
	public void clear() {
		try {
			getDelegate().clear();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NotesProperty#getDescription()
	 */
	@Override
	public String getDescription() {
		try {
			return getDelegate().getDescription();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NotesProperty#getName()
	 */
	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NotesProperty#getNamespace()
	 */
	@Override
	public String getNamespace() {
		try {
			return getDelegate().getNamespace();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NotesProperty#getTitle()
	 */
	@Override
	public String getTitle() {
		try {
			return getDelegate().getTitle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NotesProperty#getTypeName()
	 */
	@Override
	public String getTypeName() {
		try {
			return getDelegate().getTypeName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NotesProperty#getValueString()
	 */
	@Override
	public String getValueString() {
		try {
			return getDelegate().getValueString();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NotesProperty#getValues()
	 */
	@Override
	public Vector<Object> getValues() {
		try {
			// Does this even use DateTime? Who knows?
			return Factory.wrapColumnValues(getDelegate().getValues(), this.getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NotesProperty#isInput()
	 */
	@Override
	public boolean isInput() {
		try {
			return getDelegate().isInput();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.NotesProperty#publish()
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
		return getAncestor().getAncestorSession();
	}
}
