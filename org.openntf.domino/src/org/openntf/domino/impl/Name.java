package org.openntf.domino.impl;

import lotus.domino.NotesException;
import lotus.domino.Session;

import org.openntf.domino.utils.DominoUtils;

public class Name extends Base<org.openntf.domino.Name, lotus.domino.Name> implements org.openntf.domino.Name {
	private static final String DEFAULT_STR = "";

	private String abbreviated = DEFAULT_STR;
	private String addr821 = DEFAULT_STR;
	private String addr822comment1 = DEFAULT_STR;
	private String addr822comment2 = DEFAULT_STR;
	private String addr822comment3 = DEFAULT_STR;
	private String addr822localpart = DEFAULT_STR;
	private String addr822phrase = DEFAULT_STR;
	private String admd = DEFAULT_STR;
	private String canonical = DEFAULT_STR;
	private String common = DEFAULT_STR;
	private String country = DEFAULT_STR;
	private String generation = DEFAULT_STR;
	private String given = DEFAULT_STR;
	private String initials = DEFAULT_STR;
	private String keyword = DEFAULT_STR;
	private String language = DEFAULT_STR;
	private String organization = DEFAULT_STR;
	private String orgunit1 = DEFAULT_STR;
	private String orgunit2 = DEFAULT_STR;
	private String orgunit3 = DEFAULT_STR;
	private String orgunit4 = DEFAULT_STR;
	private String prmd = DEFAULT_STR;
	private String surname = DEFAULT_STR;
	private boolean hierarchical = false;

	public Name(lotus.domino.Name delegate) {
		super(null);
		initialize(delegate);
		Base.recycle(delegate);
	}

	private void initialize(lotus.domino.Name delegate) {
		try {
			abbreviated = delegate.getAbbreviated();
			addr821 = delegate.getAddr821();
			addr822comment1 = delegate.getAddr822Comment1();
			addr822comment2 = delegate.getAddr822Comment2();
			addr822comment3 = delegate.getAddr822Comment3();
			addr822localpart = delegate.getAddr822LocalPart();
			addr822phrase = delegate.getAddr822Phrase();
			admd = delegate.getADMD();
			canonical = delegate.getCanonical();
			common = delegate.getCommon();
			country = delegate.getCountry();
			generation = delegate.getGeneration();
			given = delegate.getGiven();
			initials = delegate.getInitials();
			keyword = delegate.getKeyword();
			language = delegate.getLanguage();
			organization = delegate.getOrganization();
			orgunit1 = delegate.getOrgUnit1();
			orgunit2 = delegate.getOrgUnit2();
			orgunit3 = delegate.getOrgUnit3();
			orgunit4 = delegate.getOrgUnit4();
			prmd = delegate.getPRMD();
			surname = delegate.getSurname();
			hierarchical = delegate.isHierarchical();
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
		}
	}

	public String getADMD() {
		return admd;
	}

	public String getAbbreviated() {
		return abbreviated;
	}

	public String getAddr821() {
		return this.addr821;
	}

	public String getAddr822Comment1() {
		return this.addr822comment1;
	}

	public String getAddr822Comment2() {
		return this.addr822comment2;
	}

	public String getAddr822Comment3() {
		return this.addr822comment3;
	}

	public String getAddr822LocalPart() {
		return this.addr822localpart;
	}

	public String getAddr822Phrase() {
		return this.addr822phrase;
	}

	public String getCanonical() {
		return canonical;
	}

	public String getCommon() {
		return common;
	}

	public String getCountry() {
		return country;
	}

	public String getGeneration() {
		return generation;
	}

	public String getGiven() {
		return given;
	}

	public String getInitials() {
		return initials;
	}

	public String getKeyword() {
		return keyword;
	}

	public String getLanguage() {
		return language;
	}

	public String getOrgUnit1() {
		return orgunit1;
	}

	public String getOrgUnit2() {
		return orgunit2;
	}

	public String getOrgUnit3() {
		return orgunit3;
	}

	public String getOrgUnit4() {
		return orgunit4;
	}

	public String getOrganization() {
		return organization;
	}

	public String getPRMD() {
		return prmd;
	}

	public Session getParent() {
		// FIXME NTF - the fact that this exists is silly. But we should address it somehow.
		return null;
	}

	public String getSurname() {
		return surname;
	}

	public boolean isHierarchical() {
		return hierarchical;
	}

}
