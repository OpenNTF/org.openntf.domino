package org.openntf.domino.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import lotus.domino.NotesException;
import lotus.domino.RichTextItem;
import lotus.domino.XSLTResultTarget;

import org.openntf.domino.utils.DominoUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class EmbeddedObject extends Base<org.openntf.domino.EmbeddedObject, lotus.domino.EmbeddedObject> implements
		org.openntf.domino.EmbeddedObject {
	private lotus.domino.EmbeddedObject temp;

	protected EmbeddedObject(lotus.domino.EmbeddedObject delegate) {
		super(delegate);
	}

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

	public void doVerb(String paramString) {
		try {
			getDelegate().doVerb(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void extractFile(String paramString) {
		try {
			getDelegate().extractFile(paramString);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public String getClassName() {
		try {
			return getDelegate().getClassName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public int getFileSize() {
		try {
			return getDelegate().getFileSize();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public int getObject() {
		try {
			return getDelegate().getObject();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	@Override
	public RichTextItem getParent() {
		try {
			return getDelegate().getParent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getSource() {
		try {
			return getDelegate().getSource();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public int getType() {
		try {
			return getDelegate().getType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	public Vector getVerbs() {
		try {
			return getDelegate().getVerbs();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

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

	public Document parseXML(boolean paramBoolean) throws IOException {
		try {
			return getDelegate().parseXML(paramBoolean);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public void transformXML(Object paramObject, XSLTResultTarget paramXSLTResultTarget) {
		try {
			getDelegate().transformXML(paramObject, paramXSLTResultTarget);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

}
