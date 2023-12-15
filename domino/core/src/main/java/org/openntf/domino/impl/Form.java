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

import java.util.Date;
import java.util.Set;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.NoteCollection.SelectOption;
import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class Form.
 */
public class Form extends BaseThreadSafe<org.openntf.domino.Form, lotus.domino.Form, Database> implements org.openntf.domino.Form {

	/**
	 * Instantiates a new form.
	 *
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	protected Form(final lotus.domino.Form delegate, final Database parent) {
		super(delegate, parent, NOTES_FORM);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#getAliases()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector<String> getAliases() {
		try {
			return getDelegate().getAliases();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.Design#getDocument()
	 */
	@Override
	public Document getDocument() {
		return parent.getDocumentByUNID(this.getUniversalID());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#getFieldType(java.lang.String)
	 */
	@Override
	public int getFieldType(final String arg0) {
		try {
			return getDelegate().getFieldType(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#getFields()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector<String> getFields() {
		try {
			return getDelegate().getFields();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#getFormUsers()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector<String> getFormUsers() {
		try {
			return getDelegate().getFormUsers();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#getHttpURL()
	 */
	@Override
	public String getHttpURL() {
		try {
			return getDelegate().getHttpURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#getLockHolders()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector<String> getLockHolders() {
		try {
			return getDelegate().getLockHolders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#getName()
	 */
	@Override
	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.Design#getNoteID()
	 */
	@Override
	public String getNoteID() {
		NoteCollection notes = parent.createNoteCollection(false);
		notes.add(this);
		return notes.getFirstNoteID();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#getNotesURL()
	 */
	@Override
	public String getNotesURL() {
		try {
			return getDelegate().getNotesURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public final Database getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#getReaders()
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector<String> getReaders() {
		try {
			return getDelegate().getReaders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.Design#getUniversalID()
	 */
	@Override
	public String getUniversalID() {
		NoteCollection notes = parent.createNoteCollection(false);
		notes.add(this);
		return notes.getUNID(notes.getFirstNoteID());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#getURL()
	 */
	@Override
	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Form#getXPageAlt()
	 */
	@Override
	public String getXPageAlt() {
		if (getDocument().hasItem("$XPageAlt")) { //$NON-NLS-1$
			return getDocument().getItemValueString("$XPageAlt"); //$NON-NLS-1$
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.ext.Form#getXPageAlt()
	 */
	@Override
	public String getXPageAltClient() {
		if (getDocument().hasItem("$XPageAltClient")) { //$NON-NLS-1$
			return getDocument().getItemValueString("$XPageAltClient"); //$NON-NLS-1$
		} else if (getDocument().hasItem("$XPageAlt")) { //$NON-NLS-1$
			return getDocument().getItemValueString("$XPageAlt"); //$NON-NLS-1$
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#isProtectReaders()
	 */
	@Override
	public boolean isProtectReaders() {
		try {
			return getDelegate().isProtectReaders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#isProtectUsers()
	 */
	@Override
	public boolean isProtectUsers() {
		try {
			return getDelegate().isProtectUsers();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#isSubForm()
	 */
	@Override
	public boolean isSubForm() {
		try {
			return getDelegate().isSubForm();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#lock()
	 */
	@Override
	public boolean lock() {
		try {
			return getDelegate().lock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#lock(boolean)
	 */
	@Override
	public boolean lock(final boolean arg0) {
		try {
			return getDelegate().lock(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#lock(java.lang.String, boolean)
	 */
	@Override
	public boolean lock(final String arg0, final boolean arg1) {
		try {
			return getDelegate().lock(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#lock(java.lang.String)
	 */
	@Override
	public boolean lock(final String arg0) {
		try {
			return getDelegate().lock(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#lock(java.util.Vector, boolean)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public boolean lock(final Vector arg0, final boolean arg1) {
		try {
			return getDelegate().lock(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#lock(java.util.Vector)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public boolean lock(final Vector arg0) {
		try {
			return getDelegate().lock(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#lockProvisional()
	 */
	@Override
	public boolean lockProvisional() {
		try {
			return getDelegate().lockProvisional();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#lockProvisional(java.lang.String)
	 */
	@Override
	public boolean lockProvisional(final String arg0) {
		try {
			return getDelegate().lockProvisional(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#lockProvisional(java.util.Vector)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public boolean lockProvisional(final Vector arg0) {
		try {
			return getDelegate().lockProvisional(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#remove()
	 */
	@Override
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#setFormUsers(java.util.Vector)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void setFormUsers(final Vector arg0) {
		try {
			getDelegate().setFormUsers(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#setProtectReaders(boolean)
	 */
	@Override
	public void setProtectReaders(final boolean arg0) {
		try {
			getDelegate().setProtectReaders(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#setProtectUsers(boolean)
	 */
	@Override
	public void setProtectUsers(final boolean arg0) {
		try {
			getDelegate().setProtectUsers(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#setReaders(java.util.Vector)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void setReaders(final Vector arg0) {
		try {
			getDelegate().setReaders(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.Form#unlock()
	 */
	@Override
	public void unlock() {
		try {
			getDelegate().unlock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.DatabaseDescendant#getAncestorDatabase()
	 */
	@Override
	public final Database getAncestorDatabase() {
		return parent;
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

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.ext.Form#getModifiedNoteCount(java.util.Date)
	 */
	@Override
	public int getModifiedNoteCount(final Date since) {
		if (since.after(getAncestorDatabase().getLastModified().toJavaDate())) {
			return 0;
		}
		NoteCollection nc = getAncestorDatabase().createNoteCollection(false);
		nc.setSinceTime(since);
		Set<SelectOption> noteClass = new java.util.HashSet<SelectOption>();
		noteClass.add(SelectOption.DOCUMENTS);
		nc.setSelectOptions(noteClass);
		String selectionFormula = getSelectionFormula();
		nc.setSelectionFormula(selectionFormula);
		nc.buildCollection();
		return nc.getCount();
	}

	@Override
	public NoteCollection getNoteCollection() {
		NoteCollection nc = getAncestorDatabase().createNoteCollection(false);
		Set<SelectOption> noteClass = new java.util.HashSet<SelectOption>();
		noteClass.add(SelectOption.DOCUMENTS);
		nc.setSelectOptions(noteClass);
		String selectionFormula = getSelectionFormula();
		nc.setSelectionFormula(selectionFormula);
		nc.buildCollection();
		return nc;
	}

	@Override
	public String getSelectionFormula() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "); //$NON-NLS-1$
		sb.append("Form=\""); //$NON-NLS-1$
		sb.append(this.getName());
		sb.append("\""); //$NON-NLS-1$
		if (!this.getAliases().isEmpty()) {
			Vector<String> aliases = getAliases();
			for (String alias : aliases) {
				sb.append("|"); //$NON-NLS-1$
				sb.append("Form=\""); //$NON-NLS-1$
				sb.append(alias);
				sb.append("\""); //$NON-NLS-1$
			}
		}
		return sb.toString();
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

	private transient String metaversalid_;

	@Override
	public String getMetaversalID() {
		if (metaversalid_ == null) {
			metaversalid_ = getAncestorDatabase().getReplicaID() + getUniversalID();
		}
		return metaversalid_;
	}

}
