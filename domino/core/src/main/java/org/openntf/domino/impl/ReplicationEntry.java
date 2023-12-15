/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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
package org.openntf.domino.impl;

import java.util.Collection;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Replication;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ReplicationEntry.
 */
public class ReplicationEntry extends BaseThreadSafe<org.openntf.domino.ReplicationEntry, lotus.domino.ReplicationEntry, Replication>
implements org.openntf.domino.ReplicationEntry {

	/**
	 * Instantiates a new outline.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected ReplicationEntry(final lotus.domino.ReplicationEntry delegate, final Replication parent) {
		super(delegate, parent, NOTES_REPLENT);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#getDestination()
	 */
	@Override
	public String getDestination() {
		try {
			return getDelegate().getDestination();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#getFormula()
	 */
	@Override
	public String getFormula() {
		try {
			return getDelegate().getFormula();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final Replication getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#getSource()
	 */
	@Override
	public String getSource() {
		try {
			return getDelegate().getSource();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#getViews()
	 */
	@Override
	public String getViews() {
		try {
			return getDelegate().getViews();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#isIncludeACL()
	 */
	@Override
	public boolean isIncludeACL() {
		try {
			return getDelegate().isIncludeACL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#isIncludeAgents()
	 */
	@Override
	public boolean isIncludeAgents() {
		try {
			return getDelegate().isIncludeAgents();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#isIncludeDocuments()
	 */
	@Override
	public boolean isIncludeDocuments() {
		try {
			return getDelegate().isIncludeDocuments();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#isIncludeForms()
	 */
	@Override
	public boolean isIncludeForms() {
		try {
			return getDelegate().isIncludeForms();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#isIncludeFormulas()
	 */
	@Override
	public boolean isIncludeFormulas() {
		try {
			return getDelegate().isIncludeFormulas();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#remove()
	 */
	@Override
	public int remove() {
		try {
			return getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#save()
	 */
	@Override
	public int save() {
		try {
			return getDelegate().save();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#setFormula(java.lang.String)
	 */
	@Override
	public void setFormula(final String formula) {
		try {
			getDelegate().setFormula(formula);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#setIncludeACL(boolean)
	 */
	@Override
	public void setIncludeACL(final boolean flag) {
		try {
			getDelegate().setIncludeACL(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#setIncludeAgents(boolean)
	 */
	@Override
	public void setIncludeAgents(final boolean flag) {
		try {
			getDelegate().setIncludeAgents(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#setIncludeDocuments(boolean)
	 */
	@Override
	public void setIncludeDocuments(final boolean flag) {
		try {
			getDelegate().setIncludeDocuments(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#setIncludeForms(boolean)
	 */
	@Override
	public void setIncludeForms(final boolean flag) {
		try {
			getDelegate().setIncludeForms(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#setIncludeFormulas(boolean)
	 */
	@Override
	public void setIncludeFormulas(final boolean flag) {
		try {
			getDelegate().setIncludeFormulas(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ReplicationEntry#setViews(java.lang.String)
	 */
	@Override
	public void setViews(final String views) {
		try {
			getDelegate().setViews(views);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ReplicationEntry#setViews(java.util.Collection)
	 */
	@Override
	public void setViews(final Collection<String> views) {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (String view : views) {
			if (!first) {	//removed cleverness attempt on advice from FindBugs
				result.append(";"); //$NON-NLS-1$
			} else {
				first = false;
			}
			result.append(view);
		}
		this.setViews(result.toString());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public final Database getAncestorDatabase() {
		return parent.getAncestorDatabase();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return this.getAncestorDatabase().getAncestorSession();
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}
