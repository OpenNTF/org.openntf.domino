package org.openntf.domino.graph2.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openntf.domino.big.impl.NoteCoordinate;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
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
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FrameInitializer;
import com.tinkerpop.frames.FramedGraphConfiguration;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.javahandler.JavaFrameInitializer;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

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

	@SuppressWarnings("unchecked")
	public EdgeFrame toEdgeFrame(final Map<String, Object> map) {
		EdgeFrame result = null;
		Object id = map.get("id");
		Object typeName = map.get("type");
		Class<? extends EdgeFrame> type = null;
		if (typeName != null) {
			try {
				type = (Class<? extends EdgeFrame>) Class.forName(String.valueOf(typeName));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		if (id == null) {
			//new vertex
			if (type == null) {
				//what are we going to create? How do we even know?
			} else {
				//				result = addEdge(null, type);
			}
		} else {
			if (type == null) {
				try {
					result = getEdge(id, null);
				} catch (Throwable t) {
					//TODO NTF
					t.printStackTrace();
				}
			} else {
				//				result = addEdge(id, type);
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public VertexFrame toVertexFrame(final Map<String, Object> map) {
		VertexFrame result = null;
		Object id = map.get("id");
		Object typeName = map.get("type");
		Class<? extends VertexFrame> type = null;
		if (typeName != null) {
			try {
				type = (Class<? extends VertexFrame>) Class.forName(String.valueOf(typeName));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		if (id == null) {
			//new vertex
			if (type == null) {
				//what are we going to create? How do we even know?
			} else {
				result = addVertex(null, type);
			}
		} else {
			if (type == null) {
				try {
					result = getVertex(id, null);
				} catch (Throwable t) {
					//TODO NTF
					t.printStackTrace();
				}
			} else {
				result = addVertex(id, type);
			}
		}
		if (result != null) {
			Class<?>[] interfaces = result.getClass().getInterfaces();
			if (interfaces.length > 0) {
				Map<CaseInsensitiveString, Method> crystals = getTypeRegistry().getPropertiesSetters(interfaces);
				for (CaseInsensitiveString key : crystals.keySet()) {
					Method crystal = crystals.get(key);
					if (crystal != null) {
						try {
							Object raw = map.get(key);
							crystal.invoke(result, raw);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						//						System.out.println("No method found for key " + key);
					}
				}
			}
		}
		return result;
	}

	public List<Map<String, Object>> toJsonableArray(final Iterable<EdgeFrame> edges) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (EdgeFrame edge : edges) {
			result.add(toJsonableMap(edge));
		}
		return result;
	}

	public Map<String, Object> toJsonableMap(final VertexFrame frame, Iterable<CaseInsensitiveString> properties) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("@id", frame.asVertex().getId().toString());
		result.put("@type", getTypeManager().resolve(frame).getName());
		Class<?>[] interfaces = frame.getClass().getInterfaces();
		boolean includeEdges = false;
		if (interfaces.length > 0) {
			Map<CaseInsensitiveString, Method> crystals = getTypeRegistry().getPropertiesGetters(interfaces);
			if (properties == null)
				properties = crystals.keySet();
			for (CaseInsensitiveString property : properties) {
				if (!property.equals("@edges")) {
					Method crystal = crystals.get(property);
					if (crystal != null) {
						try {
							Object raw = crystal.invoke(frame, (Object[]) null);
							result.put(property.toString(), raw);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						//						System.out.println("No method found for key " + property);
					}
				} else {
					includeEdges = true;
				}
			}
			if (includeEdges) {
				Map<String, Integer> edgeCounts = new LinkedHashMap<String, Integer>();
				crystals = getTypeRegistry().getCounters(interfaces);
				for (CaseInsensitiveString key : crystals.keySet()) {
					Method crystal = crystals.get(key);
					if (crystal != null) {
						try {
							Object raw = crystal.invoke(frame, (Object[]) null);
							if (raw instanceof Integer) {
								edgeCounts.put(key.toString(), (Integer) raw);
							} else {
								//								System.out.println("Count method " + crystal.getName() + " on a frame of type "
								//										+ frame.getClass().getName() + " returned a " + (raw == null ? "null" : raw.getClass().getName()));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						//						System.out.println("No method found for key " + key);
					}
				}

				if (!edgeCounts.isEmpty()) {
					result.put("@edges", edgeCounts);
				}
			}
		}
		return result;
	}

	public Map<String, Object> toJsonableMap(final Object frame, final Iterable<CaseInsensitiveString> properties,
			final Iterable<CaseInsensitiveString> inProps, final Iterable<CaseInsensitiveString> outProps) {
		if (frame == null)
			return null;
		if (frame instanceof VertexFrame) {
			return toJsonableMap((VertexFrame) frame, properties);
		} else if (frame instanceof EdgeFrame) {
			return toJsonableMap((EdgeFrame) frame, properties, inProps, outProps);
		} else {
			throw new IllegalStateException("Object is neither a VertexFrame nor an EdgeFrame. It's a " + frame.getClass().getName());
		}
	}

	public Map<String, Object> toJsonableMap(final Object frame, final Iterable<CaseInsensitiveString> properties) {
		if (frame == null)
			return null;
		if (frame instanceof VertexFrame) {
			return toJsonableMap((VertexFrame) frame, properties);
		} else if (frame instanceof EdgeFrame) {
			return toJsonableMap((EdgeFrame) frame, properties);
		} else {
			throw new IllegalStateException("Object is neither a VertexFrame nor an EdgeFrame. It's a " + frame.getClass().getName());
		}
	}

	public Map<String, Object> toJsonableMap(final Object frame) {
		return toJsonableMap(frame, null);
	}

	public Map<String, Object> toJsonableMap(final VertexFrame frame) {
		return toJsonableMap(frame, (Iterable<CaseInsensitiveString>) null);
	}

	public Map<String, Object> toJsonableMap(final Class<? extends VertexFrame> vertexClass) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("@name", vertexClass.getName());
		result.put("@type", "Vertex");
		TypeValue tv = vertexClass.getAnnotation(TypeValue.class);
		if (tv != null) {
			result.put("@typevalue", tv.value());
		}
		for (Method crystal : vertexClass.getMethods()) {
			TypedProperty tp = crystal.getAnnotation(TypedProperty.class);
			if (tp != null) {

			}
			Property p = crystal.getAnnotation(Property.class);
			if (p != null) {

			}
		}
		return result;
	}

	public Map<String, Object> toJsonableMap(final EdgeFrame frame, Iterable<CaseInsensitiveString> properties,
			final Iterable<CaseInsensitiveString> inProps, final Iterable<CaseInsensitiveString> outProps) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		DEdge dedge = (DEdge) frame.asEdge();
		Class<?> type = getTypeManager().resolve(frame);
		//		System.out.println("TEMP DEBUG: type has resolved to " + type.getName() + " while form is "
		//				+ String.valueOf(dedge.getProperty("form")));

		result.put("@id", dedge.getId().toString());
		result.put("@type", type.getName());

		if (inProps == null) {
			Map<String, Object> in = new LinkedHashMap<String, Object>();
			in.put("@id", dedge.getVertexId(Direction.IN).toString());
			Class<?> inType = getTypeRegistry().getInType(type);
			if (inType == null) {
				//				System.out.println("TEMP DEBUG: Unable to find In type for edge " + type.getName());
				in.put("@type", "Vertex");
			} else {
				in.put("@type", inType.getName());
			}
			result.put("@in", in);
		} else {
			Method inMethod = getTypeRegistry().getIn(type);
			if (inMethod != null) {
				try {
					Object raw = inMethod.invoke(frame, (Object[]) null);
					if (raw instanceof VertexFrame) {
						VertexFrame inFrame = (VertexFrame) raw;
						Map<String, Object> in = toJsonableMap(inFrame, inProps);
						result.put("@in", in);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				//				System.out.println("No in method found in Edge " + frame.getClass().getName());
			}
		}

		if (outProps == null) {
			Map<String, Object> out = new LinkedHashMap<String, Object>();
			out.put("@id", dedge.getVertexId(Direction.OUT).toString());
			Class<?> outType = getTypeRegistry().getOutType(type);
			if (outType == null) {
				out.put("@type", "Vertex");
			} else {
				out.put("@type", outType.getName());
			}
			result.put("@out", out);
		} else {
			Method outMethod = getTypeRegistry().getOut(type);
			if (outMethod != null) {
				try {
					Object raw = outMethod.invoke(frame, (Object[]) null);
					if (raw instanceof VertexFrame) {
						VertexFrame outFrame = (VertexFrame) raw;
						Map<String, Object> out = toJsonableMap(outFrame, outProps);
						result.put("@out", out);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				//				System.out.println("No out method found in Edge " + frame.getClass().getName());
			}
		}

		result.put("Label", dedge.getLabel());
		Class<?>[] interfaces = frame.getClass().getInterfaces();
		if (interfaces.length > 0) {
			Map<CaseInsensitiveString, Method> crystals = getTypeRegistry().getPropertiesGetters(interfaces);
			if (properties == null)
				properties = crystals.keySet();
			for (CaseInsensitiveString property : properties) {
				Method crystal = crystals.get(property);
				if (crystal != null) {
					try {
						Object raw = crystal.invoke(frame, (Object[]) null);
						result.put(property.toString(), raw);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					//					System.out.println("No method found for key " + property);
				}
			}
		}
		return result;
	}

	public Map<String, Object> toJsonableMap(final EdgeFrame frame) {
		return toJsonableMap(frame, null, null, null);
	}

	public Map<String, Object> toJsonableMap(final EdgeFrame frame, final Iterable<CaseInsensitiveString> properties) {
		return toJsonableMap(frame, properties, null, null);
	}

	protected DTypeRegistry getTypeRegistry() {
		Graph graph = this.getBaseGraph();
		if (graph instanceof DGraph) {
			DConfiguration config = (DConfiguration) ((DGraph) graph).getConfiguration();
			return config.getTypeRegistry();
		}
		return null;
	}

	protected DTypeManager getTypeManager() {
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

	public <F> F getElement(final Object id, final Class<F> kind) {
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
		Element elem = store.getElement(id);
		if (null == elem) {
			return null;
		}
		if (elem instanceof Edge) {
			return frame((Edge) elem, kind);
		} else if (elem instanceof Vertex) {
			return frame((Vertex) elem, kind);
		} else {
			throw new IllegalStateException("Key " + id.toString() + " returned an element of type " + elem.getClass().getName());
		}
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
		return new FramedElementIterable(this, elements, kind);
	}

	public <F> F frame(final Element element, final Class<F> kind) {
		Class<F> klazz = kind;
		DConfiguration config = (DConfiguration) this.getConfig();
		config.getTypeManager().initElement(klazz, this, element);
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (!(initializer instanceof JavaFrameInitializer)) {
				initializer.initElement(klazz, this, element);
			}
		}
		@SuppressWarnings("deprecation")
		F result = null;
		if (element instanceof Edge) {
			klazz = (Class<F>) (klazz == null ? DEdgeFrame.class : kind);
			result = frame((Edge) element, klazz);
		} else if (element instanceof Vertex) {
			klazz = (Class<F>) (klazz == null ? DVertexFrame.class : kind);
			result = frame((Vertex) element, klazz);
		} else {
			throw new IllegalStateException("Cannot frame an element of type " + element.getClass().getName());
		}
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (initializer instanceof JavaFrameInitializer) {
				((JavaFrameInitializer) initializer).initElement(klazz, this, result);
			}
		}
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
	public <F> F frame(final Vertex vertex, final Class<F> kind) {
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
		return result;
	}

}
