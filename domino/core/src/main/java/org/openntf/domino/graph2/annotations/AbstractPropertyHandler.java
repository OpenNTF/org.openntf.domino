package org.openntf.domino.graph2.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Vector;

import org.openntf.domino.Document;
import org.openntf.domino.graph2.impl.DElement;
import org.openntf.domino.helpers.Formula;
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

	private Object processFormula(final Formula formula, final Element element) {
		Object result = null;
		if (element instanceof DElement) {
			Map<String, Object> rawDoc = ((DElement) element).getDelegate();
			if (rawDoc instanceof Document) {
				result = formula.getValue((Document) rawDoc);
				if (result instanceof Vector) {
					Vector<Object> v = (Vector) result;
					if (v.size() == 1) {
						result = v.get(0);
					}
				}
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public Object processElementProperty(final Object frame, final Method method, final Object[] arguments, final Annotation annotation,
			final FramedGraph framedGraph, final Element element) {
		boolean isDerived = false;
		String value = "";
		Class<?> converter = null;
		String defaultValue = null;
		String computation = null;
		if (annotation instanceof Property) {
			value = ((Property) annotation).value();
		} else if (annotation instanceof TypedProperty) {
			value = ((TypedProperty) annotation).value();
			isDerived = ((TypedProperty) annotation).derived();
			converter = ((TypedProperty) annotation).converter();
			defaultValue = ((TypedProperty) annotation).defaultValue();
		} else if (annotation instanceof ComputedProperty) {
			//			System.out.println("TEMP DEBUG handling a computed property");
			value = ((ComputedProperty) annotation).value();
			computation = ((ComputedProperty) annotation).computation();
			//			isDerived = true;
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
		if (null == raw || (raw instanceof String && ((String) raw).length() == 0)) {
			if (defaultValue != null && defaultValue.length() > 0) {
				Formula formula = new Formula(defaultValue);
				raw = processFormula(formula, element);
			} else if (computation != null && computation.length() > 0) {
				//				System.out.println("TEMP DEBUG Running a computed property: " + value);
				Formula formula = new Formula(computation);
				raw = processFormula(formula, element);
			}
		}
		Object result = null;
		if (raw == null) {
			result = null;
		} else if (type.isAssignableFrom(raw.getClass())) {
			result = type.cast(raw);
		} else if (lotus.domino.Base.class.isAssignableFrom(type)) {
			result = org.openntf.domino.utils.TypeUtils.convertToTarget(raw, type,
					org.openntf.domino.utils.Factory.getSession(SessionType.CURRENT));
		} else {
			result = org.openntf.domino.utils.TypeUtils.convertToTarget(raw, type, null);
		}
		if (computation != null && computation.length() > 0) {
			element.setProperty(value, result);
		}
		return result;

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
