package org.openntf.domino;

public interface ReplicationEntry extends Base<lotus.domino.ReplicationEntry>, lotus.domino.ReplicationEntry {

	public String getDestination();

	@Override
	public String getFormula();

	@Override
	public String getSource();

	@Override
	public String getViews();

	@Override
	public boolean isIncludeACL();

	@Override
	public boolean isIncludeAgents();

	@Override
	public boolean isIncludeDocuments();

	@Override
	public boolean isIncludeForms();

	@Override
	public boolean isIncludeFormulas();

	@Override
	public int remove();

	@Override
	public int save();

	@Override
	public void setFormula(String formula);

	@Override
	public void setIncludeACL(boolean flag);

	@Override
	public void setIncludeAgents(boolean flag);

	@Override
	public void setIncludeDocuments(boolean flag);

	@Override
	public void setIncludeForms(boolean flag);

	@Override
	public void setIncludeFormulas(boolean flag);

	@Override
	public void setViews(String views);

}
