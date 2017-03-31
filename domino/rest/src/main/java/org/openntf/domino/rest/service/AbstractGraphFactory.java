package org.openntf.domino.rest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.openntf.domino.graph2.DConfiguration.IExtConfiguration;
import org.openntf.domino.rest.resources.command.ICommandProcessor;
import org.openntf.domino.rest.resources.info.IInfoProvider;

import com.tinkerpop.frames.FramedGraph;

public abstract class AbstractGraphFactory implements IGraphFactory {
	protected final Map<String, ICommandProcessor> processorMap_ = new HashMap<String, ICommandProcessor>();
	protected final Map<String, IInfoProvider> providerMap_ = new HashMap<String, IInfoProvider>();
	protected final List<IExtConfiguration> extConfigs_ = new ArrayList<IExtConfiguration>();

	@Override
	public void registerConfigExtension(IExtConfiguration extConfig) {
		extConfigs_.add(extConfig);
	}

	@Override
	public List<IExtConfiguration> getConfigExtensions() {
		return extConfigs_;
	}

	@Override
	public Object processCommand(String namespace, String command, MultivaluedMap<String, String> params) {
		Object result = null;
		ICommandProcessor proc = processorMap_.get(namespace + ":" + command);
		if (proc != null) {
			FramedGraph graph = getRegisteredGraphs().get(namespace);
			if (graph != null) {
				result = proc.processCommand(graph, command, params);
			}
		}
		return result;
	}

	@Override
	public void registerCommandProcessor(String namespace, String command, ICommandProcessor processor) {
		processorMap_.put(namespace + ":" + command, processor);
	}

	@Override
	public void unregisterCommandProcessor(ICommandProcessor processor) {
		List<String> keys = new ArrayList<String>();
		for (Map.Entry entry : processorMap_.entrySet()) {
			if (entry.getValue().equals(processor)) {
				keys.add(String.valueOf(entry.getKey()));
			}
		}
		for (String key : keys) {
			processorMap_.remove(key);
		}
	}

	@Override
	public Object processRequest(String namespace, String item, MultivaluedMap<String, String> params) {
		Object result = null;
		IInfoProvider provider = providerMap_.get(namespace + ":" + item);
		if (provider != null) {
			FramedGraph graph = getRegisteredGraphs().get(namespace);
			if (graph != null) {
				result = provider.processRequest(graph, item, params);
			}
		}
		return result;
	}

	@Override
	public void registerInfoProvider(String namespace, String item, IInfoProvider provider) {
		providerMap_.put(namespace + ":" + item, provider);
	}

	@Override
	public void unregisterInfoProvider(IInfoProvider provider) {
		List<String> keys = new ArrayList<String>();
		for (Map.Entry entry : providerMap_.entrySet()) {
			if (entry.getValue().equals(provider)) {
				keys.add(String.valueOf(entry.getKey()));
			}
		}
		for (String key : keys) {
			providerMap_.remove(key);
		}
	}

}
