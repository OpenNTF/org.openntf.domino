package org.openntf.domino.graph2.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javolution.util.FastSet;

import org.openntf.domino.big.impl.DbCache;
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

	protected Map<Class<?>, Integer> getTypeMap() {
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
	public List<DElementStore> getElementStores() {
		return configuration_.getElementStoreList();
	}

	@Override
	public DElementStore findElementStore(final Element element) {
		DElementStore result = null;
		Class<?> type = element.getClass();
		Integer key = getTypeMap().get(type);
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
		Integer key = getTypeMap().get(type);
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
			if (skey.length() == 36) {
				if (DominoUtils.isHex(skey)) {
					CharSequence prefix = skey.subSequence(0, 4);
					//					CharSequence unid = skey.subSequence(4, 36);
					Integer esKey = Integer.valueOf(prefix.toString(), 16);
					result = getElementStores().get(esKey);
				} else {
					throw new ElementKeyException("Cannot resolve a key of " + skey.toString());
				}
			} else if (skey.length() == 32) {
				result = getDefaultElementStore();
			} else {
				throw new ElementKeyException("Cannot resolve a key of " + skey.toString());
			}
		} else if (delegateKey instanceof byte[]) {
			byte[] bkey = (byte[]) delegateKey;
			if (bkey.length == 18) {
				byte[] sub = new byte[2];
				sub[0] = bkey[0];
				sub[1] = bkey[1];
				Integer esKey = DominoUtils.toInteger(sub);
				result = getElementStores().get(esKey);
			} else if (bkey.length == 16) {
				result = getDefaultElementStore();
			} else {
				throw new ElementKeyException("Cannot resolve a byte key of " + bkey.length + " length");
			}
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
