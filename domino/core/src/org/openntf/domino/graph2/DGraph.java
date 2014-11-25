package org.openntf.domino.graph2;

import java.util.Map;
import java.util.Set;

import org.openntf.domino.graph2.impl.DEdgeList;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("rawtypes")
public interface DGraph extends com.tinkerpop.blueprints.Graph, com.tinkerpop.blueprints.MetaGraph,
com.tinkerpop.blueprints.TransactionalGraph {

	public void startTransaction(final Element elem);

	public Map<String, Object> findDelegate(Object delegateKey);

	public void removeDelegate(Element element);

	public Map<Long, DElementStore> getElementStores();

	public DElementStore findElementStore(Element element);

	public DElementStore findElementStore(Class<?> type);

	public DElementStore findElementStore(Object delegateKey);

	public DElementStore getDefaultElementStore();

	public DEdgeList getEdgesFromIds(Vertex source, final Set<String> set);

	public DEdgeList getEdgesFromIds(Vertex source, final Set<String> set, final String... labels);

	public Object getStoreDelegate(DElementStore store);

	public Object getProxyStoreDelegate(DElementStore store);

	public Object getStoreDelegate(DElementStore store, Object provisionalKey);

	public Object getProxyStoreDelegate(DElementStore store, Object provisionalKey);

}
