package org.openntf.domino.rest.service;

import com.tinkerpop.frames.FramedGraph;

import java.util.Map;

import org.openntf.domino.rest.resources.command.ICommandProcessor;

public interface IGraphFactory {

	public Map<String, FramedGraph<?>> getRegisteredGraphs();

	public Object processCommand(String namespace, String command, String... args);

	public void registerCommandProcessor(String namespace, String command, ICommandProcessor processor);

	public void unregisterCommandProcessor(ICommandProcessor processor);

}
