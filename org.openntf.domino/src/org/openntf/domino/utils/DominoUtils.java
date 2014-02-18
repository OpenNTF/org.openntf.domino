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
package org.openntf.domino.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.openntf.domino.Base;
import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Item;
import org.openntf.domino.MIMEEntity;
import org.openntf.domino.MIMEHeader;
import org.openntf.domino.Name;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.Stream;
import org.openntf.domino.exceptions.InvalidNotesUrlException;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.logging.LogUtils;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * The Enum DominoUtils.
 */
public enum DominoUtils {
	;

	public static class MIMEBufferedInputStream extends InputStream {
		private static final int DEFAULT_BUFFER_SIZE = 16384;

		private static final boolean FORCE_READ = false;
		private static int instanceCount = 0;

		private Stream is;
		private byte[] buffer;
		private int length;
		private int buffered;
		private int bufferPos;

		public static InputStream get(final Stream source) {
			return source != null ? new MIMEBufferedInputStream(source) : null;
		}

		public MIMEBufferedInputStream(final Stream is, final int size) {
			this.is = is;
			this.buffer = new byte[size];
			this.length = is.getBytes();
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
				buffer = is.read(buffered);
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
			int remaining = length - is.getPosition();
			if (remaining < buffer.length) {
				return remaining;
			} else {
				return buffer.length;
			}
		}

		private int _read(final byte[] array, final int off, final int length) throws IOException {
			int avail = buffered - bufferPos;
			if (avail == 0) {
				buffered = getNextByteCount();
				buffer = is.read(buffered);
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
				if (n < (long) avail) {
					bufferPos += n;
					return n;
				}
				bufferPos = buffered;
				return avail;
			}
			long newPos = is.getPosition() + n;
			if (newPos > length) {
				return 0;
			} else {
				int intPos = Integer.parseInt(Long.toString(newPos));
				is.setPosition(intPos);
				return n;
			}
		}

		@Override
		public int available() throws IOException {
			return length - is.getPosition();
		}

		@Override
		public void close() throws IOException {
			is.close();
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

	public static Class<?> getClass(final String className) {
		Class<?> result = null;
		try {
			result = AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
				@Override
				public Class<?> run() throws Exception {
					Class<?> result = null;
					ClassLoader cl = Thread.currentThread().getContextClassLoader();
					try {
						result = cl.loadClass(className);
					} catch (NullPointerException ne) {
						if (cl != null && "com.ibm.domino.xsp.module.nsf.ModuleClassLoader".equals(cl.getClass().getName())) {
							//							log_.log(Level.WARNING,
							//									"ModuleClassLoader lost DynamicClassLoader pointer. Resorting to System ClassLoader for " + className
							//											+ " instead...");
							result = Class.forName(className);
						}
					}
					return result;
				}
			});
		} catch (AccessControlException e) {
			e.printStackTrace();
		} catch (PrivilegedActionException e) {
			e.printStackTrace();
		}
		if (result == null) {
			log_.log(Level.WARNING, "Unable to resolve class " + className + " Please check logs for more details.");
		}
		return result;
	}

	private static ThreadLocal<Boolean> bubbleExceptions_ = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
	};

	public static Boolean getBubbleExceptions() {
		if (bubbleExceptions_.get() == null) {
			setBubbleExceptions(Boolean.FALSE);
		}
		return bubbleExceptions_.get();
	}

	public static void setBubbleExceptions(final Boolean value) {
		bubbleExceptions_.set(value);
	}

	public static class LoaderObjectInputStream extends ObjectInputStream {
		//		private final ClassLoader loader_;

		public LoaderObjectInputStream(final InputStream in) throws IOException {
			super(in);
			//			loader_ = null;
		}

		//		public LoaderObjectInputStream(final ClassLoader classLoader, final InputStream in) throws IOException {
		//			super(in);
		//			loader_ = classLoader;
		//		}

		@Override
		protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
			String name = desc.getName();
			//			if (loader_ == null) {
			return DominoUtils.getClass(name);
			//			}
			//			try {
			//				return Class.forName(name, false, loader_);
			//			} catch (ClassNotFoundException e) {
			//				return super.resolveClass(desc);
			//			}
		}
	}

	/** The Constant log_. */
	private final static Logger log_ = Logger.getLogger("org.openntf.domino");

	/** The Constant logBackup_. */
	private final static Logger logBackup_ = Logger.getLogger("com.ibm.xsp.domino");

	/**
	 * Checksum.
	 * 
	 * @param bytes
	 *            the bytes
	 * @param alg
	 *            the alg
	 * @return the string
	 */
	public static String checksum(final byte[] bytes, final String alg) {
		String hashed = "";
		byte[] defaultBytes = bytes;
		try {
			MessageDigest algorithm = MessageDigest.getInstance(alg);
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte[] messageDigest = algorithm.digest();
			BigInteger bi = new BigInteger(1, messageDigest);

			// StringBuffer hexString = new StringBuffer();
			// for (byte element : messageDigest) {
			// String hex = Integer.toHexString(0xFF & element);
			// if (hex.length() == 1) {
			// hexString.append('0');
			// }
			// hexString.append(hex);
			// }

			hashed = bi.toString(16);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
		return hashed;
	}

	/**
	 * Checks if is number.
	 * 
	 * @param value
	 *            the value
	 * @return true, if is number
	 */
	public static boolean isNumber(final String value) {
		boolean seenDot = false;
		boolean seenExp = false;
		boolean justSeenExp = false;
		boolean seenDigit = false;
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c >= '0' && c <= '9') {
				seenDigit = true;
				continue;
			}
			if ((c == '-' || c == '+') && (i == 0 || justSeenExp)) {
				continue;
			}
			if (c == '.' && !seenDot) {
				seenDot = true;
				continue;
			}
			justSeenExp = false;
			if ((c == 'e' || c == 'E') && !seenExp) {
				seenExp = true;
				justSeenExp = true;
				continue;
			}
			return false;
		}
		if (!seenDigit) {
			return false;
		}
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Checksum.
	 * 
	 * @param object
	 *            the object
	 * @param algorithm
	 *            the algorithm
	 * @return the string
	 */
	public static String checksum(final Serializable object, final String algorithm) {
		String result = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(object);
			result = DominoUtils.checksum(baos.toByteArray(), algorithm);
			out.close();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
		return result;
	}

	/**
	 * Handle exception.
	 * 
	 * @param t
	 *            the t
	 * @return the throwable
	 */
	public static Throwable handleException(final Throwable t) {
		return (handleException(t, null));
	}

	public static Throwable handleException(final Throwable t, final String details) {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					if (log_.getLevel() == null) {
						LogUtils.loadLoggerConfig(false, "");
					}
					if (log_.getLevel() == null) {
						log_.setLevel(Level.WARNING);
					}
					return null;
				}
			});
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				@Override
				public Object run() throws Exception {
					if (LogUtils.hasAccessException(log_)) {
						logBackup_.log(Level.SEVERE, t.getLocalizedMessage(), t);
					} else {
						log_.log(Level.WARNING, t.getLocalizedMessage(), t);
					}
					return null;
				}
			});
		} catch (Throwable e) {
			e.printStackTrace();
		}

		if (getBubbleExceptions()) {
			throw new OpenNTFNotesException(t);
		}
		return t;
	}

	/**
	 * Gets the unid from notes url.
	 * 
	 * @param notesurl
	 *            the notesurl
	 * @return the unid from notes url
	 */
	public static String getUnidFromNotesUrl(final String notesurl) {
		String result = null;
		String trimmed = notesurl.toLowerCase().trim();
		if (trimmed.startsWith("notes://")) {
			int arg = trimmed.lastIndexOf('?');
			if (arg == -1) { // there's no ? so we'll just start from the end
				String chk = trimmed.substring(trimmed.length() - 32, trimmed.length());
				if (isUnid(chk)) {
					result = chk;
				} else {
					System.out.println("Not a unid. We got " + chk);
				}
			} else {
				String chk = trimmed.substring(0, arg);
				chk = chk.substring(chk.length() - 32, chk.length());
				// String chk = trimmed.substring(trimmed.length() - 32 - (arg + 1), trimmed.length() - (arg + 1));
				if (isUnid(chk)) {
					result = chk;
				} else {
					System.out.println("Not a unid. We got " + chk);
				}
			}
		} else {
			throw new InvalidNotesUrlException(notesurl);
		}

		return result;
	}

	/**
	 * Incinerate.
	 * 
	 * @param args
	 *            the args
	 */
	public static void incinerate(final Object... args) {
		for (Object o : args) {
			if (o != null) {
				if (o instanceof lotus.domino.Base) {
					// try {
					// ((Base) o).recycle();
					// } catch (Throwable t) {
					// // who cares?
					// }
					org.openntf.domino.impl.Base.s_recycle(o);
				} else if (o instanceof Map) {
					Set<Map.Entry> entries = ((Map) o).entrySet();
					for (Map.Entry<?, ?> entry : entries) {
						incinerate(entry.getKey(), entry.getValue());
					}
				} else if (o instanceof Collection) {
					Iterator i = ((Collection) o).iterator();
					while (i.hasNext()) {
						Object obj = i.next();
						incinerate(obj);
					}

				} else if (o.getClass().isArray()) {
					try {
						Object[] objs = (Object[]) o;
						for (Object ao : objs) {
							incinerate(ao);
						}
					} catch (Throwable t) {
						// who cares?
					}
				}
			}
		}
	}

	public static Pattern IS_HIERARCHICAL_MATCH = Pattern.compile("^((CN=)|(O=)|(OU=)|(C=))[^/]+", Pattern.CASE_INSENSITIVE);

	public static Pattern CN_MATCH = Pattern.compile("^(CN=)[^/]+", Pattern.CASE_INSENSITIVE);

	public static Pattern OU_MATCH = Pattern.compile("(OU=)[^/]+", Pattern.CASE_INSENSITIVE);

	public static Pattern O_MATCH = Pattern.compile("(O=)[^/]+", Pattern.CASE_INSENSITIVE);

	public static Pattern C_MATCH = Pattern.compile("(C=)[^/]+", Pattern.CASE_INSENSITIVE);

	public static boolean isHierarchicalName(final String name) {
		if (name == null)
			return false;
		return IS_HIERARCHICAL_MATCH.matcher(name).find();
	}

	public static String toAbbreviatedName(final String name) {
		if (isHierarchicalName(name)) {
			StringBuilder builder = new StringBuilder();
			boolean isFirst = true;
			String cn = toCommonName(name);
			if (cn.length() > 0) {
				isFirst = false;
				builder.append(cn);
			}
			String ouString = toOUString(name);
			if (ouString.length() > 0) {
				if (!isFirst) {
					builder.append('/');
				}
				isFirst = false;
				builder.append(toOUString(name));
			}
			//			String[] ous = toOU(name);
			//			if (ous.length > 0) {
			//				for (String ou : ous) {
			//					if (ou.length() > 0) {
			//						if (!isFirst)
			//							builder.append('/');
			//						isFirst = false;
			//						builder.append(ou);
			//					}
			//				}
			//			}
			String o = toOrgName(name);
			if (o.length() > 0) {
				if (!isFirst)
					builder.append('/');
				isFirst = false;
				builder.append(o);
			}
			String c = toCountry(name);
			if (c.length() > 0) {
				if (!isFirst)
					builder.append('/');
				isFirst = false;
				builder.append(c);
			}
			return builder.toString();
		} else {
			return name;
		}
	}

	public static String toCommonName(final String name) {
		if (isHierarchicalName(name)) {
			Matcher m = CN_MATCH.matcher(name);
			if (m.find()) {
				int start = m.start() + 3;
				int end = m.end();
				if (start < end) {
					return name.substring(start, end);
				} else {
					return name;
				}
			} else {
				return "";
			}
		} else {
			return name;
		}
	}

	public static String toOrgName(final String name) {
		if (isHierarchicalName(name)) {
			Matcher m = O_MATCH.matcher(name);
			if (m.find()) {
				int start = m.start() + 2;
				int end = m.end();
				if (start < end) {
					return name.substring(start, end);
				} else {
					return name;
				}
			} else {
				return "";
			}
		} else {
			return name;
		}
	}

	public static String toOUString(final String name) {
		if (isHierarchicalName(name)) {
			Matcher m = OU_MATCH.matcher(name);
			StringBuilder builder = new StringBuilder();
			int i = 0;
			while (m.find()) {
				int start = m.start() + 3;
				int end = m.end();
				if (start < end) {
					if (i > 0) {
						builder.append('/');
					}
					builder.append(name.substring(start, end));
					i++;
				}
			}
			if (i == 0) {
				return "";
			} else {
				return builder.toString();
			}
		} else {
			return "";
		}
	}

	public static String[] toOU(final String name) {
		if (isHierarchicalName(name)) {
			Matcher m = OU_MATCH.matcher(name);
			String[] ous = new String[4];	//maximum number of OUs according to spec
			int i = 0;
			while (m.find()) {
				int start = m.start() + 3;
				int end = m.end();
				if (start < end) {
					ous[i++] = name.substring(start, end);
				}
			}
			if (i == 0) {
				return new String[0];
			} else {
				String[] result = new String[i];
				System.arraycopy(ous, 0, result, 0, i);
				return result;
			}
		} else {
			return new String[0];
		}
	}

	public static String toCountry(final String name) {
		if (isHierarchicalName(name)) {
			Matcher m = C_MATCH.matcher(name);
			if (m.find()) {
				int start = m.start() + 2;
				int end = m.end();
				if (start < end) {
					return name.substring(start, end);
				} else {
					return name;
				}
			} else {
				return "";
			}
		} else {
			return name;
		}
	}

	public static String toNameType(final String name, final Name.NameType type) {
		switch (type) {
		case COMMON:
			return toCommonName(name);
		case ABBREVIATED:
			return toAbbreviatedName(name);
		case CANONICAL:
			return name;
		case ORG:
			return toOrgName(name);
		case ORGUNIT:
			return toOUString(name);
		case COUNTRY:
			return toCountry(name);
		}
		return name;
	}

	public static Map<String, String> mapNames(final Collection<String> names, final Name.NameType keyType, final Name.NameType valueType) {
		if (names != null) {
			Map<String, String> result = new LinkedHashMap<String, String>();
			for (String name : names) {
				String key = toNameType(name, keyType);
				String value = toNameType(name, valueType);
				result.put(key, value);
			}
			return result;
		} else {
			return null;
		}
	}

	public static List<String> toSelectionList(final Collection<String> names, final Name.NameType firstType, final Name.NameType secondType) {
		return toSelectionList(names, firstType, secondType, "|");
	}

	public static List<String> toSelectionList(final Collection<String> names, final Name.NameType firstType,
			final Name.NameType secondType, final CharSequence separator) {
		if (names != null) {
			List<String> result = new LinkedList<String>();
			for (String name : names) {
				String key = toNameType(name, firstType);
				String value = toNameType(name, secondType);
				result.add(key + separator + value);
			}
			return result;
		} else {
			return null;
		}
	}

	/**
	 * Checks if is hex.
	 * 
	 * @param value
	 *            the value
	 * @return true, if is hex
	 */
	public static boolean isHex(final String value) {
		String chk = value.trim().toLowerCase();
		for (int i = 0; i < chk.length(); i++) {
			char c = chk.charAt(i);
			boolean isHexDigit = Character.isDigit(c) || Character.isWhitespace(c) || c == 'a' || c == 'b' || c == 'c' || c == 'd'
					|| c == 'e' || c == 'f';

			if (!isHexDigit) {
				return false;
			}

		}
		return true;
	}

	/**
	 * Checks if is unid.
	 * 
	 * @param value
	 *            the value
	 * @return true, if is unid
	 */
	public static boolean isUnid(final String value) {
		if (value.length() != 32)
			return false;
		return DominoUtils.isHex(value);
	}

	/**
	 * Md5.
	 * 
	 * @param object
	 *            the object
	 * @return the string
	 */
	public static String md5(final Serializable object) {
		return DominoUtils.checksum(object, "MD5");
	}

	/**
	 * To unid.
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 */
	public static String toUnid(final Serializable value) {
		if (value instanceof String && DominoUtils.isUnid((String) value))
			return (String) value;
		String hash = DominoUtils.md5(value);
		while (hash.length() < 32) {
			hash = "0" + hash;
		}
		return hash.toUpperCase();
	}

	/**
	 * To java calendar safe.
	 * 
	 * @param dt
	 *            the dt
	 * @return the calendar
	 */
	public static Calendar toJavaCalendarSafe(final lotus.domino.DateTime dt) {
		Date d = DominoUtils.toJavaDateSafe(dt);
		Calendar c = Calendar.getInstance(ULocale.getDefault());
		c.setTime(d);
		return c;
	}

	/**
	 * To java date.
	 * 
	 * @param l
	 *            the l
	 * @return the date
	 */
	public static Date toJavaDate(final long l) {
		Date result = new Date();
		result.setTime(l);
		return result;
	}

	/**
	 * To java date.
	 * 
	 * @param ls
	 *            the ls
	 * @return the collection
	 */
	public static Collection<Date> toJavaDate(final long[] ls) {
		Collection<Date> result = new ArrayList<Date>();
		for (long l : ls) {
			result.add(DominoUtils.toJavaDate(l));
		}
		return result;
	}

	/**
	 * To java date safe.
	 * 
	 * @param dt
	 *            the dt
	 * @return the date
	 */
	public static Date toJavaDateSafe(final lotus.domino.DateTime dt) {
		Date date = null;
		if (dt != null) {
			if (dt instanceof org.openntf.domino.DateTime) {
				date = ((org.openntf.domino.DateTime) dt).toJavaDate(); // no need to recycle 'cause it's not toxic
			} else {
				try {
					date = dt.toJavaDate();
				} catch (Throwable t) {
					t.printStackTrace();
				} finally {
					DominoUtils.incinerate(dt);
				}
			}
		}
		return date;
	}

	// MIMEBean methods

	@SuppressWarnings("unchecked")
	public static Object restoreState(final org.openntf.domino.Document doc, final String itemName, final MIMEEntity entity)
			throws Exception {
		Session session = doc.getAncestorSession();
		Object result = null;
		Stream mimeStream = session.createStream();
		Class<?> chkClass = null;
		String allHeaders = entity.getHeaders();
		MIMEHeader header = entity.getNthHeader("X-Java-Class");
		if (header != null) {
			String className = header.getHeaderVal();
			chkClass = getClass(className);
			if (chkClass == null) {
				log_.log(Level.SEVERE, "Unable to load class " + className + " from currentThread classLoader"
						+ " so object deserialization is likely to fail...");
			}
		}

		entity.getContentAsBytes(mimeStream);

		mimeStream.setPosition(0);
		//		ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
		//		mimeStream.getContents(streamOut);
		// mimeStream.recycle();

		//		byte[] stateBytes = streamOut.toByteArray();
		//		ByteArrayInputStream byteStream = new ByteArrayInputStream(stateBytes);
		InputStream is = new MIMEBufferedInputStream(mimeStream);
		ObjectInputStream objectStream;

		if (allHeaders == null) {
			System.out.println("No headers available. Testing gzip by experimentation...");
			try {
				GZIPInputStream zipStream = new GZIPInputStream(is);
				objectStream = new LoaderObjectInputStream(zipStream);
			} catch (Exception ioe) {
				objectStream = new LoaderObjectInputStream(is);
			}
		} else if (allHeaders.toLowerCase().contains("content-encoding: gzip")) {
			//			GZIPInputStream zipStream = new GZIPInputStream(byteStream);
			GZIPInputStream zipStream = new GZIPInputStream(is);
			objectStream = new LoaderObjectInputStream(zipStream);
		} else {
			objectStream = new LoaderObjectInputStream(is);
		}

		// There are three potential storage forms: Externalizable, Serializable, and StateHolder, distinguished by type or header
		if ("x-java-externalized-object".equals(entity.getContentSubType())) {
			Class<Externalizable> externalizableClass = (Class<Externalizable>) getClass(entity.getNthHeader("X-Java-Class").getHeaderVal());
			Externalizable restored = externalizableClass.newInstance();
			restored.readExternal(objectStream);
			result = restored;
		} else {
			Object restored = (Serializable) objectStream.readObject();

			// But wait! It might be a StateHolder object or Collection!
			MIMEHeader storageScheme = entity.getNthHeader("X-Storage-Scheme");
			MIMEHeader originalJavaClass = entity.getNthHeader("X-Original-Java-Class");
			if (storageScheme != null && "StateHolder".equals(storageScheme.getHeaderVal())) {
				Class<?> facesContextClass = getClass("javax.faces.context.FacesContext");
				Method getCurrentInstance = facesContextClass.getMethod("getCurrentInstance");

				Class<?> stateHoldingClass = getClass(originalJavaClass.getHeaderVal());
				Method restoreStateMethod = stateHoldingClass.getMethod("restoreState", facesContextClass, Object.class);
				result = stateHoldingClass.newInstance();
				restoreStateMethod.invoke(result, getCurrentInstance.invoke(null), restored);
			} else if (originalJavaClass != null && "org.openntf.domino.DocumentCollection".equals(originalJavaClass.getHeaderVal())) {
				// Maybe this can be sped up by not actually getting the documents
				try {
					String[] unids = (String[]) restored;
					Database db = doc.getParentDatabase();
					DocumentCollection docCollection = db.createDocumentCollection();
					for (String unid : unids) {
						docCollection.addDocument(db.getDocumentByUNID(unid));
					}
					result = docCollection;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (originalJavaClass != null && "org.openntf.domino.NoteCollection".equals(originalJavaClass.getHeaderVal())) {
				String[] unids = (String[]) restored;
				Database db = doc.getParentDatabase();
				NoteCollection noteCollection = db.createNoteCollection(false);
				for (String unid : unids) {
					noteCollection.add(db.getDocumentByUNID(unid));
				}
				result = noteCollection;
			} else {
				result = restored;
			}
		}

		// entity.recycle();
		if (!doc.closeMIMEEntities(false, itemName)) {
			log_.log(Level.WARNING, "closeMIMEEntities returned false for item " + itemName + " on doc " + doc.getNoteID() + " in db "
					+ doc.getAncestorDatabase().getApiPath());
		}

		return result;
	}

	/**
	 * Restore state.
	 * 
	 * @param doc
	 *            the doc
	 * @param itemName
	 *            the item name
	 * @return the serializable
	 * @throws Throwable
	 *             the throwable
	 */
	@SuppressWarnings("unchecked")
	public static Object restoreState(final org.openntf.domino.Document doc, final String itemName) throws Exception {
		Session session = Factory.getSession((Base<?>) doc);
		boolean convertMime = session.isConvertMime();
		session.setConvertMime(false);

		Object result = null;
		MIMEEntity entity = doc.getMIMEEntity(itemName);
		if (entity == null) {
			return null;
		}
		result = restoreState(doc, itemName, entity);
		session.setConvertMime(convertMime);

		return result;
	}

	/**
	 * Save state.
	 * 
	 * @param object
	 *            the object
	 * @param doc
	 *            the doc
	 * @param itemName
	 *            the item name
	 * @throws Throwable
	 *             the throwable
	 */
	public static void saveState(final Serializable object, final Document doc, final String itemName) throws Exception {
		saveState(object, doc, itemName, true, null);
	}

	// private static Map<String, Integer> diagCount = new HashMap<String, Integer>();

	/**
	 * Save state.
	 * 
	 * @param object
	 *            the object
	 * @param doc
	 *            the doc
	 * @param itemName
	 *            the item name
	 * @param compress
	 *            the compress
	 * @param headers
	 *            the headers
	 * @throws Throwable
	 *             the throwable
	 */
	public static void saveState(final Serializable object, final Document doc, final String itemName, final boolean compress,
			final Map<String, String> headers) throws Exception {
		if (object == null) {
			log_.log(Level.INFO, "Ignoring attempt to save MIMEBean value of null");
			return;
		}
		Session session = doc.getAncestorSession();
		boolean convertMime = session.isConvertMime();
		session.setConvertMime(false);

		// String diagKey = doc.getUniversalID() + itemName;
		// if (diagCount.containsKey(diagKey)) {
		// diagCount.put(diagKey, diagCount.get(diagKey) + 1);
		// } else {
		// diagCount.put(diagKey, 1);
		// }

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = compress ? new ObjectOutputStream(new GZIPOutputStream(byteStream)) : new ObjectOutputStream(
				byteStream);
		String contentType = null;
		// Prefer externalization if available
		if (object instanceof Externalizable) {
			((Externalizable) object).writeExternal(objectStream);
			contentType = "application/x-java-externalized-object";
		} else {
			objectStream.writeObject(object);
			contentType = "application/x-java-serialized-object";
		}

		objectStream.flush();
		objectStream.close();

		Stream mimeStream = session.createStream();
		MIMEEntity previousState = doc.getMIMEEntity(itemName);
		MIMEEntity entity = null;
		if (previousState == null) {
			Item itemChk = doc.getFirstItem(itemName);
			if (itemChk != null) {
				itemChk.remove();
			}
			entity = doc.createMIMEEntity(itemName);
		} else {
			entity = previousState;
		}
		MIMEHeader javaClass = entity.getNthHeader("X-Java-Class");
		MIMEHeader contentEncoding = entity.getNthHeader("Content-Encoding");
		if (javaClass == null) {
			javaClass = entity.createHeader("X-Java-Class");
		} else {
			// long jcid = org.openntf.domino.impl.Base.getDelegateId((org.openntf.domino.impl.Base) javaClass);
			// if (jcid < 1) {
			// System.out.println("EXISTING javaClassid: " + jcid);
			// System.out.println("Item: " + itemName + " in document " + doc.getUniversalID() + " (" + doc.getNoteID()
			// + ") update count: " + diagCount.get(diagKey));
			// }
		}
		try {
			javaClass.setHeaderVal(object.getClass().getName());
		} catch (Throwable t) {
			t.printStackTrace();
		}

		if (compress) {
			if (contentEncoding == null) {
				contentEncoding = entity.createHeader("Content-Encoding");
			}
			contentEncoding.setHeaderVal("gzip");

			// contentEncoding.recycle();
		} else {
			if (contentEncoding != null) {

				contentEncoding.remove();
				// contentEncoding.recycle();
			}
		}

		// javaClass.recycle();

		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				MIMEHeader paramHeader = entity.getNthHeader(entry.getKey());
				if (paramHeader == null) {
					paramHeader = entity.createHeader(entry.getKey());
				}
				paramHeader.setHeaderVal(entry.getValue());
				// paramHeader.recycle();
			}
		}
		byte[] bytes = byteStream.toByteArray();
		ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);

		mimeStream.setContents(byteIn);
		entity.setContentFromBytes(mimeStream, contentType, MIMEEntity.ENC_NONE);

		// entity.recycle();
		// mimeStream.recycle();
		//		entity = null;	//NTF - why set to null? We're properly closing the entities now.
		//		previousState = null;	// why set to null?
		if (!doc.closeMIMEEntities(true, itemName)) {
			log_.log(Level.WARNING, "closeMIMEEntities returned false for item " + itemName + " on doc " + doc.getNoteID() + " in db "
					+ doc.getAncestorDatabase().getApiPath());
		}
		session.setConvertMime(convertMime);
	}

	/**
	 * Gets the domino ini var.
	 * 
	 * @param propertyName
	 *            String property to retrieve from notes.ini
	 * @param defaultValue
	 *            String default to use if property is not found
	 * @return String return value from the notes.ini
	 */
	public static String getDominoIniVar(final String propertyName, final String defaultValue) {
		String newVal = Factory.getSession().getEnvironmentString(propertyName, true);
		if (!"".equals(newVal)) {
			return newVal;
		} else {
			return defaultValue;
		}
	}

	/**
	 * Gets properties file and returns as an InputStream.
	 * 
	 * @param fileType
	 *            int passed to switch statement. <br/>
	 *            1 -> name of a properties file in this package<br/>
	 *            2 -> literal path of a properties file<br/>
	 *            3 -> relative path of a properties file, relative to Domino <data> directory
	 * @param fileLoc
	 *            String filepath location of properties file
	 * @return InputStream (or BufferedInputStream) of properties file content
	 */
	public static InputStream getDominoProps(final int fileType, final String fileLoc) {
		InputStream returnStream = null;
		InputStream is;
		try {
			switch (fileType) {
			case 1:
				// Properties file in this package
				DominoUtils.class.getResourceAsStream(fileLoc);
				break;
			case 2:
				// File in file system at literal path
				is = new FileInputStream(fileLoc);
				returnStream = new BufferedInputStream(is);
				break;
			case 3:
				// File in file system relative to data directory
				String dirPath = getDominoIniVar("Directory", "");
				is = new FileInputStream(dirPath + "/" + fileLoc);
				returnStream = new BufferedInputStream(is);
				break;
			// TODO Need to work out how to get from properties file in NSF
			}
			return returnStream;
		} catch (Throwable e) {
			handleException(e);
			return returnStream;
		}
	}

	public static Item itemFromCalendar(final Item item, final Calendar cal) {
		DateTime dt = Factory.getSession(item).createDateTime(cal);
		item.setDateTimeValue(dt);
		DominoUtils.incinerate(dt);
		return item;
	}

	public static Item itemFromCalendarAppend(final Item item, final Calendar cal) {
		DateTime dt = Factory.getSession(item).createDateTime(cal);
		Vector<DateTime> v = item.getValueDateTimeArray();
		v.add(dt);
		item.setValues(v);
		DominoUtils.incinerate(dt);
		return item;
	}

	public static Item itemFromDate(final Item item, final Date cal) {
		DateTime dt = Factory.getSession(item).createDateTime(cal);
		item.setDateTimeValue(dt);
		DominoUtils.incinerate(dt);
		return item;
	}

	public static Item itemFromDateAppend(final Item item, final Date cal) {
		DateTime dt = Factory.getSession(item).createDateTime(cal);
		Vector<DateTime> v = item.getValueDateTimeArray();
		v.add(dt);
		DominoUtils.incinerate(dt);
		return item;
	}

	public static Calendar itemToCalendar(final Item item) {
		DateTime dt = item.getDateTimeValue();
		if (dt != null) {
			return DominoUtils.toJavaCalendarSafe(dt);
		} else {
			return null;
		}
	}

	public static Date itemToDate(final Item item) {
		DateTime dt = item.getDateTimeValue();
		if (dt != null) {
			return DominoUtils.toJavaDateSafe(dt);
		} else {
			return null;
		}
	}

	public static String escapeForFormulaString(final String value) {
		// I wonder if this is sufficient escaping
		return value.replace("{", "\\{").replace("}", "\\}");
	}

	public static boolean isSerializable(final Collection<?> values) {
		if (values == null)
			return false;
		boolean result = true;
		Iterator<?> it = values.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof DateTime) {
				return false;
			}
			if (!(o instanceof Serializable)) {
				result = false;
				break;
			}
		}
		return result;
	}

	public static String toNameString(final Name name) {
		String result = "";
		if (!name.isHierarchical()) {
			result = name.getCommon();
		} else {
			result = name.getCanonical();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static Collection<Comparable> toComparable(final Collection<?> values) {
		Collection<Serializable> colls = toSerializable(values);
		Collection<Comparable> result = new ArrayList<Comparable>();
		if (colls != null && !colls.isEmpty()) {
			for (Serializable ser : colls) {
				if (ser instanceof Comparable) {
					result.add((Comparable) ser);
				} else {
					log_.warning("Unable to convert to Comparable from " + ser.getClass().getName());
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Collection<Serializable> toSerializable(final Collection<?> values) {
		if (DominoUtils.isSerializable(values))
			return (Collection<Serializable>) values;
		Collection<Serializable> result = new ArrayList<Serializable>();
		if (values != null && !values.isEmpty()) {
			Iterator<?> it = values.iterator();

			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof DateTime) {
					Date date = null;
					DateTime dt = (DateTime) o;
					date = dt.toJavaDate();
					result.add(date);
				} else if (o instanceof Name) {
					result.add(DominoUtils.toNameString((Name) o));
				} else if (o instanceof String) {
					result.add((String) o);
				} else if (o instanceof Number) {
					result.add((Number) o);
				}
			}
		}
		return result;
	}

	public static String javaBinaryNameToFilePath(final String binaryName, final String separator) {
		return binaryName.replace(".", separator) + ".class";
	}

	public static String filePathToJavaBinaryName(final String filePath, final String separator) {
		return filePath.substring(0, filePath.length() - 6).replace(separator, ".");
	}
}
