package org.openntf.domino.graph2.impl;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javolution.util.FastSet;

import org.openntf.domino.big.impl.NoteCoordinate;
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
	}

	DProxyVertex(final org.openntf.domino.graph2.DGraph parent, final org.openntf.domino.graph2.DVertex vertex,
			final Map<String, Object> delegate) {
		super(parent, delegate);
		proxyDelegate_ = vertex;
		proxyId_ = (NoteCoordinate) vertex.getId();
		setProperty(PROXY_ITEM, proxyId_.toString());
	}

	DProxyVertex(final org.openntf.domino.graph2.DGraph parent, final Map<String, Object> delegate) {
		super(parent, delegate);
	}

	protected org.openntf.domino.graph2.DVertex getProxyDelegate() {
		if (proxyDelegate_ == null) {
			if (proxyId_ == null) {
				proxyId_ = new NoteCoordinate((String) super.getProperty(PROXY_ITEM));
			}
			proxyDelegate_ = (org.openntf.domino.graph2.DVertex) getParent().getVertex(proxyId_);
		}
		return proxyDelegate_;
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

	@Override
	public Object getProperty(final String key) {
		if (isGraphKey(key)) {
			return super.getProperty(key);
		} else {
			return getProxyDelegate().getProperty(key);
		}
	}

	@Override
	public Set<String> getPropertyKeys() {
		FastSet<String> result = new FastSet<String>();
		result.addAll(super.getPropertyKeys());
		result.addAll(getProxyDelegate().getPropertyKeys());
		return result.unmodifiable();
	}

	@Override
	public void setProperty(final String key, final Object value) {
		if (isGraphKey(key)) {
			super.setProperty(key, value);
		} else {
			getProxyDelegate().setProperty(key, value);
		}
	}

	@Override
	public Object removeProperty(final String key) {
		if (isGraphKey(key)) {
			return super.removeProperty(key);
		} else {
			return getProxyDelegate().removeProperty(key);
		}
	}

}
