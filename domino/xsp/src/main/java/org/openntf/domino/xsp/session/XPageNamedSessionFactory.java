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
package org.openntf.domino.xsp.session;

import org.openntf.domino.Session;
import org.openntf.domino.session.INamedSessionFactory;
import org.openntf.domino.utils.DominoUtils;

import com.ibm.domino.napi.c.xsp.XSPNative;

public class XPageNamedSessionFactory extends AbstractXPageSessionFactory implements INamedSessionFactory {
	private static final long serialVersionUID = 1L;

	private boolean fullAccess_;
	private String runAs_;

	public XPageNamedSessionFactory(final String runAs, final boolean fullAccess) {
		super();
		fullAccess_ = fullAccess;
		runAs_ = runAs;
	}

	public XPageNamedSessionFactory(final boolean fullAccess) {
		super();
		fullAccess_ = fullAccess;
	}

	@Override
	public Session createSession() {
		if (runAs_ == null)
			throw new NullPointerException();
		return createSession(runAs_);
	}

	@Override
	public Session createSession(final String userName) {
		try {
			final long userHandle = createUserNameList(userName);
			lotus.domino.Session rawSession = XSPNative.createXPageSessionExt(userName, userHandle, false, true, fullAccess_);
			return wrapSession(rawSession, true);
		} catch (Exception e) {
			DominoUtils.handleException(e);
			return null;
		}
	}

}
