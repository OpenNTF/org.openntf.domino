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

import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextParagraphStyle.
 */
public class RichTextParagraphStyle extends
		BaseThreadSafe<org.openntf.domino.RichTextParagraphStyle, lotus.domino.RichTextParagraphStyle, Session> implements
org.openntf.domino.RichTextParagraphStyle {

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
	protected RichTextParagraphStyle(final lotus.domino.RichTextParagraphStyle delegate, final Session parent, final WrapperFactory wf,
			final long cppId) {
		super(delegate, parent, NOTES_RTPSTYLE);
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final Session getParent() {
		return parent;
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
			return fromLotusAsVector(getDelegate().getTabs(), org.openntf.domino.RichTextTab.SCHEMA, this);
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
	public void setAlignment(final int value) {
		try {
			getDelegate().setAlignment(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextParagraphStyle#setAlignment(org.openntf.domino.RichTextParagraphStyle.Align)
	 */
	@Override
	public void setAlignment(final Align value) {
		setAlignment(value.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextParagraphStyle#setFirstLineLeftMargin(int)
	 */
	@Override
	public void setFirstLineLeftMargin(final int value) {
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
	public void setInterLineSpacing(final int value) {
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
	public void setLeftMargin(final int value) {
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
	public void setPagination(final int value) {
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
	public void setRightMargin(final int value) {
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
	public void setSpacingAbove(final int value) {
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
	public void setSpacingBelow(final int value) {
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
	public void setTab(final int position, final int type) {
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
	public void setTabs(final int count, final int startPos, final int interval) {
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
	public void setTabs(final int count, final int startPos, final int interval, final int type) {
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
	public final Session getAncestorSession() {
		return parent;
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getFactory();
	}

}
