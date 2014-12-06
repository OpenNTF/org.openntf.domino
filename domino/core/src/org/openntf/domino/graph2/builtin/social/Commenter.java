package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

public interface Commenter extends DVertexFrame {
	@IncidenceUnique(label = CommentsOn.LABEL_COMMENTSON)
	public Iterable<CommentsOn> getCommentsOn();

	@IncidenceUnique(label = CommentsOn.LABEL_COMMENTSON)
	public CommentsOn addCommentsOn(Comment comment);

	@IncidenceUnique(label = CommentsOn.LABEL_COMMENTSON)
	public void removeCommentsOn(Comment comment);

	@AdjacencyUnique(label = CommentsOn.LABEL_COMMENTSON)
	public Iterable<Comment> getComments();

	@AdjacencyUnique(label = CommentsOn.LABEL_COMMENTSON)
	public Comment addComment(Comment comment);

	@AdjacencyUnique(label = CommentsOn.LABEL_COMMENTSON)
	public void removeComment(Comment comment);

}
