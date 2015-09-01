package org.openntf.domino.rest.resources.command;

import com.tinkerpop.frames.FramedGraph;

import java.util.List;

public interface ICommandProcessor extends Runnable {
	public Object processCommand(@SuppressWarnings("rawtypes") FramedGraph graph, String command, String... args);

	public List<String> getNamespaces();

	public List<String> getCommands();
}
