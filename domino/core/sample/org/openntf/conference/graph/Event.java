package org.openntf.conference.graph;

import org.openntf.conference.graph.Attendee.Attending;
import org.openntf.conference.graph.Attendee.PlansToAttend;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.social.Commentable;
import org.openntf.domino.graph2.builtin.social.Likeable;
import org.openntf.domino.graph2.builtin.social.Rateable;

public interface Event extends Commentable, Likeable, Rateable {
	public static interface HappeningOn extends DEdgeFrame {
		public TimeSlot getTimeslot();

		public Event getEvent();
	}

	public static interface HappeningAt extends DEdgeFrame {
		public Location getLocation();

		public Event getEvent();
	}

	public static enum Status {
		DRAFT, PROPOSED, CONFIRMED, CANCELLED;
	}

	public String getTitle();

	public void setTitle(String title);

	public String getDescription();

	public void setDescription(String description);

	public Status getStatus();

	public void setStatus(Status status);

	public Iterable<Attendee> getPlansToAttendAttendees();

	public Iterable<PlansToAttend> getPlansToAttends();

	public Iterable<Attendee> getAttendingAttendees();

	public Iterable<Attending> getAttendings();

	public Iterable<Attendee> getPresentingAttendees();

	public Iterable<Presenting> getPresentings();
}
