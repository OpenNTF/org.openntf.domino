package org.openntf.conference.graph;

import org.openntf.conference.graph.Track.Includes;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Presentation")
public interface Presentation extends Event {
	@TypeValue(PresentedBy.LABEL)
	public static interface PresentedBy extends DEdgeFrame {
		public static final String LABEL = "PresentedBy";

		@OutVertex
		public Attendee getPresenter();

		@InVertex
		public Presentation getSession();
	}

	@TypedProperty("SessionID")
	public String getSessionId();

	@TypedProperty("SessionID")
	public void setSessionId(String sessionid);

	@TypedProperty("Level")
	public String getLevel();

	@TypedProperty("Level")
	public void setLevel(String level);

	@AdjacencyUnique(label = PresentedBy.LABEL, direction = Direction.IN)
	public Iterable<Attendee> getPresentingAttendees();

	@AdjacencyUnique(label = PresentedBy.LABEL, direction = Direction.IN)
	public PresentedBy addPresentedBy(Attendee presenter);

	@AdjacencyUnique(label = PresentedBy.LABEL, direction = Direction.IN)
	public void removePresentedBy(Attendee presenter);

	@IncidenceUnique(label = PresentedBy.LABEL, direction = Direction.IN)
	public Iterable<PresentedBy> getPresentings();

	@IncidenceUnique(label = PresentedBy.LABEL, direction = Direction.IN)
	public void removePresentedBy(PresentedBy presentedBy);

	@AdjacencyUnique(label = Includes.LABEL)
	public Iterable<Track> getIncludedInTracks();

	@AdjacencyUnique(label = Includes.LABEL)
	public Includes addIncludedIn(Track track);

	@AdjacencyUnique(label = Includes.LABEL)
	public void removeIncludedIn(Track track);

	@IncidenceUnique(label = Includes.LABEL)
	public Iterable<Includes> getIncludedIn();

	@IncidenceUnique(label = Includes.LABEL)
	public void removeIncludedIn(Includes includes);

}
