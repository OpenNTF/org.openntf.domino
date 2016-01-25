package com.bttf.buzzquotebingo.graph;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.bttf.buzzquotebingo.graph.Quote.UsedIn;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Section")
public interface Section extends DVertexFrame {

	@TypedProperty("Name")
	public String getName();

	@TypedProperty("Name")
	public void setName(String name);

	@AdjacencyUnique(label = UsedIn.LABEL, direction = Direction.IN)
	public Iterable<Quote> getQuotes();

}
