package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface CommentsAbout extends DEdgeFrame {
	public static final String LABEL_COMMENTSABOUT = "commentsAbout";

	@OutVertex
	public Comment getComment();

	@InVertex
	public Commentable getCommentable();

}
