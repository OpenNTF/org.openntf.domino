package org.openntf.domino.nsfdata.structs;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;
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
	 * This class represents an element in the static structure definition.
	 * 
	 * @author jgallagher
	 *
	 */
	private static class FixedElement {
		private final String name;
		private final Class<?> sizeClass;
		private final boolean upgrade;
		private final int count;

		public FixedElement(final String name, final Class<?> sizeClass, final boolean upgrade, final int count) {
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

	private static final Map<String, Collection<FixedElement>> fixedElements_ = new HashMap<String, Collection<FixedElement>>();

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

	protected Object getStructElement(final String name) {
		int preceding = 0;

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
				preceding += size;
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
