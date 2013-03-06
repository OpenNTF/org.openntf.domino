package org.openntf.domino;

import java.util.Vector;

import lotus.domino.Database;

public interface Form extends Base<lotus.domino.Form>, lotus.domino.Form {

	@Override
	public Vector getAliases();

	@Override
	public lotus.domino.Form getDelegate();

	@Override
	public Vector getFields();

	@Override
	public int getFieldType(String arg0);

	@Override
	public Vector getFormUsers();

	@Override
	public String getHttpURL();

	@Override
	public Vector getLockHolders();

	@Override
	public String getName();

	@Override
	public String getNotesURL();

	@Override
	public Database getParent();

	@Override
	public Vector getReaders();

	@Override
	public String getURL();

	@Override
	public boolean isProtectReaders();

	@Override
	public boolean isProtectUsers();

	@Override
	public boolean isSubForm();

	@Override
	public boolean lock();

	@Override
	public boolean lock(boolean arg0);

	@Override
	public boolean lock(String arg0);

	@Override
	public boolean lock(String arg0, boolean arg1);

	@Override
	public boolean lock(Vector arg0);

	@Override
	public boolean lock(Vector arg0, boolean arg1);

	@Override
	public boolean lockProvisional();

	@Override
	public boolean lockProvisional(String arg0);

	@Override
	public boolean lockProvisional(Vector arg0);

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void remove();

	@Override
	public void setFormUsers(Vector arg0);

	@Override
	public void setProtectReaders(boolean arg0);

	@Override
	public void setProtectUsers(boolean arg0);

	@Override
	public void setReaders(Vector arg0);

	@Override
	public void unlock();

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
