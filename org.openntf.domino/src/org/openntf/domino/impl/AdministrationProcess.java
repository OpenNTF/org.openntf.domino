package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.NotesException;
import lotus.domino.Session;

import org.openntf.domino.utils.DominoUtils;

public class AdministrationProcess extends Base<org.openntf.domino.AdministrationProcess, lotus.domino.AdministrationProcess> implements
		org.openntf.domino.AdministrationProcess {

	public AdministrationProcess(lotus.domino.AdministrationProcess delegate) {
		super(delegate);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String addGroupMembers(String group, Vector members) throws NotesException {
		try {
			return getDelegate().addGroupMembers(group, members);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String addInternetCertificateToUser(String user, String keyringfile, String keyringpassword) throws NotesException {
		try {
			return getDelegate().addInternetCertificateToUser(user, keyringfile, keyringpassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String addInternetCertificateToUser(String user, String keyringfile, String keyringpassword, DateTime expiration)
			throws NotesException {
		try {
			return getDelegate().addInternetCertificateToUser(user, keyringfile, keyringpassword, expiration);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String addServerToCluster(String server, String cluster) throws NotesException {
		try {
			return getDelegate().addServerToCluster(server, cluster);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveDeletePersonInDirectory(String noteid) throws NotesException {
		try {
			return getDelegate().approveDeletePersonInDirectory(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveDeleteServerInDirectory(String noteid) throws NotesException {
		try {
			return getDelegate().approveDeleteServerInDirectory(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveDesignElementDeletion(String noteid) throws NotesException {
		try {
			return getDelegate().approveDesignElementDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveHostedOrgStorageDeletion(String noteid) throws NotesException {
		try {
			return getDelegate().approveHostedOrgStorageDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveMailFileDeletion(String noteid) throws NotesException {
		try {
			return getDelegate().approveMailFileDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveMovedReplicaDeletion(String noteid) throws NotesException {
		try {
			return getDelegate().approveMovedReplicaDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveNameChangeRetraction(String noteid) throws NotesException {
		try {
			return getDelegate().approveNameChangeRetraction(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveRenamePersonInDirectory(String noteid) throws NotesException {
		try {
			return getDelegate().approveRenamePersonInDirectory(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveRenameServerInDirectory(String noteid) throws NotesException {
		try {
			return getDelegate().approveRenameServerInDirectory(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveReplicaDeletion(String noteid) throws NotesException {
		try {
			return getDelegate().approveReplicaDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String approveResourceDeletion(String noteid) throws NotesException {
		try {
			return getDelegate().approveResourceDeletion(noteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String changeHTTPPassword(String username, String oldpassword, String newpassword) throws NotesException {
		try {
			return getDelegate().changeHTTPPassword(username, oldpassword, newpassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String configureMailAgent(String username, String agentname) throws NotesException {
		try {
			return getDelegate().configureMailAgent(username, agentname);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String configureMailAgent(String username, String agentname, boolean activatable, boolean enable) throws NotesException {
		try {
			return getDelegate().configureMailAgent(username, agentname, activatable, enable);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String createReplica(String sourceserver, String sourcedbfile, String destserver) throws NotesException {
		try {
			return getDelegate().createReplica(sourceserver, sourcedbfile, destserver);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String createReplica(String sourceserver, String sourcedbfile, String destserver, String destdbfile, boolean copyacl,
			boolean createftindex) throws NotesException {
		try {
			return getDelegate().createReplica(sourceserver, sourcedbfile, destserver, destdbfile, copyacl, createftindex);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteGroup(String groupname, boolean immediate) throws NotesException {
		try {
			return getDelegate().deleteGroup(groupname, immediate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteGroup(String groupname, boolean immediate, boolean deletewindowsgroup) throws NotesException {
		try {
			return getDelegate().deleteGroup(groupname, immediate, deletewindowsgroup);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteReplicas(String servername, String filename) throws NotesException {
		try {
			return getDelegate().deleteReplicas(servername, filename);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteServer(String servername, boolean immediate) throws NotesException {
		try {
			return getDelegate().deleteServer(servername, immediate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteUser(String username, boolean immediate, int mailfileaction, String denygroup) throws NotesException {
		try {
			return getDelegate().deleteUser(username, immediate, mailfileaction, denygroup);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteUser(String username, boolean immediate, int mailfileaction, String denygroup, boolean deletewindowsuser)
			throws NotesException {
		try {
			return getDelegate().deleteUser(username, immediate, mailfileaction, denygroup, deletewindowsuser);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String findGroupInDomain(String group) throws NotesException {
		try {
			return getDelegate().findGroupInDomain(group);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String findServerInDomain(String server) throws NotesException {
		try {
			return getDelegate().findServerInDomain(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String findUserInDomain(String user) throws NotesException {
		try {
			return getDelegate().findUserInDomain(user);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getCertificateAuthorityOrg() throws NotesException {
		try {
			return getDelegate().getCertificateAuthorityOrg();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public DateTime getCertificateExpiration() throws NotesException {
		try {
			return getDelegate().getCertificateExpiration();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getCertifierFile() throws NotesException {
		try {
			return getDelegate().getCertifierFile();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getCertifierPassword() throws NotesException {
		try {
			return getDelegate().getCertifierPassword();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Session getParent() throws NotesException {
		try {
			return getDelegate().getParent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public boolean isCertificateAuthorityAvailable() throws NotesException {
		try {
			return getDelegate().isCertificateAuthorityAvailable();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isUseCertificateAuthority() throws NotesException {
		try {
			return getDelegate().isUseCertificateAuthority();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public String moveMailUser(String username, String newhomeserver, String newhomeservermailpath) throws NotesException {
		try {
			return getDelegate().moveMailUser(username, newhomeserver, newhomeservermailpath);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String moveMailUser(String username, String newhomeserver, String newhomeservermailpath, boolean usescos,
			Vector newclusterreplicas, boolean deleteoldclusterreplicas) throws NotesException {
		try {
			return getDelegate().moveMailUser(username, newhomeserver, newhomeservermailpath, usescos, newclusterreplicas,
					deleteoldclusterreplicas);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveReplica(String sourceserver, String sourcedbfile, String destserver) throws NotesException {
		try {
			return getDelegate().moveReplica(sourceserver, sourcedbfile, destserver);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveReplica(String sourceserver, String sourcedbfile, String destserver, String destdbfile, boolean copyacl,
			boolean createftindex) throws NotesException {
		try {
			return getDelegate().moveReplica(sourceserver, sourcedbfile, destserver, destdbfile, copyacl, createftindex);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveRoamingUser(String username, String destserver, String destserverpath) throws NotesException {
		try {
			return getDelegate().moveRoamingUser(username, destserver, destserverpath);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveUserInHierarchyComplete(String requestnoteid) throws NotesException {
		try {
			return getDelegate().moveUserInHierarchyComplete(requestnoteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveUserInHierarchyComplete(String requestnoteid, String lastname, String firstname, String middleinitial,
			String orgunit, String altcommonname, String altorgunit, String altlanguage, boolean renamewindowsuser) throws NotesException {
		try {
			return getDelegate().moveUserInHierarchyComplete(requestnoteid, lastname, firstname, middleinitial, orgunit, altcommonname,
					altorgunit, altlanguage, renamewindowsuser);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveUserInHierarchyRequest(String username, String targetcertifier) throws NotesException {
		try {
			return getDelegate().moveUserInHierarchyRequest(username, targetcertifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveUserInHierarchyRequest(String username, String targetcertifier, boolean allowprimarynamechange) throws NotesException {
		try {
			return getDelegate().moveUserInHierarchyRequest(username, targetcertifier, allowprimarynamechange);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String recertifyServer(String server) throws NotesException {
		try {
			return getDelegate().recertifyServer(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String recertifyUser(String username) throws NotesException {
		try {
			return getDelegate().recertifyUser(username);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String removeServerFromCluster(String server) throws NotesException {
		try {
			return getDelegate().removeServerFromCluster(server);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String renameGroup(String group, String newgroup) throws NotesException {
		try {
			return getDelegate().renameGroup(group, newgroup);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String renameNotesUser(String username, String lastname, String firstname, String middleinitial, String orgunit)
			throws NotesException {
		try {
			return getDelegate().renameNotesUser(username, lastname, firstname, middleinitial, orgunit);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String renameNotesUser(String username, String lastname, String firstname, String middleinitial, String orgunit,
			String altcommonname, String altorgunit, String altlanguage, boolean renamewindowsuser) throws NotesException {
		try {
			return getDelegate().renameNotesUser(username, lastname, firstname, middleinitial, orgunit, altcommonname, altorgunit,
					altlanguage, renamewindowsuser);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String renameWebUser(String username, String newfullname, String newlastname, String newfirstname, String newmiddleinitial,
			String newshortname, String newinternetaccess) throws NotesException {
		try {
			return getDelegate().renameWebUser(username, newfullname, newlastname, newfirstname, newmiddleinitial, newshortname,
					newinternetaccess);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public void setCertificateAuthorityOrg(String org) throws NotesException {
		try {
			getDelegate().setCertificateAuthorityOrg(org);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCertificateExpiration(DateTime expiration) throws NotesException {
		try {
			getDelegate().setCertificateExpiration(expiration);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCertifierFile(String filespec) throws NotesException {
		try {
			getDelegate().setCertifierFile(filespec);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCertifierPassword(String password) throws NotesException {
		try {
			getDelegate().setCertifierPassword(password);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public String setServerDirectoryAssistanceSettings(String server, String dbfile) throws NotesException {
		try {
			return getDelegate().setServerDirectoryAssistanceSettings(server, dbfile);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public void setUseCertificateAuthority(boolean flag) throws NotesException {
		try {
			getDelegate().setUseCertificateAuthority(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public String setUserPasswordSettings(String username, Integer notespasswordchecksetting, Integer notespasswordchangeinterval,
			Integer notespasswordgraceperiod, Boolean internetpasswordforcechange) throws NotesException {
		try {
			return getDelegate().setUserPasswordSettings(username, notespasswordchecksetting, notespasswordchangeinterval,
					notespasswordgraceperiod, internetpasswordforcechange);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String signDatabaseWithServerID(String server, String dbfile) throws NotesException {
		try {
			return getDelegate().signDatabaseWithServerID(server, dbfile);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String signDatabaseWithServerID(String server, String dbfile, boolean updateonly) throws NotesException {
		try {
			return getDelegate().signDatabaseWithServerID(server, dbfile, updateonly);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String upgradeUserToHierarchical(String username) throws NotesException {
		try {
			return getDelegate().upgradeUserToHierarchical(username);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String upgradeUserToHierarchical(String username, String orgunit, String altcommonname, String altorgunit, String altlanguage)
			throws NotesException {
		try {
			return getDelegate().upgradeUserToHierarchical(username, orgunit, altcommonname, altorgunit, altlanguage);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

}
