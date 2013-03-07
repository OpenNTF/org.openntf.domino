package org.openntf.domino;

import java.util.Vector;

import lotus.domino.ACL;
import lotus.domino.Name;

;

public interface ACLEntry extends Base<lotus.domino.ACLEntry>, lotus.domino.ACLEntry {

	@Override
	public void disableRole(String arg0);

	public void enableRole(String arg0);

	public lotus.domino.ACLEntry getDelegate();

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
	public boolean isRoleEnabled(String arg0);

	@Override
	public boolean isServer();

	@Override
	public void remove();

	@Override
	public void setAdminReaderAuthor(boolean arg0);

	@Override
	public void setAdminServer(boolean arg0);

	@Override
	public void setCanCreateDocuments(boolean arg0);

	@Override
	public void setCanCreateLSOrJavaAgent(boolean arg0);

	@Override
	public void setCanCreatePersonalAgent(boolean arg0);

	@Override
	public void setCanCreatePersonalFolder(boolean arg0);

	@Override
	public void setCanCreateSharedFolder(boolean arg0);

	@Override
	public void setCanDeleteDocuments(boolean arg0);

	@Override
	public void setCanReplicateOrCopyDocuments(boolean arg0);

	@Override
	public void setGroup(boolean arg0);

	@Override
	public void setLevel(int arg0);

	@Override
	public void setName(Name arg0);

	@Override
	public void setName(String arg0);

	@Override
	public void setPerson(boolean arg0);

	@Override
	public void setPublicReader(boolean arg0);

	@Override
	public void setPublicWriter(boolean arg0);

	@Override
	public void setServer(boolean arg0);

	@Override
	public void setUserType(int arg0);

}
