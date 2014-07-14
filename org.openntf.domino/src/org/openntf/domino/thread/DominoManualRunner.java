package org.openntf.domino.thread;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Session;
import org.openntf.domino.thread.model.IDominoRunnable;
import org.openntf.domino.utils.Factory;

public class DominoManualRunner /*extends AbstractDominoRunnable */implements Runnable {
	private static final Logger log_ = Logger.getLogger(DominoManualRunner.class.getName());
	private final Runnable runnable_;
	private ClassLoader classLoader_;
	private transient Session session_;

	public DominoManualRunner(final Runnable runnable) {
		runnable_ = runnable;
		if (runnable instanceof DominoManualRunner) {
			throw new IllegalArgumentException("Can't wrap a " + DominoManualRunner.class.getName() + " in another "
					+ DominoManualRunner.class.getName());
		}
	}

	public DominoManualRunner(final Runnable runnable, final ClassLoader classLoader) {
		runnable_ = runnable;
		classLoader_ = classLoader;
		if (runnable instanceof DominoManualRunner) {
			throw new IllegalArgumentException("Can't wrap a " + DominoManualRunner.class.getName() + " in another "
					+ DominoManualRunner.class.getName());
		}
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
		return session_;
	}

	protected void preRun() {
		if (getSession() == null) {
			throw new IllegalStateException("Cannot run a " + DominoManualRunner.class.getName()
					+ " without first calling setSession() to define the session.");
		}
		lotus.domino.NotesThread.sinitThread();
	}

	protected void postRun() {
		lotus.domino.Session session = Factory.terminate();
		lotus.domino.NotesThread.stermThread();
	}

	@Override
	public void run() {
		if (runnable_ != null) {
			try {
				preRun();
				if (!(runnable_ instanceof DominoManualRunner)) {
					runnable_.run();
				} else {
					Throwable t = new Throwable();
					log_.log(Level.SEVERE,
							"Cannot run a " + DominoManualRunner.class.getName() + " in another " + DominoManualRunner.class.getName(), t);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				postRun();
			}
		}
	}
}
