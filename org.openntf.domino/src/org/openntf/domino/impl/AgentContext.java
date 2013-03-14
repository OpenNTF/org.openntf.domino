package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.Agent;
import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.DocumentCollection;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class AgentContext extends Base<org.openntf.domino.AgentContext, lotus.domino.AgentContext> implements
		org.openntf.domino.AgentContext {

	protected AgentContext(lotus.domino.AgentContext delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public Agent getCurrentAgent() {
		try {
			return Factory.fromLotus(getDelegate().getCurrentAgent(), Agent.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Database getCurrentDatabase() {
		try {
			return Factory.fromLotus(getDelegate().getCurrentDatabase(), Database.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public Document getDocumentContext() {
		try {
			return Factory.fromLotus(getDelegate().getDocumentContext(), Document.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getEffectiveUserName() {
		try {
			return getDelegate().getEffectiveUserName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public int getLastExitStatus() {
		try {
			return getDelegate().getLastExitStatus();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return 0;
	}

	@Override
	public DateTime getLastRun() {
		try {
			return Factory.fromLotus(getDelegate().getLastRun(), DateTime.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public lotus.domino.Document getSavedData() {
		try {
			return getDelegate().getSavedData();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public DocumentCollection getUnprocessedDocuments() {
		try {
			return Factory.fromLotus(getDelegate().getUnprocessedDocuments(), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public DocumentCollection unprocessedFTSearch(String query, int maxDocs) {
		try {
			return Factory.fromLotus(getDelegate().unprocessedFTSearch(query, maxDocs), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public DocumentCollection unprocessedFTSearch(String query, int maxDocs, int sortOpt, int otherOpt) {
		try {
			return Factory.fromLotus(getDelegate().unprocessedFTSearch(query, maxDocs, sortOpt, otherOpt), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public DocumentCollection unprocessedFTSearchRange(String query, int maxDocs, int sortOpt) {
		try {
			return Factory.fromLotus(getDelegate().unprocessedFTSearchRange(query, maxDocs, sortOpt), DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public DocumentCollection unprocessedFTSearchRange(String query, int maxDocs, int sortOpt, int otherOpt, int start) {
		try {
			return Factory.fromLotus(getDelegate().unprocessedFTSearchRange(query, maxDocs, sortOpt, otherOpt, start),
					DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public DocumentCollection unprocessedSearch(String formula, lotus.domino.DateTime limit, int maxDocs) {
		try {
			return Factory.fromLotus(getDelegate().unprocessedSearch(formula, (DateTime) toLotus(limit), maxDocs),
					DocumentCollection.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public void updateProcessedDoc(lotus.domino.Document doc) {
		try {
			getDelegate().updateProcessedDoc(doc);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
