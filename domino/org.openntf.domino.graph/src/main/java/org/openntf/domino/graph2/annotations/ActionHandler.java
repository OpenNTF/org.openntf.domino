/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
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
