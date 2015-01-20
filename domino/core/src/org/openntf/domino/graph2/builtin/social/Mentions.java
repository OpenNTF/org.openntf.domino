package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface Mentions extends DEdgeFrame {
	public static final String LABEL_MENTIONS = "mentions";

	@OutVertex
	public Socializer getWhoMentioned();

	@InVertex
	public DVertexFrame getMentionedOn();
}
