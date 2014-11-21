package org.openntf.domino.xsp.junit.test;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lotus.domino.NotesException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.junit.SessionDb;
import org.openntf.domino.junit.SessionUser;
import org.openntf.domino.thread.AbstractDominoExecutor.DominoFutureTask;
import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Tasklet;
import org.openntf.domino.xots.Xots;
import org.openntf.domino.xsp.ODAPlatform;
import org.openntf.domino.xsp.junit.ModuleJUnitRunner;
import org.openntf.junit4xpages.OsgiTest;

import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

@OsgiTest
@SessionDb("entwicklung/jfof4/proglibjfof.nsf")
@RunWith(ModuleJUnitRunner.class)
public class XOTSTest {

	//@Test
	public void testCallable() throws InterruptedException, ExecutionException {
		Future<String> future = Xots.getService().submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return "SUCCESS";
			}
		});

		assertEquals("SUCCESS", future.get());
	}

	//@Test
	public void testModule() throws InterruptedException, ExecutionException {

		Future<NSFComponentModule> future = Xots.getService().submit(new Callable<NSFComponentModule>() {
			@Override
			public NSFComponentModule call() throws Exception {
				return NotesContext.getCurrent().getModule();
			}
		});

		assertEquals("entwicklung/jfof4/proglibjfof.nsf", future.get().getDatabasePath());
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

	//@Test
	@SessionUser("CN=Roland Praml/OU=01/OU=int/O=FOCONIS")
	public void testSessionPassing() throws InterruptedException, ExecutionException, NotesException {

		assertEquals("CN=Roland Praml/OU=01/OU=int/O=FOCONIS", Factory.getSession(SessionType.CURRENT).getEffectiveUserName());
		Future<String> future = Xots.getService().submit(new SessionPassingCallable());

		assertEquals("CN=Roland Praml/OU=01/OU=int/O=FOCONIS", future.get());
	}

	//@Test
	public void testLongRunningRunnable() throws InterruptedException {
		Xots.getService().submit(new Callable<String>() {

			@Override
			public String call() {
				// TODO Auto-generated method stub
				System.out.println("Long callable started");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Long callable finished");
				return "DONE";
			}
		});

		Xots.getService().submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Long runnable started");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Long runnable finished");
			}
		});
		System.out.println("Tasklist 1: " + Xots.getTasks(null));
		Thread.sleep(1000);
		System.out.println("Tasklist 2: " + Xots.getTasks(null));
		Thread.sleep(1000);
		System.out.println("Tasklist 3: " + Xots.getTasks(null));
		Thread.sleep(1000);
		System.out.println("Tasklist 4: " + Xots.getTasks(null));
		Thread.sleep(1000);
		System.out.println("Tasklist 5: " + Xots.getTasks(null));
		Thread.sleep(1000);
		System.out.println("Tasklist 6: " + Xots.getTasks(null));
	}

	private static class HighLoadTest implements Runnable {
		@SuppressWarnings("unused")
		private int i;

		public HighLoadTest(final int i) {
			this.i = i;
		}

		@Override
		public void run() {
			//System.out.println("Thread " + i);

		}

	}

	//@Test
	public void testHighLoad() throws InterruptedException {
		for (int i = 0; i < 1000; i++) {
			Xots.getService().submit(new HighLoadTest(i));
		}
		Collection<DominoFutureTask<?>> lst1 = null;
		do {
			lst1 = Xots.getTasks(null);
			System.out.println("--- TASK LIST ---");
			for (DominoFutureTask<?> task : lst1) {
				System.out.println(task);
			}

			Thread.sleep(1000);
		} while (!lst1.isEmpty());

	}

	@Test
	public void testDelayed() throws InterruptedException {
		for (int i = 0; i < 5; i++) {
			Xots.getService().schedule(new HighLoadTest(i), 5 * i, TimeUnit.SECONDS);
		}
		Collection<DominoFutureTask<?>> lst1 = null;
		do {
			lst1 = Xots.getTasks(null);
			System.out.println("--- TASK LIST ---");
			for (DominoFutureTask<?> task : lst1) {
				System.out.println(task);
			}

			Thread.sleep(1000);
		} while (!lst1.isEmpty());

	}

	public static class EndlessTest extends AbstractDominoRunnable {
		private static final long serialVersionUID = 1L;

		@Override
		public void run() {
			try {
				for (;;) {
					System.out.println("Running...");
					Thread.sleep(1000);
					if (shouldStop())
						break;
				}
			} catch (InterruptedException ie) {
				System.out.println("Terminated");
			}

		}

	}

	//@Test
	public void testEndless() throws InterruptedException {
		Xots.getService().submit(new EndlessTest());
		Thread.sleep(1000);
		ODAPlatform.stop();
		ODAPlatform.start();
	}

}
