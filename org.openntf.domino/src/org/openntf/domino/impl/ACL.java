package org.openntf.domino.impl;

import lotus.domino.ACLEntry;
import lotus.domino.Database;
import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public class ACL extends Base<org.openntf.domino.ACL, lotus.domino.ACL> implements org.openntf.domino.ACL {

	public ACL(lotus.domino.ACL delegate) {
		super(delegate);
	}

	@Override
	public void addRole(String name) {
		try {
			getDelegate().addRole(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public ACLEntry createACLEntry(String name, int level) {
		try {
			return getDelegate().createACLEntry(name, level);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public void deleteRole(String name) {
		try {
			getDelegate().deleteRole(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public String getAdministrationServer() {
		try {
			return getDelegate().getAdministrationServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public ACLEntry getEntry(String ename) {
		try {
			return getDelegate().getEntry(ename);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public ACLEntry getFirstEntry() {
		try {
			return getDelegate().getFirstEntry();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public int getInternetLevel() {
		try {
			return getDelegate().getInternetLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return ACL.LEVEL_NOACCESS;
	}

	@Override
	public ACLEntry getNextEntry() {
		try {
			return getDelegate().getNextEntry();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public ACLEntry getNextEntry(ACLEntry entry) {
		try {
			return getDelegate().getNextEntry(entry);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Database getParent() {
		try {
			return getDelegate().getParent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector getRoles() {
		try {
			return new Vector(getDelegate().getRoles());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public boolean isAdminNames() {
		try {
			return getDelegate().isAdminNames();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isAdminReaderAuthor() {
		try {
			return getDelegate().isAdminReaderAuthor();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isExtendedAccess() {
		try {
			return getDelegate().isExtendedAccess();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isUniformAccess() {
		try {
			return getDelegate().isUniformAccess();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public void removeACLEntry(String name) {
		try {
			getDelegate().removeACLEntry(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void renameRole(String oldname, String newname) {
		try {
			getDelegate().renameRole(oldname, newname);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void save() {
		try {
			getDelegate().save();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setAdminNames(boolean flag) {
		try {
			getDelegate().setAdminNames(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setAdminReaderAuthor(boolean flag) {
		try {
			getDelegate().setAdminReaderAuthor(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setAdministrationServer(String servername) {
		try {
			getDelegate().setAdministrationServer(servername);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setExtendedAccess(boolean flag) {
		try {
			getDelegate().setExtendedAccess(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setInternetLevel(int level) {
		try {
			getDelegate().setInternetLevel(level);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setUniformAccess(boolean flag) {
		try {
			getDelegate().setUniformAccess(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

}
