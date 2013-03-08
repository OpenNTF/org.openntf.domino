package org.openntf.domino;

import java.util.Vector;

public interface ACL extends Base<lotus.domino.ACL>, lotus.domino.ACL {

	@Override
	public void addRole(String name);

	@Override
	public ACLEntry createACLEntry(String name, int level);

	@Override
	public void deleteRole(String name);

	@Override
	public String getAdministrationServer();

	@Override
	public ACLEntry getEntry(String name);

	@Override
	public ACLEntry getFirstEntry();

	@Override
	public int getInternetLevel();

	@Override
	public ACLEntry getNextEntry();

	@Override
	public ACLEntry getNextEntry(lotus.domino.ACLEntry entry);

	@Override
	public Database getParent();

	@Override
	public Vector getRoles();

	@Override
	public boolean isAdminNames();

	@Override
	public boolean isAdminReaderAuthor();

	@Override
	public boolean isExtendedAccess();

	@Override
	public boolean isUniformAccess();

	@Override
	public void removeACLEntry(String name);

	@Override
	public void renameRole(String oldName, String newName);

	@Override
	public void save();

	@Override
	public void setAdministrationServer(String server);

	@Override
	public void setAdminNames(boolean flag);

	@Override
	public void setAdminReaderAuthor(boolean flag);

	@Override
	public void setExtendedAccess(boolean flag);

	@Override
	public void setInternetLevel(int level);

	@Override
	public void setUniformAccess(boolean flag);

}
