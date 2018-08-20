package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.openntf.domino.graph2.DGraphUtils;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DVertex;
import org.openntf.domino.graph2.impl.DVertexList;
import org.openntf.domino.utils.TypeUtils;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.structures.FramedVertexIterable;

public class FramedVertexList<T extends VertexFrame> extends FramedVertexIterable<T> implements List<T> {
	public static class FramedListIterator<T> implements ListIterator<T> {
		protected final Class<T> kind_;
		//		    protected final Direction direction_;
		protected final ListIterator<Vertex> iterator_;
		protected final DFramedTransactionalGraph framedGraph_;

		public FramedListIterator(final DFramedTransactionalGraph graph, final ListIterator<Vertex> iterator, final Class<T> kind/*, Direction direction*/) {
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
				iterator_.add(((VertexFrame) kind_.cast(arg0)).asVertex());
			} else if (arg0 instanceof Edge) {
				iterator_.add((Vertex) arg0);
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
			T result = null;
			Vertex v = iterator_.next();
			while (v == null && iterator_.hasNext()) {
				v = iterator_.next();
			}
			if (v != null) {
				result = (T) framedGraph_.getElement(v.getId(), kind_);
			}
			return result;
		}

		@Override
		public int nextIndex() {
			return iterator_.nextIndex();
		}

		@Override
		public T previous() {
			T result = null;
			Vertex v = iterator_.previous();
			while (v == null && iterator_.hasPrevious()) {
				v = iterator_.previous();
			}
			if (v != null) {
				result = (T) framedGraph_.getElement(v.getId(), kind_);
			}
			return result;
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
			if (arg0 instanceof Vertex) {
				iterator_.set((Vertex) arg0);
			} else if (kind_.isAssignableFrom(arg0.getClass())) {
				iterator_.set(((VertexFrame) kind_.cast(arg0)).asVertex());
			} else {
				throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of "
						+ kind_.getName());
			}
		}

	}

	protected List<Vertex> list_;
	protected Vertex sourceVertex_;

	public Class<?> getKind() {
		return kind;
	}

	public FramedVertexList(final FramedGraph<? extends Graph> framedGraph, final Vertex sourceVertex, final Iterable<Vertex> list,
			final Class<T> kind) {
		super(framedGraph, list, kind);
		//		Throwable t = new Throwable();
		//		t.printStackTrace();
		sourceVertex_ = sourceVertex;
		if (list instanceof List) {
			list_ = (List<Vertex>) list;
		} else {
			list_ = new ArrayList<Vertex>();
			Iterator<Vertex> itty = list.iterator();
			while (itty.hasNext()) {
				Vertex v = null;
				try {
					v = itty.next();
				} catch (Exception e) {
					//do nothing
				}
				if (v != null) {
					list_.add(v);
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FramedVertexList<T> applyFilter(final String key, final Object value) {
		DVertexList vertList = new DVertexList((DVertex) sourceVertex_);
		if (this.size() > 0) {
			for (VertexFrame vertex : this) {
				try {
					if ("@type".equals(key)) {
						if (DGraphUtils.isType(vertex, TypeUtils.toString(value))) {
							vertList.add((DVertex) vertex.asVertex());
						}
					} else {
						Object vertexVal = DGraphUtils.getFramedProperty(getGraph(), vertex, key);
						if (value.equals(TypeUtils.toString(vertexVal))) {
							vertList.add((DVertex) vertex.asVertex());
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		FramedVertexList result = new FramedVertexList(getGraph(), sourceVertex_, vertList, this.kind);
		return result;
	}

	public DFramedTransactionalGraph<?> getGraph() {
		return (DFramedTransactionalGraph<?>) framedGraph;
	}

	@Override
	public boolean add(final T arg0) {
		return list_.add(arg0.asVertex());
	}

	@Override
	public void add(final int arg0, final T arg1) {
		list_.add(arg0, arg1.asVertex());
	}

	protected static List<Vertex> convertToVertexes(final Collection<?> arg0) {
		List<Vertex> result = new ArrayList<Vertex>(arg0.size());
		for (Object raw : arg0) {
			if (raw instanceof Vertex) {
				result.add((Vertex) raw);
			} else if (raw instanceof VertexFrame) {
				result.add(((VertexFrame) raw).asVertex());
			} else {
				throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName()
						+ " to an EdgeFrame iterator");
			}
		}
		return result;
	}

	protected static List<Vertex> convertToVertexes(final Object[] arg0) {
		List<Vertex> result = new ArrayList<Vertex>(arg0.length);
		for (Object raw : arg0) {
			if (raw == null) {

			} else if (raw instanceof Vertex) {
				result.add((Vertex) raw);
			} else if (raw instanceof VertexFrame) {
				result.add(((VertexFrame) raw).asVertex());
			} else if (raw.getClass().isArray()) {
				result.addAll(convertToVertexes((Object[]) raw));
			} else {
				throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to List<Vertex>");
			}
		}
		return result;
	}

	@Override
	public boolean addAll(final Collection<? extends T> arg0) {
		return list_.addAll(convertToVertexes(arg0));
	}

	@Override
	public boolean addAll(final int arg0, final Collection<? extends T> arg1) {
		return list_.addAll(arg0, convertToVertexes(arg1));
	}

	@Override
	public void clear() {
		list_.clear();
	}

	@Override
	public boolean contains(final Object arg0) {
		if (arg0 instanceof Vertex) {
			return list_.contains(arg0);
		} else if (kind.isAssignableFrom(arg0.getClass())) {
			return list_.contains(((VertexFrame) kind.cast(arg0)).asVertex());
		} else {
			throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of "
					+ kind.getName());
		}
	}

	@Override
	public boolean containsAll(final Collection<?> arg0) {
		return list_.containsAll(convertToVertexes(arg0));
	}

	@Override
	public T get(final int arg0) {
		return framedGraph.frame(list_.get(arg0), kind);
	}

	@Override
	public int indexOf(final Object arg0) {
		if (arg0 instanceof Vertex) {
			return list_.indexOf(arg0);
		} else if (kind.isAssignableFrom(arg0.getClass())) {
			return list_.indexOf(((VertexFrame) kind.cast(arg0)).asVertex());
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
		if (arg0 instanceof Vertex) {
			return list_.lastIndexOf(arg0);
		} else if (kind.isAssignableFrom(arg0.getClass())) {
			return list_.lastIndexOf(((VertexFrame) kind.cast(arg0)).asVertex());
		} else {
			throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of "
					+ kind.getName());
		}
	}

	@Override
	public ListIterator<T> listIterator() {
		return new FramedListIterator<T>((DFramedTransactionalGraph) framedGraph, list_.listIterator(), kind);
	}

	@Override
	public ListIterator<T> listIterator(final int arg0) {
		return new FramedListIterator<T>((DFramedTransactionalGraph) framedGraph, list_.listIterator(arg0), kind);
	}

	@Override
	public Iterator<T> iterator() {
		ListIterator<Vertex> iterator = list_.listIterator();
		if (iterator == null)
			System.err.println("ListIterator IS NULL from list of type " + list_.getClass().getName());
		return new FramedListIterator<T>((DFramedTransactionalGraph) framedGraph, iterator, kind);
	}

	@Override
	public T remove(final int arg0) {
		Vertex e = list_.remove(arg0);
		return framedGraph.frame(e, kind);
	}

	@Override
	public boolean remove(final Object arg0) {
		if (arg0 instanceof Vertex) {
			return list_.remove(arg0);
		} else if (kind.isAssignableFrom(arg0.getClass())) {
			return list_.remove(((VertexFrame) kind.cast(arg0)).asVertex());
		} else {
			throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of "
					+ kind.getName());
		}
	}

	@Override
	public boolean removeAll(final Collection<?> arg0) {
		return list_.removeAll(convertToVertexes(arg0));
	}

	@Override
	public boolean retainAll(final Collection<?> arg0) {
		return list_.retainAll(convertToVertexes(arg0));
	}

	@Override
	public T set(final int arg0, final T arg1) {
		Vertex edge = arg1.asVertex();
		list_.set(arg0, edge);
		return arg1;
	}

	@Override
	public int size() {
		return list_.size();
	}

	@Override
	public List<T> subList(final int arg0, int arg1) {
		if (arg1 > list_.size()) {
			arg1 = list_.size();
		}
		List<Vertex> sublist = list_.subList(arg0, arg1);
		return new FramedVertexList<T>(framedGraph, sourceVertex_, sublist, kind);
	}

	@Override
	public Object[] toArray() {
		int size = list_.size();
		Object[] result = new Object[size];
		for (int i = 0; i < size; i++) {
			Vertex e = list_.get(i);
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
		int i = 0;
		for (Vertex v : list_) {
			if (v != null) {
				result[i++] = (U) framedGraph.frame(v, kind);
			}
		}
		//		for (int i = 0; i < size; i++) {
		//			Vertex e = list_.get(i);
		//			result[i] = (U) framedGraph.frame(e, kind);
		//		}
		if (i < size) {
			result = Arrays.copyOf(result, i);
		}
		return result;
	}

	public static List<Vertex> toVertexList(final VertexFrame[] vfArray) {
		List<Vertex> result = new ArrayList<Vertex>(vfArray.length);
		for (VertexFrame vf : vfArray) {
			if (vf != null) {
				result.add(vf.asVertex());
			}
		}
		return result;
	}

	private static final VertexFrame[] VF = new VertexFrame[1];

	public FramedVertexList<T> sortBy(final List<CharSequence> keys, final boolean desc) {
		//TODO: optimize! This should really be resorting the Vertex list but using the VertexFrame as the criteria
		VertexFrame[] array = toArray(VF);
		Arrays.sort(array, new DGraphUtils.VertexFrameComparator(getGraph(), keys, desc));
		return new FramedVertexList<T>(framedGraph, sourceVertex_, toVertexList(array), kind);
	}
}
