package org.openntf.domino;

import java.util.Vector;

public interface Directory extends Base<lotus.domino.Directory>, lotus.domino.Directory {

	@Override
	// TODO Switch to new class
	public lotus.domino.DirectoryNavigator createNavigator();

	@Override
	public void freeLookupBuffer();

	@Override
	public Vector getAvailableItems();

	@Override
	public Vector getAvailableNames();

	@Override
	public String getAvailableView();

	@Override
	public Vector getMailInfo(String userName);

	@Override
	public Vector getMailInfo(String userName, boolean getVersion, boolean errorOnMultipleMatches);

	@Override
	public String getServer();

	@Override
	public boolean isGroupAuthorizationOnly();

	@Override
	public boolean isLimitMatches();

	@Override
	public boolean isPartialMatches();

	@Override
	public boolean isSearchAllDirectories();

	@Override
	public boolean isTrustedOnly();

	@Override
	public boolean isUseContextServer();

	@Override
	// TODO Switch to new class
	public lotus.domino.DirectoryNavigator lookupAllNames(String view, String item);

	@Override
	// TODO Switch to new class
	public lotus.domino.DirectoryNavigator lookupAllNames(String view, Vector items);

	@Override
	// TODO Switch to new class
	public lotus.domino.DirectoryNavigator lookupNames(String view, String name, String item);

	@Override
	// TODO Switch to new class
	public lotus.domino.DirectoryNavigator lookupNames(String arg0, Vector names, Vector items, boolean partialMatches);

	@Override
	public void setGroupAuthorizationOnly(boolean flag);

	@Override
	public void setLimitMatches(boolean flag);

	@Override
	public void setSearchAllDirectories(boolean flag);

	@Override
	public void setTrustedOnly(boolean flag);

	@Override
	public void setUseContextServer(boolean flag);

}
