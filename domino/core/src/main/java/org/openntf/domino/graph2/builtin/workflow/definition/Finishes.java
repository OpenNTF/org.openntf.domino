package org.openntf.domino.graph2.builtin.workflow.definition;

import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface Finishes extends DEdgeFrame {
	public final static String LABEL_FINISHES = "finishes";

	@OutVertex
	public TaskDefinition getTaskDefinition();

	@InVertex
	public FlowDefinition getFlowDefinition();
}
