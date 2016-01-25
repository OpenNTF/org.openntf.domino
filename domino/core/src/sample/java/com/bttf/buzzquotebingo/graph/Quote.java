package com.bttf.buzzquotebingo.graph;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.bttf.buzzquotebingo.graph.FilmCharacter.QuotedBy;
import com.bttf.buzzquotebingo.graph.FilmCharacter.SaidTo;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.OutVertex;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Quote")
public interface Quote extends DVertexFrame {

	@TypeValue(UsedIn.LABEL)
	public static interface UsedIn extends DEdgeFrame {
		public static String LABEL = "UsedIn";

		@OutVertex
		public Section getSection();

		@InVertex
		public Quote getQuote();
	}

	@TypedProperty("summary")
	public String getSummary();

	@TypedProperty("summary")
	public void setSummary(String summary);

	@TypedProperty("quote")
	public String getQuote();

	@TypedProperty("quote")
	public void setQuote(String quote);

	@TypedProperty("film")
	public String getFilm();

	// TODO: Store as enum
	@TypedProperty("film")
	public void setFilm(String film);

	@TypedProperty("saidByPresenter")
	public boolean isSaidByPresenter();

	@TypedProperty("saidByPresenter")
	public void setSaidByPresenter(boolean saidByPresenter);

	@AdjacencyUnique(label = UsedIn.LABEL)
	public UsedIn addUsedIn(Section section);

	@AdjacencyUnique(label = UsedIn.LABEL)
	public void removeUsedIn(Section section);

	@AdjacencyUnique(label = UsedIn.LABEL)
	public Iterable<Section> getSection();

	@AdjacencyUnique(label = QuotedBy.LABEL)
	public QuotedBy addQuotedBy(FilmCharacter character);

	@AdjacencyUnique(label = QuotedBy.LABEL)
	public void removeQuotedBy(FilmCharacter character);

	@AdjacencyUnique(label = QuotedBy.LABEL)
	public Iterable<FilmCharacter> getQuotedBy();

	@AdjacencyUnique(label = SaidTo.LABEL)
	public SaidTo addSaidTo(FilmCharacter character);

	@AdjacencyUnique(label = SaidTo.LABEL)
	public void removeSaidTo(FilmCharacter character);

	@AdjacencyUnique(label = SaidTo.LABEL)
	public Iterable<FilmCharacter> getSaidTo();
}
