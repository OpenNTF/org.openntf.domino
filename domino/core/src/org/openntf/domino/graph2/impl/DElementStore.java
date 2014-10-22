package org.openntf.domino.graph2.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastSet;
import javolution.util.FastTable;

import org.openntf.domino.Database;
import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.utils.Factory;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

public class DElementStore implements org.openntf.domino.graph2.DElementStore {
	private static final Logger log_ = Logger.getLogger(DElementStore.class.getName());

	private List<Class<?>> types_;
	private Object delegate_;
	private Long delegateKey_;
	private transient Map<Object, Element> elementCache_;
	private transient org.openntf.domino.graph2.DConfiguration configuration_;

	protected void setTypes(final List<Class<?>> types) {
		types_ = types;
	}

	protected Map<Object, Element> getElementCache() {
		if (elementCache_ == null) {
			elementCache_ = new FastMap<Object, Element>().atomic();
		}
		return elementCache_;
	}

	public DElementStore() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public org.openntf.domino.graph2.DConfiguration getConfiguration() {
		return configuration_;
	}

	public void setConfiguration(final DConfiguration config) {
		configuration_ = config;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		delegateKey_ = in.readLong();
		int count = in.readInt();
		types_ = new FastTable<Class<?>>();

		ClassLoader cl = Factory.getClassLoader();
		if (cl == null)
			cl = Thread.currentThread().getContextClassLoader();
		for (int i = 0; i < count; i++) {
			String className = in.readUTF();
			Class<?> clazz = cl.loadClass(className);
			types_.add(clazz);
		}
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeLong(delegateKey_);
		out.writeInt(getTypes().size());
		for (Class<?> clazz : getTypes()) {
			out.writeUTF(clazz.getName());
		}
	}

	@Override
	public void addType(final Class<?> type) {
		List<Class<?>> types = getTypes();
		if (!types.contains(type)) {
			types.add(type);
		}
	}

	@Override
	public void removeType(final Class<?> type) {
		List<Class<?>> types = getTypes();
		types.remove(type);
	}

	@Override
	public List<Class<?>> getTypes() {
		if (types_ == null) {
			types_ = new FastTable<Class<?>>();
		}
		return types_;
	}

	@Override
	public Object getStoreDelegate() {
		if (delegate_ == null) {
			org.openntf.domino.graph2.DConfiguration config = getConfiguration();
			org.openntf.domino.graph2.DGraph graph = config.getGraph();
			delegate_ = graph.getStoreDelegate(this);
		}
		return delegate_;
	}

	@Override
	public void setStoreDelegate(final Object store) {
		delegate_ = store;
		if (store instanceof Database) {
			String rid = ((Database) store).getReplicaID();
			delegateKey_ = NoteCoordinate.Utils.getLongFromReplid(rid);
		} else {
			//TODO Some other mechanism to get the key
		}
	}

	@Override
	public Long getStoreKey() {
		if (delegateKey_ == null) {
			Object delegate = getStoreDelegate();
			if (delegate != null) {
				if (delegate instanceof Database) {
					String rid = ((Database) delegate).getReplicaID();
					delegateKey_ = NoteCoordinate.Utils.getLongFromReplid(rid);
				} else {
					//TODO Some other mechanism to get the key
				}
			}
		}
		return delegateKey_;
	}

	@Override
	public void setStoreKey(final Long storeKey) {
		delegateKey_ = storeKey;
	}

	protected Object localizeKey(final Object id) {
		if (id instanceof CharSequence) {
			String idStr = id.toString();
			if (idStr.length() > 16) {
				String prefix = idStr.substring(0, 16);
				String keyStr = NoteCoordinate.Utils.getReplidFromLong(getStoreKey());
				//				System.out.println("TEMP DEBUG: prefix on key is " + prefix + " while store key is " + keyStr);
				if (prefix.equalsIgnoreCase(keyStr)) {
					String localKey = idStr.substring(16);
					//					System.out.println("TEMP DEBUG: adding element with local key " + localKey);
					return localKey;
				} else {
					return idStr;
				}
			} else {
				return idStr;
			}
		} else {
			return id;
		}
	}

	@Override
	public Vertex addVertex(final Object id) {
		Vertex result = null;
		Element chk = getCachedElement(id, Vertex.class);
		if (chk != null) {
			result = (Vertex) chk;
		} else {
			Object localkey = localizeKey(id);
			Map<String, Object> delegate = addElementDelegate(localkey, Vertex.class);
			if (delegate != null) {
				DVertex vertex = new DVertex(getConfiguration().getGraph(), delegate);
				result = vertex;
				getElementCache().put(result.getId(), result);
				getConfiguration().getGraph().startTransaction(result);
			}
		}
		return result;
	}

	@Override
	public Vertex getVertex(final Object id) {
		Vertex result = null;
		Element chk = getCachedElement(id, Vertex.class);
		if (chk != null) {
			result = (Vertex) chk;
		} else {
			Object localkey = localizeKey(id);
			Map<String, Object> delegate = findElementDelegate(localkey, Vertex.class);
			if (delegate != null) {
				DVertex vertex = new DVertex(getConfiguration().getGraph(), delegate);
				result = vertex;
				getElementCache().put(result.getId(), result);
			}
		}
		return result;
	}

	@Override
	public void removeVertex(final Vertex vertex) {
		startTransaction(vertex);
		DVertex dv = (DVertex) vertex;
		for (Edge edge : dv.getEdges(Direction.BOTH)) {
			getConfiguration().getGraph().removeEdge(edge);
		}
		removeCache(vertex);
		dv._remove();
	}

	@Override
	public Edge addEdge(final Object id) {
		Edge result = null;
		Element chk = getCachedElement(id, Edge.class);
		if (chk != null) {
			result = (Edge) chk;
		} else {
			if (id == null) {

			}
			Object localkey = localizeKey(id);
			Map<String, Object> delegate = addElementDelegate(localkey, Edge.class);
			if (delegate != null) {
				DEdge edge = new DEdge(getConfiguration().getGraph(), delegate);
				result = edge;
				//				System.out.println("TEMP DEBUG: Returning edge " + result.getId());
				getElementCache().put(result.getId(), result);
				getConfiguration().getGraph().startTransaction(result);
			}
		}
		return result;
	}

	protected Element getCachedElement(final Object id, final Class<? extends Element> type) {
		if (id == null)
			return null;
		Element chk = getElementCache().get(id);
		if (chk != null) {
			if (type.isAssignableFrom(chk.getClass())) {
				return chk;
			} else {
				throw new IllegalStateException("Requested id of " + String.valueOf(id) + " is already in cache but is a "
						+ chk.getClass().getName());
			}
		}
		return null;
	}

	@Override
	public Edge getEdge(final Object id) {
		Edge result = null;
		Element chk = getCachedElement(id, Edge.class);
		if (chk != null) {
			result = (Edge) chk;
		} else {
			Object localkey = localizeKey(id);
			Map<String, Object> delegate = findElementDelegate(localkey, Edge.class);
			if (delegate != null) {
				DEdge edge = new DEdge(getConfiguration().getGraph(), delegate);
				result = edge;
				getElementCache().put(result.getId(), result);
			}
		}
		return result;
	}

	@Override
	public void removeEdge(final Edge edge) {
		startTransaction(edge);
		Vertex in = edge.getVertex(Direction.IN);
		((DVertex) in).removeEdge(edge);
		Vertex out = edge.getVertex(Direction.OUT);
		((DVertex) out).removeEdge(edge);
		removeCache(edge);
		((DEdge) edge)._remove();
	}

	private void startTransaction(final Element element) {
		getConfiguration().getGraph().startTransaction(element);
	}

	private void removeCache(final Element element) {

	}

	@Override
	public Map<String, Object> findElementDelegate(final Object delegateKey, final Class<? extends Element> type) {
		Map<String, Object> result = null;
		Object del = getStoreDelegate();
		if (del instanceof Database) {
			Database db = (Database) del;
			if (delegateKey instanceof Serializable) {
				if (delegateKey instanceof NoteCoordinate) {
					String unid = ((NoteCoordinate) delegateKey).getUNID();
					result = db.getDocumentWithKey(unid, false);
				} else {
					result = db.getDocumentWithKey((Serializable) delegateKey, false);
				}
				if (result == null) {
					System.out.println("Request with delegatekey " + delegateKey.getClass().getName() + " (" + delegateKey + ")"
							+ " returned null");
				}
			} else {
				throw new IllegalArgumentException("Cannot find a delegate with a key of type " + delegateKey.getClass().getName());
			}
		} else {
			//TODO NTF alternative strategies...
		}
		if (result != null) {
			if (type.equals(Element.class)) {
				return result;
			}
			Object typeChk = result.get(org.openntf.domino.graph2.DElement.TYPE_FIELD);
			String strChk = org.openntf.domino.utils.TypeUtils.toString(typeChk);
			if (org.openntf.domino.utils.Strings.isBlankString(strChk)) {//NTF new delegate
				if (Vertex.class.isAssignableFrom(type)) {
					result.put(org.openntf.domino.graph2.DElement.TYPE_FIELD, org.openntf.domino.graph2.DVertex.GRAPH_TYPE_VALUE);
				} else if (Edge.class.isAssignableFrom(type)) {
					result.put(org.openntf.domino.graph2.DElement.TYPE_FIELD, org.openntf.domino.graph2.DEdge.GRAPH_TYPE_VALUE);
				} else {
					//Illegal request
				}
			} else {//NTF existing delegate that's a vertex
				if (Vertex.class.isAssignableFrom(type) && org.openntf.domino.graph2.DVertex.GRAPH_TYPE_VALUE.equals(strChk)) {
					//okay
				} else if (Edge.class.isAssignableFrom(type) && org.openntf.domino.graph2.DEdge.GRAPH_TYPE_VALUE.equals(strChk)) {
					//okay
				} else {
					throw new IllegalStateException("Requested id of " + String.valueOf(delegateKey)
							+ " results in a delegate with a graph type of " + strChk);
				}
			}
		} else {
			throw new IllegalStateException("Requested id of " + String.valueOf(delegateKey)
					+ " results in a null delegate and therefore cannot be persisted.");
		}
		return result;
	}

	@Override
	public void removeElementDelegate(final Element element) {
		// TODO Auto-generated method stub

	}

	protected Map<String, Object> addElementDelegate(final Object delegateKey, final Class<? extends Element> type) {
		Map<String, Object> result = null;
		Object del = getStoreDelegate();
		if (del instanceof Database) {
			Database db = (Database) del;
			if (delegateKey == null || delegateKey instanceof Serializable) {
				result = db.getDocumentWithKey((Serializable) delegateKey, true);
			} else {
				throw new IllegalArgumentException("Cannot add a delegate with a key of type " + delegateKey.getClass().getName());
			}
		} else {
			//TODO NTF alternative strategies...
		}
		if (result != null) {
			Object typeChk = result.get(org.openntf.domino.graph2.DElement.TYPE_FIELD);
			String strChk = org.openntf.domino.utils.TypeUtils.toString(typeChk);
			if (org.openntf.domino.utils.Strings.isBlankString(strChk)) {//NTF new delegate
			//				System.out.println("TEMP DEBUG: New delegate for key " + String.valueOf(delegateKey));
				if (Vertex.class.isAssignableFrom(type)) {
					//					System.out.println("TEMP DEBUG: New vertex for key " + String.valueOf(delegateKey));
					result.put(org.openntf.domino.graph2.DElement.TYPE_FIELD, org.openntf.domino.graph2.DVertex.GRAPH_TYPE_VALUE);
				} else if (Edge.class.isAssignableFrom(type)) {
					//					System.out.println("TEMP DEBUG: New edge for key " + String.valueOf(delegateKey));
					result.put(org.openntf.domino.graph2.DElement.TYPE_FIELD, org.openntf.domino.graph2.DEdge.GRAPH_TYPE_VALUE);
				} else {
					//Illegal request
				}
			} else {//NTF existing delegate
			//				System.out.println("TEMP DEBUG: Delegate already exists for " + String.valueOf(delegateKey));
				if (Vertex.class.isAssignableFrom(type) && org.openntf.domino.graph2.DVertex.GRAPH_TYPE_VALUE.equals(strChk)) {
					//okay
				} else if (Edge.class.isAssignableFrom(type) && org.openntf.domino.graph2.DEdge.GRAPH_TYPE_VALUE.equals(strChk)) {
					//okay
				} else {
					throw new IllegalStateException("Requested id of " + String.valueOf(delegateKey)
							+ " results in a delegate with a graph type of " + strChk);
				}
			}
		} else {
			throw new IllegalStateException("Requested id of " + String.valueOf(delegateKey)
					+ " results in a null delegate and therefore cannot be persisted.");
		}

		return result;
	}

	@Override
	public void setConfiguration(final org.openntf.domino.graph2.DConfiguration config) {
		configuration_ = config;
	}

	@Override
	public Set<Vertex> getCachedVertices() {
		FastSet<Vertex> result = new FastSet<Vertex>();
		for (Element elem : getElementCache().values()) {
			if (elem instanceof Vertex) {
				result.add((Vertex) elem);
			}
		}
		return result.unmodifiable();
	}

	@Override
	public Set<Edge> getCachedEdges() {
		FastSet<Edge> result = new FastSet<Edge>();
		for (Element elem : getElementCache().values()) {
			if (elem instanceof Edge) {
				result.add((Edge) elem);
			}
		}
		return result.unmodifiable();
	}

}
