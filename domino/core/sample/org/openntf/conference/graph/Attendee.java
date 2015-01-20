package org.openntf.conference.graph;

import org.openntf.conference.graph.Session.PresentedBy;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.social.Socializer;

public interface Attendee extends Socializer {

	public static interface PlansToAttend extends DEdgeFrame {
		//TODO Status: Cancelled, Fulfilled, Undetermined
	}

	public static interface Attending extends DEdgeFrame {
		public Attendee getAttendee();

		public Event getEvent();
	}

	public static interface MemberOf extends DEdgeFrame {
		public Group getGroup();

		public Attendee getAttendee();
	}

	//TODO NTF FirstName, LastName, Email, URL, Twitter, Facebook, Phone, Country, Role
	public Iterable<Event> getPlansToAttendEvents();

	public Iterable<PlansToAttend> getPlansToAttend();

	public Iterable<Event> getAttendingEvents();

	public Iterable<Attending> getAttendings();

	public Iterable<Event> getPresentingEvents();

	public Iterable<PresentedBy> getPresentings();

	public Iterable<Group> getMemberOfGroups();

	public Iterable<MemberOf> getMemberOfs();
}
