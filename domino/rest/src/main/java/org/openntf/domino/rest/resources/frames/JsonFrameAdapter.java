package org.openntf.domino.rest.resources.frames;

import com.ibm.commons.util.io.json.JsonObject;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.VertexFrame;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openntf.domino.View;
import org.openntf.domino.ViewColumn;
import org.openntf.domino.graph2.DEdgeList;
import org.openntf.domino.graph2.DGraphUtils;
import org.openntf.domino.graph2.annotations.FramedEdgeList;
import org.openntf.domino.graph2.annotations.FramedVertexList;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.ViewVertex;
import org.openntf.domino.graph2.impl.DEdge;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DProxyVertex;
import org.openntf.domino.graph2.impl.DVertex;
import org.openntf.domino.graph2.impl.DVertexList;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.TypeUtils;

public class JsonFrameAdapter implements JsonObject {
	private final static List<String> EMPTY_STRINGS = new ArrayList<String>();
	protected final boolean isCollectionRoute_;

	// TODO NTF Add support for modification date checking prior to permitting
	// PUT/PATCH
	static private boolean ifUnmodifiedSince(Object object, Date ifUnmodifiedSince) {
		if (object instanceof DEdgeFrame) {
			Date mod = ((DEdgeFrame) object).getModified();
			return !mod.after(ifUnmodifiedSince);
		} else if (object instanceof DVertexFrame) {
			Date mod = ((DVertexFrame) object).getModified();
			return !mod.after(ifUnmodifiedSince);
		} else {
			throw new IllegalArgumentException("Cannot verify modification time of a non-framed object: "
					+ object.getClass().getName());
		}
	}

	public static VertexFrame toVertexFrame(DFramedTransactionalGraph graph, Edge edge, Vertex source) {
		VertexFrame result = null;
		Vertex other = null;
		if (edge instanceof DEdge) {
			try {
				other = ((DEdge) edge).getOtherVertex(source);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} else {
			System.out.println("TEMP DEBUG edge is actually a " + edge.getClass().getName());
		}
		result = (VertexFrame) graph.frame(other, null);
		return result;
	}

	protected Object frame_;
	@SuppressWarnings("rawtypes")
	protected DFramedTransactionalGraph graph_;
	protected Class<?> type_;
	protected Map<CaseInsensitiveString, Method> getters_;
	protected Map<CaseInsensitiveString, Method> incidences_;
	protected Map<CaseInsensitiveString, Method> counters_;
	protected Map<CaseInsensitiveString, Method> setters_;
	protected ParamMap parameters_;

	@SuppressWarnings("rawtypes")
	public JsonFrameAdapter(DFramedTransactionalGraph graph, EdgeFrame frame, ParamMap pm, boolean isCollectionRoute) {
		graph_ = graph;
		frame_ = frame;
		parameters_ = pm;
		type_ = graph_.getTypeManager().resolve(frame);
		isCollectionRoute_ = isCollectionRoute;
	}

	@SuppressWarnings("rawtypes")
	public JsonFrameAdapter(DFramedTransactionalGraph graph, VertexFrame frame, ParamMap pm, boolean isCollectionRoute) {
		graph_ = graph;
		frame_ = frame;
		parameters_ = pm;
		type_ = graph_.getTypeManager().resolve(frame);
		isCollectionRoute_ = isCollectionRoute;
	}

	public Object getFrame() {
		return frame_;
	}

	@SuppressWarnings("rawtypes")
	public DFramedTransactionalGraph getGraph() {
		return graph_;
	}

	public List<CharSequence> getProperties() {
		if (parameters_ != null) {
			return parameters_.getProperties();
		}
		return null;
	}

	public List<CharSequence> getHideProperties() {
		if (parameters_ != null) {
			return parameters_.getHideProperties();
		}
		return null;
	}

	public List<CharSequence> getInProperties() {
		if (parameters_ != null) {
			return parameters_.getInProperties();
		}
		return null;
	}

	public List<CharSequence> getOutProperties() {
		if (parameters_ != null) {
			return parameters_.getOutProperties();
		}
		return null;
	}

	public List<CharSequence> getLabels() {
		if (parameters_ != null) {
			return parameters_.getLabels();
		}
		return null;
	}

	public List<CharSequence> getFilterKeys() {
		if (parameters_ != null) {
			return parameters_.getFilterKeys();
		}
		return null;
	}

	public List<CharSequence> getFilterValues() {
		if (parameters_ != null) {
			return parameters_.getFilterValues();
		}
		return null;
	}

	public List<CharSequence> getOrderBys() {
		if (parameters_ != null) {
			return parameters_.getOrderBys();
		}
		return null;
	}

	public int getStart() {
		if (parameters_ != null) {
			return parameters_.getStart();
		}
		return 0;
	}

	public int getCount() {
		if (parameters_ != null) {
			return parameters_.getCount();
		}
		return 0;
	}

	public boolean getIncludeEdges() {
		if (parameters_ != null) {
			return parameters_.getIncludeEdges();
		}
		return false;
	}

	public boolean getDescending() {
		if (parameters_ != null) {
			return parameters_.getDescending();
		}
		return false;
	}

	public boolean getIncludeCounts() {
		if (parameters_ != null) {
			return parameters_.getIncludeCounts();
		}
		return false;
	}

	public boolean getIncludeVertices() {
		if (parameters_ != null) {
			return parameters_.getIncludeVertices();
		}
		return false;
	}

	public Map<CaseInsensitiveString, Method> getCounters() {
		if (counters_ == null) {
			counters_ = getGraph().getTypeRegistry().getCounters(type_);
		}
		if (counters_.size() == 0) {
			// System.out.println("TEMP DEBUG No counters found for type " +
			// type_.getName() + " and we found "
			// + getIncidences().size() + " incidences");
		}
		return counters_;
	}

	public Map<CaseInsensitiveString, Method> getGetters() {
		if (getters_ == null) {
			getters_ = getGraph().getTypeRegistry().getPropertiesGetters(type_);
		}
		return getters_;
	}

	public Map<CaseInsensitiveString, Method> getIncidences() {
		if (incidences_ == null) {
			incidences_ = getGraph().getTypeRegistry().getIncidences(type_);
		}
		return incidences_;
	}

	public Map<CaseInsensitiveString, Method> getSetters() {
		if (setters_ == null) {
			setters_ = getGraph().getTypeRegistry().getPropertiesSetters(type_);
		}
		return setters_;
	}

	@Override
	public Iterator<String> getJsonProperties() {
		// System.out
		// .println("TEMP DEBUG getting Json properties list for a frame of type "
		// + frame_.getClass().getName());
		List<String> result = new ArrayList<String>();
		result.add("@id");
		result.add("@type");
		Collection<CharSequence> props = getProperties();
		if (props == null) {
			props = new ArrayList<CharSequence>();
			props.addAll(getGetters().keySet());
			if (props == null || props.size() < 4) {
				if (frame_ instanceof DVertexFrame) {
					Set<CharSequence> raw = ((DVertexFrame) frame_).asMap().keySet();
					props.addAll(CaseInsensitiveString.toCaseInsensitive(raw));
				} else if (frame_ instanceof DEdgeFrame) {
					// Set<CharSequence> raw = ((DEdgeFrame)
					// frame_).asMap().keySet();
					// props.addAll(CaseInsensitiveString.toCaseInsensitive(raw));
				}
			}
		}
		for (CharSequence cis : props) {
			result.add(cis.toString());
			// System.out.println("Adding " + cis.toString());
		}
		Object frame = getFrame();
		if (frame instanceof VertexFrame && getIncludeEdges()) {
			result.add("@edges");
		}
		if (frame instanceof VertexFrame && getIncludeCounts()) {
			for (CaseInsensitiveString key : getCounters().keySet()) {
				result.add("@counts" + key.toString());
			}
		}
		if (frame instanceof VertexFrame) {
			Vertex v = ((VertexFrame) frame).asVertex();
			if (v instanceof DProxyVertex) {
				result.add("@proxyid");
			}
		}
		if (frame instanceof VertexFrame && getLabels() != null) {
			for (CharSequence cis : getLabels()) {
				result.add("#" + cis.toString());
			}
		}
		if (frame instanceof EdgeFrame) {
			result.add("@in");
			result.add("@out");
		}
		if (frame instanceof ViewVertex) {
			result.add("@columninfo");
		}
		if (frame instanceof ViewVertex.Contains) {
			Edge edge = ((ViewVertex.Contains) frame).asEdge();
			if (edge instanceof DEdge) {
				result.addAll(((DEdge) edge).getDelegate().keySet());
			}
		}
		Collection<CharSequence> hideProps = getHideProperties();
		if (hideProps != null && !hideProps.isEmpty()) {
			for (CharSequence cis : hideProps) {
				result.remove(cis.toString());
			}
		}
		return result.iterator();
	}

	@Override
	public Object getJsonProperty(String paramKey) {
		Object result = null;
		Object frame = getFrame();
		if (frame != null) {
			CaseInsensitiveString key = new CaseInsensitiveString(paramKey);
			if (key.equals("@id")) {
				if (frame instanceof VertexFrame) {
					result = ((VertexFrame) frame).asVertex().getId().toString();
				} else if (frame instanceof EdgeFrame) {
					result = ((EdgeFrame) frame).asEdge().getId().toString();
				}
			} else if (key.equals("@proxyid")) {
				// System.out.println("TEMP DEBUG @proxyid requested");
				if (frame instanceof VertexFrame) {
					Vertex v = ((VertexFrame) frame).asVertex();
					if (v instanceof DProxyVertex) {
						result = ((DProxyVertex) v).getProperty(DProxyVertex.PROXY_ITEM, String.class);
					}
				}
			} else if (key.equals("@type")) {
				if (frame instanceof VertexFrame) {
					result = type_;
				} else if (frame instanceof EdgeFrame) {
					result = type_;
				}
			} else if (key.equals("@in") && frame instanceof EdgeFrame) {
				if (getInProperties() == null) {
					// why not just make a frame adapter with the vertex?
					// because that's another I/O operation. We already have the
					// information needed to
					DEdge dedge = (DEdge) ((EdgeFrame) frame).asEdge();
					Map<String, String> minProps = new LinkedHashMap<String, String>();
					minProps.put("@id", dedge.getVertexId(Direction.IN).toString());
					Class<?> inType = graph_.getTypeRegistry().getInType(type_);
					if (inType == null) {
						minProps.put("@type", "Vertex");
					} else {
						minProps.put("@type", inType.getName());
					}
					result = minProps;
				} else {
					ParamMap inMap = new ParamMap();
					inMap.put(Parameters.PROPS, CaseInsensitiveString.toStrings(getInProperties()));
					if (getIncludeEdges()) {
						inMap.put(Parameters.EDGES, EMPTY_STRINGS);
					}
					if (getIncludeCounts()) {
						inMap.put(Parameters.COUNTS, EMPTY_STRINGS);
					}
					Method inMethod = graph_.getTypeRegistry().getIn(type_);
					if (inMethod != null) {
						try {
							Object raw = inMethod.invoke(frame, (Object[]) null);
							if (raw instanceof VertexFrame) {
								VertexFrame inFrame = (VertexFrame) raw;
								result = new JsonFrameAdapter(graph_, inFrame, inMap, isCollectionRoute_);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else if (key.equals("@out") && frame instanceof EdgeFrame) {
				if (getOutProperties() == null) {
					// why not just make a frame adapter with the vertex?
					// because that's another I/O operation. We already have the
					// information needed to
					DEdge dedge = (DEdge) ((EdgeFrame) frame).asEdge();
					Map<String, String> minProps = new LinkedHashMap<String, String>();
					minProps.put("@id", dedge.getVertexId(Direction.OUT).toString());
					Class<?> outType = graph_.getTypeRegistry().getOutType(type_);
					if (outType == null) {
						minProps.put("@type", "Vertex");
					} else {
						minProps.put("@type", outType.getName());
					}
					result = minProps;
				} else {
					ParamMap outMap = new ParamMap();
					outMap.put(Parameters.PROPS, CaseInsensitiveString.toStrings(getOutProperties()));
					if (getIncludeEdges()) {
						outMap.put(Parameters.EDGES, EMPTY_STRINGS);
					}
					if (getIncludeCounts()) {
						outMap.put(Parameters.COUNTS, EMPTY_STRINGS);
					}
					Method outMethod = graph_.getTypeRegistry().getOut(type_);
					if (outMethod != null) {
						try {
							Object raw = outMethod.invoke(frame, (Object[]) null);
							if (raw instanceof VertexFrame) {
								VertexFrame outFrame = (VertexFrame) raw;
								result = new JsonFrameAdapter(graph_, outFrame, outMap, isCollectionRoute_);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else if (key.equals("@edges")) {
				Map<String, Integer> edgeCounts = new LinkedHashMap<String, Integer>();
				Set<CaseInsensitiveString> counterKeys = getCounters().keySet();
				// System.out.println("TEMP DEBUG Found " + counterKeys.size() +
				// " edge types");
				for (CaseInsensitiveString label : counterKeys) {
					Method crystal = getCounters().get(label);
					if (crystal != null) {
						// System.out.println("TEMP DEBUG Found method for " +
						// key);
						try {
							Object raw = crystal.invoke(getFrame(), (Object[]) null);
							if (raw instanceof Integer) {
								edgeCounts.put(label.toString(), (Integer) raw);
							} else {

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						// System.out.println("TEMP DEBUG No method found for key "
						// + key);
					}
				}
				result = edgeCounts;
			} else if (key.startsWith("@counts")) {
				String label = key.toString().substring("@counts".length());
				Method crystal = getCounters().get(new CaseInsensitiveString(label));
				if (crystal != null) {
					try {
						Object raw = crystal.invoke(getFrame(), (Object[]) null);
						if (raw instanceof Integer) {
							result = raw;
						} else {
							// System.out.println("TEMP DEBUG Invokation of a counter resulted in a "
							// + (raw == null ? "null" :
							// raw.getClass().getName()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// System.out.println("TEMP DEBUG No method found for key "
					// + label);
				}
			} else if (key.equals("@columninfo")) {
				if (frame instanceof ViewVertex) {
					Map<String, String> columnInfo = new LinkedHashMap<String, String>();
					View view = ((ViewVertex) frame).asView();
					for (ViewColumn column : view.getColumns()) {
						String progName = column.getItemName();
						String title = column.getTitle();
						columnInfo.put(progName, title);
					}
					return columnInfo;
				}
			} else if (key.startsWith("#") && frame instanceof VertexFrame) {
				CharSequence label = key.subSequence(1, key.length());
				// System.out.println("DEBUG: Attempting to get edges with label "
				// + label);
				Method crystal = getIncidences().get(label);
				if (crystal != null) {
					try {
						try {
							result = crystal.invoke(frame, (Object[]) null);
						} catch (Throwable t) {
							System.err.println("TEMP DEBUG Ignoring an issue with an invokation of "
									+ crystal.getName()
									+ " on a "
									+ DGraphUtils.findInterface(frame).getName()
									+ ": "
									+ t.getClass().getName()
									+ (t.getCause() != null ? (" caused by a " + t.getCause().getClass().getName()
											+ ": " + t.getStackTrace()[0].toString()) : ""));
						}
						if (result != null) {
							if (!(result instanceof Iterable)) {
								if (result instanceof EdgeFrame) {
									Vertex v = ((VertexFrame) frame).asVertex();
									List<Edge> edges = new org.openntf.domino.graph2.impl.DEdgeList((DVertex) v);
									edges.add(((EdgeFrame) result).asEdge());
									result = new FramedEdgeList(getGraph(), ((VertexFrame) frame).asVertex(), edges,
											crystal.getReturnType());
								}

							}
							if (getIncludeVertices()) {
								// System.out.println("TEMP DEBUG: Turning EdgeList into VertexList");
								if (result instanceof DEdgeList) {
									result = ((DEdgeList) result).toVertexList();
								} else if (result instanceof FramedEdgeList) {
									result = ((FramedEdgeList<?>) result).toVertexList();
								} else {
									System.err.println("TEMP DEBUG: Expected a DEdgeList but got a "
											+ result.getClass().getName());
								}
							}
							if (getFilterKeys() != null && !isCollectionRoute_) {
								if (result instanceof DEdgeList) {
									// System.out.println("TEMP DEBUG: Applying a filter to a DEdgeList");
									List<CharSequence> filterKeys = getFilterKeys();
									List<CharSequence> filterValues = getFilterValues();
									for (int i = 0; i < filterKeys.size(); i++) {
										result = ((DEdgeList) result).applyFilter(filterKeys.get(i).toString(),
												filterValues.get(i).toString());
									}
								} else if (result instanceof DVertexList) {
									// System.out.println("TEMP DEBUG: Applying a filter to a DVertexList");
									List<CharSequence> filterKeys = getFilterKeys();
									List<CharSequence> filterValues = getFilterValues();
									for (int i = 0; i < filterKeys.size(); i++) {
										result = ((DVertexList) result).applyFilter(filterKeys.get(i).toString(),
												filterValues.get(i).toString());
									}
								} else if (result instanceof FramedEdgeList) {
									// System.out.println("TEMP DEBUG: Applying a filter to a FramedEdgeList");
									List<CharSequence> filterKeys = getFilterKeys();
									List<CharSequence> filterValues = getFilterValues();
									for (int i = 0; i < filterKeys.size(); i++) {
										result = ((FramedEdgeList<?>) result).applyFilter(filterKeys.get(i).toString(),
												filterValues.get(i).toString());
									}
								} else if (result instanceof FramedVertexList) {
									List<CharSequence> filterKeys = getFilterKeys();
									List<CharSequence> filterValues = getFilterValues();
									for (int i = 0; i < filterKeys.size(); i++) {
										String curkey = filterKeys.get(i).toString();
										String curvalue = filterValues.get(i).toString();
										// System.out.println("TEMP DEBUG: Applying a filter to a FramedVertexList - "
										// + curkey + ":" + curvalue);
										result = ((FramedVertexList<?>) result).applyFilter(curkey, curvalue);
									}
								}
							}
							if (getOrderBys() != null) {
								if (result instanceof FramedEdgeList) {
									// System.out.println("Ordering an edge list");
									result = ((FramedEdgeList<?>) result).sortBy(getOrderBys(), getDescending());
								} else if (result instanceof FramedVertexList) {
									// System.out.println("Ordering a vertex list");
									result = ((FramedVertexList<?>) result).sortBy(getOrderBys(), getDescending());
								}
							}

							if (getStart() > 0) {
								if (getCount() > 0) {
									if (result instanceof FramedEdgeList) {
										result = ((FramedEdgeList<?>) result).subList(getStart(), getStart()
												+ getCount());
									} else if (result instanceof FramedVertexList) {
										result = ((FramedVertexList<?>) result).subList(getStart(), getStart()
												+ getCount());
									}
								} else {
									if (result instanceof FramedEdgeList) {
										result = ((FramedEdgeList<?>) result).subList(getStart(),
												((FramedEdgeList<?>) result).size());
									} else if (result instanceof FramedVertexList) {
										result = ((FramedVertexList<?>) result).subList(getStart(),
												((FramedVertexList<?>) result).size());
									}
								}
							}
							// if (result instanceof List) {
							// System.out.println("Result is a " +
							// result.getClass().getName() + " with "
							// + ((List) result).size() + " elements");
							// } else {
							// System.out.println("Result is a " +
							// result.getClass().getName());
							// }
							if (result instanceof FramedVertexList) {
								ParamMap listMap = new ParamMap();
								if (getIncludeEdges()) {
									listMap.put(Parameters.EDGES, EMPTY_STRINGS);
								}
								if (getIncludeCounts()) {
									listMap.put(Parameters.COUNTS, EMPTY_STRINGS);
								}
								listMap.put(Parameters.PROPS, CaseInsensitiveString.toStrings(this.getProperties()));
								result = new JsonFrameListAdapter(getGraph(), (FramedVertexList<?>) result, listMap,
										isCollectionRoute_);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					System.err.println("No method found for key " + label);
				}
			} else {
				// System.out.println("TEMP DEBUG finding property " + key);
				Method crystal = getGetters().get(key);
				if (crystal != null) {
					try {
						result = crystal.invoke(frame, (Object[]) null);
						// if (frame instanceof VertexFrame) {
						// Vertex v = ((VertexFrame) frame).asVertex();
						// if (v instanceof DProxyVertex) {
						// System.out.println("TEMP DEBUG using a proxy vertex");
						// }
						// System.out.println("TEMP DEBUG invoking getter for "
						// + crystal.getName());
						// }
					} catch (Exception e) {
						if (frame instanceof EdgeFrame) {
							result = ((EdgeFrame) frame).asEdge().getProperty(paramKey);
						} else if (frame instanceof VertexFrame) {
							result = ((VertexFrame) frame).asVertex().getProperty(paramKey);
							// System.out.println("TEMP DEBUG using getProperty for key "
							// + key);
						} else {
							System.err.println("Trying to get property " + paramKey + " from an object "
									+ frame.getClass().getName());
						}
					}
				} else {
					if (frame instanceof ViewVertex.Contains) {
						result = ((EdgeFrame) frame).asEdge().getProperty(paramKey);
					} else if (frame instanceof VertexFrame) {
						result = ((VertexFrame) frame).asVertex().getProperty(paramKey);
					} else if (frame instanceof EdgeFrame) {
						result = ((EdgeFrame) frame).asEdge().getProperty(paramKey);
					} else {
						System.err.println("No method found for key " + paramKey);
					}
				}
			}
		} else {
			System.err.println("Unable to get property " + paramKey + " on a null object");
		}
		return result;
	}

	@Override
	public void putJsonProperty(String paramKey, Object value) {
		Object frame = getFrame();
		if (frame != null) {
			CaseInsensitiveString key = new CaseInsensitiveString(paramKey);
			Method crystal = getSetters().get(key);
			if (crystal != null) {
				Class<?> type = null;
				try {
					Class<?>[] types = crystal.getParameterTypes();
					if (types != null && types.length > 0) {
						type = types[0];
						if (!(type.isPrimitive() && value == null)) {
							Object newValue = TypeUtils.convertToTarget(value, type, null);
							crystal.invoke(frame, newValue);
						}
					} else {
						crystal.invoke(frame, value);
					}
				} catch (Exception e) {
					System.err.println("Exception trying to replace property " + paramKey + " with a value of "
							+ String.valueOf(value) + " of type "
							+ (value != null ? value.getClass().getName() : "null")
							+ (type != null ? " when what we need is a " + type.getName() : ""));
					e.printStackTrace();
				}
			} else {
				if (frame instanceof EdgeFrame) {
					((EdgeFrame) frame).asEdge().setProperty(paramKey, value);
				} else if (frame instanceof VertexFrame) {
					((VertexFrame) frame).asVertex().setProperty(paramKey, value);
				}
			}
		}
	}

}
