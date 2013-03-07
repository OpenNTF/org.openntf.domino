package org.openntf.domino;

import java.util.Vector;

import lotus.domino.RichTextStyle;

public interface RichTextSection extends Base<lotus.domino.RichTextSection>, lotus.domino.RichTextSection {

	@Override
	public ColorObject getBarColor();

	public lotus.domino.RichTextSection getDelegate();

	@Override
	public String getTitle();

	@Override
	public RichTextStyle getTitleStyle();

	@Override
	public boolean isExpanded();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void remove();

	@Override
	public void setBarColor(lotus.domino.ColorObject arg0);

	@Override
	public void setExpanded(boolean arg0);

	@Override
	public void setTitle(String arg0);

	@Override
	public void setTitleStyle(RichTextStyle arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
