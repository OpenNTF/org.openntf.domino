/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.graph2.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Vector;

import org.openntf.domino.Document;
import org.openntf.domino.exceptions.UserAccessException;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object processElementProperty(final Object frame, final Method method, final Object[] arguments, final Annotation annotation,
			final FramedGraph framedGraph, final Element element) {
		boolean isDerived = false;
		String value = ""; //$NON-NLS-1$
		@SuppressWarnings("unused")
		Class<?> converter = null;
		String defaultValue = null;
		String computation = null;
		if (annotation instanceof Property) {
			value = ((Property) annotation).value();
		} else if (annotation instanceof TypedProperty) {
			value = ((TypedProperty) annotation).value();
			isDerived = ((TypedProperty) annotation).derived();
			//			converter = ((TypedProperty) annotation).converter();
			defaultValue = ((TypedProperty) annotation).defaultValue();
			//			if (defaultValue != null) {
			//				System.out.println("TEMP DEBUG defaultValue found of " + defaultValue + " on method " + method.getName());
			//			}
		} else if (annotation instanceof ComputedProperty) {
			//			System.out.println("TEMP DEBUG handling a computed property");
			value = ((ComputedProperty) annotation).value();
			computation = ((ComputedProperty) annotation).computation();
			//			isDerived = true;
		}

		if (ClassUtilities.isSetMethod(method) && isDerived) {
			throw new DerivedPropertySetException(MessageFormat.format("Setting on a derived property {0} is not permitted.", value)); //$NON-NLS-1$
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
				//				System.out.println("TEMP DEBUG defaultValue of " + String.valueOf(raw) + " calculated for " + value);
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
						value = ""; //$NON-NLS-1$
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
		} catch (UserAccessException uae) {
			throw uae;
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return null;
	}

}
