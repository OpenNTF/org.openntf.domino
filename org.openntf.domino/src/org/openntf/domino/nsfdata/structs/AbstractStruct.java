package org.openntf.domino.nsfdata.structs;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

	public abstract int getStructSize();

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "]";
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
		private final boolean isString;

		private VariableElement(final String name, final String lengthMethodName, final boolean isString) {
			this.name = name;
			this.lengthMethodName = lengthMethodName;
			this.isString = isString;
		}

		@Override
		public int hashCode() {
			return 11 + name.hashCode() + lengthMethodName.hashCode() + (isString ? 2 : 1);
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

	protected static void addFixedUpgrade(final String name, final Class<?> sizeClass) {
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

	protected static void addFixedArrayUpgrade(final String name, final Class<?> sizeClass, final int size) {
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
		variableElements_.get(caller).add(new VariableElement(name, lengthMethodName, false));
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
		variableElements_.get(caller).add(new VariableElement(name, lengthMethodName, true));
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
						data.position(data.position() + preceding);
						data.limit(data.position() + size);
						try {
							result[i] = element.sizeClass.getDeclaredConstructor(ByteBuffer.class).newInstance(data);
						} catch (Throwable t) {
							throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
						}
					}
				}

				return element.count == 1 ? result[0] : result;
			} else {
				preceding += size * element.count;
			}
		}

		// Now see if it's one of the variable-length bits
		for (VariableElement element : variableElements_.get(getClass().getName())) {
			try {
				Method method = getClass().getDeclaredMethod(element.lengthMethodName);
				int size = (Integer) method.invoke(this);

				if (StringUtil.equals(name, element.name)) {
					if (element.isString) {
						ByteBuffer data = getData().duplicate();
						data.order(ByteOrder.LITTLE_ENDIAN);
						data.position(data.position() + preceding);
						data.limit(data.position() + size);
						return ODSUtils.fromLMBCS(data);
					} else {
						byte[] result = new byte[size];
						if (size > 0) {
							for (int i = 0; i < size; i++) {
								result[i] = getData().get(getData().position() + preceding + i);
							}
						}
						return result;
					}
				} else {
					preceding += size;
				}
			} catch (Throwable t) {
				throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
			}
		}

		return null;
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
		} else if (Double.class.equals(sizeClass)) {
			// Ignore upgrade
			return getData().getDouble(getData().position() + preceding);
		}
		return null;
	}
}
