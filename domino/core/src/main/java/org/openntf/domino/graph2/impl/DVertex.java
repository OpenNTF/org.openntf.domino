/**
 * Copyright © 2013-2020 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.graph2.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.big.NoteList;
import org.openntf.domino.graph2.DEdgeList;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.util.DefaultVertexQuery;
import com.tinkerpop.blueprints.util.MultiIterable;
import com.tinkerpop.blueprints.util.VerticesFromEdgesIterable;

import javolution.util.FastMap;
import javolution.util.FastSet;

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
	protected Map<String, DEdgeList> inEdgeCache_;
	protected Map<String, DEdgeList> outEdgeCache_;

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
		String label = edge.getLabel();
		getParent().startTransaction(this);
		getInDirtyKeySet().add(label);
		List<Edge> inLabelObjs = getInEdgeObjects(label);
		inLabelObjs.add(edge);
	}

	@Override
	public void addOutEdge(final Edge edge) {
		String label = edge.getLabel();
		getParent().startTransaction(this);
		getOutDirtyKeySet().add(label);
		List<Edge> outLabelObjs = getOutEdgeObjects(label);
		outLabelObjs.add(edge);
	}

	@Override
	public boolean validateEdges() {
		boolean result = true;

		Set<String> inLabels = getInEdgeLabels();
		for (String label : inLabels) {
			List<Edge> curEdges = getInEdgeObjects(label);
			if (curEdges instanceof DFastEdgeList) {
				DFastEdgeList curList = (DFastEdgeList) curEdges;
				boolean dirty = curList.repairInvalidEdges();
				if (dirty) {
					result = false;
					getInDirtyKeySet().add(label);
				}
			}
		}

		Set<String> outLabels = getOutEdgeLabels();
		for (String label : outLabels) {
			List<Edge> curEdges = getOutEdgeObjects(label);
			if (curEdges instanceof DFastEdgeList) {
				DFastEdgeList curList = (DFastEdgeList) curEdges;
				boolean dirty = curList.repairInvalidEdges();
				if (dirty) {
					result = false;
					getOutDirtyKeySet().add(label);
				}
			}
		}
		return result;
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
		if (beforeRemove()) {
			getParent().removeVertex(this);
		}
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
		//		System.out.println("Removing an edge of type " + label + " from vertex " + getId());
		Object inId = ((DEdge) edge).getVertexId(Direction.IN);
		if (inId.equals(this.getId())) {
			//			System.out.println("Removing from IN");
			boolean inChanged = false;
			try {
				List<Edge> inObjs = getInEdgeObjects(label);
				inChanged = inObjs.remove(edge);
				if (inChanged) {
					getInDirtyKeySet().add(label);
				}
			} catch (Throwable t) {
				System.err
						.println("Exception occured trying to remove an edge from vertex " + getId() + ": " + t.getClass().getSimpleName());
				t.printStackTrace();
			}
		}

		Object outId = ((DEdge) edge).getVertexId(Direction.OUT);
		if (outId.equals(this.getId())) {
			//			System.out.println("Removing from OUT");
			boolean outChanged = false;
			try {
				List<Edge> outObjs = getOutEdgeObjects(label);
				outChanged = outObjs.remove(edge);
				if (outChanged) {
					getOutDirtyKeySet().add(label);
				}
			} catch (Throwable t) {
				System.err
						.println("Exception occured trying to remove an edge from vertex " + getId() + ": " + t.getClass().getSimpleName());
				t.printStackTrace();
			}
		}
	}

	protected Map<String, DEdgeList> getInEdgeCache() {
		if (inEdgeCache_ == null) {
			inEdgeCache_ = Collections.synchronizedMap(new HashMap<String, DEdgeList>());
		}
		return inEdgeCache_;
	}

	protected DEdgeList getInEdgeCache(final String label) {
		Map<String, DEdgeList> inCache = getInEdgeCache();
		DEdgeList result = null;
		result = inCache.get(label);
		if (result == null) {
			NoteList list = getInEdgesSet(label);
			int count = getInEdgeCount(label);
			if (count != list.size()) {
				setProperty("_COUNT" + DVertex.IN_PREFIX + label, list.size());
				applyChanges();
			}
			result = new DFastEdgeList(this, getParent(), list, label);
			inCache.put(label, result);
			//			System.out.println("TEMP DEBUG Cache MISS for edge list " + label + " size " + result.size() + " for "
			//					+ result.getClass().getSimpleName() + ": " + System.identityHashCode(result) + " into inCache: "
			//					+ System.identityHashCode(inCache) + " from Vertex: " + System.identityHashCode(this));
		} else {
			//			System.out.println("TEMP DEBUG Cache hit for edge list " + label + " size " + result.size() + " for "
			//					+ result.getClass().getSimpleName() + ": " + System.identityHashCode(result) + " from inCache: "
			//					+ System.identityHashCode(inCache) + " from Vertex: " + System.identityHashCode(this));
		}
		return result.atomic();
	}

	protected DEdgeList getOutEdgeCache(final String label) {
		Map<String, DEdgeList> outCache = getOutEdgeCache();
		DEdgeList result = null;
		result = outCache.get(label);
		if (result == null) {
			NoteList list = getOutEdgesSet(label);
			int count = getOutEdgeCount(label);
			if (count != list.size()) {
				setProperty("_COUNT" + DVertex.OUT_PREFIX + label, list.size());
				applyChanges();
			}
			result = new DFastEdgeList(this, getParent(), list, label);
			outCache.put(label, result);
		}
		return result.atomic();
	}

	protected Map<String, DEdgeList> getOutEdgeCache() {
		if (outEdgeCache_ == null) {
			outEdgeCache_ = Collections.synchronizedMap(new HashMap<String, DEdgeList>());
		}
		return outEdgeCache_;
	}

	@Override
	public int getInEdgeCount(final String label) {
		if (hasProperty("_COUNT" + DVertex.IN_PREFIX + label)) {
			return getProperty("_COUNT" + DVertex.IN_PREFIX + label, Integer.class, false);
		} else {
			NoteList edgeIds = getInEdgesMap().get(label);
			if (edgeIds == null) {
				return getProperty("_COUNT" + DVertex.IN_PREFIX + label, Integer.class, false);
			} else {
				return edgeIds.size();
			}
		}
	}

	protected NoteList getInEdgesSet(final String label) {
		NoteList edgeIds = getInEdgesMap().get(label);
		if (edgeIds == null) {
			edgeIds = new org.openntf.domino.big.impl.NoteList(true);
			String key = DVertex.IN_PREFIX + label;
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
		if (hasProperty("_COUNT" + DVertex.OUT_PREFIX + label)) {
			try {
				return getProperty("_COUNT" + DVertex.OUT_PREFIX + label, Integer.class, false);
			} catch (Throwable t) {
				throw new RuntimeException("Exception getting edge count for label " + label, t);
			}
		} else {
			NoteList edgeIds = getOutEdgesMap().get(label);
			if (edgeIds == null) {
				try {
					return getProperty("_COUNT" + DVertex.OUT_PREFIX + label, Integer.class, false);
				} catch (Throwable t) {
					throw new RuntimeException("Exception getting edge count for label " + label, t);
				}
			} else {
				return edgeIds.size();
			}
		}
	}

	protected NoteList getOutEdgesSet(final String label) {
		NoteList edgeIds = getOutEdgesMap().get(label);
		if (edgeIds == null) {
			edgeIds = new org.openntf.domino.big.impl.NoteList(true);
			String key = DVertex.OUT_PREFIX + label;
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
		if (beforeUpdate()) {
			writeEdges();
			super.applyChanges();
		}
	}

	protected boolean writeEdges() {
		boolean result = false;
		Map<String, NoteList> inMap = getInEdgesMap();
		FastSet<String> inDirtySet = getInDirtyKeySet();
		if (!inDirtySet.isEmpty()) {
			for (String key : inDirtySet) {
				NoteList edgeIds = inMap.get(key);
				if (edgeIds != null) {
					//					if ("foundin".equalsIgnoreCase(key)) {
					//						System.out.println("Writing a FoundIn IN list with " + edgeIds.size() + " elements.");
					//					}
					if (edgeIds.size() == 0) {
						removeProperty(DVertex.IN_PREFIX + key);
						removeProperty("_COUNT" + DVertex.IN_PREFIX + key);
						result = true;
					} else {
						setProperty(DVertex.IN_PREFIX + key, edgeIds);
						setProperty("_COUNT" + DVertex.IN_PREFIX + key, edgeIds.size());
						result = true;
					}
					//					getInEdgeCache().remove(key);
					//					inMap.remove(key);
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
					//					if ("foundin".equalsIgnoreCase(key)) {
					//						System.out.println("Writing a FoundIn OUT list with " + edgeIds.size() + " elements.");
					//					}
					if (edgeIds.size() == 0) {
						removeProperty(DVertex.OUT_PREFIX + key);
						removeProperty("_COUNT" + DVertex.OUT_PREFIX + key);
						result = true;
					} else {
						setProperty(DVertex.OUT_PREFIX + key, edgeIds);
						setProperty("_COUNT" + DVertex.OUT_PREFIX + key, edgeIds.size());
						result = true;
					}
					//					getOutEdgeCache().remove(key);
					//					outMap.remove(key);
				}
			}
			outDirtySet.clear();
		}

		return result;
	}

	protected DEdgeList getInEdgeObjects(final String... labels) {
		//		Map<String, DEdgeList> inCache = getInEdgeCache();
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
			result = getInEdgeCache(label);
		} else {
			result = new org.openntf.domino.graph2.impl.DEdgeList(this);
			for (String label : labels) {
				result.addAll(getInEdgeObjects(label));
			}
		}
		return result == null ? null : result.unmodifiable();
	}

	protected DEdgeList getOutEdgeObjects(final String... labels) {
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
			//			if (label == null) {
			//				return getOutEdgeObjects().unmodifiable();
			//			}
			result = getOutEdgeCache(label);
			if ((result == null || result.isEmpty()) && label.equalsIgnoreCase("contents")) {
				if (getDelegateType().equals(View.class)) {
					DEdgeList edges = new DEdgeEntryList(this, (org.openntf.domino.graph2.impl.DElementStore) getStore());
					edges.setLabel(label);
					result = edges.unmodifiable();
				} else if (this instanceof DCategoryVertex || String.valueOf(getId()).startsWith("VC")) {
					DEdgeList edges = new DEdgeEntryList(this, (org.openntf.domino.graph2.impl.DElementStore) getStore());
					edges.setLabel(label);
					result = edges.unmodifiable();
				} else {
					//					System.out.println("TEMP DEBUG unable to get contents edgelist from a " + this.getClass().getName() + " with id "
					//							+ String.valueOf(this.getId()));
				}
			}
			if ((result == null || result.isEmpty()) && label.equalsIgnoreCase("doccontents")) {
				if (getDelegateType().equals(View.class)) {
					DEdgeList edges = new DEdgeEntryList(this, (org.openntf.domino.graph2.impl.DElementStore) getStore(), true);
					edges.setLabel(label);
					result = edges.unmodifiable();
				} else if (this instanceof DCategoryVertex || String.valueOf(getId()).startsWith("VC")) {
					DEdgeList edges = new DEdgeEntryList(this, (org.openntf.domino.graph2.impl.DElementStore) getStore(), true);
					edges.setLabel(label);
					result = edges.unmodifiable();
				} else {
					//					System.out.println("TEMP DEBUG unable to get contents edgelist from a " + this.getClass().getName() + " with id "
					//							+ String.valueOf(this.getId()));
				}
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

	protected Map<String, Object> frameImplCache_;

	public Map<String, Object> getFrameImplCache() {
		if (frameImplCache_ == null) {
			frameImplCache_ = new FastMap<String, Object>();
		}
		return frameImplCache_;
	}

	@Override
	public Object getFrameImplObject(final String key) {
		return getFrameImplCache().get(key);
	}

	@Override
	public void setFrameImplObject(final String key, final Object value) {
		getFrameImplCache().put(key, value);
	}

	@Override
	public org.openntf.domino.graph2.DElementStore getElementStore() {
		return getParent().findElementStore(this);
	}
}
