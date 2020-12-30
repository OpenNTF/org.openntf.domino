/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.utils;

import java.io.IOException;
import java.io.InputStream;

import lotus.domino.NotesException;
import lotus.domino.Stream;

/**
 * Utility enum as a carrier for Stream-centric static properties and methods.
 * 
 */
@SuppressWarnings("nls")
public enum Streams {
	;

	public static class MIMEBufferedInputStream extends InputStream {
		private static final int DEFAULT_BUFFER_SIZE = 16384;

		private static final boolean FORCE_READ = false;
		//		private static int instanceCount = 0;

		private Stream is;
		private byte[] buffer;
		private int length;
		private int buffered;
		private int bufferPos;

		public static InputStream get(final Stream source) {
			return source != null ? new MIMEBufferedInputStream(source) : null;
		}

		public MIMEBufferedInputStream(final Stream is, final int size) {
			try {
				this.is = is;
				this.buffer = new byte[size];
				this.length = is.getBytes();
			} catch (NotesException ne) {
				DominoUtils.handleException(ne);
				throw new RuntimeException(MIMEBufferedInputStream.class.getName() + " Constructor Failure");
			}
			//			instanceCount++;
			//			if (++instanceCount % 1000 == 0) {
			//				System.out.println("Created " + instanceCount + " MIMEInputStream objects...");
			//			}
		}

		public MIMEBufferedInputStream(final Stream is) {
			this(is, DEFAULT_BUFFER_SIZE);
		}

		public boolean isEOF() throws IOException {
			return getNextByteCount() == 0;
		}

		@Override
		public int read() throws IOException {
			if (bufferPos == buffered) {
				//we've read to the end of the buffer byte-by-byte.
				//time to refill...
				buffered = getNextByteCount();
				buffer = Streams.readStream(is, buffered);

				bufferPos = 0;
				if (buffered <= 0) {
					buffered = 0;
					return -1;
				}
			}

			return buffer[bufferPos++] & 0xFF;
		}

		@Override
		public int read(final byte[] array) throws IOException {
			return read(array, 0, array.length);
		}

		@Override
		public int read(final byte[] array, int off, int length) throws IOException {
			if (FORCE_READ) {
				int read = 0;
				while (read < length) {
					int r = _read(array, off, length);
					if (r < 0) {
						return read > 0 ? read : r;
					}
					off += r;
					length -= r;
					read += r;
				}
				return read;
			} else {
				return _read(array, off, length);
			}
		}

		private int getNextByteCount() {
			int remaining;
			int position = Streams.getStreamPosition(is);
			if (position < 0) {
				return 0;
			}

			remaining = length - position;
			return (remaining < buffer.length) ? remaining : buffer.length;

		}

		private int _read(final byte[] array, final int off, final int length) throws IOException {
			int avail = buffered - bufferPos;
			if (avail == 0) {
				buffered = getNextByteCount();
				buffer = Streams.readStream(is, buffered);
				avail = buffered;
				bufferPos = 0;
				if (buffered <= 0) {
					buffered = 0;
					return -1;
				}
			}
			int toRead = length < avail ? length : avail;
			System.arraycopy(buffer, bufferPos, array, off, toRead);
			bufferPos += toRead;
			return toRead;
		}

		@Override
		public long skip(long n) throws IOException {
			if (FORCE_READ) {
				long skip = 0;
				while (skip < n) {
					long r = _skip(n);
					if (r < 0) {
						return skip > 0 ? skip : r;
					}
					n -= r;
					skip += r;
				}
				return skip;
			} else {
				return _skip(n);
			}
		}

		private long _skip(final long n) throws IOException {
			int avail = buffered - bufferPos;
			if (avail > 0) {
				if (n < avail) {
					bufferPos += n;
					return n;
				}
				bufferPos = buffered;
				return avail;
			}
			final int position = Streams.getStreamPosition(is);
			if (position < 0) {
				throw new IOException("Unable to get Stream Position");
			}

			long newPos = position + n;
			if (newPos > length) {
				return 0;
			} else {
				int intPos = Integer.parseInt(Long.toString(newPos));
				if (!Streams.setStreamPosition(is, intPos)) {
					throw new IOException("Unable to set Stream Position to " + newPos);
				}

				return n;
			}
		}

		@Override
		public int available() throws IOException {
			final int position = Streams.getStreamPosition(is);
			if (position < 0) {
				throw new IOException("Unable to get Stream Position");
			}
			return length - position;
		}

		@Override
		public void close() throws IOException {
			try {
				is.close();
			} catch (NotesException ne) {
				DominoUtils.handleException(ne);
				throw new IOException("Unable to close stream");
			}
		}

		@Override
		public void mark(final int int0) {
		}

		@Override
		public void reset() throws IOException {
		}

		@Override
		public boolean markSupported() {
			return false;
		}
	}

	public static int getStreamBytes(final Stream stream) {
		try {
			return stream.getBytes();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
		return 0;
	}

	public static byte[] readStream(final Stream stream) {
		try {
			return stream.read();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	public static byte[] readStream(final Stream stream, final int length) {
		try {
			return stream.read(length);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	public static int getStreamPosition(final Stream stream) {
		try {
			return stream.getPosition();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return -1;
		}
	}

	public static boolean setStreamPosition(final Stream stream, final int position) {
		try {
			stream.setPosition(position);
			return true;
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return false;
		}
	}
}
