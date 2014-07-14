/**
 * 
 */
package org.openntf.domino.thread;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openntf.domino.annotations.Incomplete;
import org.openntf.domino.events.IDominoListener;
import org.openntf.domino.thread.model.IDominoRunnable;

/**
 * @author Nathan T. Freeman
 * 
 */
@Incomplete
public class DominoExecutor extends ThreadPoolExecutor {

	protected static class TrustedSecurityManager extends SecurityManager {
		public TrustedSecurityManager() {
			//			System.out.println("Made a new TrustedSecurityManager");
		}

		@Override
		public void checkPermission(final Permission paramPermission) {
			//			System.out.println("Giving permission for " + paramPermission.getName());
			//			super.checkPermission(paramPermission);
		}

		@Override
		public void checkPermission(final Permission paramPermission, final Object paramObject) {
			//			super.checkPermission(paramPermission, paramObject);
		}

	}

	protected static class TrustedRunnable implements Runnable {
		private final Runnable runnable_;
		private final AccessControlContext acc_;
		private final SecurityManager sm_;

		private ClassLoader loader_;

		public TrustedRunnable(final Runnable runnable, final AccessControlContext acc, final SecurityManager sm) {
			//			System.out.println("Created new TrustedRunnable");
			runnable_ = runnable;
			acc_ = acc;
			sm_ = sm;
		}

		public void setClassLoader(final ClassLoader loader) {
			loader_ = loader;
		}

		@Override
		public void run() {
			//			System.out.println("Running a " + runnable_.getClass().getName() + " with an AccessContext of " + acc_.getClass().getName()
			//					+ " and a security manager of " + (sm_ == null ? "null" : sm_.getClass().getName()));
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				@Override
				public Object run() {
					SecurityManager oldSM = System.getSecurityManager();
					try {
						System.setSecurityManager(sm_);
					} catch (Throwable t) {
						t.printStackTrace();
					}
					if (null != loader_) {
						Thread.currentThread().setContextClassLoader(loader_);
					}
					runnable_.run();
					try {
						//						System.out.println("Resetting securitymanager to " + (oldSM == null ? "null" : oldSM));
						System.setSecurityManager(oldSM);
					} catch (Throwable t) {
						t.printStackTrace();
					}
					return null;

				}
			}, acc_);
		}
	}

	private static final Logger log_ = Logger.getLogger(DominoExecutor.class.getName());
	private static final long serialVersionUID = 1L;
	private Set<IDominoListener> listeners_;
	private AccessControlContext factoryAccessController_;
	private SecurityManager securityManager_ = new TrustedSecurityManager();

	private static BlockingQueue<Runnable> getBlockingQueue(final int size) {
		return new LinkedBlockingQueue<Runnable>(size);
	}

	private void init() {
		//		System.out.println("New DominoExecutor constructed");
		factoryAccessController_ = AccessController.getContext();
		//		factoryAccessController_.checkPermission(new RuntimePermission("setContextClassLoader"));
		//		securityManager_ = System.getSecurityManager();
		//		System.out.println("SecurityManager is a " + (securityManager_ == null ? "null" : securityManager_.getClass().getName()));
	}

	public DominoExecutor() {
		this(50);
		//		super(50, 50, 60l, TimeUnit.SECONDS, getBlockingQueue(50), new DominoThreadFactory());
	}

	public DominoExecutor(final int corePoolSize) {
		this(corePoolSize, corePoolSize);
		//		super(corePoolSize, corePoolSize, 60l, TimeUnit.SECONDS, getBlockingQueue(corePoolSize), new DominoThreadFactory());
	}

	public DominoExecutor(final int corePoolSize, final int maximumPoolSize) {
		this(corePoolSize, maximumPoolSize, 60l);
		//		super(corePoolSize, maximumPoolSize, 60l, TimeUnit.SECONDS, getBlockingQueue(maximumPoolSize), new DominoThreadFactory());
	}

	public DominoExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, getBlockingQueue(maximumPoolSize));
		//		super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, getBlockingQueue(maximumPoolSize), new DominoThreadFactory());
	}

	public DominoExecutor(final int paramInt1, final int paramInt2, final long paramLong, final TimeUnit paramTimeUnit,
			final BlockingQueue<Runnable> paramBlockingQueue) {
		this(paramInt1, paramInt2, paramLong, paramTimeUnit, paramBlockingQueue, new DominoThreadFactory());
	}

	public DominoExecutor(final int paramInt1, final int paramInt2, final long paramLong, final TimeUnit paramTimeUnit,
			final BlockingQueue<Runnable> paramBlockingQueue, final DominoThreadFactory paramThreadFactory) {
		super(paramInt1, paramInt2, paramLong, paramTimeUnit, paramBlockingQueue, paramThreadFactory);
		init();
	}

	public DominoExecutor(final int paramInt1, final int paramInt2, final long paramLong, final TimeUnit paramTimeUnit,
			final BlockingQueue<Runnable> paramBlockingQueue, final RejectedExecutionHandler paramRejectedExecutionHandler) {
		this(paramInt1, paramInt2, paramLong, paramTimeUnit, paramBlockingQueue, new DominoThreadFactory(), paramRejectedExecutionHandler);
	}

	public DominoExecutor(final int paramInt1, final int paramInt2, final long paramLong, final TimeUnit paramTimeUnit,
			final BlockingQueue<Runnable> paramBlockingQueue, final DominoThreadFactory paramThreadFactory,
			final RejectedExecutionHandler paramRejectedExecutionHandler) {
		super(paramInt1, paramInt2, paramLong, paramTimeUnit, paramBlockingQueue, paramThreadFactory, paramRejectedExecutionHandler);
		init();
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#getThreadFactory()
	 */
	@Override
	public DominoThreadFactory getThreadFactory() {
		ThreadFactory result = super.getThreadFactory();
		if (!(result instanceof DominoThreadFactory)) {
			result = new DominoThreadFactory();
			this.setThreadFactory(result);
		}
		return (DominoThreadFactory) result;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
	 */
	@Override
	protected void beforeExecute(final Thread t, final Runnable r) {
		super.beforeExecute(t, r);
		if (r instanceof IDominoRunnable) {
			ClassLoader loader = ((IDominoRunnable) r).getContextClassLoader();
			if (loader != null) {
				t.setContextClassLoader(loader);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
	 */
	@Override
	protected void afterExecute(final Runnable r, final Throwable t) {
		super.afterExecute(r, t);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
	 */
	@Override
	public void execute(Runnable runnable) {
		if (runnable instanceof DominoNativeRunner) {
			final ClassLoader loader = ((DominoNativeRunner) runnable).getClassLoader();
			runnable = new TrustedRunnable((DominoNativeRunner) runnable, factoryAccessController_, securityManager_);
			((TrustedRunnable) runnable).setClassLoader(loader);
		} else if (runnable instanceof IDominoRunnable) {
			DominoSessionType type = ((IDominoRunnable) runnable).getSessionType();
			final ClassLoader loader = ((IDominoRunnable) runnable).getContextClassLoader();
			if (type == DominoSessionType.NATIVE) {
				DominoNativeRunner nativeRunner = new DominoNativeRunner(runnable, loader);
				TrustedRunnable runner = new TrustedRunnable(nativeRunner, factoryAccessController_, securityManager_);
				runner.setClassLoader(loader);
			} else {
				System.out.println("IDominoRunnable has session type " + type.name());
			}
		}
		RunnableFuture future = null;
		if (runnable instanceof RunnableFuture) {
			future = (RunnableFuture) runnable;
		} else {
			future = newTaskFor(runnable, null);
		}
		super.execute(future);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.AbstractExecutorService#newTaskFor(java.util.concurrent.Callable)
	 */
	@Override
	protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
		return new DominoFutureTask((Callable) callable);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.AbstractExecutorService#newTaskFor(java.lang.Runnable, java.lang.Object)
	 */
	@Override
	protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
		return new DominoFutureTask<T>(runnable, value);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#awaitTermination(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
		return super.awaitTermination(timeout, unit);
	}

	@Incomplete
	//these can't use the IDominoListener interface. Will need to put something new together for that.
	public void addListener(final IDominoListener listener) {
		if (listeners_ == null) {
			listeners_ = new LinkedHashSet<IDominoListener>();
		}
		listeners_.add(listener);
	}

	@Incomplete
	public IDominoListener removeListener(final IDominoListener listener) {
		if (listeners_ != null) {
			listeners_.remove(listener);
		}
		return listener;
	}
}
