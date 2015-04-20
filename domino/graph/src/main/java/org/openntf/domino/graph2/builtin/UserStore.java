package org.openntf.domino.graph2.builtin;

import java.util.Map;

import org.openntf.domino.graph2.impl.DElementStore;

import com.tinkerpop.blueprints.Element;

public class UserStore extends DElementStore {

	public UserStore() {
		this.addType(User.class);
		this.setStoreKey("names.nsf");
	}

	@Override
	public Map<String, Object> findElementDelegate(final Object delegateKey, final Class<? extends Element> type) {
		if (this.getProxyStoreDelegate() == null) {
			throw new IllegalStateException("Cannot find elements in a User store without a proxy store set");
		}
		return super.findElementDelegate(delegateKey, type);
	}

}
