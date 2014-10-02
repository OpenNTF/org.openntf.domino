package org.openntf.domino.thread;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Session;
import org.openntf.domino.thread.model.IDominoRunnable;
import org.openntf.domino.utils.Factory;

public class DominoNativeRunner /*extends AbstractDominoRunnable */implements Runnable {
	private static final Logger log_ = Logger.getLogger(DominoNativeRunner.class.getName());
	protected final Runnable runnable_;
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
		lotus.domino.Session s = null;
		try {
			s = lotus.domino.NotesFactory.createTrustedSession();
		} catch (Exception ne) {
			System.out.println("Unable to create a trusted session. Falling back to a session with full access...");
			try {
				s = lotus.domino.NotesFactory.createSessionWithFullAccess();
			} catch (Throwable t) {
				t.printStackTrace();
			}

		}
		if (s != null) {
			return Factory.fromLotus(s, org.openntf.domino.Session.SCHEMA, null);
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

	public Runnable getRunnable() {
		return runnable_;
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
		Session session = this.getNewFullAccessSession();
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
		} else {
			System.out.println("ALERT: session was null for a " + runnable_.getClass().getName());
		}

	}

	@Override
	public void run() {
		if (runnable_ != null) {
			try {
				preRun();
				if (!(runnable_ instanceof DominoNativeRunner)) {
					runnable_.run();
				} else {
					Throwable t = new Throwable();
					log_.log(Level.SEVERE, "Cannot run a DominoNativeRunner inside a DominoNativeRunner!", t);
				}
			} catch (Throwable t) {
				throw new RuntimeException(t);
			} finally {
				postRun();
			}
		}
	}
}
