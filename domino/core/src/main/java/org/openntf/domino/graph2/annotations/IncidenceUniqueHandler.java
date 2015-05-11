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

@SuppressWarnings("deprecation")
public class IncidenceUniqueHandler implements AnnotationHandler<IncidenceUnique> {

	@Override
	public Class<IncidenceUnique> getAnnotationType() {
		return IncidenceUnique.class;
	}

	@SuppressWarnings("rawtypes")
	private Edge addEdge(final IncidenceUnique incidence, final FramedGraph framedGraph, final Vertex vertex, final Vertex newVertex) {
		Edge result = null;
		switch (incidence.direction()) {
		case OUT:
			result = framedGraph.addEdge(null, vertex, newVertex, incidence.label());
			break;
		case IN:
			result = framedGraph.addEdge(null, newVertex, vertex, incidence.label());
			break;
		case BOTH:
			throw new UnsupportedOperationException("Direction.BOTH it not supported on 'add' or 'set' methods");
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private int countEdges(final IncidenceUnique incidence, final Vertex vertex) {
		int result = 0;
		switch (incidence.direction()) {
		case OUT:
			if (vertex instanceof DVertex) {
				result = ((DVertex) vertex).getOutEdgeCount(incidence.label());
			} else {
				Iterable<Edge> it = vertex.getEdges(Direction.OUT, incidence.label());
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
				result = ((DVertex) vertex).getInEdgeCount(incidence.label());
			} else {
				Iterable<Edge> it = vertex.getEdges(Direction.IN, incidence.label());
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
	private Edge findEdge(final IncidenceUnique adjacency, final FramedGraph framedGraph, final Vertex vertex, final Vertex newVertex) {
		Edge result = null;
		switch (adjacency.direction()) {
		case OUT:
			try {
				result = ((DVertex) vertex).findOutEdge(newVertex, adjacency.label());
			} catch (IllegalStateException ise) {
				//NTF this is a legitimate condition, since the edge does not yet exist!
			}
			break;
		case IN:
			try {
				result = ((DVertex) vertex).findInEdge(newVertex, adjacency.label());
			} catch (IllegalStateException ise) {
				//NTF this is a legitimate state, since the edge does not yet exist!
			}
			break;
		case BOTH:
			try {
				result = ((DVertex) vertex).findOutEdge(newVertex, adjacency.label());
			} catch (IllegalStateException ise) {
				//NTF this is a legitimate state, since the edge does not yet exist!
			}
			if (result == null) {
				try {
					result = ((DVertex) vertex).findInEdge(newVertex, adjacency.label());
				} catch (IllegalStateException ise) {
					//NTF this is a legitimate state, since the edge does not yet exist!
				}
			}
			break;
		default:
			break;
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object processElement(final IncidenceUnique annotation, final Method method, final Object[] arguments,
			final FramedGraph framedGraph, final Element element, final Direction direction) {
		if (element instanceof Vertex) {
			return processVertex(annotation, method, arguments, framedGraph, (Vertex) element);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object processVertex(final IncidenceUnique incidence, final Method method, final Object[] arguments,
			final FramedGraph framedGraph, final Vertex vertex) {
		if (ClassUtilities.isGetMethod(method)) {
			Class<?> returnType = method.getReturnType();
			if (Iterable.class.isAssignableFrom(returnType)) {
				return new FramedEdgeList(framedGraph, vertex.getEdges(incidence.direction(), incidence.label()),
						ClassUtilities.getGenericClass(method));
			} else if (Edge.class.isAssignableFrom(returnType)) {
				return vertex.getEdges(incidence.direction(), incidence.label()).iterator().next();
			} else {
				Edge e = vertex.getEdges(incidence.direction(), incidence.label()).iterator().next();
				return framedGraph.frame(e, returnType);
			}
		} else if (AnnotationUtilities.isFindMethod(method)) {
			Vertex newVertex;
			Edge resultEdge = null;
			newVertex = ((VertexFrame) arguments[0]).asVertex();
			resultEdge = findEdge(incidence, framedGraph, vertex, newVertex);
			if (resultEdge != null) {
				return framedGraph.frame(resultEdge, method.getReturnType());
			}
		} else if (AnnotationUtilities.isCountMethod(method)) {
			return countEdges(incidence, vertex);
		} else if (ClassUtilities.isAddMethod(method)) {
			Vertex newVertex;
			Edge resultEdge = null;
			newVertex = ((VertexFrame) arguments[0]).asVertex();
			resultEdge = findEdge(incidence, framedGraph, vertex, newVertex);
			if (resultEdge == null) {
				resultEdge = addEdge(incidence, framedGraph, vertex, newVertex);
			}
			return framedGraph.frame(resultEdge, method.getReturnType());
		} else if (ClassUtilities.isRemoveMethod(method)) {
			framedGraph.removeEdge(((EdgeFrame) arguments[0]).asEdge());
			return null;
		}

		return null;
	}
}
