package org.openntf.domino.graph;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Document;
import org.openntf.domino.Session.RunContext;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.transactions.DatabaseTransaction;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Features;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.MetaGraph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.DefaultGraphQuery;

public class DominoGraph implements Graph, MetaGraph, TransactionalGraph {
	private static final Logger log_ = Logger.getLogger(DominoGraph.class.getName());

	public static final Set<String> EMPTY_IDS = Collections.emptySet();

	public static final String EDGE_VIEW_NAME = "(_OPEN_Edges)";
	public static final String VERTEX_VIEW_NAME = "(_OPEN_Vertices)";
	private static final Features FEATURES = new Features();
	public static final boolean COMPRESS_IDS = false;

	public static SortedSet<? extends Element> sortElements(final Iterable<? extends Element> elements, final String[] sortproperties) {
		Comparator<Element> comp = new ElementComparator(sortproperties);
		SortedSet<Element> result = new TreeSet<Element>(comp);
		for (Object e : elements) {
			if (e instanceof Element) {
				result.add((Element) e);
			}
		}
		return Collections.unmodifiableSortedSet(result);
	}

	@SuppressWarnings("unchecked")
	public static SortedSet<? extends Edge> sortEdges(final Iterable<? extends Edge> elements, final String[] sortproperties) {
		return (SortedSet<Edge>) sortElements(elements, sortproperties);
	}

	@SuppressWarnings("unchecked")
	public static SortedSet<? extends Vertex> sortVertexes(final Iterable<? extends Vertex> elements, final String[] sortproperties) {
		return (SortedSet<Vertex>) sortElements(elements, sortproperties);
	}

	static {
		DominoGraph.FEATURES.supportsDuplicateEdges = true;
		DominoGraph.FEATURES.supportsSelfLoops = true;
		DominoGraph.FEATURES.supportsSerializableObjectProperty = false;
		DominoGraph.FEATURES.supportsBooleanProperty = true;
		DominoGraph.FEATURES.supportsDoubleProperty = true;
		DominoGraph.FEATURES.supportsFloatProperty = false;
		DominoGraph.FEATURES.supportsIntegerProperty = true;
		DominoGraph.FEATURES.supportsPrimitiveArrayProperty = false;
		DominoGraph.FEATURES.supportsUniformListProperty = true;
		DominoGraph.FEATURES.supportsMixedListProperty = false;
		DominoGraph.FEATURES.supportsLongProperty = false;
		DominoGraph.FEATURES.supportsMapProperty = false;
		DominoGraph.FEATURES.supportsStringProperty = true;

		DominoGraph.FEATURES.ignoresSuppliedIds = false;

		DominoGraph.FEATURES.isWrapper = false;

		DominoGraph.FEATURES.supportsIndices = false;
		DominoGraph.FEATURES.supportsKeyIndices = false;
		DominoGraph.FEATURES.supportsVertexKeyIndex = false;
		DominoGraph.FEATURES.supportsEdgeKeyIndex = false;
		DominoGraph.FEATURES.supportsVertexIndex = false;
		DominoGraph.FEATURES.supportsEdgeIndex = false;
		DominoGraph.FEATURES.supportsTransactions = false;
		DominoGraph.FEATURES.supportsVertexIteration = false;
		DominoGraph.FEATURES.supportsEdgeIteration = false;
		DominoGraph.FEATURES.supportsEdgeRetrieval = true;
		DominoGraph.FEATURES.supportsVertexProperties = true;
		DominoGraph.FEATURES.supportsEdgeProperties = true;
		DominoGraph.FEATURES.supportsThreadedTransactions = false;
		DominoGraph.FEATURES.isPersistent = true;

	}

	private java.util.Map<Object, Element> cache_;

	private transient org.openntf.domino.Database database_;
	private String filepath_;
	private String server_;
	protected transient org.openntf.domino.Session session_;
	protected transient Map<String, IEdgeHelper> edgeHelpers_ = new HashMap<String, IEdgeHelper>();

	public DominoGraph(final org.openntf.domino.Database database) {
		setRawDatabase(database);
		RunContext rc = Factory.getRunContext();
		// System.out.println("Context: " + rc.toString());
	}

	public IEdgeHelper getHelper(final String key) {
		IEdgeHelper helper = edgeHelpers_.get(key);
		return helper;
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

	public void setRawDatabase(final org.openntf.domino.Database database) {
		if (database != null) {
			database_ = database;
			session_ = database.getParent();
			server_ = database.getServer();
			filepath_ = database.getFilePath();
		}
	}

	private java.util.Map<Object, Element> getCache() {
		if (cache_ == null) {
			cache_ = Collections.synchronizedMap(new HashMap<Object, Element>());
		}
		return cache_;
	}

	public void cache(final Element elem) {
		putCache(elem);
	}

	private void putCache(final Element elem) {
		if (elem != null) {
			Map<Object, Element> cache = getCache();
			synchronized (cache) {
				cache.put(elem.getId(), elem);
			}
		}
	}

	private Element getCache(final Object id) {
		Map<Object, Element> cache = getCache();
		Element result = null;
		synchronized (cache) {
			result = cache.get(id);
		}
		return result;
	}

	private void removeCache(final Element elem) {
		Map<Object, Element> cache = getCache();
		synchronized (cache) {
			cache.remove(elem);
		}
	}

	private Set<Element> getCacheValues() {
		Map<Object, Element> cache = getCache();
		Set<Element> result = new LinkedHashSet<Element>();
		synchronized (cache) {
			result.addAll(cache.values());
		}
		return Collections.unmodifiableSet(result);
	}

	private void clearCache() {
		Map<Object, Element> cache = getCache();
		synchronized (cache) {
			cache.clear();
		}
	}

	@Override
	public Edge addEdge(Object id, final Vertex outVertex, final Vertex inVertex, final String label) {
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

	public Edge getEdge(final Vertex outVertex, final Vertex inVertex, final String label) {
		String id = DominoUtils.toUnid(outVertex.getId() + label + inVertex.getId());
		Edge result = getEdge(id);
		return result;
	}

	public Edge getOrAddEdge(Object id, final Vertex outVertex, final Vertex inVertex, final String label) {
		Edge result = null;
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
	public Vertex addVertex(final Object id) {
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
		if (session_ == null) {
			session_ = Factory.getSession();
			System.out.println("DominoGraph re-established root session.");
			DominoUtils.setBubbleExceptions(Boolean.TRUE);
		} else {
			try {
				session_.isTrustedSession();
			} catch (Exception xPagesDidThis) {
				session_ = Factory.getSession();
				System.out.println("DominoGraph re-established root session.");
				DominoUtils.setBubbleExceptions(Boolean.TRUE);
			}
		}
		return session_;
	}

	public org.openntf.domino.Database getRawDatabase() {
		if (database_ == null) {
			database_ = getDatabase();
		}
		return database_;
	}

	private Document getDocument(final Object id, final boolean createOnFail) {
		Document result = null;
		String unid = "";
		if (id == null && createOnFail) {
			result = getRawDatabase().createDocument();
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
			log_.log(Level.WARNING, "ALERT! INVALID UNID FROM id type " + (id == null ? "null" : id.getClass().getName()) + ": " + id);
		}
		if (result == null) {
			result = getRawDatabase().getDocumentByKey(unid, createOnFail);
		}
		return result;
	}

	@Override
	public Edge getEdge(final Object id) {
		Edge result = null;
		if (id == null) {
			Document d = getDocument(id, false);
			result = new DominoEdge(this, d);
			putCache(result);
		} else {
			result = (Edge) getCache(id);
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

	public Iterable<Edge> getEdgesFromIds(final Set<String> set) {
		Set<Edge> result = new LinkedHashSet<Edge>();
		for (String id : set) {
			Edge edge = getEdge(id);
			if (edge != null) {
				result.add(edge);
			}
		}
		return result;
	}

	public Iterable<Edge> getEdgesFromIds(final Set<String> set, final String... labels) {
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
	public Vertex getVertex(final Object id) {
		String vid = DominoUtils.toUnid((Serializable) id);
		Vertex result = (Vertex) getCache(vid);
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
		edge.remove();
	}

	@Override
	public void removeVertex(final Vertex vertex) {
		startTransaction(vertex);
		DominoVertex dv = (DominoVertex) vertex;
		for (Edge edge : dv.getEdges(Direction.BOTH)) {
			removeEdge(edge);
		}
		removeCache(vertex);
		vertex.remove();
	}

	@Override
	public void shutdown() {
		commit();
	}

	@Override
	public Object getRawGraph() {
		return getRawDatabase();
	}

	private boolean inTransaction_ = false;
	private DatabaseTransaction txn_;

	public void startTransaction(final Element elem) {
		putCache(elem);
		if (!inTransaction_) {
			// System.out.println("Not yet in transaction. Starting...");
			txn_ = getRawDatabase().startTransaction();
			inTransaction_ = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tinkerpop.blueprints.TransactionalGraph#stopTransaction(com.tinkerpop.blueprints.TransactionalGraph.Conclusion)
	 */
	@Override
	@Deprecated
	public void stopTransaction(final Conclusion conclusion) {
		// TODO Auto-generated method stub

	}

	public void commit(final boolean clearCache) {
		if (inTransaction_) {
			// System.out.println("Committing transaction");

			if (txn_ == null) {
				// System.out.println("Transaction is null!?!?!");
			} else {
				if (getCache().size() > 0) {
					// System.out.println("Reapplying cache to " + getCache().size() + " elements...");
					int vCount = 0;
					Set<Element> elems = getCacheValues();
					for (Element elem : elems) {
						if (elem instanceof DominoElement) {
							((DominoElement) elem).reapplyChanges();
						}
						// if (elem instanceof DominoVertex) {
						// if (((DominoVertex) elem).writeEdges()) {
						// vCount++;
						// System.out.println("Updating edges to vertex: " + ((DominoVertex) elem).getRawDocument().getFormName()
						// + ": " + elem.getId());
						// // txn_.queueUpdate(((DominoVertex)elem).getRawDocument());
						// } else {
						// // System.out.println("No edge updates to vertex: " + ((DominoVertex) elem).getRawDocument().getFormName()
						// // + ": " + elem.getId());
						// }
						// }
					}
				} else {
					// System.out.println("Element cache is empty (so what are we committing?)");
				}
				// System.out.println("Committing transaction with " + txn_.getUpdateSize() + " updates...");
				txn_.commit();
				txn_ = null;
			}
			inTransaction_ = false;

		} else {
			// System.out.println("Not in transaction!");
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
		if (inTransaction_) {
			// System.out.println("Rollbacking transaction");

			if (txn_ == null) {
				// System.out.println("Transaction is null!?!?!");
			} else {
				txn_.rollback();
				txn_ = null;
			}
			inTransaction_ = false;

		} else {
			// System.out.println("Not in transaction!");
		}
		clearCache();
		// System.out.println("Transaction rollbacked");
	}

}
