package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Commentable")
public interface Commentable extends DVertexFrame {
	@AdjacencyUnique(label = CommentsAbout.LABEL, direction = Direction.IN)
	public Iterable<Comment> getComments();

	@AdjacencyUnique(label = CommentsAbout.LABEL, direction = Direction.IN)
	public CommentsAbout addComment(Comment comment);

	@AdjacencyUnique(label = CommentsAbout.LABEL, direction = Direction.IN)
	public void removeComment(Comment comment);

	@IncidenceUnique(label = CommentsAbout.LABEL, direction = Direction.IN)
	public Iterable<CommentsAbout> getCommentsAbouts();

	@IncidenceUnique(label = CommentsAbout.LABEL, direction = Direction.IN)
	public int countCommentsAbouts();

	@IncidenceUnique(label = CommentsAbout.LABEL, direction = Direction.IN)
	public void removeCommentsAbout(CommentsAbout commentsAbout);

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Iterable<Socializer> getSocializers();

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Mentions addSocializer(Socializer socializer);

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public void removeSocializer(Socializer socializer);

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Iterable<Mentions> getMentions();

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public int countMentions();

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public void removeMentions(Mentions mentions);

}
