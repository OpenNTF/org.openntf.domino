package org.openntf.domino.graph2.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.NoteCollection;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.big.ViewEntryCoordinate;
import org.openntf.domino.design.impl.DesignFactory;
import org.openntf.domino.exceptions.UnimplementedException;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.graph2.DIdentityFactory;
import org.openntf.domino.graph2.builtin.CategoryVertex;
import org.openntf.domino.graph2.builtin.DbInfoVertex;
import org.openntf.domino.graph2.builtin.ViewVertex;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

import javolution.util.FastTable;

public class DElementStore implements org.openntf.domino.graph2.DElementStore {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DElementStore.class.getName());

	public static class ElementStoreCacheLoader extends CacheLoader<NoteCoordinate, Element> {
		protected DElementStore parent_;

		protected ElementStoreCacheLoader(final DElementStore parent) {
			parent_ = parent;
		}

		protected Object getStoreDelegate(final Object key) {
			Object result = parent_.getStoreDelegate();
			if (parent_.isProxied()) {
				NoteCoordinate nc = null;
				if (key instanceof NoteCoordinate) {
					nc = (NoteCoordinate) key;
				} else if (key instanceof CharSequence) {
					nc = NoteCoordinate.Utils.getNoteCoordinate((CharSequence) key);
				}
				if (nc != null) {
					long dbkey = nc.getReplicaLong();
					if (parent_.getProxyStoreKey().equals(dbkey)) {
						result = parent_.getProxyStoreDelegate();
					}
					//			System.out.println("TEMP DEBUG Retrieving from proxied store");
				}
			}
			return result;
		}

		protected Object getElementDelegate(final Database db, final Object key) {
			Object result = null;
			db.getAncestorSession().setFixEnable(Fixes.MIME_BLOCK_ITEM_INTERFACE, false);
			if (key instanceof Serializable) {
				if (key instanceof ViewEntryCoordinate) {
					result = ((ViewEntryCoordinate) key).getViewEntry();
				} else if (key instanceof NoteCoordinate) {
					if (((NoteCoordinate) key).isIcon()) {
						result = db.getIconNote();
						if (result == null) {	//NTF the database doesn't have an icon
							result = db.getACLNote();
							//							System.out.println("TEMP DEBUG icon request is returning document "
							//									+ (result == null ? "null" : ((Document) result).getUniversalID()));
						}
					} else {
						String unid = ((NoteCoordinate) key).getUNID();
						result = db.getDocumentWithKey(unid, false);
						if (result != null && ((Document) result).isNewNote()) {
							((Document) result).save();
						}
					}
					//					System.out.println("Retrieved result using NoteCoordinate with unid " + unid);
				} else if (key instanceof CharSequence) {
					String skey = ((CharSequence) key).toString();
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
					//					System.out.println("TEMP DEBUG getting document by key " + (key));
					result = db.getDocumentWithKey((Serializable) key, false);
					if (result != null && ((Document) result).isNewNote()) {
						((Document) result).save();
					}
				}
				if (result != null) {
					//					System.out.println("TEMP DEBUG Checking for proxy status for type " + type.getSimpleName());
					boolean isProxy = false;
					if (result instanceof Document) {
						isProxy = ((Document) result).hasItem(DProxyVertex.PROXY_ITEM);
					}
					if (parent_.isProxied()) {
						if (result instanceof Document) {
							//							System.out.println("TEMP DEBUG from ElementStoreCacheLoader: ElementStore "
							//									+ ((Database) parent_.getStoreDelegate()).getApiPath()
							//									+ " is proxied. Setting up proxy on delegate...");
							result = parent_.setupProxy(result, (Serializable) key);
						} else if (result instanceof ViewEntry) {
							//NO OP
						} else {
							//							System.out.println("TEMP DEBUG from ElementStoreCacheLoader: ElementStore "
							//									+ ((Database) parent_.getStoreDelegate()).getApiPath() + " is proxied. But our result is a "
							//									+ result.getClass().getName());
						}
					} else {
						//						System.out.println(
						//								"TEMP DEBUG ElementStore " + ((Database) parent_.getStoreDelegate()).getApiPath() + " is not proxied.");

					}
				}
			} else {
				if (key != null) {
					//					System.out.println(
					//							"WARNING: Unknown delegatekey of type " + key.getClass().getName() + ". Creating a brand new delegate.");
				}
				//null is a perfectly valid key, since it means we want to let the system assign it.
				result = db.createDocument();
			}
			return result;
		}

		public Element toElement(final Object delegate, final Object id, final boolean isProxiedSource) {
			Element result = null;
			if (delegate instanceof Element) {
				result = (Element) delegate;
			} else if (delegate instanceof Document) {
				if (DesignFactory.isView((Document) delegate)) {
					DVertex vertex = new DVertex(parent_.getConfiguration().getGraph(), (Document) delegate);
					result = vertex;
				} else if (DesignFactory.isIcon((Document) delegate) || DesignFactory.isACL((Document) delegate)) {
					DVertex vertex = new DVertex(parent_.getConfiguration().getGraph(), (Document) delegate);
					result = vertex;
					//				} else if (((Document) delegate).hasItem(DProxyVertex.PROXY_ITEM)) {
					//					DVertex vertex = new DVertex(parent_.getConfiguration().getGraph(), (Document) delegate);
					//					result = parent_.setupProxy(vertex, null);
				} else if (isProxiedSource) {
					DVertex vertex = new DVertex(parent_.getConfiguration().getGraph(), (Document) delegate);
					if (parent_.isReverseProxied()) {
						result = parent_.setupReverseProxy(vertex);
						//						System.out.println("TEMP DEBUG from toElement: Created a new reverse proxied vertex for original "
						//								+ vertex.getId().toString() + " at proxy " + result.getId().toString());
					} else {
						result = vertex;
					}
				} else {
					Object typeChk = ((Document) delegate).get(org.openntf.domino.graph2.DElement.TYPE_FIELD);
					String strChk = org.openntf.domino.utils.TypeUtils.toString(typeChk);
					if (org.openntf.domino.graph2.DEdge.GRAPH_TYPE_VALUE.equals(strChk)) {
						DEdge edge = new DEdge(parent_.getConfiguration().getGraph(), (Document) delegate);
						result = edge;
					} else {
						DVertex vertex = new DVertex(parent_.getConfiguration().getGraph(), (Document) delegate);
						result = vertex;
					}
				}
			} else if (delegate instanceof ViewEntry) {
				if (id instanceof ViewEntryCoordinate) {
					ViewEntryCoordinate vec = (ViewEntryCoordinate) id;
					String entryType = vec.getEntryType();
					if (entryType.startsWith("E")) {
						DEdge edge = new DEntryEdge(parent_.getConfiguration().getGraph(), (ViewEntry) delegate, (ViewEntryCoordinate) id,
								parent_);
						result = edge;
					} else if (entryType.startsWith("V")) {
						ViewEntry entry = (ViewEntry) delegate;
						if (entry.isCategory()) {
							Map<String, Object> delegateMap = new LinkedHashMap<String, Object>();
							delegateMap.put("value", entry.getCategoryValue());
							delegateMap.put("position", entry.getPosition());
							delegateMap.put("noteid", entry.getNoteID());
							DCategoryVertex vertex = new DCategoryVertex(parent_.getConfiguration().getGraph(), delegateMap,
									entry.getParentView());
							vertex.delegateKey_ = vec;
							result = vertex;
						} else {
							System.out.println("TEMP DEBUG ViewVertex entry is not a category");
						}
					}
				} else {
					System.out.println("TEMP DEBUG ViewEntry's id is not a ViewEntryCoordinate. It's a " + id.getClass().getName());
				}
			}
			return result;
		}

		@Override
		public Element load(NoteCoordinate key) throws Exception {
			Element result = null;
			Object delegate = null;
			boolean isProxiedSource = false;
			if (key instanceof NoteCoordinate && (parent_.isProxied() || parent_.isReverseProxied())
					&& !(key instanceof ViewEntryCoordinate || key.getX() == 0)) {
				//				System.out.println("TEMP DEBUG in proxy branch...");
				NoteCoordinate nc = key;
				if (parent_.getStoreKey().equals(nc.getReplicaLong())) {
					//this is a request for a vertex out of the proxied store, not the proxy itself
					Database sourcedb = (Database) parent_.getStoreDelegate();
					if (sourcedb != null) {
						String unid = nc.getUNID();
						delegate = sourcedb.getDocumentByUNID(unid, true);
						if (delegate != null) {
							isProxiedSource = true;
						}
					} else {
						System.out.println("EXCEPTION Delegate database for an ElementStore is not available. The key requested is " + key
								+ " and the expect path to the store is " + String.valueOf(parent_.provisionalDelegateKey_));
						return null;
					}
				}
			}
			Object del = null;
			if (key instanceof CharSequence) {
				if (!org.openntf.domino.big.NoteCoordinate.Utils.isNoteCoordinate((CharSequence) key)) {
					if (parent_.isProxied()) {
						del = parent_.getProxyStoreDelegate();
					} else {
						del = parent_.getStoreDelegate();
					}
					//					if (del == null) {
					//						if (parent_.isProxied()) {
					//							System.out.println("Delegate is null for a proxied element store");
					//						} else {
					//							System.out.println("Delegate is null for a regular element store");
					//						}
					//					}
					String unid = DominoUtils.toUnid(key).toLowerCase();
					String replid = org.openntf.domino.big.NoteCoordinate.Utils.getReplidFromLong(parent_.getStoreKey());
					key = org.openntf.domino.big.NoteCoordinate.Utils.getNoteCoordinate(replid, unid);
				}
			}
			if (delegate == null) {
				if (del == null) {
					del = getStoreDelegate(key);
				}
				if (del instanceof Database) {
					Object localkey = parent_.localizeKey(key);
					delegate = getElementDelegate((Database) del, localkey);
				} else {
					String replid = org.openntf.domino.big.NoteCoordinate.Utils.getReplidFromLong(parent_.getStoreKey());
					String typelist = "";
					for (Class type : parent_.types_) {
						typelist = typelist + "," + type.getName();
					}
					throw new IllegalStateException("ElementStore delegate is not a Database; it's a "
							+ (del == null ? "null" : del.getClass().getName())
							+ ". We don't handle this case yet. The key for the element store is " + replid + " and it houses " + typelist);
					//TODO NTF alternative strategies...
				}
			}
			if (delegate != null) {
				result = toElement(delegate, key, isProxiedSource);
			}
			return result;
		}

	}

	private List<Class<?>> types_;
	private Object delegate_;
	private Long delegateKey_;
	private Object provisionalDelegateKey_;
	private Object proxyDelegate_;
	private Long proxyDelegateKey_;
	private boolean isProxyStore_ = false;

	public boolean isProxyStore() {
		return isProxyStore_;
	}

	public void setProxyStore(final boolean isProxyStore) {
		isProxyStore_ = isProxyStore;
	}

	public boolean isReverseProxyStore() {
		return isReverseProxyStore_;
	}

	public void setReverseProxyStore(final boolean isReverseProxyStore) {
		isReverseProxyStore_ = isReverseProxyStore;
	}

	private Object provisionalProxyDelegateKey_;

	private CustomProxyResolver proxyResolver_;
	private Object reverseProxyDelegate_;
	private Long reverseProxyDelegateKey_;
	private boolean isReverseProxyStore_ = false;
	private Object provisionalReverseProxyDelegateKey_;
	private CustomProxyResolver reverseProxyResolver_;
	private DIdentityFactory identityFactory_;
	private transient Map<Object, NoteCoordinate> keyMap_;
	//	private transient Map<Object, Element> elementCache_OLD;
	private LoadingCache<NoteCoordinate, Element> elementCache_;
	private transient org.openntf.domino.graph2.DConfiguration configuration_;

	protected void setTypes(final List<Class<?>> types) {
		types_ = types;
	}

	protected LoadingCache<NoteCoordinate, Element> getElementCache() {
		if (elementCache_ == null) {
			elementCache_ = CacheBuilder.newBuilder().maximumSize(16384).expireAfterWrite(20, TimeUnit.HOURS)
					.build(new ElementStoreCacheLoader(this));
		}
		return elementCache_;
	}

	//	protected Map<Object, Element> getElementCache_OLD() {
	//		if (elementCache_ == null) {
	//			elementCache_ = new FastMap<Object, Element>().atomic();
	//		}
	//		return elementCache_;
	//	}

	protected Map<Object, NoteCoordinate> getKeyMap() {
		if (keyMap_ == null) {
			keyMap_ = new ConcurrentHashMap<Object, NoteCoordinate>();
		}
		return keyMap_;
	}

	public DElementStore() {
		addType(ViewVertex.class);
		addType(CategoryVertex.class);
		addType(DbInfoVertex.class);
	}

	@Override
	public void uncache(final Element elem) {
		//		getElementCache().remove(elem);
		getElementCache().invalidate(elem.getId());
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
		if (cl == null) {
			cl = Thread.currentThread().getContextClassLoader();
		}
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
	public Object getReverseProxyStoreDelegate() {
		if (reverseProxyDelegate_ == null) {
			org.openntf.domino.graph2.DConfiguration config = getConfiguration();
			org.openntf.domino.graph2.DGraph graph = config.getGraph();
			if (reverseProxyDelegateKey_ == null) {
				reverseProxyDelegate_ = graph.getProxyStoreDelegate(this, provisionalReverseProxyDelegateKey_);
			} else {
				reverseProxyDelegate_ = graph.getProxyStoreDelegate(this);
			}
		}
		return reverseProxyDelegate_;
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
		if (store instanceof Database) {
			String rid = ((Database) store).getReplicaID();
			proxyDelegateKey_ = NoteCoordinate.Utils.getLongFromReplid(rid);
			proxyDelegate_ = store;
		} else if (store instanceof DElementStore) {
			proxyDelegateKey_ = ((DElementStore) store).getStoreKey();
			proxyDelegate_ = ((DElementStore) store).getStoreDelegate();
			//			System.out.println("TEMP DEBUG Setting proxy store delegate for element store " + ((Database) getStoreDelegate()).getApiPath()
			//					+ " to " + (proxyDelegate_ == null ? "null" : ((Database) proxyDelegate_).getApiPath()));
		} else {
			//TODO Some other mechanism to get the key
		}
	}

	@Override
	public void setReverseProxyStoreDelegate(final Object store) {
		if (store instanceof Database) {
			String rid = ((Database) store).getReplicaID();
			reverseProxyDelegateKey_ = NoteCoordinate.Utils.getLongFromReplid(rid);
			reverseProxyDelegate_ = store;
		} else if (store instanceof DElementStore) {
			reverseProxyDelegateKey_ = ((DElementStore) store).getStoreKey();
			reverseProxyDelegate_ = ((DElementStore) store).getStoreDelegate();
			//			System.out.println(
			//					"TEMP DEBUG Setting reverse proxy store delegate for element store " + ((Database) getStoreDelegate()).getApiPath()
			//							+ " to " + (reverseProxyDelegate_ == null ? "null" : ((Database) reverseProxyDelegate_).getApiPath()));
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
	public Long getReverseProxyStoreKey() {
		if (reverseProxyDelegateKey_ == null) {
			if (provisionalReverseProxyDelegateKey_ != null) {
				//				System.out.println("Setting up proxy key using provisional delegate " + provisionalProxyDelegateKey_);
				Object delegate = getProxyStoreDelegate();
				if (delegate != null) {
					if (delegate instanceof Database) {
						String rid = ((Database) delegate).getReplicaID();
						reverseProxyDelegateKey_ = NoteCoordinate.Utils.getLongFromReplid(rid);
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
		return reverseProxyDelegateKey_;
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

	@Override
	public void setReverseProxyStoreKey(final Long storeKey) {
		reverseProxyDelegateKey_ = storeKey;

	}

	@Override
	public void setReverseProxyStoreKey(final CharSequence storeKey) {
		if (DominoUtils.isReplicaId(storeKey)) {
			setProxyStoreKey(NoteCoordinate.Utils.getLongFromReplid(storeKey));
		} else {
			if (storeKey.toString().contains("!!")) {
				this.provisionalReverseProxyDelegateKey_ = storeKey;
			} else {
				this.provisionalReverseProxyDelegateKey_ = storeKey;
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
	public Vertex addVertex(final Object id, final boolean temporary) {
		Vertex result = null;
		if (id != null) {
			Element chk = getElement(id);
			if (chk != null && chk instanceof Vertex) {
				return (Vertex) chk;
			}
		}
		Object localkey = localizeKey(id);
		Map<String, Object> delegate = addElementDelegate(localkey, Vertex.class, temporary);
		if (delegate != null) {
			if (isProxied()) {
				//				System.out.println("TEMP DEBUG from addVertex: ElementStore " + ((Database) getStoreDelegate()).getApiPath()
				//						+ " is proxied. Setting up proxy on delegate...");
				result = setupProxy(delegate, (Serializable) id);
			} else {
				DVertex vertex = new DVertex(getConfiguration().getGraph(), delegate);
				result = vertex;
			}
			if (!temporary) {
				getElementCache().put((NoteCoordinate) result.getId(), result);
				if (id != null) {
					getKeyMap().put(id, (NoteCoordinate) result.getId()); //TODO shouldn't force NoteCoordinate, but it covers all current use cases
				}
				getConfiguration().getGraph().startTransaction(result);
			}
		}
		return result;
	}

	@Override
	public Vertex addVertex(final Object id) {
		return addVertex(id, false);
	}

	//	@Override
	//	public Vertex getVertex(final Object id) {
	//		return (Vertex) getElement(id, Vertex.class);
	//	}

	@Override
	public void removeVertex(final Vertex vertex) {
		startTransaction(vertex);
		DVertex dv = (DVertex) vertex;
		Iterable<Edge> edges = dv.getEdges(Direction.BOTH);
		if (edges != null) {
			int i = 0;
			try {
				if (edges instanceof List) {
					ListIterator<Edge> li = ((List<Edge>) edges).listIterator();
					while (li.hasNext()) {
						Edge e = li.next();
						if (e != null) {
							getConfiguration().getGraph().removeEdge(e, dv);
						}
						i++;
					}
				} else {
					for (Edge edge : edges) {
						getConfiguration().getGraph().removeEdge(edge, dv);
						i++;
					}
				}
			} catch (Exception e) {
				System.err.println("Problem with a " + dv.getClass().getName() + " with id " + dv.getId() + " on edgelist "
						+ (edges == null ? "null" : edges.getClass().getName()) + " on iteration " + i);
				DominoUtils.handleException(e);
			}
		}
		removeCache(vertex);
		dv._remove();
	}

	@Override
	public Edge addEdge(final Object id) {
		Edge result = null;
		if (id != null) {
			Element chk = getElement(id);
			if (chk != null && chk instanceof Vertex) {
				return (Edge) chk;
			}
		}
		Object localkey = localizeKey(id);
		Map<String, Object> delegate = addElementDelegate(localkey, Edge.class, false);
		if (delegate != null) {
			DEdge edge = new DEdge(getConfiguration().getGraph(), delegate);
			result = edge;
			getElementCache().put((NoteCoordinate) result.getId(), result);
			if (id != null) {
				getKeyMap().put(id, (NoteCoordinate) result.getId()); //TODO shouldn't force NoteCoordinate, but it covers all current use cases
			}
			getConfiguration().getGraph().startTransaction(result);
		}
		return result;
	}

	//	protected Element getCachedElement(final Object id, final Class<? extends Element> type) {
	//		if (id == null)
	//			return null;
	//		Element chk = getElementCache().get(id);
	//		if (chk == null) {
	//			NoteCoordinate nc = getKeyMap().get(id);
	//			if (nc != null) {
	//				chk = getElementCache().get(nc);
	//			}
	//		}
	//		if (chk != null) {
	//			if (type.isAssignableFrom(chk.getClass())) {
	//				return chk;
	//			} else {
	//				throw new IllegalStateException("Requested id of " + String.valueOf(id) + " is already in cache but is a "
	//						+ chk.getClass().getName());
	//			}
	//		}
	//		return null;
	//	}

	protected NoteCoordinate normalizeId(final Serializable id) {
		Object del = null;
		if (id instanceof NoteCoordinate) {
			return (NoteCoordinate) id;
		}
		if (id instanceof CharSequence) {
			if (!org.openntf.domino.big.NoteCoordinate.Utils.isNoteCoordinate((CharSequence) id)) {
				if (isProxied()) {
					del = getProxyStoreDelegate();
				} else {
					del = getStoreDelegate();
				}
				String unid = DominoUtils.toUnid(id).toLowerCase();
				String replid = ((Database) del).getReplicaID().toLowerCase();
				return org.openntf.domino.big.NoteCoordinate.Utils.getNoteCoordinate(replid, unid);
			} else {
				return NoteCoordinate.Utils.getNoteCoordinate((CharSequence) id);
			}
		}
		return null;
	}

	@Override
	public Element getElement(final Object id) throws IllegalStateException {
		try {
			//			if (isProxied()) {
			//				System.out.println("Getting a proxied element for id: " + String.valueOf(id));
			//			}
			NoteCoordinate nc = normalizeId((Serializable) id);
			Element result = null;
			//			if (nc.getReplicaId().equalsIgnoreCase("852582F7007073B5")) {
			//				System.out.println("Getting a demo document " + nc + " from the element cache");
			//				result = getElementCache().get(nc);
			//				if (result != null) {
			//					System.out.println(
			//							"Result was loaded from cache with id " + nc + " and cache now has " + getElementCache().size() + " elements");
			//
			//				}
			//			} else {
			result = getElementCache().get(nc);
			//			}
			//			if (isProxied()) {
			//				if (result instanceof DProxyVertex) {
			//					NoteCoordinate proxyid = ((DProxyVertex) result).getProxiedId();
			//					getElementCache().put(proxyid, result);
			//				}
			//			}
			//			System.out.println("TEMP DEBUG requesting element for id " + id.getClass().getSimpleName() + ": " + String.valueOf(id)
			//					+ " from element store " + System.identityHashCode(this) + " using element cache "
			//					+ System.identityHashCode(getElementCache()) + " resulting in a " + result.getClass().getSimpleName() + ": "
			//					+ System.identityHashCode(result));
			return result;
		} catch (InvalidCacheLoadException icle) {
			//			System.out.println(
			//					"TEMP DEBUG invalidCacheLoad for id " + String.valueOf(id) + " from element store " + System.identityHashCode(this));
			return null;
		} catch (UncheckedExecutionException uee) {
			Throwable cause = uee.getCause();
			if (cause != null && cause instanceof UserAccessException) {
				throw new UserAccessException(cause.getMessage(), cause);
			} else {
				throw uee;
			}
		} catch (UserAccessException uae) {
			throw uae;
		} catch (Throwable t) {
			throw new IllegalStateException("Unable to retrieve id " + String.valueOf(id), t);
		}
	}

	/*public Element getElement_OLD(final Object id, final Class<? extends Element> type) throws IllegalStateException {
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
						Database db = (Database) getStoreDelegate();
						String unid = nc.getUNID();
						delegate = db.getDocumentByUNID(unid, true);
						if (delegate != null) {
							isProxiedSource = true;
						}
					}
				}
				if (delegate == null) {
					Object localkey = localizeKey(id);
					delegate = findElementDelegate(localkey);
				}
				if (delegate != null) {
					if (delegate instanceof Element) {
						result = (Element) delegate;
					} else if (delegate instanceof Document) {
						if (isProxiedSource) {
							DVertex vertex = new DVertex(getConfiguration().getGraph(), (Document) delegate);
							result = vertex;
						} else if (((Document) delegate).hasItem("$Index") || ((Document) delegate).hasItem("$Collation")) {
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
								DVertex vertex = new DVertex(getConfiguration().getGraph(), (Document) delegate);
								result = vertex;
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
								} else {
									System.out.println("TEMP DEBUG ViewVertex entry is not a category");
								}
							}
						} else {
							System.out.println("TEMP DEBUG ViewEntry's id is not a ViewEntryCoordinate. It's a " + id.getClass().getName());
						}
					}
					getElementCache().put(result.getId(), result);
					getKeyMap().put(id, (NoteCoordinate) result.getId()); //TODO shouldn't force NoteCoordinate, but it covers all current use cases
				}
			}
			return result;
		}*/

	//	@Override
	//	public Element getElement(final Object id) throws IllegalStateException {
	//		return getElement(id, Element.class);
	//	}

	//	@Override
	//	public Edge getEdge(final Object id) {
	//		return (Edge) getElement(id, Edge.class);
	//	}

	@Override
	public void removeEdge(final Edge edge) {
		if (edge instanceof DEdge) {
			if (org.openntf.domino.ViewEntry.class.equals(((DEdge) edge).getDelegateType())) {
				throw new UnsupportedOperationException("ViewEntry edges cannot be removed.");
			}
		}
		startTransaction(edge);
		Vertex in = edge.getVertex(Direction.IN);
		if (in != null) {
			((DVertex) in).removeEdge(edge);
		}
		Vertex out = edge.getVertex(Direction.OUT);
		if (out != null) {
			((DVertex) out).removeEdge(edge);
		}
		removeCache(edge);
		((DEdge) edge)._remove();
	}

	@Override
	public void removeEdge(final Edge edge, final Vertex removingVertex) {
		if (edge instanceof DEdge) {
			if (org.openntf.domino.ViewEntry.class.equals(((DEdge) edge).getDelegateType())) {
				throw new UnsupportedOperationException("ViewEntry edges cannot be removed.");
			}
		}
		startTransaction(edge);
		Vertex in = edge.getVertex(Direction.IN);
		if (!in.equals(removingVertex)) {
			((DVertex) in).removeEdge(edge);
			removeCache(in);
		}
		Vertex out = edge.getVertex(Direction.OUT);
		if (!out.equals(removingVertex)) {
			((DVertex) out).removeEdge(edge);
			removeCache(out);
		}
		removeCache(edge);
		((DEdge) edge)._remove();
	}

	private void startTransaction(final Element element) {
		getConfiguration().getGraph().startTransaction(element);
	}

	private void removeCache(final Element element) {
		Object key = element.getId();
		getElementCache().invalidate(key);
		getKeyMap().remove(key);
	}

	@Override
	public boolean isProxied() {
		Object pDelegate = getProxyStoreDelegate();
		Long l = this.getProxyStoreKey();
		boolean result = (l != null || pDelegate != null);
		return result;
	}

	@Override
	public boolean isReverseProxied() {
		Object pDelegate = getReverseProxyStoreDelegate();
		Long l = this.getReverseProxyStoreKey();
		boolean result = (l != null || pDelegate != null);
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
			if (pDoc != null && pDoc.isNewNote()) {
				pDoc.save();
			}
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
		if (rawpid != null) {
			if (result.getProxyDelegate() == null) {
				DElement elem = (DElement) getConfiguration().getGraph().getElement(rawpid);
				if (elem == null) {
					rawpid = null;
				}
			}
		}
		if (rawpid == null) {
			CustomProxyResolver resolver = getCustomProxyResolver();
			if (resolver == null) {
				System.err.println("No default resolver implemented yet");
				throw new UnimplementedException("Generics resolution of proxied vertices not yet implemented");
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
						//						System.out.println("No original delegate found for key " + String.valueOf(originalKey));
					} else {
						//						System.err.println("original delegate returned a " + originalDelegate.getClass().getName());
					}
				}
			}
		} else {

		}
		//		System.out.println("Setup a proxy with an id of " + String.valueOf(result.getProxiedId()));
		return result;
	}

	protected DProxyVertex setupReverseProxy(final DVertex original) {
		if (original == null) {
			return null;
		}
		DProxyVertex result = null;
		try {
			CustomProxyResolver resolver = getCustomReverseProxyResolver();
			Map<String, Object> proxyDel = null;
			if (resolver == null) {
				Object rpDel = getReverseProxyStoreDelegate();
				if (rpDel instanceof Database) {
					proxyDel = ((Database) rpDel).getDocumentWithKey((Serializable) original.getId(), true);
				} else {
					System.out.println("WARNING reverse proxy delegate is not a Database. It's a "
							+ (rpDel == null ? "null" : rpDel.getClass().getName()));
				}
			} else {
				proxyDel = resolver.getProxyDelegate((Serializable) original.getId());
			}
			result = new DProxyVertex(getConfiguration().getGraph(), proxyDel);
			DElementStore proxyStore = (DElementStore) result.getElementStore();
			proxyStore.getElementCache().put((NoteCoordinate) result.getId(), result);
			result.setProxyDelegate(original);
			result.setReverseProxy(true);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return result;
	}

	//	@Override
	@Override
	public Object findElementDelegate(final Object delegateKey) throws IllegalStateException, IllegalArgumentException {
		Object result = null;
		Object del = null;
		del = getStoreDelegate();
		if (isProxied()) {
			//			System.out.println("TEMP DEBUG Retrieving from proxied store for key " + String.valueOf(delegateKey));
			NoteCoordinate nc = null;
			if (delegateKey instanceof NoteCoordinate) {
				nc = (NoteCoordinate) delegateKey;
			} else if (delegateKey instanceof CharSequence) {
				nc = NoteCoordinate.Utils.getNoteCoordinate((CharSequence) delegateKey);
			}
			if (nc != null) {
				long dbkey = nc.getReplicaLong();
				if (getProxyStoreKey().equals(dbkey)) {
					del = getProxyStoreDelegate();
				}
			}
		}
		if (del instanceof Database) {
			Database db = (Database) del;
			boolean isDeletedDoc;
			db.getAncestorSession().setFixEnable(Fixes.MIME_BLOCK_ITEM_INTERFACE, false);
			if (delegateKey instanceof Serializable) {
				if (delegateKey instanceof ViewEntryCoordinate) {
					result = ((ViewEntryCoordinate) delegateKey).getViewEntry();
				} else if (delegateKey instanceof NoteCoordinate) {
					String unid = ((NoteCoordinate) delegateKey).getUNID();
					try {
						result = db.getDocumentWithKey(unid, false);
					} catch (UserAccessException uae) {
						//this is normal and acceptable. Don't report.
					} catch (RuntimeException re) {
						throw re;
					}
					if (result != null) {
						if (((Document) result).isDeleted()) {
							isDeletedDoc = true;
							result = null;
						}
					}
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
					if (result != null) {
						if (((Document) result).isDeleted()) {
							isDeletedDoc = true;
							result = null;
						}
					}
				}
				if (result != null) {
					//					System.out.println("TEMP DEBUG Checking for proxy status for type " + type.getSimpleName());
					//					boolean isProxy = false;
					//					if (result instanceof Document) {
					//						isProxy = ((Document) result).hasItem(DProxyVertex.PROXY_ITEM);
					//					}
					if (isProxied()) {
						//						System.out.println("TEMP DEBUG from findElementDelegate: ElementStore "
						//								+ ((Database) getStoreDelegate()).getApiPath() + "is proxied. Setting up proxy on delegate...");
						result = setupProxy(result, (Serializable) delegateKey);
					} else {
						//						System.out.println("TEMP DEBUG ElementStore " + ((Database) getStoreDelegate()).getApiPath() + " is not proxied.");
					}
				}
			} else {
				if (delegateKey != null) {
					System.out.println("WARNING: Unknown delegatekey of type " + delegateKey.getClass().getName()
							+ ". Creating a brand new delegate.");
				}
				//null is a perfectly valid key, since it means we want to let the system assign it.
				result = db.createDocument();
				//				throw new IllegalArgumentException("Cannot find a delegate with a key of type "
				//						+ (delegateKey == null ? "null" : delegateKey.getClass().getName()));
			}
		} else {
			throw new IllegalStateException("ElementStore delegate is not a Database; it's a "
					+ (del == null ? "null" : del.getClass().getName()) + ". We don't handle this case yet.");
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
		//		if (result != null) {
		//			setTypeProperty(result, type, delegateKey);
		//		}
		return result;
	}

	protected void setTypeProperty(final Object result, final Class<?> type, final Object delegateKey) {
		if (type.equals(Element.class)) {
			return;
		}
		if (result instanceof org.openntf.domino.ViewEntry) {
			return;
		}
		if (delegateKey instanceof NoteCoordinate && ((NoteCoordinate) delegateKey).isView()) {
			return;
		}
		if (delegateKey instanceof NoteCoordinate && ((NoteCoordinate) delegateKey).isIcon()) {
			return;
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
				throw new IllegalStateException(
						"Requested id of " + String.valueOf(delegateKey) + " results in a delegate with a graph type of " + strChk);
			}
		}
	}

	@Override
	public void removeElementDelegate(final Element element) {
		if (element instanceof DElement) {
			Object del = ((DElement) element).getDelegate();
			if (del instanceof Document) {
				((Document) del).remove(true);
			} else {
				System.err.println("Cannot remove a delegate of type " + (del == null ? "null" : del.getClass().getName()));
				Throwable t = new Throwable();
				t.printStackTrace();
			}
			((DElement) element).setDelegate(null);
		} else {
			System.err.println("Cannot remove a delegate for element of type " + (element == null ? "null" : element.getClass().getName()));
		}
	}

	protected Map<String, Object> addElementDelegate(final Object delegateKey, final Class<? extends Element> type,
			final boolean temporary) {
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
				if (result != null && ((Document) result).isNewNote()) {
					if (!temporary) {
						//						((Document) result).save();
					}
				}
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
					throw new IllegalStateException(
							"Requested id of " + String.valueOf(delegateKey) + " results in a delegate with a graph type of " + strChk);
				}
			}
		} else {
			throw new IllegalStateException(
					"Requested id of " + String.valueOf(delegateKey) + " results in a null delegate and therefore cannot be persisted.");
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
		String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getElementFormula(key, value);
		return getElements(formulaFilter);
	}

	//	@Override
	//	public Set<Vertex> getCachedVertices() {
	//		FastSet<Vertex> result = new FastSet<Vertex>();
	//		for (Element elem : getElementCache().values()) {
	//			if (elem instanceof Vertex) {
	//				result.add((Vertex) elem);
	//			}
	//		}
	//		return result.unmodifiable();
	//	}

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

	//	@Override
	//	public Set<Edge> getCachedEdges() {
	//		FastSet<Edge> result = new FastSet<Edge>();
	//		for (Element elem : getElementCache().values()) {
	//			if (elem instanceof Edge) {
	//				result.add((Edge) elem);
	//			}
	//		}
	//		return result.unmodifiable();
	//	}

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
		List<NoteCoordinate> result = null;
		Object raw = getStoreDelegate();
		if (raw instanceof Database) {
			Database db = (Database) raw;
			NoteCollection nc = db.createNoteCollection(false);
			nc.setSelectDocuments(true);
			nc.setSelectionFormula(
					org.openntf.domino.graph2.DEdge.FORMULA_FILTER + " | " + org.openntf.domino.graph2.DVertex.FORMULA_FILTER);
			nc.buildCollection();
			result = Lists.newArrayListWithCapacity(nc.getCount());
			for (String noteid : nc) {
				result.add(NoteCoordinate.Utils.getNoteCoordinate(nc, noteid));
			}
		} else {
			//TODO NTF implement alternative
			throw new IllegalStateException("Non-Domino implementations not yet available");
		}
		return ImmutableList.copyOf(result);
	}

	protected List<NoteCoordinate> getElementIds(final String formulaFilter) {
		List<NoteCoordinate> result = null;
		Object raw = getStoreDelegate();
		if (raw instanceof Database) {
			Database db = (Database) raw;
			NoteCollection nc = db.createNoteCollection(false);
			nc.setSelectDocuments(true);
			nc.setSelectionFormula(formulaFilter);
			nc.buildCollection();
			if (nc.getCount() == 0) {
				raw = this.getProxyStoreDelegate();
				if (raw != null) {
					db = (Database) raw;
					nc = db.createNoteCollection(false);
					nc.setSelectDocuments(true);
					nc.setSelectionFormula(formulaFilter);
					nc.buildCollection();
				}
			}
			result = Lists.newArrayListWithCapacity(nc.getCount());
			for (String noteid : nc) {
				result.add(NoteCoordinate.Utils.getNoteCoordinate(nc, noteid));
			}
		} else {
			//TODO NTF implement alternative
			throw new IllegalStateException("Non-Domino implementations not yet available");
		}
		return ImmutableList.copyOf(result);
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

	@Override
	public void setCustomReverseProxyResolver(final CustomProxyResolver resolver) {
		reverseProxyResolver_ = resolver;
		resolver.setProxiedElementStore(this);
	}

	@Override
	public CustomProxyResolver getCustomProxyResolver() {
		return proxyResolver_;
	}

	@Override
	public CustomProxyResolver getCustomReverseProxyResolver() {
		return reverseProxyResolver_;
	}

	@Override
	public void flushCache() {
		keyMap_ = null;
		elementCache_ = null;
	}

	@Override
	public void flushCache(final String id) {
		//		if (id.startsWith("852582F7007073B5".toLowerCase())) {
		//			System.out.println("TEMP DEBUG flushing cache for metaversalid: " + id);
		//		}
		try {
			if (keyMap_ != null && keyMap_.containsKey(id)) {
				keyMap_.remove(id);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		try {
			if (this.getElementCache() != null) {
				NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id);
				Object chk = getElementCache().getIfPresent(nc);
				if (chk != null) {
					//					getElementCache().refresh(nc);
					getElementCache().invalidate(nc);
					//					System.out.println("TEMP DEBUG Invalidated id " + nc + " in Element Store");
				} else {
					//					if (id.startsWith("852582F7007073B5".toLowerCase())) {
					//						System.out.println("TEMP DEBUG id " + nc + " not found in cache");
					//						System.out.println("TEMP DEBUG cache has " + getElementCache().size() + " elements");
					//						if (getElementCache().size() < 6) {
					//							//							getElementCache().asMap()
					//						}
					//					}
				}
			} else {
				System.out.println("Element Cache not available!");
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
