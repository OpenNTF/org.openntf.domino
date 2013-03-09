package org.openntf.domino.thread;

import java.lang.ref.Reference;

import org.openntf.domino.impl.Base;
import org.openntf.domino.utils.Factory;

public class DominoThread extends Thread {
	// This will be the Thread for executing Runnables that need Domino objects created from scratch

	public DominoThread() {
		// TODO Auto-generated constructor stub
	}

	public DominoThread(Runnable runnable) {
		super(runnable);
		// TODO Auto-generated constructor stub
	}

	public DominoThread(String threadName) {
		super(threadName);
		// TODO Auto-generated constructor stub
	}

	public DominoThread(Runnable runnable, String threadName) {
		super(runnable, threadName);
		// TODO Auto-generated constructor stub
	}

	public DominoThread(ThreadGroup group, Runnable runnable) {
		super(group, runnable);
		// TODO Auto-generated constructor stub
	}

	public DominoThread(ThreadGroup group, String threadName) {
		super(group, threadName);
		// TODO Auto-generated constructor stub
	}

	public DominoThread(ThreadGroup arg0, Runnable arg1, String arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		try {
			lotus.domino.NotesThread.sinitThread();
			super.run();
			// System.out.println("Completed run. GCing...");
			// System.out.println("GC complete");
		} catch (Throwable t) {
			throw new RuntimeException(t);
		} finally {
			// Base.recycleAll();
			System.gc();
			// try {
			// Thread.sleep(500);
			// } catch (InterruptedException e) {
			//
			// }
			// System.out.println("Thread " + Thread.currentThread().getName() + " still has " + Factory.getLotusCount()
			// + " lotus objects. Auto-recycling those...");
			int runRecycleCount = Factory.getAutoRecycleCount();
			int drCount = 0;
			DominoReferenceQueue drq = Base._getRecycleQueue();
			// System.out
			// .println("Got a queue on thread " + Thread.currentThread().getName() + " (" + Thread.currentThread().hashCode() + ")");
			Reference<?> ref = drq.poll();

			while (ref != null) {
				if (ref instanceof DominoReference) {
					// System.out.println("Found a phantom reference of type " + ((DominoReference) ref).getType().getName());
					((DominoReference) ref).recycle();
					drCount++;
				}
				ref = drq.poll();
			}
			System.out.println("Thread " + Thread.currentThread().getName() + " auto-recycled " + runRecycleCount
					+ " lotus references during run. Then recycled " + drCount + " lotus references on completion and had "
					+ Factory.getRecycleErrorCount() + " recycle errors");
			// DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
			// System.out.println(df.format(new Date()) + "DominoReferenceQueue drained");
			lotus.domino.NotesThread.stermThread();
		}
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		super.start();
	}
}
