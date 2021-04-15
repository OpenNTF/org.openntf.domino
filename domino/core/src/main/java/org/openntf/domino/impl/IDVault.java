/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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

import org.openntf.domino.Session;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

public class IDVault extends BaseThreadSafe<org.openntf.domino.IDVault, lotus.domino.IDVault, Session>
		implements org.openntf.domino.IDVault {

	protected IDVault(final lotus.domino.IDVault delegate, final Session parent) {
		super(delegate, parent, NOTES_IDVAULT);
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
	public org.openntf.domino.UserID getUserID(final String arg0, final String arg1) {
		try {
			return fromLotus(getDelegate().getUserID(arg0, arg1), UserID.SCHEMA, getAncestorSession());
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public org.openntf.domino.UserID getUserID(final String arg0, final String arg1, final String arg2) {
		try {
			return fromLotus(getDelegate().getUserID(arg0, arg1, arg2), UserID.SCHEMA, getAncestorSession());
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

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.domino.types.SessionDescendant#getAncestorSession()
	 */
	@Override
	public final Session getAncestorSession() {
		return parent;
	}

}