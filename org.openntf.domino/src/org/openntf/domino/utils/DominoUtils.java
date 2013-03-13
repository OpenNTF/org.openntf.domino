package org.openntf.domino.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import lotus.domino.ACL;
import lotus.domino.Agent;
import lotus.domino.Base;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Form;
import lotus.domino.Item;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewColumn;

import org.openntf.domino.DocumentCollection;
import org.openntf.domino.exceptions.InvalidNotesUrlException;

public enum DominoUtils {
	;
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

	public static Throwable handleException(Throwable t) {
		// TODO implement standard logging approaches
		boolean someMeansOfControllingThrows = true;
		if (someMeansOfControllingThrows) {
			throw new RuntimeException(t);
		} else {
			return t; // we already handled it, but maybe somebody wants to do something....
		}
	}

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

	public static Session getSession(Base base) throws Throwable {
		Session result = null;
		if (base instanceof Database) {
			result = ((Database) base).getParent();
		} else if (base instanceof Document) {
			result = DominoUtils.getSession(((Document) base).getParentDatabase());
		} else if (base instanceof View) {
			result = DominoUtils.getSession(((View) base).getParent());
		} else if (base instanceof Form) {
			result = DominoUtils.getSession(((Form) base).getParent());
		} else if (base instanceof Item) {
			result = DominoUtils.getSession(((Item) base).getParent());
		} else if (base instanceof ViewColumn) {
			result = DominoUtils.getSession(((ViewColumn) base).getParent());
		} else if (base instanceof Agent) {
			result = DominoUtils.getSession(((Agent) base).getParent());
		} else if (base instanceof ACL) {
			result = DominoUtils.getSession(((ACL) base).getParent());
		} else if (base instanceof Item) {
			result = DominoUtils.getSession(((Item) base).getParent());
		} else if (base instanceof DocumentCollection) {
			result = DominoUtils.getSession(((DocumentCollection) base).getParent());
		}
		return result;
	}

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

	public static boolean isUnid(String value) {
		if (value.length() != 32)
			return false;
		return DominoUtils.isHex(value);
	}

	public static String md5(Serializable object) {
		return DominoUtils.checksum(object, "MD5");
	}

	public static String toUnid(String value) {
		if (DominoUtils.isUnid(value))
			return value;
		return DominoUtils.md5(value);
	}

	public static Calendar toJavaCalendarSafe(lotus.domino.DateTime dt) {
		Date d = DominoUtils.toJavaDateSafe(dt);
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(d);
		return c;
	}

	public static Date toJavaDate(long l) {
		Date result = new Date();
		result.setTime(l);
		return result;
	}

	public static Collection<Date> toJavaDate(long[] ls) {
		Collection<Date> result = new ArrayList<Date>();
		for (long l : ls) {
			result.add(DominoUtils.toJavaDate(l));
		}
		return result;
	}

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

	public static Serializable restoreState(Document doc, String itemName) throws Throwable {
		Session session = getSession(doc);
		boolean convertMime = session.isConvertMime();
		session.setConvertMime(false);

		Serializable result = null;
		lotus.domino.Stream mimeStream = session.createStream();
		lotus.domino.MIMEEntity entity = doc.getMIMEEntity(itemName);
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
		Serializable restored = (Serializable) objectStream.readObject();
		result = restored;

		entity.recycle();

		session.setConvertMime(convertMime);

		return result;
	}

	public static void saveState(Serializable object, Document doc, String itemName) throws Throwable {
		saveState(object, doc, itemName, true);
	}

	public static void saveState(Serializable object, Document doc, String itemName, boolean compress) throws Throwable {
		Session session = getSession(doc);
		boolean convertMime = session.isConvertMime();
		session.setConvertMime(false);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objectStream = compress ? new ObjectOutputStream(new GZIPOutputStream(byteStream)) : new ObjectOutputStream(
				byteStream);
		objectStream.writeObject(object);
		objectStream.flush();
		objectStream.close();

		lotus.domino.Stream mimeStream = session.createStream();
		lotus.domino.MIMEEntity previousState = doc.getMIMEEntity(itemName);
		lotus.domino.MIMEEntity entity = previousState == null ? doc.createMIMEEntity(itemName) : previousState;
		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteStream.toByteArray());
		mimeStream.setContents(byteIn);
		entity.setContentFromBytes(mimeStream, "application/x-java-serialized-object", lotus.domino.MIMEEntity.ENC_NONE);
		lotus.domino.MIMEHeader header = entity.getNthHeader("Content-Encoding");
		if (compress) {
			if (header == null) {
				header = entity.createHeader("Content-Encoding");
			}
			header.setHeaderVal("gzip");
			header.recycle();
		} else {
			if (header != null) {
				header.remove();
				header.recycle();
			}
		}

		entity.recycle();
		mimeStream.recycle();

		session.setConvertMime(convertMime);
	}
}
