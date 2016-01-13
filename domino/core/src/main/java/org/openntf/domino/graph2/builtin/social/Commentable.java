package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Commentable")
public interface Commentable extends DVertexFrame {
	public static enum CommentType {
		GENERAL, QUESTION, FEEDBACK;
	}

	@TypedProperty("CommentType")
	public CommentType getCommentType();

	@TypedProperty("CommentType")
	public void setCommentType(CommentType type);

	@IncidenceUnique(label = CommentsAbout.LABEL, direction = Direction.IN)
	public Iterable<CommentsAbout> getCommentsAbout();

	@IncidenceUnique(label = CommentsAbout.LABEL, direction = Direction.IN)
	public CommentsAbout addCommentsAbout(Comment comment);

	@IncidenceUnique(label = CommentsAbout.LABEL, direction = Direction.IN)
	public void removeCommentsAbout(Comment comment);

	@AdjacencyUnique(label = CommentsAbout.LABEL, direction = Direction.IN)
	public Iterable<Comment> getComments();

	@AdjacencyUnique(label = CommentsAbout.LABEL, direction = Direction.IN)
	public Comment addComment(Comment comment);

	@AdjacencyUnique(label = CommentsAbout.LABEL, direction = Direction.IN)
	public void removeComment(Comment comment);

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Iterable<Mentions> getMentions();

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Mentions addMentions(Socializer mentioned);

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public void removeMentions(Socializer mentioned);

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Iterable<Socializer> getMentioned();

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Socializer addMentioned(Socializer mentioned);

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public void removeMentioned(Socializer mentioned);

}
