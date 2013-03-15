package org.openntf.domino;

public interface RichTextRange extends Base<lotus.domino.RichTextRange>, lotus.domino.RichTextRange {

	@Override
	public lotus.domino.RichTextRange Clone();

	public int findandReplace(String target, String replacement);

	@Override
	public int findandReplace(String target, String replacement, long options);

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
	public void remove();

	@Override
	public void reset(boolean begin, boolean end);

	@Override
	public void setBegin(lotus.domino.Base element);

	@Override
	public void setEnd(lotus.domino.Base element);

	@Override
	public void setStyle(lotus.domino.RichTextStyle style);

}
