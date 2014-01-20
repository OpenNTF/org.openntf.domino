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

import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

/**
 * The Class RichTextStyle.
 */
public class RichTextStyle extends Base<org.openntf.domino.RichTextStyle, lotus.domino.RichTextStyle, Session> implements
		org.openntf.domino.RichTextStyle {

	/**
	 * Instantiates a new rich text style.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public RichTextStyle(final lotus.domino.RichTextStyle delegate, final org.openntf.domino.Base<?> parent) {
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
	public RichTextStyle(final lotus.domino.RichTextStyle delegate, final Session parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_SESSION);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected Session findParent(final lotus.domino.RichTextStyle delegate) throws NotesException {
		return fromLotus(delegate.getParent(), Session.SCHEMA, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#getBold()
	 */
	@Override
	public int getBold() {
		try {
			return getDelegate().getBold();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#getColor()
	 */
	@Override
	public int getColor() {
		try {
			return getDelegate().getColor();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#getEffects()
	 */
	@Override
	public int getEffects() {
		try {
			return getDelegate().getEffects();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#getFont()
	 */
	@Override
	public int getFont() {
		try {
			return getDelegate().getFont();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#getFontSize()
	 */
	@Override
	public int getFontSize() {
		try {
			return getDelegate().getFontSize();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#getItalic()
	 */
	@Override
	public int getItalic() {
		try {
			return getDelegate().getItalic();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return getAncestor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#getPassThruHTML()
	 */
	@Override
	public int getPassThruHTML() {
		try {
			return getDelegate().getPassThruHTML();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#getStrikeThrough()
	 */
	@Override
	public int getStrikeThrough() {
		try {
			return getDelegate().getStrikeThrough();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#getUnderline()
	 */
	@Override
	public int getUnderline() {
		try {
			return getDelegate().getUnderline();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#isDefault()
	 */
	@Override
	public boolean isDefault() {
		try {
			return getDelegate().isDefault();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#setBold(int)
	 */
	@Override
	public void setBold(final int value) {
		try {
			getDelegate().setBold(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	public void setBold(final BoldStyle ISBN) {
		try {
			getDelegate().setBold(ISBN.getValue());
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#setColor(int)
	 */
	@Override
	public void setColor(final int value) {
		try {
			getDelegate().setColor(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#setEffects(int)
	 */
	@Override
	public void setEffects(final int value) {
		try {
			getDelegate().setEffects(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#setFont(int)
	 */
	@Override
	public void setFont(final int value) {
		try {
			getDelegate().setFont(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#setFontSize(int)
	 */
	@Override
	public void setFontSize(final int value) {
		try {
			getDelegate().setFontSize(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#setItalic(int)
	 */
	@Override
	public void setItalic(final int value) {
		try {
			getDelegate().setItalic(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#setPassThruHTML(int)
	 */
	@Override
	public void setPassThruHTML(final int value) {
		try {
			getDelegate().setPassThruHTML(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#setStrikeThrough(int)
	 */
	@Override
	public void setStrikeThrough(final int value) {
		try {
			getDelegate().setStrikeThrough(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextStyle#setUnderline(int)
	 */
	@Override
	public void setUnderline(final int value) {
		try {
			getDelegate().setUnderline(value);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public org.openntf.domino.Session getAncestorSession() {
		return this.getParent();
	}
}
