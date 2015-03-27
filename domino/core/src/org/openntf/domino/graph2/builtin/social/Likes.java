package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue(Likes.LABEL)
public interface Likes extends DEdgeFrame {
	public static final String LABEL = "likes";

	@OutVertex
	Liker getLiker();

	@InVertex
	Likeable getLikable();

}
