package org.openntf.domino;

import java.util.Collection;
import java.util.Vector;

import lotus.domino.Document;

public interface Session extends lotus.domino.Session, Base<lotus.domino.Session> {
	@Override
	public Vector<Object> evaluate(String arg0);

	@Override
	public Vector<Object> evaluate(String arg0, Document arg1);

	@Override
	public Vector<Database> getAddressBooks();

	@Override
	public Vector<DateRange> freeTimeSearch(lotus.domino.DateRange arg0, int arg1, Object arg2, boolean arg3);

	@Override
	public Vector<Name> getUserGroupNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so
												// there's no recycle burden?

	public Collection<String> getUserGroupNameListSafe();

	@Override
	public Vector<Name> getUserNameList(); // TODO should we use a Vector of names? Or allow someone to request it as String-only so there's
											// no recycle burden?

	public Collection<String> getUserNameListSafe();
}
