package org.openntf.domino.graph2.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javolution.util.FastMap;

import org.openntf.domino.graph2.DElementStore;

import com.tinkerpop.frames.FramedGraphConfiguration;
import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.typedgraph.TypedGraphModuleBuilder;

public class DConfiguration extends FramedGraphConfiguration implements org.openntf.domino.graph2.DConfiguration {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DConfiguration.class.getName());
	private Long defaultElementStoreKey_ = null;
	private Map<Long, DElementStore> elementStoreMap_;
	private Map<Class<?>, Long> typeMap_;
	private transient DGraph graph_;
	private transient Module module_;

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
	public Map<Class<?>, Long> getTypeMap() {
		if (typeMap_ == null) {
			typeMap_ = new FastMap<Class<?>, Long>();
		}
		return typeMap_;
	}

	@Override
	public void setDefaultElementStore(final Long key) {
		defaultElementStoreKey_ = key;
	}

	@Override
	public DElementStore getDefaultElementStore() {
		return getElementStores().get(defaultElementStoreKey_);
	}

	@Override
	public Map<Long, DElementStore> getElementStores() {
		if (elementStoreMap_ == null) {
			elementStoreMap_ = new FastMap<Long, DElementStore>();
		}
		return elementStoreMap_;
	}

	@Override
	public void addElementStore(final DElementStore store) {
		store.setConfiguration(this);
		Long key = store.getStoreKey();
		DElementStore schk = getElementStores().get(key);
		if (schk == null) {
			getElementStores().put(key, store);
		}
		List<Class<?>> types = store.getTypes();
		for (Class<?> type : types) {
			Long chk = getTypeMap().get(type);
			if (chk != null) {
				if (!chk.equals(key)) {
					throw new IllegalStateException("Element store has already been registered for type " + type.getName());
				}
			} else {
				getTypeMap().put(type, key);
			}
		}
	}

	private TypedGraphModuleBuilder getTypedBuilder() {
		TypedGraphModuleBuilder typedBuilder = new TypedGraphModuleBuilder();
		for (DElementStore store : getElementStores().values()) {
			for (Class<?> klazz : store.getTypes()) {
				typedBuilder.withClass(klazz);
			}
		}
		return typedBuilder;
	}

	@Override
	public Module getModule() {
		if (module_ == null) {
			TypedGraphModuleBuilder builder = getTypedBuilder();
			module_ = builder.build();
		}
		return module_;
	}

	@Override
	public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
		defaultElementStoreKey_ = in.readLong();
		int count = in.readInt();
		for (int i = 0; i < count; i++) {
			DElementStore store = (DElementStore) in.readObject();
			addElementStore(store);
			store.setConfiguration(this);
		}

	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeLong(defaultElementStoreKey_);
		out.writeInt(getElementStores().size());
		for (DElementStore store : getElementStores().values()) {
			out.writeObject(store);
		}
	}

}
