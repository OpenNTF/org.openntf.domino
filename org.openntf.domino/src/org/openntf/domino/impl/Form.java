package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.NotesException;

import org.openntf.domino.annotations.Legacy;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class Form extends org.openntf.domino.impl.Base<org.openntf.domino.Form, lotus.domino.Form> implements org.openntf.domino.Form {
	lotus.domino.Form temp_;

	public Form(lotus.domino.Form delegate) {
		super(delegate);
	}

	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector getAliases() {
		try {
			return getDelegate().getAliases();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public int getFieldType(String arg0) {
		try {
			return getDelegate().getFieldType(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return 0;

		}
	}

	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector getFields() {
		try {
			return getDelegate().getFields();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector getFormUsers() {
		try {
			return getDelegate().getFormUsers();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getHttpURL() {
		try {
			return getDelegate().getHttpURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector getLockHolders() {
		try {
			return getDelegate().getLockHolders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getName() {
		try {
			return getDelegate().getName();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getNotesURL() {
		try {
			return getDelegate().getNotesURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public Database getParent() {
		// FIXME NTF - in the case of parenting, we really should be passing the wrapper objects around when they create children
		try {
			return Factory.fromLotus(getDelegate().getParent(), org.openntf.domino.Database.class);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	@Legacy({ Legacy.GENERICS_WARNING, Legacy.INTERFACES_WARNING })
	public Vector getReaders() {
		try {
			return getDelegate().getReaders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public String getURL() {
		try {
			return getDelegate().getURL();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;

		}
	}

	public boolean isProtectReaders() {
		try {
			return getDelegate().isProtectReaders();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isProtectUsers() {
		try {
			return getDelegate().isProtectUsers();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean isSubForm() {
		try {
			return getDelegate().isSubForm();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean lock() {
		try {
			return getDelegate().lock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean lock(boolean arg0) {
		try {
			return getDelegate().lock(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean lock(String arg0, boolean arg1) {
		try {
			return getDelegate().lock(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean lock(String arg0) {
		try {
			return getDelegate().lock(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean lock(Vector arg0, boolean arg1) {
		try {
			return getDelegate().lock(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean lock(Vector arg0) {
		try {
			return getDelegate().lock(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean lockProvisional() {
		try {
			return getDelegate().lockProvisional();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean lockProvisional(String arg0) {
		try {
			return getDelegate().lockProvisional(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public boolean lockProvisional(Vector arg0) {
		try {
			return getDelegate().lockProvisional(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;

		}
	}

	public void remove() {
		try {
			getDelegate().remove();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setFormUsers(Vector arg0) {
		try {
			getDelegate().setFormUsers(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setProtectReaders(boolean arg0) {
		try {
			getDelegate().setProtectReaders(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setProtectUsers(boolean arg0) {
		try {
			getDelegate().setProtectUsers(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void setReaders(Vector arg0) {
		try {
			getDelegate().setReaders(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

	public void unlock() {
		try {
			getDelegate().unlock();
		} catch (NotesException e) {
			DominoUtils.handleException(e);

		}
	}

}
