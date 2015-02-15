package org.openntf.domino.graph;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastSet;
import javolution.util.function.Equalities;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.types.BigString;
import org.openntf.domino.types.Null;
import org.openntf.domino.utils.BeanUtils;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.TypeUtils;

import com.tinkerpop.blueprints.Element;

public abstract class DominoElement implements IDominoElement, Serializable {

	public static boolean setReflectiveProperty(final IDominoElement element, final String prop, final Object value) {
		if (prop == null || prop.isEmpty())
			throw new IllegalArgumentException("Cannot set a null or empty property on a DominoElement");
		boolean result = false;
		Object localValues = value;
		Class<? extends Element> elemClass = element.getClass();
		IDominoProperties domProp = IDominoProperties.Reflect.findMappedProperty(elemClass, prop);
		if (domProp != null) {
			localValues = TypeUtils.objectToClass(value, domProp.getType(), null);
		}
		//TODO NTF - first see if it's a mapped property and if so, find out the type and coerce the value to the proper type
		Method setter = BeanUtils.findSetter(elemClass, prop, BeanUtils.toParameterType(localValues));
		if (setter != null) {
			try {
				setter.invoke(element, localValues);
				result = true;
			} catch (Exception e) {
				DominoUtils.handleException(e);
				System.out.println("Unable to invoke " + setter.getName() + " on a " + element.getClass() + " for property: " + prop
						+ " with a value of " + String.valueOf(value));
			}
		} else {
			if (domProp != null) {
				element.setProperty(domProp, localValues);
				result = true;
			} else {
				element.setProperty(prop, localValues);
				result = true;
			}
		}
		return result;
	}

	public static Object getReflectiveProperty(final IDominoElement element, final String prop) {
		if (prop == null || prop.isEmpty())
			throw new IllegalArgumentException("Cannot set a null or empty property on a DominoElement");
		Object result = false;
		Class<? extends Element> elemClass = element.getClass();
		Method getter = BeanUtils.findGetter(elemClass, prop);
		if (getter != null) {
			try {
				result = getter.invoke(element, (Object[]) null);
			} catch (Exception e) {
				DominoUtils.handleException(e);
				System.out.println("Unable to invoke " + getter.getName() + " on a " + element.getClass() + " for property: " + prop);
			}
		} else {
			IDominoProperties domProp = IDominoProperties.Reflect.findMappedProperty(elemClass, prop);
			if (domProp != null) {
				result = element.getProperty(domProp);
			} else {
				result = element.getProperty(prop);
			}
		}
		return result;
	}

	public static Object getReflectiveProperty(final IDominoElement element, final IDominoProperties prop) {
		return prop.getType().cast(getReflectiveProperty(element, prop.getName()));
	}

	private static final Logger log_ = Logger.getLogger(DominoElement.class.getName());
	private static final long serialVersionUID = 1L;
	public static final String TYPE_FIELD = "_OPEN_GRAPHTYPE";
	private String key_;
	protected transient DominoGraph parent_;
	private String unid_;
	private Map<String, Serializable> props_;
	public final String[] DEFAULT_STR_ARRAY = { "" };

	public static Document toDocument(final DominoElement element) {
		return element.getRawDocument();
	}

	public static enum Properties implements IDominoProperties {
		TITLE(String.class), KEY(String.class), FORM(String.class);

		private Class<?> type_;

		Properties(final Class<?> type) {
			type_ = type;
		}

		@Override
		public Class<?> getType() {
			return type_;
		}

		@Override
		public String getName() {
			return super.name();
		}
	}

	public DominoElement(final DominoGraph parent, final Document doc) {
		parent_ = parent;

		unid_ = doc.getUniversalID().toUpperCase();
	}

	private transient java.lang.Object lockHolder_;

	public synchronized boolean hasLock() {
		return lockHolder_ != null;
	}

	public synchronized boolean lock(final java.lang.Object lockHolder) {
		if (lockHolder_ == null) {
			lockHolder_ = lockHolder;
			return true;
		}
		return false;
	}

	public synchronized boolean unlock(final java.lang.Object lockHolder) {
		if (lockHolder.equals(lockHolder_)) {
			lockHolder_ = null;
			return true;
		}
		return false;
	}

	synchronized void unlock() {
		lockHolder_ = null;
	}

	public String getTitle() {
		return getProperty(Properties.TITLE, false);
	}

	public void setTitle(final String value) {
		setProperty(Properties.TITLE, value);
	}

	public String getKey() {
		return getProperty(Properties.KEY, false);
	}

	public void setKey(final String value) {
		setProperty(Properties.KEY, value);
	}

	public String getForm() {
		return getProperty(Properties.FORM, false);
	}

	public void setForm(final String value) {
		String current = getForm();
		if (current == null || !current.equalsIgnoreCase(value)) {
			setProperty(Properties.FORM, value);
		}
	}

	private Boolean isNew_;

	void setNew(final boolean isnew) {
		isNew_ = isnew;
	}

	public boolean isNew() {
		if (isNew_ == null) {
			isNew_ = false;
		}
		return isNew_;
	}

	private Map<String, Serializable> getProps() {
		if (props_ == null) {
			props_ = new FastMap<String, Serializable>(Equalities.LEXICAL_CASE_INSENSITIVE).atomic();
		}
		return props_;
	}

	public void addProperty(final String propertyName, final Object value) {
		setProperty(propertyName, value);
	}

	private Database getDatabase() {
		return getParent().getRawDatabase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (o instanceof DominoElement) {
			return ((DominoElement) o).getId().equals(getId());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public Document getRawDocument() {
		return getDocument();
	}

	public String getRawId() {
		String prefix = getDatabase().getServer() + "!!" + getDatabase().getFilePath();
		return prefix + ": " + getRawDocument().getNoteID();
	}

	@Override
	public int incrementProperty(final String propertyName) {
		Integer result = getProperty(propertyName, Integer.class);
		if (result == null)
			result = 0;
		setProperty(propertyName, ++result);
		return result;
	}

	@Override
	public int decrementProperty(final String propertyName) {
		Integer result = getProperty(propertyName, Integer.class);
		if (result == null)
			result = 0;
		setProperty(propertyName, --result);
		return result;
	}

	private Document getDocument() {
		return getParent().getDocument(unid_, true);
		// Map<String, Document> map = documentCache.get();
		// Document doc = map.get(unid_);
		// if (doc == null) {
		// synchronized (map) {
		// doc = getDatabase().getDocumentByKey(unid_, true);
		// String localUnid = doc.getUniversalID().toUpperCase();
		// if (!unid_.equals(localUnid)) {
		// log_.log(Level.SEVERE, "UNIDs do not match! Expected: " + unid_ + ", Result: " + localUnid);
		// }
		// map.put(unid_, doc);
		// }
		// }
		// return doc;
	}

	@Override
	public String getId() {
		if (key_ == null) {
			key_ = unid_;
		}
		return key_;
	}

	@Override
	public DominoGraph getParent() {
		return parent_;
	}

	@Override
	public boolean hasProperty(final String key) {
		return getPropertyKeys().contains(key);
	}

	@Override
	public <T> T getProperty(final String key) {
		return (T) getProperty(key, java.lang.Object.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProperty(final String propertyName, final Class<T> type) {
		Object result = null;
		String key = propertyName;
		Map<String, Serializable> props = getProps();
		// synchronized (props) {
		result = props.get(key);
		if (result == null) {
			//			if ("PROGNAME".equalsIgnoreCase(propertyName)) {
			//				System.out.println("DEBUG: " + propertyName + " getting from Document");
			//			}
			try {
				Document doc = getRawDocument();
				result = doc.getItemValue(propertyName, type);
				if (result == null) {
					//					synchronized (props) {
					props.put(key, Null.INSTANCE);
					//					}
				} else if (result instanceof Serializable) {
					//					synchronized (props) {
					props.put(key, (Serializable) result);
					//					}
				} else {
					log_.log(Level.WARNING, "Got a value from the document but it's not Serializable. It's a "
							+ result.getClass().getName());
				}
			} catch (Exception e) {
				log_.log(Level.WARNING, "Exception occured attempting to get value from document for " + propertyName
						+ " so we cannot return a value", e);
			}
		} else if (result == Null.INSTANCE) {

		} else {
			if (result != null && !type.isAssignableFrom(result.getClass())) {
				//				if ("PROGNAME".equalsIgnoreCase(propertyName)) {
				//					System.out.println("DEBUG: " + propertyName + " result is a " + result.getClass().getSimpleName());
				//				}
				// System.out.println("AH! We have the wrong type in the property cache! How did this happen?");
				try {
					Document doc = getRawDocument();
					result = doc.getItemValue(propertyName, type);
					if (result == null) {
						//						synchronized (props) {
						props.put(key, Null.INSTANCE);
						//						}
					} else if (result instanceof Serializable) {
						//						synchronized (props) {
						props.put(key, (Serializable) result);
						//						}
					}
				} catch (Exception e) {
					log_.log(Level.WARNING, "Exception occured attempting to get value from document for " + propertyName
							+ " but we have a value in the cache.", e);
				}
			} else {
				//				if ("PROGNAME".equalsIgnoreCase(propertyName)) {
				//					System.out.println("DEBUG: " + propertyName + " result is a " + result.getClass().getSimpleName());
				//				}
			}
		}
		// }
		//		if (result != null && !T.isAssignableFrom(result.getClass())) {
		//			log_.log(Level.WARNING, "Returning a " + result.getClass().getName() + " when we asked for a " + T.getName());
		//		}
		if (result == Null.INSTANCE) {
			result = null;
		}
		//		if ("PROGNAME".equalsIgnoreCase(propertyName)) {
		//			System.out.println("DEBUG: " + propertyName + " result is a " + (result == null ? "null" : result.getClass().getSimpleName()));
		//		}
		return (T) result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProperty(final String propertyName, final Class<T> type, final boolean allowNull) {
		T result = getProperty(propertyName, type);
		if (allowNull) {
			return result;
		} else {
			if (result == null || Null.INSTANCE == result) {
				if (type.isArray())
					if (type.getComponentType() == String.class) {
						return (T) DEFAULT_STR_ARRAY;
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
			} else {
				return result;
			}
		}
	}

	@Override
	public Set<String> getPropertyKeys() {

		return getPropertyKeys(true);
	}

	private FastSet<String> propKeys_;
	private boolean checkedDocProps_ = false;

	private FastSet<String> getPropKeysInt() {
		if (propKeys_ == null) {
			propKeys_ = new FastSet<String>(Equalities.LEXICAL_CASE_INSENSITIVE).atomic();
		}
		return propKeys_;
	}

	@Override
	public Set<String> getPropertyKeys(final boolean includeEdgeFields) {
		if (!checkedDocProps_) {
			Set<String> raws = getRawDocument().keySet();
			getPropKeysInt().addAll(raws);
			checkedDocProps_ = true;
		}
		if (includeEdgeFields) {
			return getPropKeysInt().unmodifiable();
		} else {
			FastSet<String> result = new FastSet<String>(Equalities.LEXICAL_CASE_INSENSITIVE);
			for (String name : getPropKeysInt()) {
				if (!(name.startsWith(DominoVertex.IN_PREFIX) || name.startsWith(DominoVertex.OUT_PREFIX))) {
					result.add(name);
				}
			}
			return result.unmodifiable();
		}
	}

	@Override
	public abstract void remove();

	//	{
	//		getParent().startTransaction(this);
	//		getRawDocument().removePermanently(true);
	//	}

	void _remove() {
		getParent().startTransaction(this);
		getRawDocument().removePermanently(true);
	}

	private FastSet<String> removedProperties_;

	private FastSet<String> getRemovedPropertiesInt() {
		if (removedProperties_ == null) {
			removedProperties_ = new FastSet<String>(Equalities.LEXICAL_CASE_INSENSITIVE).atomic();
		}
		return removedProperties_;
	}

	@Override
	public <T> T removeProperty(final String key) {
		getParent().startTransaction(this);
		T result = getProperty(key);
		Map<String, Serializable> props = getProps();
		//		synchronized (props) {
		props.remove(key);
		//		}
		Document doc = getRawDocument();
		synchronized (doc) {
			doc.removeItem(key);
		}
		//		synchronized (removedProperties_) {
		getRemovedPropertiesInt().add(key);
		//		}
		//		synchronized (propKeys_) {
		getPropKeysInt().remove(key);
		//		}
		return result;
	}

	// public void save() {
	// getRawDocument().save();
	// }

	@Override
	public void setRawDocument(final org.openntf.domino.Document doc) {
		unid_ = doc.getUniversalID().toUpperCase();
	}

	private FastSet<String> changedProperties_;

	private FastSet<String> getChangedPropertiesInt() {
		if (changedProperties_ == null) {
			changedProperties_ = new FastSet<String>(Equalities.LEXICAL_CASE_INSENSITIVE).atomic();
		}
		return changedProperties_;
	}

	//	void setProperty(final String propertyName, final java.lang.Object value, final boolean force) {
	//
	//	}

	@SuppressWarnings("unused")
	@Override
	public void setProperty(final String propertyName, final java.lang.Object value) {
		//		if ("PROGNAME".equalsIgnoreCase(propertyName)) {
		//			System.out.println("DEBUG Setting " + propertyName);
		//		}
		boolean isEdgeCollection = false;
		boolean isEqual = false;
		String key = propertyName;
		Map<String, Serializable> props = getProps();
		Object old = null;
		if (props != null) {
			if (propertyName != null) {

				//				synchronized (propKeys_) {
				getPropKeysInt().add(propertyName);
				//				}
				Object current = getProperty(propertyName);
				if (propertyName.startsWith(DominoVertex.IN_PREFIX) && value instanceof java.util.Collection) {
					isEdgeCollection = true;
				}
				if (current == null && value == null) {
					return;
				}
				if (value != null && current != null) {
					if (!(value instanceof java.util.Collection) && !(value instanceof java.util.Map) && !value.getClass().isArray()) {
						isEqual = value.equals(current);
					}
				}
				if (isEqual) {
					log_.log(Level.FINE, "Not setting property " + propertyName + " because the new value is equal to the existing value");
				}
				boolean changeMade = false;
				//				synchronized (props) {

				if (value instanceof Serializable) {
					if (current == null || Null.INSTANCE.equals(current)) {
						//							if ("PROGNAME".equalsIgnoreCase(propertyName)) {
						//								System.out.println("DEBUG: " + propertyName + " checking FROM NULL values from " + String.valueOf(current)
						//										+ " to " + String.valueOf(value));
						//							}
						getParent().startTransaction(this);
						old = props.put(key, (Serializable) value);
						//							synchronized (changedProperties_) {
						getChangedPropertiesInt().add(propertyName);
						//							}
					} else if (!isEqual) {
						getParent().startTransaction(this);
						old = props.put(key, (Serializable) value);
						//							synchronized (changedProperties_) {
						getChangedPropertiesInt().add(propertyName);
						//							}
					} else {
						//							if ("PROGNAME".equalsIgnoreCase(propertyName)) {
						//								System.out.println("DEBUG: " + propertyName + " equal?? values match from " + String.valueOf(current)
						//										+ " to " + String.valueOf(value));
						//							}
					}
				} else if (value == null) {
					if (current != null && !current.equals(Null.INSTANCE)) {
						getParent().startTransaction(this);
						old = props.put(key, Null.INSTANCE);
						//							synchronized (changedProperties_) {
						getChangedPropertiesInt().add(propertyName);
						//							}
					}
				} else {
					//						if ("PROGNAME".equalsIgnoreCase(propertyName)) {
					//							System.out.println("DEBUG: " + propertyName + " values from " + String.valueOf(current) + " to "
					//									+ String.valueOf(value));
					//						}
					log_.log(Level.WARNING, "Attempted to set property " + propertyName + " to a non-serializable value: "
							+ value.getClass().getName());
				}
				//				}

			} else {
				log_.log(Level.WARNING, "propertyName is null on a setProperty request?");
			}
		} else {
			log_.log(Level.WARNING, "Properties are null for element!");
		}
	}

	protected void reapplyChanges() {
		Map<String, Serializable> props = getProps();
		Document doc = getDocument();
		//		synchronized (props) {
		if (props.isEmpty()) {
			// System.out.println("Cached properties is empty!");
		} else {
			//				synchronized (changedProperties_) {
			// System.out.println("Re-applying cached properties: " + changedProperties_.size());
			for (String s : getChangedPropertiesInt()) {
				//				CharSequence key = new CaseInsensitiveString(s);
				String key = s;
				Object v = props.get(key);
				if (v == null) {
					// System.out.println("Writing a null value for property: " + key
					// + " to an Element document. Probably not good...");
				}
				if (s.startsWith(DominoVertex.IN_PREFIX) || s.startsWith(DominoVertex.OUT_PREFIX)) {
					doc.replaceItemValue(s, v, false);
				} else {
					doc.replaceItemValue(s, v);
				}
			}
			getChangedPropertiesInt().clear();
			//				}

		}
		//		}
		//		synchronized (removedProperties_) {
		for (String key : getRemovedPropertiesInt()) {
			doc.removeItem(key);
		}
		//		}
	}

	@Override
	public int incrementProperty(final IDominoProperties prop) {
		return incrementProperty(prop.getName());
	}

	@Override
	public int decrementProperty(final IDominoProperties prop) {
		return decrementProperty(prop.getName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProperty(final IDominoProperties prop) {
		if (prop == null) {
			log_.log(Level.WARNING, "getProperty was called with a null argument, therefore it's impossible to return a property.");
			return null;
		}
		Class<?> type = prop.getType();
		Object result = getProperty(prop.getName(), type);
		if (result != null && type.isAssignableFrom(result.getClass())) {
			return (T) type.cast(result);
		} else {
			// System.out.println("Property returned a " + (result == null ? "null" : result.getClass().getName())
			// + " even though we requested a " + type.getName());
		}
		return (T) result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProperty(final IDominoProperties prop, final boolean allowNull) {
		Class<?> type = prop.getType();
		Object result = getProperty(prop.getName(), type, allowNull);
		if (result != null && type.isAssignableFrom(result.getClass())) {
			return (T) type.cast(result);
		} else {
			// System.out.println("Property returned a " + (result == null ? "null" : result.getClass().getName())
			// + " even though we requested a " + type.getName());
		}
		return (T) result;
	}

	@Override
	public void setProperty(final IDominoProperties prop, final java.lang.Object value) {
		Object current = getProperty(prop, true);
		if (current == null || !current.equals(value)) {
			setProperty(prop.getName(), value);
		}
	}

	public static Object fromMapValue(final String key, final Object value) {
		Object result = value;

		return result;
	}

	public static Object toMapValue(final Object value) {
		Object result = value;
		if (EnumSet.class.isAssignableFrom(value.getClass())) {
			System.out.println("DEBUG: Mapping an EnumSet");
			if (!((EnumSet<?>) value).isEmpty()) {
				StringBuilder eListing = new StringBuilder();
				eListing.append('[');
				for (Object rawEnum : (EnumSet<?>) value) {
					if (Enum.class.isAssignableFrom(rawEnum.getClass())) {
						eListing.append(((Enum<?>) rawEnum).name());
					} else {
						eListing.append("ERROR: expected Enum was a " + rawEnum.getClass().getName());
					}
					eListing.append(',');
				}
				eListing.deleteCharAt(eListing.length() - 1);
				eListing.append(']');
				result = eListing.toString();
			} else {
				result = "";
			}
		} else if (Enum.class.isAssignableFrom(value.getClass())) {
			result = ((Enum<?>) value).name();
		} else if (CharSequence.class.isAssignableFrom(value.getClass())) {
			result = ((CharSequence) value).toString();
		} else if (BigString.class.isAssignableFrom(value.getClass())) {
			result = ((BigString) value).toString();
		} else {
			result = value;
		}
		return result;
	}

	public Map<String, Object> toMap(final IDominoProperties[] props, final byte keyStyle) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		for (IDominoProperties prop : props) {
			String mapKey = prop.getName();
			if (keyStyle == Character.LOWERCASE_LETTER) {
				mapKey = mapKey.toLowerCase();
			} else if (keyStyle == Character.UPPERCASE_LETTER) {
				mapKey = mapKey.toUpperCase();
			}
			Object value = getProperty(prop, true);
			if (value != null) {
				result.put(mapKey, toMapValue(value));
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> toMap(final IDominoProperties[] props) {
		return toMap(props, (byte) 0);
	}

	public Map<String, Object> toMap(final Set<IDominoProperties> props, final byte keyStyle) {
		return toMap(props.toArray(new IDominoProperties[props.size()]), keyStyle);
	}

	@Override
	public Map<String, Object> toMap(final Set<IDominoProperties> props) {
		return toMap(props, (byte) 0);
	}

	public boolean fromMap(final Map<String, Object> map) {
		boolean result = true;
		for (String key : map.keySet()) {
			boolean success = DominoElement.setReflectiveProperty(this, key, map.get(key));
			if (!success)
				result = false;
		}
		return result;
	}

}
