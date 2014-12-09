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

import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextNavigator.
 */
public class RichTextNavigator extends
		BaseNonThreadSafe<org.openntf.domino.RichTextNavigator, lotus.domino.RichTextNavigator, RichTextItem> implements
		org.openntf.domino.RichTextNavigator {

	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(RichTextNavigator.class.getName());

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
	public RichTextNavigator(final lotus.domino.RichTextNavigator delegate, final RichTextItem parent, final WrapperFactory wf,
			final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_RTNAVIGATOR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#Clone()
	 */
	@Override
	public org.openntf.domino.RichTextNavigator Clone() {
		try {
			return fromLotus(getDelegate().Clone(), RichTextNavigator.SCHEMA, parent);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#findFirstElement(int)
	 */
	@Override
	public boolean findFirstElement(final int type) {
		try {
			return getDelegate().findFirstElement(type);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#findFirstString(java.lang.String)
	 */
	@Override
	public boolean findFirstString(final String target) {
		try {
			return getDelegate().findFirstString(target);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#findFirstString(java.lang.String, int)
	 */
	@Override
	public boolean findFirstString(final String target, final int options) {
		try {
			return getDelegate().findFirstString(target, options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#findLastElement(int)
	 */
	@Override
	public boolean findLastElement(final int type) {
		try {
			return getDelegate().findLastElement(type);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#findNextElement()
	 */
	@Override
	public boolean findNextElement() {
		try {
			return getDelegate().findNextElement();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#findNextElement(int)
	 */
	@Override
	public boolean findNextElement(final int type) {
		try {
			return getDelegate().findNextElement(type);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#findNextElement(int, int)
	 */
	@Override
	public boolean findNextElement(final int type, final int occurrence) {
		try {
			return getDelegate().findNextElement(type, occurrence);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#findNextString(java.lang.String)
	 */
	@Override
	public boolean findNextString(final String target) {
		try {
			return getDelegate().findNextString(target);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#findNextString(java.lang.String, int)
	 */
	@Override
	public boolean findNextString(final String target, final int options) {
		try {
			return getDelegate().findNextString(target, options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#findNthElement(int, int)
	 */
	@Override
	public boolean findNthElement(final int type, final int occurrence) {
		try {
			return getDelegate().findNthElement(type, occurrence);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#getElement()
	 */
	@Override
	public org.openntf.domino.Base<?> getElement() {
		try {
			return fromLotus(getDelegate().getElement(), null, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#getFirstElement(int)
	 */
	@Override
	public org.openntf.domino.Base<?> getFirstElement(final int type) {
		try {
			return fromLotus(getDelegate().getFirstElement(type), null, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#getLastElement(int)
	 */
	@Override
	public org.openntf.domino.Base<?> getLastElement(final int type) {
		try {
			return fromLotus(getDelegate().getLastElement(type), null, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#getNextElement()
	 */
	@Override
	public org.openntf.domino.Base<?> getNextElement() {
		try {
			return fromLotus(getDelegate().getNextElement(), null, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#getNextElement(int)
	 */
	@Override
	public org.openntf.domino.Base<?> getNextElement(final int type) {
		try {
			return fromLotus(getDelegate().getNextElement(type), null, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#getNextElement(int, int)
	 */
	@Override
	public org.openntf.domino.Base<?> getNextElement(final int type, final int occurrence) {
		try {
			return fromLotus(getDelegate().getNextElement(type, occurrence), null, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#getNthElement(int, int)
	 */
	@Override
	public org.openntf.domino.Base<?> getNthElement(final int type, final int occurrence) {
		try {
			return fromLotus(getDelegate().getNthElement(type, occurrence), null, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#setCharOffset(int)
	 */
	@Override
	public void setCharOffset(final int offset) {
		try {
			getDelegate().setCharOffset(offset);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#setPosition(lotus.domino.Base)
	 */
	@Override
	public void setPosition(final lotus.domino.Base element) {
		try {
			getDelegate().setPosition(toLotus(element));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#setPositionAtEnd(lotus.domino.Base)
	 */
	@Override
	public void setPositionAtEnd(final lotus.domino.Base element) {
		try {
			getDelegate().setPositionAtEnd(toLotus(element));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public org.openntf.domino.Database getParentDatabase() {
		return getAncestorDocument().getParentDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DocumentDescendant#getAncestorDocument()
	 */
	@Override
	public final Document getAncestorDocument() {
		return parent.getAncestorDocument();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public final Database getAncestorDatabase() {
		return parent.getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return parent.getAncestorSession();
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}
