package org.openntf.domino.graph2;

import java.util.Iterator;
import java.util.List;

import com.tinkerpop.blueprints.Edge;

public interface DEdgeIterable extends DElementIterable, List<Edge> {
	public interface DEdgeIterator extends DElementIterator, Iterator<Edge> {
		@Override
		public Edge next();
	}

	@Override
	public Iterator<Edge> iterator();
}
