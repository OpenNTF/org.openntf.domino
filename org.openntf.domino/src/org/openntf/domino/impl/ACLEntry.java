package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.ACL;
import lotus.domino.Name;
import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public class ACLEntry extends Base<org.openntf.domino.ACLEntry, lotus.domino.ACLEntry> implements org.openntf.domino.ACLEntry {

	public ACLEntry(lotus.domino.ACLEntry delegate) {
		super(delegate);
	}

	@Override
	public void disableRole(String role) throws NotesException {
		try {
			getDelegate().disableRole(role);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void enableRole(String role) throws NotesException {
		try {
			getDelegate().enableRole(role);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public int getLevel() throws NotesException {
		try {
			return getDelegate().getLevel();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return ACL.LEVEL_NOACCESS;
	}

	@Override
	public String getName() throws NotesException {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Name getNameObject() throws NotesException {
		try {
			return getDelegate().getNameObject();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public ACL getParent() throws NotesException {
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
	public int getUserType() throws NotesException {
		try {
			return getDelegate().getUserType();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return ACLEntry.TYPE_UNSPECIFIED;
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
	public boolean isAdminServer() throws NotesException {
		try {
			return getDelegate().isAdminServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanCreateDocuments() throws NotesException {
		try {
			return getDelegate().isCanCreateDocuments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanCreateLSOrJavaAgent() throws NotesException {
		try {
			return getDelegate().isCanCreateLSOrJavaAgent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanCreatePersonalAgent() throws NotesException {
		try {
			return getDelegate().isCanCreatePersonalAgent();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanCreatePersonalFolder() throws NotesException {
		try {
			return getDelegate().isCanCreatePersonalFolder();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanCreateSharedFolder() throws NotesException {
		try {
			return getDelegate().isCanCreateSharedFolder();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanDeleteDocuments() throws NotesException {
		try {
			return getDelegate().isCanDeleteDocuments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isCanReplicateOrCopyDocuments() throws NotesException {
		try {
			return getDelegate().isCanReplicateOrCopyDocuments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isGroup() throws NotesException {
		try {
			return getDelegate().isGroup();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isPerson() throws NotesException {
		try {
			return getDelegate().isPerson();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isPublicReader() throws NotesException {
		try {
			return getDelegate().isPublicReader();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isPublicWriter() throws NotesException {
		try {
			return getDelegate().isPublicWriter();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isRoleEnabled(String role) throws NotesException {
		try {
			return getDelegate().isRoleEnabled(role);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isServer() throws NotesException {
		try {
			return getDelegate().isServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public void remove() throws NotesException {
		try {
			getDelegate().remove();
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
	public void setAdminServer(boolean flag) throws NotesException {
		try {
			getDelegate().setAdminServer(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanCreateDocuments(boolean flag) throws NotesException {
		try {
			getDelegate().setCanCreateDocuments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanCreateLSOrJavaAgent(boolean flag) throws NotesException {
		try {
			getDelegate().setCanCreateLSOrJavaAgent(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanCreatePersonalAgent(boolean flag) throws NotesException {
		try {
			getDelegate().setCanCreatePersonalAgent(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanCreatePersonalFolder(boolean flag) throws NotesException {
		try {
			getDelegate().setCanCreatePersonalFolder(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanCreateSharedFolder(boolean flag) throws NotesException {
		try {
			getDelegate().setCanCreateSharedFolder(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanDeleteDocuments(boolean flag) throws NotesException {
		try {
			getDelegate().setCanDeleteDocuments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setCanReplicateOrCopyDocuments(boolean flag) throws NotesException {
		try {
			getDelegate().setCanReplicateOrCopyDocuments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setGroup(boolean flag) throws NotesException {
		try {
			getDelegate().setGroup(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setLevel(int level) throws NotesException {
		try {
			getDelegate().setLevel(level);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setName(String name) throws NotesException {
		try {
			getDelegate().setName(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setName(Name n) throws NotesException {
		try {
			getDelegate().setName(n);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPerson(boolean flag) throws NotesException {
		try {
			getDelegate().setPerson(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPublicReader(boolean flag) throws NotesException {
		try {
			getDelegate().setPublicReader(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setPublicWriter(boolean flag) throws NotesException {
		try {
			getDelegate().setPublicWriter(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setServer(boolean flag) throws NotesException {
		try {
			getDelegate().setServer(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setUserType(int tp) throws NotesException {
		try {
			getDelegate().setUserType(tp);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

}
