package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue(ShareAbout.LABEL)
public interface ShareAbout extends DEdgeFrame {
	public static final String LABEL = "shareAbout";

	@OutVertex
	public Share getShare();

	@InVertex
	public Shareable getShareable();
}
