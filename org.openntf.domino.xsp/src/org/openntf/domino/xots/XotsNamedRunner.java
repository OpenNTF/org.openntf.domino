package org.openntf.domino.xots;

import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Session;
import org.openntf.domino.thread.model.IDominoRunnable;
import org.openntf.domino.utils.Factory;

import com.ibm.domino.napi.c.xsp.XSPNative;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.domino.xsp.module.nsf.SessionCloner;

public class XotsNamedRunner implements Runnable {
	private static final Logger log_ = Logger.getLogger(XotsNamedRunner.class.getName());
	protected final Runnable runnable_;
	protected ClassLoader classLoader_;
	protected NSFComponentModule module_;
	protected SessionCloner cloner_;
	private transient Session session_;

	public XotsNamedRunner(final Runnable runnable) {
		runnable_ = runnable;
		if (runnable instanceof XotsNamedRunner) {
			throw new IllegalArgumentException("Can't wrap a " + XotsNamedRunner.class.getName() + " in another "
					+ XotsNamedRunner.class.getName());
		}
		initModule();
	}

	public XotsNamedRunner(final Runnable runnable, final ClassLoader classLoader) {
		this(runnable);
		classLoader_ = classLoader;
	}

	public XotsNamedRunner(final Runnable runnable, final NSFComponentModule module) {
		runnable_ = runnable;
		if (runnable instanceof XotsNamedRunner) {
			throw new IllegalArgumentException("Can't wrap a " + XotsNamedRunner.class.getName() + " in another "
					+ XotsNamedRunner.class.getName());
		}
		module_ = module;
	}

	public XotsNamedRunner(final Runnable runnable, final NSFComponentModule module, final ClassLoader classLoader) {
		this(runnable, module);
		classLoader_ = classLoader;
	}

	private void initModule() {
		NotesContext ctx = NotesContext.getCurrent();
		if (ctx != null) {
			module_ = ctx.getRunningModule();
		} else {
			throw new IllegalArgumentException("Can't queue a " + XotsNamedRunner.class.getName() + " without a current NotesContext.");
		}
	}

	protected org.openntf.domino.Session getNamedSession() {
		lotus.domino.Session s = null;
		String name = null;
		if (runnable_ instanceof IDominoRunnable) {
			name = ((IDominoRunnable) runnable_).getRunAs();
		}
		if (null == name || name.length() < 1) {
			throw new IllegalStateException("Cannot execute a " + runnable_.getClass().getName()
					+ " as a Named session type that does not have a name set using .setRunAs()");
		}
		final String argname = name;
		try {
			s = AccessController.doPrivileged(new PrivilegedExceptionAction<lotus.domino.Session>() {
				@Override
				public lotus.domino.Session run() throws Exception {
					long hList = com.ibm.domino.napi.c.NotesUtil.createUserNameList(argname);
					return XSPNative.createXPageSession(argname, hList, false, true);
				}
			});
		} catch (Exception ne) {
			throw new RuntimeException(ne);
		}
		if (s instanceof lotus.domino.Session) {
			return Factory.fromLotus((lotus.domino.Session) s, org.openntf.domino.Session.SCHEMA, null);
		}
		return null;
	}

	public ClassLoader getClassLoader() {
		if (classLoader_ == null) {
			if (runnable_ instanceof IDominoRunnable) {
				ClassLoader loader = ((IDominoRunnable) runnable_).getContextClassLoader();
				if (loader != null)
					classLoader_ = loader;
			}
		}
		return classLoader_;
	}

	public void setSession(final lotus.domino.Session session) {
		session_ = Factory.fromLotus(session, org.openntf.domino.Session.SCHEMA, null);
		Factory.setSession(session);
	}

	public org.openntf.domino.Session getSession() {
		if (session_ == null) {
			setSession(getNamedSession());
		}
		return session_;
	}

	protected void preRun() {
		// lotus.domino.NotesThread.sinitThread();
		NotesContext nctx = new NotesContext(module_);
		NotesContext.initThread(nctx);

		Session session = this.getNamedSession();
		setSession(session);
	}

	protected void postRun() {
		lotus.domino.Session session = Factory.terminate();
		if (session != null) {
			try {
				session.recycle();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		NotesContext.termThread();
	}

	@Override
	public void run() {
		if (runnable_ != null) {
			try {
				preRun();
				if (!(runnable_ instanceof XotsNamedRunner)) {
					runnable_.run();
				} else {
					Throwable t = new Throwable();
					log_.log(Level.SEVERE,
							"Cannot run a " + XotsNamedRunner.class.getName() + " inside a " + XotsNamedRunner.class.getName() + "!", t);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				postRun();
			}
		}
	}
}
