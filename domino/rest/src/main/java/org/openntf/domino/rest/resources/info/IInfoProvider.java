package org.openntf.domino.rest.resources.info;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.tinkerpop.frames.FramedGraph;

public interface IInfoProvider {
	public Object processRequest(@SuppressWarnings("rawtypes") FramedGraph graph, String item,
			MultivaluedMap<String, String> params);

	public List<String> getItems();

	public List<String> getNamespaces();
}
