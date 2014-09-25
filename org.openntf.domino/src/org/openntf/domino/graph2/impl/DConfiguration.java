package org.openntf.domino.graph2.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;

import org.openntf.domino.graph2.DElementStore;

public class DConfiguration implements org.openntf.domino.graph2.DConfiguration {
	private static final Logger log_ = Logger.getLogger(DConfiguration.class.getName());
	private int defaultElementStoreIndex_ = 0;
	private List<DElementStore> elementStoreList_;
	private Map<Class<?>, Integer> typeMap_;
	private transient DGraph graph_;

	public DConfiguration() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public DGraph getGraph() {
		return graph_;
	}

	@Override
	public void setGraph(final DGraph graph) {
		graph_ = graph;
	}

	@Override
	public Map<Class<?>, Integer> getTypeMap() {
		if (typeMap_ == null) {
			typeMap_ = new FastMap<Class<?>, Integer>();
		}
		return typeMap_;
	}

	@Override
	public void setDefaultElementStore(final int index) {
		defaultElementStoreIndex_ = index;
	}

	@Override
	public DElementStore getDefaultElementStore() {
		return getElementStoreList().get(defaultElementStoreIndex_);
	}

	@Override
	public List<DElementStore> getElementStoreList() {
		if (elementStoreList_ == null) {
			elementStoreList_ = new FastTable<DElementStore>();
		}
		return elementStoreList_;
	}

	@Override
	public void addElementStore(final DElementStore store) {
		int index = getElementStoreList().indexOf(store);
		if (index < 0) {
			index = getElementStoreList().size();
			getElementStoreList().add(store);
		}
		List<Class<?>> types = store.getTypes();
		for (Class<?> type : types) {
			Integer chk = getTypeMap().get(type);
			if (chk != null) {
				if (chk != index) {
					throw new IllegalStateException("Element store has already been registered for type " + type.getName());
				}
			} else {
				getTypeMap().put(type, index);
			}
		}
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		defaultElementStoreIndex_ = in.readInt();
		int count = in.readInt();
		List<DElementStore> list = getElementStoreList();
		for (int i = 0; i < count; i++) {
			DElementStore store = (DElementStore) in.readObject();
			list.add(store);
			store.setConfiguration(this);
		}

	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeInt(defaultElementStoreIndex_);
		out.writeInt(getElementStoreList().size());
		for (DElementStore store : getElementStoreList()) {
			out.writeObject(store);
		}
	}

}
