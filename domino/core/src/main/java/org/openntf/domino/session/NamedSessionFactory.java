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
package org.openntf.domino.session;

import org.openntf.domino.Session;

public class NamedSessionFactory extends AbstractSessionFactory implements INamedSessionFactory {
	private static final long serialVersionUID = 1L;
	final private String runAs_;

	public NamedSessionFactory(final String apiPath) {
		super(apiPath);
		runAs_ = null;
	}

	public NamedSessionFactory(final String apiPath, final String runAs) {
		super(apiPath);
		runAs_ = runAs;
	}


	@Override
	public Session createSession() {
		return createSession(runAs_);
	}

	@Override
	public Session createSession(final String userName) {
		if (userName == null)
			throw new NullPointerException();
		lotus.domino.Session raw = LotusSessionFactory.createSessionWithTokenEx(userName);
		return wrapSession(raw, true);
	}
}
