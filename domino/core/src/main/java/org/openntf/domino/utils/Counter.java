/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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