package org.openntf.domino;

import lotus.domino.DateTime;
import lotus.domino.DocumentCollection;

public interface AgentContext extends Base<lotus.domino.AgentContext>, lotus.domino.AgentContext {

	@Override
	public Agent getCurrentAgent();

	public Database getCurrentDatabase();

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
	public DocumentCollection unprocessedFTSearch(String query, int maxDocs);

	@Override
	public DocumentCollection unprocessedFTSearch(String query, int maxDocs, int sortOpt, int otherOpt);

	@Override
	public DocumentCollection unprocessedFTSearchRange(String query, int maxDocs, int sortOpt);

	@Override
	public DocumentCollection unprocessedFTSearchRange(String query, int maxDocs, int sortOpt, int otherOpt, int start);

	@Override
	public DocumentCollection unprocessedSearch(String formula, DateTime limit, int maxDocs);

	@Override
	public void updateProcessedDoc(lotus.domino.Document doc);

}
