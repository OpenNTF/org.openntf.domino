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
package org.openntf.domino.session;

import org.openntf.domino.Session;

public class SessionFullAccessFactory extends AbstractSessionFactory implements INamedSessionFactory {
	private static final long serialVersionUID = 1L;
	final private String runAs_;

	public SessionFullAccessFactory(final String apiPath, final String runAs) {
		super(apiPath);
		runAs_ = runAs;
	}

	public SessionFullAccessFactory(final String apiPath) {
		this(apiPath, null);
	}

	@Override
	public Session createSession() {
		return createSession(runAs_);
	}

	@Override
	@SuppressWarnings("nls")
	public Session createSession(final String userName) {
		if (userName != null) {
			//			Thread.currentThread().interrupt();
			throw new UnsupportedOperationException("SessionType FULL_ACCESS as user " + String.valueOf(userName)
					+ " is not (yet)supported in Domino environment: ");
		}
		lotus.domino.Session raw = LotusSessionFactory.createSessionWithFullAccess(userName);
		return wrapSession(raw, true);
	}

}
