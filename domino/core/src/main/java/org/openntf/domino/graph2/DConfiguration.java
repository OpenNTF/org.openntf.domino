package org.openntf.domino.graph2;

import java.io.Externalizable;
import java.util.Map;

import org.openntf.domino.graph2.impl.DGraph;

import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.typedgraph.TypeManager;
import com.tinkerpop.frames.modules.typedgraph.TypeRegistry;

public interface DConfiguration extends Externalizable {

	public static interface IExtConfiguration {
		public void extendConfiguration(DConfiguration config);
	}

	public Map<Class<?>, Long> getTypeMap();

	public Map<Long, DElementStore> getElementStores();

	public DElementStore addElementStore(DElementStore store);

	public void addKeyResolver(DKeyResolver resolver);

	public DKeyResolver getKeyResolver(Class<?> type);

	public DGraph getGraph();

	public Module getModule();

	public TypeRegistry getTypeRegistry();

	public TypeManager getTypeManager();

	public DGraph setGraph(DGraph graph);

	public DElementStore getDefaultElementStore();

	public void setDefaultElementStore(Long key);

	public void setDefaultElementStore(DElementStore store);

}
