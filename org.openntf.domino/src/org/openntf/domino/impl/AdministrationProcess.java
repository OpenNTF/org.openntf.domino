package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

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
	public String addInternetCertificateToUser(String user, String keyringfile, String keyringpassword) {
		try {
			return getDelegate().addInternetCertificateToUser(user, keyringfile, keyringpassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String addInternetCertificateToUser(String user, String keyringfile, String keyringpassword, DateTime expiration) {
		try {
			return getDelegate().addInternetCertificateToUser(user, keyringfile, keyringpassword, expiration);
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
	public String changeHTTPPassword(String username, String oldpassword, String newpassword) {
		try {
			return getDelegate().changeHTTPPassword(username, oldpassword, newpassword);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String configureMailAgent(String username, String agentname) {
		try {
			return getDelegate().configureMailAgent(username, agentname);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String configureMailAgent(String username, String agentname, boolean activatable, boolean enable) {
		try {
			return getDelegate().configureMailAgent(username, agentname, activatable, enable);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String createReplica(String sourceserver, String sourcedbfile, String destserver) {
		try {
			return getDelegate().createReplica(sourceserver, sourcedbfile, destserver);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String createReplica(String sourceserver, String sourcedbfile, String destserver, String destdbfile, boolean copyacl,
			boolean createftindex) {
		try {
			return getDelegate().createReplica(sourceserver, sourcedbfile, destserver, destdbfile, copyacl, createftindex);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteGroup(String groupname, boolean immediate) {
		try {
			return getDelegate().deleteGroup(groupname, immediate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteGroup(String groupname, boolean immediate, boolean deletewindowsgroup) {
		try {
			return getDelegate().deleteGroup(groupname, immediate, deletewindowsgroup);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteReplicas(String servername, String filename) {
		try {
			return getDelegate().deleteReplicas(servername, filename);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteServer(String servername, boolean immediate) {
		try {
			return getDelegate().deleteServer(servername, immediate);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteUser(String username, boolean immediate, int mailfileaction, String denygroup) {
		try {
			return getDelegate().deleteUser(username, immediate, mailfileaction, denygroup);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String deleteUser(String username, boolean immediate, int mailfileaction, String denygroup, boolean deletewindowsuser) {
		try {
			return getDelegate().deleteUser(username, immediate, mailfileaction, denygroup, deletewindowsuser);
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
			return getDelegate().getCertificateExpiration();
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
	public org.openntf.domino.Session getParent() {
		return (org.openntf.domino.Session) super.getParent();
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
	public String moveMailUser(String username, String newhomeserver, String newhomeservermailpath) {
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
			Vector newclusterreplicas, boolean deleteoldclusterreplicas) {
		try {
			return getDelegate().moveMailUser(username, newhomeserver, newhomeservermailpath, usescos, newclusterreplicas,
					deleteoldclusterreplicas);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveReplica(String sourceserver, String sourcedbfile, String destserver) {
		try {
			return getDelegate().moveReplica(sourceserver, sourcedbfile, destserver);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveReplica(String sourceserver, String sourcedbfile, String destserver, String destdbfile, boolean copyacl,
			boolean createftindex) {
		try {
			return getDelegate().moveReplica(sourceserver, sourcedbfile, destserver, destdbfile, copyacl, createftindex);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveRoamingUser(String username, String destserver, String destserverpath) {
		try {
			return getDelegate().moveRoamingUser(username, destserver, destserverpath);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveUserInHierarchyComplete(String requestnoteid) {
		try {
			return getDelegate().moveUserInHierarchyComplete(requestnoteid);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveUserInHierarchyComplete(String requestnoteid, String lastname, String firstname, String middleinitial,
			String orgunit, String altcommonname, String altorgunit, String altlanguage, boolean renamewindowsuser) {
		try {
			return getDelegate().moveUserInHierarchyComplete(requestnoteid, lastname, firstname, middleinitial, orgunit, altcommonname,
					altorgunit, altlanguage, renamewindowsuser);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveUserInHierarchyRequest(String username, String targetcertifier) {
		try {
			return getDelegate().moveUserInHierarchyRequest(username, targetcertifier);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String moveUserInHierarchyRequest(String username, String targetcertifier, boolean allowprimarynamechange) {
		try {
			return getDelegate().moveUserInHierarchyRequest(username, targetcertifier, allowprimarynamechange);
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
	public String recertifyUser(String username) {
		try {
			return getDelegate().recertifyUser(username);
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
	public String renameGroup(String group, String newgroup) {
		try {
			return getDelegate().renameGroup(group, newgroup);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String renameNotesUser(String username, String lastname, String firstname, String middleinitial, String orgunit) {
		try {
			return getDelegate().renameNotesUser(username, lastname, firstname, middleinitial, orgunit);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String renameNotesUser(String username, String lastname, String firstname, String middleinitial, String orgunit,
			String altcommonname, String altorgunit, String altlanguage, boolean renamewindowsuser) {
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
			String newshortname, String newinternetaccess) {
		try {
			return getDelegate().renameWebUser(username, newfullname, newlastname, newfirstname, newmiddleinitial, newshortname,
					newinternetaccess);
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
	public void setCertificateExpiration(DateTime expiration) {
		try {
			getDelegate().setCertificateExpiration(expiration);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCertifierFile(String filespec) {
		try {
			getDelegate().setCertifierFile(filespec);
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
	public String setServerDirectoryAssistanceSettings(String server, String dbfile) {
		try {
			return getDelegate().setServerDirectoryAssistanceSettings(server, dbfile);
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
	public String setUserPasswordSettings(String username, Integer notespasswordchecksetting, Integer notespasswordchangeinterval,
			Integer notespasswordgraceperiod, Boolean internetpasswordforcechange) {
		try {
			return getDelegate().setUserPasswordSettings(username, notespasswordchecksetting, notespasswordchangeinterval,
					notespasswordgraceperiod, internetpasswordforcechange);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String signDatabaseWithServerID(String server, String dbfile) {
		try {
			return getDelegate().signDatabaseWithServerID(server, dbfile);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String signDatabaseWithServerID(String server, String dbfile, boolean updateonly) {
		try {
			return getDelegate().signDatabaseWithServerID(server, dbfile, updateonly);
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
	public String upgradeUserToHierarchical(String username, String orgunit, String altcommonname, String altorgunit, String altlanguage) {
		try {
			return getDelegate().upgradeUserToHierarchical(username, orgunit, altcommonname, altorgunit, altlanguage);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

}
