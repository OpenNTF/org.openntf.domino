package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Sharer")
public interface Sharer extends DVertexFrame {
	@IncidenceUnique(label = SharedBy.LABEL)
	public Iterable<SharedBy> getSharedBy();

	@IncidenceUnique(label = SharedBy.LABEL)
	public SharedBy addSharedBy(Share share);

	@IncidenceUnique(label = SharedBy.LABEL)
	public void removeSharedBy(Share share);

	@AdjacencyUnique(label = SharedBy.LABEL)
	public Iterable<Share> getShares();

	@AdjacencyUnique(label = SharedBy.LABEL)
	public Share addShare(Share share);

	@AdjacencyUnique(label = SharedBy.LABEL)
	public void removeShare(Share share);

	@IncidenceUnique(label = SharedWith.LABEL, direction = Direction.IN)
	public Iterable<SharedWith> getSharedWith();

	@IncidenceUnique(label = SharedWith.LABEL, direction = Direction.IN)
	public SharedBy addSharedWith(Share share);

	@IncidenceUnique(label = SharedWith.LABEL, direction = Direction.IN)
	public void removeSharedWith(Share share);

	@AdjacencyUnique(label = SharedWith.LABEL, direction = Direction.IN)
	public Iterable<Share> getSharedWithShares();

	@AdjacencyUnique(label = SharedWith.LABEL, direction = Direction.IN)
	public Share addSharedWithShare(Share share);

	@AdjacencyUnique(label = SharedWith.LABEL, direction = Direction.IN)
	public void removeSharedWithShare(Share share);
}
