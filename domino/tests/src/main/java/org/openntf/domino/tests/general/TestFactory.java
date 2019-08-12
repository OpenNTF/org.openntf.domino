package org.openntf.domino.tests.general;

import static org.junit.Assert.assertEquals;

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
}
