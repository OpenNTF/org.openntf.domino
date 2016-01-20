package org.openntf.domino.graph2.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastSet;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.big.NoteList;
import org.openntf.domino.graph.DominoVertex;
import org.openntf.domino.graph2.DEdgeList;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.util.DefaultVertexQuery;
import com.tinkerpop.blueprints.util.MultiIterable;
import com.tinkerpop.blueprints.util.VerticesFromEdgesIterable;

public class DVertex extends DElement implements org.openntf.domino.graph2.DVertex {
	private static final long serialVersionUID = 1L;

	private static final Logger log_ = Logger.getLogger(DVertex.class.getName());
	public static final String IN_PREFIX = "_OPEN_IN_";
	public static final String OUT_PREFIX = "_OPEN_OUT_";

	protected FastSet<String> inDirtyKeySet_;
	protected FastSet<String> outDirtyKeySet_;
	//	private FastMap<String, FastSet<String>> inEdgesMap_;
	protected FastMap<String, NoteList> inEdgesMapCompressed_;
	//	private FastMap<String, FastSet<String>> outEdgesMap_;
	protected FastMap<String, NoteList> outEdgesMapCompressed_;
	protected transient FastMap<String, DEdgeList> inEdgeCache_;
	protected transient FastMap<String, DEdgeList> outEdgeCache_;

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
			DEdgeList result = null;
			if (labels == null || labels.length == 0) {
				result = getInEdgeObjects();
				if (result == null) {
					result = getOutEdgeObjects();
				} else {
					result.addAll(getOutEdgeObjects());
				}
			} else {
				result = getInEdgeObjects(labels);
				if (result == null) {
					result = getOutEdgeObjects(labels);
				} else {
					result.addAll(getOutEdgeObjects(labels));
				}
			}
			//			if (result != null) {
			//				System.out.println("Found " + result.size() + " edges going " + direction.name());
			//			}
			return result;
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
		//		boolean adding = false;
		String label = edge.getLabel();
		//		NoteList ins = getInEdgesSet(label);
		//		Object eid = edge.getId();
		//		if (eid instanceof NoteCoordinate) {
		//			// NOP
		//		} else if (eid instanceof CharSequence) {
		//			eid = new org.openntf.domino.big.impl.NoteCoordinate((CharSequence) eid);
		//		} else {
		//			log_.log(Level.WARNING, "Edge ids of type " + eid.getClass().getName() + " not yet supported");
		//		}
		//		if (!ins.contains(eid)) {
		//			adding = true;
		//			//			ins.add((NoteCoordinate) eid);
		//		} else {
		//			//			System.out.println("TEMP DEBUG: Not adding an in edge on " + label + " because it's already in the list");
		//		}
		//		if (adding) {
		getParent().startTransaction(this);
		getInDirtyKeySet().add(label);
		List<Edge> inLabelObjs = getInEdgeCache(label);
		inLabelObjs.add(edge);
		//		}
	}

	@Override
	public void addOutEdge(final Edge edge) {
		//		boolean adding = false;
		String label = edge.getLabel();
		//		NoteList outs = getOutEdgesSet(label);
		//		Object eid = edge.getId();
		//		if (eid instanceof NoteCoordinate) {
		//			// NOP
		//		} else if (eid instanceof CharSequence) {
		//			eid = new org.openntf.domino.big.impl.NoteCoordinate((CharSequence) eid);
		//		} else {
		//			log_.log(Level.WARNING, "Edge ids of type " + eid.getClass().getName() + " not yet supported");
		//		}
		//		if (!outs.contains(eid)) {
		//			adding = true;
		//			//			outs.add((NoteCoordinate) eid);
		//		} else {
		//			//			System.out.println("TEMP DEBUG: Not adding an out edge on " + label + " because it's already in the list");
		//		}
		//		if (adding) {
		getParent().startTransaction(this);
		getOutDirtyKeySet().add(label);
		List<Edge> outLabelObjs = getOutEdgeCache(label);
		outLabelObjs.add(edge);
		//		}
	}

	@Override
	public String validateEdges() {
		// TODO Implement this or remove comment
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
	public Iterable<Edge> getEdges(final String... labels) {
		DEdgeList result = new org.openntf.domino.graph2.impl.DEdgeList(this).atomic();
		result.addAll(getInEdgeObjects(labels));
		result.addAll(getOutEdgeObjects(labels));
		return result.unmodifiable();
	}

	@Override
	public void remove() {
		getParent().removeVertex(this);
	}

	@Override
	public Edge findInEdge(final Vertex otherVertex, final String label, final boolean isUnique) {
		DEdgeList edgeList = getInEdgeCache(label);
		edgeList.setUnique(isUnique);
		Edge result = edgeList.findEdge(otherVertex);
		return result;
	}

	@Override
	public Edge findOutEdge(final Vertex otherVertex, final String label, final boolean isUnique) {
		//		System.out.println("DEBUG: Attempting to find OUT edge");
		DEdgeList edgeList = getOutEdgeCache(label);
		edgeList.setUnique(isUnique);
		return edgeList.findEdge(otherVertex);
	}

	@Override
	public Edge findEdge(final Vertex otherVertex, final String label) {
		//		System.out.println("DEBUG: FIND method");
		Edge result = findInEdge(otherVertex, label, false);
		if (result == null) {
			result = findOutEdge(otherVertex, label, false);
		}
		return result;
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

	protected Map<String, DEdgeList> getInEdgeCache() {
		if (inEdgeCache_ == null) {
			inEdgeCache_ = new FastMap<String, DEdgeList>().atomic();
		}
		return inEdgeCache_;
	}

	protected DEdgeList getInEdgeCache(final String label) {
		Map<String, DEdgeList> inCache = getInEdgeCache();
		DEdgeList result = null;
		result = inCache.get(label);
		if (result == null) {
			NoteList list = getInEdgesSet(label);
			//			if (list.size() > 0) {
			//				Factory.println("Got a " + label + " list with " + list.size() + " entries for Vertex " + getId());
			//			}
			//			result = getParent().getEdgesFromIds(this, list);
			result = new DFastEdgeList(this, getParent(), list, label);
			//			result.setLabel(label);
			inCache.put(label, result);
		}
		return result.atomic();
	}

	protected DEdgeList getOutEdgeCache(final String label) {
		Map<String, DEdgeList> outCache = getOutEdgeCache();
		DEdgeList result = null;
		result = outCache.get(label);
		if (result == null) {
			NoteList list = getOutEdgesSet(label);
			result = new DFastEdgeList(this, getParent(), list, label);
			outCache.put(label, result);
		}
		return result.atomic();
	}

	protected FastMap<String, DEdgeList> getOutEdgeCache() {
		if (outEdgeCache_ == null) {
			outEdgeCache_ = new FastMap<String, DEdgeList>().atomic();
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

	protected NoteList getInEdgesSet(final String label) {
		NoteList edgeIds = getInEdgesMap().get(label);
		if (edgeIds == null) {
			edgeIds = new org.openntf.domino.big.impl.NoteList(true);
			String key = DominoVertex.IN_PREFIX + label;
			Map<String, Object> delegate = getDelegate();
			if (delegate.containsKey(key)) {
				if (delegate instanceof Document) {
					byte[] bytes = ((Document) delegate).readBinary(key);
					//					Factory.println("Loading a NoteList from an item for label " + label + " for vertex " + getId());
					edgeIds.loadByteArray(bytes);
				} else {
					Object o = getProperty(key, java.util.Collection.class);
					if (o instanceof NoteList) {
						edgeIds = ((NoteList) o);
					} else if (o instanceof java.util.Collection) {
						for (Object raw : (Collection<?>) o) {
							if (raw instanceof String) {
								edgeIds.add(new org.openntf.domino.big.impl.NoteCoordinate(
										""/*TODO NTF This should be some default replid*/, (String) raw));
							} else {
								//TODO NTF
							}
						}
					} else {
						log_.log(Level.SEVERE, "ALERT! InEdges returned something other than a Collection " + o.getClass().getName()
								+ ". We are clearing the values and rebuilding the edges.");
					}

				}
			} else {
				//				Factory.println("Created a new in NoteList for " + label + " in vertex " + getId());
				//				Throwable t = new Throwable();
				//				t.printStackTrace();
			}
			Map<String, NoteList> map = getInEdgesMap();
			map.put(label, edgeIds);
		} else {
			//			Factory.println("Found a cached in NoteList for " + label + " in vertex " + getId());
		}
		return edgeIds;
	}

	@Override
	public int getOutEdgeCount(final String label) {
		NoteList edgeIds = getOutEdgesMap().get(label);
		if (edgeIds == null) {
			try {
				return getProperty("_COUNT" + DominoVertex.OUT_PREFIX + label, Integer.class, false);
			} catch (Throwable t) {
				throw new RuntimeException("Exception getting edge count for label " + label, t);
			}
		} else {
			return edgeIds.size();
		}
	}

	protected NoteList getOutEdgesSet(final String label) {
		NoteList edgeIds = getOutEdgesMap().get(label);
		if (edgeIds == null) {
			edgeIds = new org.openntf.domino.big.impl.NoteList(true);
			String key = DominoVertex.OUT_PREFIX + label;
			Map<String, Object> delegate = getDelegate();
			if (delegate.containsKey(key)) {
				if (delegate instanceof Document) {
					byte[] bytes = ((Document) delegate).readBinary(key);
					//					System.out.println("TEMP DEBUG: read Document-based NoteList binary for " + label + " and found " + bytes.length
					//							+ " bytes");
					edgeIds.loadByteArray(bytes);
				} else {
					Object o = getProperty(key, java.util.Collection.class);
					if (o != null) {
						if (o instanceof NoteList) {
							edgeIds = ((NoteList) o);
						} else if (o instanceof java.util.Collection) {
							for (Object raw : (Collection<?>) o) {
								if (raw instanceof String) {
									edgeIds.add(new org.openntf.domino.big.impl.NoteCoordinate(
											""/*TODO NTF This should be some default replid*/, (String) raw));
								} else {
									//TODO NTF
								}
							}
						} else {
							log_.log(Level.SEVERE, "ALERT! OutEdges returned something other than a Collection " + o.getClass().getName()
									+ ". We are clearing the values and rebuilding the edges.");
						}
					}
				}
			} else {
				//				System.out.println("TEMP DEBUG Returning new out edge set for " + label);
			}
			Map<String, NoteList> map = getOutEdgesMap();
			map.put(label, edgeIds);
		}
		return edgeIds;
	}

	protected FastSet<String> getInDirtyKeySet() {
		if (inDirtyKeySet_ == null) {
			inDirtyKeySet_ = new FastSet<String>().atomic();
		}
		return inDirtyKeySet_;
	}

	protected Map<String, NoteList> getInEdgesMap() {
		if (inEdgesMapCompressed_ == null) {
			inEdgesMapCompressed_ = new FastMap<String, NoteList>().atomic();
		}
		return inEdgesMapCompressed_;
	}

	protected FastSet<String> getOutDirtyKeySet() {
		if (outDirtyKeySet_ == null) {
			outDirtyKeySet_ = new FastSet<String>().atomic();
		}
		return outDirtyKeySet_;
	}

	protected Map<String, NoteList> getOutEdgesMap() {
		if (outEdgesMapCompressed_ == null) {
			outEdgesMapCompressed_ = new FastMap<String, NoteList>().atomic();
		}
		return outEdgesMapCompressed_;
	}

	@Override
	public void applyChanges() {
		writeEdges();
		super.applyChanges();
	}

	protected boolean writeEdges() {
		boolean result = false;
		Map<String, NoteList> inMap = getInEdgesMap();
		FastSet<String> inDirtySet = getInDirtyKeySet();
		if (!inDirtySet.isEmpty()) {
			for (String key : inDirtySet) {
				NoteList edgeIds = inMap.get(key);
				if (edgeIds != null) {
					setProperty(DominoVertex.IN_PREFIX + key, edgeIds);
					setProperty("_COUNT" + DominoVertex.IN_PREFIX + key, edgeIds.size());
					result = true;
				}
			}
			inDirtySet.clear();
		}

		Map<String, NoteList> outMap = getOutEdgesMap();
		FastSet<String> outDirtySet = getOutDirtyKeySet();
		if (!outDirtySet.isEmpty()) {
			for (String key : outDirtySet) {
				NoteList edgeIds = outMap.get(key);
				if (edgeIds != null) {
					setProperty(DominoVertex.OUT_PREFIX + key, edgeIds);
					setProperty("_COUNT" + DominoVertex.OUT_PREFIX + key, edgeIds.size());
					result = true;
				}
			}
			outDirtySet.clear();
		}

		return result;
	}

	protected DEdgeList getInEdgeObjects(final String... labels) {
		Map<String, DEdgeList> inCache = getInEdgeCache();
		DEdgeList result = null;
		if (labels == null || labels.length == 0) {
			Set<String> labelSet = getInEdgeLabels();
			for (String label : labelSet) {
				if (result == null) {
					result = getInEdgeObjects(label);
				} else {
					result.addAll(getInEdgeObjects(label));
				}
			}
		} else if (labels.length == 1) {
			String label = labels[0];
			//			System.out.println("TEMP DEBUG Getting in edge objects for label " + label);
			result = inCache.get(label);
			if (result == null) {
				NoteList edgeIds = getInEdgesSet(label);
				//				DEdgeList edges = getParent().getEdgesFromIds(this, edgeIds);
				DEdgeList edges = new DFastEdgeList(this, getParent(), edgeIds, label);
				//				edges.setLabel(label);
				if (edges != null) {
					result = edges.atomic();
				}
				inCache.put(label, result);
			}
		} else {
			result = new org.openntf.domino.graph2.impl.DEdgeList(this);
			for (String label : labels) {
				result.addAll(getInEdgeObjects(label));
			}
		}
		return result == null ? null : result.unmodifiable();
	}

	protected DEdgeList getOutEdgeObjects(final String... labels) {
		FastMap<String, DEdgeList> outCache = getOutEdgeCache();
		DEdgeList result = null;
		if (labels == null || labels.length == 0) {
			Set<String> labelSet = getOutEdgeLabels();
			for (String label : labelSet) {
				if (result == null) {
					result = getOutEdgeObjects(label);
				} else {
					result.addAll(getOutEdgeObjects(label));
				}
			}
		} else if (labels.length == 1) {
			String label = labels[0];
			if (label == null) {
				return getOutEdgeObjects().unmodifiable();
			}
			result = outCache.get(label);
			if ((result == null || result.isEmpty()) && label.equalsIgnoreCase("contents")) {
				//				System.out.println("TEMP DEBUG getting 'contents' label for a vertex with delegate " + getDelegateType().getName());
				if (getDelegateType().equals(View.class)) {
					//					System.out.println("TEMP DEBUG getting contents from ViewVertex");
					DEdgeList edges = new DEdgeEntryList(this, (org.openntf.domino.graph2.impl.DElementStore) getStore());
					edges.setLabel(label);
					result = edges.unmodifiable();
				} else if (this instanceof DCategoryVertex) {
					DEdgeList edges = new DEdgeEntryList(this, (org.openntf.domino.graph2.impl.DElementStore) getStore());
					edges.setLabel(label);
					result = edges.unmodifiable();
				}
			}
			if (result == null) {
				//				System.out.println("TEMP DEBUG Getting out edge objects for label " + label);
				NoteList edgeIds = getOutEdgesSet(label);
				DEdgeList edges = new DFastEdgeList(this, getParent(), edgeIds, label);
				//						getParent().getEdgesFromIds(this, edgeIds);
				//				edges.setLabel(label);
				if (edges != null) {
					result = edges.atomic();
				}
			}
			if (result != null) {
				outCache.put(label, result);
			}
		} else {
			result = new org.openntf.domino.graph2.impl.DEdgeList(this);
			for (String label : labels) {
				result.addAll(getOutEdgeObjects(label));
			}
		}
		return result == null ? null : result.unmodifiable();
	}

	@Override
	public View getView() {
		if (getDelegateType().equals(org.openntf.domino.View.class)) {
			Database db = ((Document) getDelegate()).getParentDatabase();
			return db.getView((Document) getDelegate());
		}
		return null;
	}
}
