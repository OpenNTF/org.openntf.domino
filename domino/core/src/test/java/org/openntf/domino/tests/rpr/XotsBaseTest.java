package org.openntf.domino.tests.rpr;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.junit.DominoJUnitRunner;
import org.openntf.domino.junit.SessionUser;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Tasklet;
import org.openntf.domino.xots.Xots;

@RunWith(DominoJUnitRunner.class)
public class XotsBaseTest {
	@Test
	@SessionUser("CN=Roland Praml/OU=01/OU=int/O=FOCONIS")
	public void testCallableClone() throws InterruptedException, ExecutionException {

		//Future<String> f = Xots.getService().submit(new SessionPassingCallable());
		//assertEquals("CN=Roland Praml/OU=01/OU=int/O=FOCONIS", f.get());

		List<Callable<String>> concats = new ArrayList<Callable<String>>();
		concats.add(new SessionPassingCallable());
		concats.add(new SessionPassingCallable());
		List<Future<String>> results;

		if (false) {
			// This does not work
			results = Xots.getService().invokeAll(concats);
		} else {
			// this works:
			results = new ArrayList<Future<String>>();
			for (Callable<String> callable : concats) {
				results.add(Xots.getService().submit(callable));
			}
		}

		assertEquals(2, results.size());
		for (Future f : results) {
			assertEquals("CN=Roland Praml/OU=01/OU=int/O=FOCONIS", f.get());
		}

		//		ArrayList<Callable<String>> concats = new ArrayList<Callable<String>>();
		//		for (int i = 0; i < 1; i++) {
		//			concats.add(new SessionPassingCallable());
		//		}
		//
		//		List<Future<String>> results = Xots.getService().invokeAll(concats);
		//		for (int i = 0; i < 1; i++) {
		//			assertEquals("CN=Roland Praml/OU=01/OU=int/O=FOCONIS", results.get(i).get());
		//		}
	}

	@Tasklet(session = Tasklet.Session.CLONE)
	private static class SessionPassingCallable implements Callable<String> {
		@Override
		public String call() {
			try {
				return Factory.getSession(SessionType.CURRENT).getEffectiveUserName();
			} catch (Throwable t) {
				t.printStackTrace();
				return t.getMessage();
			}
		}
	}

}
