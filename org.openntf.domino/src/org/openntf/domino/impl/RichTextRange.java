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

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.RichTextNavigator;
import org.openntf.domino.RichTextStyle;
import org.openntf.domino.Session;
import org.openntf.domino.types.DocumentDescendant;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextRange.
 */
public class RichTextRange extends Base<org.openntf.domino.RichTextRange, lotus.domino.RichTextRange> implements
		org.openntf.domino.RichTextRange {

	/**
	 * Instantiates a new rich text range.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public RichTextRange(final lotus.domino.RichTextRange delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextRange#Clone()
	 */
	@Override
	public org.openntf.domino.RichTextRange Clone() {
		try {
			return fromLotus(getDelegate().Clone(), RichTextRange.SCHEMA, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextRange#findandReplace(java.lang.String, java.lang.String)
	 */
	@Override
	public int findandReplace(final String target, final String replacement) {
		int result = 0;
		try {
			result = getDelegate().findandReplace(target, replacement);
			if (result > 0)
				markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextRange#findandReplace(java.lang.String, java.lang.String, long)
	 */
	@Override
	public int findandReplace(final String target, final String replacement, final long options) {
		int result = 0;
		try {
			result = getDelegate().findandReplace(target, replacement, options);
			if (result > 0)
				markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextRange#getNavigator()
	 */
	@Override
	public RichTextNavigator getNavigator() {
		try {
			return fromLotus(getDelegate().getNavigator(), RichTextNavigator.SCHEMA, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public RichTextItem getParent() {
		return (RichTextItem) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextRange#getStyle()
	 */
	@Override
	public RichTextStyle getStyle() {
		try {
			return fromLotus(getDelegate().getStyle(), RichTextStyle.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextRange#getTextParagraph()
	 */
	@Override
	public String getTextParagraph() {
		try {
			return getDelegate().getTextParagraph();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextRange#getTextRun()
	 */
	@Override
	public String getTextRun() {
		try {
			return getDelegate().getTextRun();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextRange#getType()
	 */
	@Override
	public int getType() {
		try {
			return getDelegate().getType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextRange#remove()
	 */
	@Override
	public void remove() {
		markDirty();
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextRange#reset(boolean, boolean)
	 */
	@Override
	public void reset(final boolean begin, final boolean end) {
		try {
			getDelegate().reset(begin, end);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextRange#setBegin(lotus.domino.Base)
	 */
	@Override
	public void setBegin(final lotus.domino.Base element) {
		try {
			getDelegate().setBegin(toLotus(element));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextRange#setEnd(lotus.domino.Base)
	 */
	@Override
	public void setEnd(final lotus.domino.Base element) {
		try {
			getDelegate().setEnd(toLotus(element));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextRange#setStyle(lotus.domino.RichTextStyle)
	 */
	@Override
	public void setStyle(final lotus.domino.RichTextStyle style) {
		markDirty();
		try {
			getDelegate().setStyle((lotus.domino.RichTextStyle) toLotus(style));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	void markDirty() {
		getAncestorDocument().markDirty();
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
