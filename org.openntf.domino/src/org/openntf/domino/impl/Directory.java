package org.openntf.domino.impl;

import java.util.Vector;

import lotus.domino.NotesException;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

public class Directory extends Base<org.openntf.domino.Directory, lotus.domino.Directory> implements org.openntf.domino.Directory {

	protected Directory(lotus.domino.Directory delegate, org.openntf.domino.Base<?> parent) {
		super(delegate, parent);
	}

	@Override
	public DirectoryNavigator createNavigator() {
		try {
			return Factory.fromLotus(getDelegate().createNavigator(), DirectoryNavigator.class, this);
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

	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getAvailableItems() {
		try {
			return getDelegate().getAvailableItems();
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getAvailableNames() {
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

	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getMailInfo(String userName) {
		try {
			return getDelegate().getMailInfo(userName);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getMailInfo(String userName, boolean getVersion, boolean errorOnMultipleMatches) {
		try {
			return getDelegate().getMailInfo(userName, getVersion, errorOnMultipleMatches);
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
	public DirectoryNavigator lookupAllNames(String view, String item) {
		try {
			return Factory.fromLotus(getDelegate().lookupAllNames(view, item), DirectoryNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public DirectoryNavigator lookupAllNames(String view, Vector items) {
		try {
			return Factory.fromLotus(getDelegate().lookupAllNames(view, items), DirectoryNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public DirectoryNavigator lookupNames(String view, String name, String item) {
		try {
			return Factory.fromLotus(getDelegate().lookupNames(view, name, item), DirectoryNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public DirectoryNavigator lookupNames(String view, Vector names, Vector items, boolean partialMatches) {
		try {
			return Factory.fromLotus(getDelegate().lookupNames(view, names, items, partialMatches), DirectoryNavigator.class, this);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

	@Override
	public void setGroupAuthorizationOnly(boolean flag) {
		try {
			getDelegate().setGroupAuthorizationOnly(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setLimitMatches(boolean flag) {
		try {
			getDelegate().setLimitMatches(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setSearchAllDirectories(boolean flag) {
		try {
			getDelegate().setSearchAllDirectories(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setTrustedOnly(boolean flag) {
		try {
			getDelegate().setTrustedOnly(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void setUseContextServer(boolean flag) {
		try {
			getDelegate().setUseContextServer(flag);
		} catch (NotesException e) {
			DominoUtils.handleException(e);
		}
	}
}