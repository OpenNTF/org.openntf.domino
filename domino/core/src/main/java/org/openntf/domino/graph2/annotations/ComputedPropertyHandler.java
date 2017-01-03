package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Method;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.modules.MethodHandler;

public class ComputedPropertyHandler extends AbstractPropertyHandler implements MethodHandler<ComputedProperty> {
	@Override
	public Class<ComputedProperty> getAnnotationType() {
		return ComputedProperty.class;
	}

	@Override
	public Object processElement(final Object frame, final Method method, final Object[] arguments, final ComputedProperty annotation,
			final FramedGraph<?> framedGraph, final Element element) {
		Object result = processElementProperty(frame, method, arguments, annotation, framedGraph, element);
		return result;
	}

}
