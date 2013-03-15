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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import lotus.domino.NotesException;
import lotus.domino.XSLTResultTarget;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.w3c.dom.Document;
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
	public EmbeddedObject(lotus.domino.EmbeddedObject delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, (parent instanceof org.openntf.domino.Session) ? parent : Factory.getSession(parent));
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.EmbeddedObject#activate(boolean)
	 */
	public int activate(boolean paramBoolean) {
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.EmbeddedObject#doVerb(java.lang.String)
	 */
	public void doVerb(String paramString) {
		try {
			getDelegate().doVerb(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.EmbeddedObject#extractFile(java.lang.String)
	 */
	public void extractFile(String paramString) {
		try {
			getDelegate().extractFile(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public org.openntf.domino.RichTextItem getParent() {
		try {
			return Factory.fromLotus(getDelegate().getParent(), org.openntf.domino.RichTextItem.class, null); // FIXME NTF - this ain't
			// gonna work. Need an
			// RTItem implementation
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.EmbeddedObject#getVerbs()
	 */
	public Vector getVerbs() {
		try {
			return getDelegate().getVerbs();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.EmbeddedObject#remove()
	 */
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.EmbeddedObject#parseXML(boolean)
	 */
	public Document parseXML(boolean paramBoolean) throws IOException {
		try {
			return getDelegate().parseXML(paramBoolean);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.EmbeddedObject#transformXML(java.lang.Object, lotus.domino.XSLTResultTarget)
	 */
	public void transformXML(Object paramObject, XSLTResultTarget paramXSLTResultTarget) {
		try {
			getDelegate().transformXML(paramObject, paramXSLTResultTarget);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

}
