package org.openntf.domino;

import java.util.Vector;

import lotus.domino.ACLEntry;
import lotus.domino.Database;

;

public interface ACL extends Base<lotus.domino.ACL>, lotus.domino.ACL {

	@Override
	public void addRole(String arg0);

	@Override
	public ACLEntry createACLEntry(String arg0, int arg1);

	@Override
	public void deleteRole(String arg0);

	@Override
	public String getAdministrationServer();

	@Override
	public ACLEntry getEntry(String arg0);

	@Override
	public ACLEntry getFirstEntry();

	@Override
	public int getInternetLevel();

	@Override
	public ACLEntry getNextEntry();

	@Override
	public ACLEntry getNextEntry(ACLEntry arg0);

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
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void removeACLEntry(String arg0);

	@Override
	public void renameRole(String arg0, String arg1);

	@Override
	public void save();

	@Override
	public void setAdministrationServer(String arg0);

	@Override
	public void setAdminNames(boolean arg0);

	@Override
	public void setAdminReaderAuthor(boolean arg0);

	@Override
	public void setExtendedAccess(boolean arg0);

	@Override
	public void setInternetLevel(int arg0);

	@Override
	public void setUniformAccess(boolean arg0);

}
