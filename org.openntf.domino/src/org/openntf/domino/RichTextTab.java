package org.openntf.domino;


public interface RichTextTab extends Base<lotus.domino.RichTextTab>, lotus.domino.RichTextTab {

	@Override
	public void clear();

	public int getPosition();

	@Override
	public int getType();

}
