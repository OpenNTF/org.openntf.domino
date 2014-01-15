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
package org.openntf.domino.impl;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.DocumentCollection;
import org.openntf.domino.Form;
import org.openntf.domino.Item;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.exceptions.DataNotCompatibleException;
import org.openntf.domino.exceptions.ItemNotFoundException;
import org.openntf.domino.exceptions.MIMEConversionException;
import org.openntf.domino.ext.Database.Events;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.helpers.Formula;
import org.openntf.domino.transactions.DatabaseTransaction;
import org.openntf.domino.types.BigString;
import org.openntf.domino.types.Null;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.TypeUtils;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.util.JsonWriter;

// TODO: Auto-generated Javadoc
/**
 * The Class Document.
 */
public class Document extends Base<org.openntf.domino.Document, lotus.domino.Document> implements org.openntf.domino.Document {
	private static final Logger log_ = Logger.getLogger(Document.class.getName());

	public static enum RemoveType {
		SOFT_FALSE, SOFT_TRUE, HARD_FALSE, HARD_TRUE;
	}

	private RemoveType removeType_;

	private boolean isDirty_ = false;
	private String noteid_;
	private String unid_;
	private boolean isNew_;
	private boolean isQueued_ = false;
	private boolean isRemoveQueued_ = false;
	private boolean shouldWriteItemMeta_ = false; // TODO NTF create rules for making this true
	private boolean shouldResurrect_ = false;

	// NTF - these are immutable by definition, so we should just copy it when we read in the doc
	// yes, we're creating objects we might not need, but that's better than risking the toxicity of evil, wicked DateTime
	// these ought to be final, since they can't change, but it makes the constructor really messy

	// NTF - Okay, after testing, maybe these just need to be JIT getters. It added about 10% to Document iteration time.
	// NTF - Done. And yeah, it make quite a performance difference. More like 20%, really
	/** The created_. */
	private Date created_;

	/** The initially modified_. */
	private Date initiallyModified_;

	/** The last modified_. */
	private Date lastModified_;

	/** The last accessed_. */
	private Date lastAccessed_;

	/**
	 * Instantiates a new document.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public Document(final lotus.domino.Document delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getParentDatabase(parent));
		initialize(delegate);
	}

	/**
	 * Initialize.
	 * 
	 * @param delegate
	 *            the delegate
	 */
	private void initialize(final lotus.domino.Document delegate) {
		try {
			// delegate.setPreferJavaDates(true);
			noteid_ = delegate.getNoteID();
			unid_ = delegate.getUniversalID();
			// System.out.println("initializing new document from " + unid_);
			isNew_ = delegate.isNewNote();
			if (getAncestorSession().isFixEnabled(Fixes.FORCE_JAVA_DATES)) {
				delegate.setPreferJavaDates(true);
			}
			if (isNew_) {
				//				System.out.println("Wrapping a new document rather than an existing one...");
				//				Thread.sleep(100);
			}
			// created_ = DominoUtils.toJavaDateSafe(delegate.getCreated());
			// initiallyModified_ = DominoUtils.toJavaDateSafe(delegate.getInitiallyModified());
			// lastModified_ = DominoUtils.toJavaDateSafe(delegate.getLastModified());
			// lastAccessed_ = DominoUtils.toJavaDateSafe(delegate.getLastAccessed());
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getCreated()
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.DATETIME_WARNING)
	public org.openntf.domino.DateTime getCreated() {
		try {
			// if (created_ == null) {
			// created_ = DominoUtils.toJavaDateSafe(getDelegate().getCreated());
			// }
			if (getDelegate().getCreated() == null)
				return null;
			return new DateTime(getDelegate().getCreated(), this); // TODO NTF - maybe ditch the parent?
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getCreatedDate()
	 */
	public Date getCreatedDate() {
		if (created_ == null) {
			try {
				created_ = DominoUtils.toJavaDateSafe(getDelegate().getCreated());
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
		}
		return created_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getInitiallyModified()
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.DATETIME_WARNING)
	public DateTime getInitiallyModified() {
		try {
			// if (initiallyModified_ == null) {
			// initiallyModified_ = DominoUtils.toJavaDateSafe(getDelegate().getInitiallyModified());
			// }
			if (getDelegate().getInitiallyModified() == null)
				return null;
			return new DateTime(getDelegate().getInitiallyModified(), this); // TODO NTF - maybe ditch the parent?

		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getInitiallyModifiedDate()
	 */
	public Date getInitiallyModifiedDate() {
		if (initiallyModified_ == null) {
			try {
				initiallyModified_ = DominoUtils.toJavaDateSafe(getDelegate().getInitiallyModified());
			} catch (NotesException e) {
				DominoUtils.handleException(e);

			}
		}
		return initiallyModified_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getLastAccessed()
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.DATETIME_WARNING)
	public DateTime getLastAccessed() {
		try {
			// if (lastAccessed_ == null) {
			// lastAccessed_ = DominoUtils.toJavaDateSafe(getDelegate().getLastAccessed());
			// }
			if (getDelegate().getLastAccessed() == null)
				return null;
			return new DateTime(getDelegate().getLastAccessed(), this); // TODO NTF - maybe ditch the parent?

		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getLastAccessedDate()
	 */
	public Date getLastAccessedDate() {
		if (lastAccessed_ == null) {
			try {
				lastAccessed_ = DominoUtils.toJavaDateSafe(getDelegate().getLastAccessed());
			} catch (NotesException e) {
				DominoUtils.handleException(e);

			}
		}
		return lastAccessed_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getLastModified()
	 */
	@Override
	@Deprecated
	@Legacy(Legacy.DATETIME_WARNING)
	public DateTime getLastModified() {
		try {
			// if (lastModified_ == null) {
			// lastModified_ = DominoUtils.toJavaDateSafe(getDelegate().getLastModified());
			// }
			if (getDelegate().getLastModified() == null)
				return null;
			return new DateTime(getDelegate().getLastModified(), this); // TODO NTF - maybe ditch the parent?

		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getLastModifiedDate()
	 */
	public Date getLastModifiedDate() {
		if (lastModified_ == null) {
			try {
				lastModified_ = DominoUtils.toJavaDateSafe(getDelegate().getLastModified());
			} catch (NotesException e) {
				DominoUtils.handleException(e);

			}
		}
		return lastModified_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#appendItemValue(java.lang.String)
	 */
	@Override
	public Item appendItemValue(final String name) {
		markDirty();
		try {
			return Factory.fromLotus(getDelegate().appendItemValue(name), Item.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#appendItemValue(java.lang.String, double)
	 */
	@Override
	public Item appendItemValue(final String name, final double value) {
		return appendItemValue(name, Double.valueOf(value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#appendItemValue(java.lang.String, int)
	 */
	@Override
	public Item appendItemValue(final String name, final int value) {
		return appendItemValue(name, Integer.valueOf(value));
	}

	public Item appendItemValue(final String name, final Object value, final boolean unique) {
		Item result = null;
		if (unique && hasItem(name)) {
			result = getFirstItem(name);
			if (result.containsValue(value)) {
				return result;
			}
		}
		result = appendItemValue(name, value);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#appendItemValue(java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Item appendItemValue(final String name, final Object value) {
		markDirty();
		Item result = null;
		try {
			if (!hasItem(name)) {
				result = replaceItemValue(name, value);
			} else if (value != null) {
				Object domNode = toDominoFriendly(value, this);
				if (getAncestorSession().isFixEnabled(Fixes.APPEND_ITEM_VALUE)) {
					Object current = getItemValue(name);
					if (current == null) {
						result = replaceItemValue(name, value);
					} else if (current instanceof Vector) {
						Object newVal = ((Vector) current).add(domNode);
						result = replaceItemValue(name, newVal);
					} else {
						log_.log(Level.WARNING, "Unable to append a value of type " + value.getClass().getName()
								+ " to an existing item returning a " + current.getClass().getName() + ". Reverting to legacy behavior...");
						result = Factory.fromLotus(getDelegate().appendItemValue(name, domNode), Item.class, this);
					}

				} else {
					result = Factory.fromLotus(getDelegate().appendItemValue(name, domNode), Item.class, this);
				}
				Base.enc_recycle(domNode);
			} else {
				result = appendItemValue(name);
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#attachVCard(lotus.domino.Base)
	 */
	@Override
	public void attachVCard(final lotus.domino.Base document) {
		markDirty();
		try {
			getDelegate().attachVCard(toLotus(document));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#attachVCard(lotus.domino.Base, java.lang.String)
	 */
	@Override
	public void attachVCard(final lotus.domino.Base document, final String charset) {
		markDirty();
		try {
			getDelegate().attachVCard(toLotus(document), charset);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#closeMIMEEntities()
	 */
	@Override
	public boolean closeMIMEEntities() {
		try {
			return getDelegate().closeMIMEEntities();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#closeMIMEEntities(boolean)
	 */
	@Override
	public boolean closeMIMEEntities(final boolean saveChanges) {
		try {
			return getDelegate().closeMIMEEntities(saveChanges);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#closeMIMEEntities(boolean, java.lang.String)
	 */
	@Override
	public boolean closeMIMEEntities(final boolean saveChanges, final String entityItemName) {
		try {
			return getDelegate().closeMIMEEntities(saveChanges, entityItemName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#computeWithForm(boolean, boolean)
	 */
	@Override
	public boolean computeWithForm(final boolean doDataTypes, final boolean raiseError) {
		markDirty();
		try {
			return getDelegate().computeWithForm(doDataTypes, raiseError);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#convertToMIME()
	 */
	@Override
	public void convertToMIME() {
		try {
			getDelegate().convertToMIME();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#convertToMIME(int)
	 */
	@Override
	public void convertToMIME(final int conversionType) {
		try {
			getDelegate().convertToMIME(conversionType);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#convertToMIME(int, int)
	 */
	@Override
	public void convertToMIME(final int conversionType, final int options) {
		try {
			getDelegate().convertToMIME(conversionType, options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#copyAllItems(lotus.domino.Document, boolean)
	 */
	@Override
	public void copyAllItems(final lotus.domino.Document doc, final boolean replace) {
		try {
			getDelegate().copyAllItems((lotus.domino.Document) toLotus(doc), replace);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#copyItem(lotus.domino.Item)
	 */
	@Override
	public Item copyItem(final lotus.domino.Item item) {
		// TODO - NTF markDirty()?
		try {
			return Factory.fromLotus(getDelegate().copyItem((lotus.domino.Item) toLotus(item)), Item.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#copyItem(lotus.domino.Item, java.lang.String)
	 */
	@Override
	public Item copyItem(final lotus.domino.Item item, final String newName) {
		// TODO - NTF markDirty()?
		try {
			return Factory.fromLotus(getDelegate().copyItem((lotus.domino.Item) toLotus(item), newName), Item.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#copyToDatabase(lotus.domino.Database)
	 */
	@Override
	public Document copyToDatabase(final lotus.domino.Database db) {
		// TODO - NTF markDirty()?
		try {
			return Factory.fromLotus(getDelegate().copyToDatabase((lotus.domino.Database) toLotus(db)), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#createMIMEEntity()
	 */
	@Override
	public MIMEEntity createMIMEEntity() {
		markDirty();
		try {
			return Factory.fromLotus(getDelegate().createMIMEEntity(), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	//RPr: currently not used. So I commented this out
	//private final transient Map<String, MIMEEntity> entityCache_ = new HashMap<String, MIMEEntity>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#createMIMEEntity(java.lang.String)
	 */
	@Override
	public MIMEEntity createMIMEEntity(final String itemName) {
		// if (entityCache_.containsKey(itemName)) {
		// log_.warning("Returning MIMEEntity for " + itemName + " from cache instead of creating...");
		// return entityCache_.get(itemName);
		// }
		try {
			try {
				lotus.domino.MIMEEntity me = getDelegate().createMIMEEntity(itemName);
				markDirty();
				MIMEEntity wrapped = Factory.fromLotus(me, MIMEEntity.class, this);
				// entityCache_.put(itemName, wrapped);
				return wrapped;
			} catch (NotesException alreadyThere) {
				Item item = getFirstItem(itemName);
				if (item != null) {
					log_.warning("Already have a non-MIME item for " + itemName + ". Removing...");
					item.remove();
				}
				lotus.domino.MIMEEntity me = getDelegate().createMIMEEntity(itemName);
				markDirty();
				MIMEEntity wrapped = Factory.fromLotus(me, MIMEEntity.class, this);
				// entityCache_.put(itemName, wrapped);
				return wrapped;
			}
			// return Factory.fromLotus(getDelegate().createMIMEEntity(itemName), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#createReplyMessage(boolean)
	 */
	@Override
	public Document createReplyMessage(final boolean toAll) {
		// TODO - NTF markDirty()?
		try {
			return Factory.fromLotus(getDelegate().createReplyMessage(toAll), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#createRichTextItem(java.lang.String)
	 */
	@Override
	public RichTextItem createRichTextItem(final String name) {
		markDirty();
		try {
			return Factory.fromLotus(getDelegate().createRichTextItem(name), RichTextItem.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#encrypt()
	 */
	@Override
	public void encrypt() {
		markDirty();
		try {
			getDelegate().encrypt();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#generateXML()
	 */
	@Override
	public String generateXML() {
		try {
			return getDelegate().generateXML();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#generateXML(java.lang.Object, lotus.domino.XSLTResultTarget)
	 */
	@Override
	public void generateXML(final Object style, final lotus.domino.XSLTResultTarget result) throws IOException {
		try {
			getDelegate().generateXML(style, result);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#generateXML(java.io.Writer)
	 */
	@Override
	public void generateXML(final Writer w) throws IOException {
		try {
			getDelegate().generateXML(w);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getAttachment(java.lang.String)
	 */
	@Override
	public EmbeddedObject getAttachment(final String fileName) {
		try {
			return Factory.fromLotus(getDelegate().getAttachment(fileName), EmbeddedObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getAuthors()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getAuthors() {
		try {
			return getDelegate().getAuthors();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getColumnValues()
	 */
	@Override
	public Vector<Object> getColumnValues() {
		try {
			Vector<?> values = getDelegate().getColumnValues();
			if (values != null) {
				return Factory.wrapColumnValues(values, this.getAncestorSession());
			} else {
				return null;
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getEmbeddedObjects()
	 */
	@Override
	public Vector<org.openntf.domino.EmbeddedObject> getEmbeddedObjects() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getEmbeddedObjects(), org.openntf.domino.EmbeddedObject.class,
					this.getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getEncryptionKeys()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getEncryptionKeys() {
		try {
			return getDelegate().getEncryptionKeys();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getFTSearchScore()
	 */
	@Override
	public int getFTSearchScore() {
		try {
			return getDelegate().getFTSearchScore();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getFirstItem(java.lang.String)
	 */
	@Override
	public Item getFirstItem(final String name) {
		try {
			return Factory.fromLotus(getDelegate().getFirstItem(name), Item.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getFolderReferences()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getFolderReferences() {
		try {
			return getDelegate().getFolderReferences();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getHttpURL()
	 */
	@Override
	public String getHttpURL() {
		try {
			return getDelegate().getHttpURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * Behavior: If the document does not have the item, then we look at the requested class. If it's a primitive or an array of primitives,
	 * we cannot return a null value that can be assigned to that type, so therefore we throw an Exception. If what was request is an
	 * object, we return null.
	 * 
	 * If the item does exist, then we get it's value and attempt a conversion. If the data cannot be converted, we throw an Exception
	 */

	@SuppressWarnings("unchecked")
	public <T> T getItemValue(final String name, final Class<?> T) throws ItemNotFoundException, DataNotCompatibleException {
		// TODO NTF - Add type conversion extensibility of some kind, maybe attached to the Database or the Session
		// if (T.equals(java.util.Collection.class) && getItemValueString("form").equalsIgnoreCase("container")) {
		// System.out.println("Requesting a value of type " + T.getName() + " in name " + name);
		// }

		boolean hasMime = hasMIMEEntity(name);
		if (!hasMime) {
			Item item = getFirstItem(name);
			if (item != null && item.getType() == Item.MIME_PART) {
				return (T) getItemValueMIME(name);
			}
			// if (T.equals(java.util.Collection.class) && getItemValueString("form").equalsIgnoreCase("container")) {
			// System.out.println("No MIMEEntity found for " + name + ". Using regular item API...");
			// }
			Object result = TypeUtils.itemValueToClass(this, name, T);
			// if (T.equals(java.util.Collection.class) && getItemValueString("form").equalsIgnoreCase("container")) {
			// System.out.println("Returning a value of " + (result == null ? "null" : result.getClass().getSimpleName())
			// + " for value in " + name + " of type " + T.getSimpleName());
			// }

			return (T) result;
		} else {
			// if (T.equals(java.util.Collection.class) && getItemValueString("form").equalsIgnoreCase("container")) {
			// System.out.println("MIMEEntity found for " + name + ". Using MIMEBean strategy...");
			// }
			return (T) getItemValueMIME(name);
		}
	}

	private Object getItemValueMIME(final String name) {
		Object resultObj = null;
		try {
			Session session = this.getAncestorSession();
			boolean convertMime = session.isConvertMIME();
			session.setConvertMIME(false);

			MIMEEntity entity = this.getMIMEEntity(name);
			MIMEHeader contentType = entity.getNthHeader("Content-Type");
			if (contentType != null
					&& (contentType.getHeaderVal().equals("application/x-java-serialized-object") || contentType.getHeaderVal().equals(
							"application/x-java-externalized-object"))) {
				// Then it's a MIMEBean
				resultObj = DominoUtils.restoreState(this, name);
			}
			session.setConvertMIME(convertMime);
		} catch (Throwable t) {
			DominoUtils.handleException(new MIMEConversionException("Unable to getItemValueMIME for item name " + name + " on document "
					+ getNoteID(), t));
		}
		return resultObj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getItemValue(java.lang.String)
	 */
	@Override
	public Vector<Object> getItemValue(final String name) {
		try {
			// Check the item type to see if it's MIME - if so, then see if it's a MIMEBean
			// This is a bit more expensive than I'd like
			MIMEEntity entity = this.getMIMEEntity(name);
			if (entity != null) {
				Object mimeValue = getItemValueMIME(name);
				if (mimeValue instanceof Vector) {
					return (Vector<Object>) mimeValue;
				}
				if (mimeValue instanceof Collection) {
					return new Vector((Collection) mimeValue);
				}
				if (mimeValue.getClass().isArray()) {
					return (Vector<Object>) Arrays.asList((Object[]) mimeValue);
				}
				Vector<Object> result = new Vector<Object>(1);
				result.add(mimeValue);
				return result;
				// TODO NTF: What if we have a "real" mime item like a body field (Handle RT/MIME correctly)
			}
			Vector<?> vals = null;
			try {
				vals = getDelegate().getItemValue(name);
			} catch (NotesException ne) {
				log_.log(Level.WARNING, "Unable to get value for item " + name + " in Document " + getAncestorDatabase().getFilePath()
						+ " " + noteid_ + ": " + ne.text);
				return null;
			}
			return Factory.wrapColumnValues(vals, this.getAncestorSession());
		} catch (Throwable t) {
			// From DominoUtils.restoreState(...)
			DominoUtils.handleException(t);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getItemValueCustomData(java.lang.String)
	 */
	@Override
	public Object getItemValueCustomData(final String itemName) throws IOException, ClassNotFoundException {
		try {
			return getDelegate().getItemValueCustomData(itemName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getItemValueCustomData(java.lang.String, java.lang.String)
	 */
	@Override
	public Object getItemValueCustomData(final String itemName, final String dataTypeName) throws IOException, ClassNotFoundException {
		try {
			return getDelegate().getItemValueCustomData(itemName, dataTypeName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getItemValueCustomDataBytes(java.lang.String, java.lang.String)
	 */
	@Override
	public byte[] getItemValueCustomDataBytes(final String itemName, final String dataTypeName) throws IOException {
		try {
			return getDelegate().getItemValueCustomDataBytes(itemName, dataTypeName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getItemValueDateTimeArray(java.lang.String)
	 */
	@Override
	public Vector<org.openntf.domino.DateTime> getItemValueDateTimeArray(final String name) {
		try {
			return Factory.fromLotusAsVector(getDelegate().getItemValueDateTimeArray(name), org.openntf.domino.DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getItemValueDouble(java.lang.String)
	 */
	@Override
	public double getItemValueDouble(final String name) {
		try {
			return getDelegate().getItemValueDouble(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return 0d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getItemValueInteger(java.lang.String)
	 */
	@Override
	public int getItemValueInteger(final String name) {
		try {
			return getDelegate().getItemValueInteger(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getItemValueString(java.lang.String)
	 */
	@Override
	public String getItemValueString(final String name) {
		try {
			return getDelegate().getItemValueString(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getItems()
	 */
	@Override
	public Vector<Item> getItems() {
		ItemVector iv = new ItemVector(this);
		return iv;
		// try {
		// return Factory.fromLotusAsVector(getDelegate().getItems(), Item.class, this);
		// } catch (NotesException e) {
		// DominoUtils.handleException(e);
		// }
		// return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getKey()
	 */
	@Override
	public String getKey() {
		try {
			return getDelegate().getKey();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getLockHolders()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getLockHolders() {
		try {
			return getDelegate().getLockHolders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getMIMEEntity()
	 */
	@Override
	public MIMEEntity getMIMEEntity() {
		try {
			return Factory.fromLotus(getDelegate().getMIMEEntity(), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getMIMEEntity(java.lang.String)
	 */
	@Override
	public MIMEEntity getMIMEEntity(final String itemName) {
		// if (entityCache_.containsKey(itemName)) {
		// log_.warning("Returning MIMEEntity " + itemName + " from local document cache...");
		// return entityCache_.get(itemName);
		// }
		try {
			MIMEEntity wrapped = Factory.fromLotus(getDelegate().getMIMEEntity(itemName), MIMEEntity.class, this);
			// entityCache_.put(itemName, wrapped);
			return wrapped;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getNameOfProfile()
	 */
	@Override
	public String getNameOfProfile() {
		try {
			return getDelegate().getNameOfProfile();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getNoteID()
	 */
	@Override
	public String getNoteID() {
		try {
			return getDelegate().getNoteID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getNotesURL()
	 */
	@Override
	public String getNotesURL() {
		try {
			return getDelegate().getNotesURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getParentDatabase()
	 */
	@Override
	public Database getParentDatabase() {
		return (Database) super.getParent();
	}

	public Document getParentDocument() {
		return this.getParentDatabase().getDocumentByUNID(this.getParentDocumentUNID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getParentDocumentUNID()
	 */
	@Override
	public String getParentDocumentUNID() {
		try {
			return getDelegate().getParentDocumentUNID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getParentView()
	 */
	@Override
	public View getParentView() {
		try {
			return Factory.fromLotus(getDelegate().getParentView(), View.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getRead()
	 */
	@Override
	public boolean getRead() {
		try {
			return getDelegate().getRead();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getRead(java.lang.String)
	 */
	@Override
	public boolean getRead(final String userName) {
		try {
			return getDelegate().getRead(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getReceivedItemText()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getReceivedItemText() {
		try {
			return getDelegate().getReceivedItemText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getResponses()
	 */
	@Override
	public DocumentCollection getResponses() {
		try {
			return Factory.fromLotus(getDelegate().getResponses(), DocumentCollection.class, Factory.getParentDatabase(this));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getSigner()
	 */
	@Override
	public String getSigner() {
		try {
			return getDelegate().getSigner();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getSize()
	 */
	@Override
	public int getSize() {
		try {
			return getDelegate().getSize();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getURL()
	 */
	@Override
	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getUniversalID()
	 */
	@Override
	public String getUniversalID() {
		try {
			return getDelegate().getUniversalID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#getVerifier()
	 */
	@Override
	public String getVerifier() {
		try {
			return getDelegate().getVerifier();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#hasEmbedded()
	 */
	@Override
	public boolean hasEmbedded() {
		try {
			return getDelegate().hasEmbedded();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	//	private Boolean hasReaders_;

	public boolean hasReaders() {
		//TODO won't that be handy?

		for (Item item : getItems()) {
			if (item.isReaders()) {
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#hasItem(java.lang.String)
	 */
	@Override
	public boolean hasItem(final String name) {
		try {
			if (name == null) {
				return false;
			} else {
				return getDelegate().hasItem(name);
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	public boolean hasMIMEEntity(final String name) {
		boolean result = false;
		Session session = this.getAncestorSession();
		boolean convertMime = session.isConvertMIME();
		session.setConvertMIME(false);

		MIMEEntity entity = this.getMIMEEntity(name);
		if (entity != null)
			result = true;
		session.setConvertMIME(convertMime);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#isDeleted()
	 */
	@Override
	public boolean isDeleted() {
		try {
			lotus.domino.Document delegate = getDelegate();
			if (delegate == null)
				return false;
			return delegate.isDeleted();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#isEncryptOnSend()
	 */
	@Override
	public boolean isEncryptOnSend() {
		try {
			return getDelegate().isEncryptOnSend();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#isEncrypted()
	 */
	@Override
	public boolean isEncrypted() {
		try {
			return getDelegate().isEncrypted();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#isNewNote()
	 */
	@Override
	public boolean isNewNote() {
		try {
			return getDelegate().isNewNote();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#isPreferJavaDates()
	 */
	@Override
	public boolean isPreferJavaDates() {
		try {
			return getDelegate().isPreferJavaDates();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#isProfile()
	 */
	@Override
	public boolean isProfile() {
		try {
			return getDelegate().isProfile();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#isResponse()
	 */
	@Override
	public boolean isResponse() {
		try {
			return getDelegate().isResponse();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#isSaveMessageOnSend()
	 */
	@Override
	public boolean isSaveMessageOnSend() {
		try {
			return getDelegate().isSaveMessageOnSend();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#isSentByAgent()
	 */
	@Override
	public boolean isSentByAgent() {
		try {
			return getDelegate().isSentByAgent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#isSignOnSend()
	 */
	@Override
	public boolean isSignOnSend() {
		try {
			return getDelegate().isSignOnSend();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#isSigned()
	 */
	@Override
	public boolean isSigned() {
		try {
			return getDelegate().isSigned();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#isValid()
	 */
	@Override
	public boolean isValid() {
		try {
			return getDelegate().isValid();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#lock()
	 */
	@Override
	public boolean lock() {
		try {
			return getDelegate().lock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#lock(boolean)
	 */
	@Override
	public boolean lock(final boolean provisionalOk) {
		try {
			return getDelegate().lock(provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#lock(java.lang.String)
	 */
	@Override
	public boolean lock(final String name) {
		try {
			return getDelegate().lock(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#lock(java.lang.String, boolean)
	 */
	@Override
	public boolean lock(final String name, final boolean provisionalOk) {
		try {
			return getDelegate().lock(name, provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#lock(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(final Vector names) {
		try {
			return getDelegate().lock(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#lock(java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(final Vector names, final boolean provisionalOk) {
		try {
			return getDelegate().lock(names, provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#lockProvisional()
	 */
	@Override
	public boolean lockProvisional() {
		try {
			return getDelegate().lockProvisional();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#lockProvisional(java.lang.String)
	 */
	@Override
	public boolean lockProvisional(final String name) {
		try {
			return getDelegate().lockProvisional(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#lockProvisional(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean lockProvisional(final Vector names) {
		try {
			return getDelegate().lockProvisional(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#makeResponse(lotus.domino.Document)
	 */
	@Override
	public void makeResponse(final lotus.domino.Document doc) {
		markDirty();
		try {
			getDelegate().makeResponse((lotus.domino.Document) toLotus(doc));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#markRead()
	 */
	@Override
	public void markRead() {
		// TODO - NTF transaction context?
		try {
			getDelegate().markRead();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#markRead(java.lang.String)
	 */
	@Override
	public void markRead(final String userName) {
		// TODO - NTF transaction context?
		try {
			getDelegate().markRead(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#markUnread()
	 */
	@Override
	public void markUnread() {
		// TODO - NTF transaction context?
		try {
			getDelegate().markUnread();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#markUnread(java.lang.String)
	 */
	@Override
	public void markUnread(final String userName) {
		// TODO - NTF transaction context?
		try {
			getDelegate().markUnread(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#putInFolder(java.lang.String)
	 */
	@Override
	public void putInFolder(final String name) {
		// TODO - NTF handle transaction context
		try {
			getDelegate().putInFolder(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#putInFolder(java.lang.String, boolean)
	 */
	@Override
	public void putInFolder(final String name, final boolean createOnFail) {
		// TODO - NTF handle transaction context
		try {
			getDelegate().putInFolder(name, createOnFail);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#remove(boolean)
	 */
	@Override
	public boolean remove(final boolean force) {
		boolean result = false;
		boolean go = true;
		go = getAncestorDatabase().fireListener(generateEvent(Events.BEFORE_DELETE_DOCUMENT, null));
		if (go) {
			System.out.println("Listener for BEFORE_DELETE_DOCUMENT allowed the remove call");
			removeType_ = force ? RemoveType.SOFT_TRUE : RemoveType.SOFT_FALSE;
			System.out.println("Remove type is " + removeType_.name());
			if (queueRemove()) {
				System.out.println("We queued the remove as part of a transaction so tell the calling code that its done");
				result = true;
			} else {
				System.out.println("We're not currently in a transaction, so we should force the delegate removal immediately");
				result = forceDelegateRemove();
			}
		} else {
			System.out.println("Listener for BEFORE_DELETE_DOCUMENT blocked the remove call");
			result = false;
		}
		if (result) {
			System.out.println("Remove executed, so firing AFTER_DELETE_DOCUMENT listener");
			getAncestorDatabase().fireListener(generateEvent(Events.AFTER_DELETE_DOCUMENT, null));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#removeFromFolder(java.lang.String)
	 */
	@Override
	public void removeFromFolder(final String name) {
		// TODO - NTF handle transaction context
		try {
			getDelegate().removeFromFolder(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#removeItem(java.lang.String)
	 */
	@Override
	public void removeItem(final String name) {
		markDirty();
		keySet();
		fieldNames_.remove(name);
		try {
			if (getAncestorSession().isFixEnabled(Fixes.REMOVE_ITEM)) {
				while (getDelegate().hasItem(name)) {
					getDelegate().removeItem(name);
				}
			} else {
				getDelegate().removeItem(name);
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#removePermanently(boolean)
	 */
	@Override
	public boolean removePermanently(final boolean force) {
		boolean result = false;
		boolean go = true;
		go = getAncestorDatabase().fireListener(generateEvent(Events.BEFORE_DELETE_DOCUMENT, null));
		if (go) {
			removeType_ = force ? RemoveType.HARD_TRUE : RemoveType.HARD_FALSE;
			if (!queueRemove()) {
				result = forceDelegateRemove();
			} else {
				result = true;
			}
		} else {
			result = false;
		}
		if (result) {
			getAncestorDatabase().fireListener(generateEvent(Events.AFTER_DELETE_DOCUMENT, null));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#renderToRTItem(lotus.domino.RichTextItem)
	 */
	@Override
	public boolean renderToRTItem(final lotus.domino.RichTextItem rtitem) {
		try {
			getDelegate().renderToRTItem((lotus.domino.RichTextItem) toLotus(rtitem));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Document#replaceItemValue(java.lang.String, java.lang.Object, java.lang.Boolean)
	 */
	@Override
	public Item replaceItemValue(final String itemName, final Object value, final boolean isSummary) {
		markDirty();
		Item result = replaceItemValue(itemName, value);
		if (result != null && result.isSummary() != isSummary)
			result.setSummary(isSummary);
		return result;
	}

	public static int MAX_NATIVE_VECTOR_SIZE = 255;
	public static int MAX_NATIVE_STRING_SIZE = 32000;
	public static int MAX_SUMMARY_STRING_SIZE = 14000;

	private lotus.domino.Item replaceItemValueExt(final String itemName, final Object value, final IllegalArgumentException iae)
			throws Exception {
		lotus.domino.Item result = null;
		boolean oldConvert = getAncestorSession().isConvertMime();
		getAncestorSession().setConvertMime(false);
		// Then try serialization
		if (value instanceof Serializable) {
			DominoUtils.saveState((Serializable) value, this, itemName);

			result = getDelegate().getFirstItem(itemName);
			// result = null;
		} else if (value instanceof DocumentCollection) {
			// NoteIDs would be faster for this and, particularly, NoteCollection, but it should be replica-friendly
			DocumentCollection docs = (DocumentCollection) value;
			String[] unids = new String[docs.getCount()];
			int index = 0;
			for (org.openntf.domino.Document doc : docs) {
				unids[index++] = doc.getUniversalID();
			}
			Map<String, String> headers = new HashMap<String, String>(1);
			headers.put("X-Original-Java-Class", "org.openntf.domino.DocumentCollection");
			DominoUtils.saveState(unids, this, itemName, true, headers);
			// result = null;
			result = getDelegate().getFirstItem(itemName);
		} else if (value instanceof NoteCollection) {
			// Maybe it'd be faster to use .getNoteIDs - I'm not sure how the performance compares
			NoteCollection notes = (NoteCollection) value;
			String[] unids = new String[notes.getCount()];
			String noteid = notes.getFirstNoteID();
			int index = 0;
			while (noteid != null && !noteid.isEmpty()) {
				unids[index++] = notes.getUNID(noteid);
				noteid = notes.getNextNoteID(noteid);
			}
			Map<String, String> headers = new HashMap<String, String>(1);
			headers.put("X-Original-Java-Class", "org.openntf.domino.NoteCollection");
			DominoUtils.saveState(unids, this, itemName, true, headers);
			// result = null;
			result = getDelegate().getFirstItem(itemName);
		} else {
			// Check to see if it's a StateHolder
			try {
				Class<?> stateHolderClass = Class.forName("javax.faces.component.StateHolder", true, Factory.getClassLoader());
				if (stateHolderClass.isInstance(value)) {
					Class<?> facesContextClass = Class.forName("javax.faces.context.FacesContext", true, Factory.getClassLoader());
					Method getCurrentInstance = facesContextClass.getMethod("getCurrentInstance");
					Method saveState = stateHolderClass.getMethod("saveState", facesContextClass);
					Serializable state = (Serializable) saveState.invoke(value, getCurrentInstance.invoke(null));
					Map<String, String> headers = new HashMap<String, String>();
					headers.put("X-Storage-Scheme", "StateHolder");
					headers.put("X-Original-Java-Class", value.getClass().getName());
					DominoUtils.saveState(state, this, itemName, true, headers);
					//					result = null;
					result = getDelegate().getFirstItem(itemName);
				} else {
					// Well, we tried.
					throw iae;
				}
			} catch (ClassNotFoundException cnfe) {
				throw iae;
			}
		}
		getAncestorSession().setConvertMime(oldConvert);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#replaceItemValue(java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Item replaceItemValue(final String itemName, Object value) {
		// if (getItemValueString("form").equalsIgnoreCase("container") && itemName.equals(DominoVertex.IN_NAME)) {
		// System.out.println("Replacing a value in " + itemName + " with a type of "
		// + (value == null ? "null" : value.getClass().getSimpleName()));
		// }
		markDirty();
		if (!keySet().contains(itemName)) {
			fieldNames_.add(itemName);
		}
		if (value == null || value instanceof Null) {
			if (hasItem(itemName)) {
				if (getAncestorSession().isFixEnabled(Fixes.REPLACE_ITEM_NULL)) {
					removeItem(itemName);
					return null;
				} else {
					value = "";
				}
			} else {
				return null;
			}
		}
		try {
			lotus.domino.Item result = null;
			Class<?> valueClass = value.getClass();
			boolean isNonSummary = false;	// why isNon instead of is? Because we want to default to the existing behavior and only mark
											// non-summary by exception
			try {
				if (value instanceof Collection || value instanceof Object[]) {
					if (value instanceof Collection) {
						if (((Collection) value).size() > MAX_NATIVE_VECTOR_SIZE) {
							throw new IllegalArgumentException();
						}
					} else if (value instanceof Object[]) {
						if (((Object[]) value).length > MAX_NATIVE_VECTOR_SIZE) {
							throw new IllegalArgumentException();
						}
					}
					Vector<Object> resultList = new Vector<Object>();
					Class<?> objectClass = null;
					long totalStringSize = 0;

					Collection<?> listValue = value instanceof Collection ? (Collection<?>) value : Arrays.asList((Object[]) value);

					for (Object valNode : listValue) {
						if (valNode instanceof BigString)
							isNonSummary = true;
						Object domNode = toDominoFriendly(valNode, this);
						if (domNode != null) {
							if (objectClass == null) {
								objectClass = domNode.getClass();
							} else {
								if (!objectClass.equals(domNode.getClass())) {
									// Domino only allows uniform lists
									// There may have been some native DateTimes added in so far; burn them
									enc_recycle(resultList);
									if (log_.isLoggable(Level.WARNING)) {
										log_.log(Level.WARNING, "Attempt to write a non-uniform list to item " + itemName + " in document "
												+ getAncestorDatabase().getFilePath() + ": " + getUniversalID()
												+ ". The list contains items of both " + objectClass.getName() + " and "
												+ domNode.getClass().getName() + ". A MIME entity will be used instead.");
									}
									throw new IllegalArgumentException();
								}
							}
							if (domNode instanceof String) {
								totalStringSize += ((String) domNode).length();
								if (totalStringSize > MAX_SUMMARY_STRING_SIZE)
									isNonSummary = true;

								// Escape to serializing if there's too much text data
								// Leave fudge room for multibyte? This is clearly not the best way to do it
								if (totalStringSize > MAX_NATIVE_STRING_SIZE) {
									throw new IllegalArgumentException();
								}
							}
							resultList.add(domNode);
						} else {
							log_.log(Level.INFO, "toDominoFriendly returned a null value for an original list member value of "
									+ (valNode == null ? "null" : valNode.getClass().getName()));
						}
					}
					// If it ended up being something we could store, make note of the original class instead of the list class
					if (!(listValue).isEmpty()) {
						//						valueClass = (listValue).get(0).getClass();
						MIMEEntity mimeChk = getMIMEEntity(itemName);
						if (mimeChk != null) {
							mimeChk.remove();
						}
					}
					try {
						result = getDelegate().replaceItemValue(itemName, resultList);
						if (isNonSummary) {
							result.setSummary(false);
						}
					} catch (NotesException ne) {
						String msg = ne.text;
						if (msg.equalsIgnoreCase("Cannot convert item to requested datatype")) {
							String types = "";
							if (resultList instanceof Vector) {
								StringBuilder elemType = new StringBuilder();
								for (Object o : ((Vector) resultList)) {
									if (o != null) {
										elemType.append(o.getClass().getSimpleName() + ", ");
									} else {
										elemType.append("null, ");
									}
								}
								types = " DETAILS: " + elemType.toString();
							}
							throw new DataNotCompatibleException("Unable to write a " + resultList.getClass().getName()
									+ (resultList.isEmpty() ? " empty" : types) + " object (originally " + value.getClass().getName()
									+ ") to item " + itemName + " in document " + unid_ + " in " + getAncestorDatabase().getFilePath());
						} else {
							DominoUtils.handleException(ne);
						}
					} finally {
						enc_recycle(resultList);
					}
				} else {
					if (value instanceof BigString)
						isNonSummary = true;
					Object domNode = toDominoFriendly(value, this);
					try {
						if (domNode instanceof String) {
							String s = (String) domNode;
							if (s.equals("\n") || s.equals("\r") || s.equals("\r\n")) {
								// Domino can't store linefeed only in item
								throw new IllegalArgumentException();
							}
							if (s.length() > MAX_SUMMARY_STRING_SIZE)
								isNonSummary = true;
							if (s.length() > MAX_NATIVE_STRING_SIZE) {
								throw new IllegalArgumentException();
							}
						}
						MIMEEntity mimeChk = getMIMEEntity(itemName);
						if (mimeChk != null) {
							mimeChk.remove();
						}
						result = getDelegate().replaceItemValue(itemName, domNode);
						if (isNonSummary)
							result.setSummary(false);
					} catch (NotesException nativeError) {
						log_.warning("Native error occured when replacing " + itemName + " item on doc " + this.noteid_
								+ " with a value of type " + (domNode == null ? "null" : domNode.getClass().getName()) + " of value "
								+ String.valueOf(domNode));
					} finally {
						Base.enc_recycle(domNode);
					}
				}
			} catch (IllegalArgumentException iae) {
				result = this.replaceItemValueExt(itemName, value, iae);
			}

			if (this.shouldWriteItemMeta_) {
				// If we've gotten this far, it must be legal - update or create the item info map

				Map<String, Map<String, Serializable>> itemInfo = getItemInfo();
				Map<String, Serializable> infoNode = null;
				if (itemInfo.containsKey(itemName)) {
					infoNode = itemInfo.get(itemName);
				} else {
					infoNode = new HashMap<String, Serializable>();
				}
				infoNode.put("valueClass", valueClass.getName());
				infoNode.put("updated", new Date()); // For sanity checking if the value was changed outside of Java
				itemInfo.put(itemName, infoNode);
			}
			if (result != null) {
				return Factory.fromLotus(result, Item.class, this);
			}
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
		return null;
	}

	private void writeItemInfo() {
		if (this.shouldWriteItemMeta_) {
			Map<String, Map<String, Serializable>> itemInfo = getItemInfo();
			if (itemInfo != null && itemInfo.size() > 0) {
				boolean convertMime = this.getAncestorSession().isConvertMime();
				this.getAncestorSession().setConvertMime(false);
				try {
					DominoUtils.saveState((Serializable) getItemInfo(), this, "$$ItemInfo", false, null);
				} catch (Throwable e) {
					DominoUtils.handleException(e);
				}
				this.getAncestorSession().setConvertMime(convertMime);
			}
		}
	}

	private Map<String, Map<String, Serializable>> itemInfo_;

	public Map<String, Map<String, Serializable>> getItemInfo() {
		// TODO NTF make this optional
		if (itemInfo_ == null) {
			if (this.hasItem("$$ItemInfo")) {
				if (this.getFirstItem("$$ItemInfo").getType() == Item.MIME_PART) {
					// Then use the existing value
					try {
						itemInfo_ = (Map<String, Map<String, Serializable>>) DominoUtils.restoreState(this, "$$ItemInfo");
					} catch (Throwable t) {
						DominoUtils.handleException(t);
					}
				} else {
					// Then destroy it (?)
					this.removeItem("$$ItemInfo");
					itemInfo_ = new TreeMap<String, Map<String, Serializable>>();
				}
			} else {
				itemInfo_ = new TreeMap<String, Map<String, Serializable>>();
			}
		}
		return itemInfo_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#replaceItemValueCustomData(java.lang.String, java.lang.Object)
	 */
	@Override
	public Item replaceItemValueCustomData(final String itemName, final Object userObj) throws IOException {
		markDirty();
		try {
			return Factory.fromLotus(getDelegate().replaceItemValueCustomData(itemName, userObj), Item.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#replaceItemValueCustomData(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public Item replaceItemValueCustomData(final String itemName, final String dataTypeName, final Object userObj) throws IOException {
		markDirty();
		try {
			return Factory.fromLotus(getDelegate().replaceItemValueCustomData(itemName, dataTypeName, userObj), Item.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#replaceItemValueCustomDataBytes(java.lang.String, java.lang.String, byte[])
	 */
	@Override
	public Item replaceItemValueCustomDataBytes(final String itemName, final String dataTypeName, final byte[] byteArray)
			throws IOException {
		markDirty();
		try {
			if (byteArray.length > 65535) {
				// Then fall back to the normal method, which will MIMEBean it
				return this.replaceItemValue(itemName, byteArray);
			} else {
				return Factory
						.fromLotus(getDelegate().replaceItemValueCustomDataBytes(itemName, dataTypeName, byteArray), Item.class, this);
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#save()
	 */
	@Override
	public boolean save() {
		boolean result = false;
		result = save(false, false, false);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#save(boolean)
	 */
	@Override
	public boolean save(final boolean force) {
		boolean result = false;
		result = save(force, false, false);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#save(boolean, boolean)
	 */
	@Override
	public boolean save(final boolean force, final boolean makeResponse) {
		boolean result = false;
		result = save(force, makeResponse, false);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#save(boolean, boolean, boolean)
	 */
	@Override
	public boolean save(final boolean force, final boolean makeResponse, final boolean markRead) {
		// System.out.println("Starting save operation...");
		boolean result = false;
		if (removeType_ != null) {
			log_.log(Level.INFO, "Save called on a document marked for a transactional delete. So there's no point...");
			return true;
		}
		if (isDirty()) {
			boolean go = true;
			go = getAncestorDatabase().fireListener(generateEvent(Events.BEFORE_UPDATE_DOCUMENT, null));
			if (go) {
				writeItemInfo();
				isNew_ = false;
				try {
					lotus.domino.Document del = getDelegate();
					if (del != null) {
						result = del.save(force, makeResponse, markRead);
						if (noteid_ == null || !noteid_.equals(del.getNoteID())) {
							// System.out.println("Resetting note id from " + noteid_ + " to " + del.getNoteID());
							noteid_ = del.getNoteID();
						}
						if (unid_ == null || !unid_.equals(del.getUniversalID())) {
							// System.out.println("Resetting unid from " + unid_ + " to " + del.getUniversalID());
							unid_ = del.getUniversalID();
						}
					} else {
						log_.severe("Delegate document for " + unid_ + " is NULL!??!");
					}

				} catch (NotesException e) {
					// System.out.println("Exception from attempted save...");
					// e.printStackTrace();
					if (e.text.contains("Database already contains a document with this ID")) {
						//						Throwable t = new RuntimeException();
						String newunid = DominoUtils.toUnid(new Date().getTime());
						String message = "Unable to save a document with id " + getUniversalID()
								+ " because that id already exists. Saving a " + this.getFormName()
								+ (this.hasItem("$$Key") ? " (" + getItemValueString("$$Key") + ")" : "")
								+ " to a different unid instead: " + newunid;
						setUniversalID(newunid);
						try {
							getDelegate().save(force, makeResponse, markRead);
							if (!noteid_.equals(getDelegate().getNoteID())) {
								noteid_ = getDelegate().getNoteID();
							}
							System.out.println(message);
							log_.log(Level.WARNING, message/* , t */);
						} catch (NotesException ne) {
							log_.log(Level.SEVERE, "Okay, now it's time to really panic. Sorry...");
							DominoUtils.handleException(e);
						}
					} else {
						DominoUtils.handleException(e);
					}
				}
				if (result) {
					clearDirty();
					getAncestorDatabase().fireListener(generateEvent(Events.AFTER_UPDATE_DOCUMENT, null));
				}
			} else {
				// System.out.println("Before Update listener blocked save.");
				if (log_.isLoggable(Level.FINE)) {
					log_.log(Level.FINE, "Document " + getNoteID()
							+ " was not saved because the DatabaseListener for update returned false.");
				}
				result = false;
			}
		} else {
			// System.out.println("No changes occured therefore not saving.");
			if (log_.isLoggable(Level.FINE)) {
				log_.log(Level.FINE, "Document " + getNoteID() + " was not saved because nothing on it was changed.");
			}
			result = true; // because nothing changed, we don't want to activate any potential failure behavior in the caller
		}
		// System.out.println("Save completed returning " + String.valueOf(result));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#send()
	 */
	@Override
	public void send() {
		// TODO - NTF handle transaction context
		try {
			getDelegate().send();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#send(boolean)
	 */
	@Override
	public void send(final boolean attachForm) {
		// TODO - NTF handle transaction context
		try {
			getDelegate().send(attachForm);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#send(boolean, java.lang.String)
	 */
	@Override
	public void send(final boolean attachForm, final String recipient) {
		// TODO - NTF handle transaction context
		try {
			getDelegate().send(attachForm, recipient);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#send(boolean, java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void send(final boolean attachForm, final Vector recipients) {
		// TODO - NTF handle transaction context
		try {
			getDelegate().send(attachForm, recipients);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#send(java.lang.String)
	 */
	@Override
	public void send(final String recipient) {
		// TODO - NTF handle transaction context
		try {
			getDelegate().send(recipient);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#send(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void send(final Vector recipients) {
		// TODO - NTF handle transaction context
		try {
			getDelegate().send(recipients);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#setEncryptOnSend(boolean)
	 */
	@Override
	public void setEncryptOnSend(final boolean flag) {
		try {
			getDelegate().setEncryptOnSend(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#setEncryptionKeys(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setEncryptionKeys(final Vector keys) {
		try {
			getDelegate().setEncryptionKeys(keys);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		markDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#setPreferJavaDates(boolean)
	 */
	@Override
	public void setPreferJavaDates(final boolean flag) {
		try {
			getDelegate().setPreferJavaDates(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#setSaveMessageOnSend(boolean)
	 */
	@Override
	public void setSaveMessageOnSend(final boolean flag) {
		// TODO NTF - mark dirty?
		try {
			getDelegate().setSaveMessageOnSend(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#setSignOnSend(boolean)
	 */
	@Override
	public void setSignOnSend(final boolean flag) {
		// TODO NTF - mark dirty?
		try {
			getDelegate().setSignOnSend(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#setUniversalID(java.lang.String)
	 */
	@Override
	public void setUniversalID(final String unid) {
		try {
			try {
				lotus.domino.Document del = getDelegate().getParentDatabase().getDocumentByUNID(unid);
				if (del != null) { // this is surprising. Why didn't we already get it?
					log_.log(Level.WARNING,
							"Document " + unid + " already existed in the database with noteid " + del.getNoteID()
									+ " and we're trying to set a doc with noteid " + getNoteID() + " to that. The existing document is a "
									+ del.getItemValueString("form") + " and the new document is a " + getItemValueString("form"));
					if (isDirty()) { // we've already made other changes that we should tuck away...
						log_.log(Level.WARNING,
								"Attempting to stash changes to this document to apply to other document of the same UNID. This is pretty dangerous...");
						Document stashDoc = copyToDatabase(getParentDatabase());
						setDelegate(del);
						for (Item item : stashDoc.getItems()) {
							lotus.domino.Item delItem = del.getFirstItem(item.getName());
							if (delItem != null) {
								lotus.domino.DateTime delDt = delItem.getLastModified();
								java.util.Date delDate = delDt.toJavaDate();
								delDt.recycle();
								Date modDate = item.getLastModifiedDate();
								if (modDate.after(delDate)) {
									item.copyItemToDocument(del);
								}
							} else {
								item.copyItemToDocument(del);
							}
							// TODO NTF properties?
						}
					} else {
						log_.log(Level.WARNING, "Resetting delegate to existing document for id " + unid);
						setDelegate(del);
					}
				} else {
					getDelegate().setUniversalID(unid);
				}
			} catch (NotesException ne) {
				// this is what's expected
				getDelegate().setUniversalID(unid);
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		unid_ = unid;
		markDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#sign()
	 */
	@Override
	public void sign() {
		try {
			getDelegate().sign();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		markDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Document#unlock()
	 */
	@Override
	public void unlock() {
		try {
			getDelegate().unlock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void markDirty() {
		isDirty_ = true;
		if (!isQueued_) {
			DatabaseTransaction txn = getParentDatabase().getTransaction();
			if (txn != null) {
				txn.queueUpdate(this);
				isQueued_ = true;
			}
		}
	}

	private boolean queueRemove() {
		if (!isRemoveQueued_) {
			DatabaseTransaction txn = getParentDatabase().getTransaction();
			if (txn != null) {
				System.out.println("Found a transaction: " + txn + " from parent Database " + getParentDatabase().getApiPath());
				txn.queueRemove(this);
				isRemoveQueued_ = true;
				return true; // we queued this, so whoever asked shouldn't do it yet.
			} else {
				return false; // calling function should just go ahead and execute
			}
		} else { // we already queued this for removal.
			return false;
		}
	}

	void clearDirty() {
		isDirty_ = false;
		isQueued_ = false;
	}

	public void rollback() {
		if (removeType_ != null)
			removeType_ = null;
		if (isDirty()) {
			String nid = getNoteID();
			try {
				lotus.domino.Database delDb = getDelegate().getParentDatabase();
				getDelegate().recycle();
				shouldResurrect_ = true;
				// lotus.domino.Document junkDoc = delDb.createDocument(); // NTF - Why? To make sure I get a new cppid. Otherwise the
				// handle
				// gets reused
				// lotus.domino.Document resetDoc = delDb.getDocumentByID(nid);
				// setDelegate(resetDoc);
				// junkDoc.recycle();
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
			clearDirty();
		}
	}

	public boolean isDirty() {
		return isDirty_;
	}

	public boolean forceDelegateRemove() {
		boolean result = false;
		RemoveType type = removeType_;
		System.out.println("Forcing delegate removal of type " + type == null ? "null!" : type.name());
		try {
			switch (type) {
			case SOFT_FALSE:
				result = getDelegate().remove(false);
				break;
			case SOFT_TRUE:
				result = getDelegate().remove(true);
				break;
			case HARD_TRUE:
				lotus.domino.Document delegate = getDelegate();
				result = delegate.removePermanently(true);
				if (result) {
					Base.s_recycle(delegate);
					this.setDelegate(null);
				}
				break;
			case HARD_FALSE:
				result = getDelegate().removePermanently(false);
				break;
			default:
				System.out.println("UNKNOWN REMOVE TYPE!");
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		System.out.println("Delegate remove call returned " + String.valueOf(result));
		return result;
	}

	@Override
	protected lotus.domino.Document getDelegate() {
		lotus.domino.Document d = super.getDelegate();
		try {
			d.isProfile();
		} catch (NotesException recycleSucks) {
			// if (shouldResurrect_) {
			resurrect();
			// }
		}
		return super.getDelegate();
	}

	private void resurrect() {
		if (noteid_ != null) {
			try {
				lotus.domino.Document d = null;
				lotus.domino.Database db = ((org.openntf.domino.impl.Database) getParentDatabase()).getDelegate();
				if (db != null) {
					if (Integer.valueOf(noteid_, 16) == 0) {
						if (isNew_) {
							d = db.createDocument();
							d.setUniversalID(unid_);
							if (log_.isLoggable(Level.FINE)) {
								log_.log(Level.FINE, "NO NOTEID AVAILABLE for document unid " + String.valueOf(unid_)
										+ ". However the document was new, so we'll just create a new one.");
							}
						} else {
							log_.log(Level.INFO, "ALERT! NO NOTEID AVAILABLE for document unid " + String.valueOf(unid_)
									+ ". It is questionable whether this document can successfully be resurrected.");
							try {
								d = db.getDocumentByUNID(unid_);
							} catch (NotesException ne) {
								log_.log(Level.WARNING, "Attempted to resurrect non-new document unid " + String.valueOf(unid_)
										+ ", but the document was not found in " + getParentDatabase().getServer() + "!!"
										+ getParentDatabase().getFilePath() + " because of: " + ne.text);
							}
						}
					} else {
						d = db.getDocumentByID(noteid_);
					}
				}
				// if (noteid_ == null || Integer.valueOf(noteid_, 16) == 0) {
				// log_.log(Level.WARNING,
				// "Noteid is null or zero. May not be able to resurrect. Attempting to use UNID " + String.valueOf(unid_));
				// if (unid_ == null) {
				// log_.log(Level.SEVERE, "UNID is null. Will not be able to resurrect...");
				// } else {
				// d = ((org.openntf.domino.impl.Database) getParentDatabase()).getDelegate().getDocumentByUNID(unid_);
				// }
				//
				// } else {
				// d = ((org.openntf.domino.impl.Database) getParentDatabase()).getDelegate().getDocumentByID(noteid_);
				// }
				setDelegate(d);
				shouldResurrect_ = false;
				if (log_.isLoggable(Level.FINE)) {
					log_.log(Level.FINE, "Document " + noteid_ + " in database path " + getParentDatabase().getFilePath()
							+ " had been recycled and was auto-restored. Changes may have been lost.");
					if (log_.isLoggable(Level.FINER)) {
						Throwable t = new Throwable();
						StackTraceElement[] elements = t.getStackTrace();
						log_.log(Level.FINER,
								elements[0].getClassName() + "." + elements[0].getMethodName() + " ( line " + elements[0].getLineNumber()
										+ ")");
						log_.log(Level.FINER,
								elements[1].getClassName() + "." + elements[1].getMethodName() + " ( line " + elements[1].getLineNumber()
										+ ")");
						log_.log(Level.FINER,
								elements[2].getClassName() + "." + elements[2].getMethodName() + " ( line " + elements[2].getLineNumber()
										+ ")");
					}
					log_.log(Level.FINE,
							"If you recently rollbacked a transaction and this document was included in the rollback, this outcome is normal.");
				}
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
		} else {
			if (log_.isLoggable(Level.SEVERE)) {
				log_.log(Level.SEVERE,
						"Document doesn't have noteid value. Something went terribly wrong. Nothing good can come of this...");
			}
		}
	}

	/*
	 * Map methods
	 */

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsKey(final Object key) {
		return this.hasItem(key == null ? null : String.valueOf(key));
	}

	@Override
	public boolean containsValue(final Object value) {
		// JG - God, I hope nobody ever actually uses this method
		// NTF - Actually I have some good use cases for it! WHEEEEEE!!
		for (String key : this.keySet()) {
			if (hasItem(key) && value instanceof CharSequence) {
				Item item = getFirstItem(key);
				if (item instanceof RichTextItem) {
					String text = ((RichTextItem) item).getText();
					return text.contains((CharSequence) value);
				}
			}
			Object itemVal = this.get(key);
			if (itemVal instanceof List) {
				return ((List) itemVal).contains(value);
			}
			if ((value == null && itemVal == null) || (value != null && value.equals(itemVal))) {
				return true;
			}
		}
		return false;
	}

	public boolean containsValue(final Object value, final String[] itemnames) {
		for (String key : itemnames) {
			if (hasItem(key) && value instanceof CharSequence) {
				Item item = getFirstItem(key);
				if (item instanceof RichTextItem) {
					String text = ((RichTextItem) item).getText();
					return text.contains((CharSequence) value);
				}
			}
			Object itemVal = this.get(key);
			if (itemVal instanceof List) {
				return ((List) itemVal).contains(value);
			}
			if ((value == null && itemVal == null) || (value != null && value.equals(itemVal))) {
				return true;
			}
		}
		return false;
	}

	public boolean containsValue(final Object value, final Collection<String> itemnames) {
		return containsValue(value, itemnames.toArray(new String[0]));
	}

	public boolean containsValues(final Map<String, Object> filterMap) {
		boolean result = false;
		for (String key : filterMap.keySet()) {
			String[] args = new String[1];
			args[0] = key;
			result = containsValue(filterMap.get(key), args);
			if (!result)
				break;
		}

		return result;
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		// TODO Implement a "viewing" Set and Map.Entry for this or throw an UnsupportedOperationException
		return null;
	}

	@Override
	public Object get(final Object key) {
		if (key == null) {
			return null;
		}
		// Check for "special" cases
		if ("parentDocument".equals(key)) {
			return this.getParentDocument();
		}

		if (this.containsKey(key)) {
			Vector<Object> value = this.getItemValue(key.toString());
			if (value == null) {
				//TODO Throw an exception if the item data can't be read? Null implies the key doesn't exist
				return null;
			} else if (value.size() == 1) {
				return value.get(0);
			}
			return value;
		} else if (key instanceof CharSequence) {
			// Execute the key as a formula in the context of the document
			Formula formula = new Formula();
			formula.setExpression(key.toString());
			List<?> value = formula.getValue(this);
			if (value.size() == 1) {
				return value.get(0);
			}
			return value;
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	private Set<String> fieldNames_;

	@Override
	public Set<String> keySet() {
		if (fieldNames_ == null) {
			fieldNames_ = new LinkedHashSet<String>();
			ItemVector items = (ItemVector) this.getItems();
			String[] names = items.getNames();
			for (int i = 0; i < names.length; i++) {
				fieldNames_.add(names[i]);
			}
		}
		return Collections.unmodifiableSet(fieldNames_);
	}

	@Override
	public Object put(final String key, final Object value) {
		if (key != null) {
			Object previousState = this.get(key);
			this.removeItem(key);
			this.replaceItemValue(key, value);
			this.get(key);
			// this.save();
			return previousState;
		}
		return null;
	}

	@Override
	public void putAll(final Map<? extends String, ? extends Object> m) {
		for (Map.Entry<? extends String, ? extends Object> entry : m.entrySet()) {
			this.removeItem(entry.getKey());
			this.replaceItemValue(entry.getKey(), entry.getValue());
		}
		// this.save();
	}

	@Override
	public Object remove(final Object key) {
		if (key != null) {
			Object previousState = this.get(key);
			this.removeItem(key.toString());
			// this.save();
			return previousState;
		}
		return null;
	}

	@Override
	public int size() {
		return this.getItems().size();
	}

	@Override
	public Collection<Object> values() {
		// TODO Implement a "viewing" collection for this or throw an UnsupportedOperationException
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public Database getAncestorDatabase() {
		return this.getParentDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getParentDatabase().getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Document#getFormName()
	 */
	@Override
	public String getFormName() {
		if (hasItem("form")) {
			return getItemValueString("form");
		} else {
			return "";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Document#getForm()
	 */
	@Override
	public Form getForm() {
		Form result = null;
		if (!getFormName().isEmpty()) {
			result = getParentDatabase().getForm(getFormName());
		}
		return result;
	}

	private IDominoEvent generateEvent(final EnumEvent event, final Object payload) {
		return getAncestorDatabase().generateEvent(event, this, payload);
	}

	@Override
	public String toJson(final boolean compact) {
		StringWriter sw = new StringWriter();
		JsonWriter jw = new JsonWriter(sw, compact);
		try {
			jw.startObject();
			jw.outStringProperty("@unid", getUniversalID());
			Set<String> keys = keySet();
			for (String key : keys) {
				jw.outProperty(key, DominoUtils.toSerializable(getItemValue(key)));
			}
			jw.endObject();
			jw.flush();
		} catch (IOException e) {
			DominoUtils.handleException(e);
			return null;
		} catch (JsonException e) {
			DominoUtils.handleException(e);
			return null;
		}
		return sw.toString();
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Document#getMetaversalID()
	 */
	public String getMetaversalID() {
		String replid = getAncestorDatabase().getReplicaID();
		String unid = getUniversalID();
		return replid + unid;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Document#getMetaversalID(java.lang.String)
	 */
	public String getMetaversalID(final String serverName) {
		return serverName + "!!" + getMetaversalID();
	}
}
