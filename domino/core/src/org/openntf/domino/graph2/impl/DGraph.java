package org.openntf.domino.graph2.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javolution.util.FastSet;
import javolution.util.FastTable;

import org.openntf.domino.Session;
import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.big.impl.DbCache;
import org.openntf.domino.big.impl.NoteList;
import org.openntf.domino.graph2.DConfiguration;
import org.openntf.domino.graph2.DElementStore;
import org.openntf.domino.graph2.exception.ElementKeyException;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Features;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.Vertex;

public class DGraph implements org.openntf.domino.graph2.DGraph {
	private static final Logger log_ = Logger.getLogger(DGraph.class.getName());
	public static final Set<String> EMPTY_IDS = Collections.emptySet();
	private DConfiguration configuration_;
	private DbCache dbCache_;

	public static class GraphTransaction extends FastTable<Element> {

	}

	public static ThreadLocal<GraphTransaction> localTxn = new ThreadLocal<GraphTransaction>() {
		@Override
		public GraphTransaction get() {
			if (super.get() == null) {
				super.set(new GraphTransaction());
			}
			return super.get();
		}
	};

	public DGraph(final DConfiguration config) {
		configuration_ = config;
		config.setGraph(this);
	}

	protected Map<Class<?>, Long> getTypeMap() {
		return configuration_.getTypeMap();
	}

	public DConfiguration getConfiguration() {
		return configuration_;
	}

	public void setConfiguration(final DConfiguration configuration) {
		configuration_ = configuration;
	}

	@Override
	public Features getFeatures() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vertex addVertex(final Object id) {
		return findElementStore(id).addVertex(id);
	}

	@Override
	public Vertex getVertex(final Object id) {
		return findElementStore(id).getVertex(id);
	}

	@Override
	public void removeVertex(final Vertex vertex) {
		findElementStore(vertex).removeVertex(vertex);
	}

	@Override
	public Iterable<Vertex> getVertices() {
		FastSet<Vertex> result = new FastSet<Vertex>();
		for (DElementStore store : getElementStores().values()) {
			result.addAll(store.getCachedVertices());
		}
		return result.unmodifiable();
	}

	@Override
	public Iterable<Vertex> getVertices(final String key, final Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edge addEdge(final Object id, final Vertex outVertex, final Vertex inVertex, final String label) {
		Edge result = null;
		//		if (id != null) {
		//			System.out.println("TEMP DEBUG: Adding " + label + " edge with id " + String.valueOf(id));
		result = findElementStore(id).addEdge(id);
		((DEdge) result).setLabel(label);
		((DEdge) result).setInVertex(inVertex);
		((DEdge) result).setOutVertex(outVertex);
		//		} else {
		//			//TODO NTF implementation
		//			System.out.println("TEMP DEBUG: id is null so we don't have an implementation yet.");
		//		}
		return result;
	}

	@Override
	public Edge getEdge(final Object id) {
		if (id instanceof NoteCoordinate) {

		}
		return findElementStore(id).getEdge(id);
	}

	@Override
	public void removeEdge(final Edge edge) {
		findElementStore(edge).removeEdge(edge);
	}

	@Override
	public Iterable<Edge> getEdges() {
		FastSet<Edge> result = new FastSet<Edge>();
		for (DElementStore store : getElementStores().values()) {
			result.addAll(store.getCachedEdges());
		}
		return result.unmodifiable();
	}

	@Override
	public Iterable<Edge> getEdges(final String key, final Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GraphQuery query() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getRawGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stopTransaction(final Conclusion conclusion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void commit() {
		GraphTransaction txn = localTxn.get();
		for (Element elem : txn) {
			if (elem instanceof DElement) {
				DElement delem = (DElement) elem;
				delem.applyChanges();
			}
		}

	}

	@Override
	public void rollback() {
		localTxn.set(null);
	}

	@Override
	public void startTransaction(final Element elem) {
		GraphTransaction txn = localTxn.get();
		txn.add(elem);
	}

	@Override
	public Map<String, Object> findDelegate(final Object delegateKey) {
		DElementStore store = findElementStore(delegateKey);
		return store.findElementDelegate(delegateKey, Element.class);
	}

	@Override
	public void removeDelegate(final Element element) {
		DElementStore store = findElementStore(element);
		store.removeElementDelegate(element);
	}

	@Override
	public Map<Long, DElementStore> getElementStores() {
		return configuration_.getElementStores();
	}

	@Override
	public DElementStore findElementStore(final Element element) {
		DElementStore result = null;
		Class<?> type = element.getClass();
		Long key = getTypeMap().get(type);
		if (key != null) {
			result = findElementStore(key);
		} else {
			//TODO NTF Keep??
			result = findElementStore(element.getId());
		}
		if (result == null) {
			result = getDefaultElementStore();
		}
		return result;
	}

	@Override
	public DElementStore findElementStore(final Class<?> type) {
		DElementStore result = getDefaultElementStore();
		Long key = getTypeMap().get(type);
		if (key != null) {
			DElementStore chk = getElementStores().get(key);
			if (chk != null) {
				result = chk;
			}
		}
		return result;
	}

	@Override
	public DElementStore findElementStore(final Object delegateKey) {
		DElementStore result = null;
		if (delegateKey == null) {
			return getDefaultElementStore();
		}
		if (delegateKey instanceof CharSequence) {
			CharSequence skey = (CharSequence) delegateKey;
			if (skey.length() > 16) {
				CharSequence prefix = skey.subSequence(0, 16);
				if (DominoUtils.isReplicaId(prefix)) {
					Long rid = NoteCoordinate.Utils.getLongFromReplid(prefix);
					result = getElementStores().get(rid);
				} else {
					throw new ElementKeyException("Cannot resolve a key of " + skey.toString());
				}
			} else if (skey.length() == 32) {
				result = getDefaultElementStore();
			} else {
				throw new ElementKeyException("Cannot resolve a key of " + skey.toString());
			}
		} else if (delegateKey instanceof NoteCoordinate) {
			result = getElementStores().get(((NoteCoordinate) delegateKey).getReplicaLong());
		}
		if (result == null) {
			result = getDefaultElementStore();
		}
		return result;
	}

	@Override
	public DElementStore getDefaultElementStore() {
		return getConfiguration().getDefaultElementStore();
	}

	public FastTable<Edge> getEdgesFromIds(final NoteList list) {
		FastTable<Edge> result = new FastTable<Edge>();
		for (NoteCoordinate id : list) {
			Edge edge = getEdge(id);
			if (edge != null) {
				result.add(edge);
			}
		}
		return result;
	}

	@Override
	public FastSet<Edge> getEdgesFromIds(final Set<String> set) {
		FastSet<Edge> result = new FastSet<Edge>();
		for (String id : set) {
			Edge edge = getEdge(id);
			if (edge != null) {
				result.add(edge);
			}
		}
		return result;
	}

	@Override
	public Set<Edge> getEdgesFromIds(final Set<String> set, final String... labels) {
		FastSet<Edge> result = new FastSet<Edge>();
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

	@Override
	public Object getStoreDelegate(final DElementStore store) {
		//FIXME NTF probably need to farm this out to some kind of Factory...
		Object result = null;
		Long key = store.getStoreKey();
		Session session = Factory.getSession();
		String keyStr = NoteCoordinate.Utils.getReplidFromLong(key);
		result = session.getDatabase(keyStr);	//TODO NTF sort out server?
		return result;
	}

}
