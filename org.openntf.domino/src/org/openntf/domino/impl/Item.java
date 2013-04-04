/*
 * Copyright OpenNTF 2013
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
import java.io.InputStream;
import java.io.Reader;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.xml.sax.InputSource;

// TODO: Auto-generated Javadoc
/**
 * The Class Item.
 */
public class Item extends Base<org.openntf.domino.Item, lotus.domino.Item> implements org.openntf.domino.Item {
	// TODO NTF - all setters should check to see if the new value is different from the old and only markDirty if there's a change

	/**
	 * Instantiates a new item.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public Item(lotus.domino.Item delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#abstractText(int, boolean, boolean)
	 */
	@Override
	public String abstractText(int maxLen, boolean dropVowels, boolean userDict) {
		try {
			return getDelegate().abstractText(maxLen, dropVowels, userDict);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#appendToTextList(java.lang.String)
	 */
	@Override
	public void appendToTextList(String value) {
		markDirty();
		try {
			getDelegate().appendToTextList(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#appendToTextList(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void appendToTextList(java.util.Vector values) {
		markDirty();
		try {
			getDelegate().appendToTextList(toDominoFriendly(values, this));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value) {
		try {
			return getDelegate().containsValue(toDominoFriendly(value, this));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#copyItemToDocument(lotus.domino.Document)
	 */
	@Override
	public Item copyItemToDocument(lotus.domino.Document doc) {
		// TODO - mark dirty?
		try {
			return Factory.fromLotus(getDelegate().copyItemToDocument((lotus.domino.Document) toLotus(doc)), Item.class,
					(org.openntf.domino.Document) doc);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#copyItemToDocument(lotus.domino.Document, java.lang.String)
	 */
	@Override
	public Item copyItemToDocument(lotus.domino.Document doc, String newName) {
		// TODO - mark dirty?
		try {
			return Factory.fromLotus(getDelegate().copyItemToDocument((lotus.domino.Document) toLotus(doc), newName), Item.class,
					(org.openntf.domino.Document) doc);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getDateTimeValue()
	 */
	@Override
	public DateTime getDateTimeValue() {
		try {
			return Factory.fromLotus(getDelegate().getDateTimeValue(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getInputSource()
	 */
	@Override
	public InputSource getInputSource() {
		try {
			return getDelegate().getInputSource();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		try {
			return getDelegate().getInputStream();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getLastModified()
	 */
	@Override
	public DateTime getLastModified() {
		try {
			return Factory.fromLotus(getDelegate().getLastModified(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getMIMEEntity()
	 */
	@Override
	public MIMEEntity getMIMEEntity() {
		try {
			return Factory.fromLotus(getDelegate().getMIMEEntity(), MIMEEntity.class, this.getParent());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getName()
	 */
	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Document getParent() {
		return (Document) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getReader()
	 */
	@Override
	public Reader getReader() {
		try {
			return getDelegate().getReader();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getText()
	 */
	@Override
	public String getText() {
		try {
			return getDelegate().getText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getText(int)
	 */
	@Override
	public String getText(int maxLen) {
		try {
			return getDelegate().getText(maxLen);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getType()
	 */
	@Override
	public int getType() {
		try {
			return getDelegate().getType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getValueCustomData()
	 */
	@Override
	public Object getValueCustomData() throws IOException, ClassNotFoundException {
		try {
			return getDelegate().getValueCustomData();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getValueCustomData(java.lang.String)
	 */
	@Override
	public Object getValueCustomData(String dataTypeName) throws IOException, ClassNotFoundException {
		try {
			return getDelegate().getValueCustomData(dataTypeName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getValueCustomDataBytes(java.lang.String)
	 */
	@Override
	public byte[] getValueCustomDataBytes(String dataTypeName) throws IOException {
		try {
			return getDelegate().getValueCustomDataBytes(dataTypeName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getValueDateTimeArray()
	 */
	@Override
	public Vector<org.openntf.domino.DateTime> getValueDateTimeArray() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getValueDateTimeArray(), org.openntf.domino.DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getValueDouble()
	 */
	@Override
	public double getValueDouble() {
		try {
			return getDelegate().getValueDouble();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0d;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getValueInteger()
	 */
	@Override
	public int getValueInteger() {
		try {
			return getDelegate().getValueInteger();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getValueLength()
	 */
	@Override
	public int getValueLength() {
		try {
			return getDelegate().getValueLength();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getValueString()
	 */
	@Override
	public String getValueString() {
		try {
			return getDelegate().getValueString();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getValues()
	 */
	@Override
	public Vector<Object> getValues() {
		// Just use the parent Doc for this, since it understands MIMEBean
		return new Vector<Object>(this.getParent().getItemValue(this.getName()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#isAuthors()
	 */
	@Override
	public boolean isAuthors() {
		try {
			return getDelegate().isAuthors();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#isEncrypted()
	 */
	@Override
	public boolean isEncrypted() {
		try {
			return getDelegate().isEncrypted();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#isNames()
	 */
	@Override
	public boolean isNames() {
		try {
			return getDelegate().isNames();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#isProtected()
	 */
	@Override
	public boolean isProtected() {
		try {
			return getDelegate().isProtected();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#isReaders()
	 */
	@Override
	public boolean isReaders() {
		try {
			return getDelegate().isReaders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#isSaveToDisk()
	 */
	@Override
	public boolean isSaveToDisk() {
		try {
			return getDelegate().isSaveToDisk();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#isSigned()
	 */
	@Override
	public boolean isSigned() {
		try {
			return getDelegate().isSigned();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#isSummary()
	 */
	@Override
	public boolean isSummary() {
		try {
			return getDelegate().isSummary();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#parseXML(boolean)
	 */
	@Override
	public org.w3c.dom.Document parseXML(boolean validate) throws IOException {
		try {
			return getDelegate().parseXML(validate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#remove()
	 */
	@Override
	public void remove() {
		markDirty();
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setAuthors(boolean)
	 */
	@Override
	public void setAuthors(boolean flag) {
		markDirty();
		try {
			getDelegate().setAuthors(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setDateTimeValue(lotus.domino.DateTime)
	 */
	@Override
	public void setDateTimeValue(lotus.domino.DateTime dateTime) {
		markDirty();
		try {
			getDelegate().setDateTimeValue((lotus.domino.DateTime) toLotus(dateTime));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setEncrypted(boolean)
	 */
	@Override
	public void setEncrypted(boolean flag) {
		markDirty();
		try {
			getDelegate().setEncrypted(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setNames(boolean)
	 */
	@Override
	public void setNames(boolean flag) {
		markDirty();
		try {
			getDelegate().setNames(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setProtected(boolean)
	 */
	@Override
	public void setProtected(boolean flag) {
		markDirty();
		try {
			getDelegate().setProtected(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setReaders(boolean)
	 */
	@Override
	public void setReaders(boolean flag) {
		markDirty();
		try {
			getDelegate().setReaders(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setSaveToDisk(boolean)
	 */
	@Override
	public void setSaveToDisk(boolean flag) {
		markDirty();
		try {
			getDelegate().setReaders(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setSigned(boolean)
	 */
	@Override
	public void setSigned(boolean flag) {
		markDirty();
		try {
			getDelegate().setSigned(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setSummary(boolean)
	 */
	@Override
	public void setSummary(boolean flag) {
		markDirty();
		try {
			getDelegate().setSummary(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setValueCustomData(java.lang.Object)
	 */
	@Override
	public void setValueCustomData(Object userObj) throws IOException {
		markDirty();
		try {
			getDelegate().setValueCustomData(userObj);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setValueCustomData(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setValueCustomData(String dataTypeName, Object userObj) throws IOException {
		markDirty();
		try {
			getDelegate().setValueCustomData(dataTypeName, userObj);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setValueCustomDataBytes(java.lang.String, byte[])
	 */
	@Override
	public void setValueCustomDataBytes(String dataTypeName, byte[] byteArray) throws IOException {
		markDirty();
		try {
			getDelegate().setValueCustomData(dataTypeName, byteArray);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setValueDouble(double)
	 */
	@Override
	public void setValueDouble(double value) {
		markDirty();
		try {
			getDelegate().setValueDouble(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setValueInteger(int)
	 */
	@Override
	public void setValueInteger(int value) {
		markDirty();
		try {
			getDelegate().setValueInteger(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setValueString(java.lang.String)
	 */
	@Override
	public void setValueString(String value) {
		markDirty();
		try {
			getDelegate().setValueString(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setValues(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setValues(java.util.Vector values) {
		markDirty();
		try {
			getDelegate().setValues(toDominoFriendly(values, this));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#transformXML(java.lang.Object, lotus.domino.XSLTResultTarget)
	 */
	@Override
	public void transformXML(Object style, lotus.domino.XSLTResultTarget result) {
		try {
			getDelegate().transformXML(style, result);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public Document getParentDocument() {
		return getParent();
	}

	public org.openntf.domino.Database getParentDatabase() {
		return getParentDocument().getParentDatabase();
	}

	void markDirty() {
		getParentDocument().markDirty();
	}

}
