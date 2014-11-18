package org.openntf.domino.xsp.tests.rpr;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import lotus.domino.NotesException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openntf.domino.junit.SessionDb;
import org.openntf.domino.junit.SessionUser;
import org.openntf.domino.thread.model.XotsSessionType;
import org.openntf.domino.thread.model.XotsTasklet;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.XotsDaemon;
import org.openntf.domino.xsp.junit.ModuleJUnitRunner;
import org.openntf.junit4xpages.OsgiTest;

import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

@OsgiTest
@SessionDb("entwicklung/jfof4/proglibjfof.nsf")
@RunWith(ModuleJUnitRunner.class)
public class XOTSTest {

	@Test
	public void testCallable() throws InterruptedException, ExecutionException {
		Future<String> future = XotsDaemon.queue(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return "SUCCESS";
			}
		});

		assertEquals("SUCCESS", future.get());
	}

	@Test
	public void testModule() throws InterruptedException, ExecutionException {

		Future<NSFComponentModule> future = XotsDaemon.queue(new Callable<NSFComponentModule>() {
			@Override
			public NSFComponentModule call() throws Exception {
				return NotesContext.getCurrent().getModule();
			}
		});

		assertEquals("entwicklung/jfof4/proglibjfof.nsf", future.get().getDatabasePath());
	}

	@XotsTasklet(session = XotsSessionType.CLONE)
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

	@Test
	@SessionUser("CN=Roland Praml/OU=01/OU=int/O=FOCONIS")
	public void testSessionPassing() throws InterruptedException, ExecutionException, NotesException {

		assertEquals("CN=Roland Praml/OU=01/OU=int/O=FOCONIS", Factory.getSession(SessionType.CURRENT).getEffectiveUserName());
		Future<String> future = XotsDaemon.queue(new SessionPassingCallable());

		assertEquals("CN=Roland Praml/OU=01/OU=int/O=FOCONIS", future.get());
	}

	@Test
	public void testLongRunningRunnable() throws InterruptedException {
		XotsDaemon.queue(new Callable<String>() {

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

		XotsDaemon.queue(new Runnable() {

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
		System.out.println("Tasklist 1: " + XotsDaemon.getRunningTasks());
		Thread.sleep(1000);
		System.out.println("Tasklist 2: " + XotsDaemon.getRunningTasks());
		Thread.sleep(1000);
		System.out.println("Tasklist 3: " + XotsDaemon.getRunningTasks());
		Thread.sleep(1000);
		System.out.println("Tasklist 4: " + XotsDaemon.getRunningTasks());
		Thread.sleep(1000);
		System.out.println("Tasklist 5: " + XotsDaemon.getRunningTasks());
		Thread.sleep(1000);
		System.out.println("Tasklist 6: " + XotsDaemon.getRunningTasks());
	}
}
