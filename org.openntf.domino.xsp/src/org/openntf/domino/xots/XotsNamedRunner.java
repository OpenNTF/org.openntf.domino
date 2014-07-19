package org.openntf.domino.xots;

import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.notes.NotesThread;

import org.openntf.domino.Session;
import org.openntf.domino.thread.model.IDominoRunnable;
import org.openntf.domino.utils.Factory;

import com.ibm.domino.napi.c.xsp.XSPNative;
import com.ibm.domino.xsp.module.nsf.ModuleClassLoader;
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
			throw new IllegalArgumentException("Can't wrap a " + runnable.getClass().getName() + " in another "
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
			throw new IllegalArgumentException("Can't wrap a " + runnable.getClass().getName() + " in another "
					+ XotsNamedRunner.class.getName());
		}
		module_ = module;
	}

	public XotsNamedRunner(final Runnable runnable, final NSFComponentModule module, final ClassLoader classLoader) {
		this(runnable, module);
		classLoader_ = classLoader;
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

		return Factory.fromLotus(s, org.openntf.domino.Session.SCHEMA, null);
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
		if (module_ != null) {
			NotesContext nctx = new NotesContext(module_);
			NotesContext.initThread(nctx);
		} else {
			NotesThread.sinitThread();
		}
		Session session = this.getNamedSession();
		setSession(session);
	}

	protected void postRun() {
		lotus.domino.Session session = Factory.terminate();
		if (session != null) {
			System.out.println("DEBUG: recycling a Session with object id: " + System.identityHashCode(session) + " after running a "
					+ runnable_.getClass().getName());
			try {
				session.recycle();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} else {
			System.out.println("ALERt: session was null for a " + runnable_.getClass().getName());
		}
		if (module_ != null) {
			NotesContext.termThread();
		} else {
			NotesThread.stermThread();
		}
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
