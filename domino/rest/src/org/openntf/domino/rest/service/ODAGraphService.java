package org.openntf.domino.rest.service;

import com.ibm.domino.das.service.IRestServiceExt;
import com.ibm.domino.das.service.RestService;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.openntf.domino.AutoMime;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.impl.DEdge;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.rest.resources.data.DataCollectionResource;
import org.openntf.domino.rest.resources.data.DataResource;
import org.openntf.domino.rest.resources.data.DocumentCollectionResource;
import org.openntf.domino.rest.resources.data.DocumentResource;
import org.openntf.domino.rest.resources.data.ViewCollectionResource;
import org.openntf.domino.rest.resources.data.ViewResource;
import org.openntf.domino.rest.resources.frames.EdgeFrameCollectionResource;
import org.openntf.domino.rest.resources.frames.EdgeFrameResource;
import org.openntf.domino.rest.resources.frames.FramedCollectionResource;
import org.openntf.domino.rest.resources.frames.FramedResource;
import org.openntf.domino.rest.resources.frames.VertexFrameCollectionResource;
import org.openntf.domino.rest.resources.frames.VertexFrameResource;
import org.openntf.domino.rest.resources.graph.EdgeCollectionResource;
import org.openntf.domino.rest.resources.graph.EdgeResource;
import org.openntf.domino.rest.resources.graph.GraphCollectionResource;
import org.openntf.domino.rest.resources.graph.GraphResource;
import org.openntf.domino.rest.resources.graph.VertexCollectionResource;
import org.openntf.domino.rest.resources.graph.VertexResource;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.Factory.ThreadConfig;
import org.openntf.domino.xsp.ODAPlatform;
import org.openntf.domino.xsp.session.DasCurrentSessionFactory;

public class ODAGraphService extends RestService implements IRestServiceExt {
	@SuppressWarnings("rawtypes")
	private Map<String, FramedGraph> graphMap_;
	private Map<String, IGraphFactory> factoryMap_;

	public ODAGraphService() {
		init();
	}

	protected void initDynamicGraphs() {
		// Get a list of all registered extensions

		IExtension extensions[] = null;
		final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		if (extensionRegistry != null) {
			final IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint("org.openntf.domino.rest.graph"); // $NON-NLS-1$
			if (extensionPoint != null) {
				extensions = extensionPoint.getExtensions();
			}
		}

		if (extensions == null) {
			return;
		}

		// Walk through each extension in the list

		for (final IExtension extension : extensions) {
			final IConfigurationElement configElements[] = extension.getConfigurationElements();
			if (configElements == null) {
				continue;
			}

			for (final IConfigurationElement configElement : configElements) {
				try {
					beforeDoService(null);
					// We only handle serviceResources elements for now
					if (!("serviceResources".equalsIgnoreCase(configElement.getName()))) { // $NON-NLS-1$
						continue;
					}

					final Object object = configElement.createExecutableExtension("class"); // $NON-NLS-1$
					if (!(object instanceof IGraphFactory)) {
						// Class was constructed but it is the wrong type
						continue;
					}

					System.out.println("Found an extension point instance");
					IGraphFactory factory = (IGraphFactory) object;
					Map<String, FramedGraph> registry = factory.getRegisteredGraphs();
					for (String key : registry.keySet()) {
						FramedGraph graph = registry.get(key);
						// System.out.println("Adding graph called " + key);
						addGraph(key, graph);
						addFactory(key, factory);
					}
				} catch (final CoreException e) {
					e.printStackTrace();
				} finally {
					afterDoService(null);
				}
			}
		}
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> supers = super.getClasses();
		Set<Class<?>> result = new HashSet<Class<?>>(supers);
		return result;
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> supers = super.getSingletons();
		Set<Object> result = new HashSet<Object>(supers);
		result.add(new DataResource(this));
		result.add(new DataCollectionResource(this));
		result.add(new ViewResource(this));
		result.add(new ViewCollectionResource(this));
		result.add(new DocumentResource(this));
		result.add(new DocumentCollectionResource(this));
		result.add(new FramedResource(this));
		result.add(new FramedCollectionResource(this));
		result.add(new VertexFrameResource(this));
		result.add(new VertexFrameCollectionResource(this));
		result.add(new EdgeFrameResource(this));
		result.add(new EdgeFrameCollectionResource(this));
		result.add(new GraphResource(this));
		result.add(new GraphCollectionResource(this));
		result.add(new EdgeResource(this));
		result.add(new EdgeCollectionResource(this));
		result.add(new VertexResource(this));
		result.add(new VertexCollectionResource(this));
		return result;
	}

	protected Map<String, FramedGraph> getGraphMap() {
		if (graphMap_ == null) {
			graphMap_ = new HashMap<String, FramedGraph>();
		}
		return graphMap_;
	}

	protected Map<String, IGraphFactory> getFactoryMap() {
		if (factoryMap_ == null) {
			factoryMap_ = new HashMap<String, IGraphFactory>();
		}
		return factoryMap_;
	}

	public void addFactory(String name, IGraphFactory factory) {
		getFactoryMap().put(name, factory);
	}

	public IGraphFactory getFactory(String name) {
		return getFactoryMap().get(name);
	}

	@SuppressWarnings("rawtypes")
	public DFramedTransactionalGraph getGraph(String name) {
		return (DFramedTransactionalGraph) getGraphMap().get(name);
	}

	public void addGraph(String name, FramedGraph graph) {
		getGraphMap().put(name, graph);
	}

	public FramedGraph removeGraph(String name) {
		return getGraphMap().remove(name);
	}

	public void init() {
		try {
			ODAPlatform.start();
			initDynamicGraphs();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		ODAPlatform.stop();
	}

	protected ThreadConfig getDataServiceConfig() {
		Fixes[] fixes = Fixes.values();
		AutoMime autoMime = AutoMime.WRAP_32K;
		boolean bubbleExceptions = false;
		return new ThreadConfig(fixes, autoMime, bubbleExceptions);
	}

	@Override
	public boolean beforeDoService(HttpServletRequest request) {
		Factory.initThread(getDataServiceConfig());
		Factory.setSessionFactory(new DasCurrentSessionFactory(), SessionType.CURRENT);
		return true;
	}

	@Override
	public void afterDoService(HttpServletRequest request) {
		Factory.termThread();
	}

	@Override
	public void onError(HttpServletRequest request, Throwable t) {
		Factory.termThread();
		t.printStackTrace();
	}

	protected List<Map<String, Object>> toJsonableArray(DFramedTransactionalGraph graph,
			final Iterable<EdgeFrame> edges, List<CaseInsensitiveString> properties,
			List<CaseInsensitiveString> inProps, List<CaseInsensitiveString> outProps) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (EdgeFrame edge : edges) {
			result.add(toJsonableMapEdge(graph, edge, properties, inProps, outProps, null, false));
		}
		return result;
	}

	// NTF - Why is this method protected, Nathan?
	// Because it's a hideous mess, of course!
	protected Map<String, Object> toJsonableMapVertex(DFramedTransactionalGraph graph, final VertexFrame frame,
			List<CaseInsensitiveString> properties, List<CaseInsensitiveString> inProps,
			List<CaseInsensitiveString> outProps, List<CaseInsensitiveString> labels, boolean includeEdges) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("@id", frame.asVertex().getId().toString());
		result.put("@type", graph.getTypeManager().resolve(frame).getName());
		Class<?>[] interfaces = frame.getClass().getInterfaces();
		if (interfaces.length > 0) {
			Map<CaseInsensitiveString, Method> crystals = graph.getTypeRegistry().getPropertiesGetters(interfaces);
			if (properties == null) {
				properties = new ArrayList<CaseInsensitiveString>(crystals.keySet());
			}
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
					// System.out.println("No method found for key " +
					// property);
				}
			}
			if (includeEdges) {
				Map<String, Integer> edgeCounts = new LinkedHashMap<String, Integer>();
				crystals = graph.getTypeRegistry().getCounters(interfaces);
				for (CaseInsensitiveString key : crystals.keySet()) {
					Method crystal = crystals.get(key);
					if (crystal != null) {
						try {
							Object raw = crystal.invoke(frame, (Object[]) null);
							if (raw instanceof Integer) {
								edgeCounts.put(key.toString(), (Integer) raw);
							} else {
								// System.out.println("Count method " +
								// crystal.getName() + " on a frame of type "
								// + frame.getClass().getName() + " returned a "
								// + (raw == null ? "null" :
								// raw.getClass().getName()));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						// System.out.println("No method found for key " + key);
					}
				}

				if (!edgeCounts.isEmpty()) {
					result.put("@edges", edgeCounts);
				}
			}
			if (labels != null && labels.size() > 0) {
				System.out.println("Found some labels");

				crystals = graph.getTypeRegistry().getIncidences(interfaces);
				for (CaseInsensitiveString label : labels) {
					System.out.println("Processing label " + label);
					Method crystal = crystals.get(label);
					if (crystal != null) {
						try {
							Object raw = crystal.invoke(frame, (Object[]) null);
							if (raw instanceof Iterable) {
								List edges = toJsonableArray(graph, (Iterable) raw, null, inProps, outProps);
								result.put(label.toString(), edges);
							} else if (raw instanceof EdgeFrame) {
								List edges = new ArrayList();
								edges.add(toJsonableMapEdge(graph, (EdgeFrame) raw, null, inProps, outProps, null,
										false));
								result.put(label.toString(), edges);
							} else {
								System.out.println("Result of invokation for label " + label.toString() + " is a "
										+ (raw == null ? "null" : raw.getClass().getName()));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("No method found for label " + label);
					}
				}

			}
		}
		return result;

	}

	public Map<String, Object> toJsonableMap(DFramedTransactionalGraph graph, final VertexFrame frame, ParamMap pm) {
		List<CaseInsensitiveString> properties = null;
		List<CaseInsensitiveString> inProps = null;
		List<CaseInsensitiveString> outProps = null;
		List<CaseInsensitiveString> labels = null;
		boolean includeEdges = false;
		if (pm != null) {
			properties = CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.PROPS));
			inProps = CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.INPROPS));
			outProps = CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.OUTPROPS));
			labels = CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.LABEL));
			includeEdges = pm.get(Parameters.EDGES) != null;
		}
		return toJsonableMapVertex(graph, frame, properties, inProps, outProps, labels, includeEdges);

	}

	public Map<String, Object> toJsonableMap(DFramedTransactionalGraph graph, final Object frame, ParamMap pm) {
		List<CaseInsensitiveString> properties = null;
		List<CaseInsensitiveString> inProps = null;
		List<CaseInsensitiveString> outProps = null;
		List<CaseInsensitiveString> labels = null;
		boolean includeEdges = false;
		if (pm != null) {
			properties = CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.PROPS));
			inProps = CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.INPROPS));
			outProps = CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.OUTPROPS));
			labels = CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.LABEL));
			includeEdges = pm.get(Parameters.EDGES) != null;
		}

		if (frame == null)
			return null;
		if (frame instanceof VertexFrame) {
			return toJsonableMapVertex(graph, (VertexFrame) frame, properties, inProps, outProps, labels, includeEdges);
		} else if (frame instanceof EdgeFrame) {
			return toJsonableMapEdge(graph, (EdgeFrame) frame, properties, inProps, outProps, labels, includeEdges);
		} else {
			throw new IllegalStateException("Object is neither a VertexFrame nor an EdgeFrame. It's a "
					+ frame.getClass().getName());
		}
	}

	public Map<String, Object> toJsonableMap(DFramedTransactionalGraph graph, final Object frame) {
		return toJsonableMap(graph, frame, (ParamMap) null);
	}

	public Map<String, Object> toJsonableMap(DFramedTransactionalGraph graph, final VertexFrame frame) {
		return toJsonableMap(graph, frame, (ParamMap) null);
	}

	public Map<String, Object> toJsonableMap(DFramedTransactionalGraph graph,
			final Class<? extends VertexFrame> vertexClass) {
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

	protected Map<String, Object> toJsonableMapEdge(DFramedTransactionalGraph graph, final EdgeFrame frame,
			List<CaseInsensitiveString> properties, List<CaseInsensitiveString> inProps,
			List<CaseInsensitiveString> outProps, List<CaseInsensitiveString> labels, boolean includeEdges) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		DEdge dedge = (DEdge) frame.asEdge();
		Class<?> type = graph.getTypeManager().resolve(frame);
		// System.out.println("TEMP DEBUG: type has resolved to " +
		// type.getName() + " while form is "
		// + String.valueOf(dedge.getProperty("form")));

		result.put("@id", dedge.getId().toString());
		result.put("@type", type.getName());

		if (inProps == null) {
			Map<String, Object> in = new LinkedHashMap<String, Object>();
			in.put("@id", dedge.getVertexId(Direction.IN).toString());
			Class<?> inType = graph.getTypeRegistry().getInType(type);
			if (inType == null) {
				// System.out.println("TEMP DEBUG: Unable to find In type for edge "
				// + type.getName());
				in.put("@type", "Vertex");
			} else {
				in.put("@type", inType.getName());
			}
			result.put("@in", in);
		} else {
			Method inMethod = graph.getTypeRegistry().getIn(type);
			if (inMethod != null) {
				try {
					Object raw = inMethod.invoke(frame, (Object[]) null);
					if (raw instanceof VertexFrame) {
						VertexFrame inFrame = (VertexFrame) raw;
						Map<String, Object> in = toJsonableMapVertex(graph, inFrame, inProps, null, null, null,
								includeEdges);
						result.put("@in", in);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// System.out.println("No in method found in Edge " +
				// frame.getClass().getName());
			}
		}

		if (outProps == null) {
			Map<String, Object> out = new LinkedHashMap<String, Object>();
			out.put("@id", dedge.getVertexId(Direction.OUT).toString());
			Class<?> outType = graph.getTypeRegistry().getOutType(type);
			if (outType == null) {
				out.put("@type", "Vertex");
			} else {
				out.put("@type", outType.getName());
			}
			result.put("@out", out);
		} else {
			Method outMethod = graph.getTypeRegistry().getOut(type);
			if (outMethod != null) {
				try {
					Object raw = outMethod.invoke(frame, (Object[]) null);
					if (raw instanceof VertexFrame) {
						VertexFrame outFrame = (VertexFrame) raw;
						Map<String, Object> out = toJsonableMapVertex(graph, outFrame, outProps, null, null, null,
								includeEdges);
						result.put("@out", out);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// System.out.println("No out method found in Edge " +
				// frame.getClass().getName());
			}
		}

		result.put("Label", dedge.getLabel());
		Class<?>[] interfaces = frame.getClass().getInterfaces();
		if (interfaces.length > 0) {
			Map<CaseInsensitiveString, Method> crystals = graph.getTypeRegistry().getPropertiesGetters(interfaces);
			if (properties == null)
				properties = new ArrayList<CaseInsensitiveString>(crystals.keySet());
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
					// System.out.println("No method found for key " +
					// property);
				}
			}
		}
		return result;

	}

	public Map<String, Object> toJsonableMap(DFramedTransactionalGraph graph, final EdgeFrame frame, ParamMap pm) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		List<CaseInsensitiveString> properties = null;
		List<CaseInsensitiveString> inProps = null;
		List<CaseInsensitiveString> outProps = null;
		List<CaseInsensitiveString> labels = null;
		boolean includeEdges = false;
		if (pm != null) {
			properties = CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.PROPS));
			inProps = CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.INPROPS));
			outProps = CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.OUTPROPS));
			labels = CaseInsensitiveString.toCaseInsensitive(pm.get(Parameters.LABEL));
			includeEdges = pm.get(Parameters.EDGES) != null;
		}
		return toJsonableMapEdge(graph, frame, properties, inProps, outProps, labels, includeEdges);
	}

	public Map<String, Object> toJsonableMap(DFramedTransactionalGraph graph, final EdgeFrame frame) {
		return toJsonableMap(graph, frame, null);
	}

	@SuppressWarnings("unchecked")
	public EdgeFrame toEdgeFrame(DFramedTransactionalGraph graph, final Map<String, Object> map) {
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
			// new vertex
			if (type == null) {
				// what are we going to create? How do we even know?
			} else {
				// result = addEdge(null, type);
			}
		} else {
			if (type == null) {
				try {
					result = (EdgeFrame) graph.getEdge(id, null);
				} catch (Throwable t) {
					// TODO NTF
					t.printStackTrace();
				}
			} else {
				// result = addEdge(id, type);
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public VertexFrame toVertexFrame(DFramedTransactionalGraph graph, final Map<String, Object> map) {
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
			// new vertex
			if (type == null) {
				// what are we going to create? How do we even know?
			} else {
				result = (VertexFrame) graph.addVertex(null, type);
			}
		} else {
			if (type == null) {
				try {
					result = (VertexFrame) graph.getVertex(id, null);
				} catch (Throwable t) {
					// TODO NTF
					t.printStackTrace();
				}
			} else {
				result = (VertexFrame) graph.addVertex(id, type);
			}
		}
		if (result != null) {
			Class<?>[] interfaces = result.getClass().getInterfaces();
			if (interfaces.length > 0) {
				Map<CaseInsensitiveString, Method> crystals = graph.getTypeRegistry().getPropertiesSetters(interfaces);
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
						// System.out.println("No method found for key " + key);
					}
				}
			}
		}
		return result;
	}

}
