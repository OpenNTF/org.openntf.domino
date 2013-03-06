package org.openntf.domino;

import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.DocumentCollection;

public interface AgentContext extends Base<lotus.domino.AgentContext>, lotus.domino.AgentContext {

	@Override
	public Agent getCurrentAgent();

	public lotus.domino.Database getCurrentDatabase();

	public lotus.domino.AgentContext getDelegate();

	@Override
	public Document getDocumentContext();

	@Override
	public String getEffectiveUserName();

	@Override
	public int getLastExitStatus();

	@Override
	public DateTime getLastRun();

	@Override
	public lotus.domino.Document getSavedData();

	@Override
	public DocumentCollection getUnprocessedDocuments();

	@Override
	public void recycle();

	@Override
	public void recycle(Vector arg0);

	@Override
	public DocumentCollection unprocessedFTSearch(String arg0, int arg1);

	@Override
	public DocumentCollection unprocessedFTSearch(String arg0, int arg1, int arg2, int arg3);

	@Override
	public DocumentCollection unprocessedFTSearchRange(String arg0, int arg1, int arg2);

	@Override
	public DocumentCollection unprocessedFTSearchRange(String arg0, int arg1, int arg2, int arg3, int arg4);

	@Override
	public DocumentCollection unprocessedSearch(String arg0, DateTime arg1, int arg2);

	@Override
	public void updateProcessedDoc(lotus.domino.Document arg0);

	@Override
	public boolean equals(Object obj);

	@Override
	public int hashCode();

	@Override
	public String toString();

}
