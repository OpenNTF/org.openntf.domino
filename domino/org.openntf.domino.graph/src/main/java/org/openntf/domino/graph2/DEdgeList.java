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
package org.openntf.domino.graph2;

import java.util.List;

import org.openntf.domino.graph2.impl.DVertexList;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public interface DEdgeList extends List<Edge> {

	public abstract DEdgeList atomic();

	public abstract DEdgeList unmodifiable();

	public abstract Edge findEdge(Vertex toVertex);

	public abstract DEdgeList applyFilter(String key, Object value);

	public abstract DVertexList toVertexList();

	public boolean isUnique();

	public void setUnique(boolean isUnique);

	public String getLabel();

	public void setLabel(String label);

}