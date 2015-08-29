package org.openntf.domino.rest.service;

import com.tinkerpop.frames.FramedGraph;

import java.util.List;
import java.util.Map;

public interface IGraphFactory {
	public static interface ICommandProcessor extends Runnable {

		public Object processCommand(@SuppressWarnings("rawtypes") FramedGraph graph, String command, String... args);

		public List<String> getNamespaces();

		public List<String> getCommands();

	}

	public Map<String, FramedGraph<?>> getRegisteredGraphs();

	public Object processCommand(String namespace, String command, String... args);

	public void registerCommandProcessor(String namespace, String command, ICommandProcessor processor);

	public void unregisterCommandProcessor(ICommandProcessor processor);

}
