package org.openntf.domino.graph2.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastSet;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.graph.DominoVertex;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.util.DefaultVertexQuery;
import com.tinkerpop.blueprints.util.MultiIterable;
import com.tinkerpop.blueprints.util.VerticesFromEdgesIterable;

public class DVertex extends DElement implements org.openntf.domino.graph2.DVertex {
	private static final Logger log_ = Logger.getLogger(DVertex.class.getName());
	public static final String IN_PREFIX = "_OPEN_IN_";
	public static final String OUT_PREFIX = "_OPEN_OUT_";

	private FastSet<String> inDirtyKeySet_;
	private FastSet<String> outDirtyKeySet_;
	private FastMap<String, FastSet<String>> inEdgesMap_;
	private FastMap<String, FastSet<NoteCoordinate>> inEdgesMapCompressed_;
	private FastMap<String, FastSet<String>> outEdgesMap_;
	private FastMap<String, FastSet<NoteCoordinate>> outEdgesMapCompressed_;
	private transient FastMap<String, FastSet<Edge>> inEdgeCache_;
	private transient FastMap<String, FastSet<Edge>> outEdgeCache_;

	public DVertex(final org.openntf.domino.graph2.DGraph parent) {
		super(parent);
	}

	DVertex(final org.openntf.domino.graph2.DGraph parent, final Map<String, Object> delegate) {
		super(parent);
		setDelegate(delegate);
	}

	@Override
	public Iterable<Edge> getEdges(final Direction direction, final String... labels) {
		if (direction == Direction.IN) {
			if (labels == null || labels.length == 0) {
				return getInEdgeObjects().unmodifiable();
			} else {
				return getInEdgeObjects(labels);
			}
		} else if (direction == Direction.OUT) {
			if (labels == null || labels.length == 0) {
				return getOutEdgeObjects().unmodifiable();
			} else {
				return getOutEdgeObjects(labels);
			}
		} else {
			FastSet<Edge> result = new FastSet<Edge>();
			if (labels == null || labels.length == 0) {
				result.addAll(getInEdgeObjects());
				result.addAll(getOutEdgeObjects());
			} else {
				result.addAll(getInEdgeObjects(labels));
				result.addAll(getOutEdgeObjects(labels));
			}
			return result.unmodifiable();
		}
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

	@Override
	public Edge addEdge(final String label, final Vertex inVertex) {
		return getParent().addEdge(null, this, inVertex, label);
	}

	@Override
	public void addInEdge(final Edge edge) {
		boolean adding = false;
		String label = edge.getLabel();
		Set<String> ins = getInEdgesSet(label);
		if (!ins.contains(edge.getId())) {
			adding = true;
			ins.add((String) edge.getId());
		}
		if (adding) {
			getParent().startTransaction(this);
			getInDirtyKeySet().add(label);
			Set<Edge> inLabelObjs = getInEdgeCache(label);
			inLabelObjs.add(edge);
		}
	}

	@Override
	public void addOutEdge(final Edge edge) {
		boolean adding = false;
		String label = edge.getLabel();
		Set<String> outs = getOutEdgesSet(label);
		if (!outs.contains(edge.getId())) {
			adding = true;
			outs.add((String) edge.getId());
		}
		if (adding) {
			getParent().startTransaction(this);
			getOutDirtyKeySet().add(label);
			Set<Edge> outLabelObjs = getOutEdgeCache(label);
			outLabelObjs.add(edge);
		}
	}

	@Override
	public String validateEdges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getInEdgeLabels() {
		FastSet<String> result = new FastSet<String>();
		for (String key : getPropertyKeys()) {
			if (key.startsWith(IN_PREFIX)) {
				result.add(key.substring(IN_PREFIX.length()));
			}
		}
		return result.unmodifiable();
	}

	@Override
	public Set<String> getOutEdgeLabels() {
		FastSet<String> result = new FastSet<String>();
		for (String key : getPropertyKeys()) {
			if (key.startsWith(OUT_PREFIX)) {
				result.add(key.substring(OUT_PREFIX.length()));
			}
		}
		return result.unmodifiable();
	}

	@Override
	public Set<Edge> getEdges(final String... labels) {
		FastSet<Edge> result = new FastSet<Edge>();
		result.addAll(getInEdgeObjects(labels));
		result.addAll(getOutEdgeObjects(labels));
		return result.unmodifiable();
	}

	@Override
	public void remove() {
		getParent().removeVertex(this);
	}

	protected void removeEdge(final Edge edge) {
		getParent().startTransaction(this);
		String label = edge.getLabel();

		boolean inChanged = false;
		Set<String> ins = getInEdgesSet(label);
		if (ins != null) {
			inChanged = ins.remove(edge.getId());
		}
		if (inChanged) {
			Set<Edge> inObjsLabel = getInEdgeCache(label);
			inObjsLabel.remove(edge);
			getInDirtyKeySet().add(label);
		}

		boolean outChanged = false;
		Set<String> outs = getOutEdgesSet(label);
		if (outs != null) {
			outChanged = outs.remove(edge.getId());
		}
		if (outChanged) {
			Set<Edge> outObjsLabel = getOutEdgeCache(label);
			outObjsLabel.remove(edge);
			getOutDirtyKeySet().add(label);
		}
	}

	Map<String, FastSet<Edge>> getInEdgeCache() {
		if (inEdgeCache_ == null) {
			inEdgeCache_ = new FastMap<String, FastSet<Edge>>().atomic();
		}
		return inEdgeCache_;
	}

	FastSet<Edge> getInEdgeCache(final String label) {
		Map<String, FastSet<Edge>> inCache = getInEdgeCache();
		FastSet<Edge> result = null;
		result = inCache.get(label);
		if (result == null) {
			result = new FastSet<Edge>().atomic();
			inCache.put(label, result);
		}
		return result;
	}

	FastSet<Edge> getOutEdgeCache(final String label) {
		Map<String, FastSet<Edge>> outCache = getOutEdgeCache();
		FastSet<Edge> result = null;
		result = outCache.get(label);
		if (result == null) {
			result = new FastSet<Edge>().atomic();
			outCache.put(label, result);
		}
		return result;
	}

	FastMap<String, FastSet<Edge>> getOutEdgeCache() {
		if (outEdgeCache_ == null) {
			outEdgeCache_ = new FastMap<String, FastSet<Edge>>().atomic();
		}
		return outEdgeCache_;
	}

	@Override
	public int getInEdgeCount(final String label) {
		Set<String> edgeIds = getInEdgesMap().get(label);
		if (edgeIds == null) {
			return getProperty("_COUNT" + DominoVertex.IN_PREFIX + label, Integer.class, false);
		} else {
			return edgeIds.size();
		}
	}

	@SuppressWarnings("unchecked")
	Set<String> getInEdgesSet(final String label) {
		Set<String> edgeIds = getInEdgesMap().get(label);
		if (edgeIds == null) {
			Object o = getProperty(DominoVertex.IN_PREFIX + label, java.util.Collection.class);
			if (o != null) {
				if (o instanceof FastSet) {
					edgeIds = ((FastSet) o).atomic();
				} else if (o instanceof java.util.Collection) {
					FastSet<String> result = new FastSet<String>();
					result.addAll((Collection<String>) o);
					edgeIds = result.atomic();
				} else {
					log_.log(Level.SEVERE, "ALERT! InEdges returned something other than a Collection " + o.getClass().getName()
							+ ". We are clearing the values and rebuilding the edges.");
					edgeIds = new FastSet<String>().atomic();
				}
			} else {
				edgeIds = new FastSet<String>().atomic();
			}
			Map map = getInEdgesMap();
			//			synchronized (map) {
			map.put(label, edgeIds);
			//			}
		}
		return edgeIds;
	}

	@Override
	public int getOutEdgeCount(final String label) {
		Set<String> edgeIds = getOutEdgesMap().get(label);
		if (edgeIds == null) {
			return getProperty("_COUNT" + DominoVertex.OUT_PREFIX + label, Integer.class, false);
		} else {
			return edgeIds.size();
		}
	}

	FastSet<String> getOutEdgesSet(final String label) {
		FastSet<String> edgeIds = getOutEdgesMap().get(label);
		if (edgeIds == null) {
			Object o = getProperty(DominoVertex.OUT_PREFIX + label, java.util.Collection.class);
			if (o != null) {
				if (o instanceof FastSet) {
					edgeIds = ((FastSet) o).atomic();
				} else if (o instanceof java.util.Collection) {
					FastSet<String> result = new FastSet<String>();
					result.addAll((Collection<String>) o);
					edgeIds = result.atomic();
				} else {
					log_.log(Level.SEVERE, "ALERT! OutEdges returned something other than a Collection " + o.getClass().getName()
							+ ". We are clearing the values and rebuilding the edges.");
					edgeIds = new FastSet<String>().atomic();
				}
			} else {
				edgeIds = new FastSet<String>().atomic();
			}
			Map map = getOutEdgesMap();
			map.put(label, edgeIds);
		}
		return edgeIds;
	}

	FastSet<String> getInDirtyKeySet() {
		if (inDirtyKeySet_ == null) {
			inDirtyKeySet_ = new FastSet<String>().atomic();
		}
		return inDirtyKeySet_;
	}

	Map<String, FastSet<String>> getInEdgesMap() {
		if (inEdgesMap_ == null) {
			inEdgesMap_ = new FastMap<String, FastSet<String>>().atomic();
		}
		return inEdgesMap_;
	}

	FastSet<String> getOutDirtyKeySet() {
		if (outDirtyKeySet_ == null) {
			outDirtyKeySet_ = new FastSet<String>().atomic();
		}
		return outDirtyKeySet_;
	}

	Map<String, FastSet<String>> getOutEdgesMap() {
		if (outEdgesMap_ == null) {
			outEdgesMap_ = new FastMap<String, FastSet<String>>().atomic();
		}
		return outEdgesMap_;
	}

	@Override
	public void applyChanges() {
		writeEdges();
		applyChanges();
	}

	boolean writeEdges() {
		boolean result = false;
		Map<String, FastSet<String>> inMap = getInEdgesMap();
		FastSet<String> inDirtySet = getInDirtyKeySet();
		if (!inDirtySet.isEmpty()) {
			Iterator<String> it = inDirtySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				Set<String> edgeIds = inMap.get(key);
				if (edgeIds != null) {
					setProperty(DominoVertex.IN_PREFIX + key, edgeIds);
					setProperty("_COUNT" + DominoVertex.IN_PREFIX + key, edgeIds.size());
					result = true;
				}
				it.remove();
			}
		}

		Map<String, FastSet<String>> outMap = getOutEdgesMap();
		FastSet<String> outDirtySet = getOutDirtyKeySet();
		if (!outDirtySet.isEmpty()) {
			Iterator<String> it = outDirtySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				Set<String> edgeIds = outMap.get(key);
				if (edgeIds != null) {
					setProperty(DominoVertex.OUT_PREFIX + key, edgeIds);
					setProperty("_COUNT" + DominoVertex.OUT_PREFIX + key, edgeIds.size());
					result = true;
				}
				it.remove();
			}

		}

		return result;
	}

	FastSet<Edge> getInEdgeObjects(final String... labels) {
		Map<String, FastSet<Edge>> inCache = getInEdgeCache();
		FastSet<Edge> result = null;
		if (labels == null || labels.length == 0) {
			result = new FastSet<Edge>().atomic();
			Set<String> labelSet = getInEdgeLabels();
			for (String label : labelSet) {
				result.addAll(getInEdgeObjects(label));
			}
		} else if (labels.length == 1) {
			String label = labels[0];
			result = inCache.get(label);
			if (result == null) {
				Set<String> edgeIds = getInEdgesSet(label);
				FastSet<Edge> edges = getParent().getEdgesFromIds(edgeIds);
				if (edges != null) {
					result = edges.atomic();
				}
				inCache.put(label, result);
			}
		} else {
			result = new FastSet<Edge>();
			for (String label : labels) {
				result.addAll(getInEdgeObjects(label));
			}
		}
		return result.unmodifiable();
	}

	FastSet<Edge> getOutEdgeObjects(final String... labels) {
		FastMap<String, FastSet<Edge>> outCache = getOutEdgeCache();
		FastSet<Edge> result = null;
		if (labels == null || labels.length == 0) {
			result = new FastSet<Edge>();
			Set<String> labelSet = getOutEdgeLabels();
			for (String label : labelSet) {
				FastSet<Edge> curEdges = getOutEdgeObjects(label);
				result.addAll(curEdges);
			}
		} else if (labels.length == 1) {
			String label = labels[0];
			if (label == null) {
				return getOutEdgeObjects().unmodifiable();
			}
			result = outCache.get(label);
			if (result == null) {
				FastSet<String> edgeIds = getOutEdgesSet(label);
				FastSet<Edge> edges = getParent().getEdgesFromIds(edgeIds);
				if (edges != null) {
					result = edges.atomic();
				}
				outCache.put(label, result);
			}
		} else {
			result = new FastSet<Edge>();
			for (String label : labels) {
				result.addAll(getOutEdgeObjects(label));
			}
		}
		return result.unmodifiable();
	}

}
