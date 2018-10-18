package org.openntf.domino.tests.rpr;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.openntf.domino.thread.PeriodicScheduler;

public class PeriodicSchedulerTest {

	@Test
	public void test() {
		PeriodicScheduler s = new PeriodicScheduler("period:90m 08:00-18:00 MTRF");
		System.out.println(s);
		fail("Not yet implemented");
	}

}
