package org.openntf.domino.graph2.builtin.workflow;

import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;

public interface Performs extends DEdgeFrame {
	public static final String LABEL_PERFORMS = "performs";

	@TypedProperty("Outcome")
	public String getOutcome();

	@TypedProperty("Outcome")
	public void setOutcome(String outcome);

	@OutVertex
	public Flower getFlower();

	@InVertex
	public Task getTask();

}
