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
package org.openntf.domino;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Vector;

import org.openntf.domino.types.DatabaseDescendant;
import org.openntf.domino.types.FactorySchema;

/**
 * The Interface Document.
 */
public interface Document extends Base<lotus.domino.Document>, lotus.domino.Document, org.openntf.domino.ext.Document, DatabaseDescendant,
		Map<String, Object> {

	public static class Schema extends FactorySchema<Document, lotus.domino.Document, Database> {
		@Override
		public Class<Document> typeClass() {
			return Document.class;
		}

		@Override
		public Class<lotus.domino.Document> delegateClass() {
			return lotus.domino.Document.class;
		}

		@Override
		public Class<Database> parentClass() {
			return Database.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#appendItemValue(java.lang.String)
	 */
	@Override
	public Item appendItemValue(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#appendItemValue(java.lang.String, double)
	 */
	@Override
	public Item appendItemValue(final String name, final double value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#appendItemValue(java.lang.String, int)
	 */
	@Override
	public Item appendItemValue(final String name, final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#appendItemValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public Item appendItemValue(final String name, final Object value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#attachVCard(lotus.domino.Base)
	 */
	@Override
	public void attachVCard(final lotus.domino.Base document);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#attachVCard(lotus.domino.Base, java.lang.String)
	 */
	@Override
	public void attachVCard(final lotus.domino.Base document, final String arg1);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#closeMIMEEntities()
	 */
	@Override
	public boolean closeMIMEEntities();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#closeMIMEEntities(boolean)
	 */
	@Override
	public boolean closeMIMEEntities(final boolean savechanges);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#closeMIMEEntities(boolean, java.lang.String)
	 */
	@Override
	public boolean closeMIMEEntities(final boolean savechanges, final String entityitemname);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#computeWithForm(boolean, boolean)
	 */
	@Override
	public boolean computeWithForm(final boolean dodatatypes, final boolean raiseerror);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#convertToMIME()
	 */
	@Override
	public void convertToMIME();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#convertToMIME(int)
	 */
	@Override
	public void convertToMIME(final int conversiontype);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#convertToMIME(int, int)
	 */
	@Override
	public void convertToMIME(final int conversiontype, final int options);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#copyAllItems(lotus.domino.Document, boolean)
	 */
	@Override
	public void copyAllItems(final lotus.domino.Document doc, final boolean replace);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#copyItem(lotus.domino.Item)
	 */
	@Override
	public Item copyItem(final lotus.domino.Item item);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#copyItem(lotus.domino.Item, java.lang.String)
	 */
	@Override
	public Item copyItem(final lotus.domino.Item item, final String newName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#copyToDatabase(lotus.domino.Database)
	 */
	@Override
	public Document copyToDatabase(final lotus.domino.Database db);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#createMIMEEntity()
	 */
	@Override
	public MIMEEntity createMIMEEntity();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#createMIMEEntity(java.lang.String)
	 */
	@Override
	public MIMEEntity createMIMEEntity(final String itemName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#createReplyMessage(boolean)
	 */
	@Override
	public Document createReplyMessage(final boolean toall);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#createRichTextItem(java.lang.String)
	 */
	@Override
	public RichTextItem createRichTextItem(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#encrypt()
	 */
	@Override
	public void encrypt();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#generateXML()
	 */
	@Override
	public String generateXML();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#generateXML(java.lang.Object, lotus.domino.XSLTResultTarget)
	 */
	@Override
	public void generateXML(final Object style, final lotus.domino.XSLTResultTarget target) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#generateXML(java.io.Writer)
	 */
	@Override
	public void generateXML(final Writer w) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getAttachment(java.lang.String)
	 */
	@Override
	public EmbeddedObject getAttachment(final String fileName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getAuthors()
	 */
	@Override
	public Vector<String> getAuthors();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getColumnValues()
	 */
	@Override
	public Vector<Object> getColumnValues();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getCreated()
	 */
	@Override
	public DateTime getCreated();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getEmbeddedObjects()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Vector getEmbeddedObjects();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getEncryptionKeys()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Vector getEncryptionKeys();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getFirstItem(java.lang.String)
	 */
	@Override
	public Item getFirstItem(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getFolderReferences()
	 */
	@SuppressWarnings("rawtypes")
	public Vector getFolderReferences();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getFTSearchScore()
	 */
	@Override
	public int getFTSearchScore();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getHttpURL()
	 */
	@Override
	public String getHttpURL();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getInitiallyModified()
	 */
	@Override
	public DateTime getInitiallyModified();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getItems()
	 */
	// @SuppressWarnings("unchecked")
	@Override
	public Vector<Item> getItems();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getItemValue(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Vector getItemValue(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getItemValueCustomData(java.lang.String)
	 */
	@Override
	public Object getItemValueCustomData(final String itemName) throws IOException, ClassNotFoundException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getItemValueCustomData(java.lang.String, java.lang.String)
	 */
	@Override
	public Object getItemValueCustomData(final String itemName, final String dataTypeName) throws IOException, ClassNotFoundException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getItemValueCustomDataBytes(java.lang.String, java.lang.String)
	 */
	@Override
	public byte[] getItemValueCustomDataBytes(final String itemName, final String dataTypeName) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getItemValueDateTimeArray(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Vector getItemValueDateTimeArray(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getItemValueDouble(java.lang.String)
	 */
	@Override
	public double getItemValueDouble(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getItemValueInteger(java.lang.String)
	 */
	@Override
	public int getItemValueInteger(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getItemValueString(java.lang.String)
	 */
	@Override
	public String getItemValueString(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getKey()
	 */
	@Override
	public String getKey();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getLastAccessed()
	 */
	@Override
	public DateTime getLastAccessed();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getLastModified()
	 */
	@Override
	public DateTime getLastModified();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getLockHolders()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Vector getLockHolders();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getMIMEEntity()
	 */
	@Override
	public MIMEEntity getMIMEEntity();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getMIMEEntity(java.lang.String)
	 */
	@Override
	public MIMEEntity getMIMEEntity(final String itemName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getNameOfProfile()
	 */
	@Override
	public String getNameOfProfile();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getNoteID()
	 */
	@Override
	public String getNoteID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getNotesURL()
	 */
	@Override
	public String getNotesURL();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getParentDatabase()
	 */
	@Override
	public org.openntf.domino.Database getParentDatabase();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getParentDocumentUNID()
	 */
	@Override
	public String getParentDocumentUNID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getParentView()
	 */
	@Override
	public View getParentView();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getRead()
	 */
	@Override
	public boolean getRead();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getRead(java.lang.String)
	 */
	@Override
	public boolean getRead(final String username);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getReceivedItemText()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Vector getReceivedItemText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getResponses()
	 */
	@Override
	public DocumentCollection getResponses();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getSigner()
	 */
	@Override
	public String getSigner();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getSize()
	 */
	@Override
	public int getSize();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getUniversalID()
	 */
	@Override
	public String getUniversalID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getURL()
	 */
	@Override
	public String getURL();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#getVerifier()
	 */
	@Override
	public String getVerifier();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#hasEmbedded()
	 */
	@Override
	public boolean hasEmbedded();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#hasItem(java.lang.String)
	 */
	@Override
	public boolean hasItem(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#isDeleted()
	 */
	@Override
	public boolean isDeleted();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#isEncrypted()
	 */
	@Override
	public boolean isEncrypted();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#isEncryptOnSend()
	 */
	@Override
	public boolean isEncryptOnSend();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#isNewNote()
	 */
	@Override
	public boolean isNewNote();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#isPreferJavaDates()
	 */
	@Override
	public boolean isPreferJavaDates();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#isProfile()
	 */
	@Override
	public boolean isProfile();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#isResponse()
	 */
	@Override
	public boolean isResponse();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#isSaveMessageOnSend()
	 */
	@Override
	public boolean isSaveMessageOnSend();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#isSentByAgent()
	 */
	@Override
	public boolean isSentByAgent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#isSigned()
	 */
	@Override
	public boolean isSigned();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#isSignOnSend()
	 */
	@Override
	public boolean isSignOnSend();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#isValid()
	 */
	@Override
	public boolean isValid();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#lock()
	 */
	@Override
	public boolean lock();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#lock(boolean)
	 */
	@Override
	public boolean lock(final boolean provisionalok);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#lock(java.lang.String)
	 */
	@Override
	public boolean lock(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#lock(java.lang.String, boolean)
	 */
	@Override
	public boolean lock(final String name, final boolean provisionalok);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#lock(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#lock(java.util.Vector, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lock(final Vector names, final boolean provisionalok);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#lockProvisional()
	 */
	@Override
	public boolean lockProvisional();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#lockProvisional(java.lang.String)
	 */
	@Override
	public boolean lockProvisional(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#lockProvisional(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean lockProvisional(final Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#makeResponse(lotus.domino.Document)
	 */
	@Override
	public void makeResponse(final lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#markRead()
	 */
	@Override
	public void markRead();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#markRead(java.lang.String)
	 */
	@Override
	public void markRead(final String username);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#markUnread()
	 */
	@Override
	public void markUnread();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#markUnread(java.lang.String)
	 */
	@Override
	public void markUnread(final String username);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#putInFolder(java.lang.String)
	 */
	@Override
	public void putInFolder(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#putInFolder(java.lang.String, boolean)
	 */
	@Override
	public void putInFolder(final String name, final boolean createonfail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#remove(boolean)
	 */
	@Override
	public boolean remove(final boolean force);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#removeFromFolder(java.lang.String)
	 */
	@Override
	public void removeFromFolder(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#removeItem(java.lang.String)
	 */
	@Override
	public void removeItem(final String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#removePermanently(boolean)
	 */
	@Override
	public boolean removePermanently(final boolean force);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#renderToRTItem(lotus.domino.RichTextItem)
	 */
	@Override
	public boolean renderToRTItem(final lotus.domino.RichTextItem rtitem);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#replaceItemValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public Item replaceItemValue(final String itemName, final Object value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#replaceItemValueCustomData(java.lang.String, java.lang.Object)
	 */
	@Override
	public Item replaceItemValueCustomData(final String itemName, final Object userObj) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#replaceItemValueCustomData(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public Item replaceItemValueCustomData(final String itemName, final String dataTypeName, final Object userObj) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#replaceItemValueCustomDataBytes(java.lang.String, java.lang.String, byte[])
	 */
	@Override
	public Item replaceItemValueCustomDataBytes(final String itemName, final String dataTypeName, final byte[] byteArray)
			throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#save()
	 */
	@Override
	public boolean save();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#save(boolean)
	 */
	@Override
	public boolean save(final boolean force);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#save(boolean, boolean)
	 */
	@Override
	public boolean save(final boolean force, final boolean makeresponse);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#save(boolean, boolean, boolean)
	 */
	@Override
	public boolean save(final boolean force, final boolean makeresponse, final boolean markread);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#send()
	 */
	@Override
	public void send();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#send(boolean)
	 */
	@Override
	public void send(final boolean attachform);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#send(boolean, java.lang.String)
	 */
	@Override
	public void send(final boolean attachform, final String recipient);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#send(boolean, java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void send(final boolean attachform, final Vector recipients);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#send(java.lang.String)
	 */
	@Override
	public void send(final String recipient);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#send(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void send(final Vector recipients);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#setEncryptionKeys(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setEncryptionKeys(final Vector keys);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#setEncryptOnSend(boolean)
	 */
	@Override
	public void setEncryptOnSend(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#setPreferJavaDates(boolean)
	 */
	@Override
	public void setPreferJavaDates(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#setSaveMessageOnSend(boolean)
	 */
	@Override
	public void setSaveMessageOnSend(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#setSignOnSend(boolean)
	 */
	@Override
	public void setSignOnSend(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#setUniversalID(java.lang.String)
	 */
	@Override
	public void setUniversalID(final String unid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#sign()
	 */
	@Override
	public void sign();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Document#unlock()
	 */
	@Override
	public void unlock();

	/**
	 * Replaces the itemvalue in a document.
	 * 
	 * @param itemName
	 * @param value
	 * @param isSummary
	 * @param autoBox
	 * @param returnItem
	 * @return
	 */
	Item replaceItemValue(String itemName, Object value, Boolean isSummary, final boolean boxCompatibleOnly, boolean returnItem);

	public void markDirty();
}
