package org.openntf.domino.rest.resources.frames;

import com.ibm.commons.util.io.json.JsonObject;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
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

import org.openntf.domino.graph2.DEdgeList;
import org.openntf.domino.graph2.annotations.FramedEdgeList;
import org.openntf.domino.graph2.annotations.FramedVertexList;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.ViewVertex;
import org.openntf.domino.graph2.impl.DEdge;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DVertexList;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.types.CaseInsensitiveString;

public class JsonFrameAdapter implements JsonObject {
	private final static List<String> EMPTY_STRINGS = new ArrayList<String>();

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
	public JsonFrameAdapter(DFramedTransactionalGraph graph, EdgeFrame frame, ParamMap pm) {
		graph_ = graph;
		frame_ = frame;
		parameters_ = pm;
		type_ = graph_.getTypeManager().resolve(frame);
	}

	@SuppressWarnings("rawtypes")
	public JsonFrameAdapter(DFramedTransactionalGraph graph, VertexFrame frame, ParamMap pm) {
		graph_ = graph;
		frame_ = frame;
		parameters_ = pm;
		type_ = graph_.getTypeManager().resolve(frame);
	}

	public Object getFrame() {
		return frame_;
	}

	@SuppressWarnings("rawtypes")
	public DFramedTransactionalGraph getGraph() {
		return graph_;
	}

	public List<CaseInsensitiveString> getProperties() {
		if (parameters_ != null) {
			return parameters_.getProperties();
		}
		return null;
	}

	public List<CaseInsensitiveString> getInProperties() {
		if (parameters_ != null) {
			return parameters_.getInProperties();
		}
		return null;
	}

	public List<CaseInsensitiveString> getOutProperties() {
		if (parameters_ != null) {
			return parameters_.getOutProperties();
		}
		return null;
	}

	public List<CaseInsensitiveString> getLabels() {
		if (parameters_ != null) {
			return parameters_.getLabels();
		}
		return null;
	}

	public List<CaseInsensitiveString> getFilterKeys() {
		if (parameters_ != null) {
			return parameters_.getFilterKeys();
		}
		return null;
	}

	public List<CaseInsensitiveString> getFilterValues() {
		if (parameters_ != null) {
			return parameters_.getFilterValues();
		}
		return null;
	}

	public List<CaseInsensitiveString> getOrderBys() {
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
		List<String> result = new ArrayList<String>();
		result.add("@id");
		result.add("@type");
		Collection<CaseInsensitiveString> props = getProperties();
		if (props == null) {
			props = getGetters().keySet();
			// System.out.println("Found " + props.size() +
			// " properties from getters");
		} else {
			// System.out.println("Found " + props.size() +
			// " properties from parameters");
		}
		for (CaseInsensitiveString cis : props) {
			result.add(cis.toString());
			// System.out.println("Adding " + cis.toString());
		}
		Object frame = getFrame();
		if (frame instanceof VertexFrame && getIncludeEdges()) {
			result.add("@edges");
		}
		if (frame instanceof VertexFrame && getLabels() != null) {
			for (CaseInsensitiveString cis : getLabels()) {
				result.add("#" + cis);
			}
		}
		if (frame instanceof EdgeFrame) {
			result.add("@in");
			result.add("@out");
		}
		if (frame instanceof ViewVertex.Contains) {
			Edge edge = ((ViewVertex.Contains) frame).asEdge();
			if (edge instanceof DEdge) {
				result.addAll(((DEdge) edge).getDelegate().keySet());
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
				if (frame instanceof EdgeFrame) {
					result = ((EdgeFrame) frame).asEdge().getId().toString();
				}
				if (frame instanceof VertexFrame) {
					result = ((VertexFrame) frame).asVertex().getId().toString();
				}
			} else if (key.equals("@type")) {
				if (frame instanceof EdgeFrame) {
					result = type_;
				} else if (frame instanceof VertexFrame) {
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
					Method inMethod = graph_.getTypeRegistry().getIn(type_);
					if (inMethod != null) {
						try {
							Object raw = inMethod.invoke(frame, (Object[]) null);
							if (raw instanceof VertexFrame) {
								VertexFrame inFrame = (VertexFrame) raw;
								result = new JsonFrameAdapter(graph_, inFrame, inMap);
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
					Method outMethod = graph_.getTypeRegistry().getOut(type_);
					if (outMethod != null) {
						try {
							Object raw = outMethod.invoke(frame, (Object[]) null);
							if (raw instanceof VertexFrame) {
								VertexFrame outFrame = (VertexFrame) raw;
								result = new JsonFrameAdapter(graph_, outFrame, outMap);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else if (key.equals("@edges")) {
				Map<String, Integer> edgeCounts = new LinkedHashMap<String, Integer>();
				for (CaseInsensitiveString label : getCounters().keySet()) {
					Method crystal = getCounters().get(label);
					if (crystal != null) {
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
						// System.out.println("No method found for key " + key);
					}
				}
				result = edgeCounts;
			} else if (key.startsWith("#") && frame instanceof VertexFrame) {
				CharSequence label = key.subSequence(1, key.length());
				// System.out.println("DEBUG: Attempting to get edges with label "
				// + label);
				Method crystal = getIncidences().get(label);
				if (crystal != null) {
					try {
						result = crystal.invoke(frame, (Object[]) null);
						// System.out.println("Invoked " + crystal.getName() +
						// " against an object of type "
						// + frame.getClass().getName() + ". Result is a " +
						// result.getClass().getName());

						if (getIncludeVertices()) {
							System.out.println("TEMP DEBUG: Turning EdgeList into VertexList");
							if (result instanceof DEdgeList) {
								result = ((DEdgeList) result).toVertexList();

							} else if (result instanceof FramedEdgeList) {
								result = ((FramedEdgeList<?>) result).toVertexList();
							} else {
								// System.err.println("TEMP DEBUG: Expected a DEdgeList but got a "
								// + result.getClass().getName());
							}
						}
						if (getFilterKeys() != null) {
							if (result instanceof DEdgeList) {
								// System.out.println("TEMP DEBUG: Applying a filter to a DEdgeList");
								List<CaseInsensitiveString> filterKeys = getFilterKeys();
								List<CaseInsensitiveString> filterValues = getFilterValues();
								for (int i = 0; i < filterKeys.size(); i++) {
									result = ((DEdgeList) result).applyFilter(filterKeys.get(i).toString(),
											filterValues.get(i).toString());
								}
							} else if (result instanceof DVertexList) {
								// System.out.println("TEMP DEBUG: Applying a filter to a DVertexList");
								List<CaseInsensitiveString> filterKeys = getFilterKeys();
								List<CaseInsensitiveString> filterValues = getFilterValues();
								for (int i = 0; i < filterKeys.size(); i++) {
									result = ((DVertexList) result).applyFilter(filterKeys.get(i).toString(),
											filterValues.get(i).toString());
								}
							} else if (result instanceof FramedEdgeList) {
								// System.out.println("TEMP DEBUG: Applying a filter to a FramedEdgeList");
								List<CaseInsensitiveString> filterKeys = getFilterKeys();
								List<CaseInsensitiveString> filterValues = getFilterValues();
								for (int i = 0; i < filterKeys.size(); i++) {
									result = ((FramedEdgeList<?>) result).applyFilter(filterKeys.get(i).toString(),
											filterValues.get(i).toString());
								}
							} else if (result instanceof FramedVertexList) {
								List<CaseInsensitiveString> filterKeys = getFilterKeys();
								List<CaseInsensitiveString> filterValues = getFilterValues();
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
								result = ((FramedEdgeList<?>) result).sortBy(getOrderBys());
							} else if (result instanceof FramedVertexList) {
								// System.out.println("Ordering a vertex list");
								result = ((FramedVertexList<?>) result).sortBy(getOrderBys());
							}
						}

						if (getStart() > 0) {
							if (getCount() > 0) {
								if (result instanceof FramedEdgeList) {
									result = ((FramedEdgeList<?>) result).subList(getStart(), getStart() + getCount());
								} else if (result instanceof FramedVertexList) {
									result = ((FramedVertexList<?>) result)
											.subList(getStart(), getStart() + getCount());
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
							listMap.put(Parameters.PROPS, CaseInsensitiveString.toStrings(this.getProperties()));
							result = new JsonFrameListAdapter(getGraph(), (FramedVertexList<?>) result, listMap);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					System.err.println("No method found for key " + label);
				}
			} else {
				Method crystal = getGetters().get(key);
				if (crystal != null) {
					try {
						result = crystal.invoke(frame, (Object[]) null);
					} catch (Exception e) {
						if (frame instanceof EdgeFrame) {
							result = ((EdgeFrame) frame).asEdge().getProperty(paramKey);
						} else if (frame instanceof VertexFrame) {
							result = ((VertexFrame) frame).asVertex().getProperty(paramKey);
						} else {
							System.err.println("Trying to get property " + paramKey + " from an object "
									+ frame.getClass().getName());
						}
					}
				} else {
					if (frame instanceof ViewVertex.Contains) {
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
				try {
					crystal.invoke(frame, value);
				} catch (Exception e) {
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
