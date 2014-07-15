package org.openntf.domino.xots;

import lotus.notes.NotesThread;

import org.openntf.domino.thread.DominoNativeRunner;

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
			throw new IllegalArgumentException("Can't queue a " + XotsNativeRunner.class.getName() + " without a current NotesContext.");
		}
	}

	@Override
	protected void preRun() {
		if (module_ != null) {
			NotesContext nctx = new NotesContext(module_);
			NotesContext.initThread(nctx);
		} else {
			NotesThread.sinitThread();
		}
		super.preRun();
	}

	@Override
	protected void postRun() {
		super.postRun();
		if (module_ != null) {
			NotesContext.termThread();
		} else {
			NotesThread.stermThread();
		}
	}

}
