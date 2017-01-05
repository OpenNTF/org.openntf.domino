package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue(CommentsAbout.LABEL)
public interface CommentsAbout extends DEdgeFrame {
	public static final String LABEL = "commentsAbout";

	@OutVertex
	public Comment getComment();

	@InVertex
	public Commentable getCommentable();

}
