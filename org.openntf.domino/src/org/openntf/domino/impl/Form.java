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

import java.util.Date;
import java.util.Set;
import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.NoteCollection.SelectOption;
import org.openntf.domino.Session;
import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.utils.DominoUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class Form.
 */
public class Form extends org.openntf.domino.impl.Base<org.openntf.domino.Form, lotus.domino.Form> implements org.openntf.domino.Form {

	/** The temp_. */
	lotus.domino.Form temp_;

	/**
	 * Instantiates a new form.
	 * 
	 * @param delegate
	 *            the delegate
	 * @param parent
	 *            the parent
	 */
	@Deprecated
	public Form(final lotus.domino.Form delegate, final org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Form#getAliases()
	 */
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
		return this.getParent().getDocumentByUNID(this.getUniversalID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Form#getFieldType(java.lang.String)
	 */
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
		NoteCollection notes = this.getParent().createNoteCollection(false);
		notes.add(this);
		return notes.getFirstNoteID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Form#getNotesURL()
	 */
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
	public Database getParent() {
		return (Database) super.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Form#getReaders()
	 */
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
		NoteCollection notes = this.getParent().createNoteCollection(false);
		notes.add(this);
		return notes.getUNID(notes.getFirstNoteID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Form#getURL()
	 */
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
		return getDocument().getItemValueString("$XPageAlt");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.Form#isProtectReaders()
	 */
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	public Database getAncestorDatabase() {
		return this.getParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public Session getAncestorSession() {
		return this.getAncestorDatabase().getAncestorSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.ext.Form#getModifiedNoteCount(java.util.Date)
	 */
	@Override
	public int getModifiedNoteCount(final Date since) {
		if (since.after(getAncestorDatabase().getLastModified().toJavaDate()))
			return 0;
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

	public String getSelectionFormula() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append("Form=\"");
		sb.append(this.getName());
		sb.append("\"");
		if (!this.getAliases().isEmpty()) {
			Vector<String> aliases = getAliases();
			for (String alias : aliases) {
				sb.append("|");
				sb.append("Form=\"");
				sb.append(alias);
				sb.append("\"");
			}
		}
		return sb.toString();
	}

}
