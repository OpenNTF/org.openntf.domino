/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

import javolution.io.Struct;

import com.ibm.commons.util.StringUtil;

public abstract class AbstractStruct extends Struct implements Externalizable {

	public void init() {
		setByteBuffer(ByteBuffer.allocate(size()).order(ByteOrder.LITTLE_ENDIAN), 0);
	}

	public void init(final ByteBuffer data) {
		setByteBuffer(data.duplicate().order(ByteOrder.LITTLE_ENDIAN), data.position());
	}

	@Override
	public boolean isPacked() {
		return true;
	}

	@Override
	public ByteOrder byteOrder() {
		return ByteOrder.LITTLE_ENDIAN;
	}

	public ByteBuffer getData() {
		return getByteBuffer();
	}

	protected void setData(final byte[] data) {
		setByteBuffer(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN), 0);
	}

	protected void setData(final ByteBuffer data) {
		setByteBuffer(data.duplicate().order(ByteOrder.LITTLE_ENDIAN), 0);
	}

	public long getPayload(final byte[] result, final long offset) {
		ByteBuffer data = getData().duplicate();
		data.position(data.position() + size());
		long length = Math.min(getVariableSize(), result.length - offset);
		data.get(result, (int) offset, (int) length);
		return length;
	}

	public byte[] getBytes() {
		ByteBuffer data = getData().duplicate();
		//		int size = data.limit() - data.position();
		byte[] result = new byte[(int) getTotalSize()];
		// Ignore extra length when storing
		data.get(result, 0, (int) (size() + getVariableSize()));
		return result;
	}

	public long getStructSize() {
		return size();
	}

	public int getExtraLength() {
		return (int) ((size() + getVariableSize()) % 2);
	}

	public long getTotalSize() {
		long result = size() + getVariableSize() + getExtraLength();
		return result;
	}

	public long getVariableSize() {
		long result = 0;
		Collection<VariableElement> varElements = variableElements_.get(getClass().getName());
		if (varElements != null) {
			for (VariableElement element : varElements) {
				try {
					int length = -1;
					boolean found = false;
					// The length method name could either be the name of a fixed variable or a method
					try {
						Field field = getClass().getDeclaredField(element.lengthMethodName);
						if (Unsigned8.class.isAssignableFrom(field.getType())) {
							length = ((Unsigned8) field.get(this)).get();
							found = true;
						} else if (Unsigned16.class.isAssignableFrom(field.getType())) {
							length = ((Unsigned16) field.get(this)).get();
							found = true;
						} else if (Unsigned32.class.isAssignableFrom(field.getType())) {
							length = (int) ((Unsigned32) field.get(this)).get();
							found = true;
						}
					} catch (NoSuchFieldException nsfe) {
						// Ignore and move on
					}

					if (!found) {
						Method method = getClass().getDeclaredMethod(element.lengthMethodName);
						length = (Integer) method.invoke(this);
					}

					result += length;
				} catch (Throwable t) {
					throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
				}

			}
		}
		return result;
	}

	@Override
	public String toString() {
		return buildDebugString();
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		int len = in.readInt();
		byte[] storage = new byte[len];
		in.read(storage);
		setByteBuffer(ByteBuffer.wrap(storage).order(ByteOrder.LITTLE_ENDIAN), 0);
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		byte[] storage = getBytes();
		//		getByteBuffer().get(storage);
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

	private static final Map<String, Collection<VariableElement>> variableElements_ = new HashMap<String, Collection<VariableElement>>();

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

	protected Object getVariableElement(final String name) {
		int preceding = size();

		// Now see if it's one of the variable-length bits
		Collection<VariableElement> varElements = variableElements_.get(getClass().getName());
		if (varElements != null) {
			for (VariableElement element : varElements) {
				try {
					int length = -1;
					boolean found = false;
					// The length method name could either be the name of a fixed variable or a method
					try {
						Field field = getClass().getDeclaredField(element.lengthMethodName);
						if (Unsigned8.class.isAssignableFrom(field.getType())) {
							length = ((Unsigned8) field.get(this)).get();
							found = true;
						} else if (Unsigned16.class.isAssignableFrom(field.getType())) {
							length = ((Unsigned16) field.get(this)).get();
							found = true;
						} else if (Unsigned32.class.isAssignableFrom(field.getType())) {
							length = (int) ((Unsigned32) field.get(this)).get();
							found = true;
						}
					} catch (NoSuchFieldException nsfe) {
						// Ignore and move on
					}

					if (!found) {
						Method method = getClass().getDeclaredMethod(element.lengthMethodName);
						length = (Integer) method.invoke(this);
					}
					int size = String.class.equals(element.dataClass) ? 1 : _getSize(element.dataClass);

					// LMBCS strings are always even length
					int extra = String.class.equals(element.dataClass) && !element.isAscii ? length % 2 : 0;
					//					int extra = String.class.equals(element.dataClass) ? length % 2 : 0;

					if (StringUtil.equals(name, element.name)) {
						if (String.class.equals(element.dataClass)) {
							ByteBuffer data = getData().duplicate();
							data.order(ByteOrder.LITTLE_ENDIAN);
							//							System.out.println("length for " + name + " is " + length);
							//							System.out.println("setting position to " + (data.position() + preceding));
							data.position(data.position() + preceding);
							//							System.out.println("setting limit to " + (data.position() + length));
							data.limit(data.position() + length);
							if (element.isAscii) {
								byte[] chars = new byte[length];
								data.get(chars);
								return new String(chars, Charset.forName("US-ASCII")); //$NON-NLS-1$
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
										result[i] = element.dataClass.newInstance();
										element.dataClass.getMethod("init", ByteBuffer.class).invoke(result[i], data); //$NON-NLS-1$
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
	 * Sets a variable-sized element of the struct to the provided value. If the element's length method name is the name of a field, it
	 * also sets that field to the new length.
	 * 
	 * @return The size in bytes of the stored struct element
	 */
	protected int setVariableElement(final String name, final Object value) {
		//		System.out.println("setting field " + name + " to " + value);
		int preceding = size();

		// Now see if it's one of the variable-length bits
		Collection<VariableElement> varElements = variableElements_.get(getClass().getName());
		if (varElements != null) {
			for (VariableElement element : varElements) {
				try {
					int length = -1;
					boolean found = false;
					// The length method name could either be the name of a fixed variable or a method
					Field field = null;
					try {
						field = getClass().getDeclaredField(element.lengthMethodName);
						if (Unsigned8.class.isAssignableFrom(field.getType())) {
							length = ((Unsigned8) field.get(this)).get();
							found = true;
						} else if (Unsigned16.class.isAssignableFrom(field.getType())) {
							length = ((Unsigned16) field.get(this)).get();
							found = true;
						} else if (Unsigned32.class.isAssignableFrom(field.getType())) {
							length = (int) ((Unsigned32) field.get(this)).get();
							found = true;
						}
					} catch (NoSuchFieldException nsfe) {
						// Ignore and move on
					}

					if (!found) {
						Method method = getClass().getDeclaredMethod(element.lengthMethodName);
						length = (Integer) method.invoke(this);
					}
					int size = String.class.equals(element.dataClass) ? 1 : _getSize(element.dataClass);

					// LMBCS strings are always even length
					int extra = String.class.equals(element.dataClass) && !element.isAscii ? length % 2 : 0;

					if (StringUtil.equals(name, element.name)) {
						//						System.out.println("determined length for existing data in " + name + " is " + length);

						ByteBuffer data = getData().duplicate().order(ByteOrder.LITTLE_ENDIAN);
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
									replacedBytes = stringVal.getBytes(Charset.forName("US-ASCII")); //$NON-NLS-1$
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
						//						System.out.println("replacing with data length " + replacedBytes.length);

						// Check if the result size is different from the original
						if (replacedBytes.length == length) {
							// If it's the same, the job is easy
							data.position(data.position() + preceding);
							data.put(replacedBytes);
						} else {
							// Otherwise, we have to break apart the array and stitch it together
							// Create an array at the new total size
							// TODO make this only use the part of the data needed for the struct
							int initialPosition = data.position();
							int newLength = data.capacity() - length + replacedBytes.length;
							byte[] newBytes = new byte[newLength];
							//							System.out.println("original capacity: " + data.capacity());
							//							System.out.println("new size: " + newLength);

							// Pour in the data before this element
							int start = data.position() + preceding;
							data.position(0);
							//							System.out.println("reading data from 0 to " + (start - 1));
							data.get(newBytes, 0, start);
							//							System.arraycopy(dataArray, 0, newBytes, 0, start);
							//							System.out.println("data's position is now " + data.position());

							// Write this element's data
							//							System.out.println("original length=" + length);
							//							System.out.println("replacedBytes.length=" + replacedBytes.length);
							//							System.out.println("newBytes.length=" + newBytes.length);
							//							System.out.println("start=" + start);
							System.arraycopy(replacedBytes, 0, newBytes, start, replacedBytes.length);

							// Write any data from after this element
							int remaining = newBytes.length - start - replacedBytes.length;
							data.position(start + length);
							int sourceLength = data.capacity() - data.position();
							if (remaining > 0) {
								//								System.out.println("reading from data position " + data.position());
								int destOffset = start + replacedBytes.length;
								//								int sourceOffset = start + length;
								//								System.out.println("want to write " + sourceLength + " bytes into an array of size " + newBytes.length
								//										+ " starting at " + destOffset);
								//								System.arraycopy(dataArray, sourceOffset, newBytes, destOffset, sourceLength);
								data.get(newBytes, destOffset, sourceLength);
							}

							ByteBuffer newData = ByteBuffer.wrap(newBytes);
							newData.order(ByteOrder.LITTLE_ENDIAN).position(initialPosition);
							//							this.setData(newBytes);
							this.setByteBuffer(newData, newData.position());

							// If the element was defined by a field, set that field to the new length value
							if (field != null) {
								//								System.out.println("setting size field to " + replacedBytes.length);
								if (Unsigned8.class.isAssignableFrom(field.getType())) {
									Unsigned8.class.getMethod("set", Short.TYPE).invoke(field.get(this), (short) replacedBytes.length); //$NON-NLS-1$
								} else if (Unsigned16.class.isAssignableFrom(field.getType())) {
									Unsigned16.class.getMethod("set", Integer.TYPE).invoke(field.get(this), replacedBytes.length); //$NON-NLS-1$
								} else if (Unsigned32.class.isAssignableFrom(field.getType())) {
									Unsigned32.class.getMethod("set", Long.TYPE).invoke(field.get(this), (long) replacedBytes.length); //$NON-NLS-1$
								}
							}
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
		} else if (Struct.class.isAssignableFrom(sizeClass)) {
			try {
				Struct example = (Struct) sizeClass.newInstance();
				return example.size();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		} else {
			Field field = null;
			try {
				field = sizeClass.getField("SIZE"); //$NON-NLS-1$
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

		throw new UnsupportedOperationException("Unknown size class " + sizeClass); //$NON-NLS-1$
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

	//	protected String buildDebugString(final String... properties) {
	//		StringBuilder result = new StringBuilder();
	//		result.append("[");
	//		result.append(getClass().getSimpleName());
	//		result.append(": ");
	//		boolean addedProp = false;
	//		for (String property : properties) {
	//			if (addedProp) {
	//				result.append(", ");
	//			} else {
	//				addedProp = true;
	//			}
	//			result.append(property);
	//			result.append("=");
	//
	//			try {
	//				Method getter = getClass().getMethod("get" + property);
	//				result.append(getter.invoke(this));
	//			} catch (Throwable t) {
	//				throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
	//			}
	//		}
	//		result.append("]");
	//		return result.toString();
	//	}
	//
	protected String buildDebugString() {
		String currentField = null;
		try {
			StringBuilder result = new StringBuilder();
			result.append("["); //$NON-NLS-1$
			result.append(getClass().getSimpleName());
			result.append(": "); //$NON-NLS-1$

			boolean addedProp = false;

			for (Field field : getClass().getDeclaredFields()) {
				currentField = field.getName();
				// TODO add array support
				if (Struct.Member.class.isAssignableFrom(field.getType())) {
					if (addedProp) {
						result.append(", "); //$NON-NLS-1$
					} else {
						addedProp = true;
					}
					result.append(field.getName());
					result.append("="); //$NON-NLS-1$
					try {
						//						System.out.println("getting field " + field.getName());
						Method getMethod = field.getType().getDeclaredMethod("get"); //$NON-NLS-1$
						result.append(getMethod.invoke(field.get(this)));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}

			if (variableElements_.containsKey(getClass().getName())) {
				for (VariableElement element : variableElements_.get(getClass().getName())) {
					currentField = element.name;
					//					System.out.println("getting element " + element.name);
					if (addedProp) {
						result.append(", "); //$NON-NLS-1$
					} else {
						addedProp = true;
					}

					result.append(element.name);
					result.append("="); //$NON-NLS-1$
					result.append(getVariableElement(element.name));
				}
			}

			result.append("]"); //$NON-NLS-1$
			return result.toString();
		} catch (RuntimeException re) {
			System.err.println("RUNTIME EXCEPTION IN " + getClass().getName() + " FOR FIELD " + currentField); //$NON-NLS-1$ //$NON-NLS-2$
			throw re;
		}
	}
}
