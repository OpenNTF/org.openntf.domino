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

public class NativeSessionFactory extends AbstractSessionFactory {
	private static final long serialVersionUID = 1L;

	public NativeSessionFactory(final String apiPath) {
		super(apiPath);
	}

	@Override
	public Session createSession() {
		lotus.domino.Session raw = LotusSessionFactory.createSession();
		return wrapSession(raw, true);
	}
}
