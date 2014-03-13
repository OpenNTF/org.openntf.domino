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
	protected AbstractDominoRunnable runnable_;

	/**
	 * @param paramCallable
	 */
	public DominoFutureTask(final Callable<V> paramCallable) {
		super(paramCallable);
	}

	public DominoFutureTask(final Runnable runnable, final V result) {
		super(runnable, result);
		if (runnable instanceof AbstractDominoRunnable) {
			runnable_ = (AbstractDominoRunnable) runnable;
		}
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
			runnable_.clean();
		}
		super.done();
	}

	private static final Logger log_ = Logger.getLogger(DominoFutureTask.class.getName());
	private static final long serialVersionUID = 1L;
}
