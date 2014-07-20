package org.openntf.domino.xots;

import org.openntf.domino.thread.AbstractDominoRunnable;
import org.openntf.domino.thread.DominoNativeRunner;

import com.ibm.domino.xsp.module.nsf.ModuleClassLoader;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XotsNativeRunner extends DominoNativeRunner {
	protected NSFComponentModule module_;

	public XotsNativeRunner(final Runnable runnable) {
		super(runnable);
		if (runnable instanceof DominoNativeRunner) {
			throw new IllegalArgumentException("Can't wrap a " + runnable.getClass().getName() + " in another "
					+ DominoNativeRunner.class.getName());
		}
		initModule();
	}

	public XotsNativeRunner(final Runnable runnable, final ClassLoader classLoader) {
		super(runnable, classLoader);
		initModule();
	}

	public XotsNativeRunner(final Runnable runnable, final NSFComponentModule module) {
		super(runnable);
		if (runnable instanceof DominoNativeRunner) {
			throw new IllegalArgumentException("Can't wrap a " + runnable.getClass().getName() + " in another "
					+ DominoNativeRunner.class.getName());
		}
		module_ = module;
	}

	public XotsNativeRunner(final Runnable runnable, final NSFComponentModule module, final ClassLoader classLoader) {
		super(runnable, classLoader);
		module_ = module;
	}

	private void initModule() {
		NotesContext ctx = NotesContext.getCurrentUnchecked();
		if (ctx != null) {
			module_ = ctx.getRunningModule();
		} else {
			if (classLoader_ == null) {
				classLoader_ = XotsNativeRunner.class.getClassLoader();
			} else if (classLoader_ instanceof ModuleClassLoader) {
				throw new IllegalArgumentException("Can't queue a " + XotsNativeRunner.class.getName() + " without a current NotesContext.");
			} else {
				classLoader_ = XotsNativeRunner.class.getClassLoader();
			}
		}
	}

	@Override
	protected void preRun() {
		ClassLoader cl = ((AbstractDominoRunnable) getRunnable()).getContextClassLoader();
		if (cl == null) {
			cl = classLoader_;
		}
		System.out.println("DEBUG: " + getClass().getName() + " is starting a run for a " + getRunnable().getClass().getName()
				+ " from a loader of " + cl.getClass().getName());
		if (module_ != null) {
			NotesContext nctx = new NotesContext(module_);
			NotesContext.initThread(nctx);
		} else {
			System.out.println("DEBUG: skipping thread initialization.");
			// NotesThread.sinitThread();
		}
		super.preRun();
	}

	@Override
	protected void postRun() {
		super.postRun();
		if (module_ != null) {
			// System.out.println("DEBUG: Starting termination of NotesContext thread...");
			// NotesContext.termThread();
			// System.out.println("DEBUG: Completed termination of NotesContext thread");
		} else {
			// NotesThread.stermThread();
		}
		System.out.println("DEBUG: " + getClass().getName() + " completed a run for a " + getRunnable().getClass().getName());

	}

}
