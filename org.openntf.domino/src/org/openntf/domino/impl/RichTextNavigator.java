/*
 * Copyright OpenNTF 2013
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
import org.openntf.domino.Session;
import org.openntf.domino.types.DocumentDescendant;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextNavigator.
 */
public class RichTextNavigator extends Base<org.openntf.domino.RichTextNavigator, lotus.domino.RichTextNavigator> implements
		org.openntf.domino.RichTextNavigator {

	private static final Logger log_ = Logger.getLogger(RichTextNavigator.class.getName());

	/**
	 * Instantiates a new rich text navigator.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public RichTextNavigator(lotus.domino.RichTextNavigator delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextNavigator#Clone()
	 */
	@Override
	public RichTextNavigator Clone() {
		try {
			return Factory.fromLotus(getDelegate().Clone(), RichTextNavigator.class, super.getParent());
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
	public boolean findFirstElement(int type) {
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
	public boolean findFirstString(String target) {
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
	public boolean findFirstString(String target, int options) {
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
	public boolean findLastElement(int type) {
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
	public boolean findNextElement(int type) {
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
	public boolean findNextElement(int type, int occurrence) {
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
	public boolean findNextString(String target) {
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
	public boolean findNextString(String target, int options) {
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
	public boolean findNthElement(int type, int occurrence) {
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
			return Factory.fromLotus(getDelegate().getElement(), Base.class, this);
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
	public org.openntf.domino.Base<?> getFirstElement(int type) {
		try {
			return Factory.fromLotus(getDelegate().getFirstElement(type), Base.class, this);
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
	public org.openntf.domino.Base<?> getLastElement(int type) {
		try {
			return Factory.fromLotus(getDelegate().getLastElement(type), Base.class, this);
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
			return Factory.fromLotus(getDelegate().getNextElement(), Base.class, this);
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
	public org.openntf.domino.Base<?> getNextElement(int type) {
		try {
			return Factory.fromLotus(getDelegate().getNextElement(type), Base.class, this);
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
	public org.openntf.domino.Base<?> getNextElement(int type, int occurrence) {
		try {
			return Factory.fromLotus(getDelegate().getNextElement(type, occurrence), Base.class, this);
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
	public org.openntf.domino.Base<?> getNthElement(int type, int occurrence) {
		try {
			return Factory.fromLotus(getDelegate().getNthElement(type, occurrence), Base.class, this);
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
	public void setCharOffset(int offset) {
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
	public void setPosition(lotus.domino.Base element) {
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
	public void setPositionAtEnd(lotus.domino.Base element) {
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
	public Document getAncestorDocument() {
		return (Document) ((DocumentDescendant) this.getParent()).getAncestorDocument();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public Database getAncestorDatabase() {
		return this.getAncestorDocument().getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getAncestorDocument().getAncestorSession();
	}
}
