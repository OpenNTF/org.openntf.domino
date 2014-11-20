package org.openntf.domino.graph2.builtin.social;

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

	@IncidenceUnique(label = CommentsOn.LABEL_COMMENTSON, direction = Direction.IN)
	public CommentsOn getCommentsOn();

	@IncidenceUnique(label = CommentsOn.LABEL_COMMENTSON, direction = Direction.IN)
	public CommentsOn addCommentsOn(Commenter comment);

	@IncidenceUnique(label = CommentsOn.LABEL_COMMENTSON, direction = Direction.IN)
	public void removeCommentsOn(Commenter comment);

	@AdjacencyUnique(label = CommentsOn.LABEL_COMMENTSON, direction = Direction.IN)
	public Commenter getCommenter();

	@AdjacencyUnique(label = CommentsOn.LABEL_COMMENTSON, direction = Direction.IN)
	public Commenter addCommenter(Commenter commenter);

	@AdjacencyUnique(label = CommentsOn.LABEL_COMMENTSON, direction = Direction.IN)
	public void removeCommenter(Commenter commenter);

	@IncidenceUnique(label = CommentsAbout.LABEL_COMMENTSABOUT)
	public CommentsAbout getCommentsAbout();

	@IncidenceUnique(label = CommentsAbout.LABEL_COMMENTSABOUT)
	public CommentsAbout addCommentsAbout(Commentable commentable);

	@IncidenceUnique(label = CommentsAbout.LABEL_COMMENTSABOUT)
	public void removeCommentsAbout(Commentable commentable);

	@AdjacencyUnique(label = CommentsAbout.LABEL_COMMENTSABOUT)
	public Iterable<Commentable> getCommentable();

	@AdjacencyUnique(label = CommentsAbout.LABEL_COMMENTSABOUT)
	public Commentable addCommentable(Commentable commentable);

	@AdjacencyUnique(label = CommentsAbout.LABEL_COMMENTSABOUT)
	public void removeCommentable(Commentable commentable);

}
