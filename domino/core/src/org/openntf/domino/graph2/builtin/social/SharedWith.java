package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface SharedWith extends DEdgeFrame {
	public static final String LABEL = "sharedWith";

	@InVertex
	public Sharer getSharer();

	@OutVertex
	public Share getShare();
}
