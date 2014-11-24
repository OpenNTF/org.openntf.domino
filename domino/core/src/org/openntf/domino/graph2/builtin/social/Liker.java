package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

public interface Liker extends DVertexFrame {
	@IncidenceUnique(label = Likes.LABEL_LIKEABLE)
	public Iterable<Likes> getLikes();

	@IncidenceUnique(label = Likes.LABEL_LIKEABLE)
	public Likes addLikes(Likeable likeable);

	@IncidenceUnique(label = Likes.LABEL_LIKEABLE)
	public void removeLikes(Likeable likeable);

	@AdjacencyUnique(label = Likes.LABEL_LIKEABLE)
	public Iterable<Likeable> getLikesLikeables();

	@AdjacencyUnique(label = Likes.LABEL_LIKEABLE)
	public Likeable addLikesLikeable(Likeable likeable);

	@AdjacencyUnique(label = Likes.LABEL_LIKEABLE)
	public void removeLikesLikeable(Likeable likeable);

}
