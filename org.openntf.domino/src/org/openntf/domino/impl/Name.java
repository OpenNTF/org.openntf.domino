package org.openntf.domino.impl;

import lotus.domino.NotesException;
import lotus.domino.Session;

import org.openntf.domino.utils.DominoUtils;

public class Name extends Base<org.openntf.domino.Name, lotus.domino.Name> implements org.openntf.domino.Name {

	public Name() {
		// TODO Auto-generated constructor stub
	}

	public Name(lotus.domino.Name delegate) {
		super(delegate);
		// TODO Auto-generated constructor stub
	}

	public String getADMD() {
		try {
			return getDelegate().getADMD();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getAbbreviated() {
		try {
			return getDelegate().getAbbreviated();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getAddr821() {
		try {
			return getDelegate().getAddr821();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getAddr822Comment1() {
		try {
			return getDelegate().getAddr822Comment1();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getAddr822Comment2() {
		try {
			return getDelegate().getAddr822Comment2();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getAddr822Comment3() {
		try {
			return getDelegate().getAddr822Comment3();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getAddr822LocalPart() {
		try {
			return getDelegate().getAddr822LocalPart();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getAddr822Phrase() {
		try {
			return getDelegate().getAddr822Phrase();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getCanonical() {
		try {
			return getDelegate().getCanonical();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getCommon() {
		try {
			return getDelegate().getCommon();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getCountry() {
		try {
			return getDelegate().getCountry();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getGeneration() {
		try {
			return getDelegate().getGeneration();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getGiven() {
		try {
			return getDelegate().getGiven();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getInitials() {
		try {
			return getDelegate().getInitials();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getKeyword() {
		try {
			return getDelegate().getKeyword();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getLanguage() {
		try {
			return getDelegate().getLanguage();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getOrgUnit1() {
		try {
			return getDelegate().getOrgUnit1();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getOrgUnit2() {
		try {
			return getDelegate().getOrgUnit2();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getOrgUnit3() {
		try {
			return getDelegate().getOrgUnit3();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getOrgUnit4() {
		try {
			return getDelegate().getOrgUnit4();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getOrganization() {
		try {
			return getDelegate().getOrganization();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getPRMD() {
		try {
			return getDelegate().getPRMD();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Session getParent() {
		try {
			return getDelegate().getParent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getSurname() {
		try {
			return getDelegate().getSurname();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public boolean isHierarchical() {
		try {
			return getDelegate().isHierarchical();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

}
