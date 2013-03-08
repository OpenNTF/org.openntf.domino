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
	public boolean findNthMatch(long n);

	@Override
	public long findNthName(int n);

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
	public Vector getFirstItemValue();

	@Override
	public Vector getNextItemValue();

	@Override
	public Vector getNthItemValue(int n);

	@Override
	public boolean isMatchLocated();

	@Override
	public boolean isNameLocated();

}
