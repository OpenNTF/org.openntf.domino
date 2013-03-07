package org.openntf.domino;

import java.util.Vector;

import lotus.domino.Session;

public interface RichTextStyle extends Base<lotus.domino.RichTextStyle>, lotus.domino.RichTextStyle {

	@Override
	public int getBold();

	public int getColor();

	@Override
	public int getEffects();

	@Override
	public int getFont();

	@Override
	public int getFontSize();

	@Override
	public int getItalic();

	@Override
	public Session getParent();

	@Override
	public int getPassThruHTML();

	@Override
	public int getStrikeThrough();

	@Override
	public int getUnderline();

	@Override
	public boolean isDefault();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void setBold(int arg0);

	@Override
	public void setColor(int arg0);

	@Override
	public void setEffects(int arg0);

	@Override
	public void setFont(int arg0);

	@Override
	public void setFontSize(int arg0);

	@Override
	public void setItalic(int arg0);

	@Override
	public void setPassThruHTML(int arg0);

	@Override
	public void setStrikeThrough(int arg0);

	@Override
	public void setUnderline(int arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
