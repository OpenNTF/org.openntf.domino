package com.tinkerpop.frames.core;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.Predicate;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphQuery;
import com.tinkerpop.frames.structures.FramedEdgeIterable;
import com.tinkerpop.frames.structures.FramedVertexIterable;

public class FramedGraphQueryImpl implements FramedGraphQuery {
	private GraphQuery graphQuery;
	private FramedGraph<?> graph;

	//NTF change to force SCM checkin

	public FramedGraphQueryImpl(final FramedGraph<?> graph, final GraphQuery graphQuery) {
		this.graph = graph;
		this.graphQuery = graphQuery;
	}

	@Override
	public FramedGraphQuery has(final String key) {
		graphQuery = graphQuery.has(key);
		return this;
	}

	@Override
	public FramedGraphQuery hasNot(final String key) {
		graphQuery = graphQuery.hasNot(key);
		return this;
	}

	@Override
	public FramedGraphQuery has(final String key, final Object value) {
		graphQuery = graphQuery.has(key, value);
		return this;
	}

	@Override
	public FramedGraphQuery hasNot(final String key, final Object value) {
		graphQuery = graphQuery.hasNot(key, value);
		return this;
	}

	@Override
	public FramedGraphQuery has(final String key, final Predicate predicate, final Object value) {
		graphQuery = graphQuery.has(key, predicate, value);
		return this;
	}

	@SuppressWarnings("deprecation")
	@Override
	public <T extends Comparable<T>> FramedGraphQuery has(final String key, final T value, final Compare compare) {
		graphQuery = graphQuery.has(key, value, compare);
		return this;
	}

	@Override
	public <T extends Comparable<?>> FramedGraphQuery interval(final String key, final T startValue, final T endValue) {
		graphQuery = graphQuery.interval(key, startValue, endValue);
		return this;
	}

	@Override
	public FramedGraphQuery limit(final int limit) {
		graphQuery = graphQuery.limit(limit);
		return this;
	}

	@Override
	public <T> Iterable<T> edges(final Class<T> kind) {
		return new FramedEdgeIterable<T>(graph, edges(), kind);
	}

	@Override
	public <T> Iterable<T> vertices(final Class<T> kind) {
		return new FramedVertexIterable<T>(graph, vertices(), kind);
	}

	@Override
	public Iterable<Edge> edges() {
		return graphQuery.edges();
	}

	@Override
	public Iterable<Vertex> vertices() {
		return graphQuery.vertices();
	}

}
