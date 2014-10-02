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
package org.openntf.formula;

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
public class ValueHolderNumber extends ValueHolder implements Serializable {
	private static final long serialVersionUID = 8290517470597891417L;

	private final int valuesI[];
	private final double valuesD[];

	ValueHolderNumber(final int size) {
		valuesI = new int[size];
		valuesD = new double[size];
	}

	@Override
	public Number getObject(final int i) {
		switch (dataType) {
		//case ERROR:
		//	throw currentError;

		case INTEGER:
			return getInt(i);

		case DOUBLE:
			return getDouble(i);

		default:
			throw new IllegalStateException(dataType + " is not supported here");

		}

	}

	/* (non-Javadoc)
	 * @see org.openntf.formula.ValueHolder#getInt(int)
	 */
	@Override
	public int getInt(final int i) {
		switch (dataType) {
		//case ERROR:
		//	throw currentError;
		case INTEGER:
			if (i < size) {
				return valuesI[i];
			} else {
				return valuesI[size - 1];
			}

		case DOUBLE:
			if (i < size) {
				return (int) valuesD[i];
			} else {
				return (int) valuesD[size - 1];
			}
		default:
			throw new IllegalStateException(dataType + " is not supported here");

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.formula.ValueHolder#getDouble(int)
	 */
	@Override
	public double getDouble(final int i) {
		switch (dataType) {
		//case ERROR:
		//	throw currentError;

		case INTEGER:
			if (i < size) {
				return valuesI[i];
			} else {
				return valuesI[size - 1];
			}

		case DOUBLE:
			if (i < size) {
				return valuesD[i];
			} else {
				return valuesD[size - 1];
			}
		default:
			throw new IllegalStateException(dataType + " is not supported here");

		}
	}

	/* (non-Javadoc)
	 * @see org.openntf.formula.ValueHolder#add(java.lang.Boolean)
	 */
	@Override
	public boolean add(final int i) {
		checkImmutable();

		switch (dataType) {
		case ERROR:
			return false;

		case _UNSET:
			dataType = DataType.INTEGER;
			// fall through
		case INTEGER:
			valuesI[size] = i;
			break;
		case DOUBLE:
			break;
		default:
			return super.add(i);
		}
		valuesD[size++] = i; // We also save the int as double 
		return true;
	}

	/* (non-Javadoc)
	 * @see org.openntf.formula.ValueHolder#add(java.lang.Boolean)
	 */
	@Override
	public boolean add(final double d) {
		checkImmutable();

		switch (dataType) {
		case ERROR:
			return false;

		case _UNSET:
		case INTEGER:
			if (Integer.MIN_VALUE < d && d < Integer.MAX_VALUE && d == (valuesI[size] = (int) d)) {
				dataType = DataType.INTEGER;
			} else {
				dataType = DataType.DOUBLE; // upconvert to double
			}

			break;
		case DOUBLE:
			break;

		default:
			return super.add(d);
		}
		valuesD[size++] = d; // We also save the int as double 
		return true;
	}

	@Override
	public boolean addAll(final ValueHolder other) {
		checkImmutable();
		if (dataType == DataType.ERROR)
			return false;

		switch (other.dataType) {
		case _UNSET:
			return false; // we do not add unset

		case DOUBLE:
			dataType = DataType.DOUBLE;
			break;

		case INTEGER:
			if (dataType != DataType.DOUBLE) {
				// do not downconvert
				dataType = DataType.INTEGER;
			}
			break;
		default:
			return super.addAll(other);
		}
		ValueHolderNumber toAdd = (ValueHolderNumber) other;
		System.arraycopy(toAdd.valuesI, 0, valuesI, size, toAdd.size);
		System.arraycopy(toAdd.valuesD, 0, valuesD, size, toAdd.size);
		size += other.size;
		return true;
	}

	@Override
	public List<Object> toList() throws EvaluateException {
		throwError();
		List<Object> ret = new ArrayList<Object>(size);
		if (dataType == DataType.INTEGER) {
			for (int i = 0; i < size; i++) {
				ret.add(valuesI[i]);
			}
		} else {
			for (int i = 0; i < size; i++) {
				ret.add(valuesD[i]);
			}
		}
		return ret;
	}

	@Override
	public ValueHolder newInstance(final int size) {
		return new ValueHolderNumber(size);
	}

	@Override
	public void swap(final int i, final int j) {
		int tmpI = valuesI[i];
		valuesI[i] = valuesI[j];
		valuesI[j] = tmpI;

		double tmpD = valuesD[i];
		valuesD[i] = valuesD[j];
		valuesD[j] = tmpD;
	}

	@Override
	public String quoteValue() throws EvaluateException {
		throwError();
		StringBuilder sb = new StringBuilder();

		if (dataType == DataType.DOUBLE) {
			// TODO: this does not work yet for DE
			throw new UnsupportedOperationException("This is not yet locale compatible. So it is not implemented.");
		}
		sb.append(getInt(0));
		for (int i = 1; i < valuesD.length; i++) {
			sb.append(':');
			sb.append(getInt(i));
		}

		return sb.toString();
	}

}
