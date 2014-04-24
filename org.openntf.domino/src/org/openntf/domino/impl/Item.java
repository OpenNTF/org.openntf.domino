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
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.MIMEEntity;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.exceptions.DataNotCompatibleException;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.TypeUtils;
import org.xml.sax.InputSource;

// TODO: Auto-generated Javadoc
/**
 * The Class Item.
 */
public class Item extends Base<org.openntf.domino.Item, lotus.domino.Item, Document> implements org.openntf.domino.Item {
	private static final Logger log_ = Logger.getLogger(Item.class.getName());

	// TODO NTF - all setters should check to see if the new value is different from the old and only markDirty if there's a change
	private String name_;
	private Integer dataType_;

	/**
	 * Instantiates a new item.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public Item(final lotus.domino.Item delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, (Document) parent);
		initialize(delegate);
	}

	/**
	 * Instantiates a new outline.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 * @param wf
	 *            the wrapperfactory
	 * @param cppId
	 *            the cpp-id
	 */
	public Item(final lotus.domino.Item delegate, final Document parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_ITEM);
		initialize(delegate);
	}

	/**
	 * Instatiates a new RichtextItem
	 * 
	 * @param delegate
	 * @param parent
	 * @param wf
	 * @param cppId
	 */
	protected Item(final lotus.domino.RichTextItem delegate, final Document parent, final WrapperFactory wf, final long cppId) {
		super(delegate, parent, wf, cppId, NOTES_RTITEM);
		initialize(delegate);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#findParent(lotus.domino.Base)
	 */
	@Override
	protected Document findParent(final lotus.domino.Item delegate) throws NotesException {
		return fromLotus(delegate.getParent(), Document.SCHEMA, null);
	}

	protected void initialize(final lotus.domino.Item delegate) {
		// TODO Auto-generated method stub
		String name;
		try {
			name = delegate.getName();
		} catch (NotesException ne) {
			name = "";
			if (log_.isLoggable(Level.WARNING)) {
				log_.log(Level.WARNING, "Exception trying to get item from Document " + getAncestorDatabase().getFilePath() + " "
						+ getAncestorDocument().getNoteID());
			}
			throw new RuntimeException(ne);
		}
		name_ = name;
	}

	// private void initialize(lotus.domino.Item delegate) {
	//
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#abstractText(int, boolean, boolean)
	 */
	@Override
	public String abstractText(final int maxLen, final boolean dropVowels, final boolean userDict) {
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
	public void appendToTextList(final String value) {
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void appendToTextList(final Vector values) {
		markDirty();
		List<lotus.domino.Base> recycleThis = new ArrayList();
		try {
			Vector v = toDominoFriendly(values, this, recycleThis);
			getDelegate().appendToTextList(v);

		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(final Object value) {
		List<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			boolean result;
			Object domObj = toDominoFriendly(value, this, recycleThis);
			result = getDelegate().containsValue(domObj);
			Base.s_recycle(domObj);
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		} finally {
			s_recycle(recycleThis);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#copyItemToDocument(lotus.domino.Document)
	 */
	@Override
	public org.openntf.domino.Item copyItemToDocument(final lotus.domino.Document lotusDoc) {
		// TODO - mark dirty?
		try {
			if (lotusDoc == null) {
				throw new IllegalArgumentException();
			}

			Document doc = fromLotus(lotusDoc, Document.SCHEMA, null);
			return fromLotus(getDelegate().copyItemToDocument(toLotus(doc)), Item.SCHEMA, doc);
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
	public org.openntf.domino.Item copyItemToDocument(final lotus.domino.Document lotusDoc, final String newName) {
		// TODO - mark dirty?
		try {
			if (lotusDoc == null) {
				throw new IllegalArgumentException();
			}

			Document doc = fromLotus(lotusDoc, Document.SCHEMA, null); // TODO: this is not yet optimal. wrap document with it's session & db
			return fromLotus(getDelegate().copyItemToDocument(toLotus(doc), newName), Item.SCHEMA, doc);
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
		//		System.out.println("Getting DateTime value from item " + getName());
		try {
			lotus.domino.DateTime delegate = getDelegate().getDateTimeValue();
			if (delegate == null) {
				//				System.out.println("Delegate DateTime is null for item " + getName() + " in doc " + getAncestorDocument().getUniversalID());
			}
			return fromLotus(delegate, DateTime.SCHEMA, getAncestorSession());
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
			return fromLotus(getDelegate().getLastModified(), DateTime.SCHEMA, getAncestorSession());
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
	public Date getLastModifiedDate() {
		try {
			lotus.domino.DateTime dt = getDelegate().getLastModified();
			if (dt == null) {
				return getParent().getLastModifiedDate();
			} else {
				java.util.Date jdate = dt.toJavaDate();
				return jdate;
			}
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
		//		System.err.println("GetMIMEEntity in Item");
		return getParent().getMIMEEntity(getName());
		//		try {
		//			return fromLotus(getDelegate().getMIMEEntity(), MIMEEntity.SCHEMA, this.getParent());
		//		} catch (NotesException e) {
		//			DominoUtils.handleException(e);
		//			return null;
		//		}
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
		return getAncestor();
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
	public String getText(final int maxLen) {
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
	public Object getValueCustomData(final String dataTypeName) throws IOException, ClassNotFoundException {
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
	public byte[] getValueCustomDataBytes(final String dataTypeName) throws IOException {
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
		//		System.out.println("Getting DateTimeArray value from item " + getName());
		try {
			return fromLotusAsVector(getDelegate().getValueDateTimeArray(), org.openntf.domino.DateTime.SCHEMA, getAncestorSession());
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
		// Check for null in case there was a problem with the parent's method
		@SuppressWarnings("unchecked")
		Vector<Object> values = this.getParent().getItemValue(this.getName());
		if (values != null) {
			return new Vector<Object>(values);
		} else {
			log_.log(Level.WARNING, "Item " + getName() + " in document " + getAncestorDatabase().getApiPath() + ": "
					+ getParent().getNoteID() + " is a NULL");
		}
		return null;
	}

	public <T> T getValues(final Class<?> T) {
		return TypeUtils.itemValueToClass(this, T);
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
	public org.w3c.dom.Document parseXML(final boolean validate) throws IOException {
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
	public void setAuthors(final boolean flag) {
		markDirty();
		try {
			// Make sure it's a text field!!
			if (flag) {
				if (getType() == AUTHORS) {
					return;
				}
				if (!isReadersNamesAuthors()) {
					if (getType() != TEXT) {
						throw new DataNotCompatibleException("Field " + getName() + " is not Text so cannot be set as an Authors field");
					}
				}
			}
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
	public void setDateTimeValue(final lotus.domino.DateTime dateTime) {
		markDirty();
		@SuppressWarnings("rawtypes")
		List recycleThis = new ArrayList();
		try {
			getDelegate().setDateTimeValue(toLotus(dateTime, recycleThis));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#setEncrypted(boolean)
	 */
	@Override
	public void setEncrypted(final boolean flag) {
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
	public void setNames(final boolean flag) {
		markDirty();
		try {
			// Make sure it's a text field!!
			if (flag) {
				if (hasFlag(Flags.NAMES)) {
					return;
				}
				if (!isReadersNamesAuthors()) {
					if (getType() != TEXT) {
						throw new DataNotCompatibleException("Field " + getName() + " is not Text so cannot be set as an Names field");
					}
				}
			}
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
	public void setProtected(final boolean flag) {
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
	public void setReaders(final boolean flag) {
		markDirty();
		try {
			// Make sure it's a text field!!
			if (flag) {
				if (getType() == READERS) {
					return;
				}
				if (!isReadersNamesAuthors()) {
					if (getType() != TEXT) {
						throw new DataNotCompatibleException("Field " + getName() + " is not Text so cannot be set as an Readers field");
					}
				}
			}
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
	public void setSaveToDisk(final boolean flag) {
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
	public void setSigned(final boolean flag) {
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
	public void setSummary(final boolean flag) {
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
	public void setValueCustomData(final Object userObj) throws IOException {
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
	public void setValueCustomData(final String dataTypeName, final Object userObj) throws IOException {
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
	public void setValueCustomDataBytes(final String dataTypeName, final byte[] byteArray) throws IOException {
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
	public void setValueDouble(final double value) {
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
	public void setValueInteger(final int value) {
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
	public void setValueString(final String value) {
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
	@SuppressWarnings("rawtypes")
	@Override
	public void setValues(final java.util.Vector values) {
		markDirty();
		List<lotus.domino.Base> recycleThis = new ArrayList<lotus.domino.Base>();
		try {
			java.util.Vector v = toDominoFriendly(values, this, recycleThis);
			getDelegate().setValues(v);
			s_recycle(v);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	public void setValues(final Object value) {
		this.getParent().replaceItemValue(this.getName(), value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#transformXML(java.lang.Object, lotus.domino.XSLTResultTarget)
	 */
	@Override
	public void transformXML(final Object style, final lotus.domino.XSLTResultTarget result) {
		try {
			getDelegate().transformXML(style, result);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	void markDirty() {
		getAncestorDocument().markDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DocumentDescendant#getAncestorDocument()
	 */
	@Override
	public Document getAncestorDocument() {
		return this.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public Database getAncestorDatabase() {
		return this.getParent().getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getParent().getAncestorSession();
	}

	@Override
	protected lotus.domino.Item getDelegate() {
		lotus.domino.Item item = super.getDelegate();
		try {
			item.isEncrypted();
		} catch (NotesException recycleSucks) {
			resurrect();
		}
		return super.getDelegate();
	}

	private void resurrect() {
		if (name_ != null) {
			try {
				lotus.domino.Document d = toLotus(getAncestorDocument());
				lotus.domino.Item item = d.getFirstItem(name_);
				setDelegate(item, 0);
				if (log_.isLoggable(Level.INFO)) {
					log_.log(Level.INFO, "Item " + name_ + " in document path " + getAncestorDocument().getNoteID()
							+ " had been recycled and was auto-restored. Changes may have been lost.");
					log_.log(Level.FINE,
							"If you recently rollbacked a transaction and this document was included in the rollback, this outcome is normal.");
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
				}
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
		} else {
			if (log_.isLoggable(Level.WARNING)) {
				log_.log(Level.WARNING, getClass().getSimpleName()
						+ " doesn't have name value. Something went terribly wrong. Nothing good can come of this...");
			}
		}
	}

	public boolean hasFlag(final org.openntf.domino.Item.Flags flag) {
		switch (flag) {
		case PROTECTED:
			return isProtected();
		case AUTHORS:
			return isAuthors();
		case ENCRYPTED:
			return isEncrypted();
		case NAMES:
			return isNames();
		case READERS:
			return isReaders();
		case SIGNED:
			return isSigned();
		case SUMMARY:
			return isSummary();
		default:
			break;
		}
		return false;
	}

	public boolean isReadersNamesAuthors() {
		return (hasFlag(Flags.NAMES) || hasFlag(Flags.READERS) || hasFlag(Flags.AUTHORS));
	}

}
