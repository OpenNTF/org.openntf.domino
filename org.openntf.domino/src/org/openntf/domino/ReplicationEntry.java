package org.openntf.domino;

import java.util.Vector;

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
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public int remove();

	@Override
	public int save();

	@Override
	public void setFormula(String arg0);

	@Override
	public void setIncludeACL(boolean arg0);

	@Override
	public void setIncludeAgents(boolean arg0);

	@Override
	public void setIncludeDocuments(boolean arg0);

	@Override
	public void setIncludeForms(boolean arg0);

	@Override
	public void setIncludeFormulas(boolean arg0);

	@Override
	public void setViews(String arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
