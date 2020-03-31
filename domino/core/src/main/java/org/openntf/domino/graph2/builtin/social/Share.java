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
package org.openntf.domino.graph2.builtin.social;

import java.util.List;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Share")
public interface Share extends Likeable {
	@TypedProperty("Body")
	public String getBody();

	@TypedProperty("Body")
	public void setBody(String body);

	@TypedProperty("FollowUpDate")
	public String getFollowUpDate();

	@TypedProperty("FollowUpDate")
	public void setFollowUpDate(String date);

	@AdjacencyUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public List<Sharer> getSharers();

	@AdjacencyUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public SharedBy addSharer(Sharer sharer);

	@AdjacencyUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public void removeSharer(Sharer sharer);

	@IncidenceUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public List<SharedBy> getSharedBys();

	@IncidenceUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public int countSharedBys();

	@IncidenceUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public void removeSharedBy(SharedBy sharedBy);

	@AdjacencyUnique(label = SharedWith.LABEL)
	public List<Sharer> getSharedWithSharers();

	@AdjacencyUnique(label = SharedWith.LABEL)
	public Sharer addSharedWithSharer(Sharer sharer);

	@AdjacencyUnique(label = SharedWith.LABEL)
	public void removeSharedWithSharer(Sharer sharer);

	@IncidenceUnique(label = SharedWith.LABEL)
	public List<SharedWith> getSharedWiths();

	@IncidenceUnique(label = SharedWith.LABEL)
	public int countSharedWiths();

	@IncidenceUnique(label = SharedWith.LABEL)
	public void removeSharedWith(SharedWith sharedWith);

}
