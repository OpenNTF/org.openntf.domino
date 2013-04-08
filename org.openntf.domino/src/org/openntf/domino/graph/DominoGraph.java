package org.openntf.domino.graph;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Features;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.MetaGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.DefaultGraphQuery;

public class DominoGraph implements Graph, MetaGraph {
	private static final Logger log_ = Logger.getLogger(DominoGraph.class.getName());

	public static final String EDGE_VIEW_NAME = "(_ONTF_Edges)";
	public static final String VERTEX_VIEW_NAME = "(_ONTF_Vertices)";
	private static final Features FEATURES = new Features();
	public static final boolean COMPRESS_IDS = false;

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

	private java.util.Map<Object, DominoElement> cache_;
	private transient org.openntf.domino.Database database_;
	private String filepath_;
	private String server_;
	private transient org.openntf.domino.Session session_;

	public DominoGraph(org.openntf.domino.Database database) {
		setRawDatabase(database);
	}

	public void setRawDatabase(org.openntf.domino.Database database) {
		if (database != null) {
			database_ = database;
			session_ = database.getParent();
			server_ = database.getServer();
			filepath_ = database.getFilePath();
		}
	}

	private java.util.Map<Object, DominoElement> getCache() {
		if (cache_ == null) {
			cache_ = new HashMap<Object, DominoElement>();
		}
		return cache_;
	}

	@Override
	public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex, String label) {
		if (id == null)
			id = (outVertex.getId() + label + inVertex.getId());
		Document d = getDocument(id, true);
		d.replaceItemValue(DominoElement.TYPE_FIELD, DominoEdge.GRAPH_TYPE_VALUE);
		DominoEdge ed = new DominoEdge(this, d);
		getCache().put(id == null ? ed.getId() : id, ed);
		ed.setOutDoc(outVertex);
		ed.setInDoc(inVertex);
		ed.setLabel(label);
		return ed;
	}

	public Edge getOrAddEdge(Object id, Vertex outVertex, Vertex inVertex, String label) {
		Edge result = null;
		if (id == null) {
			id = (outVertex.getId() + label + inVertex.getId());
			result = getEdge(id);
		}
		if (result == null) {
			for (Edge e : outVertex.getEdges(Direction.OUT, label)) {
				Vertex v = e.getVertex(Direction.IN);
				if (v.getId().equals(inVertex.getId())) {
					result = e;
					break;
				}
			}
		}
		if (result == null) {
			result = addEdge(id, outVertex, inVertex, label);
		}
		return result;
	}

	@Override
	public Vertex addVertex(Object id) {
		Document d = getDocument(id, true);
		d.replaceItemValue(DominoElement.TYPE_FIELD, DominoVertex.GRAPH_TYPE_VALUE);
		DominoVertex result = new DominoVertex(this, d);
		getCache().put(id == null ? result.getId() : id, result);
		return result;
	}

	private Database getDatabase() {
		return getRawSession().getDatabase(server_, filepath_);
	}

	public org.openntf.domino.Session getRawSession() {
		if (session_ == null) {
			session_ = Factory.getSession();
		} else {
			session_.isTrustedSession();
		}
		return session_;
	}

	public Database getRawDatabase() {
		if (database_ == null) {
			database_ = getDatabase();
		}
		return database_;
	}

	private Document getDocument(Object id, boolean createOnFail) {
		Document result = null;
		String unid = "";
		if (id == null) {
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
			result = getRawDatabase().getDocumentByUNID(unid);
			if (result == null) {
				// TODO replace with a straight .get call to Database
				result = getRawDatabase().createDocument();
				result.setUniversalID(unid);
				DateTime now = getRawDatabase().getParent().createDateTime(new Date());
				result.replaceItemValue("$Created", now);
			}
		}
		return result;
	}

	@Override
	public Edge getEdge(Object id) {
		if (!getCache().containsKey(id)) {
			Document d = getDocument(id, false);
			if (d == null)
				return null;
			DominoEdge result = new DominoEdge(this, d);
			getCache().put(id, result);
		}
		return (Edge) getCache().get(id);
	}

	@Override
	public Iterable<Edge> getEdges() {
		Set<Edge> result = new LinkedHashSet<Edge>();
		ViewEntryCollection vec = getEdgeView().getAllEntries();
		for (ViewEntry entry : vec) {
			result.add(getEdge(entry.getUniversalID()));
		}

		return result;
	}

	@Override
	public Iterable<Edge> getEdges(String key, Object value) {
		// TODO
		throw new UnsupportedOperationException();
	}

	public Iterable<Edge> getEdgesFromIds(Set<String> set) {
		Set<Edge> result = new HashSet<Edge>();
		for (String id : set) {
			result.add(getEdge(id));
		}
		return result;
	}

	public Iterable<Edge> getEdgesFromIds(Set<String> set, String... labels) {
		Set<Edge> result = new HashSet<Edge>();
		for (String id : set) {
			Edge edge = getEdge(id);
			for (String label : labels) {
				if (label.equals(edge.getLabel())) {
					result.add(edge);
					break;
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
	public Vertex getVertex(Object id) {
		if (!getCache().containsKey(id)) {
			Document d = getDocument(id, false);
			if (d == null)
				return null;
			DominoVertex result = new DominoVertex(this, d);
			getCache().put(id, result);
		}
		return (Vertex) getCache().get(id);
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
		return result;
	}

	@Override
	public Iterable<Vertex> getVertices(String key, Object value) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public GraphQuery query() {
		return new DefaultGraphQuery(this);
	}

	@Override
	public void removeEdge(Edge edge) {
		Vertex in = edge.getVertex(Direction.IN);
		((DominoVertex) in).removeEdge(edge);
		Vertex out = edge.getVertex(Direction.OUT);
		((DominoVertex) out).removeEdge(edge);
	}

	@Override
	public void removeVertex(Vertex vertex) {
		DominoVertex dv = (DominoVertex) vertex;
		for (Edge edge : dv.getEdges(Direction.BOTH)) {
			removeEdge(edge);
		}

	}

	@Override
	public void shutdown() {
		for (DominoElement d : getCache().values()) {
			d.save();
		}
	}

	@Override
	public Object getRawGraph() {
		return getRawDatabase();
	}

}
