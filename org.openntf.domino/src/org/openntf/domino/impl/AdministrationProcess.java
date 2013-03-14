package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class AdministrationProcess extends Base<org.openntf.domino.AdministrationProcess, lotus.domino.AdministrationProcess> implements
		org.openntf.domino.AdministrationProcess {

	public AdministrationProcess(lotus.domino.AdministrationProcess delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String addGroupMembers(String group, Vector members) {
		try {
			return getDelegate().addGroupMembers(group, members);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String addInternetCertificateToUser(String user, String keyringFile, String keyringPassword) {
		try {
			return getDelegate().addInternetCertificateToUser(user, keyringFile, keyringPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String addInternetCertificateToUser(String user, String keyringFile, String keyringPassword, lotus.domino.DateTime expiration) {
		try {
			return getDelegate().addInternetCertificateToUser(user, keyringFile, keyringPassword,
					(lotus.domino.DateTime) toLotus(expiration));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String addServerToCluster(String server, String cluster) {
		try {
			return getDelegate().addServerToCluster(server, cluster);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveDeletePersonInDirectory(String noteid) {
		try {
			return getDelegate().approveDeletePersonInDirectory(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveDeleteServerInDirectory(String noteid) {
		try {
			return getDelegate().approveDeleteServerInDirectory(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveDesignElementDeletion(String noteid) {
		try {
			return getDelegate().approveDesignElementDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveHostedOrgStorageDeletion(String noteid) {
		try {
			return getDelegate().approveHostedOrgStorageDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveMailFileDeletion(String noteid) {
		try {
			return getDelegate().approveMailFileDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveMovedReplicaDeletion(String noteid) {
		try {
			return getDelegate().approveMovedReplicaDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveNameChangeRetraction(String noteid) {
		try {
			return getDelegate().approveNameChangeRetraction(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveRenamePersonInDirectory(String noteid) {
		try {
			return getDelegate().approveRenamePersonInDirectory(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveRenameServerInDirectory(String noteid) {
		try {
			return getDelegate().approveRenameServerInDirectory(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveReplicaDeletion(String noteid) {
		try {
			return getDelegate().approveReplicaDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveResourceDeletion(String noteid) {
		try {
			return getDelegate().approveResourceDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String changeHTTPPassword(String userName, String oldPassword, String newPassword) {
		try {
			return getDelegate().changeHTTPPassword(userName, oldPassword, newPassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String configureMailAgent(String userName, String agentName) {
		try {
			return getDelegate().configureMailAgent(userName, agentName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String configureMailAgent(String userName, String agentName, boolean activatable, boolean enable) {
		try {
			return getDelegate().configureMailAgent(userName, agentName, activatable, enable);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String createReplica(String sourceServer, String sourceDBFile, String destServer) {
		try {
			return getDelegate().createReplica(sourceServer, sourceDBFile, destServer);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String createReplica(String sourceServer, String sourceDBFile, String destServer, String destDBFile, boolean copyACL,
			boolean createFTIndex) {
		try {
			return getDelegate().createReplica(sourceServer, sourceDBFile, destServer, destDBFile, copyACL, createFTIndex);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteGroup(String groupName, boolean immediate) {
		try {
			return getDelegate().deleteGroup(groupName, immediate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteGroup(String groupName, boolean immediate, boolean deleteWindowsGroup) {
		try {
			return getDelegate().deleteGroup(groupName, immediate, deleteWindowsGroup);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteReplicas(String serverName, String fileName) {
		try {
			return getDelegate().deleteReplicas(serverName, fileName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteServer(String serverName, boolean immediate) {
		try {
			return getDelegate().deleteServer(serverName, immediate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteUser(String userName, boolean immediate, int mailFileAction, String denyGroup) {
		try {
			return getDelegate().deleteUser(userName, immediate, mailFileAction, denyGroup);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteUser(String username, boolean immediate, int mailFileAction, String denyGroup, boolean deleteWindowsUser) {
		try {
			return getDelegate().deleteUser(username, immediate, mailFileAction, denyGroup, deleteWindowsUser);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String findGroupInDomain(String group) {
		try {
			return getDelegate().findGroupInDomain(group);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String findServerInDomain(String server) {
		try {
			return getDelegate().findServerInDomain(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String findUserInDomain(String user) {
		try {
			return getDelegate().findUserInDomain(user);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getCertificateAuthorityOrg() {
		try {
			return getDelegate().getCertificateAuthorityOrg();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public DateTime getCertificateExpiration() {
		try {
			return Factory.fromLotus(getDelegate().getCertificateExpiration(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getCertifierFile() {
		try {
			return getDelegate().getCertifierFile();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getCertifierPassword() {
		try {
			return getDelegate().getCertifierPassword();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Session getParent() {
		return (Session) super.getParent();
	}

	@Override
	public boolean isCertificateAuthorityAvailable() {
		try {
			return getDelegate().isCertificateAuthorityAvailable();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isUseCertificateAuthority() {
		try {
			return getDelegate().isUseCertificateAuthority();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public String moveMailUser(String userName, String newHomeServer, String newHomeServerMailPath) {
		try {
			return getDelegate().moveMailUser(userName, newHomeServer, newHomeServerMailPath);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String moveMailUser(String userName, String newHomeServer, String newHomeServerMailPath, boolean useSCOS,
			Vector newClusterReplicas, boolean deleteOldClusterReplicas) {
		try {
			return getDelegate().moveMailUser(userName, newHomeServer, newHomeServerMailPath, useSCOS, newClusterReplicas,
					deleteOldClusterReplicas);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveReplica(String sourceServer, String sourceDbFile, String destServer) {
		try {
			return getDelegate().moveReplica(sourceServer, sourceDbFile, destServer);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveReplica(String sourceServer, String sourceDbFile, String destServer, String destDbFile, boolean copyACL,
			boolean createFTIndex) {
		try {
			return getDelegate().moveReplica(sourceServer, sourceDbFile, destServer, destDbFile, copyACL, createFTIndex);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveRoamingUser(String userName, String destServer, String destServerPath) {
		try {
			return getDelegate().moveRoamingUser(userName, destServer, destServerPath);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveUserInHierarchyComplete(String requestNoteid) {
		try {
			return getDelegate().moveUserInHierarchyComplete(requestNoteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveUserInHierarchyComplete(String requestNoteid, String lastName, String firstName, String middleInitial,
			String orgUnit, String altCommonName, String altOrgUnit, String altLanguage, boolean renameWindowsUser) {
		try {
			return getDelegate().moveUserInHierarchyComplete(requestNoteid, lastName, firstName, middleInitial, orgUnit, altCommonName,
					altOrgUnit, altLanguage, renameWindowsUser);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveUserInHierarchyRequest(String userName, String targetCertifier) {
		try {
			return getDelegate().moveUserInHierarchyRequest(userName, targetCertifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveUserInHierarchyRequest(String userName, String targetCertifier, boolean allowPrimaryNameChange) {
		try {
			return getDelegate().moveUserInHierarchyRequest(userName, targetCertifier, allowPrimaryNameChange);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String recertifyServer(String server) {
		try {
			return getDelegate().recertifyServer(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String recertifyUser(String userName) {
		try {
			return getDelegate().recertifyUser(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String removeServerFromCluster(String server) {
		try {
			return getDelegate().removeServerFromCluster(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String renameGroup(String group, String newGroup) {
		try {
			return getDelegate().renameGroup(group, newGroup);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String renameNotesUser(String userName, String lastName, String firstName, String middleInitial, String orgUnit) {
		try {
			return getDelegate().renameNotesUser(userName, lastName, firstName, middleInitial, orgUnit);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String renameNotesUser(String userName, String lastName, String firstName, String middleInitial, String orgUnit,
			String altCommonName, String altOrgUnit, String altLanguage, boolean renameWindowsUser) {
		try {
			return getDelegate().renameNotesUser(userName, lastName, firstName, middleInitial, orgUnit, altCommonName, altOrgUnit,
					altLanguage, renameWindowsUser);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String renameWebUser(String userName, String newFullName, String newLastName, String newFirstName, String newMiddleInitial,
			String newShortName, String newInternetAddress) {
		try {
			return getDelegate().renameWebUser(userName, newFullName, newLastName, newFirstName, newMiddleInitial, newShortName,
					newInternetAddress);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public void setCertificateAuthorityOrg(String org) {
		try {
			getDelegate().setCertificateAuthorityOrg(org);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCertificateExpiration(lotus.domino.DateTime expiration) {
		try {
			getDelegate().setCertificateExpiration((DateTime) toLotus(expiration));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCertifierFile(String fileSpec) {
		try {
			getDelegate().setCertifierFile(fileSpec);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCertifierPassword(String password) {
		try {
			getDelegate().setCertifierPassword(password);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public String setServerDirectoryAssistanceSettings(String server, String dbFile) {
		try {
			return getDelegate().setServerDirectoryAssistanceSettings(server, dbFile);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public void setUseCertificateAuthority(boolean flag) {
		try {
			getDelegate().setUseCertificateAuthority(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public String setUserPasswordSettings(String userName, Integer notesPasswordCheckSetting, Integer notesPasswordChangeInterval,
			Integer notesPasswordGracePeriod, Boolean internetPasswordForceChange) {
		try {
			return getDelegate().setUserPasswordSettings(userName, notesPasswordCheckSetting, notesPasswordChangeInterval,
					notesPasswordGracePeriod, internetPasswordForceChange);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String signDatabaseWithServerID(String server, String dbFile) {
		try {
			return getDelegate().signDatabaseWithServerID(server, dbFile);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String signDatabaseWithServerID(String server, String dbFile, boolean updateonly) {
		try {
			return getDelegate().signDatabaseWithServerID(server, dbFile, updateonly);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String upgradeUserToHierarchical(String username) {
		try {
			return getDelegate().upgradeUserToHierarchical(username);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String upgradeUserToHierarchical(String userName, String orgUnit, String altCommonName, String altOrgUnit, String altLanguage) {
		try {
			return getDelegate().upgradeUserToHierarchical(userName, orgUnit, altCommonName, altOrgUnit, altLanguage);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

}