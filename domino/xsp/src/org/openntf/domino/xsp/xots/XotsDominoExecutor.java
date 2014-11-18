package org.openntf.domino.xsp.xots;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

import org.openntf.domino.thread.DominoExecutor;

import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XotsDominoExecutor extends DominoExecutor {

	public XotsDominoExecutor(final int corePoolSize) {
		super(corePoolSize);
	}

	@Override
	protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
		NotesContext ctx = NotesContext.getCurrentUnchecked();
		NSFComponentModule module = ctx == null ? null : ctx.getModule();

		if (module == null) {
			return super.newTaskFor(callable);
		} else {
			return new XotsDominoFutureTask(module, callable);
		}

	}

	@Override
	protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T result) {
		NotesContext ctx = NotesContext.getCurrentUnchecked();
		NSFComponentModule module = ctx == null ? null : ctx.getModule();

		if (module == null) {
			return super.newTaskFor(runnable, result);
		} else {
			return new XotsDominoFutureTask(module, runnable, result);
		}
	}

}
