package org.openntf.domino.graph2.builtin.social;

import java.util.List;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Comment")
public interface Comment extends Likeable {
	@TypedProperty("Body")
	public String getBody();

	@TypedProperty("Body")
	public void setBody(String body);

	@AdjacencyUnique(label = CommentsOn.LABEL, direction = Direction.IN)
	public Commenter getCommenter();

	@AdjacencyUnique(label = CommentsOn.LABEL, direction = Direction.IN)
	public CommentsOn addCommenter(Commenter commenter);

	@AdjacencyUnique(label = CommentsOn.LABEL, direction = Direction.IN)
	public void removeCommenter(Commenter commenter);

	@IncidenceUnique(label = CommentsOn.LABEL, direction = Direction.IN)
	public CommentsOn getCommentsOn();

	@IncidenceUnique(label = CommentsOn.LABEL, direction = Direction.IN)
	public int countCommentsOn();

	@IncidenceUnique(label = CommentsOn.LABEL, direction = Direction.IN)
	public void removeCommentsOn(CommentsOn commentsOn);

	@AdjacencyUnique(label = CommentsAbout.LABEL)
	public List<Commentable> getCommentables();

	@AdjacencyUnique(label = CommentsAbout.LABEL)
	public CommentsAbout addCommentable(Commentable commentable);

	@AdjacencyUnique(label = CommentsAbout.LABEL)
	public void removeCommentable(Commentable commentable);

	@IncidenceUnique(label = CommentsAbout.LABEL)
	public List<CommentsAbout> getCommentsAbouts();

	@IncidenceUnique(label = CommentsAbout.LABEL)
	public int countCommentsAbouts();

	@IncidenceUnique(label = CommentsAbout.LABEL)
	public void removeCommentsAbout(CommentsAbout commentsAbout);

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public List<Socializer> getSocializers();

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public Mentions addSocializer(Socializer socializer);

	@AdjacencyUnique(label = Mentions.LABEL, direction = Direction.IN)
	public void removeSocializer(Socializer socializer);

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public List<Mentions> getMentions();

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public int countMentions();

	@IncidenceUnique(label = Mentions.LABEL, direction = Direction.IN)
	public void removeMentions(Mentions mentions);

}
