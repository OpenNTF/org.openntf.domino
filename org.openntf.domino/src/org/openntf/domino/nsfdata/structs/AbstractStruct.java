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
		byte[] storage = new byte[data_.limit() - data_.position()];
		data_.get(storage);
		out.writeInt(storage.length);
		out.write(storage);
	}

	/* ******************************************************************************
	 * Internal structure methods
	 ********************************************************************************/

	/**
	 * This class represents a fixed-length element in the static structure definition.
	 * 
	 * @author jgallagher
	 *
	 */
	private static class FixedElement {
		private final String name;
		private final Class<?> sizeClass;
		private final boolean upgrade;
		private final int count;

		private FixedElement(final String name, final Class<?> sizeClass, final boolean upgrade, final int count) {
			this.name = name;
			this.sizeClass = sizeClass;
			this.upgrade = upgrade;
			this.count = count;
		}

		@Override
		public int hashCode() {
			return 10 + name.hashCode() + sizeClass.hashCode() + (upgrade ? 2 : 1) + count;
		}
	}

	/**
	 * This class represents a variable-length element in the static structure definition.
	 * 
	 * @author jgallagher
	 *
	 */
	private static class VariableElement {
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

	protected Object getStructElement(final String name) {
		int preceding = 0;

		// Look through the fixed elements first, building up the preceding byte count while we're at it
		for (FixedElement element : fixedElements_.get(getClass().getName())) {
			int size = _getSize(element.sizeClass);
			if (StringUtil.equals(name, element.name)) {
				Object[] result = new Object[element.count];

				for (int i = 0; i < element.count; i++) {
					Object primitive = _getPrimitive(element.sizeClass, preceding + (size * i), element.upgrade);
					if (primitive != null) {
						result[i] = primitive;
					} else {
						// Then it's a struct
						ByteBuffer data = getData().duplicate();
						data.order(ByteOrder.LITTLE_ENDIAN);
						data.position(data.position() + preceding + (size * i));
						data.limit(data.position() + size);
						try {
							result[i] = element.sizeClass.getDeclaredConstructor(ByteBuffer.class).newInstance(data);
						} catch (Throwable t) {
							throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
						}
					}
				}

				if (element.count == 1) {
					return result[0];
				} else if (_isPrimitive(element.sizeClass)) {
					if (element.upgrade) {
						if (Byte.class.equals(element.sizeClass)) {
							return _toPrimitiveArray(result, Short.class);
						} else if (Short.class.equals(element.sizeClass)) {
							return _toPrimitiveArray(result, Integer.class);
						} else if (Integer.class.equals(element.sizeClass)) {
							return _toPrimitiveArray(result, Long.class);
						} else if (Float.class.equals(element.sizeClass)) {
							return _toPrimitiveArray(result, Double.class);
						} else {
							return _toPrimitiveArray(result, element.sizeClass);
						}
					} else {
						return _toPrimitiveArray(result, element.sizeClass);
					}
				} else {
					// Then switch it to an array of the appropriate class
					Object structArray = Array.newInstance(element.sizeClass, result.length);
					for (int i = 0; i < result.length; i++) {
						Array.set(structArray, i, result[i]);
					}
					return structArray;
				}
			} else {
				preceding += size * element.count;
			}
		}

		// Now see if it's one of the variable-length bits
		for (VariableElement element : variableElements_.get(getClass().getName())) {
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

		return null;
	}

	protected void setStructElement(final String name, final Object value) {
		// TODO allow null to zero out all data
		if (value == null) {
			throw new IllegalArgumentException("Value cannot be null.");
		}

		int preceding = 0;

		// Look through the fixed elements first, building up the preceding byte count while we're at it
		for (FixedElement element : fixedElements_.get(getClass().getName())) {
			int size = _getSize(element.sizeClass);
			if (StringUtil.equals(name, element.name)) {
				// First, make sure the value is correct
				// TODO handle upgraded values
				// TODO handle arrays
				if (!value.getClass().equals(element.sizeClass)) {
					throw new IllegalArgumentException("Value class '" + value.getClass().getName() + "' does not match expected class '"
							+ element.sizeClass.getName() + "'");
				}
				ByteBuffer data = getData().duplicate().order(ByteOrder.LITTLE_ENDIAN);
				data.position(data.position() + preceding + (size * 0));
				if (Byte.class.equals(element.sizeClass)) {
					data.put((Byte) value);
				} else if (Short.class.equals(element.sizeClass)) {
					data.putShort((Short) value);
				} else if (Integer.class.equals(element.sizeClass)) {
					data.putInt((Integer) value);
				} else if (Long.class.equals(element.sizeClass)) {
					data.putLong((Long) value);
				} else if (Float.class.equals(element.sizeClass)) {
					data.putFloat((Float) value);
				} else if (Double.class.equals(element.sizeClass)) {
					data.putDouble((Double) value);
				} else {
					ByteBuffer structData = ((AbstractStruct) value).getData().duplicate();
					data.put(structData);
				}

			} else {
				preceding += size * element.count;
			}
		}

		// TODO: Add variable elements
	}

	private int _getSize(final Class<?> sizeClass) {
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

	private boolean _isPrimitive(final Class<?> clazz) {
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
	private Object _toPrimitiveArray(final Object[] value, final Class<?> primitiveClass) {
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
