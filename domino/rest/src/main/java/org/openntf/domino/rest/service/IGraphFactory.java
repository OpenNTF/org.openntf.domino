package org.openntf.domino.rest.service;

import com.tinkerpop.frames.FramedGraph;

import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.openntf.domino.rest.resources.command.ICommandProcessor;

public interface IGraphFactory {

	public Map<String, FramedGraph<?>> getRegisteredGraphs();

	public Object processCommand(String namespace, String command, MultivaluedMap<String, String> params);

	public void registerCommandProcessor(String namespace, String command, ICommandProcessor processor);

	public void unregisterCommandProcessor(ICommandProcessor processor);

}
