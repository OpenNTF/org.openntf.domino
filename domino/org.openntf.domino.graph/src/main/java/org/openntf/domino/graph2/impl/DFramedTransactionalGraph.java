/**
 * Copyright © 2013-2023 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openntf.domino.graph2.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.big.NoteCoordinate;
import org.openntf.domino.design.impl.DesignFactory;
import org.openntf.domino.exceptions.UserAccessException;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.extmgr.AbstractEMBridgeSubscriber;
import org.openntf.domino.extmgr.EMBridgeEventFactory;
import org.openntf.domino.extmgr.EMBridgeMessageQueue;
import org.openntf.domino.extmgr.events.EMEventIds;
import org.openntf.domino.extmgr.events.document.UpdateExtendedEvent;
import org.openntf.domino.graph2.DElementStore;
import org.openntf.domino.graph2.DKeyResolver;
import org.openntf.domino.graph2.annotations.FramedEdgeList;
import org.openntf.domino.graph2.annotations.FramedVertexList;
import org.openntf.domino.graph2.builtin.CategoryVertex;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DbInfoVertex;
import org.openntf.domino.graph2.builtin.Eventable;
import org.openntf.domino.graph2.builtin.ViewVertex;
import org.openntf.domino.graph2.builtin.search.IndexScanner;
import org.openntf.domino.graph2.impl.DConfiguration.DTypeManager;
import org.openntf.domino.graph2.impl.DConfiguration.DTypeRegistry;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FrameInitializer;
import com.tinkerpop.frames.FramedGraphConfiguration;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.javahandler.JavaFrameInitializer;

@SuppressWarnings({"unchecked", "rawtypes", "nls"})
public class DFramedTransactionalGraph<T extends TransactionalGraph> extends FramedTransactionalGraph<T> {
	private Cache<Object, Object> framedElementCache_;
	private IndexScanner indexScanner_;

	public static class CacheMonitor extends AbstractEMBridgeSubscriber {
		private DFramedTransactionalGraph parentGraph_;

		protected CacheMonitor(final DFramedTransactionalGraph parent) {
			parentGraph_ = parent;
		}

		@Override
		public Collection<EMEventIds> getSubscribedEventIds() {
			List<EMEventIds> result = new ArrayList<EMEventIds>();
			System.out.println("Registering Graph extension manager events");
			result.add(EMEventIds.EM_NSFNOTEUPDATEXTENDED);
			result.add(EMEventIds.EM_NSFNOTEUPDATE);
			result.add(EMEventIds.EM_NSFNOTEDELETE);
			return result;
		}

		@Override
		public void handleMessage(final EMEventIds eventid, final String eventMessage) {
			if (EMEventIds.EM_NSFNOTEUPDATEXTENDED.equals(eventid) || EMEventIds.EM_NSFNOTEUPDATE.equals(eventid)) {
				try {
					UpdateExtendedEvent event = new UpdateExtendedEvent();
					EMBridgeEventFactory.parseEventBuffer(eventMessage, event);

					Session session = Factory.getSession(SessionType.NATIVE);
					session.setFixEnable(Fixes.FORCE_HEX_LOWER_CASE, true);
					Database database = session.getDatabase("", event.getDbPath());
					if (database != null) {
						String unid = database.getUNID(event.getNoteId());
						if (unid != null) {
							String mid = database.getReplicaID() + unid;
							parentGraph_.flushCache(mid.toLowerCase());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (EMEventIds.EM_NSFNOTEDELETE.equals(eventid)) {
				try {
					UpdateExtendedEvent event = new UpdateExtendedEvent();
					EMBridgeEventFactory.parseEventBuffer(eventMessage, event);

					Session session = Factory.getSession(SessionType.NATIVE);
					session.setFixEnable(Fixes.FORCE_HEX_LOWER_CASE, true);
					Database database = session.getDatabase("", event.getDbPath());
					if (database != null) {
						String unid = database.getUNID(event.getNoteId());
						if (unid != null) {
							String mid = database.getReplicaID() + unid;
							parentGraph_.flushCache(mid.toLowerCase());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class DefaultKeyResolver implements DKeyResolver {
		private Collection<Class<?>> types_;
		private DFramedTransactionalGraph dgraph_;

		public DefaultKeyResolver(final DFramedTransactionalGraph graph) {
			dgraph_ = graph;
			//			System.out.println("TEMP DEBUG Created new DefaultKeyResolver");
		}

		@Override
		public Collection<Class<?>> getTypes() {
			if (types_ == null) {
				DConfiguration config = (DConfiguration) dgraph_.getConfig();
				types_ = new HashSet<Class<?>>();
				types_.add(config.getDefaultVertexFrameType());
			}
			return types_;
		}

		@Override
		public Element handleMissingKey(final Class<?> type, final Object key) {
			//			System.out.println("TEMP DEBUG default resolver handling missing key");
			if (String.valueOf(key).equals("NEW")) {
				//				System.out.println("TEMP DEBUG handling NEW case for type " + type.getName());
				Object rawFrame = dgraph_.addVertex(null, type, true);
				//				System.out.println("TEMP DEBUG added a " + rawFrame.getClass().getName());
				if (rawFrame instanceof VertexFrame) {
					//					System.out.println("TEMP DEBUG returning vertex");
					return ((VertexFrame) rawFrame).asVertex();
				}
			}
			return null;

		}

		@Override
		public NoteCoordinate resolveKey(final Class<?> type, final Object key) {
			DElementStore store = dgraph_.getElementStore(type);
			long pid = store.getStoreKey();
			String rid = NoteCoordinate.Utils.getReplidFromLong(pid);
			String id = DominoUtils.toUnid(String.valueOf(key));
			NoteCoordinate result = NoteCoordinate.Utils.getNoteCoordinate(rid, id);
			return result;
		}
	}

	public static class FrameLoadCallable implements Callable<Object> {
		protected final DFramedTransactionalGraph parent_;
		protected Object id_;
		protected Class<?> kind_;
		protected Class<?> delegateType_;

		public FrameLoadCallable(final DFramedTransactionalGraph parent, final Object id, final Class<?> kind,
				final Class<?> delegateType) {
			parent_ = parent;
			id_ = id;
			kind_ = kind;
			delegateType_ = delegateType;
		}

		@Override
		public Object call() throws Exception {
			Object result = null;
			DGraph base = (DGraph) parent_.getBaseGraph();
			org.openntf.domino.graph2.DElementStore store = null;
			if (id_ instanceof NoteCoordinate) {
				store = base.findElementStore(id_);
			} else {
				//				System.out.println("Resolving an id of type " + (id_ == null ? "null" : id_.getClass().getName()));
				String typeid = parent_.getTypedId(id_);
				if (typeid == null) {
					store = base.findElementStore(kind_);
				} else {
					store = base.findElementStore(typeid);
				}
			}
			Element elem = store.getElement(id_);
			if (null == elem) {
				result = null;
			} else if (elem instanceof Edge) {
				result = parent_.frame((Edge) elem, kind_);
			} else if (elem instanceof Vertex) {
				result = parent_.frame((Vertex) elem, kind_);
			} else {
				throw new IllegalStateException("Key " + String.valueOf(id_) + " returned an element of type " + elem.getClass().getName());
			}
			return result;
		}

	}

	protected Cache<Object, Object> getFramedElementCache() {
		if (framedElementCache_ == null) {
			framedElementCache_ = CacheBuilder.newBuilder().maximumSize(250000).expireAfterWrite(20, TimeUnit.HOURS).build();
		}
		return framedElementCache_;
	}

	//	protected <F> F getCachedElement(final Element element, final Class<F> kind) {
	//		if (element == null)
	//			return null;
	//		//		System.out.println("TEMP DEBUG checking cache for element " + element.getId().toString());
	//		Object chk = getFramedElementCache().get(element);
	//		if (chk != null) {
	//			if (kind.isAssignableFrom(chk.getClass())) {
	//				//				System.out.println("TEMP DEBUG returning from cache for element " + element.getId().toString());
	//				return (F) chk;
	//			} else {
	//				throw new IllegalStateException("Requested id of " + String.valueOf(element) + " is already in cache but is a "
	//						+ chk.getClass().getName());
	//			}
	//		}
	//		return null;
	//	}

	@Override
	public void removeEdge(final Edge edge) {
		IndexScanner is = getIndexScanner();
		if (is != null) {
			String mid = String.valueOf(edge.getId());
			is.removeDocument(mid);
		}
		removeCache(edge);
		((DGraph) getBaseGraph()).removeEdge(edge);
	}

	@Override
	public void removeVertex(final Vertex vertex) {
		IndexScanner is = getIndexScanner();
		if (is != null) {
			String mid = String.valueOf(vertex.getId());
			is.removeDocument(mid);
		}
		removeCache(vertex);
		((DGraph) getBaseGraph()).removeVertex(vertex);
	}

	public void removeEdgeFrame(final EdgeFrame edge) {
		removeEdge(edge.asEdge());
	}

	public void removeVertexFrame(final VertexFrame removingVertex) {
		removeVertex(removingVertex.asVertex());
	}

	void removeCache(final Object object) {
		Object key = null;
		if (object instanceof Edge) {
			key = ((Edge) object).getId();
		} else if (object instanceof Vertex) {
			key = ((Vertex) object).getId();
		} else if (object instanceof EdgeFrame) {
			key = ((EdgeFrame) object).asEdge().getId();
		} else if (object instanceof VertexFrame) {
			key = ((VertexFrame) object).asVertex().getId();
		}
		if (key != null) {
			getFramedElementCache().invalidate(key);
		}
	}

	public class FramedElementIterable<F> implements Iterable<T> {
		protected final Class<T> kind;
		protected final Iterable<Element> iterable;
		protected final DFramedTransactionalGraph<? extends Graph> framedGraph;

		public FramedElementIterable(final DFramedTransactionalGraph<? extends Graph> framedGraph, final Iterable<Element> iterable,
				final Class<T> kind) {
			this.framedGraph = framedGraph;
			this.iterable = iterable;
			this.kind = kind;
		}

		@Override
		public Iterator<T> iterator() {
			return new Iterator<T>() {
				private Iterator<Element> iterator = iterable.iterator();

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}

				@Override
				public boolean hasNext() {
					return this.iterator.hasNext();
				}

				@Override
				public T next() {
					Element e = this.iterator.next();
					return framedGraph.getElement(e.getId(), kind);
				}
			};
		}
	}

	public DFramedTransactionalGraph(final T baseGraph, final FramedGraphConfiguration config) {
		super(baseGraph, config);
		CacheMonitor monitor = new CacheMonitor(this);
		EMBridgeMessageQueue.addSubscriber(monitor);
	}

	public DTypeRegistry getTypeRegistry() {
		Graph graph = this.getBaseGraph();
		if (graph instanceof DGraph) {
			DConfiguration config = (DConfiguration) ((DGraph) graph).getConfiguration();
			return config.getTypeRegistry();
		}
		return null;
	}

	public DTypeManager getTypeManager() {
		Graph graph = this.getBaseGraph();
		if (graph instanceof DGraph) {
			DConfiguration config = (DConfiguration) ((DGraph) graph).getConfiguration();
			return config.getTypeManager();
		}
		return null;
	}

	private String getTypedId(final Object id) {
		String result = null;
		if (id != null && id instanceof String) {
			String idStr = (String) id;
			if (idStr.length() == 16) {
				if (DominoUtils.isReplicaId(idStr)) {
					//					result = idStr;
				}
			} else if (idStr.length() > 16) {
				if (idStr.length() == 32) {
					if (DominoUtils.isUnid(idStr)) {
						result = null;
					}
				} else {
					String prefix = idStr.substring(0, 16);
					if (DominoUtils.isReplicaId(prefix)) {
						result = prefix;
					}
				}
			}
		}
		return result;
	}

	public <F> F addVertex(final Object id, final Class<F> kind, final boolean temporary) {
		if (id != null && id instanceof NoteCoordinate) {
			Object cacheChk = getElement(id, kind);
			if (cacheChk != null) {
				return (F) cacheChk;
			}
		}
		DGraph base = (DGraph) this.getBaseGraph();
		org.openntf.domino.graph2.DElementStore store = null;
		if (kind != null) {
			store = base.findElementStore(kind);
		}
		if (store == null) {
			//			System.out.println("TEMP DEBUG store was null for type " + kind.getName());
			if (id instanceof NoteCoordinate) {
				store = base.findElementStore(id);
			} else {
				String typeid = getTypedId(id);
				if (typeid == null) {
					store = base.getDefaultElementStore();
				} else {
					store = base.findElementStore(typeid);
				}
			}
		} else {
			//			System.out.println("TEMP DEBUG adding to store " + ((Database) store.getStoreDelegate()).getApiPath());
		}

		Vertex vertex = store.addVertex(id, temporary);
		String typeValue = ((DConfiguration) this.getConfig()).getTypeValue(kind);
		//		System.out.println("TEMP DEBUG Creating new instance of " + kind.getName() + " with typeValue of " + typeValue);
		vertex.setProperty("form", typeValue);
		F result = frame(vertex, kind, temporary);
		if (result instanceof Eventable) {

		}
		if (!temporary) {
			getFramedElementCache().put(vertex.getId(), result);
		}
		return result;
	}

	@Override
	public <F> F addVertex(final Object id, final Class<F> kind) {
		return addVertex(id, kind, false);
	}

	public <F> F getVertex(final Class<F> kind, final Object context, final Object... args) {
		DGraph base = (DGraph) this.getBaseGraph();
		org.openntf.domino.graph2.DElementStore store = base.findElementStore(kind);
		Object id = store.getIdentity(kind, context, args);
		return getVertex(id, kind);
	}

	public <F> Iterable<F> getElements(final Class<F> kind) {
		org.openntf.domino.graph2.DElementStore store = null;
		DGraph base = (DGraph) this.getBaseGraph();
		store = base.findElementStore(kind);
		if (store != null) {
			String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getFramedElementFormula(kind);
			Iterable<Element> elements = (org.openntf.domino.graph2.impl.DElementIterable) store.getElements(formulaFilter);
			//			if (elements instanceof List) {
			//				int size = ((List) elements).size();
			//				System.out.println("TEMP DEBUG Found a list of size " + size + " for kind " + kind.getName());
			//			}
			return this.frameElements(elements, kind);
		} else {
			//			System.out.println("TEMP DEBUG Unable to find an element store for type " + kind.getName());
			return null;
		}
	}

	protected Class<?> getClassFromName(final String classname) {
		Class<?> chkClass = getTypeRegistry().getType(VertexFrame.class, classname);
		if (chkClass == null) {
			chkClass = getTypeRegistry().getType(EdgeFrame.class, classname);
		}
		if (chkClass == null) {
			chkClass = getTypeRegistry().findClassByName(classname);
		}
		if (chkClass == null) {
			//			System.out.println("TEMP DEBUG Unable to find an element store for type " + classname);
		}
		return chkClass;
	}

	public <F> Iterable<F> getElements(final String classname) {
		org.openntf.domino.graph2.DElementStore store = null;
		DGraph base = (DGraph) this.getBaseGraph();
		Class<?> chkClass = getClassFromName(classname);
		if (chkClass != null) {
			store = base.findElementStore(chkClass);
			if (store != null) {
				String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getFramedElementFormula(chkClass);
				Iterable<Element> elements = (org.openntf.domino.graph2.impl.DElementIterable) store.getElements(formulaFilter);
				if (elements instanceof List) {
//					int size = ((List) elements).size();
					//					System.out.println("TEMP DEBUG Found a list of size " + size + " for kind " + classname);
				}
				return this.frameElements(elements, (Class<F>) chkClass);
			} else {
				//				System.out.println("TEMP DEBUG Unable to find an element store for type " + classname);
				return null;
			}
		} else {
			throw new IllegalArgumentException("Class " + classname + " not registered in graph");
		}
	}

	public <F> Iterable<F> getFilteredElements(final String classname, final List<CharSequence> keys,
			final List<CaseInsensitiveString> values) {
		//		System.out.println("Getting a filtered list of elements of type " + classname);
		org.openntf.domino.graph2.DElementStore store = null;
		DGraph base = (DGraph) this.getBaseGraph();
		Class<?> chkClass = getClassFromName(classname);
		if (chkClass != null) {
			store = base.findElementStore(chkClass);
			if (store != null) {
				List<String> keystrs = CaseInsensitiveString.toStrings(keys);
				List<Object> valobj = new ArrayList<Object>(values);
				String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getFramedElementFormula(keystrs, valobj, chkClass);
				long startTime = new Date().getTime();
				Iterable<Element> elements = (org.openntf.domino.graph2.impl.DElementIterable) store.getElements(formulaFilter);
				long endTime = new Date().getTime();
				System.out
						.println("TEMP DEBUG Retrieved elements for type " + classname + " in " + (endTime - startTime) + "ms. Framing...");
				return this.frameElements(elements, null);
			} else {
				return null;
			}
		} else {
			throw new IllegalArgumentException("Class " + classname + " not registered in graph");
		}
	}

	public <F> Iterable<F> getFilteredElementsPartial(final String classname, final List<CharSequence> keys,
			final List<CaseInsensitiveString> values) {
		//		System.out.println("Getting a filtered list of elements of type " + classname);
		org.openntf.domino.graph2.DElementStore store = null;
		DGraph base = (DGraph) this.getBaseGraph();
		Class<?> chkClass = getClassFromName(classname);
		if (chkClass != null) {
			store = base.findElementStore(chkClass);
			if (store != null) {
				List<String> keystrs = CaseInsensitiveString.toStrings(keys);
				List<Object> valobj = new ArrayList<Object>(values);
				String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getFramedElementPartialFormula(keystrs, valobj, chkClass);
				Iterable<Element> elements = (org.openntf.domino.graph2.impl.DElementIterable) store.getElements(formulaFilter);
				return this.frameElements(elements, null);
			} else {
				return null;
			}
		} else {
			throw new IllegalArgumentException("Class " + classname + " not registered in graph");
		}
	}

	public <F> Iterable<F> getFilteredElementsStarts(final String classname, final List<CharSequence> keys,
			final List<CaseInsensitiveString> values) {
		//		System.out.println("Getting a filtered list of elements of type " + classname);
		org.openntf.domino.graph2.DElementStore store = null;
		DGraph base = (DGraph) this.getBaseGraph();
		Class<?> chkClass = getClassFromName(classname);
		if (chkClass != null) {
			store = base.findElementStore(chkClass);
			if (store != null) {
				List<String> keystrs = CaseInsensitiveString.toStrings(keys);
				List<Object> valobj = new ArrayList<Object>(values);
				String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getFramedElementStartsFormula(keystrs, valobj, chkClass);
				Iterable<Element> elements = (org.openntf.domino.graph2.impl.DElementIterable) store.getElements(formulaFilter);
				return this.frameElements(elements, null);
			} else {
				return null;
			}
		} else {
			throw new IllegalArgumentException("Class " + classname + " not registered in graph");
		}
	}

	public Element getElement(final Object id) {
		Object frame = getElement(id, null);
		if (frame == null) {
			return null;
		}
		if (frame instanceof EdgeFrame) {
			return ((EdgeFrame) frame).asEdge();
		} else if (frame instanceof VertexFrame) {
			return ((VertexFrame) frame).asVertex();
		} else {
			throw new IllegalStateException(
					"Attempt to get element with id " + String.valueOf(id) + " returned a " + frame.getClass().getName());
		}
	}

	public <F> F getElement(final Object id, final Class<F> kind) {
		F result = null;
		if (id == null) {
			return null;
		}
		try {
			//			System.out.println("Attempting to retrieve a " + (null == kind ? "Frame" : kind.getName()) + " with id "
			//					+ (null == id ? "null" : String.valueOf(id)));
			Object cacheChk = getFramedElementCache().get(id, new FrameLoadCallable(this, id, kind, Element.class));
			if (cacheChk != null && (kind == null || kind.isAssignableFrom(cacheChk.getClass()))) {
				if (cacheChk instanceof VertexFrame) {
					Vertex v = ((VertexFrame) cacheChk).asVertex();
					if (v instanceof DProxyVertex) {
						NoteCoordinate proxyid = ((DProxyVertex) v).getProxiedId();
						if (proxyid != null) {
							getFramedElementCache().put(proxyid, cacheChk);
						}
					}
				}
				return (F) cacheChk;
			}
		} catch (InvalidCacheLoadException icle) {
			//			System.out.println("Cache miss on id " + String.valueOf(id) + " for type " + (kind == null ? "null" : kind.getName()));
			//NTF this is no problem and quite normal
			return null;
		} catch (UncheckedExecutionException uee) {
			Throwable cause = uee.getCause();
			if (cause != null && cause instanceof UserAccessException) {
				throw new UserAccessException(cause.getMessage(), cause);
			} else {
				throw uee;
			}
		} catch (Throwable t) {
			t.printStackTrace();
			try {
				throw t;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public <F> F getVertex(final Object id, final Class<F> kind) {
		return getElement(id, kind);
	}

	@Override
	public <F> F getEdge(final Object id, final Class<F> kind) {
		return getElement(id, kind);
	}

	@Override
	public <F> Iterable<F> getVertices(final String key, final Object value, final Class<F> kind) {
		org.openntf.domino.graph2.DElementStore store = null;
		DGraph base = (DGraph) this.getBaseGraph();
		store = base.findElementStore(kind);
		if (store != null) {
			String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getFramedVertexFormula(key, value, kind);
			Iterable<Vertex> vertices = store.getVertices(formulaFilter);
			return this.frameVertices(vertices, kind);
		} else {
			return null;
		}
	}

	//	public <F> F addEdge(final Object id, final Vertex outVertex, final Vertex inVertex, final String label) {
	//		return (F) super.addEdge(id, outVertex, inVertex, label);
	//	}

	@Override
	public <F> F addEdge(final Object id, final Vertex outVertex, final Vertex inVertex, final String label, final Direction direction,
			final Class<F> kind) {
		if (id != null && id instanceof NoteCoordinate) {
			Object cacheChk = getElement(id, kind);
			if (cacheChk != null) {
				return (F) cacheChk;
			}
		}
		DGraph base = (DGraph) this.getBaseGraph();
		org.openntf.domino.graph2.DElementStore store = null;
		if (id instanceof NoteCoordinate) {
			store = base.findElementStore(id);
		} else {
			String typeid = getTypedId(id);
			if (typeid == null) {
				store = base.findElementStore(kind);
			} else {
				store = base.findElementStore(typeid);
			}
		}
		Edge edge = store.addEdge(id);
		((DEdge) edge).setLabel(label);
		((DEdge) edge).setInVertex(inVertex);
		((DEdge) edge).setOutVertex(outVertex);

		F result = frame(edge, kind);
		getFramedElementCache().put(edge.getId(), result);
		return result;
	}

	@Override
	public <F> Iterable<F> getEdges(final String key, final Object value, final Class<F> kind) {
		org.openntf.domino.graph2.DElementStore store = null;
		DGraph base = (DGraph) this.getBaseGraph();
		store = base.findElementStore(kind);
		if (store != null) {
			String formulaFilter = org.openntf.domino.graph2.DGraph.Utils.getFramedEdgeFormula(key, value, kind);
			Iterable<Edge> edges = store.getEdges(formulaFilter);
			return this.frameEdges(edges, kind);
		} else {
			return null;
		}
	}

	//	public <F> F initializeAndFrame(final Edge edge, final Class<F> kind) {
	//		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
	//			initializer.initElement(kind, this, edge);
	//		}
	//		return frame(edge, kind);
	//	}
	//
	//	@Deprecated
	//	public <F> F initializeAndFrame(final Edge edge, final Direction direction, final Class<F> kind) {
	//		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
	//			initializer.initElement(kind, this, edge);
	//		}
	//		return frame(edge, direction, kind);
	//	}
	//
	//	public <F> F initializeAndFrame(final Vertex vertex, final Class<F> kind) {
	//		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
	//			initializer.initElement(kind, this, vertex);
	//		}
	//		return frame(vertex, kind);
	//	}

	public <F> Iterable<F> frameElements(final Iterable<Element> elements, final Class<F> kind) {
		Iterator<Element> it = elements.iterator();
		if (it.hasNext()) {
			Object chkElement = it.next();
			if (chkElement instanceof Edge) {
				return new FramedEdgeList(this, null, elements, this.getReplacementType(kind));
			}
			if (chkElement instanceof Vertex) {
				return new FramedVertexList(this, null, elements, this.getReplacementType(kind));
			}
		}

		return new FramedElementIterable(this, elements, this.getReplacementType(kind));
	}

	public <F> F frame(final Element element, final Class<F> kind) {
		//		Class<F> klazz = kind;
		//		DConfiguration config = (DConfiguration) this.getConfig();
		//		config.getTypeManager().initElement(klazz, this, element);
		//		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
		//			if (!(initializer instanceof JavaFrameInitializer)) {
		//				initializer.initElement(klazz, this, element);
		//			}
		//		}
		F result = null;
		if (element instanceof Edge) {
			result = frame((Edge) element, kind);
		} else if (element instanceof Vertex) {
			result = frame((Vertex) element, kind);
		} else {
			throw new IllegalStateException("Cannot frame an element of type " + element.getClass().getName());
		}
		//		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
		//			if (initializer instanceof JavaFrameInitializer) {
		//				((JavaFrameInitializer) initializer).initElement(klazz, this, result);
		//			}
		//		}
		return result;
	}

	@Override
	@Deprecated
	public <F> F frame(final Edge edge, final Direction direction, final Class<F> kind) {
		DConfiguration config = (DConfiguration) this.getConfig();
		Class<F> klazz = (Class<F>) (kind == null ? DEdgeFrame.class : kind);
		config.getTypeManager().initElement(klazz, this, edge);
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (!(initializer instanceof JavaFrameInitializer)) {
				initializer.initElement(klazz, this, edge);
			}
		}
		F result = super.frame(edge, direction, klazz);
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (initializer instanceof JavaFrameInitializer) {
				((JavaFrameInitializer) initializer).initElement(klazz, this, result);
			}
		}
		return result;
	}

	@Override
	@SuppressWarnings("deprecation")
	public <F> F frame(final Edge edge, final Class<F> kind) {
		if (edge == null) {
			return null;
		}
		DConfiguration config = (DConfiguration) this.getConfig();
		Class<F> klazz = (Class<F>) (kind == null ? config.getDefaultEdgeFrameType() : kind);
		if (config.getReplacementType(klazz) != null) {
			klazz = (Class<F>) config.getReplacementType(klazz);
		}
		DTypeManager manager = config.getTypeManager();
		klazz = (Class<F>) manager.initElement(klazz, this, edge, false);
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (!(initializer instanceof JavaFrameInitializer)) {
				initializer.initElement(klazz, this, edge);
			}
		}

		F result = null;
		try {
			result = super.frame(edge, Direction.OUT, klazz);
		} catch (Throwable t) {
			System.out.println("Exception while attempting to frame a edge " + edge.getId() + " with class " + klazz.getName());
			t.printStackTrace();
			try {
				result = (F) super.frame(edge, Direction.OUT, config.getDefaultEdgeFrameType());
			} catch (Exception e) {
				System.out.println("Exception while attempting to frame a edge " + edge.getId() + " with class " + klazz.getName());
				e.printStackTrace();
				DominoUtils.handleException(e);
			}
		}
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (initializer instanceof JavaFrameInitializer) {
				((JavaFrameInitializer) initializer).initElement(klazz, this, result);
			}
		}
		return result;
	}

	public <F> F frame(final Vertex vertex, Class<F> kind, final boolean temporary) {
		if (vertex == null) {
			return null;
		}
		if (vertex instanceof DCategoryVertex) {
			kind = (Class<F>) CategoryVertex.class;
		} else {
			Map map = ((DVertex) vertex).getDelegate();
			if (map instanceof Document) {
				if (DesignFactory.isView((Document) map)) {
					kind = (Class<F>) ViewVertex.class;
				}
				if (DesignFactory.isIcon((Document) map) || DesignFactory.isACL((Document) map)) {
					//				System.out.println("TEMP DEBUG framing an icon note");
					kind = (Class<F>) DbInfoVertex.class;
				}
			}
		}
		DConfiguration config = (DConfiguration) this.getConfig();
		Class<F> klazz = (Class<F>) (kind == null ? config.getDefaultVertexFrameType() : kind);
		if (config.getReplacementType(klazz) != null) {
			klazz = (Class<F>) config.getReplacementType(klazz);
		}
		DTypeManager manager = config.getTypeManager();
		klazz = (Class<F>) manager.initElement(klazz, this, vertex, temporary);
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (!(initializer instanceof JavaFrameInitializer)) {
				initializer.initElement(klazz, this, vertex);
			}
		}
		F result = null;
		try {
			result = super.frame(vertex, klazz);
		} catch (Throwable t) {
			System.out.println("Exception while attempting to frame a vertex " + vertex.getId() + " with class " + klazz.getName());
			t.printStackTrace();
			//			DominoUtils.handleException(e);
			try {
				result = (F) super.frame(vertex, config.getDefaultVertexFrameType());
			} catch (Exception e) {
				System.out.println("Exception while attempting to frame a vertex " + vertex.getId() + " with class " + klazz.getName());
				e.printStackTrace();
				DominoUtils.handleException(e);
			}
		}
		for (FrameInitializer initializer : getConfig().getFrameInitializers()) {
			if (initializer instanceof JavaFrameInitializer) {
				((JavaFrameInitializer) initializer).initElement(klazz, this, result);
			}
		}
		if (vertex instanceof DProxyVertex) {
			List<CaseInsensitiveString> proxied = config.getTypeRegistry().getProxied(klazz);
			if (proxied != null) {
				((DProxyVertex) vertex).setProxied(proxied);
			}
		}
		if (result instanceof Eventable) {
			if (((Eventable) result).isNew()) {
				try {
					Method crystal = result.getClass().getMethod("create"); //$NON-NLS-1$
					if (crystal != null) {
						((Eventable) result).create();
					}
				} catch (Throwable t) {
					//nothing
				}
			} else {
				//				try {
				//					Method crystal = result.getClass().getMethod("read", null);
				//					if (crystal != null) {
				//						((Eventable) result).read();
				//					}
				//				} catch (Throwable t) {
				//					//nothing
				//				}
			}
		}
		return result;
	}

	@Override
	public <F> F frame(final Vertex vertex, final Class<F> kind) {
		return frame(vertex, kind, false);
	}

	public org.openntf.domino.graph2.DElementStore getElementStore(final Class<?> kind) {
		DGraph base = (DGraph) this.getBaseGraph();
		return base.findElementStore(kind);
	}

	public void addKeyResolver(final DKeyResolver resolver) {
		DGraph base = (DGraph) this.getBaseGraph();
		base.addKeyResolver(resolver);
	}

	public DKeyResolver getKeyResolver(final Class<?> type) {
		DGraph base = (DGraph) this.getBaseGraph();
		DKeyResolver result = base.getKeyResolver(type);
		if (result == null) {
			DConfiguration config = (DConfiguration) getConfig();
			result = base.getKeyResolver(config.getDefaultVertexFrameType());
			if (result == null) {
				result = new DefaultKeyResolver(this);
				base.addKeyResolver(result);
			}
		}
		return result;
	}

	public IndexScanner getIndexScanner() {
		return indexScanner_;
	}

	public void setIndexScanner(final IndexScanner indexScanner) {
		indexScanner_ = indexScanner;
	}

	public Class<?> getReplacementType(final Class<?> requestedType) {
		if (requestedType != null) {
			//		System.out.println("TEMP DEBUG Seeking replacement type for " + requestedType.getName());
			DConfiguration config = (DConfiguration) getConfig();
			Class<?> replace = config.getReplacementType(requestedType);
			if (replace == null) {
				return requestedType;
			} else {
				//			System.out.println("TEMP DEBUG Replacing with " + replace.getName());
				return replace;
			}
		} else {
			return requestedType;
		}
	}

	public void flushCache(final String id) {
		((DGraph) this.getBaseGraph()).flushCache(id);
		try {
			if (this.getFramedElementCache() != null) {
				NoteCoordinate nc = NoteCoordinate.Utils.getNoteCoordinate(id);
				Object chk = getFramedElementCache().getIfPresent(nc);
				if (chk != null) {
					getFramedElementCache().invalidate(nc);
					//					System.out.println("Invalidated id " + id + " in Framed Cache");
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
