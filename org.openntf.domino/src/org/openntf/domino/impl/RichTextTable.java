package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.ColorObject;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class RichTextTable extends Base<org.openntf.domino.RichTextTable, lotus.domino.RichTextTable> implements
		org.openntf.domino.RichTextTable {

	public RichTextTable(lotus.domino.RichTextTable delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public void addRow() {
		try {
			getDelegate().addRow();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addRow(int count) {
		try {
			getDelegate().addRow(count);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addRow(int count, int targetRow) {
		try {
			getDelegate().addRow(count, targetRow);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public ColorObject getAlternateColor() {
		try {
			return Factory.fromLotus(getDelegate().getAlternateColor(), ColorObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public ColorObject getColor() {
		try {
			return Factory.fromLotus(getDelegate().getColor(), ColorObject.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getColumnCount() {
		try {
			return getDelegate().getColumnCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getRowCount() {
		try {
			return getDelegate().getRowCount();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

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

	@Override
	public int getStyle() {
		try {
			return getDelegate().getStyle();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public boolean isRightToLeft() {
		try {
			return getDelegate().isRightToLeft();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void removeRow() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void removeRow(int count) {
		try {
			getDelegate().removeRow(count);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void removeRow(int count, int targetRow) {
		try {
			getDelegate().removeRow(count, targetRow);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setAlternateColor(lotus.domino.ColorObject color) {
		try {
			getDelegate().setAlternateColor((lotus.domino.ColorObject) toLotus(color));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setColor(lotus.domino.ColorObject color) {
		try {
			getDelegate().setColor((lotus.domino.ColorObject) toLotus(color));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setRightToLeft(boolean flag) {
		try {
			getDelegate().setRightToLeft(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setRowLabels(Vector labels) {
		try {
			getDelegate().setRowLabels(labels);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setStyle(int tableStyle) {
		try {
			getDelegate().setStyle(tableStyle);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
