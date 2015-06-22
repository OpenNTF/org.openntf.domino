package org.openntf.domino.rest.service;

import com.tinkerpop.frames.FramedGraph;

import java.util.Map;

public interface IGraphFactory {
	public static interface ICommandProcessor {
		public Object processCommand(@SuppressWarnings("rawtypes") FramedGraph graph, String command, String... args);
	}

	@SuppressWarnings("rawtypes")
	public Map<String, FramedGraph> getRegisteredGraphs();

	public Object processCommand(String namespace, String command, String... args);

	public void registerCommandProcessor(String namespace, String command, ICommandProcessor processor);

	public void unregisterCommandProcessor(ICommandProcessor processor);

}
