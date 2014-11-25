package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Method;

import com.tinkerpop.blueprints.Edge;

public enum AnnotationUtilities {
	;

	private static final String FIND = "find";

	public static boolean isFindMethod(final Method method) {
		Class<?> returnType = method.getReturnType();
		return (method.getName().startsWith(FIND));
	}

	public static boolean returnsEdge(final Method method) {
		return Edge.class.isAssignableFrom(method.getReturnType());
	}

}
