package org.openntf.domino.impl;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public class ReplicationEntry extends Base<org.openntf.domino.ReplicationEntry, lotus.domino.ReplicationEntry> implements
		org.openntf.domino.ReplicationEntry {

	public ReplicationEntry(lotus.domino.ReplicationEntry delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public String getDestination() {
		try {
			return getDelegate().getDestination();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getFormula() {
		try {
			return getDelegate().getFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getSource() {
		try {
			return getDelegate().getSource();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getViews() {
		try {
			return getDelegate().getViews();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean isIncludeACL() {
		try {
			return getDelegate().isIncludeACL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isIncludeAgents() {
		try {
			return getDelegate().isIncludeAgents();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isIncludeDocuments() {
		try {
			return getDelegate().isIncludeDocuments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isIncludeForms() {
		try {
			return getDelegate().isIncludeForms();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isIncludeFormulas() {
		try {
			return getDelegate().isIncludeFormulas();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public int remove() {
		try {
			return getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public int save() {
		try {
			return getDelegate().save();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	@Override
	public void setFormula(String formula) {
		try {
			getDelegate().setFormula(formula);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setIncludeACL(boolean flag) {
		try {
			getDelegate().setIncludeACL(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setIncludeAgents(boolean flag) {
		try {
			getDelegate().setIncludeAgents(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setIncludeDocuments(boolean flag) {
		try {
			getDelegate().setIncludeDocuments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setIncludeForms(boolean flag) {
		try {
			getDelegate().setIncludeForms(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setIncludeFormulas(boolean flag) {
		try {
			getDelegate().setIncludeFormulas(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setViews(String views) {
		try {
			getDelegate().setViews(views);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
