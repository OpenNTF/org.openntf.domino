package org.openntf.domino.graph2.builtin.workflow;

import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface Requires extends DEdgeFrame {
	public static final String LABEL_REQUIRES = "requires";

	@OutVertex
	public Task getTask();

	@InVertex
	public Flowable getFlowable();
}
