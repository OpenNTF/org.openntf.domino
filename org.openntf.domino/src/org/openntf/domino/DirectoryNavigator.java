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

import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface DirectoryNavigator.
 */
public interface DirectoryNavigator extends Base<lotus.domino.DirectoryNavigator>, lotus.domino.DirectoryNavigator,
		org.openntf.domino.ext.DirectoryNavigator, SessionDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#findFirstMatch()
	 */
	@Override
	public boolean findFirstMatch();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#findFirstName()
	 */
	@Override
	public long findFirstName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#findNextMatch()
	 */
	@Override
	public boolean findNextMatch();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#findNextName()
	 */
	@Override
	public long findNextName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#findNthMatch(long)
	 */
	@Override
	public boolean findNthMatch(long n);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#findNthName(int)
	 */
	@Override
	public long findNthName(int n);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#getCurrentItem()
	 */
	@Override
	public String getCurrentItem();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#getCurrentMatch()
	 */
	@Override
	public long getCurrentMatch();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#getCurrentMatches()
	 */
	@Override
	public long getCurrentMatches();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#getCurrentName()
	 */
	@Override
	public String getCurrentName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#getCurrentView()
	 */
	@Override
	public String getCurrentView();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#getFirstItemValue()
	 */
	@Override
	public Vector<Object> getFirstItemValue();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#getNextItemValue()
	 */
	@Override
	public Vector<Object> getNextItemValue();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#getNthItemValue(int)
	 */
	@Override
	public Vector<Object> getNthItemValue(int n);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#isMatchLocated()
	 */
	@Override
	public boolean isMatchLocated();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.DirectoryNavigator#isNameLocated()
	 */
	@Override
	public boolean isNameLocated();

}
