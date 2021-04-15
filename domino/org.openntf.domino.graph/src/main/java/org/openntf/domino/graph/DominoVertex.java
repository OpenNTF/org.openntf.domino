/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
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
package org.openntf.domino.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.text.TextBuilder;
import javolution.util.FastMap;
import javolution.util.FastSet;

import org.openntf.domino.Document;
import org.openntf.domino.graph.DominoGraph.DominoGraphException;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.util.DefaultVertexQuery;
import com.tinkerpop.blueprints.util.MultiIterable;
import com.tinkerpop.blueprints.util.VerticesFromEdgesIterable;

@Deprecated
@SuppressWarnings("nls")
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

	private FastMap<String, Boolean> inDirtyMap_;
	private FastMap<String, Boolean> outDirtyMap_;
	private FastMap<String, FastSet<String>> inEdgesMap_;
	private FastMap<String, FastSet<String>> outEdgesMap_;
	private transient FastMap<String, FastSet<Edge>> inEdgeCache_;
	private transient FastMap<String, FastSet<Edge>> outEdgeCache_;

	public DominoVertex(final DominoGraph parent, final org.openntf.domino.Document doc) {
		super(parent, doc);
	}

	Map<String, Boolean> getInDirtyMap() {
		if (inDirtyMap_ == null) {
			inDirtyMap_ = new FastMap<String, Boolean>().atomic();
		}
		return inDirtyMap_;
	}

	Map<String, Boolean> getOutDirtyMap() {
		if (outDirtyMap_ == null) {
			outDirtyMap_ = new FastMap<String, Boolean>().atomic();
		}
		return outDirtyMap_;
	}

	Map<String, FastSet<String>> getInEdgesMap() {
		if (inEdgesMap_ == null) {
			inEdgesMap_ = new FastMap<String, FastSet<String>>().atomic();
		}
		return inEdgesMap_;
	}

	Map<String, FastSet<String>> getOutEdgesMap() {
		if (outEdgesMap_ == null) {
			outEdgesMap_ = new FastMap<String, FastSet<String>>().atomic();
		}
		return outEdgesMap_;
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
		FastSet<String> edgeIds = getInEdgesMap().get(label);
		if (edgeIds == null) {
			Object o = getProperty(DominoVertex.IN_PREFIX + label, java.util.Collection.class);
			if (o != null) {
				if (o instanceof FastSet) {
					edgeIds = ((FastSet<String>) o).atomic();
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
			Map<String, FastSet<String>> map = getInEdgesMap();
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

	@SuppressWarnings("unchecked")
	FastSet<String> getOutEdgesSet(final String label) {
		FastSet<String> edgeIds = getOutEdgesMap().get(label);
		if (edgeIds == null) {
			Object o = getProperty(DominoVertex.OUT_PREFIX + label, java.util.Collection.class);
			if (o != null) {
				if (o instanceof FastSet) {
					edgeIds = ((FastSet<String>) o).atomic();
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
			Map<String, FastSet<String>> map = getOutEdgesMap();
			//			synchronized (map) {
			map.put(label, edgeIds);
			//			}
		}
		return edgeIds;
	}

	@Override
	public Edge addEdge(final String label, final Vertex vertex) {
		return parent_.addEdge(null, this, vertex, label);
	}

	@Override
	public void addInEdge(final Edge edge) {
		String label = edge.getLabel();

		Set<String> ins = getInEdgesSet(label);
		if (!ins.contains(edge.getId())) {
			getParent().startTransaction(this);
			Map<String, Boolean> map = getInDirtyMap();
			map.put(label, true);
			Set<Edge> inLabelObjs = getInEdgeCache(label);
			inLabelObjs.add(edge);
			ins.add((String) edge.getId());
		}
	}

	@Override
	public void addOutEdge(final Edge edge) {
		String label = edge.getLabel();

		Set<String> outs = getOutEdgesSet(label);
		if (!outs.contains(edge.getId())) {
			getParent().startTransaction(this);
			Map<String, Boolean> map = getOutDirtyMap();
			map.put(label, true);
			Set<Edge> outLabelObjs = getOutEdgeCache(label);
			outLabelObjs.add(edge);
			outs.add((String) edge.getId());
		}
	}

	public java.util.Set<String> getBothEdges() {
		FastSet<String> result = new FastSet<String>();
		result.addAll(getInEdges());
		result.addAll(getOutEdges());
		return result.unmodifiable();
	}

	@Override
	public Set<Edge> getEdges(final String... labels) {
		FastSet<Edge> result = new FastSet<Edge>();
		result.addAll(getInEdgeObjects(labels));
		result.addAll(getOutEdgeObjects(labels));
		return result.unmodifiable();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
			FastSet result = new FastSet<Edge>();
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

	private Map<String, FastSet<Edge>> getInEdgeCache() {
		if (inEdgeCache_ == null) {
			inEdgeCache_ = new FastMap<String, FastSet<Edge>>().atomic();
		}
		return inEdgeCache_;
	}

	private FastSet<Edge> getInEdgeCache(final String label) {
		Map<String, FastSet<Edge>> inCache = getInEdgeCache();
		FastSet<Edge> result = null;
		//		synchronized (inCache) {
		result = inCache.get(label);
		if (result == null) {
			result = new FastSet<Edge>().atomic();
			inCache.put(label, result);
		}
		//		}
		return result;
	}

	private FastSet<Edge> getOutEdgeCache(final String label) {
		Map<String, FastSet<Edge>> outCache = getOutEdgeCache();
		FastSet<Edge> result = null;
		//		synchronized (outCache) {
		result = outCache.get(label);
		if (result == null) {
			result = new FastSet<Edge>().atomic();
			outCache.put(label, result);
		}
		//		}
		return result;
	}

	private FastMap<String, FastSet<Edge>> getOutEdgeCache() {
		if (outEdgeCache_ == null) {
			outEdgeCache_ = new FastMap<String, FastSet<Edge>>().atomic();
		}
		return outEdgeCache_;
	}

	protected FastSet<Edge> getInEdgeObjects(final String... labels) {
		Map<String, FastSet<Edge>> inCache = getInEdgeCache();
		FastSet<Edge> result = null;
		if (labels == null || labels.length == 0) {
			result = new FastSet<Edge>().atomic();
			Set<String> labelSet = this.getInEdgeLabels();
			//			System.out.println("INFO: Getting all IN edges for a vertex across " + labelSet.size() + " labels.");
			for (String label : labelSet) {
				result.addAll(getInEdgeObjects(label));
			}
			//			System.out.println("INFO: Found " + result.size() + " IN edges.");
		} else if (labels.length == 1) {
			String label = labels[0];
			//			System.out.println("Getting in edges from " + getClass().getName() + " with label: " + label + " ...");
			//			synchronized (inCache) {
			result = inCache.get(label);
			//			}
			if (result == null) {
				// result = Collections.synchronizedSet(new LinkedHashSet<Edge>());
				Set<String> edgeIds = getInEdgesSet(label);
				FastSet<Edge> edges = getParent().getEdgesFromIds(edgeIds);
				if (edges != null) {
					result = edges.atomic();
				}

				// Set<Edge> allEdges = Collections.unmodifiableSet(getInEdgeObjects());
				// for (Edge edge : allEdges) {
				// if (label.equals(edge.getLabel())) {
				// result.add(edge);
				// }
				// }

				//				synchronized (inCache) {
				inCache.put(label, result);
				//				}
			}
		} else {
			result = new FastSet<Edge>();
			for (String label : labels) {
				result.addAll(getInEdgeObjects(label));
			}
		}
		//		System.out.println("Returning " + (result == null ? "null" : result.size()) + " edges");
		return result == null ? null : result.unmodifiable();
	}

	protected FastSet<Edge> getOutEdgeObjects(final String... labels) {
		FastMap<String, FastSet<Edge>> outCache = getOutEdgeCache();
		FastSet<Edge> result = null;

		if (labels == null || labels.length == 0) {
			result = new FastSet<Edge>();
			FastSet<String> labelSet = this.getOutEdgeLabels();
			//			System.out.println("INFO: Getting all OUT edges for a vertex across " + labelSet.size() + " labels.");
			for (String label : labelSet) {
				FastSet<Edge> curEdges = getOutEdgeObjects(label);
				//				System.out.println("INFO: Found " + curEdges.size() + " OUT edges for label " + label);
				result.addAll(curEdges);
			}
			//			System.out.println("INFO: Found " + result.size() + " OUT edges.");
		} else if (labels.length == 1) {
			String label = labels[0];
			if (label == null) {
				return getOutEdgeObjects().unmodifiable();
			}
			//			synchronized (outCache) {
			result = outCache.get(label);
			//			}
			if (result == null) {
				FastSet<String> edgeIds = getOutEdgesSet(label);
				FastSet<Edge> edges = getParent().getEdgesFromIds(edgeIds);
				if (edges != null) {
					result = edges.atomic();
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

				//				synchronized (outCache) {
				outCache.put(label, result);
				//				}
			}
		} else {
			result = new FastSet<Edge>();
			for (String label : labels) {
				result.addAll(getOutEdgeObjects(label));
			}
		}
		return result == null ? null : result.unmodifiable();
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

	@Override
	public FastSet<String> getInEdgeLabels() {
		FastSet<String> result = new FastSet<String>();
		Set<String> rawKeys = getRawDocument().keySet();
		for (String key : rawKeys) {
			if (key.startsWith(IN_PREFIX)) {
				result.add(key.substring(IN_PREFIX.length()));
			}
		}
		return result;
	}

	private FastSet<String> getInEdges() {
		FastSet<String> result = new FastSet<String>();
		for (String label : getInEdgeLabels()) {
			result.addAll(getInEdgesSet(label));
		}
		return result;
	}

	@Override
	public FastSet<String> getOutEdgeLabels() {
		FastSet<String> result = new FastSet<String>();
		Set<String> rawKeys = getRawDocument().keySet();
		for (String key : rawKeys) {
			if (key.startsWith(OUT_PREFIX)) {
				result.add(key.substring(OUT_PREFIX.length()));
			}
		}
		return result;
	}

	private FastSet<String> getOutEdges() {
		FastSet<String> result = new FastSet<String>();
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
			//			synchronized (ins) {
			inChanged = ins.remove(edge.getId());
			//			}
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
			//			synchronized (inObjsLabel) {
			inObjsLabel.remove(edge);
			//			}
			Map<String, Boolean> inDirtyMap = getInDirtyMap();
			//			synchronized (inDirtyMap) {
			inDirtyMap.put(label, true);
			//			}
		}

		boolean outChanged = false;
		Set<String> outs = getOutEdgesSet(label);
		if (outs != null) {
			//			System.out.println("Removing an out edge from " + label + " with id " + edge.getId() + " from a vertex of type "
			//					+ getProperty("Form"));
			//			synchronized (outs) {
			outChanged = outs.remove(edge.getId());
			//			}
		} else {
			//			System.out.println("out edges were null from a vertex of type " + getProperty("Form") + ": " + getId());
		}
		if (outChanged) {
			//			System.out.println("Out were changed so recording cache invalidation...");
			Set<Edge> outObjsLabel = getOutEdgeCache(label);
			//			synchronized (outObjsLabel) {
			outObjsLabel.remove(edge);
			//			}
			Map<String, Boolean> outDirtyMap = getOutDirtyMap();
			//			synchronized (outDirtyMap) {
			outDirtyMap.put(label, true);
			//			}
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
		Map<String, FastSet<String>> inMap = getInEdgesMap();
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
					//					synchronized (inDirtyMap) {
					inDirtyMap.put(key, Boolean.FALSE);
					//					}
				}
			}
		}

		Map<String, FastSet<String>> outMap = getOutEdgesMap();
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
					//					synchronized (outDirtyMap) {
					outDirtyMap.put(key, Boolean.FALSE);
					//					}
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

	@Override
	public String validateEdges() {
		TextBuilder sb = new TextBuilder();
		Set<String> inIds = getInEdges();
		for (String id : inIds.toArray(new String[inIds.size()])) {
			Document chk = getParent().getRawDatabase().getDocumentByUNID(id);
			if (chk == null) {
				inIds.remove(id);
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

	@Override
	public Set<IEdgeHelper> getEdgeHelpers() {
		Set<IEdgeHelper> result = new FastSet<IEdgeHelper>();
		for (String in : getInEdgeLabels()) {
			IEdgeHelper helper = getParent().getHelper(in);
			if (helper != null) {
				result.add(helper);
			}
		}
		for (String out : getOutEdgeLabels()) {
			IEdgeHelper helper = getParent().getHelper(out);
			if (helper != null) {
				result.add(helper);
			}
		}
		return result;
	}
}
