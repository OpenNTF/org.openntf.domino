package org.openntf.domino.graph2.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tinkerpop.blueprints.Direction;

/**
 * Ajacency annotate getters and adders to represent a Vertex incident to an Edge. AjacencyUnique extends this idea to ensure that there is
 * only one instance of an Edge with a specific label between any two specific Vertices. This allows the user of the graph to call an "add"
 * method and return the existing ajacency if it already exists.
 *
 * @author Nathan T. Freeman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AdjacencyUnique {
	/**
	 * The label of the edges making the adjacency between the vertices.
	 *
	 * @return the edge label
	 */
	public String label();

	/**
	 * The edge direction of the adjacency.
	 *
	 * @return the direction of the edges composing the adjacency
	 */
	public Direction direction() default Direction.OUT;
}
