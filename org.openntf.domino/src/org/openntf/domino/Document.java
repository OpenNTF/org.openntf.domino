package org.openntf.domino;

import java.io.IOException;
import java.io.Writer;
import java.util.Vector;

public interface Document extends Base<lotus.domino.Document>, lotus.domino.Document {
	@Override
	public Item appendItemValue(String name);

	@Override
	public Item appendItemValue(String name, double value);

	@Override
	public Item appendItemValue(String name, int value);

	@Override
	public Item appendItemValue(String name, Object value);

	@Override
	public void attachVCard(lotus.domino.Base document);

	@Override
	public void attachVCard(lotus.domino.Base document, String arg1);

	@Override
	public boolean closeMIMEEntities();

	@Override
	public boolean closeMIMEEntities(boolean savechanges);

	@Override
	public boolean closeMIMEEntities(boolean savechanges, String entityitemname);

	@Override
	public boolean computeWithForm(boolean dodatatypes, boolean raiseerror);

	@Override
	public void convertToMIME();

	@Override
	public void convertToMIME(int conversiontype);

	@Override
	public void convertToMIME(int conversiontype, int options);

	@Override
	public void copyAllItems(lotus.domino.Document doc, boolean replace);

	@Override
	public Item copyItem(lotus.domino.Item item);

	@Override
	public Item copyItem(lotus.domino.Item item, String newName);

	@Override
	public Document copyToDatabase(lotus.domino.Database db);

	@Override
	public MIMEEntity createMIMEEntity();

	@Override
	public MIMEEntity createMIMEEntity(String itemName);

	@Override
	public Document createReplyMessage(boolean toall);

	@Override
	// TODO Switch to new class
	public lotus.domino.RichTextItem createRichTextItem(String name);

	@Override
	public void encrypt();

	@Override
	public String generateXML();

	@Override
	public void generateXML(Object style, lotus.domino.XSLTResultTarget target) throws IOException;

	@Override
	public void generateXML(Writer w) throws IOException;

	@Override
	public EmbeddedObject getAttachment(String fileName);

	@Override
	public Vector<String> getAuthors();

	@Override
	public Vector<Object> getColumnValues();

	@Override
	public DateTime getCreated();

	@SuppressWarnings("unchecked")
	@Override
	public Vector getEmbeddedObjects();

	@SuppressWarnings("unchecked")
	@Override
	public Vector getEncryptionKeys();

	@Override
	public Item getFirstItem(String name);

	@SuppressWarnings("unchecked")
	@Override
	public Vector getFolderReferences();

	@Override
	public int getFTSearchScore();

	@Override
	public String getHttpURL();

	@Override
	public DateTime getInitiallyModified();

	@SuppressWarnings("unchecked")
	@Override
	public Vector getItems();

	@SuppressWarnings("unchecked")
	@Override
	public Vector getItemValue(String name);

	@Override
	public Object getItemValueCustomData(String itemName) throws IOException, ClassNotFoundException;

	@Override
	public Object getItemValueCustomData(String itemName, String dataTypeName) throws IOException, ClassNotFoundException;

	@Override
	public byte[] getItemValueCustomDataBytes(String itemName, String dataTypeName) throws IOException;

	@SuppressWarnings("unchecked")
	@Override
	public Vector getItemValueDateTimeArray(String name);

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

	@SuppressWarnings("unchecked")
	@Override
	public Vector getLockHolders();

	@Override
	public MIMEEntity getMIMEEntity();

	@Override
	public MIMEEntity getMIMEEntity(String itemName);

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
	public boolean getRead(String username);

	@SuppressWarnings("unchecked")
	@Override
	public Vector getReceivedItemText();

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
	public boolean lock(boolean provisionalok);

	@Override
	public boolean lock(String name);

	@Override
	public boolean lock(String name, boolean provisionalok);

	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(Vector names);

	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(Vector names, boolean provisionalok);

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
	public void markRead(String username);

	@Override
	public void markUnread();

	@Override
	public void markUnread(String username);

	@Override
	public void putInFolder(String name);

	@Override
	public void putInFolder(String name, boolean createonfail);

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
	public Item replaceItemValue(String itemName, Object value);

	@Override
	public Item replaceItemValueCustomData(String itemName, Object userObj) throws IOException;

	@Override
	public Item replaceItemValueCustomData(String itemName, String dataTypeName, Object userObj) throws IOException;

	@Override
	public Item replaceItemValueCustomDataBytes(String itemName, String dataTypeName, byte[] byteArray) throws IOException;

	@Override
	public boolean save();

	@Override
	public boolean save(boolean force);

	@Override
	public boolean save(boolean force, boolean makeresponse);

	@Override
	public boolean save(boolean force, boolean makeresponse, boolean markread);

	@Override
	public void send();

	@Override
	public void send(boolean attachform);

	@Override
	public void send(boolean attachform, String recipient);

	@SuppressWarnings("unchecked")
	@Override
	public void send(boolean attachform, Vector recipients);

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
