package org.openntf.domino;

import java.util.Collection;
import java.util.Vector;

import lotus.domino.Document;

import org.openntf.domino.annotations.Legacy;

public interface Session extends lotus.domino.Session, Base<lotus.domino.Session> {
	@Override
	public Vector<Object> evaluate(String arg0);

	@Override
	public Vector<Object> evaluate(String arg0, Document arg1);

	@Override
	@Deprecated
	@Legacy("Methods should return interfaces instead of classes")
	public Vector<Database> getAddressBooks();

	public Collection<Database> getAddressBookCollection();

	@Override
	@Deprecated
	@Legacy("Methods should return interfaces instead of classes")
	public Vector<DateRange> freeTimeSearch(lotus.domino.DateRange arg0, int arg1, Object arg2, boolean arg3);

	public Collection<DateRange> freeTimeSearch(org.openntf.domino.DateRange arg0, int arg1, String arg2, boolean arg3);

	public Collection<DateRange> freeTimeSearch(org.openntf.domino.DateRange arg0, int arg1, Collection<String> arg2, boolean arg3);

	@Override
	@Deprecated
	@Legacy("Methods should return interfaces instead of classes")
	public Vector<Name> getUserGroupNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so
												// there's no recycle burden?

	public Collection<String> getUserGroupNameCollection();

	@Override
	@Deprecated
	@Legacy("Methods should return interfaces instead of classes")
	public Vector<Name> getUserNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so there's
											// no recycle burden?

	public Collection<String> getUserNameCollection();
}
