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
package org.openntf.domino.tests.wrapper;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.tests.AllTests;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

public class TestWrapperFactory {

	@Test
	public void testWrapSession() throws Exception {
		AllTests.session = Factory.getWrapperFactory().fromLotus(AllTests.lotusSession, Session.SCHEMA, null);
		assertNotEquals("Wrapped session should not be null", null, AllTests.session);
		System.out.println("Path is " + AllTests.EMPTY_DB);
		Database db = AllTests.session.getDatabase(AllTests.EMPTY_DB);
		assertNotNull(db);
		
		Factory.setSessionFactory(() -> AllTests.session, SessionType.CURRENT);
	}
}
