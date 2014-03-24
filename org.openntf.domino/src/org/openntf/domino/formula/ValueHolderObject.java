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
import java.util.ArrayList;
import java.util.List;

import org.openntf.domino.DateTime;
import org.openntf.domino.exceptions.IHaveNoIdeaHowThisHappenedException;

/**
 * Valueholder to hold single or multiple values.
 * 
 * When evaluating a formula, every String/int/double value is wrapped in a "ValueHolder". The holder has several get-methods to return the
 * different types. You always must check the datatype before calling one of the getters, because a ValueHolder that contains Strings cannot
 * return
 * 
 * The code itself might look strange, but this was done to be as fast as possible
 * 
 * @author Roland Praml, Foconis AG
 * 
 */
public class ValueHolderObject<T> extends ValueHolder implements Serializable {
	private static final long serialVersionUID = 8290517470597891417L;

	private final Object values[];

	ValueHolderObject(final int size) {
		values = new Object[size];
	}

	/**
	 * Returns the object @
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getObject(final int i) {
		switch (dataType) {
		case ERROR:
			throw currentError;
		case _UNSET:
			return null;

		case BOOLEAN:
		case DOUBLE:
		case INTEGER:
			throw new IHaveNoIdeaHowThisHappenedException();

		default:
			if (i < size)
				return (T) values[i];
			return (T) values[size - 1];

		}
	}

	/**
	 * Returns the object as String
	 */
	@Override
	public String getString(final int i) {
		switch (dataType) {
		case ERROR:
			throw currentError;
		case STRING:
			if (i < size)
				return (String) values[i];
			return (String) values[size - 1];

		default:
			throw new ClassCastException("STRING expected. Got '" + dataType + "'");
		}

	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.formula.ValueHolder#getDateTime(int)
	 */
	@Override
	public DateTime getDateTime(final int i) {
		switch (dataType) {
		case ERROR:
			throw currentError;
		case DATETIME:
			if (i < size)
				return (DateTime) values[i];
			return (DateTime) values[size - 1];

		default:
			throw new ClassCastException("DATETIME expected. Got '" + dataType + "'");
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.formula.ValueHolder#add(java.lang.String)
	 */
	@Override
	public boolean add(final String obj) {
		switch (dataType) {
		case _UNSET:
			dataType = DataType.STRING;

		case STRING:
		case OBJECT:
			values[size++] = obj;
			return true;
		default:
			return super.add(obj);
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.formula.ValueHolder#add(org.openntf.domino.DateTime)
	 */
	@Override
	public boolean add(final DateTime dateTime) {
		switch (dataType) {
		case _UNSET:
			dataType = DataType.DATETIME;

		case DATETIME:
		case OBJECT:
			values[size++] = dateTime;
			return true;
		default:
			return super.add(dateTime);
		}

	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.formula.ValueHolder#add(java.lang.Object)
	 */
	@Deprecated
	@Override
	public boolean add(final Object obj) {
		if (obj instanceof String)
			return add((String) obj);
		if (obj instanceof DateTime)
			return add((DateTime) obj);

		switch (dataType) {
		case _UNSET:
			dataType = DataType.OBJECT;
		case OBJECT:
			values[size++] = obj;
			return true;
		default:
			return super.add(obj);
		}
	}

	@Override
	public boolean addAll(final ValueHolder other) {
		checkAdd();
		if (dataType == DataType.ERROR)
			return false;

		//System.out.println("Adding " + other.dataType + " to " + dataType);
		switch (other.dataType) {
		case _UNSET:
			return false; // we do not add unset

		case ERROR:
			setError(other.getError());
			return true;

		case DOUBLE:
		case INTEGER:
			return super.addAll(other);

		default:
			if (dataType == DataType._UNSET)
				dataType = other.dataType;
			if (dataType == DataType.OBJECT || dataType == other.dataType) {
				@SuppressWarnings("unchecked")
				ValueHolderObject<T> toAdd = (ValueHolderObject<T>) other;
				System.arraycopy(toAdd.values, 0, values, size, toAdd.size);
				size += other.size;
				return true;
			}
			return super.addAll(other);
		}
	}

	@Override
	public List<Object> toList() {
		List<Object> ret = new ArrayList<Object>(size);
		for (int i = 0; i < size; i++) {
			ret.add(values[i]);
		}
		return ret;
	}

	@Override
	public ValueHolder newInstance(final int size) {
		return new ValueHolderObject<Object>(size);
	}
}
