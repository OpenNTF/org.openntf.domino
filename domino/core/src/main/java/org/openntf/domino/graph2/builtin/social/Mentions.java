package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue(Mentions.LABEL)
public interface Mentions extends DEdgeFrame {
	public static final String LABEL = "mentions";

	@OutVertex
	public Socializer getWhoMentioned();

	@InVertex
	public DVertexFrame getMentionedOn();
}
