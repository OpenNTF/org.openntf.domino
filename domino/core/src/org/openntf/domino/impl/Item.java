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
import java.util.EnumSet;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.ExceptionDetails;
import org.openntf.domino.MIMEEntity;
import org.openntf.domino.MIMEHeader;
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
public class Item extends BaseNonThreadSafe<org.openntf.domino.Item, lotus.domino.Item, Document> implements org.openntf.domino.Item {
	private static final Logger log_ = Logger.getLogger(Item.class.getName());

	// TODO NTF - all setters should check to see if the new value is different from the old and only markDirty if there's a change
	private String name_;
	private Type itemType;
	private EnumSet<Flags> flagSet_;

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
	protected Item(final lotus.domino.Item delegate, final Document parent) {
		super(delegate, parent, NOTES_ITEM);
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
	protected Item(final lotus.domino.RichTextItem delegate, final Document parent) {
		super(delegate, parent, NOTES_RTITEM);
		initialize(delegate);
	}

	protected Item(final Document parent, final String name) {
		super(null, parent, NOTES_ITEM);
		name_ = name;
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

	private EnumSet<Flags> getFlagSet() {
		if (flagSet_ == null) {
			flagSet_ = EnumSet.noneOf(Flags.class);
			try {
				if (getDelegate().isAuthors())
					flagSet_.add(Flags.AUTHORS);
				if (getDelegate().isEncrypted())
					flagSet_.add(Flags.ENCRYPTED);
				if (getDelegate().isNames())
					flagSet_.add(Flags.NAMES);
				if (getDelegate().isProtected())
					flagSet_.add(Flags.PROTECTED);
				if (getDelegate().isReaders())
					flagSet_.add(Flags.READERS);
				if (getDelegate().isSigned())
					flagSet_.add(Flags.SIGNED);
				if (getDelegate().isSummary())
					flagSet_.add(Flags.SUMMARY);
			} catch (NotesException ne) {
				DominoUtils.handleException(ne);
			}
		}
		return flagSet_;
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			Vector v = toDominoFriendly(values, getAncestorSession(), recycleThis);
			getDelegate().appendToTextList(v);

		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
			Object domObj = toDominoFriendly(value, getAncestorSession(), recycleThis);
			result = getDelegate().containsValue(domObj);
			Base.s_recycle(domObj);
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getLastModified()
	 */
	@Override
	public Date getLastModifiedDate() {
		try {
			lotus.domino.DateTime dt = getDelegate().getLastModified();
			if (dt == null) {
				return parent.getLastModifiedDate();
			} else {
				java.util.Date jdate = dt.toJavaDate();
				return jdate;
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		return parent.getMIMEEntity(getName());
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
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final Document getParent() {
		return parent;
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#getType()
	 */
	@Override
	@Deprecated
	public int getType() {
		return getTypeEx().getValue();
	}

	@Override
	public Type getTypeEx() {
		try {
			if (itemType == null) {
				itemType = Type.valueOf(getDelegate().getType());
			}
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}

		if (itemType == Type.MIME_PART) {
			MIMEEntity entity = getMIMEEntity();
			if (entity != null) {
				MIMEHeader contentType = entity.getNthHeader("Content-Type");
				String headerval = contentType.getHeaderVal();
				if ("application/x-java-serialized-object".equals(headerval) || "application/x-java-externalized-object".equals(headerval)) {
					itemType = Type.MIME_BEAN;
				}
				parent.closeMIMEEntities(false, getName());
			}
		}

		return itemType;
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
		Vector<Object> values = parent.getItemValue(this.getName());
		if (values != null) {
			return new Vector<Object>(values);
		} else {
			log_.log(Level.WARNING, "Item " + getName() + " in document " + getAncestorDatabase().getApiPath() + ": " + parent.getNoteID()
					+ " is a NULL");
		}
		return null;
	}

	@Override
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
		return getFlagSet().contains(Flags.AUTHORS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#isEncrypted()
	 */
	@Override
	public boolean isEncrypted() {
		return getFlagSet().contains(Flags.ENCRYPTED);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#isNames()
	 */
	@Override
	public boolean isNames() {
		return getFlagSet().contains(Flags.NAMES);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#isProtected()
	 */
	@Override
	public boolean isProtected() {
		return getFlagSet().contains(Flags.PROTECTED);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#isReaders()
	 */
	@Override
	public boolean isReaders() {
		return getFlagSet().contains(Flags.READERS);
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
			DominoUtils.handleException(e, this);
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
		return getFlagSet().contains(Flags.SIGNED);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Item#isSummary()
	 */
	@Override
	public boolean isSummary() {
		return getFlagSet().contains(Flags.SUMMARY);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
				if (getTypeEx() == Type.AUTHORS) {
					return;
				}
				if (!isReadersNamesAuthors()) {
					if (getTypeEx() != Type.TEXT) {
						throw new DataNotCompatibleException("Field " + getName() + " is not Text so cannot be set as an Authors field");
					}
				}
			}
			getDelegate().setAuthors(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
					if (getTypeEx() != Type.TEXT) {
						throw new DataNotCompatibleException("Field " + getName() + " is not Text so cannot be set as an Names field");
					}
				}
			}
			getDelegate().setNames(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
				if (getTypeEx() == Type.READERS) {
					return;
				}
				if (!isReadersNamesAuthors()) {
					if (getTypeEx() != Type.TEXT) {
						throw new DataNotCompatibleException("Field " + getName() + " is not Text so cannot be set as an Readers field");
					}
				}
			}
			getDelegate().setReaders(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
			getDelegate().setSaveToDisk(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
			java.util.Vector v = toDominoFriendly(values, getAncestorSession(), recycleThis);
			getDelegate().setValues(v);
			s_recycle(v);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	public void setValues(final Object value) {
		parent.replaceItemValue(this.getName(), value);
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
			DominoUtils.handleException(e, this);
		}
	}

	@Override
	public void markDirty() {
		itemType = null; // RPr: not sure if the datatype has changed. So to be sure, we set it to zero
		getAncestorDocument().markDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DocumentDescendant#getAncestorDocument()
	 */
	@Override
	public final Document getAncestorDocument() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public final Database getAncestorDatabase() {
		return parent.getAncestorDatabase();
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

	@Override
	protected void resurrect() {
		if (name_ != null) {
			try {
				lotus.domino.Document d = toLotus(getAncestorDocument());
				lotus.domino.Item item = d.getFirstItem(name_);
				setDelegate(item, true);
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
				DominoUtils.handleException(e, this);
			}
		} else {
			if (log_.isLoggable(Level.WARNING)) {
				log_.log(Level.WARNING, getClass().getSimpleName()
						+ " doesn't have name value. Something went terribly wrong. Nothing good can come of this...");
			}
		}
	}

	@Override
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

	@Override
	public boolean isReadersNamesAuthors() {
		return (hasFlag(Flags.NAMES) || hasFlag(Flags.READERS) || hasFlag(Flags.AUTHORS));
	}

	@Override
	public void fillExceptionDetails(final List<ExceptionDetails.Entry> result) {
		parent.fillExceptionDetails(result);
		String myDetail = name_;
		try {
			myDetail = ", Type:" + getDelegate().getType();
		} catch (NotesException e) {
			myDetail += ", [getType -> NotesException: " + e.text + "]";
		}

		try {
			myDetail = ", ValueString:" + getDelegate().getValueString();
		} catch (NotesException e) {
			myDetail += ", [ValueString -> NotesException: " + e.text + "]";
		}
		result.add(new ExceptionDetails.Entry(this, myDetail));
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}
