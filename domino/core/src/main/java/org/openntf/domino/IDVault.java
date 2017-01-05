package org.openntf.domino;

import lotus.domino.UserID;

public interface IDVault extends lotus.domino.IDVault, org.openntf.domino.ext.IDVault, Base<lotus.domino.IDVault> {

	@Override
	public String getServerName();

	@Override
	public UserID getUserID(String arg0, String arg1);

	@Override
	public UserID getUserID(String arg0, String arg1, String arg2);

	@Override
	public void getUserIDFile(String arg0, String arg1, String arg2);

	@Override
	public void getUserIDFile(String arg0, String arg1, String arg2, String arg3);

	@Override
	public boolean isIDInVault(String arg0);

	@Override
	public boolean isIDInVault(String arg0, String arg1);

	@Override
	public void putUserIDFile(String arg0, String arg1, String arg2);

	@Override
	public void putUserIDFile(String arg0, String arg1, String arg2, String arg3);

	@Override
	public void resetUserPassword(String arg0, String arg1);

	@Override
	public void resetUserPassword(String arg0, String arg1, String arg2, int arg3);

	@Override
	public boolean syncUserIDFile(String arg0, String arg1, String arg2);

	@Override
	public boolean syncUserIDFile(String arg0, String arg1, String arg2, String arg3);

}
