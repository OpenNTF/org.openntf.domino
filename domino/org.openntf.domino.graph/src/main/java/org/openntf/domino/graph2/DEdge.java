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
/**
 * 
 */
package org.openntf.domino.graph2;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author nfreeman
 * 
 */
@SuppressWarnings("nls")
public interface DEdge extends DElement, com.tinkerpop.blueprints.Edge {
	public static final String GRAPH_TYPE_VALUE = "E";
	public static final String IN_NAME = "_OPEN_IN";
	public static final String LABEL_NAME = "_OPEN_LABEL";
	public static final String OUT_NAME = "_OPEN_OUT";
	public static final String FORMULA_FILTER = DElement.TYPE_FIELD + "=\"" + GRAPH_TYPE_VALUE + "\"";

	public Vertex getOtherVertex(Vertex vertex);

	public Object getOtherVertexProperty(Vertex vertex, String property);

	public Object getOtherVertexId(final Vertex vertex);

	public Object getVertexId(final Direction direction);

}
