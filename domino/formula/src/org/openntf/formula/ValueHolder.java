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
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

/**
 * This Valueholder is to hold single or multiple values.
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
public abstract class ValueHolder implements Serializable {
	private static final long serialVersionUID = 8290517470597891417L;

	/**
	 * These are the possible datatypes. <br>
	 * DOUBLE means 'only doubles', INTEGER means 'only integers'. If you mix Integers and Doubles, the type changes to "NUMBER"
	 */
	public enum DataType {
		ERROR, STRING, KEYWORD_STRING, INTEGER(true), DOUBLE(true), DATETIME, BOOLEAN, _UNSET, OBJECT, UNAVAILABLE;
		public boolean numeric = false;

		DataType() {
		}

		DataType(final boolean n) {
			numeric = n;
		}
	}

	private static ValueHolder nothing;

	private static ValueHolder unavailable;

	private static ValueHolder defaultVar;

	protected boolean immutable;

	// For performance reasons we allow direct access to these members
	public int size;
	public DataType dataType = DataType._UNSET;

	private EvaluateException currentError;

	// Caches
	protected static final ValueHolder TRUE;
	protected static final ValueHolder FALSE;

	protected static final ValueHolder integerCache[];
	protected static final ValueHolder stringCache[];

	/**
	 * Initializer to initialize some default ValueHolders
	 */
	static {
		TRUE = new ValueHolderBoolean(1);
		TRUE.add(Boolean.TRUE);
		TRUE.immutable = true;

		FALSE = new ValueHolderBoolean(1);
		FALSE.add(Boolean.FALSE);
		FALSE.immutable = true;

		integerCache = new ValueHolder[256];
		stringCache = new ValueHolder[256];

		nothing = new ValueHolderObject<Object>(1);
		nothing.add("");
		nothing.immutable = true;

		defaultVar = new ValueHolderObject<Object>(1);
		defaultVar.add("");
		defaultVar.immutable = true;

		unavailable = new ValueHolderObject<String>(1);
		unavailable.add("");
		unavailable.dataType = DataType.UNAVAILABLE;
		unavailable.immutable = true;

		for (int i = 0; i < 256; i++) {
			ValueHolder vhn = integerCache[i] = new ValueHolderNumber(1);
			vhn.add(i - 128);
			vhn.immutable = true;

			ValueHolder vho = stringCache[i] = new ValueHolderObject<Object>(1);
			if (i == 0) {
				vho.add("");
			} else {
				vho.add(String.valueOf((char) i));
			}
			vho.immutable = true;
		}

	}

	/**
	 * Need for serialization
	 */
	public ValueHolder() {
	}

	/**
	 * Create a new ValueHolder
	 * 
	 * @param clazz
	 *            the class to create the ValueHolder
	 * @param size
	 *            the size of the ValueHolder. You must specify the size at construction time
	 * @return a valueHOlder
	 */
	public static ValueHolder createValueHolder(final Class<?> clazz, final int size) {

		if (boolean.class.equals(clazz))
			return new ValueHolderBoolean(size);

		if (double.class.equals(clazz))
			return new ValueHolderNumber(size);

		if (int.class.equals(clazz))
			return new ValueHolderNumber(size);

		// the rest of the numeric values in JAVA -> handled as double
		if (byte.class.equals(clazz))
			return new ValueHolderNumber(size);
		if (char.class.equals(clazz))
			return new ValueHolderNumber(size);
		if (short.class.equals(clazz))
			return new ValueHolderNumber(size);
		if (long.class.equals(clazz))
			return new ValueHolderNumber(size);
		if (float.class.equals(clazz))
			return new ValueHolderNumber(size);

		if (clazz.isPrimitive()) {
			throw new UnsupportedOperationException("Cannot return objectholder for " + clazz);
		}

		if (Number.class.isAssignableFrom(clazz))
			return new ValueHolderNumber(size);

		if (Boolean.class.isAssignableFrom(clazz))
			return new ValueHolderBoolean(size);

		if (Character.class.isAssignableFrom(clazz))
			return new ValueHolderNumber(size);

		return new ValueHolderObject<Object>(size);

	}

	/**
	 * thows the current error, if there is one stored in the exception
	 * 
	 * @throws EvaluateException
	 * @throws UnavailableException
	 */
	protected void throwError() throws EvaluateException {
		if (currentError != null)
			throw currentError;
	}

	/**
	 * Init a ValueHolder based on a single value or a collection<br>
	 * If possible use one of the special "valueOf" constructors as these are faster
	 * 
	 */
	public static ValueHolder valueOf(final Object init) {
		if (init == null)
			return valueDefault();

		if (init instanceof String)
			return valueOf((String) init);

		if (init instanceof Integer)
			return valueOf(((Integer) init).intValue());

		if (init instanceof Number)
			return valueOf(((Number) init).doubleValue());

		if (init instanceof Boolean)
			return valueOf(((Boolean) init).booleanValue());

		ValueHolder vh = null;
		// Array handling and other objects
		if (init.getClass().isArray()) {
			int lh = Array.getLength(init);
			if (lh == 0)
				return valueDefault();
			if (lh == 1)
				return valueOf(Array.get(init, 0));

			for (int i = 0; i < lh; i++) {
				Object o = Array.get(init, i);
				if (o != null) {
					if (vh == null) {
						vh = createValueHolder(o.getClass(), lh);
					}
					vh.add(o);
				}
			}

		} else if (init instanceof Collection) {
			Collection<?> c = (Collection<?>) init;
			int lh = c.size();
			if (lh == 0)
				return valueDefault();
			if (lh == 1)
				return valueOf(c.iterator().next());

			for (Object o : c) {
				if (o != null) {
					if (vh == null) {
						vh = createValueHolder(o.getClass(), lh);
					}
					vh.add(o);
				}
			}

		} else {
			vh = createValueHolder(init.getClass(), 1);
			vh.add(init);
		}
		if (vh == null)
			return valueDefault();
		vh.immutable = true;
		return vh;
	}

	/**
	 * Initializes a new ValueHolder that holds a RuntimeException
	 * 
	 * @param init
	 *            the RuntimeException
	 * @return the Valuholder
	 */
	public static ValueHolder valueOf(final EvaluateException init) {
		ValueHolder vh = new ValueHolderObject<Object>(1);
		vh.dataType = DataType.ERROR;
		vh.currentError = init;
		vh.size = 1;
		vh.immutable = true;
		return vh;
	}

	/**
	 * Initializes a new ValueHolder that holds a integer value. Values -128..128 are cached.
	 * 
	 * @param init
	 *            the int value
	 * @return the Valuholder
	 */

	public static ValueHolder valueOf(final int init) {
		if (-128 <= init && init < 128) {
			return integerCache[init + 128];
		}
		ValueHolder vh = new ValueHolderNumber(1);
		vh.add(init);
		vh.immutable = true;
		return vh;
	}

	/**
	 * Initializes a new ValueHolder that holds a double value
	 * 
	 * @param init
	 *            the double
	 * @return the ValueHolder
	 */
	public static ValueHolder valueOf(final double init) {
		ValueHolder vh = new ValueHolderNumber(1);
		vh.add(init);
		vh.immutable = true;
		return vh;
	}

	/**
	 * Initializes a new ValueHolder that holds a String value
	 * 
	 * @param init
	 *            the String
	 * @return the ValueHolder
	 */
	public static ValueHolder valueOf(final String init) {
		if (init == null || init.length() == 0)
			return stringCache[0];
		if (init.length() == 1) {
			char ch = init.charAt(0);
			if (0 < ch && ch < 256)
				return stringCache[ch];
		}

		ValueHolder vh = new ValueHolderObject<String>(1);
		vh.add(init);
		vh.immutable = true;
		return vh;
	}

	/**
	 * Initializes a new ValueHolder that contains a DateTime
	 * 
	 * @param init
	 *            the DateTime
	 * @return the ValueHolder
	 */
	public static ValueHolder valueOf(final DateTime init) {
		ValueHolder vh = new ValueHolderObject<DateTime>(1);
		vh.add(init);
		vh.immutable = true;
		return vh;
	}

	/**
	 * Returns one of the two cached Boolean holders that represent TRUE or FALSE
	 * 
	 * @param init
	 *            the boolean.
	 * @return the ValueHolder
	 */
	public static ValueHolder valueOf(final boolean init) {
		if (init)
			return TRUE;
		return FALSE;
	}

	/**
	 * Returns the ValueHolder for the default value .
	 * 
	 * @return the ValueHolder
	 */

	public static ValueHolder valueDefault() {
		return defaultVar;
	}

	public static ValueHolder valueNothing() {
		return nothing;
	}

	/**
	 * Initializes a new ValueHolder that holds a RuntimeException
	 * 
	 * @param init
	 *            the RuntimeException
	 * @return the Valuholder
	 */
	public static ValueHolder valueUnavailable() {
		return unavailable;
	}

	/**
	 * returns the error or null
	 * 
	 * @return the error or null
	 */
	public EvaluateException getError() {
		return currentError;
	}

	/**
	 * get the nth entry (0=first entry)
	 * 
	 * @param i
	 *            the position
	 * @return the entry as Object
	 * 
	 * @Deprecated if you know the datatype, use the apropriate get-Method!
	 */
	@Deprecated
	public Object get(final int i) {
		switch (dataType) {
		case ERROR:
			return currentError;
		case DOUBLE:
			return getDouble(i);
		case INTEGER:
			return getInt(i);
		default:
			return getObject(i);
		}
	}

	/**
	 * Returns the stored value as Object.
	 * 
	 * @param i
	 *            the position
	 * @return the value as object
	 */
	public abstract Object getObject(final int i);

	/**
	 * Returns the value at position i as String
	 * 
	 * @param i
	 *            the position
	 * @return the value as String
	 */
	public String getString(final int i) {
		throw new ClassCastException("STRING expected. Got '" + dataType + "'");
	}

	/**
	 * Returns the value at position i as DateTime
	 */
	public DateTime getDateTime(final int i) {
		throw new ClassCastException("DATETIME expected. Got '" + dataType + "'");
	}

	/**
	 * Returns the value at position i as int
	 */
	public int getInt(final int i) {
		throw new ClassCastException("INTEGER expected. Got '" + dataType + "'");
	}

	/**
	 * Returns the value at position i as double
	 */
	public double getDouble(final int i) {
		throw new ClassCastException("DOUBLE expected. Got '" + dataType + "'");
	}

	/**
	 * Returns the value at position i as boolen
	 */
	public boolean getBoolean(final int i) {
		throw new ClassCastException("BOOLEAN expected. Got '" + dataType + "'");
	}

	/**
	 * Returns TRUE if one of the values is true
	 */
	public boolean isTrue(final FormulaContext ctx) {
		if (ctx.useBooleans) {
			for (int i = 0; i < size; i++) {
				if (getBoolean(i)) {
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

	/**
	 * Adds all values of an other ValueHolder
	 * 
	 */
	public boolean addAll(final ValueHolder other) {
		if (dataType == DataType.ERROR) {
			return false;
		} else if (other.dataType == DataType.ERROR) {
			dataType = DataType.ERROR;
			currentError = other.currentError;
			size = 1;
			return true;
		} else {
			throw new IllegalArgumentException("Cannot add " + other.dataType + " to " + dataType);
		}
	}

	/**
	 * Adds the value as String. You have to ensure that you have called grow() before!
	 * 
	 */
	public boolean add(final String obj) {
		throw new IllegalArgumentException("Cannot mix datatypes " + dataType + " and STRING");
	}

	/**
	 * Adds an integer as value
	 * 
	 */
	public boolean add(final int value) {
		throw new IllegalArgumentException("Cannot mix datatypes " + dataType + " and INTEGER");
	}

	/**
	 * Adds a double as value
	 */
	public boolean add(final double value) {
		throw new IllegalArgumentException("Cannot mix datatypes " + dataType + " and DOUBLE");
	}

	public boolean add(final boolean bool) {
		throw new IllegalArgumentException("Cannot mix datatypes " + dataType + " and BOOLEAN");
	}

	public boolean add(final DateTime bool) {
		throw new IllegalArgumentException("Cannot mix datatypes " + dataType + " and DATETIME");
	}

	/**
	 * throws an Exception if the ValueHolder is immutable
	 */
	protected void checkImmutable() {
		if (immutable)
			throw new UnsupportedOperationException("ValueHolder is immutable.");
	}

	/**
	 * Add anything as value. Better use the apropriate "add" method. it is faster
	 */
	@Deprecated
	public boolean add(final Object obj) {
		checkImmutable();

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
			return add(((Boolean) obj).booleanValue());

		} else if (obj instanceof DateTime) {
			return add((DateTime) obj);

			//} else if (obj instanceof RuntimeException) {
			//	setError((RuntimeException) obj);
		} else {
			dataType = DataType.OBJECT;
			throw new IllegalArgumentException("TODO: Datatype " + obj.getClass() + " is not yet supported");
		}
		//return true;
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

	public abstract List<Object> toList() throws EvaluateException;

	public abstract ValueHolder newInstance(int size2);

	public abstract void swap(int i, int j);

	public abstract String quoteValue() throws EvaluateException;

}
