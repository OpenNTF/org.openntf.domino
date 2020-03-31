/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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

import java.util.Vector;

import org.openntf.domino.IDVault;
import org.openntf.domino.WrapperFactory;
import org.openntf.domino.utils.DominoUtils;

public class UserID extends BaseThreadSafe<org.openntf.domino.UserID, lotus.domino.UserID, IDVault> implements org.openntf.domino.UserID {

	protected UserID(final lotus.domino.UserID delegate, final IDVault parent) {
		super(delegate, parent, NOTES_USERID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector<String> getEncryptionKeys() {
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