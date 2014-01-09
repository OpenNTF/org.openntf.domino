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

import java.util.HashMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import lotus.domino.NotesException;

import org.openntf.arpa.RFC822name;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Names;
import org.openntf.domino.utils.Strings;

/**
 * The Class Name.
 */
public class Name extends Base<org.openntf.domino.Name, lotus.domino.Name> implements org.openntf.domino.Name, Comparable<Name> {
	public static enum NamePart {
		Abbreviated, Addr821, Addr822Comment1, Addr822Comment2, Addr822Comment3, Addr822LocalPart, Addr822Phrase, ADMD, Canonical, Common, Country, Generation, Given, Initials, Keyword, Language, Organization, OrgUnit1, OrgUnit2, OrgUnit3, OrgUnit4, PRMD, Surname, IDprefix, SourceString;

		@Override
		public String toString() {
			return this.name();
		}

		public String getInfo() {
			return this.getDeclaringClass() + "." + this.getClass() + ":" + this.name();
		}
	};

	private static final Logger log_ = Logger.getLogger(Name.class.getName());
	private static final long serialVersionUID = 1L;
	private HashMap<NamePart, String> _parts;
	private RFC822name _rfc822name;
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
	 * Resets the object.
	 */
	public void clear() {
		this.Hierarchical = false;
		if (null != this._parts) {
			this._parts.clear();
		}

		if (null != this._rfc822name) {
			this._rfc822name.clear();
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

	public RFC822name getRFC822name() {
		if (null == this._rfc822name) {
			this._rfc822name = new RFC822name();
		}
		return this._rfc822name;
	}

	public void setRFC822name(final RFC822name rfc822name) {
		this._rfc822name = rfc822name;
	}

	private HashMap<NamePart, String> getNameParts() {
		if (null == this._parts) {
			this._parts = new HashMap<NamePart, String>();
		}

		return this._parts;
	}

	private void setNameParts(final HashMap<NamePart, String> nameParts) {
		this._parts = nameParts;
	}

	//	/**
	//	 * @param sourceString
	//	 *            the sourceString to set
	//	 */
	//	public void setSourceString(final String sourceString) {
	//		this.SourceString = sourceString;
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
		for (NamePart key : NamePart.values()) {
			String s = this.getNamePart(key);
			if (!Strings.isBlankString(s)) {
				sb.append(key.name() + "=" + s);
			}
		}

		sb.append("]");

		return sb.toString();
	}

	public void parseRFC82xContent(final String source) {
		this.getRFC822name().parseRFC82xContent(source);
	}

	public boolean isHasRFC82xContent() {
		return (null == this._rfc822name) ? false : this.getRFC822name().isHasRFC82xContent();
	}

	public void setName(final lotus.domino.Name name) {
		try {
			if (null == name) {
				throw new IllegalArgumentException("Source Name is null");
			}

			final HashMap<NamePart, String> p = this.getNameParts();
			//			p.put(Name.NamePart.Abbreviated, name.getAbbreviated());
			p.put(Name.NamePart.ADMD, name.getADMD());
			//			p.put(Name.NamePart.Canonical, name.getCanonical());
			
			UNFINISHED - need to deal with Common and Abbreviated
			
			
			p.put(Name.NamePart.Common, name.getCommon());
			p.put(Name.NamePart.Country, name.getCountry());
			p.put(Name.NamePart.Generation, name.getGeneration());
			p.put(Name.NamePart.Given, name.getGiven());
			p.put(Name.NamePart.Initials, name.getInitials());
			p.put(Name.NamePart.Keyword, name.getKeyword());
			p.put(Name.NamePart.Language, name.getLanguage());
			p.put(Name.NamePart.Organization, name.getOrganization());
			p.put(Name.NamePart.OrgUnit1, name.getOrgUnit1());
			p.put(Name.NamePart.OrgUnit2, name.getOrgUnit2());
			p.put(Name.NamePart.OrgUnit3, name.getOrgUnit3());
			p.put(Name.NamePart.OrgUnit4, name.getOrgUnit4());
			p.put(Name.NamePart.PRMD, name.getPRMD());
			p.put(Name.NamePart.Surname, name.getSurname());

			this.parseRFC82xContent(Names.buildAddr822(name));

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

	public String getNamePart(final Name.NamePart key) {
		try {
			if (null == key) {
				throw new IllegalArgumentException("NamePart is null");
			}

			switch (key) {
			case Abbreviated: { 
				String common = this.getNamePart(Name.NamePart.Common);
				String ou1 = this.getNamePart(Name.NamePart.OrgUnit1);
				String ou2 = this.getNamePart(Name.NamePart.OrgUnit2);
				String ou3 = this.getNamePart(Name.NamePart.OrgUnit3);
				String organization = this.getNamePart(Name.NamePart.Organization);
				String country = this.getNamePart(Name.NamePart.Country);
				
				StringBuffer sb = new StringBuffer(""); 
				if (!Strings.isBlankString(common)) { sb.append(common); } 
				if (!Strings.isBlankString(ou3)) { sb.append("/" + ou3); } 
				if (!Strings.isBlankString(ou2)) { sb.append("/" + ou2); } 
				if (!Strings.isBlankString(ou1)) { sb.append("/" + ou1); } 
				if (!Strings.isBlankString(organization)) { sb.append("/" + organization); } 
				if (!Strings.isBlankString(country)) { sb.append("/" + country); } 

				return sb.toString();
			}
			
			case Addr821:
				return this.getRFC822name().getAddr821();

			case Addr822Comment1:
				return this.getRFC822name().getAddr822Comment1();

			case Addr822Comment2:
				return this.getRFC822name().getAddr822Comment2();

			case Addr822Comment3:
				return this.getRFC822name().getAddr822Comment3();

			case Addr822LocalPart:
				return this.getRFC822name().getAddr822LocalPart();

			case Addr822Phrase:
				return this.getRFC822name().getAddr822Phrase();

			case Canonical: { 
				String common = this.getNamePart(Name.NamePart.Common);
				String ou1 = this.getNamePart(Name.NamePart.OrgUnit1);
				String ou2 = this.getNamePart(Name.NamePart.OrgUnit2);
				String ou3 = this.getNamePart(Name.NamePart.OrgUnit3);
				String organization = this.getNamePart(Name.NamePart.Organization);
				String country = this.getNamePart(Name.NamePart.Country);
				
				StringBuffer sb = new StringBuffer(""); 
				if (!Strings.isBlankString(common)) { sb.append("CN=" + common); } 
				if (!Strings.isBlankString(ou3)) { sb.append("/OU=" + ou3); } 
				if (!Strings.isBlankString(ou2)) { sb.append("/OU=" + ou2); } 
				if (!Strings.isBlankString(ou1)) { sb.append("/OU=" + ou1); } 
				if (!Strings.isBlankString(organization)) { sb.append("/O=" + organization); } 
				if (!Strings.isBlankString(country)) { sb.append("/C=" + country); } 

				return sb.toString();
			}

			
			default:
				final String result = this.getNameParts().get(key);
				return (null == result) ? "" : result;
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	/**
	 * @return the sourceString used to construct this object
	 */
	public String getSourceString() {
		return this.getNamePart(Name.NamePart.SourceString);
	}

	public String getAbbreviated() {
		return this.getNamePart(Name.NamePart.Abbreviated);
	}

	public String getAddr821() {
		return this.getNamePart(Name.NamePart.Addr821);
	}

	public String getAddr822Comment1() {
		return this.getNamePart(Name.NamePart.Addr822Comment1);
	}

	public String getAddr822Comment2() {
		return this.getNamePart(Name.NamePart.Addr822Comment2);
	}

	public String getAddr822Comment3() {
		return this.getNamePart(Name.NamePart.Addr822Comment3);
	}

	public String getAddr822LocalPart() {
		return this.getNamePart(Name.NamePart.Addr822LocalPart);
	}

	public String getAddr822Phrase() {
		return this.getNamePart(Name.NamePart.Addr822Phrase);
	}

	public String getAddr822Full() {
		return this.getRFC822name().getAddr822Full();
	}

	public String getAddr822FullFirstLast() {
		return this.getRFC822name().getAddr822FullFirstLast();
	}

	public String getADMD() {
		return this.getNamePart(Name.NamePart.ADMD);
	}

	public String getCanonical() {
		return this.getNamePart(Name.NamePart.Canonical);
	}

	public String getCommon() {
		return this.getNamePart(Name.NamePart.Common);
	}

	public String getCountry() {
		return this.getNamePart(Name.NamePart.Country);
	}

	public String getGeneration() {
		return this.getNamePart(Name.NamePart.Generation);
	}

	public String getGiven() {
		return this.getNamePart(Name.NamePart.Given);
	}

	public String getInitials() {
		return this.getNamePart(Name.NamePart.Initials);
	}

	public String getKeyword() {
		return this.getNamePart(Name.NamePart.Keyword);
	}

	public String getLanguage() {
		return this.getNamePart(Name.NamePart.Language);
	}

	public String getOrganization() {
		return this.getNamePart(Name.NamePart.Organization);
	}

	public String getOrgUnit1() {
		return this.getNamePart(Name.NamePart.OrgUnit1);
	}

	public String getOrgUnit2() {
		return this.getNamePart(Name.NamePart.OrgUnit2);
	}

	public String getOrgUnit3() {
		return this.getNamePart(Name.NamePart.OrgUnit3);
	}

	public String getOrgUnit4() {
		return this.getNamePart(Name.NamePart.OrgUnit4);
	}

	public String getPRMD() {
		return this.getNamePart(Name.NamePart.PRMD);
	}

	public String getSurname() {
		return this.getNamePart(Name.NamePart.Surname);
	}

	public String getIDprefix() {
		String result = this.getNamePart(Name.NamePart.IDprefix);
		if (Strings.isBlankString(result)) {

			final String alphanumericandspacacesonly = this.getCommon().toUpperCase().trim().replaceAll("[^A-Za-z0-9 ]", "");
			final int idx = alphanumericandspacacesonly.indexOf(" ");
			String firstname = alphanumericandspacacesonly;
			String lastname = alphanumericandspacacesonly;

			if (idx > 0) {
				final String[] chunks = alphanumericandspacacesonly.split(" ");
				firstname = chunks[0].trim().replaceAll("[^A-Za-z0-9]", "");
				lastname = chunks[chunks.length - 1].replaceAll("[^A-Za-z0-9]", "");
			}

			final StringBuilder sb = new StringBuilder(firstname.substring(0, 1));
			sb.append(lastname.substring(0, 2));
			sb.append(lastname.substring(lastname.length() - 1));
			while (sb.length() < 4) {
				sb.append("X");
			}

			result = sb.toString();
			this.getNameParts().put(Name.NamePart.IDprefix, result);
		}

		return result;
	}

	public boolean equalsIgnoreCase(final String checkstring) {
		try {
			if (!Strings.isBlankString(checkstring)) {

				if (checkstring.equalsIgnoreCase(this.getCanonical()) || checkstring.equalsIgnoreCase(this.getAbbreviated())
						|| checkstring.equalsIgnoreCase(this.getCommon())) {
					return true;
				} else {

					for (final String s : this.getNameParts().values()) {
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
		result = prime * result + ((_parts == null) ? 0 : _parts.hashCode());
		result = prime * result + ((_rfc822name == null) ? 0 : _rfc822name.hashCode());
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
		if (_parts == null) {
			if (other._parts != null) {
				return false;
			}
		} else if (!_parts.equals(other._parts)) {
			return false;
		}
		if (_rfc822name == null) {
			if (other._rfc822name != null) {
				return false;
			}
		} else if (!_rfc822name.equals(other._rfc822name)) {
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
