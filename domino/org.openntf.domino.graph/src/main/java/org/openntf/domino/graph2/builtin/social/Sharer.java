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
package org.openntf.domino.graph2.builtin.social;

import java.util.List;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Sharer")
public interface Sharer extends DVertexFrame {
	@AdjacencyUnique(label = SharedBy.LABEL)
	public List<Share> getShares();

	@AdjacencyUnique(label = SharedBy.LABEL)
	public SharedBy addShare(Share share);

	@AdjacencyUnique(label = SharedBy.LABEL)
	public void removeShare(Share share);

	@IncidenceUnique(label = SharedBy.LABEL)
	public List<SharedBy> getSharedBys();

	@IncidenceUnique(label = SharedBy.LABEL)
	public int countSharedBys();

	@IncidenceUnique(label = SharedBy.LABEL)
	public void removeSharedBy(SharedBy sharedBy);

	@AdjacencyUnique(label = SharedWith.LABEL, direction = Direction.IN)
	public List<Share> getSharedWithShares();

	@AdjacencyUnique(label = SharedWith.LABEL, direction = Direction.IN)
	public SharedWith addSharedWithShare(Share share);

	@AdjacencyUnique(label = SharedWith.LABEL, direction = Direction.IN)
	public void removeSharedWithShare(Share share);

	@IncidenceUnique(label = SharedWith.LABEL, direction = Direction.IN)
	public List<SharedWith> getSharedWiths();

	@IncidenceUnique(label = SharedWith.LABEL, direction = Direction.IN)
	public int countSharedWiths();

	@IncidenceUnique(label = SharedWith.LABEL, direction = Direction.IN)
	public void removeSharedWith(SharedWith sharedWith);

}
