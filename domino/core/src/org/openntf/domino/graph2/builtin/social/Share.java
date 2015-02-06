package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Share")
public interface Share extends Likeable {
	@TypedProperty("Body")
	public String getBody();

	@TypedProperty("Body")
	public void setBody(String body);

	@TypedProperty("FollowUpDate")
	public String getFollowUpDate();

	@TypedProperty("FollowUpDate")
	public void setFollowUpDate(String date);

	@IncidenceUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public SharedBy getSharedBy();

	@IncidenceUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public SharedBy addSharedBy(Sharer sharer);

	@IncidenceUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public void removeSharedBy(Sharer sharer);

	@AdjacencyUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public Sharer getSharer();

	@AdjacencyUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public Sharer addSharer(Sharer sharer);

	@AdjacencyUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public void removeSharer(Sharer sharer);

	@IncidenceUnique(label = SharedWith.LABEL)
	public SharedWith getSharedWith();

	@IncidenceUnique(label = SharedWith.LABEL)
	public SharedWith addSharedWith(Sharer sharer);

	@IncidenceUnique(label = SharedWith.LABEL)
	public void removeSharedWith(Sharer sharer);

	@AdjacencyUnique(label = SharedWith.LABEL)
	public Sharer getSharedWithSharer();

	@AdjacencyUnique(label = SharedWith.LABEL)
	public Sharer addSharedWithSharer(Sharer sharer);

	@AdjacencyUnique(label = SharedWith.LABEL)
	public void removeSharedWithSharer(Sharer sharer);
}
