package org.openntf.domino;

import java.util.Vector;

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
	public Vector<String> getAltOrgUnit();

	@Override
	public Vector<String> getAltOrgUnitLang();

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
	public Vector<String> getGroupList();

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
	public Vector<String> getMailReplicaServers();

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

	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Override
	public void setAltOrgUnit(Vector names);

	@SuppressWarnings("unchecked")
	@Override
	public void setAltOrgUnitLang(Vector languages);

	@Override
	public void setCertifierIDFile(String idFile);

	@Override
	public void setCertifierName(String name);

	@Override
	public void setCreateMailDb(boolean flag);

	@Override
	public void setEnforceUniqueShortName(boolean flag);

	@Override
	public void setExpiration(lotus.domino.DateTime expiration);

	@Override
	public void setForeignDN(String dn);

	@SuppressWarnings("unchecked")
	@Override
	public void setGroupList(Vector groups);

	@Override
	public void setIDType(int type);

	@Override
	public void setMailACLManager(String name);

	@Override
	public void setMailCreateFTIndex(boolean flag);

	@Override
	public void setMailInternetAddress(String address);

	@Override
	public void setMailOwnerAccess(int access);

	@Override
	public void setMailQuotaSizeLimit(int limit);

	@Override
	public void setMailQuotaWarningThreshold(int threshold);

	@SuppressWarnings("unchecked")
	@Override
	public void setMailReplicaServers(Vector servers);

	@Override
	public void setMailSystem(int system);

	@Override
	public void setMailTemplateName(String name);

	@Override
	public void setMinPasswordLength(int length);

	@Override
	public void setNoIDFile(boolean flag);

	@Override
	public void setNorthAmerican(boolean flag);

	@Override
	public void setOrgUnit(String unit);

	@Override
	public void setPolicyName(String name);

	@Override
	public void setPublicKeySize(int size);

	@Override
	public void setRegistrationLog(String name);

	@Override
	public void setRegistrationServer(String server);

	@Override
	public void setRoamingCleanupPeriod(int period);

	@Override
	public void setRoamingCleanupSetting(int setting);

	@Override
	public void setRoamingServer(String server);

	@Override
	public void setRoamingSubdir(String dirPath);

	@Override
	public void setRoamingUser(boolean flag);

	@Override
	public void setShortName(String shortName);

	@Override
	public void setStoreIDInAddressBook(boolean flag);

	@Override
	public void setStoreIDInMailfile(boolean flag);

	@Override
	public void setSynchInternetPassword(boolean flag);

	@Override
	public void setUpdateAddressBook(boolean flag);

	@Override
	public void setUseCertificateAuthority(boolean flag);

	@Override
	public String switchToID(String idFile, String userPassword);
}
