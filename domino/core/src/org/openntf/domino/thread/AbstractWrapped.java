package org.openntf.domino.thread;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;

import lotus.domino.NotesThread;

import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.thread.AbstractDominoExecutor.DominoFutureTask;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Tasklet;

/**
 * A Wrapper for callables and runnable that provides proper setup/teardown
 * 
 * @author Roland Praml, FOCONIS AG
 * 
 */
public abstract class AbstractWrapped<T> {
	public static abstract class WrappedCallable<V> extends AbstractWrapped<Callable<V>> implements Callable<V> {

	}

	public static abstract class WrappedRunnable extends AbstractWrapped<Runnable> implements Runnable {

	}

	private T wrappedTask;

	protected Tasklet.Scope scope;
	protected Tasklet.Context context;
	protected ISessionFactory sessionFactory;
	protected boolean bubbleException;

	/**
	 * Determines the sessionType under which the current runnable should run. The first non-null value of the following list is returned
	 * <ul>
	 * <li>If the runnable implements <code>IDominoRunnable</code>: result of <code>getSessionType</code></li>
	 * <li>the value of {@link SessionType} Annotation</li>
	 * <li>DominoSessionType.DEFAULT</li>
	 * </ul>
	 * 
	 * @param task
	 *            the runnable to determine the DominoSessionType
	 */
	protected void init(final T task) {
		wrappedTask = task;
		// some security checks...
		if (task instanceof NotesThread) {
			// RPr: I'm not sure if this should be allowed anyway...
			throw new IllegalArgumentException("Cannot wrap the NotesThread " + task.getClass().getName() + " into a DominoRunner");
		}
		if (task instanceof DominoFutureTask) {
			// RPr: don't know if this is possible
			throw new IllegalArgumentException("Cannot wrap the WrappedCallable " + task.getClass().getName() + " into a DominoRunner");
		}
		if (task instanceof AbstractWrapped) {
			// RPr: don't know if this is possible
			throw new IllegalArgumentException("Cannot wrap the WrappedCallable " + task.getClass().getName() + " into a DominoRunner");
		}

		// save current bubbleExceptions settings
		bubbleException = DominoUtils.getBubbleExceptions();

		if (task instanceof Tasklet.Interface) {
			Tasklet.Interface dominoRunnable = (Tasklet.Interface) task;
			sessionFactory = dominoRunnable.getSessionFactory();
			scope = dominoRunnable.getScope();
			context = dominoRunnable.getContext();
		}
		Tasklet annot = task.getClass().getAnnotation(Tasklet.class);

		if (annot != null) {
			if (sessionFactory == null) {
				switch (annot.session()) {
				case CLONE:
					sessionFactory = Factory.getSessionFactory(SessionType.CURRENT);
					break;
				case CLONE_FULL_ACCESS:
					sessionFactory = Factory.getSessionFactory(SessionType.CURRENT_FULL_ACCESS);
					break;

				case FULL_ACCESS:
					sessionFactory = Factory.getSessionFactory(SessionType.FULL_ACCESS);
					break;

				case NATIVE:
					sessionFactory = Factory.getSessionFactory(SessionType.NATIVE);
					break;

				case NONE:
					sessionFactory = null;
					break;

				case SIGNER:
					sessionFactory = Factory.getSessionFactory(SessionType.SIGNER);
					break;

				case SIGNER_FULL_ACCESS:
					sessionFactory = Factory.getSessionFactory(SessionType.SIGNER_FULL_ACCESS);
					break;

				case TRUSTED:
					sessionFactory = Factory.getSessionFactory(SessionType.TRUSTED);
					break;

				default:
					break;
				}
				if ((annot.session() != Tasklet.Session.NONE) && sessionFactory == null) {
					throw new IllegalStateException("Could not create a Fatory for " + annot.session());
				}
				if (sessionFactory != null) {
					Factory.setSessionFactory(sessionFactory, SessionType.CURRENT);
				}
			}

			if (context == null) {
				context = annot.context();
			}
			if (context == null) {
				context = Tasklet.Context.DEFAULT;
			}

			if (scope == null) {
				scope = annot.scope();
			}
			if (scope == null) {
				scope = Tasklet.Scope.NONE;
			}
		}
	}

	public void addObserver(final Observer o) {
		if (wrappedTask instanceof Observable) {
			((Observable) wrappedTask).addObserver(o);
		}
	}

	protected T getWrappedTask() {
		return wrappedTask;
	}
}