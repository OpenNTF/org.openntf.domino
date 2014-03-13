/**
 * 
 */
package org.openntf.domino.thread;

import java.io.Serializable;
import java.util.Observable;
import java.util.logging.Logger;

import org.openntf.domino.impl.Base;
import org.openntf.domino.utils.Factory;

/**
 * @author Nathan T. Freeman
 * 
 */
public abstract class AbstractDominoRunnable extends Observable implements Runnable, Serializable {
	private static final Logger log_ = Logger.getLogger(AbstractDominoRunnable.class.getName());
	private static final long serialVersionUID = 1L;
	private transient org.openntf.domino.Session session_;

	public AbstractDominoRunnable() {

	}

	public abstract boolean shouldStop();

	private transient boolean recycle_ = false;

	public boolean shouldRecycle() {
		return recycle_;
	}

	public void setSession(final lotus.domino.Session session) {
		Factory.setSession(session);
		Base.lock(Factory.getSession());
	}

	public org.openntf.domino.Session getSession() {
		return session_;
	}

	public void clean() {
		recycle_ = true;
	}

}
