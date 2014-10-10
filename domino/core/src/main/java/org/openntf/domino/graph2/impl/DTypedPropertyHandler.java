package org.openntf.domino.graph2.impl;

import java.lang.reflect.Method;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.annotations.PropertyAnnotationHandler;
import com.tinkerpop.frames.modules.MethodHandler;

public class DTypedPropertyHandler implements MethodHandler<Property> {
	PropertyAnnotationHandler delegate_ = new PropertyAnnotationHandler();

	public DTypedPropertyHandler() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class getAnnotationType() {
		return Property.class;
	}

	@Override
	public Object processElement(final Object frame, final Method method, final Object[] arguments, final Property property,
			final FramedGraph framedGraph, final Element element) {
		Object raw = delegate_.processElement(property, method, arguments, framedGraph, element, null);
		Class<?> type = method.getReturnType();
		if (raw == null)
			return null;
		if (type.isAssignableFrom(raw.getClass()))
			return type.cast(raw);
		if (lotus.domino.Base.class.isAssignableFrom(type)) {
			return org.openntf.domino.utils.TypeUtils.convertToTarget(raw, type, org.openntf.domino.utils.Factory.getSession());
		} else {
			return org.openntf.domino.utils.TypeUtils.convertToTarget(raw, type, null);
		}

	}
}
