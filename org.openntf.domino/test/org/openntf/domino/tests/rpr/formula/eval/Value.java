package org.openntf.domino.tests.rpr.formula.eval;

import java.util.Collection;
import java.util.Date;

public class Value {
	private Object values[];
	private int size;

	public Value() {
	}

	public Value(final Object init) {
		if (init instanceof Collection) {
			Collection c = (Collection) init;
			incSize(c.size());
			for (Object el : c) {
				append(el);
			}
		} else {
			incSize(1);
			append(init);
		}
	}

	protected void incSize(final int inc) {
		Object arr[] = new Object[size + inc];
		if (size > 0) {
			System.arraycopy(values, 0, arr, 0, size);
		}
		values = arr;
	}

	/**
	 * get the nth entry (0=first entry)
	 * 
	 * @param i
	 * @return
	 */
	public Object get(final int i) {
		if (size == 0) {
			return null; // TODO: What to do?
		} else if (i < size) {
			return values[i];
		} else {
			return values[size - 1];
		}
	}

	public String getText(final int i) {
		Object o = get(i);
		if (o instanceof String) {
			return (String) o;
		}
		throw new ClassCastException("Text expected. Got '" + o + "'");
	}

	public Date getDate(final int i) {
		Object o = get(i);
		if (o instanceof Date) {
			return (Date) o;
		}
		throw new ClassCastException("Text expected. Got '" + o + "'");
	}

	public int getInt(final int i) {
		Object o = get(i);
		if (o instanceof Number) {
			return ((Number) o).intValue();
		}
		throw new ClassCastException("Cannot convert " + o + " to number");
	}

	public double getDouble(final int i) {
		Object o = get(i);
		if (o instanceof Double) {
			return ((Double) o);
		}
		if (o instanceof Number) {
			return ((Number) o).doubleValue();
		}
		throw new ClassCastException("Cannot convert " + o + " to double");
	}

	public int size() {
		return size;
	}

	public boolean isTrue() {
		for (int i = 0; i < size(); i++) {
			if (getInt(i) != 0) {
				return true;
			}
		}
		return false;
	}

	//	@Override
	//	public Value clone() {
	//		if (multiValue == null) {
	//			return new Value(singleValue);
	//		} else {
	//			return new Value(multiValue);
	//		}
	//	}

	public void append(final Object other) {
		if (values == null || size == values.length) {
			incSize(4);
		}
		values[size++] = other;
	}

	public void append(final Value other) {
		incSize(other.size());
		for (int i = 0; i < other.size(); i++) {
			append(other.get(i));
		}
	}
}
