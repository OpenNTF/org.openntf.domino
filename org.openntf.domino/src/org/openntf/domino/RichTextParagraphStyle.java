package org.openntf.domino;

import java.util.Vector;

public interface RichTextParagraphStyle extends Base<lotus.domino.RichTextParagraphStyle>, lotus.domino.RichTextParagraphStyle {

	@Override
	public void clearAllTabs();

	@Override
	public int getAlignment();

	@Override
	public int getFirstLineLeftMargin();

	@Override
	public int getInterLineSpacing();

	@Override
	public int getLeftMargin();

	@Override
	public int getPagination();

	@Override
	public int getRightMargin();

	@Override
	public int getSpacingAbove();

	@Override
	public int getSpacingBelow();

	@Override
	public Vector<lotus.domino.RichTextTab> getTabs();

	@Override
	public void setAlignment(int value);

	@Override
	public void setFirstLineLeftMargin(int value);

	@Override
	public void setInterLineSpacing(int value);

	@Override
	public void setLeftMargin(int value);

	@Override
	public void setPagination(int value);

	@Override
	public void setRightMargin(int value);

	@Override
	public void setSpacingAbove(int value);

	@Override
	public void setSpacingBelow(int value);

	@Override
	public void setTab(int position, int type);

	@Override
	public void setTabs(int count, int startPos, int interval);

	@Override
	public void setTabs(int count, int startPos, int interval, int type);

}
