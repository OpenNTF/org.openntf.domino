package org.openntf.conference.graph;

import java.util.Date;

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

@TypeValue("Conference")
public interface Conference extends Commentable, Likeable, Rateable {
	@TypeValue(PartOf.LABEL)
	public static interface PartOf extends DEdgeFrame {
		public static final String LABEL = "PartOf";

		@InVertex
		public Conference getConference();

		@OutVertex
		public Event getEvent();
	}

	@TypeValue(HasPlace.LABEL)
	public static interface HasPlace extends DEdgeFrame {
		public static final String LABEL = "HasPlace";

		@InVertex
		public Conference getConference();

		@OutVertex
		public Location getLocation();
	}

	@TypeValue(HasTime.LABEL)
	public static interface HasTime extends DEdgeFrame {
		public static final String LABEL = "HasTime";

		@InVertex
		public Conference getConference();

		@OutVertex
		public TimeSlot getTimeSlot();
	}

	@TypeValue(HasRegistrant.LABEL)
	public static interface HasRegistrant extends DEdgeFrame {
		public static final String LABEL = "HasRegistrant";

		@InVertex
		public Conference getConference();

		@OutVertex
		public Attendee getAttendee();

		@TypedProperty("Status")
		public Attendee.Status getStatus();

		@TypedProperty("Status")
		public void setStatus(Attendee.Status status);

		@TypedProperty("StatusTime")
		public Date getStatusUpdateTime();

		@TypedProperty("StatusTime")
		public void setStatusUpdateTime(Date statusTime);

	}

	@TypedProperty("Title")
	public String getTitle();

	@TypedProperty("Title")
	public void setTitle(String title);

	@TypedProperty("Hashtag")
	public String getHashtag();

	@TypedProperty("Hashtag")
	public void setHashtag(String hashtag);

	@TypedProperty("City")
	public String getCity();

	@TypedProperty("City")
	public void setCity(String city);

	@TypedProperty("StartDate")
	public Date getStartDate();

	@TypedProperty("StartDate")
	public void setStartDate(Date startDate);

	@TypedProperty("EndDate")
	public Date getEndDate();

	@TypedProperty("EndDate")
	public void setEndDate(Date endDate);

	@AdjacencyUnique(label = PartOf.LABEL, direction = Direction.IN)
	public Iterable<Event> getEvents();

	@AdjacencyUnique(label = PartOf.LABEL, direction = Direction.IN)
	public PartOf addEvent(Event event);

	@AdjacencyUnique(label = PartOf.LABEL, direction = Direction.IN)
	public void removeEvent(Event event);

	@IncidenceUnique(label = PartOf.LABEL, direction = Direction.IN)
	public Iterable<PartOf> getPartOfs();

	@IncidenceUnique(label = PartOf.LABEL, direction = Direction.IN)
	public void removePartOf(PartOf partOf);

	@AdjacencyUnique(label = HasPlace.LABEL, direction = Direction.IN)
	public Iterable<Location> getLocations();

	@AdjacencyUnique(label = HasPlace.LABEL, direction = Direction.IN)
	public HasPlace addLocation(Location location);

	@AdjacencyUnique(label = HasPlace.LABEL, direction = Direction.IN)
	public void removeLocation(Location location);

	@IncidenceUnique(label = HasPlace.LABEL, direction = Direction.IN)
	public Iterable<HasPlace> getHasPlaces();

	@IncidenceUnique(label = HasPlace.LABEL, direction = Direction.IN)
	public void removeHasPlace(HasPlace hasPlace);

	@AdjacencyUnique(label = HasTime.LABEL, direction = Direction.IN)
	public Iterable<TimeSlot> getTimeSlots();

	@AdjacencyUnique(label = HasTime.LABEL, direction = Direction.IN)
	public HasTime addTimeSlot(TimeSlot timeSlot);

	@AdjacencyUnique(label = HasTime.LABEL, direction = Direction.IN)
	public void removeTimeSlot(TimeSlot timeSlot);

	@IncidenceUnique(label = HasTime.LABEL, direction = Direction.IN)
	public Iterable<HasTime> getHasTimes();

	@IncidenceUnique(label = HasTime.LABEL, direction = Direction.IN)
	public void removeHasTime(HasTime hasTime);

	@AdjacencyUnique(label = HasRegistrant.LABEL, direction = Direction.IN)
	public Iterable<Attendee> getAttendees();

	@AdjacencyUnique(label = HasRegistrant.LABEL, direction = Direction.IN)
	public HasTime addAttendee(Attendee attendee);

	@AdjacencyUnique(label = HasRegistrant.LABEL, direction = Direction.IN)
	public void removeAttendee(Attendee attendee);

	@IncidenceUnique(label = HasRegistrant.LABEL, direction = Direction.IN)
	public Iterable<HasRegistrant> getHasRegistrants();

	@IncidenceUnique(label = HasRegistrant.LABEL, direction = Direction.IN)
	public void removeHasRegistrant(HasRegistrant hasRegistrant);

}
