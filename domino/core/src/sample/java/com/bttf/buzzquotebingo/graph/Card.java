package com.bttf.buzzquotebingo.graph;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Card")
public interface Card extends DVertexFrame {

	@TypeValue(HasQuote.LABEL)
	public interface HasQuote extends DEdgeFrame {
		public static String LABEL = "HasQuote";

		@TypedProperty("heardByAttendee")
		public Boolean isHeardByAttendee();

		@TypedProperty("heardByAttendee")
		public void setHeardByAttendee(Boolean heard);

		@OutVertex
		public Quote getQuote();

		@InVertex
		public Card getCard();
	}

	@TypedProperty("name")
	public String getName();

	@TypedProperty("name")
	public void setName(String name);

	@TypedProperty("winner")
	public boolean isWinner();

	@TypedProperty("winner")
	public void setWinner(boolean isWinner);

	@IncidenceUnique(label = HasQuote.LABEL)
	public HasQuote addQuote(Quote quote);

	@IncidenceUnique(label = HasQuote.LABEL)
	public void removeQuote(Quote quote);

	@AdjacencyUnique(label = HasQuote.LABEL)
	public Iterable<Quote> getQuotes();

	@IncidenceUnique(label = HasQuote.LABEL)
	public HasQuote getHasQuoteEdges(Quote quote);

}
