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

import lotus.domino.XSLTResultTarget;

import org.openntf.domino.types.DocumentDescendant;
import org.openntf.domino.types.FactorySchema;
import org.xml.sax.InputSource;

/**
 * The Interface EmbeddedObject.
 */
public interface EmbeddedObject extends Base<lotus.domino.EmbeddedObject>, lotus.domino.EmbeddedObject,
org.openntf.domino.ext.EmbeddedObject, DocumentDescendant {

	public static class Schema extends FactorySchema<EmbeddedObject, lotus.domino.EmbeddedObject, Document> {
		@Override
		public Class<EmbeddedObject> typeClass() {
			return EmbeddedObject.class;
		}

		@Override
		public Class<lotus.domino.EmbeddedObject> delegateClass() {
			return lotus.domino.EmbeddedObject.class;
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
	 * @see lotus.domino.EmbeddedObject#activate(boolean)
	 */
	@Override
	public int activate(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#doVerb(java.lang.String)
	 */
	@Override
	@Deprecated
	public void doVerb(final String verb);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#extractFile(java.lang.String)
	 */
	@Override
	public void extractFile(final String path);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#getClassName()
	 */
	@Override
	public String getClassName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#getFileSize()
	 */
	@Override
	public int getFileSize();

	@Override
	public DateTime getFileCreated();

	@Override
	public DateTime getFileModified();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#getInputSource()
	 */
	@Override
	public InputSource getInputSource();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#getInputStream()
	 */
	@Override
	public InputStream getInputStream();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#getName()
	 */
	@Override
	public String getName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#getObject()
	 */
	@Override
	@Deprecated
	public int getObject();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#getParent()
	 */
	@Override
	public RichTextItem getParent();

	/**
	 * Gets the parent document.
	 * 
	 * @return the parent document
	 */
	public Document getParentDocument();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#getReader()
	 */
	@Override
	public Reader getReader();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#getSource()
	 */
	@Override
	public String getSource();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#getType()
	 */
	@Override
	public int getType();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#getVerbs()
	 */
	@Override
	public Vector<String> getVerbs();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#parseXML(boolean)
	 */
	@Override
	public org.w3c.dom.Document parseXML(final boolean validate) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#remove()
	 */
	@Override
	public void remove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.EmbeddedObject#transformXML(java.lang.Object, lotus.domino.XSLTResultTarget)
	 */
	@Override
	public void transformXML(final Object style, final XSLTResultTarget result);

}
