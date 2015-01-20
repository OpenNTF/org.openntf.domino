package org.openntf.conference.graph;

import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;

public interface Invite extends DVertexFrame {
	public static interface Invites extends DEdgeFrame {
		public Attendee getInviter();

		public Event getEvent();
	}

	public static interface InvitedTo extends DEdgeFrame {
		public Attendee getInvitee();

		public Event getEvent();
	}

	public static enum Status {
		DRAFT, UNREAD, OPEN, TENTATIVE, COMMITTED, DECLINED;
	}

	public String getMessage();

	public void setMessage(String message);

	public Status getStatus();

	public void setStatus(Status status);

}
