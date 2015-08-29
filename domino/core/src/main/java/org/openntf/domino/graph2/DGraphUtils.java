package org.openntf.domino.graph2;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.types.CaseInsensitiveString;

import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;

public enum DGraphUtils {
	;

	public abstract static class AbstractFrameComparator {
		protected List<CaseInsensitiveString> keys_;
		protected DFramedTransactionalGraph<?> graph_;

		public AbstractFrameComparator(final FramedGraph<?> graph, final List<CaseInsensitiveString> keys) {
			if (graph instanceof DFramedTransactionalGraph) {
				graph_ = (DFramedTransactionalGraph<?>) graph;
			}
			keys_ = keys;
		}

		public AbstractFrameComparator(final FramedGraph<?> graph, final String key) {
			if (graph instanceof DFramedTransactionalGraph) {
				graph_ = (DFramedTransactionalGraph<?>) graph;
			}
			keys_ = new ArrayList<CaseInsensitiveString>();
			keys_.add(new CaseInsensitiveString(key));
		}

		public Class<?> getCompareType(final Object frame, final CaseInsensitiveString key) {
			return getFramedPropertyType(graph_, frame, key);
		}

		public Class<?> toCompatibleClass(final Class<?> class1, final Class<?> class2) {
			if (class1.equals(class2))
				return class1;
			if (class2.isAssignableFrom(class1))
				return class2;
			if (class1.isAssignableFrom(class2))
				return class1;
			//TODO exhaustive search of class hierarchy & interfaces
			return Object.class;
		}
	}

	public static class EdgeFrameComparator extends AbstractFrameComparator implements Comparator<EdgeFrame> {

		public EdgeFrameComparator(final FramedGraph<?> graph, final List<CaseInsensitiveString> keys) {
			super(graph, keys);
		}

		public EdgeFrameComparator(final FramedGraph<?> graph, final String key) {
			super(graph, key);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public int compare(final EdgeFrame e1, final EdgeFrame e2) {
			int result = 0;
			ListIterator<CaseInsensitiveString> li = keys_.listIterator(keys_.size());
			while (li.hasPrevious()) {
				CaseInsensitiveString key = li.previous();
				Class<?> type1 = getCompareType(e1, key);
				Class<?> type2 = getCompareType(e2, key);
				Class<?> compareType = toCompatibleClass(type1, type2);
				Object val1 = getFramedProperty(graph_, e1, key);
				Object val2 = getFramedProperty(graph_, e2, key);
				Comparable castVal1 = (Comparable) compareType.cast(val1);
				Comparable castVal2 = (Comparable) compareType.cast(val2);
				int curComp = castVal1.compareTo(castVal2);
				if (curComp != 0) {
					result = curComp;
				}
			}
			return result;
		}

	}

	public static class VertexFrameComparator extends AbstractFrameComparator implements Comparator<VertexFrame> {

		public VertexFrameComparator(final FramedGraph<?> graph, final List<CaseInsensitiveString> keys) {
			super(graph, keys);
		}

		public VertexFrameComparator(final FramedGraph<?> graph, final String key) {
			super(graph, key);
		}

		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public int compare(final VertexFrame e1, final VertexFrame e2) {
			int result = 0;
			ListIterator<CaseInsensitiveString> li = keys_.listIterator(keys_.size());
			while (li.hasPrevious()) {
				CaseInsensitiveString key = li.previous();
				//				System.out.println("TEMP DEBUG: Checking key " + key.toString());
				try {
					Class<?> type1 = getCompareType(e1, key);
					Class<?> type2 = getCompareType(e2, key);
					Class<?> compareType = toCompatibleClass(type1, type2);
					Object val1 = getFramedProperty(graph_, e1, key);
					Object val2 = getFramedProperty(graph_, e2, key);
					Comparable castVal1 = (Comparable) compareType.cast(val1);
					Comparable castVal2 = (Comparable) compareType.cast(val2);
					int curComp = castVal1.compareTo(castVal2);
					if (curComp != 0) {
						result = curComp;
					} else {
						//						System.out.println("Got a matching values for key " + key.toString() + ": " + String.valueOf(castVal1) + ", "
						//								+ String.valueOf(castVal2));
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (result == 0) {
				//				System.out.println("TEMP DEBUG: Found matching values for keys " + CaseInsensitiveString.toString(keys_));
			}
			return result;
		}

	}

	public static Object getFramedProperty(final FramedGraph<?> graph, final Object frame, final CaseInsensitiveString key) {
		Object result = null;
		Map<CaseInsensitiveString, Method> getters = null;
		if (frame instanceof EdgeFrame) {
			getters = getGetters(graph, (EdgeFrame) frame);
		} else {
			getters = getGetters(graph, (VertexFrame) frame);
		}
		Method crystal = getters.get(key);
		if (crystal != null) {
			try {
				result = crystal.invoke(frame, (Object[]) null);
			} catch (Exception e) {
				if (frame instanceof EdgeFrame) {
					result = ((EdgeFrame) frame).asEdge().getProperty(key.toString());
				} else {
					result = ((VertexFrame) frame).asVertex().getProperty(key.toString());
				}
			}
		}
		return result;
	}

	public static Object getFramedProperty(final FramedGraph<?> graph, final Object frame, final String key) {
		CaseInsensitiveString lkey = new CaseInsensitiveString(key);
		return getFramedProperty(graph, frame, lkey);
	}

	public static Class<?> getFramedPropertyType(final FramedGraph<?> graph, final Object frame, final CaseInsensitiveString key) {
		Class<?> result = Object.class;
		Map<CaseInsensitiveString, Method> getters = null;
		if (frame instanceof EdgeFrame) {
			getters = getGetters(graph, (EdgeFrame) frame);
		} else {
			getters = getGetters(graph, (VertexFrame) frame);
		}
		Method crystal = getters.get(key);
		if (crystal != null) {
			result = crystal.getReturnType();
		}
		return result;
	}

	public static boolean isType(final Object frame, final String typename) {
		Class<?>[] interfaces = frame.getClass().getInterfaces();
		for (Class<?> inter : interfaces) {
			if (inter.getName().equals(typename))
				return true;
		}
		return false;
	}

	public static Class<?> findInterface(final Object frame) {
		Class<?>[] interfaces = frame.getClass().getInterfaces();
		return interfaces[interfaces.length - 1];
	}

	public static Map<CaseInsensitiveString, Method> getGetters(final FramedGraph<?> graph, final VertexFrame frame) {
		if (graph instanceof DFramedTransactionalGraph) {
			DFramedTransactionalGraph<?> dgraph = (DFramedTransactionalGraph<?>) graph;
			Class<?> type = dgraph.getTypeManager().resolve(frame.asVertex(), findInterface(frame));
			return dgraph.getTypeRegistry().getPropertiesGetters(type);
		}
		throw new IllegalArgumentException("Cannot discover registered getters for graph type " + graph.getClass().getName());
	}

	public static Map<CaseInsensitiveString, Method> getGetters(final FramedGraph<?> graph, final EdgeFrame frame) {
		if (graph instanceof DFramedTransactionalGraph) {
			DFramedTransactionalGraph<?> dgraph = (DFramedTransactionalGraph<?>) graph;
			Class<?> type = dgraph.getTypeManager().resolve(frame.asEdge(), findInterface(frame));
			return dgraph.getTypeRegistry().getPropertiesGetters(type);
		}
		throw new IllegalArgumentException("Cannot discover registered getters for graph type " + graph.getClass().getName());
	}

}
