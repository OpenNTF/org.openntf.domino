package org.openntf.domino;

import java.util.Vector;

public interface AdministrationProcess extends Base<lotus.domino.AdministrationProcess>, lotus.domino.AdministrationProcess {

	@Override
	public String addGroupMembers(String group, Vector members);

	@Override
	public String addInternetCertificateToUser(String user, String keyringFile, String keyringPassword);

	@Override
	public String addInternetCertificateToUser(String user, String keyringFile, String keyringPassword, lotus.domino.DateTime expiration);

	@Override
	public String addServerToCluster(String server, String cluster);

	@Override
	public String approveDeletePersonInDirectory(String noteid);

	@Override
	public String approveDeleteServerInDirectory(String noteid);

	@Override
	public String approveDesignElementDeletion(String noteid);

	@Override
	public String approveHostedOrgStorageDeletion(String noteid);

	@Override
	public String approveMailFileDeletion(String noteid);

	@Override
	public String approveMovedReplicaDeletion(String noteid);

	@Override
	public String approveNameChangeRetraction(String noteid);

	@Override
	public String approveRenamePersonInDirectory(String noteid);

	@Override
	public String approveRenameServerInDirectory(String noteid);

	@Override
	public String approveReplicaDeletion(String noteid);

	@Override
	public String approveResourceDeletion(String noteid);

	@Override
	public String changeHTTPPassword(String userName, String oldPassword, String newPassword);

	@Override
	public String configureMailAgent(String userName, String agentName);

	@Override
	public String configureMailAgent(String userName, String agentName, boolean activatable, boolean enable);

	@Override
	public String createReplica(String sourceServer, String sourceDBFile, String destServer);

	@Override
	public String createReplica(String sourceServer, String sourceDBFile, String destServer, String destDBFile, boolean copyACL,
			boolean createFTIndex);

	@Override
	public String deleteGroup(String groupName, boolean immediate);

	@Override
	public String deleteGroup(String groupName, boolean immediate, boolean deleteWindowsGroup);

	@Override
	public String deleteReplicas(String serverName, String fileName);

	@Override
	public String deleteServer(String serverName, boolean immediate);

	@Override
	public String deleteUser(String userName, boolean immediate, int mailFileAction, String denyGroup);

	@Override
	public String deleteUser(String userName, boolean immediate, int mailFileAction, String denyGroup, boolean deleteWindowsUser);

	@Override
	public String findGroupInDomain(String group);

	@Override
	public String findServerInDomain(String server);

	@Override
	public String findUserInDomain(String userName);

	@Override
	public String getCertificateAuthorityOrg();

	@Override
	public DateTime getCertificateExpiration();

	@Override
	public String getCertifierFile();

	@Override
	public String getCertifierPassword();

	@Override
	public Session getParent();

	@Override
	public boolean isCertificateAuthorityAvailable();

	@Override
	public boolean isUseCertificateAuthority();

	@Override
	public String moveMailUser(String userName, String newHomeServer, String newHomeServerMailPath);

	@Override
	public String moveMailUser(String userName, String newHomeServer, String newHomeServerMailPath, boolean useSCOS,
			Vector newClusterReplicas, boolean deleteOldClusterReplicas);

	@Override
	public String moveReplica(String sourceServer, String sourceDbFile, String destServer);

	@Override
	public String moveReplica(String sourceServer, String sourceDbFile, String destServer, String destDbFile, boolean copyACL,
			boolean createFTIndex);

	@Override
	public String moveRoamingUser(String userName, String destServer, String destServerPath);

	@Override
	public String moveUserInHierarchyComplete(String requestNoteid);

	@Override
	public String moveUserInHierarchyComplete(String requestNoteid, String lastName, String firstName, String middleInitial,
			String orgUnit, String altCommonName, String altOrgUnit, String altLanguage, boolean renameWindowsUser);

	@Override
	public String moveUserInHierarchyRequest(String userName, String targetCertifier);

	@Override
	public String moveUserInHierarchyRequest(String userName, String targetCertifier, boolean allowPrimaryNameChange);

	@Override
	public String recertifyServer(String server);

	@Override
	public String recertifyUser(String userName);

	@Override
	public String removeServerFromCluster(String server);

	@Override
	public String renameGroup(String group, String newGroup);

	@Override
	public String renameNotesUser(String userName, String lastName, String firstName, String middleInitial, String orgUnit);

	@Override
	public String renameNotesUser(String userName, String lastName, String firstName, String middleInitial, String orgUnit,
			String altCommonName, String altOrgUnit, String altLanguage, boolean renameWindowsUser);

	@Override
	public String renameWebUser(String userName, String newFullName, String newLastName, String newFirstName, String newMiddleInitial,
			String newShortName, String newInternetAddress);

	@Override
	public void setCertificateAuthorityOrg(String org);

	@Override
	public void setCertificateExpiration(lotus.domino.DateTime expiration);

	@Override
	public void setCertifierFile(String fileSpec);

	@Override
	public void setCertifierPassword(String password);

	@Override
	public String setServerDirectoryAssistanceSettings(String server, String dbFile);

	@Override
	public void setUseCertificateAuthority(boolean flag);

	@Override
	public String setUserPasswordSettings(String userName, Integer notesPasswordCheckSetting, Integer notesPasswordChangeInterval,
			Integer notesPasswordGracePeriod, Boolean internetPasswordForceChange);

	@Override
	public String signDatabaseWithServerID(String server, String dbFile);

	@Override
	public String signDatabaseWithServerID(String server, String dbFile, boolean updateOnly);

	@Override
	public String upgradeUserToHierarchical(String userName);

	@Override
	public String upgradeUserToHierarchical(String userName, String orgUnit, String altCommonName, String altOrgUnit, String altLanguage);

}
