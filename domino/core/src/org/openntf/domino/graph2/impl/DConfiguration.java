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
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.ClassUtilities;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraphConfiguration;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.AbstractModule;
import com.tinkerpop.frames.modules.Module;
import com.tinkerpop.frames.modules.typedgraph.TypeField;
import com.tinkerpop.frames.modules.typedgraph.TypeManager;
import com.tinkerpop.frames.modules.typedgraph.TypeRegistry;
import com.tinkerpop.frames.modules.typedgraph.TypeValue;
import com.tinkerpop.frames.modules.typedgraph.TypedGraphModuleBuilder;
import com.tinkerpop.frames.util.Validate;

public class DConfiguration extends FramedGraphConfiguration implements org.openntf.domino.graph2.DConfiguration {
	@SuppressWarnings("unused")
	private static final Logger log_ = Logger.getLogger(DConfiguration.class.getName());

	public static class DTypedGraphModuleBuilder extends TypedGraphModuleBuilder {
		private DTypeRegistry localTypeRegistry_ = new DTypeRegistry();
		private DTypeManager localManager_;

		@Override
		public TypedGraphModuleBuilder withClass(final Class<?> type) {
			localTypeRegistry_.add(type);
			return this;
		}

		@Override
		public Module build() {
			localManager_ = new DTypeManager(localTypeRegistry_);
			return new AbstractModule() {

				@Override
				public void doConfigure(final FramedGraphConfiguration config) {
					config.addTypeResolver(localManager_);
					config.addFrameInitializer(localManager_);
				}
			};
		}

		public DTypeRegistry getTypeRegistry() {
			return localTypeRegistry_;
		}

		public DTypeManager getTypeManager() {
			return localManager_;
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
				if (annChk != null && subtype.isInterface()) {
					add(subtype);
				}
			}
			return this;
		}

		//		public String getTypeNamesForFrame(final Object element) {
		//			StringBuilder sb = new StringBuilder();
		//			for (Class<?> klazz : getTypesForFrame(element)) {
		//				sb.append(klazz.getSimpleName());
		//				sb.append(',');
		//			}
		//			return sb.toString();
		//		}

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
				boolean derived = false;
				Annotation typed = method.getAnnotation(TypedProperty.class);
				if (typed != null) {
					key = ((TypedProperty) typed).value();
					derived = ((TypedProperty) typed).derived();
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
					if (ClassUtilities.isSetMethod(method) && !derived) {
						setters.put(key, method);
					}
				}
			}
			getterMap_.put(type, getters);
			setterMap_.put(type, setters);
		}
	}

	public static class DTypeManager extends TypeManager {
		private DTypeRegistry typeRegistry_;

		public DTypeManager(final DTypeRegistry typeRegistry) {
			super(typeRegistry);
			typeRegistry_ = typeRegistry;
		}

		@Override
		public Class<?>[] resolveTypes(final Vertex v, final Class<?> defaultType) {
			return new Class<?>[] { resolve(v, defaultType), VertexFrame.class };
		}

		@Override
		public Class<?>[] resolveTypes(final Edge e, final Class<?> defaultType) {
			return new Class<?>[] { resolve(e, defaultType), EdgeFrame.class };
		}

		public Class<?> resolve(final Element e, final Class<?> defaultType) {
			Class<?> typeHoldingTypeField = typeRegistry_.getTypeHoldingTypeField(defaultType);
			if (typeHoldingTypeField != null) {
				String value = e.getProperty(typeHoldingTypeField.getAnnotation(TypeField.class).value());
				Class<?> type = value == null ? null : typeRegistry_.getType(typeHoldingTypeField, value);
				if (type != null) {
					return type;
				}
			}
			return defaultType;
		}

		public Class<?> resolve(final VertexFrame vertex, final Class<?> defaultType) {
			return resolve(vertex.asVertex(), defaultType);
		}

		public Class<?> resolve(final EdgeFrame edge, final Class<?> defaultType) {
			return resolve(edge.asEdge(), defaultType);
		}

		public Class<?> resolve(final VertexFrame vertex) {
			return resolve(vertex.asVertex(), DVertexFrame.class);
		}

		public Class<?> resolve(final EdgeFrame edge) {
			return resolve(edge.asEdge(), DEdgeFrame.class);
		}

	}

	private Long defaultElementStoreKey_ = null;
	private Map<Long, DElementStore> elementStoreMap_;
	private Map<Class<?>, Long> typeMap_;
	private transient DGraph graph_;
	private transient Module module_;
	private DTypeRegistry typeRegistry_;
	private DTypeManager typeManager_;

	public DConfiguration() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public DGraph getGraph() {
		return graph_;
	}

	@Override
	public DGraph setGraph(final DGraph graph) {
		graph_ = graph;
		return graph;
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
	public DElementStore addElementStore(final DElementStore store) {
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
		return store;
	}

	private DTypedGraphModuleBuilder getTypedBuilder() {
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
			DTypedGraphModuleBuilder builder = getTypedBuilder();
			module_ = builder.build();
			typeManager_ = builder.getTypeManager();
		}
		return module_;
	}

	@Override
	public DTypeRegistry getTypeRegistry() {
		return typeRegistry_;
	}

	@Override
	public DTypeManager getTypeManager() {
		return typeManager_;
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
