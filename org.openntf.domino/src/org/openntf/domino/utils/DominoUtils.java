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
package org.openntf.domino.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import lotus.domino.Document;
import lotus.domino.MIMEEntity;
import lotus.domino.MIMEHeader;
import lotus.domino.Session;
import lotus.domino.Stream;

import org.openntf.domino.Base;
import org.openntf.domino.Database;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.exceptions.InvalidNotesUrlException;
import org.openntf.domino.logging.LogUtils;

//TODO: Auto-generated Javadoc
/**
 * The Enum DominoUtils.
 */
public enum DominoUtils {
	;
	private final static Logger log_ = Logger.getLogger("org.openntf.domino");
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
	public static String checksum(byte[] bytes, String alg) {
		String hashed = "";
		byte[] defaultBytes = bytes;
		try {
			MessageDigest algorithm = MessageDigest.getInstance(alg);
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();

			StringBuffer hexString = new StringBuffer();
			for (byte element : messageDigest) {
				String hex = Integer.toHexString(0xFF & element);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}

			hashed = hexString.toString();
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
		return hashed;
	}

	public static boolean isNumber(String value) {
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
	public static String checksum(Serializable object, String algorithm) {
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
	public static Throwable handleException(Throwable t) {
		try {
			if (log_.getLevel() == null) {
				LogUtils.loadLoggerConfig(false, "");
			}
			if (LogUtils.hasAccessException(log_)) {
				logBackup_.log(Level.SEVERE, t.getLocalizedMessage(), t);
			} else {
				log_.log(Level.WARNING, t.getLocalizedMessage(), t);
				t.printStackTrace();
			}
			return null;
		} catch (Throwable e) {
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
	public static String getUnidFromNotesUrl(String notesurl) {
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
	public static void incinerate(Object... args) {
		for (Object o : args) {
			if (o != null) {
				if (o instanceof lotus.domino.Base) {
					// try {
					// ((Base) o).recycle();
					// } catch (Throwable t) {
					// // who cares?
					// }
					org.openntf.domino.impl.Base.recycle(o);
				} else if (o instanceof Collection) {
					if (o instanceof Map) {
						Set<Map.Entry> entries = ((Map) o).entrySet();
						for (Map.Entry<?, ?> entry : entries) {
							DominoUtils.incinerate(entry.getKey(), entry.getValue());
						}
					} else {
						Iterator i = ((Collection) o).iterator();
						while (i.hasNext()) {
							Object obj = i.next();
							DominoUtils.incinerate(obj);
						}
					}
				} else if (o.getClass().isArray()) {
					try {
						Object[] objs = (Object[]) o;
						for (Object ao : objs) {
							DominoUtils.incinerate(ao);
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
	public static boolean isHex(String value) {
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
	public static boolean isUnid(String value) {
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
	public static String md5(Serializable object) {
		return DominoUtils.checksum(object, "MD5");
	}

	/**
	 * To unid.
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 */
	public static String toUnid(String value) {
		if (DominoUtils.isUnid(value))
			return value;
		return DominoUtils.md5(value);
	}

	/**
	 * To java calendar safe.
	 * 
	 * @param dt
	 *            the dt
	 * @return the calendar
	 */
	public static Calendar toJavaCalendarSafe(lotus.domino.DateTime dt) {
		Date d = DominoUtils.toJavaDateSafe(dt);
		Calendar c = GregorianCalendar.getInstance();
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
	public static Date toJavaDate(long l) {
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
	public static Collection<Date> toJavaDate(long[] ls) {
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
	public static Date toJavaDateSafe(lotus.domino.DateTime dt) {
		Date date = null;
		if (dt != null) {
			try {
				date = dt.toJavaDate();
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				DominoUtils.incinerate(dt);
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
	public static Object restoreState(org.openntf.domino.Document doc, String itemName) throws Throwable {
		Session session = Factory.getSession((Base<?>) doc);
		boolean convertMime = session.isConvertMime();
		session.setConvertMime(false);

		Object result = null;
		lotus.domino.Stream mimeStream = session.createStream();
		lotus.domino.MIMEEntity entity = doc.getMIMEEntity(itemName);
		if (entity == null) {
			return null;
		}
		entity.getContentAsBytes(mimeStream);

		ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
		mimeStream.getContents(streamOut);
		mimeStream.recycle();

		byte[] stateBytes = streamOut.toByteArray();
		ByteArrayInputStream byteStream = new ByteArrayInputStream(stateBytes);
		ObjectInputStream objectStream;
		if (entity.getHeaders().toLowerCase().contains("content-encoding: gzip")) {
			GZIPInputStream zipStream = new GZIPInputStream(byteStream);
			objectStream = new ObjectInputStream(zipStream);
		} else {
			objectStream = new ObjectInputStream(byteStream);
		}

		// There are three potential storage forms: Externalizable, Serializable, and StateHolder, distinguished by type or header
		if (entity.getContentSubType().equals("x-java-externalized-object")) {
			Class<Externalizable> externalizableClass = (Class<Externalizable>) Class.forName(entity.getNthHeader("X-Java-Class")
					.getHeaderVal());
			Externalizable restored = externalizableClass.newInstance();
			restored.readExternal(objectStream);
			result = restored;
		} else {
			Object restored = (Serializable) objectStream.readObject();

			// But wait! It might be a StateHolder object or Collection!
			MIMEHeader storageScheme = entity.getNthHeader("X-Storage-Scheme");
			MIMEHeader originalJavaClass = entity.getNthHeader("X-Original-Java-Class");
			if (storageScheme != null && storageScheme.getHeaderVal().equals("StateHolder")) {
				Class<?> facesContextClass = Class.forName("javax.faces.context.FacesContext");
				Method getCurrentInstance = facesContextClass.getMethod("getCurrentInstance");

				Class<?> stateHoldingClass = (Class<?>) Class.forName(originalJavaClass.getHeaderVal());
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

		entity.recycle();

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
	public static void saveState(Serializable object, Document doc, String itemName) throws Throwable {
		saveState(object, doc, itemName, true, null);
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
	 * @param compress
	 *            the compress
	 * @throws Throwable
	 *             the throwable
	 */
	public static void saveState(Serializable object, Document doc, String itemName, boolean compress, Map<String, String> headers)
			throws Throwable {
		Session session = Factory.getSession((Base<?>) doc);
		boolean convertMime = session.isConvertMime();
		session.setConvertMime(false);

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
		MIMEEntity entity = previousState == null ? doc.createMIMEEntity(itemName) : previousState;
		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteStream.toByteArray());
		mimeStream.setContents(byteIn);
		entity.setContentFromBytes(mimeStream, contentType, MIMEEntity.ENC_NONE);
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
		MIMEHeader javaClass = entity.getNthHeader("X-Java-Class");
		if (javaClass == null) {
			javaClass = entity.createHeader("X-Java-Class");
		}
		javaClass.setHeaderVal(object.getClass().getName());
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

		session.setConvertMime(convertMime);
	}

	/**
	 * @param propertyName
	 *            String property to retrieve from notes.ini
	 * @param defaultValue
	 *            String default to use if property is not found
	 * @return String return value from the notes.ini
	 */
	public static String getDominoIniVar(String propertyName, String defaultValue) {
		String newVal = Factory.getSession().getEnvironmentString(propertyName, true);
		if (!"".equals(newVal)) {
			return newVal;
		} else {
			return defaultValue;
		}
	}

	/**
	 * Gets properties file and returns as an InputStream
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
	public static InputStream getDominoProps(int fileType, String fileLoc) {
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
}
