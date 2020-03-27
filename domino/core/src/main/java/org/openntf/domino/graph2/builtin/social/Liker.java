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

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Liker")
public interface Liker extends DVertexFrame {
	@AdjacencyUnique(label = Likes.LABEL)
	public List<Likeable> getLikeables();

	@AdjacencyUnique(label = Likes.LABEL)
	public Likes addLikeable(Likeable likeable);

	@AdjacencyUnique(label = Likes.LABEL)
	public void removeLikeable(Likeable likeable);

	@IncidenceUnique(label = Likes.LABEL)
	public List<Likes> getLikes();

	@IncidenceUnique(label = Likes.LABEL)
	public int countLikes();

	@IncidenceUnique(label = Likes.LABEL)
	public void removeLikes(Likes likes);

}
