package org.openntf.domino;

import java.util.Vector;

import lotus.domino.ColorObject;

public interface RichTextTable extends Base<lotus.domino.RichTextTable>, lotus.domino.RichTextTable {

	@Override
	public void addRow();

	public void addRow(int count);

	@Override
	public void addRow(int count, int targetRow);

	@Override
	public ColorObject getAlternateColor();

	@Override
	public ColorObject getColor();

	@Override
	public int getColumnCount();

	@Override
	public int getRowCount();

	@Override
	public Vector<String> getRowLabels();

	@Override
	public int getStyle();

	@Override
	public boolean isRightToLeft();

	@Override
	public void remove();

	@Override
	public void removeRow();

	@Override
	public void removeRow(int count);

	@Override
	public void removeRow(int count, int targetRow);

	@Override
	public void setAlternateColor(ColorObject color);

	@Override
	public void setColor(ColorObject color);

	@Override
	public void setRightToLeft(boolean flag);

	@Override
	public void setRowLabels(Vector labels);

	@Override
	public void setStyle(int tableStyle);
}
