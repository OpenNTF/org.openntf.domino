/*
 * Copyright OpenNTF 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino;

// TODO: Auto-generated Javadoc
/**
 * The Interface AgentContext.
 */
public interface AgentContext extends Base<lotus.domino.AgentContext>, lotus.domino.AgentContext {

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#getCurrentAgent()
	 */
	@Override
	public Agent getCurrentAgent();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#getCurrentDatabase()
	 */
	public Database getCurrentDatabase();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#getDocumentContext()
	 */
	@Override
	public Document getDocumentContext();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#getEffectiveUserName()
	 */
	@Override
	public String getEffectiveUserName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#getLastExitStatus()
	 */
	@Override
	public int getLastExitStatus();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#getLastRun()
	 */
	@Override
	public DateTime getLastRun();

	public Session getParentSession();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#getSavedData()
	 */
	@Override
	public lotus.domino.Document getSavedData();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#getUnprocessedDocuments()
	 */
	@Override
	public DocumentCollection getUnprocessedDocuments();

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#unprocessedFTSearch(java.lang.String, int)
	 */
	@Override
	public DocumentCollection unprocessedFTSearch(String query, int maxDocs);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#unprocessedFTSearch(java.lang.String, int, int, int)
	 */
	@Override
	public DocumentCollection unprocessedFTSearch(String query, int maxDocs, int sortOpt, int otherOpt);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#unprocessedFTSearchRange(java.lang.String, int, int)
	 */
	@Override
	public DocumentCollection unprocessedFTSearchRange(String query, int maxDocs, int sortOpt);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#unprocessedFTSearchRange(java.lang.String, int, int, int, int)
	 */
	@Override
	public DocumentCollection unprocessedFTSearchRange(String query, int maxDocs, int sortOpt, int otherOpt, int start);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#unprocessedSearch(java.lang.String, lotus.domino.DateTime, int)
	 */
	@Override
	public DocumentCollection unprocessedSearch(String formula, lotus.domino.DateTime limit, int maxDocs);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#updateProcessedDoc(lotus.domino.Document)
	 */
	@Override
	public void updateProcessedDoc(lotus.domino.Document doc);

}
