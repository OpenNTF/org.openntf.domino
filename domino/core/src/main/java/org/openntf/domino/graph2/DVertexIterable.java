package org.openntf.domino.graph2;

import java.util.Iterator;
import java.util.List;

import com.tinkerpop.blueprints.Vertex;

public interface DVertexIterable extends DElementIterable, List<Vertex> {
	public interface DVertexIterator extends DElementIterator, Iterator<Vertex> {
		@Override
		public Vertex next();
	}

	@Override
	public Iterator<Vertex> iterator();
}
