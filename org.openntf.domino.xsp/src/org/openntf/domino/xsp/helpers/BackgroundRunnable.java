package org.openntf.domino.xsp.helpers;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import lotus.domino.NotesFactory;
import lotus.domino.Session;

import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.ibm.domino.napi.c.xsp.XSPNative;
import com.ibm.domino.xsp.module.nsf.NSFComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;

public abstract class BackgroundRunnable implements Runnable {
	public static class ThreadContext {
		private final String dbpath;
		private final NSFComponentModule module;
		private final String servername;
		private final String username;

		public ThreadContext(final String username, final String servername, final String dpath) {
			this.username = username;
			this.servername = servername;
			this.dbpath = dpath;
			NotesContext current = NotesContext.getCurrentUnchecked();
			if (current == null) {
				this.module = null;	// Ummmm... bad things are about to happen...
			} else {
				this.module = NotesContext.getCurrent().getModule();
			}
		}

		public ThreadContext(final String username, final String servername, final String dpath, final NotesContext sourceCtx) {
			this.username = username;
			this.servername = servername;
			this.dbpath = dpath;
			this.module = sourceCtx.getModule();
		}

		public lotus.domino.Database getContextDatabase(final lotus.domino.Session session) {
			try {
				session.getDatabase(this.servername, this.dbpath);
			} catch (Throwable t) {
				DominoUtils.handleException(t);
			}
			return null;
		}

		public lotus.domino.Session getNrpcSessionAsHost() {
			lotus.domino.Session result = null;
			try {
				result = AccessController.doPrivileged(new PrivilegedExceptionAction<lotus.domino.Session>() {
					@Override
					public Session run() throws Exception {
						return NotesFactory.createSessionWithFullAccess();
					}
				});
			} catch (PrivilegedActionException e) {
				DominoUtils.handleException(e);
			}
			return result;
		}

		public lotus.domino.Session getTrustedSession() {
			lotus.domino.Session result = null;
			try {
				result = AccessController.doPrivileged(new PrivilegedExceptionAction<lotus.domino.Session>() {
					@Override
					public Session run() throws Exception {
						return NotesFactory.createTrustedSession();
					}
				});
			} catch (PrivilegedActionException e) {
				DominoUtils.handleException(e);
			}
			return result;
		}

		public lotus.domino.Session getXspSessionAsUser() {
			final NSFComponentModule mod = this.module;
			lotus.domino.Session result = null;
			try {
				result = AccessController.doPrivileged(new PrivilegedExceptionAction<lotus.domino.Session>() {
					@Override
					public Session run() throws Exception {
						NotesContext nc = new NotesContext(mod);
						NotesContext.initThread(nc);
						long hList = com.ibm.domino.napi.c.NotesUtil.createUserNameList(username);
						return XSPNative.createXPageSession(username, hList, true, false);
					}
				});
			} catch (PrivilegedActionException e) {
				DominoUtils.handleException(e);
			}
			return result;
		}

		public void initClassLoader(final Thread t) {
			if (this.module != null) {
				t.setContextClassLoader(this.module.getModuleClassLoader());
			}
		}

		public boolean isXspContext() {
			return null != module;
		}
	}

	private static ThreadLocal<lotus.domino.Session> localSession_ = new ThreadLocal<lotus.domino.Session>() {

	};
	private ThreadContext threadContext_;
	private boolean useOpenntf_ = true;

	public BackgroundRunnable(final ThreadContext threadContext) {
		setThreadContext(threadContext);
	}

	public BackgroundRunnable(final ThreadContext threadContext, final boolean useOpenntf) {
		setThreadContext(threadContext);
		useOpenntf_ = useOpenntf;
	}

	protected lotus.domino.Session getSession() {
		return BackgroundRunnable.localSession_.get();
	}

	private ThreadContext getThreadContext() {
		return threadContext_;
	}

	private void initSession(final ThreadContext tc) {
		lotus.domino.Session delegate = null;
		if (tc.isXspContext()) {
			delegate = tc.getXspSessionAsUser();
		} else {
			delegate = tc.getNrpcSessionAsHost();
		}
		if (useOpenntf_) {
			BackgroundRunnable.localSession_
					.set((lotus.domino.Session) Factory.fromLotus(delegate, org.openntf.domino.Session.class, null));
		} else {
			BackgroundRunnable.localSession_.set(delegate);
		}
	}

	@Override
	public void run() {
		ThreadContext tc = getThreadContext();
		tc.initClassLoader(Thread.currentThread());
		try {
			initSession(tc);
			if (tc.isXspContext()) {
				runXsp();
			} else {
				runNrpc();
			}
		} catch (Throwable t) {
			DominoUtils.handleException(t);
		} finally {
			termSession();
		}

	}

	protected abstract void runNrpc();

	protected abstract void runXsp();

	private void setThreadContext(final ThreadContext ctx) {
		threadContext_ = ctx;
	}

	private void termSession() {
		lotus.domino.Session s = BackgroundRunnable.localSession_.get();
		if (null != s) {
			try {
				s.recycle();
			} catch (Exception e) {
				DominoUtils.handleException(e);
			}
			BackgroundRunnable.localSession_.set(null);
		}
	}

}
