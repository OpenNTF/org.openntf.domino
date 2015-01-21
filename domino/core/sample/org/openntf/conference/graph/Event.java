package org.openntf.conference.graph;

import org.openntf.conference.graph.Attendee.Attending;
import org.openntf.conference.graph.Attendee.PlansToAttend;
import org.openntf.conference.graph.Invite.InvitationFor;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.social.Commentable;
import org.openntf.domino.graph2.builtin.social.Likeable;
import org.openntf.domino.graph2.builtin.social.Rateable;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Event")
public abstract interface Event extends Commentable, Likeable, Rateable {
	public static interface HappeningOn extends DEdgeFrame {
		public static final String LABEL = "HappeningOn";

		@InVertex
		public TimeSlot getTimeslot();

		@OutVertex
		public Event getEvent();
	}

	public static interface HappeningAt extends DEdgeFrame {
		public static final String LABEL = "HappeningAt";

		@InVertex
		public Location getLocation();

		@OutVertex
		public Event getEvent();
	}

	public static enum Status {
		DRAFT, PROPOSED, CONFIRMED, CANCELLED;
	}

	@TypedProperty("Title")
	public String getTitle();

	@TypedProperty("Title")
	public void setTitle(String title);

	@TypedProperty("Description")
	public String getDescription();

	@TypedProperty("Description")
	public void setDescription(String description);

	@TypedProperty("Status")
	public Status getStatus();

	@TypedProperty("Status")
	public void setStatus(Status status);

	@AdjacencyUnique(label = PlansToAttend.LABEL, direction = Direction.IN)
	public Iterable<Attendee> getPlansToAttendAttendees();

	@AdjacencyUnique(label = PlansToAttend.LABEL, direction = Direction.IN)
	public PlansToAttend addPlansToAttend(Attendee attendee);

	@AdjacencyUnique(label = PlansToAttend.LABEL, direction = Direction.IN)
	public void removePlansToAttend(Attendee attendee);

	@IncidenceUnique(label = PlansToAttend.LABEL, direction = Direction.IN)
	public Iterable<PlansToAttend> getPlansToAttends();

	@IncidenceUnique(label = PlansToAttend.LABEL, direction = Direction.IN)
	public void removePlansToAttend(PlansToAttend plansToAttend);

	@AdjacencyUnique(label = Attending.LABEL, direction = Direction.IN)
	public Iterable<Attendee> getAttendingAttendees();

	@AdjacencyUnique(label = Attending.LABEL, direction = Direction.IN)
	public Attending addAttendingAttendee(Attendee attendee);

	@AdjacencyUnique(label = Attending.LABEL, direction = Direction.IN)
	public void removeAttendingAttendee(Attendee attendee);

	@IncidenceUnique(label = Attending.LABEL, direction = Direction.IN)
	public Iterable<Attending> getAttendings();

	@IncidenceUnique(label = Attending.LABEL, direction = Direction.IN)
	public void removeAttending(Attending attending);

	@AdjacencyUnique(label = HappeningOn.LABEL)
	public Iterable<TimeSlot> getTimes();

	@AdjacencyUnique(label = HappeningOn.LABEL)
	public HappeningOn addTime(TimeSlot time);

	@AdjacencyUnique(label = HappeningOn.LABEL)
	public void removingTime(TimeSlot time);

	@IncidenceUnique(label = HappeningOn.LABEL)
	public Iterable<HappeningOn> getHappeningOns();

	@IncidenceUnique(label = HappeningOn.LABEL)
	public void removingHappeningOn(HappeningOn happeningOn);

	@AdjacencyUnique(label = HappeningAt.LABEL)
	public Iterable<Location> getLocations();

	@AdjacencyUnique(label = HappeningAt.LABEL)
	public HappeningAt addLocation(Location location);

	@AdjacencyUnique(label = HappeningAt.LABEL)
	public void removingLocation(Location location);

	@IncidenceUnique(label = HappeningAt.LABEL)
	public Iterable<HappeningAt> getHappeningAts();

	@IncidenceUnique(label = HappeningAt.LABEL)
	public void removingHappeningAt(HappeningAt happeningAt);

	@AdjacencyUnique(label = InvitationFor.LABEL)
	public Iterable<Invite> getInvites();

	@AdjacencyUnique(label = InvitationFor.LABEL)
	public InvitationFor addInvite(Invite invite);

	@AdjacencyUnique(label = InvitationFor.LABEL)
	public void removeInvite(Invite invite);

	@IncidenceUnique(label = InvitationFor.LABEL)
	public InvitationFor getInvitationFors();

	@IncidenceUnique(label = InvitationFor.LABEL)
	public void removeInvitationFor(InvitationFor invitationFor);

}
