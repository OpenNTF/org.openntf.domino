/**
 * 
 */
package org.openntf.domino.graph;

import com.tinkerpop.blueprints.Vertex;

/**
 * @author Nathan T. Freeman
 * 
 */
@Deprecated
public interface IDominoEdgeType {
	public String getLabel();

	public Class<? extends Vertex> getInType();

	public Class<? extends Vertex> getOutType();
}
