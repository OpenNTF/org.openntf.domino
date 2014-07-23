/**
 * 
 */
package org.openntf.domino.thread;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
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

	public static class OpenSecurityManager extends SecurityManager {
		public OpenSecurityManager() {
		}
	}

	public static class OpenRunnable implements Runnable {
		protected final Runnable runnable_;
		protected ClassLoader loader_;

		public OpenRunnable(final Runnable runnable) {
			runnable_ = runnable;
		}

		public OpenRunnable(final Runnable runnable, final ClassLoader loader) {
			runnable_ = runnable;
			loader_ = loader;
		}

		public void setClassLoader(final ClassLoader loader) {
			loader_ = loader;
		}

		@Override
		public void run() {
			if (null != loader_) {
				System.out.println("DEBUG: setting ClassLoader to a " + loader_.getClass().getName() + " for a "
						+ runnable_.getClass().getName());
				Thread.currentThread().setContextClassLoader(loader_);
			}
			runnable_.run();
		}
	}

	private static final Logger log_ = Logger.getLogger(DominoExecutor.class.getName());
	private static final long serialVersionUID = 1L;
	private Set<IDominoListener> listeners_;

	public static BlockingQueue<Runnable> getBlockingQueue(final int size) {
		return new LinkedBlockingQueue<Runnable>(size);
	}

	protected void init() {

	}

	public DominoExecutor() {
		this(50);
	}

	public DominoExecutor(final int corePoolSize) {
		this(corePoolSize, corePoolSize);
	}

	public DominoExecutor(final int corePoolSize, final int maximumPoolSize) {
		this(corePoolSize, maximumPoolSize, 60l);
	}

	public DominoExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, getBlockingQueue(maximumPoolSize));
	}

	public DominoExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit paramTimeUnit,
			final BlockingQueue<Runnable> runnableQueue) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, paramTimeUnit, runnableQueue, new DominoThreadFactory());
	}

	public DominoExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit timeUnit,
			final BlockingQueue<Runnable> runnableQueue, final DominoThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, runnableQueue, threadFactory);
		init();
	}

	public DominoExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit timeUnit,
			final BlockingQueue<Runnable> runnableQueue, final RejectedExecutionHandler paramRejectedExecutionHandler) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, runnableQueue, new DominoThreadFactory(),
				paramRejectedExecutionHandler);
	}

	public DominoExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit timeUnit,
			final BlockingQueue<Runnable> runnableQueue, final DominoThreadFactory threadFactory,
			final RejectedExecutionHandler paramRejectedExecutionHandler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, runnableQueue, threadFactory, paramRejectedExecutionHandler);
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
		if (t instanceof lotus.domino.NotesThread) {
			((lotus.domino.NotesThread) t).initThread();
		}
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
		Thread thread = Thread.currentThread();
		if (thread instanceof lotus.domino.NotesThread) {
			//			System.out.println("DEBUG: afterExecute terminating a thread on a " + r.getClass().getName() + " in a "
			//					+ thread.getClass().getName());
			((lotus.domino.NotesThread) thread).termThread();
			//			Throwable throwable = new Throwable();
			//			throwable.printStackTrace();
		}
		super.afterExecute(r, t);
	}

	@Override
	public boolean isTerminating() {
		System.out.println("DEBUG: DominoExecutor is TERMINATING!!");
		return super.isTerminating();
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
	 */
	@Override
	public void execute(Runnable runnable) {
		if (runnable instanceof OpenRunnable) {

		} else if (runnable instanceof DominoNativeRunner) {
			final ClassLoader loader = ((DominoNativeRunner) runnable).getClassLoader();
			runnable = new OpenRunnable(runnable);
			((OpenRunnable) runnable).setClassLoader(loader);
		} else if (runnable instanceof IDominoRunnable) {
			DominoSessionType type = ((IDominoRunnable) runnable).getSessionType();
			final ClassLoader loader = ((IDominoRunnable) runnable).getContextClassLoader();
			if (type == DominoSessionType.NATIVE) {
				DominoNativeRunner nativeRunner = new DominoNativeRunner(runnable, loader);
				runnable = new OpenRunnable(nativeRunner);
				((OpenRunnable) runnable).setClassLoader(loader);
			} else {
				System.out.println("DEBUG: IDominoRunnable has session type " + type.name());
			}
		}
		if (runnable instanceof Future) {
			super.execute(runnable);
		} else {
			//			System.out.println("Would have submitted a " + runnable.getClass().getName());

			//			super.submit(runnable);
			super.execute(runnable);
		}
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

	@Override
	public void shutdown() {
		((DominoThreadFactory) getThreadFactory()).terminate();
		super.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		((DominoThreadFactory) getThreadFactory()).terminate();
		return super.shutdownNow();
	}
}
