package org.openntf.domino.graph2.builtin.workflow;

import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface Assigns extends DEdgeFrame {
	public static final String LABEL_ASSIGNS = "assigns";

	@OutVertex
	public Flower getFlower();

	@InVertex
	public Task getTask();

}
