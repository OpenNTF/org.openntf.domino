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
import static org.junit.Assert.assertNotEquals;

import org.eclipse.core.runtime.Platform;
import org.junit.Test;
import org.openntf.domino.utils.Factory;
import org.osgi.framework.Bundle;

public class TestFactory {
	@Test
	public void testVersionInfo() {
		Bundle odaBundle = Platform.getBundle("org.openntf.domino");
		String version = odaBundle.getVersion().toString();
		assertEquals("Version should match OSGi's info", version, Factory.getVersion());
	}
	
	@Test
	public void testName() {
		Bundle odaBundle = Platform.getBundle("org.openntf.domino");
		String name = odaBundle.getHeaders().get("Bundle-Name");
		assertEquals("Name should match OSGi's info", name, Factory.getTitle());
	}
	
	/**
	 * Test for https://github.com/OpenNTF/org.openntf.domino/issues/58
	 */
	@Test
	public void testGetSession() {
		assertNotEquals("First session should not be null", null, Factory.getSession());
		assertNotEquals("Second session should not be null", null, Factory.getSession());
	}
}
