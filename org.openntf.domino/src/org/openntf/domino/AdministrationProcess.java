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
 * The Interface AdministrationProcess.
 */
public interface AdministrationProcess extends Base<lotus.domino.AdministrationProcess>, lotus.domino.AdministrationProcess,
		org.openntf.domino.ext.AdministrationProcess, SessionDescendant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#addGroupMembers(java.lang.String, java.util.Vector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String addGroupMembers(String group, Vector members);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#addInternetCertificateToUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String addInternetCertificateToUser(String user, String keyringFile, String keyringPassword);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#addInternetCertificateToUser(java.lang.String, java.lang.String, java.lang.String,
	 * lotus.domino.DateTime)
	 */
	@Override
	public String addInternetCertificateToUser(String user, String keyringFile, String keyringPassword, lotus.domino.DateTime expiration);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#addServerToCluster(java.lang.String, java.lang.String)
	 */
	@Override
	public String addServerToCluster(String server, String cluster);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#approveDeletePersonInDirectory(java.lang.String)
	 */
	@Override
	public String approveDeletePersonInDirectory(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#approveDeleteServerInDirectory(java.lang.String)
	 */
	@Override
	public String approveDeleteServerInDirectory(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#approveDesignElementDeletion(java.lang.String)
	 */
	@Override
	public String approveDesignElementDeletion(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#approveHostedOrgStorageDeletion(java.lang.String)
	 */
	@Override
	public String approveHostedOrgStorageDeletion(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#approveMailFileDeletion(java.lang.String)
	 */
	@Override
	public String approveMailFileDeletion(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#approveMovedReplicaDeletion(java.lang.String)
	 */
	@Override
	public String approveMovedReplicaDeletion(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#approveNameChangeRetraction(java.lang.String)
	 */
	@Override
	public String approveNameChangeRetraction(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#approveRenamePersonInDirectory(java.lang.String)
	 */
	@Override
	public String approveRenamePersonInDirectory(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#approveRenameServerInDirectory(java.lang.String)
	 */
	@Override
	public String approveRenameServerInDirectory(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#approveReplicaDeletion(java.lang.String)
	 */
	@Override
	public String approveReplicaDeletion(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#approveResourceDeletion(java.lang.String)
	 */
	@Override
	public String approveResourceDeletion(String noteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#changeHTTPPassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String changeHTTPPassword(String userName, String oldPassword, String newPassword);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#configureMailAgent(java.lang.String, java.lang.String)
	 */
	@Override
	public String configureMailAgent(String userName, String agentName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#configureMailAgent(java.lang.String, java.lang.String, boolean, boolean)
	 */
	@Override
	public String configureMailAgent(String userName, String agentName, boolean activatable, boolean enable);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#createReplica(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String createReplica(String sourceServer, String sourceDBFile, String destServer);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#createReplica(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * boolean, boolean)
	 */
	@Override
	public String createReplica(String sourceServer, String sourceDBFile, String destServer, String destDBFile, boolean copyACL,
			boolean createFTIndex);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#deleteGroup(java.lang.String, boolean)
	 */
	@Override
	public String deleteGroup(String groupName, boolean immediate);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#deleteGroup(java.lang.String, boolean, boolean)
	 */
	@Override
	public String deleteGroup(String groupName, boolean immediate, boolean deleteWindowsGroup);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#deleteReplicas(java.lang.String, java.lang.String)
	 */
	@Override
	public String deleteReplicas(String serverName, String fileName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#deleteServer(java.lang.String, boolean)
	 */
	@Override
	public String deleteServer(String serverName, boolean immediate);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#deleteUser(java.lang.String, boolean, int, java.lang.String)
	 */
	@Override
	public String deleteUser(String userName, boolean immediate, int mailFileAction, String denyGroup);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#deleteUser(java.lang.String, boolean, int, java.lang.String, boolean)
	 */
	@Override
	public String deleteUser(String userName, boolean immediate, int mailFileAction, String denyGroup, boolean deleteWindowsUser);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#findGroupInDomain(java.lang.String)
	 */
	@Override
	public String findGroupInDomain(String group);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#findServerInDomain(java.lang.String)
	 */
	@Override
	public String findServerInDomain(String server);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#findUserInDomain(java.lang.String)
	 */
	@Override
	public String findUserInDomain(String userName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#getCertificateAuthorityOrg()
	 */
	@Override
	public String getCertificateAuthorityOrg();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#getCertificateExpiration()
	 */
	@Override
	public DateTime getCertificateExpiration();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#getCertifierFile()
	 */
	@Override
	public String getCertifierFile();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#getCertifierPassword()
	 */
	@Override
	public String getCertifierPassword();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#getParent()
	 */
	@Override
	public Session getParent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#isCertificateAuthorityAvailable()
	 */
	@Override
	public boolean isCertificateAuthorityAvailable();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#isUseCertificateAuthority()
	 */
	@Override
	public boolean isUseCertificateAuthority();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#moveMailUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String moveMailUser(String userName, String newHomeServer, String newHomeServerMailPath);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#moveMailUser(java.lang.String, java.lang.String, java.lang.String, boolean, java.util.Vector,
	 * boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String moveMailUser(String userName, String newHomeServer, String newHomeServerMailPath, boolean useSCOS,
			Vector newClusterReplicas, boolean deleteOldClusterReplicas);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#moveReplica(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String moveReplica(String sourceServer, String sourceDBFile, String destServer);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#moveReplica(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean,
	 * boolean)
	 */
	@Override
	public String moveReplica(String sourceServer, String sourceDBFile, String destServer, String destDBFile, boolean copyACL,
			boolean createFTIndex);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#moveRoamingUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String moveRoamingUser(String userName, String destServer, String destServerPath);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#moveUserInHierarchyComplete(java.lang.String)
	 */
	@Override
	public String moveUserInHierarchyComplete(String requestNoteid);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#moveUserInHierarchyComplete(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String moveUserInHierarchyComplete(String requestNoteid, String lastName, String firstName, String middleInitial,
			String orgUnit, String altCommonName, String altOrgUnit, String altLanguage, boolean renameWindowsUser);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#moveUserInHierarchyRequest(java.lang.String, java.lang.String)
	 */
	@Override
	public String moveUserInHierarchyRequest(String userName, String targetCertifier);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#moveUserInHierarchyRequest(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String moveUserInHierarchyRequest(String userName, String targetCertifier, boolean allowPrimaryNameChange);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#recertifyServer(java.lang.String)
	 */
	@Override
	public String recertifyServer(String server);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#recertifyUser(java.lang.String)
	 */
	@Override
	public String recertifyUser(String userName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#removeServerFromCluster(java.lang.String)
	 */
	@Override
	public String removeServerFromCluster(String server);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#renameGroup(java.lang.String, java.lang.String)
	 */
	@Override
	public String renameGroup(String group, String newGroup);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#renameNotesUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String renameNotesUser(String userName, String lastName, String firstName, String middleInitial, String orgUnit);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#renameNotesUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String renameNotesUser(String userName, String lastName, String firstName, String middleInitial, String orgUnit,
			String altCommonName, String altOrgUnit, String altLanguage, boolean renameWindowsUser);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#renameWebUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String renameWebUser(String userName, String newFullName, String newLastName, String newFirstName, String newMiddleInitial,
			String newShortName, String newInternetAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#setCertificateAuthorityOrg(java.lang.String)
	 */
	@Override
	public void setCertificateAuthorityOrg(String org);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#setCertificateExpiration(lotus.domino.DateTime)
	 */
	@Override
	public void setCertificateExpiration(lotus.domino.DateTime expiration);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#setCertifierFile(java.lang.String)
	 */
	@Override
	public void setCertifierFile(String fileSpec);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#setCertifierPassword(java.lang.String)
	 */
	@Override
	public void setCertifierPassword(String password);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#setServerDirectoryAssistanceSettings(java.lang.String, java.lang.String)
	 */
	@Override
	public String setServerDirectoryAssistanceSettings(String server, String dbFile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#setUseCertificateAuthority(boolean)
	 */
	@Override
	public void setUseCertificateAuthority(boolean flag);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#setUserPasswordSettings(java.lang.String, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer, java.lang.Boolean)
	 */
	@Override
	public String setUserPasswordSettings(String userName, Integer notesPasswordCheckSetting, Integer notesPasswordChangeInterval,
			Integer notesPasswordGracePeriod, Boolean internetPasswordForceChange);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#signDatabaseWithServerID(java.lang.String, java.lang.String)
	 */
	@Override
	public String signDatabaseWithServerID(String server, String dbFile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#signDatabaseWithServerID(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String signDatabaseWithServerID(String server, String dbFile, boolean updateOnly);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#upgradeUserToHierarchical(java.lang.String)
	 */
	@Override
	public String upgradeUserToHierarchical(String userName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AdministrationProcess#upgradeUserToHierarchical(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String upgradeUserToHierarchical(String userName, String orgUnit, String altCommonName, String altOrgUnit, String altLanguage);

}
