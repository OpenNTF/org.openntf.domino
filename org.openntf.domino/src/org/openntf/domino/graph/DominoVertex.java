package org.openntf.domino.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.util.DefaultVertexQuery;
import com.tinkerpop.blueprints.util.MultiIterable;
import com.tinkerpop.blueprints.util.VerticesFromEdgesIterable;

public class DominoVertex extends DominoElement implements Vertex {
	public static final String GRAPH_TYPE_VALUE = "DVertex";
	public static final String IN_NAME = "_RPD_IN";
	public static final String OUT_NAME = "_RPD_OUT";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient boolean inDirty_ = false;
	private Set<String> inEdges_;
	private transient boolean outDirty_ = false;
	private Set<String> outEdges_;

	public DominoVertex(DominoGraph parent, org.openntf.domino.Document doc) {
		super(parent, doc);
	}

	@Override
	public Edge addEdge(String label, Vertex vertex) {
		return parent_.addEdge(null, this, vertex, label);
	}

	void addInEdge(Edge edge) {
		getInEdges().add((String) edge.getId());
		inDirty_ = true;
	}

	void addOutEdge(Edge edge) {
		getOutEdges().add((String) edge.getId());
		outDirty_ = true;
	}

	public java.util.Set<String> getBothEdges() {
		Set<String> result = getInEdges();
		result.addAll(getOutEdges());
		return result;
	}

	@Override
	public Iterable<Edge> getEdges(Direction direction, String... labels) {
		if (direction == Direction.IN) {
			return getParent().getEdgesFromIds(getInEdges(), labels);
		} else if (direction == Direction.OUT) {
			return getParent().getEdgesFromIds(getOutEdges(), labels);
		} else {
			return getParent().getEdgesFromIds(getBothEdges(), labels);
		}
	}

	@SuppressWarnings("unchecked")
	public Set<String> getInEdges() {
		if (inEdges_ == null) {
			Object o = getProperty(DominoVertex.IN_NAME, java.util.Collection.class);
			if (o != null && o instanceof java.util.Collection) {
				inEdges_ = new HashSet<String>((Collection<String>) o);
			} else {
				inEdges_ = new HashSet<String>();
			}
			inDirty_ = false;
		}
		return inEdges_;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getOutEdges() {
		if (outEdges_ == null) {
			Object o = getProperty(DominoVertex.OUT_NAME, java.util.Collection.class);
			if (o != null && o instanceof java.util.Collection) {
				outEdges_ = new HashSet<String>((Collection<String>) o);
			} else {
				outEdges_ = new HashSet<String>();
			}
			outDirty_ = false;
		}
		return outEdges_;
	}

	@Override
	public Iterable<Vertex> getVertices(Direction direction, String... labels) {
		if (direction == Direction.BOTH) {
			List<Iterable<Vertex>> list = new ArrayList<Iterable<Vertex>>();
			list.add(new VerticesFromEdgesIterable(this, Direction.IN, labels));
			list.add(new VerticesFromEdgesIterable(this, Direction.OUT, labels));
			return new MultiIterable<Vertex>(list);
		} else {
			return new VerticesFromEdgesIterable(this, direction, labels);
		}
	}

	@Override
	public VertexQuery query() {
		return new DefaultVertexQuery(this);
	}

	public void removeEdge(Edge edge) {
		getInEdges().remove(edge.getId());
		inDirty_ = true;
		getOutEdges().remove(edge.getId());
		outDirty_ = true;
	}

	@Override
	public void save() {
		if (inDirty_) {
			setProperty(DominoVertex.IN_NAME, inEdges_);
			inDirty_ = false;
		}
		if (outDirty_) {
			setProperty(DominoVertex.OUT_NAME, outEdges_);
			outDirty_ = false;
		}
		super.save();
	}

}
