package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.User;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Likable")
public interface Likeable extends DVertexFrame {

	@IncidenceUnique(label = Likes.LABEL, direction = Direction.IN)
	public Iterable<Likes> getLikedBy();

	@IncidenceUnique(label = Likes.LABEL, direction = Direction.IN)
	public Likes addLikedBy(Liker user);

	@IncidenceUnique(label = Likes.LABEL, direction = Direction.IN)
	public Likes findLikedBy(Liker user);

	@IncidenceUnique(label = Likes.LABEL, direction = Direction.IN)
	public void removeLikedBy(User user);

	@AdjacencyUnique(label = Likes.LABEL, direction = Direction.IN)
	public Iterable<User> getLikedByUsers();

	@AdjacencyUnique(label = Likes.LABEL, direction = Direction.IN)
	public User addLikedByUser(User user);
}
