package org.openntf.domino.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class Document extends Base<org.openntf.domino.Document, lotus.domino.Document> implements org.openntf.domino.Document {
	// NTF - these are immutable by definition, so we should just copy it when we read in the doc
	// yes, we're creating objects we might not need, but that's better than risking the toxicity of evil, wicked DateTime
	// these ought to be final, since they can't change, but it makes the constructor really messy

	// NTF - Okay, after testing, maybe these just need to be JIT getters. It added about 10% to Document iteration time.
	// NTF - Done. And yeah, it make quite a performance difference. More like 20%, really
	private Date created_;
	private Date initiallyModified_;
	private Date lastModified_;
	private Date lastAccessed_;

	public Document(lotus.domino.Document delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, Factory.getParentDatabase(parent));
		// initialize(delegate);
	}

	@SuppressWarnings("unused")
	private void initialize(lotus.domino.Document delegate) {
		try {
			delegate.setPreferJavaDates(true);
			// created_ = DominoUtils.toJavaDateSafe(delegate.getCreated());
			// initiallyModified_ = DominoUtils.toJavaDateSafe(delegate.getInitiallyModified());
			// lastModified_ = DominoUtils.toJavaDateSafe(delegate.getLastModified());
			// lastAccessed_ = DominoUtils.toJavaDateSafe(delegate.getLastAccessed());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public lotus.domino.Item appendItemValue(String name) {
		try {
			return getDelegate().appendItemValue(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.Item appendItemValue(String name, double value) {
		try {
			return getDelegate().appendItemValue(name, value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.Item appendItemValue(String name, int value) {
		try {
			return getDelegate().appendItemValue(name, value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.Item appendItemValue(String name, Object value) {
		try {
			return getDelegate().appendItemValue(name, value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public void attachVCard(lotus.domino.Base document) {
		try {
			getDelegate().attachVCard(document);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void attachVCard(lotus.domino.Base document, String charset) {
		try {
			getDelegate().attachVCard(document, charset);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public boolean closeMIMEEntities() {
		try {
			return getDelegate().closeMIMEEntities();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean closeMIMEEntities(boolean saveChanges) {
		try {
			return getDelegate().closeMIMEEntities(saveChanges);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean closeMIMEEntities(boolean saveChanges, String entityItemName) {
		try {
			return getDelegate().closeMIMEEntities(saveChanges, entityItemName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean computeWithForm(boolean doDataTypes, boolean raiseError) {
		try {
			return getDelegate().computeWithForm(doDataTypes, raiseError);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public void convertToMIME() {
		try {
			getDelegate().convertToMIME();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void convertToMIME(int conversionType) {
		try {
			getDelegate().convertToMIME(conversionType);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void convertToMIME(int conversionType, int options) {
		try {
			getDelegate().convertToMIME(conversionType, options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void copyAllItems(lotus.domino.Document doc, boolean replace) {
		try {
			getDelegate().copyAllItems((lotus.domino.Document) toLotus(doc), replace);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public lotus.domino.Item copyItem(lotus.domino.Item item) {
		try {
			return getDelegate().copyItem((lotus.domino.Item) toLotus(item));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.Item copyItem(lotus.domino.Item item, String newName) {
		try {
			return getDelegate().copyItem((lotus.domino.Item) toLotus(item), newName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Document copyToDatabase(lotus.domino.Database db) {
		try {
			return Factory.fromLotus(getDelegate().copyToDatabase((lotus.domino.Database) toLotus(db)), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.MIMEEntity createMIMEEntity() {
		try {
			return getDelegate().createMIMEEntity();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.MIMEEntity createMIMEEntity(String itemName) {
		try {
			return getDelegate().createMIMEEntity(itemName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Document createReplyMessage(boolean toAll) {
		try {
			return Factory.fromLotus(getDelegate().createReplyMessage(toAll), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.RichTextItem createRichTextItem(String name) {
		try {
			return getDelegate().createRichTextItem(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public void encrypt() {
		try {
			getDelegate().encrypt();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public String generateXML() {
		try {
			return getDelegate().generateXML();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public void generateXML(Object style, lotus.domino.XSLTResultTarget result) throws IOException {
		try {
			getDelegate().generateXML(style, result);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void generateXML(Writer w) throws IOException {
		try {
			getDelegate().generateXML(w);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public EmbeddedObject getAttachment(String fileName) {
		try {
			return Factory.fromLotus(getDelegate().getAttachment(fileName), EmbeddedObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

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

	@Override
	public Vector<Object> getColumnValues() {
		try {
			Vector<Object> result = new Vector<Object>();
			for (Object value : getDelegate().getColumnValues()) {
				if (value instanceof lotus.domino.DateTime) {
					result.add(Factory.fromLotus((lotus.domino.DateTime) value, DateTime.class, this));
				} else {
					result.add(value);
				}
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	@Deprecated
	@Legacy(Legacy.DATETIME_WARNING)
	public org.openntf.domino.DateTime getCreated() {
		try {
			if (created_ == null) {
				created_ = DominoUtils.toJavaDateSafe(getDelegate().getCreated());
			}
			return new DateTime(created_, this); // TODO NTF - maybe ditch the parent?
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

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

	@Override
	public Vector<org.openntf.domino.EmbeddedObject> getEmbeddedObjects() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getEmbeddedObjects(), org.openntf.domino.EmbeddedObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector getEncryptionKeys() {
		try {
			return getDelegate().getEncryptionKeys();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public int getFTSearchScore() {
		try {
			return getDelegate().getFTSearchScore();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return 0;
	}

	@Override
	public lotus.domino.Item getFirstItem(String name) {
		try {
			return getDelegate().getFirstItem(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector getFolderReferences() {
		try {
			return getDelegate().getFolderReferences();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getHttpURL() {
		try {
			return getDelegate().getHttpURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	@Deprecated
	@Legacy(Legacy.DATETIME_WARNING)
	public DateTime getInitiallyModified() {
		try {
			if (initiallyModified_ == null) {
				initiallyModified_ = DominoUtils.toJavaDateSafe(getDelegate().getInitiallyModified());
			}
			return new DateTime(initiallyModified_, this); // TODO NTF - maybe ditch the parent?

		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

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

	@SuppressWarnings("unchecked")
	@Override
	public Vector getItemValue(String name) {
		try {
			return getDelegate().getItemValue(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Object getItemValueCustomData(String itemName) throws IOException, ClassNotFoundException {
		try {
			return getDelegate().getItemValueCustomData(itemName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Object getItemValueCustomData(String itemName, String dataTypeName) throws IOException, ClassNotFoundException {
		try {
			return getDelegate().getItemValueCustomData(itemName, dataTypeName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public byte[] getItemValueCustomDataBytes(String itemName, String dataTypeName) throws IOException {
		try {
			return getDelegate().getItemValueCustomDataBytes(itemName, dataTypeName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector getItemValueDateTimeArray(String name) {
		try {
			return getDelegate().getItemValueDateTimeArray(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public double getItemValueDouble(String name) {
		try {
			return getDelegate().getItemValueDouble(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return 0d;
	}

	@Override
	public int getItemValueInteger(String name) {
		try {
			return getDelegate().getItemValueInteger(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return 0;
	}

	@Override
	public String getItemValueString(String name) {
		try {
			return getDelegate().getItemValueString(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector getItems() {
		try {
			return getDelegate().getItems();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getKey() {
		try {
			return getDelegate().getKey();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	@Deprecated
	@Legacy(Legacy.DATETIME_WARNING)
	public DateTime getLastAccessed() {
		try {
			if (lastAccessed_ == null) {
				lastAccessed_ = DominoUtils.toJavaDateSafe(getDelegate().getLastAccessed());
			}
			return new DateTime(lastAccessed_, this); // TODO NTF - maybe ditch the parent?

		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

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

	@Override
	@Deprecated
	@Legacy(Legacy.DATETIME_WARNING)
	public DateTime getLastModified() {
		try {
			if (lastModified_ == null) {
				lastModified_ = DominoUtils.toJavaDateSafe(getDelegate().getLastModified());
			}
			return new DateTime(lastModified_, this); // TODO NTF - maybe ditch the parent?

		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

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

	@SuppressWarnings("unchecked")
	@Override
	public Vector getLockHolders() {
		try {
			return getDelegate().getLockHolders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.MIMEEntity getMIMEEntity() {
		try {
			return getDelegate().getMIMEEntity();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.MIMEEntity getMIMEEntity(String itemName) {
		try {
			return getDelegate().getMIMEEntity(itemName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getNameOfProfile() {
		try {
			return getDelegate().getNameOfProfile();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getNoteID() {
		try {
			return getDelegate().getNoteID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getNotesURL() {
		try {
			return getDelegate().getNotesURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public org.openntf.domino.Database getParentDatabase() {
		return (org.openntf.domino.Database) super.getParent();
	}

	@Override
	public String getParentDocumentUNID() {
		try {
			return getDelegate().getParentDocumentUNID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public View getParentView() {
		try {
			return Factory.fromLotus(getDelegate().getParentView(), View.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public boolean getRead() {
		try {
			return getDelegate().getRead();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean getRead(String userName) {
		try {
			return getDelegate().getRead(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

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

	@Override
	public DocumentCollection getResponses() {
		try {
			return Factory.fromLotus(getDelegate().getResponses(), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getSigner() {
		try {
			return getDelegate().getSigner();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public int getSize() {
		try {
			return getDelegate().getSize();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return 0;
	}

	@Override
	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getUniversalID() {
		try {
			return getDelegate().getUniversalID();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getVerifier() {
		try {
			return getDelegate().getVerifier();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public boolean hasEmbedded() {
		try {
			return getDelegate().hasEmbedded();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean hasItem(String name) {
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

	@Override
	public boolean isDeleted() {
		try {
			return getDelegate().isDeleted();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isEncryptOnSend() {
		try {
			return getDelegate().isEncryptOnSend();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isEncrypted() {
		try {
			return getDelegate().isEncrypted();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isNewNote() {
		try {
			return getDelegate().isNewNote();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isPreferJavaDates() {
		try {
			return getDelegate().isPreferJavaDates();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isProfile() {
		try {
			return getDelegate().isProfile();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isResponse() {
		try {
			return getDelegate().isResponse();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isSaveMessageOnSend() {
		try {
			return getDelegate().isSaveMessageOnSend();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isSentByAgent() {
		try {
			return getDelegate().isSentByAgent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isSignOnSend() {
		try {
			return getDelegate().isSignOnSend();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isSigned() {
		try {
			return getDelegate().isSigned();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isValid() {
		try {
			return getDelegate().isValid();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean lock() {
		try {
			return getDelegate().lock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean lock(boolean provisionalOk) {
		try {
			return getDelegate().lock(provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean lock(String name) {
		try {
			return getDelegate().lock(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean lock(String name, boolean provisionalOk) {
		try {
			return getDelegate().lock(name, provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(Vector names) {
		try {
			return getDelegate().lock(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(Vector names, boolean provisionalOk) {
		try {
			return getDelegate().lock(names, provisionalOk);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean lockProvisional() {
		try {
			return getDelegate().lockProvisional();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean lockProvisional(String name) {
		try {
			return getDelegate().lockProvisional(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean lockProvisional(Vector names) {
		try {
			return getDelegate().lockProvisional(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public void makeResponse(lotus.domino.Document doc) {
		try {
			getDelegate().makeResponse((lotus.domino.Document) toLotus(doc));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void markRead() {
		try {
			getDelegate().markRead();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void markRead(String userName) {
		try {
			getDelegate().markRead(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void markUnread() {
		try {
			getDelegate().markUnread();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void markUnread(String userName) {
		try {
			getDelegate().markUnread(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void putInFolder(String name) {
		try {
			getDelegate().putInFolder(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void putInFolder(String name, boolean createOnFail) {
		try {
			getDelegate().putInFolder(name, createOnFail);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public boolean remove(boolean force) {
		try {
			return getDelegate().remove(force);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public void removeFromFolder(String name) {
		try {
			getDelegate().removeFromFolder(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void removeItem(String name) {
		try {
			getDelegate().removeItem(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public boolean removePermanently(boolean force) {
		try {
			return getDelegate().removePermanently(force);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean renderToRTItem(lotus.domino.RichTextItem rtitem) {
		try {
			getDelegate().renderToRTItem(rtitem);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public lotus.domino.Item replaceItemValue(String itemName, Object value) {
		try {
			if (value instanceof DateTime) {
				return getDelegate().replaceItemValue(itemName, ((DateTime) value).getDelegate());
			} else if (value instanceof Number && !(value instanceof Integer || value instanceof Double)) {
				return getDelegate().replaceItemValue(itemName, ((Number) value).intValue());
				// } else if (value instanceof Date) {
				// // TODO: make sure this use of DateTime isn't a bug when Session and createDateTime are extended
				// lotus.domino.DateTime dt = DominoUtils.getSession(this).createDateTime((Date) value);
				// Item result = getDelegate().replaceItemValue(name, dt);
				// dt.recycle();
				// return result;
				// } else if (value instanceof Calendar) {
				// lotus.domino.DateTime dt = DominoUtils.getSession(this).createDateTime((Calendar) value);
				// Item result = getDelegate().replaceItemValue(name, dt);
				// dt.recycle();
				// return result;
				// } else if (value instanceof Collection) {
				// // TODO: make this filter the collection for newly-supported types
				// return getDelegate().replaceItemValue(name, new java.util.Vector((Collection) value));
				// } else if (value instanceof Externalizable) {
				// // TODO: implement this - saveState will likely have to store the class name as a header, to be read by restoreState
				// } else if (value instanceof Serializable) {
				// DominoUtils.saveState((Serializable) value, this, name);
			}
			// TODO: also cover StateHolder? That could probably be done with reflection without actually requiring the XSP classes to
			// build

			return getDelegate().replaceItemValue(itemName, value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
		return null;
	}

	@Override
	public lotus.domino.Item replaceItemValueCustomData(String itemName, Object userObj) throws IOException {
		try {
			return getDelegate().replaceItemValueCustomData(itemName, userObj);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.Item replaceItemValueCustomData(String itemname, String dataTypeName, Object userObj) throws IOException {
		try {
			return getDelegate().replaceItemValueCustomData(itemname, dataTypeName, userObj);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.Item replaceItemValueCustomDataBytes(String itemname, String dataTypeName, byte[] byteArray) throws IOException {
		try {
			return getDelegate().replaceItemValueCustomDataBytes(itemname, dataTypeName, byteArray);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public boolean save() {
		try {
			return getDelegate().save();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean save(boolean force) {
		try {
			return getDelegate().save(force);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean save(boolean force, boolean makeResponse) {
		try {
			return getDelegate().save(force, makeResponse);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean save(boolean force, boolean makeResponse, boolean markRead) {
		try {
			return getDelegate().save(force, makeResponse, markRead);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public void send() {
		try {
			getDelegate().send();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void send(boolean attachForm) {
		try {
			getDelegate().send(attachForm);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void send(boolean attachForm, String recipient) {
		try {
			getDelegate().send(attachForm, recipient);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void send(boolean attachForm, Vector recipients) {
		try {
			getDelegate().send(attachForm, recipients);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void send(String recipient) {
		try {
			getDelegate().send(recipient);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void send(Vector recipients) {
		try {
			getDelegate().send(recipients);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setEncryptOnSend(boolean flag) {
		try {
			getDelegate().setEncryptOnSend(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setEncryptionKeys(Vector keys) {
		try {
			getDelegate().setEncryptionKeys(keys);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPreferJavaDates(boolean flag) {
		try {
			getDelegate().setPreferJavaDates(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSaveMessageOnSend(boolean flag) {
		try {
			getDelegate().setSaveMessageOnSend(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSignOnSend(boolean flag) {
		try {
			getDelegate().setSignOnSend(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setUniversalID(String unid) {
		try {
			getDelegate().setUniversalID(unid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void sign() {
		try {
			getDelegate().sign();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void unlock() {
		try {
			getDelegate().unlock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

}