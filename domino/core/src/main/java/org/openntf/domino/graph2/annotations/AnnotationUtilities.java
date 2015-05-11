package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Method;

import com.tinkerpop.blueprints.Edge;

public enum AnnotationUtilities {
	;

	private static final String FIND = "find";
	private static final String COUNT = "count";

	public static boolean isFindMethod(final Method method) {
		return (method.getName().startsWith(FIND));
	}

	public static boolean isCountMethod(final Method method) {
		return (method.getName().startsWith(COUNT));
	}

	public static boolean returnsEdge(final Method method) {
		return Edge.class.isAssignableFrom(method.getReturnType());
	}

}
