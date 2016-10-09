package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Commenter")
public interface Commenter extends DVertexFrame {
	@AdjacencyUnique(label = CommentsOn.LABEL)
	public Iterable<Comment> getComments();

	@AdjacencyUnique(label = CommentsOn.LABEL)
	public CommentsOn addComment(Comment comment);

	@AdjacencyUnique(label = CommentsOn.LABEL)
	public void removeComment(Comment comment);

	@IncidenceUnique(label = CommentsOn.LABEL)
	public Iterable<CommentsOn> getCommentsOns();

	@IncidenceUnique(label = CommentsOn.LABEL)
	public int countCommentsOns();

	@IncidenceUnique(label = CommentsOn.LABEL)
	public void removeCommentsOn(CommentsOn commentsOn);

}
