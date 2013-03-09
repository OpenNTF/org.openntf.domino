package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public class Name extends Base<org.openntf.domino.Name, lotus.domino.Name> implements org.openntf.domino.Name {

	private static final long serialVersionUID = 1L;

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

	public Name(lotus.domino.Name delegate, org.openntf.domino.Base parent) {
		super(null, parent);
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

	@Override
	public org.openntf.domino.Session getParent() {
		return (org.openntf.domino.Session) super.getParent();
	}

	public String getSurname() {
		return surname;
	}

	public boolean isHierarchical() {
		return hierarchical;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abbreviated == null) ? 0 : abbreviated.hashCode());
		result = prime * result + ((addr821 == null) ? 0 : addr821.hashCode());
		result = prime * result + ((addr822comment1 == null) ? 0 : addr822comment1.hashCode());
		result = prime * result + ((addr822comment2 == null) ? 0 : addr822comment2.hashCode());
		result = prime * result + ((addr822comment3 == null) ? 0 : addr822comment3.hashCode());
		result = prime * result + ((addr822localpart == null) ? 0 : addr822localpart.hashCode());
		result = prime * result + ((addr822phrase == null) ? 0 : addr822phrase.hashCode());
		result = prime * result + ((admd == null) ? 0 : admd.hashCode());
		result = prime * result + ((canonical == null) ? 0 : canonical.hashCode());
		result = prime * result + ((common == null) ? 0 : common.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((generation == null) ? 0 : generation.hashCode());
		result = prime * result + ((given == null) ? 0 : given.hashCode());
		result = prime * result + (hierarchical ? 1231 : 1237);
		result = prime * result + ((initials == null) ? 0 : initials.hashCode());
		result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((organization == null) ? 0 : organization.hashCode());
		result = prime * result + ((orgunit1 == null) ? 0 : orgunit1.hashCode());
		result = prime * result + ((orgunit2 == null) ? 0 : orgunit2.hashCode());
		result = prime * result + ((orgunit3 == null) ? 0 : orgunit3.hashCode());
		result = prime * result + ((orgunit4 == null) ? 0 : orgunit4.hashCode());
		result = prime * result + ((prmd == null) ? 0 : prmd.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Name other = (Name) obj;
		if (abbreviated == null) {
			if (other.abbreviated != null)
				return false;
		} else if (!abbreviated.equals(other.abbreviated))
			return false;
		if (addr821 == null) {
			if (other.addr821 != null)
				return false;
		} else if (!addr821.equals(other.addr821))
			return false;
		if (addr822comment1 == null) {
			if (other.addr822comment1 != null)
				return false;
		} else if (!addr822comment1.equals(other.addr822comment1))
			return false;
		if (addr822comment2 == null) {
			if (other.addr822comment2 != null)
				return false;
		} else if (!addr822comment2.equals(other.addr822comment2))
			return false;
		if (addr822comment3 == null) {
			if (other.addr822comment3 != null)
				return false;
		} else if (!addr822comment3.equals(other.addr822comment3))
			return false;
		if (addr822localpart == null) {
			if (other.addr822localpart != null)
				return false;
		} else if (!addr822localpart.equals(other.addr822localpart))
			return false;
		if (addr822phrase == null) {
			if (other.addr822phrase != null)
				return false;
		} else if (!addr822phrase.equals(other.addr822phrase))
			return false;
		if (admd == null) {
			if (other.admd != null)
				return false;
		} else if (!admd.equals(other.admd))
			return false;
		if (canonical == null) {
			if (other.canonical != null)
				return false;
		} else if (!canonical.equals(other.canonical))
			return false;
		if (common == null) {
			if (other.common != null)
				return false;
		} else if (!common.equals(other.common))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (generation == null) {
			if (other.generation != null)
				return false;
		} else if (!generation.equals(other.generation))
			return false;
		if (given == null) {
			if (other.given != null)
				return false;
		} else if (!given.equals(other.given))
			return false;
		if (hierarchical != other.hierarchical)
			return false;
		if (initials == null) {
			if (other.initials != null)
				return false;
		} else if (!initials.equals(other.initials))
			return false;
		if (keyword == null) {
			if (other.keyword != null)
				return false;
		} else if (!keyword.equals(other.keyword))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		if (orgunit1 == null) {
			if (other.orgunit1 != null)
				return false;
		} else if (!orgunit1.equals(other.orgunit1))
			return false;
		if (orgunit2 == null) {
			if (other.orgunit2 != null)
				return false;
		} else if (!orgunit2.equals(other.orgunit2))
			return false;
		if (orgunit3 == null) {
			if (other.orgunit3 != null)
				return false;
		} else if (!orgunit3.equals(other.orgunit3))
			return false;
		if (orgunit4 == null) {
			if (other.orgunit4 != null)
				return false;
		} else if (!orgunit4.equals(other.orgunit4))
			return false;
		if (prmd == null) {
			if (other.prmd != null)
				return false;
		} else if (!prmd.equals(other.prmd))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Name [abbreviated=" + abbreviated + ", addr821=" + addr821 + ", addr822comment1=" + addr822comment1 + ", addr822comment2="
				+ addr822comment2 + ", addr822comment3=" + addr822comment3 + ", addr822localpart=" + addr822localpart + ", addr822phrase="
				+ addr822phrase + ", admd=" + admd + ", canonical=" + canonical + ", common=" + common + ", country=" + country
				+ ", generation=" + generation + ", given=" + given + ", initials=" + initials + ", keyword=" + keyword + ", language="
				+ language + ", organization=" + organization + ", orgunit1=" + orgunit1 + ", orgunit2=" + orgunit2 + ", orgunit3="
				+ orgunit3 + ", orgunit4=" + orgunit4 + ", prmd=" + prmd + ", surname=" + surname + ", hierarchical=" + hierarchical + "]";
	}

}
