package org.openntf.domino.graph2.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openntf.domino.Document;
//import javolution.util.FastMap;
import org.openntf.domino.graph2.DElementStore;
import org.openntf.domino.graph2.DKeyResolver;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.AnnotationUtilities;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.Shardable;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.CategoryVertex;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.ViewVertex;
import org.openntf.domino.types.CaseInsensitiveString;
import org.openntf.domino.utils.TypeUtils;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.ClassUtilities;
import com.tinkerpop.frames.EdgeFrame;
import com.tinkerpop.frames.FrameInitializer;
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
		private DConfiguration config_;

		public DTypedGraphModuleBuilder(final DConfiguration config) {
			config_ = config;
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
				localTypeRegistry_ = new DTypeRegistry(config_);
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
		private static class InternalTypeRegistry extends TypeRegistry {
			@Override
			protected void registerTypeValue(final Class<?> type, final Class<?> typeHoldingTypeField) {
				TypeValue typeValue = type.getAnnotation(TypeValue.class);
				if (Modifier.isAbstract(type.getModifiers())) {
					typeDiscriminators.put(new TypeRegistry.TypeDiscriminator(typeHoldingTypeField, type.getCanonicalName()), type);
				} else {
					Validate.assertArgument(typeValue != null, "The type does not have a @TypeValue annotation: %s", type.getName());
					typeDiscriminators.put(new TypeRegistry.TypeDiscriminator(typeHoldingTypeField, typeValue.value()), type);
				}
			}
		}

		private Map<Class<?>, Map<CaseInsensitiveString, Method>> getterMap_ = new LinkedHashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> counterMap_ = new LinkedHashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> finderMap_ = new LinkedHashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> adderMap_ = new LinkedHashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> removerMap_ = new LinkedHashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> setterMap_ = new LinkedHashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> incidenceMap_ = new LinkedHashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Method> inMap_ = new LinkedHashMap<Class<?>, Method>();
		private Map<Class<?>, Method> outMap_ = new LinkedHashMap<Class<?>, Method>();
		private Map<String, String> simpleNameMap_ = new HashMap<String, String>();
		private Map<String, Class<?>> localTypeMap_ = new HashMap<String, Class<?>>();
		private Map<DElementStore, InternalTypeRegistry> storeTypeMap_ = new HashMap<DElementStore, InternalTypeRegistry>();
		private DConfiguration config_;

		public DTypeRegistry(final DConfiguration config) {
			config_ = config;
		}

		@Override
		public Class<?> getType(final Class<?> typeHoldingTypeField, final String typeValue) {
			//			System.out.println("TEMP DEBUG Attempting to resolve type name " + typeValue);
			Class<?> result = super.getType(typeHoldingTypeField, typeValue);
			if (result == null) {
				result = findClassByName(typeValue);
				//				System.out.println("TEMP DEBUG Falling back to local map for typeValue " + typeValue + " resulting in "
				//						+ String.valueOf(result));
			}
			return result;
		}

		public Class<?> getType(final Class<?> typeHoldingTypeField, final String typeValue, final Element elem) {
			Class<?> result = null;
			DGraph graph = config_.getGraph();
			DElementStore store = graph.findElementStore(elem);
			TypeRegistry reg = storeTypeMap_.get(store);
			//			System.out.println("TEMP DEBUG Attempting to resolve type name " + typeValue + " with an element");
			if (reg == null) {
				result = getType(typeHoldingTypeField, typeValue);
			} else {
				//				System.out.println("TEMP DEBUG Using local element store registry " + reg.toString());
				result = reg.getType(typeHoldingTypeField, typeValue);
			}
			if (result == null) {
				result = findClassByName(typeValue);
			}
			//			System.out.println("TEMP DEBUG Returning type " + result.getName());
			return result;
		}

		@Override
		public Class<?> getTypeHoldingTypeField(final Class<?> type) {
			if (type == null) {
				throw new IllegalArgumentException("Cannot pass a null type to getTypeHoldingTypeField");
			}
			Class<?> result = super.getTypeHoldingTypeField(type);
			if (result == null) {
				Class<?> doublechk = findTypeHoldingTypeField(type);
				if (doublechk != null) {
					result = doublechk;
				} else {
					//					System.out.println("TEMP DEBUG: Double check failed too?");
				}
			}
			return result;
		}

		public Class<?> findClassByName(final String name) {
			if (name == null || name.length() < 1)
				return null;
			for (Class<?> klazz : typeDiscriminators.values()) {
				if (klazz.getName().equals(name) /*|| klazz.getSimpleName().equalsIgnoreCase(name)*/) {
					//					System.out.println("TEMP DEBUG Matched name " + name + " to type " + klazz.getName());
					return klazz;
				}
			}
			Class<?> result = localTypeMap_.get(name);
			if (result != null) {
				return result;
			}
			//			String fullName = simpleNameMap_.get(name);
			//			if (fullName != null) {
			//				result = localTypeMap_.get(fullName);
			//				if (result != null) {
			//					return result;
			//				}
			//			}
			throw new IllegalArgumentException("No class for " + name + " found in TypeRegistry");
		}

		@Override
		protected Class<?> findTypeHoldingTypeField(final Class<?> type) {
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
			Validate.assertArgument(type.isInterface(), "Not an interface: %s", type.getName());
			//			Class<?> typeHoldingTypeField = findTypeHoldingTypeField(type);
			try {
				super.add(type);
				//				String simpleName = type.getSimpleName();
				//				if (!simpleNameMap_.containsKey(simpleName)) {
				//					simpleNameMap_.put(simpleName, type.getName());
				//				}
				localTypeMap_.put(type.getName(), type);
				Annotation valChk = type.getAnnotation(TypeValue.class);
				if (valChk != null) {
					String value = ((TypeValue) valChk).value();
					localTypeMap_.put(value, type);
				}
				addProperties(type);
				//				System.out.println("TEMP DEBUG Adding type " + type.getName() + " to registry");
			} catch (Throwable t) {
				t.printStackTrace();
			}
			for (Class<?> subtype : type.getClasses()) {
				Annotation annChk = subtype.getAnnotation(TypeValue.class);
				if (annChk != null && subtype.isInterface()) {
					add(subtype);
					//					System.out.println("TEMP DEBUG: TypeHoldingField from " + this.getTypeHoldingTypeField(subtype));
				}
			}
			return this;
		}

		public TypeRegistry add(final Class<?> type, final DElementStore store) {
			Validate.assertArgument(type.isInterface(), "Not an interface: %s", type.getName());
			//			Class<?> typeHoldingTypeField = findTypeHoldingTypeField(type);
			InternalTypeRegistry reg = storeTypeMap_.get(store);
			if (reg == null) {
				reg = new InternalTypeRegistry();
				storeTypeMap_.put(store, reg);
			}
			try {
				reg.add(type);
				//				String simpleName = type.getSimpleName();
				//				if (!simpleNameMap_.containsKey(simpleName)) {
				//					simpleNameMap_.put(simpleName, type.getName());
				//				}
				localTypeMap_.put(type.getName(), type);
				Annotation valChk = type.getAnnotation(TypeValue.class);
				if (valChk != null) {
					String value = ((TypeValue) valChk).value();
					localTypeMap_.put(value, type);
				}
				addProperties(type);
				//				System.out.println("TEMP DEBUG Adding type " + type.getName() + " to registry");
			} catch (Throwable t) {
				t.printStackTrace();
			}

			for (Class<?> subtype : type.getClasses()) {
				Annotation annChk = subtype.getAnnotation(TypeValue.class);
				if (annChk != null && subtype.isInterface()) {
					add(subtype);
				}
			}
			return reg;
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

		public Map<CaseInsensitiveString, Method> getPropertiesGetters(final Class<?> type) {
			Map<CaseInsensitiveString, Method> result = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> crystals = getterMap_.get(type);
			if (crystals != null) {
				result.putAll(crystals);
			}
			Class<?>[] inters = type.getInterfaces();
			for (Class<?> inter : inters) {
				crystals = getterMap_.get(inter);
				if (crystals != null) {
					result.putAll(crystals);
				}
			}
			return result;
		}

		public Map<CaseInsensitiveString, Method> getCounters(final Class<?> type) {
			Map<CaseInsensitiveString, Method> result = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> crystals = counterMap_.get(type);
			if (crystals != null) {
				result.putAll(crystals);
			}
			Class<?>[] inters = type.getInterfaces();
			for (Class<?> inter : inters) {
				crystals = counterMap_.get(inter);
				if (crystals != null) {
					result.putAll(crystals);
				}
			}
			return Collections.unmodifiableMap(result);
		}

		public Map<CaseInsensitiveString, Method> getIncidences(final Class<?> type) {
			Map<CaseInsensitiveString, Method> result = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> crystals = incidenceMap_.get(type);
			if (crystals != null) {
				result.putAll(crystals);
			}
			Class<?>[] inters = type.getInterfaces();
			for (Class<?> inter : inters) {
				crystals = incidenceMap_.get(inter);
				if (crystals != null) {
					result.putAll(crystals);
				}
			}
			return Collections.unmodifiableMap(result);
		}

		public Map<CaseInsensitiveString, Method> getAdders(final Class<?> type) {
			Map<CaseInsensitiveString, Method> result = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> crystals = adderMap_.get(type);
			if (crystals != null) {
				result.putAll(crystals);
			}
			Class<?>[] inters = type.getInterfaces();
			for (Class<?> inter : inters) {
				crystals = adderMap_.get(inter);
				if (crystals != null) {
					result.putAll(crystals);
				}
			}
			return Collections.unmodifiableMap(result);
		}

		public Map<CaseInsensitiveString, Method> getPropertiesSetters(final Class<?> type) {
			Map<CaseInsensitiveString, Method> result = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> crystals = setterMap_.get(type);
			if (crystals != null) {
				result.putAll(crystals);
			}
			Class<?>[] inters = type.getInterfaces();
			for (Class<?> inter : inters) {
				crystals = setterMap_.get(inter);
				if (crystals != null) {
					result.putAll(crystals);
				}
			}
			return Collections.unmodifiableMap(result);
		}

		public void addProperties(final Class<?> type) {
			Map<CaseInsensitiveString, Method> getters = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> setters = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> counters = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> finders = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> adders = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> removers = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> incidences = new LinkedHashMap<CaseInsensitiveString, Method>();
			Method in = null;
			Method out = null;
			Method[] methods = type.getMethods();
			for (Method method : methods) {
				if (EdgeFrame.class.isAssignableFrom(type)) {
					Annotation inA = method.getAnnotation(InVertex.class);
					if (inA != null) {
						in = method;
					}
					Annotation outA = method.getAnnotation(OutVertex.class);
					if (outA != null) {
						out = method;
					}
				}
				CaseInsensitiveString key = null;
				boolean derived = false;
				Annotation typed = method.getAnnotation(TypedProperty.class);
				if (typed != null) {
					key = new CaseInsensitiveString(((TypedProperty) typed).value());
					derived = ((TypedProperty) typed).derived();
				} else {
					Annotation property = method.getAnnotation(Property.class);
					if (property != null) {
						key = new CaseInsensitiveString(((Property) property).value());
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
						Direction direction = ((IncidenceUnique) incidenceUnique).direction();
						key = new CaseInsensitiveString(((IncidenceUnique) incidenceUnique).label()
								+ (direction == Direction.IN ? "In" : ""));
						if (ClassUtilities.isGetMethod(method)) {
							incidences.put(key, method);
						}
					}
					Annotation incidence = method.getAnnotation(Incidence.class);
					if (incidence != null) {
						Direction direction = ((Incidence) incidence).direction();
						key = new CaseInsensitiveString(((Incidence) incidence).label() + (direction == Direction.IN ? "In" : ""));
						if (ClassUtilities.isGetMethod(method)) {
							incidences.put(key, method);
						}
					}
					Annotation adjacencyUnique = method.getAnnotation(AdjacencyUnique.class);
					if (adjacencyUnique != null) {
						Direction direction = ((AdjacencyUnique) adjacencyUnique).direction();
						key = new CaseInsensitiveString(((AdjacencyUnique) adjacencyUnique).label()
								+ (direction == Direction.IN ? "In" : ""));
					}
					Annotation adjacency = method.getAnnotation(Adjacency.class);
					if (adjacency != null) {
						Direction direction = ((Adjacency) adjacency).direction();
						key = new CaseInsensitiveString(((Adjacency) adjacency).label() + (direction == Direction.IN ? "In" : ""));
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
			if (in != null) {
				inMap_.put(type, in);
				//				System.out.println("TEMP DEBUG: registering InVertex method " + in.getName() + " for class " + type.getName());
			}
			if (out != null) {
				outMap_.put(type, out);
			}
			incidenceMap_.put(type, incidences);
		}
	}

	public static class DTypeManager extends TypeManager {
		private DTypeRegistry typeRegistry_;

		public DTypeManager(final DTypeRegistry typeRegistry) {
			super(typeRegistry);
			typeRegistry_ = typeRegistry;
		}

		private Class<?> getDefaultType(final Vertex v) {
			if (v instanceof DVertex) {
				if ("1".equals(((DVertex) v).getProperty("$FormulaClass", String.class)))
					return ViewVertex.class;
				if (v instanceof DCategoryVertex)
					return CategoryVertex.class;
			}
			return DVertexFrame.class;
		}

		private Class<?> getDefaultType(final Edge e) {
			if (e instanceof DEdge) {
				if (((DEdge) e).getDelegateType() == org.openntf.domino.ViewEntry.class) {
					return ViewVertex.Contains.class;
				}
			}
			return DEdgeFrame.class;
		}

		@Override
		public Class<?>[] resolveTypes(final Vertex v, final Class<?> defaultType) {
			Class<?>[] result = new Class<?>[] { resolve(v,
					defaultType == null || VertexFrame.class.equals(defaultType) ? getDefaultType(v) : defaultType) };
			return result;
		}

		@Override
		public Class<?>[] resolveTypes(final Edge e, final Class<?> defaultType) {
			return new Class<?>[] { resolve(e, defaultType == null || EdgeFrame.class.equals(defaultType) ? getDefaultType(e) : defaultType) };
		}

		public Class<?> resolve(final Element e, final Class<?> defaultType) {
			//			System.out.println("Resolving element with default type " + defaultType.getName());
			Class<?> result = defaultType;
			Class<?> typeHoldingTypeField = typeRegistry_.getTypeHoldingTypeField(defaultType);
			if (typeHoldingTypeField != null) {
				String value = ((DElement) e).getProperty(typeHoldingTypeField.getAnnotation(TypeField.class).value(), String.class);
				//				System.out.println("TEMP DEBUG: Found type value: " + (value == null ? "null" : value));
				Class<?> type = null;
				try {
					type = value == null ? null : typeRegistry_.getType(typeHoldingTypeField, value, e);
				} catch (Throwable t) {
					if (defaultType == DVertexFrame.class) {
						type = DVertexFrame.class;
					}
				}
				if (type != null) {
					//					System.out.println("TEMP DEBUG: Returning type: " + type.getName());
					if (type.getSimpleName().equalsIgnoreCase(defaultType.getSimpleName())) {
						//						System.out.println("Simple name collision on vertex for name " + defaultType.getSimpleName()
						//								+ ". Using requested type: " + defaultType.getName());
						result = defaultType;
					} else {
						result = type;
					}
				}
			}
			//			System.out.println("TEMP DEBUG returning type " + result.getName());
			return result;
		}

		@Override
		public void initElement(Class<?> kind, final FramedGraph<?> framedGraph, final Element element) {
			if (element == null)
				return;
			if (kind == null) {
				if (element instanceof Edge) {
					kind = getDefaultType((Edge) element);
				} else if (element instanceof Vertex) {
					kind = getDefaultType((Vertex) element);
				} else {
					throw new IllegalArgumentException("element parameter is a "
							+ (element == null ? "null" : element.getClass().getName()));
				}
			}
			//			System.out.println("TEMP DEBUG: Initing an element with kind: " + kind.getName());
			Class<?> typeHoldingTypeField = typeRegistry_.getTypeHoldingTypeField(kind);
			if (typeHoldingTypeField != null) {
				TypeValue typeValue = kind.getAnnotation(TypeValue.class);
				if (typeValue != null) {
					//					System.out.println("TEMP DEBUG TypeValue is " + typeValue.value());
					String field = typeHoldingTypeField.getAnnotation(TypeField.class).value();
					Object current = element.getProperty(field);
					String currentVal = null;
					Class<?> classChk = null;
					boolean update = true;
					if (current != null) {
						currentVal = TypeUtils.toString(current);
						//						System.out.println("TEMP DEBUG currentVal is " + currentVal);
						if (currentVal.equals(typeValue.value())) {
							update = false;
							//							System.out.println("TEMP DEBUG value already applied: " + typeValue.value());
						} else {
							classChk = typeRegistry_.getType(typeHoldingTypeField, currentVal);
							//							System.out.println("TEMP DEBUG: Registry returned " + (classChk == null ? "null" : classChk.getName())
							//									+ " for name of " + currentVal);
							if (classChk == null) {
								if (field.equalsIgnoreCase("form") && currentVal != null && currentVal.length() > 0
										&& !(element instanceof DProxyVertex)) {
									//									System.out.print("TEMP DEBUG Not changing a form value of " + currentVal
									//											+ " even though we're looking for type " + typeValue.value() + " on an Element of type "
									//											+ element.getClass().getName());
									update = false;
								}
							} else if (!kind.isAssignableFrom(classChk)) {
								update = !(currentVal).equals(typeValue.value());
								if (!update) {
									//									System.out.println("TEMP DEBUG Not updating form because value is already " + currentVal);
								}
							} else if (kind.isAssignableFrom(classChk)) {
								update = false;
							} else {
								update = false;
								//							System.out.println("TEMP DEBUG: existing type value " + classChk.getName() + " extends requested type value "
								//									+ kind.getName());
							}
						}
					} else {
						//						System.out.println("TEMP DEBUG: current value is null in field " + field);
					}
					if (update) {
						if (kind == DEdgeFrame.class || kind == DVertexFrame.class || kind == ViewVertex.class
								|| kind == ViewVertex.Contains.class) {
							//							System.out.println("TEMP DEBUG not setting form value because kind is excluded");
						} else {
							//							element.setProperty(field, typeValue.value());
							if (element instanceof DElement) {
								Document doc = ((DElement) element).asDocument();
								doc.replaceItemValue(field, typeValue.value());
								doc.save();
								element.setProperty(field, typeValue.value());
								if (currentVal != null && currentVal.length() > 0) {
									//									System.out.println("TEMP DEBUG Forcing type on document id " + doc.getMetaversalID() + " to "
									//											+ typeValue.value() + ". Was previously " + String.valueOf(currentVal) + " ("
									//											+ (classChk == null ? "null" : classChk.getName()) + ")");
								}
							}
							//							if (framedGraph instanceof TransactionalGraph) {
							//								((TransactionalGraph) framedGraph).commit();
							//							}
						}
					}
				} else {
					//					System.out.println("TEMP DEBUG: type value annotation is null");
				}
			} else {
				//				System.out.println("TEMP DEBUG: TypeHoldingTypeField is null for type " + kind.getName());
			}

		}

		public Class<?> resolve(final VertexFrame vertex, final Class<?> defaultType) {
			return resolve(vertex.asVertex(), defaultType);
		}

		public Class<?> resolve(final EdgeFrame edge, final Class<?> defaultType) {
			return resolve(edge.asEdge(), defaultType);
		}

		public Class<?> resolve(final VertexFrame vertex) {
			return resolve(vertex.asVertex(), getDefaultType(vertex.asVertex()));
		}

		public Class<?> resolve(final EdgeFrame edge) {
			return resolve(edge.asEdge(), getDefaultType(edge.asEdge()));
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
	public List<FrameInitializer> getFrameInitializers() {
		List<FrameInitializer> result = super.getFrameInitializers();
		//		System.out.println("FrameInitializers requested. Result has " + result.size() + " elements");
		return result;
	}

	@Override
	public DElementStore addElementStore(final DElementStore store) {
		store.setConfiguration(this);
		Long key = store.getStoreKey();
		DElementStore schk = getElementStores().get(key);
		if (schk == null) {
			getElementStores().put(key, store);
		}
		Long pkey = store.getProxyStoreKey();
		if (pkey != null) {
			DElementStore pchk = getElementStores().get(pkey);
			if (pchk == null) {
				getElementStores().put(pkey, store);
			}
		}
		List<Class<?>> types = store.getTypes();
		for (Class<?> type : types) {
			getTypeRegistry().add(type, store);
			Long chk = getTypeMap().get(type);
			if (chk != null) {
				if (!chk.equals(key)) {
					Shardable s = type.getAnnotation(Shardable.class);
					if (s == null) {
						throw new IllegalStateException("Element store has already been registered for type " + type.getName());
					} else {
						getTypeMap().put(type, key);
					}
				}
			} else {
				getTypeMap().put(type, key);
			}
		}
		return store;
	}

	private DTypedGraphModuleBuilder getTypedBuilder() {
		if (builder_ == null) {
			builder_ = new DTypedGraphModuleBuilder(this);
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

	private Map<Class<?>, DKeyResolver> keyResolverMap_;

	protected Map<Class<?>, DKeyResolver> getResolverMap() {
		if (keyResolverMap_ == null) {
			keyResolverMap_ = new HashMap<Class<?>, DKeyResolver>();
		}
		return keyResolverMap_;
	}

	@Override
	public void addKeyResolver(final DKeyResolver resolver) {
		for (Class<?> type : resolver.getTypes()) {
			getResolverMap().put(type, resolver);
		}
	}

	@Override
	public DKeyResolver getKeyResolver(final Class<?> type) {
		return getResolverMap().get(type);
	}

}
