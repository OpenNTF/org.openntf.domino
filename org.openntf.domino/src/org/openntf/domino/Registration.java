package org.openntf.domino;

import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.NotesException;
import lotus.domino.Session;

public interface Registration extends Base<lotus.domino.Registration>, lotus.domino.Registration {

	@Override
	public boolean addCertifierToAddressBook(String arg0);

	public boolean addCertifierToAddressBook(String arg0, String arg1);

	@Override
	public boolean addCertifierToAddressBook(String arg0, String arg1, String arg2, String arg3);

	@Override
	public boolean addServerToAddressBook(String arg0, String arg1, String arg2);

	@Override
	public boolean addServerToAddressBook(String arg0, String arg1, String arg2, String arg3);

	@Override
	public boolean addServerToAddressBook(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6,
			String arg7, String arg8);

	@Override
	public void addUserProfile(String arg0, String arg1);

	@Override
	public boolean addUserToAddressBook(String arg0, String arg1, String arg2);

	@Override
	public boolean addUserToAddressBook(String arg0, String arg1, String arg2, String arg3);

	@Override
	public boolean addUserToAddressBook(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6,
			String arg7, String arg8, String arg9, String arg10);

	@Override
	public boolean crossCertify(String arg0);

	@Override
	public boolean crossCertify(String arg0, String arg1);

	@Override
	public boolean crossCertify(String arg0, String arg1, String arg2);

	@Override
	public void deleteIDOnServer(String arg0, boolean arg1);

	@Override
	public Vector getAltOrgUnit();

	@Override
	public Vector getAltOrgUnitLang();

	@Override
	public String getCertifierIDFile();

	@Override
	public String getCertifierName();

	@Override
	public boolean getCreateMailDb();

	@Override
	public lotus.domino.Registration getDelegate();

	@Override
	public DateTime getExpiration();

	@Override
	public String getForeignDN();

	@Override
	public Vector getGroupList();

	@Override
	public void getIDFromServer(String arg0, String arg1, boolean arg2);

	@Override
	public int getIDType();

	@Override
	public String getMailACLManager();

	@Override
	public String getMailInternetAddress();

	@Override
	public int getMailOwnerAccess();

	@Override
	public int getMailQuotaSizeLimit();

	@Override
	public int getMailQuotaWarningThreshold();

	@Override
	public Vector getMailReplicaServers();

	@Override
	public int getMailSystem();

	@Override
	public String getMailTemplateName();

	@Override
	public int getMinPasswordLength();

	@Override
	public String getOrgUnit();

	@Override
	public Session getParent();

	@Override
	public String getPolicyName();

	@Override
	public int getPublicKeySize();

	@Override
	public String getRegistrationLog();

	@Override
	public String getRegistrationServer();

	@Override
	public int getRoamingCleanupPeriod();

	@Override
	public int getRoamingCleanupSetting();

	@Override
	public String getRoamingServer();

	@Override
	public String getRoamingSubdir();

	@Override
	public String getShortName();

	@Override
	public boolean getStoreIDInAddressBook();

	@Override
	public boolean getUpdateAddressBook();

	@Override
	public void getUserInfo(String arg0, StringBuffer arg1, StringBuffer arg2, StringBuffer arg3, StringBuffer arg4, Vector arg5)
			throws NotesException;

	@Override
	public boolean isEnforceUniqueShortName();

	@Override
	public boolean isMailCreateFTIndex();

	@Override
	public boolean isNoIDFile();

	@Override
	public boolean isNorthAmerican();

	@Override
	public boolean isRoamingUser();

	@Override
	public boolean isStoreIDInMailfile();

	@Override
	public boolean isSynchInternetPassword();

	@Override
	public boolean isUseCertificateAuthority();

	@Override
	public boolean recertify(String arg0);

	@Override
	public boolean recertify(String arg0, String arg1);

	@Override
	public boolean recertify(String arg0, String arg1, String arg2);

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public boolean registerNewCertifier(String arg0, String arg1, String arg2);

	@Override
	public boolean registerNewCertifier(String arg0, String arg1, String arg2, String arg3);

	@Override
	public boolean registerNewServer(String arg0, String arg1, String arg2, String arg3);

	@Override
	public boolean registerNewServer(String arg0, String arg1, String arg2, String arg3, String arg4);

	@Override
	public boolean registerNewServer(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6,
			String arg7, String arg8, String arg9);

	@Override
	public boolean registerNewUser(String arg0, String arg1, String arg2);

	@Override
	public boolean registerNewUser(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5);

	@Override
	public boolean registerNewUser(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7,
			String arg8, String arg9, String arg10);

	@Override
	public boolean registerNewUser(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7,
			String arg8, String arg9, String arg10, String arg11, String arg12);

	@Override
	public void setAltOrgUnit(Vector arg0) throws NotesException;

	@Override
	public void setAltOrgUnitLang(Vector arg0) throws NotesException;

	@Override
	public void setCertifierIDFile(String arg0) throws NotesException;

	@Override
	public void setCertifierName(String arg0) throws NotesException;

	@Override
	public void setCreateMailDb(boolean arg0) throws NotesException;

	@Override
	public void setEnforceUniqueShortName(boolean arg0) throws NotesException;

	@Override
	public void setExpiration(DateTime arg0) throws NotesException;

	@Override
	public void setForeignDN(String arg0) throws NotesException;

	@Override
	public void setGroupList(Vector arg0) throws NotesException;

	@Override
	public void setIDType(int arg0) throws NotesException;

	@Override
	public void setMailACLManager(String arg0) throws NotesException;

	@Override
	public void setMailCreateFTIndex(boolean arg0) throws NotesException;

	@Override
	public void setMailInternetAddress(String arg0) throws NotesException;

	@Override
	public void setMailOwnerAccess(int arg0) throws NotesException;

	@Override
	public void setMailQuotaSizeLimit(int arg0) throws NotesException;

	@Override
	public void setMailQuotaWarningThreshold(int arg0) throws NotesException;

	@Override
	public void setMailReplicaServers(Vector arg0) throws NotesException;

	@Override
	public void setMailSystem(int arg0) throws NotesException;

	@Override
	public void setMailTemplateName(String arg0) throws NotesException;

	@Override
	public void setMinPasswordLength(int arg0) throws NotesException;

	@Override
	public void setNoIDFile(boolean arg0) throws NotesException;

	@Override
	public void setNorthAmerican(boolean arg0) throws NotesException;

	@Override
	public void setOrgUnit(String arg0) throws NotesException;

	@Override
	public void setPolicyName(String arg0) throws NotesException;

	@Override
	public void setPublicKeySize(int arg0) throws NotesException;

	@Override
	public void setRegistrationLog(String arg0) throws NotesException;

	@Override
	public void setRegistrationServer(String arg0) throws NotesException;

	@Override
	public void setRoamingCleanupPeriod(int arg0) throws NotesException;

	@Override
	public void setRoamingCleanupSetting(int arg0) throws NotesException;

	@Override
	public void setRoamingServer(String arg0) throws NotesException;

	@Override
	public void setRoamingSubdir(String arg0) throws NotesException;

	@Override
	public void setRoamingUser(boolean arg0) throws NotesException;

	@Override
	public void setShortName(String arg0) throws NotesException;

	@Override
	public void setStoreIDInAddressBook(boolean arg0) throws NotesException;

	@Override
	public void setStoreIDInMailfile(boolean arg0) throws NotesException;

	@Override
	public void setSynchInternetPassword(boolean arg0) throws NotesException;

	@Override
	public void setUpdateAddressBook(boolean arg0) throws NotesException;

	@Override
	public void setUseCertificateAuthority(boolean arg0) throws NotesException;

	@Override
	public String switchToID(String arg0, String arg1) throws NotesException;

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
