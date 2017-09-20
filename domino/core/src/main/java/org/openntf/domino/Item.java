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

import org.openntf.domino.exceptions.DataNotCompatibleException;
import org.openntf.domino.types.DocumentDescendant;
import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.Resurrectable;
import org.xml.sax.InputSource;

/**
 * Represents a discrete value or set of values in a document.
 * <p>
 * The client interface displays items in a document through fields on a form. When a field on a form and an item in a document have the
 * same name, the field displays the item (for example, the Subject field displays the Subject item).
 * </p>
 *
 * <p>
 * All items in a document are accessible programmatically, regardless of what form is used to display the document in the user interface.
 * </p>
 * <p>
 * <h3>Notable enhancements and changes</h3>
 * <ul>
 * <li>Use {@link org.openntf.domino.ext.Item#getValues(Class)} to get values of the item directly cast to a required class</li>
 * <li>Use {@link org.openntf.domino.ext.Item#isReadersNamesAuthors()} to check if the item is either a Names, Authors or Readers type</li>
 * </ul>
 * </p>
 * <h3>Creation</h3>
 * <p>
 * To create a new <code>Item</code> object:
 * </p>
 * <ul>
 * <li>To create a new <code>Item</code> object from scratch, use {@link Document#replaceItemValue(String, Object)}. The method
 * {@link Document#appendItemValue(String)} also creates an item, but creates another item of the same name if the specified item exists.
 * Use <code>replaceItemValue</code> unless your intent is to create another item with the same name (not recommended).</li>
 * <li>To create a new <code>Item</code> object from one that already exists, use {@link #copyItemToDocument(lotus.domino.Document)},
 * {@link Document#copyItem(lotus.domino.Item)}, or {@link Document#replaceItemValue(String, Object)}.</li>
 * </ul>
 * <p>
 * You must call <code>save</code> on the document if you want the modified document to be saved to disk. The document won't display the new
 * item in the user interface unless there is a field of the same name on the form used to display the document.
 * </p>
 *
 * <p>
 * Explicitly call {@link #setSummary(boolean)} and specify true if you want the value to be displayed in a view or folder.
 * </p>
 * <h3>Access</h3>
 * <p>
 * To access an existing <code>Item</code> object:
 * </p>
 * <ul>
 * <li>To access an item when you know its name, use {@link Document#getFirstItem(String)}.</li>
 * <li>To access all the items in a document, use {@link Document#getItems()}.</li>
 * </ul>
 * <p>
 * Document has methods to access items without creating an <code>Item</code> object. You need to know the name of the item.
 * </p>
 * <ul>
 * <li>To get an item's value, use {@link Document#getItemValue(String)}.</li>
 * <li>To create a new item or set an item's value, use {@link Document#replaceItemValue(String, Object)}.</li>
 * <li>To check for the existence of a particular item in a document, use {@link Document#hasItem(String)}.</li>
 * <li>To delete an item from a document, use {@link Document#removeItem(String)}.</li>
 * </ul>
 * <h3>Rich-text items</h3>
 * <p>
 * {@link RichTextItem} inherits the properties and methods of <code>Item</code> and has additional properties and methods you can use to
 * manipulate rich text.
 * </p>
 *
 * <h3>Saving Changes</h3>
 * <p>
 * After you create or modify an item, you must save the changes by calling the parent document's <code>save</code> method.
 * </p>
 *
 * <p>
 * If you don't call <code>save</code> before the program finishes, all of your changes to an <code>Item</code> object are lost.
 * </p>
 *
 * @see org.openntf.domino.ext.Item
 */
public interface Item extends Base<lotus.domino.Item>, lotus.domino.Item, org.openntf.domino.ext.Item, Resurrectable, DocumentDescendant,
		ExceptionDetails {
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
			if (item.isSummary()) {
				result = result | SUMMARY.getValue();
			}
			if (item.isNames()) {
				result = result | NAMES.getValue();
			}
			if (item.isAuthors()) {
				result = result | AUTHORS.getValue();
			}
			if (item.isReaders()) {
				result = result | READERS.getValue();
			}
			if (item.isProtected()) {
				result = result | PROTECTED.getValue();
			}
			if (item.isSigned()) {
				result = result | SIGNED.getValue();
			}
			if (item.isEncrypted()) {
				result = result | ENCRYPTED.getValue();
			}

			return result;
		}

		private final int value_;

		private Flags(final int value) {
			value_ = value;
		}

		public int getValue() {
			return value_;
		}

	}

	public static enum Type {

		ACTIONCD(lotus.domino.Item.ACTIONCD), ASSISTANTINFO(lotus.domino.Item.ASSISTANTINFO), ATTACHMENT(lotus.domino.Item.ATTACHMENT),
		AUTHORS(lotus.domino.Item.AUTHORS), COLLATION(lotus.domino.Item.COLLATION), DATETIMES(lotus.domino.Item.DATETIMES),
		EMBEDDEDOBJECT(lotus.domino.Item.EMBEDDEDOBJECT), ERRORITEM(lotus.domino.Item.ERRORITEM), FORMULA(lotus.domino.Item.FORMULA),
		HTML(lotus.domino.Item.HTML), ICON(lotus.domino.Item.ICON), LSOBJECT(lotus.domino.Item.LSOBJECT),
		MIME_PART(lotus.domino.Item.MIME_PART), NAMES(lotus.domino.Item.NAMES), NOTELINKS(lotus.domino.Item.NOTELINKS),
		NOTEREFS(lotus.domino.Item.NOTEREFS), NUMBERS(lotus.domino.Item.NUMBERS), OTHEROBJECT(lotus.domino.Item.OTHEROBJECT),
		QUERYCD(lotus.domino.Item.QUERYCD), READERS(lotus.domino.Item.READERS), RFC822TEXT(lotus.domino.Item.RFC822TEXT),
		RICHTEXT(lotus.domino.Item.RICHTEXT), SIGNATURE(lotus.domino.Item.SIGNATURE), TEXT(lotus.domino.Item.TEXT),
		UNAVAILABLE(lotus.domino.Item.UNAVAILABLE), UNKNOWN(lotus.domino.Item.UNKNOWN), USERDATA(lotus.domino.Item.USERDATA),
		USERID(lotus.domino.Item.USERID), VIEWMAPDATA(lotus.domino.Item.VIEWMAPDATA), VIEWMAPLAYOUT(lotus.domino.Item.VIEWMAPLAYOUT),
		// Unfortunately, the XPage DominoDocument cannot handle serialized MIME beans correctly, so we will return a custom datatype of 10001
		MIME_BEAN(10001);

		/**
		 * @Deprecated better use valueOf
		 */
		@Deprecated
		public static Type getType(final int value) {
			return valueOf(value);
		}

		/**
		 * Return the {@link Item.Type} of a numeric value
		 *
		 * @param value
		 *            the numeric value
		 * @return a {@link Item.Type} Object
		 */
		public static Type valueOf(final int value) {
			for (Type level : Type.values()) {
				if (level.getValue() == value) {
					return level;
				}
			}
			return Type.UNKNOWN;
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

	/**
	 * Abbreviates the contents of a text item.
	 *
	 * @param maxLen
	 *            The maximum length of the abbreviation.
	 * @param dropVowels
	 *            Specify true if you want to drop vowels from the words in the item. Otherwise, specify false.
	 * @param userDict
	 *            Specify true if you want to use the table of abbreviations in NOTEABBR.TXT. Otherwise, specify false.
	 * @return The contents of the item, with vowels dropped and abbreviations substituted (if specified), then truncated to fit to maxlen.
	 */
	@Override
	public String abstractText(final int maxLen, final boolean dropVowels, final boolean userDict);

	/**
	 * For an item that's a text list, adds a new value to the item without erasing any existing values.
	 * <p>
	 * Note:Text lists have an upper limit of 64K. If appending a new value to an existing text list would result in a text list greater
	 * than 64K, the new value will not be appended.
	 * </p>
	 *
	 * @param value
	 *            The string you want to add to the item.
	 */
	@Override
	public void appendToTextList(final String value);

	/**
	 * For an item that's a text list, adds a new value to the item without erasing any existing values.
	 * <p>
	 * Note:Text lists have an upper limit of 64K. If appending a new value to an existing text list would result in a text list greater
	 * than 64K, the new value will not be appended.
	 * </p>
	 *
	 * @param values
	 *            The string(s) you want to add to the item. Each vector element is an object of type String.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void appendToTextList(final Vector values);

	/**
	 * Checks whether a value matches at least one of an item's values.
	 * <p>
	 * This method <em>does not</em> search a text item to see if it contains a specific word. It is intended to search a text list, number
	 * list, or date-time range to see if one of the values in the list or range matches <em>value.</em>
	 * </p>
	 * <p>
	 * If <em>value</em> is a distinguished name, and if the item contains Domino user names, the distinguished name matches the common
	 * version of the hierarchical name.
	 * </p>
	 *
	 * @param value
	 *            String, Number, or DateTime value
	 * @return true if the value matches one of the values in the item. false if the value matches no value in the item
	 */
	@Override
	public boolean containsValue(final Object value);

	/**
	 * Copies an item to a specified document.
	 * <p>
	 * When you call this method using a {@link RichTextItem} object, file attachments, embedded objects, and object links that are
	 * contained within the rich-text item are <em>not </em>copied to the destination document.
	 * </p>
	 *
	 * @param doc
	 *            The document on which to create the item. Cannot be null.
	 *
	 * @return The new item.
	 * @throws IllegalArgumentException
	 *             when the doc parameter is null
	 */
	@Override
	public Item copyItemToDocument(final lotus.domino.Document doc);

	/**
	 * Copies an item to a specified document.
	 * <p>
	 * When you call this method using a {@link RichTextItem} object, file attachments, embedded objects, and object links that are
	 * contained within the rich-text item are <em>not </em>copied to the destination document.
	 * </p>
	 *
	 * @param doc
	 *            The document on which to create the item. Cannot be null.
	 * @param newname
	 *            The name of the new item. Specify an empty string ("") if you want to keep the name of the original item.
	 * @return The new item.
	 * @throws IllegalArgumentException
	 *             when the doc parameter is null
	 */
	@Override
	public Item copyItemToDocument(final lotus.domino.Document doc, final String newName);

	/**
	 * For a date-time item, returns a <code>DateTime</code> object representing the value of the item. For items of other types, returns
	 * null.
	 *
	 */
	@Override
	public DateTime getDateTimeValue();

	/**
	 * SAX InputSource representation of the contents of an {@link EmbeddedObject}, <code>Item</code>, or {@link MIMEEntity} object.
	 * <p>
	 * For standalone applications, you should include the NCSO.jar or Notes.jar file in the classpath for the lotus.domino classes. You
	 * should also include the XML4j.jar file in your classpath to use the XML parser, and the LotusXSL.jar file to use the XSL processor.
	 * </p>
	 *
	 * <p>
	 * Note: You must include the XML4j.jar file in your classpath even if you only need to use the transformXML methods.
	 * </p>
	 * <p>
	 * For applets that run in a browser, you should include the XML4j.jar and/or XML4j.cab file with the applet itself, to use the XML
	 * parser. To use the XSL processor, you should include the XML4j.jar and/or XML4j.cab file as well as the LotusXSL.jar and/or
	 * LotusXSL.cab file with the applet.
	 * </p>
	 *
	 * <p>
	 * {@link EmbeddedObject#getInputSource()} creates a temporary file. The file is deleted when <code>EmbeddedObject</code> is recycled.
	 * </p>
	 *
	 * @return SAX InputSource representation of the contents
	 */
	@Override
	public InputSource getInputSource();

	/**
	 * InputStream representation of the contents of an {@link EmbeddedObject}, <code>Item</code>, or {@link MIMEEntity} object.
	 * <p>
	 * {@link EmbeddedObject#getInputStream()} creates a temporary file. The file is deleted when the <code>EmbeddedObject</code> is
	 * recycled, but only if the close() method of the InputStream object returned by this property has been called before the
	 * <code>EmbeddedObject</code> is recycled. Failure to explicitly close the <code>InputStream</code> object will result in the temporary
	 * file remaining in the filesystem, which can cause the system to run out of disk space when running an agent that processes many
	 * <code>EmbeddedObjects</code>.
	 * </p>
	 *
	 * @return InputStream representation of the contents
	 */
	@Override
	public InputStream getInputStream();

	/**
	 * The date/time that an item was last modified.
	 */
	@Override
	public DateTime getLastModified();

	/**
	 * Returns the MIMEEntity that this item represents, typically a ""Body" Item of a mailed Internet document of RFC822 and subsequent
	 * MIME RFCXXX formats.
	 *
	 */
	@Override
	public MIMEEntity getMIMEEntity();

	/**
	 * The name of an item.
	 * <p>
	 * A document may have multiple items with the same name.
	 * </p>
	 * <p>
	 * All file attachments have the name $FILE.
	 * </p>
	 *
	 */
	@Override
	public String getName();

	/**
	 * The document that contains an item.
	 */
	@Override
	public Document getParent();

	/**
	 * Contents of an an {@link EmbeddedObject}, <code>Item</code>, or {@link MIMEEntity} object in the form of a java.io.Reader object.
	 * <p>
	 * {@link EmbeddedObject#getReader()} creates a temporary file. The file is deleted when <code>EmbeddedObject</code> is recycled.
	 * </p>
	 */
	@Override
	public Reader getReader();

	/**
	 * A plain text representation of an item's value.
	 * <p>
	 * Multiple values in a list are separated by semicolons in the returned string. If an item's value is large, the returned string may be
	 * truncated.
	 * </p>
	 * <p>
	 * For rich-text items, this property skips non-text data such as bitmaps and file attachments.
	 * </p>
	 * <p>
	 * For HTML items, this property returns null.
	 * </p>
	 *
	 */
	@Override
	public String getText();

	/**
	 * A plain text representation of an item's value.
	 * <p>
	 * Multiple values in a list are separated by semicolons in the returned string. If an item's value is large, the returned string may be
	 * truncated.
	 * </p>
	 * <p>
	 * For rich-text items, this property skips non-text data such as bitmaps and file attachments.
	 * </p>
	 * <p>
	 * For HTML items, this property returns null.
	 * </p>
	 *
	 * @param maxLen
	 *            Maximum length of returned text.
	 * @return A plain text representation of an item's value.
	 *
	 */
	@Override
	public String getText(final int maxLen);

	/**
	 * The data type of an item.
	 * <p>
	 * You can also test for Names, Readers, and Authors items with:
	 * </p>
	 * <ul>
	 * <li>{@link #isNames()}</li>
	 * <li>{@link #isReaders()}</li>
	 * <li>{@link #isAuthors()}</li>
	 * </ul>
	 *
	 * @return The data type of an item. One of
	 *         <ul>
	 *         <li>Item.ACTIONCD</li>
	 *         <li>Item.ASSISTANTINFO</li>
	 *         <li>Item.ATTACHMENT (file attachment)</li>
	 *         <li>Item.AUTHORS</li>
	 *         <li>Item.COLLATION</li>
	 *         <li>Item.DATETIMES (date-time or range of date-time values)</li>
	 *         <li>Item.EMBEDDEDOBJECT</li>
	 *         <li>Item.ERRORITEM (error occurred while getting type)</li>
	 *         <li>Item.FORMULA (Domino formula)</li>
	 *         <li>Item.HTML (HTML source text)</li>
	 *         <li>Item.ICON</li>
	 *         <li>Item.LSOBJECT</li>
	 *         <li>Item.MIME_PART</li>
	 *         <li>Item.NAMES</li>
	 *         <li>Item.NOTELINKS (link to a database, view, or document)</li>
	 *         <li>Item.NOTEREFS (reference to the parent document)</li>
	 *         <li>Item.NUMBERS (number or number list)</li>
	 *         <li>Item.OTHEROBJECT</li>
	 *         <li>Item.QUERYCD</li>
	 *         <li>Item.READERS</li>
	 *         <li>Item. RFC822TEXT</li>
	 *         <li>Item.RICHTEXT</li>
	 *         <li>Item.SIGNATURE</li>
	 *         <li>Item.TEXT (text or text list)</li>
	 *         <li>Item.UNAVAILABLE</li>
	 *         <li>Item.UNKNOWN</li>
	 *         <li>Item.USERDATA</li>
	 *         <li>Item.USERID</li>
	 *         <li>Item.VIEWMAPDATA</li>
	 *         <li>Item.VIEWMAPLAYOUT</li>
	 *         </ul>
	 * @see lotus.domino.Item#getType()
	 * @deprecated use {@link org.openntf.domino.ext.Item#getTypeEx()} instead
	 */
	@Override
	@Deprecated
	public int getType();

	/**
	 * Returns as a byte array the value of an item containing custom data.
	 * <p>
	 * An item that contains custom data is of type Item.USERDATA (14).
	 * </p>
	 * <p>
	 * For other methods that get custom data, see:
	 * </p>
	 * <ul>
	 * <li>{@link Document#getItemValueCustomData(String)}</li>
	 * <li>{@link Document#getItemValueCustomDataBytes(String, String)}</li>
	 * <li>{@link #getValueCustomData()}</li>
	 * </ul>
	 * <p>
	 * To write custom data to an item, see:
	 * </p>
	 * <ul>
	 * <li>{@link Document#replaceItemValueCustomData(String, Object)}</li>
	 * <li>{@link Document#replaceItemValueCustomDataBytes(String, String, byte[])}</li>
	 * <li>{@link #setValueCustomData(Object)}</li>
	 * <li>{@link #setValueCustomDataBytes(String, byte[])}</li>
	 * </ul>
	 *
	 * @return A byte array that receives the value of the item.
	 */
	@Override
	public Object getValueCustomData() throws IOException, ClassNotFoundException;

	/**
	 * Returns as a byte array the value of an item containing custom data.
	 * <p>
	 * An item that contains custom data is of type Item.USERDATA (14).
	 * </p>
	 * <p>
	 * For other methods that get custom data, see:
	 * </p>
	 * <ul>
	 * <li>{@link Document#getItemValueCustomData(String)}</li>
	 * <li>{@link Document#getItemValueCustomDataBytes(String, String)}</li>
	 * <li>{@link #getValueCustomData()}</li>
	 * </ul>
	 * <p>
	 * To write custom data to an item, see:
	 * </p>
	 * <ul>
	 * <li>{@link Document#replaceItemValueCustomData(String, Object)}</li>
	 * <li>{@link Document#replaceItemValueCustomDataBytes(String, String, byte[])}</li>
	 * <li>{@link #setValueCustomData(Object)}</li>
	 * <li>{@link #setValueCustomDataBytes(String, byte[])}</li>
	 * </ul>
	 *
	 * @param datatypename
	 *            The name of the data type. This name must match the data type name specified when the item was written.
	 * @return A byte array that receives the value of the item.
	 */
	@Override
	public Object getValueCustomData(final String dataTypeName) throws IOException, ClassNotFoundException;

	/**
	 * Returns as a byte array the value of an item containing custom data.
	 * <p>
	 * An item that contains custom data is of type Item.USERDATA (14).
	 * </p>
	 * <p>
	 * For other methods that get custom data, see:
	 * </p>
	 * <ul>
	 * <li>{@link Document#getItemValueCustomData(String)}</li>
	 * <li>{@link Document#getItemValueCustomDataBytes(String, String)}</li>
	 * <li>{@link #getValueCustomData()}</li>
	 * </ul>
	 * <p>
	 * To write custom data to an item, see:
	 * </p>
	 * <ul>
	 * <li>{@link Document#replaceItemValueCustomData(String, Object)}</li>
	 * <li>{@link Document#replaceItemValueCustomDataBytes(String, String, byte[])}</li>
	 * <li>{@link #setValueCustomData(String, Object)}</li>
	 * <li>{@link #setValueCustomDataBytes(String, byte[])}</li>
	 * </ul>
	 *
	 * @param datatypename
	 *            The name of the data type. This name must match the data type name specified when the item was written.
	 * @return A byte array that receives the value of the item.
	 */
	@Override
	public byte[] getValueCustomDataBytes(final String dataTypeName) throws IOException;

	/**
	 * Returns the value of a date-time item.
	 * <p>
	 * You can determine the class of each element with Object.getClass().getName(). For the local interface, the class name is either
	 * lotus.domino.local.DateTime or lotus.domino.local.DateRange; for the remote interface, the class name is either
	 * lotus.domino.cso.DateTime or lotus.domino.cso.DateRange.
	 * </p>
	 *
	 * @return The value or values contained in the item. Each element in the vector corresponds to a value in the item and is of type
	 *         DateTime or DateRange If the item contains a single value, the vector has one element.
	 */
	@Override
	public Vector<DateTime> getValueDateTimeArray();

	/**
	 * The value of an item with a single numeric value.
	 * <p>
	 * If the item has no value or the value is text, date-time, or empty, this method returns 0.0.
	 * </p>
	 * <p>
	 * If the item has mutiple values, this method returns the first value.
	 * </p>
	 *
	 */
	@Override
	public double getValueDouble();

	/**
	 * The value of an item with a single numeric value.
	 * <p>
	 * If the item has no value or the value is text, date-time, or empty, this method returns 0.
	 * </p>
	 * <p>
	 * If the item has mutiple values, this method returns the first value.
	 * </p>
	 *
	 */
	@Override
	public int getValueInteger();

	/**
	 * The number of bytes of internal storage, including overhead, required to store an item.
	 */
	@Override
	public int getValueLength();

	/**
	 * The value of this item.
	 * <p>
	 * If the item has no value, this method returns null.
	 * </p>
	 *
	 * <p>
	 * This property returns the same value(s) for an item as {@link Document#getItemValue(String)}.</>
	 *
	 * @return The data type of the value depends upon the type of the item. Each element corresponds to a value in the item. If the item
	 *         contains a single value, the vector has just one element.
	 *         <table cellpadding="4" cellspacing="0" summary="" rules="all" frame="border" border="1">
	 *         <thead align="left" valign="bottom">
	 *         <tr>
	 *         <th align="left" valign="top" width="NaN%" id="d3100394e62">
	 *         <p>
	 *         Item type
	 *         </p>
	 *         </th>
	 *         <th align="left" valign="top" width="NaN%" id="d3100394e67">
	 *         <p>
	 *         Valid return type
	 *         </p>
	 *         </th>
	 *         </tr>
	 *         </thead> <tbody>
	 *         <tr >
	 *         <td align="left" valign="top" width="NaN%" headers="d3100394e62 ">
	 *         <p>
	 *         Rich text
	 *         </p>
	 *         </td>
	 *         <td align="left" valign="top" width="NaN%" headers="d3100394e67 ">
	 *         <p>
	 *         java.util.Vector with one String element rendered into plain text
	 *         </p>
	 *         </td>
	 *         </tr>
	 *         <tr>
	 *         <td align="left" valign="top" width="NaN%" headers="d3100394e62 ">
	 *         <p>
	 *         Text (includes Names, Authors, and Readers item types)
	 *         </p>
	 *         </td>
	 *         <td align="left" valign="top" width="NaN%" headers="d3100394e67 ">
	 *         <p>
	 *         java.util.Vector with String elements
	 *         </p>
	 *         </td>
	 *         </tr>
	 *         <tr >
	 *         <td align="left" valign="top" width="NaN%" headers="d3100394e62 ">
	 *         <p>
	 *         Number or number list
	 *         </p>
	 *         </td>
	 *         <td align="left" valign="top" width="NaN%" headers="d3100394e67 ">
	 *         <p>
	 *         java.util.Vector with Double elements
	 *         </p>
	 *         </td>
	 *         </tr>
	 *         <tr>
	 *         <td align="left" valign="top" width="NaN%" headers="d3100394e62 ">
	 *         <p>
	 *         Date-time or range of date-time values
	 *         </p>
	 *         </td>
	 *         <td align="left" valign="top" width="NaN%" headers="d3100394e67 ">
	 *         <p>
	 *         java.util.Vector with DateTime elements
	 *         </p>
	 *         </td>
	 *         </tr>
	 *         </tbody>
	 *         </table>
	 */
	@Override
	public Vector<Object> getValues();

	/**
	 * The value that an item holds.
	 * <p>
	 * If the item has no value or the value is numeric or date-time, this method returns an empty string.
	 * </p>
	 * <p>
	 * This method returns a rich-text item rendered to plain text. Formatting and embedded objects are lost.
	 * </p>
	 * <p>
	 * If the item has multiple values, this method returns the first value.
	 * </p>
	 *
	 */
	@Override
	public String getValueString();

	/**
	 * Indicates whether an item is of type Authors.
	 * <p>
	 * An Authors item contains a text list of user names indicating people who have Author access to a particular document. An Authors item
	 * returns Item.TEXT for {@link #getType()}.
	 * </p>
	 *
	 * @return true, if this item is of type Authors.
	 */
	@Override
	public boolean isAuthors();

	/**
	 * Indicates whether an item is encrypted.
	 * <p>
	 * If you set this property to true, the item is not actually encrypted until you call {@link Document#encrypt()} on the parent
	 * <code>Document</code>.
	 * </p>
	 *
	 * @return True when this item is encrypted.
	 *
	 */
	@Override
	public boolean isEncrypted();

	/**
	 * Indicates whether an item is a Names item.
	 * <p>
	 * A Names item contains a list of user names. A Names item returns Item.TEXT for {@link #getType()}.
	 * </p>
	 *
	 * @return True when this item is a Names item.
	 *
	 */
	@Override
	public boolean isNames();

	/**
	 * Indicates whether a user needs at least Editor access to modify an item.
	 *
	 * @return True when a user needs at least Editor access to modify an item.
	 */
	@Override
	public boolean isProtected();

	/**
	 * Indicates whether an item is of type Readers.
	 * <p>
	 * A Readers item contains a list of user names indicating people who have Reader access to a document. A Readers item returns Item.TEXT
	 * for {@link #getType()}.
	 * </p>
	 *
	 * @return True when an item is of type Readers.
	 *
	 */
	@Override
	public boolean isReaders();

	/**
	 * Indicates whether an item is saved when the document is saved.
	 * <p>
	 * If you mark an existing item as not to be saved, it disappears from storage the next time you save the document.
	 * </p>
	 *
	 * @return true if the item is saved when the document is saved
	 */
	@Override
	public boolean isSaveToDisk();

	/**
	 * Indicates whether an item contains a signature the next time the document is signed.
	 *
	 */
	@Override
	public boolean isSigned();

	/**
	 * Indicates whether an item contains summary or non-summary data.
	 * <p>
	 * Items are flagged as containing summary or non-summary data. Summary data can appear in views and folders; non-summary data cannot.
	 * In general, items created through the UI are tagged as non-summary if they contain rich text or are very long.
	 * </p>
	 * <p>
	 * When you create a new item using {@link Document#appendItemValue(String)} or {@link Document#replaceItemValue(String, Object)}, the
	 * <code>isSummary</code> property for the item is true. If you don't want the item to appear in views and folders, you must change its
	 * <code>IsSummary</code> property to false.
	 * </p>
	 * <p>
	 * You can enable or disable the appearance of an existing item in views and folders by changing its <code>IsSummary</code> property.
	 * </p>
	 * <p>
	 * An item whose <code>IsSummary</code> property is true may not appear as expected in views and folders if the data is not suitable.
	 * For example, a rich text item whose <code>IsSummary</code> property is true generally appears as a question mark.
	 * </p>
	 *
	 * @return True when this item contains summary data.
	 *
	 */
	@Override
	public boolean isSummary();

	/**
	 * Parses the contents of an attachment and creates the DOM tree of the XML.
	 * <p>
	 * For standalone applications, you should include the NCSO.jar or Notes.jar file in the classpath for the lotus.domino classes. You
	 * should also include the XML4j.jar file in your classpath to use the XML parser, and the LotusXSL.jar file to use the XSL processor.
	 * </p>
	 * <p>
	 * For applets that run in a browser, you should include the XML4j.jar and/or XML4j.cab file with the applet itself, to use the XML
	 * parser. To use the XSL processor, you should include the XML4j.jar and/or XML4j.cab file as well as the LotusXSL.jar and/or
	 * LotusXSL.cab file with the applet.
	 * </p>
	 * <p>
	 * Errors generated during parsing are directed to System.err.
	 * </p>
	 * <p>
	 * If a stream of XML contains relative or partial URLs, the <code>parseXML</code> method or <code>transformXML</code> method resolves
	 * the partial URL as a Page on the database where the InputStream originated. For example, when the <code>parseXML</code> or
	 * <code>transformXML</code> method encounters the XML stream &lt;!DOCTYPE software-release-note SYSTEM "readme.dtd"&gt;, it looks for a
	 * Page named "readme.dtd" in the database where the source stream originated.
	 * </p>
	 *
	 * @param validate
	 *            Specify true to use the Validating DOMParser and false to use the Non-Validating DOMParser.
	 * @return The DOM tree object.
	 */
	@Override
	public org.w3c.dom.Document parseXML(final boolean validate) throws IOException;

	/**
	 * Permanently deletes an item from a document.
	 * <p>
	 * After calling remove, you must call {@link Document#save()} to save the change.
	 * </p>
	 * <p>
	 * You can achieve the same result with {@link Document#removeItem(String)}.
	 * </p>
	 *
	 */
	@Override
	public void remove();

	/**
	 * Marks this item as Authors type
	 * <p>
	 * An Authors item contains a text list of user names indicating people who have Author access to a particular document. An Authors item
	 * returns Item.TEXT for {@link #getType()}.
	 * </p>
	 *
	 * @throws DataNotCompatibleException
	 *             when this Item is not a text item.
	 */
	@Override
	public void setAuthors(final boolean flag);

	/**
	 * For a date-time item, returns a DateTime object representing the value of the item. For items of other types, returns null.
	 * <p>
	 * Can be set to null.
	 * </p>
	 *
	 * @param dateTime
	 */
	@Override
	public void setDateTimeValue(final lotus.domino.DateTime dateTime);

	/**
	 * Sets whether an item is encrypted.
	 * <p>
	 * If you set this property to true, the item is not actually encrypted until you call {@link Document#encrypt()} on the parent
	 * <code>Document</code>
	 * </p>
	 *
	 * @param flag
	 *            true if the item will be encrypted
	 */
	@Override
	public void setEncrypted(final boolean flag);

	/**
	 * Marks this item as Names type containing list of user names.
	 * <p>
	 * A Names item contains a list of user names. A Names item returns Item.TEXT for {@link #getType()}.
	 * </p>
	 *
	 * @throws DataNotCompatibleException
	 *             it this Item is not a Text type
	 */
	@Override
	public void setNames(final boolean flag);

	/**
	 * Sets whether a user needs at least Editor access to modify an item.
	 *
	 * @param flag
	 *            true if you need at least Editor access to modify the item, false if you do not need Editor access to modify the item; you
	 *            can modify it as long as you have Author access or better
	 */
	@Override
	public void setProtected(final boolean flag);

	/**
	 * Marks this item as Readers type.
	 * <p>
	 * A Readers item contains a list of user names indicating people who have Reader access to a document. A Readers item returns Item.TEXT
	 * for {@link #getType()}.
	 * </p>
	 *
	 * @throws DataNotCompatibleException
	 *             if this Item is not a text item
	 */
	@Override
	public void setReaders(final boolean flag);

	/**
	 * Sets whether an item is saved when the document is saved.
	 * <p>
	 * If you mark an existing item as not to be saved, it disappears from storage the next time you save the document.
	 * </p>
	 *
	 * @param flag
	 *            true if the item is saved when the document is saved, false if not
	 */
	@Override
	public void setSaveToDisk(final boolean flag);

	/**
	 * Sets whether an item contains a signature the next time the document is signed.
	 *
	 * @param flag
	 *            true if the item is signed when the document is next signed
	 *
	 */
	@Override
	public void setSigned(final boolean flag);

	/**
	 * Sets whether an item contains summary or non-summary data.
	 * <p>
	 * Items are flagged as containing summary or non-summary data. Summary data can appear in views and folders; non-summary data cannot.
	 * In general, items created through the UI are tagged as non-summary if they contain rich text or are very long.
	 * </p>
	 * <p>
	 * When you create a new item using {@link Document#appendItemValue(String)} or {@link Document#replaceItemValue(String, Object)}, the
	 * <code>isSummary</code> property for the item is true. If you don't want the item to appear in views and folders, you must change its
	 * <code>IsSummary</code> property to false.
	 * </p>
	 * <p>
	 * You can enable or disable the appearance of an existing item in views and folders by changing its <code>IsSummary</code> property.
	 * </p>
	 * <p>
	 * An item whose <code>IsSummary</code> property is true may not appear as expected in views and folders if the data is not suitable.
	 * For example, a rich text item whose <code>IsSummary</code> property is true generally appears as a question mark.
	 * </p>
	 *
	 * @param flag
	 *            true when this item contains summary data, false if not
	 *
	 */
	@Override
	public void setSummary(final boolean flag);

	/**
	 * Sets the value of an item to custom data from an object.
	 * <p>
	 * The new value replaces the existing value.
	 * </p>
	 * <p>
	 * To keep the changes, you must call {@link Document#save()} after calling this method.
	 * </p>
	 * <p>
	 * The custom data cannot exceed 64K.
	 * </p>
	 * <p>
	 * If you intend to get the custom data through a language binding other than Java, use the
	 * {@link #setValueCustomDataBytes(String, byte[])} method.
	 * </p>
	 * <p>
	 * For other methods that get custom data, see:
	 * </p>
	 * <ul>
	 * <li>{@link Document#getItemValueCustomData(String)}</li>
	 * <li>{@link Document#getItemValueCustomDataBytes(String, String)}</li>
	 * <li>{@link #getValueCustomData()}</li>
	 * </ul>
	 * <p>
	 * To write custom data to an item, see:
	 * </p>
	 * <ul>
	 * <li>{@link Document#replaceItemValueCustomData(String, Object)}</li>
	 * <li>{@link Document#replaceItemValueCustomDataBytes(String, String, byte[])}</li>
	 * <li>{@link #setValueCustomData(Object)}</li>
	 * <li>{@link #setValueCustomDataBytes(String, byte[])}</li>
	 * </ul>
	 *
	 *
	 * @param userobj
	 *            An object that contains the custom data. The class that defines this object must implement Serializable. If desired, you
	 *            can override readObject and writeObject.
	 */
	@Override
	public void setValueCustomData(final Object userObj) throws IOException;

	/**
	 * Sets the value of an item to custom data from an object.
	 * <p>
	 * The new value replaces the existing value.
	 * </p>
	 * <p>
	 * To keep the changes, you must call {@link Document#save()} after calling this method.
	 * </p>
	 * <p>
	 * The custom data cannot exceed 64K.
	 * </p>
	 * <p>
	 * If you intend to get the custom data through a language binding other than Java, use the
	 * {@link #setValueCustomDataBytes(String, byte[])} method.
	 * </p>
	 * <p>
	 * For other methods that get custom data, see:
	 * </p>
	 * <ul>
	 * <li>{@link Document#getItemValueCustomData(String)}</li>
	 * <li>{@link Document#getItemValueCustomDataBytes(String, String)}</li>
	 * <li>{@link #getValueCustomData()}</li>
	 * </ul>
	 * <p>
	 * To write custom data to an item, see:
	 * </p>
	 * <ul>
	 * <li>{@link Document#replaceItemValueCustomData(String, Object)}</li>
	 * <li>{@link Document#replaceItemValueCustomDataBytes(String, String, byte[])}</li>
	 * <li>{@link #setValueCustomData(Object)}</li>
	 * <li>{@link #setValueCustomDataBytes(String, byte[])}</li>
	 * </ul>
	 *
	 *
	 * @param datatypename
	 *            A name for the data type. When getting custom data, use this name for verification.
	 * @param userobj
	 *            An object that contains the custom data. The class that defines this object must implement Serializable. If desired, you
	 *            can override readObject and writeObject.
	 */
	@Override
	public void setValueCustomData(final String dataTypeName, final Object userObj) throws IOException;

	/**
	 * Sets the value of an item to custom data from a byte array.
	 * <p>
	 * The new value replaces the existing value.
	 * </p>
	 * <p>
	 * To keep the changes, you must call {@link Document#save()} after calling this method.
	 * </p>
	 * <p>
	 * The custom data cannot exceed 64K.
	 * </p>
	 * <p>
	 * Use this method if you intend to get the custom data through a language binding other than Java
	 * </p>
	 * <p>
	 * For other methods that get custom data, see:
	 * </p>
	 * <ul>
	 * <li>{@link Document#getItemValueCustomData(String)}</li>
	 * <li>{@link Document#getItemValueCustomDataBytes(String, String)}</li>
	 * <li>{@link #getValueCustomData()}</li>
	 * <li>{@link #getValueCustomDataBytes(String)}</li>
	 * </ul>
	 * <p>
	 * To write custom data to an item, see:
	 * </p>
	 * <ul>
	 * <li>{@link Document#replaceItemValueCustomData(String, Object)}</li>
	 * <li>{@link Document#replaceItemValueCustomDataBytes(String, String, byte[])}</li>
	 * <li>{@link #setValueCustomData(Object)}</li>
	 * </ul>
	 *
	 * @param dataTypeName
	 *            A name for the data type. When getting custom data, use this name for verification.
	 * @param byteArray
	 *            A byte array that contains the custom data.
	 */
	@Override
	public void setValueCustomDataBytes(final String dataTypeName, final byte[] byteArray) throws IOException;

	/**
	 * Sets the value of this item with a single numeric value.
	 *
	 * @param value
	 *            the new value
	 */
	@Override
	public void setValueDouble(final double value);

	/**
	 * Sets the value of this item with a single numeric value.
	 *
	 * @param value
	 *            the new value
	 */
	@Override
	public void setValueInteger(final int value);

	/**
	 * Sets the value of this item.
	 *
	 *
	 * @param values
	 *            The data type of the value depends upon the type of the item. Each element corresponds to a value in the item.
	 *            <table cellpadding="4" cellspacing="0" summary="" rules="all" frame="border" border="1">
	 *            <thead align="left" valign="bottom">
	 *            <tr>
	 *            <th align="left" valign="top" width="NaN%" id="d3100394e62">
	 *            <p>
	 *            Item type
	 *            </p>
	 *            </th>
	 *            <th align="left" valign="top" width="NaN%" id="d3100394e67">
	 *            <p>
	 *            Valid return type
	 *            </p>
	 *            </th>
	 *            </tr>
	 *            </thead> <tbody>
	 *            <tr >
	 *            <td align="left" valign="top" width="NaN%" headers="d3100394e62 ">
	 *            <p>
	 *            Rich text
	 *            </p>
	 *            </td>
	 *            <td align="left" valign="top" width="NaN%" headers="d3100394e67 ">
	 *            <p>
	 *            java.util.Vector with one String element rendered into plain text
	 *            </p>
	 *            </td>
	 *            </tr>
	 *            <tr>
	 *            <td align="left" valign="top" width="NaN%" headers="d3100394e62 ">
	 *            <p>
	 *            Text (includes Names, Authors, and Readers item types)
	 *            </p>
	 *            </td>
	 *            <td align="left" valign="top" width="NaN%" headers="d3100394e67 ">
	 *            <p>
	 *            java.util.Vector with String elements
	 *            </p>
	 *            </td>
	 *            </tr>
	 *            <tr >
	 *            <td align="left" valign="top" width="NaN%" headers="d3100394e62 ">
	 *            <p>
	 *            Number or number list
	 *            </p>
	 *            </td>
	 *            <td align="left" valign="top" width="NaN%" headers="d3100394e67 ">
	 *            <p>
	 *            java.util.Vector with Double elements
	 *            </p>
	 *            </td>
	 *            </tr>
	 *            <tr>
	 *            <td align="left" valign="top" width="NaN%" headers="d3100394e62 ">
	 *            <p>
	 *            Date-time or range of date-time values
	 *            </p>
	 *            </td>
	 *            <td align="left" valign="top" width="NaN%" headers="d3100394e67 ">
	 *            <p>
	 *            java.util.Vector with DateTime elements
	 *            </p>
	 *            </td>
	 *            </tr>
	 *            </tbody>
	 *            </table>
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setValues(final Vector values);

	/**
	 * Sets the value of the item.
	 *
	 * @param value
	 *            the new value
	 */
	@Override
	public void setValueString(final String value);

	/**
	 * Transforms the contents of an attachment using the specified Domino EmbeddedObject, Item, MIMEEntity, or RichTextItem style, or any
	 * InputSource style, and provides the results to the specified XSLTResultTarget object.
	 * <p>
	 * For standalone applications, you should include the NCSO.jar or Notes.jar file in the classpath for the lotus.domino classes. You
	 * should also include the XML4j.jar file in your classpath to use the XML parser, and the LotusXSL.jar file to use the XSL processor.
	 * </p>
	 * <p>
	 * Note: You must include the XML4j.jar file in your classpath even if you only need to use the transformXML methods. For applets that
	 * run in a browser, you should include the XML4j.jar and/or XML4j.cab file with the applet itself, to use the XML parser. To use the
	 * XSL processor, you should include the XML4j.jar and/or XML4j.cab file as well as the LotusXSL.jar and/or LotusXSL.cab file with the
	 * applet.
	 * </p>
	 * <p>
	 * Errors generated during transformation are directed to System.err.
	 * </p>
	 * <p>
	 * If a stream of XML contains relative or partial URLs, the parseXML method or transformXML method resolves the partial URL as a Page
	 * on the database where the InputStream originated. For example, when the parseXML or transformXML method encounters the XML stream
	 * <!DOCTYPE software-release-note SYSTEM "readme.dtd">, it looks for a Page named "readme.dtd" in the database where the source stream
	 * originated.
	 * </p>
	 *
	 * @param style
	 *            The stylesheet source that you use to transform the XML data.
	 * @param result
	 *            The object that receives the transformed data.
	 */
	@Override
	public void transformXML(final Object style, final XSLTResultTarget result);
}
