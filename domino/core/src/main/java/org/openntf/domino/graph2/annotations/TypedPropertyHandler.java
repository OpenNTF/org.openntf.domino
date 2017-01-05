package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Method;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.modules.MethodHandler;

public class TypedPropertyHandler extends AbstractPropertyHandler implements MethodHandler<TypedProperty> {
	@Override
	public Class<TypedProperty> getAnnotationType() {
		return TypedProperty.class;
	}

	@Override
	public Object processElement(final Object frame, final Method method, final Object[] arguments, final TypedProperty annotation,
			final FramedGraph<?> framedGraph, final Element element) {
		Object result = processElementProperty(frame, method, arguments, annotation, framedGraph, element);
		return result;
	}

}
