package org.openntf.domino;

public interface RichTextSection extends Base<lotus.domino.RichTextSection>, lotus.domino.RichTextSection {

	@Override
	public ColorObject getBarColor();

	@Override
	public String getTitle();

	@Override
	public RichTextStyle getTitleStyle();

	@Override
	public boolean isExpanded();

	@Override
	public void remove();

	@Override
	public void setBarColor(lotus.domino.ColorObject color);

	@Override
	public void setExpanded(boolean flag);

	@Override
	public void setTitle(String title);

	@Override
	public void setTitleStyle(lotus.domino.RichTextStyle style);

}
