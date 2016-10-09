package org.openntf.domino.graph2.builtin.workflow.definition;

import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.workflow.Task;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface Defines extends DEdgeFrame {
	public final static String LABEL_FLOWABLEDEFINES = "FlowDefines";

	@OutVertex
	public TaskDefinition getTaskDefinition();

	@InVertex
	public Task getTask();

}
