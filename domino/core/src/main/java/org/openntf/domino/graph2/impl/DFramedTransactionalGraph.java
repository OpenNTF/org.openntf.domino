package org.openntf.domino.graph2.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openntf.domino.big.impl.NoteCoordinate;
import org.openntf.domino.graph2.DKeyResolver;
import org.openntf.domino.graph2.annotations.FramedEdgeList;
import org.openntf.domino.graph2.annotations.FramedVertexList;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.ViewVertex;
import org.openntf.domino.graph2.impl.DConfiguration.DTypeManager;
import org.openntf.domino.graph2.impl.DConfiguration.DTypeRegistry;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.DominoUtils;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FrameInitializer;
import com.tinkerpop.frames.FramedGraphConfiguration;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.modules.javahandler.JavaFrameInitializer;

public class DFramedTransactionalGraph<T extends TransactionalGraph> extends FramedTransactionalGraph<T> {

	public class FramedElementIterable<T> implements Iterable<T> {
		protected final Class<T> kind;
		protected final Iterable<Element> iterable;
		protected final DFramedTransactionalGraph<? extends Graph> framedGraph;

		public FramedElementIterable(final DFramedTransactionalGraph<? extends Graph> framedGraph, final Iterable<Element> iterable,
				final Class<T> kind) {
			this.framedGraph = framedGraph;
			this.iterable = iterable;
			this.kind = kind;
		}

		@Override
		public Iterator<T> iterator() {
			return new Iterator<T>() {
				private Iterator<Element> iterator = iterable.iterator();

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}

				@Override
				public boolean hasNext() {
					return this.iterator.hasNext();
				}

				@Override
				public T next() {
					return framedGraph.frame(this.iterator.next(), kind);
				}
			};
		}
	}

	public DFramedTransactionalGraph(final T baseGraph, final FramedGraphConfiguration config) {
		super(baseGraph, config);
	}

	public DTypeRegistry getTypeRegistry() {
		Graph graph = this.getBaseGraph();
		if (graph instanceof DGraph) {
			DConfiguration config = (DConfiguration) ((DGraph) graph).getConfiguration();
			return config.getTypeRegistry();
		}
		return null;
	}

	public DTypeManager getTypeManager() {
		Graph graph = this.getBaseGraph();
		if (graph instanceof DGraph) {
			DConfiguration config = (DConfiguration) ((DGraph) graph).getConfiguration();
			return config.getTypeManager();
		}
		return null;
	}

	private String getTypedId(final Object id) {
		String result = null;
		if (id != null && id instanceof String) {
			String idStr = (String) id;
			if (idStr.length() == 16) {
				if (DominoUtils.isReplicaId(idStr)) {
					result = idStr;
				}
			} else if (idStr.length() > 16) {
				if (idStr.length() == 32) {
					if (DominoUtils.isUnid(idStr)) {
						result = null;
					}
				} else {
					String prefix = idStr.substring(0, 16);
					if (DominoUtils.isReplicaId(prefix)) {
						result = prefix;
					}
				}
			}
		}
		return result;
	}

	@Override
	public <F> F addVertex(final Object id, final Class<F> kind) {
		DGraph base = (DGraph) this.getBaseGraph();
		org.openntf.domino.graph2.DElementStore store = null;
		if (kind != null) {
			store = base.findElementStore(kind);
		}
		if (store == null) {
			if (id instanceof NoteCoordinate) {
				store = base.findElementStore(id);
			} else {
				String typeid = getTypedId(id);
				if (typeid == null) {
					store = base.getDefaultElementStore();
				} else {
					store = base.findElementStore(typeid);
				}
			}
		}
		Vertex vertex = store.addVertex(id);
		return frame(vertex, kind);
	}

	public <F> F getVertex(final Class<F> kind, final Object context, final Object... args) {
		DGraph base = (DGraph) this.getBaseGraph();
		org.openntf.domino.graph2.DElementStore store = base.findElementStore(kind);
		Object id = store.getIdentity(kind, context, args);
		return getVertex(id, kind);
	}

	public <F> Iterable<F> getElements(final Class<F> kind) {
		org.openntf.domino.graph2.DElementStore store = null;
		DGraph base = (DGraph) this.getBaseGraph();
		store = base.findElementStore(kind);
		if (store != null) {
			String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getFramedElementFormula(kind);
			Iterable<Element> elements = (org.openntf.domino.graph2.impl.DElementIterable) store.getElements(formulaFilter);
			return this.frameElements(elements, kind);
		} else {
			return null;
		}
	}

	public <F> Iterable<F> getElements(final String classname) {
		org.openntf.domino.graph2.DElementStore store = null;
		DGraph base = (DGraph) this.getBaseGraph();
		Class<?> chkClass = getTypeRegistry().getType(DVertexFrame.class, classname);
		if (chkClass == null) {
			chkClass = getTypeRegistry().getType(DEdgeFrame.class, classname);
		}
		if (chkClass != null) {
			store = base.findElementStore(chkClass);
			if (store != null) {
				String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getFramedElementFormula(chkClass);
				Iterable<Element> elements = (org.openntf.domino.graph2.impl.DElementIterable) store.getElements(formulaFilter);
				return this.frameElements(elements, null);
			} else {
				return null;
			}
		} else {
			throw new IllegalArgumentException("Class " + classname + " not registered in graph");
		}
	}

	public <F> Iterable<F> getFilteredElements(final String classname, final List<CaseInsensitiveString> keys,
			final List<CaseInsensitiveString> values) {
		//		System.out.println("Getting a filtered list of elements of type " + classname);
		org.openntf.domino.graph2.DElementStore store = null;
		DGraph base = (DGraph) this.getBaseGraph();
		Class<?> chkClass = getTypeRegistry().getType(DVertexFrame.class, classname);
		if (chkClass == null) {
			chkClass = getTypeRegistry().getType(DEdgeFrame.class, classname);
		}
		if (chkClass != null) {
			store = base.findElementStore(chkClass);
			if (store != null) {
				List<String> keystrs = CaseInsensitiveString.toStrings(keys);
				List<Object> valobj = new ArrayList<Object>(values);
				String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getFramedElementFormula(keystrs, valobj, chkClass);
				Iterable<Element> elements = (org.openntf.domino.graph2.impl.DElementIterable) store.getElements(formulaFilter);
				return this.frameElements(elements, null);
			} else {
				return null;
			}
		} else {
			throw new IllegalArgumentException("Class " + classname + " not registered in graph");
		}
	}

	public <F> Iterable<F> getFilteredElementsPartial(final String classname, final List<CaseInsensitiveString> keys,
			final List<CaseInsensitiveString> values) {
		//		System.out.println("Getting a filtered list of elements of type " + classname);
		org.openntf.domino.graph2.DElementStore store = null;
		DGraph base = (DGraph) this.getBaseGraph();
		Class<?> chkClass = getTypeRegistry().getType(DVertexFrame.class, classname);
		if (chkClass == null) {
			chkClass = getTypeRegistry().getType(DEdgeFrame.class, classname);
		}
		if (chkClass != null) {
			store = base.findElementStore(chkClass);
			if (store != null) {
				List<String> keystrs = CaseInsensitiveString.toStrings(keys);
				List<Object> valobj = new ArrayList<Object>(values);
				String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getFramedElementPartialFormula(keystrs, valobj, chkClass);
				Iterable<Element> elements = (org.openntf.domino.graph2.impl.DElementIterable) store.getElements(formulaFilter);
				return this.frameElements(elements, null);
			} else {
				return null;
			}
		} else {
			throw new IllegalArgumentException("Class " + classname + " not registered in graph");
		}
	}

	public <F> F getElement(final Object id, final Class<F> kind) {
		F result = null;
		DGraph base = (DGraph) this.getBaseGraph();
		org.openntf.domino.graph2.DElementStore store = null;
		if (id instanceof NoteCoordinate) {
			store = base.findElementStore(id);
			//			System.out.println("Got element store from NoteCoordinate " + id.toString());
		} else {
			String typeid = getTypedId(id);
			if (typeid == null) {
				store = base.findElementStore(kind);
			} else {
				store = base.findElementStore(typeid);
			}
		}
		//		System.out.println("Attempting to get an element from element store " + System.identityHashCode(store));
		Element elem = store.getElement(id);
		if (null == elem) {
			result = null;
		} else if (elem instanceof Edge) {
			result = frame((Edge) elem, kind);
		} else if (elem instanceof Vertex) {
			result = frame((Vertex) elem, kind);
		} else {
			throw new IllegalStateException("Key " + id.toString() + " returned an element of type " + elem.getClass().getName());
		}
		//		if (result == null) {
		//			System.out.println("Unable to resolve an element with id " + id.toString() + " though we did find a store "
		//					+ store.getStoreKey());
		//		}
		return result;
	}

	@Override
	public <F> F getVertex(final Object id, final Class<F> kind) {
		DGraph base = (DGraph) this.getBaseGraph();
		org.openntf.domino.graph2.DElementStore store = null;
		if (id instanceof NoteCoordinate) {
			store = base.findElementStore(id);
		} else {
			String typeid = getTypedId(id);
			if (typeid == null) {
				store = base.findElementStore(kind);
			} else {
				store = base.findElementStore(typeid);
			}
		}
		Vertex vertex = store.getVertex(id);
		if (null == vertex) {
			return null;
		}

		return frame(vertex, kind);
	}

	@Override
	public <F> F getEdge(final Object id, final Class<F> kind) {
		DGraph base = (DGraph) this.getBaseGraph();
		org.openntf.domino.graph2.DElementStore store = null;
		if (id instanceof NoteCoordinate) {
			store = base.findElementStore(id);
		} else {
			String typeid = getTypedId(id);
			if (typeid == null) {
				store = base.findElementStore(kind);
			} else {
				store = base.findElementStore(typeid);
			}
		}
		Edge edge = store.getEdge(id);
		if (null == edge) {
			return null;
		}
		//		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
		//			initializer.initElement(kind, this, edge);
		//		}
		return frame(edge, kind);
	}

	@Override
	public <F> Iterable<F> getVertices(final String key, final Object value, final Class<F> kind) {
		org.openntf.domino.graph2.DElementStore store = null;
		DGraph base = (DGraph) this.getBaseGraph();
		store = base.findElementStore(kind);
		if (store != null) {
			String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getFramedVertexFormula(key, value, kind);
			Iterable<Vertex> vertices = store.getVertices(formulaFilter);
			return this.frameVertices(vertices, kind);
		} else {
			return null;
		}
	}

	public <F> F addEdge(final Object id, final Vertex outVertex, final Vertex inVertex, final String label) {
		if (id != null) {
			System.out.println("TEMP DEBUG Adding an edge with a forced id of " + String.valueOf(id));
		}
		return (F) super.addEdge(id, outVertex, inVertex, label);
	}

	@Override
	public <F> F addEdge(final Object id, final Vertex outVertex, final Vertex inVertex, final String label, final Direction direction,
			final Class<F> kind) {
		DGraph base = (DGraph) this.getBaseGraph();
		org.openntf.domino.graph2.DElementStore store = null;
		if (id instanceof NoteCoordinate) {
			store = base.findElementStore(id);
		} else {
			String typeid = getTypedId(id);
			if (typeid == null) {
				store = base.findElementStore(kind);
			} else {
				store = base.findElementStore(typeid);
			}
		}
		Edge edge = store.addEdge(id);
		((DEdge) edge).setLabel(label);
		((DEdge) edge).setInVertex(inVertex);
		((DEdge) edge).setOutVertex(outVertex);

		return frame(edge, kind);
	}

	@Override
	public <F> Iterable<F> getEdges(final String key, final Object value, final Class<F> kind) {
		org.openntf.domino.graph2.DElementStore store = null;
		DGraph base = (DGraph) this.getBaseGraph();
		store = base.findElementStore(kind);
		if (store != null) {
			String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getFramedEdgeFormula(key, value, kind);
			Iterable<Edge> edges = store.getEdges(formulaFilter);
			return this.frameEdges(edges, kind);
		} else {
			return null;
		}
	}

	//	public <F> F initializeAndFrame(final Edge edge, final Class<F> kind) {
	//		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
	//			initializer.initElement(kind, this, edge);
	//		}
	//		return frame(edge, kind);
	//	}
	//
	//	@Deprecated
	//	public <F> F initializeAndFrame(final Edge edge, final Direction direction, final Class<F> kind) {
	//		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
	//			initializer.initElement(kind, this, edge);
	//		}
	//		return frame(edge, direction, kind);
	//	}
	//
	//	public <F> F initializeAndFrame(final Vertex vertex, final Class<F> kind) {
	//		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
	//			initializer.initElement(kind, this, vertex);
	//		}
	//		return frame(vertex, kind);
	//	}

	public <F> Iterable<F> frameElements(final Iterable<Element> elements, final Class<F> kind) {
		Iterator<Element> it = elements.iterator();
		if (it.hasNext()) {
			Object chkElement = it.next();
			if (chkElement instanceof Edge) {
				return new FramedEdgeList(this, null, elements, kind);
			}
			if (chkElement instanceof Vertex) {
				return new FramedVertexList(this, null, elements, kind);
			}
		}

		return new FramedElementIterable(this, elements, kind);
	}

	public <F> F frame(final Element element, final Class<F> kind) {
		//		Class<F> klazz = kind;
		//		DConfiguration config = (DConfiguration) this.getConfig();
		//		config.getTypeManager().initElement(klazz, this, element);
		//		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
		//			if (!(initializer instanceof JavaFrameInitializer)) {
		//				initializer.initElement(klazz, this, element);
		//			}
		//		}
		@SuppressWarnings("deprecation")
		F result = null;
		if (element instanceof Edge) {
			//			klazz = (Class<F>) (klazz == null ? DEdgeFrame.class : kind);
			result = frame((Edge) element, kind);
		} else if (element instanceof Vertex) {
			//			klazz = (Class<F>) (klazz == null ? DVertexFrame.class : kind);
			result = frame((Vertex) element, kind);
		} else {
			throw new IllegalStateException("Cannot frame an element of type " + element.getClass().getName());
		}
		//		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
		//			if (initializer instanceof JavaFrameInitializer) {
		//				((JavaFrameInitializer) initializer).initElement(klazz, this, result);
		//			}
		//		}
		return result;
	}

	@Override
	public <F> F frame(final Edge edge, final Class<F> kind) {
		Class<F> klazz = (Class<F>) (kind == null ? DEdgeFrame.class : kind);
		DConfiguration config = (DConfiguration) this.getConfig();
		config.getTypeManager().initElement(klazz, this, edge);
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (!(initializer instanceof JavaFrameInitializer)) {
				initializer.initElement(klazz, this, edge);
			}
		}
		@SuppressWarnings("deprecation")
		F result = super.frame(edge, Direction.OUT, klazz);
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (initializer instanceof JavaFrameInitializer) {
				((JavaFrameInitializer) initializer).initElement(klazz, this, result);
			}
		}
		return result;
	}

	@Override
	@Deprecated
	public <F> F frame(final Edge edge, final Direction direction, final Class<F> kind) {
		Class<F> klazz = (Class<F>) (kind == null ? DEdgeFrame.class : kind);
		DConfiguration config = (DConfiguration) this.getConfig();
		config.getTypeManager().initElement(klazz, this, edge);
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (!(initializer instanceof JavaFrameInitializer)) {
				initializer.initElement(klazz, this, edge);
			}
		}
		F result = super.frame(edge, direction, klazz);
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (initializer instanceof JavaFrameInitializer) {
				((JavaFrameInitializer) initializer).initElement(klazz, this, result);
			}
		}
		return result;
	}

	@Override
	public <F> F frame(final Vertex vertex, Class<F> kind) {
		boolean isView = "1".equals(((DVertex) vertex).getProperty("$FormulaClass", String.class));
		if (isView) {
			kind = (Class<F>) ViewVertex.class;
		}
		Class<F> klazz = (Class<F>) (kind == null ? DVertexFrame.class : kind);
		DConfiguration config = (DConfiguration) this.getConfig();
		DTypeManager manager = config.getTypeManager();
		//		if (manager == null)
		//			System.out.println("TypeManager is null!??!??! How?");
		manager.initElement(klazz, this, vertex);
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (!(initializer instanceof JavaFrameInitializer)) {
				initializer.initElement(klazz, this, vertex);
			}
		}
		F result = super.frame(vertex, klazz);
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (initializer instanceof JavaFrameInitializer) {
				((JavaFrameInitializer) initializer).initElement(klazz, this, result);
			}
		}
		//		if (isView) {
		//			StringBuilder sb = new StringBuilder();
		//			Class<?>[] interfaces = result.getClass().getInterfaces();
		//			for (Class<?> inter : interfaces) {
		//				sb.append(inter.getName());
		//				sb.append(", ");
		//			}
		//			System.out.println("Requested a " + klazz.getName() + " and resulted in a [" + sb.toString() + "]");
		//		}
		return result;
	}

	public org.openntf.domino.graph2.DElementStore getElementStore(final Class<?> kind) {
		DGraph base = (DGraph) this.getBaseGraph();
		return base.findElementStore(kind);
	}

	public void addKeyResolver(final DKeyResolver resolver) {
		DGraph base = (DGraph) this.getBaseGraph();
		base.addKeyResolver(resolver);
	}

	public DKeyResolver getKeyResolver(final Class<?> type) {
		DGraph base = (DGraph) this.getBaseGraph();
		return base.getKeyResolver(type);
	}

}
