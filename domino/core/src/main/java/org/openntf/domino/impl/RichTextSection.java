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

import org.openntf.domino.ColorObject;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.RichTextNavigator;
import org.openntf.domino.RichTextStyle;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.types.DocumentDescendant;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextSection.
 */
public class RichTextSection extends BaseThreadSafe<org.openntf.domino.RichTextSection, lotus.domino.RichTextSection, RichTextNavigator>
implements org.openntf.domino.RichTextSection {
	//private static final Logger log_ = Logger.getLogger(RichTextSection.class.getName());

	/**
	 * Instantiates a new outline.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected RichTextSection(final lotus.domino.RichTextSection delegate, final RichTextNavigator parent) {
		super(delegate, parent, NOTES_COLOR);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextSection#getBarColor()
	 */
	@Override
	public ColorObject getBarColor() {
		try {
			return fromLotus(getDelegate().getBarColor(), ColorObject.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final RichTextNavigator getParent() {
		return parent;
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
			return fromLotus(getDelegate().getTitleStyle(), RichTextStyle.SCHEMA, getAncestorSession());
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
	 * @see org.openntf.domino.RichTextSection#setBarColor(lotus.domino.ColorObject)
	 */
	@Override
	public void setBarColor(final lotus.domino.ColorObject color) {
		markDirty();
		try {
			getDelegate().setBarColor(toLotus(color));
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
	public void setExpanded(final boolean flag) {
		markDirty();
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
	public void setTitle(final String title) {
		markDirty();
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
	public void setTitleStyle(final lotus.domino.RichTextStyle style) {
		markDirty();
		try {
			getDelegate().setTitleStyle(toLotus(style));
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
	public final Document getAncestorDocument() {
		return ((DocumentDescendant) parent).getAncestorDocument();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public final Database getAncestorDatabase() {
		return this.getAncestorDocument().getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return this.getAncestorDocument().getAncestorSession();
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}
