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
import lotus.domino.RichTextStyle;

import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextDoclink.
 */
public class RichTextDoclink extends Base<org.openntf.domino.RichTextDoclink, lotus.domino.RichTextDoclink> implements
		org.openntf.domino.RichTextDoclink {

	/**
	 * Instantiates a new rich text doclink.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public RichTextDoclink(lotus.domino.RichTextDoclink delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextDoclink#getHotSpotTextStyle()
	 */
	@Override
	public RichTextStyle getHotSpotTextStyle() {
		try {
			return getDelegate().getHotSpotTextStyle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextDoclink#remove()
	 */
	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextDoclink#setDBReplicaID(java.lang.String)
	 */
	@Override
	public void setDBReplicaID(String replicaId) {
		try {
			getDelegate().setDBReplicaID(replicaId);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextDoclink#setDisplayComment(java.lang.String)
	 */
	@Override
	public void setDisplayComment(String comment) {
		try {
			getDelegate().setDisplayComment(comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextDoclink#setDocUnID(java.lang.String)
	 */
	@Override
	public void setDocUnID(String unid) {
		try {
			getDelegate().setDocUnID(unid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextDoclink#setHotSpotText(java.lang.String)
	 */
	@Override
	public void setHotSpotText(String text) {
		try {
			getDelegate().setHotSpotText(text);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextDoclink#setHotSpotTextStyle(lotus.domino.RichTextStyle)
	 */
	@Override
	public void setHotSpotTextStyle(RichTextStyle rtstyle) {
		try {
			getDelegate().setHotSpotTextStyle((lotus.domino.RichTextStyle) toLotus(rtstyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextDoclink#setServerHint(java.lang.String)
	 */
	@Override
	public void setServerHint(String server) {
		try {
			getDelegate().setServerHint(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextDoclink#setViewUnID(java.lang.String)
	 */
	@Override
	public void setViewUnID(String unid) {
		try {
			getDelegate().setViewUnID(unid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
