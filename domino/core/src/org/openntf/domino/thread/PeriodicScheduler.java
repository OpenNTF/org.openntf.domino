package org.openntf.domino.thread;

import java.util.concurrent.TimeUnit;

public class PeriodicScheduler implements Scheduler {

	long time = 0;
	long period = 0;

	public PeriodicScheduler(final long delay, final long period, final TimeUnit timeUnit) {
		super();
		this.time = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delay, timeUnit);
		this.period = TimeUnit.NANOSECONDS.convert(period, timeUnit);
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

	@Override
	public String toString() {
		return "delay: " + getNextExecutionTime(TimeUnit.MILLISECONDS) + " ms, period: "
				+ TimeUnit.MILLISECONDS.convert(period, TimeUnit.NANOSECONDS);
	}

}
