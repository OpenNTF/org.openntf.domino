package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.structures.FramedEdgeIterable;

public class FramedEdgeList<T extends EdgeFrame> extends FramedEdgeIterable<T> implements List<T> {
	public static class FramedListIterator<T> implements ListIterator<T> {
		protected final Class<T> kind_;
		//		    protected final Direction direction_;
		protected final ListIterator<Edge> iterator_;
		protected final FramedGraph<? extends Graph> framedGraph_;

		public FramedListIterator(final FramedGraph<? extends Graph> graph, final ListIterator<Edge> iterator, final Class<T> kind/*, Direction direction*/) {
			kind_ = kind;
			//		    	direction_ = direction;
			iterator_ = iterator;
			framedGraph_ = graph;
		}

		@Override
		public void add(final Object arg0) {
			if (arg0 == null)
				return;
			if (kind_.isAssignableFrom(arg0.getClass())) {
				iterator_.add(((EdgeFrame) kind_.cast(arg0)).asEdge());
			} else if (arg0 instanceof Edge) {
				iterator_.add((Edge) arg0);
			} else {
				throw new IllegalArgumentException("Cannot add an object of type " + arg0.getClass().getName() + " to an iterator of "
						+ kind_.getName());
			}
		}

		@Override
		public boolean hasNext() {
			return iterator_.hasNext();
		}

		@Override
		public boolean hasPrevious() {
			return iterator_.hasPrevious();
		}

		@Override
		public T next() {
			return framedGraph_.frame(iterator_.next(), kind_);
		}

		@Override
		public int nextIndex() {
			return iterator_.nextIndex();
		}

		@Override
		public T previous() {
			return framedGraph_.frame(iterator_.previous(), kind_);
		}

		@Override
		public int previousIndex() {
			return iterator_.previousIndex();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(final Object arg0) {
			if (arg0 instanceof Edge) {
				iterator_.set((Edge) arg0);
			} else if (kind_.isAssignableFrom(arg0.getClass())) {
				iterator_.set(((EdgeFrame) kind_.cast(arg0)).asEdge());
			} else {
				throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of "
						+ kind_.getName());
			}
		}

	}

	protected List<Edge> list_;

	public FramedEdgeList(final FramedGraph<? extends Graph> framedGraph, final Iterable<Edge> list, final Class<T> kind) {
		super(framedGraph, list, kind);
		if (list instanceof List) {
			list_ = (List<Edge>) list;
		} else {
			list_ = new ArrayList<Edge>();
			for (Edge e : list) {
				list_.add(e);
			}
		}
	}

	@Override
	public boolean add(final T arg0) {
		return list_.add(arg0.asEdge());
	}

	@Override
	public void add(final int arg0, final T arg1) {
		list_.add(arg0, arg1.asEdge());
	}

	protected static Collection<Edge> convertToEdges(final Collection<?> arg0) {
		List<Edge> result = new ArrayList<Edge>(arg0.size());
		for (Object raw : arg0) {
			if (raw instanceof Edge) {
				result.add((Edge) raw);
			} else if (raw instanceof EdgeFrame) {
				result.add(((EdgeFrame) raw).asEdge());
			} else {
				throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName()
						+ " to an EdgeFrame iterator");
			}
		}
		return result;
	}

	@Override
	public boolean addAll(final Collection<? extends T> arg0) {
		return list_.addAll(convertToEdges(arg0));
	}

	@Override
	public boolean addAll(final int arg0, final Collection<? extends T> arg1) {
		return list_.addAll(arg0, convertToEdges(arg1));
	}

	@Override
	public void clear() {
		list_.clear();
	}

	@Override
	public boolean contains(final Object arg0) {
		if (arg0 instanceof Edge) {
			return list_.contains(arg0);
		} else if (kind.isAssignableFrom(arg0.getClass())) {
			return list_.contains(((EdgeFrame) kind.cast(arg0)).asEdge());
		} else {
			throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of "
					+ kind.getName());
		}
	}

	@Override
	public boolean containsAll(final Collection<?> arg0) {
		return list_.containsAll(convertToEdges(arg0));
	}

	@Override
	public T get(final int arg0) {
		return framedGraph.frame(list_.get(arg0), kind);
	}

	@Override
	public int indexOf(final Object arg0) {
		if (arg0 instanceof Edge) {
			return list_.indexOf(arg0);
		} else if (kind.isAssignableFrom(arg0.getClass())) {
			return list_.indexOf(((EdgeFrame) kind.cast(arg0)).asEdge());
		} else {
			throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of "
					+ kind.getName());
		}
	}

	@Override
	public boolean isEmpty() {
		return list_.isEmpty();
	}

	@Override
	public int lastIndexOf(final Object arg0) {
		if (arg0 instanceof Edge) {
			return list_.lastIndexOf(arg0);
		} else if (kind.isAssignableFrom(arg0.getClass())) {
			return list_.lastIndexOf(((EdgeFrame) kind.cast(arg0)).asEdge());
		} else {
			throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of "
					+ kind.getName());
		}
	}

	@Override
	public ListIterator<T> listIterator() {
		return new FramedListIterator<T>(framedGraph, list_.listIterator(), kind);
	}

	@Override
	public ListIterator<T> listIterator(final int arg0) {
		return new FramedListIterator<T>(framedGraph, list_.listIterator(arg0), kind);
	}

	@Override
	public Iterator<T> iterator() {
		return new FramedListIterator<T>(framedGraph, list_.listIterator(), kind);
	}

	@Override
	public T remove(final int arg0) {
		Edge e = list_.remove(arg0);
		return framedGraph.frame(e, kind);
	}

	@Override
	public boolean remove(final Object arg0) {
		if (arg0 instanceof Edge) {
			return list_.remove(arg0);
		} else if (kind.isAssignableFrom(arg0.getClass())) {
			return list_.remove(((EdgeFrame) kind.cast(arg0)).asEdge());
		} else {
			throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of "
					+ kind.getName());
		}
	}

	@Override
	public boolean removeAll(final Collection<?> arg0) {
		return list_.removeAll(convertToEdges(arg0));
	}

	@Override
	public boolean retainAll(final Collection<?> arg0) {
		return list_.retainAll(convertToEdges(arg0));
	}

	@Override
	public T set(final int arg0, final T arg1) {
		Edge edge = arg1.asEdge();
		list_.set(arg0, edge);
		return arg1;
	}

	@Override
	public int size() {
		return list_.size();
	}

	@Override
	public List<T> subList(final int arg0, final int arg1) {
		List<Edge> sublist = list_.subList(arg0, arg1);
		return new FramedEdgeList<T>(framedGraph, sublist, kind);
	}

	@Override
	public Object[] toArray() {
		int size = list_.size();
		Object[] result = new Object[size];
		for (int i = 0; i < size; i++) {
			Edge e = list_.get(i);
			result[i] = framedGraph.frame(e, kind);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <U> U[] toArray(final U[] arg0) {
		int size = list_.size();
		Class c = arg0.getClass().getComponentType();
		U[] result = (U[]) Array.newInstance(c, size);
		for (int i = 0; i < size; i++) {
			Edge e = list_.get(i);
			result[i] = (U) framedGraph.frame(e, kind);
		}
		return result;
	}
}
