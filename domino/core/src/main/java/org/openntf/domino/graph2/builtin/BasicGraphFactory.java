package org.openntf.domino.graph2.builtin;

import org.openntf.domino.graph2.DElementStore;
import org.openntf.domino.graph2.impl.DConfiguration;
import org.openntf.domino.graph2.impl.DGraph;

public class BasicGraphFactory {

	public BasicGraphFactory() {
	}

	protected static synchronized DGraph getGraph(final String apipath) {
		DConfiguration config = new DConfiguration();
		DGraph graph = new DGraph(config);
		DElementStore store = new org.openntf.domino.graph2.impl.DElementStore();
		store.setStoreKey(apipath);
		config.setDefaultElementStore(store);
		return graph;
	}

}
