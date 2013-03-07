package org.openntf.domino;

import java.util.Vector;

public interface DirectoryNavigator extends Base<lotus.domino.DirectoryNavigator>, lotus.domino.DirectoryNavigator {

	@Override
	public boolean findFirstMatch();

	@Override
	public long findFirstName();

	@Override
	public boolean findNextMatch();

	@Override
	public long findNextName();

	@Override
	public boolean findNthMatch(long arg0);

	@Override
	public long findNthName(int arg0);

	@Override
	public String getCurrentItem();

	@Override
	public long getCurrentMatch();

	@Override
	public long getCurrentMatches();

	@Override
	public String getCurrentName();

	@Override
	public String getCurrentView();

	@Override
	public lotus.domino.DirectoryNavigator getDelegate();

	@Override
	public Vector getFirstItemValue();

	@Override
	public Vector getNextItemValue();

	@Override
	public Vector getNthItemValue(int arg0);

	@Override
	public boolean isMatchLocated();

	@Override
	public boolean isNameLocated();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
