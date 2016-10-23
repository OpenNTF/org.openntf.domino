package org.openntf.domino.graph2.builtin.workflow.definition;

import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface FollowedBy extends DEdgeFrame {
	public final static String LABEL_FLOWFOLLOWEDBY = "FlowFollowedBy";

	@TypedProperty("NeededOutcome")
	public String getNeededOutcome();

	@TypedProperty("NeededOutcome")
	public void setNeededOutcome(String outcome);

	@OutVertex
	public TaskDefinition getSourceDefinition();

	@InVertex
	public TaskDefinition getTargetDefinition();

}
