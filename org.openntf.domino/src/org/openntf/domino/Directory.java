package org.openntf.domino;

import java.util.Vector;

import lotus.domino.DirectoryNavigator;

public interface Directory extends Base<lotus.domino.Directory>, lotus.domino.Directory {

	@Override
	public DirectoryNavigator createNavigator();

	@Override
	public void freeLookupBuffer();

	@Override
	public java.util.Vector getAvailableItems();

	@Override
	public Vector getAvailableNames();

	@Override
	public String getAvailableView();

	@Override
	public lotus.domino.Directory getDelegate();

	@Override
	public Vector getMailInfo(String arg0);

	@Override
	public Vector getMailInfo(String arg0, boolean arg1, boolean arg2);

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
	public DirectoryNavigator lookupAllNames(String arg0, String arg1);

	@Override
	public DirectoryNavigator lookupAllNames(String arg0, Vector arg1);

	@Override
	public DirectoryNavigator lookupNames(String arg0, String arg1, String arg2);

	@Override
	public DirectoryNavigator lookupNames(String arg0, Vector arg1, Vector arg2, boolean arg3);

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void setGroupAuthorizationOnly(boolean arg0);

	@Override
	public void setLimitMatches(boolean arg0);

	@Override
	public void setSearchAllDirectories(boolean arg0);

	@Override
	public void setTrustedOnly(boolean arg0);

	@Override
	public void setUseContextServer(boolean arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
