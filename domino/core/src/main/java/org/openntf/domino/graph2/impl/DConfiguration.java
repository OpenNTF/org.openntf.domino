package org.openntf.domino.graph2.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//import javolution.util.FastMap;
import org.openntf.domino.graph2.DElementStore;
import org.openntf.domino.graph2.annotations.AnnotationUtilities;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.ClassUtilities;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphConfiguration;
import com.tinkerpop.frames.InVertex;
import com.tinkerpop.frames.Incidence;
import com.tinkerpop.frames.OutVertex;
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
		private DTypeRegistry localTypeRegistry_;
		private DTypeManager localManager_;

		public DTypedGraphModuleBuilder() {
			getTypeRegistry().add(DEdgeFrame.class);
			getTypeRegistry().add(DVertexFrame.class);

		}

		@Override
		public TypedGraphModuleBuilder withClass(final Class<?> type) {
			getTypeRegistry().add(type);
			return this;
		}

		@Override
		public Module build() {
			return new AbstractModule() {
				@Override
				public void doConfigure(final FramedGraphConfiguration config) {
					config.addTypeResolver(getTypeManager());
				}
			};
		}

		public DTypeRegistry getTypeRegistry() {
			if (localTypeRegistry_ == null) {
				localTypeRegistry_ = new DTypeRegistry();
			}
			return localTypeRegistry_;
		}

		public DTypeManager getTypeManager() {
			if (localManager_ == null) {
				localManager_ = new DTypeManager(getTypeRegistry());
			}
			return localManager_;
		}
	}

	public static class DTypeRegistry extends TypeRegistry {
		private Map<Class<?>, Map<String, Method>> getterMap_ = new LinkedHashMap<Class<?>, Map<String, Method>>();
		private Map<Class<?>, Map<String, Method>> counterMap_ = new LinkedHashMap<Class<?>, Map<String, Method>>();
		private Map<Class<?>, Map<String, Method>> finderMap_ = new LinkedHashMap<Class<?>, Map<String, Method>>();
		private Map<Class<?>, Map<String, Method>> adderMap_ = new LinkedHashMap<Class<?>, Map<String, Method>>();
		private Map<Class<?>, Map<String, Method>> removerMap_ = new LinkedHashMap<Class<?>, Map<String, Method>>();
		private Map<Class<?>, Map<String, Method>> setterMap_ = new LinkedHashMap<Class<?>, Map<String, Method>>();
		private Map<Class<?>, Method> inMap_ = new LinkedHashMap<Class<?>, Method>();
		private Map<Class<?>, Method> outMap_ = new LinkedHashMap<Class<?>, Method>();

		//		private Map<Class<?>, Class<?>> inTypeMap_ = new LinkedHashMap<Class<?>, Class<?>>();
		//		private Map<Class<?>, Class<?>> outTypeMap_ = new LinkedHashMap<Class<?>, Class<?>>();
		private Map<Class<?>, List<String>> incidenceMap_ = new LinkedHashMap<Class<?>, List<String>>();

		public DTypeRegistry() {
		}

		@Override
		public Class<?> getTypeHoldingTypeField(final Class<?> type) {
			Class<?> result = super.getTypeHoldingTypeField(type);
			if (result == null) {
				Class<?> doublechk = findTypeHoldingTypeField(type);
				if (doublechk != null) {
					System.out.println("But we found it in " + doublechk.getName() + " in registry " + System.identityHashCode(this));
				} else {
					System.out.println("Double check failed too?");
				}
			}
			return result;
		}

		private Class<?> findTypeHoldingTypeField(final Class<?> type) {
			Class<?> typeHoldingTypeField = type.getAnnotation(TypeField.class) == null ? null : type;
			for (Class<?> parentType : type.getInterfaces()) {
				Class<?> parentTypeHoldingTypeField = findTypeHoldingTypeField(parentType);
				Validate.assertArgument(parentTypeHoldingTypeField == null || typeHoldingTypeField == null
						|| parentTypeHoldingTypeField == typeHoldingTypeField,
						"You have multiple TypeField annotations in your class-hierarchy for %s", type.getName());
				if (typeHoldingTypeField == null)
					typeHoldingTypeField = parentTypeHoldingTypeField;
			}
			return typeHoldingTypeField;
		}

		@Override
		public TypeRegistry add(final Class<?> type) {
			//			System.out.println("Registering " + type.getName() + " in registry " + this.getClass().getName() + ": "
			//					+ System.identityHashCode(this));
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

		public Class<?> getInType(final Class<?> type) {
			Method crystal = getIn(type);
			if (crystal != null) {
				return crystal.getReturnType();
			}
			return null;
		}

		public Class<?> getOutType(final Class<?> type) {
			Method crystal = getOut(type);
			if (crystal != null) {
				return crystal.getReturnType();
			}
			return null;
		}

		public Method getIn(final Class<?> type) {
			return inMap_.get(type);

		}

		public Method getOut(final Class<?> type) {
			return outMap_.get(type);
		}

		public Map<String, Method> getPropertiesGetters(final Class<?>[] types) {
			Map<String, Method> result = new LinkedHashMap<String, Method>();
			for (Class<?> klazz : types) {
				Map<String, Method> crystals = getPropertiesGetters(klazz);
				if (crystals != null) {
					result.putAll(crystals);
				}
			}
			return Collections.unmodifiableMap(result);
		}

		public Map<String, Method> getPropertiesGetters(final Class<?> type) {
			Map<String, Method> getters = getterMap_.get(type);
			return getters;
		}

		public Map<String, Method> getCounters(final Class<?>[] types) {
			Map<String, Method> result = new LinkedHashMap<String, Method>();
			for (Class<?> klazz : types) {
				Map<String, Method> crystals = getCounters(klazz);
				if (crystals != null) {
					result.putAll(crystals);
				}
			}
			return Collections.unmodifiableMap(result);
		}

		public Map<String, Method> getCounters(final Class<?> type) {
			Map<String, Method> counters = counterMap_.get(type);
			return counters;
		}

		public Map<String, Method> getPropertiesSetters(final Class<?>[] types) {
			Map<String, Method> result = new LinkedHashMap<String, Method>();
			for (Class<?> klazz : types) {
				Map<String, Method> crystals = getPropertiesSetters(klazz);
				if (crystals != null) {
					result.putAll(crystals);
				}
			}
			return Collections.unmodifiableMap(result);
		}

		public Map<String, Method> getPropertiesSetters(final Class<?> type) {
			Map<String, Method> setters = setterMap_.get(type);
			return setters;
		}

		public void addProperties(final Class<?> type) {
			Map<String, Method> getters = new LinkedHashMap<String, Method>();
			Map<String, Method> setters = new LinkedHashMap<String, Method>();
			Map<String, Method> counters = new LinkedHashMap<String, Method>();
			Map<String, Method> finders = new LinkedHashMap<String, Method>();
			Map<String, Method> adders = new LinkedHashMap<String, Method>();
			Map<String, Method> removers = new LinkedHashMap<String, Method>();
			List<String> incidences = new ArrayList<String>();
			Method in = null;
			Method out = null;
			Method[] methods = type.getMethods();
			for (Method method : methods) {
				if (EdgeFrame.class.isAssignableFrom(type)) {
					Annotation inA = method.getAnnotation(InVertex.class);
					if (inA != null)
						in = method;
					Annotation outA = method.getAnnotation(OutVertex.class);
					if (outA != null)
						out = method;
				}
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
				} else {
					Annotation incidenceUnique = method.getAnnotation(IncidenceUnique.class);
					if (incidenceUnique != null) {
						key = ((IncidenceUnique) incidenceUnique).label();
						if (ClassUtilities.isGetMethod(method)) {
							incidences.add(key);
						}
					}
					Annotation incidence = method.getAnnotation(Incidence.class);
					if (incidence != null) {
						key = ((Incidence) incidence).label();
						if (ClassUtilities.isGetMethod(method)) {
							incidences.add(key);
						}
					}
				}
				if (key != null) {
					if (AnnotationUtilities.isCountMethod(method)) {
						counters.put(key, method);
					}
					if (AnnotationUtilities.isFindMethod(method)) {
						finders.put(key, method);
					}
					if (ClassUtilities.isAddMethod(method)) {
						adders.put(key, method);
					}
					if (ClassUtilities.isRemoveMethod(method)) {
						removers.put(key, method);
					}
				}
			}
			getterMap_.put(type, getters);
			setterMap_.put(type, setters);
			counterMap_.put(type, counters);
			finderMap_.put(type, finders);
			adderMap_.put(type, adders);
			removerMap_.put(type, removers);
			inMap_.put(type, in);
			outMap_.put(type, out);
			incidenceMap_.put(type, incidences);
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
			return new Class<?>[] { resolve(v, defaultType), DVertexFrame.class };
		}

		@Override
		public Class<?>[] resolveTypes(final Edge e, final Class<?> defaultType) {
			return new Class<?>[] { resolve(e, defaultType), DEdgeFrame.class };
		}

		public Class<?> resolve(final Element e, final Class<?> defaultType) {
			//			System.out.println("Resolving element with default type " + defaultType.getName());
			Class<?> typeHoldingTypeField = typeRegistry_.getTypeHoldingTypeField(defaultType);
			if (typeHoldingTypeField != null) {
				String value = e.getProperty(typeHoldingTypeField.getAnnotation(TypeField.class).value());
				//				System.out.println("Found type value: " + (value == null ? "null" : value));
				Class<?> type = value == null ? null : typeRegistry_.getType(typeHoldingTypeField, value);
				if (type != null) {
					//					System.out.println("Returning type: " + type.getName());
					return type;
				}
			}
			return defaultType;
		}

		@Override
		public void initElement(final Class<?> kind, final FramedGraph<?> framedGraph, final Element element) {
			Class<?> typeHoldingTypeField = typeRegistry_.getTypeHoldingTypeField(kind);
			if (typeHoldingTypeField != null) {
				TypeValue typeValue = kind.getAnnotation(TypeValue.class);
				if (typeValue != null) {
					String field = typeHoldingTypeField.getAnnotation(TypeField.class).value();
					Object current = element.getProperty(field);
					boolean update = true;
					if (current != null) {
						if (current instanceof String) {
							update = !((String) current).equals(typeValue.value());
						}
					}
					if (update) {
						element.setProperty(field, typeValue.value());
					}
				}
			}
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
	private DElementStore defaultElementStore_;
	private Map<Long, DElementStore> elementStoreMap_;
	private Map<Class<?>, Long> typeMap_;
	private transient DGraph graph_;
	private transient Module module_;
	private DTypedGraphModuleBuilder builder_;

	//	private DTypeRegistry typeRegistry_;
	//	private DTypeManager typeManager_;

	public DConfiguration() {
		getTypedBuilder();
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
			typeMap_ = new HashMap<Class<?>, Long>();
		}
		return typeMap_;
	}

	@Override
	public void setDefaultElementStore(final Long key) {
		defaultElementStoreKey_ = key;
	}

	@Override
	public void setDefaultElementStore(final DElementStore store) {
		defaultElementStore_ = store;
	}

	@Override
	public DElementStore getDefaultElementStore() {
		if (defaultElementStore_ == null) {
			defaultElementStore_ = getElementStores().get(defaultElementStoreKey_);
		}
		return defaultElementStore_;
	}

	@Override
	public Map<Long, DElementStore> getElementStores() {
		if (elementStoreMap_ == null) {
			elementStoreMap_ = new HashMap<Long, DElementStore>();
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
			getTypeRegistry().add(type);
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
		if (builder_ == null) {
			builder_ = new DTypedGraphModuleBuilder();
			for (DElementStore store : getElementStores().values()) {
				for (Class<?> klazz : store.getTypes()) {
					builder_.withClass(klazz);
				}
			}
		}
		return builder_;
	}

	@Override
	public Module getModule() {
		if (module_ == null) {
			module_ = getTypedBuilder().build();
		}
		return module_;
	}

	@Override
	public DTypeRegistry getTypeRegistry() {
		return getTypedBuilder().getTypeRegistry();
	}

	@Override
	public DTypeManager getTypeManager() {
		return getTypedBuilder().getTypeManager();
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
