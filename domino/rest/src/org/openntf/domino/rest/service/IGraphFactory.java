package org.openntf.domino.rest.service;

import com.tinkerpop.frames.FramedGraph;

import java.util.Map;

public interface IGraphFactory {

	@SuppressWarnings("rawtypes")
	public Map<String, FramedGraph> getRegisteredGraphs();

}
