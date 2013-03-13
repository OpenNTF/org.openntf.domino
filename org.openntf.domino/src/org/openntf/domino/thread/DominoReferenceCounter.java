package org.openntf.domino.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DominoReferenceCounter extends ThreadLocal<Object> {
	private static final Logger log_ = Logger.getLogger(DominoReferenceCounter.class.getName());

	public static class Counter extends Number {
		private int value;

		public Counter() {
			super();
		}

		public Counter(int value) {
			super();
			this.value = value;
		}

		public int increment() {
			return ++value;
		}

		public int decrement() {
			return --value;
		}

		public int add(int operand) {
			this.value += operand;
			return value;
		}

		public int subtract(int operand) {
			this.value -= operand;
			return value;
		}

		@Override
		public double doubleValue() {
			return value;
		}

		@Override
		public float floatValue() {
			return value;
		}

		@Override
		public int intValue() {
			return value;
		}

		@Override
		public long longValue() {
			return value;
		}

	}

	public DominoReferenceCounter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object initialValue() {
		return new HashMap<Long, Counter>();
	}

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

	@SuppressWarnings("unchecked")
	public int getCount(Long id) {
		Map<Long, Counter> map = (Map<Long, Counter>) get();
		if (map.containsKey(id)) {
			return map.get(id).intValue();
		} else {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public void forcedRecycle(Long id) {
		Map<Long, Counter> map = (Map<Long, Counter>) get();
		map.remove(id);
	}
}
