package org.openntf.domino.graph2;

import java.io.Externalizable;
import java.util.Map;

import org.openntf.domino.graph2.impl.DGraph;

public interface DConfiguration extends Externalizable {

	public Map<Class<?>, String> getTypeMap();

	public Map<String, DElementStore> getElementStores();

	public void addElementStore(DElementStore store);

	public DGraph getGraph();

	public void setGraph(DGraph graph);

	public DElementStore getDefaultElementStore();

	public void setDefaultElementStore(String key);

}
