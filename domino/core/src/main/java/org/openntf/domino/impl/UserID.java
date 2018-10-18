package org.openntf.domino.impl;

import java.util.Vector;

import org.openntf.domino.IDVault;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

public class UserID extends BaseThreadSafe<org.openntf.domino.UserID, lotus.domino.UserID, IDVault> implements org.openntf.domino.UserID {

	protected UserID(final lotus.domino.UserID delegate, final IDVault parent) {
		super(delegate, parent, NOTES_USERID);
	}

	@Override
	public Vector getEncryptionKeys() {
		try {
			return getDelegate().getEncryptionKeys();
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	public String getUserName() {
		try {
			return getDelegate().getUserName();
		} catch (Exception e) {
			DominoUtils.handleException(e);
		}
		return null;
	}

	@Override
	protected WrapperFactory getFactory() {
		return parent.getAncestorSession().getFactory();
	}

}