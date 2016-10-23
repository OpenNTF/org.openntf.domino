package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Method;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.MethodHandler;

public class PropertyHandler extends AbstractPropertyHandler implements MethodHandler<Property> {

	@Override
	public Class<Property> getAnnotationType() {
		return Property.class;
	}

	@Override
	public Object processElement(final Object frame, final Method method, final Object[] arguments, final Property annotation,
			final FramedGraph<?> framedGraph, final Element element) {
		return processElementProperty(frame, method, arguments, annotation, framedGraph, element);
	}

}
