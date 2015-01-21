package org.openntf.domino.graph2.impl;

import java.util.Iterator;

import org.openntf.domino.big.NoteCoordinate;

import com.tinkerpop.blueprints.Vertex;

public class DVertexIterable implements Iterable<Vertex> {
	public static class DVertexIterator implements Iterator<Vertex> {
		private final DElementStore elementStore_;
		private final Iterable<NoteCoordinate> index_;
		private Iterator<NoteCoordinate> iterator_;

		public DVertexIterator(final DElementStore store, final Iterable<NoteCoordinate> index) {
			elementStore_ = store;
			index_ = index;
		}

		private Iterator<NoteCoordinate> getIterator() {
			if (iterator_ == null) {
				iterator_ = index_.iterator();
			}
			return iterator_;
		}

		@Override
		public boolean hasNext() {
			return getIterator().hasNext();
		}

		@Override
		public Vertex next() {
			Vertex result = null;
			NoteCoordinate nc = getIterator().next();
			if (nc != null) {
				result = elementStore_.getVertex(nc);
			}
			return result;
		}

		@Override
		public void remove() {
			getIterator().remove();
		}
	}

	private final Iterable<NoteCoordinate> index_;
	private final DElementStore store_;

	public DVertexIterable(final DElementStore store, final Iterable<NoteCoordinate> index) {
		store_ = store;
		index_ = index;
	}

	@Override
	public Iterator<Vertex> iterator() {
		return new DVertexIterator(store_, index_);
	}

}
