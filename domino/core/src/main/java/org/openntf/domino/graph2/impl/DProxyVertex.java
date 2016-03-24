package org.openntf.domino.graph2.impl;

import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import javolution.util.FastSet;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.graph.DominoVertex;

public class DProxyVertex extends DVertex {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DProxyVertex.class.getName());
	public static final String PROXY_ITEM = "_ODA_PROXYID";
	protected org.openntf.domino.graph2.DVertex proxyDelegate_;
	protected NoteCoordinate proxyId_;

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

	public org.openntf.domino.graph2.DVertex getProxyDelegate() {
		if (proxyDelegate_ == null) {
			NoteCoordinate id = getProxiedId();
			if (id != null && id.equals(this.getId())) {
				System.out.println("ALERT: Vertex is its own proxy! This could be bad.");
			}
			if (id != null) {
				//				System.out.println("Resolving proxy delegate using id " + id.toString());
				proxyDelegate_ = (org.openntf.domino.graph2.DVertex) getParent().getVertex(id);
				//				if (proxyDelegate_ == null) {
				//					System.err.println("Unable to resolve proxy delegate using id " + String.valueOf(id));
				//				}
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

	protected boolean isGraphKey(final String key) {
		if (key.startsWith(DominoVertex.IN_PREFIX))
			return true;
		if (key.startsWith(DominoVertex.OUT_PREFIX))
			return true;
		if (key.startsWith("_COUNT" + DominoVertex.OUT_PREFIX))
			return true;
		if (key.startsWith("_COUNT" + DominoVertex.IN_PREFIX))
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
			org.openntf.domino.graph2.DVertex delVertex = getProxyDelegate();
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
		} else {
			org.openntf.domino.graph2.DVertex delVertex = getProxyDelegate();
			if (delVertex == null) {
				super.setProperty(key, value);
			} else {
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

}
