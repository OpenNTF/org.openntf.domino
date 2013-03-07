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
	public Vector getTabs();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void setAlignment(int arg0);

	@Override
	public void setFirstLineLeftMargin(int arg0);

	@Override
	public void setInterLineSpacing(int arg0);

	@Override
	public void setLeftMargin(int arg0);

	@Override
	public void setPagination(int arg0);

	@Override
	public void setRightMargin(int arg0);

	@Override
	public void setSpacingAbove(int arg0);

	@Override
	public void setSpacingBelow(int arg0);

	@Override
	public void setTab(int arg0, int arg1);

	@Override
	public void setTabs(int arg0, int arg1, int arg2);

	@Override
	public void setTabs(int arg0, int arg1, int arg2, int arg3);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
