/*
 * Copyright OpenNTF 2013
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
package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class Registration.
 */
public class Registration extends Base<org.openntf.domino.Registration, lotus.domino.Registration> implements
		org.openntf.domino.Registration {

	/**
	 * Instantiates a new registration.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public Registration(lotus.domino.Registration delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#addCertifierToAddressBook(java.lang.String)
	 */
	@Override
	public boolean addCertifierToAddressBook(String idFile) {
		try {
			return getDelegate().addCertifierToAddressBook(idFile);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#addCertifierToAddressBook(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addCertifierToAddressBook(String idFile, String password) {
		try {
			return getDelegate().addCertifierToAddressBook(idFile, password);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#addCertifierToAddressBook(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean addCertifierToAddressBook(String idFile, String password, String location, String comment) {
		try {
			return getDelegate().addCertifierToAddressBook(idFile, password, location, comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#addServerToAddressBook(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addServerToAddressBook(String idFile, String server, String domain) {
		try {
			return getDelegate().addServerToAddressBook(idFile, server, domain);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#addServerToAddressBook(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addServerToAddressBook(String idFile, String server, String domain, String userPassword) {
		try {
			return getDelegate().addServerToAddressBook(idFile, server, domain, userPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#addServerToAddressBook(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addServerToAddressBook(String idFile, String server, String domain, String userPassword, String network,
			String adminName, String title, String location, String comment) {
		try {
			return getDelegate().addServerToAddressBook(idFile, server, domain, userPassword, network, adminName, title, location, comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#addUserProfile(java.lang.String, java.lang.String)
	 */
	@Override
	public void addUserProfile(String userName, String profile) {
		try {
			getDelegate().addUserProfile(userName, profile);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#addUserToAddressBook(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addUserToAddressBook(String idFile, String fullName, String lastName) {
		try {
			return getDelegate().addUserToAddressBook(idFile, fullName, lastName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#addUserToAddressBook(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addUserToAddressBook(String idFile, String fullName, String lastName, String userPassword) {
		try {
			return getDelegate().addUserToAddressBook(idFile, fullName, lastName, userPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#addUserToAddressBook(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addUserToAddressBook(String idFile, String fullName, String lastName, String userPassword, String firstName,
			String middleName, String mailServer, String mailFilePath, String forwardingAddress, String location, String comment) {
		try {
			return getDelegate().addUserToAddressBook(idFile, fullName, lastName, userPassword, firstName, middleName, mailServer,
					mailFilePath, forwardingAddress, location, comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#crossCertify(java.lang.String)
	 */
	@Override
	public boolean crossCertify(String idFile) {
		try {
			return getDelegate().crossCertify(idFile);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#crossCertify(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean crossCertify(String idFile, String certPassword) {
		try {
			return getDelegate().crossCertify(idFile, certPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#crossCertify(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean crossCertify(String idFile, String certPassword, String comment) {
		try {
			return getDelegate().crossCertify(idFile, certPassword, comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#deleteIDOnServer(java.lang.String, boolean)
	 */
	@Override
	public void deleteIDOnServer(String userName, boolean isServerID) {
		try {
			getDelegate().deleteIDOnServer(userName, isServerID);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getAltOrgUnit()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getAltOrgUnit() {
		try {
			return getDelegate().getAltOrgUnit();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getAltOrgUnitLang()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getAltOrgUnitLang() {
		try {
			return getDelegate().getAltOrgUnitLang();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getCertifierIDFile()
	 */
	@Override
	public String getCertifierIDFile() {
		try {
			return getDelegate().getCertifierIDFile();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getCertifierName()
	 */
	@Override
	public String getCertifierName() {
		try {
			return getDelegate().getCertifierName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getCreateMailDb()
	 */
	@Override
	public boolean getCreateMailDb() {
		try {
			return getDelegate().getCreateMailDb();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getExpiration()
	 */
	@Override
	public DateTime getExpiration() {
		try {
			return Factory.fromLotus(getDelegate().getExpiration(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getForeignDN()
	 */
	@Override
	public String getForeignDN() {
		try {
			return getDelegate().getForeignDN();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getGroupList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getGroupList() {
		try {
			return getDelegate().getGroupList();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getIDFromServer(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void getIDFromServer(String userName, String filePath, boolean isServerID) {
		try {
			getDelegate().getIDFromServer(userName, filePath, isServerID);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getIDType()
	 */
	@Override
	public int getIDType() {
		try {
			return getDelegate().getIDType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getMailACLManager()
	 */
	@Override
	public String getMailACLManager() {
		try {
			return getDelegate().getMailACLManager();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getMailInternetAddress()
	 */
	@Override
	public String getMailInternetAddress() {
		try {
			return getDelegate().getMailInternetAddress();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getMailOwnerAccess()
	 */
	@Override
	public int getMailOwnerAccess() {
		try {
			return getDelegate().getMailOwnerAccess();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getMailQuotaSizeLimit()
	 */
	@Override
	public int getMailQuotaSizeLimit() {
		try {
			return getDelegate().getMailQuotaSizeLimit();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getMailQuotaWarningThreshold()
	 */
	@Override
	public int getMailQuotaWarningThreshold() {
		try {
			return getDelegate().getMailQuotaWarningThreshold();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getMailReplicaServers()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getMailReplicaServers() {
		try {
			return getDelegate().getMailReplicaServers();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getMailSystem()
	 */
	@Override
	public int getMailSystem() {
		try {
			return getDelegate().getMailSystem();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getMailTemplateName()
	 */
	@Override
	public String getMailTemplateName() {
		try {
			return getDelegate().getMailTemplateName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getMinPasswordLength()
	 */
	@Override
	public int getMinPasswordLength() {
		try {
			return getDelegate().getMinPasswordLength();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getOrgUnit()
	 */
	@Override
	public String getOrgUnit() {
		try {
			return getDelegate().getOrgUnit();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public Session getParent() {
		return (Session) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getPolicyName()
	 */
	@Override
	public String getPolicyName() {
		try {
			return getDelegate().getPolicyName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getPublicKeySize()
	 */
	@Override
	public int getPublicKeySize() {
		try {
			return getDelegate().getPublicKeySize();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getRegistrationLog()
	 */
	@Override
	public String getRegistrationLog() {
		try {
			return getDelegate().getRegistrationLog();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getRegistrationServer()
	 */
	@Override
	public String getRegistrationServer() {
		try {
			return getDelegate().getRegistrationServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getRoamingCleanupPeriod()
	 */
	@Override
	public int getRoamingCleanupPeriod() {
		try {
			return getDelegate().getRoamingCleanupPeriod();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getRoamingCleanupSetting()
	 */
	@Override
	public int getRoamingCleanupSetting() {
		try {
			return getDelegate().getRoamingCleanupSetting();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getRoamingServer()
	 */
	@Override
	public String getRoamingServer() {
		try {
			return getDelegate().getRoamingServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getRoamingSubdir()
	 */
	@Override
	public String getRoamingSubdir() {
		try {
			return getDelegate().getRoamingSubdir();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getShortName()
	 */
	@Override
	public String getShortName() {
		try {
			return getDelegate().getShortName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getStoreIDInAddressBook()
	 */
	@Override
	public boolean getStoreIDInAddressBook() {
		try {
			return getDelegate().getStoreIDInAddressBook();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getUpdateAddressBook()
	 */
	@Override
	public boolean getUpdateAddressBook() {
		try {
			return getDelegate().getUpdateAddressBook();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#getUserInfo(java.lang.String, java.lang.StringBuffer, java.lang.StringBuffer,
	 * java.lang.StringBuffer, java.lang.StringBuffer, java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void getUserInfo(String userName, StringBuffer mailServer, StringBuffer mailFile, StringBuffer mailDomain,
			StringBuffer mailSystem, Vector profile) {
		try {
			getDelegate().getUserInfo(userName, mailServer, mailFile, mailDomain, mailSystem, toDominoFriendly(profile, this));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#isEnforceUniqueShortName()
	 */
	@Override
	public boolean isEnforceUniqueShortName() {
		try {
			return getDelegate().isEnforceUniqueShortName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#isMailCreateFTIndex()
	 */
	@Override
	public boolean isMailCreateFTIndex() {
		try {
			return getDelegate().isMailCreateFTIndex();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#isNoIDFile()
	 */
	@Override
	public boolean isNoIDFile() {
		try {
			return getDelegate().isNoIDFile();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#isNorthAmerican()
	 */
	@Override
	public boolean isNorthAmerican() {
		try {
			return getDelegate().isNorthAmerican();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#isRoamingUser()
	 */
	@Override
	public boolean isRoamingUser() {
		try {
			return getDelegate().isRoamingUser();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#isStoreIDInMailfile()
	 */
	@Override
	public boolean isStoreIDInMailfile() {
		try {
			return getDelegate().isStoreIDInMailfile();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#isSynchInternetPassword()
	 */
	@Override
	public boolean isSynchInternetPassword() {
		try {
			return getDelegate().isSynchInternetPassword();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#isUseCertificateAuthority()
	 */
	@Override
	public boolean isUseCertificateAuthority() {
		try {
			return getDelegate().isUseCertificateAuthority();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#recertify(java.lang.String)
	 */
	@Override
	public boolean recertify(String idFile) {
		try {
			return getDelegate().recertify(idFile);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#recertify(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recertify(String idFile, String certPassword) {
		try {
			return getDelegate().recertify(idFile, certPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#recertify(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean recertify(String idFile, String certPassword, String comment) {
		try {
			return getDelegate().recertify(idFile, certPassword, comment);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#registerNewCertifier(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewCertifier(String org, String idFile, String certPassword) {
		try {
			return getDelegate().registerNewCertifier(org, idFile, certPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#registerNewCertifier(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewCertifier(String org, String idFile, String certPassword, String country) {
		try {
			return getDelegate().registerNewCertifier(org, idFile, certPassword, country);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#registerNewServer(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewServer(String server, String idFile, String domain, String password) {
		try {
			return getDelegate().registerNewServer(server, idFile, domain, password);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#registerNewServer(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean registerNewServer(String server, String idFile, String domain, String serverPassword, String certPassword) {
		try {
			return getDelegate().registerNewServer(server, idFile, domain, serverPassword, certPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#registerNewServer(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewServer(String server, String idFile, String domain, String serverPassword, String certPassword,
			String location, String comment, String network, String adminName, String title) {
		try {
			return getDelegate().registerNewServer(server, idFile, domain, serverPassword, certPassword, location, comment, network,
					adminName, title);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#registerNewUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewUser(String lastName, String idFile, String server) {
		try {
			return getDelegate().registerNewUser(lastName, idFile, server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#registerNewUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewUser(String lastName, String idFile, String server, String firstName, String middleName, String certPassword) {
		try {
			return getDelegate().registerNewUser(lastName, idFile, server, firstName, middleName, certPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#registerNewUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewUser(String lastName, String idFile, String server, String firstName, String middleName, String certPassword,
			String location, String comment, String mailDBPath, String forward, String userPassword) {
		try {
			return getDelegate().registerNewUser(lastName, idFile, server, firstName, middleName, certPassword, location, comment,
					mailDBPath, forward, userPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#registerNewUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean registerNewUser(String lastName, String idFile, String server, String firstName, String middleName, String certPassword,
			String location, String comment, String mailDBPath, String forward, String userPassword, String altName, String altNameLang) {
		try {
			return getDelegate().registerNewUser(lastName, idFile, server, firstName, middleName, certPassword, location, comment,
					mailDBPath, forward, userPassword, altName, altNameLang);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setAltOrgUnit(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setAltOrgUnit(Vector names) {
		try {
			getDelegate().setAltOrgUnit(names);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setAltOrgUnitLang(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setAltOrgUnitLang(Vector languages) {
		try {
			getDelegate().setAltOrgUnitLang(toDominoFriendly(languages, this));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setCertifierIDFile(java.lang.String)
	 */
	@Override
	public void setCertifierIDFile(String idFile) {
		try {
			getDelegate().setCertifierIDFile(idFile);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setCertifierName(java.lang.String)
	 */
	@Override
	public void setCertifierName(String name) {
		try {
			getDelegate().setCertifierName(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setCreateMailDb(boolean)
	 */
	@Override
	public void setCreateMailDb(boolean flag) {
		try {
			getDelegate().setCreateMailDb(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setEnforceUniqueShortName(boolean)
	 */
	@Override
	public void setEnforceUniqueShortName(boolean flag) {
		try {
			getDelegate().setEnforceUniqueShortName(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setExpiration(lotus.domino.DateTime)
	 */
	@Override
	public void setExpiration(lotus.domino.DateTime expiration) {
		try {
			getDelegate().setExpiration((lotus.domino.DateTime) toLotus(expiration));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setForeignDN(java.lang.String)
	 */
	@Override
	public void setForeignDN(String dn) {
		try {
			getDelegate().setForeignDN(dn);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setGroupList(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setGroupList(Vector groups) {
		try {
			getDelegate().setGroupList(toDominoFriendly(groups, this));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setIDType(int)
	 */
	@Override
	public void setIDType(int type) {
		try {
			getDelegate().setIDType(type);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setMailACLManager(java.lang.String)
	 */
	@Override
	public void setMailACLManager(String name) {
		try {
			getDelegate().setMailACLManager(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setMailCreateFTIndex(boolean)
	 */
	@Override
	public void setMailCreateFTIndex(boolean flag) {
		try {
			getDelegate().setMailCreateFTIndex(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setMailInternetAddress(java.lang.String)
	 */
	@Override
	public void setMailInternetAddress(String address) {
		try {
			getDelegate().setMailInternetAddress(address);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setMailOwnerAccess(int)
	 */
	@Override
	public void setMailOwnerAccess(int access) {
		try {
			getDelegate().setMailOwnerAccess(access);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setMailQuotaSizeLimit(int)
	 */
	@Override
	public void setMailQuotaSizeLimit(int limit) {
		try {
			getDelegate().setMailQuotaSizeLimit(limit);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setMailQuotaWarningThreshold(int)
	 */
	@Override
	public void setMailQuotaWarningThreshold(int threshold) {
		try {
			getDelegate().setMailQuotaWarningThreshold(threshold);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setMailReplicaServers(java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setMailReplicaServers(Vector servers) {
		try {
			getDelegate().setMailReplicaServers(toDominoFriendly(servers, this));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setMailSystem(int)
	 */
	@Override
	public void setMailSystem(int system) {
		try {
			getDelegate().setMailSystem(system);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setMailTemplateName(java.lang.String)
	 */
	@Override
	public void setMailTemplateName(String name) {
		try {
			getDelegate().setMailTemplateName(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setMinPasswordLength(int)
	 */
	@Override
	public void setMinPasswordLength(int length) {
		try {
			getDelegate().setMinPasswordLength(length);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setNoIDFile(boolean)
	 */
	@Override
	public void setNoIDFile(boolean flag) {
		try {
			getDelegate().setNoIDFile(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setNorthAmerican(boolean)
	 */
	@Override
	public void setNorthAmerican(boolean flag) {
		try {
			getDelegate().setNorthAmerican(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setOrgUnit(java.lang.String)
	 */
	@Override
	public void setOrgUnit(String unit) {
		try {
			getDelegate().setOrgUnit(unit);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setPolicyName(java.lang.String)
	 */
	@Override
	public void setPolicyName(String name) {
		try {
			getDelegate().setPolicyName(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setPublicKeySize(int)
	 */
	@Override
	public void setPublicKeySize(int size) {
		try {
			getDelegate().setPublicKeySize(size);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setRegistrationLog(java.lang.String)
	 */
	@Override
	public void setRegistrationLog(String name) {
		try {
			getDelegate().setRegistrationLog(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setRegistrationServer(java.lang.String)
	 */
	@Override
	public void setRegistrationServer(String server) {
		try {
			getDelegate().setRegistrationServer(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setRoamingCleanupPeriod(int)
	 */
	@Override
	public void setRoamingCleanupPeriod(int period) {
		try {
			getDelegate().setRoamingCleanupPeriod(period);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setRoamingCleanupSetting(int)
	 */
	@Override
	public void setRoamingCleanupSetting(int setting) {
		try {
			getDelegate().setRoamingCleanupSetting(setting);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setRoamingServer(java.lang.String)
	 */
	@Override
	public void setRoamingServer(String server) {
		try {
			getDelegate().setRoamingServer(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setRoamingSubdir(java.lang.String)
	 */
	@Override
	public void setRoamingSubdir(String dirPath) {
		try {
			getDelegate().setRoamingSubdir(dirPath);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setRoamingUser(boolean)
	 */
	@Override
	public void setRoamingUser(boolean flag) {
		try {
			getDelegate().setRoamingUser(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setShortName(java.lang.String)
	 */
	@Override
	public void setShortName(String shortName) {
		try {
			getDelegate().setShortName(shortName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setStoreIDInAddressBook(boolean)
	 */
	@Override
	public void setStoreIDInAddressBook(boolean flag) {
		try {
			getDelegate().setStoreIDInAddressBook(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setStoreIDInMailfile(boolean)
	 */
	@Override
	public void setStoreIDInMailfile(boolean flag) {
		try {
			getDelegate().setStoreIDInMailfile(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setSynchInternetPassword(boolean)
	 */
	@Override
	public void setSynchInternetPassword(boolean flag) {
		try {
			getDelegate().setSynchInternetPassword(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setUpdateAddressBook(boolean)
	 */
	@Override
	public void setUpdateAddressBook(boolean flag) {
		try {
			getDelegate().setUpdateAddressBook(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#setUseCertificateAuthority(boolean)
	 */
	@Override
	public void setUseCertificateAuthority(boolean flag) {
		try {
			getDelegate().setUseCertificateAuthority(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Registration#switchToID(java.lang.String, java.lang.String)
	 */
	@Override
	public String switchToID(String idFile, String userPassword) {
		try {
			return getDelegate().switchToID(idFile, userPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}
}
