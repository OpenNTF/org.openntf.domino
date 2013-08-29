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
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.openntf.domino.logging.LogUtils;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * The Enum DominoUtils.
 */
public enum DominoUtils {
	;

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
		private final ClassLoader loader_;

		public LoaderObjectInputStream(final ClassLoader classLoader, final InputStream in) throws IOException {
			super(in);
			loader_ = classLoader;
		}

		@Override
		protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
			try {
				String name = desc.getName();
				return Class.forName(name, false, loader_);
			} catch (ClassNotFoundException e) {
				return super.resolveClass(desc);
			}
		}
	}

	/** The Constant log_. */
	private final static Logger log_ = Logger.getLogger("org.openntf.domino");

	/** The Constant logBackup_. */
	private final static Logger logBackup_ = Logger.getLogger("org.openntf.domino");

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
			if (getBubbleExceptions()) {
				throw new RuntimeException(t);
			}
			return t;
		} catch (Throwable e) {
			e.printStackTrace();
			return t;
		}

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
	public static Object restoreState(final org.openntf.domino.Document doc, final String itemName) throws Throwable {
		Session session = Factory.getSession((Base<?>) doc);
		boolean convertMime = session.isConvertMime();
		session.setConvertMime(false);

		Object result = null;
		Stream mimeStream = session.createStream();
		MIMEEntity entity = doc.getMIMEEntity(itemName);
		if (entity == null) {
			return null;
		}
		Class<?> chkClass = null;
		String className = entity.getNthHeader("X-Java-Class").getHeaderVal();
		ClassLoader cl = Factory.getClassLoader();
		try {
			chkClass = (Class<?>) Class.forName(className, true, cl);
		} catch (Throwable t) {
			log_.log(Level.SEVERE, "Unable to load class " + className + " from a ClassLoader of " + cl.getClass().getName()
					+ " so object deserialization is likely to fail...");
		}
		if (chkClass == null) {
			log_.log(Level.SEVERE, "Unable to load class " + className + " from a ClassLoader of " + cl.getClass().getName()
					+ " so object deserialization is likely to fail...");
		}

		entity.getContentAsBytes(mimeStream);

		ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
		mimeStream.getContents(streamOut);
		// mimeStream.recycle();

		byte[] stateBytes = streamOut.toByteArray();
		ByteArrayInputStream byteStream = new ByteArrayInputStream(stateBytes);
		ObjectInputStream objectStream;
		if (entity.getHeaders().toLowerCase().contains("content-encoding: gzip")) {
			GZIPInputStream zipStream = new GZIPInputStream(byteStream);
			objectStream = new LoaderObjectInputStream(cl, zipStream);
		} else {
			objectStream = new LoaderObjectInputStream(cl, byteStream);
		}

		// There are three potential storage forms: Externalizable, Serializable, and StateHolder, distinguished by type or header
		if (entity.getContentSubType().equals("x-java-externalized-object")) {
			Class<Externalizable> externalizableClass = (Class<Externalizable>) Class.forName(entity.getNthHeader("X-Java-Class")
					.getHeaderVal(), true, Factory.getClassLoader());
			Externalizable restored = externalizableClass.newInstance();
			restored.readExternal(objectStream);
			result = restored;
		} else {

			Object restored = (Serializable) objectStream.readObject();

			// But wait! It might be a StateHolder object or Collection!
			MIMEHeader storageScheme = entity.getNthHeader("X-Storage-Scheme");
			MIMEHeader originalJavaClass = entity.getNthHeader("X-Original-Java-Class");
			if (storageScheme != null && storageScheme.getHeaderVal().equals("StateHolder")) {
				Class<?> facesContextClass = Class.forName("javax.faces.context.FacesContext", true, Factory.getClassLoader());
				Method getCurrentInstance = facesContextClass.getMethod("getCurrentInstance");

				Class<?> stateHoldingClass = (Class<?>) Class.forName(originalJavaClass.getHeaderVal(), true, Factory.getClassLoader());
				Method restoreStateMethod = stateHoldingClass.getMethod("restoreState", facesContextClass, Object.class);
				result = stateHoldingClass.newInstance();
				restoreStateMethod.invoke(result, getCurrentInstance.invoke(null), restored);
			} else if (originalJavaClass != null && originalJavaClass.getHeaderVal().equals("org.openntf.domino.DocumentCollection")) {
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
			} else if (originalJavaClass != null && originalJavaClass.getHeaderVal().equals("org.openntf.domino.NoteCollection")) {
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
	public static void saveState(final Serializable object, final Document doc, final String itemName) throws Throwable {
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
			final Map<String, String> headers) throws Throwable {
		if (object == null) {
			System.out.println("Ignoring attempt to save MIMEBean value of null");
			return;
		}
		Session session = Factory.getSession((Base<?>) doc);
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
		byte[] bytes = byteStream.toByteArray();
		ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);

		mimeStream.setContents(byteIn);
		entity.setContentFromBytes(mimeStream, contentType, MIMEEntity.ENC_NONE);
		MIMEHeader javaClass = entity.getNthHeader("X-Java-Class");
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
		MIMEHeader contentEncoding = entity.getNthHeader("Content-Encoding");
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

		// entity.recycle();
		// mimeStream.recycle();
		entity = null;
		previousState = null;
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
		return value.replace("\\", "\\\\").replace("\"", "\\\"");
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
}
