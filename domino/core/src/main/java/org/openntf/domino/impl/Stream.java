/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import lotus.domino.NotesException;

import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class Stream.
 */
public class Stream extends BaseThreadSafe<org.openntf.domino.Stream, lotus.domino.Stream, Session> implements org.openntf.domino.Stream {

	/**
	 * Instantiates a new outline.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected Stream(final lotus.domino.Stream delegate, final Session parent) {
		super(delegate, parent, NOTES_SESSTRM);
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
	public void getContents(final OutputStream stream) {
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
	public void getContents(final Writer writer) {
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
	public final Session getParent() {
		return parent;
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
	public boolean open(final String pathName) {
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
	public boolean open(final String pathName, final String charSet) {
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
	public byte[] read(final int length) {
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
	public String readText(final int length) {
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
	public String readText(final int length, final int eolType) {
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
	public void setContents(final InputStream stream) {
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
	public void setContents(final Reader reader) {
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
	public void setPosition(final int position) {
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
	public int write(final byte[] buffer) {
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
	public int writeText(final String text) {
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
	public int writeText(final String text, final int eolType) {
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
	public final Session getAncestorSession() {
		return parent;
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getFactory();
	}

}
