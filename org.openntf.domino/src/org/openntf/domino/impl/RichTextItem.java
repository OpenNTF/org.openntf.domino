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
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.DateTime;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.xml.sax.InputSource;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextItem.
 */
public class RichTextItem extends Base<org.openntf.domino.RichTextItem, lotus.domino.RichTextItem> implements
		org.openntf.domino.RichTextItem {

	/**
	 * Instantiates a new rich text item.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public RichTextItem(lotus.domino.RichTextItem delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#abstractText(int, boolean, boolean)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#addNewLine()
	 */
	@Override
	public void addNewLine() {
		try {
			getDelegate().addNewLine();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#addNewLine(int)
	 */
	@Override
	public void addNewLine(int count) {
		try {
			getDelegate().addNewLine(count);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#addNewLine(int, boolean)
	 */
	@Override
	public void addNewLine(int count, boolean newParagraph) {
		try {
			getDelegate().addNewLine(count, newParagraph);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#addPageBreak()
	 */
	@Override
	public void addPageBreak() {
		try {
			getDelegate().addPageBreak();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#addPageBreak(lotus.domino.RichTextParagraphStyle)
	 */
	@Override
	public void addPageBreak(lotus.domino.RichTextParagraphStyle pstyle) {
		try {
			getDelegate().addPageBreak((lotus.domino.RichTextParagraphStyle) toLotus(pstyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#addTab()
	 */
	@Override
	public void addTab() {
		try {
			getDelegate().addTab();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#addTab(int)
	 */
	@Override
	public void addTab(int count) {
		try {
			getDelegate().addTab(count);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Database)
	 */
	@Override
	public void appendDocLink(lotus.domino.Database db) {
		try {
			getDelegate().appendDocLink((lotus.domino.Database) toLotus(db));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Database, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Database db, String comment) {
		try {
			getDelegate().appendDocLink((lotus.domino.Database) db, comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Database, java.lang.String, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Database db, String comment, String hotspotText) {
		try {
			getDelegate().appendDocLink((lotus.domino.Database) db, comment, hotspotText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Document)
	 */
	@Override
	public void appendDocLink(lotus.domino.Document doc) {
		try {
			getDelegate().appendDocLink((lotus.domino.Document) toLotus(doc));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Document, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Document doc, String comment) {
		try {
			getDelegate().appendDocLink((lotus.domino.Document) toLotus(doc), comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Document, java.lang.String, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Document doc, String comment, String hotspotText) {
		try {
			getDelegate().appendDocLink((lotus.domino.Document) toLotus(doc), comment, hotspotText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.View)
	 */
	@Override
	public void appendDocLink(lotus.domino.View view) {
		try {
			getDelegate().appendDocLink((lotus.domino.View) toLotus(view));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.View, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.View view, String comment) {
		try {
			getDelegate().appendDocLink((lotus.domino.View) toLotus(view), comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.View, java.lang.String, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.View view, String comment, String hotspotText) {
		try {
			getDelegate().appendDocLink((lotus.domino.View) toLotus(view), hotspotText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendParagraphStyle(lotus.domino.RichTextParagraphStyle)
	 */
	@Override
	public void appendParagraphStyle(lotus.domino.RichTextParagraphStyle pstyle) {
		try {
			getDelegate().appendParagraphStyle((lotus.domino.RichTextParagraphStyle) toLotus(pstyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendRTItem(lotus.domino.RichTextItem)
	 */
	@Override
	public void appendRTItem(lotus.domino.RichTextItem rtitem) {
		try {
			getDelegate().appendRTItem((lotus.domino.RichTextItem) toLotus(rtitem));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendStyle(lotus.domino.RichTextStyle)
	 */
	@Override
	public void appendStyle(lotus.domino.RichTextStyle rstyle) {
		try {
			getDelegate().appendStyle((lotus.domino.RichTextStyle) toLotus(rstyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendTable(int, int)
	 */
	@Override
	public void appendTable(int rows, int columns) {
		try {
			getDelegate().appendTable(rows, columns);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendTable(int, int, java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void appendTable(int rows, int columns, Vector labels) {
		try {
			getDelegate().appendTable(rows, columns, labels);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendTable(int, int, java.util.Vector, int, java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void appendTable(int rows, int columns, Vector labels, int leftMargin, Vector pstyles) {
		try {
			getDelegate().appendTable(rows, columns, labels, leftMargin, toLotus(pstyles));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendText(java.lang.String)
	 */
	@Override
	public void appendText(String text) {
		try {
			getDelegate().appendText(text);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendToTextList(java.lang.String)
	 */
	@Override
	public void appendToTextList(String value) {
		try {
			getDelegate().appendToTextList(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#appendToTextList(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void appendToTextList(Vector values) {
		try {
			getDelegate().appendToTextList(values);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#beginInsert(lotus.domino.Base)
	 */
	@Override
	public void beginInsert(lotus.domino.Base element) {
		try {
			getDelegate().beginInsert(toLotus(element));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#beginInsert(lotus.domino.Base, boolean)
	 */
	@Override
	public void beginInsert(lotus.domino.Base element, boolean after) {
		try {
			getDelegate().beginInsert(toLotus(element), after);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#beginSection(java.lang.String)
	 */
	@Override
	public void beginSection(String title) {
		try {
			getDelegate().beginSection(title);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#beginSection(java.lang.String, lotus.domino.RichTextStyle)
	 */
	@Override
	public void beginSection(String title, lotus.domino.RichTextStyle titleStyle) {
		try {
			getDelegate().beginSection(title, (lotus.domino.RichTextStyle) toLotus(titleStyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#beginSection(java.lang.String, lotus.domino.RichTextStyle, lotus.domino.ColorObject, boolean)
	 */
	@Override
	public void beginSection(String title, lotus.domino.RichTextStyle titleStyle, lotus.domino.ColorObject barColor, boolean expand) {
		try {
			getDelegate().beginSection(title, (lotus.domino.RichTextStyle) toLotus(titleStyle),
					(lotus.domino.ColorObject) toLotus(barColor), expand);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#compact()
	 */
	@Override
	public void compact() {
		try {
			getDelegate().compact();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value) {
		try {
			if (value instanceof lotus.domino.Base) {
				return getDelegate().containsValue(toLotus((lotus.domino.Base) value));
			}
			return getDelegate().containsValue(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#copyItemToDocument(lotus.domino.Document)
	 */
	@Override
	public Item copyItemToDocument(lotus.domino.Document doc) {
		try {
			return Factory.fromLotus(getDelegate().copyItemToDocument((lotus.domino.Document) toLotus(doc)), Item.class,
					(org.openntf.domino.Document) doc);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#copyItemToDocument(lotus.domino.Document, java.lang.String)
	 */
	@Override
	public Item copyItemToDocument(lotus.domino.Document doc, String newName) {
		try {
			return Factory.fromLotus(getDelegate().copyItemToDocument((lotus.domino.Document) toLotus(doc), newName), Item.class,
					(org.openntf.domino.Document) doc);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#createNavigator()
	 */
	@Override
	public RichTextNavigator createNavigator() {
		try {
			return Factory.fromLotus(getDelegate().createNavigator(), RichTextNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#createRange()
	 */
	@Override
	public org.openntf.domino.RichTextRange createRange() {
		try {
			return Factory.fromLotus(getDelegate().createRange(), org.openntf.domino.RichTextRange.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#embedObject(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public EmbeddedObject embedObject(int type, String className, String source, String name) {
		try {
			return Factory.fromLotus(getDelegate().embedObject(type, className, source, name), EmbeddedObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#endInsert()
	 */
	@Override
	public void endInsert() {
		try {
			getDelegate().endInsert();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#endSection()
	 */
	@Override
	public void endSection() {
		try {
			getDelegate().endSection();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getDateTimeValue()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getEmbeddedObject(java.lang.String)
	 */
	@Override
	public EmbeddedObject getEmbeddedObject(String name) {
		try {
			return Factory.fromLotus(getDelegate().getEmbeddedObject(name), EmbeddedObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getEmbeddedObjects()
	 */
	@Override
	public Vector<org.openntf.domino.EmbeddedObject> getEmbeddedObjects() {
		try {
			return Factory.fromLotusAsVector(getDelegate().getEmbeddedObjects(), org.openntf.domino.EmbeddedObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getFormattedText(boolean, int, int)
	 */
	@Override
	public String getFormattedText(boolean tabStrip, int lineLen, int maxLen) {
		try {
			return getDelegate().getFormattedText(tabStrip, lineLen, maxLen);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getInputSource()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getInputStream()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getLastModified()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getMIMEEntity()
	 */
	@Override
	public MIMEEntity getMIMEEntity() {
		try {
			return Factory.fromLotus(getDelegate().getMIMEEntity(), MIMEEntity.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getName()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getNotesFont(java.lang.String, boolean)
	 */
	@Override
	public int getNotesFont(String faceName, boolean addOnFail) {
		try {
			return getDelegate().getNotesFont(faceName, addOnFail);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Document getParent() {
		try {
			return Factory.fromLotus(getDelegate().getParent(), Document.class, this);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getReader()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getText()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getText(int)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getType()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getUnformattedText()
	 */
	@Override
	public String getUnformattedText() {
		try {
			return getDelegate().getUnformattedText();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getValueCustomData()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getValueCustomData(java.lang.String)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getValueCustomDataBytes(java.lang.String)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getValueDateTimeArray()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getValueDouble()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getValueInteger()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getValueLength()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getValueString()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#getValues()
	 */
	@Override
	public Vector<Object> getValues() {
		try {
			Vector<Object> result = new Vector<Object>();
			for (Object value : getDelegate().getValues()) {
				if (value instanceof lotus.domino.DateTime) {
					result.add(Factory.fromLotus((lotus.domino.DateTime) value, DateTime.class, this));
				} else if (value instanceof lotus.domino.DateRange) {
					result.add(Factory.fromLotus((lotus.domino.DateRange) value, DateRange.class, this));
				} else {
					result.add(value);
				}
			}
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#isAuthors()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#isEncrypted()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#isNames()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#isProtected()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#isReaders()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#isSaveToDisk()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#isSigned()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#isSummary()
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#parseXML(boolean)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#remove()
	 */
	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setAuthors(boolean)
	 */
	@Override
	public void setAuthors(boolean flag) {
		try {
			getDelegate().setAuthors(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setDateTimeValue(lotus.domino.DateTime)
	 */
	@Override
	public void setDateTimeValue(lotus.domino.DateTime dateTime) {
		try {
			getDelegate().setDateTimeValue((lotus.domino.DateTime) toLotus(dateTime));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setEncrypted(boolean)
	 */
	@Override
	public void setEncrypted(boolean flag) {
		try {
			getDelegate().setEncrypted(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setNames(boolean)
	 */
	@Override
	public void setNames(boolean flag) {
		try {
			getDelegate().setNames(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setProtected(boolean)
	 */
	@Override
	public void setProtected(boolean flag) {
		try {
			getDelegate().setProtected(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setReaders(boolean)
	 */
	@Override
	public void setReaders(boolean flag) {
		try {
			getDelegate().setReaders(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setSaveToDisk(boolean)
	 */
	@Override
	public void setSaveToDisk(boolean flag) {
		try {
			getDelegate().setReaders(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setSigned(boolean)
	 */
	@Override
	public void setSigned(boolean flag) {
		try {
			getDelegate().setSigned(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setSummary(boolean)
	 */
	@Override
	public void setSummary(boolean flag) {
		try {
			getDelegate().setSummary(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setValueCustomData(java.lang.Object)
	 */
	@Override
	public void setValueCustomData(Object userObj) throws IOException {
		try {
			getDelegate().setValueCustomData(userObj);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setValueCustomData(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setValueCustomData(String dataTypeName, Object userObj) throws IOException {
		try {
			getDelegate().setValueCustomData(dataTypeName, userObj);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setValueCustomDataBytes(java.lang.String, byte[])
	 */
	@Override
	public void setValueCustomDataBytes(String dataTypeName, byte[] byteArray) throws IOException {
		try {
			getDelegate().setValueCustomData(dataTypeName, byteArray);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setValueDouble(double)
	 */
	@Override
	public void setValueDouble(double value) {
		try {
			getDelegate().setValueDouble(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setValueInteger(int)
	 */
	@Override
	public void setValueInteger(int value) {
		try {
			getDelegate().setValueInteger(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setValueString(java.lang.String)
	 */
	@Override
	public void setValueString(String value) {
		try {
			getDelegate().setValueString(value);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#setValues(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setValues(java.util.Vector values) {
		try {
			java.util.Vector<Object> result = new java.util.Vector<Object>(values.size());
			for (Object value : values) {
				if (value instanceof org.openntf.domino.DateTime) {
					result.add(toLotus((org.openntf.domino.DateTime) value));
				} else if (value instanceof org.openntf.domino.DateRange) {
					result.add(toLotus((org.openntf.domino.DateRange) value));
				} else {
					result.add(value);
				}
			}
			getDelegate().setValues(result);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#transformXML(java.lang.Object, lotus.domino.XSLTResultTarget)
	 */
	@Override
	public void transformXML(Object style, lotus.domino.XSLTResultTarget result) {
		try {
			getDelegate().transformXML(style, result);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.RichTextItem#update()
	 */
	@Override
	public void update() {
		try {
			getDelegate().update();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
