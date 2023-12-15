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
package org.openntf.domino;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface Stream.
 */
public interface Stream
		extends Base<lotus.domino.Stream>, lotus.domino.Stream, org.openntf.domino.ext.Stream, SessionDescendant, Closeable {

	public static class Schema extends FactorySchema<Stream, lotus.domino.Stream, Session> {
		@Override
		public Class<Stream> typeClass() {
			return Stream.class;
		}

		@Override
		public Class<lotus.domino.Stream> delegateClass() {
			return lotus.domino.Stream.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#close()
	 */
	@Override
	public void close();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#getBytes()
	 */
	@Override
	public int getBytes();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#getCharset()
	 */
	@Override
	public String getCharset();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#getContents(java.io.OutputStream)
	 */
	@Override
	public void getContents(final OutputStream stream);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#getContents(java.io.Writer)
	 */
	@Override
	public void getContents(final Writer writer);

	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	@Override
	public Session getParent();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#getPosition()
	 */
	@Override
	public int getPosition();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#isEOS()
	 */
	@Override
	public boolean isEOS();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#isReadOnly()
	 */
	@Override
	public boolean isReadOnly();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#open(java.lang.String)
	 */
	@Override
	public boolean open(final String pathName);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#open(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean open(final String pathName, final String charSet);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#read()
	 */
	@Override
	public byte[] read();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#read(int)
	 */
	@Override
	public byte[] read(final int length);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#readText()
	 */
	@Override
	public String readText();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#readText(int)
	 */
	@Override
	public String readText(final int length);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#readText(int, int)
	 */
	@Override
	public String readText(final int length, final int eolType);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#setContents(java.io.InputStream)
	 */
	@Override
	public void setContents(final InputStream stream);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#setContents(java.io.Reader)
	 */
	@Override
	public void setContents(final Reader reader);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#setPosition(int)
	 */
	@Override
	public void setPosition(final int position);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#truncate()
	 */
	@Override
	public void truncate();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#write(byte[])
	 */
	@Override
	public int write(final byte[] buffer);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#writeText(java.lang.String)
	 */
	@Override
	public int writeText(final String text);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.Stream#writeText(java.lang.String, int)
	 */
	@Override
	public int writeText(final String text, final int eolType);
}
