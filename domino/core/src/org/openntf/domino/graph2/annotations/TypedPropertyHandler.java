package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Method;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.frames.ClassUtilities;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.modules.MethodHandler;

public class TypedPropertyHandler implements MethodHandler<TypedProperty> {
	public static class DerivedPropertySetException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public DerivedPropertySetException(final String message) {
			super(message);
		}
	}

	public TypedPropertyHandler() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getAnnotationType() {
		return TypedProperty.class;
	}

	@Override
	public Object processElement(final Object frame, final Method method, final Object[] arguments, final TypedProperty property,
			final FramedGraph framedGraph, final Element element) {
		if (ClassUtilities.isSetMethod(method) && property.derived()) {
			throw new DerivedPropertySetException("Setting on a derived property " + property.value() + " is not permitted.");
		}
		Object raw = orig_processElement(property.value(), method, arguments, framedGraph, element, null);
		Class<?> type = method.getReturnType();
		if (raw == null)
			return null;
		if (type.isAssignableFrom(raw.getClass()))
			return type.cast(raw);
		if (lotus.domino.Base.class.isAssignableFrom(type)) {
			return org.openntf.domino.utils.TypeUtils.convertToTarget(raw, type,
					org.openntf.domino.utils.Factory.getSession((lotus.domino.Base) raw));
		} else {
			return org.openntf.domino.utils.TypeUtils.convertToTarget(raw, type, null);
		}

	}

	public Object orig_processElement(final String annotationValue, final Method method, final Object[] arguments,
			final FramedGraph framedGraph, final Element element, final Direction direction) {
		if (ClassUtilities.isGetMethod(method)) {
			Object value = element.getProperty(annotationValue);
			return value;
		} else if (ClassUtilities.isSetMethod(method)) {
			Object value = arguments[0];
			if (null == value) {
				element.removeProperty(annotationValue);
			} else {
				element.setProperty(annotationValue, value);
			}
			return null;
		} else if (ClassUtilities.isRemoveMethod(method)) {
			element.removeProperty(annotationValue);
			return null;
		}

		return null;
	}

	private Enum getValueAsEnum(final Method method, final Object value) {
		Class<Enum> en = (Class<Enum>) method.getReturnType();
		if (value != null)
			return Enum.valueOf(en, value.toString());

		return null;
	}
}
