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

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextItem.
 */
public class RichTextItem extends Item implements org.openntf.domino.RichTextItem {

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
	 * @see org.openntf.domino.impl.Base#getDelegate()
	 */
	@Override
	protected lotus.domino.RichTextItem getDelegate() {
		return (lotus.domino.RichTextItem) super.getDelegate();
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Database, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Database db, String comment) {
		try {
			getDelegate().appendDocLink((lotus.domino.Database) toLotus(db), comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Database, java.lang.String, java.lang.String)
	 */
	@Override
	public void appendDocLink(lotus.domino.Database db, String comment, String hotspotText) {
		try {
			getDelegate().appendDocLink((lotus.domino.Database) toLotus(db), comment, hotspotText);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextItem#createRange()
	 */
	@Override
	public RichTextRange createRange() {
		try {
			return Factory.fromLotus(getDelegate().createRange(), RichTextRange.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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
