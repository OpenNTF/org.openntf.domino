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
public class NameHandle extends Base<org.openntf.domino.Name, lotus.domino.Name> implements org.openntf.domino.Name, Comparable<NameHandle> {
	private static final Logger log_ = Logger.getLogger(NameHandle.class.getName());
	private static final long serialVersionUID = 1L;
	private HashMap<NameType, String> _nameParts;
	private RFC822name _rfc822name;
	private String SourceString;
	private boolean Hierarchical;

	public static enum NameType {
		Abbreviated, ADMD, Canonical, Common, Country, Generation, Given, Initials, Keyword, Language, Organization, OrgUnit1, OrgUnit2, OrgUnit3, OrgUnit4, PRMD, Surname, IDprefix;

		@Override
		public String toString() {
			return this.name();
		}

		public String getInfo() {
			return this.getDeclaringClass() + "." + this.getClass() + ":" + this.name();
		}
	};

	/**
	 * * Zero-Argument Constructor
	 */
	public NameHandle() {
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
	public NameHandle(final lotus.domino.Name delegate, final org.openntf.domino.Base<?> parent) {
		super(null, parent);
		this.initialize(delegate);
		Base.s_recycle(delegate);
	}

	//
	//	/**
	//	 * Optional Constructor
	//	 * 
	//	 * @param session
	//	 *            Session used for Name processing
	//	 * @param name
	//	 *            Name from which to create the object
	//	 */
	//	public NameHandle(final Session session, final Name name) {
	//		try {
	//			if (null == session) {
	//				throw new IllegalArgumentException("Session is null");
	//			}
	//			this.initialize(name);
	//			this.computeInternetAddress(session);
	//
	//		} catch (final Exception e) {
	//			DominoUtils.logException(this, e);
	//			throw new RuntimeException("EXCEPTION thrown in in NameHandle Constructor()");
	//		}
	//	}
	//

	/**
	 * Optional Constructor
	 * 
	 * @param session
	 *            Session used for Name processing
	 */
	public NameHandle(final org.openntf.domino.Session session) {
		super(null, session);
		this.initialize(session, session.getEffectiveUserName());
	}

	/**
	 * Optional Constructor
	 * 
	 * @param session
	 *            Session used for Name processing
	 */
	public NameHandle(final lotus.domino.Session session) {
		super(null, (org.openntf.domino.Base<?>) Factory.fromLotus(session, org.openntf.domino.Session.class, null));
		org.openntf.domino.Session s = Factory.fromLotus(session, org.openntf.domino.Session.class, null);
		this.initialize(s, s.getEffectiveUserName());
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
	public NameHandle(final org.openntf.domino.Session session, final String name) {
		super(null, session);
		this.initialize(session, name);
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
	public NameHandle(final lotus.domino.Session session, final String name) {
		super(null, (org.openntf.domino.Base<?>) Factory.fromLotus(session, org.openntf.domino.Session.class, null));
		org.openntf.domino.Session s = Factory.fromLotus(session, org.openntf.domino.Session.class, null);
		this.initialize(s, name);
	}

	/**
	 * Optional Constructor
	 * 
	 * @param name
	 *            String used to construct the Name object
	 */
	public NameHandle(final String name) {
		super(null, (org.openntf.domino.Base<?>) Factory.getSession());
		this.initialize(Factory.getSession(), name);
	}

	/**
	 * Resets the object.
	 */
	public void clear() {
		this.Hierarchical = false;
		if (null != this._nameParts) {
			this._nameParts.clear();
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

	public void setHierarchical(final boolean arg0) {
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

	public HashMap<NameType, String> getNameParts() {
		if (null == this._nameParts) {
			this._nameParts = new HashMap<NameType, String>();
		}

		return this._nameParts;
	}

	public void setNameParts(final HashMap<NameType, String> nameParts) {
		this._nameParts = nameParts;
	}

	/**
	 * @param sourceString
	 *            the sourceString to set
	 */
	public void setSourceString(final String sourceString) {
		this.SourceString = sourceString;
	}

	/**
	 * @return the sourceString
	 */
	public String getSourceString() {
		return this.SourceString;
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
			throw new RuntimeException("EXCEPTION thrown in in NameHandle.initialize()");
		}
	}

	private void initialize(final Name name) {
		this.initialize(name.getDelegate());
	}

	private void initialize(final org.openntf.domino.Session session, final String name) {
		try {
			if (null == session) {
				throw new IllegalArgumentException("Session is null");
			}
			if (Strings.isBlankString(name)) {
				throw new IllegalArgumentException("Source name is null or blank");
			}

			org.openntf.domino.Name n = session.createName(name);
			if (null != n) {
				this.setName(n);
			}

			this.parseRFC82xContent(name);

		} catch (final Exception e) {
			DominoUtils.handleException(e);
			throw new RuntimeException("EXCEPTION thrown in in NameHandle.initialize()");
		}
	}

	//	private void computeInternetAddress(final Session session) {
	//
	//		Name name = null;
	//		try {
	//			final String sourceString = this.getSourceString();
	//
	//			if (!Strings.isBlankString(sourceString)) {
	//				final String pattern = "^.*<.*@.*>$";
	//				/*
	//				 * Match Pattern: anytext<useremail@domain.suffix>
	//				 * 
	//				 * pattern definition:
	//				 * 
	//				 * ^ match the beginning of the string
	//				 * 
	//				 * . match any single character
	//				 * 
	//				 * * match the preceding match character zero or more times.
	//				 * 
	//				 * < match a less thank character
	//				 * 
	//				 * . match any single character
	//				 * 
	//				 * * match the preceding match character zero or more times.
	//				 * 
	//				 * @ match an ampersand character
	//				 * 
	//				 * . match any single character
	//				 * 
	//				 * * match the preceding match character zero or more times.
	//				 * 
	//				 * > match a greater than character
	//				 * 
	//				 * $ match the preceding match instructions against the end of the string.
	//				 */
	//				if (sourceString.matches(pattern)) {
	//					// test matches <useremail@domain.suffix>
	//					String common = "";
	//					final String username = Strings.left(sourceString, "<").trim();
	//					final String patternquoted = "^\".*\"$";
	//					/*
	//					 * Match Pattern: "username"
	//					 * 
	//					 * pattern definition:
	//					 * 
	//					 * ^ match the beginning of the string
	//					 * 
	//					 * \" match a double quote
	//					 * 
	//					 * . match any single character
	//					 * 
	//					 * * match the preceding match character zero or more times.
	//					 * 
	//					 * \" match a double quote
	//					 * 
	//					 * $ match the preceding match instructions against the end of the string.
	//					 */
	//					if (username.matches(patternquoted)) {
	//						final Pattern patternname = Pattern.compile("\"(.+?)\"");
	//						final Matcher matchername = patternname.matcher(sourceString);
	//						matchername.find(); // get the text between the ""
	//						common = matchername.group(1);
	//					} else {
	//						common = username;
	//					}
	//
	//					if (common.indexOf(',') > 0) {
	//						// assume name format of Lastname, Firstname and reverse
	//						// to format of Firstname Lastname
	//						final String[] chunks = common.split(",");
	//						final StringBuilder sb = new StringBuilder();
	//						for (int i = chunks.length - 1; i > -1; i--) {
	//							sb.append(Strings.toProperCase(chunks[i].trim()));
	//							sb.append(" ");
	//						}
	//
	//						common = sb.toString();
	//
	//					} else if (common.indexOf('.') > 0) {
	//						// assume name format of Firstname.Lastname and strip
	//						// out the period
	//						final String[] chunks = common.split("\\.");
	//						final StringBuilder sb = new StringBuilder();
	//						for (final String s : chunks) {
	//							sb.append(Strings.toProperCase(s.trim()));
	//							sb.append(" ");
	//						}
	//
	//						common = sb.toString();
	//					}
	//
	//					if (!Strings.isBlankString(common)) {
	//						name = Names.createName(session, common);
	//						this.setName(name);
	//					}
	//
	//					final Pattern patternemail = Pattern.compile("<(.+?)>");
	//					final Matcher matcheremail = patternemail.matcher(sourceString);
	//					matcheremail.find(); // get the text between the <>
	//					final String email = matcheremail.group(1);
	//					this.setInternetAddress(Strings.isBlankString(email) ? "" : email.toLowerCase());
	//				}
	//			}
	//
	//		} catch (final Exception e) {
	//			DominoUtils.handleException(e);
	//		} finally {
	//			DominoUtils.incinerate(name);
	//		}
	//	}

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
			return this.getParent().getDelegate().createName(this.getCanonical());
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
		return this.getCanonical();
	}

	public void parseRFC82xContent(final String source) {
		this.getRFC822name().parseRFC82xContent(source);
	}

	public void setName(final lotus.domino.Name name) {
		try {
			if (null == name) {
				throw new IllegalArgumentException("Source Name is null");
			}

			final HashMap<NameType, String> p = this.getNameParts();
			p.put(NameHandle.NameType.Abbreviated, name.getAbbreviated());
			p.put(NameHandle.NameType.ADMD, name.getADMD());
			p.put(NameHandle.NameType.Canonical, name.getCanonical());
			p.put(NameHandle.NameType.Common, name.getCommon());
			p.put(NameHandle.NameType.Country, name.getCountry());
			p.put(NameHandle.NameType.Generation, name.getGeneration());
			p.put(NameHandle.NameType.Given, name.getGiven());
			p.put(NameHandle.NameType.Initials, name.getInitials());
			p.put(NameHandle.NameType.Keyword, name.getKeyword());
			p.put(NameHandle.NameType.Language, name.getLanguage());
			p.put(NameHandle.NameType.Organization, name.getOrganization());
			p.put(NameHandle.NameType.OrgUnit1, name.getOrgUnit1());
			p.put(NameHandle.NameType.OrgUnit2, name.getOrgUnit2());
			p.put(NameHandle.NameType.OrgUnit3, name.getOrgUnit3());
			p.put(NameHandle.NameType.OrgUnit4, name.getOrgUnit4());
			p.put(NameHandle.NameType.PRMD, name.getPRMD());
			p.put(NameHandle.NameType.Surname, name.getSurname());

			String phrase = name.getAddr822Phrase();
			String addr821 = name.getAddr821();
			if ((null != addr821) && (addr821.trim().length() > 0)) {
				StringBuilder sb = new StringBuilder((null == phrase) ? "" : phrase.trim());
				sb.append("<");
				sb.append(addr821);
				sb.append(">");

				String comment1 = name.getAddr822Comment1();
				if ((null != comment1) && (comment1.trim().length() > 0)) {
					sb.append("(");
					sb.append(comment1);
					sb.append(")");
				}

				String comment2 = name.getAddr822Comment2();
				if ((null != comment2) && (comment2.trim().length() > 0)) {
					sb.append("(");
					sb.append(comment2);
					sb.append(")");
				}

				String comment3 = name.getAddr822Comment3();
				if ((null != comment3) && (comment3.trim().length() > 0)) {
					sb.append("(");
					sb.append(comment3);
					sb.append(")");
				}

				this.parseRFC82xContent(sb.toString());
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}
	}

	public void setName(final org.openntf.domino.Name name) {
		try {
			if (null == name) {
				throw new IllegalArgumentException("Source Name is null");
			}

			final HashMap<NameType, String> p = this.getNameParts();
			p.put(NameHandle.NameType.Abbreviated, name.getAbbreviated());
			p.put(NameHandle.NameType.ADMD, name.getADMD());
			p.put(NameHandle.NameType.Canonical, name.getCanonical());
			p.put(NameHandle.NameType.Common, name.getCommon());
			p.put(NameHandle.NameType.Country, name.getCountry());
			p.put(NameHandle.NameType.Generation, name.getGeneration());
			p.put(NameHandle.NameType.Given, name.getGiven());
			p.put(NameHandle.NameType.Initials, name.getInitials());
			p.put(NameHandle.NameType.Keyword, name.getKeyword());
			p.put(NameHandle.NameType.Language, name.getLanguage());
			p.put(NameHandle.NameType.Organization, name.getOrganization());
			p.put(NameHandle.NameType.OrgUnit1, name.getOrgUnit1());
			p.put(NameHandle.NameType.OrgUnit2, name.getOrgUnit2());
			p.put(NameHandle.NameType.OrgUnit3, name.getOrgUnit3());
			p.put(NameHandle.NameType.OrgUnit4, name.getOrgUnit4());
			p.put(NameHandle.NameType.PRMD, name.getPRMD());
			p.put(NameHandle.NameType.Surname, name.getSurname());

			String phrase = name.getAddr822Phrase();
			String addr821 = name.getAddr821();
			if ((null != addr821) && (addr821.trim().length() > 0)) {
				StringBuilder sb = new StringBuilder((null == phrase) ? "" : phrase.trim());
				sb.append("<");
				sb.append(addr821);
				sb.append(">");

				String comment1 = name.getAddr822Comment1();
				if ((null != comment1) && (comment1.trim().length() > 0)) {
					sb.append("(");
					sb.append(comment1);
					sb.append(")");
				}

				String comment2 = name.getAddr822Comment2();
				if ((null != comment2) && (comment2.trim().length() > 0)) {
					sb.append("(");
					sb.append(comment2);
					sb.append(")");
				}

				String comment3 = name.getAddr822Comment3();
				if ((null != comment3) && (comment3.trim().length() > 0)) {
					sb.append("(");
					sb.append(comment3);
					sb.append(")");
				}

				this.parseRFC82xContent(sb.toString());
			}

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}
	}

	public String getNamePart(final NameHandle.NameType arg0) {
		try {
			if (null == arg0) {
				throw new IllegalArgumentException("NameType is null");
			}

			final String result = this.getNameParts().get(arg0);
			return (null == result) ? "" : result;

		} catch (final Exception e) {
			DominoUtils.handleException(e);
		}

		return "";
	}

	public String getAbbreviated() {
		return this.getNamePart(NameHandle.NameType.Abbreviated);
	}

	public String getAddr821() {
		return this.getRFC822name().getAddr821();
	}

	public String getAddr822Comment1() {
		return this.getRFC822name().getAddr822Comment1();
	}

	public String getAddr822Comment2() {
		return this.getRFC822name().getAddr822Comment2();
	}

	public String getAddr822Comment3() {
		return this.getRFC822name().getAddr822Comment3();
	}

	public String getAddr822LocalPart() {
		return this.getRFC822name().getAddr822LocalPart();
	}

	public String getAddr822Phrase() {
		return this.getRFC822name().getAddr822Phrase();
	}

	public String getAddr822Full() {
		return this.getRFC822name().getAddr822Full();
	}

	public String getAddr822FullFirstLast() {
		return this.getRFC822name().getAddr822FullFirstLast();
	}

	public String getADMD() {
		return this.getNamePart(NameHandle.NameType.ADMD);
	}

	public String getCanonical() {
		return this.getNamePart(NameHandle.NameType.Canonical);
	}

	public String getCommon() {
		return this.getNamePart(NameHandle.NameType.Common);
	}

	public String getCountry() {
		return this.getNamePart(NameHandle.NameType.Country);
	}

	public String getGeneration() {
		return this.getNamePart(NameHandle.NameType.Generation);
	}

	public String getGiven() {
		return this.getNamePart(NameHandle.NameType.Given);
	}

	public String getInitials() {
		return this.getNamePart(NameHandle.NameType.Initials);
	}

	public String getKeyword() {
		return this.getNamePart(NameHandle.NameType.Keyword);
	}

	public String getLanguage() {
		return this.getNamePart(NameHandle.NameType.Language);
	}

	public String getOrganization() {
		return this.getNamePart(NameHandle.NameType.Organization);
	}

	public String getOrgUnit1() {
		return this.getNamePart(NameHandle.NameType.OrgUnit1);
	}

	public String getOrgUnit2() {
		return this.getNamePart(NameHandle.NameType.OrgUnit2);
	}

	public String getOrgUnit3() {
		return this.getNamePart(NameHandle.NameType.OrgUnit3);
	}

	public String getOrgUnit4() {
		return this.getNamePart(NameHandle.NameType.OrgUnit4);
	}

	public String getPRMD() {
		return this.getNamePart(NameHandle.NameType.PRMD);
	}

	public String getSurname() {
		return this.getNamePart(NameHandle.NameType.Surname);
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
	 * Determines if one of the NameHandle's properties begins with the prefix.
	 * 
	 * Checks the following properties, in order:
	 * <ul>
	 * <li>Abbreviated</li>
	 * <li>Canonical</li>
	 * </ul>
	 * 
	 * @param prefix
	 *            Value to compare to the properties of the NameHandle
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

	public String getIDprefix() {
		String result = this.getNamePart(NameHandle.NameType.IDprefix);
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
			this.getNameParts().put(NameHandle.NameType.IDprefix, result);
		}

		return result;
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

	//	/*
	//	 * (non-Javadoc)
	//	 * 
	//	 * @see java.lang.Object.hashCode()
	//	 */
	//	@Override
	//	public int hashCode() {
	//		final int prime = 31;
	//		int result = 1;
	//		result = (prime * result) + (this.Hierarchical ? 1231 : 1237);
	//		result = (prime * result) + ((null == this.SourceString) ? 0 : this.SourceString.hashCode());
	//		result = (prime * result) + ((null == this.InternetAddress) ? 0 : this.InternetAddress.hashCode());
	//		result = (prime * result) + ((Strings.isBlankString(this.getCanonical())) ? 0 : this.getCanonical().hashCode());
	//		return result;
	//	}
	//
	//	/*
	//	 * (non-Javadoc)
	//	 * 
	//	 * @see java.lang.Object#equals(java.lang.Object)
	//	 */
	//	@Override
	//	public boolean equals(final Object obj) {
	//		if (this == obj) {
	//			return true;
	//		}
	//		if (null == obj) {
	//			return false;
	//		}
	//		if (!(obj instanceof NameHandle)) {
	//			return false;
	//		}
	//
	//		final NameHandle other = (NameHandle) obj;
	//		if (null == this.SourceString) {
	//			if (null != other.SourceString) {
	//				return false;
	//			}
	//		} else if (!this.SourceString.equals(other.SourceString)) {
	//			return false;
	//		}
	//
	//		if (this.Hierarchical != other.Hierarchical) {
	//			return false;
	//		}
	//
	//		if (null == this.InternetAddress) {
	//			if (null != other.InternetAddress) {
	//				return false;
	//			}
	//		} else if (!this.InternetAddress.equals(other.InternetAddress)) {
	//			return false;
	//		}
	//
	//		if (Strings.isBlankString(this.getCanonical())) {
	//			if (!Strings.isBlankString(other.getCanonical())) {
	//				return false;
	//			}
	//		}
	//
	//		return (this.getCanonical().equals(other.getCanonical()));
	//	}

	/**
	 * Compares this object with another NameHandle
	 * 
	 * @param arg0
	 *            NameHandle object to be compared.
	 * 
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 * 
	 * 
	 * @see java.lang.Comparable#compareTo(Object)
	 * @see packers.czardev.util.Core#LESS_THAN
	 * @see packers.czardev.util.Core#EQUAL
	 * @see packers.czardev.util.Core#GREATER_THAN
	 */
	public int compareTo(final NameHandle arg0) {
		return NameHandle.compare(this, arg0);
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
	 *            First NameHandle object for comparison.
	 * @param arg1
	 *            Second NameHandle object for comparison.
	 * 
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 * 
	 * 
	 * @see java.lang.Comparable#compareTo(Object)
	 * @see packers.czardev.util.Core#LESS_THAN
	 * @see packers.czardev.util.Core#EQUAL
	 * @see packers.czardev.util.Core#GREATER_THAN
	 */
	public static int compare(final NameHandle arg0, final NameHandle arg1) {
		if (null == arg0) {
			return (null == arg1) ? DominoUtils.EQUAL : DominoUtils.LESS_THAN;
		} else if (null == arg1) {
			return DominoUtils.GREATER_THAN;
		}

		return (arg0.equals(arg1)) ? DominoUtils.EQUAL : arg0.getAbbreviated().compareTo(arg1.getAbbreviated());
	}

}
