/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tinkerpop.blueprints.Direction;

/**
 * Ajacency annotate getters and adders to represent a Vertex incident to an Edge. AjacencyUnique extends this idea to ensure that there is
 * only one instance of an Edge with a specific label between any two specific Vertices. This allows the user of the graph to call an "add"
 * method and return the existing ajacency if it already exists.
 *
 * @author Nathan T. Freeman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AdjacencyUnique {
	/**
	 * The label of the edges making the adjacency between the vertices.
	 *
	 * @return the edge label
	 */
	public String label();

	/**
	 * The edge direction of the adjacency.
	 *
	 * @return the direction of the edges composing the adjacency
	 */
	public Direction direction() default Direction.OUT;
}
