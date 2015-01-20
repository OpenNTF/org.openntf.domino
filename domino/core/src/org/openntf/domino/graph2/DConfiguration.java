package org.openntf.domino.graph2;

import java.io.Externalizable;
import java.util.Map;

import org.openntf.domino.graph2.impl.DGraph;

import com.tinkerpop.frames.modules.Module;

public interface DConfiguration extends Externalizable {

	public Map<Class<?>, Long> getTypeMap();

	public Map<Long, DElementStore> getElementStores();

	public void addElementStore(DElementStore store);

	public DGraph getGraph();

	public Module getModule();

	public void setGraph(DGraph graph);

	public DElementStore getDefaultElementStore();

	public void setDefaultElementStore(Long key);

}
