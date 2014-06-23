/**
 * 
 */
package org.openntf.domino.graph;

import java.util.Set;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author nfreeman
 * 
 */
public interface IDominoVertex extends Vertex, IDominoElement {
	public void addInEdge(final Edge edge);

	public void addOutEdge(final Edge edge);

	public String validateEdges();

	public int getInEdgeCount(String label);

	public int getOutEdgeCount(String label);

	public Set<IEdgeHelper> getEdgeHelpers();

	public Set<String> getInEdgeLabels();

	public Set<String> getOutEdgeLabels();

	public Set<Edge> getEdges(final String... labels);

	public IDominoEdge relate(Vertex vertex);

	public IDominoEdge find(Vertex vertex);

}
