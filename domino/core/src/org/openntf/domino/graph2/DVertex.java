/**
 * 
 */
package org.openntf.domino.graph2;

import java.util.Set;

import com.tinkerpop.blueprints.Edge;

/**
 * @author nfreeman
 * 
 */
public interface DVertex extends com.tinkerpop.blueprints.Vertex, DElement {
	public static final String GRAPH_TYPE_VALUE = "V";

	public void addInEdge(final Edge edge);

	public void addOutEdge(final Edge edge);

	public String validateEdges();

	public int getInEdgeCount(String label);

	public int getOutEdgeCount(String label);

	public Set<String> getInEdgeLabels();

	public Set<String> getOutEdgeLabels();

	public Set<Edge> getEdges(final String... labels);

}
