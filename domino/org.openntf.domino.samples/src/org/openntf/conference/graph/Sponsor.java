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
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Sponsor")
@SuppressWarnings("nls")
public abstract interface Sponsor extends Group {

	public static enum Level {
		STRATEGIC(0), PLATINUM(1), GOLD(2), SILVER(3), BRONZE(4);

		private final int value_;

		private Level(final int level) {
			value_ = level;
		}

		public int getValue() {
			return value_;
		}
	}

	@TypeValue(ContactFor.LABEL)
	public static interface ContactFor extends DEdgeFrame {
		public static final String LABEL = "ContactFor";

		@OutVertex
		public Attendee getContacts();

		@InVertex
		public Sponsor getContactsFor();
	}

	// How do we always set the type to COMPANY? Do we need a FrameInitializer
	// for this?

	@TypedProperty("Url")
	public String getUrl();

	@TypedProperty("Url")
	public void setUrl(String url);

	@TypedProperty("PhotoUrl")
	public String getPhotoUrl();

	@TypedProperty("PhotoUrl")
	public void setPhotoUrl(String photoUrl);

	@TypedProperty("Profile")
	public String getProfile();

	@TypedProperty("Profile")
	public void setProfile(String profile);

	@TypedProperty("Level")
	public Level getLevel();

	@TypedProperty("Level")
	public void setLevel(Level level);

	@AdjacencyUnique(label = ContactFor.LABEL, direction = Direction.IN)
	public Iterable<Attendee> getContactAttendees();

	@AdjacencyUnique(label = ContactFor.LABEL, direction = Direction.IN)
	public ContactFor addContactFor(Attendee contact);

	@AdjacencyUnique(label = ContactFor.LABEL, direction = Direction.IN)
	public void removeContactFor(Attendee contact);

}
