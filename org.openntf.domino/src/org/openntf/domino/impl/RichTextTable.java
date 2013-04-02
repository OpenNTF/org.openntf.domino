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

import org.openntf.domino.ColorObject;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class RichTextTable.
 */
public class RichTextTable extends Base<org.openntf.domino.RichTextTable, lotus.domino.RichTextTable> implements
		org.openntf.domino.RichTextTable {

	/**
	 * Instantiates a new rich text table.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public RichTextTable(lotus.domino.RichTextTable delegate, org.openntf.domino.Base<?> parent) {
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
	public void addRow(int count) {
		try {
			getDelegate().addRow(count);
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
	public void addRow(int count, int targetRow) {
		try {
			getDelegate().addRow(count, targetRow);
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
			getDelegate().remove();
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
	public void removeRow(int count) {
		try {
			getDelegate().removeRow(count);
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
	public void removeRow(int count, int targetRow) {
		try {
			getDelegate().removeRow(count, targetRow);
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
	public void setAlternateColor(lotus.domino.ColorObject color) {
		try {
			getDelegate().setAlternateColor((lotus.domino.ColorObject) toLotus(color));
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
	public void setColor(lotus.domino.ColorObject color) {
		try {
			getDelegate().setColor((lotus.domino.ColorObject) toLotus(color));
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
	public void setRightToLeft(boolean flag) {
		try {
			getDelegate().setRightToLeft(flag);
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
	public void setRowLabels(Vector labels) {
		try {
			getDelegate().setRowLabels(toDominoFriendly(labels, this));
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
	public void setStyle(int tableStyle) {
		try {
			getDelegate().setStyle(tableStyle);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
