package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;

public interface Commentable extends DVertexFrame {
	@IncidenceUnique(label = CommentsAbout.LABEL_COMMENTSABOUT, direction = Direction.IN)
	public Iterable<CommentsAbout> getCommentsAbout();

	@IncidenceUnique(label = CommentsAbout.LABEL_COMMENTSABOUT, direction = Direction.IN)
	public CommentsAbout addCommentsAbout(Comment comment);

	@IncidenceUnique(label = CommentsAbout.LABEL_COMMENTSABOUT, direction = Direction.IN)
	public void removeCommentsAbout(Comment comment);

	@AdjacencyUnique(label = CommentsAbout.LABEL_COMMENTSABOUT, direction = Direction.IN)
	public Iterable<Comment> getComments();

	@AdjacencyUnique(label = CommentsAbout.LABEL_COMMENTSABOUT, direction = Direction.IN)
	public Comment addComment(Comment comment);

	@AdjacencyUnique(label = CommentsAbout.LABEL_COMMENTSABOUT, direction = Direction.IN)
	public void removeComment(Comment comment);
}
