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
public interface IDominoEdge extends IDominoElement, Edge {

	public Vertex getOtherVertex(Vertex vertex);

}
