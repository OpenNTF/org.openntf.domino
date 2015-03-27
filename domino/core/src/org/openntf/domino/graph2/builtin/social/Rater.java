package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Rater")
public interface Rater extends DVertexFrame {
	@IncidenceUnique(label = Rates.LABEL)
	public Iterable<Rates> getRates();

	@IncidenceUnique(label = Rates.LABEL)
	public Rates addRates(Rateable rateable);

	@IncidenceUnique(label = Rates.LABEL)
	public void removeRates(Rateable rateable);

	@AdjacencyUnique(label = Rates.LABEL)
	public Iterable<Rateable> getRateables();

	@AdjacencyUnique(label = Rates.LABEL)
	public Rateable addRateable(Rateable rateable);

	@AdjacencyUnique(label = Rates.LABEL)
	public void removeRateable(Rateable rateable);
}
