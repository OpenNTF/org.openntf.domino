/**
 * 
 */
package org.openntf.domino.thread;

import java.util.logging.Logger;

import org.openntf.domino.utils.Factory;

/**
 * @author Nathan T. Freeman
 * 
 */
public abstract class AbstractDominoRunnable implements Runnable {
	private static final Logger log_ = Logger.getLogger(AbstractDominoRunnable.class.getName());
	private static final long serialVersionUID = 1L;
	//	private int threadident_ = 0;
	//	private transient lotus.domino.Session runsess_;
	private boolean started_ = false;

	/**
	 * 
	 */
	public AbstractDominoRunnable() {
		// TODO Auto-generated constructor stub
	}

	//	public void setRunThread(final Thread t) {
	//		threadident_ = System.identityHashCode(t);
	//	}

	public int getRunThreadId() {
		return 0;
		//		return threadident_;
	}

	public void setup(final lotus.domino.Session session) {
		//		runsess_ = session;
		//		setRunThread(Thread.currentThread());
	}

	public void clean() {
		System.out.println("Cleaning runnable off thread " + System.identityHashCode(Thread.currentThread()));
		try {
			Factory.terminate().recycle();
		} catch (lotus.domino.NotesException ne) {
			ne.printStackTrace();
		}
		lotus.domino.NotesThread.stermThread();
	}

}
