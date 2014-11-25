package org.openntf.domino.thread;

import java.util.concurrent.TimeUnit;

public interface Scheduler {

	public void computeNextExecutionTime();

	public long getDelay(final TimeUnit timeUnit);

	public long getNextExecutionTime(final TimeUnit timeUnit);

	public boolean isPeriodic();

}
