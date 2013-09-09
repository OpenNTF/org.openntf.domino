/*
 * Copyright 2013
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
package org.openntf.domino.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lotus.domino.NotesException;

import org.openntf.domino.Agent;
import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

// TODO: Auto-generated Javadoc
/**
 * The Class AgentContext.
 */
public class AgentContext extends Base<org.openntf.domino.AgentContext, lotus.domino.AgentContext> implements
		org.openntf.domino.AgentContext {

	/**
	 * Instantiates a new agent context.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	public AgentContext(final lotus.domino.AgentContext delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#getCurrentAgent()
	 */
	@Override
	public Agent getCurrentAgent() {
		try {
			return Factory.fromLotus(getDelegate().getCurrentAgent(), Agent.class, getCurrentDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#getCurrentDatabase()
	 */
	@Override
	public Database getCurrentDatabase() {
		try {
			return Factory.fromLotus(getDelegate().getCurrentDatabase(), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#getDocumentContext()
	 */
	@Override
	public Document getDocumentContext() {
		try {
			return Factory.fromLotus(getDelegate().getDocumentContext(), Document.class, getCurrentDatabase());
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#getEffectiveUserName()
	 */
	@Override
	public String getEffectiveUserName() {
		try {
			return getDelegate().getEffectiveUserName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#getLastExitStatus()
	 */
	@Override
	public int getLastExitStatus() {
		try {
			return getDelegate().getLastExitStatus();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#getLastRun()
	 */
	@Override
	public DateTime getLastRun() {
		try {
			return Factory.fromLotus(getDelegate().getLastRun(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.AgentContext#getParentSession()
	 */
	public Session getParentSession() {
		return (Session) super.getParent();
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.AgentContext#getQueryStringParameters()
	 */
	@Override
	public Map<String, List<String>> getQueryStringParameters() {
		Map<String, List<String>> result = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);

		Document doc = getDocumentContext();
		String queryString = doc.getItemValueString("QUERY_STRING");

		for (String pair : queryString.split("&")) {
			if (!pair.isEmpty()) {
				String[] bits = pair.split("=");
				try {
					String left = java.net.URLDecoder.decode(bits[0], "UTF-8");
					String right = bits.length > 1 ? java.net.URLDecoder.decode(bits[1], "UTF-8") : "";

					if (!result.containsKey(left)) {
						result.put(left, new ArrayList<String>());
					}
					result.get(left).add(right);
				} catch (UnsupportedEncodingException uee) {
					// I can't imagine how we'd get here, so we're free to fail entirely
					DominoUtils.handleException(uee);
					return null;
				}
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#getSavedData()
	 */
	@Override
	public Document getSavedData() {
		try {
			return Factory.fromLotus(getDelegate().getSavedData(), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#getUnprocessedDocuments()
	 */
	@Override
	public DocumentCollection getUnprocessedDocuments() {
		try {
			return Factory.fromLotus(getDelegate().getUnprocessedDocuments(), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#unprocessedFTSearch(java.lang.String, int)
	 */
	@Override
	public DocumentCollection unprocessedFTSearch(final String query, final int maxDocs) {
		try {
			return Factory.fromLotus(getDelegate().unprocessedFTSearch(query, maxDocs), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#unprocessedFTSearch(java.lang.String, int, int, int)
	 */
	@Override
	public DocumentCollection unprocessedFTSearch(final String query, final int maxDocs, final int sortOpt, final int otherOpt) {
		try {
			return Factory.fromLotus(getDelegate().unprocessedFTSearch(query, maxDocs, sortOpt, otherOpt), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#unprocessedFTSearchRange(java.lang.String, int, int)
	 */
	@Override
	public DocumentCollection unprocessedFTSearchRange(final String query, final int maxDocs, final int sortOpt) {
		try {
			return Factory.fromLotus(getDelegate().unprocessedFTSearchRange(query, maxDocs, sortOpt), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#unprocessedFTSearchRange(java.lang.String, int, int, int, int)
	 */
	@Override
	public DocumentCollection unprocessedFTSearchRange(final String query, final int maxDocs, final int sortOpt, final int otherOpt,
			final int start) {
		try {
			return Factory.fromLotus(getDelegate().unprocessedFTSearchRange(query, maxDocs, sortOpt, otherOpt, start),
					DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#unprocessedSearch(java.lang.String, lotus.domino.DateTime, int)
	 */
	@Override
	public DocumentCollection unprocessedSearch(final String formula, final lotus.domino.DateTime limit, final int maxDocs) {
		try {
			DocumentCollection result;
			lotus.domino.DateTime dt = (lotus.domino.DateTime) toLotus(limit);
			result = Factory.fromLotus(getDelegate().unprocessedSearch(formula, dt, maxDocs), DocumentCollection.class, this);
			enc_recycle(dt);
			return result;
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.AgentContext#updateProcessedDoc(lotus.domino.Document)
	 */
	@Override
	public void updateProcessedDoc(final lotus.domino.Document doc) {
		try {
			getDelegate().updateProcessedDoc((lotus.domino.Document) toLotus(doc));
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public org.openntf.domino.Session getAncestorSession() {
		return this.getParentSession();
	}
}
