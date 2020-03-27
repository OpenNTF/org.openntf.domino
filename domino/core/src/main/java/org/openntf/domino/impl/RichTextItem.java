/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Document;
import org.openntf.domino.EmbeddedObject;
import org.openntf.domino.RichTextNavigator;
import org.openntf.domino.RichTextRange;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.TypeUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextItem.
 */
public class RichTextItem extends Item implements org.openntf.domino.RichTextItem, lotus.domino.Base {

	/**
	 * Instantiates a new richtext-item.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected RichTextItem(final lotus.domino.RichTextItem delegate, final Document parent) {
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
		markDirty();
		try {
			getDelegate().addNewLine();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#addNewLine(int)
	 */
	@Override
	public void addNewLine(final int count) {
		markDirty();
		try {
			getDelegate().addNewLine(count);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#addNewLine(int, boolean)
	 */
	@Override
	public void addNewLine(final int count, final boolean newParagraph) {
		markDirty();
		try {
			getDelegate().addNewLine(count, newParagraph);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#addPageBreak()
	 */
	@Override
	public void addPageBreak() {
		markDirty();
		try {
			getDelegate().addPageBreak();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#addPageBreak(lotus.domino.RichTextParagraphStyle)
	 */
	@Override
	public void addPageBreak(final lotus.domino.RichTextParagraphStyle pstyle) {
		markDirty();
		try {
			getDelegate().addPageBreak(toLotus(pstyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#addTab()
	 */
	@Override
	public void addTab() {
		markDirty();
		try {
			getDelegate().addTab();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#addTab(int)
	 */
	@Override
	public void addTab(final int count) {
		markDirty();
		try {
			getDelegate().addTab(count);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Database)
	 */
	@Override
	public void appendDocLink(final lotus.domino.Database db) {
		markDirty();
		try {
			getDelegate().appendDocLink(toLotus(db));
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Database, java.lang.String)
	 */
	@Override
	public void appendDocLink(final lotus.domino.Database db, final String comment) {
		markDirty();
		try {
			getDelegate().appendDocLink(toLotus(db), comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Database, java.lang.String, java.lang.String)
	 */
	@Override
	public void appendDocLink(final lotus.domino.Database db, final String comment, final String hotspotText) {
		markDirty();
		try {
			getDelegate().appendDocLink(toLotus(db), comment, hotspotText);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Document)
	 */
	@Override
	public void appendDocLink(final lotus.domino.Document doc) {
		markDirty();
		try {
			getDelegate().appendDocLink(toLotus(doc));
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Document, java.lang.String)
	 */
	@Override
	public void appendDocLink(final lotus.domino.Document doc, final String comment) {
		markDirty();
		try {
			getDelegate().appendDocLink(toLotus(doc), comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.Document, java.lang.String, java.lang.String)
	 */
	@Override
	public void appendDocLink(final lotus.domino.Document doc, final String comment, final String hotspotText) {
		markDirty();
		try {
			getDelegate().appendDocLink(toLotus(doc), comment, hotspotText);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.View)
	 */
	@Override
	public void appendDocLink(final lotus.domino.View view) {
		markDirty();
		try {
			getDelegate().appendDocLink(toLotus(view));
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.View, java.lang.String)
	 */
	@Override
	public void appendDocLink(final lotus.domino.View view, final String comment) {
		markDirty();
		try {
			getDelegate().appendDocLink(toLotus(view), comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendDocLink(lotus.domino.View, java.lang.String, java.lang.String)
	 */
	@Override
	public void appendDocLink(final lotus.domino.View view, final String comment, final String hotspotText) {
		markDirty();
		try {
			getDelegate().appendDocLink(toLotus(view), hotspotText);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendParagraphStyle(lotus.domino.RichTextParagraphStyle)
	 */
	@Override
	public void appendParagraphStyle(final lotus.domino.RichTextParagraphStyle pstyle) {
		markDirty();
		try {
			getDelegate().appendParagraphStyle(toLotus(pstyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendRTItem(lotus.domino.RichTextItem)
	 */
	@Override
	public void appendRTItem(final lotus.domino.RichTextItem rtitem) {
		markDirty();
		try {
			getDelegate().appendRTItem(toLotus(rtitem));
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendStyle(lotus.domino.RichTextStyle)
	 */
	@Override
	public void appendStyle(final lotus.domino.RichTextStyle rstyle) {
		markDirty();
		try {
			getDelegate().appendStyle(toLotus(rstyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendTable(int, int)
	 */
	@Override
	public void appendTable(final int rows, final int columns) {
		markDirty();
		try {
			getDelegate().appendTable(rows, columns);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendTable(int, int, java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void appendTable(final int rows, final int columns, final Vector labels) {
		markDirty();
		try {
			getDelegate().appendTable(rows, columns, labels);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendTable(int, int, java.util.Vector, int, java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void appendTable(final int rows, final int columns, final Vector labels, final int leftMargin, final Vector pstyles) {
		markDirty();
		try {
			getDelegate().appendTable(rows, columns, labels, leftMargin, TypeUtils.toLotus(pstyles));
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#appendText(java.lang.String)
	 */
	@Override
	public void appendText(final String text) {
		markDirty();
		try {
			getDelegate().appendText(text);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#beginInsert(lotus.domino.Base)
	 */
	@Override
	public void beginInsert(final lotus.domino.Base element) {
		markDirty();
		try {
			getDelegate().beginInsert(toLotus(element));
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#beginInsert(lotus.domino.Base, boolean)
	 */
	@Override
	public void beginInsert(final lotus.domino.Base element, final boolean after) {
		markDirty();
		try {
			getDelegate().beginInsert(toLotus(element), after);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#beginSection(java.lang.String)
	 */
	@Override
	public void beginSection(final String title) {
		markDirty();
		try {
			getDelegate().beginSection(title);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#beginSection(java.lang.String, lotus.domino.RichTextStyle)
	 */
	@Override
	public void beginSection(final String title, final lotus.domino.RichTextStyle titleStyle) {
		markDirty();
		try {
			getDelegate().beginSection(title, toLotus(titleStyle));
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#beginSection(java.lang.String, lotus.domino.RichTextStyle, lotus.domino.ColorObject, boolean)
	 */
	@Override
	public void beginSection(final String title, final lotus.domino.RichTextStyle titleStyle, final lotus.domino.ColorObject barColor,
			final boolean expand) {
		markDirty();
		try {
			getDelegate().beginSection(title, toLotus(titleStyle), toLotus(barColor), expand);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#compact()
	 */
	@Override
	public void compact() {
		markDirty();
		try {
			getDelegate().compact();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
			return fromLotus(getDelegate().createNavigator(), RichTextNavigator.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
			return fromLotus(getDelegate().createRange(), RichTextRange.SCHEMA, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#embedObject(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public EmbeddedObject embedObject(final int type, final String className, final String source, final String name) {
		markDirty();
		try {
			return fromLotus(getDelegate().embedObject(type, className, source, name), EmbeddedObject.SCHEMA, parent);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
		markDirty();
		try {
			getDelegate().endInsert();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#endSection()
	 */
	@Override
	public void endSection() {
		markDirty();
		try {
			getDelegate().endSection();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#getEmbeddedObject(java.lang.String)
	 */
	@Override
	public EmbeddedObject getEmbeddedObject(final String name) {
		try {
			return fromLotus(getDelegate().getEmbeddedObject(name), EmbeddedObject.SCHEMA, getAncestorDocument());
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
			return fromLotusAsVector(getDelegate().getEmbeddedObjects(), EmbeddedObject.SCHEMA, parent);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	// TODO: does this make sense here?
	public List<String> getAttachmentNames() {
		List<String> result = new ArrayList<String>();
		Vector<org.openntf.domino.EmbeddedObject> objects = getEmbeddedObjects();
		if (objects != null && !objects.isEmpty()) {
			for (org.openntf.domino.EmbeddedObject eo : objects) {
				if (eo.getType() == lotus.domino.EmbeddedObject.EMBED_ATTACHMENT) {
					result.add(eo.getName());
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#getFormattedText(boolean, int, int)
	 */
	@Override
	public String getFormattedText(final boolean tabStrip, final int lineLen, final int maxLen) {
		try {
			return getDelegate().getFormattedText(tabStrip, lineLen, maxLen);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.RichTextItem#getNotesFont(java.lang.String, boolean)
	 */
	@Override
	public int getNotesFont(final String faceName, final boolean addOnFail) {
		try {
			return getDelegate().getNotesFont(faceName, addOnFail);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
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
			DominoUtils.handleException(e, this);
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
		markDirty();
		try {
			getDelegate().update();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
		}
	}

	@Override
	public void markDirty() {
		getAncestorDocument().markDirty();
	}

	@Override
	public EmbeddedObject replaceAttachment(final String filename, final String sourcePath) {
		//		System.out.println("TEMP DEBUG replacing filename " + filename + " from source at " + sourcePath);
		EmbeddedObject result = null;
		RichTextNavigator navigator = this.createNavigator();
		EmbeddedObject eo = (EmbeddedObject) navigator.getFirstElement(RTELEM_TYPE_FILEATTACHMENT);
		boolean replaced = false;
		while (eo != null) {
			if (filename.equals(eo.getSource())) {
				//				System.out.println("TEMP DEBUG Found matching embeddedobject for name " + filename);
				beginInsert(eo, true);
				result = embedObject(org.openntf.domino.EmbeddedObject.Type.EMBED_ATTACHMENT.getValue(), "", sourcePath, "");
				endInsert();
				eo.remove();
				replaced = true;
				break;
			}
			eo = (EmbeddedObject) navigator.getNextElement(RTELEM_TYPE_FILEATTACHMENT);
		}
		if (!replaced) {
			result = embedObject(org.openntf.domino.EmbeddedObject.Type.EMBED_ATTACHMENT.getValue(), "", sourcePath, "");
		}
		return result;
	}

	@Override
	public boolean removeAttachment(final String filename) {
		boolean result = false;
		RichTextNavigator navigator = this.createNavigator();
		EmbeddedObject eo = (EmbeddedObject) navigator.getFirstElement(RTELEM_TYPE_FILEATTACHMENT);
		while (eo != null) {
			if (filename.equals(eo.getSource())) {
				eo.remove();
				result = true;
				break;
			}
			eo = (EmbeddedObject) navigator.getNextElement(RTELEM_TYPE_FILEATTACHMENT);
		}
		return result;
	}

	@Override
	public String convertToHTML(final Vector htmlOptions) {
		try {
			return getDelegate().convertToHTML(htmlOptions);
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}

	@Override
	public Vector<String> getHTMLReferences() {
		try {
			return getDelegate().getHTMLReferences();
		} catch (NotesException e) {
			DominoUtils.handleException(e, this);
			return null;
		}
	}
}
