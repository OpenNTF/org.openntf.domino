package org.openntf.domino.graph2;

import java.util.Map;
import java.util.Set;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;

@SuppressWarnings("rawtypes")
public interface DGraph extends com.tinkerpop.blueprints.Graph, com.tinkerpop.blueprints.MetaGraph,
		com.tinkerpop.blueprints.TransactionalGraph {

	public void startTransaction(final Element elem);

	public Map<String, Object> findDelegate(Object delegateKey);

	public void removeDelegate(Element element);

	public Map<String, DElementStore> getElementStores();

	public DElementStore findElementStore(Element element);

	public DElementStore findElementStore(Class<?> type);

	public DElementStore findElementStore(Object delegateKey);

	public DElementStore getDefaultElementStore();

	public Set<Edge> getEdgesFromIds(final Set<String> set);

	public Set<Edge> getEdgesFromIds(final Set<String> set, final String... labels);

	public Object getStoreDelegate(DElementStore store);

}
