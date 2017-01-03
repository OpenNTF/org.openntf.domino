package org.openntf.domino.graph2.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.AutoMime;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.RichTextItem;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.big.impl.NoteCoordinate;
//import javolution.util.FastMap;
//import javolution.util.FastSet;
//import javolution.util.function.Equalities;
import org.openntf.domino.big.impl.NoteList;
import org.openntf.domino.big.impl.ViewEntryCoordinate;
import org.openntf.domino.graph2.builtin.Eventable;
import org.openntf.domino.types.Null;
import org.openntf.domino.types.SessionDescendant;
import org.openntf.domino.utils.TypeUtils;

import javolution.util.FastMap;

public abstract class DElement implements org.openntf.domino.graph2.DElement, Serializable, Map<String, Object> {
	private static final Logger log_ = Logger.getLogger(DElement.class.getName());
	private static final long serialVersionUID = 1L;
	public static final String TYPE_FIELD = "_OPEN_GRAPHTYPE";

	public static enum Deferred {
		INSTANCE;
		private static final long serialVersionUID = 1L;
	}

	protected transient org.openntf.domino.graph2.DGraph parent_;
	protected Object delegateKey_;
	protected transient Object framedObject_;
	protected transient Map<String, Object> delegate_;
	protected boolean isRemoved_ = false;

	public DElement(final org.openntf.domino.graph2.DGraph parent) {
		parent_ = parent;
	}

	protected org.openntf.domino.graph2.impl.DGraph getParent() {
		return (org.openntf.domino.graph2.impl.DGraph) parent_;
	}

	protected org.openntf.domino.graph2.DElementStore getStore() {
		return getParent().findElementStore(this);
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
				if (key != null && key.length() > 0) {
					localProps.put(key, Deferred.INSTANCE);
				}
			}
			props_ = Collections.synchronizedMap(localProps);
		}
		return props_;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProperty(final String propertyName, final Class<T> type) {
		//TODO NTF cached properties should be automatically reset if the base Document is known to have changed
		//		if ("form".equalsIgnoreCase(propertyName)) {
		//			System.out.println("Getting form value now...");
		//		}
		Object result = null;
		Map<String, Object> props = getProps();
		result = props.get(propertyName);
		Map<String, Object> delegate = getDelegate();
		if (result == null || Deferred.INSTANCE.equals(result)) {
			try {
				if (delegate instanceof Document) {
					Document doc = (Document) delegate;
					doc.setAutoMime(AutoMime.WRAP_ALL);
					if (doc.hasItem(propertyName)) {
						Item item = doc.getFirstItem(propertyName);
						if (item instanceof RichTextItem && Object.class.equals(type)) {
							result = item;
						} else {
							try {
								result = doc.getItemValue(propertyName, type);
							} catch (Throwable t) {
								//							System.out.println("TEMP DEBUG didn't get property " + propertyName + " with type " + type.getSimpleName()
								//									+ " because of a " + t.getClass().getSimpleName() + ": " + t.getMessage());
							}
						}
					}
					if (result == null || Deferred.INSTANCE.equals(result)) {
						try {
							Object raw = doc.get(propertyName);
							if (raw instanceof Vector) {
								if (((Vector) raw).isEmpty()) {
									props.put(propertyName, Null.INSTANCE);
									return null;
								}
							}
							result = TypeUtils.objectToClass(raw, type, doc.getAncestorSession());
						} catch (Throwable t) {
							if (log_.isLoggable(Level.FINE)) {
								log_.log(Level.FINE, "Invalid property for document " + propertyName, t);
							}
						}
					}
				} else if (delegate instanceof SessionDescendant) {
					Session s = ((SessionDescendant) delegate).getAncestorSession();
					result = TypeUtils.convertToTarget(delegate.get(propertyName), type, s);
				} else if (delegate != null) {
					try {
						result = TypeUtils.convertToTarget(delegate.get(propertyName), type, null);
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
				if (result == null) {
					props.put(propertyName, Null.INSTANCE);
				} else if (result instanceof Serializable) {
					props.put(propertyName, result);
				} else {
					if (log_.isLoggable(Level.FINE)) {
						log_.log(Level.FINE,
								"Got a value from the document but it's not Serializable. It's a " + result.getClass().getName());
					}
					props.put(propertyName, result);
				}
			} catch (Exception e) {
				log_.log(Level.WARNING,
						"Exception occured attempting to get value from document for " + propertyName + " so we cannot return a value", e);
			}
		} else if (result == Null.INSTANCE) {

		} else {
			if (result != null && !type.isAssignableFrom(result.getClass())) {
				//				System.out.println(propertyName + " returned a " + result.getClass().getName() + " when we asked for a " + type.getName());

				try {
					//					Map<String, Object> delegate = getDelegate();
					if (delegate instanceof Document) {
						Document doc = (Document) delegate;
						Item item = doc.getFirstItem(propertyName);
						if (item instanceof RichTextItem && Object.class.equals(type)) {
							result = ((RichTextItem) item).getUnformattedText();
						} else {
							result = doc.getItemValue(propertyName, type);
						}
					} else if (delegate instanceof SessionDescendant) {
						Session s = ((SessionDescendant) delegate).getAncestorSession();
						result = TypeUtils.convertToTarget(delegate.get(propertyName), type, s);
					} else if (delegate != null) {
						Object chk = delegate.get(propertyName);
						if (chk != null) {
							result = TypeUtils.convertToTarget(delegate.get(propertyName), type, null);
						}
					}
					if (result == null) {
						props.put(propertyName, Null.INSTANCE);
					} else if (result instanceof Serializable) {
						props.put(propertyName, result);
					} else {
						log_.log(Level.FINE,
								"Got a value from the document but it's not Serializable. It's a " + result.getClass().getName());
						props.put(propertyName, result);
					}
				} catch (Exception e) {
					log_.log(Level.WARNING,
							"Exception occured attempting to get value from document for " + propertyName
									+ " but we have a value in the cache of type " + result.getClass().getName()
									+ " when we were looking for a " + type.getName(),
							e);
				}
			}
		}
		if (result == Null.INSTANCE) {
			result = null;
		}
		if (delegate instanceof Document && delegate != null) {
			((Document) delegate).closeMIMEEntities();
		}
		//		if ("form".equalsIgnoreCase(propertyName)) {
		//			if (result == null) {
		//				Factory.println("TEMP DEBUG returning null as value for Form field");
		//			}
		//		}
		if (result == Deferred.INSTANCE) {
			//			System.out.println("Returning Deferred INSTANCE for property " + propertyName);
		}
		return (T) result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProperty(final String key) {
		if ("form".equalsIgnoreCase(key)) {
			Object result = getProperty(key, String.class);
			//			System.out.println("Found a " + result.getClass().getName() + " in the form field: '" + (String) result + "'");
			return (T) result;
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
					try {
						current = getProperty(key, value.getClass());
					} catch (Exception e) {
						if (e instanceof ClassCastException) {
							current = null;
						} else {
							throw new RuntimeException(e);
						}
					}
				} else {
					current = getProperty(key);
				}
				if (current == null && value == null) {
					//					System.out.println("TEMP DEBUG: Both current and new value for " + key + " are null");
					return;
				}
				//NTF The standard equality check has a fast out based on array size, so I think it's safe to use here...
				if (key.startsWith(DVertex.IN_PREFIX) || key.startsWith(DVertex.OUT_PREFIX)) {
					isEqual = false;	//NTF ALWAYS set edge collections. Checking them can be expensive
				} else if (value != null && current != null) {
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
					log_.log(Level.FINE,
							"Attempted to set property " + key + " to a non-serializable value: " + value.getClass().getName());
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

	@SuppressWarnings("unchecked")
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

	protected boolean beforeRemove() {
		if (getFramedObject() instanceof Eventable) {
			return ((Eventable) getFramedObject()).delete();
		} else {
			return true;
		}
	}

	protected boolean beforeUpdate() {
		if (getFramedObject() instanceof Eventable) {
			return ((Eventable) getFramedObject()).update();
		} else {
			return true;
		}
	}

	void _remove() {
		isRemoved_ = true;
		getParent().startTransaction(this);
		getParent().removeDelegate(this);
	}

	@Override
	public Object getId() {
		if (delegateKey_ == null) {
			Map<String, Object> delegate = getDelegate();
			if (delegate instanceof Document) {
				delegateKey_ = new NoteCoordinate((Document) delegate);
			} else if (delegate instanceof View) {
				delegateKey_ = new NoteCoordinate((View) delegate);
			} else if (delegate instanceof ViewEntry) {
				delegateKey_ = new ViewEntryCoordinate((ViewEntry) delegate);
			}
		}
		return delegateKey_;
	}

	@Override
	public Class<?> getDelegateType() {
		Class<?> result = null;
		if (delegate_ == null) {
			if (delegateKey_ instanceof ViewEntryCoordinate) {
				result = org.openntf.domino.ViewEntry.class;
			} else if (delegateKey_ instanceof NoteCoordinate) {
				if (((NoteCoordinate) delegateKey_).isView()) {
					result = org.openntf.domino.View.class;
				} else if (((NoteCoordinate) delegateKey_).isIcon()) {
					result = org.openntf.domino.Document.class;
				} else {
					result = org.openntf.domino.Document.class;
				}
			} else {
				result = null;
			}
		} else {
			if (delegate_ instanceof ViewEntry) {
				result = org.openntf.domino.ViewEntry.class;
			} else if (delegate_ instanceof View) {
				result = org.openntf.domino.View.class;
			} else if (delegate_ instanceof Document) {
				if (delegateKey_ instanceof NoteCoordinate) {
					if (((NoteCoordinate) delegateKey_).isView()) {
						result = org.openntf.domino.View.class;
					} else if (((NoteCoordinate) delegateKey_).isIcon()) {
						result = org.openntf.domino.Document.class;
					} else {
						result = org.openntf.domino.Document.class;
					}
				}
			} else {
				result = delegate_.getClass();
			}
		}
		return result;
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
			if (result == null || Null.INSTANCE == result || Deferred.INSTANCE == result) {
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
				delegate_ = (Map<String, Object>) getParent().findDelegate(delegateKey_);
				if (delegateKey_ == null && delegate_ instanceof Document) {
					delegateKey_ = ((Document) delegate_).getMetaversalID();
				}
			}
		} else if (delegate_ instanceof View) {
			try {
				//FIXME: This shouldn't be done this way. .isDead should really know for sure if it is not going to work across threads...
				((View) delegate_).isDefaultView();
			} catch (Throwable t) {
				delegate_ = (Map<String, Object>) getParent().findDelegate(delegateKey_);
			}
		}
		if (delegate_ == null) {
			delegate_ = (Map<String, Object>) getParent().findDelegate(delegateKey_);
		}
		if (delegate_ == null) {
			//			System.err.println("Domino graph element " + getClass().getSimpleName() + " has a null delegate for key " + delegateKey_
			//					+ ". This will not turn out well.");
			//			if (this instanceof DVertex) {
			//				Throwable t = new Throwable();
			//				t.printStackTrace();
			//			}
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

	}

	protected void applyChanges() {
		if (isRemoved_) {
			return;	//NTF there's no point in applying changes to an element that's been removed.
		}
		Map<String, Object> props = getProps();
		Map<String, Object> delegate = getDelegate();
		if (delegate == null) {
			throw new IllegalStateException("Get delegate returned null for id " + getId() + " so we cannot apply changes to it.");
		}
		Set<String> changes = getChangedPropertiesInt();
		if (!props.isEmpty() && !changes.isEmpty()) {
			//			System.out.println("TEMP DEBUG: Writing " + getChangedPropertiesInt().size() + " changed properties for " + getId());
			for (String s : changes) {
				String key = s;
				Object v = props.get(key);
				//				System.out.println("TEMP DEBUG: Writing a " + v.getClass().getSimpleName() + " to " + key);
				if (s != null && v != null) {
					if (s.startsWith(DVertex.IN_PREFIX) || s.startsWith(DVertex.OUT_PREFIX)) {
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
							try {
								delegate.put(s, v);
							} catch (Throwable t) {
								System.err.println("ALERT Failed to write a property of " + s + " to element id " + getId()
										+ " which has a delegate of type " + delegate.getClass().getName() + " due to a "
										+ t.getClass().getSimpleName());
								t.printStackTrace();
							}
						}
					} else {
						try {
							delegate.put(s, v);
						} catch (Throwable t) {
							System.err.println("ALERT Failed to write a property of " + s + " to element id " + getId() + " due to a "
									+ t.getClass().getSimpleName());
							t.printStackTrace();
						}
					}
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
			Document doc = (Document) delegate;
			//			if (!doc.hasItem("form")) {
			//				System.err.println("Graph element being saved without a form value.");
			//			}
			doc.save();
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

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsKey(final Object arg0) {
		return getDelegate().containsKey(arg0);
	}

	@Override
	public boolean containsValue(final Object arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object get(final Object arg0) {
		//NTF this might not work. We're trying to avoid an infinite loop.
		return getDelegate().get(String.valueOf(arg0));
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Set<String> keySet() {
		return getPropertyKeys();
	}

	@Override
	public Object put(final String arg0, final Object arg1) {
		Map delegate = getDelegate();
		if (delegate == null) {
			throw new IllegalStateException("An element of type " + getClass().getSimpleName() + " with id " + getId()
					+ " has no delegate and therefore cannot put new value updates.");
		}
		getDelegate().put(arg0, arg1);
		return arg1;
	}

	@Override
	public void putAll(final Map<? extends String, ? extends Object> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object remove(final Object arg0) {
		Object result = getProperty(String.valueOf(arg0));
		removeProperty(String.valueOf(arg0));
		return result;
	}

	@Override
	public int size() {
		return getPropertyKeys().size();
	}

	@Override
	public Collection<Object> values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Document asDocument() {
		Document result = null;
		Object raw = getDelegate();
		if (raw instanceof Document) {
			result = (Document) raw;
		} else if (raw instanceof DProxyVertex) {
			result = (Document) ((DProxyVertex) raw).getDelegate();
			//			System.out.println("Element has a delegate of a DProxyVertex. It should be the other way around?")
		}
		return result;
	}

	public Object getFramedObject() {
		return framedObject_;
	}

	public void setFramedObject(final Object frame) {
		framedObject_ = frame;
	}

}
