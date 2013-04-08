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

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextParagraphStyle.
 */
public class RichTextParagraphStyle extends Base<org.openntf.domino.RichTextParagraphStyle, lotus.domino.RichTextParagraphStyle> implements
		org.openntf.domino.RichTextParagraphStyle {

	/**
	 * Instantiates a new rich text paragraph style.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public RichTextParagraphStyle(lotus.domino.RichTextParagraphStyle delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getSession(parent));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#clearAllTabs()
	 */
	@Override
	public void clearAllTabs() {
		try {
			getDelegate().clearAllTabs();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#getAlignment()
	 */
	@Override
	public int getAlignment() {
		try {
			return getDelegate().getAlignment();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#getFirstLineLeftMargin()
	 */
	@Override
	public int getFirstLineLeftMargin() {
		try {
			return getDelegate().getFirstLineLeftMargin();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#getInterLineSpacing()
	 */
	@Override
	public int getInterLineSpacing() {
		try {
			return getDelegate().getInterLineSpacing();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#getLeftMargin()
	 */
	@Override
	public int getLeftMargin() {
		try {
			return getDelegate().getLeftMargin();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#getPagination()
	 */
	@Override
	public int getPagination() {
		try {
			return getDelegate().getPagination();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public Session getParent() {
		return (Session) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#getRightMargin()
	 */
	@Override
	public int getRightMargin() {
		try {
			return getDelegate().getRightMargin();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#getSpacingAbove()
	 */
	@Override
	public int getSpacingAbove() {
		try {
			return getDelegate().getSpacingAbove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#getSpacingBelow()
	 */
	@Override
	public int getSpacingBelow() {
		try {
			return getDelegate().getSpacingBelow();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#getTabs()
	 */
	@Override
	public Vector<org.openntf.domino.RichTextTab> getTabs() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getTabs(), org.openntf.domino.RichTextTab.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#setAlignment(int)
	 */
	@Override
	public void setAlignment(int value) {
		try {
			getDelegate().setAlignment(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setAlignment(Align value) {
		setAlignment(value.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#setFirstLineLeftMargin(int)
	 */
	@Override
	public void setFirstLineLeftMargin(int value) {
		try {
			getDelegate().setFirstLineLeftMargin(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#setInterLineSpacing(int)
	 */
	@Override
	public void setInterLineSpacing(int value) {
		try {
			getDelegate().setInterLineSpacing(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#setLeftMargin(int)
	 */
	@Override
	public void setLeftMargin(int value) {
		try {
			getDelegate().setLeftMargin(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#setPagination(int)
	 */
	@Override
	public void setPagination(int value) {
		try {
			getDelegate().setPagination(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#setRightMargin(int)
	 */
	@Override
	public void setRightMargin(int value) {
		try {
			getDelegate().setRightMargin(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#setSpacingAbove(int)
	 */
	@Override
	public void setSpacingAbove(int value) {
		try {
			getDelegate().setSpacingAbove(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#setSpacingBelow(int)
	 */
	@Override
	public void setSpacingBelow(int value) {
		try {
			getDelegate().setSpacingBelow(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#setTab(int, int)
	 */
	@Override
	public void setTab(int position, int type) {
		try {
			getDelegate().setTab(position, type);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#setTabs(int, int, int)
	 */
	@Override
	public void setTabs(int count, int startPos, int interval) {
		try {
			getDelegate().setTabs(count, startPos, interval);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#setTabs(int, int, int, int)
	 */
	@Override
	public void setTabs(int count, int startPos, int interval, int type) {
		try {
			getDelegate().setTabs(count, startPos, interval, type);
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
	public org.openntf.domino.Session getAncestorSession() {
		return this.getParent();
	}
}
