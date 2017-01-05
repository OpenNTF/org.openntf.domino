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

	@AdjacencyUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public Iterable<Sharer> getSharers();

	@AdjacencyUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public SharedBy addSharer(Sharer sharer);

	@AdjacencyUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public void removeSharer(Sharer sharer);

	@IncidenceUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public Iterable<SharedBy> getSharedBys();

	@IncidenceUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public int countSharedBys();

	@IncidenceUnique(label = SharedBy.LABEL, direction = Direction.IN)
	public void removeSharedBy(SharedBy sharedBy);

	@AdjacencyUnique(label = SharedWith.LABEL)
	public Iterable<Sharer> getSharedWithSharers();

	@AdjacencyUnique(label = SharedWith.LABEL)
	public Sharer addSharedWithSharer(Sharer sharer);

	@AdjacencyUnique(label = SharedWith.LABEL)
	public void removeSharedWithSharer(Sharer sharer);

	@IncidenceUnique(label = SharedWith.LABEL)
	public Iterable<SharedWith> getSharedWiths();

	@IncidenceUnique(label = SharedWith.LABEL)
	public int countSharedWiths();

	@IncidenceUnique(label = SharedWith.LABEL)
	public void removeSharedWith(SharedWith sharedWith);

}
