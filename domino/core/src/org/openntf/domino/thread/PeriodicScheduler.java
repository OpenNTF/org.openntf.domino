package org.openntf.domino.thread;

import java.util.concurrent.TimeUnit;

public class PeriodicScheduler implements Scheduler {

	long time = 0;
	long period = 0;

	public PeriodicScheduler(final long delay, final long period, final TimeUnit timeUnit) {
		super();
		this.time = System.nanoTime() + timeUnit.convert(delay, TimeUnit.NANOSECONDS);
		this.period = timeUnit.convert(period, TimeUnit.NANOSECONDS);
	}

	@Override
	public void computeNextExecutionTime() {
		if (this.period > 0L) {
			this.time += this.period;
		} else {
			this.time = System.nanoTime() - this.period;
		}

	}

	@Override
	public long getDelay(final TimeUnit timeUnit) {
		return timeUnit.convert(time - System.nanoTime(), TimeUnit.NANOSECONDS);
	}

	@Override
	public long getNextExecutionTime(final TimeUnit timeUnit) {
		return timeUnit.convert(time, TimeUnit.NANOSECONDS);
	}

	@Override
	public boolean isPeriodic() {
		return period != 0L;
	}

}
