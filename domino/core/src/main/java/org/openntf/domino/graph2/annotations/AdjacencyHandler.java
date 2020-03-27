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

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.annotations.AnnotationHandler;

public class AdjacencyHandler extends AbstractIncidenceHandler implements AnnotationHandler<Adjacency> {

	@Override
	public Class<Adjacency> getAnnotationType() {
		return Adjacency.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object processElement(final Adjacency annotation, final Method method, final Object[] arguments, final FramedGraph framedGraph,
			final Element element, final Direction direction) {
		if (element instanceof Vertex) {
			return processVertexAdjacency(annotation, method, arguments, framedGraph, (Vertex) element);
		} else {
			throw new UnsupportedOperationException();
		}
	}

}
