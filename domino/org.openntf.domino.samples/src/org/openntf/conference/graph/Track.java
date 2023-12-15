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
package org.openntf.conference.graph;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Track")
@SuppressWarnings("nls")
public interface Track extends DVertexFrame {
	@TypeValue(Includes.LABEL)
	public static interface Includes extends DEdgeFrame {
		public static final String LABEL = "Includes";

		@InVertex
		public Track getTrack();

		@OutVertex
		public Presentation getSession();
	}

	@TypedProperty("Title")
	public String getTitle();

	@TypedProperty("Title")
	public void setTitle(String title);

	@TypedProperty("Description")
	public String getDescription();

	@TypedProperty("Description")
	public void setDescription(String description);

	@AdjacencyUnique(label = Includes.LABEL, direction = Direction.IN)
	public Iterable<Presentation> getIncludesSessions();

	@AdjacencyUnique(label = Includes.LABEL, direction = Direction.IN)
	public Includes addIncludesSession(Presentation session);

	@AdjacencyUnique(label = Includes.LABEL, direction = Direction.IN)
	public void removeIncludesSession(Presentation session);

	@IncidenceUnique(label = Includes.LABEL, direction = Direction.IN)
	public Iterable<Includes> getIncludes();

	@IncidenceUnique(label = Includes.LABEL, direction = Direction.IN)
	public void removeIncludes(Includes includes);

}
