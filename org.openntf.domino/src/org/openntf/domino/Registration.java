package org.openntf.domino;

import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.NotesException;
import lotus.domino.Session;

public interface Registration extends Base<lotus.domino.Registration>, lotus.domino.Registration {

	@Override
	public boolean addCertifierToAddressBook(String idFile);

	public boolean addCertifierToAddressBook(String idFile, String password);

	@Override
	public boolean addCertifierToAddressBook(String idFile, String password, String location, String comment);

	@Override
	public boolean addServerToAddressBook(String idFile, String server, String domain);

	@Override
	public boolean addServerToAddressBook(String idFile, String server, String domain, String userPassword);

	@Override
	public boolean addServerToAddressBook(String idFile, String server, String domain, String userPassword, String network,
			String adminName, String title, String location, String comment);

	@Override
	public void addUserProfile(String userName, String profile);

	@Override
	public boolean addUserToAddressBook(String idFile, String fullName, String lastName);

	@Override
	public boolean addUserToAddressBook(String idFile, String fullName, String lastName, String userPassword);

	@Override
	public boolean addUserToAddressBook(String idFile, String fullName, String lastName, String userPassword, String firstName,
			String middleName, String mailServer, String mailFilePath, String forwardingAddress, String location, String comment);

	@Override
	public boolean crossCertify(String idFile);

	@Override
	public boolean crossCertify(String idFile, String certPassword);

	@Override
	public boolean crossCertify(String idFile, String certPassword, String comment);

	@Override
	public void deleteIDOnServer(String userName, boolean isServerID);

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
	public DateTime getExpiration();

	@Override
	public String getForeignDN();

	@Override
	public Vector getGroupList();

	@Override
	public void getIDFromServer(String userName, String filePath, boolean isServerID);

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
	public void getUserInfo(String userName, StringBuffer mailServer, StringBuffer mailFile, StringBuffer mailDomain,
			StringBuffer mailSystem, Vector profile);

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
	public boolean recertify(String idFile);

	@Override
	public boolean recertify(String idFile, String certPassword);

	@Override
	public boolean recertify(String idFile, String certPassword, String comment);

	@Override
	public boolean registerNewCertifier(String org, String idFile, String certPassword);

	@Override
	public boolean registerNewCertifier(String org, String idFile, String certPassword, String country);

	@Override
	public boolean registerNewServer(String server, String idFile, String domain, String password);

	@Override
	public boolean registerNewServer(String server, String idFile, String domain, String serverPassword, String certPassword);

	@Override
	public boolean registerNewServer(String server, String idFile, String domain, String serverPassword, String certPassword,
			String location, String comment, String network, String adminName, String title);

	@Override
	public boolean registerNewUser(String lastName, String idFile, String server);

	@Override
	public boolean registerNewUser(String lastName, String idFile, String server, String firstName, String middleName, String certPassword);

	@Override
	public boolean registerNewUser(String lastName, String idFile, String server, String firstName, String middleName, String certPassword,
			String location, String comment, String mailDBPath, String forward, String userPassword);

	@Override
	public boolean registerNewUser(String lastName, String idFile, String server, String firstName, String middleName, String certPassword,
			String location, String comment, String mailDBPath, String forward, String userPassword, String altName, String altNameLang);

	@Override
	public void setAltOrgUnit(Vector names) throws NotesException;

	@Override
	public void setAltOrgUnitLang(Vector languages) throws NotesException;

	@Override
	public void setCertifierIDFile(String idFile) throws NotesException;

	@Override
	public void setCertifierName(String name) throws NotesException;

	@Override
	public void setCreateMailDb(boolean flag) throws NotesException;

	@Override
	public void setEnforceUniqueShortName(boolean flag) throws NotesException;

	@Override
	public void setExpiration(DateTime expiration) throws NotesException;

	@Override
	public void setForeignDN(String dn) throws NotesException;

	@Override
	public void setGroupList(Vector groups) throws NotesException;

	@Override
	public void setIDType(int type) throws NotesException;

	@Override
	public void setMailACLManager(String name) throws NotesException;

	@Override
	public void setMailCreateFTIndex(boolean flag) throws NotesException;

	@Override
	public void setMailInternetAddress(String address) throws NotesException;

	@Override
	public void setMailOwnerAccess(int access) throws NotesException;

	@Override
	public void setMailQuotaSizeLimit(int limit) throws NotesException;

	@Override
	public void setMailQuotaWarningThreshold(int threshold) throws NotesException;

	@Override
	public void setMailReplicaServers(Vector servers) throws NotesException;

	@Override
	public void setMailSystem(int system) throws NotesException;

	@Override
	public void setMailTemplateName(String name) throws NotesException;

	@Override
	public void setMinPasswordLength(int length) throws NotesException;

	@Override
	public void setNoIDFile(boolean flag) throws NotesException;

	@Override
	public void setNorthAmerican(boolean flag) throws NotesException;

	@Override
	public void setOrgUnit(String unit) throws NotesException;

	@Override
	public void setPolicyName(String name) throws NotesException;

	@Override
	public void setPublicKeySize(int size) throws NotesException;

	@Override
	public void setRegistrationLog(String name) throws NotesException;

	@Override
	public void setRegistrationServer(String server) throws NotesException;

	@Override
	public void setRoamingCleanupPeriod(int period) throws NotesException;

	@Override
	public void setRoamingCleanupSetting(int setting) throws NotesException;

	@Override
	public void setRoamingServer(String server) throws NotesException;

	@Override
	public void setRoamingSubdir(String dirPath) throws NotesException;

	@Override
	public void setRoamingUser(boolean flag) throws NotesException;

	@Override
	public void setShortName(String shortName) throws NotesException;

	@Override
	public void setStoreIDInAddressBook(boolean flag) throws NotesException;

	@Override
	public void setStoreIDInMailfile(boolean flag) throws NotesException;

	@Override
	public void setSynchInternetPassword(boolean flag) throws NotesException;

	@Override
	public void setUpdateAddressBook(boolean flag) throws NotesException;

	@Override
	public void setUseCertificateAuthority(boolean flag) throws NotesException;

	@Override
	public String switchToID(String idFile, String userPassword) throws NotesException;
}
