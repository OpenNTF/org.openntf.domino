package org.openntf.domino.tests.general;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openntf.domino.tests.AllTests;

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
}