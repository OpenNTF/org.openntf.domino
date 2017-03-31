package org.openntf.domino.rest.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.openntf.domino.graph2.DConfiguration.IExtConfiguration;
import org.openntf.domino.rest.resources.command.ICommandProcessor;
import org.openntf.domino.rest.resources.info.IInfoProvider;

import com.tinkerpop.frames.FramedGraph;

public interface IGraphFactory {

	public Map<String, FramedGraph<?>> getRegisteredGraphs();

	public Object processCommand(String namespace, String command, MultivaluedMap<String, String> params);

	public Object processRequest(String namespace, String item, MultivaluedMap<String, String> params);

	public void registerCommandProcessor(String namespace, String command, ICommandProcessor processor);

	public void unregisterCommandProcessor(ICommandProcessor processor);

	public void registerInfoProvider(String namespace, String item, IInfoProvider provider);

	public void unregisterInfoProvider(IInfoProvider provider);

	public void registerConfigExtension(IExtConfiguration extConfig);

	public List<IExtConfiguration> getConfigExtensions();
}
