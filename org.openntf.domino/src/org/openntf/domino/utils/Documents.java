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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Item;
import org.openntf.domino.MIMEEntity;
import org.openntf.domino.MIMEHeader;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.Stream;
import org.openntf.domino.exceptions.DataNotCompatibleException;
import org.openntf.domino.exceptions.MIMEConversionException;
import org.openntf.domino.utils.DominoUtils.LoaderObjectInputStream;

/**
 * Utility enum as a carrier for Document-centric static properties and methods.
 * 
 */
public enum Documents {
	;

	public static final String ITEMNAME_ATTACHMENTS = "attachments";
	public static final String ITEMNAME_BODY = "Body";
	public static final String ITEMNAME_DESCRIPTION = "description";
	public static final String ITEMNAME_FILEPATH = "filepath";
	public static final String ITEMNAME_FORM = "Form";
	public static final String ITEMNAME_MEMBERS = "Members";
	public static final String ITEMNAME_RECORDID = "recordID";
	public static final String ITEMNAME_SERVER = "server";
	public static final String ITEMNAME_SUBJECT = "subject";
	public static final String ITEMNAME_UNIVERSALID = "universalID";

	/** The Constant log_. */
	private final static Logger log_ = Logger.getLogger("org.openntf.domino");

	/** The Constant logBackup_. */
	//	private final static Logger logBackup_ = Logger.getLogger("com.ibm.xsp.domino");

	/*
	 * **************************************************************************
	 * **************************************************************************
	 * 
	 * MIMEBean methods
	 * 
	 * **************************************************************************
	 * **************************************************************************
	 */
	/**
	 * Restore state.
	 * 
	 * @param doc
	 *            the doc
	 * @param itemName
	 *            the item name
	 * @param entity
	 *            the MIMEentity to use, may be null. If specified, it must match itemName
	 * 
	 * @return the serializable
	 * @throws Throwable
	 *             the throwable
	 */
	@SuppressWarnings("unchecked")
	public static Object restoreState(final Document doc, final String itemName, final MIMEEntity entity) throws Exception {
		Session session = doc.getAncestorSession();
		Object result = null;
		Stream mimeStream = session.createStream();
		Class<?> chkClass = null;
		String allHeaders = entity.getHeaders();
		MIMEHeader header = entity.getNthHeader("X-Java-Class");
		if (header != null) {
			String className = header.getHeaderVal();
			chkClass = DominoUtils.getClass(className);
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
		InputStream is = new Streams.MIMEBufferedInputStream(mimeStream);
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
			Class<Externalizable> externalizableClass = (Class<Externalizable>) DominoUtils.getClass(entity.getNthHeader("X-Java-Class")
					.getHeaderVal());
			Externalizable restored = externalizableClass.newInstance();
			restored.readExternal(objectStream);
			result = restored;
		} else {
			Object restored = objectStream.readObject();

			// But wait! It might be a StateHolder object or Collection!
			MIMEHeader storageScheme = entity.getNthHeader("X-Storage-Scheme");
			MIMEHeader originalJavaClass = entity.getNthHeader("X-Original-Java-Class");
			if (storageScheme != null && "StateHolder".equals(storageScheme.getHeaderVal())) {
				Class<?> facesContextClass = DominoUtils.getClass("javax.faces.context.FacesContext");
				Method getCurrentInstance = facesContextClass.getMethod("getCurrentInstance");

				Class<?> stateHoldingClass = DominoUtils.getClass(originalJavaClass.getHeaderVal());
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
	@SuppressWarnings({ "cast" })
	public static Object restoreState(final Document doc, final String itemName) throws Exception {
		return restoreState(doc, itemName, null);
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
		Documents.saveState(object, doc, itemName, true, null);
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
		if (convertMime) {
			session.setConvertMime(false);
		}

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
			while (itemChk != null) {
				if (itemChk.isNames() || itemChk.isReaders() || itemChk.isAuthors()) {
					throw new DataNotCompatibleException("Cannot overwrite item '" + itemName + "' with serialized data in NoteID "
							+ doc.getNoteID() + ", because it is a Name/Reader/Author item.");
				}
				itemChk.remove();
				itemChk = doc.getFirstItem(itemName);
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
		if (!convertMime) {
			session.setConvertMime(true);
		}
	}

	/**
	 * Gets the MIME Item value
	 * 
	 * @param document
	 *            Document from which to get the MIME Value
	 * @param itemname
	 *            Name of the item containing the MIME entity
	 * 
	 * @return Value of the MIME item, if it exists. Null otherwise.
	 */
	public static Object getItemValueMIME(final Document document, final String itemname) {
		String noteID = null;
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (Strings.isBlankString(itemname)) {
				throw new IllegalArgumentException("Itemname is blank or null");
			}

			noteID = document.getNoteID();
			Session session = document.getAncestorSession();
			boolean convertMime = session.isConvertMIME();
			session.setConvertMIME(false);

			MIMEEntity entity = document.getMIMEEntity(itemname);
			Object result = (null == entity) ? null : Documents.getItemValueMIME(document, itemname, entity);

			session.setConvertMIME(convertMime);
			return result;

		} catch (Throwable t) {
			DominoUtils.handleException(new MIMEConversionException("Unable to getItemValueMIME for item name " + itemname
					+ " on document " + noteID, t));
		}

		return null;
	}

	/**
	 * Gets the MIME Item value
	 * 
	 * @param document
	 *            Document from which to get the MIME Value
	 * @param itemname
	 *            Name of the item containing the MIME entity
	 * @param entity
	 *            MIMEEntity from which to retrive the MIME value.
	 * 
	 * @return Value of the MIME item, if it exists. Null otherwise.
	 */
	public static Object getItemValueMIME(final Document document, final String itemname, MIMEEntity entity) {
		String noteID = null;
		boolean convertMime = false;
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}
			if (Strings.isBlankString(itemname)) {
				throw new IllegalArgumentException("Itemname is blank or null");
			}

			noteID = document.getNoteID();
			Session session = document.getAncestorSession();

			convertMime = session.isConvertMIME();
			if (convertMime) {
				session.setConvertMIME(false);
			}

			Object result = null;
			if (entity == null) {
				entity = document.getMIMEEntity(itemname);
			}
			if (entity == null) {
				return null;
			}
			MIMEHeader contentType = entity.getNthHeader("Content-Type");
			String headerval = (null == contentType) ? "" : contentType.getHeaderVal();
			if ("application/x-java-serialized-object".equals(headerval) || "application/x-java-externalized-object".equals(headerval)) {
				// entity is a MIMEBean
				return Documents.restoreState(document, itemname, entity);
			}

		} catch (Throwable t) {
			DominoUtils.handleException(new MIMEConversionException("Unable to getItemValueMIME for item name " + itemname
					+ " on document " + noteID, t));
		} finally {
			if (convertMime) {
				Session session = document.getAncestorSession();
				session.setConvertMIME(true);
			}
		}

		return null;
	}

	public static boolean isHasRecordID(final Document document) {
		try {
			if (null == document) {
				throw new IllegalArgumentException("Document is null");
			}

			return (document.hasItem(ITEMNAME_RECORDID) && (!Strings.isBlankString(document.getItemValueString(ITEMNAME_RECORDID))));

		} catch (Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}
}
