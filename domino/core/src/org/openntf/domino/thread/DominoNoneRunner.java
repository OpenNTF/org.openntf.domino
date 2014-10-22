package org.openntf.domino.thread;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.thread.model.IDominoRunnable;

public class DominoNoneRunner implements Runnable {
	private static final Logger log_ = Logger.getLogger(DominoNoneRunner.class.getName());
	private final Runnable runnable_;
	private ClassLoader classLoader_;

	public DominoNoneRunner(final Runnable runnable) {
		runnable_ = runnable;
		if (runnable instanceof DominoNoneRunner) {
			throw new IllegalArgumentException("Can't wrap a " + runnable.getClass().getName() + " in another "
					+ DominoNoneRunner.class.getName());
		}
	}

	public DominoNoneRunner(final Runnable runnable, final ClassLoader classLoader) {
		runnable_ = runnable;
		classLoader_ = classLoader;
		if (runnable instanceof DominoNoneRunner) {
			throw new IllegalArgumentException("Can't wrap a " + runnable.getClass().getName() + " in another "
					+ DominoNoneRunner.class.getName());
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

	protected void preRun() {

	}

	protected void postRun() {

	}

	@Override
	public void run() {
		if (runnable_ != null) {
			try {
				preRun();
				if (!(runnable_ instanceof DominoNoneRunner)) {
					runnable_.run();
				} else {
					Throwable t = new Throwable();
					log_.log(Level.SEVERE,
							"Cannot run a " + runnable_.getClass().getName() + " in another " + DominoNoneRunner.class.getName(), t);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				postRun();
			}
		}
	}
}
