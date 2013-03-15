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

import org.openntf.domino.types.Encapsulated;

// TODO: Auto-generated Javadoc
/**
 * The Interface Name.
 */
public interface Name extends Base<lotus.domino.Name>, lotus.domino.Name, Encapsulated {

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getAbbreviated()
	 */
	@Override
	public String getAbbreviated();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getAddr821()
	 */
	@Override
	public String getAddr821();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getAddr822Comment1()
	 */
	@Override
	public String getAddr822Comment1();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getAddr822Comment2()
	 */
	@Override
	public String getAddr822Comment2();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getAddr822Comment3()
	 */
	@Override
	public String getAddr822Comment3();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getAddr822LocalPart()
	 */
	@Override
	public String getAddr822LocalPart();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getAddr822Phrase()
	 */
	@Override
	public String getAddr822Phrase();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getADMD()
	 */
	@Override
	public String getADMD();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getCanonical()
	 */
	@Override
	public String getCanonical();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getCommon()
	 */
	@Override
	public String getCommon();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getCountry()
	 */
	@Override
	public String getCountry();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getGeneration()
	 */
	@Override
	public String getGeneration();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getGiven()
	 */
	@Override
	public String getGiven();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getInitials()
	 */
	@Override
	public String getInitials();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getKeyword()
	 */
	@Override
	public String getKeyword();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getLanguage()
	 */
	@Override
	public String getLanguage();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getOrganization()
	 */
	@Override
	public String getOrganization();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getOrgUnit1()
	 */
	@Override
	public String getOrgUnit1();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getOrgUnit2()
	 */
	@Override
	public String getOrgUnit2();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getOrgUnit3()
	 */
	@Override
	public String getOrgUnit3();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getOrgUnit4()
	 */
	@Override
	public String getOrgUnit4();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getParent()
	 */
	@Override
	public org.openntf.domino.Session getParent();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getPRMD()
	 */
	@Override
	public String getPRMD();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#getSurname()
	 */
	@Override
	public String getSurname();

	/* (non-Javadoc)
	 * @see lotus.domino.Name#isHierarchical()
	 */
	@Override
	public boolean isHierarchical();

	/**
	 * Equals.
	 * 
	 * @param obj
	 *            the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj);

	/**
	 * Hash code.
	 * 
	 * @return the int
	 */
	@Override
	public int hashCode();

	/**
	 * To string.
	 * 
	 * @return the string
	 */
	@Override
	public String toString();

	/* (non-Javadoc)
	 * @see lotus.domino.Base#recycle()
	 */
	@Deprecated
	public void recycle();
}
