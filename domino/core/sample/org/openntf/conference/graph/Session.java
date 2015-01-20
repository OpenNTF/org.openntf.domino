package org.openntf.conference.graph;

import org.openntf.conference.graph.Track.Includes;
import org.openntf.domino.graph2.builtin.DEdgeFrame;

public interface Session extends Event {

	public static interface PresentedBy extends DEdgeFrame {
		public Attendee getPresenter();

		public Session getSession();
	}

	public Iterable<PresentedBy> getPresentedBys();

	public Iterable<Attendee> getPresenters();

	public Iterable<Includes> getIncludedIn();

	public Iterable<Track> getIncludedInTracks();

}
