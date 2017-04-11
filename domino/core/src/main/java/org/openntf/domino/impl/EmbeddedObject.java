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
import java.nio.CharBuffer;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import lotus.domino.NotesEntityResolver;
import lotus.domino.NotesException;
import lotus.domino.XSLTResultTarget;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.xml.sax.InputSource;

// TODO: Auto-generated Javadoc
/**
 * The Class EmbeddedObject.
 */
public class EmbeddedObject extends BaseThreadSafe<org.openntf.domino.EmbeddedObject, lotus.domino.EmbeddedObject, Document> implements
		org.openntf.domino.EmbeddedObject {

	protected AtomicInteger referenceCounter = new AtomicInteger();
	protected RichTextItem parent_;

	/**
	 * Instantiates a new outline.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected EmbeddedObject(final lotus.domino.EmbeddedObject delegate, final Document parent) {
		super(delegate, parent, NOTES_EMBEDOBJ);
	}

	private class EOReader extends Reader {
		Reader delegateReader;

		public EOReader(final Reader delegate) {
			super();
			this.delegateReader = delegate;
			referenceCounter.incrementAndGet();
		}

		@Override
		public int read(final CharBuffer paramCharBuffer) throws IOException {
			return delegateReader.read(paramCharBuffer);
		}

		@Override
		public int read() throws IOException {
			return delegateReader.read();
		}

		@Override
		public int hashCode() {
			return delegateReader.hashCode();
		}

		@Override
		public int read(final char[] paramArrayOfChar) throws IOException {
			return delegateReader.read(paramArrayOfChar);
		}

		@Override
		public int read(final char[] paramArrayOfChar, final int paramInt1, final int paramInt2) throws IOException {
			return delegateReader.read(paramArrayOfChar, paramInt1, paramInt2);
		}

		@Override
		public long skip(final long paramLong) throws IOException {
			return delegateReader.skip(paramLong);
		}

		@Override
		public boolean equals(final Object obj) {
			return delegateReader.equals(obj);
		}

		@Override
		public boolean ready() throws IOException {
			return delegateReader.ready();
		}

		@Override
		public boolean markSupported() {
			return delegateReader.markSupported();
		}

		@Override
		public void mark(final int paramInt) throws IOException {
			delegateReader.mark(paramInt);
		}

		@Override
		public void reset() throws IOException {
			delegateReader.reset();
		}

		@Override
		public void close() throws IOException {
			delegateReader.close();
			if (referenceCounter.decrementAndGet() == 0) {
				EmbeddedObject.this.markInvalid();
			}
		}

		@Override
		public String toString() {
			return delegateReader.toString();
		}

	}

	private class EOInputStream extends InputStream {
		private InputStream delegateStream;

		public EOInputStream(final InputStream delegateStream) {
			super();
			this.delegateStream = delegateStream;
			referenceCounter.incrementAndGet();
		}

		@Override
		public int read() throws IOException {
			return delegateStream.read();
		}

		@Override
		public int read(final byte[] paramArrayOfByte) throws IOException {
			return delegateStream.read(paramArrayOfByte);
		}

		@Override
		public int read(final byte[] paramArrayOfByte, final int paramInt1, final int paramInt2) throws IOException {
			return delegateStream.read(paramArrayOfByte, paramInt1, paramInt2);
		}

		@Override
		public int hashCode() {
			return delegateStream.hashCode();
		}

		@Override
		public long skip(final long paramLong) throws IOException {
			return delegateStream.skip(paramLong);
		}

		@Override
		public boolean equals(final Object obj) {
			return delegateStream.equals(obj);
		}

		@Override
		public int available() throws IOException {
			return delegateStream.available();
		}

		@Override
		public void close() throws IOException {
			delegateStream.close();
			if (referenceCounter.decrementAndGet() == 0) {
				EmbeddedObject.this.markInvalid();
			}
		}

		@Override
		public void mark(final int paramInt) {
			delegateStream.mark(paramInt);
		}

		@Override
		public void reset() throws IOException {
			delegateStream.reset();
		}

		@Override
		public boolean markSupported() {
			return delegateStream.markSupported();
		}

		@Override
		public String toString() {
			return delegateStream.toString();
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.EmbeddedObject#getParent()
	 */
	@Override
	public final RichTextItem getParent() {
		if (parent_ == null) {
			try {
				parent_ = fromLotus(getDelegate().getParent(), RichTextItem.SCHEMA, parent);
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
		}
		return parent_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.EmbeddedObject#activate(boolean)
	 */
	@Override
	public int activate(final boolean paramBoolean) {
		try {
			return getDelegate().activate(paramBoolean);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		} finally {
			if (_delegateLocal.get() instanceof lotus.domino.local.EmbeddedObject) {
				((lotus.domino.local.EmbeddedObject) _delegateLocal.get()).markInvalid();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.EmbeddedObject#doVerb(java.lang.String)
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public int getObject() {
		try {
			return getDelegate().getObject();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.EmbeddedObject#getParentDocument()
	 */
	@Override
	public final Document getParentDocument() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.EmbeddedObject#getSource()
	 */
	@Override
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
	@Override
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
	@Override
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
	 * @see org.openntf.domino.EmbeddedObject#getReader()
	 */
	@Override
	public Reader getReader() {
		try {
			return new EOReader(getDelegate().getReader());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.EmbeddedObject#getInputSource()
	 */
	@Override
	public InputSource getInputSource() {
		return NotesEntityResolver.newEntityInputSource(getReader(), getAncestorDocument(), toString());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.EmbeddedObject#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		try {
			return new EOInputStream(getDelegate().getInputStream());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/**
	 * marks the object as invalid and deletes local attachments
	 */
	@Override
	public void markInvalid() {
		((lotus.domino.local.EmbeddedObject) _delegateLocal.get()).markInvalid();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.EmbeddedObject#parseXML(boolean)
	 */
	@Override
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
	@Override
	public void transformXML(final Object paramObject, final XSLTResultTarget paramXSLTResultTarget) {
		try {
			getDelegate().transformXML(paramObject, paramXSLTResultTarget);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void markDirty() {
		getParentDocument().markDirty();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.DocumentDescendant#getAncestorDocument()
	 */
	@Override
	public final Document getAncestorDocument() {
		return this.getParentDocument();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public final Database getAncestorDatabase() {
		return this.getAncestorDocument().getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return this.getAncestorDocument().getAncestorSession();
	}

	/* (non-Javadoc)
	 * @see lotus.domino.EmbeddedObject#getFileCreated()
	 */
	@Override
	public DateTime getFileCreated() {
		try {
			return getFactory().fromLotus(getDelegate().getFileCreated(), DateTime.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see lotus.domino.EmbeddedObject#getFileModified()
	 */
	@Override
	public DateTime getFileModified() {
		try {
			return getFactory().fromLotus(getDelegate().getFileModified(), DateTime.SCHEMA, getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}
