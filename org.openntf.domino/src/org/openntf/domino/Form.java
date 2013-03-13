package org.openntf.domino;

import java.util.Vector;

public interface Form extends Base<lotus.domino.Form>, lotus.domino.Form {

	public org.openntf.domino.Document getDocument();

	@Override
	public Vector getAliases();

	@Override
	public Vector getFields();

	@Override
	public int getFieldType(String name);

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
	public org.openntf.domino.Database getParent();

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
	public boolean lock(boolean provisionalOk);

	@Override
	public boolean lock(String name);

	@Override
	public boolean lock(String name, boolean provisionalOk);

	@Override
	public boolean lock(Vector names);

	@Override
	public boolean lock(Vector names, boolean provisionalOk);

	@Override
	public boolean lockProvisional();

	@Override
	public boolean lockProvisional(String name);

	@Override
	public boolean lockProvisional(Vector names);

	@Override
	public void remove();

	@Override
	public void setFormUsers(Vector names);

	@Override
	public void setProtectReaders(boolean flag);

	@Override
	public void setProtectUsers(boolean flag);

	@Override
	public void setReaders(Vector names);

	@Override
	public void unlock();
}
