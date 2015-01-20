package org.openntf.conference.graph;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;

public interface Track extends DVertexFrame {
	public static interface Includes extends DEdgeFrame {
		public static final String LABEL = "Includes";

		public Track getTrack();

		public Session getSession();
	}

	@TypedProperty("Title")
	public String getTitle();

	@TypedProperty("Title")
	public void setTitle(String title);

	@TypedProperty("Description")
	public String getDescription();

	@TypedProperty("Description")
	public void setDescription(String description);

	@AdjacencyUnique(label = Includes.LABEL, direction = Direction.IN)
	public Iterable<Session> getIncludesSessions();

	@AdjacencyUnique(label = Includes.LABEL, direction = Direction.IN)
	public Includes addIncludesSession(Session session);

	@AdjacencyUnique(label = Includes.LABEL, direction = Direction.IN)
	public void removeIncludesSession(Session session);

	@IncidenceUnique(label = Includes.LABEL, direction = Direction.IN)
	public Iterable<Includes> getIncludes();

	@IncidenceUnique(label = Includes.LABEL, direction = Direction.IN)
	public void removeIncludes(Includes includes);

}
