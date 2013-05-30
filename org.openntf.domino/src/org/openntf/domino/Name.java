/*
 * Copyright 2013
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
import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface Name represents a user or server name.
 * <p>
 * A canonical hierarchical name is a series of components separated by slashes. Each component starts with a keyword and equals sign.
 * Domino names use the following components: CN (common name), OU (organizational unit - up to four are permitted), O (organization), and C
 * (country). Domino names do not use the following components but this class recognizes them: G (given name), I (initial), S (surname), Q
 * (generation; for example, "Jr"), A (ADMD name), and P (PRMD name).
 * <p>
 * The following is an example of a Domino hierarchical name in canonical format. It uses two of the available organizational units. The
 * hierarchy is right to left, so East is organizational unit 1 and Sales is organizational unit 2.
 * <p>
 * <code>CN=John B Goode/OU=Sales/OU=East/O=Acme/C=US</code>
 * <p>
 * An abbreviated name is a series of components separated by slashes. The components are not identified by keyword but depend on order for
 * identification. The above name in abbreviated format is as follows:
 * <p>
 * <code>John B Goode/Sales/East/Acme/US</code>
 * <p>
 * This class does not abbreviate a canonical name if the abbreviation would be ambiguous. For example, <code>CN=John B Goode/OU=East</code>
 * cannot be abbreviated because East would appear to be the organization (rather than an organizational unit). This class also does not
 * abbreviate a canonical name if it contains any of the components not used in Domino names.
 * <p>
 * A specification of an abbreviated name can include only those components used in Domino names. The components must be in proper sequence.
 * The following are acceptable:
 * <ul>
 * <li>common name/country
 * <li>common name/organization (unless the name of the organization is identical to a country code)
 * <li>common name/organization/country
 * <li>common name/organizational unit/organization/country (up to four organizational units)
 * </ul>
 * <p>
 * A common name by itself is interpreted as a flat name. A common name followed by a slash receives the organizational and country
 * information of the effective user.
 * <p>
 * A name that conforms to RFC 821 or RFC 822 is interpreted as an Internet address. Examples of Internet addresses are as follows:
 * <ul>
 * <li>jbg@us.acme.com
 * <li>"John B Goode" <jbg@us.acme.com>
 * <li>"John B Goode" <jbg@us.acme.com> (Sales) (East)
 * </ul>
 */
public interface Name extends Base<lotus.domino.Name>, lotus.domino.Name, org.openntf.domino.ext.Name, Encapsulated, SessionDescendant {

	/**
	 * A hierarchical name in abbreviated form.
	 * 
	 * @return The abbreviated name. If the abbreviated name would be ambiguous or contains non-domino components then the canonical name is
	 *         returned.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getAbbreviated();

	/**
	 * IP address in the format based on RFC 821 Address Format Syntax.
	 * <p>
	 * The RFC 821 address is a name followed by an at sign followed by an organization, for example, <code>jbgoode@acme.us.com</code>. In
	 * an RFC 822 address, it is the part enclosed in pointed brackets.
	 * 
	 * @return The RFC821 Address. If there is none then an empty string is returned.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getAddr821();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Name#getAddr822Comment1()
	 */
	@Override
	public String getAddr822Comment1();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Name#getAddr822Comment2()
	 */
	@Override
	public String getAddr822Comment2();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Name#getAddr822Comment3()
	 */
	@Override
	public String getAddr822Comment3();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Name#getAddr822LocalPart()
	 */
	@Override
	public String getAddr822LocalPart();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Name#getAddr822Phrase()
	 */
	@Override
	public String getAddr822Phrase();

	/**
	 * Gets the administration management domain name (ADMD) component of a hierarchical name (A=).
	 * <p>
	 * This property returns an empty string for a hierarchical name with no ADMD component.
	 * <p>
	 * This property returns an empty string for an Internet or flat name.
	 * <p>
	 * A name created through Domino administration does not contain a ADMD component.
	 * 
	 * @return The A= part of the name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getADMD();

	/**
	 * Get the hierarchical name in canonical form.
	 * <p>
	 * This property returns an Internet or flat name as is.
	 * 
	 * @return A hierarchical name in canonical form.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getCanonical();

	/**
	 * The common name component (CN=) of a hierarchical name.
	 * 
	 * @return The common name component of a hierarchical name.
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getCommon();

	/**
	 * Gets the country component of a hierarchical name (C=).
	 * <p>
	 * This property returns an empty string for a hierarchical name with no country component.
	 * <p>
	 * This property returns an empty string for an Internet or flat name.
	 * 
	 * @return The C= part of the name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getCountry();

	/**
	 * Gets the generation component of a hierarchical name (Q=).
	 * <p>
	 * This property returns an empty string for a hierarchical name with no generation component.
	 * <p>
	 * This property returns an empty string for an Internet or flat name.
	 * <p>
	 * A name created through Domino administration does not contain a generation component.
	 * 
	 * @return The Q= part of the name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getGeneration();

	/**
	 * Gets the given name component of a hierarchical name (G=).
	 * <p>
	 * This property returns an empty string for a hierarchical name with no given name component.
	 * <p>
	 * This property returns an empty string for an Internet or flat name.
	 * <p>
	 * A name created through Domino administration does not contain a given name component.
	 * 
	 * @return The G= part of the name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getGiven();

	/**
	 * Gets the initials component of a hierarchical name (I=).
	 * <p>
	 * This property returns an empty string for a hierarchical name with no initials component.
	 * <p>
	 * This property returns an empty string for an Internet or flat name.
	 * <p>
	 * A name created through Domino administration does not contain an initials component.
	 * 
	 * @return The I= part of the name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getInitials();

	/**
	 * The following components of a hierarchical name in the order shown separated by backslashes: country or region, organization,
	 * organizational unit 1, organizational unit 2, organizational unit 3, and organizational unit 4.
	 * <p>
	 * This property returns an empty string for an Internet or flat name.
	 * 
	 * @return The keyword part of the name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getKeyword();

	/**
	 * The language code associated with an alternate user name.
	 * <p>
	 * This property returns an empty string if the language is undefined.
	 * <p>
	 * A primary name created through Domino� administration does not have a language code; an alternate name may have a language code.
	 * 
	 * @return The language code of the alternative name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getLanguage();

	/**
	 * Gets the organization component of a hierarchical name (O=).
	 * <p>
	 * This property returns an empty string for a hierarchical name with no organization component.
	 * <p>
	 * This property returns an empty string for an Internet or flat name.
	 * 
	 * @return The O= part of the name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getOrganization();

	/**
	 * Gets the first organizational unit component of a hierarchical name (OU=).
	 * <p>
	 * This property returns an empty string for a hierarchical name with no organizational unit 1 component.
	 * <p>
	 * This property returns an empty string for an Internet or flat name.
	 * <p>
	 * Organizational unit 1 is the rightmost organizational unit in the name.
	 * 
	 * @return The OU= part of the name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getOrgUnit1();

	/**
	 * Gets the second organizational unit component of a hierarchical name (OU=).
	 * <p>
	 * This property returns an empty string for a hierarchical name with no organizational unit 2 component.
	 * <p>
	 * This property returns an empty string for an Internet or flat name.
	 * <p>
	 * Organizational unit 2 is the second rightmost organizational unit in the name.
	 * 
	 * @return The OU= part of the name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getOrgUnit2();

	/**
	 * Gets the third organizational unit component of a hierarchical name (OU=).
	 * <p>
	 * This property returns an empty string for a hierarchical name with no organizational unit 3 component.
	 * <p>
	 * This property returns an empty string for an Internet or flat name.
	 * <p>
	 * Organizational unit 3 is the third rightmost organizational unit in the name.
	 * 
	 * @return The OU= part of the name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getOrgUnit3();

	/**
	 * Gets the fourth organizational unit component of a hierarchical name (OU=).
	 * <p>
	 * This property returns an empty string for a hierarchical name with no organizational unit 4 component.
	 * <p>
	 * This property returns an empty string for an Internet or flat name.
	 * <p>
	 * Organizational unit 4 is the fourth rightmost organizational unit in the name.
	 * 
	 * @return The OU= part of the name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getOrgUnit4();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Name#getParent()
	 */
	@Override
	public org.openntf.domino.Session getParent();

	/**
	 * Gets the private management domain name (PRMD) component of a hierarchical name (P=).
	 * <p>
	 * This property returns an empty string for a hierarchical name with no PRMD component.
	 * <p>
	 * This property returns an empty string for an Internet or flat name.
	 * <p>
	 * A name created through Domino administration does not contain a PRMD component.
	 * 
	 * @return The P= part of the name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getPRMD();

	/**
	 * Gets the surname component of a hierarchical name (S=).
	 * <p>
	 * This property returns an empty string for a hierarchical name with no surname component.
	 * <p>
	 * This property returns an empty string for an Internet or flat name.
	 * <p>
	 * A name created through Domino administration does not contain a surname component.
	 * 
	 * @return The S= part of the name
	 * @since lotus.domino 4.5.0
	 */
	@Override
	public String getSurname();

	/**
	 * Indicates if a name is hierarchical.
	 * 
	 * @return Returns <code>true</code> if the name is hierarchical, otherwise returns <code>false</code>.
	 * @since lotus.domino 4.5.0
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Base#recycle()
	 */
	@Deprecated
	public void recycle();
}
