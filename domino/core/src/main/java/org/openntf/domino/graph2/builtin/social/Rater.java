package org.openntf.domino.graph2.builtin.social;

import java.util.List;

import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.frames.modules.typedgraph.TypeValue;

@TypeValue("Rater")
public interface Rater extends DVertexFrame {
	@AdjacencyUnique(label = Rates.LABEL)
	public List<Rateable> getRateables();

	@AdjacencyUnique(label = Rates.LABEL)
	public Rates addRateable(Rateable rateable);

	@AdjacencyUnique(label = Rates.LABEL)
	public void removeRateable(Rateable rateable);

	@IncidenceUnique(label = Rates.LABEL)
	public List<Rates> getRates();

	@IncidenceUnique(label = Rates.LABEL)
	public int countRates();

	@IncidenceUnique(label = Rates.LABEL)
	public void removeRates(Rates rates);

}
