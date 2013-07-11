/**
 * 
 */
package org.openntf.domino.graph;

import com.tinkerpop.blueprints.Vertex;

/**
 * @author nfreeman
 * 
 */
public interface IEdgeHelper {

	public String getLabel();

	public Class<? extends Vertex> getInType();

	public Class<? extends Vertex> getOutType();

}
