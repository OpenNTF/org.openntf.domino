package org.openntf.domino;

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

public interface Document extends Base<lotus.domino.Document>, lotus.domino.Document {
	@Override
	public Item appendItemValue(String arg0);

	@Override
	public Item appendItemValue(String arg0, double arg1);

	@Override
	public Item appendItemValue(String arg0, int arg1);

	@Override
	public Item appendItemValue(String arg0, Object arg1);

	@Override
	public void attachVCard(lotus.domino.Base arg0);

	@Override
	public void attachVCard(lotus.domino.Base arg0, String arg1);

	@Override
	public boolean closeMIMEEntities();

	@Override
	public boolean closeMIMEEntities(boolean arg0);

	@Override
	public boolean closeMIMEEntities(boolean arg0, String arg1);

	@Override
	public boolean computeWithForm(boolean arg0, boolean arg1);

	@Override
	public void convertToMIME();

	@Override
	public void convertToMIME(int arg0);

	@Override
	public void convertToMIME(int arg0, int arg1);

	@Override
	public void copyAllItems(lotus.domino.Document arg0, boolean arg1);

	@Override
	public Item copyItem(Item arg0);

	@Override
	public Item copyItem(Item arg0, String arg1);

	@Override
	public lotus.domino.Document copyToDatabase(Database arg0);

	@Override
	public MIMEEntity createMIMEEntity();

	@Override
	public MIMEEntity createMIMEEntity(String arg0);

	@Override
	public lotus.domino.Document createReplyMessage(boolean arg0);

	@Override
	public RichTextItem createRichTextItem(String arg0);

	@Override
	public void encrypt();

	@Override
	public String generateXML();

	@Override
	public void generateXML(Object arg0, XSLTResultTarget arg1) throws IOException, NotesException;

	@Override
	public void generateXML(Writer arg0) throws NotesException, IOException;

	@Override
	public EmbeddedObject getAttachment(String arg0);

	@Override
	public Vector getAuthors();

	@Override
	public Vector getColumnValues();

	@Override
	public DateTime getCreated();

	@Override
	public Vector getEmbeddedObjects();

	@Override
	public Vector getEncryptionKeys();

	@Override
	public Item getFirstItem(String arg0);

	@Override
	public Vector getFolderReferences();

	@Override
	public int getFTSearchScore();

	@Override
	public String getHttpURL();

	@Override
	public DateTime getInitiallyModified();

	@Override
	public Vector getItems();

	@Override
	public Vector getItemValue(String arg0);

	@Override
	public Object getItemValueCustomData(String arg0) throws IOException, ClassNotFoundException, NotesException;

	@Override
	public Object getItemValueCustomData(String arg0, String arg1) throws IOException, ClassNotFoundException, NotesException;

	@Override
	public byte[] getItemValueCustomDataBytes(String arg0, String arg1) throws IOException, NotesException;

	@Override
	public Vector getItemValueDateTimeArray(String arg0);

	@Override
	public double getItemValueDouble(String arg0);

	@Override
	public int getItemValueInteger(String arg0);

	@Override
	public String getItemValueString(String arg0);

	@Override
	public String getKey();

	@Override
	public DateTime getLastAccessed();

	@Override
	public DateTime getLastModified();

	@Override
	public Vector getLockHolders();

	@Override
	public MIMEEntity getMIMEEntity();

	@Override
	public MIMEEntity getMIMEEntity(String arg0);

	@Override
	public String getNameOfProfile();

	@Override
	public String getNoteID();

	@Override
	public String getNotesURL();

	@Override
	public Database getParentDatabase();

	@Override
	public String getParentDocumentUNID();

	@Override
	public View getParentView();

	@Override
	public boolean getRead();

	@Override
	public boolean getRead(String arg0);

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
	public boolean hasItem(String arg0);

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
	public boolean lock(boolean arg0);

	@Override
	public boolean lock(String arg0);

	@Override
	public boolean lock(String arg0, boolean arg1);

	@Override
	public boolean lock(Vector arg0);

	@Override
	public boolean lock(Vector arg0, boolean arg1);

	@Override
	public boolean lockProvisional();

	@Override
	public boolean lockProvisional(String arg0);

	@Override
	public boolean lockProvisional(Vector arg0);

	@Override
	public void makeResponse(lotus.domino.Document arg0);

	@Override
	public void markRead();

	@Override
	public void markRead(String arg0);

	@Override
	public void markUnread();

	@Override
	public void markUnread(String arg0);

	@Override
	public void putInFolder(String arg0);

	@Override
	public void putInFolder(String arg0, boolean arg1);

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public boolean remove(boolean arg0);

	@Override
	public void removeFromFolder(String arg0);

	@Override
	public void removeItem(String arg0);

	@Override
	public boolean removePermanently(boolean arg0);

	@Override
	public boolean renderToRTItem(RichTextItem arg0);

	@Override
	public Item replaceItemValue(String arg0, Object arg1);

	@Override
	public Item replaceItemValueCustomData(String arg0, Object arg1) throws IOException, NotesException;

	@Override
	public Item replaceItemValueCustomData(String arg0, String arg1, Object arg2) throws IOException, NotesException;

	@Override
	public Item replaceItemValueCustomDataBytes(String arg0, String arg1, byte[] arg2) throws IOException, NotesException;

	@Override
	public boolean save();

	@Override
	public boolean save(boolean arg0);

	@Override
	public boolean save(boolean arg0, boolean arg1);

	@Override
	public boolean save(boolean arg0, boolean arg1, boolean arg2);

	@Override
	public void send();

	@Override
	public void send(boolean arg0);

	@Override
	public void send(boolean arg0, String arg1);

	@Override
	public void send(boolean arg0, Vector arg1);

	@Override
	public void send(String arg0);

	@Override
	public void send(Vector arg0);

	@Override
	public void setEncryptionKeys(Vector arg0);

	@Override
	public void setEncryptOnSend(boolean arg0);

	@Override
	public void setPreferJavaDates(boolean arg0);

	@Override
	public void setSaveMessageOnSend(boolean arg0);

	@Override
	public void setSignOnSend(boolean arg0);

	@Override
	public void setUniversalID(String arg0);

	@Override
	public void sign();

	@Override
	public void unlock();
}
