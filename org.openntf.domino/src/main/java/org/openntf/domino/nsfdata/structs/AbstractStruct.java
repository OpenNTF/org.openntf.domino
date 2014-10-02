package org.openntf.domino.nsfdata.structs;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import com.ibm.commons.util.StringUtil;

public abstract class AbstractStruct implements Externalizable {
	private ByteBuffer data_;

	public AbstractStruct() {
		byte[] byteData = new byte[(int) getStructSize()];
		data_ = ByteBuffer.wrap(byteData).order(ByteOrder.LITTLE_ENDIAN);
	}

	public AbstractStruct(final ByteBuffer data) {
		data_ = data.duplicate();
		data_.order(ByteOrder.LITTLE_ENDIAN);
	}

	public ByteBuffer getData() {
		return data_;
	}

	protected void setData(final byte[] data) {
		data_ = ByteBuffer.wrap(data);
	}

	protected void setData(final ByteBuffer data) {
		data_ = data.duplicate().order(ByteOrder.LITTLE_ENDIAN);
	}

	protected byte[] getBytes() {
		ByteBuffer data = getData().duplicate();
		int size = data.limit() - data.position();
		byte[] result = new byte[size];
		data.get(result);
		return result;
	}

	public abstract long getStructSize();

	@Override
	public String toString() {
		return buildDebugString();
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		int len = in.readInt();
		byte[] storage = new byte[len];
		in.read(storage);
		data_ = ByteBuffer.wrap(storage);
		data_.order(ByteOrder.LITTLE_ENDIAN);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		byte[] storage = getBytes();
		data_.get(storage);
		out.writeInt(storage.length);
		out.write(storage);
	}

	/* ******************************************************************************
	 * Internal structure methods
	 ********************************************************************************/

	private static interface Element {
		Class<?> getDataClass();

		boolean isUpgrade();

		boolean isArray();
	}

	/**
	 * This class represents a fixed-length element in the static structure definition.
	 * 
	 * @author jgallagher
	 *
	 */
	private static class FixedElement implements Element {
		private final String name;
		private final Class<?> dataClass;
		private final boolean upgrade;
		private final int count;

		private FixedElement(final String name, final Class<?> sizeClass, final boolean upgrade, final int count) {
			this.name = name;
			this.dataClass = sizeClass;
			this.upgrade = upgrade;
			this.count = count;
		}

		@Override
		public Class<?> getDataClass() {
			return dataClass;
		}

		@Override
		public boolean isUpgrade() {
			return upgrade;
		}

		@Override
		public boolean isArray() {
			return count > 1;
		}

		@Override
		public int hashCode() {
			return 10 + name.hashCode() + dataClass.hashCode() + (upgrade ? 2 : 1) + count;
		}
	}

	/**
	 * This class represents a variable-length element in the static structure definition.
	 * 
	 * @author jgallagher
	 *
	 */
	private static class VariableElement implements Element {
		private final String name;
		private final String lengthMethodName;
		private final Class<?> dataClass;
		private final boolean isAscii;

		private VariableElement(final String name, final String lengthMethodName, final Class<?> dataClass, final boolean isAscii) {
			this.name = name;
			this.lengthMethodName = lengthMethodName;
			this.dataClass = dataClass;
			this.isAscii = isAscii;
		}

		@Override
		public Class<?> getDataClass() {
			return dataClass;
		}

		@Override
		public boolean isUpgrade() {
			return false;
		}

		@Override
		public boolean isArray() {
			return !String.class.equals(dataClass);
		}

		@Override
		public int hashCode() {
			return 11 + name.hashCode() + lengthMethodName.hashCode() + dataClass.hashCode() + (isAscii ? 2 : 1);
		}
	}

	private static final Map<String, Collection<FixedElement>> fixedElements_ = new HashMap<String, Collection<FixedElement>>();
	private static final Map<String, Collection<VariableElement>> variableElements_ = new HashMap<String, Collection<VariableElement>>();

	protected static void addFixed(final String name, final Class<?> sizeClass) {
		Exception e = new Exception();
		String caller = e.getStackTrace()[1].getClassName();
		if (!fixedElements_.containsKey(caller)) {
			fixedElements_.put(caller, new LinkedHashSet<FixedElement>());
		}
		fixedElements_.get(caller).add(new FixedElement(name, sizeClass, false, 1));
	}

	protected static void addFixedUnsigned(final String name, final Class<?> sizeClass) {
		Exception e = new Exception();
		String caller = e.getStackTrace()[1].getClassName();
		if (!fixedElements_.containsKey(caller)) {
			fixedElements_.put(caller, new LinkedHashSet<FixedElement>());
		}
		fixedElements_.get(caller).add(new FixedElement(name, sizeClass, true, 1));
	}

	protected static void addFixedArray(final String name, final Class<?> sizeClass, final int size) {
		Exception e = new Exception();
		String caller = e.getStackTrace()[1].getClassName();
		if (!fixedElements_.containsKey(caller)) {
			fixedElements_.put(caller, new LinkedHashSet<FixedElement>());
		}
		fixedElements_.get(caller).add(new FixedElement(name, sizeClass, false, size));
	}

	protected static void addFixedArrayUnsigned(final String name, final Class<?> sizeClass, final int size) {
		Exception e = new Exception();
		String caller = e.getStackTrace()[1].getClassName();
		if (!fixedElements_.containsKey(caller)) {
			fixedElements_.put(caller, new LinkedHashSet<FixedElement>());
		}
		fixedElements_.get(caller).add(new FixedElement(name, sizeClass, true, size));
	}

	/**
	 * @param name
	 *            The name of the field, used in "getStructElement" calls
	 * @param lengthMethodName
	 *            The name of a method that can be called to get a int of the length in bytes
	 */
	protected static void addVariableData(final String name, final String lengthMethodName) {
		Exception e = new Exception();
		String caller = e.getStackTrace()[1].getClassName();
		if (!variableElements_.containsKey(caller)) {
			variableElements_.put(caller, new LinkedHashSet<VariableElement>());
		}
		variableElements_.get(caller).add(new VariableElement(name, lengthMethodName, Byte.class, false));
	}

	protected static void addVariableArray(final String name, final String lengthMethodName, final Class<?> dataClass) {
		Exception e = new Exception();
		String caller = e.getStackTrace()[1].getClassName();
		if (!variableElements_.containsKey(caller)) {
			variableElements_.put(caller, new LinkedHashSet<VariableElement>());
		}
		variableElements_.get(caller).add(new VariableElement(name, lengthMethodName, dataClass, false));
	}

	/**
	 * @param name
	 *            The name of the field, used in "getStructElement" calls
	 * @param lengthMethodName
	 *            The name of a method that can be called to get a int of the length in bytes
	 */
	protected static void addVariableString(final String name, final String lengthMethodName) {
		Exception e = new Exception();
		String caller = e.getStackTrace()[1].getClassName();
		if (!variableElements_.containsKey(caller)) {
			variableElements_.put(caller, new LinkedHashSet<VariableElement>());
		}
		variableElements_.get(caller).add(new VariableElement(name, lengthMethodName, String.class, false));
	}

	/**
	 * @param name
	 *            The name of the field, used in "getStructElement" calls
	 * @param lengthMethodName
	 *            The name of a method that can be called to get a int of the length in bytes
	 */
	protected static void addVariableAsciiString(final String name, final String lengthMethodName) {
		Exception e = new Exception();
		String caller = e.getStackTrace()[1].getClassName();
		if (!variableElements_.containsKey(caller)) {
			variableElements_.put(caller, new LinkedHashSet<VariableElement>());
		}
		variableElements_.get(caller).add(new VariableElement(name, lengthMethodName, String.class, true));
	}

	protected static int getFixedStructSize(final String className) {
		int result = 0;
		if (fixedElements_.containsKey(className)) {
			Collection<FixedElement> elements = fixedElements_.get(className);
			for (FixedElement element : elements) {
				int size = _getSize(element.dataClass);
				result += size * element.count;
			}
		}
		return result;
	}

	protected static int getFixedStructSize() {
		Exception e = new Exception();
		String caller = e.getStackTrace()[1].getClassName();
		return getFixedStructSize(caller);
	}

	protected Object getStructElement(final String name) {
		int preceding = 0;

		// Look through the fixed elements first, building up the preceding byte count while we're at it
		Collection<FixedElement> fixedElements = fixedElements_.get(getClass().getName());
		if (fixedElements != null) {
			for (FixedElement element : fixedElements) {
				int size = _getSize(element.dataClass);
				if (StringUtil.equals(name, element.name)) {
					Object[] result = new Object[element.count];

					for (int i = 0; i < element.count; i++) {
						Object primitive = _getPrimitive(element.dataClass, preceding + (size * i), element.upgrade);
						if (primitive != null) {
							result[i] = primitive;
						} else {
							// Then it's a struct
							ByteBuffer data = getData().duplicate();
							data.order(ByteOrder.LITTLE_ENDIAN);
							data.position(data.position() + preceding + (size * i));
							data.limit(data.position() + size);
							try {
								result[i] = element.dataClass.getDeclaredConstructor(ByteBuffer.class).newInstance(data);
							} catch (Throwable t) {
								throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
							}
						}
					}

					if (element.count == 1) {
						return result[0];
					} else if (_isPrimitive(element.dataClass)) {
						if (element.upgrade) {
							if (Byte.class.equals(element.dataClass)) {
								return _toPrimitiveArray(result, Short.class);
							} else if (Short.class.equals(element.dataClass)) {
								return _toPrimitiveArray(result, Integer.class);
							} else if (Integer.class.equals(element.dataClass)) {
								return _toPrimitiveArray(result, Long.class);
							} else if (Float.class.equals(element.dataClass)) {
								return _toPrimitiveArray(result, Double.class);
							} else {
								return _toPrimitiveArray(result, element.dataClass);
							}
						} else {
							return _toPrimitiveArray(result, element.dataClass);
						}
					} else {
						// Then switch it to an array of the appropriate class
						Object structArray = Array.newInstance(element.dataClass, result.length);
						for (int i = 0; i < result.length; i++) {
							Array.set(structArray, i, result[i]);
						}
						return structArray;
					}
				} else {
					preceding += size * element.count;
				}
			}
		}

		// Now see if it's one of the variable-length bits
		Collection<VariableElement> varElements = variableElements_.get(getClass().getName());
		if (varElements != null) {
			for (VariableElement element : varElements) {
				try {
					int length = -1;
					boolean found = false;
					// The length method name could either be the name of a fixed variable or a method
					for (FixedElement fixElem : fixedElements_.get(getClass().getName())) {
						if (StringUtil.equals(fixElem.name, element.lengthMethodName)) {
							length = (Integer) getStructElement(element.lengthMethodName);
							found = true;
						}
					}
					if (!found) {
						Method method = getClass().getDeclaredMethod(element.lengthMethodName);
						length = (Integer) method.invoke(this);
					}
					int size = String.class.equals(element.dataClass) ? 1 : _getSize(element.dataClass);

					// LMBCS strings are always even length
					int extra = String.class.equals(element.dataClass) && !element.isAscii ? length % 2 : 0;

					if (StringUtil.equals(name, element.name)) {
						if (String.class.equals(element.dataClass)) {
							ByteBuffer data = getData().duplicate();
							data.order(ByteOrder.LITTLE_ENDIAN);
							data.position(data.position() + preceding);
							data.limit(data.position() + length);
							if (element.isAscii) {
								byte[] chars = new byte[length];
								data.get(chars);
								return new String(chars, Charset.forName("US-ASCII"));
							} else {
								return ODSUtils.fromLMBCS(data);
							}
						} else {
							Object[] result = new Object[length];
							for (int i = 0; i < length; i++) {
								Object primitive = _getPrimitive(element.dataClass, preceding + (size * i), false);
								if (primitive != null) {
									result[i] = primitive;
								} else {
									// Then it's a struct
									ByteBuffer data = getData().duplicate();
									data.order(ByteOrder.LITTLE_ENDIAN);
									data.position(data.position() + preceding + (size * i));
									data.limit(data.position() + size);
									try {
										result[i] = element.dataClass.getDeclaredConstructor(ByteBuffer.class).newInstance(data);
									} catch (Throwable t) {
										throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
									}
								}
							}

							if (_isPrimitive(element.dataClass)) {
								return _toPrimitiveArray(result, element.dataClass);
							} else {
								// TODO see if there's a better way
								Object resultArray = Array.newInstance(element.dataClass, length);
								for (int i = 0; i < length; i++) {
									Array.set(resultArray, i, result[i]);
								}
								return resultArray;
							}
						}
					} else {
						preceding += (size * length) + extra;
					}
				} catch (Throwable t) {
					throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
				}
			}
		}

		return null;
	}

	/**
	 * @return The size in bytes of the stored struct element
	 */
	protected int setStructElement(final String name, final Object value) {
		int preceding = 0;

		// Look through the fixed elements first, building up the preceding byte count while we're at it
		Collection<FixedElement> fixedElements = fixedElements_.get(getClass().getName());
		if (fixedElements != null) {
			for (FixedElement element : fixedElements) {
				int size = _getSize(element.dataClass);
				if (StringUtil.equals(name, element.name)) {
					// First, make sure the value is correct
					if (!_matchesType(value, element)) {
						// value will not be null here
						throw new IllegalArgumentException("Value class '" + value.getClass().getName()
								+ "' does not match expected class '" + element.dataClass.getName() + "'");
					}

					// Now, actually set the value
					ByteBuffer data = getData().duplicate().order(ByteOrder.LITTLE_ENDIAN);
					data.position(data.position() + preceding);
					if (value == null) {
						// Then zero out the appropriate number of bytes
						for (int i = 0; i < size * element.count; i++) {
							data.put(data.position() + i, (byte) 0);
						}
					} else {
						// Then set the value properly

						// Convert to an array first for consistency
						Object arrayValue = _toArrayType(value);

						// Since we always have an array type, loop through it to pour in the data
						for (int i = 0; i < Array.getLength(arrayValue); i++) {
							Object val = Array.get(arrayValue, i);
							if (Byte.class.equals(element.dataClass)) {
								data.put(((Number) val).byteValue());
							} else if (Short.class.equals(element.dataClass)) {
								data.putShort(((Number) val).shortValue());
							} else if (Integer.class.equals(element.dataClass)) {
								data.putInt(((Number) val).intValue());
							} else if (Long.class.equals(element.dataClass)) {
								data.putLong(((Number) val).longValue());
							} else if (Float.class.equals(element.dataClass)) {
								data.putFloat(((Number) val).floatValue());
							} else if (Double.class.equals(element.dataClass)) {
								data.putDouble(((Number) val).doubleValue());
							} else {
								ByteBuffer structData = ((AbstractStruct) val).getData().duplicate();
								data.put(structData);
							}
						}
					}

					// Then we're done
					return size * element.count;
				} else {
					preceding += size * element.count;
				}
			}
		}

		// Now see if it's one of the variable-length bits
		Collection<VariableElement> varElements = variableElements_.get(getClass().getName());
		if (varElements != null) {
			for (VariableElement element : varElements) {
				try {
					int length = -1;
					boolean found = false;
					// The length method name could either be the name of a fixed variable or a method
					for (FixedElement fixElem : fixedElements_.get(getClass().getName())) {
						if (StringUtil.equals(fixElem.name, element.lengthMethodName)) {
							length = (Integer) getStructElement(element.lengthMethodName);
							found = true;
						}
					}
					if (!found) {
						Method method = getClass().getDeclaredMethod(element.lengthMethodName);
						length = (Integer) method.invoke(this);
					}
					int size = String.class.equals(element.dataClass) ? 1 : _getSize(element.dataClass);

					// LMBCS strings are always even length
					int extra = String.class.equals(element.dataClass) && !element.isAscii ? length % 2 : 0;

					if (StringUtil.equals(name, element.name)) {
						ByteBuffer data = getData();
						byte[] replacedBytes;
						if (value == null) {
							// Then outright remove the data
							// We'll have to split and re-combine the underlying array
							replacedBytes = new byte[0];
						} else {
							// The paths for strings and non-strings are quite different, but both result in a byte array
							if (String.class.equals(element.dataClass)) {
								String stringVal = String.valueOf(value);
								if (element.isAscii) {
									replacedBytes = stringVal.getBytes(Charset.forName("US-ASCII"));
								} else {
									replacedBytes = ODSUtils.toLMBCS(stringVal).array();
								}
							} else {
								Object arrayValue = _toArrayType(value);
								replacedBytes = new byte[size * Array.getLength(arrayValue)];
								ByteBuffer outData = ByteBuffer.wrap(replacedBytes).order(ByteOrder.LITTLE_ENDIAN);
								for (int i = 0; i < Array.getLength(arrayValue); i++) {
									Object val = Array.get(arrayValue, i);
									if (Byte.class.equals(element.dataClass)) {
										outData.put(((Number) val).byteValue());
									} else if (Short.class.equals(element.dataClass)) {
										outData.putShort(((Number) val).shortValue());
									} else if (Integer.class.equals(element.dataClass)) {
										outData.putInt(((Number) val).intValue());
									} else if (Long.class.equals(element.dataClass)) {
										outData.putLong(((Number) val).longValue());
									} else if (Float.class.equals(element.dataClass)) {
										outData.putFloat(((Number) val).floatValue());
									} else if (Double.class.equals(element.dataClass)) {
										outData.putDouble(((Number) val).doubleValue());
									} else {
										ByteBuffer structData = ((AbstractStruct) val).getData().duplicate();
										outData.put(structData);
									}
								}
							}
						}

						// Check if the result size is different from the original
						if (replacedBytes.length == size) {
							// If it's the same, the job is easy
							data.position(data.position() + preceding);
							data.put(replacedBytes);
						} else {
							// Otherwise, we have to break apart the array and stitch it together
							// Create an array at the new total size
							int newLength = data.capacity() - size + replacedBytes.length + 1;
							byte[] newBytes = new byte[newLength];

							// Pour in the data before this element
							int start = data.position() + preceding;
							data.position(0);
							data.get(newBytes, 0, start);

							// Write this element's data
							System.out.println("replacedBytes.length=" + replacedBytes.length);
							System.out.println("newBytes.length=" + newBytes.length);
							System.out.println("start=" + start);
							System.arraycopy(replacedBytes, 0, newBytes, start, replacedBytes.length);

							// Write any data from after this element
							int remaining = newBytes.length - start - replacedBytes.length;
							if (remaining > 0) {
								data.position(data.position() + remaining);
								data.get(newBytes, start, remaining);
							}

							this.setData(newBytes);
						}

						// Then we're done
						return replacedBytes.length;
					} else {
						preceding += (size * length) + extra;
					}
				} catch (Throwable t) {
					throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
				}
			}
		}
		return 0;
	}

	private static Object _toArrayType(final Object value) {
		Object arrayValue;
		if (!value.getClass().isArray()) {
			arrayValue = Array.newInstance(value.getClass(), 1);
			Array.set(arrayValue, 0, value);
		} else {
			arrayValue = value;
		}
		return arrayValue;
	}

	private static int _getSize(final Class<?> sizeClass) {
		if (Byte.class.equals(sizeClass)) {
			return 1;
		} else if (Short.class.equals(sizeClass)) {
			return 2;
		} else if (Integer.class.equals(sizeClass)) {
			return 4;
		} else if (Long.class.equals(sizeClass)) {
			return 8;
		} else if (Float.class.equals(sizeClass)) {
			return 4;
		} else if (Double.class.equals(sizeClass)) {
			return 8;
		} else {
			Field field = null;
			try {
				field = sizeClass.getField("SIZE");
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
			}
			if (field != null) {
				try {
					return field.getInt(sizeClass);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		throw new UnsupportedOperationException("Unknown size class " + sizeClass);
	}

	private static boolean _isPrimitive(final Class<?> clazz) {
		return Byte.class.equals(clazz) || Short.class.equals(clazz) || Integer.class.equals(clazz) || Long.class.equals(clazz)
				|| Float.class.equals(clazz) || Double.class.equals(clazz);
	}

	private Object _getPrimitive(final Class<?> sizeClass, final int preceding, final boolean upgrade) {
		if (Byte.class.equals(sizeClass)) {
			byte value = getData().get(getData().position() + preceding);
			if (upgrade) {
				return (short) (value & 0xFF);
			} else {
				return value;
			}
		} else if (Short.class.equals(sizeClass)) {
			short value = getData().getShort(getData().position() + preceding);
			if (upgrade) {
				return value & 0xFFFF;
			} else {
				return value;
			}
		} else if (Integer.class.equals(sizeClass)) {
			int value = getData().getInt(getData().position() + preceding);
			if (upgrade) {
				return (long) (value & 0xFFFFFFFF);
			} else {
				return value;
			}
		} else if (Long.class.equals(sizeClass)) {
			// Ignore upgrade
			return getData().getLong(getData().position() + preceding);
		} else if (Float.class.equals(sizeClass)) {
			// Ignore upgrade
			return getData().getFloat(getData().position() + preceding);
		} else if (Double.class.equals(sizeClass)) {
			// Ignore upgrade
			return getData().getDouble(getData().position() + preceding);
		}
		return null;
	}

	// TODO check if any stdlib array-copy methods do this unboxing
	private static Object _toPrimitiveArray(final Object[] value, final Class<?> primitiveClass) {
		if (Byte.class.equals(primitiveClass)) {
			byte[] result = new byte[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = (Byte) value[i];
			}
			return result;
		} else if (Short.class.equals(primitiveClass)) {
			short[] result = new short[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = (Short) value[i];
			}
			return result;
		} else if (Integer.class.equals(primitiveClass)) {
			int[] result = new int[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = (Integer) value[i];
			}
			return result;
		} else if (Long.class.equals(primitiveClass)) {
			long[] result = new long[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = (Long) value[i];
			}
			return result;
		} else if (Float.class.equals(primitiveClass)) {
			float[] result = new float[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = (Float) value[i];
			}
			return result;
		} else if (Double.class.equals(primitiveClass)) {
			double[] result = new double[value.length];
			for (int i = 0; i < value.length; i++) {
				result[i] = (Double) value[i];
			}
			return result;
		}
		return value;
	}

	private static boolean _matchesType(final Object value, final Element element) {
		if (value == null) {
			// null means "zero out"
			return true;
		}

		Class<?> baseClass;
		if (element.isUpgrade()) {
			baseClass = _upgradeClass(element.getDataClass());
		} else {
			baseClass = element.getDataClass();
		}
		if (element.isArray()) {
			Object arrayObj = Array.newInstance(baseClass, 0);
			baseClass = arrayObj.getClass();
		}
		return baseClass.equals(value.getClass());
	}

	private static Class<?> _upgradeClass(final Class<?> dataClass) {
		if (Byte.class.equals(dataClass)) {
			return Short.class;
		} else if (Byte[].class.equals(dataClass)) {
			return Short[].class;
		} else if (Short.class.equals(dataClass)) {
			return Integer.class;
		} else if (Short[].class.equals(dataClass)) {
			return Integer[].class;
		} else if (Integer.class.equals(dataClass)) {
			return Long.class;
		} else if (Integer[].class.equals(dataClass)) {
			return Long[].class;
		} else if (Float.class.equals(dataClass)) {
			return Double.class;
		} else if (Float[].class.equals(dataClass)) {
			return Double[].class;
		} else {
			return dataClass;
		}
	}

	protected String buildDebugString(final String... properties) {
		StringBuilder result = new StringBuilder();
		result.append("[");
		result.append(getClass().getSimpleName());
		result.append(": ");
		boolean addedProp = false;
		for (String property : properties) {
			if (addedProp) {
				result.append(", ");
			} else {
				addedProp = true;
			}
			result.append(property);
			result.append("=");

			try {
				Method getter = getClass().getMethod("get" + property);
				result.append(getter.invoke(this));
			} catch (Throwable t) {
				throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
			}
		}
		result.append("]");
		return result.toString();
	}

	protected String buildDebugString() {
		StringBuilder result = new StringBuilder();
		result.append("[");
		result.append(getClass().getSimpleName());
		result.append(": ");

		boolean addedProp = false;

		if (fixedElements_.containsKey(getClass().getName())) {
			for (FixedElement element : fixedElements_.get(getClass().getName())) {
				if (addedProp) {
					result.append(", ");
				} else {
					addedProp = true;
				}

				result.append(element.name);
				result.append("=");
				result.append(getStructElement(element.name));
			}
		}

		if (variableElements_.containsKey(getClass().getName())) {
			for (VariableElement element : variableElements_.get(getClass().getName())) {
				if (addedProp) {
					result.append(", ");
				} else {
					addedProp = true;
				}

				result.append(element.name);
				result.append("=");
				result.append(getStructElement(element.name));
			}
		}

		result.append("]");
		return result.toString();
	}
}
