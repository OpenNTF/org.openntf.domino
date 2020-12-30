/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
package org.openntf.domino.impl;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.logging.Logger;

import lotus.domino.NotesException;
import lotus.notes.addins.DominoServer;

import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.Strings;
import org.openntf.formula.impl.StringSplitSimple;

/**
 * The class NameODA - alternative implementation for Name
 * 
 * @author Praml, Steinsiek
 */

@SuppressWarnings("nls")
public class NameODA extends BaseThreadSafe<org.openntf.domino.Name, lotus.domino.Name, Session> implements org.openntf.domino.Name,
Comparable<org.openntf.domino.Name>, Cloneable {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(Name.class.getName());

	/*-------------------------------------------------------------------------------------*/
	/*
	 * Constructors
	 */
	@Deprecated
	// Needed for Externalization
	public NameODA() {
		super(null, Factory.getSession(SessionType.CURRENT), NOTES_NAME);
	}

	// Called from WrapperFactory.create
	protected NameODA(final Session sess, final String name, final String lang) {
		super(null, sess, NOTES_NAME);
		_language = null2Empty(lang);
		parse(name);
	}

	// Called from WrapperFactory.wrapLotusObject
	protected NameODA(final lotus.domino.Name delegate, final Session parent) {
		super(delegate, parent, NOTES_NAME);
		try {
			_language = delegate.getLanguage();
			parse(delegate.getCanonical());
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		} finally {
			// WARNING: Wrapping recycles the caller's object. This may cause issues, if 
			// the Lotus object is used outside openNTF
			Base.s_recycle(delegate);
		}
	}

	/*-------------------------------------------------------------------------------------*/
	/*
	 * Simple String methods
	 */
	private static final String _emptyString = "";

	private static String null2Empty(final String s) {
		return (s == null) ? _emptyString : s;
	}

	private static boolean isEmpty(final String s) {
		return s == null || s.isEmpty();
	}

	/*-------------------------------------------------------------------------------------*/
	/*
	 * Instance variables
	 */
	private String _language;

	private NameFormat _nameFormat = NameFormat.UNKNOWN;
	private NameError _nameError = NameError.NO_ERROR;

	private String _sourceString;
	private String _addr821;
	private String[] _addr822Comments = new String[3];
	private String _addr822LocalPart;
	private String _addr822Phrase;

	private String _canonical;
	private String _abbreviated;
	private String _keyword;

	private static final int _iA = 0;			// ADMD
	private static final int _iCN = _iA + 1;	// Common
	private static final int _iC = _iCN + 1;	// Country
	private static final int _iQ = _iC + 1;		// Generation
	private static final int _iG = _iQ + 1;		// Given
	private static final int _iI = _iG + 1;		// Initials
	private static final int _iO = _iI + 1;		// Organization
	private static final int _iOU1 = _iO + 1;	// OrgUnit1
	private static final int _iOU2 = _iOU1 + 1;	// OrgUnit2
	private static final int _iOU3 = _iOU2 + 1;	// OrgUnit3
	private static final int _iOU4 = _iOU3 + 1;	// OrgUnit4
	private static final int _iP = _iOU4 + 1;	// PRMD
	private static final int _iS = _iP + 1;		// Surname;
	private static final int _hpSize = _iS + 1;	// # of parts
	private static final int _iOU = _hpSize;		// Pseudo for OU=

	private String[] _hierParts = new String[_hpSize];

	private static final String[] _hierPartPrefices = { "A=", "CN=", "C=", "Q=", "G=", "I=", "O=", //
		"OU1=", "OU2=", "OU3=", "OU4=", "P=", "S=", "OU=" };

	private String _routingHint;	// For addresses like CN=John Smith/OU=HR/O=Shell/C=US@SHELL@Esso
	// In this case, the hint will be "SHELL@Esso"
	private String _idPrefix;

	/*-------------------------------------------------------------------------------------*/
	/*
	 * ODA methods
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

	@Override
	protected WrapperFactory getFactory() {
		return parent.getFactory();
	}

	@Override
	public final Session getAncestorSession() {
		return parent;
	}

	@Override
	public final Session getParent() {
		return parent;
	}

	/*-------------------------------------------------------------------------------------*/
	/*
	 * A few additional methods
	 */
	@Override
	// Since Name is immutable (at moment), this simply becomes:
	public NameODA clone() {
		return this;
	}

	@Override
	public boolean isHierarchical() {
		return _nameFormat.isHierarchical();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> getGroups(final String serverName) {
		Collection<String> result = null;
		try {
			DominoServer server = new DominoServer(serverName);
			result = server.getNamesList(getCanonical());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return result;
	}

	@Override
	public NameFormat getNameFormat() {
		return _nameFormat;
	}

	@Override
	public NameError getNameError() {
		return _nameError;
	}

	/*-------------------------------------------------------------------------------------*/
	/*
	 * toString, equals etc.
	 */
	@Override
	public String toString() {
		return getClass().getName() + " [Canonical='" + getCanonical() + "', Language='" + _language + "']";
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof NameODA))	// This comprises obj = null
			return false;
		NameODA other = (NameODA) obj;
		return getCanonical().equals(other.getCanonical()) && getAbbreviated().equals(other.getAbbreviated())
				&& _language.equals(other._language);
	}

	@Override
	public int compareTo(final org.openntf.domino.Name other) {
		if (other == null)
			return 1;
		return getCanonical().compareTo(other.getCanonical());
	}

	/*-------------------------------------------------------------------------------------*/
	/*
	 * Externalization
	 */
	private static final int EXTERNALVERSIONUID = 20141219; // The current date (when it was implemented)

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		int version = in.readInt();
		if (version != EXTERNALVERSIONUID)
			throw new InvalidClassException("Cannot read data version " + version);
		String canonical = in.readUTF();
		_language = in.readUTF();
		this.parse(canonical);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeInt(EXTERNALVERSIONUID);
		out.writeUTF(getCanonical());
		out.writeUTF(_language);
	}

	/*-------------------------------------------------------------------------------------*/
	/*
	 * Lotus-get-methods
	 */
	@Override
	public String getAbbreviated() {
		if (_abbreviated == null)
			_abbreviated = (_nameFormat == NameFormat.HIERARCHICAL) ? buildX400Path(false, false) : _sourceString;
			return _abbreviated;
	}

	@Override
	public String getAddr821() {
		return null2Empty(_addr821);
	}

	@Override
	public String getAddr822Comment1() {
		return null2Empty(_addr822Comments[0]);
	}

	@Override
	public String getAddr822Comment2() {
		return null2Empty(_addr822Comments[1]);
	}

	@Override
	public String getAddr822Comment3() {
		return null2Empty(_addr822Comments[2]);
	}

	@Override
	public String getAddr822LocalPart() {
		return null2Empty(_addr822LocalPart);
	}

	@Override
	public String getAddr822Phrase() {
		return null2Empty(_addr822Phrase);
	}

	@Override
	public String getADMD() {
		return null2Empty(_hierParts[_iA]);
	}

	@Override
	public String getCanonical() {
		if (_canonical == null)
			_canonical = _nameFormat.isHierarchical() && !_nameFormat.isError() ? buildX400Path(true, true) : null2Empty(_sourceString);
			return _canonical;
	}

	@Override
	public String getCommon() {
		return null2Empty(_hierParts[_iCN]);
	}

	@Override
	public String getCountry() {
		return null2Empty(_hierParts[_iC]);
	}

	@Override
	public String getGeneration() {
		return null2Empty(_hierParts[_iQ]);
	}

	@Override
	public String getGiven() {
		return null2Empty(_hierParts[_iG]);
	}

	@Override
	public String getInitials() {
		return null2Empty(_hierParts[_iI]);
	}

	@Override
	public String getKeyword() {
		if (_keyword == null) {
			if (_nameFormat != NameFormat.HIERARCHICAL)
				_keyword = _emptyString;
			else {
				StringBuilder sb = new StringBuilder();
				if (!isEmpty(_hierParts[_iC])) {
					if (sb.length() != 0)
						sb.append('\\');
					sb.append(_hierParts[_iC]);
				}
				if (!isEmpty(_hierParts[_iO])) {
					if (sb.length() != 0)
						sb.append('\\');
					sb.append(_hierParts[_iO]);
				}
				if (!isEmpty(_hierParts[_iOU1])) {
					if (sb.length() != 0)
						sb.append('\\');
					sb.append(_hierParts[_iOU1]);
				}
				if (!isEmpty(_hierParts[_iOU2])) {
					if (sb.length() != 0)
						sb.append('\\');
					sb.append(_hierParts[_iOU2]);
				}
				if (!isEmpty(_hierParts[_iOU3])) {
					if (sb.length() != 0)
						sb.append('\\');
					sb.append(_hierParts[_iOU3]);
				}
				if (!isEmpty(_hierParts[_iOU4])) {
					if (sb.length() != 0)
						sb.append('\\');
					sb.append(_hierParts[_iOU4]);
				}
				_keyword = sb.toString();
			}
		}
		return _keyword;
	}

	@Override
	public String getLanguage() {
		return _language;
	}

	@Override
	public String getOrganization() {
		return null2Empty(_hierParts[_iO]);
	}

	@Override
	public String getOrgUnit1() {
		return null2Empty(_hierParts[_iOU1]);
	}

	@Override
	public String getOrgUnit2() {
		return null2Empty(_hierParts[_iOU2]);
	}

	@Override
	public String getOrgUnit3() {
		return null2Empty(_hierParts[_iOU3]);
	}

	@Override
	public String getOrgUnit4() {
		return null2Empty(_hierParts[_iOU4]);
	}

	@Override
	public String getPRMD() {
		return null2Empty(_hierParts[_iP]);
	}

	@Override
	public String getSurname() {
		return null2Empty(_hierParts[_iS]);
	}

	/*-------------------------------------------------------------------------------------*/
	/*
	 * Auxiliary method for building X400 paths
	 */
	private String buildX400Path(final boolean withPref, final boolean includeOthers) {
		StringBuilder sb = new StringBuilder();
		if (includeOthers) {
			if (!isEmpty(_hierParts[_iI])) {
				if (sb.length() != 0)
					sb.append('/');
				if (withPref)
					sb.append(_hierPartPrefices[_iI]);
				sb.append(_hierParts[_iI]);
			}
			if (!isEmpty(_hierParts[_iG])) {
				if (sb.length() != 0)
					sb.append('/');
				if (withPref)
					sb.append(_hierPartPrefices[_iG]);
				sb.append(_hierParts[_iG]);
			}
			if (!isEmpty(_hierParts[_iS])) {
				if (sb.length() != 0)
					sb.append('/');
				if (withPref)
					sb.append(_hierPartPrefices[_iS]);
				sb.append(_hierParts[_iS]);
			}
			if (!isEmpty(_hierParts[_iQ])) {
				if (sb.length() != 0)
					sb.append('/');
				if (withPref)
					sb.append(_hierPartPrefices[_iQ]);
				sb.append(_hierParts[_iQ]);
			}
		}
		if (!isEmpty(_hierParts[_iCN])) {
			if (sb.length() != 0)
				sb.append('/');
			if (withPref && _hierParts[_iCN].charAt(0) != '*')
				sb.append(_hierPartPrefices[_iCN]);
			sb.append(_hierParts[_iCN]);
		}
		if (includeOthers) {
			if (!isEmpty(_hierParts[_iA])) {
				if (sb.length() != 0)
					sb.append('/');
				if (withPref)
					sb.append(_hierPartPrefices[_iA]);
				sb.append(_hierParts[_iA]);
			}
			if (!isEmpty(_hierParts[_iP])) {
				if (sb.length() != 0)
					sb.append('/');
				if (withPref)
					sb.append(_hierPartPrefices[_iP]);
				sb.append(_hierParts[_iP]);
			}
		}
		if (!isEmpty(_hierParts[_iOU4])) {
			if (sb.length() != 0)
				sb.append('/');
			if (withPref)
				sb.append(_hierPartPrefices[_iOU]);
			sb.append(_hierParts[_iOU4]);
		}
		if (!isEmpty(_hierParts[_iOU3])) {
			if (sb.length() != 0)
				sb.append('/');
			if (withPref)
				sb.append(_hierPartPrefices[_iOU]);
			sb.append(_hierParts[_iOU3]);
		}
		if (!isEmpty(_hierParts[_iOU2])) {
			if (sb.length() != 0)
				sb.append('/');
			if (withPref)
				sb.append(_hierPartPrefices[_iOU]);
			sb.append(_hierParts[_iOU2]);
		}
		if (!isEmpty(_hierParts[_iOU1])) {
			if (sb.length() != 0)
				sb.append('/');
			if (withPref)
				sb.append(_hierPartPrefices[_iOU]);
			sb.append(_hierParts[_iOU1]);
		}
		if (!isEmpty(_hierParts[_iO])) {
			if (sb.length() != 0)
				sb.append('/');
			if (withPref)
				sb.append(_hierPartPrefices[_iO]);
			sb.append(_hierParts[_iO]);
		}
		if (!isEmpty(_hierParts[_iC])) {
			if (sb.length() != 0)
				sb.append('/');
			if (withPref)
				sb.append(_hierPartPrefices[_iC]);
			sb.append(_hierParts[_iC]);
		}
		if (!isEmpty(_routingHint)) {
			sb.append('@');
			sb.append(_routingHint);
		}
		return sb.toString();
	}

	/*-------------------------------------------------------------------------------------*/
	/*
	 * Additional methods from ext interface
	 */
	@Override
	public String getRFC82xInternetAddress() {
		if (_nameFormat != NameFormat.RFC822)
			return _emptyString;
		// Works like javax.mail.internet.InternetAddress.toString (only difference: phrase is always quoted)
		if (isEmpty(_addr822Phrase) && isEmpty(_addr822Comments[0]))
			return _addr821;
		String phrase = isEmpty(_addr822Phrase) ? _addr822Comments[0] : _addr822Phrase;
		int lh = phrase.length();
		StringBuilder sb = new StringBuilder(lh + _addr821.length() + 32);
		sb.append('"');
		for (int i = 0; i < lh; i++) {
			char c = phrase.charAt(i);
			if (c == '"' || c == '\\')
				sb.append('\\');
			sb.append(c);
		}
		sb.append('"');
		sb.append(' ');
		sb.append('<');
		sb.append(_addr821);
		sb.append('>');
		return sb.toString();
	}

	@Override
	public String getIDprefix() {
		if (isEmpty(_idPrefix)) {
			char[] idp = new char[4];
			int count = 0;
			if (!isEmpty(_hierParts[_iCN])) {
				String aux = _hierParts[_iCN].toUpperCase().replaceAll("[^A-Z0-9 ]", "");
				String[] parts = aux.split(" ");
				if (parts.length >= 1) {
					String firstName = parts[0];
					String lastName = parts[parts.length - 1];
					if (!firstName.isEmpty())
						idp[count++] = firstName.charAt(0);
					int lh = lastName.length();
					if (lh > 0) {
						idp[count++] = lastName.charAt(0);
						if (lh > 1) {
							idp[count++] = lastName.charAt(1);
							idp[count++] = lastName.charAt(lh - 1);
						}
					}
				}
			}
			while (count < 4)
				idp[count++] = 'X';
			_idPrefix = new String(idp);
		}
		return _idPrefix;
	}

	@Override
	public String getNamePart(final NamePartKey key) {
		String ret;
		if (key == NamePartKey.Abbreviated)
			ret = getAbbreviated();
		else if (key == NamePartKey.Addr821)
			ret = _addr821;
		else if (key == NamePartKey.Addr822Comment1)
			ret = _addr822Comments[0];
		else if (key == NamePartKey.Addr822Comment2)
			ret = _addr822Comments[1];
		else if (key == NamePartKey.Addr822Comment3)
			ret = _addr822Comments[2];
		else if (key == NamePartKey.Addr822LocalPart)
			ret = _addr822LocalPart;
		else if (key == NamePartKey.Addr822Phrase)
			ret = _addr822Phrase;
		else if (key == NamePartKey.ADMD)
			ret = _hierParts[_iA];
		else if (key == NamePartKey.Canonical)
			ret = getCanonical();
		else if (key == NamePartKey.Common)
			ret = _hierParts[_iCN];
		else if (key == NamePartKey.Country)
			ret = _hierParts[_iC];
		else if (key == NamePartKey.Generation)
			ret = _hierParts[_iQ];
		else if (key == NamePartKey.Given)
			ret = _hierParts[_iG];
		else if (key == NamePartKey.Initials)
			ret = _hierParts[_iI];
		else if (key == NamePartKey.Keyword)
			ret = getKeyword();
		else if (key == NamePartKey.Language)
			ret = _language;
		else if (key == NamePartKey.Organization)
			ret = _hierParts[_iO];
		else if (key == NamePartKey.OrgUnit1)
			ret = _hierParts[_iOU1];
		else if (key == NamePartKey.OrgUnit2)
			ret = _hierParts[_iOU2];
		else if (key == NamePartKey.OrgUnit3)
			ret = _hierParts[_iOU3];
		else if (key == NamePartKey.OrgUnit4)
			ret = _hierParts[_iOU4];
		else if (key == NamePartKey.PRMD)
			ret = _hierParts[_iP];
		else if (key == NamePartKey.Surname)
			ret = _hierParts[_iS];
		else if (key == NamePartKey.IDprefix)
			ret = getIDprefix();
		else
			// if(key==NamePartKey.SourceString)
			ret = _sourceString;
		return null2Empty(ret);
	}

	/*-------------------------------------------------------------------------------------*/
	/*
	 * Parsing
	 */
	private void parse(final String name) {
		_sourceString = name.trim();
		int indAt = _sourceString.indexOf('@');
		if (indAt < 0) { // Then it's canonical or abbreviated
			parseHierarchical(_sourceString);
			return;
		}
		// There are several possibilities: An RFC822 Internet address, a hierarchical address of the form
		// [CN=]xy/[OU=]xx/[O=]zz@zz[@ww], or a Notes mail address sitting in an Internet address:
		// Phrase <John Smith/Dev/Company/EN%Company@Company.en>.
		// Since Notes doesn't seem to handle addresses of the latter form correctly, we won't consider
		// such addresses in the following.
		//
		// Moreover, the '@' may sit in an RFC822 phrase: a@b <a@b> is correct. (So is a@/b<a@b>, too.)
		// [In this latter case, the "real" address must be enclosed in <>.]
		// Unfortunately, the following address is likewise correct: "<a@b>" <a@b>
		// Finally, expressions like <a@b>(a<@@b) or a@b(a@@b) or even a@b(a@\(@>b) are also admissible.
		// Hence we first have a look at the tail in order to extract possible RFC822 comments.
		//
		char c = _sourceString.charAt(_sourceString.length() - 1);
		if (c == ')' || c == '>') { // It should be an Internet address
			parseRFC822(_sourceString);
			return;
		}
		if (c == '@') { // That is obviously not allowed
			_nameFormat = NameFormat.FLATERROR;
			_nameError = NameError.GENERAL_SYNTAX_ERROR;
			return;
		}
		// Remaining cases: Pure Internet address a@b or extended hierarchical address.
		// A 100% solution seems impossible: The following Internet address is syntactically correct
		//		CN=John-Smith/OU=Dev/O=Comp/C=en@Comp
		// The only totally reliable criterion would be the presence of a blank left to the '@', e.g.
		// CN=John Smith, then it must be hierarchical.
		// So we make a compromise: If there isn't such a blank present, we consider the name as
		// hierarchical, if it contains a '/', or begins with one of the defined X400 prefixes (CN=,
		// OU=, ...).
		//
		String leftPart = _sourceString.substring(0, indAt);
		for (;;) {
			if (leftPart.indexOf(' ') > 0)
				break;
			if (leftPart.indexOf('/') >= 0)
				break;
			if (getHierPartIndex(leftPart) >= 0)
				break;
			// We are left in the case of a simple Internet address john.smith@x.y
			parseRFC822(_sourceString);
			return;
		}
		_routingHint = _sourceString.substring(indAt + 1);
		parseHierarchical(leftPart);
	}

	/*-------------------------------------------------------------------------------------*/
	private void parseHierarchical(final String what) {
		if (what.isEmpty()) {
			_nameFormat = NameFormat.FLATERROR;
			_nameError = NameError.EMPTY_NAME;
			return;
		}
		StringSplitSimple sss = new StringSplitSimple(what, '/');
		int numP1 = sss.split(true);
		String[] parts = new String[numP1];
		int[] hpis = new int[numP1];
		int numParts = 0;
		for (int i = 0; i < numP1; i++) {
			String part = sss.getSplitN(i, true);
			if (part.isEmpty()) {
				if (i == 0) {
					_nameFormat = NameFormat.HIERARCHICALERROR;
					_nameError = NameError.EMPTY_FIRST_PART;
					return;
				}
				continue; // Ignore empty non-first part
			}
			int hpi = getHierPartIndex(part);
			if (hpi < 0 && part.indexOf('=') >= 0) {
				_nameFormat = NameFormat.HIERARCHICALERROR;
				_nameError = NameError.UNKNOWN_PART;
				return;
			}
			if (hpi >= 0 && part.length() == _hierPartPrefices[hpi].length())
				continue;	// Ignore parts consisting only of "O=" or similar
			parts[numParts] = part;
			hpis[numParts++] = hpi;
		}
		if (numParts == 0) {	// I.e. only CN=/O=/C=
			_nameFormat = NameFormat.HIERARCHICALERROR;
			_nameError = NameError.EMPTY_NAME;
			return;
		}
		boolean abbreviated;
		if (hpis[0] >= 0)
			abbreviated = false;
		else if (parts[0].charAt(0) == '*')
			abbreviated = (numParts == 1 || hpis[1] < 0);
		else
			abbreviated = true;
		for (int i = 1; i < numParts; i++)
			if (abbreviated != (hpis[i] < 0)) {
				_nameFormat = NameFormat.HIERARCHICALERROR;
				_nameError = NameError.MIXED_HIERARCHICAL;
				return;
			}
		_nameFormat = NameFormat.HIERARCHICAL;
		if (abbreviated)
			parseHierAbbrev(parts, numParts);
		else
			parseHierX400(parts, hpis, numParts);
	}

	/*-------------------------------------------------------------------------------------*/
	private void parseHierAbbrev(final String[] parts, int numParts) {
		_hierParts[_iCN] = parts[0];
		if (numParts == 1) {
			_nameFormat = NameFormat.FLAT;
			return;
		}
		String country = parts[numParts - 1];
		if (lookupCountry(country)) {
			_hierParts[_iC] = country;
			if (--numParts == 1)	// Only Country present
				return;
		}
		int numOUs = numParts - 2;
		if (numOUs > 4) {
			_nameFormat = NameFormat.HIERARCHICALERROR;
			_nameError = NameError.TWO_MANY_OUS;
			return;
		}
		_hierParts[_iO] = parts[numParts - 1];
		for (int i = 0; i < numOUs; i++)
			_hierParts[_iOU1 + i] = parts[numParts - 2 - i];
	}

	private static Comparator<String> _icComp = new Comparator<String>() {
		@Override
		public int compare(final String s1, final String s2) {
			return s1.compareToIgnoreCase(s2);
		}
	};

	private boolean lookupCountry(final String country) {
		return (country.length() == 2 && Arrays.binarySearch(Locale.getISOCountries(), country, _icComp) >= 0);
	}

	/*-------------------------------------------------------------------------------------*/
	private void parseHierX400(final String[] parts, final int[] hpis, final int numParts) {
		//
		// Treating the OUs is rather cumbersome, because OU= may be mixed with OUn=, ...
		// BTW: Notes seems to not always work entirely correct, when dealing with mixed OU=/OUn=.
		// For example, take the address cn=John Smith/O=Comp/ou2=xxx/ou=dev/ou4=dd/ou=hr
		// Then Notes-getCanonical gives: CN=John Smith/OU=dd/OU=dev/OU=hr/O=Comp
		// That is, ou2=xxx gets lost.
		int[] ouExpl = new int[4];
		int[] ouImpl = new int[4];
		int numOUExpl = 0, numOUImpl = 0;
		for (int i = 0; i < 4; i++)
			ouExpl[i] = ouImpl[i] = -1;
		for (int i = numParts - 1; i >= 0; i--) {
			int hpi = hpis[i];
			if (hpi >= _iOU1 && hpi <= _iOU4) {
				if (ouExpl[hpi - _iOU1] != -1) {
					numOUExpl = -111;
					break;
				}
				ouExpl[hpi - _iOU1] = i;
				hpis[i] = -1;
				numOUExpl++;
			} else if (hpi == _iOU) {
				if (++numOUImpl > 4)
					break;
				ouImpl[numOUImpl - 1] = i;
				hpis[i] = -1;
			}
		}
		if (numOUExpl == -111) {
			_nameFormat = NameFormat.HIERARCHICALERROR;
			_nameError = NameError.DOUBLE_PART;
			return;
		}
		if (numOUImpl + numOUExpl > 4) {
			_nameFormat = NameFormat.HIERARCHICALERROR;
			_nameError = NameError.TWO_MANY_OUS;
			return;
		}
		for (int i = 0; i < numOUImpl; i++)
			for (int j = 0; j < 4; j++)
				if (ouExpl[j] == -1) {
					ouExpl[j] = ouImpl[i];
					break;
				}
		numOUExpl += numOUImpl;
		//
		// The rest is trivial
		//
		for (int i = 0; i < numParts; i++) {
			int hpi = hpis[i];
			if (hpi == -1)
				continue;
			if (!isEmpty(_hierParts[hpi])) {
				_nameFormat = NameFormat.HIERARCHICALERROR;
				_nameError = NameError.DOUBLE_PART;
				return;
			}
			String toSet = parts[i].substring(_hierPartPrefices[hpi].length()).trim();
			_hierParts[hpi] = toSet;
			if (hpi == _iA || hpi == _iQ || hpi == _iG || hpi == _iI || hpi == _iP || hpi == _iS)
				_nameFormat = NameFormat.HIERARCHICALEX;
		}
		for (int i = 0; i < 4; i++)
			if (ouExpl[i] != -1) {
				String s = parts[ouExpl[i]];
				String toSet = s.substring(s.indexOf('=') + 1).trim();
				_hierParts[_iOU1 + i] = toSet;
			}
		// Finally: Special treatment in case when CN is given, but no O= and no OU=
		// Lotus only does this special treatment in case of an address CN=xxx, but never, when
		// "additional" X400 parts are present (G=, Q=, ...). We are not going to imitate that.
		//
		if (!isEmpty(_hierParts[_iCN]) && isEmpty(_hierParts[_iO]) && numOUExpl == 0 && this != _serverName) {
			if (_serverName._nameFormat == NameFormat.UNKNOWN) {
				synchronized (_serverName) {
					_serverName.parse(Factory.getLocalServerName());
				}
				if (!_serverName._nameFormat.isError() && _serverName.isHierarchical()) {
					for (int i = _iO; i <= _iOU4; i++)
						_hierParts[i] = _serverName._hierParts[i];
					if (isEmpty(_hierParts[_iC]))
						_hierParts[_iC] = _serverName._hierParts[_iC];
				}
			}
		}
	}

	private static NameODA _serverName = new NameODA();

	/*-------------------------------------------------------------------------------------*/
	private int getHierPartIndex(final String part) {
		//
		// Question: Should also expressions like "CN = xxx" be supported (blank in front of '=')?
		// At the moment, we don't. (And neither does Notes.)
		//
		for (int i = 0; i <= _hpSize; i++)
			if (Strings.startsWithIgnoreCase(part, _hierPartPrefices[i]))
				return i;
		return -1;
	}

	/*-------------------------------------------------------------------------------------*/
	private void parseRFC822(final String what) {
		// First we examine possible comments:
		int lh = what.length() - 1;
		char c = what.charAt(lh);
		if (c == ')') {
			if ((lh = parse822Comments(what, lh)) < 0) {
				_nameFormat = NameFormat.RFC822ERROR;
				_nameError = NameError.INVALID_RFC822;
				return;
			}
			c = what.charAt(lh);
		}
		String mailAddr;
		if (c != '>')
			mailAddr = (lh == what.length() - 1) ? what : what.substring(0, lh + 1);
		else if ((mailAddr = parse822PhrasePlusAddr(what, lh)) == null) {
			_nameFormat = NameFormat.RFC822ERROR;
			_nameError = NameError.INVALID_RFC822;
			return;
		}
		if (!checkMailAddress(mailAddr)) {
			_nameFormat = NameFormat.RFC822ERROR;
			_nameError = NameError.INVALID_MAILADDR;
			return;
		}
		_addr821 = mailAddr;
		_nameFormat = NameFormat.RFC822;
		//
		// Finally set up a "Common Name": Lotus sets CN to 822Phrase or, if phrase is empty, to 822LocalPart
		// In accordance with javax.mail, we set CN to 822Comment1, if phrase is empty, and finally to
		// 822LocalPart, if both phrase and comment1 are empty
		//
		if (isEmpty(_hierParts[_iCN]))
			_hierParts[_iCN] = !isEmpty(_addr822Phrase) ? _addr822Phrase : !isEmpty(_addr822Comments[0]) ? _addr822Comments[0]
					: _addr822LocalPart;
	}

	/*-------------------------------------------------------------------------------------*/
	private String parse822PhrasePlusAddr(final String what, final int endPos) {
		int startPos;
		for (startPos = endPos - 1; startPos >= 0; startPos--)
			if (what.charAt(startPos) == '<')
				break;
		if (startPos < 0)
			return null;
		String ret = what.substring(startPos + 1, endPos).trim();
		if (ret.isEmpty())
			return null;
		for (startPos--; startPos >= 0; startPos--)
			if (!Character.isWhitespace(what.charAt(startPos)))
				break;
		if (startPos < 0)
			return ret;
		// One more difference to Lotus: Lotus doesn't respect the fact that a phrase may be enclosed in
		// quotes. And this is essential in case the phrase contains '<' or '>'.
		//
		boolean inQuotes = (what.charAt(0) == '"' && what.charAt(startPos) == '"');
		if (!inQuotes) {
			for (int i = 0; i <= startPos; i++) {
				char c = what.charAt(i);
				if (c == '<' || c == '>')
					return null;
			}
			_addr822Phrase = what.substring(0, startPos + 1).trim();
		} else {
			if (startPos == 0)
				return null;
			if (startPos == 1)
				return ret;
			if ((countBackslashesBefore(what, startPos) & 1) != 0)
				return null;
			for (int i = startPos - 1; i > 0; i--) {
				if (what.charAt(i) != '"')
					continue;
				if ((countBackslashesBefore(what, i) & 1) == 0)
					return null;
			}
			char[] sp = new char[startPos + 1];
			int filled = 0;
			for (int i = 1; i < startPos; i++) {
				char c = what.charAt(i);
				if (c == '\\')
					c = what.charAt(++i);
				sp[filled++] = c;
			}
			_addr822Phrase = new String(sp, 0, filled);
		}
		return ret;
	}

	/*-------------------------------------------------------------------------------------*/
	private int parse822Comments(final String what, int endPos) {
		do
			if ((endPos = parseOne822Comment(what, endPos)) < 0)
				return -1;
		while (what.charAt(endPos) == ')');
		return endPos;
	}

	private int parseOne822Comment(final String what, int endPos) {
		//
		// Remark: Notes has here some deficiencies, too: Consider the following (correct) mail address:
		// "Phrase with <>" <JohnSmith@Company.en> (Should work\\)
		// Then Notes fails to interpret it and gives Addr821 = empty. - On the other hand, the
		// invalid mail address "Phrase with <>" <JohnSmith@Company.en> ((Works?) too?))
		// is interpreted by Notes.

		if ((countBackslashesBefore(what, endPos) & 1) != 0)
			return -1;
		for (endPos--; endPos >= 0; endPos--)
			if (!Character.isWhitespace(what.charAt(endPos)))
				break;
		if (endPos < 0)
			return -1;
		int brackCount = 1;
		int startPos;
		for (startPos = endPos; startPos >= 0; startPos--) {
			char c = what.charAt(startPos);
			if (c != ')' && c != '(')
				continue;
			if ((countBackslashesBefore(what, startPos) & 1) != 0)
				continue;
			if (c == ')')
				brackCount++;
			else if (--brackCount == 0)
				break;
		}
		if (startPos < 0)
			return -1;
		char[] sp = new char[endPos - startPos + 1];
		int filled = 0;
		for (int i = startPos + 1; i <= endPos; i++) {
			char c = what.charAt(i);
			if (filled == 0 && Character.isWhitespace(c))
				continue;
			if (c == '\\')
				c = what.charAt(++i);
			sp[filled++] = c;
		}
		if (filled > 0) {
			_addr822Comments[2] = _addr822Comments[1];
			_addr822Comments[1] = _addr822Comments[0];
			_addr822Comments[0] = new String(sp, 0, filled);
		}
		for (startPos--; startPos >= 0; startPos--)
			if (!Character.isWhitespace(what.charAt(startPos)))
				break;
		return startPos;
	}

	private int countBackslashesBefore(final String what, final int where) {
		int ret = 0;
		for (int i = where - 1; i >= 0; i--, ret++)
			if (what.charAt(i) != '\\')
				break;
		return ret;
	}

	/*-------------------------------------------------------------------------------------*/
	private boolean checkMailAddress(final String mailAddr) {
		//
		// At the moment, we restrict ourselves to "normal" mail addresses. That is, exotic (but valid)
		// mail addresses like "very.unusual.@.unusual.com"@example.com aren't supported.
		//
		int atPos = mailAddr.indexOf('@');
		if (atPos <= 0)
			return false;
		String preAt = mailAddr.substring(0, atPos);
		String postAt = mailAddr.substring(atPos + 1);
		if (postAt.isEmpty() || postAt.charAt(0) == '.')
			return false;
		int lh = preAt.length();
		for (int i = 0; i < lh; i++) {
			char c = preAt.charAt(i);
			if (c <= ' ' || c > 0x7f || c == '<' || c == '>' || c == '[' || c == ']')
				return false;
		}
		lh = postAt.length();
		for (int i = 0; i < lh; i++) {
			char c = postAt.charAt(i);
			if (c <= ' ' || c > 0x7f)
				return false;
			if ((!Character.isLetterOrDigit(c)) && (c != '-') && (c != '.'))
				return false;
			if (c == '.' && (i == lh - 1 || postAt.charAt(i + 1) == '.'))
				return false;
		}
		_addr822LocalPart = preAt;
		return true;
	}

}
