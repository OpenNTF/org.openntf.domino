package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.ACLEntry;
import lotus.domino.Database;
import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public class ACL extends Base<org.openntf.domino.ACL, lotus.domino.ACL> implements org.openntf.domino.ACL {

	public ACL(lotus.domino.ACL delegate) {
		super(delegate);
	}

	@Override
	public void addRole(String name) throws NotesException {
		try {
			getDelegate().addRole(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public ACLEntry createACLEntry(String name, int level) throws NotesException {
		try {
			return getDelegate().createACLEntry(name, level);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public void deleteRole(String name) throws NotesException {
		try {
			getDelegate().deleteRole(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public String getAdministrationServer() throws NotesException {
		try {
			return getDelegate().getAdministrationServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public ACLEntry getEntry(String ename) throws NotesException {
		try {
			return getDelegate().getEntry(ename);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public ACLEntry getFirstEntry() throws NotesException {
		try {
			return getDelegate().getFirstEntry();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public int getInternetLevel() throws NotesException {
		try {
			return getDelegate().getInternetLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return ACL.LEVEL_NOACCESS;
	}

	@Override
	public ACLEntry getNextEntry() throws NotesException {
		try {
			return getDelegate().getNextEntry();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public ACLEntry getNextEntry(ACLEntry entry) throws NotesException {
		try {
			return getDelegate().getNextEntry(entry);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Database getParent() throws NotesException {
		try {
			return getDelegate().getParent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector getRoles() throws NotesException {
		try {
			return getDelegate().getRoles();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public boolean isAdminNames() throws NotesException {
		try {
			return getDelegate().isAdminNames();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isAdminReaderAuthor() throws NotesException {
		try {
			return getDelegate().isAdminReaderAuthor();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isExtendedAccess() throws NotesException {
		try {
			return getDelegate().isExtendedAccess();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isUniformAccess() throws NotesException {
		try {
			return getDelegate().isUniformAccess();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public void removeACLEntry(String name) throws NotesException {
		try {
			getDelegate().removeACLEntry(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void renameRole(String oldname, String newname) throws NotesException {
		try {
			getDelegate().renameRole(oldname, newname);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void save() throws NotesException {
		try {
			getDelegate().save();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setAdminNames(boolean flag) throws NotesException {
		try {
			getDelegate().setAdminNames(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setAdminReaderAuthor(boolean flag) throws NotesException {
		try {
			getDelegate().setAdminReaderAuthor(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setAdministrationServer(String servername) throws NotesException {
		try {
			getDelegate().setAdministrationServer(servername);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setExtendedAccess(boolean flag) throws NotesException {
		try {
			getDelegate().setExtendedAccess(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setInternetLevel(int level) throws NotesException {
		try {
			getDelegate().setInternetLevel(level);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setUniformAccess(boolean flag) throws NotesException {
		try {
			getDelegate().setUniformAccess(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

}
