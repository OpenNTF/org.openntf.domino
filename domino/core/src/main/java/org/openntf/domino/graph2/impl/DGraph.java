package org.openntf.domino.graph2.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.DbDirectory;
import org.openntf.domino.Session;
import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.big.NoteList;
import org.openntf.domino.exceptions.DocumentWriteAccessException;
import org.openntf.domino.ext.Session.Fixes;
//import org.openntf.domino.big.impl.DbCache;
import org.openntf.domino.graph2.DConfiguration;
import org.openntf.domino.graph2.DElementStore;
import org.openntf.domino.graph2.DKeyResolver;
import org.openntf.domino.graph2.exception.ElementKeyException;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Features;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.MultiIterable;

public class DGraph implements org.openntf.domino.graph2.DGraph {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DGraph.class.getName());
	public static final Set<String> EMPTY_IDS = Collections.emptySet();
	private DConfiguration configuration_;
	protected Graph extendedGraph_;

	//	private DbCache dbCache_;
	public static class GraphTransaction extends HashSet<Element> {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean add(final Element e) {
			if (e instanceof DEntryEdge) {
				return false;
			}
			return super.add(e);
		}

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
		// TODO Implement this
		return null;
	}

	@Override
	public Element getElement(final Object id) {
		if (id instanceof NoteCoordinate) {

		}
		DElementStore store = findElementStore(id);
		Element result = store.getElement(id);
		return result;
	}

	@Override
	public Vertex addVertex(final Object id) {
		return addVertex(id, false);
	}

	@Override
	public Vertex addVertex(final Object id, final boolean temporary) {
		return findElementStore(id).addVertex(id, temporary);
	}

	@Override
	public Vertex getVertex(final Object id) {
		DElementStore store = findElementStore(id);
		if (store == null) {
			Throwable t = new IllegalArgumentException("No element store found for id " + String.valueOf(id));
			t.printStackTrace();
			try {
				throw t;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return (Vertex) store.getElement(id);
	}

	@Override
	public void removeVertex(final Vertex vertex) {
		findElementStore(vertex).removeVertex(vertex);
	}

	@Override
	public Iterable<Vertex> getVertices() {
		List<Iterable<Vertex>> storeList = new ArrayList<Iterable<Vertex>>();
		for (DElementStore store : getElementStores().values()) {
			storeList.add(store.getVertices());
		}
		return new MultiIterable<Vertex>(storeList);
	}

	@Override
	public Iterable<Vertex> getVertices(final String key, final Object value) {
		List<Iterable<Vertex>> storeList = new ArrayList<Iterable<Vertex>>();
		for (DElementStore store : getElementStores().values()) {
			storeList.add(store.getVertices(key, value));
		}
		return new MultiIterable<Vertex>(storeList);
	}

	@Override
	public Edge addEdge(final Object id, final Vertex outVertex, final Vertex inVertex, final String label) {
		DEdge result = null;
		//		if (id != null) {
		//			System.out.println("TEMP DEBUG: Adding " + label + " edge with id " + String.valueOf(id));
		result = (DEdge) findElementStore(id).addEdge(id);
		result.setLabel(label);
		result.setInVertex(inVertex);
		result.setOutVertex(outVertex);
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
		return (Edge) findElementStore(id).getElement(id);
	}

	@Override
	public void removeEdge(final Edge edge) {
		findElementStore(edge).removeEdge(edge);
	}

	public void removeEdge(final Edge edge, final Vertex removingVertex) {
		findElementStore(edge).removeEdge(edge, removingVertex);
	}

	@Override
	public Iterable<Edge> getEdges() {
		List<Iterable<Edge>> storeList = new ArrayList<Iterable<Edge>>();
		for (DElementStore store : getElementStores().values()) {
			storeList.add(store.getEdges());
		}
		return new MultiIterable<Edge>(storeList);
	}

	@Override
	public Iterable<Edge> getEdges(final String key, final Object value) {
		List<Iterable<Edge>> storeList = new ArrayList<Iterable<Edge>>();
		for (DElementStore store : getElementStores().values()) {
			storeList.add(store.getEdges(key, value));
		}
		return new MultiIterable<Edge>(storeList);
	}

	@Override
	public GraphQuery query() {
		// TODO Implement this or remove comment
		return null;
	}

	@Override
	public void shutdown() {

	}

	@Override
	public Object getRawGraph() {
		// TODO Implement this or remove comment
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void stopTransaction(final Conclusion conclusion) {
		// TODO Implement this or remove comment

	}

	@Override
	public void commit() {
		GraphTransaction txn = localTxn.get();
		if (txn != null) {
			Iterator<Element> it = txn.iterator();
			int count = 0;
			while (it.hasNext()) {
				Element elem = it.next();
				if (elem instanceof DElement) {
					DElement delem = (DElement) elem;
					try {
						delem.applyChanges();
					} catch (DocumentWriteAccessException t) {
						System.err.println(t.getMessage());
					} catch (Throwable t) {
						t.printStackTrace();
					}
					//					DElementStore store = findElementStore(elem.getId());
					//				store.uncache(delem);
					count++;
				}
				it.remove();
			}
			//			System.out.println("TEMP DEBUG: Transaction committed changes to " + count + " elements");
		}
		localTxn.set(null);
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

	public void clearTransaction() {
		localTxn.set(null);
	}

	@Override
	public Object findDelegate(final Object delegateKey) {
		DElementStore store = findElementStore(delegateKey);
		return store.findElementDelegate(delegateKey/*, Element.class*/);
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
			//			System.out.println("delegateKey is null");
			return getDefaultElementStore();
		}
		if (delegateKey instanceof CharSequence) {
			CharSequence skey = (CharSequence) delegateKey;
			//			System.out.println("delegateKey is CharSequence " + skey.length());
			if (skey.length() == 16) {
				if (DominoUtils.isReplicaId(skey)) {
					Long rid = NoteCoordinate.Utils.getLongFromReplid(skey);
					result = getElementStores().get(rid);
					if (result == null) {
						DElementStore newStore = new org.openntf.domino.graph2.impl.DElementStore();
						newStore.setStoreKey(rid);
						newStore.setConfiguration(this.getConfiguration());
						getElementStores().put(rid, newStore);
						//						System.out.println("TEMP DEBUG Added new dynamic element store " + String.valueOf(rid));
						result = newStore;
					}
				} else {
					throw new ElementKeyException("Cannot resolve a key of " + skey.toString());
				}
			} else if (skey.length() == 32) {
				result = getDefaultElementStore();
			} else if (skey.length() > 50) {
				String prefix = skey.subSequence(0, 2).toString();
				if (prefix.equals("EC") || prefix.equals("ED") || prefix.equals("ET") || prefix.equals("EU")) {
					CharSequence mid = skey.subSequence(2, 50);
					if (DominoUtils.isMetaversalId(mid)) {
						CharSequence ridStr = skey.subSequence(2, 18);
						Long rid = NoteCoordinate.Utils.getLongFromReplid(ridStr);
						result = getElementStores().get(rid);
						if (result == null) {
							DElementStore newStore = new org.openntf.domino.graph2.impl.DElementStore();
							newStore.setStoreKey(rid);
							DConfiguration config = this.getConfiguration();
							if (config.getDefaultProxyStore() != null) {
								newStore.setProxyStoreDelegate(config.getDefaultProxyStore());
							}
							newStore.setConfiguration(config);
							getElementStores().put(rid, newStore);
							//							System.out.println("TEMP DEBUG Added new dynamic element store " + String.valueOf(rid));
							result = newStore;
						}
					}
				} else if (prefix.equals("VC") || prefix.equals("VD") || prefix.equals("VT") || prefix.equals("VU")) {
					CharSequence mid = skey.subSequence(2, 50);
					if (DominoUtils.isMetaversalId(mid)) {
						CharSequence ridStr = skey.subSequence(2, 18);
						Long rid = NoteCoordinate.Utils.getLongFromReplid(ridStr);
						result = getElementStores().get(rid);
						if (result == null) {
							DElementStore newStore = new org.openntf.domino.graph2.impl.DElementStore();
							newStore.setStoreKey(rid);
							DConfiguration config = this.getConfiguration();
							if (config.getDefaultProxyStore() != null) {
								newStore.setProxyStoreDelegate(config.getDefaultProxyStore());
							}
							newStore.setConfiguration(config);
							getElementStores().put(rid, newStore);
							//							System.out.println("TEMP DEBUG Added new dynamic element store " + String.valueOf(rid));
							result = newStore;
						}
					}
				}
				if (result == null) {
					throw new ElementKeyException("Cannot resolve a key of " + skey.toString());
				}
			} else if (skey.length() > 16) {
				CharSequence prefix = skey.subSequence(0, 16);
				if (DominoUtils.isReplicaId(prefix)) {
					System.out.println("TEMP DEBUG Attempting to resolve replica id " + prefix + " to an element store");
					Long rid = NoteCoordinate.Utils.getLongFromReplid(prefix);
					result = getElementStores().get(rid);
					if (result == null) {
						DElementStore newStore = new org.openntf.domino.graph2.impl.DElementStore();
						newStore.setStoreKey(rid);
						newStore.setConfiguration(this.getConfiguration());
						getElementStores().put(rid, newStore);
						System.out.println("TEMP DEBUG Added new dynamic element store " + String.valueOf(rid));
						result = newStore;
					}
				} else {
					throw new ElementKeyException("Cannot resolve a key of " + skey.toString());
				}

			} else {
				throw new ElementKeyException("Cannot resolve a key of " + skey.toString());
			}
		} else if (delegateKey instanceof NoteCoordinate) {
			//			System.out.println("delegateKey is a NoteCoordinate");
			long key = ((NoteCoordinate) delegateKey).getReplicaLong();
			result = getElementStores().get(key);
			if (result == null) {
				DElementStore newStore = new org.openntf.domino.graph2.impl.DElementStore();
				newStore.setStoreKey(key);
				newStore.setConfiguration(this.getConfiguration());
				getElementStores().put(key, newStore);
				//				System.out.println("TEMP DEBUG Added new dynamic element store " + String.valueOf(key));
				result = newStore;
			}
			//			System.out.println("TEMP DEBUG returning an element store for key " + NoteCoordinate.Utils.getReplidFromLong(key)
			//					+ " and isProxied is " + result.isProxied());
		} else {
			System.err.println("delegateKey is a " + delegateKey.getClass().getName());
		}
		if (result == null) {
			result = getDefaultElementStore();
			//			System.out.println("Returning default element store");
		}
		return result;
	}

	@Override
	public DElementStore getDefaultElementStore() {
		return getConfiguration().getDefaultElementStore();
	}

	public org.openntf.domino.graph2.DEdgeList getEdgesFromIds(final Vertex source, final NoteList list) {
		org.openntf.domino.graph2.DEdgeList result = new DFastEdgeList((DVertex) source, this, list);
		return result;
	}

	@Override
	public DEdgeList getEdgesFromIds(final Vertex source, final Set<String> set) {
		DEdgeList result = new DEdgeList((DVertex) source);
		for (String id : set) {
			Edge edge = getEdge(id);
			if (edge != null) {
				result.add(edge);
			}
		}
		return result;
	}

	@Override
	public DEdgeList getEdgesFromIds(final Vertex source, final Set<String> set, final String... labels) {
		DEdgeList result = new DEdgeList((DVertex) source);
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
		String keyStr = NoteCoordinate.Utils.getReplidFromLong(key);
		//		System.out.println("Attempting to find database with replica id " + keyStr);
		Session session = Factory.getSession(SessionType.CURRENT);
		//		DbDirectory dir = session.getDbDirectory("");
		//		result = dir.openDatabaseByReplicaID(keyStr);
		//		if (result == null) {
		//			System.err.println("Unable to open database by replica id " + keyStr);
		//		}
		result = session.getDatabase(keyStr);	//TODO NTF sort out server?
		return result;
	}

	@Override
	public Object getStoreDelegate(final DElementStore store, final Object provisionalKey) {
		Object result = null;
		Session session = Factory.getSession(SessionType.CURRENT);
		if (provisionalKey instanceof CharSequence) {
			String key = provisionalKey.toString();
			String server = "";
			if (key.contains("!!")) {
				server = "";	//TODO NTF parse key string to find server name in form of 'Server!!path.nsf'
				//key = key;	//TODO NTF parse again
			}
			DbDirectory dir = session.getDbDirectory(server);
			result = dir.openDatabase(key);
			if (result == null) {
				//				System.out.println("Creating NSF for delegate: " + key);
				Session localSession = Factory.getSession(SessionType.NATIVE);
				localSession.setFixEnable(Fixes.CREATE_DB, true);
				DbDirectory localDir = localSession.getDbDirectory(server);
				Database newDb = localDir.createDatabase(key, true);
				if (newDb.isOpen()) {
					//					System.out.println("Configuring NSF...");
					newDb.setCategories("graph2");
					newDb.setFolderReferencesEnabled(false);
					newDb.setTitle("Auto-generated for " + key);
					//					System.out.println("Configuring view...");
					for (org.openntf.domino.View v : newDb.getViews()) {
						v.setName("NONE");
						v.setSelectionFormula("SELECT @False");
					}
				} else {
					System.out.println("Database is not open");
				}
				result = newDb;
				//				System.out.println("New NSF complete");
			}
			store.setStoreKey(((Database) result).getReplicaID());
		} else {
			//TODO NTF Unimplemented
		}
		return result;
	}

	@Override
	public Object getProxyStoreDelegate(final DElementStore store) {
		//FIXME NTF probably need to farm this out to some kind of Factory...
		Object result = null;
		Long key = store.getProxyStoreKey();
		Session session = Factory.getSession(SessionType.CURRENT);
		String keyStr = NoteCoordinate.Utils.getReplidFromLong(key);
		result = session.getDatabase(keyStr);	//TODO NTF sort out server?
		return result;
	}

	@Override
	public Object getProxyStoreDelegate(final DElementStore store, final Object provisionalKey) {
		Object result = null;
		Session session = Factory.getSession(SessionType.CURRENT);
		if (provisionalKey instanceof CharSequence) {
			String key = provisionalKey.toString();
			String server = "";
			if (key.contains("!!")) {
				server = "";	//TODO NTF parse key string to find server name in form of 'Server!!path.nsf'
				//key = key;	//TODO NTF parse again
			}
			DbDirectory dir = session.getDbDirectory(server);
			result = dir.openDatabase(key);
			if (result == null) {
				//				System.out.println("Creating NSF for proxy delegate: " + key);
				Session localSession = Factory.getSession(SessionType.NATIVE);
				localSession.setFixEnable(Fixes.CREATE_DB, true);
				DbDirectory localDir = localSession.getDbDirectory(server);
				Database newDb = localDir.createDatabase(key, true);
				if (newDb.isOpen()) {
					//					System.out.println("Configuring NSF...");
					newDb.setCategories("graph2");
					newDb.setFolderReferencesEnabled(false);
					newDb.setTitle("Auto-generated for " + key);
					//					System.out.println("Configuring view...");
					for (org.openntf.domino.View v : newDb.getViews()) {
						v.setName("NONE");
						v.setSelectionFormula("SELECT @False");
					}
				} else {
					System.out.println("Db not open");
				}
				result = newDb;
				//				System.out.println("New NSF complete");
			}
			store.setProxyStoreKey(((Database) result).getReplicaID());
		} else {
			//TODO NTF Unimplemented
		}
		return result;
	}

	@Override
	public void addKeyResolver(final DKeyResolver resolver) {
		getConfiguration().addKeyResolver(resolver);
	}

	@Override
	public DKeyResolver getKeyResolver(final Class<?> type) {
		return getConfiguration().getKeyResolver(type);
	}

	@Override
	public void setExtendedGraph(final Graph graph) {
		extendedGraph_ = graph;
	}

	@Override
	public Graph getExtendedGraph() {
		return extendedGraph_;
	}

	@Override
	public void flushCache() {
		for (Long key : getElementStores().keySet()) {
			DElementStore elemStore = getElementStores().get(key);
			elemStore.flushCache();
		}
	}
}
