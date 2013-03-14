package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class ACLEntry extends Base<org.openntf.domino.ACLEntry, lotus.domino.ACLEntry> implements org.openntf.domino.ACLEntry {

	public ACLEntry(lotus.domino.ACLEntry delegate, org.openntf.domino.ACL parent) {
		super(delegate, parent);
	}

	@Override
	public void disableRole(String role) {
		try {
			getDelegate().disableRole(role);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void enableRole(String role) {
		try {
			getDelegate().enableRole(role);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public int getLevel() {
		try {
			return getDelegate().getLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return ACL.LEVEL_NOACCESS;
	}

	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Name getNameObject() {
		try {
			return Factory.fromLotus(getDelegate().getNameObject(), Name.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public ACL getParent() {
		return (ACL) super.getParent();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getRoles() {
		try {
			return getDelegate().getRoles();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public int getUserType() {
		try {
			return getDelegate().getUserType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return ACLEntry.TYPE_UNSPECIFIED;
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
	public boolean isAdminServer() {
		try {
			return getDelegate().isAdminServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanCreateDocuments() {
		try {
			return getDelegate().isCanCreateDocuments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanCreateLSOrJavaAgent() {
		try {
			return getDelegate().isCanCreateLSOrJavaAgent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanCreatePersonalAgent() {
		try {
			return getDelegate().isCanCreatePersonalAgent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanCreatePersonalFolder() {
		try {
			return getDelegate().isCanCreatePersonalFolder();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanCreateSharedFolder() {
		try {
			return getDelegate().isCanCreateSharedFolder();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanDeleteDocuments() {
		try {
			return getDelegate().isCanDeleteDocuments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanReplicateOrCopyDocuments() {
		try {
			return getDelegate().isCanReplicateOrCopyDocuments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isGroup() {
		try {
			return getDelegate().isGroup();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isPerson() {
		try {
			return getDelegate().isPerson();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isPublicReader() {
		try {
			return getDelegate().isPublicReader();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isPublicWriter() {
		try {
			return getDelegate().isPublicWriter();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isRoleEnabled(String role) {
		try {
			return getDelegate().isRoleEnabled(role);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isServer() {
		try {
			return getDelegate().isServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public void remove() {
		try {
			getDelegate().remove();
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
	public void setAdminServer(boolean flag) {
		try {
			getDelegate().setAdminServer(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanCreateDocuments(boolean flag) {
		try {
			getDelegate().setCanCreateDocuments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanCreateLSOrJavaAgent(boolean flag) {
		try {
			getDelegate().setCanCreateLSOrJavaAgent(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanCreatePersonalAgent(boolean flag) {
		try {
			getDelegate().setCanCreatePersonalAgent(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanCreatePersonalFolder(boolean flag) {
		try {
			getDelegate().setCanCreatePersonalFolder(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanCreateSharedFolder(boolean flag) {
		try {
			getDelegate().setCanCreateSharedFolder(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanDeleteDocuments(boolean flag) {
		try {
			getDelegate().setCanDeleteDocuments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanReplicateOrCopyDocuments(boolean flag) {
		try {
			getDelegate().setCanReplicateOrCopyDocuments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setGroup(boolean flag) {
		try {
			getDelegate().setGroup(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setLevel(int level) {
		try {
			getDelegate().setLevel(level);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setName(String name) {
		try {
			getDelegate().setName(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setName(lotus.domino.Name n) {
		try {
			getDelegate().setName((lotus.domino.Name) toLotus(n));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPerson(boolean flag) {
		try {
			getDelegate().setPerson(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPublicReader(boolean flag) {
		try {
			getDelegate().setPublicReader(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPublicWriter(boolean flag) {
		try {
			getDelegate().setPublicWriter(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setServer(boolean flag) {
		try {
			getDelegate().setServer(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setUserType(int tp) {
		try {
			getDelegate().setUserType(tp);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

}