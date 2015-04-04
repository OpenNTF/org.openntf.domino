package org.openntf.domino.graph2.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;

import org.openntf.domino.Document;
import org.openntf.domino.big.impl.NoteCoordinate;
//import javolution.util.FastMap;
//import javolution.util.FastSet;
//import javolution.util.function.Equalities;
import org.openntf.domino.big.impl.NoteList;
import org.openntf.domino.graph.DominoVertex;
import org.openntf.domino.types.Null;
import org.openntf.domino.utils.TypeUtils;

public abstract class DElement implements org.openntf.domino.graph2.DElement, Serializable {
	private static final Logger log_ = Logger.getLogger(DElement.class.getName());
	private static final long serialVersionUID = 1L;
	public static final String TYPE_FIELD = "_OPEN_GRAPHTYPE";

	public static enum Deferred {
		INSTANCE;
	}

	protected transient org.openntf.domino.graph2.DGraph parent_;
	private Object delegateKey_;
	private transient Map<String, Object> delegate_;

	public DElement(final org.openntf.domino.graph2.DGraph parent) {
		parent_ = parent;
	}

	protected org.openntf.domino.graph2.impl.DGraph getParent() {
		return (org.openntf.domino.graph2.impl.DGraph) parent_;
	}

	private Set<String> changedProperties_;

	private Set<String> getChangedPropertiesInt() {
		if (changedProperties_ == null) {
			changedProperties_ = Collections.synchronizedSet(new TreeSet<String>(String.CASE_INSENSITIVE_ORDER));
		}
		return changedProperties_;
	}

	private Set<String> removedProperties_;

	private Set<String> getRemovedPropertiesInt() {
		if (removedProperties_ == null) {
			removedProperties_ = Collections.synchronizedSet(new TreeSet<String>(String.CASE_INSENSITIVE_ORDER));
		}
		return removedProperties_;
	}

	private Map<String, Object> props_;

	private Map<String, Object> getProps() {
		if (props_ == null) {
			Map<String, Object> localProps = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
			for (String key : getDelegate().keySet()) {
				localProps.put(key, Deferred.INSTANCE);
			}
			props_ = Collections.synchronizedMap(localProps);
		}
		return props_;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProperty(final String propertyName, final Class<T> type) {
		//TODO NTF cached properties should be automatically reset if the base Document is known to have changed
		Object result = null;
		Map<String, Object> props = getProps();
		result = props.get(propertyName);
		if (result == null || Deferred.INSTANCE.equals(result)) {
			try {
				Map<String, Object> delegate = getDelegate();
				if (delegate instanceof Document) {
					Document doc = (Document) delegate;
					if (doc.hasItem(propertyName)) {
						result = doc.getItemValue(propertyName, type);
					}
					if (result == null) {
						try {
							Object raw = doc.get(propertyName);
							result = TypeUtils.objectToClass(raw, type, doc.getAncestorSession());
						} catch (Throwable t) {
							log_.log(Level.WARNING, "Invalid property for document " + propertyName);
						}
					}
				} else {
					result = type.cast(delegate.get(propertyName));
				}
				if (result == null) {
					props.put(propertyName, Null.INSTANCE);
				} else if (result instanceof Serializable) {
					props.put(propertyName, result);
				} else {
					log_.log(Level.FINE, "Got a value from the document but it's not Serializable. It's a " + result.getClass().getName());
					props.put(propertyName, result);
				}
			} catch (Exception e) {
				log_.log(Level.WARNING, "Exception occured attempting to get value from document for " + propertyName
						+ " so we cannot return a value", e);
			}
		} else if (result == Null.INSTANCE) {

		} else {
			if (result != null && !type.isAssignableFrom(result.getClass())) {
				System.out.println(propertyName + " returned a " + result.getClass().getName() + " when we asked for a " + type.getName());

				try {
					Map<String, Object> delegate = getDelegate();
					if (delegate instanceof Document) {
						Document doc = (Document) delegate;
						result = doc.getItemValue(propertyName, type);
					} else {
						Object chk = delegate.get(propertyName);
						if (chk != null) {
							result = type.cast(delegate.get(propertyName));
						}
					}
					if (result == null) {
						props.put(propertyName, Null.INSTANCE);
					} else if (result instanceof Serializable) {
						props.put(propertyName, result);
					} else {
						log_.log(Level.FINE, "Got a value from the document but it's not Serializable. It's a "
								+ result.getClass().getName());
						props.put(propertyName, result);
					}
				} catch (Exception e) {
					log_.log(Level.WARNING, "Exception occured attempting to get value from document for " + propertyName
							+ " but we have a value in the cache.", e);
				}
			}
		}
		if (result == Null.INSTANCE) {
			result = null;
		}
		return (T) result;
	}

	@Override
	public <T> T getProperty(final String key) {
		if ("form".equalsIgnoreCase(key)) {
			return (T) getProperty(key, String.class);
		}
		Object result = getProperty(key, java.lang.Object.class);
		return (T) result;
	}

	@Override
	public Set<String> getPropertyKeys() {
		return Collections.unmodifiableSet(getProps().keySet());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setProperty(final String key, final Object value) {
		boolean isEqual = false;
		Map<String, Object> props = getProps();
		if (props != null) {
			if (key != null) {
				Object current = null;
				if (value != null) {
					current = getProperty(key, value.getClass());
				} else {
					current = getProperty(key);
				}
				if (current == null && value == null) {
					//					System.out.println("TEMP DEBUG: Both current and new value for " + key + " are null");
					return;
				}
				//NTF The standard equality check has a fast out based on array size, so I think it's safe to use here...
				/*if (key.startsWith(DominoVertex.IN_PREFIX) || key.startsWith(DominoVertex.OUT_PREFIX)) {
					isEqual = false;	//NTF ALWAYS set edge collections. Checking them can be expensive
				} else*/if (value != null && current != null) {
					if (!(value instanceof java.util.Collection) && !(value instanceof java.util.Map) && !value.getClass().isArray()) {
						isEqual = value.equals(current);
					}
					if (value instanceof java.util.Collection && current instanceof java.util.Collection) {
						if (((java.util.Collection) value).size() == ((java.util.Collection) current).size()) {
							Object[] vArray = ((java.util.Collection) value).toArray();
							Object[] cArray = ((java.util.Collection) current).toArray();
							isEqual = Arrays.deepEquals(vArray, cArray);
						} else {
							isEqual = false;
						}
					}
					if (value.getClass().isArray() && current.getClass().isArray()) {
						Object[] vArray = (Object[]) value;
						Object[] cArray = (Object[]) current;
						isEqual = Arrays.deepEquals(vArray, cArray);
					}
				}
				if (isEqual) {
					//					System.out.println("TEMP DEBUG: Not setting property " + key + " because the new value is equal to the existing value");
					log_.log(Level.FINE, "Not setting property " + key + " because the new value is equal to the existing value");
				}
				if (value instanceof Serializable) {
					if (current == null || Null.INSTANCE.equals(current)) {
						getParent().startTransaction(this);
						getChangedPropertiesInt().add(key);
						props.put(key, value);
					} else if (!isEqual) {
						getParent().startTransaction(this);
						getChangedPropertiesInt().add(key);
						props.put(key, value);
					} else {
					}
				} else if (value == null) {
					if (!Null.INSTANCE.equals(current)) {
						getParent().startTransaction(this);
						getChangedPropertiesInt().add(key);
						props.put(key, value);
					}
				} else {
					log_.log(Level.FINE, "Attempted to set property " + key + " to a non-serializable value: " + value.getClass().getName());
					if (current == null || Null.INSTANCE.equals(current)) {
						getParent().startTransaction(this);
						getChangedPropertiesInt().add(key);
						props.put(key, value);
					} else if (!isEqual) {
						getParent().startTransaction(this);
						getChangedPropertiesInt().add(key);
						props.put(key, value);
					} else {
					}
				}
			} else {
				log_.log(Level.WARNING, "propertyName is null on a setProperty request?");
			}
		} else {
			log_.log(Level.WARNING, "Properties are null for element!");
		}
	}

	@Override
	public Object removeProperty(final String key) {
		getParent().startTransaction(this);
		Object result = getProperty(key);
		Map<String, Object> props = getProps();
		props.remove(key);
		Map<String, Object> source = getDelegate();
		source.remove(key);
		getRemovedPropertiesInt().add(key);
		return result;
	}

	@Override
	public abstract void remove();

	void _remove() {
		getParent().startTransaction(this);
		getParent().removeDelegate(this);
	}

	@Override
	public Object getId() {
		if (delegateKey_ == null) {
			Map<String, Object> delegate = getDelegate();
			if (delegate instanceof Document) {
				//				NoteCoordinate nc
				delegateKey_ = new NoteCoordinate((Document) delegate);
				//				String replid = ((Document) delegate).getAncestorDatabase().getReplicaID().toLowerCase();
				//				String unid = ((Document) delegate).getUniversalID().toLowerCase();
				//				delegateKey_ = replid + unid;
			}
		}
		return delegateKey_;
	}

	@Override
	public boolean hasProperty(final String key) {
		return getPropertyKeys().contains(key);
	}

	@Override
	public <T> T getProperty(final String key, final Class<T> type, final boolean allowNull) {
		T result = getProperty(key, type);
		if (allowNull) {
			return result;
		} else {
			if (result == null || Null.INSTANCE == result) {
				return TypeUtils.getDefaultInstance(type);
			} else {
				return result;
			}
		}
	}

	@Override
	public int incrementProperty(final String key) {
		// TODO NTF it would be really great to figure out a way to use primitives here
		Integer result = getProperty(key, Integer.class);
		if (result == null)
			result = 0;
		setProperty(key, ++result);
		return result;
	}

	@Override
	public int decrementProperty(final String key) {
		// TODO NTF it would be really great to figure out a way to use primitives here
		Integer result = getProperty(key, Integer.class);
		if (result == null)
			result = 0;
		setProperty(key, --result);
		return result;
	}

	@Override
	public Map<String, Object> getDelegate() {
		if (delegate_ instanceof Document) {
			try {
				//FIXME: This shouldn't be done this way. .isDead should really know for sure if it is not going to work across threads...
				((Document) delegate_).containsKey("Foo");
			} catch (Throwable t) {
				delegate_ = getParent().findDelegate(delegateKey_);
			}
		}
		if (delegate_ == null) {
			delegate_ = getParent().findDelegate(delegateKey_);
		}
		return delegate_;
	}

	@Override
	public void setDelegate(final Map<String, Object> delegate) {
		delegate_ = delegate;
	}

	@Override
	public Map<String, Object> toMap(final String[] props) {
		FastMap<String, Object> result = new FastMap<String, Object>();
		for (String prop : props) {
			result.put(prop, getProperty(prop));
		}
		return result.unmodifiable();
	}

	@Override
	public Map<String, Object> toMap(final Set<String> props) {
		return toMap(props.toArray(TypeUtils.DEFAULT_STR_ARRAY));
	}

	@Override
	public void fromMap(final Map<String, Object> map) {
		// TODO Auto-generated method stub

	}

	protected void applyChanges() {
		Map<String, Object> props = getProps();
		Map<String, Object> delegate = getDelegate();
		Set<String> changes = getChangedPropertiesInt();
		if (!props.isEmpty() && !changes.isEmpty()) {
			//			System.out.println("TEMP DEBUG: Writing " + getChangedPropertiesInt().size() + " changed properties for " + getId());
			for (String s : getChangedPropertiesInt()) {
				String key = s;
				Object v = props.get(key);
				//				System.out.println("TEMP DEBUG: Writing a " + v.getClass().getSimpleName() + " to " + key);
				if (s.startsWith(DominoVertex.IN_PREFIX) || s.startsWith(DominoVertex.OUT_PREFIX)) {
					if (delegate instanceof Document) {
						if (v instanceof NoteList) {
							byte[] bytes = ((NoteList) v).toByteArray();
							((Document) delegate).writeBinary(s, bytes, 2048 * 24);
							//FIXME NTF .writeBinary needs to clear any extra items added to the document if the binary content shrank
							//							System.out.println("TEMP DEBUG: Writing a NoteList (" + ((NoteList) v).size() + ") of size " + bytes.length
							//									+ " to a Document in " + s);
						} else {
							((Document) delegate).replaceItemValue(s, v, false);
						}
					} else {
						delegate.put(s, v);
					}
				} else {
					delegate.put(s, v);
				}
			}
			getChangedPropertiesInt().clear();
		} else {
			//			System.out.println("TEMP ALERT: No changed properties for element " + getId());
		}
		for (String key : getRemovedPropertiesInt()) {
			delegate.remove(key);
		}
		getRemovedPropertiesInt().clear();
		if (delegate instanceof Document) {
			((Document) delegate).save();
		}
	}

	@Override
	public void rollback() {
		getProps().clear();
		getChangedPropertiesInt().clear();
		getRemovedPropertiesInt().clear();
	}

	@Override
	public void commit() {

	}

}
