/**
 * 
 */
package org.openntf.domino.thread;

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

/**
 * @author Nathan T. Freeman
 * 
 */
@Incomplete
public class DominoExecutor extends ThreadPoolExecutor {
	private static final Logger log_ = Logger.getLogger(DominoExecutor.class.getName());
	private static final long serialVersionUID = 1L;
	private Set<IDominoListener> listeners_;

	private static BlockingQueue<Runnable> getBlockingQueue(final int size) {
		return new LinkedBlockingQueue<Runnable>(size);
	}

	public DominoExecutor() {
		super(50, 50, 60l, TimeUnit.SECONDS, getBlockingQueue(50), new DominoThreadFactory());
	}

	public DominoExecutor(final int corePoolSize) {
		super(corePoolSize, corePoolSize, 60l, TimeUnit.SECONDS, getBlockingQueue(corePoolSize), new DominoThreadFactory());
	}

	public DominoExecutor(final int corePoolSize, final int maximumPoolSize) {
		super(corePoolSize, maximumPoolSize, 60l, TimeUnit.SECONDS, getBlockingQueue(maximumPoolSize), new DominoThreadFactory());
	}

	public DominoExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, getBlockingQueue(maximumPoolSize), new DominoThreadFactory());
	}

	public DominoExecutor(final int paramInt1, final int paramInt2, final long paramLong, final TimeUnit paramTimeUnit,
			final BlockingQueue<Runnable> paramBlockingQueue) {
		super(paramInt1, paramInt2, paramLong, paramTimeUnit, paramBlockingQueue, new DominoThreadFactory());
	}

	public DominoExecutor(final int paramInt1, final int paramInt2, final long paramLong, final TimeUnit paramTimeUnit,
			final BlockingQueue<Runnable> paramBlockingQueue, final DominoThreadFactory paramThreadFactory) {
		super(paramInt1, paramInt2, paramLong, paramTimeUnit, paramBlockingQueue, paramThreadFactory);
	}

	public DominoExecutor(final int paramInt1, final int paramInt2, final long paramLong, final TimeUnit paramTimeUnit,
			final BlockingQueue<Runnable> paramBlockingQueue, final RejectedExecutionHandler paramRejectedExecutionHandler) {
		super(paramInt1, paramInt2, paramLong, paramTimeUnit, paramBlockingQueue, paramRejectedExecutionHandler);
	}

	public DominoExecutor(final int paramInt1, final int paramInt2, final long paramLong, final TimeUnit paramTimeUnit,
			final BlockingQueue<Runnable> paramBlockingQueue, final DominoThreadFactory paramThreadFactory,
			final RejectedExecutionHandler paramRejectedExecutionHandler) {
		super(paramInt1, paramInt2, paramLong, paramTimeUnit, paramBlockingQueue, paramThreadFactory, paramRejectedExecutionHandler);
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
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
	 */
	@Override
	protected void afterExecute(final Runnable r, final Throwable t) {
		super.afterExecute(r, t);
		if (r instanceof AbstractDominoRunnable) {
			((AbstractDominoRunnable) r).clean();
		}
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
	 */
	@Override
	public void execute(final Runnable runnable) {
		//		System.out.println("DominoExecutor executing a " + runnable.getClass().getName());
		RunnableFuture future = null;
		if (runnable instanceof RunnableFuture) {
			future = (RunnableFuture) runnable;
		} else if (runnable instanceof Callable) {
			future = new DominoFutureTask((Callable) runnable);
		} else {
			future = new DominoFutureTask<Void>(runnable, null);
		}
		super.execute(future);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#awaitTermination(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
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
