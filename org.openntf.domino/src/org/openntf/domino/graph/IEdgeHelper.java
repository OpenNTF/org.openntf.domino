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
public interface IEdgeHelper {

	public String getLabel();

	public Class<? extends Vertex> getInType();

	public Class<? extends Vertex> getOutType();

	public Edge makeEdge(final Vertex defaultOut, final Vertex defaultIn);

	public boolean isUnique();

	public Edge findEdge(final Vertex defaultOut, final Vertex defaultIn);

}
