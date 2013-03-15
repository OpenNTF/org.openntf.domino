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
package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.NotesException;

import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

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
	public Form(lotus.domino.Form delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#getAliases()
	 */
	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector getAliases() {
		try {
			return getDelegate().getAliases();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#getFieldType(java.lang.String)
	 */
	public int getFieldType(String arg0) {
		try {
			return getDelegate().getFieldType(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#getFields()
	 */
	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector getFields() {
		try {
			return getDelegate().getFields();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#getFormUsers()
	 */
	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector getFormUsers() {
		try {
			return getDelegate().getFormUsers();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#getLockHolders()
	 */
	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector getLockHolders() {
		try {
			return getDelegate().getLockHolders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#getDocument()
	 */
	public org.openntf.domino.Document getDocument() {
		try {
			Database db = getDelegate().getParent();
			String url = this.getNotesURL();
			String unid = DominoUtils.getUnidFromNotesUrl(url);
			return Factory.fromLotus(db.getDocumentByUNID(unid), org.openntf.domino.Database.class, this);
		} catch (NotesException ne) {
			DominoUtils.handleException(ne);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.impl.Base#getParent()
	 */
	@Override
	public org.openntf.domino.Database getParent() {
		return (org.openntf.domino.Database) super.getParent();
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#getReaders()
	 */
	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector getReaders() {
		try {
			return getDelegate().getReaders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#lock(boolean)
	 */
	public boolean lock(boolean arg0) {
		try {
			return getDelegate().lock(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#lock(java.lang.String, boolean)
	 */
	public boolean lock(String arg0, boolean arg1) {
		try {
			return getDelegate().lock(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#lock(java.lang.String)
	 */
	public boolean lock(String arg0) {
		try {
			return getDelegate().lock(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#lock(java.util.Vector, boolean)
	 */
	public boolean lock(Vector arg0, boolean arg1) {
		try {
			return getDelegate().lock(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#lock(java.util.Vector)
	 */
	public boolean lock(Vector arg0) {
		try {
			return getDelegate().lock(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#lockProvisional(java.lang.String)
	 */
	public boolean lockProvisional(String arg0) {
		try {
			return getDelegate().lockProvisional(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#lockProvisional(java.util.Vector)
	 */
	public boolean lockProvisional(Vector arg0) {
		try {
			return getDelegate().lockProvisional(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#remove()
	 */
	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#setFormUsers(java.util.Vector)
	 */
	public void setFormUsers(Vector arg0) {
		try {
			getDelegate().setFormUsers(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#setProtectReaders(boolean)
	 */
	public void setProtectReaders(boolean arg0) {
		try {
			getDelegate().setProtectReaders(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#setProtectUsers(boolean)
	 */
	public void setProtectUsers(boolean arg0) {
		try {
			getDelegate().setProtectUsers(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#setReaders(java.util.Vector)
	 */
	public void setReaders(Vector arg0) {
		try {
			getDelegate().setReaders(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.Form#unlock()
	 */
	public void unlock() {
		try {
			getDelegate().unlock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

}
