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
