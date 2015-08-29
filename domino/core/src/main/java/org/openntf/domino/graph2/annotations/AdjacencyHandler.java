package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Method;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.annotations.AnnotationHandler;

public class AdjacencyHandler extends AbstractIncidenceHandler implements AnnotationHandler<Adjacency> {

	@Override
	public Class<Adjacency> getAnnotationType() {
		return Adjacency.class;
	}

	@Override
	public Object processElement(final Adjacency annotation, final Method method, final Object[] arguments, final FramedGraph framedGraph,
			final Element element, final Direction direction) {
		if (element instanceof Vertex) {
			return processVertexAdjacency(annotation, method, arguments, framedGraph, (Vertex) element);
		} else {
			throw new UnsupportedOperationException();
		}
	}

}
