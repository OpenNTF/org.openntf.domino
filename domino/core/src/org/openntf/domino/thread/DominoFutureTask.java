/**
 * 
 */
package org.openntf.domino.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import org.openntf.domino.annotations.Incomplete;

/**
 * @author Nathan T. Freeman
 * 
 */
@Incomplete
public class DominoFutureTask<V> extends FutureTask<V> {
	protected Runnable runnable_;

	public DominoFutureTask(final Callable<V> paramCallable) {
		super(paramCallable);
	}

	public DominoFutureTask(final Runnable runnable, final V result) {
		super(runnable, result);

		runnable_ = runnable;

	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.FutureTask#run()
	 */
	@Override
	public void run() {
		super.run();
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.FutureTask#set(java.lang.Object)
	 */
	@Override
	protected void set(final V paramV) {
		super.set(paramV);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.FutureTask#done()
	 */
	@Override
	protected void done() {
		if (runnable_ != null) {
			if (runnable_ instanceof AbstractDominoRunnable) {
				((AbstractDominoRunnable) runnable_).clean();
			}
		}
		super.done();
	}

	public Runnable getRunnable() {
		return runnable_;
	}

	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DominoFutureTask.class.getName());
}
