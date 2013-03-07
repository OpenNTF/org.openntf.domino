package org.openntf.domino;

import java.util.Vector;

import lotus.domino.RichTextStyle;

public interface RichTextRange extends Base<lotus.domino.RichTextRange>, lotus.domino.RichTextRange {

	@Override
	public lotus.domino.RichTextRange Clone();

	public int findandReplace(String arg0, String arg1);

	@Override
	public int findandReplace(String arg0, String arg1, long arg2);

	@Override
	public RichTextNavigator getNavigator();

	@Override
	public RichTextStyle getStyle();

	@Override
	public String getTextParagraph();

	@Override
	public String getTextRun();

	@Override
	public int getType();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void remove();

	@Override
	public void reset(boolean arg0, boolean arg1);

	@Override
	public void setBegin(lotus.domino.Base arg0);

	@Override
	public void setEnd(lotus.domino.Base arg0);

	@Override
	public void setStyle(RichTextStyle arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
