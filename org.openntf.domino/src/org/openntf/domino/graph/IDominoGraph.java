package org.openntf.domino.graph;

import java.util.Set;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.MetaGraph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("rawtypes")
public interface IDominoGraph extends Graph, MetaGraph, TransactionalGraph {
	public IDominoEdge addEdge(Object id, Vertex outVertex, Vertex inVertex, String label);

	public IDominoVertex addVertex(Object id);

	public IDominoEdge getEdge(Object id);

	public org.openntf.domino.Database getRawGraph();

	public IDominoVertex getVertex(Object id);

	public IEdgeHelper getHelper(final String key);

	public IEdgeHelper getHelper(final IDominoEdgeType edgeType);

	public Set<IEdgeHelper> findHelpers(final Vertex in, final Vertex out);

	public void addHelper(final String key, final IEdgeHelper helper);

	public void removeHelper(final String key);

}
