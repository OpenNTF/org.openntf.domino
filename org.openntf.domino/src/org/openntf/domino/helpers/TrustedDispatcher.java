/**
 * 
 */
package org.openntf.domino.helpers;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.thread.DominoExecutor.OpenRunnable;
import org.openntf.domino.thread.DominoExecutor.OpenSecurityManager;
import org.openntf.domino.thread.DominoFutureTask;
import org.openntf.domino.thread.DominoNativeRunner;
import org.openntf.domino.thread.DominoScheduledExecutor;
import org.openntf.domino.thread.DominoSessionType;
import org.openntf.domino.thread.DominoThreadFactory;
import org.openntf.domino.thread.model.IDominoRunnable;

/**
 * @author Nathan T. Freeman
 * 
 */
public class TrustedDispatcher /*extends AbstractDominoDaemon*/{
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(TrustedDispatcher.class.getName());
	protected TrustedExecutor intimidator_;

	//	private Queue<Runnable> runQueue_ = new ArrayDeque<Runnable>();

	protected TrustedExecutor getExecutor() {
		if (intimidator_ == null) {
			intimidator_ = new TrustedExecutor(this);
		}
		return intimidator_;
	}

	public static class TrustedSecurityManager extends OpenSecurityManager {
		public TrustedSecurityManager() {

		}

		@Override
		public void checkPermission(final Permission perm) {
			//yeah that's fine whatever
		}

		@Override
		public void checkPermission(final Permission perm, final Object context) {
			//what do I care?
		}
	}

	protected static class TrustedRunnable extends OpenRunnable {
		protected final AccessControlContext acc_;
		protected final SecurityManager sm_;

		public TrustedRunnable(final Runnable runnable, final ClassLoader loader, final AccessControlContext acc, final SecurityManager sm) {
			super(runnable, loader);
			acc_ = acc;
			sm_ = sm;
		}

		public TrustedRunnable(final Runnable runnable, final AccessControlContext acc, final SecurityManager sm) {
			super(runnable);
			acc_ = acc;
			sm_ = sm;
		}

		@Override
		public void setClassLoader(final ClassLoader loader) {
			loader_ = loader;
		}

		public Runnable getRunnable() {
			return runnable_;
		}

		protected void superRun() {
			super.run();
		}

		@Override
		public void run() {
			try {
				AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
					@Override
					public Object run() throws PrivilegedActionException {
						SecurityManager oldSM = System.getSecurityManager();
						try {
							System.setSecurityManager(sm_);
						} catch (Exception e) {
							throw new PrivilegedActionException(e);
						}
						if (loader_ != null) {
							Thread.currentThread().setContextClassLoader(loader_);
						}
						runnable_.run();	//wish there was a good way to call the super here, but there's not :(
						try {
							System.setSecurityManager(oldSM);
						} catch (Exception e) {
							throw new PrivilegedActionException(e);
						}
						return null;
					}
				}, acc_);
			} catch (PrivilegedActionException e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected static class TrustedExecutor extends DominoExecutor {
		private final TrustedDispatcher dispatcher_;
		private AccessControlContext factoryAccessController_;
		private SecurityManager securityManager_ = new TrustedSecurityManager();

		protected TrustedExecutor(final TrustedDispatcher dispatcher) {
			dispatcher_ = dispatcher;
		}

		protected TrustedExecutor(final TrustedDispatcher dispatcher, final DominoThreadFactory factory) {
			super(5, 20, 3, TimeUnit.SECONDS, DominoExecutor.getBlockingQueue(100), factory);
			dispatcher_ = dispatcher;
		}

		@Override
		protected void init() {
			factoryAccessController_ = AccessController.getContext();
		}

		/* (non-Javadoc)
		 * @see java.util.concurrent.ThreadPoolExecutor#shutdown()
		 */
		@Override
		public void shutdown() {
			//			System.out.println("Executor shutdown requested");
			super.shutdown();
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.DominoExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
		 */
		@Override
		protected void afterExecute(final Runnable r, final Throwable t) {
			super.afterExecute(r, t);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.DominoExecutor#newTaskFor(java.lang.Runnable, java.lang.Object)
		 */
		@Override
		protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
			System.out.println("DEBUG: Creating a new TrustedFutureTask for a " + runnable.getClass().getName());
			return new TrustedFutureTask(runnable, value, dispatcher_);
		}

		/* (non-Javadoc)
		 * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
		 */
		@Override
		public void execute(Runnable runnable) {
			if (runnable instanceof TrustedRunnable) {

			} else if (runnable instanceof DominoNativeRunner) {
				final ClassLoader loader = ((DominoNativeRunner) runnable).getClassLoader();
				runnable = new TrustedRunnable(runnable, factoryAccessController_, securityManager_);
				((TrustedRunnable) runnable).setClassLoader(loader);
			} else if (runnable instanceof IDominoRunnable) {
				DominoSessionType type = ((IDominoRunnable) runnable).getSessionType();
				final ClassLoader loader = ((IDominoRunnable) runnable).getContextClassLoader();
				if (type == DominoSessionType.NATIVE) {
					DominoNativeRunner nativeRunner = new DominoNativeRunner(runnable, loader);
					runnable = new TrustedRunnable(nativeRunner, factoryAccessController_, securityManager_);
					((TrustedRunnable) runnable).setClassLoader(loader);
				} else {
					System.out.println("DEBUG: IDominoRunnable has session type " + type.name());
				}
			}
			super.execute(runnable);
		}
	}

	protected static class TrustedScheduledExecutor extends DominoScheduledExecutor {
		private final TrustedDispatcher dispatcher_;
		private AccessControlContext factoryAccessController_;
		private SecurityManager securityManager_ = new TrustedSecurityManager();

		protected TrustedScheduledExecutor(final TrustedDispatcher dispatcher) {
			dispatcher_ = dispatcher;
		}

		protected TrustedScheduledExecutor(final TrustedDispatcher dispatcher, final DominoThreadFactory factory) {
			super(5, factory);
			dispatcher_ = dispatcher;
		}

		@Override
		protected void init() {
			factoryAccessController_ = AccessController.getContext();
		}

		/* (non-Javadoc)
		 * @see java.util.concurrent.ThreadPoolExecutor#shutdown()
		 */
		@Override
		public void shutdown() {
			//			System.out.println("Executor shutdown requested");
			super.shutdown();
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.DominoExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
		 */
		@Override
		protected void afterExecute(final Runnable r, final Throwable t) {
			super.afterExecute(r, t);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.DominoExecutor#newTaskFor(java.lang.Runnable, java.lang.Object)
		 */
		@Override
		protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
			System.out.println("DEBUG: Creating a new TrustedFutureTask for a " + runnable.getClass().getName());
			return new TrustedFutureTask(runnable, value, dispatcher_);
		}

		/* (non-Javadoc)
		 * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
		 */
		@Override
		public void execute(Runnable runnable) {
			if (runnable instanceof TrustedRunnable) {

			} else if (runnable instanceof DominoNativeRunner) {
				final ClassLoader loader = ((DominoNativeRunner) runnable).getClassLoader();
				runnable = new TrustedRunnable(runnable, factoryAccessController_, securityManager_);
				((TrustedRunnable) runnable).setClassLoader(loader);
			} else if (runnable instanceof IDominoRunnable) {
				DominoSessionType type = ((IDominoRunnable) runnable).getSessionType();
				final ClassLoader loader = ((IDominoRunnable) runnable).getContextClassLoader();
				if (type == DominoSessionType.NATIVE) {
					DominoNativeRunner nativeRunner = new DominoNativeRunner(runnable, loader);
					runnable = new TrustedRunnable(nativeRunner, factoryAccessController_, securityManager_);
					((TrustedRunnable) runnable).setClassLoader(loader);
				} else {
					System.out.println("DEBUG: IDominoRunnable has session type " + type.name());
				}
			}
			super.execute(runnable);
		}
	}

	protected static class TrustedFutureTask extends DominoFutureTask {
		/**
		 * @param runnable
		 * @param result
		 */
		public TrustedFutureTask(final Runnable runnable, final Object result, final TrustedDispatcher dispatcher) {
			super(runnable, result);
		}

		/* (non-Javadoc)
		 * @see org.openntf.domino.thread.DominoFutureTask#run()
		 */
		@Override
		public void run() {
			super.run();
		}

	}

	/**
	 * 
	 */
	public TrustedDispatcher() {
		//		super(50l);
	}

	/**
	 * @param delay
	 */
	public TrustedDispatcher(final long delay) {
		//		super(delay);
	}

	//	public void queueJob(final Runnable runnable) {
	//		synchronized (runQueue_) {
	//			runQueue_.add(runnable);
	//		}
	//	}

	public Object process(final Runnable runnable) {
		getExecutor().execute(runnable);
		return runnable;
	}

	public void stop(final boolean immediate) {
		if (immediate) {
			getExecutor().shutdownNow();
		} else {
			getExecutor().shutdown();
		}
	}

	//	@Override
	//	public Object process() {
	//		Runnable request = null;
	//		synchronized (runQueue_) {
	//			request = runQueue_.poll();
	//		}
	//		if (request != null) {
	//			//			System.out.println("Attempting auto-recycle of session from trusted runnable...");
	//			if (request instanceof AbstractDominoRunnable) {
	//				AbstractDominoRunnable adr = (AbstractDominoRunnable) request;
	//				if (adr.shouldRecycle()) {
	//					//					System.out.println("Attempting auto-recycle of session from trusted runnable...");
	//					lotus.domino.Session s = Factory.toLotus(adr.getSession());
	//					if (s != null)
	//						try {
	//							s.recycle();
	//							System.out.println(getClass().getName() + " recycled a session from a completed job: "
	//									+ adr.getClass().getName());
	//							log_.log(Level.INFO, getClass().getName() + " recycled a session from a completed job: "
	//									+ adr.getClass().getName());
	//						} catch (NotesException e) {
	//							e.printStackTrace();
	//						}
	//				} else {
	//					//					System.out.println("Executing a " + adr.getClass().getName() + " that's not ready to recycle...");
	//					adr.setSession(getNewTrustedSession());
	//					getExecutor().execute(request);
	//				}
	//			} else {
	//				getExecutor().execute(request);
	//			}
	//			return request;
	//		}
	//		return null;	//for future use
	//	}

	//	@Override
	//	public void run() {
	//		//		try {
	//		//			Session session = Factory.getTrustedSession();
	//		//			//			System.out.println("Got a session with ident " + session.getEffectiveUserName());
	//		//		} catch (Throwable t) {
	//		//			t.printStackTrace();
	//		//		}
	//		super.run();
	//	}

	//	public lotus.domino.Session getNewTrustedSession() {
	//		try {
	//			Object result = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
	//				@SuppressWarnings("deprecation")
	//				@Override
	//				public Object run() throws Exception {
	//					try {
	//						lotus.domino.Session s = lotus.domino.NotesFactory.createTrustedSession();
	//						return s;
	//					} catch (NotesException ne) {
	//						lotus.domino.Session s = lotus.domino.NotesFactory.createSessionWithFullAccess();
	//						return s;
	//					}
	//				}
	//			});
	//			if (result instanceof lotus.domino.Session) {
	//				return (lotus.domino.Session) result;
	//			}
	//		} catch (PrivilegedActionException e) {
	//			e.printStackTrace();
	//		}
	//		return null;
	//	}

	//	@Override
	//	public synchronized void stop() {
	//		log_.log(Level.INFO, "Stopping " + getClass().getName() + "...");
	//		try {
	//			if (intimidator_ != null)
	//				intimidator_.shutdown();
	//		} catch (Throwable t) {
	//			DominoUtils.handleException(t);
	//		}
	//		try {
	//			lotus.domino.Session s = Factory.terminate();
	//			if (s != null) {
	//				try {
	//					s.recycle();
	//				} catch (NotesException e) {
	//					DominoUtils.handleException(e);
	//				}
	//			}
	//		} catch (Throwable t) {
	//			DominoUtils.handleException(t);
	//		}
	//		try {
	//			super.stop();
	//		} catch (Throwable t) {
	//			DominoUtils.handleException(t);
	//		}
	//		log_.log(Level.INFO, getClass().getName() + " stopped.");
	//	}

}
