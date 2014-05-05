package org.openntf.domino.graph;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.exceptions.DominoGraphException;
import org.openntf.domino.transactions.DatabaseTransaction;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Features;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.DefaultGraphQuery;

//@SuppressWarnings("deprecation")
public class DominoGraph implements IDominoGraph {
	private static final Logger log_ = Logger.getLogger(DominoGraph.class.getName());

	public static final Set<String> EMPTY_IDS = Collections.emptySet();

	public static final String EDGE_VIEW_NAME = "(_OPEN_Edges)";
	public static final String VERTEX_VIEW_NAME = "(_OPEN_Vertices)";
	private static final Features FEATURES = new Features();
	public static final boolean COMPRESS_IDS = false;

	public static SortedSet<? extends Element> sortElements(final Iterable<? extends Element> elements, final String[] sortproperties) {
		return DominoGraphUtils.sortElements(elements, sortproperties);
	}

	public static SortedSet<? extends Element> sortElements(final Iterable<? extends Element> elements,
			final IDominoProperties[] sortproperties) {
		return DominoGraphUtils.sortElements(elements, sortproperties);
	}

	public static SortedSet<? extends Edge> sortEdges(final Iterable<? extends Edge> elements, final String[] sortproperties) {
		return DominoGraphUtils.sortEdges(elements, sortproperties);
	}

	public static SortedSet<? extends Vertex> sortVertexes(final Iterable<? extends Vertex> elements, final String[] sortproperties) {
		return DominoGraphUtils.sortVertexes(elements, sortproperties);
	}

	public static SortedSet<? extends Edge> sortEdges(final Iterable<? extends Edge> elements, final IDominoProperties[] sortproperties) {
		return DominoGraphUtils.sortEdges(elements, sortproperties);
	}

	public static SortedSet<? extends Vertex> sortVertexes(final Iterable<? extends Vertex> elements,
			final IDominoProperties[] sortproperties) {
		return DominoGraphUtils.sortVertexes(elements, sortproperties);
	}

	static {
		DominoGraph.FEATURES.supportsDuplicateEdges = true;
		DominoGraph.FEATURES.supportsSelfLoops = true;
		DominoGraph.FEATURES.supportsSerializableObjectProperty = true;
		DominoGraph.FEATURES.supportsBooleanProperty = true;
		DominoGraph.FEATURES.supportsDoubleProperty = true;
		DominoGraph.FEATURES.supportsFloatProperty = true;
		DominoGraph.FEATURES.supportsIntegerProperty = true;
		DominoGraph.FEATURES.supportsPrimitiveArrayProperty = true;
		DominoGraph.FEATURES.supportsUniformListProperty = true;
		DominoGraph.FEATURES.supportsMixedListProperty = true;
		DominoGraph.FEATURES.supportsLongProperty = true;
		DominoGraph.FEATURES.supportsMapProperty = true;
		DominoGraph.FEATURES.supportsStringProperty = true;

		DominoGraph.FEATURES.ignoresSuppliedIds = false;

		DominoGraph.FEATURES.isWrapper = false;

		DominoGraph.FEATURES.supportsIndices = false;
		DominoGraph.FEATURES.supportsKeyIndices = false;
		DominoGraph.FEATURES.supportsVertexKeyIndex = false;
		DominoGraph.FEATURES.supportsEdgeKeyIndex = false;
		DominoGraph.FEATURES.supportsVertexIndex = false;
		DominoGraph.FEATURES.supportsEdgeIndex = false;
		DominoGraph.FEATURES.supportsTransactions = true;
		DominoGraph.FEATURES.supportsVertexIteration = false;
		DominoGraph.FEATURES.supportsEdgeIteration = false;
		DominoGraph.FEATURES.supportsEdgeRetrieval = true;
		DominoGraph.FEATURES.supportsVertexProperties = true;
		DominoGraph.FEATURES.supportsEdgeProperties = true;
		DominoGraph.FEATURES.supportsThreadedTransactions = true;
		DominoGraph.FEATURES.isPersistent = true;

	}

	private java.util.Map<Object, IDominoElement> cache_;

	private String filepath_;
	private String server_;
	protected transient Map<String, IEdgeHelper> edgeHelpers_ = new HashMap<String, IEdgeHelper>();

	public DominoGraph(final org.openntf.domino.Database database) {
		setRawDatabase(database);
	}

	public IEdgeHelper getHelper(final String key) {
		IEdgeHelper helper = edgeHelpers_.get(key);
		return helper;
	}

	public IEdgeHelper getHelper(final IDominoEdgeType edgeType) {
		return getHelper(edgeType.getLabel());
	}

	public Set<IEdgeHelper> findHelpers(final Vertex in, final Vertex out) {
		Set<IEdgeHelper> result = new HashSet<IEdgeHelper>();
		if (in == null || out == null) {
			return result;
		}
		Class<?> inCls = in.getClass();
		Class<?> outCls = out.getClass();
		for (IEdgeHelper helper : edgeHelpers_.values()) {
			boolean inChk = helper.getInType().isAssignableFrom(inCls);
			boolean outChk = helper.getOutType().isAssignableFrom(outCls);
			if (inChk && outChk) {
				result.add(helper);
			}
		}
		return Collections.unmodifiableSet(result);
	}

	public void addHelper(final String key, final Class<? extends Vertex> inType, final Class<? extends Vertex> outType) {
		addHelper(key, inType, outType, true, key);
	}

	public void addHelper(final String key, final Class<? extends Vertex> inType, final Class<? extends Vertex> outType,
			final boolean unique) {
		addHelper(key, inType, outType, unique, key);
	}

	public void addHelper(final String key, final Class<? extends Vertex> inType, final Class<? extends Vertex> outType,
			final boolean unique, final String label) {
		if (getHelper(key) == null) {
			edgeHelpers_.put(key, new AbstractEdgeHelper(this, label, inType, outType, unique));
		}
	}

	public void addHelper(final String key, final IEdgeHelper helper) {
		if (getHelper(key) == null) {
			edgeHelpers_.put(key, helper);
		}
	}

	public void removeHelper(final String key) {
		edgeHelpers_.remove(key);
	}

	public void setRawDatabase(final org.openntf.domino.Database database) {
		if (database != null) {
			server_ = database.getServer();
			filepath_ = database.getFilePath();
		}
	}

	private java.util.Map<Object, IDominoElement> getCache() {
		if (cache_ == null) {
			cache_ = new ConcurrentHashMap<Object, IDominoElement>();
		}
		return cache_;
	}

	public void cache(final IDominoElement elem) {
		putCache(elem);
	}

	private void putCache(final Element elem) {
		if (elem != null) {
			if (elem instanceof IDominoElement) {
				Map<Object, IDominoElement> cache = getCache();
				synchronized (cache) {
					cache.put(elem.getId(), (IDominoElement) elem);
				}
			} else {
				throw new DominoGraphException("Passed elem cannot be placed in cache because it's a " + elem.getClass().getName());
			}
		}
	}

	private IDominoElement getCache(final Object id) {
		Map<Object, IDominoElement> cache = getCache();
		IDominoElement result = null;
		synchronized (cache) {
			result = cache.get(id);
		}
		return result;
	}

	private void removeCache(final Element elem) {
		Map<Object, IDominoElement> cache = getCache();
		synchronized (cache) {
			cache.remove(elem);
		}
	}

	private Set<IDominoElement> getCacheValues() {
		Map<Object, IDominoElement> cache = getCache();
		Set<IDominoElement> result = new LinkedHashSet<IDominoElement>();
		synchronized (cache) {
			result.addAll(cache.values());
		}
		return Collections.unmodifiableSet(result);
	}

	private void clearCache() {
		Map<Object, IDominoElement> cache = getCache();
		synchronized (cache) {
			cache.clear();
		}
		clearDocumentCache();
	}

	@Override
	public IDominoEdge addEdge(Object id, final Vertex outVertex, final Vertex inVertex, final String label) {
		startTransaction(null);
		if (id == null)
			id = DominoUtils.toUnid(outVertex.getId() + label + inVertex.getId());
		Document d = getDocument(id, true);
		d.replaceItemValue(DominoElement.TYPE_FIELD, DominoEdge.GRAPH_TYPE_VALUE);
		DominoEdge ed = new DominoEdge(this, d);
		putCache(ed);
		// putCache(outVertex);
		// putCache(inVertex);
		ed.setLabel(label);
		ed.setOutDoc((IDominoVertex) outVertex);
		ed.setInDoc((IDominoVertex) inVertex);
		return ed;
	}

	public IDominoEdge getEdge(final Vertex outVertex, final Vertex inVertex, final String label) {
		String id = DominoUtils.toUnid(outVertex.getId() + label + inVertex.getId());
		IDominoEdge result = getEdge(id);
		return result;
	}

	public IDominoEdge getOrAddEdge(Object id, final Vertex outVertex, final Vertex inVertex, final String label) {
		IDominoEdge result = null;
		if (id == null) {
			id = DominoUtils.toUnid(outVertex.getId() + label + inVertex.getId());
			result = getEdge(id);
			if (result != null) {
				((DominoEdge) result).setLabel(label);
				((DominoEdge) result).setOutDoc((IDominoVertex) outVertex);
				((DominoEdge) result).setInDoc((IDominoVertex) inVertex);
			}
		}
		// if (result == null) {
		// for (Edge e : outVertex.getEdges(Direction.OUT, label)) {
		// Vertex v = e.getVertex(Direction.IN);
		// if (v.getId().equals(inVertex.getId())) {
		// result = e;
		// ((DominoEdge) result).setLabel(label);
		// ((DominoEdge) result).setOutDoc(outVertex);
		// ((DominoEdge) result).setInDoc(inVertex);
		// break;
		// }
		// }
		// }
		if (result == null) {
			result = addEdge(id, outVertex, inVertex, label);
		}
		return result;
	}

	@Override
	public IDominoVertex addVertex(final Object id) {
		startTransaction(null);
		Document d = null;
		if (id == null) {
			d = getDocument(null, true);
		} else {
			String vid = DominoUtils.toUnid((Serializable) id);
			d = getDocument(vid, true);
		}

		d.replaceItemValue(DominoElement.TYPE_FIELD, DominoVertex.GRAPH_TYPE_VALUE);
		DominoVertex result = new DominoVertex(this, d);
		putCache(result);
		return result;
	}

	private org.openntf.domino.Database getDatabase() {
		return getRawSession().getDatabase(server_, filepath_);
	}

	public org.openntf.domino.Session getRawSession() {
		return Factory.getSession();
	}

	public org.openntf.domino.Database getRawDatabase() {
		return getDatabase();
	}

	Document getDocument(final Object id, final boolean createOnFail) {
		Document result = null;
		String unid = "";
		Map<String, Document> map = documentCache.get();
		if (id == null && createOnFail) {
			result = getRawDatabase().createDocument();
			synchronized (map) {
				map.put(result.getUniversalID(), result);
			}
		} else if (id instanceof String) {
			String sid = (String) id;
			if (DominoUtils.isUnid(sid)) {
				unid = sid;
			} else if (sid.length() > 32) {
				unid = DominoUtils.toUnid(sid);
			} else {

				unid = DominoUtils.toUnid(sid);
			}
		} else if (id instanceof Serializable) {
			unid = DominoUtils.toUnid((Serializable) id);
		}
		if (id != null && !DominoUtils.isUnid(unid)) {
			log_.log(Level.SEVERE, "ALERT! INVALID UNID FROM id type " + (id == null ? "null" : id.getClass().getName()) + ": " + id);
		}
		if (result == null) {

			result = map.get(unid);
			if (result == null) {
				result = getRawDatabase().getDocumentByKey(unid, createOnFail);
				if (result != null) {
					String localUnid = result.getUniversalID();
					if (!unid.equalsIgnoreCase(localUnid)) {
						log_.log(Level.SEVERE, "UNIDs do not match! Expected: " + unid + ", Result: " + localUnid);
					}
					synchronized (map) {
						map.put(unid, result);
					}
				}
			}
		}
		// if (result == null && createOnFail) {
		// log_.log(Level.SEVERE, "Returning a null document for id " + String.valueOf(id)
		// + " even though createOnFail was true. This should be guaranteed to return a real document!");
		// }
		if (result == null && createOnFail) {
			log_.log(Level.SEVERE,
					"We are about to return a null result even though createOnFail was true. We should ALWAYS return a Document in that case. For key: "
							+ String.valueOf(id) + " in database " + String.valueOf(filepath_));
			new RuntimeException().printStackTrace();
		}
		return result;
	}

	@Override
	public IDominoEdge getEdge(final Object id) {
		IDominoEdge result = null;
		if (id == null) {
			Document d = getDocument(id, false);
			result = new DominoEdge(this, d);
			putCache(result);
		} else {
			result = (IDominoEdge) getCache(id);
			if (result == null) {
				// System.out.println("Cache miss on edge with id " + id);
				Document d = getDocument(id, false);
				if (d != null) {
					result = new DominoEdge(this, d);
					putCache(result);
				}
			}

		}
		return result;
	}

	@Override
	public Iterable<Edge> getEdges() {
		Set<Edge> result = new LinkedHashSet<Edge>();
		ViewEntryCollection vec = getEdgeView().getAllEntries();
		for (ViewEntry entry : vec) {
			result.add(getEdge(entry.getUniversalID()));
		}

		return Collections.unmodifiableSet(result);
	}

	@Override
	public Iterable<Edge> getEdges(final String key, final Object value) {
		// TODO
		throw new UnsupportedOperationException();
	}

	public Set<Edge> getEdgesFromIds(final Set<String> set) {
		Set<Edge> result = new LinkedHashSet<Edge>();
		for (String id : set) {
			Edge edge = getEdge(id);
			if (edge != null) {
				result.add(edge);
			}
		}
		return result;
	}

	public Set<Edge> getEdgesFromIds(final Set<String> set, final String... labels) {
		Set<Edge> result = new LinkedHashSet<Edge>();
		for (String id : set) {
			Edge edge = getEdge(id);
			if (edge != null) {
				for (String label : labels) {
					if (label.equals(edge.getLabel())) {
						result.add(edge);
						break;
					}
				}
			}
		}
		return result;
	}

	private View getEdgeView() {
		View result = getRawDatabase().getView(DominoGraph.EDGE_VIEW_NAME);
		if (result == null) {
			result = getRawDatabase().createView(DominoGraph.EDGE_VIEW_NAME,
					"SELECT " + DominoElement.TYPE_FIELD + "=\"" + DominoEdge.GRAPH_TYPE_VALUE + "\"", null, false);
			org.openntf.domino.ViewColumn column1 = result.createColumn();
			column1.setTitle("Created");
			column1.setFormula("@Created");
			column1.setSortDescending(true);
		}
		return result;
	}

	@Override
	public Features getFeatures() {
		return DominoGraph.FEATURES;
	}

	@Override
	public IDominoVertex getVertex(final Object id) {
		String vid = DominoUtils.toUnid((Serializable) id);
		IDominoVertex result = (IDominoVertex) getCache(vid);
		if (result == null) {
			Document d = getDocument(vid, false);
			if (d == null)
				return null;
			if (d.isDeleted()) {
				// System.out.println("Found vertex for id " + String.valueOf(id) + " but it's been deleted.");
				return null;
			}
			result = new DominoVertex(this, d);
			putCache(result);
		}
		return result;
	}

	private View getVertexView() {
		View result = getRawDatabase().getView(DominoGraph.VERTEX_VIEW_NAME);
		if (result == null) {
			result = getRawDatabase().createView(DominoGraph.VERTEX_VIEW_NAME,
					"SELECT " + DominoElement.TYPE_FIELD + "=\"" + DominoVertex.GRAPH_TYPE_VALUE + "\"", null, false);
			org.openntf.domino.ViewColumn column1 = result.createColumn();
			column1.setTitle("Created");
			column1.setFormula("@Created");
			column1.setSortDescending(true);
		}
		return result;
	}

	@Override
	public Iterable<Vertex> getVertices() {
		Set<Vertex> result = new LinkedHashSet<Vertex>();
		ViewEntryCollection vec = getVertexView().getAllEntries();
		for (ViewEntry entry : vec) {
			result.add(getVertex(entry.getUniversalID()));
		}
		return Collections.unmodifiableSet(result);
	}

	@Override
	public Iterable<Vertex> getVertices(final String key, final Object value) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public GraphQuery query() {
		return new DefaultGraphQuery(this);
	}

	@Override
	public void removeEdge(final Edge edge) {
		startTransaction(edge);
		Vertex in = edge.getVertex(Direction.IN);
		((DominoVertex) in).removeEdge(edge);
		Vertex out = edge.getVertex(Direction.OUT);
		((DominoVertex) out).removeEdge(edge);
		removeCache(edge);
		((DominoEdge) edge)._remove();
	}

	@Override
	public void removeVertex(final Vertex vertex) {
		startTransaction(vertex);
		DominoVertex dv = (DominoVertex) vertex;
		for (Edge edge : dv.getEdges(Direction.BOTH)) {
			removeEdge(edge);
		}
		removeCache(vertex);
		dv._remove();
	}

	@Override
	public void shutdown() {
		commit();
	}

	@Override
	public org.openntf.domino.Database getRawGraph() {
		return getRawDatabase();
	}

	// private boolean inTransaction_ = false;
	private static ThreadLocal<DatabaseTransaction> txnHolder_ = new ThreadLocal<DatabaseTransaction>() {

	};

	public static void clearDocumentCache() {
		documentCache.set(null);
	}

	private static ThreadLocal<Map<String, Document>> documentCache = new ThreadLocal<Map<String, Document>>() {
		@Override
		protected Map<String, Document> initialValue() {
			return new ConcurrentHashMap<String, Document>();
		}

		@Override
		public Map<String, Document> get() {
			Map<String, Document> map = super.get();
			if (map == null) {
				map = new ConcurrentHashMap<String, Document>();
				super.set(map);

			}
			return map;
		}

	};

	private DatabaseTransaction getTxn() {
		return getRawDatabase().getTransaction();
		//		return txnHolder_.get();
	}

	//	public void setTxn(final DatabaseTransaction txn) {
	//		txnHolder_.set(txn);
	//	}

	// private DatabaseTransaction txn_;

	public void startTransaction(final Element elem) {
		putCache(elem);
		if (getTxn() == null) {
			getRawDatabase().startTransaction();
			//			setTxn(getRawDatabase().startTransaction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tinkerpop.blueprints.TransactionalGraph#stopTransaction(com.tinkerpop.blueprints.TransactionalGraph.Conclusion)
	 */
	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void stopTransaction(final Conclusion conclusion) {
		// TODO Auto-generated method stub

	}

	public void commit(final boolean clearCache) {
		DatabaseTransaction txn = getTxn();
		if (txn != null) {
			if (getCache().size() > 0) {
				// System.out.println("Reapplying cache to " + getCache().size() + " elements...");
				//				int vCount = 0;
				Set<IDominoElement> elems = getCacheValues();
				for (IDominoElement elem : elems) {
					elem.reapplyChanges();
				}
			} else {
				// System.out.println("Element cache is empty (so what are we committing?)");
			}
			txn.commit();
			//			setTxn(null);
		}
		if (clearCache)
			clearCache();
		// System.out.println("Transaction complete");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tinkerpop.blueprints.TransactionalGraph#commit()
	 */
	@Override
	public void commit() {
		commit(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tinkerpop.blueprints.TransactionalGraph#rollback()
	 */
	@Override
	public void rollback() {
		DatabaseTransaction txn = getTxn();
		if (txn != null) {
			txn.rollback();
			//			setTxn(null);
		}
		clearCache();
		// System.out.println("Transaction rollbacked");
	}

}
