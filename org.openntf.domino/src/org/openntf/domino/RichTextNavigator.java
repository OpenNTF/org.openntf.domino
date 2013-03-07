package org.openntf.domino;

import java.util.Vector;

public interface RichTextNavigator extends Base<lotus.domino.RichTextNavigator>, lotus.domino.RichTextNavigator {

	@Override
	public lotus.domino.RichTextNavigator Clone();

	@Override
	public boolean findFirstElement(int arg0);

	@Override
	public boolean findFirstString(String arg0);

	@Override
	public boolean findFirstString(String arg0, int arg1);

	@Override
	public boolean findLastElement(int arg0);

	@Override
	public boolean findNextElement();

	@Override
	public boolean findNextElement(int arg0);

	@Override
	public boolean findNextElement(int arg0, int arg1);

	@Override
	public boolean findNextString(String arg0);

	@Override
	public boolean findNextString(String arg0, int arg1);

	@Override
	public boolean findNthElement(int arg0, int arg1);

	@Override
	public lotus.domino.RichTextNavigator getDelegate();

	@Override
	public lotus.domino.Base getElement();

	@Override
	public lotus.domino.Base getFirstElement(int arg0);

	@Override
	public lotus.domino.Base getLastElement(int arg0);

	@Override
	public lotus.domino.Base getNextElement();

	@Override
	public lotus.domino.Base getNextElement(int arg0);

	@Override
	public lotus.domino.Base getNextElement(int arg0, int arg1);

	@Override
	public lotus.domino.Base getNthElement(int arg0, int arg1);

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void setCharOffset(int arg0);

	@Override
	public void setPosition(lotus.domino.Base arg0);

	@Override
	public void setPositionAtEnd(lotus.domino.Base arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
