package org.openntf.conference.graph;

import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;

public interface Track extends DVertexFrame {
	public static interface Includes extends DEdgeFrame {
		public Track getTrack();

		public Session getSession();
	}

	public String getTitle();

	public void setTitle(String title);

	public String getDescription();

	public void setDescription(String description);

	public Iterable<Includes> getIncludes();

	public Iterable<Session> getIncludesSessions();

}
