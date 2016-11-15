package org.openntf.domino.graph2.impl;

import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.graph2.DElementStore.CustomProxyResolver;

import javolution.util.FastSet;

public class DProxyVertex extends DVertex {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DProxyVertex.class.getName());
	public static final String PROXY_ITEM = "_ODA_PROXYID";
	protected org.openntf.domino.graph2.DVertex proxyDelegate_;
	protected NoteCoordinate proxyId_;
	protected boolean proxyResolved_ = false;

	public DProxyVertex(final org.openntf.domino.graph2.DGraph parent) {
		super(parent);
		//		System.out.println("New DProxyVertex was created with option 0. Here's a stack trace to see why...");
		//		new Throwable().printStackTrace();
	}

	DProxyVertex(final org.openntf.domino.graph2.DGraph parent, final org.openntf.domino.graph2.DVertex vertex,
			final Map<String, Object> delegate) {
		super(parent, delegate);
		proxyDelegate_ = vertex;
		proxyId_ = (NoteCoordinate) vertex.getId();
		super.setProperty(PROXY_ITEM, proxyId_.toString());
		//		System.out.println("New DProxyVertex was created with option 1. Here's a stack trace to see why...");
		//		new Throwable().printStackTrace();
	}

	DProxyVertex(final org.openntf.domino.graph2.DGraph parent, final NoteCoordinate vertexId, final Map<String, Object> delegate) {
		super(parent, delegate);
		//		proxyDelegate_ = vertex;
		proxyId_ = vertexId;
		super.setProperty(PROXY_ITEM, proxyId_.toString());
		//		System.out.println("New DProxyVertex was created with option 2. Here's a stack trace to see why...");
		//		new Throwable().printStackTrace();
	}

	DProxyVertex(final org.openntf.domino.graph2.DGraph parent, final Map<String, Object> delegate) {
		super(parent, delegate);
		//		System.out.println("New DProxyVertex was created with option 3. Here's a stack trace to see why...");
		//		new Throwable().printStackTrace();
	}

	@Override
	public void applyChanges() {
		org.openntf.domino.graph2.DVertex pd = getProxyDelegate();
		if (pd != null) {
			((DVertex) pd).applyChanges();
		}
		super.applyChanges();
	}

	public org.openntf.domino.graph2.DVertex getProxyDelegate() {
		if (proxyDelegate_ == null) {
			NoteCoordinate id = getProxiedId();
			if (id != null && id.equals(this.getId())) {
				System.out.println("ALERT: Vertex is its own proxy! This could be bad.");
			}
			if (id != null) {
				//				System.out.println("TEMP DEBUG getting proxy delegate with id " + String.valueOf(id) + " with key " + originalKey);
				proxyDelegate_ = (org.openntf.domino.graph2.DVertex) getParent().getVertex(id);
				if (!proxyResolved_) {
					if (proxyDelegate_ == null || proxyDelegate_.getPropertyKeys().isEmpty()) {
						//					System.out.println("TEMP DEBUG proxy delegate is empty");
						String originalKey = super.getProperty("$$Key", String.class);
						proxyResolved_ = true;
						org.openntf.domino.graph2.DElementStore es = this.getParent().findElementStore(id);
						CustomProxyResolver resolver = es.getCustomProxyResolver();
						if (resolver != null) {
							//							System.out.println("TEMP DEBUG attempting to re-acquire proxy delegate from original key " + originalKey);
							Map<String, Object> originalDelegate = resolver.getOriginalDelegate(originalKey);
							if (originalDelegate != null && originalDelegate instanceof Document) {
								String pid = ((Document) originalDelegate).getMetaversalID();
								NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(pid);
								setProxiedId(nc);
								applyChanges();
								proxyDelegate_ = (org.openntf.domino.graph2.DVertex) getParent().getVertex(nc);
							} else {
								//								System.out.println("TEMP DEBUG Original delegate is a "
								//										+ (originalDelegate == null ? "null" : originalDelegate.getClass().getName()));
							}
						} else {
							//							System.out.println("TEMP DEBUG No resolver available");
						}
					} /*else {
						Set<String> keys = proxyDelegate_.getPropertyKeys();
						Joiner joiner = Joiner.on(",");
						StringBuilder sb = new StringBuilder();
						joiner.appendTo(sb, keys);
						System.out.println("TEMP DEBUG proxy delegate is a " + proxyDelegate_.getClass().getName() + ": " + sb.toString());
						}*/
				}
			}
		}
		return proxyDelegate_;
	}

	protected NoteCoordinate getProxiedId() {
		if (proxyId_ == null) {
			Object raw = super.getProperty(PROXY_ITEM);
			if (raw instanceof Vector) {
				if (((Vector) raw).isEmpty())
					return null;
			}
			String storedId = super.getProperty(PROXY_ITEM, String.class);
			if (storedId == null || storedId.length() != 48)
				return null;
			proxyId_ = NoteCoordinate.Utils.getNoteCoordinate(storedId);
		}
		return proxyId_;
	}

	protected void setProxiedId(final NoteCoordinate id) {
		try {
			//			System.out.println("Setting proxied id to " + String.valueOf(id));
			proxyId_ = id;
			setProperty(PROXY_ITEM, String.valueOf(id));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected static boolean isGraphKey(final String key) {
		if (key.startsWith("_ODA_"))
			return true;
		if (key.startsWith(DVertex.IN_PREFIX))
			return true;
		if (key.startsWith(DVertex.OUT_PREFIX))
			return true;
		if (key.startsWith("_COUNT" + DVertex.IN_PREFIX))
			return true;
		if (key.startsWith("_COUNT" + DVertex.OUT_PREFIX))
			return true;
		if (key.equals(DElement.TYPE_FIELD))
			return true;
		if (key.equals(PROXY_ITEM))
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getProperty(final String key) {
		//		System.out.println("TEMP DEBUG: getting property from proxy vertex");
		if (isGraphKey(key)) {
			return super.getProperty(key);
		} else if ("form".equalsIgnoreCase(key)) {
			//			org.openntf.domino.graph2.DVertex delVertex = getProxyDelegate();
			Object localChk = super.getProperty(key);
			if (localChk == null || String.valueOf(localChk).length() == 0) {
				return null;
			} else {
				return localChk;
			}
		} else {
			org.openntf.domino.graph2.DVertex delVertex = getProxyDelegate();
			if (delVertex == null) {
				//				System.out.println("TEMP DEBUG: No delegate available. Proxy acting directly for key " + key);
				return super.getProperty(key);
			} else {
				//				System.out.println("TEMP DEBUG: Proxy found. Retrieving property " + key);
				if (delVertex.getId().equals(this.getId())) {
					return super.getProperty(key);
				}
				return delVertex.getProperty(key);
			}
		}
	}

	@Override
	public <T> T getProperty(final String propertyName, final Class<T> type) {
		//		System.out.println("TEMP DEBUG: getting property from proxy vertex");
		if (isGraphKey(propertyName)) {
			return super.getProperty(propertyName, type);
		} else if ("form".equalsIgnoreCase(propertyName)) {
			org.openntf.domino.graph2.DVertex delVertex = getProxyDelegate();
			Object localChk = super.getProperty(propertyName, type);
			if (localChk == null || String.valueOf(localChk).length() == 0) {
				return null;
			} else {
				return (T) localChk;
			}
		} else {
			org.openntf.domino.graph2.DVertex delVertex = getProxyDelegate();
			if (delVertex == null) {
				//				System.out.println("TEMP DEBUG: No delegate available. Proxy acting directly for key " + propertyName);
				return super.getProperty(propertyName, type);
			} else {
				//				System.out.println("TEMP DEBUG: Proxy found. Retrieving property " + propertyName);
				return delVertex.getProperty(propertyName, type);
			}
		}
	}

	@Override
	public <T> T getProperty(final String key, final Class<T> type, final boolean allowNull) {
		//		System.out.println("TEMP DEBUG: getting property from proxy vertex");
		if (isGraphKey(key)) {
			return super.getProperty(key, type, allowNull);
		} else {
			org.openntf.domino.graph2.DVertex delVertex = getProxyDelegate();
			if (delVertex == null) {
				//				System.out.println("TEMP DEBUG: No delegate available. Proxy acting directly for key " + key);
				return super.getProperty(key, type, allowNull);
			} else {
				//				System.out.println("TEMP DEBUG: Proxy found. Retrieving property " + key);
				return delVertex.getProperty(key, type, allowNull);
			}
		}
	}

	@Override
	public Set<String> getPropertyKeys() {
		FastSet<String> result = new FastSet<String>();
		result.addAll(super.getPropertyKeys());
		org.openntf.domino.graph2.DVertex delVertex = getProxyDelegate();
		if (delVertex != null) {
			result.addAll(delVertex.getPropertyKeys());
		}
		return result.unmodifiable();
	}

	@Override
	public void setProperty(final String key, final Object value) {
		if (isGraphKey(key)) {
			super.setProperty(key, value);
		} else if ("form".equalsIgnoreCase(key)) {
			super.setProperty(key, value);
		} else if (PROXY_ITEM.equalsIgnoreCase(key)) {
			super.setProperty(key, value);
		} else {
			org.openntf.domino.graph2.DVertex delVertex = getProxyDelegate();
			if (delVertex == null) {
				super.setProperty(key, value);
			} else {
				System.out.println("TEMP DEBUG setting a proxy delegate for property " + key + " in a " + delVertex.getProperty("form"));
				delVertex.setProperty(key, value);
			}
		}
	}

	@Override
	public Object removeProperty(final String key) {
		if (isGraphKey(key)) {
			return super.removeProperty(key);
		} else {
			org.openntf.domino.graph2.DVertex delVertex = getProxyDelegate();
			if (delVertex == null) {
				return super.removeProperty(key);
			} else {
				return delVertex.removeProperty(key);
			}
		}
	}

	//	@Override
	//	public Document asDocument() {
	//		Document result = null;
	//		Object raw = getProxyDelegate();
	//		if (raw instanceof Document) {
	//			result = (Document) raw;
	//		}
	//		return result;
	//	}

}
