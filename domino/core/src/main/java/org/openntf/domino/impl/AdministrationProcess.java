/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.DateTime;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

/**
 * The Class AdministrationProcess.
 */
public class AdministrationProcess
		extends BaseThreadSafe<org.openntf.domino.AdministrationProcess, lotus.domino.AdministrationProcess, Session>
		implements org.openntf.domino.AdministrationProcess {

	/**
	 * Instantiates a new administration process.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected AdministrationProcess(final lotus.domino.AdministrationProcess delegate, final Session parent) {
		super(delegate, parent, NOTES_ACLENTRY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#addGroupMembers(java.lang.String, java.util.Vector)
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public String addGroupMembers(final String group, final Vector members) {
		try {
			return getDelegate().addGroupMembers(group, members);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#addInternetCertificateToUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String addInternetCertificateToUser(final String user, final String keyringFile, final String keyringPassword) {
		try {
			return getDelegate().addInternetCertificateToUser(user, keyringFile, keyringPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#addInternetCertificateToUser(java.lang.String, java.lang.String, java.lang.String,
	 * lotus.domino.DateTime)
	 */
	@Override
	public String addInternetCertificateToUser(final String user, final String keyringFile, final String keyringPassword,
			final lotus.domino.DateTime expiration) {
		@SuppressWarnings("rawtypes")
		List recycleThis = new ArrayList();
		try {
			return getDelegate().addInternetCertificateToUser(user, keyringFile, keyringPassword, toLotus(expiration, recycleThis));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#addServerToCluster(java.lang.String, java.lang.String)
	 */
	@Override
	public String addServerToCluster(final String server, final String cluster) {
		try {
			return getDelegate().addServerToCluster(server, cluster);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#approveDeletePersonInDirectory(java.lang.String)
	 */
	@Override
	public String approveDeletePersonInDirectory(final String noteid) {
		try {
			return getDelegate().approveDeletePersonInDirectory(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#approveDeleteServerInDirectory(java.lang.String)
	 */
	@Override
	public String approveDeleteServerInDirectory(final String noteid) {
		try {
			return getDelegate().approveDeleteServerInDirectory(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#approveDesignElementDeletion(java.lang.String)
	 */
	@Override
	public String approveDesignElementDeletion(final String noteid) {
		try {
			return getDelegate().approveDesignElementDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#approveHostedOrgStorageDeletion(java.lang.String)
	 */
	@Override
	public String approveHostedOrgStorageDeletion(final String noteid) {
		try {
			return getDelegate().approveHostedOrgStorageDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#approveMailFileDeletion(java.lang.String)
	 */
	@Override
	public String approveMailFileDeletion(final String noteid) {
		try {
			return getDelegate().approveMailFileDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#approveMovedReplicaDeletion(java.lang.String)
	 */
	@Override
	public String approveMovedReplicaDeletion(final String noteid) {
		try {
			return getDelegate().approveMovedReplicaDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#approveNameChangeRetraction(java.lang.String)
	 */
	@Override
	public String approveNameChangeRetraction(final String noteid) {
		try {
			return getDelegate().approveNameChangeRetraction(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#approveRenamePersonInDirectory(java.lang.String)
	 */
	@Override
	public String approveRenamePersonInDirectory(final String noteid) {
		try {
			return getDelegate().approveRenamePersonInDirectory(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#approveRenameServerInDirectory(java.lang.String)
	 */
	@Override
	public String approveRenameServerInDirectory(final String noteid) {
		try {
			return getDelegate().approveRenameServerInDirectory(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#approveReplicaDeletion(java.lang.String)
	 */
	@Override
	public String approveReplicaDeletion(final String noteid) {
		try {
			return getDelegate().approveReplicaDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#approveResourceDeletion(java.lang.String)
	 */
	@Override
	public String approveResourceDeletion(final String noteid) {
		try {
			return getDelegate().approveResourceDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#changeHTTPPassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String changeHTTPPassword(final String userName, final String oldPassword, final String newPassword) {
		try {
			return getDelegate().changeHTTPPassword(userName, oldPassword, newPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#configureMailAgent(java.lang.String, java.lang.String)
	 */
	@Override
	public String configureMailAgent(final String userName, final String agentName) {
		try {
			return getDelegate().configureMailAgent(userName, agentName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#configureMailAgent(java.lang.String, java.lang.String, boolean, boolean)
	 */
	@Override
	public String configureMailAgent(final String userName, final String agentName, final boolean activatable, final boolean enable) {
		try {
			return getDelegate().configureMailAgent(userName, agentName, activatable, enable);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#createReplica(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String createReplica(final String sourceServer, final String sourceDBFile, final String destServer) {
		try {
			return getDelegate().createReplica(sourceServer, sourceDBFile, destServer);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#createReplica(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * boolean, boolean)
	 */
	@Override
	public String createReplica(final String sourceServer, final String sourceDBFile, final String destServer, final String destDBFile,
			final boolean copyACL, final boolean createFTIndex) {
		try {
			return getDelegate().createReplica(sourceServer, sourceDBFile, destServer, destDBFile, copyACL, createFTIndex);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#deleteGroup(java.lang.String, boolean)
	 */
	@Override
	public String deleteGroup(final String groupName, final boolean immediate) {
		try {
			return getDelegate().deleteGroup(groupName, immediate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#deleteGroup(java.lang.String, boolean, boolean)
	 */
	@Override
	public String deleteGroup(final String groupName, final boolean immediate, final boolean deleteWindowsGroup) {
		try {
			return getDelegate().deleteGroup(groupName, immediate, deleteWindowsGroup);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#deleteReplicas(java.lang.String, java.lang.String)
	 */
	@Override
	public String deleteReplicas(final String serverName, final String fileName) {
		try {
			return getDelegate().deleteReplicas(serverName, fileName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#deleteServer(java.lang.String, boolean)
	 */
	@Override
	public String deleteServer(final String serverName, final boolean immediate) {
		try {
			return getDelegate().deleteServer(serverName, immediate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#deleteUser(java.lang.String, boolean, int, java.lang.String)
	 */
	@Override
	public String deleteUser(final String userName, final boolean immediate, final int mailFileAction, final String denyGroup) {
		try {
			return getDelegate().deleteUser(userName, immediate, mailFileAction, denyGroup);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#deleteUser(java.lang.String, boolean, int, java.lang.String, boolean)
	 */
	@Override
	public String deleteUser(final String username, final boolean immediate, final int mailFileAction, final String denyGroup,
			final boolean deleteWindowsUser) {
		try {
			return getDelegate().deleteUser(username, immediate, mailFileAction, denyGroup, deleteWindowsUser);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#findGroupInDomain(java.lang.String)
	 */
	@Override
	public String findGroupInDomain(final String group) {
		try {
			return getDelegate().findGroupInDomain(group);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#findServerInDomain(java.lang.String)
	 */
	@Override
	public String findServerInDomain(final String server) {
		try {
			return getDelegate().findServerInDomain(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#findUserInDomain(java.lang.String)
	 */
	@Override
	public String findUserInDomain(final String user) {
		try {
			return getDelegate().findUserInDomain(user);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public final Session getAncestorSession() {
		return parent;
	}

	@Override
	protected final WrapperFactory getFactory() {
		return parent.getFactory();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#getCertificateAuthorityOrg()
	 */
	@Override
	public String getCertificateAuthorityOrg() {
		try {
			return getDelegate().getCertificateAuthorityOrg();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#getCertificateExpiration()
	 */
	@Override
	public DateTime getCertificateExpiration() {
		try {
			return fromLotus(getDelegate().getCertificateExpiration(), DateTime.SCHEMA, this.getAncestorSession());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#getCertifierFile()
	 */
	@Override
	public String getCertifierFile() {
		try {
			return getDelegate().getCertifierFile();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#getCertifierPassword()
	 */
	@Override
	public String getCertifierPassword() {
		try {
			return getDelegate().getCertifierPassword();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final Session getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#isCertificateAuthorityAvailable()
	 */
	@Override
	public boolean isCertificateAuthorityAvailable() {
		try {
			return getDelegate().isCertificateAuthorityAvailable();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#isUseCertificateAuthority()
	 */
	@Override
	public boolean isUseCertificateAuthority() {
		try {
			return getDelegate().isUseCertificateAuthority();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#moveMailUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String moveMailUser(final String userName, final String newHomeServer, final String newHomeServerMailPath) {
		try {
			return getDelegate().moveMailUser(userName, newHomeServer, newHomeServerMailPath);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#moveMailUser(java.lang.String, java.lang.String, java.lang.String, boolean,
	 * java.util.Vector, boolean)
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public String moveMailUser(final String userName, final String newHomeServer, final String newHomeServerMailPath, final boolean useSCOS,
			final Vector newClusterReplicas, final boolean deleteOldClusterReplicas) {
		try {
			return getDelegate().moveMailUser(userName, newHomeServer, newHomeServerMailPath, useSCOS, newClusterReplicas,
					deleteOldClusterReplicas);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#moveReplica(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String moveReplica(final String sourceServer, final String sourceDbFile, final String destServer) {
		try {
			return getDelegate().moveReplica(sourceServer, sourceDbFile, destServer);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#moveReplica(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * boolean, boolean)
	 */
	@Override
	public String moveReplica(final String sourceServer, final String sourceDbFile, final String destServer, final String destDbFile,
			final boolean copyACL, final boolean createFTIndex) {
		try {
			return getDelegate().moveReplica(sourceServer, sourceDbFile, destServer, destDbFile, copyACL, createFTIndex);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#moveRoamingUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String moveRoamingUser(final String userName, final String destServer, final String destServerPath) {
		try {
			return getDelegate().moveRoamingUser(userName, destServer, destServerPath);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#moveUserInHierarchyComplete(java.lang.String)
	 */
	@Override
	public String moveUserInHierarchyComplete(final String requestNoteid) {
		try {
			return getDelegate().moveUserInHierarchyComplete(requestNoteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#moveUserInHierarchyComplete(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String moveUserInHierarchyComplete(final String requestNoteid, final String lastName, final String firstName,
			final String middleInitial, final String orgUnit, final String altCommonName, final String altOrgUnit, final String altLanguage,
			final boolean renameWindowsUser) {
		try {
			return getDelegate().moveUserInHierarchyComplete(requestNoteid, lastName, firstName, middleInitial, orgUnit, altCommonName,
					altOrgUnit, altLanguage, renameWindowsUser);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#moveUserInHierarchyRequest(java.lang.String, java.lang.String)
	 */
	@Override
	public String moveUserInHierarchyRequest(final String userName, final String targetCertifier) {
		try {
			return getDelegate().moveUserInHierarchyRequest(userName, targetCertifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#moveUserInHierarchyRequest(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String moveUserInHierarchyRequest(final String userName, final String targetCertifier, final boolean allowPrimaryNameChange) {
		try {
			return getDelegate().moveUserInHierarchyRequest(userName, targetCertifier, allowPrimaryNameChange);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#recertifyServer(java.lang.String)
	 */
	@Override
	public String recertifyServer(final String server) {
		try {
			return getDelegate().recertifyServer(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#recertifyUser(java.lang.String)
	 */
	@Override
	public String recertifyUser(final String userName) {
		try {
			return getDelegate().recertifyUser(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#removeServerFromCluster(java.lang.String)
	 */
	@Override
	public String removeServerFromCluster(final String server) {
		try {
			return getDelegate().removeServerFromCluster(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#renameGroup(java.lang.String, java.lang.String)
	 */
	@Override
	public String renameGroup(final String group, final String newGroup) {
		try {
			return getDelegate().renameGroup(group, newGroup);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#renameNotesUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String renameNotesUser(final String userName, final String lastName, final String firstName, final String middleInitial,
			final String orgUnit) {
		try {
			return getDelegate().renameNotesUser(userName, lastName, firstName, middleInitial, orgUnit);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#renameNotesUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String renameNotesUser(final String userName, final String lastName, final String firstName, final String middleInitial,
			final String orgUnit, final String altCommonName, final String altOrgUnit, final String altLanguage,
			final boolean renameWindowsUser) {
		try {
			return getDelegate().renameNotesUser(userName, lastName, firstName, middleInitial, orgUnit, altCommonName, altOrgUnit,
					altLanguage, renameWindowsUser);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#renameWebUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String renameWebUser(final String userName, final String newFullName, final String newLastName, final String newFirstName,
			final String newMiddleInitial, final String newShortName, final String newInternetAddress) {
		try {
			return getDelegate().renameWebUser(userName, newFullName, newLastName, newFirstName, newMiddleInitial, newShortName,
					newInternetAddress);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#setCertificateAuthorityOrg(java.lang.String)
	 */
	@Override
	public void setCertificateAuthorityOrg(final String org) {
		try {
			getDelegate().setCertificateAuthorityOrg(org);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#setCertificateExpiration(lotus.domino.DateTime)
	 */
	@Override
	public void setCertificateExpiration(final lotus.domino.DateTime expiration) {
		@SuppressWarnings("rawtypes")
		List recycleThis = new ArrayList();
		try {
			getDelegate().setCertificateExpiration(toLotus(expiration, recycleThis));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		} finally {
			s_recycle(recycleThis);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#setCertifierFile(java.lang.String)
	 */
	@Override
	public void setCertifierFile(final String fileSpec) {
		try {
			getDelegate().setCertifierFile(fileSpec);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#setCertifierPassword(java.lang.String)
	 */
	@Override
	public void setCertifierPassword(final String password) {
		try {
			getDelegate().setCertifierPassword(password);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#setServerDirectoryAssistanceSettings(java.lang.String, java.lang.String)
	 */
	@Override
	public String setServerDirectoryAssistanceSettings(final String server, final String dbFile) {
		try {
			return getDelegate().setServerDirectoryAssistanceSettings(server, dbFile);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#setUseCertificateAuthority(boolean)
	 */
	@Override
	public void setUseCertificateAuthority(final boolean flag) {
		try {
			getDelegate().setUseCertificateAuthority(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#setUserPasswordSettings(java.lang.String, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer, java.lang.Boolean)
	 */
	@Override
	public String setUserPasswordSettings(final String userName, final Integer notesPasswordCheckSetting,
			final Integer notesPasswordChangeInterval, final Integer notesPasswordGracePeriod, final Boolean internetPasswordForceChange) {
		try {
			return getDelegate().setUserPasswordSettings(userName, notesPasswordCheckSetting, notesPasswordChangeInterval,
					notesPasswordGracePeriod, internetPasswordForceChange);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#signDatabaseWithServerID(java.lang.String, java.lang.String)
	 */
	@Override
	public String signDatabaseWithServerID(final String server, final String dbFile) {
		try {
			return getDelegate().signDatabaseWithServerID(server, dbFile);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#signDatabaseWithServerID(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String signDatabaseWithServerID(final String server, final String dbFile, final boolean updateonly) {
		try {
			return getDelegate().signDatabaseWithServerID(server, dbFile, updateonly);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#upgradeUserToHierarchical(java.lang.String)
	 */
	@Override
	public String upgradeUserToHierarchical(final String userName) {
		try {
			return getDelegate().upgradeUserToHierarchical(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.AdministrationProcess#upgradeUserToHierarchical(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String upgradeUserToHierarchical(final String userName, final String orgUnit, final String altCommonName,
			final String altOrgUnit, final String altLanguage) {
		try {
			return getDelegate().upgradeUserToHierarchical(userName, orgUnit, altCommonName, altOrgUnit, altLanguage);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see lotus.domino.AdministrationProcess#delegateMailFile(java.lang.String, java.util.Vector, java.util.Vector, java.util.Vector, java.util.Vector, java.util.Vector, java.util.Vector, java.util.Vector, java.lang.String, java.lang.String)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public String delegateMailFile(final String arg0, final Vector arg1, final Vector arg2, final Vector arg3, final Vector arg4,
			final Vector arg5, final Vector arg6, final Vector arg7, final String arg8, final String arg9) {
		try {
			return getDelegate().delegateMailFile(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see lotus.domino.AdministrationProcess#setEnableOutlookSupport(java.lang.String, boolean)
	 */
	@Override
	public String setEnableOutlookSupport(final String arg0, final boolean arg1) {
		try {
			return getDelegate().setEnableOutlookSupport(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteUser(final String arg0, final boolean arg1, final int arg2, final String arg3, final boolean arg4, final int arg5) {
		try {
			return getDelegate().deleteUser(arg0, arg1, arg2, arg3, arg4, arg5);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	public String approveRenamePersonCommonNameInDirectory(String arg0) {
		try {
			return getDelegate().approveRenamePersonCommonNameInDirectory(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

}
