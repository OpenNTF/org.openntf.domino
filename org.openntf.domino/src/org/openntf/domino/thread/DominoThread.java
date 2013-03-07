package org.openntf.domino.thread;

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

	public DominoThread(ThreadGroup group, Runnable runnable, String threadName, long stack) {
		super(group, runnable, threadName, stack);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		super.start();
	}
}
