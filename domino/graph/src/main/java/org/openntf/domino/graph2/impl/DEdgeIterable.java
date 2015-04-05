package org.openntf.domino.graph2.impl;

import java.util.Iterator;

import org.openntf.domino.big.NoteCoordinate;

import com.tinkerpop.blueprints.Edge;

public class DEdgeIterable implements Iterable<Edge> {
	public static class DEdgeIterator implements Iterator<Edge> {
		private final DElementStore elementStore_;
		private final Iterable<NoteCoordinate> index_;
		private Iterator<NoteCoordinate> iterator_;

		public DEdgeIterator(final DElementStore store, final Iterable<NoteCoordinate> index) {
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
		public Edge next() {
			Edge result = null;
			NoteCoordinate nc = getIterator().next();
			if (nc != null) {
				result = elementStore_.getEdge(nc);
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

	public DEdgeIterable(final DElementStore store, final Iterable<NoteCoordinate> index) {
		store_ = store;
		index_ = index;
	}

	@Override
	public Iterator<Edge> iterator() {
		return new DEdgeIterator(store_, index_);
	}
}
