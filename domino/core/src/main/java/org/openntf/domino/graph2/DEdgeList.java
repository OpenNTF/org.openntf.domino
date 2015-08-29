package org.openntf.domino.graph2;

import java.util.List;

import org.openntf.domino.graph2.impl.DVertexList;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public interface DEdgeList extends List<Edge> {

	public abstract DEdgeList atomic();

	public abstract DEdgeList unmodifiable();

	public abstract Edge findEdge(Vertex toVertex);

	public abstract DEdgeList applyFilter(String key, Object value);

	public abstract DVertexList toVertexList();

}