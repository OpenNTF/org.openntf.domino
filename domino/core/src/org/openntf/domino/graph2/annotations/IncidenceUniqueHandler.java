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
			System.out.println("DEBUG: Invoking AdjacencyUnique FIND method");
			Edge resultEdge = null;
			Vertex argVertex = ((VertexFrame) arguments[0]).asVertex();
			switch (incidence.direction()) {
			case OUT:
				DVertex outVertex = (DVertex) vertex;
				DVertex inVertex = (DVertex) argVertex;
				resultEdge = outVertex.findInEdge(inVertex, incidence.label());
				if (resultEdge != null) {
					return framedGraph.frame(resultEdge, method.getReturnType());
				}
			case IN:
				inVertex = (DVertex) vertex;
				outVertex = (DVertex) argVertex;
				resultEdge = inVertex.findOutEdge(outVertex, incidence.label());
				if (resultEdge != null) {
					return framedGraph.frame(resultEdge, method.getReturnType());
				}
			case BOTH:
				inVertex = (DVertex) vertex;
				outVertex = (DVertex) argVertex;
				resultEdge = inVertex.findEdge(outVertex, incidence.label());
				if (resultEdge != null) {
					return framedGraph.frame(resultEdge, method.getReturnType());
				}
			}
		} else if (ClassUtilities.isAddMethod(method)) {
			Edge resultEdge = null;
			Vertex argVertex = ((VertexFrame) arguments[0]).asVertex();
			switch (incidence.direction()) {
			case OUT:
				DVertex outVertex = (DVertex) vertex;
				DVertex inVertex = (DVertex) argVertex;
				resultEdge = outVertex.findInEdge(inVertex, incidence.label());
				if (resultEdge != null) {
					return framedGraph.frame(resultEdge, method.getReturnType());
				}
				Edge e1 = framedGraph.addEdge(null, outVertex, inVertex, incidence.label());
				return framedGraph.frame(e1, method.getReturnType());
			case IN:
				inVertex = (DVertex) vertex;
				outVertex = (DVertex) argVertex;
				resultEdge = inVertex.findOutEdge(outVertex, incidence.label());
				if (resultEdge != null) {
					return framedGraph.frame(resultEdge, method.getReturnType());
				}
				Edge e2 = framedGraph.addEdge(null, outVertex, inVertex, incidence.label());
				return framedGraph.frame(e2, method.getReturnType());
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
