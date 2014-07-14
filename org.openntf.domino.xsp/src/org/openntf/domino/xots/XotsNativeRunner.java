package org.openntf.domino.xots;

import org.openntf.domino.thread.DominoNativeRunner;

import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public class XotsNativeRunner extends DominoNativeRunner {
	protected NSFComponentModule module_;

	public XotsNativeRunner(final Runnable runnable) {
		super(runnable);
		if (runnable instanceof XotsNamedRunner) {
			throw new IllegalArgumentException("Can't wrap a " + XotsNativeRunner.class.getName() + " in another "
					+ XotsNativeRunner.class.getName());
		}
		initModule();
	}

	public XotsNativeRunner(final Runnable runnable, final ClassLoader classLoader) {
		super(runnable, classLoader);
	}

	public XotsNativeRunner(final Runnable runnable, final NSFComponentModule module) {
		super(runnable);
		if (runnable instanceof XotsNamedRunner) {
			throw new IllegalArgumentException("Can't wrap a " + XotsNativeRunner.class.getName() + " in another "
					+ XotsNativeRunner.class.getName());
		}
		module_ = module;
	}

	public XotsNativeRunner(final Runnable runnable, final NSFComponentModule module, final ClassLoader classLoader) {
		super(runnable, classLoader);
		module_ = module;
	}

	private void initModule() {
		NotesContext ctx = NotesContext.getCurrent();
		if (ctx != null) {
			module_ = ctx.getRunningModule();
		} else {
			throw new IllegalArgumentException("Can't queue a " + XotsNativeRunner.class.getName() + " without a current NotesContext.");
		}
	}

}
