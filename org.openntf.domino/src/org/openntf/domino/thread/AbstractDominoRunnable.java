/**
 * 
 */
package org.openntf.domino.thread;

import java.io.Serializable;
import java.util.Observable;
import java.util.logging.Logger;

import org.openntf.domino.utils.Factory;

/**
 * @author Nathan T. Freeman
 * 
 */
public abstract class AbstractDominoRunnable extends Observable implements Runnable, Serializable {
	private static final Logger log_ = Logger.getLogger(AbstractDominoRunnable.class.getName());
	private static final long serialVersionUID = 1L;

	public AbstractDominoRunnable() {

	}

	public abstract boolean shouldStop();

	public void clean() {
		//		System.out.println("Cleaning runnable off thread " + System.identityHashCode(Thread.currentThread()));
		try {
			Factory.terminate().recycle();
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();
		}
		lotus.domino.NotesThread.stermThread();
	}

}
