package org.openntf.domino.graph2.builtin.workflow;

import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface AssignedTo extends DEdgeFrame {
	public static final String LABEL_ASSIGNEDTO = "assignedTo";

	@OutVertex
	public Task getTask();

	@InVertex
	public Flower getFlower();
}
