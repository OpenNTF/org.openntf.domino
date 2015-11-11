package org.openntf.domino.graph2.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastSet;
import javolution.util.FastTable;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.big.ViewEntryCoordinate;
import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.graph2.DIdentityFactory;
import org.openntf.domino.graph2.builtin.CategoryVertex;
import org.openntf.domino.graph2.builtin.ViewVertex;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.google.common.base.Strings;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.VertexFrame;

public class DElementStore implements org.openntf.domino.graph2.DElementStore {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DElementStore.class.getName());

	private List<Class<?>> types_;
	private Object delegate_;
	private Long delegateKey_;
	private Object provisionalDelegateKey_;
	private Object proxyDelegate_;
	private Long proxyDelegateKey_;
	private Object provisionalProxyDelegateKey_;
	private DIdentityFactory identityFactory_;
	private CustomProxyResolver proxyResolver_;
	private transient Map<Object, NoteCoordinate> keyCache_;
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

	protected Map<Object, NoteCoordinate> getKeyCache() {
		if (keyCache_ == null) {
			keyCache_ = new FastMap<Object, NoteCoordinate>().atomic();
		}
		return keyCache_;
	}

	public DElementStore() {
		addType(ViewVertex.class);
		addType(CategoryVertex.class);
	}

	@Override
	public void uncache(final Element elem) {
		getElementCache().remove(elem);
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
		//		for (Class<?> subtype : type.getClasses()) {
		//			if (subtype.isInterface()) {
		//				addType(subtype);
		//			}
		//		}
	}

	@Override
	public void removeType(final Class<?> type) {
		List<Class<?>> types = getTypes();
		types.remove(type);
		for (Class<?> subtype : type.getClasses()) {
			removeType(subtype);
		}
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
			if (delegateKey_ == null) {
				delegate_ = graph.getStoreDelegate(this, provisionalDelegateKey_);
			} else {
				delegate_ = graph.getStoreDelegate(this);
			}
		}
		return delegate_;
	}

	@Override
	public Object getProxyStoreDelegate() {
		if (proxyDelegate_ == null) {
			org.openntf.domino.graph2.DConfiguration config = getConfiguration();
			org.openntf.domino.graph2.DGraph graph = config.getGraph();
			if (proxyDelegateKey_ == null) {
				proxyDelegate_ = graph.getProxyStoreDelegate(this, provisionalProxyDelegateKey_);
			} else {
				proxyDelegate_ = graph.getProxyStoreDelegate(this);
			}
		}
		return proxyDelegate_;
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
	public void setProxyStoreDelegate(final Object store) {
		proxyDelegate_ = store;
		if (store instanceof Database) {
			String rid = ((Database) store).getReplicaID();
			proxyDelegateKey_ = NoteCoordinate.Utils.getLongFromReplid(rid);
		} else if (store instanceof DElementStore) {
			proxyDelegateKey_ = ((DElementStore) store).getStoreKey();
		} else {
			//TODO Some other mechanism to get the key
		}
	}

	@Override
	public Long getStoreKey() {
		if (delegateKey_ == null) {
			if (provisionalDelegateKey_ != null) {
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
		}
		return delegateKey_;
	}

	@Override
	public Long getProxyStoreKey() {
		if (proxyDelegateKey_ == null) {
			if (provisionalProxyDelegateKey_ != null) {
				//				System.out.println("Setting up proxy key using provisional delegate " + provisionalProxyDelegateKey_);
				Object delegate = getProxyStoreDelegate();
				if (delegate != null) {
					if (delegate instanceof Database) {
						String rid = ((Database) delegate).getReplicaID();
						proxyDelegateKey_ = NoteCoordinate.Utils.getLongFromReplid(rid);
						//						System.out.println("Proxy database is " + System.identityHashCode(this) + " at "
						//								+ ((Database) delegate).getApiPath() + " (" + ((Database) getStoreDelegate()).getApiPath() + ")");
						//						Throwable t = new Throwable();
						//						t.printStackTrace();
					} else {
						//TODO Some other mechanism to get the key
					}
				}
			}
		}
		return proxyDelegateKey_;
	}

	@Override
	public void setStoreKey(final Long storeKey) {
		delegateKey_ = storeKey;
	}

	@Override
	public void setStoreKey(final CharSequence storeKey) {
		if (DominoUtils.isReplicaId(storeKey)) {
			setStoreKey(NoteCoordinate.Utils.getLongFromReplid(storeKey));
		} else {
			if (storeKey.toString().contains("!!")) {
				provisionalDelegateKey_ = storeKey;
			} else {
				provisionalDelegateKey_ = storeKey;
			}
		}
	}

	@Override
	public void setProxyStoreKey(final Long storeKey) {
		proxyDelegateKey_ = storeKey;

	}

	@Override
	public void setProxyStoreKey(final CharSequence storeKey) {
		if (DominoUtils.isReplicaId(storeKey)) {
			setProxyStoreKey(NoteCoordinate.Utils.getLongFromReplid(storeKey));
		} else {
			if (storeKey.toString().contains("!!")) {
				this.provisionalProxyDelegateKey_ = storeKey;
			} else {
				this.provisionalProxyDelegateKey_ = storeKey;
			}
		}
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
				if (isProxied()) {
					result = setupProxy(delegate, (Serializable) id);
				} else {
					DVertex vertex = new DVertex(getConfiguration().getGraph(), delegate);
					result = vertex;
				}
				getElementCache().put(result.getId(), result);
				getKeyCache().put(id, (NoteCoordinate) result.getId()); //TODO shouldn't force NoteCoordinate, but it covers all current use cases
				getConfiguration().getGraph().startTransaction(result);
			}
		}
		return result;
	}

	@Override
	public Vertex getVertex(final Object id) {
		return (Vertex) getElement(id, Vertex.class);
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
			Object localkey = localizeKey(id);
			Map<String, Object> delegate = addElementDelegate(localkey, Edge.class);
			if (delegate != null) {
				DEdge edge = new DEdge(getConfiguration().getGraph(), delegate);
				result = edge;
				getElementCache().put(result.getId(), result);
				getKeyCache().put(id, (NoteCoordinate) result.getId()); //TODO shouldn't force NoteCoordinate, but it covers all current use cases
				getConfiguration().getGraph().startTransaction(result);
			}
		}
		return result;
	}

	protected Element getCachedElement(final Object id, final Class<? extends Element> type) {
		if (id == null)
			return null;
		Element chk = getElementCache().get(id);
		if (chk == null) {
			NoteCoordinate nc = getKeyCache().get(id);
			if (nc != null) {
				chk = getElementCache().get(nc);
			}
		}
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

	public Element getElement(final Object id, final Class<? extends Element> type) throws IllegalStateException {
		Element result = null;
		Element chk = getCachedElement(id, Element.class);
		if (chk != null) {
			result = chk;
		} else {
			boolean isProxiedSource = false;
			Object delegate = null;
			if (id instanceof NoteCoordinate && isProxied()) {
				NoteCoordinate nc = (NoteCoordinate) id;
				if (getStoreKey().equals(nc.getReplicaLong())) {
					//this is a request for a vertex out of the proxied store, not the proxy itself
					//					System.out.println("TEMP DEBUG Attempted to get a proxied source vertex at " + nc.toString());
					//					Throwable t = new Throwable();
					//					t.printStackTrace();
					Database db = (Database) getStoreDelegate();
					String unid = nc.getUNID();
					delegate = db.getDocumentByUNID(unid, true);
					if (delegate != null) {
						isProxiedSource = true;
						//						System.out.println("TEMP DEBUG Got our original source from the proxy's proxiedid");
					}
				}
			}
			if (delegate == null) {
				Object localkey = localizeKey(id);
				try {
					delegate = findElementDelegate(localkey, type);
				} catch (IllegalStateException ise) {
					ise.printStackTrace();
				}
			}
			if (delegate != null) {
				if (delegate instanceof Element) {
					result = (Element) delegate;
				} else if (delegate instanceof Document) {
					if (isProxiedSource) {
						DVertex vertex = new DVertex(getConfiguration().getGraph(), (Document) delegate);
						result = vertex;
					} else if (((Document) delegate).hasItem("$Index")) {
						DVertex vertex = new DVertex(getConfiguration().getGraph(), (Document) delegate);
						result = vertex;
					} else {
						Object typeChk = ((Document) delegate).get(org.openntf.domino.graph2.DElement.TYPE_FIELD);
						String strChk = org.openntf.domino.utils.TypeUtils.toString(typeChk);
						if (org.openntf.domino.graph2.DVertex.GRAPH_TYPE_VALUE.equals(strChk)) {
							DVertex vertex = new DVertex(getConfiguration().getGraph(), (Document) delegate);
							result = vertex;
						} else if (org.openntf.domino.graph2.DEdge.GRAPH_TYPE_VALUE.equals(strChk)) {
							DEdge edge = new DEdge(getConfiguration().getGraph(), (Document) delegate);
							result = edge;
						} else {
							throw new IllegalStateException("Delegate for key " + id.toString() + " returned a "
									+ delegate.getClass().getName() + " with a type identifier of " + strChk);
						}
					}
				} else if (delegate instanceof ViewEntry) {
					if (id instanceof ViewEntryCoordinate) {
						ViewEntryCoordinate vec = (ViewEntryCoordinate) id;
						String entryType = vec.getEntryType();
						if (entryType.startsWith("E")) {
							DEdge edge = new DEntryEdge(getConfiguration().getGraph(), (ViewEntry) delegate, (ViewEntryCoordinate) id, this);
							result = edge;
						} else if (entryType.startsWith("V")) {
							ViewEntry entry = (ViewEntry) delegate;
							if (entry.isCategory()) {
								Map<String, Object> delegateMap = new LinkedHashMap<String, Object>();
								delegateMap.put("value", entry.getCategoryValue());
								delegateMap.put("position", entry.getPosition());
								delegateMap.put("noteid", entry.getNoteID());
								DCategoryVertex vertex = new DCategoryVertex(getConfiguration().getGraph(), delegateMap,
										entry.getParentView());
								vertex.delegateKey_ = vec;
								result = vertex;
							}
						}
					}
				}
				getElementCache().put(result.getId(), result);
				getKeyCache().put(id, (NoteCoordinate) result.getId()); //TODO shouldn't force NoteCoordinate, but it covers all current use cases
			}
		}
		return result;
	}

	@Override
	public Element getElement(final Object id) throws IllegalStateException {
		return getElement(id, Element.class);
	}

	@Override
	public Edge getEdge(final Object id) {
		return (Edge) getElement(id, Edge.class);
	}

	@Override
	public void removeEdge(final Edge edge) {
		if (edge instanceof DEdge) {
			org.openntf.domino.ViewEntry.class.equals(((DEdge) edge).getDelegateType());
			throw new UnsupportedOperationException("ViewEntry edges cannot be removed.");
		}
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
		getElementCache().remove(element);
	}

	@Override
	public boolean isProxied() {
		//		Object pDelegate = getProxyStoreDelegate();
		boolean result = getProxyStoreKey() != null;
		//		if (!result) {
		//			System.out.println("Checking proxy status on store " + System.identityHashCode(this) + " in thread "
		//					+ System.identityHashCode(Thread.currentThread()));
		//			Throwable t = new Throwable();
		//			t.printStackTrace();
		//		}
		return result;
	}

	protected Serializable getKeyProperty(final Map<String, Object> delegate) {
		Object result = null;
		String key = "";
		//TODO NTF map determination of key property
		//first find out if this delegate has a key property
		if (!Strings.isNullOrEmpty(key)) {
			result = delegate.get(key);
		}
		return (Serializable) result;
	}

	public DProxyVertex wrapProxiedVertex(final Map<String, Object> delegate) {
		//		System.out.println("Wrapping a proxied vertex...");
		DVertex vertex = new DVertex(getConfiguration().getGraph(), delegate);
		Object pDelegate = getProxyStoreDelegate();
		Serializable pKey = null;
		Map<String, Object> proxyDelegate = null;
		pKey = getKeyProperty(delegate);
		if (pKey == null) {
			if (delegate instanceof Document) {
				pKey = ((Document) delegate).getMetaversalID();
			} else {
				//TODO future implementations...
			}
		}
		if (pDelegate instanceof Database) {
			Database pDb = ((Database) pDelegate);
			//			System.out.println("Creating proxy version in database " + pDb.getApiPath());
			Document pDoc = pDb.getDocumentWithKey(pKey, true);
			proxyDelegate = pDoc;
		} else {
			//TODO future implementations...
		}
		DProxyVertex result = new DProxyVertex(getConfiguration().getGraph(), vertex, proxyDelegate);
		return result;
	}

	protected DProxyVertex setupProxy(final Object proxy, final Serializable originalKey) {
		//		System.out.println("TEMP DEBUG Setting up proxy vertex");
		DProxyVertex result = new DProxyVertex(getConfiguration().getGraph(), (Map<String, Object>) proxy);
		Object rawpid = result.getProxiedId();

		if (rawpid == null) {
			CustomProxyResolver resolver = getCustomProxyResolver();
			if (resolver == null) {
				System.err.println("No default resolver implemented yet");
				throw new UnimplementedException("Non-custom resolution of proxied vertices not yet implemented");
			} else {
				//				System.out.println("Resolving using a " + resolver.getClass().getName());
				Map<String, Object> originalDelegate = resolver.getOriginalDelegate(originalKey);

				if (originalDelegate instanceof Document) {
					String pid = ((Document) originalDelegate).getMetaversalID();
					NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(pid);
					//					System.out.println("Setting up proxy with id " + nc.toString());
					try {
						result.setProxiedId(nc);
					} catch (Throwable t) {
						t.printStackTrace();
					}
				} else {
					if (originalDelegate == null) {
						System.out.println("No original delegate found for key " + String.valueOf(originalKey));
					} else {
						System.err.println("original delegate returned a " + originalDelegate.getClass().getName());
					}
				}
			}
		} else {

		}
		//		System.out.println("Setup a proxy with an id of " + String.valueOf(result.getProxiedId()));
		return result;
	}

	@Override
	public Object findElementDelegate(final Object delegateKey, final Class<? extends Element> type) throws IllegalStateException,
	IllegalArgumentException {
		Object result = null;
		Object del = null;
		if (isProxied()) {
			del = getProxyStoreDelegate();
			//			System.out.println("TEMP DEBUG Retrieving from proxied store");
		} else {
			del = getStoreDelegate();
		}
		if (del instanceof Database) {
			Database db = (Database) del;
			if (delegateKey instanceof Serializable) {
				if (delegateKey instanceof ViewEntryCoordinate) {
					result = ((ViewEntryCoordinate) delegateKey).getViewEntry();
				} else if (delegateKey instanceof NoteCoordinate) {
					String unid = ((NoteCoordinate) delegateKey).getUNID();
					result = db.getDocumentWithKey(unid, false);
					//					System.out.println("Retrieved result using NoteCoordinate with unid " + unid);
				} else if (delegateKey instanceof CharSequence) {
					String skey = ((CharSequence) delegateKey).toString();
					if (skey.length() > 50) {
						String prefix = skey.subSequence(0, 2).toString();
						String mid = skey.subSequence(2, 50).toString();
						if ((prefix.equals("EC") || prefix.equals("ED") || prefix.equals("ET") || prefix.equals("EU"))
								&& DominoUtils.isMetaversalId(mid)) {
							ViewEntryCoordinate vec = ViewEntryCoordinate.Utils.getViewEntryCoordinate(skey);
							result = vec.getViewEntry();
						} else if ((prefix.equals("VC") || prefix.equals("VD") || prefix.equals("VT") || prefix.equals("VU"))
								&& DominoUtils.isMetaversalId(mid)) {
							ViewEntryCoordinate vec = ViewEntryCoordinate.Utils.getViewEntryCoordinate(skey);
							result = vec.getViewEntry();
						}
					}
				}
				if (result == null) {
					result = db.getDocumentWithKey((Serializable) delegateKey, false);
				}
				if (result != null) {
					//					System.out.println("TEMP DEBUG Checking for proxy status for type " + type.getSimpleName());
					boolean isProxy = false;
					if (result instanceof Document) {
						isProxy = ((Document) result).hasItem(DProxyVertex.PROXY_ITEM);
					}
					if (isProxied() && (isProxy || Vertex.class.isAssignableFrom(type) || VertexFrame.class.isAssignableFrom(type))) {
						//						System.out.println("TEMP DEBUG Setting up a proxied vertex...");
						result = setupProxy(result, (Serializable) delegateKey);
					}
				}
			} else {
				throw new IllegalArgumentException("Cannot find a delegate with a key of type " + delegateKey.getClass().getName());
			}
		} else {
			//TODO NTF alternative strategies...
		}
		//		if (result == null && Vertex.class.isAssignableFrom(type)) {
		//			if (isProxied()) {
		//				Object proxyDel = getProxyStoreDelegate();
		//				if (proxyDel instanceof Database) {
		//					Database db = (Database) proxyDel;
		//					if (delegateKey instanceof NoteCoordinate) {
		//						String unid = ((NoteCoordinate) delegateKey).getUNID();
		//						result = db.getDocumentWithKey(unid, false);
		//					} else {
		//						result = db.getDocumentWithKey((Serializable) delegateKey, false);
		//					}
		//				} else if (proxyDel instanceof DElementStore) {
		//					result = ((DElementStore) proxyDel).findElementDelegate(delegateKey, type);
		//				} else {
		//					//TODO NTF unimplemented
		//				}
		//			}
		//		}
		//		if (result == null) {
		//			System.out
		//			.println("Request with delegatekey " + delegateKey.getClass().getName() + " (" + delegateKey + ")" + " returned null");
		//		}
		if (result != null) {
			if (type.equals(Element.class)) {
				return result;
			}
			if (result instanceof org.openntf.domino.ViewEntry) {
				return result;
			}
			if (delegateKey instanceof NoteCoordinate && ((NoteCoordinate) delegateKey).isView()) {
				return result;
			}

			Object typeChk = ((Map<String, Object>) result).get(org.openntf.domino.graph2.DElement.TYPE_FIELD);
			String strChk = org.openntf.domino.utils.TypeUtils.toString(typeChk);
			if (org.openntf.domino.utils.Strings.isBlankString(strChk)) {//NTF new delegate
				if (Vertex.class.isAssignableFrom(type)) {
					((Map<String, Object>) result).put(org.openntf.domino.graph2.DElement.TYPE_FIELD,
							org.openntf.domino.graph2.DVertex.GRAPH_TYPE_VALUE);
				} else if (Edge.class.isAssignableFrom(type)) {
					((Map<String, Object>) result).put(org.openntf.domino.graph2.DElement.TYPE_FIELD,
							org.openntf.domino.graph2.DEdge.GRAPH_TYPE_VALUE);
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
		}
		return result;
	}

	@Override
	public void removeElementDelegate(final Element element) {

	}

	protected Map<String, Object> addElementDelegate(final Object delegateKey, final Class<? extends Element> type) {
		Map<String, Object> result = null;
		//		System.out.println("Adding a " + type.getName() + " to Element Store " + System.identityHashCode(this));
		Object del = null;
		if (isProxied()) {
			del = getProxyStoreDelegate();
		} else {
			del = getStoreDelegate();
		}
		if (del instanceof Database) {
			Database db = (Database) del;
			if (delegateKey == null || delegateKey instanceof Serializable) {
				result = db.getDocumentWithKey((Serializable) delegateKey, true);
			} else {
				throw new IllegalArgumentException("Cannot add a delegate with a key of type " + delegateKey.getClass().getName());
			}
		} else {
			if (del == null) {
				throw new IllegalStateException("Store delegate is null!");
			} else {
				throw new IllegalStateException("Store delegate is not a Database. It is a " + del.getClass().getName());
			}
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
	public DVertexIterable getVertices() {
		return new DVertexIterable(this, getVertexIds());
	}

	@Override
	public DEdgeIterable getEdges() {
		return new DEdgeIterable(this, getEdgeIds());
	}

	@Override
	public DElementIterable getElements(final String formulaFilter) {
		return new DElementIterable(this, getElementIds(formulaFilter));
	}

	@Override
	public DVertexIterable getVertices(final String formulaFilter) {
		return new DVertexIterable(this, getVertexIds(formulaFilter));
	}

	@Override
	public DEdgeIterable getEdges(final String formulaFilter) {
		return new DEdgeIterable(this, getEdgeIds(formulaFilter));
	}

	@Override
	public DVertexIterable getVertices(final String key, final Object value) {
		String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getVertexFormula(key, value);
		return getVertices(formulaFilter);
	}

	@Override
	public DEdgeIterable getEdges(final String key, final Object value) {
		String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getEdgeFormula(key, value);
		return getEdges(formulaFilter);
	}

	@Override
	public DElementIterable getElements(final String key, final Object value) {
		String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getEdgeFormula(key, value);
		return getElements(formulaFilter);
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

	protected List<NoteCoordinate> getVertexIds() {
		FastTable<NoteCoordinate> result = new FastTable<NoteCoordinate>();
		Object raw = getStoreDelegate();
		if (raw instanceof Database) {
			Database db = (Database) raw;
			NoteCollection nc = db.createNoteCollection(false);
			nc.setSelectDocuments(true);
			nc.setSelectionFormula(org.openntf.domino.graph2.DVertex.FORMULA_FILTER);
			nc.buildCollection();
			for (String noteid : nc) {
				result.add(NoteCoordinate.Utils.getNoteCoordinate(nc, noteid));
			}
		} else {
			//TODO NTF implement alternative
			throw new IllegalStateException("Non-Domino implementations not yet available");
		}
		return result;
	}

	protected List<NoteCoordinate> getVertexIds(final String formulaFilter) {
		FastTable<NoteCoordinate> result = new FastTable<NoteCoordinate>();
		Object raw = getStoreDelegate();
		if (raw instanceof Database) {
			Database db = (Database) raw;
			NoteCollection nc = db.createNoteCollection(false);
			nc.setSelectDocuments(true);
			nc.setSelectionFormula(formulaFilter);
			nc.buildCollection();
			for (String noteid : nc) {
				result.add(NoteCoordinate.Utils.getNoteCoordinate(nc, noteid));
			}
		} else {
			//TODO NTF implement alternative
			throw new IllegalStateException("Non-Domino implementations not yet available");
		}
		return result;
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

	protected List<NoteCoordinate> getEdgeIds() {
		FastTable<NoteCoordinate> result = new FastTable<NoteCoordinate>();
		Object raw = getStoreDelegate();
		if (raw instanceof Database) {
			Database db = (Database) raw;
			NoteCollection nc = db.createNoteCollection(false);
			nc.setSelectDocuments(true);
			nc.setSelectionFormula(org.openntf.domino.graph2.DEdge.FORMULA_FILTER);
			nc.buildCollection();
			for (String noteid : nc) {
				result.add(NoteCoordinate.Utils.getNoteCoordinate(nc, noteid));
			}
		} else {
			//TODO NTF implement alternative
			throw new IllegalStateException("Non-Domino implementations not yet available");
		}
		return result;
	}

	protected List<NoteCoordinate> getEdgeIds(final String formulaFilter) {
		FastTable<NoteCoordinate> result = new FastTable<NoteCoordinate>();
		Object raw = getStoreDelegate();
		if (raw instanceof Database) {
			Database db = (Database) raw;
			NoteCollection nc = db.createNoteCollection(false);
			nc.setSelectDocuments(true);
			nc.setSelectionFormula(formulaFilter);
			nc.buildCollection();
			for (String noteid : nc) {
				result.add(NoteCoordinate.Utils.getNoteCoordinate(nc, noteid));
			}
		} else {
			//TODO NTF implement alternative
			throw new IllegalStateException("Non-Domino implementations not yet available");
		}
		return result;
	}

	protected List<NoteCoordinate> getElementIds() {
		FastTable<NoteCoordinate> result = new FastTable<NoteCoordinate>();
		Object raw = getStoreDelegate();
		if (raw instanceof Database) {
			Database db = (Database) raw;
			NoteCollection nc = db.createNoteCollection(false);
			nc.setSelectDocuments(true);
			nc.setSelectionFormula(org.openntf.domino.graph2.DEdge.FORMULA_FILTER + " | "
					+ org.openntf.domino.graph2.DVertex.FORMULA_FILTER);
			nc.buildCollection();
			for (String noteid : nc) {
				result.add(NoteCoordinate.Utils.getNoteCoordinate(nc, noteid));
			}
		} else {
			//TODO NTF implement alternative
			throw new IllegalStateException("Non-Domino implementations not yet available");
		}
		return result;
	}

	protected List<NoteCoordinate> getElementIds(final String formulaFilter) {
		FastTable<NoteCoordinate> result = new FastTable<NoteCoordinate>();
		Object raw = getStoreDelegate();
		if (raw instanceof Database) {
			Database db = (Database) raw;
			NoteCollection nc = db.createNoteCollection(false);
			nc.setSelectDocuments(true);
			nc.setSelectionFormula(formulaFilter);
			nc.buildCollection();
			for (String noteid : nc) {
				result.add(NoteCoordinate.Utils.getNoteCoordinate(nc, noteid));
			}
		} else {
			//TODO NTF implement alternative
			throw new IllegalStateException("Non-Domino implementations not yet available");
		}
		return result;
	}

	@Override
	public DIdentityFactory getIdentityFactory() {
		return identityFactory_;
	}

	@Override
	public void setIdentityFactory(final DIdentityFactory identFactory) {
		identityFactory_ = identFactory;
	}

	@Override
	public Object getIdentity(final Class<?> type, final Object context, final Object... args) {
		DIdentityFactory factory = getIdentityFactory();
		if (factory != null) {
			return factory.getId(this, type, context, args);
		}
		return null;
	}

	@Override
	public void setCustomProxyResolver(final CustomProxyResolver resolver) {
		proxyResolver_ = resolver;
		resolver.setProxiedElementStore(this);
	}

	protected CustomProxyResolver getCustomProxyResolver() {
		return proxyResolver_;
	}

}
