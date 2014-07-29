/**
 * 
 */
package org.openntf.domino.thread;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.openntf.domino.utils.Factory;

/**
 * @author Nathan T. Freeman
 * 
 */
public abstract class AbstractDominoCallable<V> implements Callable<V> {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(AbstractDominoCallable.class.getName());

	/**
	 * 
	 */
	public AbstractDominoCallable() {
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public V call() throws Exception {
		return null;
	}

	public abstract boolean shouldStop();

	public void clean() {
		try {
			Factory.terminate().recycle();
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();
		}
		lotus.domino.NotesThread.stermThread();
	}
}
