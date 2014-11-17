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
		public String call() throws Exception {
			NotesContext ctx = NotesContext.getCurrent();
			return ctx.getCurrentSession().getEffectiveUserName();
		}

	}

	@Test
	@SessionUser("CN=Roland Praml/OU=01/OU=int/O=FOCONIS")
	public void testSessionPassing() throws InterruptedException, ExecutionException, NotesException {
		NotesContext ctx = NotesContext.getCurrent();

		assertEquals("CN=Roland Praml/OU=01/OU=int/O=FOCONIS", ctx.getCurrentSession().getEffectiveUserName());
		Future<String> future = XotsDaemon.queue(new SessionPassingCallable());

		assertEquals("CN=Roland Praml/OU=01/OU=int/O=FOCONIS", future.get());
	}
}
