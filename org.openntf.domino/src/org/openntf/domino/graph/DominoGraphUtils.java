package org.openntf.domino.graph;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

public enum DominoGraphUtils {
	;

	private DominoGraphUtils() {

	}

	public static SortedSet<? extends Element> sortElements(final Iterable<? extends Element> elements, final String[] sortproperties) {
		Comparator<Element> comp = new GenericElementComparator(sortproperties);
		SortedSet<Element> result = new TreeSet<Element>(comp);
		for (Object e : elements) {
			if (e instanceof Element) {
				result.add((Element) e);
			}
		}
		return Collections.unmodifiableSortedSet(result);
	}

	public static SortedSet<? extends Element> sortElements(final Iterable<? extends Element> elements,
			final IDominoProperties[] sortproperties) {
		Comparator<Element> comp = new GenericElementComparator(sortproperties);
		SortedSet<Element> result = new TreeSet<Element>(comp);
		for (Object e : elements) {
			if (e instanceof Element) {
				result.add((Element) e);
			}
		}
		return Collections.unmodifiableSortedSet(result);
	}

	@SuppressWarnings("unchecked")
	public static SortedSet<? extends Edge> sortEdges(final Iterable<? extends Edge> elements, final String[] sortproperties) {
		return (SortedSet<Edge>) sortElements(elements, sortproperties);
	}

	@SuppressWarnings("unchecked")
	public static SortedSet<? extends Vertex> sortVertexes(final Iterable<? extends Vertex> elements, final String[] sortproperties) {
		return (SortedSet<Vertex>) sortElements(elements, sortproperties);
	}

	@SuppressWarnings("unchecked")
	public static SortedSet<? extends Edge> sortEdges(final Iterable<? extends Edge> elements, final IDominoProperties[] sortproperties) {
		return (SortedSet<Edge>) sortElements(elements, sortproperties);
	}

	@SuppressWarnings("unchecked")
	public static SortedSet<? extends Vertex> sortVertexes(final Iterable<? extends Vertex> elements,
			final IDominoProperties[] sortproperties) {
		return (SortedSet<Vertex>) sortElements(elements, sortproperties);
	}
}
