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

import java.util.Set;

import org.openntf.domino.View;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author nfreeman
 *
 */
@SuppressWarnings("nls")
public interface DVertex extends com.tinkerpop.blueprints.Vertex, DElement {
	public static final String GRAPH_TYPE_VALUE = "V";
	public static final String FORMULA_FILTER = DElement.TYPE_FIELD + "=\"" + GRAPH_TYPE_VALUE + "\"";

	public void addInEdge(final Edge edge);

	public void addOutEdge(final Edge edge);

	public boolean validateEdges();

	public int getInEdgeCount(String label);

	public int getOutEdgeCount(String label);

	public Set<String> getInEdgeLabels();

	public Set<String> getOutEdgeLabels();

	public Iterable<Edge> getEdges(final String... labels);

	public Edge findInEdge(final Vertex otherVertex, final String label, boolean isUnique);

	public Edge findOutEdge(final Vertex otherVertex, final String label, boolean isUnique);

	public Edge findEdge(final Vertex otherVertex, final String label);

	public View getView();

	//	public Map<String, Object> getFrameImplCache();

	public Object getFrameImplObject(String key);

	public void setFrameImplObject(String key, Object value);

	DElementStore getElementStore();

}
