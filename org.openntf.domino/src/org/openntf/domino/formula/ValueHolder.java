/*
 * © Copyright FOCONIS AG, 2014
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
 * 
 */
package org.openntf.domino.formula;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;

import org.openntf.domino.DateTime;

/**
 * Valueholder to hold single or multiple values
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class ValueHolder extends AbstractList<Object> implements Serializable {
	private static final long serialVersionUID = 8290517470597891417L;

	public enum DataType {
		ERROR, STRING, INTEGER(true), NUMBER(true), DOUBLE(true), DATETIME, BOOLEAN, _UNSET, OBJECT;
		public boolean numeric = false;

		DataType() {
		}

		DataType(final boolean n) {
			numeric = n;
		}
	}

	// For performance reasons we allow direct access to these members
	private Object values[];
	private int valueInt;
	private double valueDbl;

	public int size;
	public boolean dirty;
	public DataType dataType = DataType._UNSET;

	public ValueHolder() {
	}

	/**
	 * Init a ValueHolder based on a single value or a collection
	 * 
	 */
	@Deprecated
	public ValueHolder(final Object init) {
		if (init != null && init.getClass().isArray()) {
			int lh = Array.getLength(init);
			grow(lh);
			for (int i = 0; i < lh; i++) {
				add(Array.get(init, i));
			}
		} else if (init instanceof Collection) {
			addAll((Collection<?>) init);
		} else {
			add(init);
		}
	}

	public ValueHolder(final RuntimeException init) {
		setError(init);
	}

	public ValueHolder(final int init) {
		add(init);
	}

	public ValueHolder(final double init) {
		add(init);
	}

	public ValueHolder(final String init) {
		add(init);
	}

	public ValueHolder(final DateTime init) {
		add(init);
	}

	public ValueHolder(final Boolean init) {
		add(init);
	}

	/**
	 * grows the valueHolder, so that you can efficiently add values with the "add" method
	 * 
	 * @param inc
	 *            the value to increment
	 */
	public void grow(final int inc) {
		if (size == 0 && inc == 1)
			return;

		Object arr[] = new Object[size + inc];
		if (values != null) {
			System.arraycopy(values, 0, arr, 0, size);
		}
		values = arr;
	}

	private void grow() {
		if (values == null) {
			values = new Object[0];

		} else if (values.length > size) {
			return;
		}
		Object arr[] = new Object[size + 1];
		System.arraycopy(values, 0, arr, 0, size);
		values = arr;
	}

	/**
	 * returns the error or null
	 * 
	 * @return the error or null
	 */
	public RuntimeException getError() {
		if (dataType == DataType.ERROR)
			return (RuntimeException) values[0];
		return null;
	}

	/**
	 * get the nth entry (0=first entry)
	 * 
	 * @param i
	 *            the position
	 * @return the entry as Object
	 */
	@Override
	@Deprecated
	public Object get(final int i) {
		switch (dataType) {
		case ERROR:
			throw (RuntimeException) values[0];
		case DOUBLE:
		case NUMBER:
			if (size == 1)
				return valueDbl;
			break;
		case INTEGER:
			if (size == 1)
				return valueInt;
		default:
			break;
		}
		if (size == 0) {
			return null; // TODO: What to do?
		} else if (i < size) {
			return values[i];
		} else {
			return values[size - 1];
		}
	}

	/**
	 * Returns the value at position i as String
	 * 
	 * @param i
	 *            the position
	 * @return the value as String
	 */
	public String getString(final int i) {
		if (dataType == DataType.STRING) {
			return (String) get(i);
		}
		throw new ClassCastException("STRING expected. Got '" + dataType + "'");
	}

	/**
	 * Returns the value at position i as DateTime
	 * 
	 */
	public DateTime getDateTime(final int i) {
		if (dataType == DataType.DATETIME) {
			return (DateTime) get(i);
		}
		throw new ClassCastException("DATETIME expected. Got '" + dataType + "'");
	}

	/**
	 * Returns the value at position i as Integer
	 * 
	 */
	public int getInt(final int i) {
		if (dataType == DataType.INTEGER) {
			if (i == 0)
				return valueInt;
			return (Integer) get(i);
		}
		if (dataType.numeric) {
			if (i == 0)
				return (int) valueDbl;
			return ((Number) get(i)).intValue();
		}
		throw new ClassCastException("INTEGER/NUMBER expected. Got '" + dataType + "'");
	}

	public double getDouble(final int i) {
		if (dataType == DataType.DOUBLE) {
			if (i == 0)
				return valueDbl;
		}
		if (dataType.numeric) {
			if (i == 0)
				return valueInt;
			return ((Number) get(i)).doubleValue();
		}
		throw new ClassCastException("DOUBLE/NUMBER expected. Got '" + dataType + "'");
	}

	public boolean isTrue(final FormulaContext ctx) {
		if (ctx.useBooleans) {
			if (dataType != DataType.BOOLEAN)
				throw new IllegalArgumentException("BOOLEAN expected. Got '" + dataType + "'");

			for (int i = 0; i < size; i++) {
				if ((Boolean) values[i]) {
					return true;
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				if (getInt(i) != 0) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * This is all optimized for performance
	 * 
	 */
	public boolean addAll(final ValueHolder other) {

		switch (other.dataType) {
		case _UNSET:
			return false; // we do not add unset

		case ERROR:
			setError(other.getError());
			return true;

		case DOUBLE:
		case NUMBER:
			if (other.size == 1) {
				return add(other.valueDbl);
			}

			// Check MY datatype if it is compatible with DOUBLE
			switch (dataType) {
			case DOUBLE:
			case NUMBER:
				break; // Type OK
			case INTEGER:
			case _UNSET:
				dataType = DataType.NUMBER;
				break;
			default:
				throw new ClassCastException(dataType + " expected. Got '" + other.dataType + "'");
			}
			grow(other.size);
			break;

		case INTEGER:
			if (other.size == 1) {
				return add(other.valueInt);
			}

			// Check MY datatype if it is compatible with INTEGER
			switch (dataType) {
			case DOUBLE:
			case _UNSET:
				dataType = DataType.NUMBER;
				break;
			case NUMBER:
			case INTEGER:
				break; // Type OK
			default:
				throw new ClassCastException(dataType + " expected. Got '" + other.dataType + "'");
			}
			grow(other.size);
			break;

		default:
			if (values == null) {
				values = new Object[other.size];
			} else {
				grow(other.size);
			}
			if (other.size == 1) {
				grow();
				values[size++] = other.values[0];
				return true;
			}
		}
		System.arraycopy(other.values, 0, values, size, other.size);
		size += other.size;
		return true;

	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#addAll(java.util.Collection)
	 */
	@Override
	@Deprecated
	public boolean addAll(final Collection<? extends Object> other) {
		// gives performance
		grow(other.size());
		return super.addAll(other);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return Arrays.toString(values);
	}

	@Override
	@Deprecated
	public int size() {
		return size;
	}

	// -- Add methods
	/**
	 * Adds the value as String. You have to ensure that you have called grow() before!
	 * 
	 */
	public boolean add(final String obj) {
		switch (dataType) {
		case ERROR:
			return false;
		case _UNSET:
			dataType = DataType.STRING;
			break;
		case STRING:
			break;
		default:
			throw new IllegalArgumentException("Cannot mix datatypes " + dataType + " and STRING");
		}
		if (size == 0)
			values = new Object[0];
		grow();
		values[size++] = obj;
		dirty = true;
		return true;
	}

	/**
	 * Adds an integer as value
	 * 
	 */
	public boolean add(final int value) {
		switch (dataType) {
		case ERROR:
			return false;
		case _UNSET: // size is zero here
			dataType = DataType.INTEGER;
			valueInt = value;
			size++;
			dirty = true;
			return true;

		case DOUBLE:
			dataType = DataType.NUMBER;
			if (size == 1) {
				values = new Object[1];
				values[0] = valueDbl;
			}
			break;
		case INTEGER:
			if (size == 1) {
				values = new Object[1];
				values[0] = valueInt;
			}
			break;
		case NUMBER:
			break;
		default:
			throw new IllegalArgumentException("Cannot mix datatypes " + dataType + " and INTEGER");
		}

		values[size++] = Integer.valueOf(value);
		dirty = true;
		return true;
	}

	/**
	 * Adds a double as value
	 */
	public boolean add(final double value) {
		switch (dataType) {
		case ERROR:
			return false;
		case _UNSET: // size is zero here
			dataType = DataType.DOUBLE;
			valueDbl = value;
			size++;
			dirty = true;
			return true;

		case DOUBLE:
			dataType = DataType.NUMBER;
			if (size == 1) {
				values = new Object[1];
				values[0] = valueDbl;
			}
			break;
		case INTEGER:
			if (size == 1) {
				values = new Object[1];
				values[0] = valueInt;
			}
			break;
		case NUMBER:
			break;
		default:
			throw new IllegalArgumentException("Cannot mix datatypes " + dataType + " and DOUBLE");
		}

		values[size++] = Double.valueOf(value);
		return true;
	}

	public boolean add(final Boolean bool) {
		switch (dataType) {
		case _UNSET:
			dataType = DataType.BOOLEAN;
			break;
		case BOOLEAN:
			break;
		default:
			throw new IllegalArgumentException("Cannot mix datatypes " + dataType + " and BOOLEAN");
		}
		if (size == 0)
			values = new Object[0];
		grow();

		values[size++] = bool;
		return true;
	}

	public boolean add(final DateTime bool) {
		// maybe we need calendar support here
		switch (dataType) {
		case _UNSET:
			dataType = DataType.DATETIME;
			break;
		case DATETIME:
			break;
		default:
			throw new IllegalArgumentException("Cannot mix datatypes " + dataType + " and DATETIME");
		}
		if (size == 0)
			values = new Object[0];
		values[size++] = bool;
		return true;
	}

	public void setError(final RuntimeException e) {
		dataType = DataType.ERROR;
		values = new Object[0];
		values[0] = e;
		size = 1;
	}

	/**
	 * Add anything as value. Better use the apropriate "add" method. it is faster
	 */
	@Override
	@Deprecated
	public boolean add(final Object obj) {
		if (dataType == DataType.ERROR) {
			return false;
		}

		if (obj instanceof String) {
			return add((String) obj);
		} else if (obj instanceof Integer) {
			return add(((Integer) obj).intValue());
		} else if (obj instanceof Number) {
			return add(((Number) obj).doubleValue());
		} else if (obj instanceof Boolean) {
			return add((Boolean) obj);
		} else if (obj instanceof DateTime) {
			return add((DateTime) obj);
		} else if (obj instanceof RuntimeException) {
			setError((RuntimeException) obj);
		} else {
			dataType = DataType.OBJECT;
			//throw new IllegalArgumentException("TODO: Datatype " + obj.getClass() + " is not yet supported");
		}

		if (values == null || size >= values.length) {
			throw new UnsupportedOperationException("Call grow first");
			//grow(4);
		}
		values[size++] = obj;
		return true;
	}

	@Override
	@Deprecated
	public void clear() {
		size = 0;
		dirty = true;
		dataType = DataType._UNSET;
	}

	@Override
	public Object[] toArray() {
		Object[] ret = new Object[size];

		System.arraycopy(values, 0, ret, 0, size);
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] arg0) {
		if (size > arg0.length) {
			Class<?> localClass = arg0.getClass().getComponentType();
			arg0 = (T[]) Array.newInstance(localClass, size);
		}
		System.arraycopy(values, 0, arg0, 0, size);
		if (size < arg0.length) {
			arg0[size] = null;
		}
		return arg0;
	}

	public static boolean hasMultiValues(final ValueHolder[] params) {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				if (params[i].size > 1)
					return true;
			}
		}
		return false;
	}
}
