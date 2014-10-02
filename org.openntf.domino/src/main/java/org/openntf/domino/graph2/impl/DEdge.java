package org.openntf.domino.graph2.impl;

import java.util.Map;
import java.util.logging.Logger;

import org.openntf.domino.graph.IDominoVertex;
import org.openntf.domino.graph2.DGraph;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class DEdge extends DElement implements org.openntf.domino.graph2.DEdge {
	private static final Logger log_ = Logger.getLogger(DEdge.class.getName());
	private transient Vertex in_;
	private String inKey_;
	private transient Vertex out_;
	private String outKey_;
	private String label_;

	public DEdge(final DGraph parent) {
		super(parent);
	}

	DEdge(final org.openntf.domino.graph2.DGraph parent, final Map<String, Object> delegate) {
		super(parent);
		setDelegate(delegate);
	}

	@Override
	public void remove() {
		getParent().removeEdge(this);
	}

	@Override
	public Vertex getVertex(final Direction direction) throws IllegalArgumentException {
		return getParent().getVertex(getVertexId(direction));
	}

	public String getVertexId(final Direction direction) {
		if (direction == Direction.IN) {
			if (inKey_ == null) {
				inKey_ = getProperty(org.openntf.domino.graph2.DEdge.IN_NAME, String.class);
			}
			return inKey_;
		}
		if (direction == Direction.OUT) {
			if (outKey_ == null) {
				outKey_ = getProperty(org.openntf.domino.graph2.DEdge.OUT_NAME, String.class);
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

	void setInDoc(final IDominoVertex in) {
		in.addInEdge(this);
		in_ = in;
		inKey_ = (String) in.getId();
		setProperty(org.openntf.domino.graph2.DEdge.IN_NAME, inKey_);
	}

	void setLabel(final String label) {
		label_ = label;
		setProperty(org.openntf.domino.graph2.DEdge.LABEL_NAME, label);
	}

	void setOutDoc(final IDominoVertex out) {
		out.addOutEdge(this);
		out_ = out;
		outKey_ = (String) out.getId();
		setProperty(org.openntf.domino.graph2.DEdge.OUT_NAME, outKey_);
	}

}
