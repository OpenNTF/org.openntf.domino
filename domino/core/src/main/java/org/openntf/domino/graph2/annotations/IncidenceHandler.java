package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Method;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.annotations.AnnotationHandler;

public class IncidenceHandler extends AbstractIncidenceHandler implements AnnotationHandler<Incidence> {
	@Override
	public Class<Incidence> getAnnotationType() {
		return Incidence.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object processElement(final Incidence annotation, final Method method, final Object[] arguments, final FramedGraph framedGraph,
			final Element element, final Direction direction) {
		if (element instanceof Vertex) {
			return processVertexAdjacency(annotation, method, arguments, framedGraph, (Vertex) element);
		} else {
			throw new UnsupportedOperationException();
		}
	}

}
