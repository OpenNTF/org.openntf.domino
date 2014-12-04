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
import org.openntf.domino.Document;
import org.openntf.domino.RichTextParagraphStyle;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.types.DocumentDescendant;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextTab.
 */
public class RichTextTab extends BaseNonThreadSafe<org.openntf.domino.RichTextTab, lotus.domino.RichTextTab, RichTextParagraphStyle>
		implements org.openntf.domino.RichTextTab {
	//private static final Logger log_ = Logger.getLogger(RichTextTab.class.getName());

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
	public RichTextTab(final lotus.domino.RichTextTab delegate, final RichTextParagraphStyle parent, final WrapperFactory wf,
			final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_RTTAB);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTab#clear()
	 */
	@Override
	public void clear() {
		markDirty();
		try {
			getDelegate().clear();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTab#getPosition()
	 */
	@Override
	public int getPosition() {
		try {
			return getDelegate().getPosition();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTab#getType()
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
		return ((DocumentDescendant) getAncestor()).getAncestorDocument();
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
