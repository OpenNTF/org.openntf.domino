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

import java.util.Vector;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.domino.ColorObject;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.types.DocumentDescendant;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextTable.
 */
public class RichTextTable extends Base<org.openntf.domino.RichTextTable, lotus.domino.RichTextTable> implements
		org.openntf.domino.RichTextTable {
	private static final Logger log_ = Logger.getLogger(RichTextTable.class.getName());

	/**
	 * Instantiates a new rich text table.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public RichTextTable(final lotus.domino.RichTextTable delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#addRow()
	 */
	@Override
	public void addRow() {
		try {
			getDelegate().addRow();
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#addRow(int)
	 */
	@Override
	public void addRow(final int count) {
		try {
			getDelegate().addRow(count);
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#addRow(int, int)
	 */
	@Override
	public void addRow(final int count, final int targetRow) {
		try {
			getDelegate().addRow(count, targetRow);
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#getAlternateColor()
	 */
	@Override
	public ColorObject getAlternateColor() {
		try {
			return Factory.fromLotus(getDelegate().getAlternateColor(), ColorObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#getColor()
	 */
	@Override
	public ColorObject getColor() {
		try {
			return Factory.fromLotus(getDelegate().getColor(), ColorObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		try {
			return getDelegate().getColumnCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public RichTextItem getParent() {
		return (RichTextItem) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#getRowCount()
	 */
	@Override
	public int getRowCount() {
		try {
			return getDelegate().getRowCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#getRowLabels()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getRowLabels() {
		try {
			return getDelegate().getRowLabels();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#getStyle()
	 */
	@Override
	public int getStyle() {
		try {
			return getDelegate().getStyle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#isRightToLeft()
	 */
	@Override
	public boolean isRightToLeft() {
		try {
			return getDelegate().isRightToLeft();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#remove()
	 */
	@Override
	public void remove() {
		try {
			getDelegate().remove();
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#removeRow()
	 */
	@Override
	public void removeRow() {
		try {
			getDelegate().removeRow();
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#removeRow(int)
	 */
	@Override
	public void removeRow(final int count) {
		try {
			getDelegate().removeRow(count);
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#removeRow(int, int)
	 */
	@Override
	public void removeRow(final int count, final int targetRow) {
		try {
			getDelegate().removeRow(count, targetRow);
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#setAlternateColor(lotus.domino.ColorObject)
	 */
	@Override
	public void setAlternateColor(final lotus.domino.ColorObject color) {
		try {
			getDelegate().setAlternateColor((lotus.domino.ColorObject) toLotus(color));
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#setColor(lotus.domino.ColorObject)
	 */
	@Override
	public void setColor(final lotus.domino.ColorObject color) {
		try {
			getDelegate().setColor((lotus.domino.ColorObject) toLotus(color));
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#setRightToLeft(boolean)
	 */
	@Override
	public void setRightToLeft(final boolean flag) {
		try {
			getDelegate().setRightToLeft(flag);
			markDirty();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#setRowLabels(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setRowLabels(final Vector labels) {
		try {
			java.util.Vector v = toDominoFriendly(labels, this);
			getDelegate().setRowLabels(v);
			markDirty();
			s_recycle(v);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.RichTextTable#setStyle(int)
	 */
	@Override
	public void setStyle(final int tableStyle) {
		try {
			getDelegate().setStyle(tableStyle);
			markDirty();
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
		return (Document) ((DocumentDescendant) this.getParent()).getAncestorDocument();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public Database getAncestorDatabase() {
		return this.getAncestorDocument().getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getAncestorDocument().getAncestorSession();
	}
}
