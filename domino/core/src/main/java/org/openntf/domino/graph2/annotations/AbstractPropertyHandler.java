package org.openntf.domino.graph2.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.openntf.domino.utils.Factory.SessionType;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.frames.ClassUtilities;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.Property;

public abstract class AbstractPropertyHandler {
	public static class DerivedPropertySetException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public DerivedPropertySetException(final String message) {
			super(message);
		}
	}

	@SuppressWarnings("rawtypes")
	public Object processElementProperty(final Object frame, final Method method, final Object[] arguments, final Annotation annotation,
			final FramedGraph framedGraph, final Element element) {
		boolean isDerived = false;
		String value = "";
		Class<?> converter = null;
		if (annotation instanceof Property) {
			value = ((Property) annotation).value();
		} else if (annotation instanceof TypedProperty) {
			value = ((TypedProperty) annotation).value();
			isDerived = ((TypedProperty) annotation).derived();
			converter = ((TypedProperty) annotation).converter();
		}

		if (ClassUtilities.isSetMethod(method) && isDerived) {
			throw new DerivedPropertySetException("Setting on a derived property " + value + " is not permitted.");
		}
		Class<?> type = method.getReturnType();
		if (ClassUtilities.isSetMethod(method)) {
			Class<?>[] paramTypes = method.getParameterTypes();
			int i = 0;
			for (Class paramType : paramTypes) {
				if (lotus.domino.Base.class.isAssignableFrom(paramType)) {
					arguments[i] = org.openntf.domino.utils.TypeUtils.convertToTarget(arguments[i], paramType,
							org.openntf.domino.utils.Factory.getSession(SessionType.NATIVE));
				} else {
					arguments[i] = org.openntf.domino.utils.TypeUtils.convertToTarget(arguments[i], paramType, null);
				}
				i++;
			}
		}
		Object raw = orig_processElement(value, method, arguments, framedGraph, element, null);
		if (raw == null)
			return null;
		if (type.isAssignableFrom(raw.getClass()))
			return type.cast(raw);
		if (lotus.domino.Base.class.isAssignableFrom(type)) {
			return org.openntf.domino.utils.TypeUtils.convertToTarget(raw, type,
					org.openntf.domino.utils.Factory.getSession(SessionType.NATIVE));
		} else {
			Object result = org.openntf.domino.utils.TypeUtils.convertToTarget(raw, type, null);
			return result;
		}

	}

	@SuppressWarnings("rawtypes")
	public Object orig_processElement(final String annotationValue, final Method method, final Object[] arguments,
			final FramedGraph framedGraph, final Element element, final Direction direction) {
		try {
			if (ClassUtilities.isGetMethod(method)) {
				Object value = element.getProperty(annotationValue);
				if (value instanceof java.util.Vector) {
					if (((java.util.Vector) value).isEmpty()) {
						value = "";
					}
				}
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
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return null;
	}

}
