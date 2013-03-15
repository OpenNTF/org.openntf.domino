package org.openntf.domino;

import java.util.Vector;

public interface Form extends Base<lotus.domino.Form>, lotus.domino.Form {

	public Document getDocument();

	@Override
	public Vector<String> getAliases();

	@Override
	public Vector<String> getFields();

	@Override
	public int getFieldType(String name);

	@Override
	public Vector<String> getFormUsers();

	@Override
	public String getHttpURL();

	@Override
	public Vector<String> getLockHolders();

	@Override
	public String getName();

	@Override
	public String getNotesURL();

	@Override
	public Database getParent();

	@Override
	public Vector<String> getReaders();

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

	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(Vector names);

	@SuppressWarnings("unchecked")
	@Override
	public boolean lock(Vector names, boolean provisionalOk);

	@Override
	public boolean lockProvisional();

	@Override
	public boolean lockProvisional(String name);

	@SuppressWarnings("unchecked")
	@Override
	public boolean lockProvisional(Vector names);

	@Override
	public void remove();

	@SuppressWarnings("unchecked")
	@Override
	public void setFormUsers(Vector names);

	@Override
	public void setProtectReaders(boolean flag);

	@Override
	public void setProtectUsers(boolean flag);

	@SuppressWarnings("unchecked")
	@Override
	public void setReaders(Vector names);

	@Override
	public void unlock();
}
