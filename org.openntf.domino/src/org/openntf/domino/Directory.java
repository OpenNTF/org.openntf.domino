package org.openntf.domino;

import java.util.Vector;

public interface Directory extends Base<lotus.domino.Directory>, lotus.domino.Directory {

	@Override
	public DirectoryNavigator createNavigator();

	@Override
	public void freeLookupBuffer();

	@Override
	public Vector<String> getAvailableItems();

	@Override
	public Vector<String> getAvailableNames();

	@Override
	public String getAvailableView();

	@Override
	public Vector<String> getMailInfo(String userName);

	@Override
	public Vector<String> getMailInfo(String userName, boolean getVersion, boolean errorOnMultipleMatches);

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
	public DirectoryNavigator lookupAllNames(String view, String item);

	@SuppressWarnings("unchecked")
	@Override
	public DirectoryNavigator lookupAllNames(String view, Vector items);

	@Override
	public DirectoryNavigator lookupNames(String view, String name, String item);

	@SuppressWarnings("unchecked")
	@Override
	public DirectoryNavigator lookupNames(String view, Vector names, Vector items, boolean partialMatches);

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
