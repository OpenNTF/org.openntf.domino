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
package org.openntf.domino.tests.obusse;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("ConferenceSession")
public interface ConferenceSession extends DVertexFrame {
	@Property("title")
	public String getTitle();

	@Property("title")
	public void setTitle(String title);

	@AdjacencyUnique(label = "attends", direction = Direction.IN)
	public Iterable<Attendee> getAttendees();

	@AdjacencyUnique(label = "attends", direction = Direction.IN)
	public Attendee addAttendee(Attendee attendee);

	@AdjacencyUnique(label = "attends", direction = Direction.IN)
	public void removeAttendee(Attendee attendee);

	@AdjacencyUnique(label = "presents", direction = Direction.IN)
	public Presenter getPresenter();

	@AdjacencyUnique(label = "presents", direction = Direction.IN)
	public Presenter addPresenter(Presenter presenter);

	@AdjacencyUnique(label = "presents", direction = Direction.IN)
	public void removePresenter(Presenter presenter);

	@IncidenceUnique(label = "presents", direction = Direction.IN)
	public Presents getPresents();
}
