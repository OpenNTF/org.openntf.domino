/**
 * 
 */
package org.openntf.domino.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class Counter.
 */
class Counter {
	private final AtomicInteger globalCounter;
	private final ThreadLocal<AtomicInteger> threadCounter;

	public Counter(final boolean countPerThread) {
		if (countPerThread) {
			globalCounter = null;
			threadCounter = new ThreadLocal<AtomicInteger>() {
				@Override
				protected AtomicInteger initialValue() {
					return new AtomicInteger(0);
				}
			};
		} else {
			globalCounter = new AtomicInteger(0);
			threadCounter = null;
		}
	}

	/**
	 * Increment.
	 * 
	 * @return the int
	 */
	public int increment() {
		if (globalCounter == null) {
			return threadCounter.get().incrementAndGet();
		} else {
			return globalCounter.incrementAndGet();
		}
	}

	/**
	 * Decrement.
	 * 
	 * @return the int
	 */
	public int decrement() {
		if (globalCounter == null) {
			return threadCounter.get().decrementAndGet();
		} else {
			return globalCounter.decrementAndGet();
		}
	}

	/**
	 * read the value
	 * 
	 * @return the int value
	 */
	public int intValue() {
		if (globalCounter == null) {
			return threadCounter.get().intValue();
		} else {
			return globalCounter.intValue();
		}
	}
}