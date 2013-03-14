package org.openntf.domino;

import java.io.IOException;
import java.io.Writer;
import java.util.Vector;

import lotus.domino.DocumentCollection;
import lotus.domino.EmbeddedObject;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.XSLTResultTarget;

public interface Document extends Base<lotus.domino.Document>, lotus.domino.Document {
	@Override
	// TODO Switch to new class
	public lotus.domino.Item appendItemValue(String name);

	@Override
	// TODO Switch to new class
	public lotus.domino.Item appendItemValue(String name, double value);

	@Override
	// TODO Switch to new class
	public lotus.domino.Item appendItemValue(String name, int value);

	@Override
	// TODO Switch to new class
	public lotus.domino.Item appendItemValue(String name, Object value);

	@Override
	public void attachVCard(lotus.domino.Base document);

	// TODO Check what this method does (String param isn't documented!), probably setting the charset.
	@Override
	public void attachVCard(lotus.domino.Base document, String charset);

	@Override
	public boolean closeMIMEEntities();

	@Override
	public boolean closeMIMEEntities(boolean saveChanges);

	@Override
	public boolean closeMIMEEntities(boolean saveChanges, String entityItemName);

	@Override
	public boolean computeWithForm(boolean doDataTypes, boolean raiseError);

	@Override
	public void convertToMIME();

	@Override
	public void convertToMIME(int conversionType);

	@Override
	public void convertToMIME(int conversionType, int options);

	@Override
	public void copyAllItems(lotus.domino.Document doc, boolean replace);

	@Override
	// TODO Switch to new class
	public lotus.domino.Item copyItem(lotus.domino.Item item);

	@Override
	// TODO Switch to new class
	public lotus.domino.Item copyItem(lotus.domino.Item item, String newName);

	@Override
	public Document copyToDatabase(lotus.domino.Database db);

	@Override
	// TODO Switch to new class
	public lotus.domino.MIMEEntity createMIMEEntity();

	@Override
	// TODO Switch to new class
	public lotus.domino.MIMEEntity createMIMEEntity(String itemName);

	@Override
	public Document createReplyMessage(boolean toAll);

	@Override
	// TODO Switch to new class
	public lotus.domino.RichTextItem createRichTextItem(String name);

	@Override
	public void encrypt();

	@Override
	public String generateXML();

	@Override
	public void generateXML(Object style, XSLTResultTarget target) throws IOException, NotesException;

	@Override
	public void generateXML(Writer w) throws NotesException, IOException;

	@Override
	// TODO Switch to new class
	public lotus.domino.EmbeddedObject getAttachment(String fileName);

	@SuppressWarnings("unchecked")
	@Override
	public Vector getAuthors();

	@SuppressWarnings("unchecked")
	@Override
	public Vector getColumnValues();

	@Override
	public DateTime getCreated();

	@Override
	public Vector<EmbeddedObject> getEmbeddedObjects();

	@Override
	public Vector<String> getEncryptionKeys();

	@Override
	// TODO Switch to new class
	public lotus.domino.Item getFirstItem(String name);

	@SuppressWarnings("unchecked")
	@Override
	public Vector getFolderReferences();

	@Override
	public int getFTSearchScore();

	@Override
	public String getHttpURL();

	@Override
	public DateTime getInitiallyModified();

	@Override
	public Vector<Item> getItems();

	@SuppressWarnings("unchecked")
	@Override
	public Vector getItemValue(String name);

	@Override
	public Object getItemValueCustomData(String itemName) throws IOException, ClassNotFoundException, NotesException;

	@Override
	public Object getItemValueCustomData(String itemName, String dataTypeName) throws IOException, ClassNotFoundException, NotesException;

	@Override
	public byte[] getItemValueCustomDataBytes(String itemName, String dataTypeName) throws IOException, NotesException;

	@Override
	public Vector<DateTime> getItemValueDateTimeArray(String name);

	@Override
	public double getItemValueDouble(String name);

	@Override
	public int getItemValueInteger(String name);

	@Override
	public String getItemValueString(String name);

	@Override
	public String getKey();

	@Override
	public DateTime getLastAccessed();

	@Override
	public DateTime getLastModified();

	@Override
	public Vector<String> getLockHolders();

	@Override
	// TODO Switch to new class
	public lotus.domino.MIMEEntity getMIMEEntity();

	@Override
	// TODO Switch to new class
	public lotus.domino.MIMEEntity getMIMEEntity(String itemName);

	@Override
	public String getNameOfProfile();

	@Override
	public String getNoteID();

	@Override
	public String getNotesURL();

	@Override
	public org.openntf.domino.Database getParentDatabase();

	@Override
	public String getParentDocumentUNID();

	@Override
	public View getParentView();

	@Override
	public boolean getRead();

	@Override
	public boolean getRead(String userName);

	@Override
	public Vector<String> getReceivedItemText();

	@Override
	public DocumentCollection getResponses();

	@Override
	public String getSigner();

	@Override
	public int getSize();

	@Override
	public String getUniversalID();

	@Override
	public String getURL();

	@Override
	public String getVerifier();

	@Override
	public boolean hasEmbedded();

	@Override
	public boolean hasItem(String name);

	@Override
	public boolean isDeleted();

	@Override
	public boolean isEncrypted();

	@Override
	public boolean isEncryptOnSend();

	@Override
	public boolean isNewNote();

	@Override
	public boolean isPreferJavaDates();

	@Override
	public boolean isProfile();

	@Override
	public boolean isResponse();

	@Override
	public boolean isSaveMessageOnSend();

	@Override
	public boolean isSentByAgent();

	@Override
	public boolean isSigned();

	@Override
	public boolean isSignOnSend();

	@Override
	public boolean isValid();

	@Override
	public boolean lock();

	@Override
	public boolean lock(boolean provisionalOk);

	@Override
	public boolean lock(String name);

	@Override
	public boolean lock(String name, boolean provisionalOk);

	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(Vector names);

	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(Vector names, boolean provisionalOk);

	@Override
	public boolean lockProvisional();

	@Override
	public boolean lockProvisional(String name);

	@SuppressWarnings("unchecked")
	@Override
	public boolean lockProvisional(Vector names);

	@Override
	public void makeResponse(lotus.domino.Document doc);

	@Override
	public void markRead();

	@Override
	public void markRead(String userName);

	@Override
	public void markUnread();

	@Override
	public void markUnread(String userName);

	@Override
	public void putInFolder(String name);

	@Override
	public void putInFolder(String name, boolean createOnFail);

	@Override
	public void recycle();

	@SuppressWarnings("unchecked")
	@Override
	public void recycle(Vector obj) throws NotesException;

	@Override
	public boolean remove(boolean force);

	@Override
	public void removeFromFolder(String name);

	@Override
	public void removeItem(String name);

	@Override
	public boolean removePermanently(boolean force);

	@Override
	public boolean renderToRTItem(lotus.domino.RichTextItem rtitem);

	@Override
	// TODO Switch to new class
	public lotus.domino.Item replaceItemValue(String name, Object value);

	@Override
	// TODO Switch to new class
	public lotus.domino.Item replaceItemValueCustomData(String name, Object userObj) throws IOException, NotesException;

	@Override
	// TODO Switch to new class
	public lotus.domino.Item replaceItemValueCustomData(String name, String dataTypeName, Object userObj) throws IOException,
			NotesException;

	@Override
	// TODO Switch to new class
	public lotus.domino.Item replaceItemValueCustomDataBytes(String name, String dataTypeName, byte[] byteArray) throws IOException,
			NotesException;

	@Override
	public boolean save();

	@Override
	public boolean save(boolean force);

	@Override
	public boolean save(boolean force, boolean makeResponse);

	@Override
	public boolean save(boolean force, boolean makeResponse, boolean markRead);

	@Override
	public void send();

	@Override
	public void send(boolean attachForm);

	@Override
	public void send(boolean attachForm, String recipient);

	@SuppressWarnings("unchecked")
	@Override
	public void send(boolean attachForm, Vector recipients);

	@Override
	public void send(String recipient);

	@SuppressWarnings("unchecked")
	@Override
	public void send(Vector recipients);

	@SuppressWarnings("unchecked")
	@Override
	public void setEncryptionKeys(Vector keys);

	@Override
	public void setEncryptOnSend(boolean flag);

	@Override
	public void setPreferJavaDates(boolean flag);

	@Override
	public void setSaveMessageOnSend(boolean flag);

	@Override
	public void setSignOnSend(boolean flag);

	@Override
	public void setUniversalID(String unid);

	@Override
	public void sign();

	@Override
	public void unlock();
}
