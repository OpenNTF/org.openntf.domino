package org.openntf.domino.graph2.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tinkerpop.blueprints.Direction;

/**
 * Incidences annotate getters and adders to represent a Vertex incident to an Edge. IncidenceUnique extends this idea to ensure that there
 * is only one instance of an Edge with a specific label between any two specific Vertices.
 *
 * @author Nathan T. Freeman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IncidenceUnique {
	/**
	 * The labels of the edges that are incident to the vertex.
	 *
	 * @return the edge label
	 */
	public String label();

	/**
	 * The direction of the edges.
	 *
	 * @return the edge direction
	 */
	public Direction direction() default Direction.OUT;
}
