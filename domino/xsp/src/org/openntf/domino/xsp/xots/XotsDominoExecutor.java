package org.openntf.domino.xsp.xots;

import java.util.concurrent.Callable;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.thread.AbstractDominoRunner;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.thread.DominoRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XotsDominoExecutor extends DominoExecutor {

	public XotsDominoExecutor(final int corePoolSize) {
		super(corePoolSize);
	}

	@Override
	protected Runnable wrap(final Runnable runnable) {
		if (runnable instanceof AbstractDominoRunner) {
			return runnable;
		}
		NotesContext ctx = NotesContext.getCurrentUnchecked();
		NSFComponentModule module = ctx == null ? null : ctx.getModule();

		if (module == null) {
			ISessionFactory sf = Factory.getSessionFactory(SessionType.DEFAULT);
			return new DominoRunner(runnable, sf);
		} else {
			// TODO we must clone the session here
			ISessionFactory sf = Factory.getSessionFactory(SessionType.DEFAULT);
			return new XotsDominoRunner(module, runnable, sf);
		}
	}

	@Override
	protected <T> Callable<T> wrap(final Callable<T> callable) {
		if (callable instanceof AbstractDominoRunner.WrappedCallable) {
			return callable;
		}
		NotesContext ctx = NotesContext.getCurrentUnchecked();
		NSFComponentModule module = ctx == null ? null : ctx.getModule();

		if (module == null) {
			ISessionFactory sf = Factory.getSessionFactory(SessionType.DEFAULT);
			return new DominoRunner(callable, sf).asCallable(callable);
		} else {
			// TODO we must clone the session here
			ISessionFactory sf = Factory.getSessionFactory(SessionType.DEFAULT);
			return new XotsDominoRunner(module, callable, sf).asCallable(callable);
		}
	}

}
