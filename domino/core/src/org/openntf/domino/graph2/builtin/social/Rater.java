package org.openntf.domino.graph2.builtin.social;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

public interface Rater extends DVertexFrame {
	@IncidenceUnique(label = Rates.LABEL_RATES)
	public Iterable<Rates> getRates();

	@IncidenceUnique(label = Rates.LABEL_RATES)
	public Rates addRates(Rateable rateable);

	@IncidenceUnique(label = Rates.LABEL_RATES)
	public void removeRates(Rateable rateable);

	@AdjacencyUnique(label = Rates.LABEL_RATES)
	public Iterable<Rateable> getRateables();

	@AdjacencyUnique(label = Rates.LABEL_RATES)
	public Rateable addRateable(Rateable rateable);

	@AdjacencyUnique(label = Rates.LABEL_RATES)
	public void removeRateable(Rateable rateable);
}
