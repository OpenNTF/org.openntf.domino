package org.openntf.domino.thread;

import org.openntf.domino.impl.Base;

public class DominoChildThread extends DominoThread {
	// This will be the Thread for executing Runnables that are given a Domino object starting point, such as a Session or Document or
	// Database.

	public DominoChildThread() {
		// TODO Auto-generated constructor stub
	}

	public DominoChildThread(Runnable runnable) {
		super(runnable);
		// TODO Auto-generated constructor stub
	}

	public DominoChildThread(String threadName) {
		super(threadName);
		// TODO Auto-generated constructor stub
	}

	public DominoChildThread(Runnable runnable, String threadName) {
		super(runnable, threadName);
		// TODO Auto-generated constructor stub
	}

	public DominoChildThread(ThreadGroup group, Runnable runnable) {
		super(group, runnable);
		// TODO Auto-generated constructor stub
	}

	public DominoChildThread(ThreadGroup group, String threadName) {
		super(group, threadName);
		// TODO Auto-generated constructor stub
	}

	public DominoChildThread(ThreadGroup arg0, Runnable arg1, String arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public void setContext(lotus.domino.Base... bases) {
		Base.lock(bases);
	}
}
