package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue(SharedBy.LABEL)
public interface SharedBy extends DEdgeFrame {
	public static final String LABEL = "sharedBy";

	@OutVertex
	public Sharer getSharer();

	@InVertex
	public Share getShare();
}
