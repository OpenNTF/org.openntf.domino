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
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;
import lotus.domino.UserID;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.DxlExporter;
import org.openntf.domino.EmbeddedObject;
import org.openntf.domino.ExceptionDetails;
import org.openntf.domino.Form;
import org.openntf.domino.Item;
import org.openntf.domino.Item.Flags;
import org.openntf.domino.Item.Type;
import org.openntf.domino.MIMEEntity;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.events.EnumEvent;
import org.openntf.domino.events.IDominoEvent;
import org.openntf.domino.exceptions.DataNotCompatibleException;
import org.openntf.domino.exceptions.DocumentWriteAccessException;
import org.openntf.domino.exceptions.Domino32KLimitException;
import org.openntf.domino.exceptions.ItemNotFoundException;
import org.openntf.domino.exceptions.OpenNTFNotesException;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.ext.Database.Events;
import org.openntf.domino.ext.Name;
import org.openntf.domino.ext.NoteClass;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.helpers.DocumentEntrySet;
import org.openntf.domino.helpers.Formula;
import org.openntf.domino.iterators.ItemVector;
import org.openntf.domino.transactions.DatabaseTransaction;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.Null;
import org.openntf.domino.utils.Documents;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Strings;
import org.openntf.domino.utils.TypeUtils;
import org.openntf.domino.utils.xml.XMLDocument;

import com.ibm.commons.util.io.json.util.JsonWriter;

// TODO: Auto-generated Javadoc
/**
 * The Class Document.
 */
public class Document extends BaseResurrectable<org.openntf.domino.Document, lotus.domino.Document, Database>
implements org.openntf.domino.Document {
	private static final Logger log_ = Logger.getLogger(Document.class.getName());

	/**
	 * Enum to allow easy access to deletion types
	 *
	 * @since org.openntf.domino 2.5
	 */
	protected static enum RemoveType {
		SOFT_FALSE, SOFT_TRUE, HARD_FALSE, HARD_TRUE;
	}

	protected class ExtendedNoteInfos {
		protected NoteClass noteClass;
		protected boolean isDefault;
		protected boolean isPrivate;

		protected ExtendedNoteInfos() {
			long start = System.nanoTime();
			if (Document.this.hasItem("$ACLDigest")) {// the DXL of an ACL is empty
				noteClass = NoteClass.ACL;
				isDefault = true;
			} else {
				DxlExporter exporter = getAncestorSession().createDxlExporter();
				try {
					Vector<String> items = new Vector<String>();
					items.add("-dummy-");
					exporter.setRestrictToItemNames(items);
					exporter.setForceNoteFormat(true);
					exporter.setOutputDOCTYPE(false);
					start = System.nanoTime() - start;
					XMLDocument dxl = new XMLDocument();
					dxl.loadString(exporter.exportDxl(Document.this));
					String cls = dxl.getDocumentElement().getAttribute("class");
					noteClass = NoteClass.valueOf(cls.toUpperCase());
					isDefault = "true".equals(dxl.getDocumentElement().getAttribute("default"));
					isPrivate = "true".equals(dxl.getDocumentElement().getAttribute("private"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (noteClass == null) {
				noteClass = NoteClass.UNKNOWN;
			}
		}
	}

	private RemoveType removeType_;

	private boolean isDirty_ = false;
	private String noteid_;
	private String unid_;
	private long threadid_;
	boolean isNew_;
	private boolean isDeferred_ = false;

	private boolean isQueued_ = false;
	private boolean isRemoveQueued_ = false;
	private boolean shouldWriteItemMeta_ = false;// TODO NTF create rules for making this true

	private boolean shouldResurrect_ = false;
	private Boolean containMimes_ = null;

	protected transient ExtendedNoteInfos extendedNoteInfos_;

	// NTF - these are immutable by definition, so we should just copy it when we read in the doc
	// yes, we're creating objects we might not need, but that's better than risking the toxicity of evil, wicked DateTime
	// these ought to be final, since they can't change, but it makes the constructor really messy

	// NTF - Okay, after testing, maybe these just need to be JIT getters. It added about 10% to Document iteration time.
	// NTF - Done. And yeah, it make quite a performance difference. More like 20%, really
	// RPr - I can confirm this

	/** The created_. */
	private Date created_;

	/** The initially modified_. */
	private Date initiallyModified_;

	/** The last modified_. */
	private Date lastModified_;

	/** The last accessed_. */
	private Date lastAccessed_;

	/**
	 * This variable tracks the current MIME entity. There are some important rules when working with MIME items:
	 *
	 * 1) you have to disable MIME-conversion in session<br>
	 * 2) you must not access the document with the Item interface (otherwise the server may crash)<br>
	 * 3) you should not access more than one MIME entity at once (otherwise the server may crash)<br>
	 * 4) you MUST call closeMIMEEntities() after a MIME operation (otherwise the server may crash)<br>
	 *
	 * this means: if you have opened a MIME item do NOTHING with this document until you call closeMimeEntities()
	 *
	 */
	protected transient Map<String, Set<MIMEEntity>> openMIMEEntities_;

	// to find all functions where checkMimeOpen() should be called, I use this command:
	// cat Document.java | grep "public |getDelegate|checkMimeOpen|^\t}" -P | tr "\n" " " | tr "}" "\n" | grep getDelegate | grep -v "checkMimeOpen"
	//http://www-10.lotus.com/ldd/nd8forum.nsf/5f27803bba85d8e285256bf10054620d/cd146d4165336a5e852576b600114830?OpenDocument
//	private boolean mimeWarned_ = false;

	protected boolean checkMimeOpen(final String key) {

		/* if (getAncestorSession().isFixEnabled(Fixes.MIME_BLOCK_ITEM_INTERFACE) && mimeWarned_ == false) {
			if (openMIMEEntities_ != null && !openMIMEEntities_.isEmpty()) {
				Set<String> MIMEkeys = openMIMEEntities_.keySet();
				if (key != null && MIMEkeys.contains(key.toLowerCase())) {
					return false;
				}
				if (getAncestorSession().isOnServer() && MIMEkeys.size() > 0) {
					System.out.println("******** WARNING ********");
					System.out.println("Document Items were accessed in a document while MIMEEntities are still open.");
					System.out.println("This can cause errors leading to JRE crashes.");
					System.out.println("Document: " + this.noteid_ + " in " + getAncestorDatabase().getApiPath()
							+ " while trying to use key " + String.valueOf(key));
					System.out.println("MIMEEntities: " + Strings.join(MIMEkeys, ", "));
					Throwable t = new Throwable();
					StackTraceElement[] elements = t.getStackTrace();
					for (int i = 0; i < 10; i++) {
						if (elements.length > i) {
							StackTraceElement element = elements[i];
							System.out.println("at " + element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName()
									+ ":" + element.getLineNumber() + ")");
						}
					}
					System.out.println("******** END WARNING ********");
					mimeWarned_ = true;
					return true;
				} else {
					throw new BlockedCrashException("There are open MIME items: " + openMIMEEntities_.keySet());
				}
			}

		}*/
		return false;
	}

	/**
	 * Instantiates a new Document.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected Document(final lotus.domino.Document delegate, final Database parent) {
		super(delegate, parent, NOTES_NOTE);
		initialize(delegate);
		threadid_ = System.identityHashCode(Thread.currentThread());
	}

	protected Document(final Database parent, final Object metaData) {
		super(null, parent, NOTES_NOTE);
		if (metaData instanceof String) {
			setDeferredId((String) metaData);
		} else if (metaData instanceof Integer) {
			setDeferredId((Integer) metaData);
		}
	}

	protected void setDeferredId(final String id) {
		if (DominoUtils.isUnid(id)) {
			unid_ = id;
		} else {
			noteid_ = id;
			unid_ = parent.getUNID(id);
		}
		isNew_ = false;
		isDeferred_ = true;
	}

	protected void setDeferredId(final int noteId) {
		setDeferredId(Integer.toHexString(noteId));
	}

	/**
	 * Initialize.
	 *
	 * @param delegate
	 *            the delegate
	 */
	private void initialize(final lotus.domino.Document delegate) {
		try {
			if (getAncestorSession().isFixEnabled(Fixes.FORCE_HEX_LOWER_CASE)) {
				noteid_ = delegate.getNoteID().toLowerCase();
				unid_ = delegate.getUniversalID().toLowerCase();
			} else {
				noteid_ = delegate.getNoteID();
				unid_ = delegate.getUniversalID();
			}
			isNew_ = noteid_.equals("0") || noteid_.isEmpty();

			if (getAncestorSession().isFixEnabled(Fixes.FORCE_JAVA_DATES)) {
				delegate.setPreferJavaDates(true);
			}

			// RPr: Do not read too much infos in initialize, as it affects performance!
			// created_ = DominoUtils.toJavaDateSafe(delegate.getCreated());
			// initiallyModified_ = DominoUtils.toJavaDateSafe(delegate.getInitiallyModified());
			// lastModified_ = DominoUtils.toJavaDateSafe(delegate.getLastModified());
			// lastAccessed_ = DominoUtils.toJavaDateSafe(delegate.getLastAccessed());
		} catch (NotesException ne) {
			log_.log(Level.WARNING, "Exception occured when initializing a Document wrapper: " + ne.text);
			DominoUtils.handleException(ne, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getCreated()
	 */
	@Override
	//	@Deprecated
	@Legacy(Legacy.DATETIME_WARNING)
	public DateTime getCreated() {
		checkMimeOpen(null);// RPr: needed?
		try {
			return fromLotus(getDelegate().getCreated(), DateTime.SCHEMA, getAncestorSession());// TODO NTF - maybe ditch the parent?
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getCreatedDate()
	 */
	@Override
	public Date getCreatedDate() {
		checkMimeOpen(null);// RPr: needed?
		if (created_ == null) {
			try {
				created_ = DominoUtils.toJavaDateSafe(getDelegate().getCreated());
			} catch (NotesException e) {
				DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);// RPr: needed?
		try {
			return fromLotus(getDelegate().getInitiallyModified(), DateTime.SCHEMA, getAncestorSession());// TODO NTF - maybe ditch the parent?
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getInitiallyModifiedDate()
	 */
	@Override
	public Date getInitiallyModifiedDate() {
		checkMimeOpen(null);// RPr: needed?
		if (initiallyModified_ == null) {
			try {
				initiallyModified_ = DominoUtils.toJavaDateSafe(getDelegate().getInitiallyModified());
			} catch (NotesException e) {
				DominoUtils.handleException(e, this);

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
	//	@Deprecated
	@Legacy(Legacy.DATETIME_WARNING)
	public DateTime getLastAccessed() {
		checkMimeOpen(null);// RPr: needed?
		try {
			lotus.domino.DateTime lotusDate = getDelegate().getLastAccessed();
			if (lotusDate == null) {
				return null;
			}
			return fromLotus(lotusDate, DateTime.SCHEMA, getAncestorSession());// TODO NTF - maybe ditch the parent?
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getLastAccessedDate()
	 */
	@Override
	public Date getLastAccessedDate() {
		checkMimeOpen(null);// RPr: needed?
		if (lastAccessed_ == null) {
			try {
				lastAccessed_ = DominoUtils.toJavaDateSafe(getDelegate().getLastAccessed());
			} catch (NotesException e) {
				DominoUtils.handleException(e, this);
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
	//	@Deprecated
	@Legacy(Legacy.DATETIME_WARNING)
	public DateTime getLastModified() {
		checkMimeOpen(null);// RPr: needed?
		try {
			lotus.domino.DateTime lotusDate = getDelegate().getLastModified();
			if (lotusDate == null) {
				return null;
			}
			return fromLotus(lotusDate, DateTime.SCHEMA, getAncestorSession());// TODO NTF - maybe ditch the parent?

		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getLastModifiedDate()
	 */
	@Override
	public Date getLastModifiedDate() {
		checkMimeOpen(null);// RPr: needed?
		if (lastModified_ == null) {
			try {
				lastModified_ = DominoUtils.toJavaDateSafe(getDelegate().getLastModified());
			} catch (NotesException e) {
				DominoUtils.handleException(e, this);
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
		return appendItemValue(name, (Object) null);
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

	/**
	 * Appends a value to an item (if it is not yet there)
	 */
	@Override
	public Item appendItemValue(final String name, final Object value) {
		return appendItemValue(name, value, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#appendItemValue(java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Item appendItemValue(final String name, final Object value, final boolean unique) {
		checkMimeOpen(name);
		Item result = null;
		if (unique && hasItem(name)) {
			// TODO RPr This function is not yet 100% mime compatible
			// Once mime compatible, remove the reference in org.openntf.domino.ext.Document Javadoc
			result = getFirstItem(name);
			if (result.containsValue(value)) {// this does not work when it is not dominoFriendly
				return result;
			}
		}
		try {
			if (!hasItem(name)) {
				result = replaceItemValue(name, value);
			} else if (value != null) {
				List recycleThis = new ArrayList();
				try {
					Object domNode = toDominoFriendly(value, getAncestorSession(), recycleThis);
					if (getAncestorSession().isFixEnabled(Fixes.APPEND_ITEM_VALUE)) {
						Vector current = getItemValue(name);
						if (current == null) {
							result = replaceItemValue(name, value);
						} else if (domNode instanceof Collection) {
							current.addAll((Collection) domNode);
							result = replaceItemValue(name, current);
						} else {
							current.add(domNode);
							result = replaceItemValue(name, current);
						}
					} else {
						beginEdit();
						result = fromLotus(getDelegate().appendItemValue(name, domNode), Item.SCHEMA, this);
						markDirty(name, true);
					}
				} finally {
					s_recycle(recycleThis);
				}
			} else {
				result = appendItemValue(name);
			}

		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Item=" + name);
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
		attachVCard(document, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#attachVCard(lotus.domino.Base, java.lang.String)
	 */
	@Override
	public void attachVCard(final lotus.domino.Base document, final String charset) {
		checkMimeOpen(null);
		beginEdit();
		try {
			getDelegate().attachVCard(toLotus(document), charset);
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/**
	 * This method is called before any change is made to any content. Allows e.g. the locking of the document and the history
	 * initialization.
	 */
	public void beginEdit() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#closeMIMEEntities()
	 */
	@Override
	public boolean closeMIMEEntities() {
		return closeMIMEEntities(false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#closeMIMEEntities(boolean)
	 */
	@Override
	public boolean closeMIMEEntities(final boolean saveChanges) {
		return closeMIMEEntities(saveChanges, null);
	}

	private boolean containsMimes() {
		if (containMimes_ == null) {
			lotus.domino.Document del = getDelegate();
			try {
				containMimes_ = del.hasItem("$MimeTrack") || del.hasItem("$NoteHasNativeMIME");
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
		}
		return containMimes_.booleanValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#closeMIMEEntities(boolean, java.lang.String)
	 */
	@Override
	public boolean closeMIMEEntities(final boolean saveChanges, final String entityItemName) {
		// checkMimeOpen(); RPr: This is not needed here (just to tweak my grep command)
		if (isDeferred_) {
			return true;
		}
		if (isDead()) {
			return true;
		}
		try {
			// TODO: $Mime-xxx Fields to fieldNames_ List
			if (saveChanges) {
				if (entityItemName == null) {
					markDirty();
				} else {
					markDirty("$NoteHasNativeMIME", true);
					markDirty("MIME_Version", true);
					markDirty("$MIMETrack", true);
					markDirty(entityItemName, true);
				}
			}

			//// This has to be called BEFORE we recycle the Mime entity, otherwise the data may not be stored.
			boolean ret = false;
			if (null != entityItemName) {
				try {
					ret = getDelegate().closeMIMEEntities(saveChanges, entityItemName);
					if (saveChanges && !ret) {
						if (log_.isLoggable(Level.SEVERE)) {
							log_.log(Level.SEVERE, "closeMIMEEntities returned false for item " + entityItemName + " on doc " + getNoteID()
							+ " in db " + getAncestorDatabase().getApiPath(), new Throwable());
						}
					}
				} catch (NotesException e) {
					log_.log(Level.INFO,
							"Attempted to close a MIMEEntity called " + entityItemName + " even though we can't find an item by that name.",
							e);

				}
			} else {
				try {
					ret = getDelegate().closeMIMEEntities(saveChanges, null);
				} catch (NotesException e) {
					log_.log(Level.INFO, "Failed to close all MIMEEntities", e);
				}
			}

			// RPR: I don't exactly remember, why we do that. As far as I know, we should
			// ensure that every MIME item is recycled before closing.
			if (openMIMEEntities_ != null) {
				if (entityItemName == null) {
					Collection<Set<MIMEEntity>> values = openMIMEEntities_.values();
					if (values != null && !values.isEmpty()) {
						for (Set<MIMEEntity> currEntitySet : values) {
							if (currEntitySet != null && !currEntitySet.isEmpty()) {
								for (MIMEEntity currEntity : currEntitySet) {
									((org.openntf.domino.impl.MIMEEntity) currEntity).closeMIMEEntity();
								}
							}
						}
					}
					openMIMEEntities_.clear();
				} else {
					String lcName = entityItemName.toLowerCase();
					if (openMIMEEntities_.containsKey(lcName)) {
						Set<MIMEEntity> currEntitySet = openMIMEEntities_.remove(lcName);
						if (currEntitySet != null) {
							for (MIMEEntity currEntity : currEntitySet) {
								((org.openntf.domino.impl.MIMEEntity) currEntity).closeMIMEEntity();
							}
						}
					} else {
						log_.log(Level.FINE,
								"A request was made to close MIMEEntity " + entityItemName + " but that entity isn't currently open");
					}
				}
			}

			if (saveChanges) {
				// This item is for debugging only, so keep 5-10 items in that list
				// http://www-01.ibm.com/support/docview.wss?uid=swg27002572

				Vector<Object> mt = getItemValue("$MIMETrack");
				if (mt.size() > 10) {
					replaceItemValue("$MIMETrack", mt.subList(mt.size() - 10, mt.size()));
				}
				// Other ideas: 1) Delete it completely, 2) write dummy entry
				// removeItem("$MIMETrack");
				// replaceItemValue("$MIMETrack", "Itemize by OpenNTF-Domino API on " + getAncestorSession().getServerName() + " at "
				//		+ new Date());
			}

			return ret;
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		beginEdit();
		try {
			boolean ret = getDelegate().computeWithForm(doDataTypes, raiseError);
			markDirty();
			return ret;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		convertToMIME(0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#convertToMIME(int)
	 */
	@Override
	public void convertToMIME(final int conversionType) {
		convertToMIME(conversionType, 0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#convertToMIME(int, int)
	 */
	@Override
	public void convertToMIME(final int conversionType, final int options) {
		checkMimeOpen(null);
		beginEdit();
		try {
			getDelegate().convertToMIME(conversionType, options);
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#copyAllItems(lotus.domino.Document, boolean)
	 */
	@Override
	public void copyAllItems(final lotus.domino.Document doc, final boolean replace) {
		checkMimeOpen(null);
		if (doc instanceof Document) {
			((Document) doc).beginEdit();
		}
		try {
			getDelegate().copyAllItems(toLotus(doc), replace);
			if (doc instanceof Document) {
				((Document) doc).markDirty();
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#copyItem(lotus.domino.Item)
	 */
	@Override
	public Item copyItem(final lotus.domino.Item item) {
		return copyItem(item, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#copyItem(lotus.domino.Item, java.lang.String)
	 */
	@Override
	public Item copyItem(final lotus.domino.Item item, final String newName) {
		// TODO - NTF markDirty()? Yes. It's necessary.
		// TODO RPr: ConvertMime?
		checkMimeOpen(newName);
		beginEdit();
		try {
			Item ret = fromLotus(getDelegate().copyItem(toLotus(item), newName), Item.SCHEMA, this);
			markDirty(ret.getName(), true);
			return ret;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#copyToDatabase(lotus.domino.Database)
	 */
	@Override
	public org.openntf.domino.Document copyToDatabase(final lotus.domino.Database db) {
		checkMimeOpen(null);
		// DONE - NTF markDirty() - RPr: no does not make the document dirty?
		try {
			Database odaDest;
			if(db instanceof Database) {
				odaDest = (Database)db;
			} else {
				odaDest = fromLotus(db, Database.SCHEMA, getAncestorSession());
			}
			return fromLotus(getDelegate().copyToDatabase(toLotus(db)), Document.SCHEMA, odaDest);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		return createMIMEEntity("Body");
	}

	//RPr: currently not used. So I commented this out
	//private final transient Map<String, MIMEEntity> entityCache_ = new HashMap<String, MIMEEntity>();

	/**
	 * This is used to track all MIMEEntities in this document. EVERY MIME-Item should be routed over this method!
	 *
	 * @param lotus
	 *            the lotus name
	 * @param itemName
	 *            the itemName
	 * @return the wrapped and tracked {@link MIMEEntity}
	 */
	protected MIMEEntity fromLotusMimeEntity(final lotus.domino.MIMEEntity lotus, final String itemName) {
		if (lotus == null) {
			return null;
		}
		MIMEEntity wrapped = fromLotus(lotus, MIMEEntity.SCHEMA, this);
		if (wrapped != null) {
			((org.openntf.domino.impl.MIMEEntity) wrapped).init(itemName);

			String lcName = itemName.toLowerCase();
			if (openMIMEEntities_ == null) {
				openMIMEEntities_ = new HashMap<String, Set<MIMEEntity>>();
			}
			Set<MIMEEntity> entityGroup = openMIMEEntities_.get(lcName);
			if (entityGroup == null) {
				entityGroup = new HashSet<MIMEEntity>();
				openMIMEEntities_.put(lcName, entityGroup);
			}
			entityGroup.add(wrapped);
		}
		return wrapped;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#createMIMEEntity(java.lang.String)
	 */
	@Override
	public MIMEEntity createMIMEEntity(String itemName) {
		// checkMimeOpen(); RPr: This is not needed here (just to tweak my grep command)
		containMimes_ = Boolean.TRUE;
		beginEdit();
		try {
			if (itemName == null) {
				itemName = "Body";
			}
			try {
				return fromLotusMimeEntity(getDelegate().createMIMEEntity(itemName), itemName);
			} catch (NotesException alreadyThere) {
				Item chk = getFirstItem(itemName);
				if (chk != null) {
					log_.log(Level.WARNING,
							"Already found an item for " + itemName + " that is type: " + (chk == null ? "null" : chk.getTypeEx()));
					removeItem(itemName);
				} else {
					MIMEEntity me = getMIMEEntity(itemName);
					markDirty(itemName, true);
					return me;
				}
				closeMIMEEntities(false, itemName);
				return fromLotusMimeEntity(getDelegate().createMIMEEntity(itemName), itemName);
			}
			// return fromLotus(getDelegate().createMIMEEntity(itemName), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#createReplyMessage(boolean)
	 */
	@Override
	public org.openntf.domino.Document createReplyMessage(final boolean toAll) {
		// DONE - NTF markDirty()? CHa: Does not make the current document dirty
		checkMimeOpen(null);
		beginEdit();
		try {
			return fromLotus(getDelegate().createReplyMessage(toAll), Document.SCHEMA, getParentDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(name);
		beginEdit();
		RichTextItem ret = null;
		try {
			ret = fromLotus(getDelegate().createRichTextItem(name), RichTextItem.SCHEMA, this);
			markDirty(name, true);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#encrypt()
	 */
	@Override
	public void encrypt() {
		checkMimeOpen(null);
		beginEdit();
		try {
			getDelegate().encrypt();
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#generateXML()
	 */
	@Override
	public String generateXML() {
		checkMimeOpen(null);
		try {
			return getDelegate().generateXML();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			getDelegate().generateXML(style, result);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#generateXML(java.io.Writer)
	 */
	@Override
	public void generateXML(final Writer w) throws IOException {
		checkMimeOpen(null);
		try {
			getDelegate().generateXML(w);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getAttachment(java.lang.String)
	 */
	@Override
	public EmbeddedObject getAttachment(final String fileName) {
		checkMimeOpen(null);
		try {
			return fromLotus(getDelegate().getAttachment(fileName), EmbeddedObject.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().getAuthors();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return null;
	}

	@Override
	public boolean isEditable() {
		boolean result = false;
		int access = getAncestorDatabase().getCurrentAccessLevel();
		if (access > 3) {
			//			System.out.println("isEditable is true because current user " + getAncestorSession().getEffectiveUserName()
			//					+ " has access level " + access);
			return true;	//editor, designer or manager
		}
		if (access < 3) {
			//			System.out.println("isEditable is false because current user " + getAncestorSession().getEffectiveUserName()
			//					+ " has access level " + access);
			return false;	//no access (impossible), depositor or reader
		}
		//author
		Name name = getAncestorSession().getEffectiveUserNameObject();
		String serverName = getAncestorDatabase().getServer();
		Collection<String> names = name.getGroups(serverName);
		names.retainAll(getAuthors());
		if (!names.isEmpty()) {
			result = true;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getColumnValues()
	 */
	@Override
	public Vector<Object> getColumnValues() {
		checkMimeOpen(null);
		try {
			Vector<?> values = getDelegate().getColumnValues();
			if (values != null) {
				return wrapColumnValues(values, this.getAncestorSession());
			} else {
				return null;
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return fromLotusAsVector(getDelegate().getEmbeddedObjects(), EmbeddedObject.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return null;
	}

	@Override
	public List<org.openntf.domino.EmbeddedObject> getAttachments() {
		List<org.openntf.domino.EmbeddedObject> result = new ArrayList<org.openntf.domino.EmbeddedObject>();

		lotus.domino.Session rawSession = toLotus(getAncestorSession());
		try {
			Vector<?> attachmentNames = rawSession.evaluate("@AttachmentNames", getDelegate());

			for (Object attachmentName : attachmentNames) {
				result.add(getAttachment((String) attachmentName));
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}

		//		result.addAll(getEmbeddedObjects());
		//		for (Item item : getItems()) {
		//			if (item instanceof RichTextItem) {
		//				List<org.openntf.domino.EmbeddedObject> objects = ((RichTextItem) item).getEmbeddedObjects();
		//				for (EmbeddedObject obj : objects) {
		//					if (obj.getType() == EmbeddedObject.EMBED_ATTACHMENT) {
		//						result.add(obj);
		//					}
		//				}
		//			}
		//		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getEncryptionKeys()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getEncryptionKeys() {
		checkMimeOpen(null);
		try {
			return getDelegate().getEncryptionKeys();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().getFTSearchScore();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		return getFirstItem(name, false);
	}

	@Override
	@Deprecated
	public void recycle() {
		if (openMIMEEntities_ != null && !openMIMEEntities_.isEmpty() && !isDead()) {
			// RPr: call closeMimeEntities only if really neccessary
			closeMIMEEntities(false, null);
		}
		super.recycle();
	}

	@Override
	public Item getFirstItem(final String name, final boolean returnMime) {
		checkMimeOpen(name);
		boolean convertMime = false;
		try {
			if (returnMime) {
				convertMime = getAncestorSession().isConvertMime();
				if (convertMime) {
					getAncestorSession().setConvertMime(false);
				}
			}
			return fromLotus(getDelegate().getFirstItem(name), Item.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		} finally {
			if (convertMime) {
				getAncestorSession().setConvertMime(true);
			}
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
		checkMimeOpen(null);
		try {
			return getDelegate().getFolderReferences();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		// checkMimeOpen(); RPr: needed?
		try {
			return getDelegate().getHttpURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getItemValue(final String name, final Class<T> type) throws ItemNotFoundException, DataNotCompatibleException {
		// TODO NTF - Add type conversion extensibility of some kind, maybe attached to the Database or the Session

		// RPr: this should be equal to the code below.
		synchronized (this) {
			MIMEEntity entity = getMIMEEntity(name);
			if (entity == null) {
				T result = TypeUtils.itemValueToClass(this, name, type);
				return result;
			} else {
				try {
					return (T) Documents.getItemValueMIME(this, name, entity);
				} finally {
					closeMIMEEntities(false, name);
				}
			}
		}
	}

	@Override
	public <T> List<T> getItemValues(final String name, final Class<T> type) throws ItemNotFoundException, DataNotCompatibleException {
		Vector<Object> vals = getItemValue(name);
		if (type.isArray() || Iterable.class.isAssignableFrom(type)) {
			throw new IllegalArgumentException("Type '" + type.getName() + "' is not scalar.");
		}
		List<T> tmp = new ArrayList<T>(vals.size());
		for (int i = 0; i < vals.size(); i++) {
			tmp.add(TypeUtils.objectToClass(vals.get(i), type, getAncestorSession()));
		}
		return tmp;
	}

	/*
	 * Behavior: If the document does not have the item, then we look at the requested class. If it's a primitive or an array of primitives,
	 * we cannot return a null value that can be assigned to that type, so therefore we throw an Exception. If what was request is an
	 * object, we return null.
	 *
	 * If the item does exist, then we get it's value and attempt a conversion. If the data cannot be converted, we throw an Exception
	 */

	/*@SuppressWarnings("unchecked")
	public <T> T getItemValue(final String name, final Class<T> type) throws ItemNotFoundException, DataNotCompatibleException {
		checkMimeOpen();
		// TODO NTF - Add type conversion extensibility of some kind, maybe attached to the Database or the Session
		// if (T.equals(java.util.Collection.class) && getItemValueString("form").equalsIgnoreCase("container")) {
		// System.out.println("Requesting a value of type " + T.getName() + " in name " + name);
		// }

		//try {
		Object itemValue = null;
		MIMEEntity entity = this.getMIMEEntity(name);
		if (entity != null) {
			itemValue = Documents.getItemValueMIME(this, name, entity);
		} else {
			// read it as vector
			Vector<?> vals;
			try {
				vals = getDelegate().getItemValue(name);
				itemValue = wrapColumnValues(vals, this.getAncestorSession());
			} catch (NotesException ne) {
				log_.log(Level.WARNING, "Unable to get value for item " + name + " in Document " + getAncestorDatabase().getFilePath()
						+ " " + noteid_ + ": " + ne.text);
				DominoUtils.handleException(ne,this,"Item="+name);
				return null;
			}
		}
		if (itemValue == null) {
			return null;
		}
		if (T.isAssignableFrom(itemValue.getClass())) {
			return (T) itemValue;
		}
		// if this is a collection, return the first value
		if (itemValue instanceof Collection) {
			Collection<?> c = ((Collection<?>) itemValue);
			if (c.size() == 1) {
				itemValue = c.iterator().next();
				if (T.isAssignableFrom(itemValue.getClass())) {
					return (T) itemValue;
				}
			}
		}
		throw new DataNotCompatibleException("Cannot return " + itemValue.getClass() + ", because " + T + " was requested.");

	}*/

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getItemValue(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<Object> getItemValue(final String name) {
		checkMimeOpen(name);
		Vector<?> vals = null;

		try {
			// Check the item type to see if it's MIME - if so, then see if it's a MIMEBean
			// This is a bit more expensive than I'd like
			MIMEEntity entity = this.getMIMEEntity(name);
			if (entity != null) {
				try {
					Object mimeValue = Documents.getItemValueMIME(this, name, entity);
					if (mimeValue != null) {
						if (mimeValue instanceof Vector) {
							return (Vector<Object>) mimeValue;
						}
						if (mimeValue instanceof Collection) {
							return new Vector<Object>((Collection<Object>) mimeValue);
						}
						if (mimeValue.getClass().isArray()) {
							Vector<Object> ret;
							if (mimeValue.getClass().getName().length() != 2) {
								ret = (Vector<Object>) Arrays.asList((Object[]) mimeValue);
							} else {
								int lh = Array.getLength(mimeValue);
								ret = new Vector<Object>(lh);
								for (int i = 0; i < lh; i++) {
									ret.add(Array.get(mimeValue, i));
								}
							}
							return ret;
						}
						Vector<Object> result = new Vector<Object>(1);
						result.add(mimeValue);
						return result;
					} else {
						log_.log(Level.WARNING, "We found a MIMEEntity for item name " + name + " in document " + this.getMetaversalID()
						+ " that is not a MIMEBean, so you should use either the MIMEEntity API or the RichTextItem API to access it. .getItemValue() will only attempt to return the text results.");
						//						new Throwable().printStackTrace();

						// TODO NTF: What if we have a "real" mime item like a body field (Handle RT/MIME correctly)
						Vector<Object> result = new Vector<Object>(1);
						result.add(entity.getContentAsText());// TODO: not sure if that is correct
						return result;
					}
				} finally {
					closeMIMEEntities(false, name);
				}
			}
			try {
				vals = getDelegate().getItemValue(name);
			} catch (Throwable t) {
				log_.log(Level.WARNING, "Unable to get value for item " + name + " in Document " + getAncestorDatabase().getFilePath() + " "
						+ noteid_ + ": " + t.getMessage());
				t.printStackTrace();
				DominoUtils.handleException(t, this, "Item=" + name);
				return null;
			}
			return wrapColumnValues(vals, this.getAncestorSession());
		} catch (Throwable t) {
			DominoUtils.handleException(t, this, "Item=" + name);
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
		return getItemValueCustomData(itemName, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getItemValueCustomData(java.lang.String, java.lang.String)
	 */
	@Override
	public Object getItemValueCustomData(final String itemName, final String dataTypeName) throws IOException, ClassNotFoundException {
		checkMimeOpen(itemName);
		if (dataTypeName == null || "mime-bean".equals(dataTypeName)) {
			MIMEEntity entity = this.getMIMEEntity(itemName);
			if (entity != null) {
				try {
					return Documents.getItemValueMIME(this, itemName, entity);
				} finally {
					closeMIMEEntities(false, itemName);
				}
			}
		}
		try {
			return getDelegate().getItemValueCustomData(itemName, dataTypeName);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Item=" + itemName);
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
		checkMimeOpen(itemName);
		try {
			byte[] ret = getDelegate().getItemValueCustomDataBytes(itemName, dataTypeName);
			if (ret != null && ret.length != 0) {
				return ret;
			}
			MIMEEntity entity;
			if ((entity = getMIMEEntity(itemName)) == null) {
				return ret;
			}
			Object o = null;
			try {
				o = Documents.getItemValueMIME(this, itemName, entity);
			} finally {
				closeMIMEEntities(false, itemName);
			}
			if (o != null && o.getClass().getName().equals("[B")) {
				ret = (byte[]) o;
			}
			return ret;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Item=" + itemName);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getItemValueDateTimeArray(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<org.openntf.domino.Base<?>> getItemValueDateTimeArray(final String name) {// cf. DateRange.java
		checkMimeOpen(name);
		boolean mayBeMime = true;
		Vector<org.openntf.domino.Base<?>> vGIV = null;// see below
		try {
			Vector<?> v = getDelegate().getItemValueDateTimeArray(name);
			mayBeMime = false;
			if (v == null || v.size() == 0) {
				return (Vector<org.openntf.domino.Base<?>>) v;
			}
			FactorySchema<?, ?, Session> schema = DateTime.SCHEMA;
			if (v.elementAt(0) instanceof lotus.domino.DateRange) {
				schema = DateRange.SCHEMA;
			} else {// Workaround for Vector of DateRange-s
				while (true) {// no real while!
					int sz = v.size(), i;
					for (i = 0; i < sz; i++) {
						if (v.elementAt(i) != null) {
							break;
						}
					}
					if (i < sz) {
						break;
					}
					vGIV = getDelegate().getItemValue(name);
					if (vGIV.size() != sz * 2) {
						break;
					}
					for (i = 0; i < sz * 2; i++) {
						if (!(vGIV.elementAt(i) instanceof lotus.domino.DateTime)) {
							break;
						}
					}
					if (i < sz * 2) {
						break;
					}
					Vector<lotus.domino.DateRange> aux = new Vector<lotus.domino.DateRange>(sz);
					lotus.domino.Session rawsession = toLotus(getAncestorSession());
					for (i = 0; i < sz; i++) {
						lotus.domino.DateTime dts = (lotus.domino.DateTime) vGIV.elementAt(2 * i);
						lotus.domino.DateTime dte = (lotus.domino.DateTime) vGIV.elementAt(2 * i + 1);
						lotus.domino.DateRange dr = rawsession.createDateRange(dts, dte);
						aux.add(dr);
					}
					v = aux;
					schema = DateRange.SCHEMA;
					break;
				}
			}
			return (Vector<org.openntf.domino.Base<?>>) fromLotusAsVector(v, schema, getAncestorSession());
		} catch (NotesException e) {
			while (mayBeMime) {// no real while!
				MIMEEntity entity = this.getMIMEEntity(name);
				if (entity == null) {
					break;
				}
				Object mim = null;
				try {
					mim = Documents.getItemValueMIME(this, name, entity);
				} finally {
					closeMIMEEntities(false, name);
				}
				if (mim == null) {
					break;
				}
				Vector<?> v;
				if (mim instanceof Vector) {
					v = (Vector<Object>) mim;
				} else if (mim instanceof Collection) {
					v = new Vector<Object>((Collection<Object>) mim);
				} else if (mim.getClass().isArray()) {
					v = (Vector<Object>) Arrays.asList((Object[]) mim);
				} else {
					break;
				}
				int sz = v.size(), i;
				for (i = 0; i < sz; i++) {
					Object o = v.elementAt(i);
					if (o == null) {
						break;
					}
					if ((!(o instanceof DateTime)) && (!(o instanceof DateRange))) {
						break;
					}
				}
				if (i < sz) {
					break;
				}
				return (Vector<org.openntf.domino.Base<?>>) v;
			}
			DominoUtils.handleException(e, this, "Item=" + name);
			return null;
		} finally {
			if (vGIV != null) {
				Base.s_recycle(vGIV);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getItemValueDouble(java.lang.String)
	 */
	@Override
	public double getItemValueDouble(final String name) {
		checkMimeOpen(name);
		try {
			return getDelegate().getItemValueDouble(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Item=" + name);
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
		checkMimeOpen(name);
		try {
			return getDelegate().getItemValueInteger(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Item=" + name);
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
		checkMimeOpen(name);
		// TODO RPr: is this mime-safe?
		try {
			String ret = getDelegate().getItemValueString(name);
			if (ret != null && ret.length() != 0) {
				return ret;
			}
			MIMEEntity me = getMIMEEntity(name);
			if (me == null) {
				return "";
			}
			closeMIMEEntities(false, name);
			Vector<?> v = getItemValue(name);
			ret = "";
			if (v.size() > 0 && v.elementAt(0) instanceof String) {
				ret = (String) v.elementAt(0);
			}
			return ret;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Item=" + name);
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
		// TODO At some point we should cache this result in a private List and then always return an immutable Vector
		ItemVector iv = new ItemVector(this);
		return iv;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getKey()
	 */
	@Override
	public String getKey() {
		checkMimeOpen(null);
		try {
			return getDelegate().getKey();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().getLockHolders();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		return getMIMEEntity("Body");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getMIMEEntity(java.lang.String)
	 */
	@Override
	public MIMEEntity getMIMEEntity(final String itemName) {
		// checkMimeOpen(); This is not needed here
		// 14-03-14 RPr: disabling convertMime is required here! (Not always... but in some cases)
		if (!containsMimes()) {
			return null;
		}
		boolean convertMime = getAncestorSession().isConvertMime();
		try {
			if (convertMime) {
				getAncestorSession().setConvertMime(false);
			}
			MIMEEntity ret = fromLotusMimeEntity(getDelegate().getMIMEEntity(itemName), itemName);
			if (openMIMEEntities_ != null && openMIMEEntities_.size() > 1) {
				//	throw new BlockedCrashException("Accessing two different MIME items at once can cause a server crash!");
				log_.warning("Accessing two different MIME items at once can cause a server crash!" + openMIMEEntities_.keySet());
			}
			return ret;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Item=" + itemName);
		} finally {
			if (convertMime) {
				getAncestorSession().setConvertMime(true);
			}
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
		checkMimeOpen(null);
		try {
			return getDelegate().getNameOfProfile();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		if (noteid_ == null) {
			try {
				noteid_ = getDelegate().getNoteID();
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
		}
		return noteid_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getNotesURL()
	 */
	@Override
	public String getNotesURL() {
		// checkMimeOpen(); RPr: I don't think it is neccessary here
		try {
			return getDelegate().getNotesURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getParentDatabase()
	 */
	@Override
	public final Database getParentDatabase() {
		return parent;
	}

	@Override
	public org.openntf.domino.Document getParentDocument() {
		return this.getParentDatabase().getDocumentByUNID(this.getParentDocumentUNID());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getParentDocumentUNID()
	 */
	@Override
	public String getParentDocumentUNID() {
		// checkMimeOpen(); RPr: I don't think it is neccessary here
		try {
			if (getAncestorSession().isFixEnabled(Fixes.FORCE_HEX_LOWER_CASE)) {
				return getDelegate().getParentDocumentUNID().toLowerCase();
			} else {
				return getDelegate().getParentDocumentUNID();
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		// checkMimeOpen(); RPr: I don't think it is neccessary here
		try {
			return fromLotus(getDelegate().getParentView(), View.SCHEMA, getAncestorDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		return getRead(null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getRead(java.lang.String)
	 */
	@Override
	public boolean getRead(final String userName) {
		// checkMimeOpen(); RPr: I don't think it is neccessary here
		try {
			return getDelegate().getRead(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().getReceivedItemText();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		// checkMimeOpen(); RPr: I don't think it is neccessary here
		try {
			return fromLotus(getDelegate().getResponses(), DocumentCollection.SCHEMA, getAncestorDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().getSigner();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().getSize();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		return unid_;
		//		// checkMimeOpen(); RPr: I think we do not need it here
		//		try {
		//			return getDelegate().getUniversalID();
		//		} catch (NotesException e) {
		//			DominoUtils.handleException(e, this);
		//		}
		//		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#getVerifier()
	 */
	@Override
	public String getVerifier() {
		checkMimeOpen(null);
		try {
			return getDelegate().getVerifier();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().hasEmbedded();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return false;
	}

	//	private Boolean hasReaders_;

	@Override
	public boolean hasReaders() {
		//TODO won't that be handy?
		for (Item item : getItems()) {
			if (item != null && item.isReaders()) {
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
		//		if (checkMimeOpen()) {
		//			System.out.println("DEBUG: MimeEntity found open while checking for item name " + name);
		//		}
		if (keySet() != null) {
			return keySet().contains(name);
		}
		lotus.domino.Document delegate = getDelegate();//NTF outside the try/catch so the exception will bubble
		try {
			if (name == null || delegate == null) {
				return false;
			} else {
				return delegate.hasItem(name);
			}
		} catch (UserAccessException uae) {
			throw uae;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return false;
	}

	@Override
	@Deprecated
	public MIMEEntity testMIMEEntity(final String name) {
		return getMIMEEntity(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#isDeleted()
	 */
	@Override
	public boolean isDeleted() {
		checkMimeOpen(null);
		try {
			lotus.domino.Document delegate = getDelegate();
			if (delegate == null) {
				return false;
			}
			return delegate.isDeleted();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().isEncryptOnSend();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().isEncrypted();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		if (!isDead()) {
			try {
				return getDelegate().isNewNote();
			} catch (NotesException e) {
				DominoUtils.handleException(e, this);
				return isNew_;
			}
		}
		return isNew_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#isPreferJavaDates()
	 */
	@Override
	public boolean isPreferJavaDates() {
		checkMimeOpen(null);
		try {
			return getDelegate().isPreferJavaDates();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().isProfile();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().isResponse();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().isSaveMessageOnSend();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().isSentByAgent();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().isSignOnSend();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().isSigned();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		try {
			return getDelegate().isValid();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		return lock((String) null, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#lock(boolean)
	 */
	@Override
	public boolean lock(final boolean provisionalOk) {
		return lock((String) null, provisionalOk);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#lock(java.lang.String)
	 */
	@Override
	public boolean lock(final String name) {
		return lock(name, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#lock(java.lang.String, boolean)
	 */
	@Override
	public boolean lock(final String name, final boolean provisionalOk) {
		checkMimeOpen(null);
		try {
			return getDelegate().lock(name, provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#lock(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names) {
		return lock(names, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#lock(java.util.Vector, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names, final boolean provisionalOk) {
		checkMimeOpen(null);
		try {
			return getDelegate().lock(names, provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		return lockProvisional((String) null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#lockProvisional(java.lang.String)
	 */
	@Override
	public boolean lockProvisional(final String name) {
		checkMimeOpen(name);
		try {
			return getDelegate().lockProvisional(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#lockProvisional(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lockProvisional(final Vector names) {
		checkMimeOpen(null);
		try {
			return getDelegate().lockProvisional(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		checkMimeOpen(null);
		beginEdit();
		try {
			getDelegate().makeResponse(toLotus(doc));
			markDirty("$ref", true);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	@Override
	public void makeResponse(final lotus.domino.Document doc, final String itemName) {
		if (itemName == null || itemName.equalsIgnoreCase("$ref")) {
			makeResponse(doc);
		} else {
			checkMimeOpen(itemName);
			beginEdit();
			try {
				// making a response field with different name is a litte bit difficult.
				// NOTE: we could use ODA objects instead of lotus objects to avoid try/finally nightmare
				// but I did not want to add that overhead here.
				lotus.domino.Document lotusTmpDoc = getDelegate().getParentDatabase().createDocument();
				try {
					lotusTmpDoc.makeResponse(toLotus(doc));// first: make the temp doc to a response document
					lotus.domino.Item lotusRefItem = lotusTmpDoc.getFirstItem("$REF");
					try {
						lotusRefItem.copyItemToDocument(getDelegate(), itemName);// next: copy the $REF item back to the delegate
					} finally {
						lotusRefItem.recycle();// finally: recycle the whole things
					}
				} finally {
					lotusTmpDoc.recycle();
				}
				markDirty(itemName, true);
			} catch (NotesException e) {
				DominoUtils.handleException(e, this);
			}

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#markRead()
	 */
	@Override
	public void markRead() {
		markRead(null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#markRead(java.lang.String)
	 */
	@Override
	public void markRead(final String userName) {
		checkMimeOpen(userName);
		// TODO - NTF transaction context?
		try {
			getDelegate().markRead(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#markUnread()
	 */
	@Override
	public void markUnread() {
		markUnread(null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#markUnread(java.lang.String)
	 */
	@Override
	public void markUnread(final String userName) {
		checkMimeOpen(userName);
		// TODO - NTF transaction context?
		try {
			getDelegate().markUnread(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#putInFolder(java.lang.String)
	 */
	@Override
	public void putInFolder(final String name) {
		putInFolder(name, true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#putInFolder(java.lang.String, boolean)
	 */
	@Override
	public void putInFolder(final String name, final boolean createOnFail) {
		// TODO - NTF handle transaction context
		checkMimeOpen(name);
		if (getAncestorDatabase().getFolderReferencesEnabled()) {
			beginEdit();
		}
		try {
			// This method will modify the fields $FolderInfo and $FolderRefInfo if FolderReferences are enabled in the database.
			getDelegate().putInFolder(name, createOnFail);
			if (getAncestorDatabase().getFolderReferencesEnabled()) {
				markDirty("$FolderInfo", true);
				markDirty("$FolderRefInfo", true);
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
			//			System.out.println("Listener for BEFORE_DELETE_DOCUMENT allowed the remove call");
			removeType_ = force ? RemoveType.SOFT_TRUE : RemoveType.SOFT_FALSE;
			//			System.out.println("Remove type is " + removeType_.name());
			if (queueRemove()) {
				//				System.out.println("We queued the remove as part of a transaction so tell the calling code that its done");
				result = true;
			} else {
				//				System.out.println("We're not currently in a transaction, so we should force the delegate removal immediately");
				result = forceDelegateRemove();
			}
		} else {
			//			System.out.println("Listener for BEFORE_DELETE_DOCUMENT blocked the remove call");
			result = false;
		}
		if (result) {
			//			System.out.println("Remove executed, so firing AFTER_DELETE_DOCUMENT listener");
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
		checkMimeOpen(name);
		// TODO - NTF handle transaction context
		try {
			// This method will modify the fields $FolderInfo and $FolderRefInfo if FolderReferences are enabled in the database.
			getDelegate().removeFromFolder(name);
			if (getAncestorDatabase().getFolderReferencesEnabled()) {
				markDirty("$FolderInfo", true);
				markDirty("$FolderRefInfo", true);
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#removeItem(java.lang.String)
	 */
	@Override
	public void removeItem(final String name) {
		if (name == null) {
			return;//TODO NTF There's nothing to do here. Maybe we should throw an exception?
		}
		checkMimeOpen(name);
		beginEdit();
		try {
			// RPr: it is important to check if this is a MIME entity and remove that this way.
			// Otherwise dangling $FILE items are hanging around in the document
			MIMEEntity mimeChk = getMIMEEntity(name);
			if (mimeChk != null) {
				try {
					mimeChk.remove();
				} finally {
					closeMIMEEntities(true, name);
				}
			}
			if (getAncestorSession().isFixEnabled(Fixes.REMOVE_ITEM)) {
				while (getDelegate().hasItem(name)) {
					getDelegate().removeItem(name);
				}
			} else {
				if (getDelegate().hasItem(name)) {
					getDelegate().removeItem(name);
				}
			}
			markDirty(name, false);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Item=" + name);
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
		checkMimeOpen(null);
		beginEdit();
		try {
			getDelegate().renderToRTItem(toLotus(rtitem));
			if (rtitem instanceof org.openntf.domino.RichTextItem) {
				((org.openntf.domino.RichTextItem) rtitem).markDirty();
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#replaceItemValueCustomData(java.lang.String, java.lang.Object)
	 */
	@Override
	public Item replaceItemValueCustomData(final String itemName, final Object userObj) throws IOException {
		return replaceItemValueCustomData(itemName, null, userObj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#replaceItemValueCustomData(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public Item replaceItemValueCustomData(final String itemName, final String dataTypeName, final Object userObj) throws IOException {
		if (dataTypeName == null && getAutoMime() != AutoMime.WRAP_NONE) {
			// Only wrap as MIME bean if they are not completely disabled
			return replaceItemValueCustomData(itemName, "mime-bean", userObj, true);
		} else {
			return replaceItemValueCustomData(itemName, dataTypeName, userObj, true);
		}
	}

	/**
	 * serialize the Object value and stores it in the item. if <code>dataTypeName</code>="mime-bean" the item will be a MIME-bean,
	 * otherwise, data is serialized by lotus.domino.Docmuemt.replaceItemValueCustomData
	 */
	public Item replaceItemValueCustomData(final String itemName, final String dataTypeName, final Object value, final boolean returnItem) {
		checkMimeOpen(itemName);
		lotus.domino.Item result = null;
		try {
			if (!"mime-bean".equalsIgnoreCase(dataTypeName)) {
				// if data-type is != "mime-bean" the object is written in native mode.
				beginEdit();
				result = getDelegate().replaceItemValueCustomData(itemName, dataTypeName, value);
				markDirty(itemName, true);
			} else if (value instanceof Serializable) {

				Documents.saveState((Serializable) value, this, itemName);

				// TODO RPr: Discuss if the other strategies make sense here.
				// In my opinion NoteCollection does work UNTIL the next compact task runs.
				// So it makes NO sense to serialize NoteIDs!
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
				Documents.saveState(unids, this, itemName, true, headers);

			} else if (value instanceof NoteCollection) {
				// Maybe it'd be faster to use .getNoteIDs - I'm not sure how the performance compares
				// NTF .getNoteIDs() *IS* faster. By about an order of magnitude.
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
				Documents.saveState(unids, this, itemName, true, headers);

			} else {
				// Check to see if it's a StateHolder
				// TODO RPr: Is this really needed or only a theoretical approach? See above...
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
						Documents.saveState(state, this, itemName, true, headers);

					} else {
						throw new IllegalArgumentException(
								value.getClass() + " is not of type Serializable, DocumentCollection, NoteCollection or StateHolder");
					}
				} catch (ClassNotFoundException cnfe) {
					throw new IllegalArgumentException(
							value.getClass() + " is not of type Serializable, DocumentCollection or NoteCollection");
				}
			}

			if (returnItem) {
				if (result == null) {
					return getFirstItem(itemName, true);// MSt: This is safe now. (Was tested.)
				}
				//NTF if we do a .getFirstItem here and return an item that we MIMEBeaned, it will invalidate the MIME and
				//convert back to a RichTextItem before the document is saved.
				//returnItem *MUST* be treated as false if we've written a MIME attachment.
				//If we didn't write a MIME attachment, then result is already assigned, and therefore we don't need to get it again.
				return fromLotus(result, Item.SCHEMA, this);
			} else {
				return null;
			}
		} catch (Exception e) {
			DominoUtils.handleException(e, this, "Item=" + itemName);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#replaceItemValueCustomDataBytes(java.lang.String, java.lang.String, byte[])
	 */
	@Override
	public Item replaceItemValueCustomDataBytes(final String itemName, String dataTypeName, final byte[] byteArray) throws IOException {
		checkMimeOpen(itemName);
		if (dataTypeName == null) {
			dataTypeName = "";// Passing null as par 2 to Lotus method crashes the Domino server
		}

		// Again, the Notes API documentation is not very exact: It is stated there that "custom data cannot exceed 64k".
		// That's correct. But it doesn't mean that 64k custom data are really accepted. More precisely:
		int maxCDSBytes = 64000 - 1 - dataTypeName.length();// custom data are stored as <lh(dataType)><dataType><byteArray>
		try {
			if (byteArray.length > maxCDSBytes && getAutoMime() != AutoMime.WRAP_NONE) {
				// Then fall back to the normal method, which will MIMEBean it
				return this.replaceItemValueCustomData(itemName, "mime-bean", itemName, true);// TODO: What about dataTypeName?
			} else {
				beginEdit();
				Item result = fromLotus(getDelegate().replaceItemValueCustomDataBytes(itemName, dataTypeName, byteArray), Item.SCHEMA,
						this);
				markDirty(itemName, true);
				return result;
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e, this, "Item=" + itemName);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#replaceItemValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public Item replaceItemValue(final String itemName, final Object value) {
		return replaceItemValue(itemName, value, null, true, true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ext.Document#replaceItemValue(java.lang.String, java.lang.Object, java.lang.Boolean)
	 */
	@Override
	public Item replaceItemValue(final String itemName, final Object value, final boolean isSummary) {
		return replaceItemValue(itemName, value, isSummary, true, true);
	}

	/**
	 * replaceItemValue writes itemFriendly values or a Collection of itemFriendly values.
	 *
	 * if "autoSerialisation" is enabled. Data exceeding 32k is serialized with replaceItemValueCustomData. If MIME_BEAN_SUFFIX is set, the
	 * original item contains the String $ObjectData (=MIME_BEAN_SUFFIX). This is important, if you display data in a view, so that you see
	 * immediately, that only serialized content is available
	 *
	 * @see org.openntf.domino.Document#replaceItemValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public Item replaceItemValue(final String itemName, final Object value, final Boolean isSummary, final boolean boxCompatibleOnly,
			final boolean returnItem) {
		Item result = null;
		try {

			try {
				// Special case. If the argument is an Item, just copy it.
				if (value instanceof Item) {
					List<lotus.domino.Base> recycleThis = null;
					lotus.domino.Item tmpResult;
					recycleThis = new ArrayList<lotus.domino.Base>();
					// remove the mime item first, so that it will not collide with MIME etc.
					MIMEEntity mimeChk = getMIMEEntity(itemName);
					if (mimeChk != null) {
						try {
							mimeChk.remove();
						} finally {
							closeMIMEEntities(true, itemName);
						}
					}
					beginEdit();
					tmpResult = getDelegate().replaceItemValue(itemName,
							TypeUtils.toDominoFriendly(value, getAncestorSession(), recycleThis));
					markDirty(itemName, true);

					s_recycle(tmpResult);

					if (returnItem) {
						return getFactory().create(Item.SCHEMA, this, itemName);
					} else {
						return null;
					}
				}

				result = replaceItemValueLotus(itemName, value, isSummary, returnItem);
			} catch (Exception ex2) {
				if (this.getAutoMime() == AutoMime.WRAP_NONE) {
					// AutoMime completely disabled.
					throw ex2;
				}
				if (!boxCompatibleOnly || ex2 instanceof Domino32KLimitException) {
					// if the value exceeds 32k or we are called from put() (means boxCompatibleOnly=false) we try to write the object as MIME
					result = replaceItemValueCustomData(itemName, "mime-bean", value, returnItem);
				} else if (this.getAutoMime() == AutoMime.WRAP_ALL) {
					// Compatibility mode
					result = replaceItemValueCustomData(itemName, "mime-bean", value, returnItem);
					log_.log(Level.INFO,
							"Writing " + value == null ? "null"
									: value.getClass() + " causes a " + ex2
									+ " as AutoMime.WRAP_ALL is enabled, the value will be wrapped in a MIME bean."
									+ " Consider using 'put' or something similar in your code.");
				} else {
					throw ex2;
				}
			}

			// TODO RPr: What is this?
			if (this.shouldWriteItemMeta_) {
				// If we've gotten this far, it must be legal - update or create the item info map
				Class<?> valueClass;
				if (value == null) {
					valueClass = Null.class;
				} else {
					valueClass = value.getClass();
				}
				Map<String, Map<String, Serializable>> itemInfo = getItemInfo();
				Map<String, Serializable> infoNode = null;
				if (itemInfo.containsKey(itemName)) {
					infoNode = itemInfo.get(itemName);
				} else {
					infoNode = new HashMap<String, Serializable>();
				}
				infoNode.put("valueClass", valueClass.getName());
				infoNode.put("updated", new Date());// For sanity checking if the value was changed outside of Java
				itemInfo.put(itemName, infoNode);
			}

		} catch (Throwable t) {
			DominoUtils.handleException(t, this, "Item=" + itemName);
		}
		return result;
	}

	/**
	 * returns the real LMBCS payload for a Vector of Strings
	 *
	 * @param strVect
	 *            The vector of Strings.
	 * @return LMBCS payload
	 */

	//	private static Charset lLMBCSCharset = null;
	//
	//	private static synchronized CharsetEncoder lGetEncoder() {
	//		if (lLMBCSCharset == null)
	//			lLMBCSCharset = new CharsetProviderICU().charsetForName("LMBCS");
	//		return lLMBCSCharset.newEncoder();
	//	}

	/**
	 * replaceItemValueLotus writes itemFriendly values or a Collection of itemFriendly values. it throws a Domino32KLimitException if the
	 * data does not fit into the fied. The caller can decide what to do, if this exception is thrown.
	 *
	 * It throws a DataNotCompatibleException, if the data is not domino compatible
	 *
	 * @throws Domino32KLimitException
	 *             if the item does not fit in a field
	 */
	public Item replaceItemValueLotus(final String itemName, Object value, final Boolean isSummary, final boolean returnItem)
			throws Domino32KLimitException {
		checkMimeOpen(itemName);
		// writing a value of "Null" leads to a remove of the item if configured in SESSION
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

		org.openntf.domino.Item result = TypeUtils.writeToItem(this, itemName, value, isSummary);
		if (returnItem) {
			return result;
		} else {
			return null;
		}
	}

	private AutoMime autoMime_ = null;

	@Override
	public AutoMime getAutoMime() {
		if (autoMime_ == null) {
			autoMime_ = getAncestorDatabase().getAutoMime();
		}
		return autoMime_;
	}

	@Override
	public void setAutoMime(final AutoMime value) {
		autoMime_ = value;
	}

	private void writeItemInfo() {
		if (this.shouldWriteItemMeta_) {
			Map<String, Map<String, Serializable>> itemInfo = getItemInfo();
			if (itemInfo != null && itemInfo.size() > 0) {
				boolean convertMime = this.getAncestorSession().isConvertMime();
				this.getAncestorSession().setConvertMime(false);
				try {
					Documents.saveState((Serializable) getItemInfo(), this, "$$ItemInfo", false, null);
				} catch (Throwable e) {
					DominoUtils.handleException(e, this);
				}
				this.getAncestorSession().setConvertMime(convertMime);
			}
		}
	}

	private Map<String, Map<String, Serializable>> itemInfo_;

	@SuppressWarnings("unchecked")
	protected Map<String, Map<String, Serializable>> getItemInfo() {
		// TODO NTF make this optional
		if (itemInfo_ == null) {
			if (this.hasItem("$$ItemInfo")) {
				if (this.getFirstItem("$$ItemInfo", true).getTypeEx() == Item.Type.MIME_PART) {
					// Then use the existing value
					try {
						itemInfo_ = (Map<String, Map<String, Serializable>>) Documents.restoreState(this, "$$ItemInfo");
					} catch (Throwable t) {
						DominoUtils.handleException(t, this);
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

	private static List<String> throwableToList(final Throwable t) {
		List<String> result = new ArrayList<>();
		String summary = t.getClass().getName() + ": " + t.getMessage();
		result.add(summary);
		for (StackTraceElement element : t.getStackTrace()) {
			result.add(element.getClassName() + "." + element.getMethodName() + " (" + element.getLineNumber() + ")");
		}
		if (t.getCause() != null) {
			result.add("Caused by...");
			result.addAll(throwableToList(t.getCause()));
		}
		return result;
	}

	private void writeStackTraceToItem(final String itemname, final Throwable t) {
		List<String> trace = throwableToList(t);
		RichTextItem rti = createRichTextItem(itemname);
		for (String line : trace) {
			if (line.startsWith("Caused by")) {
				rti.addPageBreak();
				rti.addNewLine();
			} else {
				rti.appendText(line);
				rti.addNewLine();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#save(boolean, boolean, boolean)
	 */
	@Override
	public boolean save(final boolean force, final boolean makeResponse, final boolean markRead) {

		//			System.out.println("Starting save operation on " + this.getMetaversalID() + ". isNew: " + String.valueOf(isNewNote()));
		//			Throwable t = new Throwable();
		//			t.printStackTrace();

		//		checkMimeOpen();
		//		System.out.println("TEMP DEBUG Starting save operation on " + unid_ + " in " + this.getAncestorDatabase().getApiPath() + " as user "
		//				+ this.getAncestorSession().getCommonUserName());
		boolean result = false;
		if (removeType_ != null) {
			log_.log(Level.INFO, "Save called on a document marked for a transactional delete. So there's no point...");
			return true;
		}
		//START SPECIAL DEBUG SECTION FOR NTF IN GRAPH2 ENGINE
		if (getAncestorDatabase().getFileName().equalsIgnoreCase("factproxy.nsf")) {
			if (getItemValueString("form").equalsIgnoreCase("vertexframe")) {
				Throwable t = new Throwable();
				writeStackTraceToItem("stackTrace", t);
			} else if (Strings.isBlankString(getItemValueString("$$key")) || getItemValueString("$$key").length() < 48) {
				Throwable t = new Throwable();
				writeStackTraceToItem("stackTrace", t);
			}
		}

		//END SPECIAL DEBUG SECTION FOR NTF IN GRAPH2 ENGINE

		if (isNewNote() || isDirty()) {
			boolean go = true;
			Database db = getAncestorDatabase();
			go = !db.hasListeners() ? true : db.fireListener(generateEvent(Events.BEFORE_UPDATE_DOCUMENT, null));
			if (go) {
				writeItemInfo();
				//				fieldNames_ = null;
				isNew_ = false;
				try {
					lotus.domino.Document del = getDelegate();
					if (del != null) {
						result = del.save(force, makeResponse, markRead);
						// complex cases are not needed unless you want to log
						//						if (noteid_ == null || !noteid_.equals(del.getNoteID())) {
						//							// System.out.println("Resetting note id from " + noteid_ + " to " + del.getNoteID());
						//							noteid_ = del.getNoteID();
						//						}
						//						if (unid_ == null || !unid_.equals(del.getUniversalID())) {
						//							// System.out.println("Resetting unid from " + unid_ + " to " + del.getUniversalID());
						//							unid_ = del.getUniversalID();
						//						}
						// so we can do that in every case
						noteid_ = del.getNoteID();
						unid_ = del.getUniversalID();
						isNew_ = noteid_.equals("0") || noteid_.isEmpty();// don't set to true, save may fail!
						invalidateCaches();
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
						+ (this.hasItem("$$Key") ? " (key: '" + getItemValueString("$$Key") + "')" : "")
						+ " to a different unid instead: " + newunid;
						setUniversalID(newunid);
						try {
							getDelegate().save(force, makeResponse, markRead);
							noteid_ = getDelegate().getNoteID();
							isNew_ = noteid_.equals("0") || noteid_.isEmpty();
							System.out.println(message);
							log_.log(Level.WARNING, message/* , t */);
						} catch (NotesException ne) {
							log_.log(Level.SEVERE, "Okay, now it's time to really panic. Sorry...");
							DominoUtils.handleException(e, this);
						}
					} else if (e.text.contains("You are not authorized")) {
						throw new DocumentWriteAccessException(this);
					} else {
						DominoUtils.handleException(e, this);
					}
				}
				if (result) {
					clearDirty();
					getAncestorDatabase().fireListener(generateEvent(Events.AFTER_UPDATE_DOCUMENT, null));
				}
			} else {
				// System.out.println("Before Update listener blocked save.");
				if (log_.isLoggable(Level.FINE)) {
					log_.log(Level.FINE,
							"Document " + getNoteID() + " was not saved because the DatabaseListener for update returned false.");
				}
				result = false;
			}
		} else {
			// System.out.println("No changes occured therefore not saving.");
			if (log_.isLoggable(Level.FINE)) {
				log_.log(Level.FINE, "Document " + getNoteID() + " was not saved because nothing on it was changed.");
			}
			result = true;// because nothing changed, we don't want to activate any potential failure behavior in the caller
		}
		// System.out.println("Save completed returning " + String.valueOf(result));
		//		System.out.println("TEMP DEBUG Completed save operation on " + unid_ + " in " + this.getAncestorDatabase().getApiPath()
		//				+ " as user " + this.getAncestorSession().getCommonUserName());

		return result;
	}

	protected void invalidateCaches() {
		// RPr: Invalidate cached values
		lastModified_ = null;
		lastAccessed_ = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#send()
	 */
	@Override
	public void send() {
		send(false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#send(boolean)
	 */
	@Override
	public void send(final boolean attachForm) {
		checkMimeOpen(null);
		beginEdit();
		try {
			getDelegate().send(false);
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#send(boolean, java.lang.String)
	 */
	@Override
	public void send(final boolean attachForm, final String recipient) {
		Vector<String> v = new Vector<String>(1);
		v.add(recipient);
		send(attachForm, v);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#send(boolean, java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void send(final boolean attachForm, final Vector recipients) {
		// TODO - NTF handle transaction context
		checkMimeOpen(null);
		beginEdit();
		try {
			getDelegate().send(attachForm, recipients);
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#send(java.lang.String)
	 */
	@Override
	public void send(final String recipient) {
		send(false, recipient);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#send(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void send(final Vector recipients) {
		checkMimeOpen(null);
		beginEdit();
		// TODO - NTF handle transaction context
		try {
			getDelegate().send(recipients);
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#setEncryptOnSend(boolean)
	 */
	@Override
	public void setEncryptOnSend(final boolean flag) {
		checkMimeOpen(null);
		beginEdit();
		try {
			getDelegate().setEncryptOnSend(flag);
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#setEncryptionKeys(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setEncryptionKeys(final Vector keys) {
		checkMimeOpen(null);
		beginEdit();
		try {
			getDelegate().setEncryptionKeys(keys);
			markDirty("SecretEncryptionKeys", true);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#setPreferJavaDates(boolean)
	 */
	@Override
	public void setPreferJavaDates(final boolean flag) {
		checkMimeOpen(null);
		try {
			getDelegate().setPreferJavaDates(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#setSaveMessageOnSend(boolean)
	 */
	@Override
	public void setSaveMessageOnSend(final boolean flag) {
		checkMimeOpen(null);
		// TODO NTF - mark dirty?
		try {
			getDelegate().setSaveMessageOnSend(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#setSignOnSend(boolean)
	 */
	@Override
	public void setSignOnSend(final boolean flag) {
		checkMimeOpen(null);
		// TODO NTF - mark dirty?
		try {
			getDelegate().setSignOnSend(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#setUniversalID(java.lang.String)
	 */
	@Override
	public void setUniversalID(final String unid) {
		if (getUniversalID().equalsIgnoreCase(unid)) {
			//it's already that unid. Don't waste time...
			return;
		}
		checkMimeOpen(unid);
		beginEdit();
		try {
			try {
				lotus.domino.Document del = getDelegate().getParentDatabase().getDocumentByUNID(unid);
				if (del != null) {// this is surprising. Why didn't we already get it?
					log_.log(Level.WARNING,
							"Document " + unid + " already existed in the database with noteid " + del.getNoteID()
							+ " and we're trying to set a doc with noteid " + getNoteID() + " to that. The existing document is a "
							+ del.getItemValueString("form") + " and the new document is a " + getItemValueString("form"));
					if (isDirty()) {// we've already made other changes that we should tuck away...
						log_.log(Level.WARNING,
								"Attempting to stash changes to this document to apply to other document of the same UNID. This is pretty dangerous...");
						org.openntf.domino.Document stashDoc = copyToDatabase(getParentDatabase());
						setDelegate(del, true);
						try {
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
						} catch (Throwable t) {
							DominoUtils.handleException(t);
						}
					} else {
						log_.log(Level.WARNING, "Resetting delegate to existing document for id " + unid);
						setDelegate(del, true);
					}
				} else {
					getDelegate().setUniversalID(unid);
				}
				markDirty();
			} catch (NotesException ne) {
				// this is what's expected
				getDelegate().setUniversalID(unid);
				markDirty();
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		unid_ = unid;
	}

	@Override
	public void setUniversalID(final CharSequence unid, final boolean includeCreated) {
		setUniversalID(unid.toString());
		replaceItemValue("$Created", new Date());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#sign()
	 */
	@Override
	public void sign() {
		checkMimeOpen(null);
		beginEdit();
		// TODO RPr: is it enough if we add $Signatue?
		try {
			getDelegate().sign();
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Document#unlock()
	 */
	@Override
	public void unlock() {
		checkMimeOpen(null);
		try {
			getDelegate().unlock();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	@Override
	public void markDirty() {
		// When calling this, we have modified a field, but we do not know which one!
		//		fieldNames_ = null;
		markDirtyInt();
	}

	protected void markDirtyInt() {
		isDirty_ = true;
		if (!isQueued_) {
			DatabaseTransaction txn = getParentDatabase().getTransaction();
			if (txn != null) {
				txn.queueUpdate(this);
				isQueued_ = true;
			}
		}
	}

	/**
	 * This method marks a certain field dirty. Use this with care. So this method should not be part of an interface!
	 *
	 * @param fieldName
	 *            the fieldName of the item you currently modify
	 * @param itemWritten
	 *            true if you have written the item, false if not
	 */
	public void markDirty(final String fieldName, final boolean itemWritten) {
		markDirtyInt();
		if (itemWritten) {
			keySetInt().add(fieldName);
		} else {
			keySetInt().remove(fieldName);
		}

	}

	private boolean queueRemove() {
		if (!isRemoveQueued_) {
			DatabaseTransaction txn = getParentDatabase().getTransaction();
			if (txn != null) {
				//				System.out.println("DEBUG: Found a transaction: " + txn + " from parent Database " + getParentDatabase().getApiPath());
				txn.queueRemove(this);
				isRemoveQueued_ = true;
				return true;// we queued this, so whoever asked shouldn't do it yet.
			} else {
				return false;// calling function should just go ahead and execute
			}
		} else {// we already queued this for removal.
			return false;
		}
	}

	protected void clearDirty() {
		isDirty_ = false;
		isQueued_ = false;
	}

	@Override
	public void rollback() {
		checkMimeOpen(null);
		if (removeType_ != null) {
			removeType_ = null;
		}
		if (isDirty()) {
			//			String nid = getNoteID();
			try {
				//				lotus.domino.Database delDb = getDelegate().getParentDatabase();
				getDelegate().recycle();
				shouldResurrect_ = true;
				invalidateCaches();
				// lotus.domino.Document junkDoc = delDb.createDocument(); // NTF - Why? To make sure I get a new cppid. Otherwise the
				// handle
				// gets reused
				// lotus.domino.Document resetDoc = delDb.getDocumentByID(nid);
				// setDelegate(resetDoc);
				// junkDoc.recycle();
			} catch (NotesException e) {
				DominoUtils.handleException(e, this);
			}
			clearDirty();
		}
	}

	@Override
	public boolean isDirty() {
		return isDirty_;
	}

	@Override
	public boolean forceDelegateRemove() {
		checkMimeOpen(null);
		boolean result = false;
		RemoveType type = removeType_;
		//		System.out.println("DEBUG: Forcing delegate removal of type " + (type == null ? "null!" : type.name()));
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
				if (delegate != null) {
					result = delegate.removePermanently(true);
				} else {
					result = true;
				}
				if (result) {
					s_recycle(delegate);
					this.setDelegate(null, false);
				}
				unid_ = null;
				noteid_ = null;
				break;
			case HARD_FALSE:
				result = getDelegate().removePermanently(false);
				break;
			default:
				System.out.println("ALERT: Unknown remove type on deletion. This should not be possible.");
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
		//		System.out.println("DEBUG: Delegate remove call returned " + String.valueOf(result));
		return result;
	}

	@Override
	protected void resurrect() {
		if (openMIMEEntities_ != null) {
			openMIMEEntities_.clear();
		}
		if (noteid_ != null) {
			try {
				lotus.domino.Document d = null;
				lotus.domino.Database db = toLotus(getParentDatabase());
				if (db != null) {
					if (noteid_.length() == 0 || Integer.valueOf(noteid_, 16) == 0) {
						if (isNewNote()) {//NTF this is redundant... not sure what the best move here is...
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
								log_.log(Level.WARNING,
										"Attempted to resurrect non-new document unid " + String.valueOf(unid_)
										+ ", but the document was not found in " + getParentDatabase().getServer() + "!!"
										+ getParentDatabase().getFilePath() + " because of: " + ne.text,
										ne);
							}
						}
					} else {
						d = db.getDocumentByID(noteid_);
					}
				}
				setDelegate(d, true);
				//getFactory().recacheLotusObject(d, this, parent_);
				if (shouldResurrect_) {
					if (log_.isLoggable(Level.FINER)) {
						log_.log(Level.FINER,
								"Document " + noteid_ + " in database path " + getParentDatabase().getFilePath() + " was rollbacked.");
					}
				} else {
					if (log_.isLoggable(Level.FINE)) {
						Throwable t = new Throwable();
						log_.log(Level.FINE, "Document " + noteid_ + " in database path " + getParentDatabase().getFilePath()
								+ " had been recycled and was auto-restored. Changes may have been lost.", t);
					}
					//					StackTraceElement[] elements = t.getStackTrace();
					//					log_.log(Level.FINER,
					//							elements[0].getClassName() + "." + elements[0].getMethodName() + " ( line " + elements[0].getLineNumber()
					//							+ ")");
					//					log_.log(Level.FINER,
					//								elements[1].getClassName() + "." + elements[1].getMethodName() + " ( line " + elements[1].getLineNumber()
					//										+ ")");
					//						log_.log(Level.FINER,
					//								elements[2].getClassName() + "." + elements[2].getMethodName() + " ( line " + elements[2].getLineNumber()
					//										+ ")");
					//					}
					//					log_.log(Level.FINE,
					//							"If you recently rollbacked a transaction and this document was included in the rollback, this outcome is normal.");
				}
			} catch (UserAccessException uae) {
				throw uae;
			} catch (NotesException e) {
				DominoUtils.handleException(e, this);
			} finally {
				shouldResurrect_ = false;
			}
		} else if (null != unid_) {
			//NTF we have a unid but no noteid because this was a deferred document using a unid
			try {
				lotus.domino.Document d = null;
				lotus.domino.Database db = toLotus(getParentDatabase());
				if (db != null) {
					try {
						d = db.getDocumentByUNID(unid_);
					} catch (NotesException ne) {
						if (isDeferred_) {
							try {
								d = db.createDocument();
								d.setUniversalID(unid_);
								isDeferred_ = false;
							} catch (NotesException ne1) {
								DominoUtils.handleException(ne1);
							}
						} else {
							if (log_.isLoggable(Level.WARNING)) {
								log_.log(Level.WARNING,
										"Attempted to resurrect non-new document unid " + String.valueOf(unid_)
										+ ", but the document was not found in " + getParentDatabase().getServer() + "!!"
										+ getParentDatabase().getFilePath() + " because of: " + ne.text);
							}
						}
					}
				} else {
					if (log_.isLoggable(Level.WARNING)) {
						log_.log(Level.WARNING, "Attempted to resurrect non-new document unid " + String.valueOf(unid_)
						+ ", but the parent database object is null!");
					}
				}
				setDelegate(d, true);
				shouldResurrect_ = false;
				if (log_.isLoggable(Level.FINE)) {
					log_.log(Level.FINE, "Document " + noteid_ + " in database path " + getParentDatabase().getFilePath()
							+ " had been recycled and was auto-restored. Changes may have been lost.");
					if (log_.isLoggable(Level.FINER)) {
						Throwable t = new Throwable();
						StackTraceElement[] elements = t.getStackTrace();
						log_.log(Level.FINER, elements[0].getClassName() + "." + elements[0].getMethodName() + " ( line "
								+ elements[0].getLineNumber() + ")");
						log_.log(Level.FINER, elements[1].getClassName() + "." + elements[1].getMethodName() + " ( line "
								+ elements[1].getLineNumber() + ")");
						log_.log(Level.FINER, elements[2].getClassName() + "." + elements[2].getMethodName() + " ( line "
								+ elements[2].getLineNumber() + ")");
					}
					log_.log(Level.FINE,
							"If you recently rollbacked a transaction and this document was included in the rollback, this outcome is normal.");
				}
			} catch (Exception e) {
				DominoUtils.handleException(e);
				//				throw new RuntimeException(e);
			}
		} else {
			long curThreadid = System.identityHashCode(Thread.currentThread());
			if (log_.isLoggable(Level.SEVERE)) {
				log_.log(Level.SEVERE, "Document doesn't have noteid or unid value so we cannot resurrect it. It was created on thread "
						+ threadid_ + " and we are attempting to resurrect it on thread " + curThreadid);
			}
		}
	}

	/*
	 * Map methods
	 */

	/**
	 * Not implemented
	 *
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Tests if the document contains an item with the given name.
	 *
	 * @param key
	 *            Name of the item to check
	 * @return true, if the document contains an item with the name given in key parameter
	 */
	@Override
	public boolean containsKey(final Object key) {
		// TODO: use keySet()?
		return this.hasItem(key == null ? null : String.valueOf(key));
	}

	/**
	 * Checks if the document contains the given value in any of its items.
	 *
	 * @param value
	 *            Value to search for
	 * @return true if any of the items contains the value
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean containsValue(final Object value) {
		// JG - God, I hope nobody ever actually uses this method
		// NTF - Actually I have some good use cases for it! WHEEEEEE!!
		for (String key : this.keySet()) {
			if (hasItem(key) && value instanceof CharSequence) {
				Item item = getFirstItem(key, true);
				if (item instanceof RichTextItem) {
					String text = ((RichTextItem) item).getText();
					if (text.contains((CharSequence) value)) {
						return true;
					} else {
						continue;
					}
				}
			}
			Object itemVal = this.get(key);
			if (itemVal instanceof List) {
				if (((List) itemVal).contains(value)) {
					return true;
				} else {
					continue;
				}
			}
			if ((value == null && itemVal == null) || (value != null && value.equals(itemVal))) {
				return true;
			}
		}
		return false;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean containsValue(final Object value, final String[] itemnames) {
		for (String key : itemnames) {
			if (hasItem(key) && value instanceof CharSequence) {
				Item item = getFirstItem(key, true);
				if (item instanceof RichTextItem) {
					String text = ((RichTextItem) item).getText();
					if (text.contains((CharSequence) value)) {
						return true;
					} else {
						continue;
					}
				}
			}
			Object itemVal = this.get(key);
			if (itemVal instanceof List) {
				if (((List) itemVal).contains(value)) {
					return true;
				} else {
					continue;
				}
			}
			if ((value == null && itemVal == null) || (value != null && value.equals(itemVal))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(final Object value, final Collection<String> itemnames) {
		return containsValue(value, itemnames.toArray(new String[itemnames.size()]));
	}

	@Override
	public boolean containsValues(final Map<String, Object> filterMap) {
		boolean result = false;
		for (String key : filterMap.keySet()) {
			String[] args = new String[1];
			args[0] = key;
			result = containsValue(filterMap.get(key), args);
			if (!result) {
				break;
			}
		}

		return result;
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		// TODO Implement a "viewing" Set and Map.Entry for this or throw an UnsupportedOperationException
		return new DocumentEntrySet(this);
	}

	/**
	 * Gets a value depending on the parameter 'key'. Key can either be a &#64;Formula, a special value or an item name.
	 * <p>
	 * Possible values for the 'key' parameter:
	 * <dl>
	 * <dt>parentdocument</dt>
	 * <dd>Returns the parent document if this document is a response</dd>
	 * <dt>@accesseddate</dt>
	 * <dd>Indicates the time and date when the document was last accessed by a Notes client, whether for reading or editing as a
	 * java.util.Date.</dd>
	 * <dt>@modifieddate</dt>
	 * <dd>Returns a time-date value indicating when the document was modified initially as a java.util.Date</dd>
	 * <dt>@createddate</dt>
	 * <dd>Returns the time-date when the document was created as a java.util.Date</dd>
	 * </dl>
	 * </p>
	 *
	 * @param key
	 *            &#64;formula, item name or a special value from a list above
	 * @return value corresponding to the given 'key'
	 */
	@Override
	public Object get(final Object key) {
		//TODO NTF: Back this with a caching Map so repeated calls don't bounce down to the delegate unless needed
		//TODO NTF: Implement a read-only mode for Documents so we know in advance that our cache is valid
		if (key == null) {
			return null;
		}
		// Check for "special" cases

		if (key instanceof CharSequence) {
			String skey = key.toString().toLowerCase();
			if ("parentdocument".equalsIgnoreCase(skey)) {
				return this.getParentDocument();
			}
			if ("form".equalsIgnoreCase(skey)) {
				return this.getFormName();
			}
			if (skey.indexOf("@") != -1) {// TODO RPr: Should we REALLY detect all formulas, like "3+5" or "field[2]" ?
				//TODO NTF: If so, we should change to looking for valid item names first, then trying to treat as formula
				int pos = skey.indexOf('(');
				if (pos != -1) {
					skey = skey.substring(0, pos);
				}
				if ("@accessed".equals(skey)) {
					return this.getLastAccessed();
				}
				if ("@modified".equals(skey)) {
					return this.getLastModified();
				}
				if ("@created".equals(skey)) {
					return this.getCreated();
				}
				if ("@accesseddate".equals(skey)) {
					return this.getLastAccessedDate();
				}
				if ("@modifieddate".equals(skey)) {
					return this.getLastModifiedDate();
				}
				if ("@createddate".equals(skey)) {
					return this.getCreatedDate();
				}
				if ("@documentuniqueid".equals(skey)) {
					return this.getUniversalID();
				}
				if ("@noteid".equals(skey)) {
					return this.getNoteID();
				}
				if ("@doclength".equals(skey)) {
					return this.getSize();
				}
				if ("@isresponsedoc".equals(skey)) {
					return this.isResponse();
				}
				if ("@replicaid".equals(skey)) {
					return this.getAncestorDatabase().getReplicaID();
				}
				if ("@responses".equals(skey)) {
					DocumentCollection resp = this.getResponses();
					if (resp == null) {
						return 0;
					}
					return resp.getCount();
				}
				if ("@isnewdoc".equals(skey)) {
					return this.isNewNote();
				}
				if ("@inheriteddocumentuniqueid".equals(skey)) {
					org.openntf.domino.Document parent = this.getParentDocument();
					if (parent == null) {
						return "";
					}
					return parent.getUniversalID();
				}

				// TODO RPr: This should be replaced
				//TODO NTF: Agreed when we can have an extensible switch for which formula engine to use
				Formula formula = new Formula();
				formula.setExpression(key.toString());
				List<?> value = formula.getValue(this);
				if (value.size() == 1) {
					return value.get(0);
				}
				return value;
			}
		}

		// TODO: What is the best way to use here without breaking everything?
		//Object value = this.getItemValue(key.toString(), Object.class);
		//Object value = this.getItemValue(key.toString(), Object[].class);
		Object value = null;
		String keyS = key.toString();
		try {
			value = this.getItemValue(keyS, Object.class);
		} catch (OpenNTFNotesException e) {
			if (e.getCause() instanceof NotesException || (e.getCause() != null && e.getCause().getCause() instanceof NotesException)) {
				value = getFirstItem(keyS, true);
			}
			if (value == null) {
				throw e;
			}
		}
		if (value instanceof Vector) {
			Vector<?> v = (Vector<?>) value;
			if (v.size() == 1) {
				return v.get(0);
			}
		}
		return value;
		//if (this.containsKey(key)) {
		//		Vector<Object> value = this.getItemValue(key.toString());
		//		if (value == null) {
		//			//TODO Throw an exception if the item data can't be read? Null implies the key doesn't exist
		//			return null;
		//		} else if (value.size() == 1) {
		//			return value.get(0);
		//		}
		//		return value;
		//}
		//return null;
	}

	/**
	 * Returns false.
	 *
	 * @return false
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

	//	private SortedSet<String> fieldNames_;

	protected SortedSet<String> keySetInt() {
		//		if (fieldNames_ == null) {
		SortedSet<String> result = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		//
		// evaluate("@DocFields",...) is 3 times faster than lotus.domino.Document.getItems()
		//
		try {
			// This must be done on the raw session!
			lotus.domino.Session rawSess = toLotus(getAncestorSession());
			Vector<?> v = null;
			try {
				v = rawSess.evaluate("@DocFields", getDelegate());
			} catch (NotesException ne) {
				v = new Vector<Object>();
			}
			if (v != null && !v.isEmpty()) {
				for (Object o : v) {
					if (o != null) {
						result.add(String.valueOf(o));
					}
				}
			}
		} catch (UserAccessException uae) {
			throw uae;
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
		}
		//			ItemVector items = (ItemVector) this.getItems();
		//			String[] names = items.getNames();
		//			for (int i = 0; i < names.length; i++) {
		//				fieldNames_.add(names[i]);
		//			}
		//		}
		//		return fieldNames_;
		return result;
	}

	/**
	 * Returns a set of names of items in this document.
	 *
	 * @return item names sorted in an alphabetical order
	 */
	@Override
	public Set<String> keySet() {
		SortedSet<String> source = keySetInt();
		if (source != null && !source.isEmpty()) {
			return Collections.unmodifiableSet(keySetInt());
		} else {
			return Collections.emptySet();
		}
	}

	/**
	 * Replaces an item's value with a new one.
	 *
	 * @param key
	 *            name of the item to change
	 * @param value
	 *            new value for the item
	 * @return item's old value
	 */
	@Override
	public Object put(final String key, final Object value) {
		if (key != null) {
			Object previousState = this.get(key);
			//this.removeItem(key); RPr: is there a reason why this is needed?
			this.replaceItemValue(key, value, null, false, false);
			// this.get(key);
			// this.save();
			return previousState;
		}
		return null;
	}

	/**
	 * Replaces items specified in the map with new values.
	 *
	 * @param m
	 *            map with item names to change as keys and new values as values
	 */
	@Override
	public void putAll(final Map<? extends String, ? extends Object> m) {
		for (Map.Entry<? extends String, ? extends Object> entry : m.entrySet()) {
			this.removeItem(entry.getKey());
			this.replaceItemValue(entry.getKey(), entry.getValue());
		}
		// this.save();
	}

	/**
	 * Removes an item from this document.
	 *
	 * @param key
	 *            Name of the item to remove
	 * @return Value of the removed item
	 */
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

	/**
	 * Returns number of items in this document
	 */
	@Override
	public int size() {
		return this.getItems().size();
	}

	/**
	 * Not implemented
	 *
	 * @returns null
	 */
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
	public final Database getAncestorDatabase() {
		return this.getParentDatabase();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return parent.getAncestorSession();
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
			jw.outStringProperty("@noteid", getNoteID());
			jw.outStringProperty("@replicaid", getParentDatabase().getReplicaID());
			jw.outStringProperty("@metaversalid", getMetaversalID());

			try {
				jw.outStringProperty("@created", getCreated().toGMTISO());
				jw.outStringProperty("@lastmodified", getLastModified().toGMTISO());
				jw.outStringProperty("@lastaccessed", getLastAccessed().toGMTISO());
			} catch (Exception e) {
				DominoUtils.handleException(e, "Exception trying to index Dates.");
			}
			Set<String> keys = keySet();
			for (String key : keys) {
				Item currItem = getFirstItem(key);

				Type itemType = currItem.getTypeEx();

				try {
					if (itemType == Type.ATTACHMENT) {
						jw.outProperty(key, "ATTACHMENT");
					} else if (itemType == Type.AUTHORS || itemType == Type.READERS || itemType == Type.NAMES || itemType == Type.TEXT
							|| itemType == Type.NUMBERS) {
						Vector<Object> values = currItem.getValues();
						if (values.size() == 1) {
							jw.outProperty(key, values.elementAt(0));
						} else {
							jw.outProperty(key, values);
						}
					} else if (itemType == Type.DATETIMES) {
						Vector<DateTime> values = currItem.getValueDateTimeArray();
						//						Vector<Date> valueDates = new Vector<Date>();
						//						for (DateTime dt : values) {
						//							valueDates.add(dt.toJavaDate());
						//						}

						if (values.size() == 1) {
							jw.outProperty(key, values.get(0).toGMTISO());
						} else {
							jw.outProperty(key, TypeUtils.toStrings(values));
						}
					} else if (itemType == Type.EMBEDDEDOBJECT) {
						jw.outProperty(key, "EMBEDDED_OBJECT");
					} else if (itemType == Type.RICHTEXT) {
						RichTextItem rtItem = (RichTextItem) currItem;
						jw.outProperty(key, rtItem.getUnformattedText());
					} else if (itemType == Type.MIME_PART) {
						MIMEEntity mimeEntity = currItem.getMIMEEntity();
						if (mimeEntity != null) {
							jw.outProperty(key, mimeEntity.getContentAsText());
						} else {
							jw.outProperty(key, "MIME_PART null");
						}
					}
				} catch (Exception e) {
					DominoUtils.handleException(e, this);
					e.printStackTrace();	// NTF - temporary
				}

				//				if (currItem.getMIMEEntity() == null) {
				//					jw.outProperty(key, currItem.getText());
				//				} else {
				//					String abstractedText = currItem.abstractText(0, false, false);
				//					if (null == abstractedText) {
				//						jw.outProperty(key, "**MIME ITEM, VALUE CANNOT BE DECODED TO JSON**");
				//					} else {
				//						jw.outProperty(key, abstractedText);
				//					}
				//				}
			}
			jw.endObject();
			jw.flush();
		} catch (IOException e) {
			DominoUtils.handleException(e, this);
			return null;
		} /*catch (JsonException e) {
			DominoUtils.handleException(e, this);
			return null;
			}*/
		return sw.toString();
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Document#getMetaversalID()
	 */
	@Override
	public String getMetaversalID() {
		String replid = getAncestorDatabase().getReplicaID();
		String unid = getUniversalID();
		return replid + unid;
	}

	@Override
	public NoteCoordinate getNoteCoordinate() {
		return NoteCoordinate.Utils.getNoteCoordinate(getMetaversalID().toLowerCase());
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Document#getMetaversalID(java.lang.String)
	 */
	@Override
	public String getMetaversalID(final String serverName) {
		return serverName + "!!" + getMetaversalID();
	}

	@Override
	public List<Item> getItems(final Type type) {
		List<Item> result = new ArrayList<Item>();
		for (Item item : getItems()) {
			if (item.getTypeEx() == type) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public List<Item> getItems(final Flags flags) {
		List<Item> result = new ArrayList<Item>();
		for (Item item : getItems()) {
			if (item.hasFlag(flags)) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> asDocMap() {
		return this;
	}

	@Override
	public void fillExceptionDetails(final List<ExceptionDetails.Entry> result) {
		Database myDB = getAncestorDatabase();
		String repId = "";

		if (myDB != null) {
			myDB.fillExceptionDetails(result);
			repId = "ReplicaID:" + myDB.getReplicaID() + ", ";
		}
		String myDetail = repId + "UNID:" + unid_ + ", NoteID:" + noteid_;

		try {
			String myForm = getDelegate().getItemValueString("form");
			if (myForm != null && !myForm.isEmpty()) {
				myDetail += ", Form:" + myForm;
			}
		} catch (NotesException e) {
		}
		result.add(new ExceptionDetails.Entry(this, myDetail));
	}

	public final static String CHUNK_TYPE_NAME = "ODAChunk";

	protected boolean isChunked(final String name) {
		boolean result = false;
		if (hasItem(name)) {
			if (hasItem(name + "$" + 1)) {
				result = true;
			}
		}
		return result;
	}

	protected String[] getChunkNames(final String name) {
		List<String> list = new ArrayList<String>();
		for (String key : keySet()) {
			if (key.equalsIgnoreCase(name)) {
				list.add(key);
			} else if (key.startsWith(name + "$")) {
				list.add(key);
			}
		}
		return list.toArray(TypeUtils.DEFAULT_STR_ARRAY);
	}

	protected void writeBinaryChunk(final String name, final int chunk, final byte[] data) {
		String itemName = name;
		if (chunk > 0) {
			itemName = name + "$" + chunk;
		}
		try {
			getDelegate().replaceItemValueCustomDataBytes(itemName, CHUNK_TYPE_NAME, data);
			markDirty(itemName, true);
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
		}
	}

	@Override
	public void writeBinary(final String name, final byte[] data, int chunkSize) {
		int len = data.length;
		if (chunkSize < 1024) {
			chunkSize = 1024 * 64;
		}
		int i = 0;
		if (len <= chunkSize) {
			writeBinaryChunk(name, 0, data);
		} else {
			byte[] buffer = new byte[chunkSize];
			int lastChunkSize = len % chunkSize;
			int chunks = (len / chunkSize);
			if (lastChunkSize > 0) {
				//				System.out.println("DEBUG: Last chunk size is: " + lastChunkSize);
				chunks++;
			}
			for (i = 0; i < chunks; i++) {
				if (i == chunks - 1 && lastChunkSize > 0) {
					//					System.out.println("DEBUG: Writing last chunk");
					byte[] lastBuffer = new byte[lastChunkSize];
					System.arraycopy(data, lastChunkSize, lastBuffer, 0, lastBuffer.length);
					writeBinaryChunk(name, i, lastBuffer);
				} else {
					System.arraycopy(data, i * chunkSize, buffer, 0, buffer.length);
					writeBinaryChunk(name, i, buffer);
				}
			}
		}
		while (hasItem(name + "$" + (++i))) {
			removeItem(name + "$" + i);
		}

	}

	@Override
	public byte[] readBinaryChunk(final String name, final int chunk) {
		String itemName = name;
		if (chunk > 0) {
			itemName = name + "$" + chunk;
		}
		try {
			return getDelegate().getItemValueCustomDataBytes(itemName, CHUNK_TYPE_NAME);
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
		}
		return null;
	}

	@Override
	public byte[] readBinary(final String name) {
		if (isChunked(name)) {
			String[] chunkNames = getChunkNames(name);
			int chunks = chunkNames.length;
			Item startChunk = getFirstItem(name);
			if (startChunk != null) {
				int chunkSize = startChunk.getValueLength();
				int resultMaxSize = chunkSize * chunks;
				byte[] accumulated = new byte[resultMaxSize];
				int actual = 0;

				for (String curChunk : chunkNames) {
					//					System.out.println("DEBUG: Attempting binary read from " + curChunk);
					try {
						byte[] cur = getDelegate().getItemValueCustomDataBytes(curChunk, CHUNK_TYPE_NAME);
						//						System.out.println("Found " + cur.length + " bytes from chunk " + curChunk);
						System.arraycopy(cur, 0, accumulated, actual, cur.length);
						actual = actual + cur.length;
					} catch (Exception e) {
						DominoUtils.handleException(e, this);
					}
				}
				byte[] result = new byte[actual];
				//				System.out.println("DEBUG: resulting read-in array is " + actual + " while we have an actual of " + result.length
				//						+ " for an accumulated " + accumulated.length);
				System.arraycopy(accumulated, 0, result, 0, actual);
				return result;
			} else {
				log_.log(Level.WARNING, "No start chunk available for binary read " + name + " on doucment " + noteid_ + " in db "
						+ getAncestorDatabase().getApiPath());
			}
		} else {
			return readBinaryChunk(name, 0);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<?> getItemSeriesValues(final CharSequence name) {
		List result = new Vector();
		String key = name.toString();
		if (hasItem(key)) {
			result.addAll(getItemValue(key));
		}
		for (int i = 0; i <= 10; i++) {
			String curKey = key + "_" + i;
			if (hasItem(curKey)) {
				result.addAll(getItemValue(curKey));
			}
		}
		int i = 11;
		while (hasItem(key + "_" + i)) {
			result.addAll(getItemValue(key + "_" + i));
			i++;
		}
		return result;
	}

	@Override
	public <T> T getItemSeriesValues(final CharSequence name, final Class<T> type) {
		T result = null;
		result = TypeUtils.vectorToClass(getItemSeriesValues(name), type, getAncestorSession());
		return result;
	}

	@Override
	public Map<String, List<Object>> getItemTable(final CharSequence... itemnames) {
		return Documents.getItemTable(this, itemnames);
	}

	@Override
	public List<Map<String, Object>> getItemTablePivot(final CharSequence... itemnames) {
		return Documents.getItemTablePivot(this, itemnames);
	}

	@Override
	public void setItemTable(final Map<String, List<Object>> table) {
		Documents.setItemTable(this, table);
	}

	@Override
	public void setItemTablePivot(final List<Map<String, Object>> pivot) {
		Documents.setItemTablePivot(this, pivot);
	}

	@Override
	public Name getItemValueName(final String itemName) {
		try {
			if (Strings.isBlankString(itemName)) {
				throw new IllegalArgumentException("Item Name is blank or null");
			}

			final String string = getItemValueString(itemName);
			return (Strings.isBlankString(string)) ? null : getAncestorSession().createName(string);
		} catch (final Exception e) {
			DominoUtils.handleException(e);
			return null;
		}

	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

	@Override
	public String toString() {
		return getAncestorDatabase().getApiPath() + "/" + unid_;
	}

	@Override
	public NoteClass getNoteClass() {
		if (extendedNoteInfos_ == null) {
			extendedNoteInfos_ = new ExtendedNoteInfos();
		}
		return extendedNoteInfos_.noteClass;
	}

	@Override
	public boolean isDefault() {
		if (extendedNoteInfos_ == null) {
			extendedNoteInfos_ = new ExtendedNoteInfos();
		}
		return extendedNoteInfos_.isDefault;
	}

	@Override
	public boolean isPrivate() {
		if (extendedNoteInfos_ == null) {
			extendedNoteInfos_ = new ExtendedNoteInfos();
		}
		return extendedNoteInfos_.isDefault;
	}

	/**
	 * Not implemented
	 */
	public void replaceAttachment(final String richTextName, final String attachmentName, final String file) {

	}

	/**
	 * Not implemented
	 *
	 */
	public void removeAttachment(final String richTextName, final String attachmentName) {

	}

	/**
	 * Not implemented
	 *
	 * @return an empty collection
	 */
	public Collection<EmbeddedObject> getAllAttachments() {
		Collection<EmbeddedObject> result = new ArrayList<EmbeddedObject>();
		return result;
	}
	//	@Override
	//	protected boolean allowAccessAcrossThreads() {
	//		return !isDirty();
	//	}

	@Override
	public void encrypt(final UserID userId) {
		try {
			getDelegate().encrypt(userId);
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
		}
	}

	@Override
	public void encrypt(final String filePath, final String password) {
		try {
			getDelegate().encrypt(filePath, password);
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
		}
	}

	@Override
	public boolean isCancelSendOnMissingKey() {
		try {
			return getDelegate().isCancelSendOnMissingKey();
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
			return false;
		}
	}

	@Override
	public void setCancelSendOnMissingKey(final boolean cancelSend) {
		try {
			getDelegate().setCancelSendOnMissingKey(cancelSend);
		} catch (Exception e) {
			DominoUtils.handleException(e, this);
		}
	}

	@Override
	public List<Item> getItemsModifiedSince(final DateTime datetime) {
		List<Item> result = new ArrayList<Item>();
		for (Item item : this.getItems()) {
			if (!item.getLastModified().isBefore(datetime)) {
				result.add(item);
			}
		}
		return result;
	}

}
