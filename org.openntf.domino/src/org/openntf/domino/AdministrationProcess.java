package org.openntf.domino;

import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.Session;

public interface AdministrationProcess extends Base<lotus.domino.AdministrationProcess>, lotus.domino.AdministrationProcess {

	@Override
	public String addGroupMembers(String arg0, Vector arg1);

	@Override
	public String addInternetCertificateToUser(String arg0, String arg1, String arg2);

	@Override
	public String addInternetCertificateToUser(String arg0, String arg1, String arg2, lotus.domino.DateTime arg3);

	@Override
	public String addServerToCluster(String arg0, String arg1);

	@Override
	public String approveDeletePersonInDirectory(String arg0);

	@Override
	public String approveDeleteServerInDirectory(String arg0);

	@Override
	public String approveDesignElementDeletion(String arg0);

	@Override
	public String approveHostedOrgStorageDeletion(String arg0);

	@Override
	public String approveMailFileDeletion(String arg0);

	@Override
	public String approveMovedReplicaDeletion(String arg0);

	@Override
	public String approveNameChangeRetraction(String arg0);

	@Override
	public String approveRenamePersonInDirectory(String arg0);

	@Override
	public String approveRenameServerInDirectory(String arg0);

	@Override
	public String approveReplicaDeletion(String arg0);

	@Override
	public String approveResourceDeletion(String arg0);

	@Override
	public String changeHTTPPassword(String arg0, String arg1, String arg2);

	@Override
	public String configureMailAgent(String arg0, String arg1);

	@Override
	public String configureMailAgent(String arg0, String arg1, boolean arg2, boolean arg3);

	@Override
	public String createReplica(String arg0, String arg1, String arg2);

	@Override
	public String createReplica(String arg0, String arg1, String arg2, String arg3, boolean arg4, boolean arg5);

	@Override
	public String deleteGroup(String arg0, boolean arg1);

	@Override
	public String deleteGroup(String arg0, boolean arg1, boolean arg2);

	@Override
	public String deleteReplicas(String arg0, String arg1);

	@Override
	public String deleteServer(String arg0, boolean arg1);

	@Override
	public String deleteUser(String arg0, boolean arg1, int arg2, String arg3);

	@Override
	public String deleteUser(String arg0, boolean arg1, int arg2, String arg3, boolean arg4);

	@Override
	public String findGroupInDomain(String arg0);

	@Override
	public String findServerInDomain(String arg0);

	@Override
	public String findUserInDomain(String arg0);

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
	public String moveMailUser(String arg0, String arg1, String arg2);

	@Override
	public String moveMailUser(String arg0, String arg1, String arg2, boolean arg3, Vector arg4, boolean arg5);

	@Override
	public String moveReplica(String arg0, String arg1, String arg2);

	@Override
	public String moveReplica(String arg0, String arg1, String arg2, String arg3, boolean arg4, boolean arg5);

	@Override
	public String moveRoamingUser(String arg0, String arg1, String arg2);

	@Override
	public String moveUserInHierarchyComplete(String arg0);

	@Override
	public String moveUserInHierarchyComplete(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6,
			String arg7, boolean arg8);

	@Override
	public String moveUserInHierarchyRequest(String arg0, String arg1);

	@Override
	public String moveUserInHierarchyRequest(String arg0, String arg1, boolean arg2);

	@Override
	public String recertifyServer(String arg0);

	@Override
	public String recertifyUser(String arg0);

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public String removeServerFromCluster(String arg0);

	@Override
	public String renameGroup(String arg0, String arg1);

	@Override
	public String renameNotesUser(String arg0, String arg1, String arg2, String arg3, String arg4);

	@Override
	public String renameNotesUser(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7,
			boolean arg8);

	@Override
	public String renameWebUser(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6);

	@Override
	public void setCertificateAuthorityOrg(String arg0);

	@Override
	public void setCertificateExpiration(DateTime arg0);

	@Override
	public void setCertifierFile(String arg0);

	@Override
	public void setCertifierPassword(String arg0);

	@Override
	public String setServerDirectoryAssistanceSettings(String arg0, String arg1);

	@Override
	public void setUseCertificateAuthority(boolean arg0);

	@Override
	public String setUserPasswordSettings(String arg0, Integer arg1, Integer arg2, Integer arg3, Boolean arg4);

	@Override
	public String signDatabaseWithServerID(String arg0, String arg1);

	@Override
	public String signDatabaseWithServerID(String arg0, String arg1, boolean arg2);

	@Override
	public String upgradeUserToHierarchical(String arg0);

	@Override
	public String upgradeUserToHierarchical(String arg0, String arg1, String arg2, String arg3, String arg4);

	@Override
	public boolean equals(Object obj);

}
