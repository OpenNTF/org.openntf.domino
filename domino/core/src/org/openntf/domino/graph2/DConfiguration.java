package org.openntf.domino.graph2;

import java.io.Externalizable;
import java.util.Map;

import org.openntf.domino.graph2.impl.DGraph;

import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.typedgraph.TypeManager;
import com.tinkerpop.frames.modules.typedgraph.TypeRegistry;

public interface DConfiguration extends Externalizable {

	public Map<Class<?>, Long> getTypeMap();

	public Map<Long, DElementStore> getElementStores();

	public void addElementStore(DElementStore store);

	public DGraph getGraph();

	public Module getModule();

	public TypeRegistry getTypeRegistry();

	public TypeManager getTypeManager();

	public void setGraph(DGraph graph);

	public DElementStore getDefaultElementStore();

	public void setDefaultElementStore(Long key);

}
