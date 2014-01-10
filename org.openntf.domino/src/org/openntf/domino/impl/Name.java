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

	public boolean isHierarchical() {
		return this.Hierarchical;
	}

	private void setHierarchical(final boolean arg0) {
		this.Hierarchical = arg0;
	}

	public NamePartsMap getNamePartsMap() {
		if (null == this._namePartsMap) {
			this._namePartsMap = new NamePartsMap();
		}
		return this._namePartsMap;
	}

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

	public void parseRFC82xContent(final String source) {
		this.getNamePartsMap().parseRFC82xContent(source);
	}

	public boolean isHasRFC82xContent() {
		return (null == this._namePartsMap) ? false : this.getNamePartsMap().isHasRFC82xContent();
	}

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
	 * @return the sourceString used to construct this object
	 */
	public String getSourceString() {
		return this.getNamePart(NamePartsMap.Key.SourceString);
	}

	public String getAbbreviated() {
		return this.getNamePart(NamePartsMap.Key.Abbreviated);
	}

	public String getAddr821() {
		return this.getNamePart(NamePartsMap.Key.Addr821);
	}

	public String getAddr822Comment1() {
		return this.getNamePart(NamePartsMap.Key.Addr822Comment1);
	}

	public String getAddr822Comment2() {
		return this.getNamePart(NamePartsMap.Key.Addr822Comment2);
	}

	public String getAddr822Comment3() {
		return this.getNamePart(NamePartsMap.Key.Addr822Comment3);
	}

	public String getAddr822LocalPart() {
		return this.getNamePart(NamePartsMap.Key.Addr822LocalPart);
	}

	public String getAddr822Phrase() {
		return this.getNamePart(NamePartsMap.Key.Addr822Phrase);
	}

	public String getAddr822Full() {
		return this.getNamePartsMap().getRFC822name().getAddr822Full();
	}

	public String getAddr822FullFirstLast() {
		return this.getNamePartsMap().getRFC822name().getAddr822FullFirstLast();
	}

	public String getADMD() {
		return this.getNamePart(NamePartsMap.Key.ADMD);
	}

	public String getCanonical() {
		return this.getNamePart(NamePartsMap.Key.Canonical);
	}

	public String getCommon() {
		return this.getNamePart(NamePartsMap.Key.Common);
	}

	public String getCountry() {
		return this.getNamePart(NamePartsMap.Key.Country);
	}

	public String getGeneration() {
		return this.getNamePart(NamePartsMap.Key.Generation);
	}

	public String getGiven() {
		return this.getNamePart(NamePartsMap.Key.Given);
	}

	public String getInitials() {
		return this.getNamePart(NamePartsMap.Key.Initials);
	}

	public String getKeyword() {
		return this.getNamePart(NamePartsMap.Key.Keyword);
	}

	public String getLanguage() {
		return this.getNamePart(NamePartsMap.Key.Language);
	}

	public String getOrganization() {
		return this.getNamePart(NamePartsMap.Key.Organization);
	}

	public String getOrgUnit1() {
		return this.getNamePart(NamePartsMap.Key.OrgUnit1);
	}

	public String getOrgUnit2() {
		return this.getNamePart(NamePartsMap.Key.OrgUnit2);
	}

	public String getOrgUnit3() {
		return this.getNamePart(NamePartsMap.Key.OrgUnit3);
	}

	public String getOrgUnit4() {
		return this.getNamePart(NamePartsMap.Key.OrgUnit4);
	}

	public String getPRMD() {
		return this.getNamePart(NamePartsMap.Key.PRMD);
	}

	public String getSurname() {
		return this.getNamePart(NamePartsMap.Key.Surname);
	}

	public String getIDprefix() {
		return this.getNamePartsMap().getIDprefix();
	}

	public boolean equalsIgnoreCase(final String checkstring) {
		try {
			if (!Strings.isBlankString(checkstring)) {

				if (checkstring.equalsIgnoreCase(this.getCanonical()) || checkstring.equalsIgnoreCase(this.getAbbreviated())
						|| checkstring.equalsIgnoreCase(this.getCommon())) {
					return true;
				} else {

					for (final String s : this.getNamePartsMap().values()) {
						if (checkstring.equalsIgnoreCase(s)) {
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
		try {
			if (null == prefix) {
				throw new IllegalArgumentException("Prefix is null");
			}

			return (casesensitive) ? ((this.getAbbreviated().startsWith(prefix)) || (this.getCanonical().startsWith(prefix))) : ((Strings
					.startsWithIgnoreCase(this.getAbbreviated(), prefix)) || (Strings.startsWithIgnoreCase(this.getCanonical(), prefix)));

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return false;
	}

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
