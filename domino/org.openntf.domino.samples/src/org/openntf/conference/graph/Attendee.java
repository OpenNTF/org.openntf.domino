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
package org.openntf.conference.graph;

import org.openntf.conference.graph.Invite.InvitedTo;
import org.openntf.conference.graph.Invite.Invites;
import org.openntf.conference.graph.Presentation.PresentedBy;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.social.Socializer;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Attendee")
@SuppressWarnings("nls")
public interface Attendee extends Socializer {
	public static enum Status {
		REGISTERED, CANCELLED, PAID, CHECKEDIN, DEPARTED
	}

	@TypeValue(PlansToAttend.LABEL)
	public static interface PlansToAttend extends DEdgeFrame {
		public static final String LABEL = "PlansToAttend";

		public static enum Status {
			CANCELLED, FULFILLED, UNDETERMINED
		}

		@OutVertex
		public Attendee getAttendee();

		@InVertex
		public Event getEvent();

		@TypedProperty("Status")
		public Status getStatus();

		@TypedProperty("Status")
		public void setStatus(Status status);
	}

	@TypeValue(Attending.LABEL)
	public static interface Attending extends DEdgeFrame {
		public static final String LABEL = "Attending";

		@OutVertex
		public Attendee getAttendee();

		@InVertex
		public Event getEvent();
	}

	@TypeValue(MemberOf.LABEL)
	public static interface MemberOf extends DEdgeFrame {
		public static final String LABEL = "MemberOf";

		@InVertex
		public Group getGroup();

		@OutVertex
		public Attendee getAttendee();
	}

	@TypedProperty("Firstname")
	public String getFirstName();

	@TypedProperty("Firstname")
	public void setFirstName(String firstName);

	@TypedProperty("Lastname")
	public String getLastName();

	@TypedProperty("Lastname")
	public void setLastName(String lastName);

	@TypedProperty("Email")
	public String getEmail();

	@TypedProperty("Email")
	public void setEmail(String email);

	@TypedProperty("Url")
	public String getUrl();

	@TypedProperty("Url")
	public void setUrl(String url);

	@TypedProperty("Twitter")
	public String getTwitterId();

	@TypedProperty("Twitter")
	public void setTwitterId(String twitterId);

	@TypedProperty("Facebook")
	public String getFacebookId();

	@TypedProperty("Facebook")
	public void setFacebookId(String facebookId);

	@TypedProperty("Phone")
	public String getPhone();

	@TypedProperty("Phone")
	public void setPhone(String phone);

	@TypedProperty("Country")
	public String getCountry();

	@TypedProperty("Country")
	public void setCountry(String country);

	@TypedProperty("Role")
	public String getRole();

	@TypedProperty("Role")
	public void setRole(String role);

	@TypedProperty(value = "Firstname + \" \" + Lastname", derived = true)
	public String getFullname();

	@AdjacencyUnique(label = PlansToAttend.LABEL)
	public Iterable<Event> getPlansToAttendEvents();

	@AdjacencyUnique(label = PlansToAttend.LABEL)
	public PlansToAttend addPlansToAttend(Event event);

	@AdjacencyUnique(label = PlansToAttend.LABEL)
	public void removePlansToAttend(Event event);

	@IncidenceUnique(label = PlansToAttend.LABEL)
	public Iterable<PlansToAttend> getPlansToAttend();

	@IncidenceUnique(label = PlansToAttend.LABEL)
	public void removePlansToAttend(PlansToAttend plansToAttend);

	@AdjacencyUnique(label = Attending.LABEL)
	public Iterable<Event> getAttendingEvents();

	@AdjacencyUnique(label = Attending.LABEL)
	public Attending addAttending(Event event);

	@AdjacencyUnique(label = Attending.LABEL)
	public void removeAttending(Event event);

	@IncidenceUnique(label = Attending.LABEL)
	public Iterable<Attending> getAttendings();

	@IncidenceUnique(label = Attending.LABEL)
	public void removeAttending(Attending attending);

	@AdjacencyUnique(label = PresentedBy.LABEL)
	public Iterable<Event> getPresentingEvents();

	@AdjacencyUnique(label = PresentedBy.LABEL)
	public PresentedBy addPresentingEvent(Event event);

	@AdjacencyUnique(label = PresentedBy.LABEL)
	public void removePresentingEvent(Event event);

	@IncidenceUnique(label = PresentedBy.LABEL)
	public Iterable<PresentedBy> getPresentings();

	@IncidenceUnique(label = PresentedBy.LABEL)
	public void removePresenting(PresentedBy presentedBy);

	@AdjacencyUnique(label = InvitedTo.LABEL, direction = Direction.IN)
	public Iterable<Invite> getInvitedToInvites();

	@AdjacencyUnique(label = InvitedTo.LABEL, direction = Direction.IN)
	public Invites addInvitedTo(Invite invite);

	@AdjacencyUnique(label = InvitedTo.LABEL, direction = Direction.IN)
	public void removeInvitedTo(Invite invite);

	@IncidenceUnique(label = InvitedTo.LABEL, direction = Direction.IN)
	public Iterable<InvitedTo> getInvitedTos();

	@IncidenceUnique(label = InvitedTo.LABEL, direction = Direction.IN)
	public void removeInvitedTo(InvitedTo invitedTo);

	@AdjacencyUnique(label = Invites.LABEL)
	public Iterable<Invite> getInvitations();

	@AdjacencyUnique(label = Invites.LABEL)
	public Invites addInvitation(Invite invite);

	@AdjacencyUnique(label = Invites.LABEL)
	public void removeInvitation(Invite invite);

	@IncidenceUnique(label = Invites.LABEL)
	public Invites getInvites();

	@IncidenceUnique(label = Invites.LABEL)
	public void removeInvites(Invites invites);

	@AdjacencyUnique(label = MemberOf.LABEL)
	public Iterable<Group> getMemberOfGroups();

	@AdjacencyUnique(label = MemberOf.LABEL)
	public MemberOf addMemberOfGroup(Group group);

	@AdjacencyUnique(label = MemberOf.LABEL)
	public void removeMemberOfGroup(Group group);

	@IncidenceUnique(label = MemberOf.LABEL)
	public Iterable<MemberOf> getMemberOfs();

	@IncidenceUnique(label = MemberOf.LABEL)
	public void removeMemberOf(MemberOf memberOf);
}
