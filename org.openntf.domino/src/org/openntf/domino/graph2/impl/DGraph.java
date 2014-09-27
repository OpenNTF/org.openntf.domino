package org.openntf.domino.graph2.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javolution.util.FastSet;
import javolution.util.FastTable;

import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.big.impl.DbCache;
import org.openntf.domino.big.impl.NoteList;
import org.openntf.domino.graph2.DConfiguration;
import org.openntf.domino.graph2.DElementStore;
import org.openntf.domino.graph2.exception.ElementKeyException;
import org.openntf.domino.utils.DominoUtils;

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

	protected class GraphTransaction {
		private DGraph parent_;
		private FastSet<Element> elements_ = new FastSet<Element>();

		GraphTransaction(final DGraph parent) {
			parent_ = parent;
		}

	}

	protected Map<Class<?>, String> getTypeMap() {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Vertex> getVertices(final String key, final Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edge addEdge(final Object id, final Vertex outVertex, final Vertex inVertex, final String label) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startTransaction(final Element elem) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> findDelegate(final Object delegateKey) {
		DElementStore store = findElementStore(delegateKey);
		return store.findElementDelegate(delegateKey);
	}

	@Override
	public void removeDelegate(final Element element) {
		DElementStore store = findElementStore(element);
		store.removeElementDelegate(element);
	}

	@Override
	public Map<String, DElementStore> getElementStores() {
		return configuration_.getElementStores();
	}

	@Override
	public DElementStore findElementStore(final Element element) {
		DElementStore result = null;
		Class<?> type = element.getClass();
		String key = getTypeMap().get(type);
		if (key != null) {
			result = findElementStore(type);
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
		String key = getTypeMap().get(type);
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
		if (delegateKey instanceof CharSequence) {
			CharSequence skey = (CharSequence) delegateKey;
			if (skey.length() == 48) {
				if (DominoUtils.isHex(skey)) {
					CharSequence prefix = skey.subSequence(0, 16);
					result = getElementStores().get(prefix);
				} else {
					throw new ElementKeyException("Cannot resolve a key of " + skey.toString());
				}
			} else if (skey.length() == 32) {
				result = getDefaultElementStore();
			} else {
				throw new ElementKeyException("Cannot resolve a key of " + skey.toString());
			}
		} else if (delegateKey instanceof NoteCoordinate) {
			//TODO
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

}
