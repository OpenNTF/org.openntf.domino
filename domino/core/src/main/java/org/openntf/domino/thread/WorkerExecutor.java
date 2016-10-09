package org.openntf.domino.thread;

public interface WorkerExecutor<T> {

	public void send(T t);
}
