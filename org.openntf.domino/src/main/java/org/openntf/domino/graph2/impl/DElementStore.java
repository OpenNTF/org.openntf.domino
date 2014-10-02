package org.openntf.domino.graph2.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;

import org.openntf.domino.Database;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.TypeUtils;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

public class DElementStore implements org.openntf.domino.graph2.DElementStore {
	private static final Logger log_ = Logger.getLogger(DElementStore.class.getName());

	private List<Class<?>> types_;
	private Object delegate_;
	private String delegateKey_;
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
		delegateKey_ = in.readUTF();
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
		out.writeUTF(delegateKey_);
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
		return delegate_;
	}

	@Override
	public void setStoreDelegate(final Object store) {
		delegate_ = store;
		if (store instanceof Database) {
			delegateKey_ = ((Database) store).getReplicaID();
		} else {
			//TODO Some other mechanism to get the key
		}
	}

	@Override
	public String getStoreKey() {
		if (delegateKey_ == null) {
			Object delegate = getStoreDelegate();
			if (delegate != null) {
				if (delegate instanceof Database) {
					delegateKey_ = ((Database) delegate).getReplicaID();
				} else {
					//TODO Some other mechanism to get the key
				}
			}
		}
		return delegateKey_;
	}

	@Override
	public Vertex addVertex(final Object id) {
		Vertex result = null;
		Element chk = getElementCache().get(id);
		if (chk != null) {
			if (chk instanceof Vertex) {
				result = (Vertex) chk;
			} else {
				throw new IllegalStateException("Requested id of " + String.valueOf(id) + " is already in cache but is a "
						+ chk.getClass().getName());
			}
		} else {
			Map<String, Object> delegate = addElementDelegate(id);
			if (delegate != null) {
				Object typeChk = delegate.get(org.openntf.domino.graph2.DElement.TYPE_FIELD);
				if (typeChk == null) {//NTF new delegate
					delegate.put(org.openntf.domino.graph2.DElement.TYPE_FIELD, org.openntf.domino.graph2.DVertex.GRAPH_TYPE_VALUE);
				} else if (org.openntf.domino.graph2.DVertex.GRAPH_TYPE_VALUE.equals(TypeUtils.toString(typeChk))) {//NTF existing delegate that's a vertex

				} else {
					throw new IllegalStateException("Requested id of " + String.valueOf(id)
							+ " results in a delegate with a graph type of " + TypeUtils.toString(typeChk));
				}
				DVertex vertex = new DVertex(getConfiguration().getGraph(), delegate);
				result = vertex;
			} else {
				throw new IllegalStateException("Requested id of " + String.valueOf(id)
						+ " results in a null delegate and therefore cannot be persisted.");
			}
			if (result != null) {
				getElementCache().put(result.getId(), result);
			}
		}
		return result;
	}

	@Override
	public Vertex getVertex(final Object id) {
		Vertex result = null;
		Element chk = getElementCache().get(id);
		if (chk != null) {
			if (chk instanceof Vertex) {
				result = (Vertex) chk;
			} else {
				throw new IllegalStateException("Requested id of " + String.valueOf(id) + " is already in cache but is a "
						+ chk.getClass().getName());
			}
		} else {
			Map<String, Object> delegate = findElementDelegate(id);
			if (delegate != null) {
				Object typeChk = delegate.get(org.openntf.domino.graph2.DElement.TYPE_FIELD);
				if (typeChk == null) {//NTF new delegate
					delegate.put(org.openntf.domino.graph2.DElement.TYPE_FIELD, org.openntf.domino.graph2.DVertex.GRAPH_TYPE_VALUE);
				} else if (org.openntf.domino.graph2.DVertex.GRAPH_TYPE_VALUE.equals(TypeUtils.toString(typeChk))) {//NTF existing delegate that's a vertex

				} else {
					throw new IllegalStateException("Requested id of " + String.valueOf(id)
							+ " results in a delegate with a graph type of " + TypeUtils.toString(typeChk));
				}
				DVertex vertex = new DVertex(getConfiguration().getGraph(), delegate);
				result = vertex;
			} else {
				return null;
			}
			if (result != null) {
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
		Element chk = getElementCache().get(id);
		if (chk != null) {
			if (chk instanceof Edge) {
				result = (Edge) chk;
			} else {
				throw new IllegalStateException("Requested id of " + String.valueOf(id) + " is already in cache but is a "
						+ chk.getClass().getName());
			}
		} else {
			Map<String, Object> delegate = addElementDelegate(id);
			if (delegate != null) {
				Object typeChk = delegate.get(org.openntf.domino.graph2.DElement.TYPE_FIELD);
				if (typeChk == null) {//NTF new delegate
					delegate.put(org.openntf.domino.graph2.DElement.TYPE_FIELD, org.openntf.domino.graph2.DEdge.GRAPH_TYPE_VALUE);
				} else if (org.openntf.domino.graph2.DEdge.GRAPH_TYPE_VALUE.equals(TypeUtils.toString(typeChk))) {//NTF existing delegate that's a vertex
				} else {
					throw new IllegalStateException("Requested id of " + String.valueOf(id)
							+ " results in a delegate with a graph type of " + TypeUtils.toString(typeChk));
				}
				DEdge edge = new DEdge(getConfiguration().getGraph(), delegate);
				result = edge;
			} else {
				throw new IllegalStateException("Requested id of " + String.valueOf(id)
						+ " results in a null delegate and therefore cannot be persisted.");
			}
			if (result != null) {
				getElementCache().put(result.getId(), result);
			}
		}
		return result;
	}

	@Override
	public Edge getEdge(final Object id) {
		Edge result = null;
		Element chk = getElementCache().get(id);
		if (chk != null) {
			if (chk instanceof Edge) {
				result = (Edge) chk;
			} else {
				throw new IllegalStateException("Requested id of " + String.valueOf(id) + " is already in cache but is a "
						+ chk.getClass().getName());
			}
		} else {
			Map<String, Object> delegate = findElementDelegate(id);
			if (delegate != null) {
				Object typeChk = delegate.get(org.openntf.domino.graph2.DElement.TYPE_FIELD);
				if (typeChk == null) {//NTF new delegate
					delegate.put(org.openntf.domino.graph2.DElement.TYPE_FIELD, org.openntf.domino.graph2.DEdge.GRAPH_TYPE_VALUE);
				} else if (org.openntf.domino.graph2.DEdge.GRAPH_TYPE_VALUE.equals(TypeUtils.toString(typeChk))) {//NTF existing delegate that's a vertex

				} else {
					throw new IllegalStateException("Requested id of " + String.valueOf(id)
							+ " results in a delegate with a graph type of " + TypeUtils.toString(typeChk));
				}
				DEdge vertex = new DEdge(getConfiguration().getGraph(), delegate);
				result = vertex;
			} else {
				return null;
			}
			if (result != null) {
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
	public Map<String, Object> findElementDelegate(final Object delegateKey) {
		Map<String, Object> result = null;
		Object del = getStoreDelegate();
		if (del instanceof Database) {
			Database db = (Database) del;
			if (delegateKey instanceof Serializable) {
				result = db.getDocumentWithKey((Serializable) delegateKey, false);
			} else {
				throw new IllegalArgumentException("Cannot find a delegate with a key of type " + delegateKey.getClass().getName());
			}
		} else {
			//TODO NTF alternative strategies...
		}
		return result;
	}

	@Override
	public void removeElementDelegate(final Element element) {
		// TODO Auto-generated method stub

	}

	protected Map<String, Object> addElementDelegate(final Object delegateKey) {
		Map<String, Object> result = null;
		Object del = getStoreDelegate();
		if (del instanceof Database) {
			Database db = (Database) del;
			if (delegateKey instanceof Serializable) {
				result = db.getDocumentWithKey((Serializable) delegateKey, true);
			} else {
				throw new IllegalArgumentException("Cannot add a delegate with a key of type " + delegateKey.getClass().getName());
			}
		} else {
			//TODO NTF alternative strategies...
		}
		return result;
	}

	@Override
	public void setConfiguration(final org.openntf.domino.graph2.DConfiguration config) {
		configuration_ = config;
	}

}
