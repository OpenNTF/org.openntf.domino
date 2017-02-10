package org.openntf.domino.impl;

import org.openntf.domino.UserID;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

public class IDVault extends BaseThreadSafe<org.openntf.domino.IDVault, lotus.domino.IDVault, Session>
		implements org.openntf.domino.IDVault {

	protected IDVault(final lotus.domino.local.IDVault delegate, final Session parent, final int classId) {
		super(delegate, parent, classId);
	}

	@Override
	public String getServerName() {
		try {
			return getDelegate().getServerName();
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public UserID getUserID(final String arg0, final String arg1) {
		try {
			return fromLotus(getDelegate().getUserID(arg0, arg1), UserID.SCHEMA, this);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public UserID getUserID(final String arg0, final String arg1, final String arg2) {
		try {
			return fromLotus(getDelegate().getUserID(arg0, arg1, arg2), UserID.SCHEMA, this);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public void getUserIDFile(final String arg0, final String arg1, final String arg2) {
		try {
			getDelegate().getUserIDFile(arg0, arg1, arg2);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void getUserIDFile(final String arg0, final String arg1, final String arg2, final String arg3) {
		try {
			getDelegate().getUserIDFile(arg0, arg1, arg2, arg3);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public boolean isIDInVault(final String arg0) {
		try {
			return getDelegate().isIDInVault(arg0);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean isIDInVault(final String arg0, final String arg1) {
		try {
			return getDelegate().isIDInVault(arg0, arg1);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public void putUserIDFile(final String arg0, final String arg1, final String arg2) {
		try {
			getDelegate().putUserIDFile(arg0, arg1, arg2);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void putUserIDFile(final String arg0, final String arg1, final String arg2, final String arg3) {
		try {
			getDelegate().putUserIDFile(arg0, arg1, arg2, arg3);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void resetUserPassword(final String arg0, final String arg1) {
		try {
			getDelegate().resetUserPassword(arg0, arg1);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public void resetUserPassword(final String arg0, final String arg1, final String arg2, final int arg3) {
		try {
			getDelegate().resetUserPassword(arg0, arg1, arg2, arg3);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
	}

	@Override
	public boolean syncUserIDFile(final String arg0, final String arg1, final String arg2) {
		try {
			return getDelegate().syncUserIDFile(arg0, arg1, arg2);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	public boolean syncUserIDFile(final String arg0, final String arg1, final String arg2, final String arg3) {
		try {
			return getDelegate().syncUserIDFile(arg0, arg1, arg2, arg3);
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return false;
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getFactory();
	}

}
