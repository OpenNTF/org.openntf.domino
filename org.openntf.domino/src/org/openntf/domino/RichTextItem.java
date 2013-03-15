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
package org.openntf.domino;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Vector;

import lotus.domino.XSLTResultTarget;

import org.xml.sax.InputSource;

// TODO: Auto-generated Javadoc
/**
 * The Interface RichTextItem.
 */
public interface RichTextItem extends lotus.domino.RichTextItem, org.openntf.domino.Base<lotus.domino.RichTextItem> {

	/* (non-Javadoc)
	 * @see lotus.domino.Item#abstractText(int, boolean, boolean)
	 */
	@Override
	public String abstractText(int maxLen, boolean dropVowels, boolean userDict);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#addNewLine()
	 */
	@Override
	public void addNewLine();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#addNewLine(int)
	 */
	@Override
	public void addNewLine(int count);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#addNewLine(int, boolean)
	 */
	@Override
	public void addNewLine(int count, boolean newParagraph);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#addPageBreak()
	 */
	@Override
	public void addPageBreak();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#addPageBreak(lotus.domino.RichTextParagraphStyle)
	 */
	@Override
	public void addPageBreak(lotus.domino.RichTextParagraphStyle pstyle);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#addTab()
	 */
	@Override
	public void addTab();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#addTab(int)
	 */
	@Override
	public void addTab(int count);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.Database)
	 */
	@Override
	public void appendDocLink(lotus.domino.Database db);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.Database, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Database db, String comment);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.Database, java.lang.String, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Database db, String comment, String hotspotText);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.Document)
	 */
	@Override
	public void appendDocLink(lotus.domino.Document doc);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.Document, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Document doc, String comment);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.Document, java.lang.String, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Document doc, String comment, String hotspotText);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.View)
	 */
	@Override
	public void appendDocLink(lotus.domino.View view);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.View, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.View view, String comment);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.View, java.lang.String, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.View view, String comment, String hotspotText);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendParagraphStyle(lotus.domino.RichTextParagraphStyle)
	 */
	@Override
	public void appendParagraphStyle(lotus.domino.RichTextParagraphStyle pstyle);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendRTItem(lotus.domino.RichTextItem)
	 */
	@Override
	public void appendRTItem(lotus.domino.RichTextItem rtitem);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendStyle(lotus.domino.RichTextStyle)
	 */
	@Override
	public void appendStyle(lotus.domino.RichTextStyle rstyle);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendTable(int, int)
	 */
	@Override
	public void appendTable(int rows, int columns);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendTable(int, int, java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void appendTable(int rows, int columns, Vector labels);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendTable(int, int, java.util.Vector, int, java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void appendTable(int rows, int columns, Vector labels, int leftMargin, Vector pstyles);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#appendText(java.lang.String)
	 */
	@Override
	public void appendText(String text);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#appendToTextList(java.lang.String)
	 */
	@Override
	public void appendToTextList(String value);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#appendToTextList(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void appendToTextList(Vector values);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#beginInsert(lotus.domino.Base)
	 */
	@Override
	public void beginInsert(lotus.domino.Base element);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#beginInsert(lotus.domino.Base, boolean)
	 */
	@Override
	public void beginInsert(lotus.domino.Base element, boolean after);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#beginSection(java.lang.String)
	 */
	@Override
	public void beginSection(String title);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#beginSection(java.lang.String, lotus.domino.RichTextStyle)
	 */
	@Override
	public void beginSection(String title, lotus.domino.RichTextStyle titleStyle);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#beginSection(java.lang.String, lotus.domino.RichTextStyle, lotus.domino.ColorObject, boolean)
	 */
	@Override
	public void beginSection(String title, lotus.domino.RichTextStyle titleStyle, lotus.domino.ColorObject barColor, boolean expand);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#compact()
	 */
	@Override
	public void compact();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#copyItemToDocument(lotus.domino.Document)
	 */
	@Override
	public Item copyItemToDocument(lotus.domino.Document doc);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#copyItemToDocument(lotus.domino.Document, java.lang.String)
	 */
	@Override
	public Item copyItemToDocument(lotus.domino.Document doc, String newName);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#createNavigator()
	 */
	@Override
	public RichTextNavigator createNavigator();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#createRange()
	 */
	@Override
	public RichTextRange createRange();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#embedObject(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public EmbeddedObject embedObject(int type, String className, String source, String name);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#endInsert()
	 */
	@Override
	public void endInsert();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#endSection()
	 */
	@Override
	public void endSection();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getDateTimeValue()
	 */
	@Override
	public DateTime getDateTimeValue();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#getEmbeddedObject(java.lang.String)
	 */
	@Override
	public EmbeddedObject getEmbeddedObject(String name);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#getEmbeddedObjects()
	 */
	@Override
	public Vector<EmbeddedObject> getEmbeddedObjects();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#getFormattedText(boolean, int, int)
	 */
	@Override
	public String getFormattedText(boolean tabStrip, int lineLen, int maxLen);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getInputSource()
	 */
	@Override
	public InputSource getInputSource();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getInputStream()
	 */
	@Override
	public InputStream getInputStream();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getLastModified()
	 */
	@Override
	public DateTime getLastModified();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getMIMEEntity()
	 */
	@Override
	public MIMEEntity getMIMEEntity();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getName()
	 */
	@Override
	public String getName();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#getNotesFont(java.lang.String, boolean)
	 */
	@Override
	public int getNotesFont(String faceName, boolean addOnFail);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getParent()
	 */
	@Override
	public Document getParent();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getReader()
	 */
	@Override
	public Reader getReader();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getText()
	 */
	@Override
	public String getText();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getText(int)
	 */
	@Override
	public String getText(int maxLen);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getType()
	 */
	@Override
	public int getType();

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#getUnformattedText()
	 */
	@Override
	public String getUnformattedText();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getValueCustomData()
	 */
	@Override
	public Object getValueCustomData() throws IOException, ClassNotFoundException;

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getValueCustomData(java.lang.String)
	 */
	@Override
	public Object getValueCustomData(String dataTypeName) throws IOException, ClassNotFoundException;

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getValueCustomDataBytes(java.lang.String)
	 */
	@Override
	public byte[] getValueCustomDataBytes(String dataTypeName) throws IOException;

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getValueDateTimeArray()
	 */
	@Override
	public Vector<DateTime> getValueDateTimeArray();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getValueDouble()
	 */
	@Override
	public double getValueDouble();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getValueInteger()
	 */
	@Override
	public int getValueInteger();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getValueLength()
	 */
	@Override
	public int getValueLength();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getValues()
	 */
	@Override
	public Vector<Object> getValues();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#getValueString()
	 */
	@Override
	public String getValueString();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#isAuthors()
	 */
	@Override
	public boolean isAuthors();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#isEncrypted()
	 */
	@Override
	public boolean isEncrypted();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#isNames()
	 */
	@Override
	public boolean isNames();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#isProtected()
	 */
	@Override
	public boolean isProtected();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#isReaders()
	 */
	@Override
	public boolean isReaders();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#isSaveToDisk()
	 */
	@Override
	public boolean isSaveToDisk();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#isSigned()
	 */
	@Override
	public boolean isSigned();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#isSummary()
	 */
	@Override
	public boolean isSummary();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#parseXML(boolean)
	 */
	@Override
	public org.w3c.dom.Document parseXML(boolean validate) throws IOException;

	/* (non-Javadoc)
	 * @see lotus.domino.Item#remove()
	 */
	@Override
	public void remove();

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setAuthors(boolean)
	 */
	@Override
	public void setAuthors(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setDateTimeValue(lotus.domino.DateTime)
	 */
	@Override
	public void setDateTimeValue(lotus.domino.DateTime dt);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setEncrypted(boolean)
	 */
	@Override
	public void setEncrypted(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setNames(boolean)
	 */
	@Override
	public void setNames(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setProtected(boolean)
	 */
	@Override
	public void setProtected(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setReaders(boolean)
	 */
	@Override
	public void setReaders(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setSaveToDisk(boolean)
	 */
	@Override
	public void setSaveToDisk(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setSigned(boolean)
	 */
	@Override
	public void setSigned(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setSummary(boolean)
	 */
	@Override
	public void setSummary(boolean flag);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setValueCustomData(java.lang.Object)
	 */
	@Override
	public void setValueCustomData(Object userObj) throws IOException;

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setValueCustomData(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setValueCustomData(String dataTypeName, Object userObj) throws IOException;

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setValueCustomDataBytes(java.lang.String, byte[])
	 */
	@Override
	public void setValueCustomDataBytes(String dataTypeName, byte[] byteArray) throws IOException;

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setValueDouble(double)
	 */
	@Override
	public void setValueDouble(double value);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setValueInteger(int)
	 */
	@Override
	public void setValueInteger(int value);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setValues(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setValues(Vector values);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#setValueString(java.lang.String)
	 */
	@Override
	public void setValueString(String value);

	/* (non-Javadoc)
	 * @see lotus.domino.Item#transformXML(java.lang.Object, lotus.domino.XSLTResultTarget)
	 */
	@Override
	public void transformXML(Object style, XSLTResultTarget result);

	/* (non-Javadoc)
	 * @see lotus.domino.RichTextItem#update()
	 */
	@Override
	public void update();
}
