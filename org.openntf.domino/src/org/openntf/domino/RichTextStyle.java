package org.openntf.domino;

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
	public void setBold(int value);

	@Override
	public void setColor(int value);

	@Override
	public void setEffects(int value);

	@Override
	public void setFont(int value);

	@Override
	public void setFontSize(int value);

	@Override
	public void setItalic(int value);

	@Override
	public void setPassThruHTML(int value);

	@Override
	public void setStrikeThrough(int value);

	@Override
	public void setUnderline(int value);

}
