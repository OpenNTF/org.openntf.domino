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
public class ValueHolderBoolean extends ValueHolder implements Serializable {
	private static final long serialVersionUID = 8290517470597891417L;

	private final boolean values[];

	ValueHolderBoolean(final int size) {
		values = new boolean[size];
	}

	@Override
	public Boolean getObject(final int i) {
		if (size == 0) {
			return null; // TODO: What to do?
		} else if (i < size) {
			return values[i];
		} else {
			return values[size - 1];
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.formula.ValueHolder#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(final int i) {
		if (size == 0) {
			return false; // TODO: What to do?
		} else if (i < size) {
			return values[i];
		} else {
			return values[size - 1];
		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.domino.formula.ValueHolder#add(java.lang.Boolean)
	 */
	@Override
	public boolean add(final boolean bool) {
		checkImmutable();

		switch (dataType) {
		case ERROR:
			return false;

		case _UNSET:
			dataType = DataType.BOOLEAN;

		case BOOLEAN:
			break;

		default:
			return super.add(bool); // this throws an exception
		}
		values[size++] = bool;
		return true;
	}

	@Override
	public boolean addAll(final ValueHolder other) {
		switch (other.dataType) {
		case _UNSET:
			return false; // we do not add unset

		case BOOLEAN:
			ValueHolderBoolean toAdd = (ValueHolderBoolean) other;
			System.arraycopy(toAdd.values, 0, values, size, toAdd.size);
			size += other.size;
			return true;

		default:
			return super.addAll(other); // This throws an error
		}
	}

	@Override
	public List<Object> toList() throws EvaluateException {
		throwError();
		List<Object> ret = new ArrayList<Object>(size);
		for (int i = 0; i < size; i++) {
			ret.add(values[i]);
		}
		return ret;
	}

	@Override
	public ValueHolder newInstance(final int size) {
		return new ValueHolderBoolean(size);
	}

	@Override
	public void swap(final int i, final int j) {
		boolean tmp = values[i];
		values[i] = values[j];
		values[j] = tmp;
	}

}
