/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.RichTextStyle;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextDoclink.
 */
public class RichTextDoclink extends BaseThreadSafe<org.openntf.domino.RichTextDoclink, lotus.domino.RichTextDoclink, RichTextItem>
implements org.openntf.domino.RichTextDoclink {
	//private static final Logger log_ = Logger.getLogger(RichTextDoclink.class.getName());

	/**
	 * Instantiates a new outline.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected RichTextDoclink(final lotus.domino.RichTextDoclink delegate, final RichTextItem parent) {
		super(delegate, parent, NOTES_RTDOCLNK);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#getDBReplicaID()
	 */
	@Override
	public String getDBReplicaID() {
		try {
			return getDelegate().getDBReplicaID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#getDisplayComment()
	 */
	@Override
	public String getDisplayComment() {
		try {
			return getDelegate().getDisplayComment();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#getDocUnID()
	 */
	@Override
	public String getDocUnID() {
		try {
			return getDelegate().getDocUnID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#getHotSpotText()
	 */
	@Override
	public String getHotSpotText() {
		try {
			return getDelegate().getHotSpotText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#getHotSpotTextStyle()
	 */
	@Override
	public RichTextStyle getHotSpotTextStyle() {
		try {
			return fromLotus(getDelegate().getHotSpotTextStyle(), RichTextStyle.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#getServerHint()
	 */
	@Override
	public String getServerHint() {
		try {
			return getDelegate().getServerHint();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#getViewUnID()
	 */
	@Override
	public String getViewUnID() {
		try {
			return getDelegate().getViewUnID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#remove()
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
	 * @see org.openntf.domino.RichTextDoclink#setDBReplicaID(java.lang.String)
	 */
	@Override
	public void setDBReplicaID(final String replicaId) {
		markDirty();
		try {
			getDelegate().setDBReplicaID(replicaId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#setDisplayComment(java.lang.String)
	 */
	@Override
	public void setDisplayComment(final String comment) {
		markDirty();
		try {
			getDelegate().setDisplayComment(comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#setDocUnID(java.lang.String)
	 */
	@Override
	public void setDocUnID(final String unid) {
		markDirty();
		try {
			getDelegate().setDocUnID(unid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#setHotSpotText(java.lang.String)
	 */
	@Override
	public void setHotSpotText(final String text) {
		markDirty();
		try {
			getDelegate().setHotSpotText(text);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#setHotSpotTextStyle(lotus.domino.RichTextStyle)
	 */
	@Override
	public void setHotSpotTextStyle(final lotus.domino.RichTextStyle rtstyle) {
		markDirty();
		try {
			getDelegate().setHotSpotTextStyle(toLotus(rtstyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#setServerHint(java.lang.String)
	 */
	@Override
	public void setServerHint(final String server) {
		markDirty();
		try {
			getDelegate().setServerHint(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextDoclink#setViewUnID(java.lang.String)
	 */
	@Override
	public void setViewUnID(final String unid) {
		markDirty();
		try {
			getDelegate().setViewUnID(unid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	protected void markDirty() {
		getAncestorDocument().markDirty();
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.RichTextDoclink#getDatabase()
	 */
	@Override
	public Database getDatabase() {
		Session session = getAncestorSession();
		Database database = session.getDatabase("", ""); //$NON-NLS-1$ //$NON-NLS-2$
		database.openByReplicaID(getServerHint(), getDBReplicaID());
		return database;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.RichTextDoclink#getDocument()
	 */
	@Override
	public org.openntf.domino.Document getDocument() {
		String documentId = getDocUnID();
		if (documentId != null && !documentId.isEmpty()) {
			Database database = getDatabase();
			return database.getDocumentByUNID(documentId);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.RichTextDoclink#getView()
	 */
	@Override
	public View getView() {
		String viewId = getViewUnID();
		if (viewId != null && !viewId.isEmpty()) {
			// Use session.resolve to not run afoul of same-named views
			Database database = getDatabase();
			Session session = getAncestorSession();
			String databaseUrl = database.getURL();
			String url = databaseUrl.substring(0, databaseUrl.length() - "?OpenDatabase".length()) + "/" + viewId + "?OpenView"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return (View) session.resolve(url);

		}
		return null;
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}