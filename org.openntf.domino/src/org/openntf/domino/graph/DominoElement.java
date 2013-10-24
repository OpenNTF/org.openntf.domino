package org.openntf.domino.graph;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.types.Null;

public abstract class DominoElement implements IDominoElement, Serializable {
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
			props_ = new ConcurrentHashMap<String, Serializable>();
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

	public Document getRawDocument() {
		return getDocument();
	}

	public String getRawId() {
		String prefix = getDatabase().getServer() + "!!" + getDatabase().getFilePath();
		return prefix + ": " + getRawDocument().getNoteID();
	}

	public int incrementProperty(final String propertyName) {
		Integer result = getProperty(propertyName, Integer.class);
		if (result == null)
			result = 0;
		setProperty(propertyName, ++result);
		return result;
	}

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

	public DominoGraph getParent() {
		return parent_;
	}

	public boolean hasProperty(final String key) {
		return getPropertyKeys().contains(key);
	}

	@Override
	public <T> T getProperty(final String key) {
		return getProperty(key, java.lang.Object.class);
	}

	public <T> T getProperty(final String propertyName, final Class<?> T) {
		Object result = null;
		Map<String, Serializable> props = getProps();
		// synchronized (props) {
		result = props.get(propertyName);
		if (result == null) {
			try {
				Document doc = getRawDocument();
				result = doc.getItemValue(propertyName, T);
				if (result instanceof Serializable) {
					synchronized (props) {
						props.put(propertyName, (Serializable) result);
					}
				}
			} catch (Exception e) {
				log_.log(Level.INFO, "Exception occured attempting to get value from document for " + propertyName
						+ " so we cannot return a value", e);
			}
		} else {
			if (result != null && !T.isAssignableFrom(result.getClass())) {
				// System.out.println("AH! We have the wrong type in the property cache! How did this happen?");
				try {
					Document doc = getRawDocument();
					result = doc.getItemValue(propertyName, T);
					if (result instanceof Serializable) {
						synchronized (props) {
							props.put(propertyName, (Serializable) result);
						}
					}
				} catch (Exception e) {
					log_.log(Level.INFO, "Exception occured attempting to get value from document for " + propertyName
							+ " but we have a value in the cache.", e);
				}
			}
		}
		// }
		//		if (result != null && !T.isAssignableFrom(result.getClass())) {
		//			log_.log(Level.WARNING, "Returning a " + result.getClass().getName() + " when we asked for a " + T.getName());
		//		}
		return (T) result;
	}

	public <T> T getProperty(final String propertyName, final Class<?> T, final boolean allowNull) {
		T result = getProperty(propertyName, T);
		if (allowNull) {
			return result;
		} else {
			if (result == null) {
				if (T.isArray())
					if (T.getComponentType() == String.class) {
						return (T) DEFAULT_STR_ARRAY;
					} else {
						return (T) Array.newInstance(T.getComponentType(), 0);
					}
				if (Boolean.class.equals(T) || Boolean.TYPE.equals(T))
					return (T) Boolean.FALSE;
				if (Integer.class.equals(T) || Integer.TYPE.equals(T))
					return (T) Integer.valueOf(0);
				if (Double.class.equals(T) || Double.TYPE.equals(T))
					return (T) Double.valueOf(0d);
				if (Float.class.equals(T) || Float.TYPE.equals(T))
					return (T) Float.valueOf(0f);
				if (String.class.equals(T))
					return (T) "";
				try {
					return (T) T.newInstance();
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
		// TODO - NTF cache?
		Set<String> result = new HashSet<String>();
		for (Item i : getRawDocument().getItems()) {
			result.add(i.getName());
		}
		return result;
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

	private final Set<String> removedProperties_ = new HashSet<String>();

	@Override
	public <T> T removeProperty(final String key) {
		getParent().startTransaction(this);
		T result = getProperty(key);
		Map<String, Serializable> props = getProps();
		synchronized (props) {
			props.remove(key);
		}
		Document doc = getRawDocument();
		synchronized (doc) {
			doc.removeItem(key);
		}
		synchronized (removedProperties_) {
			removedProperties_.add(key);
		}
		return result;
	}

	// public void save() {
	// getRawDocument().save();
	// }

	public void setRawDocument(final org.openntf.domino.Document doc) {
		unid_ = doc.getUniversalID().toUpperCase();
	}

	private final Set<String> changedProperties_ = new HashSet<String>();

	@Override
	public void setProperty(final String propertyName, final java.lang.Object value) {
		getParent().startTransaction(this);
		Map<String, Serializable> props = getProps();
		Object old = null;
		if (props != null) {
			if (propertyName != null) {
				synchronized (props) {
					if (value instanceof Serializable) {
						old = props.put(propertyName, (Serializable) value);
					} else if (value == null) {
						// TODO set some alternative value for NULL in the event of a null value!!!
						old = props.put(propertyName, Null.INSTANCE);
						// old = props.put(propertyName, null);
					} else {
						System.out.println("Attemped caching of value of type " + value.getClass().getName() + " that isn't Serializable");
					}
				}
			} else {
				System.out.println("propertyName is null on a setProperty request?");
			}
		} else {
			System.out.println("Properties are null for element!");
		}
		// if ((old == null || value == null) || !value.equals(old)) {
		synchronized (changedProperties_) {
			changedProperties_.add(propertyName);
		}
		// }
		// Document doc = getRawDocument();
		// synchronized (doc) {
		// doc.replaceItemValue(propertyName, value);
		// }
	}

	protected void reapplyChanges() {
		Map<String, Serializable> props = getProps();
		Document doc = getDocument();
		synchronized (props) {
			if (props.isEmpty()) {
				// System.out.println("Cached properties is empty!");
			} else {
				synchronized (changedProperties_) {
					// System.out.println("Re-applying cached properties: " + changedProperties_.size());
					for (String key : changedProperties_) {
						Object v = props.get(key);
						if (v == null) {
							// System.out.println("Writing a null value for property: " + key
							// + " to an Element document. Probably not good...");
						}
						if (key.startsWith(DominoVertex.IN_PREFIX) || key.startsWith(DominoVertex.OUT_PREFIX)) {
							doc.replaceItemValue(key, v, false);
						} else {
							doc.replaceItemValue(key, v);
						}
					}
					changedProperties_.clear();
				}

			}
		}
		synchronized (removedProperties_) {
			for (String key : removedProperties_) {
				doc.removeItem(key);
			}
		}
	}

	public int incrementProperty(final IDominoProperties prop) {
		return incrementProperty(prop.getName());
	}

	public int decrementProperty(final IDominoProperties prop) {
		return decrementProperty(prop.getName());
	}

	public <T> T getProperty(final IDominoProperties prop) {
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

	public void setProperty(final IDominoProperties prop, final java.lang.Object value) {
		Object current = getProperty(prop, true);
		if (current == null || !current.equals(value)) {
			setProperty(prop.getName(), value);
		}
	}

}
