package org.openntf.conference.graph.examples;

import org.openntf.conference.graph.Attendee;
import org.openntf.conference.graph.ConferenceGraph;
import org.openntf.domino.Session;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.tinkerpop.frames.FramedGraph;

public class GraphExamples {
	private ConferenceGraph theConference_;

	public GraphExamples() {
	}

	public ConferenceGraph getConference() {
		if (theConference_ == null) {
			theConference_ = new ConferenceGraph();
		}
		return theConference_;
	}

	public FramedGraph<DGraph> getGraph() {
		return getConference().getFramedGraph();
	}

	public Attendee getMe() {
		Session session = Factory.getSession(SessionType.CURRENT);
		String myName = session.getEffectiveUserName();
		return getConference().getAttendee(myName, true);
	}

}
