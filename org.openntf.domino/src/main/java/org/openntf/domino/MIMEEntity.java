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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import org.openntf.domino.types.DocumentDescendant;
import org.openntf.domino.types.FactorySchema;
import org.xml.sax.InputSource;

/**
 * The Interface MIMEEntity.
 */
public interface MIMEEntity extends Base<lotus.domino.MIMEEntity>, lotus.domino.MIMEEntity, org.openntf.domino.ext.MIMEEntity,
DocumentDescendant {

	public static class Schema extends FactorySchema<MIMEEntity, lotus.domino.MIMEEntity, Document> {
		@Override
		public Class<MIMEEntity> typeClass() {
			return MIMEEntity.class;
		}

		@Override
		public Class<lotus.domino.MIMEEntity> delegateClass() {
			return lotus.domino.MIMEEntity.class;
		}

		@Override
		public Class<Document> parentClass() {
			return Document.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#createChildEntity()
	 */
	@Override
	public MIMEEntity createChildEntity();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#createChildEntity(lotus.domino.MIMEEntity)
	 */
	@Override
	public MIMEEntity createChildEntity(final lotus.domino.MIMEEntity nextSibling);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#createHeader(java.lang.String)
	 */
	@Override
	public MIMEHeader createHeader(final String headerName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#createParentEntity()
	 */
	@Override
	public MIMEEntity createParentEntity();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#decodeContent()
	 */
	@Override
	public void decodeContent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#encodeContent(int)
	 */
	@Override
	public void encodeContent(final int encoding);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getBoundaryEnd()
	 */
	@Override
	public String getBoundaryEnd();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getBoundaryStart()
	 */
	@Override
	public String getBoundaryStart();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getCharset()
	 */
	@Override
	public String getCharset();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getContentAsBytes(lotus.domino.Stream)
	 */
	@Override
	public void getContentAsBytes(final lotus.domino.Stream stream);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getContentAsBytes(lotus.domino.Stream, boolean)
	 */
	@Override
	public void getContentAsBytes(final lotus.domino.Stream stream, final boolean decoded);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getContentAsText()
	 */
	@Override
	public String getContentAsText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getContentAsText(lotus.domino.Stream)
	 */
	@Override
	public void getContentAsText(final lotus.domino.Stream stream);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getContentAsText(lotus.domino.Stream, boolean)
	 */
	@Override
	public void getContentAsText(final lotus.domino.Stream stream, final boolean decoded);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getContentSubType()
	 */
	@Override
	public String getContentSubType();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getContentType()
	 */
	@Override
	public String getContentType();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getEncoding()
	 */
	@Override
	public int getEncoding();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getEntityAsText(lotus.domino.Stream)
	 */
	@Override
	public void getEntityAsText(final lotus.domino.Stream stream);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getEntityAsText(lotus.domino.Stream, java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void getEntityAsText(final lotus.domino.Stream stream, final Vector headerFilters);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getEntityAsText(lotus.domino.Stream, java.util.Vector, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void getEntityAsText(final lotus.domino.Stream stream, final Vector headerFilters, final boolean inclusive);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getFirstChildEntity()
	 */
	@Override
	public MIMEEntity getFirstChildEntity();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getHeaderObjects()
	 */
	@Override
	public Vector<MIMEHeader> getHeaderObjects();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getHeaders()
	 */
	@Override
	public String getHeaders();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getInputSource()
	 */
	@Override
	public InputSource getInputSource();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getInputStream()
	 */
	@Override
	public InputStream getInputStream();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getNextEntity()
	 */
	@Override
	public MIMEEntity getNextEntity();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getNextEntity(int)
	 */
	@Override
	public MIMEEntity getNextEntity(final int search);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getNextSibling()
	 */
	@Override
	public MIMEEntity getNextSibling();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getNthHeader(java.lang.String)
	 */
	@Override
	public MIMEHeader getNthHeader(final String headerName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getNthHeader(java.lang.String, int)
	 */
	@Override
	public MIMEHeader getNthHeader(final String headerName, final int instance);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getParentEntity()
	 */
	@Override
	public MIMEEntity getParentEntity();

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public Document getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getPreamble()
	 */
	@Override
	public String getPreamble();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getPrevEntity()
	 */
	@Override
	public MIMEEntity getPrevEntity();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getPrevEntity(int)
	 */
	@Override
	public MIMEEntity getPrevEntity(final int search);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getPrevSibling()
	 */
	@Override
	public MIMEEntity getPrevSibling();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getReader()
	 */
	@Override
	public Reader getReader();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getSomeHeaders()
	 */
	@Override
	public String getSomeHeaders();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getSomeHeaders(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String getSomeHeaders(final Vector headerFilters);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#getSomeHeaders(java.util.Vector, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String getSomeHeaders(final Vector headerFilters, final boolean inclusive);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#parseXML(boolean)
	 */
	@Override
	public org.w3c.dom.Document parseXML(final boolean validate) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#remove()
	 */
	@Override
	public void remove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#setContentFromBytes(lotus.domino.Stream, java.lang.String, int)
	 */
	@Override
	public void setContentFromBytes(final lotus.domino.Stream stream, final String contentType, final int encoding);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#setContentFromText(lotus.domino.Stream, java.lang.String, int)
	 */
	@Override
	public void setContentFromText(final lotus.domino.Stream stream, final String contentType, final int encoding);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#setPreamble(java.lang.String)
	 */
	@Override
	public void setPreamble(final String preamble);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.MIMEEntity#transformXML(java.lang.Object, lotus.domino.XSLTResultTarget)
	 */
	@Override
	public void transformXML(final Object style, final lotus.domino.XSLTResultTarget result);

}
