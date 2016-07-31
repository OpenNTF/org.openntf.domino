package org.openntf.domino.graph2.builtin.identity;

import org.openntf.domino.graph2.impl.DElementStore;

import com.tinkerpop.blueprints.Element;

public class PersonStore extends DElementStore {

	public PersonStore() {
		this.addType(Person.class);
		this.setStoreKey("names.nsf");
	}

	@Override
	public Object findElementDelegate(final Object delegateKey, final Class<? extends Element> type) {
		if (this.getProxyStoreDelegate() == null) {
			throw new IllegalStateException("Cannot find elements in a User store without a proxy store set");
		}
		return super.findElementDelegate(delegateKey, type);
	}

}
