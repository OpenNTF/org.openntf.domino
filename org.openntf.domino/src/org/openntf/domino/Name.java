package org.openntf.domino;

import org.openntf.domino.types.Encapsulated;

public interface Name extends Base<lotus.domino.Name>, lotus.domino.Name, Encapsulated {

	@Override
	public String getAbbreviated();

	@Override
	public String getAddr821();

	@Override
	public String getAddr822Comment1();

	@Override
	public String getAddr822Comment2();

	@Override
	public String getAddr822Comment3();

	@Override
	public String getAddr822LocalPart();

	@Override
	public String getAddr822Phrase();

	@Override
	public String getADMD();

	@Override
	public String getCanonical();

	@Override
	public String getCommon();

	@Override
	public String getCountry();

	@Override
	public String getGeneration();

	@Override
	public String getGiven();

	@Override
	public String getInitials();

	@Override
	public String getKeyword();

	@Override
	public String getLanguage();

	@Override
	public String getOrganization();

	@Override
	public String getOrgUnit1();

	@Override
	public String getOrgUnit2();

	@Override
	public String getOrgUnit3();

	@Override
	public String getOrgUnit4();

	@Override
	public org.openntf.domino.Session getParent();

	@Override
	public String getPRMD();

	@Override
	public String getSurname();

	@Override
	public boolean isHierarchical();

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

	public void recycle();
}
