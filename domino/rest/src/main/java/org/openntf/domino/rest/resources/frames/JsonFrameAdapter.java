package org.openntf.domino.rest.resources.frames;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.ViewColumn;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.graph2.DEdgeList;
import org.openntf.domino.graph2.DGraphUtils;
import org.openntf.domino.graph2.annotations.FramedEdgeList;
import org.openntf.domino.graph2.annotations.FramedVertexList;
import org.openntf.domino.graph2.annotations.Inclusive;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.DbInfoVertex;
import org.openntf.domino.graph2.builtin.ViewVertex;
import org.openntf.domino.graph2.impl.DEdge;
import org.openntf.domino.graph2.impl.DEdgeEntryList;
import org.openntf.domino.graph2.impl.DFramedTransactionalGraph;
import org.openntf.domino.graph2.impl.DProxyVertex;
import org.openntf.domino.graph2.impl.DVertex;
import org.openntf.domino.graph2.impl.DVertexList;
import org.openntf.domino.rest.service.Parameters;
import org.openntf.domino.rest.service.Parameters.ParamMap;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.utils.TypeUtils;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonObject;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;

public class JsonFrameAdapter implements JsonObject {
	private final static List<String> EMPTY_STRINGS = new ArrayList<String>();
	protected final boolean isCollectionRoute_;

	// TODO NTF Add support for modification date checking prior to permitting
	// PUT/PATCH
	static private boolean ifUnmodifiedSince(final Object object, final Date ifUnmodifiedSince) {
		if (object instanceof DEdgeFrame) {
			Date mod = ((DEdgeFrame) object).getModified();
			return !mod.after(ifUnmodifiedSince);
		} else if (object instanceof DVertexFrame) {
			Date mod = ((DVertexFrame) object).getModified();
			return !mod.after(ifUnmodifiedSince);
		} else {
			throw new IllegalArgumentException(
					"Cannot verify modification time of a non-framed object: " + object.getClass().getName());
		}
	}

	public static VertexFrame toVertexFrame(final DFramedTransactionalGraph graph, final Edge edge, final Vertex source) {
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
	protected Map<CaseInsensitiveString, Method> actions_;
	protected Map<CaseInsensitiveString, Method> counters_;
	protected Map<CaseInsensitiveString, Method> setters_;
	protected ParamMap parameters_;

	@SuppressWarnings("rawtypes")
	public JsonFrameAdapter(final DFramedTransactionalGraph graph, final EdgeFrame frame, final ParamMap pm, final boolean isCollectionRoute) {
		graph_ = graph;
		frame_ = frame;
		parameters_ = pm;
		type_ = graph_.getTypeManager().resolve(frame);
		isCollectionRoute_ = isCollectionRoute;
	}

	@SuppressWarnings("rawtypes")
	public JsonFrameAdapter(final DFramedTransactionalGraph graph, final VertexFrame frame, final ParamMap pm,
			final boolean isCollectionRoute) {
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

	public List<CharSequence> getInVProperties() {
		if (parameters_ != null) {
			return parameters_.getInVProperties();
		}
		return null;
	}

	public List<CharSequence> getOutVProperties() {
		if (parameters_ != null) {
			return parameters_.getOutVProperties();
		}
		return null;
	}

	public List<CharSequence> getLabels() {
		if (parameters_ != null) {
			return parameters_.getLabels();
		}
		return null;
	}

	public List<CharSequence> getActionsParam() {
		if (parameters_ != null) {
			return parameters_.getActions();
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

	public List<CharSequence> getStartsKeys() {
		if (parameters_ != null) {
			return parameters_.getStartsKeys();
		}
		return null;
	}

	public List<CharSequence> getStartsValues() {
		if (parameters_ != null) {
			return parameters_.getStartsValues();
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

	public boolean getIncludeActions() {
		if (parameters_ != null) {
			return parameters_.getIncludeActions();
		}
		return false;
	}

	public boolean getIncludeDebug() {
		if (parameters_ != null) {
			return parameters_.getIncludeDebug();
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

	public Map<CaseInsensitiveString, Method> getActions() {
		if (actions_ == null) {
			actions_ = getGraph().getTypeRegistry().getActions(type_);
		}
		return actions_;
	}

	public Map<CaseInsensitiveString, Method> getSetters() {
		if (setters_ == null) {
			setters_ = getGraph().getTypeRegistry().getPropertiesSetters(type_);
		}
		return setters_;
	}

	private static boolean isInclusive(final Class<?> clazz) {
		boolean result = false;
		Class<?> [] interfaces = clazz.getInterfaces();
		for (Class<?> inter : interfaces) {
			Annotation incl = inter.getAnnotation(Inclusive.class);
			if (incl != null) return true;
		}
		return result;
	}

	@Override
	public Iterator<String> getJsonProperties() {
		// System.out
		// .println("TEMP DEBUG getting Json properties list for a frame of type
		// "
		// + frame_.getClass().getName());
		List<String> result = new ArrayList<String>();
		// if (getActionsParam() != null) {
		// for (CharSequence cis : getActionsParam()) {
		// result.add("%" + cis.toString());
		// }
		// }
		result.add("@id");
		result.add("@type");
		result.add("@editable");
		if (getIncludeDebug()) {
			result.add("@debug");
		}
		Collection<CharSequence> props = getProperties();
		if (props == null) {
			// NoteCoordinate nc = (NoteCoordinate)
			// ((VertexFrame)frame_).asVertex().getId();
			props = new ArrayList<CharSequence>();
			props.addAll(getGetters().keySet());
			Class<?> klazz = frame_.getClass();
			boolean incl = isInclusive(klazz);
			//			if (incl) {
			//				System.out.println("TEMP DEBUG Found an inclusive ");
			//			}
			if (props == null || props.size() < 5 || incl) {
				if (frame_ instanceof DVertexFrame) {
					try {
						Set<String> raw =  ((DVertexFrame)frame_).asVertex().getPropertyKeys();
						//						Set<CharSequence> raw = ((DVertexFrame) frame_).asMap().keySet();
						//						System.out.println("TEMP DEBUG raw vertex has " + raw.size() + " properties");
						props.addAll(CaseInsensitiveString.toCaseInsensitive(raw));
					} catch (Throwable t) {
						Throwable cause = t.getCause();
						if (cause != null) {
							System.err.println(
									"Exception trying to process a frame of type " + DGraphUtils.findInterface(frame_)
									+ " resulting in a " + cause.getClass().getSimpleName());
							cause.printStackTrace();
							try {
								throw cause;
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
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
		if (getIncludeActions()) {
			result.add("@actions");
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
			result.add("@label");
		}
		if (frame instanceof ViewVertex) {
			result.add("@columninfo");
		}
		// if (frame instanceof DbInfoVertex) {
		// result.add("@viewinfo");
		// }
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

		//		System.out.println("TEMP DEBUG getting properties for a " + DGraphUtils.getInterfaceList(frame) + " -- " + Strings.getString(result, ","));
		return result.iterator();
	}

	@Override
	public Object getJsonProperty(final String paramKey) {
		Object result = null;
		Object frame = getFrame();
		if (frame != null) {
			CaseInsensitiveString key = new CaseInsensitiveString(paramKey);
			if (key.equals("@id")) {
				if (frame instanceof VertexFrame) {
					Vertex v = ((VertexFrame)frame).asVertex();
					//					if (v instanceof DProxyVertex) {
					//						result = ((DProxyVertex)v).getProxyDelegate().getId().toString();
					//					} else {
					result = String.valueOf(v.getId());
					//					}
				} else if (frame instanceof EdgeFrame) {
					result = String.valueOf(((EdgeFrame) frame).asEdge().getId());
				}
			} else if (key.equals("@editable")) {
				if (frame instanceof VertexFrame) {
					result = ((DVertex)((VertexFrame)frame).asVertex()).isEditable();
				} else if (frame instanceof EdgeFrame) {
					result = ((DEdge)((EdgeFrame)frame).asEdge()).isEditable();
				}
			} else if (key.equals("@proxyid")) {
				// System.out.println("TEMP DEBUG @proxyid requested");
				if (frame instanceof VertexFrame) {
					Vertex v = ((VertexFrame) frame).asVertex();
					if (v instanceof DProxyVertex) {
						result = ((DProxyVertex) v).getProperty(DProxyVertex.PROXY_ITEM, String.class);
					}
				}
			} else if (key.equals("@debug")) {
				Map<String, String> debugMap = new LinkedHashMap<String, String>();
				debugMap.put("frameIdentity", String.valueOf(System.identityHashCode(this)));
				if (frame instanceof VertexFrame) {
					Vertex v = ((VertexFrame) frame).asVertex();
					debugMap.put("vertexIdentity", String.valueOf(System.identityHashCode(v)));
				} else if (frame instanceof EdgeFrame) {
					Edge e = ((EdgeFrame) frame).asEdge();
					debugMap.put("vertexIdentity", String.valueOf(System.identityHashCode(e)));
				}
				result = debugMap;
			} else if (key.equals("@type")) {
				if (frame instanceof VertexFrame) {
					result = type_;
				} else if (frame instanceof EdgeFrame) {
					result = type_;
				}
			} else if (key.equals("@label")) {
				if (frame instanceof EdgeFrame) {
					Edge e = ((EdgeFrame) frame).asEdge();
					result = e.getLabel();
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
					} else {
						System.out.println("In method not found for type: " + type_.getName());
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
						} catch (IllegalArgumentException iae) {
							System.out.println("TEMP DEBUG Attempting to invoke " + crystal.getDeclaringClass().getName() + "." + crystal.getName() + " on an " + getFrame().getClass().getName() + " (" + DGraphUtils.getInterfaceList(getFrame()) + ")");
							throw new RuntimeException(iae);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					} else {
						// System.out.println("TEMP DEBUG No method found for
						// key "
						// + key);
					}
				}
				result = edgeCounts;
			} else if (key.equals("@actions")) {
				List<CaseInsensitiveString> actionList = new ArrayList<CaseInsensitiveString>();
				Set<CaseInsensitiveString> actionNames = getActions().keySet();
				for (CaseInsensitiveString name : actionNames) {
					actionList.add(name);
				}
				result = actionList;
			} else if (key.startsWith("@counts")) {
				String label = key.toString().substring("@counts".length());
				Method crystal = getCounters().get(new CaseInsensitiveString(label));
				if (crystal != null) {
					try {
						Object raw = crystal.invoke(getFrame(), (Object[]) null);
						if (raw instanceof Integer) {
							result = raw;
						} else {
							// System.out.println("TEMP DEBUG Invokation of a
							// counter resulted in a "
							// + (raw == null ? "null" :
							// raw.getClass().getName()));
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				} else {
					// System.out.println("TEMP DEBUG No method found for key "
					// + label);
				}
			} else if (key.equals("@columninfo")) {
				if (frame instanceof ViewVertex) {
					Map<String, String> columnInfo = new LinkedHashMap<String, String>();
					if (frame instanceof ViewVertex) {
						View view = ((ViewVertex) frame).asView();
						if (view != null){
							try {
								for (ViewColumn column : view.getColumns()) {
									String progName = column.getItemName();
									String title = column.getTitle();
									columnInfo.put(progName, title);
								}
							} catch (Throwable t) {
								columnInfo.put("@error", "Unable to access column information");
							}
						}
					} else {
						System.err.println("Frame is not a ViewVertex. It is " + DGraphUtils.findInterface(frame));
					}
					return columnInfo;
				}
			} else if (key.equals("@viewinfo")) {
				if (frame instanceof DbInfoVertex) {
					List viewInfo = ((DbInfoVertex) frame).getViewInfo();
					return viewInfo;
				}
			} else if (key.startsWith("#") && frame instanceof VertexFrame) {
				CharSequence label = key.subSequence(1, key.length());
				Method crystal = getIncidences().get(label);
				if (crystal != null) {
					try {
						result = crystal.invoke(frame, (Object[]) null);
					} catch (IllegalArgumentException iae) {
						throw new RuntimeException(iae.getMessage() + " where the expect call is " + crystal.getDeclaringClass().getName() + "." + crystal.getName() + " but the object is " + DGraphUtils.getInterfaceList(frame));
					} catch (Exception e) {
						throw new RuntimeException(e);
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
							// System.out.println("TEMP DEBUG: Turning
							// EdgeList into VertexList");
							if (result instanceof DEdgeList) {
								result = ((DEdgeList) result).toVertexList();
							} else if (result instanceof FramedEdgeList) {
								result = ((FramedEdgeList<?>) result).toVertexList();
							} else {
								System.err.println(
										"TEMP DEBUG: Expected a DEdgeList but got a " + result.getClass().getName());
							}
						}
						if (getFilterKeys() != null && !isCollectionRoute_) {
							if (result instanceof DEdgeList) {
								// System.out.println("TEMP DEBUG: Applying
								// a filter to a DEdgeList");
								List<CharSequence> filterKeys = getFilterKeys();
								List<CharSequence> filterValues = getFilterValues();
								for (int i = 0; i < filterKeys.size(); i++) {
									result = ((DEdgeList) result).applyFilter(filterKeys.get(i).toString(),
											filterValues.get(i).toString());
								}
							} else if (result instanceof DVertexList) {
								// System.out.println("TEMP DEBUG: Applying
								// a filter to a DVertexList");
								List<CharSequence> filterKeys = getFilterKeys();
								List<CharSequence> filterValues = getFilterValues();
								for (int i = 0; i < filterKeys.size(); i++) {
									result = ((DVertexList) result).applyFilter(filterKeys.get(i).toString(),
											filterValues.get(i).toString());
								}
							} else if (result instanceof FramedEdgeList) {
								// System.out.println("TEMP DEBUG: Applying
								// a filter to a FramedEdgeList");
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
									// System.out.println("TEMP DEBUG:
									// Applying a filter to a
									// FramedVertexList - "
									// + curkey + ":" + curvalue);
									result = ((FramedVertexList<?>) result).applyFilter(curkey, curvalue);
								}
							}
						}
						if (getStartsValues() != null) {
							// System.out.println(
							// "TEMP DEBUG Processing starts values with a "
							// + result.getClass().getName());
							if (result instanceof DEdgeEntryList) {
								((DEdgeEntryList) result).initEntryList(getStartsValues());
							} else if (result instanceof FramedEdgeList) {
								((FramedEdgeList) result).applyFilter("lookup", getStartsValues());
							}
						}
						if (getFilterValues() != null && getFilterKeys() == null) {
							if (result instanceof DEdgeEntryList) {
								// System.out.println("TEMP DEBUG filtering a
								// DEdgeEntryList");
								((DEdgeEntryList) result).filterEntryList(getFilterValues());
							} else if (result instanceof FramedEdgeList) {
								((FramedEdgeList) result).applyFilter("filter", getFilterValues());
							}
						}
						if (getOrderBys() != null) {
							if (result instanceof FramedEdgeList) {
								// System.out.println("Ordering an edge
								// list");
								result = ((FramedEdgeList<?>) result).sortBy(getOrderBys(), getDescending());
							} else if (result instanceof FramedVertexList) {
								// System.out.println("Ordering a vertex
								// list");
								result = ((FramedVertexList<?>) result).sortBy(getOrderBys(), getDescending());
							}
						}

						if (getStart() >= 0) {
							// System.out.println("TEMP DEBUG Start is " +
							// getStart() + " and count is " + getCount()
							// + " for a " + result.getClass().getName());
							if (getCount() > 0) {
								int end = getStart() + getCount();
								if (result instanceof FramedEdgeList) {
									// System.out.println("TEMP DEBUG Sublisting
									// a FramedEdgeList...");
									int size = ((FramedEdgeList<?>) result).size();
									result = ((FramedEdgeList<?>) result).subList(getStart(), (end>size?size:end));
								} else if (result instanceof FramedVertexList) {
									int size = ((FramedVertexList<?>) result).size();
									result = ((FramedVertexList<?>) result).subList(getStart(), (end>size?size:end));
								} else if (result instanceof DEdgeEntryList) {
									// System.out.println("TEMP DEBUG Sublisting
									// a DEdgeEntryList...");
									int size = ((DEdgeEntryList) result).size();
									result = ((DEdgeEntryList) result).subList(getStart(), (end>size?size:end));
								}
							} else {
								if (result instanceof FramedEdgeList) {
									result = ((FramedEdgeList<?>) result).subList(getStart(),
											((FramedEdgeList<?>) result).size());
								} else if (result instanceof FramedVertexList) {
									result = ((FramedVertexList<?>) result).subList(getStart(),
											((FramedVertexList<?>) result).size());
								} else if (result instanceof DEdgeEntryList) {
									// System.out.println("TEMP DEBUG Sublisting
									// a DEdgeEntryList...");
									result = ((DEdgeEntryList) result).subList(getStart()+1,
											((DEdgeEntryList) result).size());
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
							listMap.put(Parameters.HIDEPROPS,
									CaseInsensitiveString.toStrings(this.getHideProperties()));
							result = new JsonFrameListAdapter(getGraph(), (FramedVertexList<?>) result, listMap,
									isCollectionRoute_);
						}
					}
				} else {
					// NTF actually, this is a perfectly normal outcome.
					// System.err.println("No edge method found for label " +
					// label + " in a VertexFrame of type "
					// + DGraphUtils.findInterface(frame) + " with id "
					// + ((VertexFrame) frame).asVertex().getId());
				}
			} else {
				// System.out.println("TEMP DEBUG finding property " + key);
				Method crystal = getGetters().get(key);
				if (crystal != null) {
					try {
						result = crystal.invoke(frame, (Object[]) null);
					} catch (UserAccessException uae) {
						throw uae;
					} catch (Throwable t) {
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
	public void putJsonProperty(final String paramKey, Object value) {
		Object frame = getFrame();
		if (frame != null) {
			CaseInsensitiveString key = new CaseInsensitiveString(paramKey);
			Method crystal = getSetters().get(key);
			if (crystal != null) {
				try {
					Class<?>[] types = crystal.getParameterTypes();
					Class<?> type = types[0];
					if (value == null) {
						String propName = null;
						TypedProperty tprop = crystal.getAnnotation(TypedProperty.class);
						if (tprop != null) {
							propName = tprop.value();
						} else {
							Property prop = crystal.getAnnotation(Property.class);
							if (prop != null) {
								propName = prop.value();
							}
						}
						if (propName != null) {
							if (frame instanceof VertexFrame) {
								((VertexFrame) frame).asVertex().setProperty(propName, null);
							} else if (frame instanceof EdgeFrame) {
								((EdgeFrame) frame).asEdge().setProperty(propName, null);
							}
						} else {
							System.err.println("ALERT the next operation will probably throw an exception");
							Object[] nullarg = { type.cast(null) };
							crystal.invoke(frame, nullarg);
						}
					} else if (!type.isAssignableFrom(value.getClass())) {
						value = TypeUtils.convertToTarget(value, type,
								org.openntf.domino.utils.Factory.getSession(SessionType.CURRENT));
						crystal.invoke(frame, value);
					} else if (JsonJavaObject.class.equals(type)) {
						// FIXME NTF this is a complete hack :(
						TypedProperty prop = crystal.getAnnotation(TypedProperty.class);
						String itemname = prop.value();
						if (frame instanceof DVertexFrame) {
							Document doc = ((DVertexFrame) frame).asDocument();
							TypeUtils.writeToItem(doc, itemname, value, false);
						}
					} else {
						crystal.invoke(frame, value);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				Method man = getGetters().get(key);
				if (man == null) { // NTF if there's no getter, it's an
					// undefined property
					if (frame instanceof EdgeFrame) {
						((EdgeFrame) frame).asEdge().setProperty(paramKey, value);
					} else if (frame instanceof VertexFrame) {
						((VertexFrame) frame).asVertex().setProperty(paramKey, value);
					}
				} else {
					// NTF if there is a getter but no setter, this is a
					// read-only property. Disregard the JSON
				}
			}
		}
	}

	public void updateReadOnlyProperties() {
		Object frame = getFrame();
		try {
			if (frame instanceof DVertexFrame) {
				((DVertexFrame) frame).updateReadOnlyProperties((DVertexFrame) frame);
			} else if (frame instanceof DEdgeFrame) {
				((DEdgeFrame) frame).updateReadOnlyProperties();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean runAction(final CharSequence key) {
		Object result = Boolean.FALSE;
		CharSequence name = key/* .subSequence(1, key.length()) */;
		Method crystal = getActions().get(name);
		if (crystal != null) {
			try {
				result = crystal.invoke(getFrame(), (Object[]) null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			System.err.println("No action method found for name: " + name);
		}
		return (Boolean) result;
	}

	public boolean runAction(final CharSequence key, final List<Object> arguments) {
		Object result = Boolean.FALSE;
		CharSequence name = key/* .subSequence(1, key.length()) */;
		Method crystal = getActions().get(name);
		if (crystal != null) {
			try {
				Object[] args = arguments.toArray();
				result = crystal.invoke(getFrame(), args);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			System.err.println("No action method found for name: " + name);
		}
		return (Boolean) result;
	}

}
