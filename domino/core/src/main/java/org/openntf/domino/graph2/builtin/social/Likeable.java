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
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Likable")
public interface Likeable extends DVertexFrame {

	@AdjacencyUnique(label = Likes.LABEL, direction = Direction.IN)
	public List<Liker> getLikers();

	@AdjacencyUnique(label = Likes.LABEL, direction = Direction.IN)
	public Likes addLiker(Liker liker);

	@AdjacencyUnique(label = Likes.LABEL, direction = Direction.IN)
	public void removeLiker(Liker liker);

	@IncidenceUnique(label = Likes.LABEL, direction = Direction.IN)
	public List<Likes> getLikedBys();

	@IncidenceUnique(label = Likes.LABEL, direction = Direction.IN)
	public int countLikedBys();

	@IncidenceUnique(label = Likes.LABEL, direction = Direction.IN)
	public void removeLikedBy(Likes likes);

}
