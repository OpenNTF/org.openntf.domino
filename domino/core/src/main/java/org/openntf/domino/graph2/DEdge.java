/**
 * 
 */
package org.openntf.domino.graph2;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author nfreeman
 * 
 */
public interface DEdge extends DElement, com.tinkerpop.blueprints.Edge {
	public static final String GRAPH_TYPE_VALUE = "E";
	public static final String IN_NAME = "_OPEN_IN";
	public static final String LABEL_NAME = "_OPEN_LABEL";
	public static final String OUT_NAME = "_OPEN_OUT";
	public static final String FORMULA_FILTER = DElement.TYPE_FIELD + "=\"" + GRAPH_TYPE_VALUE + "\"";

	public Vertex getOtherVertex(Vertex vertex);

	public Object getOtherVertexProperty(Vertex vertex, String property);

	public Object getOtherVertexId(final Vertex vertex);

	public Object getVertexId(final Direction direction);

}
