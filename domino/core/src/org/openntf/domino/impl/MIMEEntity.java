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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.MIMEHeader;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.xml.sax.InputSource;

// TODO: Auto-generated Javadoc
/**
 * The Class MIMEEntity.
 */
public class MIMEEntity extends Base<org.openntf.domino.MIMEEntity, lotus.domino.MIMEEntity, Document> implements
		org.openntf.domino.MIMEEntity {

	/**
	 * we have to track every child element that was queried from this entity.rec
	 */
	private Set<MIMEHeader> trackedHeaders_ = new HashSet<MIMEHeader>();
	private String itemName_;
	private org.openntf.domino.impl.Document sourceDoc_;

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
	public MIMEEntity(final lotus.domino.MIMEEntity delegate, final Document parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_MIMEENTITY);
	}

	/**
	 * This method initializes the name of this MimeEntity, so that the entity knwos it's field name. Should NOT be called in your code!
	 * 
	 * @param itemName
	 */
	public void init(final String itemName) {
		itemName_ = itemName;
	}

	protected org.openntf.domino.MIMEEntity fromLotusMimeEntity(final lotus.domino.MIMEEntity lotus, final boolean markDirty) {
		org.openntf.domino.impl.Document doc = (org.openntf.domino.impl.Document) getParent();
		return doc.fromLotusMimeEntity(lotus, itemName_, markDirty);

	}

	protected MIMEHeader track(final MIMEHeader what) {
		if (what == null)
			return null;
		trackedHeaders_.add(what);
		return what;
	}

	protected Vector<MIMEHeader> track(final Vector<MIMEHeader> what) {
		if (what == null)
			return null;
		for (MIMEHeader el : what) {
			track(el);
		}
		return what;
	}

	public void closeMIMEEntity() {
		Iterator<MIMEHeader> hdrIter = trackedHeaders_.iterator();
		while (hdrIter.hasNext()) {
			try {
				hdrIter.next().recycle();
			} catch (NotesException e) {
			}
			hdrIter.remove();
		}
		recycle();
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
			return fromLotusMimeEntity(getDelegate().createChildEntity(), true);
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
			return fromLotusMimeEntity(getDelegate().createChildEntity(toLotus(nextSibling)), true);
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
			return track(fromLotus(getDelegate().createHeader(headerName), MIMEHeader.SCHEMA, this));
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
			return fromLotusMimeEntity(getDelegate().createParentEntity(), true);
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
			getDelegate().getContentAsBytes(toLotus(stream));
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
			getDelegate().getContentAsBytes(toLotus(stream), decoded);
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
			getDelegate().getContentAsText(toLotus(stream));
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
			getDelegate().getContentAsText(toLotus(stream), decoded);
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
			getDelegate().getEntityAsText(toLotus(stream));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getEntityAsText(lotus.domino.Stream, java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void getEntityAsText(final lotus.domino.Stream stream, final Vector headerFilters) {
		try {
			getDelegate().getEntityAsText(toLotus(stream), headerFilters);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.MIMEEntity#getEntityAsText(lotus.domino.Stream, java.util.Vector, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void getEntityAsText(final lotus.domino.Stream stream, final Vector headerFilters, final boolean inclusive) {
		try {
			getDelegate().getEntityAsText(toLotus(stream), headerFilters, inclusive);
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
			return fromLotusMimeEntity(getDelegate().getFirstChildEntity(), false);
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
			return track(fromLotusAsVector(getDelegate().getHeaderObjects(), MIMEHeader.SCHEMA, this));
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
			return fromLotusMimeEntity(getDelegate().getNextEntity(), false);
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
			return fromLotusMimeEntity(getDelegate().getNextEntity(search), false);
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
			return fromLotusMimeEntity(getDelegate().getNextSibling(), false);
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
			return track(fromLotus(getDelegate().getNthHeader(headerName), MIMEHeader.SCHEMA, this));
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
			return track(fromLotus(getDelegate().getNthHeader(headerName, instance), MIMEHeader.SCHEMA, this));
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
			return fromLotusMimeEntity(getDelegate().getParentEntity(), false);
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
			return fromLotusMimeEntity(getDelegate().getPrevEntity(), false);
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
			return fromLotusMimeEntity(getDelegate().getPrevEntity(search), false);
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
			return fromLotusMimeEntity(getDelegate().getPrevSibling(), false);
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getSomeHeaders(final Vector headerFilters) {
		List recycleThis = new ArrayList();
		try {
			String result;
			Vector v = toDominoFriendly(headerFilters, this, recycleThis);
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	@Override
	public void recycle() {
		//		System.out.println("TMP DEBUG: Recycle called on a MIMEEntity: " + getItemName());
		getParent().closeMIMEEntities(false, getItemName());
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
			getDelegate().setContentFromBytes(toLotus(stream), contentType, encoding);
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
			getDelegate().setContentFromText(toLotus(stream), contentType, encoding);
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

	@Override
	public void markDirty() {
		((org.openntf.domino.impl.Document) getAncestorDocument()).markDirty(getItemName(), true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DocumentDescendant#getAncestorDocument()
	 */
	@Override
	public Document getAncestorDocument() {
		return getAncestor();
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.MIMEEntity#getName()
	 */
	@Override
	public String getItemName() {
		if (itemName_ != null)
			return itemName_;

		return getParentEntity().getItemName();
	}

}
