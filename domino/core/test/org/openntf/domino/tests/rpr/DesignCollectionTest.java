package org.openntf.domino.tests.rpr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.Database;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

@RunWith(DominoJUnitRunner.class)
public class DesignCollectionTest {

	@Test
	public void testDesignCollection() {
		Database db = Factory.getSession(SessionType.CURRENT).getDatabase("fakenames.nsf");
		long start = System.currentTimeMillis();
		db.getDesign().isAPIEnabled();
		start = System.currentTimeMillis() - start;
		System.out.println(start);
	}
}
