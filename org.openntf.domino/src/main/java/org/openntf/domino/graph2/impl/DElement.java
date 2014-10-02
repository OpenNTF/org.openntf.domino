package org.openntf.domino.graph2.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastSet;
import javolution.util.function.Equalities;

import org.openntf.domino.Document;
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
	private String delegateKey_;
	private transient Map<String, Object> delegate_;

	public DElement(final org.openntf.domino.graph2.DGraph parent) {
		parent_ = parent;
	}

	protected org.openntf.domino.graph2.impl.DGraph getParent() {
		return (org.openntf.domino.graph2.impl.DGraph) parent_;
	}

	private FastSet<String> changedProperties_;

	private FastSet<String> getChangedPropertiesInt() {
		if (changedProperties_ == null) {
			changedProperties_ = new FastSet<String>(Equalities.LEXICAL_CASE_INSENSITIVE).atomic();
		}
		return changedProperties_;
	}

	private FastSet<String> removedProperties_;

	private FastSet<String> getRemovedPropertiesInt() {
		if (removedProperties_ == null) {
			removedProperties_ = new FastSet<String>(Equalities.LEXICAL_CASE_INSENSITIVE).atomic();
		}
		return removedProperties_;
	}

	private FastMap<String, Object> props_;

	private FastMap<String, Object> getProps() {
		if (props_ == null) {
			FastMap<String, Object> localProps = new FastMap<String, Object>(Equalities.LEXICAL_CASE_INSENSITIVE);
			for (String key : getDelegate().keySet()) {
				localProps.put(key, Deferred.INSTANCE);
			}
			props_ = localProps.atomic();
		}
		return props_;
	}

	@Override
	public <T> T getProperty(final String propertyName, final Class<?> T) {
		Object result = null;
		Map<String, Object> props = getProps();
		result = props.get(propertyName);
		if (result == null || Deferred.INSTANCE.equals(result)) {
			try {
				Map<String, Object> delegate = getDelegate();
				if (delegate instanceof Document) {
					Document doc = (Document) delegate;
					result = doc.getItemValue(propertyName, T);
				} else {
					result = T.cast(delegate.get(propertyName));
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
			if (result != null && !T.isAssignableFrom(result.getClass())) {
				try {
					Map<String, Object> delegate = getDelegate();
					if (delegate instanceof Document) {
						Document doc = (Document) delegate;
						result = doc.getItemValue(propertyName, T);
					} else {
						Object chk = delegate.get(propertyName);
						if (chk != null) {
							result = T.cast(delegate.get(propertyName));
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
		return getProperty(key, java.lang.Object.class);
	}

	@Override
	public Set<String> getPropertyKeys() {
		return getProps().keySet().unmodifiable();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setProperty(final String key, final Object value) {
		boolean isEqual = false;
		Map<String, Object> props = getProps();
		if (props != null) {
			if (key != null) {
				Object current = getProperty(key);
				//				if (key.startsWith(DominoVertex.IN_PREFIX) && value instanceof java.util.Collection) {
				//					isEdgeCollection = true;
				//				}
				if (current == null && value == null) {
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
						Object[] vArray = ((java.util.Collection) value).toArray();
						Object[] cArray = ((java.util.Collection) current).toArray();
						isEqual = Arrays.deepEquals(vArray, cArray);
					}
					if (value.getClass().isArray() && current.getClass().isArray()) {
						Object[] vArray = (Object[]) value;
						Object[] cArray = (Object[]) current;
						isEqual = Arrays.deepEquals(vArray, cArray);
					}
				}
				if (isEqual) {
					log_.log(Level.FINE, "Not setting property " + key + " because the new value is equal to the existing value");
				}
				if (value instanceof Serializable) {
					if (current == null || Null.INSTANCE.equals(current)) {
						getParent().startTransaction(this);
						getChangedPropertiesInt().add(key);
					} else if (!isEqual) {
						getParent().startTransaction(this);
						getChangedPropertiesInt().add(key);
					} else {
					}
				} else if (value == null) {
					if (!Null.INSTANCE.equals(current)) {
						getParent().startTransaction(this);
						getChangedPropertiesInt().add(key);
					}
				} else {
					log_.log(Level.FINE, "Attempted to set property " + key + " to a non-serializable value: " + value.getClass().getName());
					if (current == null || Null.INSTANCE.equals(current)) {
						getParent().startTransaction(this);
						getChangedPropertiesInt().add(key);
					} else if (!isEqual) {
						getParent().startTransaction(this);
						getChangedPropertiesInt().add(key);
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
	public <T> T removeProperty(final String key) {
		getParent().startTransaction(this);
		T result = getProperty(key);
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
		return delegateKey_;
	}

	@Override
	public boolean hasProperty(final String key) {
		return getPropertyKeys().contains(key);
	}

	@Override
	public <T> T getProperty(final String key, final Class<?> T, final boolean allowNull) {
		T result = getProperty(key, T);
		if (allowNull) {
			return result;
		} else {
			if (result == null || Null.INSTANCE == result) {
				return TypeUtils.getDefaultInstance(T);
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
		if (!props.isEmpty()) {
			for (String s : getChangedPropertiesInt()) {
				String key = s;
				Object v = props.get(key);
				if (s.startsWith(DominoVertex.IN_PREFIX) || s.startsWith(DominoVertex.OUT_PREFIX)) {
					if (delegate instanceof Document) {
						if (v instanceof NoteList) {
							byte[] bytes = ((NoteList) v).toByteArray();
							((Document) delegate).writeBinary(s, bytes, 2048 * 24);
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
