package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Method;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.modules.MethodHandler;

public class ActionHandler implements MethodHandler<Action> {

	@SuppressWarnings("serial")
	public static class NoDefaultHandlerException extends RuntimeException {
		private final String message_;

		public NoDefaultHandlerException(final String name) {
			super();
			message_ = "No JavaHandler is defined for Action annotation " + name;
		}

		@Override
		public String getMessage() {
			return message_;
		}
	}

	@Override
	public Class<Action> getAnnotationType() {
		return Action.class;
	}

	@Override
	public Object processElement(final Object frame, final Method method, final Object[] arguments, final Action annotation,
			final FramedGraph<?> framedGraph, final Element element) {
		throw new NoDefaultHandlerException(annotation.name());
	}

}
