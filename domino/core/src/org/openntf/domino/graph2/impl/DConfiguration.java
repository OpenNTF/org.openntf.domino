package org.openntf.domino.graph2.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javolution.util.FastMap;

import org.openntf.domino.graph2.DElementStore;
import org.openntf.domino.graph2.annotations.TypedProperty;

import com.tinkerpop.frames.ClassUtilities;
import com.tinkerpop.frames.FramedGraphConfiguration;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.AbstractModule;
import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.typedgraph.TypeManager;
import com.tinkerpop.frames.modules.typedgraph.TypeRegistry;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;
import com.tinkerpop.frames.modules.typedgraph.TypedGraphModuleBuilder;
import com.tinkerpop.frames.util.Validate;

public class DConfiguration extends FramedGraphConfiguration implements org.openntf.domino.graph2.DConfiguration {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DConfiguration.class.getName());

	public static class DTypedGraphModuleBuilder extends TypedGraphModuleBuilder {
		private DTypeRegistry localTypeRegistry = new DTypeRegistry();

		@Override
		public TypedGraphModuleBuilder withClass(final Class<?> type) {
			localTypeRegistry.add(type);
			return this;
		}

		@Override
		public Module build() {
			final TypeManager manager = new TypeManager(localTypeRegistry);
			return new AbstractModule() {

				@Override
				public void doConfigure(final FramedGraphConfiguration config) {
					config.addTypeResolver(manager);
					config.addFrameInitializer(manager);
				}
			};
		}

		public DTypeRegistry getTypeRegistry() {
			return localTypeRegistry;
		}
	}

	public static class DTypeRegistry extends TypeRegistry {
		private Map<Class<?>, Map<String, Method>> getterMap_ = new FastMap<Class<?>, Map<String, Method>>();
		private Map<Class<?>, Map<String, Method>> setterMap_ = new FastMap<Class<?>, Map<String, Method>>();

		@Override
		public TypeRegistry add(final Class<?> type) {
			Validate.assertArgument(type.isInterface(), "Not an interface: %s", type.getName());
			super.add(type);
			addProperties(type);
			for (Class<?> subtype : type.getClasses()) {
				Annotation annChk = subtype.getAnnotation(TypeValue.class);
				if (annChk != null) {
					add(subtype);
				}
			}
			return this;
		}

		public String getTypeNamesForFrame(final Object element) {
			StringBuilder sb = new StringBuilder();
			for (Class<?> klazz : getTypesForFrame(element)) {
				sb.append(klazz.getSimpleName());
				sb.append(',');
			}
			return sb.toString();
		}

		public Class<?>[] getTypesForFrame(final Object element) {
			return element.getClass().getInterfaces();
		}

		public Map<String, Method> getPropertiesGetters(final Class<?>[] types) {
			Map<String, Method> result = new FastMap<String, Method>();
			for (Class<?> klazz : types) {
				Map<String, Method> crystals = getPropertiesGetters(klazz);
				if (crystals != null) {
					result.putAll(crystals);
				}
			}
			return ((FastMap) result).unmodifiable();
		}

		public Map<String, Method> getPropertiesGetters(final Class<?> type) {
			Map<String, Method> getters = getterMap_.get(type);
			return getters;
		}

		public Map<String, Method> getPropertiesSetters(final Class<?>[] types) {
			Map<String, Method> result = new FastMap<String, Method>();
			for (Class<?> klazz : types) {
				Map<String, Method> crystals = getPropertiesSetters(klazz);
				if (crystals != null) {
					result.putAll(crystals);
				}
			}
			return ((FastMap) result).unmodifiable();
		}

		public Map<String, Method> getPropertiesSetters(final Class<?> type) {
			Map<String, Method> setters = setterMap_.get(type);
			return setters;
		}

		public void addProperties(final Class<?> type) {
			Map<String, Method> getters = new FastMap<String, Method>();
			Map<String, Method> setters = new FastMap<String, Method>();
			Method[] methods = type.getMethods();
			for (Method method : methods) {
				String key = null;
				Annotation typed = method.getAnnotation(TypedProperty.class);
				if (typed != null) {
					key = ((TypedProperty) typed).value();
				} else {
					Annotation property = method.getAnnotation(Property.class);
					if (property != null) {
						key = ((Property) property).value();
					}
				}
				if (key != null) {
					if (ClassUtilities.isGetMethod(method)) {
						getters.put(key, method);
					}
					if (ClassUtilities.isSetMethod(method)) {
						setters.put(key, method);
					}
				}
			}
			getterMap_.put(type, getters);
			setterMap_.put(type, setters);
		}

	}

	private Long defaultElementStoreKey_ = null;
	private Map<Long, DElementStore> elementStoreMap_;
	private Map<Class<?>, Long> typeMap_;
	private transient DGraph graph_;
	private transient Module module_;
	private DTypeRegistry typeRegistry_;

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
		DTypedGraphModuleBuilder typedBuilder = new DTypedGraphModuleBuilder();
		for (DElementStore store : getElementStores().values()) {
			for (Class<?> klazz : store.getTypes()) {
				typedBuilder.withClass(klazz);
			}
		}
		typeRegistry_ = typedBuilder.getTypeRegistry();
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
	public DTypeRegistry getTypeRegistry() {
		return typeRegistry_;
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
