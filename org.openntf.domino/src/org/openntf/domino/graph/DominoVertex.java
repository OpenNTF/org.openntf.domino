package org.openntf.domino.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.graph.DominoGraph.DominoGraphException;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.util.DefaultVertexQuery;
import com.tinkerpop.blueprints.util.MultiIterable;
import com.tinkerpop.blueprints.util.VerticesFromEdgesIterable;

public class DominoVertex extends DominoElement implements IDominoVertex, Serializable {
	private static final Logger log_ = Logger.getLogger(DominoVertex.class.getName());
	public static final String GRAPH_TYPE_VALUE = "OpenVertex";
	//	public static final String IN_NAME = "_OPEN_IN";
	//	public static final String OUT_NAME = "_OPEN_OUT";
	// public static final String IN_LABELS = "_OPEN_LABELS_IN";
	// public static final String OUT_LABELS = "_OPEN_LABELS_OUT";
	public static final String IN_PREFIX = "_OPEN_IN_";
	public static final String OUT_PREFIX = "_OPEN_OUT_";

	private static final long serialVersionUID = 1L;

	private Boolean inDirty_ = false;

	private Set<String> inEdges_;
	// private transient Set<Edge> inEdgesObjects_;
	private Boolean outDirty_ = false;
	// private transient Set<Edge> outEdgesObjects_;
	private Set<String> outEdges_;

	private Map<String, Boolean> inDirtyMap_;
	private Map<String, Boolean> outDirtyMap_;
	// private Set<String> inLabels_;
	// private Set<String> outLabels_;
	// private Boolean inLabelsDirty_;
	// private Boolean outLabelsDirty_;
	private Map<String, Set<String>> inEdgesMap_;
	private Map<String, Set<String>> outEdgesMap_;
	private transient Map<String, Set<Edge>> inEdgeCache_;
	private transient Map<String, Set<Edge>> outEdgeCache_;

	public DominoVertex(final DominoGraph parent, final org.openntf.domino.Document doc) {
		super(parent, doc);
	}

	Map<String, Boolean> getInDirtyMap() {
		if (inDirtyMap_ == null) {
			inDirtyMap_ = new ConcurrentHashMap<String, Boolean>();
		}
		return inDirtyMap_;
	}

	Map<String, Boolean> getOutDirtyMap() {
		if (outDirtyMap_ == null) {
			outDirtyMap_ = new ConcurrentHashMap<String, Boolean>();
		}
		return outDirtyMap_;
	}

	Map<String, Set<String>> getInEdgesMap() {
		if (inEdgesMap_ == null) {
			inEdgesMap_ = new ConcurrentHashMap<String, Set<String>>();
		}
		return inEdgesMap_;
	}

	Map<String, Set<String>> getOutEdgesMap() {
		if (outEdgesMap_ == null) {
			outEdgesMap_ = new ConcurrentHashMap<String, Set<String>>();
		}
		return outEdgesMap_;
	}

	public int getInEdgeCount(final String label) {
		Set<String> edgeIds = getInEdgesMap().get(label);
		if (edgeIds == null) {
			return getProperty("_COUNT" + DominoVertex.IN_PREFIX + label, Integer.class, false);
		} else {
			return edgeIds.size();
		}
	}

	Set<String> getInEdgesSet(final String label) {
		Set<String> edgeIds = getInEdgesMap().get(label);
		if (edgeIds == null) {
			Object o = getProperty(DominoVertex.IN_PREFIX + label, java.util.Collection.class);
			if (o != null) {
				if (o instanceof LinkedHashSet) {
					edgeIds = Collections.synchronizedSet((LinkedHashSet) o);
				} else if (o instanceof java.util.Collection) {
					edgeIds = Collections.synchronizedSet(new LinkedHashSet<String>((Collection<String>) o));
				} else {
					log_.log(Level.SEVERE, "ALERT! InEdges returned something other than a Collection " + o.getClass().getName()
							+ ". We are clearing the values and rebuilding the edges.");
					edgeIds = Collections.synchronizedSet(new LinkedHashSet<String>());
				}
			} else {
				edgeIds = Collections.synchronizedSet(new LinkedHashSet<String>());
			}
			Map map = getInEdgesMap();
			synchronized (map) {
				map.put(label, edgeIds);
			}
		}
		return edgeIds;
	}

	public int getOutEdgeCount(final String label) {
		Set<String> edgeIds = getOutEdgesMap().get(label);
		if (edgeIds == null) {
			return getProperty("_COUNT" + DominoVertex.OUT_PREFIX + label, Integer.class, false);
		} else {
			return edgeIds.size();
		}
	}

	Set<String> getOutEdgesSet(final String label) {
		Set<String> edgeIds = getOutEdgesMap().get(label);
		if (edgeIds == null) {
			Object o = getProperty(DominoVertex.OUT_PREFIX + label, java.util.Collection.class);
			if (o != null) {
				if (o instanceof LinkedHashSet) {
					edgeIds = Collections.synchronizedSet((LinkedHashSet) o);
				} else if (o instanceof java.util.Collection) {
					edgeIds = Collections.synchronizedSet(new LinkedHashSet<String>((Collection<String>) o));
				} else {
					log_.log(Level.SEVERE, "ALERT! OutEdges returned something other than a Collection " + o.getClass().getName()
							+ ". We are clearing the values and rebuilding the edges.");
					edgeIds = Collections.synchronizedSet(new LinkedHashSet<String>());
				}
			} else {
				edgeIds = Collections.synchronizedSet(new LinkedHashSet<String>());
			}
			Map map = getOutEdgesMap();
			synchronized (map) {
				map.put(label, edgeIds);
			}
		}
		return edgeIds;
	}

	@Override
	public Edge addEdge(final String label, final Vertex vertex) {
		return parent_.addEdge(null, this, vertex, label);
	}

	public void addInEdge(final Edge edge) {
		boolean adding = false;
		String label = edge.getLabel();
		// Set<String> ins = getInEdges();
		Set<String> ins = getInEdgesSet(label);
		synchronized (ins) {
			if (!ins.contains((String) edge.getId())) {
				adding = true;
				ins.add((String) edge.getId());
			}
		}
		if (adding) {
			getParent().startTransaction(this);
			Map map = getInDirtyMap();
			synchronized (map) {
				map.put(label, true);
			}
			Set<Edge> inLabelObjs = getInEdgeCache(label);
			synchronized (inLabelObjs) {
				inLabelObjs.add(edge);
			}

			// inDirty_ = true;
			// Set<Edge> inObjs = getInEdgeObjects();
			// synchronized (inObjs) {
			// inObjs.add(edge);
			// }
		}
	}

	public void addOutEdge(final Edge edge) {
		boolean adding = false;
		String label = edge.getLabel();
		// Set<String> outs = getOutEdges();
		Set<String> outs = getOutEdgesSet(label);
		synchronized (outs) {
			if (!outs.contains((String) edge.getId())) {
				adding = true;
				outs.add((String) edge.getId());
			}
		}
		if (adding) {
			getParent().startTransaction(this);
			Map map = getOutDirtyMap();
			synchronized (map) {
				map.put(label, true);
			}
			Set<Edge> outLabelObjs = getOutEdgeCache(label);
			synchronized (outLabelObjs) {
				outLabelObjs.add(edge);
			}
			// outDirty_ = true;
			// Set<Edge> outObjs = getOutEdgeObjects();
			// synchronized (outObjs) {
			// outObjs.add(edge);
			// }
		}
		// setProperty(DominoVertex.OUT_NAME, outEdges_);
	}

	public java.util.Set<String> getBothEdges() {
		Set<String> result = new LinkedHashSet<String>();
		result.addAll(getInEdges());
		result.addAll(getOutEdges());
		return Collections.unmodifiableSet(result);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Iterable<Edge> getEdges(final Direction direction, final String... labels) {
		if (direction == Direction.IN) {
			if (labels == null || labels.length == 0) {
				return Collections.unmodifiableSet(getInEdgeObjects());
			} else {
				return getInEdgeObjects(labels);
			}
		} else if (direction == Direction.OUT) {
			if (labels == null || labels.length == 0) {
				return Collections.unmodifiableSet(getOutEdgeObjects());
			} else {
				return getOutEdgeObjects(labels);
			}
		} else {
			LinkedHashSet result = new LinkedHashSet<Edge>();
			if (labels == null || labels.length == 0) {
				result.addAll(getInEdgeObjects());
				result.addAll(getOutEdgeObjects());
			} else {
				result.addAll(getInEdgeObjects(labels));
				result.addAll(getOutEdgeObjects(labels));
			}
			return Collections.unmodifiableSet(result);
		}
	}

	private Map<String, Set<Edge>> getInEdgeCache() {
		if (inEdgeCache_ == null) {
			inEdgeCache_ = Collections.synchronizedMap(new HashMap<String, Set<Edge>>());
		}
		return inEdgeCache_;
	}

	private Set<Edge> getInEdgeCache(final String label) {
		Map<String, Set<Edge>> inCache = getInEdgeCache();
		Set<Edge> result = null;
		synchronized (inCache) {
			result = inCache.get(label);
			if (result == null) {
				result = Collections.synchronizedSet(new LinkedHashSet<Edge>());
				inCache.put(label, result);
			}
		}
		return result;
	}

	private Set<Edge> getOutEdgeCache(final String label) {
		Map<String, Set<Edge>> outCache = getOutEdgeCache();
		Set<Edge> result = null;
		synchronized (outCache) {
			result = outCache.get(label);
			if (result == null) {
				result = Collections.synchronizedSet(new LinkedHashSet<Edge>());
				outCache.put(label, result);
			}
		}
		return result;
	}

	private Map<String, Set<Edge>> getOutEdgeCache() {
		if (outEdgeCache_ == null) {
			outEdgeCache_ = Collections.synchronizedMap(new HashMap<String, Set<Edge>>());
		}
		return outEdgeCache_;
	}

	protected Set<Edge> getInEdgeObjects(final String... labels) {
		Map<String, Set<Edge>> inCache = getInEdgeCache();
		Set<Edge> result = null;
		if (labels == null || labels.length == 0) {
			result = Collections.synchronizedSet(new LinkedHashSet<Edge>());
			Set<String> labelSet = this.getInEdgeLabels();
			//			System.out.println("INFO: Getting all IN edges for a vertex across " + labelSet.size() + " labels.");
			for (String label : labelSet) {
				result.addAll(getInEdgeObjects(label));
			}
			//			System.out.println("INFO: Found " + result.size() + " IN edges.");
		} else if (labels.length == 1) {
			String label = labels[0];
			// System.out.println("Getting in edges from " + getClass().getName() + " with label: " + label + " ...");
			synchronized (inCache) {
				result = inCache.get(label);
			}
			if (result == null) {
				// result = Collections.synchronizedSet(new LinkedHashSet<Edge>());
				Set<String> edgeIds = getInEdgesSet(label);
				Set<Edge> edges = getParent().getEdgesFromIds(edgeIds);
				if (edges != null) {
					result = Collections.synchronizedSet((LinkedHashSet) edges);
				}

				// Set<Edge> allEdges = Collections.unmodifiableSet(getInEdgeObjects());
				// for (Edge edge : allEdges) {
				// if (label.equals(edge.getLabel())) {
				// result.add(edge);
				// }
				// }

				synchronized (inCache) {
					inCache.put(label, result);
				}
			}
		} else {
			result = Collections.synchronizedSet(new LinkedHashSet<Edge>());
			for (String label : labels) {
				result.addAll(getInEdgeObjects(label));
			}
		}
		return Collections.unmodifiableSet(result);
	}

	protected Set<Edge> getOutEdgeObjects(final String... labels) {
		Map<String, Set<Edge>> outCache = getOutEdgeCache();
		Set<Edge> result = null;

		if (labels == null || labels.length == 0) {
			result = new LinkedHashSet<Edge>();
			Set<String> labelSet = this.getOutEdgeLabels();
			//			System.out.println("INFO: Getting all OUT edges for a vertex across " + labelSet.size() + " labels.");
			for (String label : labelSet) {
				Set<Edge> curEdges = getOutEdgeObjects(label);
				//				System.out.println("INFO: Found " + curEdges.size() + " OUT edges for label " + label);
				result.addAll(curEdges);
			}
			//			System.out.println("INFO: Found " + result.size() + " OUT edges.");
		} else if (labels.length == 1) {
			String label = labels[0];
			if (label == null) {
				return Collections.unmodifiableSet(getOutEdgeObjects());
			}
			synchronized (outCache) {
				result = outCache.get(label);
			}
			if (result == null) {
				Set<String> edgeIds = getOutEdgesSet(label);
				Set<Edge> edges = getParent().getEdgesFromIds(edgeIds);
				if (edges != null) {
					result = Collections.synchronizedSet((LinkedHashSet) edges);
				}

				// result = Collections.synchronizedSet(new LinkedHashSet<Edge>());
				// Set<Edge> allEdges = Collections.unmodifiableSet(getOutEdgeObjects());
				// if (!allEdges.isEmpty()) {
				// for (Edge edge : allEdges) {
				// if (edge == null) {
				//
				// } else {
				// String curLabel = edge.getLabel();
				// if (label.equals(curLabel)) {
				// result.add(edge);
				// }
				// }
				// }
				// }

				synchronized (outCache) {
					outCache.put(label, result);
				}
			}
		} else {
			result = Collections.synchronizedSet(new LinkedHashSet<Edge>());
			for (String label : labels) {
				result.addAll(getOutEdgeObjects(label));
			}
		}
		return Collections.unmodifiableSet(result);
	}

	// @SuppressWarnings({ "unchecked", "rawtypes" })
	// protected Set<Edge> getInEdgeObjects() {
	// if (inEdgesObjects_ == null) {
	// Set<String> ins = getInEdges();
	// Iterable<Edge> edges = getParent().getEdgesFromIds(ins);
	// if (edges != null) {
	// inEdgesObjects_ = Collections.synchronizedSet((LinkedHashSet) edges);
	// }
	// }
	// return inEdgesObjects_;
	// }

	// @SuppressWarnings({ "unchecked", "rawtypes" })
	// protected Set<Edge> getOutEdgeObjects() {
	// if (outEdgesObjects_ == null) {
	// Set<String> outs = getOutEdges();
	// Iterable<Edge> edges = getParent().getEdgesFromIds(outs);
	// if (edges != null) {
	// outEdgesObjects_ = Collections.synchronizedSet((LinkedHashSet) edges);
	// }
	// }
	// return outEdgesObjects_;
	// }

	// @SuppressWarnings("unchecked")
	// public Set<String> getInEdges() {
	// if (inEdges_ == null) {
	// Object o = getProperty(DominoVertex.IN_NAME, java.util.Collection.class);
	// if (o != null) {
	// if (o instanceof LinkedHashSet) {
	// inEdges_ = Collections.synchronizedSet((LinkedHashSet) o);
	// } else if (o instanceof java.util.Collection) {
	// inEdges_ = Collections.synchronizedSet(new LinkedHashSet<String>((Collection<String>) o));
	// } else {
	// log_.log(Level.WARNING, "ALERT! InEdges returned something other than a Collection " + o.getClass().getName());
	// }
	// } else {
	// inEdges_ = Collections.synchronizedSet(new LinkedHashSet<String>());
	// }
	// }
	//
	// return inEdges_;
	// }

	Set<String> getInEdgeLabels() {
		Set<String> result = new LinkedHashSet<String>();
		Set<String> rawKeys = getRawDocument().keySet();
		for (String key : rawKeys) {
			if (key.startsWith(IN_PREFIX)) {
				result.add(key.substring(IN_PREFIX.length()));
			}
		}
		return result;
	}

	private Set<String> getInEdges() {
		Set<String> result = new LinkedHashSet<String>();
		for (String label : getInEdgeLabels()) {
			result.addAll(getInEdgesSet(label));
		}
		return result;
	}

	Set<String> getOutEdgeLabels() {
		Set<String> result = new LinkedHashSet<String>();
		Set<String> rawKeys = getRawDocument().keySet();
		for (String key : rawKeys) {
			if (key.startsWith(OUT_PREFIX)) {
				result.add(key.substring(OUT_PREFIX.length()));
			}
		}
		return result;
	}

	private Set<String> getOutEdges() {
		Set<String> result = new LinkedHashSet<String>();
		for (String label : getOutEdgeLabels()) {
			result.addAll(getOutEdgesSet(label));
		}
		return result;
	}

	// @SuppressWarnings("unchecked")
	// public Set<String> getOutEdges() {
	// if (outEdges_ == null) {
	// Object o = getProperty(DominoVertex.OUT_NAME, java.util.Collection.class);
	// if (o != null) {
	// if (o instanceof LinkedHashSet) {
	// outEdges_ = Collections.synchronizedSet((LinkedHashSet) o);
	// } else if (o instanceof java.util.Collection) {
	// outEdges_ = Collections.synchronizedSet(new LinkedHashSet<String>((Collection<String>) o));
	// } else {
	// log_.log(Level.WARNING, "ALERT! OutEdges returned something other than a Collection " + o.getClass().getName());
	// }
	// } else {
	// outEdges_ = Collections.synchronizedSet(new LinkedHashSet<String>());
	// }
	// }
	// return outEdges_;
	// }

	@Override
	public Iterable<Vertex> getVertices(final Direction direction, final String... labels) {
		if (direction == Direction.BOTH) {
			List<Iterable<Vertex>> list = new ArrayList<Iterable<Vertex>>();
			list.add(new VerticesFromEdgesIterable(this, Direction.IN, labels));
			list.add(new VerticesFromEdgesIterable(this, Direction.OUT, labels));
			return new MultiIterable<Vertex>(list);
		} else {
			return new VerticesFromEdgesIterable(this, direction, labels);
		}
	}

	@Override
	public VertexQuery query() {
		return new DefaultVertexQuery(this);
	}

	@Override
	public void remove() {
		getParent().removeVertex(this);
	}

	public void removeEdge(final Edge edge) {
		getParent().startTransaction(this);
		String label = edge.getLabel();

		boolean inChanged = false;
		Set<String> ins = getInEdgesSet(label);
		if (ins != null) {
			//			System.out.println("Removing an in edge from " + label + " with id " + edge.getId() + " from a vertex of type "
			//					+ getProperty("Form"));
			synchronized (ins) {
				inChanged = ins.remove(edge.getId());
			}
		} else {
			//			System.out.println("in edges were null from a vertex of type " + getProperty("Form") + ": " + getId());
		}
		// Set<Edge> inObjs = getInEdgeObjects();
		// synchronized (inObjs) {
		// inObjs.remove(edge);
		// }
		if (inChanged) {
			//			System.out.println("Ins were changed so recording cache invalidation...");
			Set<Edge> inObjsLabel = getInEdgeCache(label);
			synchronized (inObjsLabel) {
				inObjsLabel.remove(edge);
			}
			Map<String, Boolean> inDirtyMap = getInDirtyMap();
			synchronized (inDirtyMap) {
				inDirtyMap.put(label, true);
			}
		}

		boolean outChanged = false;
		Set<String> outs = getOutEdgesSet(label);
		if (outs != null) {
			//			System.out.println("Removing an out edge from " + label + " with id " + edge.getId() + " from a vertex of type "
			//					+ getProperty("Form"));
			synchronized (outs) {
				outChanged = outs.remove(edge.getId());
			}
		} else {
			//			System.out.println("out edges were null from a vertex of type " + getProperty("Form") + ": " + getId());
		}
		if (outChanged) {
			//			System.out.println("Out were changed so recording cache invalidation...");
			Set<Edge> outObjsLabel = getOutEdgeCache(label);
			synchronized (outObjsLabel) {
				outObjsLabel.remove(edge);
			}
			Map<String, Boolean> outDirtyMap = getOutDirtyMap();
			synchronized (outDirtyMap) {
				outDirtyMap.put(label, true);
			}
		}
	}

	boolean writeEdges() {
		return writeEdges(false);
	}

	@Override
	protected void reapplyChanges() {
		// validateEdges();
		writeEdges(false);
		super.reapplyChanges();
	}

	boolean writeEdges(final boolean force) {
		boolean result = false;
		Map<String, Set<String>> inMap = getInEdgesMap();
		Map<String, Boolean> inDirtyMap = getInDirtyMap();
		if (!inDirtyMap.isEmpty()) {
			for (String key : inDirtyMap.keySet()) {
				if (inDirtyMap.get(key) || force) {
					Set<String> edgeIds = inMap.get(key);
					if (edgeIds != null) {
						setProperty(DominoVertex.IN_PREFIX + key, edgeIds);
						setProperty("_COUNT" + DominoVertex.IN_PREFIX + key, edgeIds.size());
						result = true;
					}
					synchronized (inDirtyMap) {
						inDirtyMap.put(key, Boolean.FALSE);
					}
				}
			}
		}

		Map<String, Set<String>> outMap = getOutEdgesMap();
		Map<String, Boolean> outDirtyMap = getOutDirtyMap();
		if (!outDirtyMap.isEmpty()) {
			for (String key : outDirtyMap.keySet()) {
				if (outDirtyMap.get(key) || force) {
					Set<String> edgeIds = outMap.get(key);
					if (edgeIds != null) {
						setProperty(DominoVertex.OUT_PREFIX + key, edgeIds);
						setProperty("_COUNT" + DominoVertex.OUT_PREFIX + key, edgeIds.size());
						result = true;
					}
					synchronized (outDirtyMap) {
						outDirtyMap.put(key, Boolean.FALSE);
					}
				}
			}
		}

		// if (inDirty_ || force) {
		// if (inEdges_ != null) {
		// setProperty(DominoVertex.IN_NAME, inEdges_);
		// // getRawDocument().replaceItemValue(DominoVertex.IN_NAME, inEdges_);
		// setProperty(DominoVertex.IN_NAME + "_COUNT", inEdges_.size());
		// // getRawDocument().replaceItemValue(DominoVertex.IN_NAME + "_COUNT", inEdges_.size());
		// // System.out.println("Updating " + inEdges_.size() + " inEdges to vertex: " + getRawDocument().getFormName());
		// result = true;
		// }
		// inDirty_ = false;
		// }
		// if (outDirty_ || force) {
		// if (outEdges_ != null) {
		// setProperty(DominoVertex.OUT_NAME, outEdges_);
		// // getRawDocument().replaceItemValue(DominoVertex.OUT_NAME, outEdges_);
		// setProperty(DominoVertex.OUT_NAME + "_COUNT", outEdges_.size());
		// // getRawDocument().replaceItemValue(DominoVertex.OUT_NAME + "_COUNT", outEdges_.size());
		// // System.out.println("Updating " + outEdges_.size() + " outEdges to vertex: " + getRawDocument().getFormName());
		// result = true;
		// }
		// outDirty_ = false;
		// }
		return result;
	}

	public String validateEdges() {
		StringBuilder sb = new StringBuilder();
		Set<String> inIds = getInEdges();
		for (String id : inIds.toArray(new String[inIds.size()])) {
			Document chk = getParent().getRawDatabase().getDocumentByUNID(id);
			if (chk == null) {
				inIds.remove(id);
				inDirty_ = true;
				sb.append("IN: ");
				sb.append(id);
				sb.append(",");
			}
		}

		Set<String> outIds = getOutEdges();
		for (String id : outIds.toArray(new String[outIds.size()])) {
			Document chk = getParent().getRawDatabase().getDocumentByUNID(id);
			if (chk == null) {
				outIds.remove(id);
				outDirty_ = true;
				sb.append("OUT: ");
				sb.append(id);
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public IEdgeHelper getHelper(final IDominoEdgeType edgeType) {
		return getParent().getHelper(edgeType);
	}

	public Set<IEdgeHelper> findHelpers(final Vertex other) {
		return getParent().findHelpers(this, other);
	}

	public static class MultipleDefinedEdgeHelpers extends DominoGraphException {
		private static final long serialVersionUID = 1L;

		public MultipleDefinedEdgeHelpers(final DominoVertex element1, final DominoVertex element2) {
			super("Multiple EdgeHelpers found for vertexes of type " + element1.getClass().getName() + " and "
					+ element2.getClass().getName(), element1, element2);
		}
	}

	public static class UndefinedEdgeHelpers extends DominoGraphException {
		private static final long serialVersionUID = 1L;

		public UndefinedEdgeHelpers(final DominoVertex element1, final DominoVertex element2) {
			super("No EdgeHelpers found for vertexes of type " + element1.getClass().getName() + " and " + element2.getClass().getName(),
					element1, element2);
		}
	}

	public Edge relate(final DominoVertex other) throws MultipleDefinedEdgeHelpers {
		Set<IEdgeHelper> helpers = findHelpers(other);
		if (helpers.size() == 1) {
			for (IEdgeHelper helper : helpers) {
				return helper.makeEdge(other, this);
			}
			return null;
		} else if (helpers.size() == 0) {
			throw new UndefinedEdgeHelpers(this, other);

		} else {
			throw new MultipleDefinedEdgeHelpers(this, other);
		}

	}
	//
	//	public Edge find(Vertex other) {
	//		return getRuleHelper().findEdge(this, other);
	//	}

}
