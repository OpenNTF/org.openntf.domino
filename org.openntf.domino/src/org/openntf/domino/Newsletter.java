package org.openntf.domino;

import java.util.Vector;

public interface Newsletter extends Base<lotus.domino.Newsletter>, lotus.domino.Newsletter {

	@Override
	public lotus.domino.Document formatDocument(lotus.domino.Database arg0, int arg1);

	@Override
	public lotus.domino.Document formatMsgWithDoclinks(lotus.domino.Database arg0);

	@Override
	public Session getParent();

	@Override
	public String getSubjectItemName();

	@Override
	public boolean isDoScore();

	@Override
	public boolean isDoSubject();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public void setDoScore(boolean arg0);

	@Override
	public void setDoSubject(boolean arg0);

	@Override
	public void setSubjectItemName(String arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
