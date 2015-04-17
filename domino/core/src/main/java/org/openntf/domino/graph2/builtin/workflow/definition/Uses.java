package org.openntf.domino.graph2.builtin.workflow.definition;

import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.workflow.Flowable;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface Uses extends DEdgeFrame {
	public final static String LABEL_FLOWABLEUSES = "FlowUses";

	@OutVertex
	public Flowable getFlowable();

	@InVertex
	public FlowDefinition getFlowDefinition();

}
