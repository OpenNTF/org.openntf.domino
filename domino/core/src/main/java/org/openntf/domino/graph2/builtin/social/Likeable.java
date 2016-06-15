package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Likable")
public interface Likeable extends DVertexFrame {

	@AdjacencyUnique(label = Likes.LABEL, direction = Direction.IN)
	public Iterable<Liker> getLikers();

	@AdjacencyUnique(label = Likes.LABEL, direction = Direction.IN)
	public Likes addLiker(Liker liker);

	@AdjacencyUnique(label = Likes.LABEL, direction = Direction.IN)
	public void removeLiker(Liker liker);

	@IncidenceUnique(label = Likes.LABEL, direction = Direction.IN)
	public Iterable<Likes> getLikedBys();

	@IncidenceUnique(label = Likes.LABEL, direction = Direction.IN)
	public int countLikedBys();

	@IncidenceUnique(label = Likes.LABEL, direction = Direction.IN)
	public void removeLikedBy(Likes likes);

}
