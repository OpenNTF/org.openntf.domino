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
import java.nio.ByteBuffer;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import org.openntf.arpa.NamePartsMap;
import org.openntf.domino.DateTime;
import org.openntf.domino.ExceptionDetails;
import org.openntf.domino.Item;
import org.openntf.domino.Name;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.exceptions.InvalidNotesUrlException;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.ext.Name.NamePartKey;
import org.openntf.domino.logging.LogUtils;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * The Enum DominoUtils.
 */
public enum DominoUtils {
	;

	//	private static ConcurrentHashMap CHM = null;
	//	private static ReentrantLock RL = null;

	//	private static ClassLoader defaultLoader = null;
	//
	//	public static void TEMP_SET_DEFAULT_CLASSLOADER(final ClassLoader loader) {
	//		defaultLoader = loader;
	//	}

	public static final String VIEWNAME_VIM_PEOPLE_AND_GROUPS = "($VIMPeopleAndGroups)";
	public static final String VIEWNAME_VIM_GROUPS = "($VIMGroups)";

	public static final int LESS_THAN = -1;
	public static final int EQUAL = 0;
	public static final int GREATER_THAN = 1;

	protected static AtomicInteger CLASS_ERROR_COUNT = new AtomicInteger(0);

	/** The Constant log_. */
	private final static Logger log_ = Logger.getLogger("org.openntf.domino");

	/** The Constant logBackup_. */
	private final static Logger logBackup_ = Logger.getLogger("com.ibm.xsp.domino");

	public static void reportType(final Object o, final String name, final Class<?> expected) {
		System.out.println("DEBUG " + name + " is not the expected type of " + expected.getName() + ". Instead its a "
				+ (o == null ? "null" : o.getClass().getName()));
	}

	public static Class<?> getClass(final CharSequence className) {
		Class<?> result = null;
		String pname = null;
		String sname = null;
		Class<?> pclass = null;
		String cname = className.toString();
		if (cname.startsWith("[L")) {
			cname = cname.substring(2);
		}
		if (cname.endsWith(";")) {
			cname = cname.substring(0, cname.length() - 1);
		}
		try {
			int pos = cname.indexOf('$');
			if (pos > -1) {
				pname = cname.substring(0, pos);
				sname = cname.substring(pos + 1);
				pclass = Class.forName(pname);
				for (Class<?> curClass : pclass.getDeclaredClasses()) {
					if (curClass.getSimpleName().equals(sname)) {
						//						Factory.println("FOUND! inner class " + curClass.getSimpleName());
						result = curClass;
					}
				}
				if (result == null) {
					//					Factory.println("Failed to default load a class called " + cname + " by parsing to " + pname + " and " + sname
					//							+ ". The pname resolved to " + (pclass == null ? "null" : pclass.getName()));
				}
			} else {
				result = Class.forName(className.toString());
			}
		} catch (Throwable t) {
			//			if (cname.contains("$")) {
			//			Factory.println("Failed to default load a class called " + cname + " by parsing to " + pname + " and " + sname
			//					+ ". The pname resolved to " + (pclass == null ? "null" : pclass.getName()));
			//			}
			//			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			//			Factory.println("Initial attempt to find " + className.toString() + " using a class loader of type " + cl.getClass().getName()
			//					+ " (" + System.identityHashCode(cl) + ") failed.");
		}
		if (result == null) {
			//			Factory.println("TEMP DEBUG Default failed so therefore we're trying privileged versions...");
			try {
				result = AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
					@Override
					public Class<?> run() throws Exception {
						Class<?> result = null;
						ClassLoader cl = Thread.currentThread().getContextClassLoader();
						String pname = null;
						String sname = null;
						String cname = className.toString();
						Class<?> pclass = null;
						try {
							if (cname.startsWith("[L")) {
								cname = cname.substring(2);
							}
							int pos = cname.indexOf('$');
							if (pos > -1) {
								pname = cname.substring(0, pos);
								sname = cname.substring(pos + 1);
								pclass = Class.forName(pname, false, cl);
								for (Class<?> curClass : pclass.getDeclaredClasses()) {
									if (curClass.getSimpleName().equals(sname)) {
										result = curClass;
									}
								}
							} else {
								pname = cname;
								result = Class.forName(pname, false, cl);
							}
						} catch (java.lang.ClassNotFoundException e) {
							try {
								if (cname.contains("$")) {
									//									Factory.println("Failed to contextClassLoader load a class called " + cname + " by parsing to " + pname
									//											+ " and " + sname + ". The pname resolved to " + (pclass == null ? "null" : pclass.getName()));
								}
								cl = DominoUtils.class.getClassLoader();
								pclass = cl.loadClass(pname);
								if (sname != null) {
									for (Class<?> curClass : pclass.getDeclaredClasses()) {
										if (curClass.getSimpleName().equals(sname)) {
											result = curClass;
										}
									}
								} else {
									result = pclass;
								}
							} catch (Exception e1) {
								if (cname.contains("$")) {
									//									Factory.println("Failed to DominoUtils.getClassLoader() load a class called " + cname
									//											+ " by parsing to " + pname + " and " + sname + ". The pname resolved to "
									//											+ (pclass == null ? "null" : pclass.getName()));
								}
								cl = ClassLoader.getSystemClassLoader();
								try {
									pclass = cl.loadClass(pname);
									if (sname != null) {
										for (Class<?> curClass : pclass.getDeclaredClasses()) {
											if (curClass.getSimpleName().equals(sname)) {
												result = curClass;
											}
										}
									} else {
										result = pclass;
									}
								} catch (Exception e2) {
									//									e2.printStackTrace();
									//									DominoUtils.handleException(e2);
									//									Factory.println("STILL couldn't get class " + pname + " using system classloader "
									//											+ cl.getClass().getName());
								}
							}
						} catch (Throwable t) {
							t.printStackTrace();
							DominoUtils.handleException(t);
						}
						return result;
					}
				});
			} catch (AccessControlException e) {
				e.printStackTrace();
				DominoUtils.handleException(e);
			} catch (PrivilegedActionException e) {
				e.printStackTrace();
				DominoUtils.handleException(e);
			}
		}
		if (result == null) {
			//			int count = DominoUtils.CLASS_ERROR_COUNT.incrementAndGet();
			//			log_.log(Level.WARNING, "Unable to resolve class " + className + " Please check logs for more details. Incident " + count);
			//			if (count > 50) {
			//				Thread.currentThread().interrupt();
			//			}
		}
		return result;
	}

	private static ThreadLocal<Boolean> bubbleExceptions_ = new ThreadLocal<Boolean>() {

		@Override
		protected Boolean initialValue() {
			//			System.out.println("INIT");
			return Boolean.valueOf(Factory.getThreadConfig().bubbleExceptions);
		}
	};

	public static Boolean getBubbleExceptions() {
		Boolean ret = bubbleExceptions_.get();
		if (ret == null) {
			ret = Boolean.valueOf(Factory.getThreadConfig().bubbleExceptions);
			bubbleExceptions_.set(ret);
		}

		return ret;
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
			Class<?> result = null;
			try {
				result = DominoUtils.getClass(name);
			} catch (Exception e) {
				result = super.resolveClass(desc);
			}
			if (result == null) {
				result = super.resolveClass(desc);
			}
			return result;
		}

		@Override
		protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
			// TODO Auto-generated method stub
			return super.readClassDescriptor();
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
	public static boolean isNumber(final CharSequence value) {
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
			Double.parseDouble(value.toString());
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
		return (handleException(t, null, null));
	}

	public static Throwable handleException(final Throwable t, final ExceptionDetails hed) {
		return handleException(t, hed, null);
	}

	public static Throwable handleException(final Throwable t, final String details) {
		return handleException(t, null, details);
	}

	public static Throwable handleException(final Throwable t, final ExceptionDetails hed, final String details) {
		if (t instanceof OpenNTFNotesException) {
			OpenNTFNotesException ne = (OpenNTFNotesException) t;
			ne.addExceptionDetails(hed);
			throw ne;
		}
		if (getBubbleExceptions()) {
			throw new OpenNTFNotesException(details, t, hed);
		}
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
		return t;
	}

	/**
	 * Gets the unid from notes url.
	 *
	 * @param notesurl
	 *            the notesurl
	 * @return the unid from notes url
	 */
	public static String getUnidFromNotesUrl(final CharSequence notesurl) {
		String result = null;
		String trimmed = notesurl.toString().toLowerCase().trim();
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
			throw new InvalidNotesUrlException(notesurl.toString());
		}

		return result;
	}

	/**
	 * Incinerate.
	 *
	 * @param args
	 *            the args
	 * @deprecated you should recycle objects by passing them to the WrapperFactory
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Deprecated
	public static void incinerate(final Object... args) {
		for (Object o : args) {
			if (o != null) {
				if (o instanceof lotus.domino.Base) {
					// try {
					// ((Base) o).recycle();
					// } catch (Throwable t) {
					// // who cares?
					// }
					WrapperFactory wf = Factory.getWrapperFactory();
					wf.recycle((lotus.domino.Base) o);
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

	public static boolean isHierarchicalName(final CharSequence name) {
		return (Strings.isBlankString(name.toString())) ? false : Names.IS_HIERARCHICAL_MATCH.matcher(name).find();
	}

	public static void parseNamesPartMap(final CharSequence name, final NamePartsMap map) {
		if (isHierarchicalName(name)) {
			Matcher m = Names.CN_MATCH.matcher(name);
			if (m.find()) {
				int start = m.start() + 3;
				int end = m.end();
				if (start < end) {
					map.put(NamePartKey.Common, name.subSequence(start, end).toString());
				} else {
					map.put(NamePartKey.Common, name.toString());
				}
			}
			m = Names.O_MATCH.matcher(name);
			if (m.find()) {
				int start = m.start() + 2;
				int end = m.end();
				if (start < end) {
					map.put(NamePartKey.Organization, name.subSequence(start, end).toString());
				} else {
					map.put(NamePartKey.Organization, name.toString());
				}
			}
			m = Names.C_MATCH.matcher(name);
			if (m.find()) {
				int start = m.start() + 2;
				int end = m.end();
				if (start < end) {
					map.put(NamePartKey.Country, name.subSequence(start, end).toString());
				} else {
					map.put(NamePartKey.Country, name.toString());
				}
			}
			m = Names.OU_MATCH.matcher(name);
			int i = 0;
			while (m.find()) {
				int start = m.start() + 3;
				int end = m.end();
				if (start < end) {
					if (i == 0) {
						map.put(NamePartKey.OrgUnit1, name.subSequence(start, end).toString());
					}
					if (i == 1) {
						map.put(NamePartKey.OrgUnit2, name.subSequence(start, end).toString());
					}
					if (i == 2) {
						map.put(NamePartKey.OrgUnit3, name.subSequence(start, end).toString());
					}
					if (i == 3) {
						map.put(NamePartKey.OrgUnit4, name.subSequence(start, end).toString());
					}
				}
			}
		}
	}

	public static String toAbbreviatedName(final CharSequence name) {
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
				if (!isFirst) {
					builder.append('/');
				}
				isFirst = false;
				builder.append(o);
			}
			String c = toCountry(name);
			if (c.length() > 0) {
				if (!isFirst) {
					builder.append('/');
				}
				isFirst = false;
				builder.append(c);
			}
			return builder.toString();
		} else {
			return name.toString();
		}
	}

	public static String toCommonName(final CharSequence name) {
		if (isHierarchicalName(name)) {
			Matcher m = Names.CN_MATCH.matcher(name);
			if (m.find()) {
				int start = m.start() + 3;
				int end = m.end();
				if (start < end) {
					return name.subSequence(start, end).toString();
				} else {
					return name.toString();
				}
			} else {
				return "";
			}
		} else {
			return name.toString();
		}
	}

	public static String toOrgName(final CharSequence name) {
		if (isHierarchicalName(name)) {
			Matcher m = Names.O_MATCH.matcher(name);
			if (m.find()) {
				int start = m.start() + 2;
				int end = m.end();
				if (start < end) {
					return name.subSequence(start, end).toString();
				} else {
					return name.toString();
				}
			} else {
				return "";
			}
		} else {
			return name.toString();
		}
	}

	public static String toOUString(final CharSequence name) {
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
					builder.append(name.subSequence(start, end));
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

	public static String[] toOU(final CharSequence name) {
		if (isHierarchicalName(name)) {
			Matcher m = Names.OU_MATCH.matcher(name);
			String[] ous = new String[4];	//maximum number of OUs according to spec
			int i = 0;
			while (m.find()) {
				int start = m.start() + 3;
				int end = m.end();
				if (start < end) {
					ous[i++] = name.subSequence(start, end).toString();
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

	public static String toCountry(final CharSequence name) {
		if (isHierarchicalName(name)) {
			Matcher m = Names.C_MATCH.matcher(name);
			if (m.find()) {
				int start = m.start() + 2;
				int end = m.end();
				if (start < end) {
					return name.subSequence(start, end).toString();
				} else {
					return name.toString();
				}
			} else {
				return "";
			}
		} else {
			return name.toString();
		}
	}

	public static String toNameType(final CharSequence name, final Name.NameType type) {
		switch (type) {
		case COMMON:
			return toCommonName(name);
		case ABBREVIATED:
			return toAbbreviatedName(name);
		case CANONICAL:
			return name.toString();
		case ORG:
			return toOrgName(name);
		case ORGUNIT:
			return toOUString(name);
		case COUNTRY:
			return toCountry(name);
		}
		return name.toString();
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

	public static List<String> toSelectionList(final Collection<String> names, final Name.NameType firstType,
			final Name.NameType secondType) {
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
	public static boolean isHex(final CharSequence value) {
		if (value == null) {
			return false;
		}
		String chk = value.toString();
		for (int i = 0; i < chk.length(); i++) {
			char c = chk.charAt(i);
			boolean isHexDigit = Character.isDigit(c) || Character.isWhitespace(c) || c == 'a' || c == 'b' || c == 'c' || c == 'd'
					|| c == 'e' || c == 'f' || c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F';

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
	 * @return true, if is 32-character hexadecimal sequence
	 */
	public static boolean isUnid(final CharSequence value) {
		if (value.length() != 32) {
			return false;
		}
		return DominoUtils.isHex(value);
	}

	/**
	 * Checks if is replica id.
	 *
	 * @param value
	 *            the value
	 * @return true, if is 16-character hexadecimal sequence
	 */
	public static boolean isReplicaId(final CharSequence value) {
		if (value.length() != 16) {
			return false;
		}
		return DominoUtils.isHex(value);
	}

	/**
	 * Checks if is metaversal id.
	 *
	 * @param value
	 *            the value
	 * @return true, if is 48-character hexadecimal sequence
	 */
	public static boolean isMetaversalId(final CharSequence value) {
		if (value == null) {
			return false;
		}
		if (value.length() != 48) {
			return false;
		}
		return DominoUtils.isHex(value);
	}

	/**
	 * Md5.
	 *
	 * @param object
	 *            the Serializable object
	 * @return the string representing the MD5 hash value of the serialized version of the object
	 */
	public static String md5(final Serializable object) {
		return DominoUtils.checksum(object, "MD5");
	}

	/**
	 * To unid.
	 *
	 * @param value
	 *            the value
	 * @return a 32-character hexadecimal string that can be used as a UNID, uniquely and deterministically based on the value argument
	 */
	public static String toUnid(final Serializable value) {
		if (value instanceof CharSequence && DominoUtils.isUnid((CharSequence) value)) {
			return value.toString();
		}
		String hash = DominoUtils.md5(value);
		while (hash.length() < 32) {
			hash = "0" + hash;
		}
		return hash.toUpperCase();
	}

	public static byte[] toByteArray(final CharSequence hexString) {
		if (hexString.length() % 2 != 0) {
			throw new IllegalArgumentException("Only hex strings with an even number of digits can be converted");
		}
		int arrLength = hexString.length() >> 1;
		byte buf[] = new byte[arrLength];

		for (int ii = 0; ii < arrLength; ii++) {
			int index = ii << 1;

			CharSequence l_digit = hexString.subSequence(index, index + 2);
			buf[ii] = (byte) Integer.parseInt(l_digit.toString(), 16);
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

	public static Integer toInteger(final byte[] bytes) {
		//FIXME NTF This feels wrong. Should we pad the byte array? Am I being pedantic?
		if (bytes.length == 1) {
			return Byte.valueOf(bytes[0]).intValue();
		} else if (bytes.length == 2) {
			ByteBuffer wrapped = ByteBuffer.wrap(bytes);
			short s = wrapped.getShort();
			return Short.valueOf(s).intValue();
		} else if (bytes.length == 4) {
			ByteBuffer wrapped = ByteBuffer.wrap(bytes);
			int i = wrapped.getInt();
			return i;
		} else {
			throw new IllegalArgumentException("Cannot convert a byte array of length " + bytes.length + " to Integer");
		}
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
		String newVal = Factory.getSession(SessionType.CURRENT).getEnvironmentString(propertyName, true);
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
				returnStream = DominoUtils.class.getResourceAsStream(fileLoc);
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
		DateTime dt = item.getAncestorSession().createDateTime(cal);
		item.setDateTimeValue(dt);
		DominoUtils.incinerate(dt);
		return item;
	}

	public static Item itemFromCalendarAppend(final Item item, final Calendar cal) {
		Session sess = item.getAncestorSession();
		DateTime dt = sess.createDateTime(cal);
		Vector<DateTime> v = item.getValueDateTimeArray();
		v.add(dt);
		item.setValues(v);
		sess.getFactory().recycle(dt);
		return item;
	}

	public static Item itemFromDate(final Item item, final Date cal) {
		Session sess = item.getAncestorSession();
		DateTime dt = sess.createDateTime(cal);
		item.setDateTimeValue(dt);
		sess.getFactory().recycle(dt);
		return item;
	}

	public static Item itemFromDateAppend(final Item item, final Date cal) {
		Session sess = item.getAncestorSession();
		DateTime dt = sess.createDateTime(cal);
		Vector<DateTime> v = item.getValueDateTimeArray();
		v.add(dt);
		item.setValues(v);
		sess.getFactory().recycle(dt);
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
		return value.replace("{", "\\{").replace("}", "\\}").replace("\\", "\\\\");
	}

	public static boolean isSerializable(final Collection<?> values) {
		if (values == null) {
			return false;
		}
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
					log_.info("Unable to convert to Comparable from " + ser.getClass().getName());
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Collection<Serializable> toSerializable(final Collection<?> values) {
		if (DominoUtils.isSerializable(values)) {
			return (Collection<Serializable>) values;
		}
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

	public static String javaBinaryNameToFilePath(final CharSequence binaryName, final String separator) {
		return binaryName.toString().replace(".", separator) + ".class";
	}

	public static String filePathToJavaBinaryName(final CharSequence filePath, final String separator) {
		return filePath.subSequence(0, filePath.length() - 6).toString().replace(separator, ".");
	}

}
