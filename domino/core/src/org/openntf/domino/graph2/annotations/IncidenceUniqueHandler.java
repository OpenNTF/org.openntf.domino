package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Method;

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
import com.tinkerpop.frames.structures.FramedEdgeIterable;

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
				return new FramedEdgeIterable(framedGraph, vertex.getEdges(incidence.direction(), incidence.label()),
						incidence.direction(), ClassUtilities.getGenericClass(method));
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
