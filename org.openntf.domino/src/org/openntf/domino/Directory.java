/*
 * Copyright OpenNTF 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino;

import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Interface Directory.
 */
public interface Directory extends Base<lotus.domino.Directory>, lotus.domino.Directory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#createNavigator()
	 */
	@Override
	public DirectoryNavigator createNavigator();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#freeLookupBuffer()
	 */
	@Override
	public void freeLookupBuffer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#getAvailableItems()
	 */
	@Override
	public Vector<String> getAvailableItems();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#getAvailableNames()
	 */
	@Override
	public Vector<String> getAvailableNames();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#getAvailableView()
	 */
	@Override
	public String getAvailableView();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#getMailInfo(java.lang.String)
	 */
	@Override
	public Vector<String> getMailInfo(String userName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#getMailInfo(java.lang.String, boolean, boolean)
	 */
	@Override
	public Vector<String> getMailInfo(String userName, boolean getVersion, boolean errorOnMultipleMatches);

	public Session getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#getServer()
	 */
	@Override
	public String getServer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#isGroupAuthorizationOnly()
	 */
	@Override
	public boolean isGroupAuthorizationOnly();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#isLimitMatches()
	 */
	@Override
	public boolean isLimitMatches();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#isPartialMatches()
	 */
	@Override
	public boolean isPartialMatches();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#isSearchAllDirectories()
	 */
	@Override
	public boolean isSearchAllDirectories();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#isTrustedOnly()
	 */
	@Override
	public boolean isTrustedOnly();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#isUseContextServer()
	 */
	@Override
	public boolean isUseContextServer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#lookupAllNames(java.lang.String, java.lang.String)
	 */
	@Override
	public DirectoryNavigator lookupAllNames(String view, String item);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#lookupAllNames(java.lang.String, java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DirectoryNavigator lookupAllNames(String view, Vector items);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#lookupNames(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public DirectoryNavigator lookupNames(String view, String name, String item);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#lookupNames(java.lang.String, java.util.Vector, java.util.Vector, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DirectoryNavigator lookupNames(String view, Vector names, Vector items, boolean partialMatches);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#setGroupAuthorizationOnly(boolean)
	 */
	@Override
	public void setGroupAuthorizationOnly(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#setLimitMatches(boolean)
	 */
	@Override
	public void setLimitMatches(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#setSearchAllDirectories(boolean)
	 */
	@Override
	public void setSearchAllDirectories(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#setTrustedOnly(boolean)
	 */
	@Override
	public void setTrustedOnly(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Directory#setUseContextServer(boolean)
	 */
	@Override
	public void setUseContextServer(boolean flag);

}
