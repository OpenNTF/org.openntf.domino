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

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.gremlin.Tokens.T;

public class MixedFramedVertexList /*extends FramedVertexIterable<T>*/ implements List {
	public static class MixedFramedListIterator implements ListIterator {
		//		    protected final Direction direction_;
		protected final ListIterator<Vertex> iterator_;
		protected final FramedGraph<? extends Graph> framedGraph_;

		public MixedFramedListIterator(final FramedGraph<? extends Graph> graph, final ListIterator<Vertex> iterator) {
			//		    	direction_ = direction;
			iterator_ = iterator;
			framedGraph_ = graph;
		}

		@Override
		public void add(final Object arg0) {
			if (arg0 == null) {
				return;
			}
			if (arg0 instanceof Vertex) {
				iterator_.add((Vertex) arg0);
			} else if (arg0 instanceof VertexFrame) {
				iterator_.add(((VertexFrame) arg0).asVertex());
			} else {
				throw new IllegalArgumentException("Cannot add an object of type " + arg0.getClass().getName() + " to an iterator");
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
		public Object next() {
			Object result = null;
			Object raw = iterator_.next();
			//			int nullCount = 0;
			while (raw == null && iterator_.hasNext()) {
				//				nullCount++;
				raw = iterator_.next();
			}
			//			if (nullCount > 0) {
			//			System.out.println("TEMP DEBUG skipped " + nullCount + " entries in iterator.");
			//			}
			if (raw != null) {
				if (raw instanceof VertexFrame) {
					result = raw;
					return result;
				} else if (raw instanceof Vertex) {
					Vertex v = (Vertex) raw;
					result = framedGraph_.frame(v, null);
					//					System.out.println("TEMP DEBUG resulting frame is a " + result.getClass().getName());
					return result;
				} else {
					throw new IllegalStateException("FramedVertexListIterator cannot process a member of type "
							+ (raw == null ? "null" : raw.getClass().getName()));
				}
			} else {
				//				System.out.println("TEMP DEBUG next iteration is null");
			}
			return null;
		}

		@Override
		public int nextIndex() {
			return iterator_.nextIndex();
		}

		@Override
		public Object previous() {
			return framedGraph_.frame(iterator_.previous(), null);
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
			} else if (arg0 instanceof VertexFrame) {
				iterator_.set(((VertexFrame) arg0).asVertex());
			} else {
				throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator");
			}
		}

	}

	protected List<Vertex> list_;
	protected Vertex sourceVertex_;
	//	protected final Iterable<Vertex> iterable;
	protected final FramedGraph<? extends Graph> framedGraph;

	public MixedFramedVertexList(final FramedGraph<? extends Graph> framedGraph, final Vertex sourceVertex,
			final List<? extends Vertex> list) {
		//		this.iterable = list;
		this.framedGraph = framedGraph;
		sourceVertex_ = sourceVertex;
		if (list instanceof List) {
			if (list instanceof FramedVertexList) {
				list_ = new ArrayList<Vertex>();
				List<Vertex> vlist = ((FramedVertexList) list).list_;
				for (Vertex v : vlist) {
					list_.add(v);
				}
			} else {
				list_ = (List<Vertex>) list;
			}
		} else {
			list_ = new ArrayList<Vertex>();
			Iterator<? extends Vertex> itty = list.iterator();
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
	public MixedFramedVertexList applyFilter(final String key, final Object value) {
		DVertexList vertList = new DVertexList((DVertex) sourceVertex_);
		if (this.size() > 0) {
			for (Object raw : this) {
				VertexFrame vertex = (VertexFrame) raw;
				try {
					if ("@type".equals(key)) {
						if (DGraphUtils.isType(vertex, TypeUtils.toString(value))) {
							vertList.add((DVertex) vertex.asVertex());
						}
					} else {
						Object vertexVal = DGraphUtils.getFramedProperty(getGraph(), vertex, key);
						if (vertexVal instanceof Collection) {
							for (Object rawVal : (Collection) vertexVal) {
								if (value.equals(TypeUtils.toString(rawVal))) {
									vertList.add((DVertex) vertex.asVertex());
								}
							}
						} else {
							if (value.equals(TypeUtils.toString(vertexVal))) {
								vertList.add((DVertex) vertex.asVertex());
							}
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		MixedFramedVertexList result = new MixedFramedVertexList(getGraph(), sourceVertex_, vertList);
		return result;
	}

	public DFramedTransactionalGraph<?> getGraph() {
		return (DFramedTransactionalGraph<?>) framedGraph;
	}

	@Override
	public boolean add(final Object arg0) {
		if (arg0 instanceof VertexFrame) {
			return list_.add(((VertexFrame) arg0).asVertex());
		} else if (arg0 instanceof Vertex) {
			return list_.add((Vertex) arg0);
		} else {
			return false;
		}
	}

	@Override
	public void add(final int arg0, final Object arg1) {
		if (arg1 instanceof VertexFrame) {
			list_.add(arg0, ((VertexFrame) arg1).asVertex());
		} else if (arg1 instanceof Vertex) {
			list_.add(arg0, (Vertex) arg1);
		}
	}

	protected static List<Vertex> convertToVertexes(final Collection<?> arg0) {
		List<Vertex> result = new ArrayList<Vertex>(arg0.size());
		for (Object raw : arg0) {
			if (raw instanceof Vertex) {
				result.add((Vertex) raw);
			} else if (raw instanceof VertexFrame) {
				result.add(((VertexFrame) raw).asVertex());
			} else {
				throw new IllegalArgumentException(
						"Cannot set an object of type " + arg0.getClass().getName() + " to an EdgeFrame iterator");
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
	public boolean addAll(final Collection arg0) {
		if (arg0 instanceof FramedVertexList) {
			FramedVertexList fvl = (FramedVertexList) arg0;
			//			Class<?> k = fvl.getKind();
			//			System.out.println("TEMP DEBUG adding a list of " + (k == null ? "null" : k.getName()) + " with " + fvl.size() + " elements");
			List<Vertex> fvlList = fvl.list_;
			for (Vertex v : fvlList) {
				if (v != null) {
					list_.add(v);
				} else {
					//					System.out.println("TEMP DEBUG Vertex from FramedVertexList is null!?!?");
				}
			}
			boolean result = true;
			//			boolean result = list_.addAll(fvl.list_);
			//			System.out.println("TEMP DEBUG list is now " + list_.size());
			return result;
		}
		return list_.addAll(convertToVertexes(arg0));
	}

	@Override
	public boolean addAll(final int arg0, final Collection arg1) {
		if (arg1 instanceof FramedVertexList) {
			return list_.addAll(arg0, ((FramedVertexList) arg1).list_);
		}
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
		} else if (arg0 instanceof VertexFrame) {
			return list_.contains(((VertexFrame) arg0).asVertex());
		} else {
			throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator");
		}
	}

	@Override
	public boolean containsAll(final Collection arg0) {
		return list_.containsAll(convertToVertexes(arg0));
	}

	@Override
	public T get(final int arg0) {
		return framedGraph.frame(list_.get(arg0), null);
	}

	@Override
	public int indexOf(final Object arg0) {
		if (arg0 instanceof Vertex) {
			return list_.indexOf(arg0);
		} else if (arg0 instanceof VertexFrame) {
			return list_.indexOf(((VertexFrame) arg0).asVertex());
		} else {
			throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator.");
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
		} else if (arg0 instanceof VertexFrame) {
			return list_.lastIndexOf(((VertexFrame) arg0).asVertex());
		} else {
			throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator.");
		}
	}

	@Override
	public ListIterator listIterator() {
		return new MixedFramedListIterator(framedGraph, list_.listIterator());
	}

	@Override
	public ListIterator listIterator(final int arg0) {
		return new MixedFramedListIterator(framedGraph, list_.listIterator(arg0));
	}

	@Override
	public Iterator iterator() {
		ListIterator<Vertex> iterator = list_.listIterator();
		if (iterator == null) {
			System.err.println("ListIterator IS NULL from list of type " + list_.getClass().getName());
		}
		return new MixedFramedListIterator(framedGraph, iterator);
	}

	@Override
	public Object remove(final int arg0) {
		Vertex e = list_.remove(arg0);
		return framedGraph.frame(e, null);
	}

	@Override
	public boolean remove(final Object arg0) {
		if (arg0 instanceof Vertex) {
			return list_.remove(arg0);
		} else if (arg0 instanceof VertexFrame) {
			return list_.remove(((VertexFrame) arg0).asVertex());
		} else {
			throw new IllegalArgumentException("Cannot set an object of type " + arg0.getClass().getName() + " to an iterator.");
		}
	}

	@Override
	public boolean removeAll(final Collection arg0) {
		return list_.removeAll(convertToVertexes(arg0));
	}

	@Override
	public boolean retainAll(final Collection arg0) {
		return list_.retainAll(convertToVertexes(arg0));
	}

	@Override
	public Object set(final int arg0, final Object arg1) {
		if (arg1 instanceof VertexFrame) {
			Vertex v = ((VertexFrame) arg1).asVertex();
			list_.set(arg0, v);
			return arg1;
		} else {
			return null;
		}
	}

	@Override
	public int size() {
		return list_.size();
	}

	@Override
	public List subList(final int arg0, int arg1) {
		if (arg1 > list_.size()) {
			arg1 = list_.size();
		}
		List<Vertex> sublist = list_.subList(arg0, arg1);
		return new MixedFramedVertexList(framedGraph, sourceVertex_, sublist);
	}

	@Override
	public Object[] toArray() {
		int size = list_.size();
		Object[] result = new Object[size];
		for (int i = 0; i < size; i++) {
			Vertex e = list_.get(i);
			result[i] = framedGraph.frame(e, null);
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

	public MixedFramedVertexList sortBy(final List<CharSequence> keys, final boolean desc) {
		//TODO: optimize! This should really be resorting the Vertex list but using the VertexFrame as the criteria
		VertexFrame[] array = (VertexFrame[]) toArray(VF);
		Arrays.sort(array, new DGraphUtils.VertexFrameComparator(getGraph(), keys, desc));
		return new MixedFramedVertexList(framedGraph, sourceVertex_, toVertexList(array));
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object[] toArray(final Object[] arg0) {
		int size = list_.size();
		Class c = arg0.getClass().getComponentType();
		Object[] result = (Object[]) Array.newInstance(c, size);
		int i = 0;
		for (Object o : list_) {
			if (o != null) {
				if (o instanceof Vertex) {
					result[i++] = framedGraph.frame((Vertex) o, null);
				} else if (o instanceof VertexFrame) {
					result[i++] = framedGraph.frame(((VertexFrame) o).asVertex(), null);
				} else {
					System.out.println("TEMP DEBUG list returned a " + o.getClass().getName());
				}
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
}
