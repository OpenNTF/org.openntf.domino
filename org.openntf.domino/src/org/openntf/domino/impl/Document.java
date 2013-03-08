package org.openntf.domino.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.DocumentCollection;
import lotus.domino.EmbeddedObject;
import lotus.domino.Item;
import lotus.domino.MIMEEntity;
import lotus.domino.NotesException;
import lotus.domino.RichTextItem;
import lotus.domino.View;
import lotus.domino.XSLTResultTarget;

import org.openntf.domino.utils.DominoUtils;

public class Document extends Base<org.openntf.domino.Document, lotus.domino.Document> implements org.openntf.domino.Document {
	lotus.domino.Document temp;

	public Document(lotus.domino.Document delegate) {
		super(delegate);
	}

	@Override
	public Item appendItemValue(String name) {
		try {
			return getDelegate().appendItemValue(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Item appendItemValue(String name, double value) {
		try {
			return getDelegate().appendItemValue(name, value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Item appendItemValue(String name, int value) {
		try {
			return getDelegate().appendItemValue(name, value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Item appendItemValue(String name, Object value) {
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
	public void attachVCard(lotus.domino.Base document, String arg1) {
		try {
			getDelegate().attachVCard(document, arg1);
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
	public boolean closeMIMEEntities(boolean savechanges) {
		try {
			return getDelegate().closeMIMEEntities(savechanges);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean closeMIMEEntities(boolean savechanges, String entityitemname) {
		try {
			return getDelegate().closeMIMEEntities(savechanges, entityitemname);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean computeWithForm(boolean dodatatypes, boolean raiseerror) {
		try {
			return getDelegate().computeWithForm(dodatatypes, raiseerror);
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
	public void convertToMIME(int conversiontype) {
		try {
			getDelegate().convertToMIME(conversiontype);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void convertToMIME(int conversiontype, int options) {
		try {
			getDelegate().convertToMIME(conversiontype, options);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void copyAllItems(lotus.domino.Document doc, boolean replace) {
		try {
			getDelegate().copyAllItems(doc, replace);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public Item copyItem(Item item) {
		try {
			return getDelegate().copyItem(item);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Item copyItem(Item item, String newname) {
		try {
			return getDelegate().copyItem(item, newname);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.Document copyToDatabase(Database db) {
		try {
			return getDelegate().copyToDatabase(db);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public MIMEEntity createMIMEEntity() {
		try {
			return getDelegate().createMIMEEntity();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public MIMEEntity createMIMEEntity(String itemName) {
		try {
			return getDelegate().createMIMEEntity(itemName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.Document createReplyMessage(boolean toall) {
		try {
			return getDelegate().createReplyMessage(toall);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public RichTextItem createRichTextItem(String name) {
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
	public void generateXML(Object style, XSLTResultTarget result) throws IOException, NotesException {
		try {
			getDelegate().generateXML(style, result);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void generateXML(Writer w) throws NotesException, IOException {
		try {
			getDelegate().generateXML(w);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public EmbeddedObject getAttachment(String filename) {
		try {
			return getDelegate().getAttachment(filename);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector getAuthors() {
		try {
			return getDelegate().getAuthors();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector getColumnValues() {
		try {
			return getDelegate().getColumnValues();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public DateTime getCreated() {
		try {
			return new DateTime(getDelegate().getCreated());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector getEmbeddedObjects() {
		try {
			return getDelegate().getEmbeddedObjects();
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
	public Item getFirstItem(String name) {
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
	public DateTime getInitiallyModified() {
		try {
			return new DateTime(getDelegate().getInitiallyModified());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
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
	public Object getItemValueCustomData(String itemname) throws IOException, ClassNotFoundException, NotesException {
		try {
			return getDelegate().getItemValueCustomData(itemname);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Object getItemValueCustomData(String itemname, String datatypename) throws IOException, ClassNotFoundException, NotesException {
		try {
			return getDelegate().getItemValueCustomData(itemname, datatypename);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public byte[] getItemValueCustomDataBytes(String itemname, String datatypename) throws IOException, NotesException {
		try {
			return getDelegate().getItemValueCustomDataBytes(itemname, datatypename);
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
	public DateTime getLastAccessed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DateTime getLastModified() {
		try {
			return new DateTime(getDelegate().getLastModified());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
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
	public MIMEEntity getMIMEEntity() {
		try {
			return getDelegate().getMIMEEntity();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public MIMEEntity getMIMEEntity(String itemName) {
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
	public Database getParentDatabase() {
		try {
			return getDelegate().getParentDatabase();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
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
			return getDelegate().getParentView();
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
	public boolean getRead(String username) {
		try {
			return getDelegate().getRead(username);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector getReceivedItemText() {
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
			return getDelegate().getResponses();
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
	public boolean lock(boolean provisionalok) {
		try {
			return getDelegate().lock(provisionalok);
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
	public boolean lock(String name, boolean provisionalok) {
		try {
			return getDelegate().lock(name, provisionalok);
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
	public boolean lock(Vector names, boolean provisionalok) {
		try {
			return getDelegate().lock(names, provisionalok);
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
			getDelegate().makeResponse(doc);
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
	public void markRead(String username) {
		try {
			getDelegate().markRead(username);
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
	public void markUnread(String username) {
		try {
			getDelegate().markUnread(username);
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
	public void putInFolder(String name, boolean createonfail) {
		try {
			getDelegate().putInFolder(name, createonfail);
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
	public boolean renderToRTItem(RichTextItem rtitem) {
		try {
			getDelegate().renderToRTItem(rtitem);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Item replaceItemValue(String name, Object value) {
		try {
			if (value instanceof DateTime) {
				return getDelegate().replaceItemValue(name, ((DateTime) value).getDelegate());
			} else if (value instanceof Number && !(value instanceof Integer || value instanceof Double)) {
				return getDelegate().replaceItemValue(name, ((Number) value).intValue());
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

			return getDelegate().replaceItemValue(name, value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		}
		return null;
	}

	@Override
	public Item replaceItemValueCustomData(String itemname, Object userobj) throws IOException, NotesException {
		try {
			return getDelegate().replaceItemValueCustomData(itemname, userobj);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Item replaceItemValueCustomData(String itemname, String datatypename, Object userobj) throws IOException, NotesException {
		try {
			return getDelegate().replaceItemValueCustomData(itemname, datatypename, userobj);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Item replaceItemValueCustomDataBytes(String itemname, String datatypename, byte[] bytearray) throws IOException, NotesException {
		try {
			return getDelegate().replaceItemValueCustomDataBytes(itemname, datatypename, bytearray);
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
	public boolean save(boolean force, boolean makeresponse) {
		try {
			return getDelegate().save(force, makeresponse);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean save(boolean force, boolean makeresponse, boolean markread) {
		try {
			return getDelegate().save(force, makeresponse, markread);
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
	public void send(boolean attachform) {
		try {
			getDelegate().send(attachform);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void send(boolean attachform, String recipient) {
		try {
			getDelegate().send(attachform, recipient);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void send(boolean attachform, Vector recipients) {
		try {
			getDelegate().send(attachform, recipients);
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
