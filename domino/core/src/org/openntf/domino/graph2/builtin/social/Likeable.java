package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.User;

import com.tinkerpop.blueprints.Direction;

public interface Likeable extends DVertexFrame {

	@IncidenceUnique(label = Likes.LABEL_LIKEABLE, direction = Direction.IN)
	public Iterable<Likes> getLikedBy();

	@IncidenceUnique(label = Likes.LABEL_LIKEABLE, direction = Direction.IN)
	public Likes addLikedBy(Liker user);

	@IncidenceUnique(label = Likes.LABEL_LIKEABLE, direction = Direction.IN)
	public Likes findLikedBy(Liker user);

	@IncidenceUnique(label = Likes.LABEL_LIKEABLE, direction = Direction.IN)
	public void removeLikedBy(User user);

	@AdjacencyUnique(label = Likes.LABEL_LIKEABLE, direction = Direction.IN)
	public Iterable<User> getLikedByUsers();

	@AdjacencyUnique(label = Likes.LABEL_LIKEABLE, direction = Direction.IN)
	public User addLikedByUser(User user);
}
