/**
 * 
 */
package org.openntf.domino.graph;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author nfreeman
 * 
 */
public interface IDominoVertex extends Vertex {
	public void addInEdge(final Edge edge);

	public void addOutEdge(final Edge edge);
}
