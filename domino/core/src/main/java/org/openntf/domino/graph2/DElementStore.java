package org.openntf.domino.graph2;

import java.io.Externalizable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

/*
 * Interface definition to register relationship between a Frame type and an NSF
 */
public interface DElementStore extends Externalizable {

	public void addType(Class<?> type);

	public void removeType(Class<?> type);

	public List<Class<?>> getTypes();

	public Object getStoreDelegate();

	public void setStoreDelegate(Object store);

	public DConfiguration getConfiguration();

	public void setConfiguration(DConfiguration config);

	public DIdentityFactory getIdentityFactory();

	public Object getIdentity(Class<?> type, Object context, Object... args);

	public void setIdentityFactory(DIdentityFactory identFactory);

	public Long getStoreKey();

	public void setStoreKey(Long key);

	public void setStoreKey(CharSequence key);

	public Long getProxyStoreKey();

	public void setProxyStoreKey(Long key);

	public void setProxyStoreKey(CharSequence key);

	public void setProxyStoreDelegate(Object proxyStore);

	public Vertex addVertex(Object id);

	public Vertex getVertex(Object id);

	public void removeVertex(Vertex vertex);

	public Edge addEdge(Object id);

	public Edge getEdge(Object id);

	public void removeEdge(Edge edge);

	public Map<String, Object> findElementDelegate(Object delegateKey, Class<? extends Element> type);

	public void removeElementDelegate(Element element);

	public Element getElement(final Object id) throws IllegalStateException;

	public Set<Vertex> getCachedVertices();

	public Set<Edge> getCachedEdges();

	public Iterable<Vertex> getVertices();

	public Iterable<Edge> getEdges();

	public Iterable<Vertex> getVertices(String formulaFilter);

	public Iterable<Edge> getEdges(String formulaFilter);

	public Iterable<Vertex> getVertices(String key, Object value);

	public Iterable<Edge> getEdges(String key, Object value);

	public void uncache(Element element);

}
