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
package org.openntf.domino.graph;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author nfreeman
 * 
 */
@Deprecated
public interface IEdgeHelper {

	public String getLabel();

	public Class<? extends Vertex> getInType();

	public Class<? extends Vertex> getOutType();

	public int getEdgeCount(final Vertex vertex);

	public Edge makeEdge(final Vertex defaultOut, final Vertex defaultIn);

	public boolean isUnique();

	public Edge findEdge(final Vertex defaultOut, final Vertex defaultIn);

	public Class<? extends Vertex> getOtherType(final Class<? extends Vertex> type);

	public Class<? extends Vertex> getOtherType(final Vertex vertex);

	public Set<? extends Edge> getEdges(final Vertex vertex);

	public SortedSet<? extends Edge> getSortedEdges(final Vertex vertex, final String... sortproperties);

	public Set<? extends Vertex> getOtherVertexes(final Vertex vertex);

	public Set<? extends Vertex> getSortedOtherVertexes(final Vertex vertex, final String... sortproperties);

	public Set<Vertex> getOtherVertexesByEdge(final Vertex vertex, final String... sortproperties);

	public Vertex getOtherVertex(final Edge edge, final Vertex vertex);

	public Set<? extends Edge> getFilteredEdges(final Vertex vertex, final Map<String, Object> filterMap);

}
