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

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface AdministrationProcess.
 */
public interface AdministrationProcess extends Base<lotus.domino.AdministrationProcess>, lotus.domino.AdministrationProcess,
		org.openntf.domino.ext.AdministrationProcess, SessionDescendant {

	public static class Schema extends FactorySchema<AdministrationProcess, lotus.domino.AdministrationProcess, Session> {
		@Override
		public Class<AdministrationProcess> typeClass() {
			return AdministrationProcess.class;
		}

		@Override
		public Class<lotus.domino.AdministrationProcess> delegateClass() {
			return lotus.domino.AdministrationProcess.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#addGroupMembers(java.lang.String, java.util.Vector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String addGroupMembers(final String group, final Vector members);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#addInternetCertificateToUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String addInternetCertificateToUser(final String user, final String keyringFile, final String keyringPassword);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#addInternetCertificateToUser(java.lang.String, java.lang.String, java.lang.String,
	 * lotus.domino.DateTime)
	 */
	@Override
	public String addInternetCertificateToUser(final String user, final String keyringFile, final String keyringPassword,
			final lotus.domino.DateTime expiration);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#addServerToCluster(java.lang.String, java.lang.String)
	 */
	@Override
	public String addServerToCluster(final String server, final String cluster);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#approveDeletePersonInDirectory(java.lang.String)
	 */
	@Override
	public String approveDeletePersonInDirectory(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#approveDeleteServerInDirectory(java.lang.String)
	 */
	@Override
	public String approveDeleteServerInDirectory(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#approveDesignElementDeletion(java.lang.String)
	 */
	@Override
	public String approveDesignElementDeletion(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#approveHostedOrgStorageDeletion(java.lang.String)
	 */
	@Override
	public String approveHostedOrgStorageDeletion(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#approveMailFileDeletion(java.lang.String)
	 */
	@Override
	public String approveMailFileDeletion(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#approveMovedReplicaDeletion(java.lang.String)
	 */
	@Override
	public String approveMovedReplicaDeletion(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#approveNameChangeRetraction(java.lang.String)
	 */
	@Override
	public String approveNameChangeRetraction(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#approveRenamePersonInDirectory(java.lang.String)
	 */
	@Override
	public String approveRenamePersonInDirectory(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#approveRenameServerInDirectory(java.lang.String)
	 */
	@Override
	public String approveRenameServerInDirectory(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#approveReplicaDeletion(java.lang.String)
	 */
	@Override
	public String approveReplicaDeletion(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#approveResourceDeletion(java.lang.String)
	 */
	@Override
	public String approveResourceDeletion(final String noteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#changeHTTPPassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String changeHTTPPassword(final String userName, final String oldPassword, final String newPassword);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#configureMailAgent(java.lang.String, java.lang.String)
	 */
	@Override
	public String configureMailAgent(final String userName, final String agentName);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#configureMailAgent(java.lang.String, java.lang.String, boolean, boolean)
	 */
	@Override
	public String configureMailAgent(final String userName, final String agentName, final boolean activatable, final boolean enable);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#createReplica(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String createReplica(final String sourceServer, final String sourceDBFile, final String destServer);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#createReplica(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * boolean, boolean)
	 */
	@Override
	public String createReplica(final String sourceServer, final String sourceDBFile, final String destServer, final String destDBFile,
			final boolean copyACL, final boolean createFTIndex);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#deleteGroup(java.lang.String, boolean)
	 */
	@Override
	public String deleteGroup(final String groupName, final boolean immediate);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#deleteGroup(java.lang.String, boolean, boolean)
	 */
	@Override
	public String deleteGroup(final String groupName, final boolean immediate, final boolean deleteWindowsGroup);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#deleteReplicas(java.lang.String, java.lang.String)
	 */
	@Override
	public String deleteReplicas(final String serverName, final String fileName);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#deleteServer(java.lang.String, boolean)
	 */
	@Override
	public String deleteServer(final String serverName, final boolean immediate);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#deleteUser(java.lang.String, boolean, int, java.lang.String)
	 */
	@Override
	public String deleteUser(final String userName, final boolean immediate, final int mailFileAction, final String denyGroup);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#deleteUser(java.lang.String, boolean, int, java.lang.String, boolean)
	 */
	@Override
	public String deleteUser(final String userName, final boolean immediate, final int mailFileAction, final String denyGroup,
			final boolean deleteWindowsUser);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#findGroupInDomain(java.lang.String)
	 */
	@Override
	public String findGroupInDomain(final String group);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#findServerInDomain(java.lang.String)
	 */
	@Override
	public String findServerInDomain(final String server);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#findUserInDomain(java.lang.String)
	 */
	@Override
	public String findUserInDomain(final String userName);

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
	public String moveMailUser(final String userName, final String newHomeServer, final String newHomeServerMailPath);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#moveMailUser(java.lang.String, java.lang.String, java.lang.String, boolean, java.util.Vector,
	 * boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String moveMailUser(final String userName, final String newHomeServer, final String newHomeServerMailPath, final boolean useSCOS,
			final Vector newClusterReplicas, final boolean deleteOldClusterReplicas);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#moveReplica(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String moveReplica(final String sourceServer, final String sourceDBFile, final String destServer);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#moveReplica(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean,
	 * boolean)
	 */
	@Override
	public String moveReplica(final String sourceServer, final String sourceDBFile, final String destServer, final String destDBFile,
			final boolean copyACL, final boolean createFTIndex);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#moveRoamingUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String moveRoamingUser(final String userName, final String destServer, final String destServerPath);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#moveUserInHierarchyComplete(java.lang.String)
	 */
	@Override
	public String moveUserInHierarchyComplete(final String requestNoteid);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#moveUserInHierarchyComplete(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String moveUserInHierarchyComplete(final String requestNoteid, final String lastName, final String firstName,
			final String middleInitial, final String orgUnit, final String altCommonName, final String altOrgUnit, final String altLanguage,
			final boolean renameWindowsUser);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#moveUserInHierarchyRequest(java.lang.String, java.lang.String)
	 */
	@Override
	public String moveUserInHierarchyRequest(final String userName, final String targetCertifier);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#moveUserInHierarchyRequest(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String moveUserInHierarchyRequest(final String userName, final String targetCertifier, final boolean allowPrimaryNameChange);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#recertifyServer(java.lang.String)
	 */
	@Override
	public String recertifyServer(final String server);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#recertifyUser(java.lang.String)
	 */
	@Override
	public String recertifyUser(final String userName);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#removeServerFromCluster(java.lang.String)
	 */
	@Override
	public String removeServerFromCluster(final String server);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#renameGroup(java.lang.String, java.lang.String)
	 */
	@Override
	public String renameGroup(final String group, final String newGroup);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#renameNotesUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String renameNotesUser(final String userName, final String lastName, final String firstName, final String middleInitial,
			final String orgUnit);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#renameNotesUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String renameNotesUser(final String userName, final String lastName, final String firstName, final String middleInitial,
			final String orgUnit, final String altCommonName, final String altOrgUnit, final String altLanguage,
			final boolean renameWindowsUser);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#renameWebUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String renameWebUser(final String userName, final String newFullName, final String newLastName, final String newFirstName,
			final String newMiddleInitial, final String newShortName, final String newInternetAddress);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#setCertificateAuthorityOrg(java.lang.String)
	 */
	@Override
	public void setCertificateAuthorityOrg(final String org);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#setCertificateExpiration(lotus.domino.DateTime)
	 */
	@Override
	public void setCertificateExpiration(final lotus.domino.DateTime expiration);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#setCertifierFile(java.lang.String)
	 */
	@Override
	public void setCertifierFile(final String fileSpec);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#setCertifierPassword(java.lang.String)
	 */
	@Override
	public void setCertifierPassword(final String password);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#setServerDirectoryAssistanceSettings(java.lang.String, java.lang.String)
	 */
	@Override
	public String setServerDirectoryAssistanceSettings(final String server, final String dbFile);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#setUseCertificateAuthority(boolean)
	 */
	@Override
	public void setUseCertificateAuthority(final boolean flag);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#setUserPasswordSettings(java.lang.String, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer, java.lang.Boolean)
	 */
	@Override
	public String setUserPasswordSettings(final String userName, final Integer notesPasswordCheckSetting,
			final Integer notesPasswordChangeInterval, final Integer notesPasswordGracePeriod, final Boolean internetPasswordForceChange);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#signDatabaseWithServerID(java.lang.String, java.lang.String)
	 */
	@Override
	public String signDatabaseWithServerID(final String server, final String dbFile);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#signDatabaseWithServerID(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String signDatabaseWithServerID(final String server, final String dbFile, final boolean updateOnly);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#upgradeUserToHierarchical(java.lang.String)
	 */
	@Override
	public String upgradeUserToHierarchical(final String userName);

	/*
	 * (non-Javadoc)
	 *
	 * @see lotus.domino.AdministrationProcess#upgradeUserToHierarchical(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String upgradeUserToHierarchical(final String userName, final String orgUnit, final String altCommonName,
			final String altOrgUnit, final String altLanguage);

	@Override
	public String deleteUser(String arg0, boolean arg1, int arg2, String arg3, boolean arg4, int arg5);

}
