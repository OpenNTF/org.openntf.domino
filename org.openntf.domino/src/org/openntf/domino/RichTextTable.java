package org.openntf.domino;

import java.util.Vector;

import lotus.domino.ColorObject;

public interface RichTextTable extends Base<lotus.domino.RichTextTable>, lotus.domino.RichTextTable {

	@Override
	public void addRow();

	public void addRow(int arg0);

	@Override
	public void addRow(int arg0, int arg1);

	@Override
	public ColorObject getAlternateColor();

	@Override
	public ColorObject getColor();

	@Override
	public int getColumnCount();

	@Override
	public lotus.domino.RichTextTable getDelegate();

	@Override
	public int getRowCount();

	@Override
	public Vector getRowLabels();

	@Override
	public int getStyle();

	@Override
	public boolean isRightToLeft();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void remove();

	@Override
	public void removeRow();

	@Override
	public void removeRow(int arg0);

	@Override
	public void removeRow(int arg0, int arg1);

	@Override
	public void setAlternateColor(ColorObject arg0);

	@Override
	public void setColor(ColorObject arg0);

	@Override
	public void setRightToLeft(boolean arg0);

	@Override
	public void setRowLabels(Vector arg0);

	@Override
	public void setStyle(int arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();
}
