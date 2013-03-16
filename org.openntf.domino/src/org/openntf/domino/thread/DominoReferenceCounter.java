/*
 * Copyright OpenNTF 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.openntf.domino.thread;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class DominoReferenceCounter.
 */
public class DominoReferenceCounter extends ThreadLocal<Object> {

	/** The Constant log_. */
	private static final Logger log_ = Logger.getLogger(DominoReferenceCounter.class.getName());

	/**
	 * The Class Counter.
	 */
	public static class Counter extends Number {

		/** The value. */
		private int value;

		/**
		 * Instantiates a new counter.
		 */
		public Counter() {
			super();
		}

		/**
		 * Instantiates a new counter.
		 * 
		 * @param value
		 *            the value
		 */
		public Counter(int value) {
			super();
			this.value = value;
		}

		/**
		 * Increment.
		 * 
		 * @return the int
		 */
		public int increment() {
			return ++value;
		}

		/**
		 * Decrement.
		 * 
		 * @return the int
		 */
		public int decrement() {
			return --value;
		}

		/**
		 * Adds the.
		 * 
		 * @param operand
		 *            the operand
		 * @return the int
		 */
		public int add(int operand) {
			this.value += operand;
			return value;
		}

		/**
		 * Subtract.
		 * 
		 * @param operand
		 *            the operand
		 * @return the int
		 */
		public int subtract(int operand) {
			this.value -= operand;
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Number#doubleValue()
		 */
		@Override
		public double doubleValue() {
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Number#floatValue()
		 */
		@Override
		public float floatValue() {
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Number#intValue()
		 */
		@Override
		public int intValue() {
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Number#longValue()
		 */
		@Override
		public long longValue() {
			return value;
		}

	}

	/**
	 * Instantiates a new domino reference counter.
	 */
	public DominoReferenceCounter() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ThreadLocal#initialValue()
	 */
	@Override
	protected Object initialValue() {
		return new WeakHashMap<Long, Counter>();
	}

	/**
	 * Increment.
	 * 
	 * @param lotus
	 *            the lotus
	 * @return the int
	 */
	@SuppressWarnings("unchecked")
	public int increment(lotus.domino.local.NotesBase lotus) {
		Long id = org.openntf.domino.impl.Base.getLotusId((lotus.domino.local.NotesBase) lotus);
		Map<Long, Counter> map = (Map<Long, Counter>) get();
		if (map.containsKey(id)) {
			return map.get(id).increment();
		} else {
			map.put(id, new Counter(1));
			return 1;
		}
	}

	/**
	 * Decrement.
	 * 
	 * @param lotus
	 *            the lotus
	 * @return the int
	 */
	@SuppressWarnings("unchecked")
	public int decrement(lotus.domino.local.NotesBase lotus) {
		Long id = org.openntf.domino.impl.Base.getLotusId((lotus.domino.local.NotesBase) lotus);
		Map<Long, Counter> map = (Map<Long, Counter>) get();
		if (map.containsKey(id)) {
			int result = map.get(id).decrement();
			if (result == 0)
				map.remove(id);
			return result;
		} else {
			return 0;
		}
	}

	/**
	 * Gets the count.
	 * 
	 * @param lotus
	 *            the lotus
	 * @return the count
	 */
	@SuppressWarnings("unchecked")
	public int getCount(lotus.domino.local.NotesBase lotus) {
		Long id = org.openntf.domino.impl.Base.getLotusId((lotus.domino.local.NotesBase) lotus);
		Map<Long, Counter> map = (Map<Long, Counter>) get();
		if (map.containsKey(id)) {
			return map.get(id).intValue();
		} else {
			return 0;
		}
	}

	/**
	 * Gets the count.
	 * 
	 * @param id
	 *            the id
	 * @return the count
	 */
	@SuppressWarnings("unchecked")
	public int getCount(Long id) {
		Map<Long, Counter> map = (Map<Long, Counter>) get();
		if (map.containsKey(id)) {
			return map.get(id).intValue();
		} else {
			return 0;
		}
	}

	/**
	 * Forced recycle.
	 * 
	 * @param id
	 *            the id
	 */
	@SuppressWarnings("unchecked")
	public void forcedRecycle(Long id) {
		Map<Long, Counter> map = (Map<Long, Counter>) get();
		map.remove(id);
	}
}
