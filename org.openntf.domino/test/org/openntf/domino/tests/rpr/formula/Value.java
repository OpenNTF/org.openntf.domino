package org.openntf.domino.tests.rpr.formula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Value {
	private Object singleValue;
	private List<Object> multiValue;

	public Value() {
	}

	public Value(final Object init) {
		if (init instanceof List) {
			multiValue = new ArrayList<Object>((Collection<?>) init);
		} else {
			singleValue = init;
		}
	}

	/**
	 * get the nth entry (0=first entry)
	 * 
	 * @param i
	 * @return
	 */
	public Object get(final int i) {
		if (multiValue == null) {
			return singleValue;
		} else if (multiValue.size() == 0) {
			return null; // TODO: What to do?
		} else if (i < multiValue.size()) {
			return multiValue.get(i);
		} else {
			return multiValue.get(multiValue.size() - 1);
		}
	}

	public Number getNumber(final int i) {
		Object o = get(i);
		if (o instanceof Number) {
			return (Number) o;
		}
		throw new ClassCastException("Cannot convert " + o + " to number");
	}

	public int size() {
		if (multiValue == null) {
			return 1;
		} else {
			return multiValue.size();
		}
	}

	public boolean isTrue() {
		for (int i = 0; i < size(); i++) {
			if (getNumber(i).intValue() != 0) {
				return true;
			}
		}
		return false;
	}

	public List getList() {
		if (multiValue == null) {
			multiValue = new ArrayList<Object>(8);
			if (singleValue != null) {
				multiValue.add(singleValue);
			}
			singleValue = null;
		}
		return multiValue;
	}

	@Override
	public Value clone() {
		if (multiValue == null) {
			return new Value(singleValue);
		} else {
			return new Value(multiValue);
		}
	}

	public void append(final Value other) {
		if (multiValue == null && singleValue == null && other.size() == 1) {
			singleValue = other.get(0);
		} else {
			getList().addAll(other.getList());
		}
	}
}
