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

import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.MIMEEntity;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class MIMEHeader.
 */
public class MIMEHeader extends Base<org.openntf.domino.MIMEHeader, lotus.domino.MIMEHeader, MIMEEntity> implements
		org.openntf.domino.MIMEHeader {
	private static final Logger log_ = Logger.getLogger(MIMEHeader.class.getName());

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
	public MIMEHeader(final lotus.domino.MIMEHeader delegate, final MIMEEntity parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_MIMEENTITY);
		initialize(delegate);
	}

	private String headerName_;

	private void initialize(final lotus.domino.MIMEHeader header) {
		try {
			headerName_ = header.getHeaderName();
		} catch (NotesException ne) {
			ne.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#addValText(java.lang.String)
	 */
	@Override
	public boolean addValText(final String valueText) {
		markDirty();
		try {
			return getDelegate().addValText(valueText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#addValText(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addValText(final String valueText, final String charSet) {
		markDirty();
		try {
			return getDelegate().addValText(valueText, charSet);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#getHeaderName()
	 */
	@Override
	public String getHeaderName() {
		return headerName_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#getHeaderVal()
	 */
	@Override
	public String getHeaderVal() {
		try {
			return getDelegate().getHeaderVal();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#getHeaderVal(boolean)
	 */
	@Override
	public String getHeaderVal(final boolean folded) {
		try {
			return getDelegate().getHeaderVal(folded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#getHeaderVal(boolean, boolean)
	 */
	@Override
	public String getHeaderVal(final boolean folded, final boolean decoded) {
		try {
			return getDelegate().getHeaderVal(folded, decoded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#getHeaderValAndParams()
	 */
	@Override
	public String getHeaderValAndParams() {
		try {
			return getDelegate().getHeaderValAndParams();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#getHeaderValAndParams(boolean)
	 */
	@Override
	public String getHeaderValAndParams(final boolean folded) {
		try {
			return getDelegate().getHeaderValAndParams(folded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#getHeaderValAndParams(boolean, boolean)
	 */
	@Override
	public String getHeaderValAndParams(final boolean folded, final boolean decoded) {
		try {
			return getDelegate().getHeaderValAndParams(folded, decoded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#getParamVal(java.lang.String)
	 */
	@Override
	public String getParamVal(final String paramName) {
		try {
			return getDelegate().getParamVal(paramName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#getParamVal(java.lang.String, boolean)
	 */
	@Override
	public String getParamVal(final String paramName, final boolean folded) {
		try {
			return getDelegate().getParamVal(paramName, folded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public MIMEEntity getParent() {
		return getAncestor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#remove()
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
	 * @see org.openntf.domino.MIMEHeader#setHeaderVal(java.lang.String)
	 */
	@Override
	public boolean setHeaderVal(final String headerValue) {
		markDirty();
		try {
			return getDelegate().setHeaderVal(headerValue);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#setHeaderValAndParams(java.lang.String)
	 */
	@Override
	public boolean setHeaderValAndParams(final String headerParamValue) {
		markDirty();
		try {
			return getDelegate().setHeaderValAndParams(headerParamValue);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEHeader#setParamVal(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean setParamVal(final String parameterName, final String parameterValue) {
		markDirty();
		try {
			return getDelegate().setParamVal(parameterName, parameterValue);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
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
		return this.getParent().getAncestorDocument();
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

	@Override
	protected lotus.domino.MIMEHeader getDelegate() {
		lotus.domino.MIMEHeader d = super.getDelegate();
		if (isDead(d)) {
			resurrect();
		}
		return super.getDelegate();
	}

	private void resurrect() {
		if (headerName_ != null) {
			try {
				lotus.domino.MIMEEntity entity = (lotus.domino.MIMEEntity) org.openntf.domino.impl.Base.getDelegate(getAncestor());
				lotus.domino.MIMEHeader header = entity.getNthHeader(headerName_);
				this.setDelegate(header);
			} catch (NotesException ne) {
				DominoUtils.handleException(ne);
			}
		} else {
			if (log_.isLoggable(Level.SEVERE)) {
				log_.log(Level.SEVERE,
						"MIMEHeader doesn't have headerName value. Something went terribly wrong. Nothing good can come of this...");
			}
		}
	}

}
