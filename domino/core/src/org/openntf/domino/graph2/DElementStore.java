package org.openntf.domino.graph2;

import java.io.Externalizable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openntf.domino.annotations.Incomplete;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

/*
 * Start of interface definition to register relationship between a Frame type and an NSF
 */
@Incomplete
public interface DElementStore extends Externalizable {

	public void addType(Class<?> type);

	public void removeType(Class<?> type);

	public List<Class<?>> getTypes();

	public Object getStoreDelegate();

	public void setStoreDelegate(Object store);

	public DConfiguration getConfiguration();

	public void setConfiguration(DConfiguration config);

	public Long getStoreKey();

	public void setStoreKey(Long key);

	public Vertex addVertex(Object id);

	public Vertex getVertex(Object id);

	public void removeVertex(Vertex vertex);

	public Edge addEdge(Object id);

	public Edge getEdge(Object id);

	public void removeEdge(Edge edge);

	public Map<String, Object> findElementDelegate(Object delegateKey, Class<? extends Element> type);

	public void removeElementDelegate(Element element);

	public Set<Vertex> getCachedVertices();

	public Set<Edge> getCachedEdges();

}
