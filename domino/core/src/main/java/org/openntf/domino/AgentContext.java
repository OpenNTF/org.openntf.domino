/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino;

import org.openntf.domino.types.FactorySchema;
import org.openntf.domino.types.SessionDescendant;

/**
 * The Interface AgentContext.
 */
public interface AgentContext extends Base<lotus.domino.AgentContext>, lotus.domino.AgentContext, org.openntf.domino.ext.AgentContext,
		SessionDescendant {

	public static class Schema extends FactorySchema<AgentContext, lotus.domino.AgentContext, Session> {
		@Override
		public Class<AgentContext> typeClass() {
			return AgentContext.class;
		}

		@Override
		public Class<lotus.domino.AgentContext> delegateClass() {
			return lotus.domino.AgentContext.class;
		}

		@Override
		public Class<Session> parentClass() {
			return Session.class;
		}
	};

	public static final Schema SCHEMA = new Schema();

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
	@Override
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

	/**
	 * Gets the parent session.
	 * 
	 * @return the parent session
	 */
	@Override
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
	public DocumentCollection unprocessedFTSearch(final String query, final int maxDocs);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#unprocessedFTSearch(java.lang.String, int, int, int)
	 */
	@Override
	public DocumentCollection unprocessedFTSearch(final String query, final int maxDocs, final int sortOpt, final int otherOpt);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#unprocessedFTSearchRange(java.lang.String, int, int)
	 */
	@Override
	public DocumentCollection unprocessedFTSearchRange(final String query, final int maxDocs, final int sortOpt);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#unprocessedFTSearchRange(java.lang.String, int, int, int, int)
	 */
	@Override
	public DocumentCollection unprocessedFTSearchRange(final String query, final int maxDocs, final int sortOpt, final int otherOpt,
			final int start);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#unprocessedSearch(java.lang.String, lotus.domino.DateTime, int)
	 */
	@Override
	public DocumentCollection unprocessedSearch(final String formula, final lotus.domino.DateTime limit, final int maxDocs);

	/*
	 * (non-Javadoc)
	 * 
	 * @see lotus.domino.AgentContext#updateProcessedDoc(lotus.domino.Document)
	 */
	@Override
	public void updateProcessedDoc(final lotus.domino.Document doc);

}
