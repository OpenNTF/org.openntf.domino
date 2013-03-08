package org.openntf.domino;

import java.util.Vector;

public interface ACLEntry extends Base<lotus.domino.ACLEntry>, lotus.domino.ACLEntry {

	@Override
	public void disableRole(String role);

	public void enableRole(String role);

	@Override
	public int getLevel();

	@Override
	public String getName();

	@Override
	public Name getNameObject();

	@Override
	public ACL getParent();

	@Override
	public Vector getRoles();

	@Override
	public int getUserType();

	@Override
	public boolean isAdminReaderAuthor();

	@Override
	public boolean isAdminServer();

	@Override
	public boolean isCanCreateDocuments();

	@Override
	public boolean isCanCreateLSOrJavaAgent();

	@Override
	public boolean isCanCreatePersonalAgent();

	@Override
	public boolean isCanCreatePersonalFolder();

	@Override
	public boolean isCanCreateSharedFolder();

	@Override
	public boolean isCanDeleteDocuments();

	@Override
	public boolean isCanReplicateOrCopyDocuments();

	@Override
	public boolean isGroup();

	@Override
	public boolean isPerson();

	@Override
	public boolean isPublicReader();

	@Override
	public boolean isPublicWriter();

	@Override
	public boolean isRoleEnabled(String role);

	@Override
	public boolean isServer();

	@Override
	public void remove();

	@Override
	public void setAdminReaderAuthor(boolean flag);

	@Override
	public void setAdminServer(boolean flag);

	@Override
	public void setCanCreateDocuments(boolean flag);

	@Override
	public void setCanCreateLSOrJavaAgent(boolean flag);

	@Override
	public void setCanCreatePersonalAgent(boolean flag);

	@Override
	public void setCanCreatePersonalFolder(boolean flag);

	@Override
	public void setCanCreateSharedFolder(boolean flag);

	@Override
	public void setCanDeleteDocuments(boolean flag);

	@Override
	public void setCanReplicateOrCopyDocuments(boolean flag);

	@Override
	public void setGroup(boolean flag);

	@Override
	public void setLevel(int level);

	@Override
	public void setName(lotus.domino.Name name);

	@Override
	public void setName(String name);

	@Override
	public void setPerson(boolean flag);

	@Override
	public void setPublicReader(boolean flag);

	@Override
	public void setPublicWriter(boolean flag);

	@Override
	public void setServer(boolean flag);

	@Override
	public void setUserType(int type);

}
