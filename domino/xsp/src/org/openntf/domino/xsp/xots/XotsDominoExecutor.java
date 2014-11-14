package org.openntf.domino.xsp.xots;

import java.util.concurrent.Callable;

import org.openntf.domino.thread.AbstractDominoRunner;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.thread.DominoRunner;

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
			return new DominoRunner(runnable);
		} else {
			return new XotsDominoRunner(module, runnable);
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
			return new DominoRunner(callable).asCallable(callable);
		} else {
			return new XotsDominoRunner(module, callable).asCallable(callable);
		}
	}

}
