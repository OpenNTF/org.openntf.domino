package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Method;

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

public class IncidenceUniqueHandler implements AnnotationHandler<IncidenceUnique> {

	@Override
	public Class<IncidenceUnique> getAnnotationType() {
		return IncidenceUnique.class;
	}

	@Override
	public Object processElement(final IncidenceUnique annotation, final Method method, final Object[] arguments,
			final FramedGraph framedGraph, final Element element, final Direction direction) {
		if (element instanceof Vertex) {
			return processVertex(annotation, method, arguments, framedGraph, (Vertex) element);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public Object processVertex(final IncidenceUnique incidence, final Method method, final Object[] arguments,
			final FramedGraph framedGraph, final Vertex vertex) {
		if (ClassUtilities.isGetMethod(method)) {
			return new FramedEdgeIterable(framedGraph, vertex.getEdges(incidence.direction(), incidence.label()), incidence.direction(),
					ClassUtilities.getGenericClass(method));
		} else if (ClassUtilities.isAddMethod(method)) {
			boolean exists = false;

			switch (incidence.direction()) {
			case OUT:
				Vertex outVertex = vertex;
				Vertex inVertex = ((VertexFrame) arguments[0]).asVertex();
				Iterable<Edge> outedges = outVertex.getEdges(Direction.OUT, incidence.label());	//FIXME NTF Correct direction?
				for (Edge edge : outedges) {
					Vertex v = edge.getVertex(Direction.IN);
					if (v.getId().equals(inVertex.getId())) {
						return framedGraph.frame(edge, method.getReturnType());
					}
				}
				return framedGraph.addEdge(null, outVertex, inVertex, incidence.label());
			case IN:
				inVertex = vertex;
				outVertex = ((VertexFrame) arguments[0]).asVertex();
				Iterable<Edge> inedges = outVertex.getEdges(Direction.OUT, incidence.label());	//FIXME NTF Correct direction?
				for (Edge edge : inedges) {
					Vertex v = edge.getVertex(Direction.IN);
					if (v.getId().equals(inVertex.getId())) {
						return framedGraph.frame(edge, method.getReturnType());
					}
				}
				return framedGraph.addEdge(null, outVertex, inVertex, incidence.label());
			case BOTH:
				throw new UnsupportedOperationException("Direction.BOTH it not supported on 'add' or 'set' methods");
			}

		} else if (ClassUtilities.isRemoveMethod(method)) {
			framedGraph.removeEdge(((EdgeFrame) arguments[0]).asEdge());
			return null;
		}

		return null;
	}
}
