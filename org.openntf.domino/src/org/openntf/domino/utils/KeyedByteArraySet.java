/*
 * Copyright 2013
 * 
 * @author Devin S. Olson (dolson@czarnowski.com)
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
 *
 */
package org.openntf.domino.utils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import org.openntf.domino.Document;
import org.openntf.domino.EmbeddedObject;
import org.openntf.domino.Item;
import org.openntf.domino.MIMEEntity;
import org.openntf.domino.MIMEHeader;
import org.openntf.domino.Name;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.Session;
import org.openntf.domino.Stream;

/**
 * Extension of TreeSet of KeyedByteArray objects.
 * 
 * Objects of this class can be used for a multitude of purposes, but are <strong>idealy</strong> suited storage and transfer of sets File
 * Attachments read from or to be written to domino Documents.
 * 
 * @author Devin S. Olson dolson@czarnowski.com
 * @updated 01/2014
 */
public class KeyedByteArraySet extends TreeSet<KeyedByteArray> {

	private static final long serialVersionUID = 1L;

	/**
	 * Zero-Argument Constructor
	 */
	public KeyedByteArraySet() {
	}

	/**
	 * Optional Constructor
	 * 
	 * @param source
	 *            Document from which to read attachments as KeyedByteArray objects. (default from BODY field)
	 * 
	 */
	public KeyedByteArraySet(final Document source) {
		this.readAttachments(source);
	}

	/**
	 * Default Constructor
	 * 
	 * @param source
	 *            Document from which to read attachments as KeyedByteArray objects.
	 * 
	 * @param itemname
	 *            Name of item from which to read attachments.
	 * 
	 */
	public KeyedByteArraySet(final Document source, final String itemname) {
		this.readAttachments(source, itemname);
	}

	/**
	 * Optional Constructor
	 * 
	 * @param source
	 *            Document from which to read attachments as KeyedByteArray objects. (default from BODY field)
	 * 
	 */
	public KeyedByteArraySet(final lotus.domino.Document source) {
		this.readAttachments(source);
	}

	/**
	 * Optional Constructor
	 * 
	 * @param source
	 *            Document from which to read attachments as KeyedByteArray objects.
	 * 
	 * @param itemname
	 *            Name of item from which to read attachments.
	 * 
	 */
	public KeyedByteArraySet(final lotus.domino.Document source, final String itemname) {
		this.readAttachments(source, itemname);
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * Public methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	@Override
	public boolean add(final KeyedByteArray tba) {
		return (super.add(tba));
	}

	/**
	 * Returns the KeyedByteArray which matches the specified Common Identifier, or null if no match is found.
	 * 
	 * @param cid
	 *            Common Identifier for the requested KeyedByteArray.
	 * 
	 * @return KeyedByteArray which matches the cid, or null if no match is found.
	 */
	public KeyedByteArray get(final String cid) {
		if (!Strings.isBlankString(cid)) {
			for (final KeyedByteArray fa : this) {
				if (cid.equals(fa.getCID())) {
					return fa;
				}
			}
		}

		return null;
	}

	/**
	 * Gets the Common Identifiers for all KeyedByteArray members.
	 * 
	 * @return List of Common Identifiers for all KeyedByteArray members, null if no members exist.
	 */
	public List<String> getCIDs() {
		if (this.size() < 1) {
			return null;
		}

		final List<String> result = new ArrayList<String>();
		for (final KeyedByteArray fa : this) {
			result.add(fa.getCID());
		}

		return result;
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * lotus.domino methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */
	/**
	 * Reads the Attachments from the specified item on a document.
	 * 
	 * @param source
	 *            Document containing the item from which to read the attachments.
	 * @param itemname
	 *            Name of item from which to read the attachments.
	 * @return Number of KeyedByteArray items read from and added to the set.
	 */
	@SuppressWarnings("unchecked")
	public int readAttachments(final lotus.domino.Document source, final String itemname) {
		lotus.domino.Item itemSource = null;
		lotus.domino.RichTextItem richtextSource = null;
		lotus.domino.EmbeddedObject eo = null;

		try {
			if (null == source) {
				throw new IllegalArgumentException("Source Document is null");
			}
			if (Strings.isBlankString(itemname)) {
				throw new IllegalArgumentException("Item Name is blank or null");
			}

			if (source.hasItem(itemname)) {
				itemSource = source.getFirstItem(itemname);

				if (lotus.domino.Item.RICHTEXT == itemSource.getType()) {
					// process attachments
					richtextSource = (lotus.domino.RichTextItem) itemSource;
					final Vector<lotus.domino.EmbeddedObject> v = richtextSource.getEmbeddedObjects();
					if (null != v) {
						final Enumeration<lotus.domino.EmbeddedObject> e = v.elements();
						int result = 0;
						while (e.hasMoreElements()) {
							eo = e.nextElement();
							if ((null != eo) && (lotus.domino.EmbeddedObject.EMBED_ATTACHMENT == eo.getType())) {
								final String cid = source.getUniversalID() + "." + itemname + "." + result + "." + eo.getName();
								final KeyedByteArray fa = new KeyedByteArray(cid, eo);
								if (fa.length() > 0) {
									this.add(fa);
									result++;
								}
								DominoUtils.incinerate(eo);
							}
						}

						return result;
					}
				}
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		} finally {
			DominoUtils.incinerate(eo, richtextSource, itemSource);
		}

		return 0;
	}

	/**
	 * Reads the Attachments from the specified item on a document. Reads from the BODY field.
	 * 
	 * @param source
	 *            Document containing the item from which to read the attachments.
	 * 
	 * @return Number of KeyedByteArray items read from and added to the set.
	 */
	public int readAttachments(final lotus.domino.Document source) {
		return this.readAttachments(source, DominoUtils.ITEMNAME_BODY);
	}

	/**
	 * Writes Attachments to a target document.
	 * 
	 * Creates a File Attachment Embedded Object on the target document for each KeyedByteArray member of this set.
	 * 
	 * @param target
	 *            Document to which to write the Attachments.
	 * @param itemname
	 *            Name of item to which to embed the File Attachments.
	 * 
	 * @return Number of Attachments added to the target document.
	 */
	public int writeAttachments(final lotus.domino.Session session, final lotus.domino.Document target, final String itemname) {
		final lotus.domino.Name name = null;
		final lotus.domino.Item item = null;
		lotus.domino.Stream stream = null;
		lotus.domino.MIMEEntity attachmentsTarget = null;
		lotus.domino.MIMEEntity entity = null;
		lotus.domino.MIMEHeader header = null;

		try {
			if (null == target) {
				throw new IllegalArgumentException("Target Document is null");
			}
			if (Strings.isBlankString(itemname)) {
				throw new IllegalArgumentException("Item Name is blank or null");
			}
			if (this.size() > 0) {
				int result = 0;
				for (final KeyedByteArray fa : this) {
					final ByteArrayInputStream is = new ByteArrayInputStream(fa.getBytes());
					stream = session.createStream();
					stream.setContents(is);

					if (null == attachmentsTarget) {
						attachmentsTarget = target.createMIMEEntity(itemname);
					}

					entity = attachmentsTarget.createChildEntity();
					header = entity.createHeader("Content-Disposition");
					header.setHeaderVal("attachment; filename=\"" + fa.getFileName() + "\"");
					header = entity.createHeader("Content-ID");
					header.setHeaderVal("<" + fa.getCID() + ">");
					entity.setContentFromBytes(stream, fa.getContentType(), lotus.domino.MIMEEntity.ENC_IDENTITY_BINARY);
					stream.close();
					DominoUtils.incinerate(stream);
					result++;
				}

				return result;
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		} finally {
			DominoUtils.incinerate(name, item, stream, attachmentsTarget, entity, header);
		}

		return 0;
	}

	/**
	 * Writes Attachments to a target document.
	 * 
	 * Creates a File Attachment Embedded Object on the target document for each KeyedByteArray member of this set. Writes to the BODY
	 * field.
	 * 
	 * @param target
	 *            Document to which to write the Attachments.
	 * @return Number of Attachments added to the target document.
	 */
	public int writeAttachments(final lotus.domino.Session session, final lotus.domino.Document source) {
		return this.writeAttachments(session, source, DominoUtils.ITEMNAME_BODY);
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * org.openntf.domino methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */
	/**
	 * Reads the Attachments from the specified item on a document.
	 * 
	 * @param source
	 *            Document containing the item from which to read the attachments.
	 * @param itemname
	 *            Name of item from which to read the attachments.
	 * @return Number of KeyedByteArray items read from and added to the set.
	 */
	public int readAttachments(final Document source, final String itemname) {
		Item itemSource = null;
		RichTextItem richtextSource = null;
		EmbeddedObject eo = null;

		try {
			if (null == source) {
				throw new IllegalArgumentException("Source Document is null");
			}
			if (Strings.isBlankString(itemname)) {
				throw new IllegalArgumentException("Item Name is blank or null");
			}

			if (source.hasItem(itemname)) {
				itemSource = source.getFirstItem(itemname);

				if (lotus.domino.Item.RICHTEXT == itemSource.getType()) {
					// process attachments
					richtextSource = (RichTextItem) itemSource;
					final Vector<EmbeddedObject> v = richtextSource.getEmbeddedObjects();
					if (null != v) {
						final Enumeration<EmbeddedObject> e = v.elements();
						int result = 0;
						while (e.hasMoreElements()) {
							eo = e.nextElement();
							if ((null != eo) && (lotus.domino.EmbeddedObject.EMBED_ATTACHMENT == eo.getType())) {
								final String cid = source.getUniversalID() + "." + itemname + "." + result + "." + eo.getName();
								final KeyedByteArray fa = new KeyedByteArray(cid, eo);
								if (fa.length() > 0) {
									this.add(fa);
									result++;
								}
								DominoUtils.incinerate(eo);
							}
						}

						return result;
					}
				}
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		} finally {
			DominoUtils.incinerate(eo, richtextSource, itemSource);
		}

		return 0;
	}

	/**
	 * Reads the Attachments from the specified item on a document. Reads from the BODY field.
	 * 
	 * @param source
	 *            Document containing the item from which to read the attachments.
	 * 
	 * @return Number of KeyedByteArray items read from and added to the set.
	 */
	public int readAttachments(final Document source) {
		return this.readAttachments(source, DominoUtils.ITEMNAME_BODY);
	}

	/**
	 * Writes Attachments to a target document.
	 * 
	 * Creates a File Attachment Embedded Object on the target document for each KeyedByteArray member of this set.
	 * 
	 * @param target
	 *            Document to which to write the Attachments.
	 * @param itemname
	 *            Name of item to which to embed the File Attachments.
	 * 
	 * @return Number of Attachments added to the target document.
	 */
	public int writeAttachments(final Session session, final Document target, final String itemname) {
		final Name name = null;
		final Item item = null;
		Stream stream = null;
		MIMEEntity attachmentsTarget = null;
		MIMEEntity entity = null;
		MIMEHeader header = null;

		try {
			if (null == target) {
				throw new IllegalArgumentException("Target Document is null");
			}
			if (Strings.isBlankString(itemname)) {
				throw new IllegalArgumentException("Item Name is blank or null");
			}
			if (this.size() > 0) {
				int result = 0;
				for (final KeyedByteArray fa : this) {
					final ByteArrayInputStream is = new ByteArrayInputStream(fa.getBytes());
					stream = session.createStream();
					stream.setContents(is);

					if (null == attachmentsTarget) {
						attachmentsTarget = target.createMIMEEntity(itemname);
					}

					entity = attachmentsTarget.createChildEntity();
					header = entity.createHeader("Content-Disposition");
					header.setHeaderVal("attachment; filename=\"" + fa.getFileName() + "\"");
					header = entity.createHeader("Content-ID");
					header.setHeaderVal("<" + fa.getCID() + ">");
					entity.setContentFromBytes(stream, fa.getContentType(), lotus.domino.MIMEEntity.ENC_IDENTITY_BINARY);
					stream.close();
					DominoUtils.incinerate(stream);
					result++;
				}

				return result;
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		} finally {
			DominoUtils.incinerate(name, item, stream, attachmentsTarget, entity, header);
		}

		return 0;
	}

	/**
	 * Writes Attachments to a target document.
	 * 
	 * Creates a File Attachment Embedded Object on the target document for each KeyedByteArray member of this set. Writes to the BODY
	 * field.
	 * 
	 * @param target
	 *            Document to which to write the Attachments.
	 * @return Number of Attachments added to the target document.
	 */
	public int writeAttachments(final Session session, final Document source) {
		return this.writeAttachments(session, source, DominoUtils.ITEMNAME_BODY);
	}

	/**
	 * Writes Attachments to a target document.
	 * 
	 * Creates a File Attachment Embedded Object on the target document for each KeyedByteArray member of this set. Writes to the BODY
	 * field.
	 * 
	 * @param target
	 *            Document to which to write the Attachments.
	 * @return Number of Attachments added to the target document.
	 */
	public int writeAttachments(final Document source) {
		return this.writeAttachments(Factory.getSession(), source, DominoUtils.ITEMNAME_BODY);
	}

}
