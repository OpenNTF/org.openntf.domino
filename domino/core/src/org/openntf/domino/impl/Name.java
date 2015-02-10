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
package org.openntf.domino.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.TreeSet;
//import java.util.logging.Logger;

import lotus.domino.NotesException;
import lotus.notes.addins.DominoServer;

import org.openntf.arpa.NamePartsMap;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.Names;
import org.openntf.domino.utils.Strings;

/**
 * The Class Name.
 */

public class Name extends BaseNonThreadSafe<org.openntf.domino.Name, lotus.domino.Name, Session> implements org.openntf.domino.Name,
		Comparable<Name>, Cloneable {
	//	private static final Logger log_ = Logger.getLogger(Name.class.getName());
	private static final long serialVersionUID = 1L;
	private NamePartsMap _namePartsMap;
	private String source_;
	private String lang_;
	private Boolean isParsed_ = false;

	/*
	 * Deprecated, but needed for Externalization
	 */
	@Deprecated
	public Name() {
		super(null, Factory.getSession(SessionType.CURRENT), NOTES_NAME);
	}

	protected Name(final Session sess) {
		super(null, sess, NOTES_NAME);
	}

	//	/**
	//	 * Optional Constructor
	//	 * 
	//	 * @param session
	//	 *            Session used for Name processing
	//	 */
	//	public Name(final Session session) {
	//		super(null, session, null, 0, NOTES_NAME);
	//		this.initialize(session.getEffectiveUserName());
	//	}

	//	/**
	//	 * Optional Constructor to clone a name
	//	 * 
	//	 * @param name
	//	 *            the name to clone
	//	 */
	//	public Name(final Session session, final lotus.domino.Name name) throws NotesException {
	//		super(null, session, null, 0, NOTES_NAME);
	//		this.initialize(name);
	//	}

	//	/**
	//	 * Optional Constructor
	//	 * 
	//	 * @param session
	//	 *            Session used for Name processing
	//	 * 
	//	 * @param name
	//	 *            String used to construct the Name object
	//	 */
	//	public Name(final Session session, final String name) {
	//		super(null, session, null, 0, NOTES_NAME);
	//		this.initialize(name);
	//	}

	//	/**
	//	 * Optional Constructor
	//	 * 
	//	 * @param name
	//	 *            String used to construct the Name object
	//	 */
	//	public Name(final String name) {
	//		super(null, Factory.getSession(SessionType.CURRENT), null, 0, NOTES_NAME);
	//		this.initialize(name);
	//	}

	/**
	 * Instantiates a new name.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 * @param wf
	 *            the wrapperfactory
	 * @param cppId
	 *            the cpp-id
	 */
	protected Name(final lotus.domino.Name delegate, final Session parent) {
		super(delegate, parent, NOTES_NAME);
		initialize(delegate);
		// TODO: Wrapping recycles the caller's object. This may cause issues.
		Base.s_recycle(delegate);
	}

	/*
	 * for clone
	 */
	protected Name(final NamePartsMap namePartsMap, final Session parent, final WrapperFactory wf) {
		super(null, parent, NOTES_NAME);
		_namePartsMap = (NamePartsMap) namePartsMap.clone();
	}

	/**
	 * Clears the object.
	 */
	public void clear() {
		if (null != this._namePartsMap) {
			this._namePartsMap.clear();
		}
	}

	//	/**
	//	 * Reloads the object
	//	 */
	//	public void reload(final Name name) {
	//		this.initialize(name);
	//	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * private methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	/**
	 * Gets the NamePartsMap for the object.
	 * 
	 * @return NamePartsMap for the object.
	 */
	private NamePartsMap getNamePartsMap() {
		if (null == this._namePartsMap) {
			this._namePartsMap = new NamePartsMap();
		}
		return this._namePartsMap;
	}

	/**
	 * Sets the NamePartsMap for the object.
	 * 
	 * @param namePartsMap
	 *            NamePartsMap for the object.
	 */
	private void setNamePartsMap(final NamePartsMap namePartsMap) {
		this._namePartsMap = namePartsMap;
	}

	private void initialize(final lotus.domino.Name delegate) {
		try {
			if (null == delegate) {
				throw new IllegalArgumentException("Source Name is null");
			}

			this.clear();
			this.setName(delegate);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
			throw new RuntimeException("EXCEPTION thrown in in Name.initialize()");
		}
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * protected methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */
	/*
	 * The returned object MUST get recycled
	 * (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getDelegate()
	 */
	@Override
	protected lotus.domino.Name getDelegate() {
		try {
			lotus.domino.Session rawsession = toLotus(parent);
			return rawsession.createName(this.getCanonical());

		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}

		return null;
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * Other public methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */
	/**
	 * Flag indicating if the object is Hierarchical
	 * 
	 * @return Hierarchical indicator flag
	 */
	@Override
	public boolean isHierarchical() {
		return getNameFormat().isHierarchical();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(Name.class.getName());
		sb.append(" [");
		boolean comma = false;
		for (NamePartsMap.Key key : NamePartsMap.Key.values()) {
			String s = this.getNamePartsMap().get(key);
			if (!Strings.isBlankString(s)) {
				if (comma)
					sb.append(", ");
				sb.append(key.name() + "=" + s);
				comma = true;
			}
		}

		sb.append("]");

		return sb.toString();
	}

	/**
	 * Parses the source string and sets the appropriate RFC822 values.
	 * 
	 * @param string
	 *            RFC822 source string from which to set the appropriate RFC822 values.
	 */
	public void parseRFC82xContent(final String source) {
		this.getNamePartsMap().parseRFC82xContent(source);
	}

	/**
	 * Indicates whether the object has RFC82xContent
	 * 
	 * @return Flag indicating if the object has RFC82xContent
	 */
	public boolean isHasRFC82xContent() {
		return (null == this._namePartsMap) ? false : this.getNamePartsMap().isHasRFC82xContent();
	}

	protected void setSource(final String sourceName) {
		this.source_ = sourceName;
	}

	public String getSource() {
		return source_;
	}

	protected void setLang(final String lang) {
		this.lang_ = lang;
	}

	/**
	 * Sets the Name for the object.
	 * 
	 * @param name
	 *            Name for the object.
	 */
	protected void setName(final lotus.domino.Name name) {
		try {

			if (null == name) {
				throw new IllegalArgumentException("Source Name is null");
			}

			String tmp;
			if (!Strings.isBlankString(tmp = name.getCanonical())) {
				this.setNamePartsMap(new NamePartsMap(tmp, Names.buildAddr822Full(name)));
			} else if (!Strings.isBlankString(tmp = name.getAbbreviated())) {
				this.setNamePartsMap(new NamePartsMap(tmp, Names.buildAddr822Full(name)));
			}

			NamePartsMap npm = this.getNamePartsMap();

			if (!Strings.isBlankString(tmp = getADMD())) { // A=
				npm.put(NamePartsMap.Key.ADMD, tmp);
			}

			if (!Strings.isBlankString(tmp = getGeneration())) { // Q=
				npm.put(NamePartsMap.Key.Generation, tmp);
			}
			if (!Strings.isBlankString(tmp = getGiven())) { // G=
				npm.put(NamePartsMap.Key.Given, tmp);
			}
			if (!Strings.isBlankString(tmp = getInitials())) { // I=
				npm.put(NamePartsMap.Key.Initials, tmp);
			}
			if (!Strings.isBlankString(tmp = getKeyword())) { // he following components of a hierarchical name in the order shown separated by backslashes: country or region\organization\organizational unit 1\organizational unit 2\organizational unit 3\organizational unit 4. Returns an empty string if the property is undefined.
				npm.put(NamePartsMap.Key.Keyword, tmp);
			}
			if (!Strings.isBlankString(tmp = getLanguage())) { //  as we have only primary user names, this is always empty
				npm.put(NamePartsMap.Key.Language, tmp);
			}

			if (!Strings.isBlankString(tmp = getPRMD())) {  // P=
				npm.put(NamePartsMap.Key.PRMD, tmp);
			}
			if (!Strings.isBlankString(tmp = getSurname())) { // S=
				npm.put(NamePartsMap.Key.Surname, tmp);
			}
			// RPr: These fields should be set by the parser
			//			if (!Strings.isBlankString(tmp = name.getCommon())) {
			//				npm.put(NamePartsMap.Key.Common, tmp);
			//			}
			//			if (!Strings.isBlankString(tmp = name.getOrgUnit1())) {
			//				npm.put(NamePartsMap.Key.OrgUnit1, tmp);
			//			}
			//			if (!Strings.isBlankString(tmp = name.getOrgUnit2())) {
			//				npm.put(NamePartsMap.Key.OrgUnit2, tmp);
			//			}
			//			if (!Strings.isBlankString(tmp = name.getOrgUnit3())) {
			//				npm.put(NamePartsMap.Key.OrgUnit3, tmp);
			//			}
			//			if (!Strings.isBlankString(tmp = name.getOrgUnit4())) {
			//				npm.put(NamePartsMap.Key.OrgUnit4, tmp);
			//			}
			//			if (!Strings.isBlankString(tmp = name.getOrganization())) {
			//				npm.put(NamePartsMap.Key.Organization, tmp);
			//			}
			//			if (!Strings.isBlankString(tmp = name.getCountry())) {
			//				npm.put(NamePartsMap.Key.Country, tmp);
			//			}
		} catch (final Exception e) {
			DominoUtils.handleException(e);

		}
	}

	//	/**
	//	 * Sets the Name for the object.
	//	 * 
	//	 * @param name
	//	 *            Name for the object.
	//	 */
	//	public void setName(final Name name) {
	//		try {
	//			if (null == name) {
	//				throw new IllegalArgumentException("Source Name is null");
	//			}
	//
	//			this.setName(name.getDelegate());
	//			this.parseRFC82xContent(Names.buildAddr822Full(name));
	//
	//		} catch (final Exception e) {
	//			DominoUtils.handleException(e);
	//		}
	//	}

	/**
	 * Gets the Name Part for the specified key.
	 * 
	 * @param key
	 *            Key identifying the specific mapped Name Part string to return.
	 * 
	 * @return Mapped String for the key. Empty string "" if no mapping exists.
	 * 
	 * @see org.openntf.arpa.NamePartsMap#get(org.openntf.arpa.NamePartsMap.Key)
	 * @see java.util.HashMap#get(Object)
	 */
	@Override
	public String getNamePart(final NamePartsMap.Key key) {
		try {
			if (null == key) {
				throw new IllegalArgumentException("NamePart is null");
			}
			//			parse();
			String result = this.getNamePartsMap().get(key);
			return (null == result) ? "" : result;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	/**
	 * Gets the Source String used to construct this object.
	 * 
	 * @return the sourceString used to construct this object
	 */
	public String getSourceString() {
		return this.getNamePart(NamePartsMap.Key.SourceString);
	}

	/**
	 * Gets the Abbreviated form of the name.
	 * 
	 * @see lotus.domino.Name#getAbbreviated()
	 */
	@Override
	public String getAbbreviated() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Abbreviated);
	}

	/**
	 * Gets the Addr821 portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr821()
	 */
	@Override
	public String getAddr821() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Addr821);
	}

	/**
	 * Gets the Addr822Comment1 portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822Comment1()
	 */
	@Override
	public String getAddr822Comment1() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Addr822Comment1);
	}

	/**
	 * Gets the Addr822Comment2 portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822Comment2()
	 */
	@Override
	public String getAddr822Comment2() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Addr822Comment2);
	}

	/**
	 * Gets the Addr822Comment3 portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822Comment3()
	 */
	@Override
	public String getAddr822Comment3() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Addr822Comment3);
	}

	/**
	 * Gets the Addr822LocalPart portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822LocalPart()
	 */
	@Override
	public String getAddr822LocalPart() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Addr822LocalPart);
	}

	/**
	 * Gets the Addr822Phrase portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822Phrase()
	 */
	@Override
	public String getAddr822Phrase() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Addr822Phrase);
	}

	/**
	 * Gets the Addr822Full portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822Full()
	 */
	public String getAddr822Full() {
		parse();
		return this.getNamePartsMap().getRFC822name().getAddr822Full();
	}

	/**
	 * Gets the Addr822FullFirstLast portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822FullFirstLast()
	 */
	public String getAddr822FullFirstLast() {
		parse();
		return this.getNamePartsMap().getRFC822name().getAddr822FullFirstLast();
	}

	/**
	 * Gets the Administration Management Domain Name portion of the name.
	 * 
	 * @see lotus.domino.Name#getADMD()
	 */
	@Override
	public String getADMD() {
		parse();
		return this.getNamePart(NamePartsMap.Key.ADMD);
	}

	/**
	 * Gets the Canonical form of the name.
	 * 
	 * @see lotus.domino.Name#getCanonical()
	 */
	@Override
	public String getCanonical() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Canonical);
	}

	/**
	 * Gets the Common portion of the name.
	 * 
	 * @see lotus.domino.Name#getCommon()
	 */
	@Override
	public String getCommon() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Common);
	}

	/**
	 * Gets the Country portion of the name.
	 * 
	 * @see lotus.domino.Name#getCountry()
	 */
	@Override
	public String getCountry() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Country);
	}

	/**
	 * Gets the Generation portion of the name.
	 * 
	 * @see lotus.domino.Name#getGeneration()
	 */
	@Override
	public String getGeneration() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Generation);
	}

	/**
	 * Gets the Given portion of the name.
	 * 
	 * @see lotus.domino.Name#getGiven()
	 */
	@Override
	public String getGiven() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Given);
	}

	/**
	 * Gets the Initials portion of the name.
	 * 
	 * @see lotus.domino.Name#getInitials()
	 */
	@Override
	public String getInitials() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Initials);
	}

	/**
	 * Gets the Keyword portion of the name.
	 * 
	 * @see lotus.domino.Name#getKeyword()
	 */
	@Override
	public String getKeyword() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Keyword);
	}

	/**
	 * Gets the Language portion of the name.
	 * 
	 * @see lotus.domino.Name#getLanguage()
	 */
	@Override
	public String getLanguage() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Language);
	}

	/**
	 * Gets the Organization portion of the name.
	 * 
	 * @see lotus.domino.Name#getOrganization()
	 */
	@Override
	public String getOrganization() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Organization);
	}

	/**
	 * Gets the OrgUnit1 portion of the name.
	 * 
	 * @see lotus.domino.Name#getOrgUnit1()
	 */
	@Override
	public String getOrgUnit1() {
		parse();
		return this.getNamePart(NamePartsMap.Key.OrgUnit1);
	}

	/**
	 * Gets the OrgUnit2 portion of the name.
	 * 
	 * @see lotus.domino.Name#getOrgUnit2()
	 */
	@Override
	public String getOrgUnit2() {
		parse();
		return this.getNamePart(NamePartsMap.Key.OrgUnit2);
	}

	/**
	 * Gets the OrgUnit3 portion of the name.
	 * 
	 * @see lotus.domino.Name#getOrgUnit3()
	 */
	@Override
	public String getOrgUnit3() {
		parse();
		return this.getNamePart(NamePartsMap.Key.OrgUnit3);
	}

	/**
	 * Gets the OrgUnit4 portion of the name.
	 * 
	 * @see lotus.domino.Name#getOrgUnit4()
	 */
	@Override
	public String getOrgUnit4() {
		parse();
		return this.getNamePart(NamePartsMap.Key.OrgUnit4);
	}

	/**
	 * Gets the Private Management Domain Name portion of the name.
	 * 
	 * @see lotus.domino.Name#getPRMD()
	 */
	@Override
	public String getPRMD() {
		parse();
		return this.getNamePart(NamePartsMap.Key.PRMD);
	}

	/**
	 * Gets the NameType (Hierarchical/Flat/RFC8229)
	 * 
	 */
	@Override
	public NameFormat getNameFormat() {
		parse();
		return getNamePartsMap().getNameFormat();
	}

	/**
	 * Gets the RFC821 or RFC822 internet address
	 * 
	 * * A name that conforms to RFC 821 or RFC 822 is interpreted as an Internet address. Examples of Internet addresses are as follows:
	 * <ul>
	 * <li>jbg@us.acme.com
	 * <li>"John B Goode" <jbg@us.acme.com>
	 * <li>"John B Goode" <jbg@us.acme.com> (Sales) (East)
	 * </ul>
	 * 
	 * @return the Internet address, comprised of the at least the minimum RFC821 Address. If no RFC821 Address exists a blank string is
	 *         returned.
	 * 
	 * @see Name#getAddr821()
	 * @see org.openntf.arpa.RFC822name#getAddr822Full()
	 */
	@Override
	public String getRFC82xInternetAddress() {
		parse();
		return this.getAddr822FullFirstLast();
	}

	/**
	 * Gets the Surname portion of the name.
	 * 
	 * @see lotus.domino.Name#getSurname()
	 */
	@Override
	public String getSurname() {
		parse();
		return this.getNamePart(NamePartsMap.Key.Surname);
	}

	/**
	 * Gets the IDprefix portion of the name.
	 * 
	 * @see org.openntf.arpa.NamePartsMap#getIDprefix()
	 */
	@Override
	public String getIDprefix() {
		parse();
		return this.getNamePartsMap().getIDprefix();
	}

	/**
	 * Determines if any portion or form of the name object's internal NamePartsMap values are equal to the passed in string. Performs a
	 * case-insensitive check.
	 * 
	 * @param string
	 *            String tom compare values against
	 * 
	 * @return Flag indicating if any of the values are equal to the string.
	 */
	public boolean equalsIgnoreCase(final String compString) {
		return (Strings.isBlankString(compString)) ? false : compString.equalsIgnoreCase(source_);
	}

	/**
	 * Determines if one of the Name's properties begins with the prefix.
	 * 
	 * Checks the following properties, in order:
	 * <ul>
	 * <li>Abbreviated</li>
	 * <li>Canonical</li>
	 * </ul>
	 * 
	 * @param prefix
	 *            Value to compare to the properties of the Name
	 * 
	 * @param casesensitive
	 *            Flag indicating if Case-Sensitive comparisons should be enforced.
	 * 
	 * @return Flag indicating if any of the mapped values begin with the prefix.
	 */
	public boolean startsWith(final String prefix, final boolean casesensitive) {
		if (!Strings.isBlankString(prefix)) {
			if (this.getNamePartsMap().startsWith(prefix, casesensitive)) {
				return true;
			}

			if (casesensitive) {
				return ((this.getAbbreviated().startsWith(prefix)) || (this.getCanonical().startsWith(prefix)));
			} else {
				return ((Strings.startsWithIgnoreCase(this.getAbbreviated(), prefix)) || (Strings.startsWithIgnoreCase(this.getCanonical(),
						prefix)));
			}
		}

		return false;
	}

	/**
	 * Determines if the name is a member of the passed in set of name strings.
	 * 
	 * Conditionally expands the names list prior to checking.
	 * 
	 * @param session
	 *            Session used for generating Name objects
	 * @param names
	 *            String name values to check against
	 * @param expandNames
	 *            Flag indicating if the set of name strings should be expanded.
	 * 
	 * @return Flag indicating if the Name is a member of the set of strings.
	 */
	public boolean isMemberOfNames(final Session session, final TreeSet<String> names, final boolean expandNames) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}
			if (null == names) {
				throw new IllegalArgumentException("Names is null");
			}

			for (final String string : names) {
				if (this.getCanonical().equalsIgnoreCase(string) || this.getAbbreviated().equalsIgnoreCase(string)
						|| this.getCommon().equalsIgnoreCase(string)) {
					return true;
				}
			}

			if (expandNames) {
				final TreeSet<String> expanded = Names.expandNamesList(session, names);
				if (null != expanded) {
					for (final String string : expanded) {
						if (this.getCanonical().equalsIgnoreCase(string) || this.getAbbreviated().equalsIgnoreCase(string)
								|| this.getCommon().equalsIgnoreCase(string)) {
							return true;
						}
					}
				}
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final Session getParent() {
		return parent;
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * hashcode, equals, and comparison methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return _namePartsMap.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Name)) {
			return false;
		}
		Name other = (Name) obj;
		if (_namePartsMap == null) {
			if (other._namePartsMap != null) {
				return false;
			}
		} else if (!_namePartsMap.equals(other._namePartsMap)) {
			return false;
		}
		return true;
	}

	/**
	 * Compares this object with another Name
	 * 
	 * @param arg0
	 *            Name object to be compared.
	 * 
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 * 
	 * 
	 * @see java.lang.Comparable#compareTo(Object)
	 * @see DominoUtils#LESS_THAN
	 * @see DominoUtils#EQUAL
	 * @see DominoUtils#GREATER_THAN
	 */
	@Override
	public int compareTo(final Name arg0) {
		return Name.compare(this, arg0);
	}

	/**
	 * Default Comparable implementation. (Natural Comparison Method)
	 * 
	 * <ol>
	 * <li>Equality using .equals() method</li>
	 * <li>Abbreviated Name</li>
	 * </ol>
	 * 
	 * @param arg0
	 *            First Name object for comparison.
	 * @param arg1
	 *            Second Name object for comparison.
	 * 
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 * 
	 * 
	 * @see java.lang.Comparable#compareTo(Object)
	 * @see DominoUtils#LESS_THAN
	 * @see DominoUtils#EQUAL
	 * @see DominoUtils#GREATER_THAN
	 */
	protected static int compare(final Name arg0, final Name arg1) {
		if (null == arg0) {
			return (null == arg1) ? DominoUtils.EQUAL : DominoUtils.LESS_THAN;
		} else if (null == arg1) {
			return DominoUtils.GREATER_THAN;
		}

		return (arg0.equals(arg1)) ? DominoUtils.EQUAL : arg0.source_.compareTo(arg1.source_);
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		// RPr: This is useless, but I don#t know if there are externalized names in the wild
		in.readBoolean(); // so we read a dummy boolean (NTF: Feel free to remove, I don't need it)
		this.parse(in.readUTF());
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeBoolean(this.isHierarchical());
		out.writeUTF((Strings.isBlankString(this.getCanonical())) ? this.getAddr822Full() : this.getCanonical());
	}

	private Collection<String> groupNames_;

	@Override
	@SuppressWarnings("unchecked")
	public Collection<String> getGroups(final String serverName) {
		if (groupNames_ == null) {
			try {
				DominoServer server = new DominoServer(serverName);
				groupNames_ = server.getNamesList(getCanonical());
			} catch (NotesException e) {
				DominoUtils.handleException(e);
			}
		}
		return groupNames_;
	}

	@Override
	public Name clone() {
		return new Name(_namePartsMap, getAncestorSession(), getFactory());
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getFactory();
	}

	@Override
	public void parse(final String name, final String lang) {
		if (_namePartsMap == null) {
			this.setNamePartsMap(new NamePartsMap(name));
		} else {
			this.clear();
			_namePartsMap.setName(name);
		}
		if (!Strings.isBlankString(lang)) {
			this.getNamePartsMap().put(NamePartsMap.Key.Language, lang);
		}
		isParsed_ = true;
	}

	@Override
	public void parse(final String name) {
		parse(name, null);
	}

	protected void parse() {
		if (!isParsed_) {
			parse(source_, lang_);
		}
	}

}
