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

import java.util.Vector;

import org.openntf.domino.types.DocumentDescendant;

/**
 * The Interface RichTextItem.
 */
public interface RichTextItem extends lotus.domino.RichTextItem, org.openntf.domino.ext.RichTextItem, org.openntf.domino.Item,
		DocumentDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#addNewLine()
	 */
	@Override
	public void addNewLine();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#addNewLine(int)
	 */
	@Override
	public void addNewLine(int count);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#addNewLine(int, boolean)
	 */
	@Override
	public void addNewLine(int count, boolean newParagraph);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#addPageBreak()
	 */
	@Override
	public void addPageBreak();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#addPageBreak(lotus.domino.RichTextParagraphStyle)
	 */
	@Override
	public void addPageBreak(lotus.domino.RichTextParagraphStyle pstyle);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#addTab()
	 */
	@Override
	public void addTab();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#addTab(int)
	 */
	@Override
	public void addTab(int count);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.Database)
	 */
	@Override
	public void appendDocLink(lotus.domino.Database db);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.Database, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Database db, String comment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.Database, java.lang.String, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Database db, String comment, String hotspotText);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.Document)
	 */
	@Override
	public void appendDocLink(lotus.domino.Document doc);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.Document, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Document doc, String comment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.Document, java.lang.String, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Document doc, String comment, String hotspotText);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.View)
	 */
	@Override
	public void appendDocLink(lotus.domino.View view);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.View, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.View view, String comment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendDocLink(lotus.domino.View, java.lang.String, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.View view, String comment, String hotspotText);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendParagraphStyle(lotus.domino.RichTextParagraphStyle)
	 */
	@Override
	public void appendParagraphStyle(lotus.domino.RichTextParagraphStyle pstyle);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendRTItem(lotus.domino.RichTextItem)
	 */
	@Override
	public void appendRTItem(lotus.domino.RichTextItem rtitem);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendStyle(lotus.domino.RichTextStyle)
	 */
	@Override
	public void appendStyle(lotus.domino.RichTextStyle rstyle);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendTable(int, int)
	 */
	@Override
	public void appendTable(int rows, int columns);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendTable(int, int, java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void appendTable(int rows, int columns, Vector labels);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendTable(int, int, java.util.Vector, int, java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void appendTable(int rows, int columns, Vector labels, int leftMargin, Vector pstyles);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#appendText(java.lang.String)
	 */
	@Override
	public void appendText(String text);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#beginInsert(lotus.domino.Base)
	 */
	@Override
	public void beginInsert(lotus.domino.Base element);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#beginInsert(lotus.domino.Base, boolean)
	 */
	@Override
	public void beginInsert(lotus.domino.Base element, boolean after);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#beginSection(java.lang.String)
	 */
	@Override
	public void beginSection(String title);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#beginSection(java.lang.String, lotus.domino.RichTextStyle)
	 */
	@Override
	public void beginSection(String title, lotus.domino.RichTextStyle titleStyle);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#beginSection(java.lang.String, lotus.domino.RichTextStyle, lotus.domino.ColorObject, boolean)
	 */
	@Override
	public void beginSection(String title, lotus.domino.RichTextStyle titleStyle, lotus.domino.ColorObject barColor, boolean expand);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#compact()
	 */
	@Override
	public void compact();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#createNavigator()
	 */
	@Override
	public RichTextNavigator createNavigator();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#createRange()
	 */
	@Override
	public RichTextRange createRange();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#embedObject(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public EmbeddedObject embedObject(int type, String className, String source, String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#endInsert()
	 */
	@Override
	public void endInsert();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#endSection()
	 */
	@Override
	public void endSection();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#getEmbeddedObject(java.lang.String)
	 */
	@Override
	public EmbeddedObject getEmbeddedObject(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#getEmbeddedObjects()
	 */
	@Override
	public Vector<EmbeddedObject> getEmbeddedObjects();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#getFormattedText(boolean, int, int)
	 */
	@Override
	public String getFormattedText(boolean tabStrip, int lineLen, int maxLen);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#getNotesFont(java.lang.String, boolean)
	 */
	@Override
	public int getNotesFont(String faceName, boolean addOnFail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#getUnformattedText()
	 */
	@Override
	public String getUnformattedText();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextItem#update()
	 */
	@Override
	public void update();
}
