package org.openntf.domino.graph2.builtin.workflow.definition;

import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface StartsWith extends DEdgeFrame {
	public final static String LABEL_STARTSWITH = "startsWith";

	@OutVertex
	public FlowDefinition getFlowDefinition();

	@InVertex
	public TaskDefinition getTaskDefinition();
}
