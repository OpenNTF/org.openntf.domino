package org.openntf.domino;

public interface Newsletter extends Base<lotus.domino.Newsletter>, lotus.domino.Newsletter {

	@Override
	public Document formatDocument(lotus.domino.Database db, int index);

	@Override
	public Document formatMsgWithDoclinks(lotus.domino.Database db);

	@Override
	public Session getParent();

	@Override
	public String getSubjectItemName();

	@Override
	public boolean isDoScore();

	@Override
	public boolean isDoSubject();

	@Override
	public void setDoScore(boolean flag);

	@Override
	public void setDoSubject(boolean flag);

	@Override
	public void setSubjectItemName(String name);

}
