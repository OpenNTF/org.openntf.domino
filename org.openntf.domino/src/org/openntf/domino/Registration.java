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
package org.openntf.domino;

import java.util.Vector;

import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface Registration.
 */
public interface Registration extends Base<lotus.domino.Registration>, lotus.domino.Registration, org.openntf.domino.ext.Registration,
		SessionDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#addCertifierToAddressBook(java.lang.String)
	 */
	@Override
	public boolean addCertifierToAddressBook(String idFile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#addCertifierToAddressBook(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addCertifierToAddressBook(String idFile, String password);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#addCertifierToAddressBook(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addCertifierToAddressBook(String idFile, String password, String location, String comment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#addServerToAddressBook(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addServerToAddressBook(String idFile, String server, String domain);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#addServerToAddressBook(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addServerToAddressBook(String idFile, String server, String domain, String userPassword);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#addServerToAddressBook(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addServerToAddressBook(String idFile, String server, String domain, String userPassword, String network,
			String adminName, String title, String location, String comment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#addUserProfile(java.lang.String, java.lang.String)
	 */
	@Override
	public void addUserProfile(String userName, String profile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#addUserToAddressBook(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addUserToAddressBook(String idFile, String fullName, String lastName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#addUserToAddressBook(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addUserToAddressBook(String idFile, String fullName, String lastName, String userPassword);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#addUserToAddressBook(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addUserToAddressBook(String idFile, String fullName, String lastName, String userPassword, String firstName,
			String middleName, String mailServer, String mailFilePath, String forwardingAddress, String location, String comment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#crossCertify(java.lang.String)
	 */
	@Override
	public boolean crossCertify(String idFile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#crossCertify(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean crossCertify(String idFile, String certPassword);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#crossCertify(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean crossCertify(String idFile, String certPassword, String comment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#deleteIDOnServer(java.lang.String, boolean)
	 */
	@Override
	public void deleteIDOnServer(String userName, boolean isServerID);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getAltOrgUnit()
	 */
	@Override
	public Vector<String> getAltOrgUnit();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getAltOrgUnitLang()
	 */
	@Override
	public Vector<String> getAltOrgUnitLang();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getCertifierIDFile()
	 */
	@Override
	public String getCertifierIDFile();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getCertifierName()
	 */
	@Override
	public String getCertifierName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getCreateMailDb()
	 */
	@Override
	public boolean getCreateMailDb();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getExpiration()
	 */
	@Override
	public DateTime getExpiration();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getForeignDN()
	 */
	@Override
	public String getForeignDN();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getGroupList()
	 */
	@Override
	public Vector<String> getGroupList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getIDFromServer(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void getIDFromServer(String userName, String filePath, boolean isServerID);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getIDType()
	 */
	@Override
	public int getIDType();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getMailACLManager()
	 */
	@Override
	public String getMailACLManager();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getMailInternetAddress()
	 */
	@Override
	public String getMailInternetAddress();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getMailOwnerAccess()
	 */
	@Override
	public int getMailOwnerAccess();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getMailQuotaSizeLimit()
	 */
	@Override
	public int getMailQuotaSizeLimit();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getMailQuotaWarningThreshold()
	 */
	@Override
	public int getMailQuotaWarningThreshold();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getMailReplicaServers()
	 */
	@Override
	public Vector<String> getMailReplicaServers();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getMailSystem()
	 */
	@Override
	public int getMailSystem();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getMailTemplateName()
	 */
	@Override
	public String getMailTemplateName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getMinPasswordLength()
	 */
	@Override
	public int getMinPasswordLength();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getOrgUnit()
	 */
	@Override
	public String getOrgUnit();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getParent()
	 */
	@Override
	public Session getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getPolicyName()
	 */
	@Override
	public String getPolicyName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getPublicKeySize()
	 */
	@Override
	public int getPublicKeySize();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getRegistrationLog()
	 */
	@Override
	public String getRegistrationLog();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getRegistrationServer()
	 */
	@Override
	public String getRegistrationServer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getRoamingCleanupPeriod()
	 */
	@Override
	public int getRoamingCleanupPeriod();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getRoamingCleanupSetting()
	 */
	@Override
	public int getRoamingCleanupSetting();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getRoamingServer()
	 */
	@Override
	public String getRoamingServer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getRoamingSubdir()
	 */
	@Override
	public String getRoamingSubdir();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getShortName()
	 */
	@Override
	public String getShortName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getStoreIDInAddressBook()
	 */
	@Override
	public boolean getStoreIDInAddressBook();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getUpdateAddressBook()
	 */
	@Override
	public boolean getUpdateAddressBook();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#getUserInfo(java.lang.String, java.lang.StringBuffer, java.lang.StringBuffer, java.lang.StringBuffer,
	 * java.lang.StringBuffer, java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void getUserInfo(String userName, StringBuffer mailServer, StringBuffer mailFile, StringBuffer mailDomain,
			StringBuffer mailSystem, Vector profile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#isEnforceUniqueShortName()
	 */
	@Override
	public boolean isEnforceUniqueShortName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#isMailCreateFTIndex()
	 */
	@Override
	public boolean isMailCreateFTIndex();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#isNoIDFile()
	 */
	@Override
	public boolean isNoIDFile();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#isNorthAmerican()
	 */
	@Override
	public boolean isNorthAmerican();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#isRoamingUser()
	 */
	@Override
	public boolean isRoamingUser();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#isStoreIDInMailfile()
	 */
	@Override
	public boolean isStoreIDInMailfile();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#isSynchInternetPassword()
	 */
	@Override
	public boolean isSynchInternetPassword();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#isUseCertificateAuthority()
	 */
	@Override
	public boolean isUseCertificateAuthority();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#recertify(java.lang.String)
	 */
	@Override
	public boolean recertify(String idFile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#recertify(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recertify(String idFile, String certPassword);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#recertify(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recertify(String idFile, String certPassword, String comment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#registerNewCertifier(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewCertifier(String org, String idFile, String certPassword);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#registerNewCertifier(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewCertifier(String org, String idFile, String certPassword, String country);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#registerNewServer(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewServer(String server, String idFile, String domain, String password);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#registerNewServer(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean registerNewServer(String server, String idFile, String domain, String serverPassword, String certPassword);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#registerNewServer(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewServer(String server, String idFile, String domain, String serverPassword, String certPassword,
			String location, String comment, String network, String adminName, String title);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#registerNewUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewUser(String lastName, String idFile, String server);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#registerNewUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewUser(String lastName, String idFile, String server, String firstName, String middleName, String certPassword);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#registerNewUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewUser(String lastName, String idFile, String server, String firstName, String middleName, String certPassword,
			String location, String comment, String mailDBPath, String forward, String userPassword);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#registerNewUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewUser(String lastName, String idFile, String server, String firstName, String middleName, String certPassword,
			String location, String comment, String mailDBPath, String forward, String userPassword, String altName, String altNameLang);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setAltOrgUnit(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setAltOrgUnit(Vector names);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setAltOrgUnitLang(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setAltOrgUnitLang(Vector languages);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setCertifierIDFile(java.lang.String)
	 */
	@Override
	public void setCertifierIDFile(String idFile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setCertifierName(java.lang.String)
	 */
	@Override
	public void setCertifierName(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setCreateMailDb(boolean)
	 */
	@Override
	public void setCreateMailDb(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setEnforceUniqueShortName(boolean)
	 */
	@Override
	public void setEnforceUniqueShortName(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setExpiration(lotus.domino.DateTime)
	 */
	@Override
	public void setExpiration(lotus.domino.DateTime expiration);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setForeignDN(java.lang.String)
	 */
	@Override
	public void setForeignDN(String dn);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setGroupList(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setGroupList(Vector groups);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setIDType(int)
	 */
	@Override
	public void setIDType(int type);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setMailACLManager(java.lang.String)
	 */
	@Override
	public void setMailACLManager(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setMailCreateFTIndex(boolean)
	 */
	@Override
	public void setMailCreateFTIndex(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setMailInternetAddress(java.lang.String)
	 */
	@Override
	public void setMailInternetAddress(String address);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setMailOwnerAccess(int)
	 */
	@Override
	public void setMailOwnerAccess(int access);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setMailQuotaSizeLimit(int)
	 */
	@Override
	public void setMailQuotaSizeLimit(int limit);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setMailQuotaWarningThreshold(int)
	 */
	@Override
	public void setMailQuotaWarningThreshold(int threshold);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setMailReplicaServers(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setMailReplicaServers(Vector servers);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setMailSystem(int)
	 */
	@Override
	public void setMailSystem(int system);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setMailTemplateName(java.lang.String)
	 */
	@Override
	public void setMailTemplateName(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setMinPasswordLength(int)
	 */
	@Override
	public void setMinPasswordLength(int length);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setNoIDFile(boolean)
	 */
	@Override
	public void setNoIDFile(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setNorthAmerican(boolean)
	 */
	@Override
	public void setNorthAmerican(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setOrgUnit(java.lang.String)
	 */
	@Override
	public void setOrgUnit(String unit);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setPolicyName(java.lang.String)
	 */
	@Override
	public void setPolicyName(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setPublicKeySize(int)
	 */
	@Override
	public void setPublicKeySize(int size);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setRegistrationLog(java.lang.String)
	 */
	@Override
	public void setRegistrationLog(String name);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setRegistrationServer(java.lang.String)
	 */
	@Override
	public void setRegistrationServer(String server);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setRoamingCleanupPeriod(int)
	 */
	@Override
	public void setRoamingCleanupPeriod(int period);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setRoamingCleanupSetting(int)
	 */
	@Override
	public void setRoamingCleanupSetting(int setting);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setRoamingServer(java.lang.String)
	 */
	@Override
	public void setRoamingServer(String server);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setRoamingSubdir(java.lang.String)
	 */
	@Override
	public void setRoamingSubdir(String dirPath);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setRoamingUser(boolean)
	 */
	@Override
	public void setRoamingUser(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setShortName(java.lang.String)
	 */
	@Override
	public void setShortName(String shortName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setStoreIDInAddressBook(boolean)
	 */
	@Override
	public void setStoreIDInAddressBook(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setStoreIDInMailfile(boolean)
	 */
	@Override
	public void setStoreIDInMailfile(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setSynchInternetPassword(boolean)
	 */
	@Override
	public void setSynchInternetPassword(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setUpdateAddressBook(boolean)
	 */
	@Override
	public void setUpdateAddressBook(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#setUseCertificateAuthority(boolean)
	 */
	@Override
	public void setUseCertificateAuthority(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.Registration#switchToID(java.lang.String, java.lang.String)
	 */
	@Override
	public String switchToID(String idFile, String userPassword);
}
