package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.DirectoryNavigator;
import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;

public class Directory extends Base<org.openntf.domino.Directory, lotus.domino.Directory> implements org.openntf.domino.Directory {

	protected Directory(lotus.domino.Directory delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public DirectoryNavigator createNavigator() {
		try {
			return getDelegate().createNavigator();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void freeLookupBuffer() {
		try {
			getDelegate().freeLookupBuffer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public Vector getAvailableItems() {
		try {
			return getDelegate().getAvailableItems();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector getAvailableNames() {
		try {
			return getDelegate().getAvailableNames();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getAvailableView() {
		try {
			return getDelegate().getAvailableView();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector getMailInfo(String arg0) {
		try {
			return getDelegate().getMailInfo(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public Vector getMailInfo(String arg0, boolean arg1, boolean arg2) {
		try {
			return getDelegate().getMailInfo(arg0, arg1, arg2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public String getServer() {
		try {
			return getDelegate().getServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public boolean isGroupAuthorizationOnly() {
		try {
			return getDelegate().isGroupAuthorizationOnly();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isLimitMatches() {
		try {
			return getDelegate().isLimitMatches();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isPartialMatches() {
		try {
			return getDelegate().isPartialMatches();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isSearchAllDirectories() {
		try {
			return getDelegate().isSearchAllDirectories();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isTrustedOnly() {
		try {
			return getDelegate().isTrustedOnly();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public boolean isUseContextServer() {
		try {
			return getDelegate().isUseContextServer();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return false;
		}
	}

	@Override
	public DirectoryNavigator lookupAllNames(String arg0, String arg1) {
		try {
			return getDelegate().lookupAllNames(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public DirectoryNavigator lookupAllNames(String arg0, Vector arg1) {
		try {
			return getDelegate().lookupAllNames(arg0, arg1);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public DirectoryNavigator lookupNames(String arg0, String arg1, String arg2) {
		try {
			return getDelegate().lookupNames(arg0, arg1, arg2);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public DirectoryNavigator lookupNames(String arg0, Vector arg1, Vector arg2, boolean arg3) {
		try {
			return getDelegate().lookupNames(arg0, arg1, arg2, arg3);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void setGroupAuthorizationOnly(boolean arg0) {
		try {
			getDelegate().setGroupAuthorizationOnly(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setLimitMatches(boolean arg0) {
		try {
			getDelegate().setLimitMatches(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSearchAllDirectories(boolean arg0) {
		try {
			getDelegate().setSearchAllDirectories(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setTrustedOnly(boolean arg0) {
		try {
			getDelegate().setTrustedOnly(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setUseContextServer(boolean arg0) {
		try {
			getDelegate().setUseContextServer(arg0);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}
