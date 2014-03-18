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
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import lotus.domino.XSLTResultTarget;

import org.openntf.domino.types.DocumentDescendant;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.Resurrectable;
import org.xml.sax.InputSource;

/**
 * The Interface Item.
 */
public interface Item extends Base<lotus.domino.Item>, lotus.domino.Item, org.openntf.domino.ext.Item, Resurrectable, DocumentDescendant {
	public static enum Flags {
		PROTECTED(16), SUMMARY(1), AUTHORS(4), READERS(8), NAMES(2), SIGNED(32), ENCRYPTED(64);

		public static Flags getFlags(final int value) {
			for (Flags level : Flags.values()) {
				if (level.getValue() == value) {
					return level;
				}
			}
			return null;
		}

		public static int getFlags(final Item item) {
			int result = 0;
			if (item.isSummary())
				result = result | SUMMARY.getValue();
			if (item.isNames())
				result = result | NAMES.getValue();
			if (item.isAuthors())
				result = result | AUTHORS.getValue();
			if (item.isReaders())
				result = result | READERS.getValue();
			if (item.isProtected())
				result = result | PROTECTED.getValue();
			if (item.isSigned())
				result = result | SIGNED.getValue();
			if (item.isEncrypted())
				result = result | ENCRYPTED.getValue();

			return result;
		}

		private final int value_;

		private Flags(final int value) {
			value_ = (int) value;
		}

		public int getValue() {
			return value_;
		}

	}

	public static enum Type {

		ACTIONCD(lotus.domino.Item.ACTIONCD), ASSISTANTINFO(lotus.domino.Item.ASSISTANTINFO), ATTACHMENT(lotus.domino.Item.ATTACHMENT), AUTHORS(
				lotus.domino.Item.AUTHORS), COLLATION(lotus.domino.Item.COLLATION), DATETIMES(lotus.domino.Item.DATETIMES), EMBEDDEDOBJECT(
				lotus.domino.Item.EMBEDDEDOBJECT), ERRORITEM(lotus.domino.Item.ERRORITEM), FORMULA(lotus.domino.Item.FORMULA), HTML(
				lotus.domino.Item.HTML), ICON(lotus.domino.Item.ICON), LSOBJECT(lotus.domino.Item.LSOBJECT), MIME_PART(
				lotus.domino.Item.MIME_PART), NAMES(lotus.domino.Item.NAMES), NOTELINKS(lotus.domino.Item.NOTELINKS), NOTEREFS(
				lotus.domino.Item.NOTEREFS), NUMBERS(lotus.domino.Item.NUMBERS), OTHEROBJECT(lotus.domino.Item.OTHEROBJECT), QUERYCD(
				lotus.domino.Item.QUERYCD), READERS(lotus.domino.Item.READERS), RFC822TEXT(lotus.domino.Item.RFC822TEXT), RICHTEXT(
				lotus.domino.Item.RICHTEXT), SIGNATURE(lotus.domino.Item.SIGNATURE), TEXT(lotus.domino.Item.TEXT), UNAVAILABLE(
				lotus.domino.Item.UNAVAILABLE), UNKNOWN(lotus.domino.Item.UNKNOWN), USERDATA(lotus.domino.Item.USERDATA), USERID(
				lotus.domino.Item.USERID), VIEWMAPDATA(lotus.domino.Item.VIEWMAPDATA), VIEWMAPLAYOUT(lotus.domino.Item.VIEWMAPLAYOUT);

		public static Type getType(final int value) {
			for (Type level : Type.values()) {
				if (level.getValue() == value) {
					return level;
				}
			}
			return null;
		}

		private final int value_;

		private Type(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}
	}

	public static class Schema extends FactorySchema<Item, lotus.domino.Item, Document> {
		@Override
		public Class<Item> typeClass() {
			return Item.class;
		}

		@Override
		public Class<lotus.domino.Item> delegateClass() {
			return lotus.domino.Item.class;
		}

		@Override
		public Class<Document> parentClass() {
			return Document.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#abstractText(int, boolean, boolean)
	 */
	@Override
	public String abstractText(final int maxLen, final boolean dropVowels, final boolean userDict);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#appendToTextList(java.lang.String)
	 */
	@Override
	public void appendToTextList(final String value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#appendToTextList(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void appendToTextList(final Vector values);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(final Object value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#copyItemToDocument(lotus.domino.Document)
	 */
	@Override
	public Item copyItemToDocument(final lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#copyItemToDocument(lotus.domino.Document, java.lang.String)
	 */
	@Override
	public Item copyItemToDocument(final lotus.domino.Document doc, final String newName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getDateTimeValue()
	 */
	@Override
	public DateTime getDateTimeValue();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getInputSource()
	 */
	@Override
	public InputSource getInputSource();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getInputStream()
	 */
	@Override
	public InputStream getInputStream();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getLastModified()
	 */
	@Override
	public DateTime getLastModified();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getMIMEEntity()
	 */
	@Override
	public MIMEEntity getMIMEEntity();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getName()
	 */
	@Override
	public String getName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getParent()
	 */
	@Override
	public Document getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getReader()
	 */
	@Override
	public Reader getReader();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getText()
	 */
	@Override
	public String getText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getText(int)
	 */
	@Override
	public String getText(final int maxLen);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getType()
	 */
	@Override
	public int getType();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getValueCustomData()
	 */
	@Override
	public Object getValueCustomData() throws IOException, ClassNotFoundException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getValueCustomData(java.lang.String)
	 */
	@Override
	public Object getValueCustomData(final String dataTypeName) throws IOException, ClassNotFoundException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getValueCustomDataBytes(java.lang.String)
	 */
	@Override
	public byte[] getValueCustomDataBytes(final String dataTypeName) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getValueDateTimeArray()
	 */
	@Override
	public Vector<DateTime> getValueDateTimeArray();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getValueDouble()
	 */
	@Override
	public double getValueDouble();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getValueInteger()
	 */
	@Override
	public int getValueInteger();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getValueLength()
	 */
	@Override
	public int getValueLength();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getValues()
	 */
	@Override
	public Vector<Object> getValues();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#getValueString()
	 */
	@Override
	public String getValueString();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#isAuthors()
	 */
	@Override
	public boolean isAuthors();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#isEncrypted()
	 */
	@Override
	public boolean isEncrypted();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#isNames()
	 */
	@Override
	public boolean isNames();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#isProtected()
	 */
	@Override
	public boolean isProtected();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#isReaders()
	 */
	@Override
	public boolean isReaders();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#isSaveToDisk()
	 */
	@Override
	public boolean isSaveToDisk();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#isSigned()
	 */
	@Override
	public boolean isSigned();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#isSummary()
	 */
	@Override
	public boolean isSummary();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#parseXML(boolean)
	 */
	@Override
	public org.w3c.dom.Document parseXML(final boolean validate) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#remove()
	 */
	@Override
	public void remove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setAuthors(boolean)
	 */
	@Override
	public void setAuthors(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setDateTimeValue(lotus.domino.DateTime)
	 */
	@Override
	public void setDateTimeValue(final lotus.domino.DateTime dateTime);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setEncrypted(boolean)
	 */
	@Override
	public void setEncrypted(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setNames(boolean)
	 */
	@Override
	public void setNames(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setProtected(boolean)
	 */
	@Override
	public void setProtected(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setReaders(boolean)
	 */
	@Override
	public void setReaders(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setSaveToDisk(boolean)
	 */
	@Override
	public void setSaveToDisk(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setSigned(boolean)
	 */
	@Override
	public void setSigned(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setSummary(boolean)
	 */
	@Override
	public void setSummary(final boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setValueCustomData(java.lang.Object)
	 */
	@Override
	public void setValueCustomData(final Object userObj) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setValueCustomData(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setValueCustomData(final String dataTypeName, final Object userObj) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setValueCustomDataBytes(java.lang.String, byte[])
	 */
	@Override
	public void setValueCustomDataBytes(final String dataTypeName, final byte[] byteArray) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setValueDouble(double)
	 */
	@Override
	public void setValueDouble(final double value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setValueInteger(int)
	 */
	@Override
	public void setValueInteger(final int value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setValues(java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setValues(final Vector values);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#setValueString(java.lang.String)
	 */
	@Override
	public void setValueString(final String value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Item#transformXML(java.lang.Object, lotus.domino.XSLTResultTarget)
	 */
	@Override
	public void transformXML(final Object style, final XSLTResultTarget result);
}
