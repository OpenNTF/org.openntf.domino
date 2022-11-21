package org.openntf.domino.impl;

import java.util.Vector;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

import lotus.domino.Base;
import lotus.domino.DominoQuery;
import lotus.domino.NotesException;

public class QueryResultsProcessor extends BaseThreadSafe<org.openntf.domino.QueryResultsProcessor, lotus.domino.QueryResultsProcessor, org.openntf.domino.Database>
	implements org.openntf.domino.QueryResultsProcessor {
	
	protected QueryResultsProcessor(final lotus.domino.QueryResultsProcessor delegate, final Database parent) {
		super(delegate, parent, NOTES_QUERYRESULTSPROCESSOR);
	}

	@Override
	public void addCollection(Base collection, String referenceName) {
		try {
			getDelegate().addCollection(toLotus(collection), referenceName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addColumn(String name, String title, String formula, int sortOrder, boolean isHidden,
			boolean isCategorized) {
		try {
			getDelegate().addColumn(name, title, formula, sortOrder, isHidden, isCategorized);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addColumn(String name) {
		try {
			getDelegate().addColumn(name);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addDominoQuery(DominoQuery query, String queryString, String referenceName) {
		try {
			getDelegate().addDominoQuery(toLotus(query), queryString, referenceName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void addFormula(String formula, String columnName, String referenceName) {
		try {
			getDelegate().addFormula(formula, columnName, referenceName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public String executeToJSON() {
		try {
			return getDelegate().executeToJSON();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public View executeToView(String name, int expireHours, String reader) {
		try {
			lotus.domino.View result = getDelegate().executeToView(name, expireHours, reader);
			return fromLotus(result, View.SCHEMA, parent);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public View executeToView(String name, int expireHours, @SuppressWarnings("rawtypes") Vector readerList) {
		try {
			lotus.domino.View result = getDelegate().executeToView(name, expireHours, readerList);
			return fromLotus(result, View.SCHEMA, parent);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public View executeToView(String name, int expireHours) {
		try {
			lotus.domino.View result = getDelegate().executeToView(name, expireHours);
			return fromLotus(result, View.SCHEMA, parent);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public View executeToView(String name) {
		try {
			lotus.domino.View result = getDelegate().executeToView(name);
			return fromLotus(result, View.SCHEMA, parent);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public int getMaxEntries() {
		try {
			return getDelegate().getMaxEntries();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int getTimeoutSec() {
		try {
			return getDelegate().getTimeoutSec();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public void setMaxEntries(int maxEntries) {
		try {
			getDelegate().setMaxEntries(maxEntries);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setTimeoutSec(int timeoutSec) {
		try {
			getDelegate().setMaxEntries(timeoutSec);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public Database getAncestorDatabase() {
		return parent;
	}

	@Override
	public Session getAncestorSession() {
		return parent.getAncestorSession();
	}

	@Override
	protected final WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}
