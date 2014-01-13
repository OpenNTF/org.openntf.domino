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

import java.util.TreeSet;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.arpa.NamePartsMap;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Names;
import org.openntf.domino.utils.Strings;

/**
 * The Class Name.
 */
public class Name extends Base<org.openntf.domino.Name, lotus.domino.Name> implements org.openntf.domino.Name, Comparable<Name> {

	private static final Logger log_ = Logger.getLogger(Name.class.getName());
	private static final long serialVersionUID = 1L;
	private NamePartsMap _namePartsMap;
	private boolean Hierarchical;

	/**
	 * * Zero-Argument Constructor
	 */
	public Name() {
		super(null, null);
	}

	/**
	 * Default Constructor.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public Name(final lotus.domino.Name delegate, final org.openntf.domino.Base<?> parent) {
		super(null, parent);
		this.initialize(delegate);
		Base.s_recycle(delegate);
	}

	/**
	 * Optional Constructor
	 * 
	 * @param session
	 *            Session used for Name processing
	 */
	public Name(final Session session) {
		super(null, session);
		this.initialize(session.getEffectiveUserName());
	}

	/**
	 * Optional Constructor
	 * 
	 * @param session
	 *            Session used for Name processing
	 * 
	 * @param name
	 *            String used to construct the Name object
	 */
	public Name(final Session session, final String name) {
		super(null, (org.openntf.domino.Base<?>) Factory.fromLotus(session, Session.class, null));
		this.initialize(name);
	}

	/**
	 * Optional Constructor
	 * 
	 * @param name
	 *            String used to construct the Name object
	 */
	public Name(final String name) {
		super(null, (org.openntf.domino.Base<?>) Factory.getSession());
		this.initialize(name);
	}

	/**
	 * Clears the object.
	 */
	public void clear() {
		this.Hierarchical = false;
		if (null != this._namePartsMap) {
			this._namePartsMap.clear();
		}
	}

	/**
	 * Reloads the object
	 */
	public void reload(final Name name) {
		this.initialize(name);
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * Serializable getters & setters
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */

	/**
	 * Flag indicating if the object is Hierarchical
	 * 
	 * @return Hierarchical indicator flag
	 */
	public boolean isHierarchical() {
		return this.Hierarchical;
	}

	/**
	 * Flag indicating if the object is Hierarchical
	 * 
	 * @param arg0
	 *            Hierarchical indicator flag
	 */
	private void setHierarchical(final boolean arg0) {
		this.Hierarchical = arg0;
	}

	/**
	 * Gets the NamePartsMap for the object.
	 * 
	 * @return NamePartsMap for the object.
	 */
	public NamePartsMap getNamePartsMap() {
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
	public void setNamePartsMap(final NamePartsMap namePartsMap) {
		this._namePartsMap = namePartsMap;
	}

	/*
	 * ******************************************************************
	 * ******************************************************************
	 * 
	 * private methods
	 * 
	 * ******************************************************************
	 * ******************************************************************
	 */
	private void initialize(final lotus.domino.Name delegate) {
		try {
			if (null == delegate) {
				throw new IllegalArgumentException("Source Name is null");
			}

			this.clear();
			this.setHierarchical(delegate.isHierarchical());
			this.setName(delegate);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
			throw new RuntimeException("EXCEPTION thrown in in Name.initialize()");
		}
	}

	private void initialize(final Name name) {
		this.initialize(name.getDelegate());
	}

	private void initialize(final String name) {
		try {
			if (Strings.isBlankString(name)) {
				throw new IllegalArgumentException("Source name is null or blank");
			}

			this.parseRFC82xContent(name);
			String phrase = this.getAddr822Phrase();

			Name n = (Name) Factory.getSession().createName((Strings.isBlankString(phrase)) ? name : phrase);
			if (null == n) {
				throw new RuntimeException("Unable to create Name object");
			}

			this.initialize(n);

			// parse again because initialize(n) may have cleared the RFC82x content
			this.parseRFC82xContent(name);

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
	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getDelegate()
	 */
	@Override
	protected lotus.domino.Name getDelegate() {
		try {
			//					return this.getParent().getDelegate().createName(this.getCanonical());
			org.openntf.domino.impl.Session s = (org.openntf.domino.impl.Session) this.getParent();
			return s.getDelegate().createName(this.getCanonical());
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(Name.class.getName());
		sb.append(" [");
		for (NamePartsMap.Key key : NamePartsMap.Key.values()) {
			String s = this.getNamePartsMap().get(key);
			if (!Strings.isBlankString(s)) {
				sb.append(key.name() + "=" + s);
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

	/**
	 * Sets the Name for the object.
	 * 
	 * @param name
	 *            Name for the object.
	 */
	public void setName(final lotus.domino.Name name) {
		try {
			if (null == name) {
				throw new IllegalArgumentException("Source Name is null");
			}

			if (!Strings.isBlankString(name.getCanonical())) {
				this.setNamePartsMap(new NamePartsMap(name.getCanonical(), Names.buildAddr822(name)));
			} else if (!Strings.isBlankString(name.getAbbreviated())) {
				this.setNamePartsMap(new NamePartsMap(name.getAbbreviated(), Names.buildAddr822(name)));
			}

			if (!Strings.isBlankString(name.getADMD())) {
				this.getNamePartsMap().put(NamePartsMap.Key.ADMD, name.getADMD());
			}
			if (!Strings.isBlankString(name.getCommon())) {
				this.getNamePartsMap().put(NamePartsMap.Key.Common, name.getCommon());
			}
			if (!Strings.isBlankString(name.getCountry())) {
				this.getNamePartsMap().put(NamePartsMap.Key.Country, name.getCountry());
			}
			if (!Strings.isBlankString(name.getGeneration())) {
				this.getNamePartsMap().put(NamePartsMap.Key.Generation, name.getGeneration());
			}
			if (!Strings.isBlankString(name.getGiven())) {
				this.getNamePartsMap().put(NamePartsMap.Key.Given, name.getGiven());
			}
			if (!Strings.isBlankString(name.getInitials())) {
				this.getNamePartsMap().put(NamePartsMap.Key.Initials, name.getInitials());
			}
			if (!Strings.isBlankString(name.getKeyword())) {
				this.getNamePartsMap().put(NamePartsMap.Key.Keyword, name.getKeyword());
			}
			if (!Strings.isBlankString(name.getLanguage())) {
				this.getNamePartsMap().put(NamePartsMap.Key.Language, name.getLanguage());
			}
			if (!Strings.isBlankString(name.getOrganization())) {
				this.getNamePartsMap().put(NamePartsMap.Key.Organization, name.getOrganization());
			}
			if (!Strings.isBlankString(name.getOrgUnit1())) {
				this.getNamePartsMap().put(NamePartsMap.Key.OrgUnit1, name.getOrgUnit1());
			}
			if (!Strings.isBlankString(name.getOrgUnit2())) {
				this.getNamePartsMap().put(NamePartsMap.Key.OrgUnit2, name.getOrgUnit2());
			}
			if (!Strings.isBlankString(name.getOrgUnit3())) {
				this.getNamePartsMap().put(NamePartsMap.Key.OrgUnit3, name.getOrgUnit3());
			}
			if (!Strings.isBlankString(name.getOrgUnit4())) {
				this.getNamePartsMap().put(NamePartsMap.Key.OrgUnit4, name.getOrgUnit4());
			}
			if (!Strings.isBlankString(name.getPRMD())) {
				this.getNamePartsMap().put(NamePartsMap.Key.PRMD, name.getPRMD());
			}
			if (!Strings.isBlankString(name.getSurname())) {
				this.getNamePartsMap().put(NamePartsMap.Key.Surname, name.getSurname());
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}
	}

	/**
	 * Sets the Name for the object.
	 * 
	 * @param name
	 *            Name for the object.
	 */
	public void setName(final Name name) {
		try {
			if (null == name) {
				throw new IllegalArgumentException("Source Name is null");
			}

			this.setName(name.getDelegate());
			this.parseRFC82xContent(Names.buildAddr822(name));

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}
	}

	/**
	 * Gets the String for the key.
	 * 
	 * @param key
	 *            Key for the mapped String
	 * 
	 * @return Mapped String for the key. Empty string "" if no mapping exists.
	 * 
	 * @see org.openntf.arpa.NamePartsMap#get(org.openntf.arpa.NamePartsMap.Key)
	 * @see java.util.HashMap#get(Object)
	 */
	public String getNamePart(final NamePartsMap.Key key) {
		try {
			if (null == key) {
				throw new IllegalArgumentException("NamePart is null");
			}

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
	public String getAbbreviated() {
		return this.getNamePart(NamePartsMap.Key.Abbreviated);
	}

	/**
	 * Gets the Addr821 portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr821()
	 */
	public String getAddr821() {
		return this.getNamePart(NamePartsMap.Key.Addr821);
	}

	/**
	 * Gets the Addr822Comment1 portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822Comment1()
	 */
	public String getAddr822Comment1() {
		return this.getNamePart(NamePartsMap.Key.Addr822Comment1);
	}

	/**
	 * Gets the Addr822Comment2 portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822Comment2()
	 */
	public String getAddr822Comment2() {
		return this.getNamePart(NamePartsMap.Key.Addr822Comment2);
	}

	/**
	 * Gets the Addr822Comment3 portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822Comment3()
	 */
	public String getAddr822Comment3() {
		return this.getNamePart(NamePartsMap.Key.Addr822Comment3);
	}

	/**
	 * Gets the Addr822LocalPart portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822LocalPart()
	 */
	public String getAddr822LocalPart() {
		return this.getNamePart(NamePartsMap.Key.Addr822LocalPart);
	}

	/**
	 * Gets the Addr822Phrase portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822Phrase()
	 */
	public String getAddr822Phrase() {
		return this.getNamePart(NamePartsMap.Key.Addr822Phrase);
	}

	/**
	 * Gets the Addr822Full portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822Full()
	 */
	public String getAddr822Full() {
		return this.getNamePartsMap().getRFC822name().getAddr822Full();
	}

	/**
	 * Gets the Addr822FullFirstLast portion of the name.
	 * 
	 * @see org.openntf.arpa.RFC822name#getAddr822FullFirstLast()
	 */
	public String getAddr822FullFirstLast() {
		return this.getNamePartsMap().getRFC822name().getAddr822FullFirstLast();
	}

	/**
	 * Gets the Administration Management Domain Name portion of the name.
	 * 
	 * @see lotus.domino.Name#getADMD()
	 */
	public String getADMD() {
		return this.getNamePart(NamePartsMap.Key.ADMD);
	}

	/**
	 * Gets the Canonical form of the name.
	 * 
	 * @see lotus.domino.Name#getCanonical()
	 */
	public String getCanonical() {
		return this.getNamePart(NamePartsMap.Key.Canonical);
	}

	/**
	 * Gets the Common portion of the name.
	 * 
	 * @see lotus.domino.Name#getCommon()
	 */
	public String getCommon() {
		return this.getNamePart(NamePartsMap.Key.Common);
	}

	/**
	 * Gets the Country portion of the name.
	 * 
	 * @see lotus.domino.Name#getCountry()
	 */
	public String getCountry() {
		return this.getNamePart(NamePartsMap.Key.Country);
	}

	/**
	 * Gets the Generation portion of the name.
	 * 
	 * @see lotus.domino.Name#getGeneration()
	 */
	public String getGeneration() {
		return this.getNamePart(NamePartsMap.Key.Generation);
	}

	/**
	 * Gets the Given portion of the name.
	 * 
	 * @see lotus.domino.Name#getGiven()
	 */
	public String getGiven() {
		return this.getNamePart(NamePartsMap.Key.Given);
	}

	/**
	 * Gets the Initials portion of the name.
	 * 
	 * @see lotus.domino.Name#getInitials()
	 */
	public String getInitials() {
		return this.getNamePart(NamePartsMap.Key.Initials);
	}

	/**
	 * Gets the Keyword portion of the name.
	 * 
	 * @see lotus.domino.Name#getKeyword()
	 */
	public String getKeyword() {
		return this.getNamePart(NamePartsMap.Key.Keyword);
	}

	/**
	 * Gets the Language portion of the name.
	 * 
	 * @see lotus.domino.Name#getLanguage()
	 */
	public String getLanguage() {
		return this.getNamePart(NamePartsMap.Key.Language);
	}

	/**
	 * Gets the Organization portion of the name.
	 * 
	 * @see lotus.domino.Name#getOrganization()
	 */
	public String getOrganization() {
		return this.getNamePart(NamePartsMap.Key.Organization);
	}

	/**
	 * Gets the OrgUnit1 portion of the name.
	 * 
	 * @see lotus.domino.Name#getOrgUnit1()
	 */
	public String getOrgUnit1() {
		return this.getNamePart(NamePartsMap.Key.OrgUnit1);
	}

	/**
	 * Gets the OrgUnit2 portion of the name.
	 * 
	 * @see lotus.domino.Name#getOrgUnit2()
	 */
	public String getOrgUnit2() {
		return this.getNamePart(NamePartsMap.Key.OrgUnit2);
	}

	/**
	 * Gets the OrgUnit3 portion of the name.
	 * 
	 * @see lotus.domino.Name#getOrgUnit3()
	 */
	public String getOrgUnit3() {
		return this.getNamePart(NamePartsMap.Key.OrgUnit3);
	}

	/**
	 * Gets the OrgUnit4 portion of the name.
	 * 
	 * @see lotus.domino.Name#getOrgUnit4()
	 */
	public String getOrgUnit4() {
		return this.getNamePart(NamePartsMap.Key.OrgUnit4);
	}

	/**
	 * Gets the Private Management Domain Name portion of the name.
	 * 
	 * @see lotus.domino.Name#getPRMD()
	 */
	public String getPRMD() {
		return this.getNamePart(NamePartsMap.Key.PRMD);
	}

	/**
	 * Gets the Surname portion of the name.
	 * 
	 * @see lotus.domino.Name#getSurname()
	 */
	public String getSurname() {
		return this.getNamePart(NamePartsMap.Key.Surname);
	}

	/**
	 * Gets the IDprefix portion of the name.
	 * 
	 * @see org.openntf.arpa.NamePartsMap#getIDprefix()
	 */
	public String getIDprefix() {
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
	public boolean equalsIgnoreCase(final String string) {
		return (Strings.isBlankString(string)) ? false : (string.equalsIgnoreCase(this.getCanonical())
				|| string.equalsIgnoreCase(this.getAbbreviated()) || string.equalsIgnoreCase(this.getCommon()) || this.getNamePartsMap()
				.equalsIgnoreCase(string));
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
	 * @return DocumentHandles whose named String Item Value begins with the prefix. Returns null if no matches found.
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
	public Session getAncestorSession() {
		return this.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return (Session) super.getParent();
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
		final int prime = 31;
		int result = 1;
		result = prime * result + (Hierarchical ? 1231 : 1237);
		result = prime * result + ((_namePartsMap == null) ? 0 : _namePartsMap.hashCode());
		return result;
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
		if (Hierarchical != other.Hierarchical) {
			return false;
		}
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
	public static int compare(final Name arg0, final Name arg1) {
		if (null == arg0) {
			return (null == arg1) ? DominoUtils.EQUAL : DominoUtils.LESS_THAN;
		} else if (null == arg1) {
			return DominoUtils.GREATER_THAN;
		}

		return (arg0.equals(arg1)) ? DominoUtils.EQUAL : arg0.getAbbreviated().compareTo(arg1.getAbbreviated());
	}

}
