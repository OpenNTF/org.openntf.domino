/**
 * 
 */
package org.openntf.domino.thread;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import org.openntf.domino.annotations.Incomplete;

/**
 * @author Nathan T. Freeman
 * 
 */
@Incomplete
public class DominoFutureTask<V extends Serializable> extends FutureTask<V> {
	/**
	 * @param paramCallable
	 */
	public DominoFutureTask(final Callable<V> paramCallable) {
		super(paramCallable);
		System.out.println("New DFT created from Callable");
	}

	public DominoFutureTask(final Runnable runnable, final V result) {
		super(runnable, result);
		System.out.println("New DFT created from Runnable");
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.FutureTask#run()
	 */
	@Override
	public void run() {
		System.out.println("DFT run called");
		super.run();
		System.out.println("DFT run completed");
		done();
	}

	private static final Logger log_ = Logger.getLogger(DominoFutureTask.class.getName());
	private static final long serialVersionUID = 1L;
}
