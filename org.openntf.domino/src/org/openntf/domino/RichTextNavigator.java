package org.openntf.domino;

public interface RichTextNavigator extends Base<lotus.domino.RichTextNavigator>, lotus.domino.RichTextNavigator {

	@Override
	public lotus.domino.RichTextNavigator Clone();

	@Override
	public boolean findFirstElement(int type);

	@Override
	public boolean findFirstString(String target);

	@Override
	public boolean findFirstString(String target, int options);

	@Override
	public boolean findLastElement(int type);

	@Override
	public boolean findNextElement();

	@Override
	public boolean findNextElement(int type);

	@Override
	public boolean findNextElement(int type, int occurrence);

	@Override
	public boolean findNextString(String target);

	@Override
	public boolean findNextString(String target, int options);

	@Override
	public boolean findNthElement(int type, int occurrence);

	@Override
	public lotus.domino.Base getElement();

	@Override
	public lotus.domino.Base getFirstElement(int type);

	@Override
	public lotus.domino.Base getLastElement(int type);

	@Override
	public lotus.domino.Base getNextElement();

	@Override
	public lotus.domino.Base getNextElement(int type);

	@Override
	public lotus.domino.Base getNextElement(int type, int occurrence);

	@Override
	public lotus.domino.Base getNthElement(int type, int occurrence);

	@Override
	public void setCharOffset(int offset);

	@Override
	public void setPosition(lotus.domino.Base element);

	@Override
	public void setPositionAtEnd(lotus.domino.Base element);

}
