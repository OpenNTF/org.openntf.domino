package org.openntf.domino.graph2.builtin.search;

import org.openntf.domino.graph2.DElementStore;
import org.openntf.domino.graph2.builtin.identity.Name;
import org.openntf.domino.graph2.impl.DConfiguration;
import org.openntf.domino.graph2.impl.DGraph;

/*
 * Demonstration class for index graph factory
 * @author Nathan T. Freeman
 */

public class IndexFactory {

	public IndexFactory() {

	}

	public void initGraph() {
		DElementStore termsStore = new org.openntf.domino.graph2.builtin.search.IndexStore();
		termsStore.setStoreKey("ODADemo/terms.nsf");
		termsStore.addType(Term.class);

		DElementStore valuesStore = new org.openntf.domino.graph2.builtin.search.IndexStore();
		valuesStore.setStoreKey("ODADemo/values.nsf");
		valuesStore.addType(Value.class);

		DElementStore namesStore = new org.openntf.domino.graph2.builtin.search.IndexStore();
		namesStore.setStoreKey("ODADemo/names.nsf");
		namesStore.addType(Name.class);

		DConfiguration config = new DConfiguration();
		DGraph graph = new DGraph(config);
		config.addElementStore(termsStore);
		config.addElementStore(valuesStore);
		config.addElementStore(namesStore);
	}

}
