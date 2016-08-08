package org.openntf.domino.graph2.impl;

import java.util.Map;
import java.util.logging.Logger;

import org.openntf.domino.graph2.DGraph;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class DEdge extends DElement implements org.openntf.domino.graph2.DEdge {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DEdge.class.getName());
	@SuppressWarnings("unused")
	private transient Vertex in_;
	protected Object inKey_;
	@SuppressWarnings("unused")
	private transient Vertex out_;
	protected Object outKey_;
	private String label_;

	public DEdge(final DGraph parent) {
		super(parent);
	}

	DEdge(final org.openntf.domino.graph2.DGraph parent, final Map<String, Object> delegate) {
		super(parent);
		setDelegate(delegate);
	}

	@Override
	protected void applyChanges() {
		if (beforeUpdate()) {
			Object inId = getVertexId(Direction.IN);
			Object outId = getVertexId(Direction.OUT);
			if (inId.equals(outId)) {
				throw new IllegalStateException("Edge cannot have the same vertex for both in and out directions.");
			}
			super.applyChanges();
		}
	}

	@Override
	public void remove() {
		if (beforeRemove()) {
			getParent().removeEdge(this);
		}
	}

	@Override
	public Vertex getVertex(final Direction direction) throws IllegalArgumentException {
		return getParent().getVertex(getVertexId(direction));
	}

	public Object getVertexId(final Direction direction) {
		if (direction == Direction.IN) {
			if (inKey_ == null) {
				String key = getProperty(org.openntf.domino.graph2.DEdge.IN_NAME, String.class);
				if (key == null) {
					throw new IllegalStateException("In key is null in edge " + getId());
				}
				inKey_ = new org.openntf.domino.big.impl.NoteCoordinate(key);
			}
			return inKey_;
		}
		if (direction == Direction.OUT) {
			if (outKey_ == null) {
				String key = getProperty(org.openntf.domino.graph2.DEdge.OUT_NAME, String.class);
				if (key == null) {
					throw new IllegalStateException("Out key is null in edge " + getId());
				}
				outKey_ = new org.openntf.domino.big.impl.NoteCoordinate(key);
			}
			return outKey_;
		}
		return null;
	}

	@Override
	public String getLabel() {
		if (label_ == null) {
			label_ = getProperty(org.openntf.domino.graph2.DEdge.LABEL_NAME, String.class);
		}
		return label_;
	}

	@Override
	public Object getOtherVertexId(final Vertex vertex) {
		if (vertex.getId().equals(getVertexId(Direction.IN))) {
			return getVertexId(Direction.OUT);
		} else {
			return getVertexId(Direction.IN);
		}
	}

	@Override
	public Vertex getOtherVertex(final Vertex vertex) {
		if (vertex.getId().equals(getVertexId(Direction.IN))) {
			return getVertex(Direction.OUT);
		} else {
			return getVertex(Direction.IN);
		}
	}

	@Override
	public Object getOtherVertexProperty(final Vertex vertex, final String property) {
		Vertex other = getOtherVertex(vertex);
		return other.getProperty(property);
	}

	void setInVertex(final Vertex in) {
		((DVertex) in).addInEdge(this);
		in_ = in;
		inKey_ = in.getId();
		setProperty(org.openntf.domino.graph2.DEdge.IN_NAME, inKey_.toString());
	}

	void setInId(final Object id) {
		inKey_ = id;
		setProperty(org.openntf.domino.graph2.DEdge.IN_NAME, inKey_.toString());
	}

	void setLabel(final String label) {
		label_ = label;
		setProperty(org.openntf.domino.graph2.DEdge.LABEL_NAME, label);
	}

	void setOutVertex(final Vertex out) {
		((DVertex) out).addOutEdge(this);
		out_ = out;
		outKey_ = out.getId();
		setProperty(org.openntf.domino.graph2.DEdge.OUT_NAME, outKey_.toString());
	}

	void setOutId(final Object id) {
		outKey_ = id;
		setProperty(org.openntf.domino.graph2.DEdge.IN_NAME, outKey_.toString());
	}

}
