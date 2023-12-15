/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 */
package org.openntf.domino.ext;

/**
 * @author withersp
 *
 *         OpenNTF extensions to Name object
 */
public interface Name extends Cloneable {

	/**
	 * Gets groups for the person / group / server etc the Name object pertains to.<br/>
	 * The groups include the hierarchical name for the Name object, all Group entries that Name is a member of, and any OUs and O the name
	 * relates to.
	 *
	 * <p>
	 * Sample output: CN=admin/O=Intec-PW,admin,*,*\/O=Intec-PW,LocalDomainAdmins,Domino Developers,SEAS TestRole - Y1
	 * </p>
	 *
	 * @param serverName
	 *            String server name to check against
	 * @return Collection<String> of any Domino Directory Person or Group the Name is found in, plus generic hierarchical responses
	 * @since org.opentf.domino 5.0.0
	 */
	public java.util.Collection<String> getGroups(String serverName);

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
	 */
	public String getRFC82xInternetAddress();

	public org.openntf.domino.Name clone();

	public String getIDprefix();

	public static enum NamePartKey {
		Abbreviated, Addr821, Addr822Comment1, Addr822Comment2, Addr822Comment3, Addr822LocalPart, Addr822Phrase, ADMD, Canonical, Common,
		Country, Generation, Given, Initials, Keyword, Language, Organization, OrgUnit1, OrgUnit2, OrgUnit3, OrgUnit4, PRMD, Surname,
		IDprefix, SourceString, DomainComponent1, DomainComponent2, DomainComponent3, DomainComponent4;

		@Override
		public String toString() {
			return NamePartKey.class.getName() + ": " + this.name(); //$NON-NLS-1$
		}

		public String getInfo() {
			return this.getDeclaringClass() + "." + this.getClass() + ":" + this.name(); //$NON-NLS-1$ //$NON-NLS-2$
		}
	};

	/**
	 * Gets the Name Part for the specified key.
	 *
	 * @param key
	 *            Key identifying the specific mapped Name Part string to return.
	 *
	 * @return Mapped String for the key. Empty string "" if no mapping exists.
	 */
	public String getNamePart(final NamePartKey key);

	public static enum NameFormat {
		/**
		 * NameFormat unknown or not supplied by implementation
		 */
		UNKNOWN(false, false),
		/**
		 * A name without a slash, not containing a "CN="
		 */
		FLAT(false, false),
		/**
		 * Something a priori invalid, like a blank string
		 */
		FLATERROR(false, true),
		/**
		 * A Domino hierarchical name
		 */
		DOMINO(true, false),
		/**
		 * A hierarchical name
		 */
		HIERARCHICAL(true, false),
		/**
		 * An extended hierarchical name with components like /A=ADMD, /Q=Generation, /G=Given, /I=Initials, /P=PRMD, /S=Surname
		 */
		HIERARCHICALEX(true, false),
		/**
		 * A hierarchical name containing errors
		 */
		HIERARCHICALERROR(true, true),
		/**
		 * An RFC822 name (Mail address)
		 */
		RFC822(false, false),
		/**
		 * An invalid RFC822 address
		 */
		RFC822ERROR(false, true);

		private final boolean _hierarchical;
		private final boolean _error;

		private NameFormat(final boolean hierarchical, final boolean error) {
			_hierarchical = hierarchical;
			_error = error;
		}

		public boolean isHierarchical() {
			return _hierarchical;
		}

		public boolean isError() {
			return _error;
		}

	}

	public static enum NameError {
		/**
		 * No error information available
		 */
		NOT_AVAILABLE,
		/**
		 * Blank string on input
		 */
		EMPTY_NAME,
		/**
		 * General syntax error, e.g. last sign = '@'
		 */
		GENERAL_SYNTAX_ERROR,
		/**
		 * Contains parts with XX= as well as parts without, e.g. CN=J Smith/Dev
		 */
		MIXED_HIERARCHICAL,
		/**
		 * Unidentifiable X400 prefix, e.g. OU5=
		 */
		UNKNOWN_PART,
		/**
		 * One part occurs at least twice
		 */
		DOUBLE_PART,
		/**
		 * The first part of a hierarchical address mustn't be empty
		 */
		EMPTY_FIRST_PART,
		/**
		 * There are only 4 OUs allowed
		 */
		TWO_MANY_OUS,
		/**
		 * Invalid RFC822 Internet address
		 */
		INVALID_MAILADDR,
		/**
		 * Invalid RFC822 expression, e.g. missing bracket, missing < or >, ...
		 */
		INVALID_RFC822,
		/**
		 * No error
		 */
		NO_ERROR
	}

	public NameFormat getNameFormat();

	public NameError getNameError();

}
