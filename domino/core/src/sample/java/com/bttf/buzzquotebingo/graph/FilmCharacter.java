package com.bttf.buzzquotebingo.graph;

import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("FilmCharacter")
public interface FilmCharacter extends DVertexFrame {

	@TypeValue(QuotedBy.LABEL)
	public static interface QuotedBy extends DEdgeFrame {
		public static final String LABEL = "QuotedBy";

		@OutVertex
		public FilmCharacter getCharacter();

		@InVertex
		public Quote getQuote();
	}

	@TypeValue(SaidTo.LABEL)
	public static interface SaidTo extends DEdgeFrame {
		public static final String LABEL = "SaidTo";

		@OutVertex
		public FilmCharacter getCharacter();

		@InVertex
		public Quote getQuote();
	}

	@TypedProperty("name")
	public String getName();

	@TypedProperty("name")
	public void setName(String name);

}
