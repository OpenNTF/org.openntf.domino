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
import javolution.util.FastTable;

import org.openntf.domino.big.impl.NoteCoordinate;
import org.openntf.domino.big.impl.NoteList;
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
	//	private FastMap<String, FastSet<String>> inEdgesMap_;
	private FastMap<String, NoteList> inEdgesMapCompressed_;
	//	private FastMap<String, FastSet<String>> outEdgesMap_;
	private FastMap<String, NoteList> outEdgesMapCompressed_;
	private transient FastMap<String, FastTable<Edge>> inEdgeCache_;
	private transient FastMap<String, FastTable<Edge>> outEdgeCache_;

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
		NoteList ins = getInEdgesSet(label);
		if (!ins.contains(edge.getId())) {
			adding = true;
			if (edge.getId() instanceof NoteCoordinate) {
				ins.add((NoteCoordinate) edge.getId());
			} else {
				//TODO
				//				ins.add((String) edge.getId());
			}
		}
		if (adding) {
			getParent().startTransaction(this);
			getInDirtyKeySet().add(label);
			List<Edge> inLabelObjs = getInEdgeCache(label);
			inLabelObjs.add(edge);
		}
	}

	@Override
	public void addOutEdge(final Edge edge) {
		boolean adding = false;
		String label = edge.getLabel();
		NoteList outs = getOutEdgesSet(label);
		if (!outs.contains(edge.getId())) {
			adding = true;
			if (edge.getId() instanceof NoteCoordinate) {
				outs.add((NoteCoordinate) edge.getId());
			} else {
				//TODO
				//				outs.add((String) edge.getId());
			}
		}
		if (adding) {
			getParent().startTransaction(this);
			getOutDirtyKeySet().add(label);
			List<Edge> outLabelObjs = getOutEdgeCache(label);
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
		NoteList ins = getInEdgesSet(label);
		if (ins != null) {
			inChanged = ins.remove(edge.getId());
		}
		if (inChanged) {
			List<Edge> inObjs = getInEdgeCache(label);
			inObjs.remove(edge);
			getInDirtyKeySet().add(label);
		}

		boolean outChanged = false;
		NoteList outs = getOutEdgesSet(label);
		if (outs != null) {
			outChanged = outs.remove(edge.getId());
		}
		if (outChanged) {
			List<Edge> outObjs = getOutEdgeCache(label);
			outObjs.remove(edge);
			getOutDirtyKeySet().add(label);
		}
	}

	Map<String, FastTable<Edge>> getInEdgeCache() {
		if (inEdgeCache_ == null) {
			inEdgeCache_ = new FastMap<String, FastTable<Edge>>().atomic();
		}
		return inEdgeCache_;
	}

	FastTable<Edge> getInEdgeCache(final String label) {
		Map<String, FastTable<Edge>> inCache = getInEdgeCache();
		FastTable<Edge> result = null;
		result = inCache.get(label);
		if (result == null) {
			result = new FastTable<Edge>().atomic();
			inCache.put(label, result);
		}
		return result;
	}

	FastTable<Edge> getOutEdgeCache(final String label) {
		Map<String, FastTable<Edge>> outCache = getOutEdgeCache();
		FastTable<Edge> result = null;
		result = outCache.get(label);
		if (result == null) {
			result = new FastTable<Edge>().atomic();
			outCache.put(label, result);
		}
		return result;
	}

	FastMap<String, FastTable<Edge>> getOutEdgeCache() {
		if (outEdgeCache_ == null) {
			outEdgeCache_ = new FastMap<String, FastTable<Edge>>().atomic();
		}
		return outEdgeCache_;
	}

	@Override
	public int getInEdgeCount(final String label) {
		NoteList edgeIds = getInEdgesMap().get(label);
		if (edgeIds == null) {
			return getProperty("_COUNT" + DominoVertex.IN_PREFIX + label, Integer.class, false);
		} else {
			return edgeIds.size();
		}
	}

	@SuppressWarnings("unchecked")
	NoteList getInEdgesSet(final String label) {
		NoteList edgeIds = getInEdgesMap().get(label);
		if (edgeIds == null) {
			Object o = getProperty(DominoVertex.IN_PREFIX + label, java.util.Collection.class);
			if (o != null) {
				if (o instanceof NoteList) {
					edgeIds = ((NoteList) o);
				} else if (o instanceof java.util.Collection) {
					NoteList result = new NoteList(true);
					for (Object raw : (Collection) o) {
						if (raw instanceof String) {
							result.add(new NoteCoordinate(""/*TODO NTF This should be some default replid*/, (String) raw));
						} else {
							//TODO NTF
						}
					}
					edgeIds = result;
				} else {
					log_.log(Level.SEVERE, "ALERT! InEdges returned something other than a Collection " + o.getClass().getName()
							+ ". We are clearing the values and rebuilding the edges.");
					edgeIds = new NoteList(true);
				}
			} else {
				edgeIds = new NoteList(true);
			}
			Map map = getInEdgesMap();
			map.put(label, edgeIds);
		}
		return edgeIds;
	}

	@Override
	public int getOutEdgeCount(final String label) {
		NoteList edgeIds = getOutEdgesMap().get(label);
		if (edgeIds == null) {
			return getProperty("_COUNT" + DominoVertex.OUT_PREFIX + label, Integer.class, false);
		} else {
			return edgeIds.size();
		}
	}

	NoteList getOutEdgesSet(final String label) {
		NoteList edgeIds = getOutEdgesMap().get(label);
		if (edgeIds == null) {
			Object o = getProperty(DominoVertex.OUT_PREFIX + label, java.util.Collection.class);
			if (o != null) {
				if (o instanceof NoteList) {
					edgeIds = ((NoteList) o);
				} else if (o instanceof java.util.Collection) {
					NoteList result = new NoteList(true);
					for (Object raw : (Collection) o) {
						if (raw instanceof String) {
							result.add(new NoteCoordinate(""/*TODO NTF This should be some default replid*/, (String) raw));
						} else {
							//TODO NTF
						}
					}
					edgeIds = result;
				} else {
					log_.log(Level.SEVERE, "ALERT! OutEdges returned something other than a Collection " + o.getClass().getName()
							+ ". We are clearing the values and rebuilding the edges.");
					edgeIds = new NoteList(true);
				}
			} else {
				edgeIds = new NoteList(true);
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

	Map<String, NoteList> getInEdgesMap() {
		if (inEdgesMapCompressed_ == null) {
			inEdgesMapCompressed_ = new FastMap<String, NoteList>().atomic();
		}
		return inEdgesMapCompressed_;
	}

	FastSet<String> getOutDirtyKeySet() {
		if (outDirtyKeySet_ == null) {
			outDirtyKeySet_ = new FastSet<String>().atomic();
		}
		return outDirtyKeySet_;
	}

	Map<String, NoteList> getOutEdgesMap() {
		if (outEdgesMapCompressed_ == null) {
			outEdgesMapCompressed_ = new FastMap<String, NoteList>().atomic();
		}
		return outEdgesMapCompressed_;
	}

	@Override
	public void applyChanges() {
		writeEdges();
		applyChanges();
	}

	boolean writeEdges() {
		boolean result = false;
		Map<String, NoteList> inMap = getInEdgesMap();
		FastSet<String> inDirtySet = getInDirtyKeySet();
		if (!inDirtySet.isEmpty()) {
			Iterator<String> it = inDirtySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				NoteList edgeIds = inMap.get(key);
				if (edgeIds != null) {
					setProperty(DominoVertex.IN_PREFIX + key, edgeIds);
					setProperty("_COUNT" + DominoVertex.IN_PREFIX + key, edgeIds.size());
					result = true;
				}
				it.remove();
			}
		}

		Map<String, NoteList> outMap = getOutEdgesMap();
		FastSet<String> outDirtySet = getOutDirtyKeySet();
		if (!outDirtySet.isEmpty()) {
			Iterator<String> it = outDirtySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				NoteList edgeIds = outMap.get(key);
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

	FastTable<Edge> getInEdgeObjects(final String... labels) {
		Map<String, FastTable<Edge>> inCache = getInEdgeCache();
		FastTable<Edge> result = null;
		if (labels == null || labels.length == 0) {
			result = new FastTable<Edge>().atomic();
			Set<String> labelSet = getInEdgeLabels();
			for (String label : labelSet) {
				result.addAll(getInEdgeObjects(label));
			}
		} else if (labels.length == 1) {
			String label = labels[0];
			result = inCache.get(label);
			if (result == null) {
				NoteList edgeIds = getInEdgesSet(label);
				FastTable<Edge> edges = getParent().getEdgesFromIds(edgeIds);
				if (edges != null) {
					result = edges.atomic();
				}
				inCache.put(label, result);
			}
		} else {
			result = new FastTable<Edge>();
			for (String label : labels) {
				result.addAll(getInEdgeObjects(label));
			}
		}
		return result.unmodifiable();
	}

	FastTable<Edge> getOutEdgeObjects(final String... labels) {
		FastMap<String, FastTable<Edge>> outCache = getOutEdgeCache();
		FastTable<Edge> result = null;
		if (labels == null || labels.length == 0) {
			result = new FastTable<Edge>();
			Set<String> labelSet = getOutEdgeLabels();
			for (String label : labelSet) {
				FastTable<Edge> curEdges = getOutEdgeObjects(label);
				result.addAll(curEdges);
			}
		} else if (labels.length == 1) {
			String label = labels[0];
			if (label == null) {
				return getOutEdgeObjects().unmodifiable();
			}
			result = outCache.get(label);
			if (result == null) {
				NoteList edgeIds = getOutEdgesSet(label);
				FastTable<Edge> edges = getParent().getEdgesFromIds(edgeIds);
				if (edges != null) {
					result = edges.atomic();
				}
				outCache.put(label, result);
			}
		} else {
			result = new FastTable<Edge>();
			for (String label : labels) {
				result.addAll(getOutEdgeObjects(label));
			}
		}
		return result.unmodifiable();
	}

}
