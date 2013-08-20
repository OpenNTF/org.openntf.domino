/**
 * 
 */
package org.openntf.domino.graph;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Logger;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author nfreeman
 * 
 */
public class AbstractEdgeHelper implements IEdgeHelper {
	private static final Logger log_ = Logger.getLogger(AbstractEdgeHelper.class.getName());

	public static class EdgeHelperException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public EdgeHelperException(final String message) {

		}
	}

	private final String label_;
	private final Class<? extends Vertex> inType_;
	private final Class<? extends Vertex> outType_;
	private final boolean sameTypes_;
	private final boolean unique_;
	private final DominoGraph parent_;

	public AbstractEdgeHelper(final DominoGraph parent, final String label, final Class<? extends Vertex> inType,
			final Class<? extends Vertex> outType, final boolean unique) {
		label_ = label;
		inType_ = inType;
		outType_ = outType;
		unique_ = unique;
		sameTypes_ = inType_.equals(outType_);
		parent_ = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.graph.IEdgeHelper#getLabel()
	 */
	@Override
	public String getLabel() {
		return label_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.graph.IEdgeHelper#getInType()
	 */
	@Override
	public Class<? extends Vertex> getInType() {
		return inType_;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.graph.IEdgeHelper#getOutType()
	 */
	@Override
	public Class<? extends Vertex> getOutType() {
		return outType_;
	}

	public boolean isUnique() {
		return unique_;
	}

	public boolean isSameTypes() {
		return sameTypes_;
	}

	public Class<? extends Vertex> getOtherType(final Class<? extends Vertex> type) {
		if (getInType().equals(type))
			return getOutType();
		if (getOutType().equals(type))
			return getInType();
		throw new EdgeHelperException(type.getName() + " is not a participating type in edge " + getLabel());
	}

	public Class<? extends Vertex> getOtherType(final Vertex vertex) {
		return getOtherType(vertex.getClass());
	}

	public Set<? extends Edge> getEdges(final Vertex vertex) {
		if (getInType().equals(vertex.getClass())) {
			// System.out.println("Request from " + getLabel() + " helper: Requesting IN edges because vertex " +
			// vertex.getClass().getName()
			// + " is IN.");
			return Collections.unmodifiableSet((Set<Edge>) vertex.getEdges(Direction.IN, getLabel()));
		}
		if (getOutType().equals(vertex.getClass())) {
			// System.out.println("Request from " + getLabel() + " helper: Requesting OUT edges because vertex " +
			// vertex.getClass().getName()
			// + " is OUT.");
			return Collections.unmodifiableSet((Set<Edge>) vertex.getEdges(Direction.OUT, getLabel()));
		}
		throw new EdgeHelperException(vertex.getClass().getName() + " is not a participating type in edge " + getLabel());
	}

	public Set<? extends Edge> getFilteredEdges(final Vertex vertex, final Map<String, Object> filterMap) {
		Set<Edge> result = new LinkedHashSet<Edge>();
		Set<? extends Edge> rawset = getEdges(vertex);
		for (Edge edge : rawset) {
			for (String key : filterMap.keySet()) {
				Object value = edge.getProperty(key);
				if (value != null) {
					if (value.equals(filterMap.get(key))) {
						result.add(edge);
					}
				}
			}
		}
		return Collections.unmodifiableSet(result);
	}

	public SortedSet<? extends Edge> getSortedEdges(final Vertex vertex, final String... sortproperties) {
		try {
			Set<? extends Edge> rawSet = getEdges(vertex);
			return Collections.unmodifiableSortedSet(DominoGraph.sortEdges(rawSet, sortproperties));
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public Set<? extends Vertex> getOtherVertexes(final Vertex vertex) {
		Set<Vertex> result = new LinkedHashSet<Vertex>();
		Direction od = Direction.IN;
		if (getInType().equals(vertex.getClass()))
			od = Direction.OUT;

		// System.out.println("Request from " + getLabel() + " helper: Getting opposite of " + vertex.getClass().getName()
		// + " with direction " + od.toString() + " (OUT: " + getOutType().getName() + ", IN: " + getInType().getName() + ")");
		Set<? extends Edge> edges = getEdges(vertex);
		// System.out.println("Request from " + getLabel() + " helper: Got " + edges.size() + " edges.");
		for (Edge edge : edges) {
			result.add(edge.getVertex(od));
		}
		return Collections.unmodifiableSet(result);
	}

	public Vertex getOtherVertex(final Edge edge, final Vertex vertex) {
		if (edge instanceof IDominoEdge) {
			return ((IDominoEdge) edge).getOtherVertex(vertex);
		} else {
			if (vertex.getId().equals(edge.getVertex(Direction.IN).getId())) {
				return edge.getVertex(Direction.OUT);
			} else {
				return edge.getVertex(Direction.IN);
			}
		}
	}

	public Set<? extends Vertex> getSortedOtherVertexes(final Vertex vertex, final String... sortproperties) {
		try {
			Set<? extends Vertex> rawSet = getOtherVertexes(vertex);
			return Collections.unmodifiableSortedSet(DominoGraph.sortVertexes(rawSet, sortproperties));
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public Set<Vertex> getOtherVertexesByEdge(final Vertex vertex, final String... sortproperties) {
		Set<Vertex> result = new LinkedHashSet<Vertex>();
		Direction od = Direction.IN;
		if (getInType().equals(vertex.getClass()))
			od = Direction.OUT;
		for (Edge edge : getSortedEdges(vertex, sortproperties)) {
			result.add(edge.getVertex(od));
		}
		return Collections.unmodifiableSet(result);
	}

	public Edge makeEdge(final Vertex defaultOut, final Vertex defaultIn) {
		Edge result = null;
		Vertex inVert = defaultIn;
		Vertex outVert = defaultOut;
		if (defaultOut == null || defaultIn == null)
			throw new EdgeHelperException("Cannot create edges with null vertex");
		if (defaultOut.getClass().equals(defaultIn.getClass()) && isSameTypes()) {

		} else {
			if (getInType().isAssignableFrom(defaultOut.getClass())/* .equals(defaultOut.getClass()) */) {
				inVert = defaultOut;
			} else if (getOutType().isAssignableFrom(defaultOut.getClass())) {
				outVert = defaultOut;
			} else {
				// System.out.println("Cannot create an edge of type " + getLabel() + " with a vertex of type "
				// + defaultOut.getClass().getName());
				throw new EdgeHelperException("Cannot create an edge of type " + getLabel() + " with a vertex of type "
						+ defaultOut.getClass().getName());
			}
			if (getInType().isAssignableFrom(defaultIn.getClass())) {
				inVert = defaultIn;
			} else if (getOutType().isAssignableFrom(defaultIn.getClass())) {
				outVert = defaultIn;
			} else {
				// System.out.println("Cannot create an edge of type " + getLabel() + " with a vertex of type "
				// + defaultIn.getClass().getName());
				throw new EdgeHelperException("Cannot create an edge of type " + getLabel() + " with a vertex of type "
						+ defaultIn.getClass().getName());
			}
		}
		if (isUnique()) {
			result = parent_.getOrAddEdge(null, outVert, inVert, getLabel());
		} else {
			result = parent_.addEdge(null, outVert, inVert, getLabel());
		}
		return result;
	}

	public Edge findEdge(final Vertex defaultOut, final Vertex defaultIn) {
		Edge result = null;
		Vertex inVert = defaultIn;
		Vertex outVert = defaultOut;
		if (defaultOut == null || defaultIn == null)
			throw new EdgeHelperException("Cannot find edges with null vertex");
		if (defaultOut.getClass().equals(defaultIn.getClass()) && isSameTypes()) {

		} else {
			if (getInType().equals(defaultOut.getClass())) {
				inVert = defaultOut;
			} else if (getOutType().equals(defaultOut.getClass())) {
				outVert = defaultOut;
			} else {
				throw new EdgeHelperException("Cannot find an edge of type " + getLabel() + " with a vertex of type "
						+ defaultOut.getClass().getName());
			}
			if (getInType().equals(defaultIn.getClass())) {
				inVert = defaultIn;
			} else if (getOutType().equals(defaultIn.getClass())) {
				outVert = defaultIn;
			} else {
				throw new EdgeHelperException("Cannot find an edge of type " + getLabel() + " with a vertex of type "
						+ defaultIn.getClass().getName());
			}
		}
		result = parent_.getEdge(outVert, inVert, getLabel());
		return result;
	}
}
