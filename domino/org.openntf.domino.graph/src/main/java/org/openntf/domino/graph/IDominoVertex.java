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
/**
 * 
 */
package org.openntf.domino.graph;

import java.util.Set;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author nfreeman
 * 
 */
@Deprecated
public interface IDominoVertex extends Vertex, IDominoElement {
	public void addInEdge(final Edge edge);

	public void addOutEdge(final Edge edge);

	public String validateEdges();

	public int getInEdgeCount(String label);

	public int getOutEdgeCount(String label);

	public Set<IEdgeHelper> getEdgeHelpers();

	public Set<String> getInEdgeLabels();

	public Set<String> getOutEdgeLabels();

	public Set<Edge> getEdges(final String... labels);

}
