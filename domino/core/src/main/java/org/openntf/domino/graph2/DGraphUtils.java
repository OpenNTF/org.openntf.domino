package org.openntf.domino.graph2;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.openntf.domino.graph2.impl.DConfiguration.DTypeManager;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.TypeUtils;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.VertexFrame;

public enum DGraphUtils {
	;

	public abstract static class AbstractElementComparator {
		protected List<CharSequence> keys_;
		protected boolean desc_;

		public AbstractElementComparator(final List<CharSequence> keys, final boolean desc) {
			keys_ = keys;
			desc_ = desc;
		}

		public AbstractElementComparator(final String key, final boolean desc) {
			keys_ = new ArrayList<CharSequence>();
			keys_.add(new CaseInsensitiveString(key));
			desc_ = desc;
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

	public static class VertexComparator extends AbstractElementComparator implements Comparator<Vertex> {
		public VertexComparator(final List<CharSequence> keys, final boolean desc) {
			super(keys, desc);
		}

		public VertexComparator(final String key, final boolean desc) {
			super(key, desc);
		}

		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public int compare(final Vertex e1, final Vertex e2) {
			int result = 0;
			ListIterator<CharSequence> li = keys_.listIterator(keys_.size());
			while (li.hasPrevious()) {
				CharSequence key = li.previous();
				try {
					Comparable val1 = e1.getProperty(key.toString());
					Comparable val2 = e2.getProperty(key.toString());
					int curComp = 0;
					if (val1 instanceof String) {
						curComp = String.CASE_INSENSITIVE_ORDER.compare((String) val1, (String) val2);
					} else {
						curComp = val1.compareTo(val2);
					}
					if (curComp != 0) {
						if (desc_) {
							result = curComp * -1;
						} else {
							result = curComp;
						}
					} else {
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

	public abstract static class AbstractFrameComparator {
		protected List<CharSequence> keys_;
		protected DFramedTransactionalGraph<?> graph_;
		protected boolean desc_;

		public AbstractFrameComparator(final FramedGraph<?> graph, final List<? extends CharSequence> keys, final boolean desc) {
			if (graph instanceof DFramedTransactionalGraph) {
				graph_ = (DFramedTransactionalGraph<?>) graph;
			}
			keys_ = (List<CharSequence>) keys;
			desc_ = desc;
		}

		public AbstractFrameComparator(final FramedGraph<?> graph, final String key, final boolean desc) {
			if (graph instanceof DFramedTransactionalGraph) {
				graph_ = (DFramedTransactionalGraph<?>) graph;
			}
			keys_ = new ArrayList<CharSequence>();
			keys_.add(new CaseInsensitiveString(key));
			desc_ = desc;
		}

		public Class<?> getCompareType(final Object frame, final CharSequence key) {
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

		//		public EdgeFrameComparator(final FramedGraph<?> graph, final List<CaseInsensitiveString> keys) {
		//			super(graph, keys, false);
		//		}
		//
		//		public EdgeFrameComparator(final FramedGraph<?> graph, final String key) {
		//			super(graph, key, false);
		//		}

		public EdgeFrameComparator(final FramedGraph<?> graph, final List<? extends CharSequence> keys, final boolean desc) {
			super(graph, keys, desc);
		}

		public EdgeFrameComparator(final FramedGraph<?> graph, final String key, final boolean desc) {
			super(graph, key, desc);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public int compare(final EdgeFrame e1, final EdgeFrame e2) {
			int result = 0;
			ListIterator<CharSequence> li = keys_.listIterator(keys_.size());
			while (li.hasPrevious()) {
				CharSequence key = li.previous();
				Class<?> type1 = getCompareType(e1, key);
				Class<?> type2 = getCompareType(e2, key);
				Class<?> compareType = toCompatibleClass(type1, type2);
				Object val1 = getFramedProperty(graph_, e1, key);
				Object val2 = getFramedProperty(graph_, e2, key);
				Comparable castVal1 = (Comparable) TypeUtils.objectToClass(val1, compareType, null);
				Comparable castVal2 = (Comparable) TypeUtils.objectToClass(val2, compareType, null);
				int curComp = 0;
				if (String.class.equals(compareType)) {
					curComp = String.CASE_INSENSITIVE_ORDER.compare((castVal1 == null ? "" : (String) castVal1),
							(castVal2 == null ? "" : (String) castVal2));
				} else {
					if (castVal1 == null || castVal2 == null) {
						result = 0;
					} else {
						curComp = castVal1.compareTo(castVal2);
					}
				}
				if (curComp != 0) {
					if (desc_) {
						result = curComp * -1;
					} else {
						result = curComp;
					}
				}
			}
			return result;
		}

	}

	public static class VertexFrameComparator extends AbstractFrameComparator implements Comparator<VertexFrame> {

		//		public VertexFrameComparator(final FramedGraph<?> graph, final List<CaseInsensitiveString> keys) {
		//			super(graph, keys, false);
		//		}
		//
		//		public VertexFrameComparator(final FramedGraph<?> graph, final String key) {
		//			super(graph, key, false);
		//		}

		public VertexFrameComparator(final FramedGraph<?> graph, final List<? extends CharSequence> keys, final boolean desc) {
			super(graph, keys, desc);
		}

		public VertexFrameComparator(final FramedGraph<?> graph, final String key, final boolean desc) {
			super(graph, key, desc);
		}

		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public int compare(final VertexFrame e1, final VertexFrame e2) {
			int result = 0;
			ListIterator<CharSequence> li = keys_.listIterator(keys_.size());
			while (li.hasPrevious()) {
				CharSequence key = li.previous();
				//				System.out.println("TEMP DEBUG: Checking key " + key.toString());
				try {
					Class<?> type1 = getCompareType(e1, key);
					Class<?> type2 = getCompareType(e2, key);
					Class<?> compareType = toCompatibleClass(type1, type2);
					Object val1 = getFramedProperty(graph_, e1, key);
					Object val2 = getFramedProperty(graph_, e2, key);
					Comparable castVal1 = (Comparable) TypeUtils.objectToClass(val1, compareType, null);
					Comparable castVal2 = (Comparable) TypeUtils.objectToClass(val2, compareType, null);
					int curComp = 0;
					if (String.class.equals(compareType)) {
						curComp = String.CASE_INSENSITIVE_ORDER.compare((castVal1 == null ? "" : (String) castVal1),
								(castVal2 == null ? "" : (String) castVal2));
					} else {
						if (castVal1 == null || castVal2 == null) {
							result = 0;
						} else {
							curComp = castVal1.compareTo(castVal2);
						}
					}
					if (curComp != 0) {
						if (desc_) {
							result = curComp * -1;
						} else {
							result = curComp;
						}
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

	public static Object getFramedProperty(final FramedGraph<?> graph, final Object frame, CharSequence key) {
		Object result = null;
		Map<CaseInsensitiveString, Method> getters = null;

		if (frame instanceof EdgeFrame) {
			getters = getGetters(graph, (EdgeFrame) frame);
		} else {
			if (key.toString().startsWith("@counts")) {
				String labelStr = key.toString().substring("@counts".length());
				key = new CaseInsensitiveString(labelStr);
				getters = getCounters(graph, (VertexFrame) frame);
			} else {
				getters = getGetters(graph, (VertexFrame) frame);
			}
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

	public static Class<?> getFramedPropertyType(final FramedGraph<?> graph, final Object frame, CharSequence key) {
		Class<?> result = Object.class;
		Map<CaseInsensitiveString, Method> getters = null;
		if (frame instanceof EdgeFrame) {
			getters = getGetters(graph, (EdgeFrame) frame);
		} else {
			getters = getGetters(graph, (VertexFrame) frame);
		}
		if (!(key instanceof CaseInsensitiveString)) {
			key = new CaseInsensitiveString(key);
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

	public static Class<?>[] getTypesForFrame(final Object element) {
		return element.getClass().getInterfaces();
	}

	public static String getInterfaceList(final Object element) {
		StringBuilder sb = new StringBuilder();
		for (Class<?> klazz : getTypesForFrame(element)) {
			sb.append(klazz.getName() + ",");
		}
		return sb.toString();
	}

	public static Map<CaseInsensitiveString, Method> getGetters(final FramedGraph<?> graph, final VertexFrame frame) {
		if (graph instanceof DFramedTransactionalGraph) {
			DFramedTransactionalGraph<?> dgraph = (DFramedTransactionalGraph<?>) graph;
			DTypeManager tman = dgraph.getTypeManager();
			Vertex v = frame.asVertex();
			Class<?> inter = findInterface(frame);
			Class<?> type = tman.resolve(v, inter);
			return dgraph.getTypeRegistry().getPropertiesGetters(type);
		}
		throw new IllegalArgumentException("Cannot discover registered getters for graph type " + graph.getClass().getName());
	}

	public static Map<CaseInsensitiveString, Method> getCounters(final FramedGraph<?> graph, final VertexFrame frame) {
		if (graph instanceof DFramedTransactionalGraph) {
			DFramedTransactionalGraph<?> dgraph = (DFramedTransactionalGraph<?>) graph;
			DTypeManager tman = dgraph.getTypeManager();
			Vertex v = frame.asVertex();
			Class<?> inter = findInterface(frame);
			Class<?> type = tman.resolve(v, inter);
			return dgraph.getTypeRegistry().getCounters(type);
		}
		throw new IllegalArgumentException("Cannot discover registered getters for graph type " + graph.getClass().getName());
	}

	public static Map<CaseInsensitiveString, Method> getGetters(final FramedGraph<?> graph, final EdgeFrame frame) {
		if (graph instanceof DFramedTransactionalGraph) {
			DFramedTransactionalGraph<?> dgraph = (DFramedTransactionalGraph<?>) graph;
			DTypeManager tman = dgraph.getTypeManager();
			Edge e = frame.asEdge();
			Class<?> inter = findInterface(frame);
			Class<?> type = tman.resolve(e, inter);
			return dgraph.getTypeRegistry().getPropertiesGetters(type);
		}
		throw new IllegalArgumentException("Cannot discover registered getters for graph type " + graph.getClass().getName());
	}

}
