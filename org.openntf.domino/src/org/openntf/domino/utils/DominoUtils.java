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
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Formatter;
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

import org.openntf.domino.DateTime;
import org.openntf.domino.Item;
import org.openntf.domino.Name;
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

	public static final String VIEWNAME_VIM_PEOPLE_AND_GROUPS = "($VIMPeopleAndGroups)";
	public static final String VIEWNAME_VIM_GROUPS = "($VIMGroups)";

	public static final int LESS_THAN = -1;
	public static final int EQUAL = 0;
	public static final int GREATER_THAN = 1;

	/** The Constant log_. */
	private final static Logger log_ = Logger.getLogger("org.openntf.domino");

	/** The Constant logBackup_. */
	private final static Logger logBackup_ = Logger.getLogger("com.ibm.xsp.domino");

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
	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	public static boolean isHierarchicalName(final String name) {
		return (Strings.isBlankString(name)) ? false : Names.IS_HIERARCHICAL_MATCH.matcher(name).find();
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
			Matcher m = Names.CN_MATCH.matcher(name);
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
			Matcher m = Names.O_MATCH.matcher(name);
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
			Matcher m = Names.OU_MATCH.matcher(name);
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
			Matcher m = Names.OU_MATCH.matcher(name);
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
			Matcher m = Names.C_MATCH.matcher(name);
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

	public static byte[] toByteArray(final String hexString) {
		if (hexString.length() % 2 != 0)
			throw new IllegalArgumentException("Only hex strings with an even number of digits can be converted");
		int arrLength = hexString.length() >> 1;
		byte buf[] = new byte[arrLength];

		for (int ii = 0; ii < arrLength; ii++) {
			int index = ii << 1;

			String l_digit = hexString.substring(index, index + 2);
			buf[ii] = (byte) Integer.parseInt(l_digit, 16);
		}
		return buf;
	}

	public static String toHex(final byte[] bytes) {
		Formatter formatter = new Formatter();
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
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
