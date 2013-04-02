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

import java.util.Vector;

import org.openntf.domino.types.DatabaseDescendant;

// TODO: Auto-generated Javadoc
/**
 * The Interface RichTextTable.
 */
public interface RichTextTable extends Base<lotus.domino.RichTextTable>, lotus.domino.RichTextTable, DatabaseDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#addRow()
	 */
	@Override
	public void addRow();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#addRow(int)
	 */
	public void addRow(int count);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#addRow(int, int)
	 */
	@Override
	public void addRow(int count, int targetRow);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#getAlternateColor()
	 */
	@Override
	public ColorObject getAlternateColor();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#getColor()
	 */
	@Override
	public ColorObject getColor();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#getColumnCount()
	 */
	@Override
	public int getColumnCount();

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public RichTextItem getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#getRowCount()
	 */
	@Override
	public int getRowCount();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#getRowLabels()
	 */
	@Override
	public Vector<String> getRowLabels();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#getStyle()
	 */
	@Override
	public int getStyle();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#isRightToLeft()
	 */
	@Override
	public boolean isRightToLeft();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#remove()
	 */
	@Override
	public void remove();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#removeRow()
	 */
	@Override
	public void removeRow();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#removeRow(int)
	 */
	@Override
	public void removeRow(int count);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#removeRow(int, int)
	 */
	@Override
	public void removeRow(int count, int targetRow);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#setAlternateColor(lotus.domino.ColorObject)
	 */
	@Override
	public void setAlternateColor(lotus.domino.ColorObject color);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#setColor(lotus.domino.ColorObject)
	 */
	@Override
	public void setColor(lotus.domino.ColorObject color);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#setRightToLeft(boolean)
	 */
	@Override
	public void setRightToLeft(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#setRowLabels(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setRowLabels(Vector labels);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.RichTextTable#setStyle(int)
	 */
	@Override
	public void setStyle(int tableStyle);
}
