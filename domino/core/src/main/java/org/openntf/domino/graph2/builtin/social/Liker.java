package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Liker")
public interface Liker extends DVertexFrame {
	@AdjacencyUnique(label = Likes.LABEL)
	public Iterable<Likeable> getLikeables();

	@AdjacencyUnique(label = Likes.LABEL)
	public Likes addLikeable(Likeable likeable);

	@AdjacencyUnique(label = Likes.LABEL)
	public void removeLikeable(Likeable likeable);

	@IncidenceUnique(label = Likes.LABEL)
	public Iterable<Likes> getLikes();

	@IncidenceUnique(label = Likes.LABEL)
	public int countLikes();

	@IncidenceUnique(label = Likes.LABEL)
	public void removeLikes(Likes likes);

}
