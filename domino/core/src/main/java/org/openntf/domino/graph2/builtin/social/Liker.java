package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Liker")
public interface Liker extends DVertexFrame {
	@IncidenceUnique(label = Likes.LABEL)
	public Iterable<Likes> getLikes();

	@IncidenceUnique(label = Likes.LABEL)
	public Likes addLikes(Likeable likeable);

	@IncidenceUnique(label = Likes.LABEL)
	public void removeLikes(Likeable likeable);

	@AdjacencyUnique(label = Likes.LABEL)
	public Iterable<Likeable> getLikesLikeables();

	@AdjacencyUnique(label = Likes.LABEL)
	public Likeable addLikesLikeable(Likeable likeable);

	@AdjacencyUnique(label = Likes.LABEL)
	public void removeLikesLikeable(Likeable likeable);

}
