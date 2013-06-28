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
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface RichTextParagraphStyle.
 */
public interface RichTextParagraphStyle extends Base<lotus.domino.RichTextParagraphStyle>, lotus.domino.RichTextParagraphStyle,
		org.openntf.domino.ext.RichTextParagraphStyle, SessionDescendant {

	/**
	 * The Enum Align.
	 */
	public static enum Align {

		/** The center. */
		CENTER(RichTextParagraphStyle.ALIGN_CENTER),
		/** The full. */
		FULL(RichTextParagraphStyle.ALIGN_FULL),
		/** The left. */
		LEFT(RichTextParagraphStyle.ALIGN_LEFT),
		/** The nowrap. */
		NOWRAP(RichTextParagraphStyle.ALIGN_NOWRAP),
		/** The right. */
		RIGHT(RichTextParagraphStyle.ALIGN_RIGHT);

		/** The value_. */
		private final int value_;

		/**
		 * Instantiates a new align.
		 * 
		 * @param value
		 *            the value
		 */
		private Align(final int value) {
			value_ = value;
		}

		/**
		 * Gets the value.
		 * 
		 * @return the value
		 */
		public int getValue() {
			return value_;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#clearAllTabs()
	 */
	@Override
	public void clearAllTabs();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getAlignment()
	 */
	@Override
	public int getAlignment();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getFirstLineLeftMargin()
	 */
	@Override
	public int getFirstLineLeftMargin();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getInterLineSpacing()
	 */
	@Override
	public int getInterLineSpacing();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getLeftMargin()
	 */
	@Override
	public int getLeftMargin();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getPagination()
	 */
	@Override
	public int getPagination();

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public Session getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getRightMargin()
	 */
	@Override
	public int getRightMargin();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getSpacingAbove()
	 */
	@Override
	public int getSpacingAbove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getSpacingBelow()
	 */
	@Override
	public int getSpacingBelow();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#getTabs()
	 */
	@Override
	public Vector<RichTextTab> getTabs();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setAlignment(int)
	 */
	@Override
	public void setAlignment(final int value);

	/**
	 * Sets the alignment.
	 * 
	 * @param value
	 *            the new alignment
	 */
	public void setAlignment(final Align value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setFirstLineLeftMargin(int)
	 */
	@Override
	public void setFirstLineLeftMargin(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setInterLineSpacing(int)
	 */
	@Override
	public void setInterLineSpacing(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setLeftMargin(int)
	 */
	@Override
	public void setLeftMargin(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setPagination(int)
	 */
	@Override
	public void setPagination(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setRightMargin(int)
	 */
	@Override
	public void setRightMargin(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setSpacingAbove(int)
	 */
	@Override
	public void setSpacingAbove(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setSpacingBelow(int)
	 */
	@Override
	public void setSpacingBelow(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setTab(int, int)
	 */
	@Override
	public void setTab(final int position, final int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setTabs(int, int, int)
	 */
	@Override
	public void setTabs(final int count, final int startPos, final int interval);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextParagraphStyle#setTabs(int, int, int, int)
	 */
	@Override
	public void setTabs(final int count, final int startPos, final int interval, final int type);

}
