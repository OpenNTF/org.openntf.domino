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

import java.lang.reflect.Method;

import com.tinkerpop.blueprints.Edge;

@SuppressWarnings("nls")
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
