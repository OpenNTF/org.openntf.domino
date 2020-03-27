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
package org.openntf.domino.tests.general;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openntf.domino.Name;
import org.openntf.domino.tests.AllTests;
import org.openntf.domino.utils.Factory;

import lotus.domino.NotesException;

public class TestName {
	/**
	 * Test for GitHub issue #166, "Name.getCanonical() does not format correct"
	 * @see <a href="https://github.com/OpenNTF/org.openntf.domino/issues/166">https://github.com/OpenNTF/org.openntf.domino/issues/166</a>
	 */
	@Test
	public void testMultipartName() throws NotesException {
		String adName = "CN=Name/OU=Org1/OU=Org2/DC=dc1/DC=dc2/DC=country";
		
		String odaName = AllTests.session.createName(adName).getCanonical();
		String lotusName = AllTests.lotusSession.createName(adName).getCanonical();
		assertEquals("ODA name should match Lotus name", lotusName, odaName);
	}
	
	/**
	 * Tests for https://github.com/OpenNTF/org.openntf.domino/issues/146 , where this
	 * method throws a NullPointerException.
	 */
	@Test
	public void testEmptyName() {
		Name n = Factory.getSession().createName("");
		n.getAddr821();
	}
}