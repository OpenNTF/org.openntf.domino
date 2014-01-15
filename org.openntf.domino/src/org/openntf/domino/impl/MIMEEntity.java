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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.MIMEHeader;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.xml.sax.InputSource;

// TODO: Auto-generated Javadoc
/**
 * The Class MIMEEntity.
 */
public class MIMEEntity extends Base<org.openntf.domino.MIMEEntity, lotus.domino.MIMEEntity> implements org.openntf.domino.MIMEEntity {

	/**
	 * Instantiates a new mIME entity.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public MIMEEntity(final lotus.domino.MIMEEntity delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#createChildEntity()
	 */
	@Override
	public org.openntf.domino.MIMEEntity createChildEntity() {
		markDirty();
		try {
			return fromLotus(getDelegate().createChildEntity(), MIMEEntity.SCHEMA, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#createChildEntity(lotus.domino.MIMEEntity)
	 */
	@Override
	public org.openntf.domino.MIMEEntity createChildEntity(final lotus.domino.MIMEEntity nextSibling) {
		markDirty();
		try {
			return fromLotus(getDelegate().createChildEntity(toLotus(nextSibling)), MIMEEntity.SCHEMA, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#createHeader(java.lang.String)
	 */
	@Override
	public MIMEHeader createHeader(final String headerName) {
		markDirty();
		try {
			return fromLotus(getDelegate().createHeader(headerName), MIMEHeader.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#createParentEntity()
	 */
	@Override
	public org.openntf.domino.MIMEEntity createParentEntity() {
		markDirty();
		try {
			return fromLotus(getDelegate().createParentEntity(), MIMEEntity.SCHEMA, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#decodeContent()
	 */
	@Override
	public void decodeContent() {
		markDirty();
		try {
			getDelegate().decodeContent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#encodeContent(int)
	 */
	@Override
	public void encodeContent(final int encoding) {
		markDirty();
		try {
			getDelegate().encodeContent(encoding);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getBoundaryEnd()
	 */
	@Override
	public String getBoundaryEnd() {
		try {
			return getDelegate().getBoundaryEnd();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getBoundaryStart()
	 */
	@Override
	public String getBoundaryStart() {
		try {
			return getDelegate().getBoundaryStart();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getCharset()
	 */
	@Override
	public String getCharset() {
		try {
			return getDelegate().getCharset();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getContentAsBytes(lotus.domino.Stream)
	 */
	@Override
	public void getContentAsBytes(final lotus.domino.Stream stream) {
		try {
			getDelegate().getContentAsBytes((lotus.domino.Stream) toLotus(stream));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getContentAsBytes(lotus.domino.Stream, boolean)
	 */
	@Override
	public void getContentAsBytes(final lotus.domino.Stream stream, final boolean decoded) {
		try {
			getDelegate().getContentAsBytes((lotus.domino.Stream) toLotus(stream), decoded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getContentAsText()
	 */
	@Override
	public String getContentAsText() {
		try {
			return getDelegate().getContentAsText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getContentAsText(lotus.domino.Stream)
	 */
	@Override
	public void getContentAsText(final lotus.domino.Stream stream) {
		try {
			getDelegate().getContentAsText((lotus.domino.Stream) toLotus(stream));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getContentAsText(lotus.domino.Stream, boolean)
	 */
	@Override
	public void getContentAsText(final lotus.domino.Stream stream, final boolean decoded) {
		try {
			getDelegate().getContentAsText((lotus.domino.Stream) toLotus(stream), decoded);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getContentSubType()
	 */
	@Override
	public String getContentSubType() {
		try {
			return getDelegate().getContentSubType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getContentType()
	 */
	@Override
	public String getContentType() {
		try {
			return getDelegate().getContentType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getEncoding()
	 */
	@Override
	public int getEncoding() {
		try {
			return getDelegate().getEncoding();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getEntityAsText(lotus.domino.Stream)
	 */
	@Override
	public void getEntityAsText(final lotus.domino.Stream stream) {
		try {
			getDelegate().getEntityAsText((lotus.domino.Stream) toLotus(stream));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getEntityAsText(lotus.domino.Stream, java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void getEntityAsText(final lotus.domino.Stream stream, final Vector headerFilters) {
		try {
			getDelegate().getEntityAsText((lotus.domino.Stream) toLotus(stream), headerFilters);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getEntityAsText(lotus.domino.Stream, java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void getEntityAsText(final lotus.domino.Stream stream, final Vector headerFilters, final boolean inclusive) {
		try {
			getDelegate().getEntityAsText((lotus.domino.Stream) toLotus(stream), headerFilters, inclusive);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getFirstChildEntity()
	 */
	@Override
	public org.openntf.domino.MIMEEntity getFirstChildEntity() {
		try {
			return fromLotus(getDelegate().getFirstChildEntity(), MIMEEntity.SCHEMA, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getHeaderObjects()
	 */
	@Override
	public Vector<MIMEHeader> getHeaderObjects() {
		try {
			return fromLotusAsVector(getDelegate().getHeaderObjects(), MIMEHeader.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getHeaders()
	 */
	@Override
	public String getHeaders() {
		try {
			return getDelegate().getHeaders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getInputSource()
	 */
	@Override
	public InputSource getInputSource() {
		try {
			return getDelegate().getInputSource();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		try {
			return getDelegate().getInputStream();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getNextEntity()
	 */
	@Override
	public org.openntf.domino.MIMEEntity getNextEntity() {
		try {
			return fromLotus(getDelegate().getNextEntity(), MIMEEntity.SCHEMA, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getNextEntity(int)
	 */
	@Override
	public org.openntf.domino.MIMEEntity getNextEntity(final int search) {
		try {
			return fromLotus(getDelegate().getNextEntity(search), MIMEEntity.SCHEMA, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getNextSibling()
	 */
	@Override
	public org.openntf.domino.MIMEEntity getNextSibling() {
		try {
			return fromLotus(getDelegate().getNextSibling(), MIMEEntity.SCHEMA, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getNthHeader(java.lang.String)
	 */
	@Override
	public MIMEHeader getNthHeader(final String headerName) {
		try {
			return fromLotus(getDelegate().getNthHeader(headerName), MIMEHeader.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getNthHeader(java.lang.String, int)
	 */
	@Override
	public MIMEHeader getNthHeader(final String headerName, final int instance) {
		try {
			return fromLotus(getDelegate().getNthHeader(headerName, instance), MIMEHeader.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Document getParent() {
		return getAncestorDocument();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getParentEntity()
	 */
	@Override
	public org.openntf.domino.MIMEEntity getParentEntity() {
		try {
			return fromLotus(getDelegate().getParentEntity(), MIMEEntity.SCHEMA, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getPreamble()
	 */
	@Override
	public String getPreamble() {
		try {
			return getDelegate().getPreamble();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getPrevEntity()
	 */
	@Override
	public org.openntf.domino.MIMEEntity getPrevEntity() {
		try {
			return fromLotus(getDelegate().getPrevEntity(), MIMEEntity.SCHEMA, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getPrevEntity(int)
	 */
	@Override
	public org.openntf.domino.MIMEEntity getPrevEntity(final int search) {
		try {
			return fromLotus(getDelegate().getPrevEntity(search), MIMEEntity.SCHEMA, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getPrevSibling()
	 */
	@Override
	public org.openntf.domino.MIMEEntity getPrevSibling() {
		try {
			return fromLotus(getDelegate().getPrevSibling(), MIMEEntity.SCHEMA, getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getReader()
	 */
	@Override
	public Reader getReader() {
		try {
			return getDelegate().getReader();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getSomeHeaders()
	 */
	@Override
	public String getSomeHeaders() {
		try {
			return getDelegate().getSomeHeaders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getSomeHeaders(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getSomeHeaders(final Vector headerFilters) {
		List recycleThis = new ArrayList();
		try {
			String result;
			java.util.Vector v = toDominoFriendly(headerFilters, this, recycleThis);
			result = getDelegate().getSomeHeaders(v);
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		} finally {
			s_recycle(recycleThis);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getSomeHeaders(java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getSomeHeaders(final Vector headerFilters, final boolean inclusive) {
		try {
			String result;
			List recycleThis = new ArrayList();
			java.util.Vector v = toDominoFriendly(headerFilters, this, recycleThis);
			result = getDelegate().getSomeHeaders(v, inclusive);
			s_recycle(recycleThis);
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#parseXML(boolean)
	 */
	@Override
	public org.w3c.dom.Document parseXML(final boolean validate) throws IOException {
		try {
			return getDelegate().parseXML(validate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#remove()
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
	 * @see org.openntf.domino.MIMEEntity#setContentFromBytes(lotus.domino.Stream, java.lang.String, int)
	 */
	@Override
	public void setContentFromBytes(final lotus.domino.Stream stream, final String contentType, final int encoding) {
		markDirty();
		try {
			getDelegate().setContentFromBytes((lotus.domino.Stream) toLotus(stream), contentType, encoding);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#setContentFromText(lotus.domino.Stream, java.lang.String, int)
	 */
	@Override
	public void setContentFromText(final lotus.domino.Stream stream, final String contentType, final int encoding) {
		markDirty();
		try {
			getDelegate().setContentFromText((lotus.domino.Stream) toLotus(stream), contentType, encoding);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#setPreamble(java.lang.String)
	 */
	@Override
	public void setPreamble(final String preamble) {
		markDirty();
		try {
			getDelegate().setPreamble(preamble);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#transformXML(java.lang.Object, lotus.domino.XSLTResultTarget)
	 */
	@Override
	public void transformXML(final Object style, final lotus.domino.XSLTResultTarget result) {
		try {
			getDelegate().transformXML(style, result);
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
	public Document getAncestorDocument() {
		org.openntf.domino.Base<?> result = super.getParent();
		if (result instanceof MIMEEntity) {
			result = ((MIMEEntity) result).getAncestorDocument();
		}
		if (result instanceof Document) {
			return (Document) result;
		} else {
			throw new org.openntf.domino.exceptions.IHaveNoIdeaHowThisHappenedException(
					"Expected to find a Document as the parent but instead we found a " + result.getClass().getName());
		}

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
