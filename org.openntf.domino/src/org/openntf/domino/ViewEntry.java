package org.openntf.domino;

import java.util.Vector;

public interface ViewEntry extends lotus.domino.ViewEntry, Base<lotus.domino.ViewEntry> {
	@Override
	public int getChildCount();

	@Override
	public int getColumnIndentLevel();

	@Override
	public Vector<Object> getColumnValues();

	@Override
	public int getDescendantCount();

	@Override
	public Document getDocument();

	@Override
	public int getFTSearchScore();

	@Override
	public int getIndentLevel();

	@Override
	public String getNoteID();

	@Override
	public int getNoteIDAsInt();

	@Override
	public Base<?> getParent();

	@Override
	public String getPosition(char separator);

	@Override
	public boolean getRead();

	@Override
	public boolean getRead(String userName);

	@Override
	public int getSiblingCount();

	@Override
	public String getUniversalID();

	@Override
	public boolean isCategory();

	@Override
	public boolean isConflict();

	@Override
	public boolean isDocument();

	@Override
	public boolean isPreferJavaDates();

	@Override
	public boolean isTotal();

	@Override
	public boolean isValid();

	@Override
	public void setPreferJavaDates(boolean flag);
}
