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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class Stream.
 */
public class Stream extends Base<org.openntf.domino.Stream, lotus.domino.Stream> implements org.openntf.domino.Stream {

	/**
	 * Instantiates a new stream.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public Stream(lotus.domino.Stream delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#close()
	 */
	@Override
	public void close() {
		try {
			getDelegate().close();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#getBytes()
	 */
	@Override
	public int getBytes() {
		try {
			return getDelegate().getBytes();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#getCharset()
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
	 * @see org.openntf.domino.Stream#getContents(java.io.OutputStream)
	 */
	@Override
	public void getContents(OutputStream stream) {
		try {
			getDelegate().getContents(stream);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#getContents(java.io.Writer)
	 */
	@Override
	public void getContents(Writer writer) {
		try {
			getDelegate().getContents(writer);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return (Session) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#getPosition()
	 */
	@Override
	public int getPosition() {
		try {
			return getDelegate().getPosition();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#isEOS()
	 */
	@Override
	public boolean isEOS() {
		try {
			return getDelegate().isEOS();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		try {
			return getDelegate().isReadOnly();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#open(java.lang.String)
	 */
	@Override
	public boolean open(String pathName) {
		try {
			return getDelegate().open(pathName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#open(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean open(String pathName, String charSet) {
		try {
			return getDelegate().open(pathName, charSet);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#read()
	 */
	@Override
	public byte[] read() {
		try {
			return getDelegate().read();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#read(int)
	 */
	@Override
	public byte[] read(int length) {
		try {
			return getDelegate().read(length);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#readText()
	 */
	@Override
	public String readText() {
		try {
			return getDelegate().readText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#readText(int)
	 */
	@Override
	public String readText(int length) {
		try {
			return getDelegate().readText(length);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#readText(int, int)
	 */
	@Override
	public String readText(int length, int eolType) {
		try {
			return getDelegate().readText(length, eolType);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#setContents(java.io.InputStream)
	 */
	@Override
	public void setContents(InputStream stream) {
		try {
			getDelegate().setContents(stream);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#setContents(java.io.Reader)
	 */
	@Override
	public void setContents(Reader reader) {
		try {
			getDelegate().setContents(reader);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#setPosition(int)
	 */
	@Override
	public void setPosition(int position) {
		try {
			getDelegate().setPosition(position);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#truncate()
	 */
	@Override
	public void truncate() {
		try {
			getDelegate().truncate();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#write(byte[])
	 */
	@Override
	public int write(byte[] buffer) {
		try {
			return getDelegate().write(buffer);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#writeText(java.lang.String)
	 */
	@Override
	public int writeText(String text) {
		try {
			return getDelegate().writeText(text);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Stream#writeText(java.lang.String, int)
	 */
	@Override
	public int writeText(String text, int eolType) {
		try {
			return getDelegate().writeText(text, eolType);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public org.openntf.domino.Session getAncestorSession() {
		return this.getParent();
	}
}
