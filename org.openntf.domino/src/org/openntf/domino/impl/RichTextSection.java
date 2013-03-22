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

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextSection.
 */
public class RichTextSection extends Base<org.openntf.domino.RichTextSection, lotus.domino.RichTextSection> implements
		org.openntf.domino.RichTextSection {

	/**
	 * Instantiates a new rich text section.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public RichTextSection(lotus.domino.RichTextSection delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextSection#getBarColor()
	 */
	@Override
	public ColorObject getBarColor() {
		try {
			return Factory.fromLotus(getDelegate().getBarColor(), ColorObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public RichTextItem getParent() {
		return (RichTextItem) getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextSection#getTitle()
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
	 * @see org.openntf.domino.RichTextSection#getTitleStyle()
	 */
	@Override
	public RichTextStyle getTitleStyle() {
		try {
			return Factory.fromLotus(getDelegate().getTitleStyle(), RichTextStyle.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextSection#isExpanded()
	 */
	@Override
	public boolean isExpanded() {
		try {
			return getDelegate().isExpanded();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextSection#remove()
	 */
	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextSection#setBarColor(lotus.domino.ColorObject)
	 */
	@Override
	public void setBarColor(lotus.domino.ColorObject color) {
		try {
			getDelegate().setBarColor((lotus.domino.ColorObject) toLotus(color));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextSection#setExpanded(boolean)
	 */
	@Override
	public void setExpanded(boolean flag) {
		try {
			getDelegate().setExpanded(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextSection#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		try {
			getDelegate().setTitle(title);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextSection#setTitleStyle(lotus.domino.RichTextStyle)
	 */
	@Override
	public void setTitleStyle(lotus.domino.RichTextStyle style) {
		try {
			getDelegate().setTitleStyle((lotus.domino.RichTextStyle) toLotus(style));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
