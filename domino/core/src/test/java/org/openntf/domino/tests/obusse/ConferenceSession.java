package org.openntf.domino.tests.obusse;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("ConferenceSession")
public interface ConferenceSession extends DVertexFrame {
	@Property("title")
	public String getTitle();

	@Property("title")
	public void setTitle(String title);

	@AdjacencyUnique(label = "attends", direction = Direction.IN)
	public Iterable<Attendee> getAttendees();

	@AdjacencyUnique(label = "attends", direction = Direction.IN)
	public Attendee addAttendee(Attendee attendee);

	@AdjacencyUnique(label = "attends", direction = Direction.IN)
	public void removeAttendee(Attendee attendee);

	@AdjacencyUnique(label = "presents", direction = Direction.IN)
	public Presenter getPresenter();

	@AdjacencyUnique(label = "presents", direction = Direction.IN)
	public Presenter addPresenter(Presenter presenter);

	@AdjacencyUnique(label = "presents", direction = Direction.IN)
	public void removePresenter(Presenter presenter);

	@IncidenceUnique(label = "presents", direction = Direction.IN)
	public Presents getPresents();
}
