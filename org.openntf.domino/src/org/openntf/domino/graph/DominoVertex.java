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
import java.util.logging.Level;
import java.util.logging.Logger;

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
	public static final String IN_NAME = "_OPEN_IN";
	public static final String OUT_NAME = "_OPEN_OUT";

	private static final long serialVersionUID = 1L;
	private Boolean inDirty_ = false;
	private Set<String> inEdges_;
	private transient Set<Edge> inEdgesObjects_;
	private transient Map<String, Set<Edge>> inEdgeCache_;
	private Boolean outDirty_ = false;
	private transient Set<Edge> outEdgesObjects_;
	private transient Map<String, Set<Edge>> outEdgeCache_;
	private Set<String> outEdges_;

	public DominoVertex(final DominoGraph parent, final org.openntf.domino.Document doc) {
		super(parent, doc);
	}

	@Override
	public Edge addEdge(final String label, final Vertex vertex) {
		return parent_.addEdge(null, this, vertex, label);
	}

	public void addInEdge(final Edge edge) {
		boolean adding = false;
		Set<String> ins = getInEdges();
		synchronized (ins) {
			if (!ins.contains((String) edge.getId())) {
				adding = true;
				ins.add((String) edge.getId());
			}
		}
		if (adding) {
			getParent().startTransaction(this);
			inDirty_ = true;

			Set<Edge> inObjs = getInEdgeObjects();
			synchronized (inObjs) {
				inObjs.add(edge);
			}
			Set<Edge> inLabelObjs = getInEdgeCache(edge.getLabel());
			synchronized (inLabelObjs) {
				inLabelObjs.add(edge);
			}
			// writeEdges();
		}
	}

	public void addOutEdge(final Edge edge) {
		boolean adding = false;
		Set<String> outs = getOutEdges();
		synchronized (outs) {
			if (!outs.contains((String) edge.getId())) {
				adding = true;
				outs.add((String) edge.getId());
			}
		}
		if (adding) {
			getParent().startTransaction(this);
			outDirty_ = true;

			Set<Edge> outObjs = getOutEdgeObjects();
			synchronized (outObjs) {
				outObjs.add(edge);
			}
			Set<Edge> outLabelObjs = getOutEdgeCache(edge.getLabel());
			synchronized (outLabelObjs) {
				outLabelObjs.add(edge);
			}
			// writeEdges();
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
		if (labels.length == 1) {
			String label = labels[0];
			// System.out.println("Getting in edges from " + getClass().getName() + " with label: " + label + " ...");
			synchronized (inCache) {
				result = inCache.get(label);
			}
			if (result == null) {
				result = Collections.synchronizedSet(new LinkedHashSet<Edge>());
				Set<Edge> allEdges = Collections.unmodifiableSet(getInEdgeObjects());
				// if (allEdges.size() < 1) {
				// System.out.println("No unfiltered in edges. However there are " + getOutEdgeObjects().size() + " out edges!");
				// System.out.println("And " + getOutEdgeObjects(label).size() + " out edges with the label " + label + "!");
				// } else {
				// System.out.println("Unfiltered in edge count: " + allEdges.size());
				// }
				for (Edge edge : allEdges) {
					if (label.equals(edge.getLabel())) {
						result.add(edge);
					}
				}
				// System.out.println("Filtered in edge count: " + result.size());
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
		if (labels.length == 1) {
			String label = labels[0];
			if (label == null) {
				return Collections.unmodifiableSet(getOutEdgeObjects());
			}
			synchronized (outCache) {
				result = outCache.get(label);
			}
			if (result == null) {
				result = Collections.synchronizedSet(new LinkedHashSet<Edge>());
				Set<Edge> allEdges = Collections.unmodifiableSet(getOutEdgeObjects());
				if (!allEdges.isEmpty()) {
					for (Edge edge : allEdges) {
						if (edge == null) {

						} else {
							String curLabel = edge.getLabel();
							if (label.equals(curLabel)) {
								result.add(edge);
							}
						}
					}
				}
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Set<Edge> getInEdgeObjects() {
		if (inEdgesObjects_ == null) {
			Set<String> ins = getInEdges();
			Iterable<Edge> edges = getParent().getEdgesFromIds(ins);
			if (edges != null) {
				inEdgesObjects_ = Collections.synchronizedSet((LinkedHashSet) edges);
			}
		}
		return inEdgesObjects_;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Set<Edge> getOutEdgeObjects() {
		if (outEdgesObjects_ == null) {
			Set<String> outs = getOutEdges();
			Iterable<Edge> edges = getParent().getEdgesFromIds(outs);
			if (edges != null) {
				outEdgesObjects_ = Collections.synchronizedSet((LinkedHashSet) edges);
			}
		}
		return outEdgesObjects_;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getInEdges() {
		if (inEdges_ == null) {
			Object o = getProperty(DominoVertex.IN_NAME, java.util.Collection.class);
			if (o != null) {
				if (o instanceof LinkedHashSet) {
					inEdges_ = Collections.synchronizedSet((LinkedHashSet) o);
				} else if (o instanceof java.util.Collection) {
					inEdges_ = Collections.synchronizedSet(new LinkedHashSet<String>((Collection<String>) o));
				} else {
					log_.log(Level.WARNING, "ALERT! InEdges returned something other than a Collection " + o.getClass().getName());
				}
			} else {
				inEdges_ = Collections.synchronizedSet(new LinkedHashSet<String>());
			}
		}

		return inEdges_;
	}

	@SuppressWarnings("unchecked")
	public Set<String> getOutEdges() {
		if (outEdges_ == null) {
			Object o = getProperty(DominoVertex.OUT_NAME, java.util.Collection.class);
			if (o != null) {
				if (o instanceof LinkedHashSet) {
					outEdges_ = Collections.synchronizedSet((LinkedHashSet) o);
				} else if (o instanceof java.util.Collection) {
					outEdges_ = Collections.synchronizedSet(new LinkedHashSet<String>((Collection<String>) o));
				} else {
					log_.log(Level.WARNING, "ALERT! OutEdges returned something other than a Collection " + o.getClass().getName());
				}
			} else {
				outEdges_ = Collections.synchronizedSet(new LinkedHashSet<String>());
			}
		}
		return outEdges_;
	}

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

	public void removeEdge(final Edge edge) {
		getParent().startTransaction(this);
		Set<String> ins = getInEdges();
		synchronized (ins) {
			ins.remove(edge.getId());
		}
		Set<Edge> inObjs = getInEdgeObjects();
		synchronized (inObjs) {
			inObjs.remove(edge);
		}
		Set<Edge> inObjsLabel = getInEdgeCache(edge.getLabel());
		synchronized (inObjsLabel) {
			inObjsLabel.remove(edge);
		}
		inDirty_ = true;

		Set<String> outs = getOutEdges();
		synchronized (outs) {
			outs.remove(edge.getId());
		}
		Set<Edge> outObjs = getOutEdgeObjects();
		synchronized (outObjs) {
			outObjs.remove(edge);
		}
		Set<Edge> outObjsLabel = getOutEdgeCache(edge.getLabel());
		synchronized (outObjsLabel) {
			outObjsLabel.remove(edge);
		}
		outDirty_ = true;
	}

	boolean writeEdges() {
		return writeEdges(false);
	}

	@Override
	protected void reapplyChanges() {
		writeEdges(false);
		super.reapplyChanges();
	}

	boolean writeEdges(final boolean force) {
		boolean result = false;
		if (inDirty_ || force) {
			if (inEdges_ != null) {
				setProperty(DominoVertex.IN_NAME, inEdges_);
				// getRawDocument().replaceItemValue(DominoVertex.IN_NAME, inEdges_);
				setProperty(DominoVertex.IN_NAME + "_COUNT", inEdges_.size());
				// getRawDocument().replaceItemValue(DominoVertex.IN_NAME + "_COUNT", inEdges_.size());
				// System.out.println("Updating " + inEdges_.size() + " inEdges to vertex: " + getRawDocument().getFormName());
				result = true;
			}
			inDirty_ = false;
		}
		if (outDirty_ || force) {
			if (outEdges_ != null) {
				setProperty(DominoVertex.OUT_NAME, outEdges_);
				// getRawDocument().replaceItemValue(DominoVertex.OUT_NAME, outEdges_);
				setProperty(DominoVertex.OUT_NAME + "_COUNT", outEdges_.size());
				// getRawDocument().replaceItemValue(DominoVertex.OUT_NAME + "_COUNT", outEdges_.size());
				// System.out.println("Updating " + outEdges_.size() + " outEdges to vertex: " + getRawDocument().getFormName());
				result = true;
			}
			outDirty_ = false;
		}
		return result;
	}

}
