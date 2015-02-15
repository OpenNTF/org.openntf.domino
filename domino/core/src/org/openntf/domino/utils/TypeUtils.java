/**
 * 
 */
package org.openntf.domino.utils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import lotus.domino.DateTime;
import lotus.domino.Name;

import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.exceptions.DataNotCompatibleException;
import org.openntf.domino.exceptions.ItemNotFoundException;
import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.ext.Formula;
import org.openntf.domino.types.BigString;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

/**
 * @author nfreeman
 * 
 */
public enum TypeUtils {
	;

	public static final String[] DEFAULT_STR_ARRAY = { "" };

	@SuppressWarnings("unchecked")
	public static <T> T getDefaultInstance(final Class<T> type) {
		if (type.isArray())
			if (type.getComponentType() == String.class) {
				return (T) DEFAULT_STR_ARRAY.clone();
			} else {
				return (T) Array.newInstance(type.getComponentType(), 0);
			}
		if (Boolean.class.equals(type) || Boolean.TYPE.equals(type))
			return (T) Boolean.FALSE;
		if (Integer.class.equals(type) || Integer.TYPE.equals(type))
			return (T) Integer.valueOf(0);
		if (Long.class.equals(type) || Long.TYPE.equals(type))
			return (T) Long.valueOf(0l);
		if (Short.class.equals(type) || Short.TYPE.equals(type))
			return (T) Short.valueOf("0");
		if (Double.class.equals(type) || Double.TYPE.equals(type))
			return (T) Double.valueOf(0d);
		if (Float.class.equals(type) || Float.TYPE.equals(type))
			return (T) Float.valueOf(0f);
		if (String.class.equals(type))
			return (T) "";
		try {
			return type.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// RPr: This method is implemented wrong, you MUST NOT pass null as "recycleThis" argument, because it may created
	// dangling DateTimes that will crash the server (if you have created too much)
	// ==>commented out and implemented stampAll method correctly
	//	@Deprecated
	//	public static Map<String, Object> toStampableMap(final Map<String, Object> rawMap, final org.openntf.domino.Base<?> context)
	//			throws IllegalArgumentException {
	//		Map<String, Object> result = new LinkedHashMap<String, Object>();
	//		synchronized (rawMap) {
	//			for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
	//				Object lValue = Base.toItemFriendly(entry.getValue(), context, null);
	//				result.put(entry.getKey(), lValue);
	//			}
	//		}
	//		return Collections.unmodifiableMap(result);
	//	}

	@SuppressWarnings("unchecked")
	public static <T> T itemValueToClass(final Document doc, final String itemName, final Class<T> type) {
		String noteid = doc.getNoteID();
		boolean hasItem = doc.hasItem(itemName);
		if (!hasItem) {
			// System.out.println("Item " + itemName + " doesn't exist in document " + doc.getNoteID() + " in "
			// + doc.getAncestorDatabase().getFilePath() + " so we can't return a " + T.getName());
			Class<?> CType = null;
			if (type.isArray()) {
				CType = type.getComponentType();
				if (CType.isPrimitive()) {
					throw new ItemNotFoundException("Item " + itemName + " was not found on document " + noteid
							+ " so we cannot return an array of " + CType.getName());
				} else {
					return null;
				}
			} else if (type.isPrimitive()) {
				throw new ItemNotFoundException("Item " + itemName + " was not found on document " + noteid + " so we cannot return a "
						+ type.getName());
			} else {
				return null;
			}
		}
		Object result = itemValueToClass(doc.getFirstItem(itemName), type);
		if (result != null && !type.isAssignableFrom(result.getClass())) {
			if (type.isPrimitive()) {
				if (Integer.TYPE.equals(type) && result instanceof Integer) {
					return (T) result;
				}
				if (Long.TYPE.equals(type) && result instanceof Long) {
					return (T) result;
				}
				if (Boolean.TYPE.equals(type) && result instanceof Boolean) {
					return (T) result;
				}
				if (Double.TYPE.equals(type) && result instanceof Double) {
					return (T) result;
				}
			} else {
				log_.log(Level.WARNING, "Auto-boxing requested a " + type.getName() + " but is returning a " + result.getClass().getName()
						+ " in item " + itemName + " for document id " + noteid);
			}
		}
		return (T) result;
	}

	@SuppressWarnings("rawtypes")
	public static <T> T itemValueToClass(final Item item, final Class<T> type) {
		// Object o = item.getAncestorDocument().getItemValue(item.getName());
		Vector v = item.getValues();
		if (v == null) {
			log_.log(Level.WARNING, "Got a null for the value of item " + item.getName());
		}
		Session session = item.getAncestorSession();
		T result = null;
		try {
			result = collectionToClass(v, type, session);
		} catch (DataNotCompatibleException e) {
			String noteid = item.getAncestorDocument().getNoteID();
			throw new DataNotCompatibleException(e.getMessage() + " for field " + item.getName() + " in document " + noteid);
		} catch (UnimplementedException e) {
			String noteid = item.getAncestorDocument().getNoteID();
			throw new UnimplementedException(e.getMessage() + ", so cannot auto-box for field " + item.getName() + " in document " + noteid);
		}

		return result;
	}

	public static boolean isNumerical(final Object rawObject) {
		boolean result = true;
		if (rawObject == null || rawObject instanceof String)
			return false;	//NTF: we know this is going to be true a LOT, so we'll have a fast out
		if (rawObject instanceof Collection) {
			for (Object obj : (Collection<?>) rawObject) {
				if (!isNumerical(obj)) {
					result = false;
					break;
				}
			}
		} else {
			if (rawObject instanceof Number || Integer.TYPE.isInstance(rawObject) || Double.TYPE.isInstance(rawObject)
					|| Byte.TYPE.isInstance(rawObject) || Short.TYPE.isInstance(rawObject) || Long.TYPE.isInstance(rawObject)
					|| Float.TYPE.isInstance(rawObject)) {
			} else {
				result = false;
			}
		}
		return result;
	}

	public static boolean isCalendrical(final Object rawObject) {
		boolean result = true;
		if (rawObject == null || rawObject instanceof String)
			return false;	//NTF: we know this is going to be true a LOT, so we'll have a fast out
		if (rawObject instanceof Collection) {
			for (Object obj : (Collection<?>) rawObject) {
				if (!isCalendrical(obj)) {
					result = false;
					break;
				}
			}
		} else {
			if (rawObject instanceof DateTime || rawObject instanceof Date) {
			} else {
				result = false;
			}
		}
		return result;
	}

	public static boolean isNameish(final Object rawObject) {
		boolean result = true;
		if (rawObject == null)
			return false;	//NTF: we know this is going to be true a LOT, so we'll have a fast out
		if (rawObject instanceof Collection) {
			for (Object obj : (Collection<?>) rawObject) {
				if (!isNameish(obj)) {
					result = false;
					break;
				}
			}
		} else {
			if (rawObject instanceof String) {
				result = DominoUtils.isHierarchicalName((String) rawObject);
			} else {
				result = false;
			}
		}
		return result;
	}

	public static <T> T objectToClass(final Object o, final Class<T> type, final Session session) {
		return convertToTarget(o, type, session);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T convertToTarget(final Object o, final Class<T> type, final Session session) {
		if (o == null) {
			return null;
		}
		Object result = null;
		if (o instanceof Collection) {
			result = collectionToClass((Collection) o, type, session);
		}
		if (type.isEnum() && o instanceof String) {
			result = toEnum(o, type);
		}
		Class<?> CType = null;
		if (type.equals(String[].class)) {
			result = toStrings(o);
			return (T) result;
		}
		if (type.isArray()) {
			if (String[].class.equals(type)) {
				// System.out.println("Shallow route to string array");
				result = toStrings(o);
			} else {
				CType = type.getComponentType();
				if (CType.isPrimitive()) {
					try {
						result = toPrimitiveArray(o, CType);
					} catch (DataNotCompatibleException e) {
						throw e;
					}
				} else if (Number.class.isAssignableFrom(CType)) {
					result = toNumberArray(o, CType);
				} else {
					if (CType == String.class) {
						// System.out.println("Deep route to string array");
						result = toStrings(o);
					} else if (CType == BigString.class) {
						result = toBigStrings(o);
					} else if (CType == Pattern.class) {
						result = toPatterns(o);
					} else if (Enum.class.isAssignableFrom(type)) {
						result = toEnums(o);
					} else if (Class.class.isAssignableFrom(CType)) {
						result = toClasses(o);
					} else if (Formula.class.isAssignableFrom(CType)) {
						result = toFormulas(o);
					} else if (CType == Date.class) {
						result = toDates(o);
					} else if (DateTime.class.isAssignableFrom(CType)) {
						result = toDateTimes(o, session);
					} else if (Name.class.isAssignableFrom(CType)) {
						result = toNames(o, session);
					} else if (CType == Boolean.class) {
						result = toBooleans(o);
					} else if (CType == java.lang.Object.class) {
						result = toObjects(o);
					} else {
						throw new UnimplementedException("Arrays for " + CType.getName() + " not yet implemented");
					}
				}
			}
		} else if (type.isPrimitive()) {
			try {
				result = toPrimitive(o, type);
			} catch (DataNotCompatibleException e) {
				throw e;
			}
		} else {
			if (type == String.class) {
				result = String.valueOf(o);
			} else if (Enum.class.isAssignableFrom(type)) {
				String str = String.valueOf(o);
				result = toEnum(str);
			} else if (BigString.class.isAssignableFrom(type)) {
				result = new BigString(String.valueOf(o));
			} else if (Pattern.class.isAssignableFrom(type)) {
				result = Pattern.compile(String.valueOf(o));
			} else if (Class.class.isAssignableFrom(type)) {
				String cn = String.valueOf(o);
				Class<?> cls = DominoUtils.getClass(cn);
				result = cls;
			} else if (Formula.class.isAssignableFrom(type)) {
				Formula formula = new org.openntf.domino.helpers.Formula(String.valueOf(o));
				result = formula;
			} else if (java.util.Collection.class.equals(type)) {
				result = new ArrayList();
				((ArrayList) result).add(o);
			} else if (java.util.Collection.class.isAssignableFrom(type)) {
				try {
					result = type.newInstance();
					Collection coll = (Collection) result;
					coll.addAll(toSerializables(o));
				} catch (IllegalAccessException e) {
					DominoUtils.handleException(e);
				} catch (InstantiationException e) {
					DominoUtils.handleException(e);
				}
			} else if (Date.class.isAssignableFrom(type)) {
				result = toDate(o);
			} else if (java.util.Calendar.class.isAssignableFrom(type)) {
				Date tmpDate = toDate(o);
				if (null == tmpDate) {
					result = null;
				} else {
					Calendar tmp = Calendar.getInstance();
					tmp.setTime(tmpDate);
					result = tmp;
				}
			} else if (org.openntf.domino.DateTime.class.isAssignableFrom(type)) {
				if (session != null) {
					result = session.createDateTime(toDate(o));
				} else {
					throw new IllegalArgumentException("Cannont convert a " + o.getClass().getName()
							+ " to DateTime without a valid Session object");
				}
			} else if (org.openntf.domino.Name.class.isAssignableFrom(type)) {
				if (session != null) {
					result = session.createName(String.valueOf(o));
				} else {
					throw new IllegalArgumentException("Cannont convert a " + o.getClass().getName()
							+ " to Name without a valid Session object");
				}
			} else if (Boolean.class.equals(type)) {
				result = toBoolean(o);
			} else if (Number.class.isAssignableFrom(type)) {
				result = toNumber(o, type);
			} else {
				result = type.cast(o);
			}
		}

		if (result != null && !type.isAssignableFrom(result.getClass())) {
			if (type.isPrimitive()) {
				if (Integer.TYPE.equals(type) && result instanceof Integer) {
					return (T) result;
				}
				if (Long.TYPE.equals(type) && result instanceof Long) {
					return (T) result;
				}
				if (Boolean.TYPE.equals(type) && result instanceof Boolean) {
					return (T) result;
				}
				if (Double.TYPE.equals(type) && result instanceof Double) {
					return (T) result;
				}
			} else {
				log_.log(Level.WARNING, "Auto-boxing requested a " + type.getName() + " but is returning a " + result.getClass().getName());
			}
		}
		return (T) result;
	}

	public static Collection<Serializable> toSerializables(final Object value) {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return DominoUtils.toSerializable((Collection<?>) value);
		} else if (value.getClass().isArray()) {
			return DominoUtils.toSerializable(Arrays.asList(value));
		} else {
			Collection<Serializable> result = new ArrayList<Serializable>();
			if (value instanceof org.openntf.domino.DateTime) {
				Date date = null;
				org.openntf.domino.DateTime dt = (org.openntf.domino.DateTime) value;
				date = dt.toJavaDate();
				result.add(date);
			} else if (value instanceof org.openntf.domino.Name) {
				result.add(DominoUtils.toNameString((org.openntf.domino.Name) value));
			} else if (value instanceof String) {
				result.add((String) value);
			} else if (value instanceof Number) {
				result.add((Number) value);
			}
			return result;
		}
	}

	public static <T> T vectorToClass(final Collection<?> v, final Class<T> type, final Session session) {
		return collectionToClass(v, type, session);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T collectionToClass(final Collection v, final Class<T> type, final Session session) {
		if (v == null) {
			return null;
		}
		Object result = null;
		Class<?> CType = null;
		if (type.equals(String[].class)) {
			result = toStrings(v);
			return (T) result;
		}
		if (type.isArray()) {
			if (type == String[].class) {
				// System.out.println("Shallow route to string array");
				result = toStrings(v);
			} else {
				CType = type.getComponentType();
				if (CType.isPrimitive()) {
					try {
						result = toPrimitiveArray(v, CType);
					} catch (DataNotCompatibleException e) {
						throw e;
					}
				} else if (Number.class.isAssignableFrom(CType)) {
					result = toNumberArray(v, CType);
				} else {
					if (CType == String.class) {
						// System.out.println("Deep route to string array");
						result = toStrings(v);
					} else if (CType == BigString.class) {
						result = toBigStrings(v);
					} else if (CType == Pattern.class) {
						result = toPatterns(v);
					} else if (Enum.class.isAssignableFrom(type)) {
						result = toEnums(v);
					} else if (Class.class.isAssignableFrom(CType)) {
						result = toClasses(v);
					} else if (Formula.class.isAssignableFrom(CType)) {
						result = toFormulas(v);
					} else if (CType == Date.class) {
						result = toDates(v);
					} else if (DateTime.class.isAssignableFrom(CType)) {
						result = toDateTimes(v, session);
					} else if (Name.class.isAssignableFrom(CType)) {
						result = toNames(v, session);
					} else if (CType == Boolean.class) {
						result = toBooleans(v);
					} else if (CType == java.lang.Object.class) {
						result = toObjects(v);
					} else {
						throw new UnimplementedException("Arrays for " + CType.getName() + " not yet implemented");
					}
				}
			}
		} else if (type.isPrimitive()) {
			try {
				result = toPrimitive(v, type);
			} catch (DataNotCompatibleException e) {
				throw e;
			}
		} else {
			if (type == String.class) {
				result = join(v);
			} else if (Enum.class.isAssignableFrom(type)) {
				String str = join(v);
				//				System.out.println("Attempting to convert string " + str + " to Enum");
				result = toEnum(str);
				//				System.out.println("result was " + (result == null ? "null" : result.getClass().getName()));
			} else if (BigString.class.isAssignableFrom(type)) {
				result = new BigString(join(v));
			} else if (Pattern.class.isAssignableFrom(type)) {
				result = Pattern.compile(join(v));
			} else if (Class.class.isAssignableFrom(type)) {
				String cn = join(v);
				Class<?> cls = DominoUtils.getClass(cn);
				result = cls;
			} else if (Formula.class.isAssignableFrom(type)) {
				Formula formula = new org.openntf.domino.helpers.Formula(join(v));
				result = formula;
			} else if (type == java.util.Collection.class) {
				result = new ArrayList();
				if (v != null) {
					((ArrayList) result).addAll(v);
				}
			} else if (java.util.Collection.class.isAssignableFrom(type)) {
				try {
					result = type.newInstance();
					Collection coll = (Collection) result;
					coll.addAll(DominoUtils.toSerializable(v));
				} catch (IllegalAccessException e) {
					DominoUtils.handleException(e);
				} catch (InstantiationException e) {
					DominoUtils.handleException(e);
				}
			} else if (Date.class.isAssignableFrom(type)) {
				result = toDate(v);
			} else if (java.util.Calendar.class.isAssignableFrom(type)) {
				Date tmpDate = toDate(v);
				if (null == tmpDate) {
					result = null;
				} else {
					Calendar tmp = Calendar.getInstance();
					tmp.setTime(tmpDate);
					result = tmp;
				}
			} else if (org.openntf.domino.DateTime.class.isAssignableFrom(type)) {
				if (session != null) {
					result = session.createDateTime(toDate(v));
				} else {
					throw new IllegalArgumentException("Cannont convert a Vector to DateTime without a valid Session object");
				}
			} else if (org.openntf.domino.Name.class.isAssignableFrom(type)) {
				if (session != null) {
					if (v.isEmpty()) {
						result = session.createName("");
					} else {
						Iterator it = v.iterator();
						result = session.createName(String.valueOf(it.next()));
					}
				} else {
					throw new IllegalArgumentException("Cannont convert a Vector to Name without a valid Session object");

				}
			} else if (type == Boolean.class) {
				if (v.isEmpty()) {
					result = Boolean.FALSE;
				} else {
					Iterator it = v.iterator();
					result = toBoolean(it.next());
				}
			} else {
				if (!v.isEmpty()) {
					if (Number.class.isAssignableFrom(type)) {
						result = toNumber(v, type);
					} else {
						Iterator it = v.iterator();
						result = it.next();
					}
				}
			}
		}

		if (result != null && !type.isAssignableFrom(result.getClass())) {
			if (type.isPrimitive()) {
				if (Integer.TYPE.equals(type) && result instanceof Integer) {
					return (T) result;
				}
				if (Long.TYPE.equals(type) && result instanceof Long) {
					return (T) result;
				}
				if (Boolean.TYPE.equals(type) && result instanceof Boolean) {
					return (T) result;
				}
				if (Double.TYPE.equals(type) && result instanceof Double) {
					return (T) result;
				}
			} else {
				log_.log(Level.WARNING, "Auto-boxing requested a " + type.getName() + " but is returning a " + result.getClass().getName());
			}
		}
		return (T) result;
	}

	private static final Logger log_ = Logger.getLogger(TypeUtils.class.getName());

	@SuppressWarnings("unchecked")
	public static <T> T toNumberArray(final Object value, final Class<T> type) {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToNumberArray((Collection<Object>) value, type);
		} else if (value.getClass().isArray()) {
			Object[] arr = (Object[]) value;
			Object[] result = (Object[]) Array.newInstance(type, arr.length);
			for (int i = 0; i < arr.length; i++) {
				result[i++] = toNumber(arr[i], type);
			}
			return (T) result;
		} else {
			Object[] result = (Object[]) Array.newInstance(type, 1);
			result[0] = toNumber(value, type);
			return (T) result;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T collectionToNumberArray(final Collection<Object> value, final Class<T> type) {
		int size = value.size();
		Object[] result = (Object[]) Array.newInstance(type, size);
		int i = 0;
		Iterator<Object> it = value.iterator();
		while (it.hasNext()) {
			result[i++] = toNumber(it.next(), type);
		}
		return (T) result;
	}

	@SuppressWarnings("unchecked")
	public static <T> T toNumber(final Object value, final Class<T> type) throws DataNotCompatibleException {
		// System.out.println("Starting toNumber to get type " + T.getName() + " from a value of type " + value.getClass().getName());
		if (value == null)
			return null;
		if (value instanceof Vector && (((Vector<?>) value).isEmpty()))
			return null;
		T result = null;
		Object localValue = value;
		if (value instanceof Collection) {
			localValue = ((Collection<?>) value).iterator().next();
		}
		// System.out.println("LocalValue is type " + localValue.getClass().getName() + ": " + String.valueOf(localValue));

		if (type == Integer.class) {
			if (localValue instanceof String) {
				result = (T) Integer.valueOf((String) localValue);
			} else if (localValue instanceof Double) {
				result = (T) Integer.valueOf(((Double) localValue).intValue());
			} else if (localValue instanceof Integer) {
				result = (T) localValue;
			} else if (localValue instanceof Long) {
				result = (T) Integer.valueOf(((Long) localValue).intValue());
			} else {
				throw new DataNotCompatibleException("Cannot create a " + type.getName() + " from a " + localValue.getClass().getName());
			}
		} else if (type == Long.class) {
			if (localValue instanceof String) {
				result = (T) Long.valueOf((String) localValue);
			} else if (localValue instanceof Double) {
				result = (T) Long.valueOf(((Double) localValue).longValue());
			} else {
				throw new DataNotCompatibleException("Cannot create a " + type.getName() + " from a " + localValue.getClass().getName());
			}
		} else if (type == Double.class) {
			if (localValue instanceof String) {
				result = (T) Double.valueOf((String) localValue);
			} else if (localValue instanceof Double) {
				result = (T) localValue;
			} else if (localValue instanceof Integer) {
				result = (T) Double.valueOf(((Integer) localValue).doubleValue());
			} else if (localValue instanceof Short) {
				result = (T) Double.valueOf(((Short) localValue).doubleValue());
			} else if (localValue instanceof Float) {
				result = (T) Double.valueOf(((Float) localValue).doubleValue());
			} else {
				throw new DataNotCompatibleException("Cannot create a " + type.getName() + " from a " + localValue.getClass().getName());
			}
		} else if (type == Short.class) {
			if (localValue instanceof String) {
				result = (T) Short.valueOf((String) localValue);
			} else if (localValue instanceof Double) {
				result = (T) Short.valueOf(((Double) localValue).shortValue());
			} else {
				throw new DataNotCompatibleException("Cannot create a " + type.getName() + " from a " + localValue.getClass().getName());
			}
		} else if (type == Byte.class) {
			if (localValue instanceof String) {
				result = (T) Byte.valueOf((String) localValue);
			} else if (localValue instanceof Double) {
				result = (T) Byte.valueOf(((Double) localValue).byteValue());
			} else {
				throw new DataNotCompatibleException("Cannot create a " + type.getName() + " from a " + localValue.getClass().getName());
			}
		} else if (type == Float.class) {
			if (localValue instanceof String) {
				result = (T) Float.valueOf((String) localValue);
			} else if (localValue instanceof Double) {
				result = (T) Float.valueOf(((Double) localValue).floatValue());
			} else {
				throw new DataNotCompatibleException("Cannot create a " + type.getName() + " from a " + localValue.getClass().getName());
			}
		} else if (type == BigDecimal.class) {
			if (localValue instanceof String) {
				result = (T) new BigDecimal((String) localValue);
			} else if (localValue instanceof Double) {
				result = (T) new BigDecimal((Double) localValue);
			} else {
				throw new DataNotCompatibleException("Cannot create a " + type.getName() + " from a " + localValue.getClass().getName());
			}
		} else if (type == BigInteger.class) {
			if (localValue instanceof String) {
				result = (T) new BigInteger((String) localValue);
			} else {
				throw new DataNotCompatibleException("Cannot create a " + type.getName() + " from a " + localValue.getClass().getName());
			}
		} else if (type == AtomicInteger.class) {
			if (localValue instanceof String) {
				result = (T) new AtomicInteger(Integer.valueOf((String) localValue));
			} else if (localValue instanceof Double) {
				result = (T) new AtomicInteger(Integer.valueOf(((Double) localValue).intValue()));
			} else {
				throw new DataNotCompatibleException("Cannot create a " + type.getName() + " from a " + localValue.getClass().getName());
			}
		} else if (type == AtomicLong.class) {
			if (localValue instanceof String) {
				result = (T) new AtomicLong(Long.valueOf((String) localValue));
			} else if (localValue instanceof Double) {
				result = (T) new AtomicLong(Long.valueOf(((Double) localValue).longValue()));
			} else {
				throw new DataNotCompatibleException("Cannot create a " + type.getName() + " from a " + localValue.getClass().getName());
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Boolean[] toBooleans(final Object value) {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToBooleans((Collection<Object>) value);
		} else if (value.getClass().isArray()) {
			return collectionToBooleans(Arrays.asList(value));
		} else {
			Boolean[] result = new Boolean[1];
			result[0] = toBoolean(value);
			return result;
		}
	}

	public static Boolean[] collectionToBooleans(final Collection<Object> vector) {
		if (vector == null || vector.isEmpty())
			return new Boolean[0];
		Boolean[] bools = new Boolean[vector.size()];
		int i = 0;
		for (Object o : vector) {
			bools[i++] = toBoolean(o);
		}
		return bools;
	}

	public static boolean toBoolean(final Object value) {
		if (value instanceof String) {
			char[] c = ((String) value).toCharArray();
			if (c.length > 1 || c.length == 0) {
				return false;
			} else {
				return c[0] == '1';
			}
		} else if (value instanceof Double) {
			if (((Double) value).intValue() == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			throw new DataNotCompatibleException("Cannot convert a " + value.getClass().getName() + " to boolean primitive.");
		}
	}

	public static int toInt(final Object value) {
		if (value instanceof Integer) {
			return ((Integer) value).intValue();
		} else if (value instanceof Double) {
			return ((Double) value).intValue();
		} else {
			throw new DataNotCompatibleException("Cannot convert a " + value.getClass().getName() + " to int primitive.");
		}
	}

	public static double toDouble(final Object value) {
		if (value instanceof Integer) {
			return ((Integer) value).doubleValue();
		} else if (value instanceof Double) {
			return ((Double) value).doubleValue();
		} else {
			throw new DataNotCompatibleException("Cannot convert a " + value.getClass().getName() + " to double primitive.");
		}
	}

	public static long toLong(final Object value) {
		if (value instanceof Integer) {
			return ((Integer) value).longValue();
		} else if (value instanceof Double) {
			return ((Double) value).longValue();
		} else {
			throw new DataNotCompatibleException("Cannot convert a " + value.getClass().getName() + " to long primitive.");
		}
	}

	public static short toShort(final Object value) {
		if (value instanceof Integer) {
			return ((Integer) value).shortValue();
		} else if (value instanceof Double) {
			return ((Double) value).shortValue();
		} else {
			throw new DataNotCompatibleException("Cannot convert a " + value.getClass().getName() + " to short primitive.");
		}

	}

	public static float toFloat(final Object value) {
		if (value instanceof Integer) {
			return ((Integer) value).floatValue();
		} else if (value instanceof Double) {
			return ((Double) value).floatValue();
		} else {
			throw new DataNotCompatibleException("Cannot convert a " + value.getClass().getName() + " to float primitive.");
		}
	}

	@SuppressWarnings("unchecked")
	public static Object toPrimitive(final Object value, final Class<?> ctype) {
		if (value instanceof Collection) {
			return toPrimitive((Collection<Object>) value, ctype);
		} else {
			if (ctype == Boolean.TYPE)
				return toBoolean(value);
			if (ctype == Integer.TYPE)
				return toInt(value);
			if (ctype == Short.TYPE)
				return toShort(value);
			if (ctype == Long.TYPE)
				return toLong(value);
			if (ctype == Float.TYPE)
				return toFloat(value);
			if (ctype == Double.TYPE)
				return toDouble(value);
			if (ctype == Byte.TYPE)
				throw new UnimplementedException("Primitive conversion for byte not yet defined");
			if (ctype == Character.TYPE)
				throw new UnimplementedException("Primitive conversion for char not yet defined");
			if (ctype == com.ibm.icu.lang.UCharacter.class)
				throw new UnimplementedException("Primitive conversion for char not yet defined");
			throw new DataNotCompatibleException("");

		}
	}

	public static Object toPrimitive(final Collection<Object> values, final Class<?> ctype) {
		if (ctype.isPrimitive()) {
			throw new DataNotCompatibleException(ctype.getName() + " is not a primitive type.");
		}
		if (values.size() > 1) {
			throw new DataNotCompatibleException("Cannot create a primitive " + ctype + " from data because we have a multiple values.");
		}
		if (values.isEmpty()) {
			throw new DataNotCompatibleException("Cannot create a primitive " + ctype + " from data because we don't have any values.");
		}
		Iterator<Object> it = values.iterator();
		if (ctype == Boolean.TYPE)
			return toBoolean(it.next());
		if (ctype == Integer.TYPE)
			return toInt(it.next());
		if (ctype == Short.TYPE)
			return toShort(it.next());
		if (ctype == Long.TYPE)
			return toLong(it.next());
		if (ctype == Float.TYPE)
			return toFloat(it.next());
		if (ctype == Double.TYPE)
			return toDouble(it.next());
		if (ctype == Byte.TYPE)
			throw new UnimplementedException("Primitive conversion for byte not yet defined");
		if (ctype == Character.TYPE)
			throw new UnimplementedException("Primitive conversion for char not yet defined");
		if (ctype == com.ibm.icu.lang.UCharacter.class)
			throw new UnimplementedException("Primitive conversion for char not yet defined");
		throw new DataNotCompatibleException("");
	}

	public static String join(final Object[] values, final String separator) {
		if (values == null || values.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (Object val : values) {
			if (!isFirst) {
				sb.append(separator);
			}
			sb.append(String.valueOf(val));
			isFirst = false;
		}
		return sb.toString();
	}

	public static String join(final Collection<?> values, final String separator) {
		if (values == null || values.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		Iterator<?> it = values.iterator();
		while (it.hasNext()) {
			sb.append(String.valueOf(it.next()));
			if (it.hasNext())
				sb.append(separator);
		}
		return sb.toString();
	}

	public static String join(final Collection<?> values) {
		return join(values, ", ");
	}

	public static String join(final Object[] values) {
		return join(values, ", ");
	}

	@SuppressWarnings("unchecked")
	public static Object toPrimitiveArray(final Object value, final Class<?> ctype) throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToPrimitiveArray((Collection<Object>) value, ctype);
		} else if (value.getClass().isArray()) {
			//TODO NTF this could be better, but I'm tired
			return collectionToPrimitiveArray(Arrays.asList(value), ctype);
		} else {
			Object result = null;
			if (ctype == Boolean.TYPE) {
				boolean[] outcome = new boolean[1];
				// TODO NTF - should allow for String fields that are binary sequences: "1001001" (SOS)
				outcome[0] = toBoolean(value);
				result = outcome;
			} else if (ctype == Byte.TYPE) {
				byte[] outcome = new byte[1];
				// TODO
				result = outcome;
			} else if (ctype == Character.TYPE) {
				char[] outcome = new char[0];
				// TODO How should this work? Just concatenate the char arrays for each String?
				result = outcome;
			} else if (ctype == Short.TYPE) {
				short[] outcome = new short[1];
				outcome[0] = toShort(value);
				result = outcome;
			} else if (ctype == Integer.TYPE) {
				int[] outcome = new int[1];
				outcome[0] = toInt(value);
				result = outcome;
			} else if (ctype == Long.TYPE) {
				long[] outcome = new long[1];
				outcome[0] = toLong(value);
				result = outcome;
			} else if (ctype == Float.TYPE) {
				float[] outcome = new float[1];
				outcome[0] = toFloat(value);
				result = outcome;
			} else if (ctype == Double.TYPE) {
				double[] outcome = new double[1];
				outcome[0] = toDouble(value);
				result = outcome;
			}
			return result;
		}
	}

	public static Object collectionToPrimitiveArray(final Collection<Object> values, final Class<?> ctype)
			throws DataNotCompatibleException {
		Object result = null;
		int size = values.size();
		Iterator<Object> it = values.iterator();
		int i = 0;
		if (ctype == Boolean.TYPE) {
			boolean[] outcome = new boolean[size];
			// TODO NTF - should allow for String fields that are binary sequences: "1001001" (SOS)
			while (it.hasNext()) {
				outcome[i++] = toBoolean(it.next());
			}
			result = outcome;
		} else if (ctype == Byte.TYPE) {
			byte[] outcome = new byte[size];
			// TODO
			result = outcome;
		} else if (ctype == Character.TYPE) {
			char[] outcome = new char[size];
			// TODO How should this work? Just concatenate the char arrays for each String?
			result = outcome;
		} else if (ctype == Short.TYPE) {
			short[] outcome = new short[size];
			while (it.hasNext()) {
				outcome[i++] = toShort(it.next());
			}
			result = outcome;
		} else if (ctype == Integer.TYPE) {
			int[] outcome = new int[size];
			while (it.hasNext()) {
				outcome[i++] = toInt(it.next());
			}
			result = outcome;
		} else if (ctype == Long.TYPE) {
			long[] outcome = new long[size];
			while (it.hasNext()) {
				outcome[i++] = toLong(it.next());
			}
			result = outcome;
		} else if (ctype == Float.TYPE) {
			float[] outcome = new float[size];
			while (it.hasNext()) {
				outcome[i++] = toFloat(it.next());
			}
			result = outcome;
		} else if (ctype == Double.TYPE) {
			double[] outcome = new double[size];
			while (it.hasNext()) {
				outcome[i++] = toDouble(it.next());
			}
			result = outcome;
		}
		return result;
	}

	public static Date toDate(Object value) throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Vector && (((Vector<?>) value).isEmpty()))
			return null;
		if (value instanceof Vector) {
			value = ((Vector<?>) value).get(0);
		}
		if (value instanceof Long) {
			return new Date(((Long) value).longValue());
		} else if (value instanceof String) {
			// TODO finish
			DateFormat df = new SimpleDateFormat();
			String str = (String) value;
			if (str.length() < 1)
				return null;
			try {
				return df.parse(str);
			} catch (ParseException e) {
				throw new DataNotCompatibleException("Cannot create a Date from String value " + (String) value);
			}
		} else if (value instanceof lotus.domino.DateTime) {
			return DominoUtils.toJavaDateSafe((lotus.domino.DateTime) value);
		} else {
			throw new DataNotCompatibleException("Cannot create a Date from a " + value.getClass().getName());
		}
	}

	@SuppressWarnings("unchecked")
	public static Date[] toDates(final Object value) throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToDates((Collection<Object>) value);
		} else if (value.getClass().isArray()) {
			return collectionToDates(Arrays.asList(value));
		} else {
			Date[] result = new Date[1];
			result[0] = toDate(value);
			return result;
		}
	}

	public static Date[] collectionToDates(final Collection<Object> vector) throws DataNotCompatibleException {
		if (vector == null || vector.isEmpty())
			return new Date[0];

		Date[] result = new Date[vector.size()];
		int i = 0;
		for (Object o : vector) {
			result[i++] = toDate(o);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static org.openntf.domino.DateTime[] toDateTimes(final Object value, final org.openntf.domino.Session session)
			throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToDateTimes((Collection<Object>) value, session);
		} else if (value.getClass().isArray()) {
			return collectionToDateTimes(Arrays.asList(value), session);
		} else {
			org.openntf.domino.DateTime[] result = new org.openntf.domino.DateTime[1];
			result[0] = session.createDateTime(toDate(value));
			;
			return result;
		}
	}

	public static org.openntf.domino.DateTime[] collectionToDateTimes(final Collection<Object> vector,
			final org.openntf.domino.Session session) throws DataNotCompatibleException {
		if (vector == null || vector.isEmpty())
			return new org.openntf.domino.DateTime[0];

		org.openntf.domino.DateTime[] result = new org.openntf.domino.DateTime[vector.size()];
		if (session != null) {
			int i = 0;
			for (Object o : vector) {
				result[i++] = session.createDateTime(toDate(o));
			}
			return result;
		} else {
			throw new IllegalArgumentException("Cannont convert to DateTime without a valid Session object");
		}
	}

	@SuppressWarnings("unchecked")
	public static org.openntf.domino.Name[] toNames(final Object value, final org.openntf.domino.Session session)
			throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToNames((Collection<Object>) value, session);
		} else if (value.getClass().isArray()) {
			return collectionToNames(Arrays.asList(value), session);
		} else {
			org.openntf.domino.Name[] result = new org.openntf.domino.Name[1];
			result[0] = session.createName(String.valueOf(value));
			return result;
		}
	}

	public static org.openntf.domino.Name[] collectionToNames(final Collection<Object> vector, final org.openntf.domino.Session session)
			throws DataNotCompatibleException {
		if (vector == null || vector.isEmpty())
			return new org.openntf.domino.Name[0];

		org.openntf.domino.Name[] result = new org.openntf.domino.Name[vector.size()];
		if (session != null) {
			int i = 0;
			for (Object o : vector) {
				result[i++] = session.createName(String.valueOf(o));
			}
			return result;
		} else {
			throw new IllegalArgumentException("Cannont convert to Name without a valid Session object");
		}
	}

	@SuppressWarnings("unchecked")
	public static String[] toStrings(final Object value) throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToStrings((Collection<Object>) value);
		} else if (value.getClass().isArray()) {
			Object[] arr = (Object[]) value;
			String[] result = new String[arr.length];
			for (int i = 0; i < arr.length; i++) {
				result[i] = String.valueOf(arr[i]);
			}
			return result;
		} else {
			String[] result = new String[1];
			result[0] = String.valueOf(value);
			return result;
		}

	}

	public static String[] collectionToStrings(final Collection<Object> vector) throws DataNotCompatibleException {
		if (vector == null || vector.isEmpty())
			return new String[0];

		String[] strings = new String[vector.size()];
		int i = 0;
		// strings = vector.toArray(new String[0]);
		for (Object o : vector) {
			if (o instanceof org.openntf.domino.DateTime) {
				strings[i++] = ((org.openntf.domino.DateTime) o).getGMTTime();
			} else {
				strings[i++] = String.valueOf(o);
			}
		}
		return strings;
	}

	public static String toString(final java.lang.Object object) throws DataNotCompatibleException {
		if (object == null)
			return null;
		if (object instanceof String) {
			return (String) object;
		} else if (object instanceof Collection) {
			return join((Collection<?>) object);
		} else if (object.getClass().isArray()) {
			return join((Object[]) object);
		} else {
			return String.valueOf(object);
		}
	}

	@SuppressWarnings("unchecked")
	public static Pattern[] toPatterns(final Object value) throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToPatterns((Collection<Object>) value);
		} else if (value.getClass().isArray()) {
			return collectionToPatterns(Arrays.asList(value));
		} else {
			Pattern[] result = new Pattern[1];
			result[0] = Pattern.compile(String.valueOf(value));
			return result;
		}
	}

	public static Pattern[] collectionToPatterns(final Collection<Object> vector) throws DataNotCompatibleException {
		if (vector == null || vector.isEmpty())
			return new Pattern[0];

		Pattern[] patterns = new Pattern[vector.size()];
		int i = 0;
		for (Object o : vector) {
			patterns[i++] = Pattern.compile(String.valueOf(o));
		}
		return patterns;
	}

	@SuppressWarnings("unchecked")
	public static java.lang.Object[] toObjects(final Object value) throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToObjects((Collection<Object>) value);
		} else if (value.getClass().isArray()) {
			return collectionToObjects(Arrays.asList(value));
		} else {
			java.lang.Object[] result = new java.lang.Object[1];
			result[0] = value;
			return result;
		}
	}

	public static java.lang.Object[] collectionToObjects(final Collection<Object> vector) throws DataNotCompatibleException {
		if (vector == null || vector.isEmpty())
			return new Object[0];

		Object[] patterns = new Object[vector.size()];
		int i = 0;
		for (Object o : vector) {
			patterns[i++] = o;
		}
		return patterns;
	}

	@SuppressWarnings("unchecked")
	public static Class<?>[] toClasses(final Object value) throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToClasses((Collection<Object>) value);
		} else if (value.getClass().isArray()) {
			return collectionToClasses(Arrays.asList(value));
		} else {
			Class<?>[] classes = new Class[1];
			String cn = String.valueOf(value);
			Class<?> cls = DominoUtils.getClass(cn);
			classes[0] = cls;
			return classes;
		}
	}

	public static Class<?>[] collectionToClasses(final Collection<Object> vector) throws DataNotCompatibleException {
		if (vector == null || vector.isEmpty())
			return new Class[0];

		@SuppressWarnings("unused")
		ClassLoader cl = Factory.getClassLoader();
		Class<?>[] classes = new Class[vector.size()];
		int i = 0;
		for (Object o : vector) {
			int pos = i++;
			String cn = String.valueOf(o);
			Class<?> cls = DominoUtils.getClass(cn);
			classes[pos] = cls;
		}
		return classes;
	}

	public static Enum<?> toEnum(final Object value, final Class<?> enumClass) throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Vector && (((Vector<?>) value).isEmpty()))
			return null;
		Enum<?> result = null;
		String ename = String.valueOf(value);
		if (ename.contains(" ")) {
			ename = String.valueOf(value).substring(ename.indexOf(' ') + 1).trim();
		}
		Object[] objs = enumClass.getEnumConstants();
		if (objs.length > 0) {
			for (Object obj : objs) {
				if (obj instanceof Enum) {
					if (((Enum<?>) obj).name().equals(ename)) {
						result = (Enum<?>) obj;
						break;
					}
				}
			}
		}
		if (result == null) {
			throw new DataNotCompatibleException("Unable to discover an Enum by the name of " + ename + " in class " + enumClass);
		}
		return result;
	}

	public static Enum<?> toEnum(final Object value) throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Vector && (((Vector<?>) value).isEmpty()))
			return null;
		Enum<?> result = null;
		String en = String.valueOf(value);
		String ename = null;
		String cn = null;
		if (en.indexOf(' ') > 0) {
			cn = String.valueOf(value).substring(0, en.indexOf(' ')).trim();
			ename = String.valueOf(value).substring(en.indexOf(' ') + 1).trim();
		}
		if (cn == null || ename == null) {
			//			System.out.println("ALERT! This isn't going to work. cn is " + String.valueOf(cn) + " and ename is " + String.valueOf(ename));
		} else {
			try {
				Class<?> cls = DominoUtils.getClass(cn);
				result = toEnum(ename, cls);
			} catch (Exception e) {
				DominoUtils.handleException(e);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Enum<?>[] toEnums(final Object value) throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToEnums((Collection<Object>) value);
		} else if (value.getClass().isArray()) {
			return collectionToEnums(Arrays.asList(value));
		} else {
			Enum<?>[] classes = new Enum[1];
			classes[0] = toEnum(value);
			return classes;
		}
	}

	public static Enum<?>[] collectionToEnums(final Collection<Object> vector) throws DataNotCompatibleException {
		if (vector == null || vector.isEmpty())
			return new Enum[0];
		ClassLoader cl = Factory.getClassLoader();
		Enum<?>[] classes = new Enum[vector.size()];
		int i = 0;
		for (Object o : vector) {
			int pos = i++;
			String en = String.valueOf(o);
			String ename = null;
			String cn = null;
			if (en.indexOf(' ') > 0) {
				cn = String.valueOf(o).substring(0, en.indexOf(' ')).trim();
				ename = String.valueOf(o).substring(en.indexOf(' ') + 1).trim();
			}
			try {
				Class<?> cls = Class.forName(cn, false, cl);
				for (Object obj : cls.getEnumConstants()) {
					if (obj instanceof Enum) {
						if (((Enum<?>) obj).name().equals(ename)) {
							classes[pos] = (Enum<?>) obj;
						}
					}
				}
			} catch (ClassNotFoundException e) {
				System.out.println("Failed to find class " + cn + " using a classloader of type " + cl.getClass().getName());
				DominoUtils.handleException(e);
				classes[pos] = null;
			}
		}
		return classes;
	}

	@SuppressWarnings("unchecked")
	public static Formula[] toFormulas(final Object value) throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToFormulas((Collection<Object>) value);
		} else if (value.getClass().isArray()) {
			return collectionToFormulas(Arrays.asList(value));
		} else {
			Formula[] strings = new Formula[1];
			strings[0] = new org.openntf.domino.helpers.Formula(String.valueOf(value));
			return strings;
		}
	}

	public static Formula[] collectionToFormulas(final Collection<Object> vector) throws DataNotCompatibleException {
		if (vector == null || vector.isEmpty())
			return new Formula[0];
		Formula[] formulas = new Formula[vector.size()];
		int i = 0;
		for (Object o : vector) {
			Formula formula = new org.openntf.domino.helpers.Formula(String.valueOf(o));
			formulas[i++] = formula;
		}
		return formulas;
	}

	@SuppressWarnings("unchecked")
	public static BigString[] toBigStrings(final Object value) throws DataNotCompatibleException {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToBigStrings((Collection<Object>) value);
		} else if (value.getClass().isArray()) {
			return collectionToBigStrings(Arrays.asList(value));
		} else {
			BigString[] strings = new BigString[1];
			strings[0] = new BigString(String.valueOf(value));
			return strings;
		}
	}

	public static BigString[] collectionToBigStrings(final Collection<Object> vector) throws DataNotCompatibleException {
		if (vector == null || vector.isEmpty())
			return new BigString[0];
		BigString[] strings = new BigString[vector.size()];
		int i = 0;
		for (Object o : vector) {
			if (o instanceof org.openntf.domino.DateTime) {
				strings[i++] = new BigString(((org.openntf.domino.DateTime) o).getGMTTime());
			} else {
				strings[i++] = new BigString(String.valueOf(o));
			}
		}
		return strings;
	}

	@SuppressWarnings("unchecked")
	public static int[] toIntArray(final Object value) {
		if (value == null)
			return null;
		if (value instanceof Collection) {
			return collectionToIntArray((Collection<Integer>) value);
		} else if (value.getClass().isArray()) {
			if (value instanceof int[]) {
				return (int[]) value;
			}
			throw new DataNotCompatibleException("Can't convert an array of " + value.getClass().getName() + " to an int[] yet");
		} else {
			int[] ret = new int[1];
			ret[0] = toInt(value);
			return ret;
		}
	}

	public static int[] collectionToIntArray(final Collection<Integer> coll) {
		int[] ret = new int[coll.size()];
		Iterator<Integer> iterator = coll.iterator();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = iterator.next().intValue();
		}
		return ret;
	}

	public static short[] collectionToShortArray(final Collection<Short> coll) {
		short[] ret = new short[coll.size()];
		Iterator<Short> iterator = coll.iterator();
		for (int i = 0; i < ret.length; i++) {
			Short s = iterator.next();
			ret[i] = s.shortValue();
		}
		return ret;
	}

	public static long[] collectionToLongArray(final Collection<Long> coll) {
		long[] ret = new long[coll.size()];
		Iterator<Long> iterator = coll.iterator();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = iterator.next().longValue();
		}
		return ret;
	}

	public static byte[] collectionToByteArray(final Collection<Byte> coll) {
		byte[] ret = new byte[coll.size()];
		Iterator<Byte> iterator = coll.iterator();
		for (int i = 0; i < ret.length; i++) {
			ret[i] = iterator.next().byteValue();
		}
		return ret;
	}

}
