package org.openntf.domino.xsp.xots;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import org.openntf.domino.thread.TaskletWorker;
import org.openntf.domino.thread.WorkerExecutor;
import org.openntf.domino.xots.Xots;
import org.openntf.domino.xsp.xots.XotsDominoExecutor.XotsModuleTasklet;

import com.ibm.domino.xsp.module.nsf.NotesContext;

public class TaskletWorkerExecutor<T> extends XotsModuleTasklet implements WorkerExecutor<T> {
	BlockingQueue<T> q = new LinkedBlockingQueue<T>();
	private static final int NOT_RUNNING = 0;
	private static final int RUNNING = 1;

	//private static final int SHUTDOWN = 2;

	private class Sync extends AbstractQueuedSynchronizer {

		public void add(final T t) {
			q.add(t);
			start();
		}

		public void stop() {
			for (;;) {
				int i = getState();
				if (i == NOT_RUNNING)
					return;
				if (i == RUNNING) {
					if (compareAndSetState(i, NOT_RUNNING)) {
						return;
					}
				}
			}
		}

		// NOT_RUNNING => RUNNING
		public void start() {
			for (;;) {
				int i = getState();
				if (i == RUNNING)
					return;
				if (i == NOT_RUNNING) {
					if (compareAndSetState(i, RUNNING)) {
						Xots.getService().submit(TaskletWorkerExecutor.this);
						return;
					}
				}
			}
		}

	}

	private T currentElement;
	private Sync inner = new Sync();

	public TaskletWorkerExecutor(final String moduleName, final String className, final Object... args) {
		super(moduleName, className, args);
	}

	public TaskletWorkerExecutor(final Class<? extends TaskletWorker<T>> clazz, final Object... args) {
		super(NotesContext.getCurrent().getModule().getDatabasePath(), clazz.getName(), args);
	}

	@Override
	public void send(final T t) {
		inner.add(t);
	}

	//	@Override
	//	public Object call() throws Exception {
	//		System.out.println("Start XotsQueueWriterThread");
	//		currentElement = q.poll(2000, TimeUnit.MILLISECONDS);
	//		for (;;) {
	//		
	//			super.call();
	//		}
	//		System.out.println("Stop XotsQueueWriterThread");
	//		return null;
	//	}

	@Override
	protected Object invokeObject(final Object wrappedTask) throws Exception {
		currentElement = q.poll(2000, TimeUnit.MILLISECONDS);
		if (currentElement == null) {
			inner.stop();
		} else {
			TaskletWorker worker = (TaskletWorker) wrappedTask;
			worker.startUp();
			do {
				worker.process(currentElement);
				currentElement = q.poll(2000, TimeUnit.MILLISECONDS);
			} while (currentElement != null);
			worker.tearDown();
			inner.stop();
		}
		return null;
	}
}