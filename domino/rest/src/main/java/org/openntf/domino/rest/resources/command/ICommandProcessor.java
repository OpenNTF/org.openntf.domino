package org.openntf.domino.rest.resources.command;

import com.tinkerpop.frames.FramedGraph;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

public interface ICommandProcessor extends Runnable {
	public Object processCommand(@SuppressWarnings("rawtypes") FramedGraph graph, String command,
			MultivaluedMap<String, String> params);

	public List<String> getNamespaces();

	public List<String> getCommands();
}
