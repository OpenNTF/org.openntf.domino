package org.openntf.domino.thread;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Session;
import org.openntf.domino.thread.model.IDominoRunnable;
import org.openntf.domino.utils.Factory;

public class DominoNativeRunner /*extends AbstractDominoRunnable */implements Runnable {
	private static final Logger log_ = Logger.getLogger(DominoNativeRunner.class.getName());
	private final Runnable runnable_;
	protected ClassLoader classLoader_;
	private transient Session session_;

	public DominoNativeRunner(final Runnable runnable) {
		runnable_ = runnable;
		if (runnable instanceof DominoNativeRunner) {
			throw new IllegalArgumentException("Can't wrap a DominoNativeRunner in another DominoNativeRunner");
		}
	}

	public DominoNativeRunner(final Runnable runnable, final ClassLoader classLoader) {
		runnable_ = runnable;
		classLoader_ = classLoader;
		if (runnable instanceof DominoNativeRunner) {
			throw new IllegalArgumentException("Can't wrap a DominoNativeRunner in another DominoNativeRunner");
		}
	}

	protected org.openntf.domino.Session getNewFullAccessSession() {
		/*try {*/
		//			Object result = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
		//				@Override
		//				public Object run() throws Exception {
		//					lotus.domino.Session s = null;
		//					try {
		//						s = lotus.domino.NotesFactory.createTrustedSession();
		//						return s;
		//					} catch (Exception ne) {
		//						System.out.println("Unable to create a trusted session. Falling back to a session with full access...");
		//						s = lotus.domino.NotesFactory.createSessionWithFullAccess();
		//						return s;
		//					}
		//				}
		//			});
		//		SecurityManager sm = System.getSecurityManager();
		//		if (sm instanceof AgentSecurityManager) {
		//			AgentSecurityManager asm = (AgentSecurityManager) sm;
		//		} else {
		//			System.out.println("Security manager is a " + (sm == null ? "null" : sm.getClass().getName()));
		//		}

		lotus.domino.Session s = null;
		try {
			s = lotus.domino.NotesFactory.createTrustedSession();
			//				return s;
		} catch (Exception ne) {
			System.out.println("Unable to create a trusted session. Falling back to a session with full access...");
			try {
				s = lotus.domino.NotesFactory.createSessionWithFullAccess();
			} catch (Throwable t) {
				t.printStackTrace();
			}
			//				return s;
		}
		if (s instanceof lotus.domino.Session) {
			return Factory.fromLotus((lotus.domino.Session) s, org.openntf.domino.Session.SCHEMA, null);
		}
		/*} catch (PrivilegedActionException e) {
			DominoUtils.handleException(e);
			e.printStackTrace();
		}*/
		//		System.out.println("Uh oh! Returning null session!");
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
		//		System.out.println("Set Factory's thread session to " + session_.hashCode());
	}

	public org.openntf.domino.Session getSession() {
		if (session_ == null) {
			setSession(getNewFullAccessSession());
		}
		return session_;
	}

	protected void preRun() {
		lotus.domino.NotesThread.sinitThread();
		//		System.out.println("Thread init'ed");
		Session session = this.getNewFullAccessSession();
		//		System.out.println("Got a session object of " + (null == session ? "null" : session.getClass().getName()));
		setSession(session);
		//		if (runnable_ instanceof IDominoRunnable) {
		//			ClassLoader loader = ((IDominoRunnable) runnable_).getContextClassLoader();
		//			if (loader != null) {
		//				Factory.setClassLoader(loader);
		//				Thread.currentThread().setContextClassLoader(loader);
		//			}
		//		}
	}

	protected void postRun() {
		lotus.domino.Session session = Factory.terminate();
		if (session != null) {
			try {
				session.recycle();
				//				System.out.println("Recycled a native domino session in thread " + Thread.currentThread().getId());
				lotus.domino.NotesThread.stermThread();
				//				System.out.println("Thread term'ed");
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		if (runnable_ != null) {
			try {
				//				System.out.println("Starting DominoNativeRunner with a security context "
				//						+ AccessController.getContext().getClass().getName());
				preRun();
				if (!(runnable_ instanceof DominoNativeRunner)) {
					runnable_.run();
				} else {
					Throwable t = new Throwable();
					log_.log(Level.SEVERE, "Cannot run a DominoNativeRunner inside a DominoNativeRunner!", t);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				postRun();
			}
		}
	}
}
