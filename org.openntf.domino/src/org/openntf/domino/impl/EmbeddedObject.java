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
import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.NotesException;
import lotus.domino.XSLTResultTarget;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.xml.sax.InputSource;

// TODO: Auto-generated Javadoc
/**
 * The Class EmbeddedObject.
 */
public class EmbeddedObject extends Base<org.openntf.domino.EmbeddedObject, lotus.domino.EmbeddedObject> implements
		org.openntf.domino.EmbeddedObject {

	/** The temp. */
	private lotus.domino.EmbeddedObject temp;

	/**
	 * Instantiates a new embedded object.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public EmbeddedObject(final lotus.domino.EmbeddedObject delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#activate(boolean)
	 */
	public int activate(final boolean paramBoolean) {
		try {
			return getDelegate().activate(paramBoolean);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		} finally {
			if (delegate_ instanceof lotus.domino.local.EmbeddedObject) {
				((lotus.domino.local.EmbeddedObject) delegate_).markInvalid();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#doVerb(java.lang.String)
	 */
	public void doVerb(final String paramString) {
		try {
			getDelegate().doVerb(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#extractFile(java.lang.String)
	 */
	public void extractFile(final String paramString) {
		try {
			getDelegate().extractFile(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#getClassName()
	 */
	public String getClassName() {
		try {
			return getDelegate().getClassName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#getFileSize()
	 */
	public int getFileSize() {
		try {
			return getDelegate().getFileSize();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#getName()
	 */
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#getObject()
	 */
	public int getObject() {
		try {
			return getDelegate().getObject();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public RichTextItem getParent() {
		// The original spec returns either an RT item or null
		org.openntf.domino.Base<?> parent = super.getParent();
		if (parent instanceof org.openntf.domino.RichTextItem) {
			return (RichTextItem) parent;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.EmbeddedObject#getParentDocument()
	 */
	public Document getParentDocument() {
		org.openntf.domino.Base<?> parent = super.getParent();
		if (parent instanceof org.openntf.domino.RichTextItem) {
			return ((RichTextItem) parent).getParent();
		}
		return (Document) parent;
	}

	public org.openntf.domino.Database getParentDatabase() {
		return getParentDocument().getParentDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#getSource()
	 */
	public String getSource() {
		try {
			return getDelegate().getSource();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#getType()
	 */
	public int getType() {
		try {
			return getDelegate().getType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#getVerbs()
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> getVerbs() {
		try {
			return getDelegate().getVerbs();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#remove()
	 */
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
	 * @see org.openntf.domino.EmbeddedObject#getReader()
	 */
	public Reader getReader() {
		try {
			return getDelegate().getReader();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		} finally {
			if (delegate_ instanceof lotus.domino.local.EmbeddedObject) {
				((lotus.domino.local.EmbeddedObject) delegate_).markInvalid();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#getInputSource()
	 */
	public InputSource getInputSource() {
		try {
			return getDelegate().getInputSource();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		} finally {
			if (delegate_ instanceof lotus.domino.local.EmbeddedObject) {
				((lotus.domino.local.EmbeddedObject) delegate_).markInvalid();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#getInputStream()
	 */
	public InputStream getInputStream() {
		try {
			return getDelegate().getInputStream();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		} finally {
			if (delegate_ instanceof lotus.domino.local.EmbeddedObject) {
				((lotus.domino.local.EmbeddedObject) delegate_).markInvalid();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#parseXML(boolean)
	 */
	public org.w3c.dom.Document parseXML(final boolean paramBoolean) throws IOException {
		try {
			return getDelegate().parseXML(paramBoolean);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.EmbeddedObject#transformXML(java.lang.Object, lotus.domino.XSLTResultTarget)
	 */
	public void transformXML(final Object paramObject, final XSLTResultTarget paramXSLTResultTarget) {
		try {
			getDelegate().transformXML(paramObject, paramXSLTResultTarget);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	void markDirty() {
		// TODO RPr: put this in interface
		((org.openntf.domino.impl.Document) getParentDocument()).markDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DocumentDescendant#getAncestorDocument()
	 */
	@Override
	public Document getAncestorDocument() {
		return this.getParentDocument();
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
	 * @see lotus.domino.EmbeddedObject#getFileCreated()
	 */
	public DateTime getFileCreated() {
		try {
			return getDelegate().getFileCreated();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.EmbeddedObject#getFileModified()
	 */
	public DateTime getFileModified() {
		try {
			return getDelegate().getFileModified();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

}
