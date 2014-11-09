package org.openntf.domino.xots;

import java.util.concurrent.Callable;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.thread.DominoRunner;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionMode;

import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XotsDominoExecutor extends DominoExecutor {

	public static class XotsDominoRunner extends DominoRunner {
		protected XotsDominoRunner(final Runnable runnable, final ISessionFactory sessionFactory) {
			super(runnable, sessionFactory);
		}

		protected <T> XotsDominoRunner(final Callable<T> callable, final ISessionFactory sessionFactory) {
			super(callable, sessionFactory);
		}

		public transient NSFComponentModule module;
		private String databasePath;
		private String templatePath;

		protected XotsDominoRunner(final Runnable runnable) {
			super(runnable);
		}

		@Override
		protected void init(final Object runnable) {
			super.init(runnable);

			if (runnable instanceof IRunsInModule) {
				module = ((IRunsInModule) runnable).getRunModule();
			} else {
				NotesContext ctx = NotesContext.getCurrentUnchecked();
				if (ctx != null) {
					module = ctx.getModule();
				}
			}
			if (module != null) {
				databasePath = module.getDatabasePath();
				templatePath = module.getTemplateDatabasePath();
			}
		}

		@Override
		public void runNotes() {
			if (module == null) {
				super.runNotes();
			} else {
				if (NotesContext.getCurrentUnchecked() != null) {
					throw new IllegalStateException("There is already a NotesContext open");
				}

				if (module.isDestroyed()) {
					throw new IllegalStateException("The module is destroyed");
				}
				module.updateLastModuleAccess();

				NotesContext ctx = new NotesContext(module);
				NotesContext.initThread(ctx);
				try {
					super.runNotes();
				} finally {
					NotesContext.termThread();
				}
			}
		}

		@Override
		public ClassLoader getContextClassLoader() {
			// TODO Auto-generated method stub
			if (module == null) {
				return super.getContextClassLoader();
			} else {
				return module.getModuleClassLoader();
			}
		}

	}

	protected XotsDominoExecutor(final int corePoolSize) {
		super(corePoolSize);
	}

	@Override
	protected XotsDominoRunner wrap(final Runnable runnable) {
		if (runnable instanceof XotsDominoRunner) {
			return (XotsDominoRunner) runnable;
		}
		return new XotsDominoRunner(runnable, Factory.getSessionFactory(SessionMode.DEFAULT));
	}

	@Override
	protected <T> Callable<T> wrap(final Callable<T> callable) {
		if (callable instanceof DominoRunner) {
			return callable;
		}
		return new XotsDominoRunner(callable, Factory.getSessionFactory(SessionMode.DEFAULT)).asCallable(callable);
	}

}
