package org.openntf.domino.graph2;

import java.io.Externalizable;
import java.util.List;
import java.util.Map;

import org.openntf.domino.graph2.impl.DGraph;

public interface DConfiguration extends Externalizable {

	public Map<Class<?>, Integer> getTypeMap();

	public List<DElementStore> getElementStoreList();

	public void addElementStore(DElementStore store);

	public DGraph getGraph();

	public void setGraph(DGraph graph);

	public DElementStore getDefaultElementStore();

	public void setDefaultElementStore(int index);

}
