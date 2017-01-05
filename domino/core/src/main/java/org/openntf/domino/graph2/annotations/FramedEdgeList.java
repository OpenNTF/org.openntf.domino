package org.openntf.domino.graph2.annotations;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.openntf.domino.graph2.DGraphUtils;
import org.openntf.domino.graph2.impl.DEdge;
import org.openntf.domino.graph2.impl.DEdgeEntryList;
import org.openntf.domino.graph2.impl.DEdgeList;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DVertex;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.TypeUtils;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.structures.FramedEdgeIterable;

public class FramedEdgeList<T extends EdgeFrame> extends FramedEdgeIterable<T> implements List<T> {
	public static class FramedListIterator<T> implements ListIterator<T> {
		protected final Class<T> kind_;
		//		    protected final Direction direction_;
		protected final ListIterator<Edge> iterator_;
		protected final DFramedTransactionalGraph framedGraph_;

		public FramedListIterator(final DFramedTransactionalGraph graph, final ListIterator<Edge> iterator,
				final Class<T> kind/*, Direction direction*/) {
			//			System.out.println("TEMP DEBUG Created new FramedListIterator with a " + iterator.getClass().getName() + " kind: "
			//					+ kind.getName());
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
				throw new IllegalArgumentException(
						"Cannot add an object of type " + arg0.getClass().getName() + " to an iterator of " + kind_.getName());
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
			Edge e = iterator_.next();
			while (e == null && iterator_.hasNext()) {
				e = iterator_.next();
			}
			if (e != null) {
				T result = (T) framedGraph_.getElement(e.getId(), kind_);
				//				 framedGraph_.frame(e, kind_);
				return result;
			} else {
				return null;
			}
		}

		@Override
		public int nextIndex() {
			return iterator_.nextIndex();
		}

		@Override
		public T previous() {
			Edge e = iterator_.previous();
			while (e == null && iterator_.hasPrevious()) {
				e = iterator_.previous();
			}
			if (e != null) {
				T result = (T) framedGraph_.getElement(e.getId(), kind_);
				//				 framedGraph_.frame(e, kind_);
				return result;
			} else {
				return null;
			}
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
				throw new IllegalArgumentException(
						"Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of " + kind_.getName());
			}
		}

	}

	protected List<Edge> list_;
	protected Vertex sourceVertex_;

	public FramedEdgeList(final FramedGraph<? extends Graph> framedGraph, final Vertex sourceVertex, final Iterable<Edge> list,
			final Class<T> kind) {
		super(framedGraph, list, kind);
		//		System.out.println("TEMP DEBUG new FramedEdgeList created from a " + list.getClass().getName());
		sourceVertex_ = sourceVertex;
		if (list instanceof List) {
			list_ = (List<Edge>) list;
		} else {
			list_ = new ArrayList<Edge>();
			for (Edge e : list) {
				list_.add(e);
			}
		}
	}

	//TODO optimize by building a NoteCoordinateList of the target vertices
	public FramedVertexList<?> toVertexList() {
		//		System.out.println("TEMP DEBUG converting a FramedEdgeList to a FramedVertexList");
		List<Vertex> vertList = new ArrayList<Vertex>();
		for (Edge edge : list_) {
			if (edge instanceof DEdge) {
				try {
					Vertex other = ((DEdge) edge).getOtherVertex(sourceVertex_);
					vertList.add(other);
				} catch (IllegalStateException ise) {
					System.out.println("WARNING: " + ise.getMessage());
				} catch (Throwable t) {
					t.printStackTrace();
				}
			} else {
				//				System.out.println("TEMP DEBUG edge is actually a " + edge.getClass().getName());
			}
		}
		FramedVertexList<?> result = new FramedVertexList<VertexFrame>(this.framedGraph, sourceVertex_, vertList, null);
		return result;
	}

	public FramedEdgeList<T> applyFilter(final String key, final Object value) {
		if ("lookup".equals(key) && list_ instanceof DEdgeEntryList && value instanceof List) {
			//			System.out.println("TEMP DEBUG dealing with DEdgeEntryList");
			((DEdgeEntryList) list_).initEntryList((List<CharSequence>) value);
			return this;
		}
		DEdgeList edgeList = new DEdgeList((DVertex) sourceVertex_);
		if (this.size() > 0) {
			for (EdgeFrame edge : this) {
				try {
					if ("@type".equals(key)) {
						if (isType(edge, TypeUtils.toString(value))) {
							edgeList.add(edge.asEdge());
						}
					} else {
						Object edgeVal = null;
						Method crystal = getGetters(edge).get(new CaseInsensitiveString(key));
						if (crystal != null) {
							try {
								edgeVal = crystal.invoke(edge, (Object[]) null);
							} catch (Exception e) {
								edgeVal = edge.asEdge().getProperty(key);
							}
						} else {
							System.err.println("No method found for key " + key);
						}
						if (value.equals(TypeUtils.toString(edgeVal))) {
							edgeList.add(edge.asEdge());
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		FramedEdgeList<T> result = new FramedEdgeList<T>(framedGraph, sourceVertex_, edgeList, this.kind);
		return result;
	}

	public boolean isType(final EdgeFrame frame, final String typename) {
		Class<?>[] interfaces = frame.getClass().getInterfaces();
		for (Class<?> inter : interfaces) {
			if (inter.getName().equals(typename))
				return true;
		}
		return false;
	}

	public Class<?> findInterface(final EdgeFrame frame) {
		Class<?>[] interfaces = frame.getClass().getInterfaces();
		return interfaces[interfaces.length - 1];
	}

	public Map<CaseInsensitiveString, Method> getGetters(final EdgeFrame frame) {
		Class<?> type = getGraph().getTypeManager().resolve(frame.asEdge(), findInterface(frame));
		return getGraph().getTypeRegistry().getPropertiesGetters(type);
	}

	public DFramedTransactionalGraph<?> getGraph() {
		return (DFramedTransactionalGraph<?>) framedGraph;
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
				throw new IllegalArgumentException(
						"Cannot set an object of type " + arg0.getClass().getName() + " to an EdgeFrame iterator");
			}
		}
		return result;
	}

	protected static Collection<Edge> convertToEdges(final Object[] arg0) {
		List<Edge> result = new ArrayList<Edge>(arg0.length);
		if (arg0.length > 0) {
			for (Object raw : arg0) {
				if (raw != null) {
					if (raw instanceof Edge) {
						result.add((Edge) raw);
					} else if (raw instanceof EdgeFrame) {
						result.add(((EdgeFrame) raw).asEdge());
					} else if (raw.getClass().isArray() && (raw.getClass().getComponentType().equals(Edge.class))) {
						result.addAll(Arrays.asList((Edge[]) raw));
					} else if (raw.getClass().isArray() && (raw.getClass().getComponentType().equals(EdgeFrame.class))) {
						for (EdgeFrame ef : ((EdgeFrame[]) raw)) {
							result.add(ef.asEdge());
						}
					} else {
						throw new IllegalArgumentException(
								"Cannot set an object of type " + arg0.getClass().getName() + " to an EdgeFrame iterator");
					}
				}
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
			throw new IllegalArgumentException(
					"Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of " + kind.getName());
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
			throw new IllegalArgumentException(
					"Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of " + kind.getName());
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
			throw new IllegalArgumentException(
					"Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of " + kind.getName());
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
		return new FramedListIterator<T>((DFramedTransactionalGraph) framedGraph, list_.listIterator(), kind);
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
			throw new IllegalArgumentException(
					"Cannot set an object of type " + arg0.getClass().getName() + " to an iterator of " + kind.getName());
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
	public List<T> subList(final int arg0, int arg1) {
		if (arg1 > list_.size()) {
			arg1 = list_.size();
		}
		List<Edge> sublist = list_.subList(arg0, arg1);
		return new FramedEdgeList<T>(framedGraph, sourceVertex_, sublist, kind);
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

	private static final EdgeFrame[] EF = new EdgeFrame[1];

	public FramedEdgeList<T> sortBy(final List<? extends CharSequence> list, final boolean desc) {
		EdgeFrame[] array = toArray(EF);
		Arrays.sort(array, new DGraphUtils.EdgeFrameComparator(getGraph(), list, desc));
		return new FramedEdgeList<T>(framedGraph, sourceVertex_, convertToEdges(array), kind);
	}
}
