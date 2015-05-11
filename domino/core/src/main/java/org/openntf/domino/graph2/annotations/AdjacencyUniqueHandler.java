package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Method;
import java.util.Collection;

import org.openntf.domino.graph2.DVertex;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.ClassUtilities;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.annotations.AnnotationHandler;
import com.tinkerpop.frames.structures.FramedVertexIterable;

@SuppressWarnings("deprecation")
public class AdjacencyUniqueHandler implements AnnotationHandler<AdjacencyUnique> {

	@Override
	public Class<AdjacencyUnique> getAnnotationType() {
		return AdjacencyUnique.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object processElement(final AdjacencyUnique annotation, final Method method, final Object[] arguments,
			final FramedGraph framedGraph, final Element element, final Direction direction) {
		if (element instanceof Vertex) {
			return processVertex(annotation, method, arguments, framedGraph, (Vertex) element);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object processVertex(final AdjacencyUnique adjacency, final Method method, final Object[] arguments,
			final FramedGraph framedGraph, final Vertex vertex) {
		Edge resultEdge = null;
		Class<?> returnType = method.getReturnType();

		if (ClassUtilities.isGetMethod(method)) {
			final FramedVertexIterable r = new FramedVertexIterable(framedGraph, vertex.getVertices(adjacency.direction(),
					adjacency.label()), ClassUtilities.getGenericClass(method));
			if (ClassUtilities.returnsIterable(method)) {
				return r;
			} else {
				return r.iterator().hasNext() ? r.iterator().next() : null;
			}
		} else if (ClassUtilities.isAddMethod(method)) {
			Vertex newVertex;
			Object returnValue = null;
			if (arguments == null) {
				// Use this method to get the vertex so that the vertex
				// initializer is called.
				returnValue = framedGraph.addVertex(null, returnType);
				newVertex = ((VertexFrame) returnValue).asVertex();
			} else {
				newVertex = ((VertexFrame) arguments[0]).asVertex();
			}
			resultEdge = findEdge(adjacency, framedGraph, vertex, newVertex);
			if (resultEdge == null) {
				resultEdge = addEdge(adjacency, framedGraph, vertex, newVertex);
			}
			if (returnType.isPrimitive()) {
				return null;
			} else if (Edge.class.isAssignableFrom(returnType)) {
				return resultEdge;
			} else if (EdgeFrame.class.isAssignableFrom(returnType)) {
				return framedGraph.frame(resultEdge, returnType);
			} else {
				return returnValue;
			}
		} else if (ClassUtilities.isRemoveMethod(method)) {
			removeEdges(adjacency.direction(), adjacency.label(), vertex, ((VertexFrame) arguments[0]).asVertex(), framedGraph);
			return null;
		} else if (AnnotationUtilities.isCountMethod(method)) {
			return countEdges(adjacency, vertex);
		} else if (ClassUtilities.isSetMethod(method)) {
			removeEdges(adjacency.direction(), adjacency.label(), vertex, null, framedGraph);
			if (ClassUtilities.acceptsIterable(method)) {
				for (Object o : (Iterable) arguments[0]) {
					Vertex v = ((VertexFrame) o).asVertex();
					addEdge(adjacency, framedGraph, vertex, v);
				}
				return null;
			} else {
				if (null != arguments[0]) {
					Vertex newVertex = ((VertexFrame) arguments[0]).asVertex();
					addEdge(adjacency, framedGraph, vertex, newVertex);
				}
				return null;
			}
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	private Edge findEdge(final AdjacencyUnique adjacency, final FramedGraph framedGraph, final Vertex vertex, final Vertex newVertex) {
		Edge result = null;
		switch (adjacency.direction()) {
		case OUT:
			result = ((DVertex) vertex).findOutEdge(newVertex, adjacency.label());
			break;
		case IN:
			result = ((DVertex) vertex).findInEdge(newVertex, adjacency.label());
			break;
		default:
			break;
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private int countEdges(final AdjacencyUnique adjacency, final Vertex vertex) {
		int result = 0;
		switch (adjacency.direction()) {
		case OUT:
			if (vertex instanceof DVertex) {
				result = ((DVertex) vertex).getOutEdgeCount(adjacency.label());
			} else {
				Iterable<Edge> it = vertex.getEdges(Direction.OUT, adjacency.label());
				if (it instanceof Collection) {
					result = ((Collection) it).size();
				} else {
					for (Edge e : it) {
						result++;
					}
				}
			}
			break;
		case IN:
			if (vertex instanceof DVertex) {
				result = ((DVertex) vertex).getInEdgeCount(adjacency.label());
			} else {
				Iterable<Edge> it = vertex.getEdges(Direction.IN, adjacency.label());
				if (it instanceof Collection) {
					result = ((Collection) it).size();
				} else {
					for (Edge e : it) {
						result++;
					}
				}
			}
			break;
		case BOTH:
			throw new UnsupportedOperationException("Direction.BOTH it not supported on 'add' or 'set' methods");
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private Edge addEdge(final AdjacencyUnique adjacency, final FramedGraph framedGraph, final Vertex vertex, final Vertex newVertex) {
		Edge result = null;
		switch (adjacency.direction()) {
		case OUT:
			//			Iterable<Edge> outedges = vertex.getEdges(Direction.OUT, adjacency.label());	//FIXME NTF Correct direction?
			//			for (Edge edge : outedges) {
			//				Vertex v = edge.getVertex(Direction.IN);
			//				if (v.getId().equals(newVertex.getId())) {
			//					result = edge;
			//					break;
			//				}
			//			}
			//			if (result == null)
			result = framedGraph.addEdge(null, vertex, newVertex, adjacency.label());
			break;
		case IN:
			//			Iterable<Edge> inedges = vertex.getEdges(Direction.IN, adjacency.label());	//FIXME NTF Correct direction?
			//			for (Edge edge : inedges) {
			//				Vertex v = edge.getVertex(Direction.OUT);
			//				if (v.getId().equals(newVertex.getId())) {
			//					result = edge;
			//					break;
			//				}
			//			}
			//			if (result == null)
			result = framedGraph.addEdge(null, newVertex, vertex, adjacency.label());
			break;
		case BOTH:
			throw new UnsupportedOperationException("Direction.BOTH it not supported on 'add' or 'set' methods");
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private void removeEdges(final Direction direction, final String label, final Vertex element, final Vertex otherVertex,
			final FramedGraph framedGraph) {
		for (final Edge edge : element.getEdges(direction, label)) {
			if (null == otherVertex || edge.getVertex(direction.opposite()).equals(otherVertex)) {
				framedGraph.removeEdge(edge);
			}
		}
	}
}
