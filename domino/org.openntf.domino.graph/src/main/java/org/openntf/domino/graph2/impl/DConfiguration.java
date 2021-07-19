/**
 * Copyright © 2013-2021 The OpenNTF Domino API Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

import org.openntf.domino.Document;
import org.openntf.domino.design.impl.DesignFactory;
//import javolution.util.FastMap;
import org.openntf.domino.graph2.DElementStore;
import org.openntf.domino.graph2.DKeyResolver;
import org.openntf.domino.graph2.annotations.Action;
import org.openntf.domino.graph2.annotations.AdjacencyUnique;
import org.openntf.domino.graph2.annotations.AnnotationUtilities;
import org.openntf.domino.graph2.annotations.ComputedProperty;
import org.openntf.domino.graph2.annotations.IncidenceUnique;
import org.openntf.domino.graph2.annotations.Proxied;
import org.openntf.domino.graph2.annotations.Replaces;
import org.openntf.domino.graph2.annotations.Shardable;
import org.openntf.domino.graph2.annotations.TypedProperty;
import org.openntf.domino.graph2.builtin.CategoryVertex;
import org.openntf.domino.graph2.builtin.DEdgeFrame;
import org.openntf.domino.graph2.builtin.DVertexFrame;
import org.openntf.domino.graph2.builtin.DbInfoVertex;
import org.openntf.domino.graph2.builtin.ViewVertex;
import org.openntf.domino.helpers.DocumentScanner;
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

@SuppressWarnings({ "rawtypes", "nls" })
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
				if (typeValue == null) {
					typeDiscriminators.put(new TypeRegistry.TypeDiscriminator(typeHoldingTypeField, type.getCanonicalName()), type);
				} else {
					typeDiscriminators.put(new TypeRegistry.TypeDiscriminator(typeHoldingTypeField, typeValue.value()), type);
				}
				//				System.out.println("TEMP DEBUG Registering internal registry " + typeHoldingTypeField.getName() + " value "
				//						+ typeValue.value() + " in registry " + System.identityHashCode(this) + " to type " + type.getName());
			}

			@Override
			public Class<?> getType(final Class<?> typeHoldingTypeField, final String typeValue) {
				//				System.out.println("TEMP DEBUG Checking internal registry for " + typeHoldingTypeField.getName() + " value " + typeValue
				//						+ " in registry " + System.identityHashCode(this));
				return super.getType(typeHoldingTypeField, typeValue);
			}
		}

		private Map<Class<?>, List<CaseInsensitiveString>> proxiedMap_ = new HashMap<Class<?>, List<CaseInsensitiveString>>();
		private Map<Class<?>, Class<?>> replacesMap_ = new HashMap<Class<?>, Class<?>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> getterMap_ = new HashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> actionMap_ = new HashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> counterMap_ = new HashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> finderMap_ = new HashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> adderMap_ = new HashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> removerMap_ = new HashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> setterMap_ = new HashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> incidenceMap_ = new HashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Map<CaseInsensitiveString, Method>> computedMap_ = new HashMap<Class<?>, Map<CaseInsensitiveString, Method>>();
		private Map<Class<?>, Method> inMap_ = new LinkedHashMap<Class<?>, Method>();
		private Map<Class<?>, Method> outMap_ = new LinkedHashMap<Class<?>, Method>();
		//		private Map<String, String> simpleNameMap_ = new HashMap<String, String>();
		private Map<String, Class<?>> localTypeMap_ = new LinkedHashMap<String, Class<?>>();
		private Map<Class<?>, String> typeValueMap_ = new LinkedHashMap<Class<?>, String>();
		private Map<DElementStore, InternalTypeRegistry> storeTypeMap_ = new LinkedHashMap<DElementStore, InternalTypeRegistry>();
		private DConfiguration config_;

		public DTypeRegistry(final DConfiguration config) {
			config_ = config;
		}

		@Override
		public Class<?> getType(final Class<?> typeHoldingTypeField, final String typeValue) {
			//			if (typeValue == null || typeValue.length() == 0) {
			//				Throwable t = new RuntimeException();
			//				t.printStackTrace();
			//			} else {
			//			}
			Class<?> result = super.getType(typeHoldingTypeField, typeValue);
			if (result == null) {
				result = findClassByName(typeValue);
				//				System.out.println("TEMP DEBUG Falling back to local map for typeValue " + typeValue + " resulting in "
				//						+ String.valueOf(result));
			}
			Class<?> replaceChk = replacesMap_.get(result);
			if (replaceChk != null) {
				//				System.out.println("Replacing class " + result.getName() + " with " + replaceChk.getName());
				result = replaceChk;
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
				result = reg.getType(typeHoldingTypeField, typeValue);
				//				System.out.println("TEMP DEBUG Used local element store for " + typeHoldingTypeField.getName() + " value " + typeValue
				//						+ " in registry " + System.identityHashCode(reg) + " resulting in type "
				//						+ (result == null ? "null" : result.getName()));
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
			if (name == null || name.length() < 1) {
				return null;
			}
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
				Validate.assertArgument(
						parentTypeHoldingTypeField == null || typeHoldingTypeField == null
								|| parentTypeHoldingTypeField == typeHoldingTypeField,
						"You have multiple TypeField annotations in your class-hierarchy for %s", type.getName());
				if (typeHoldingTypeField == null) {
					typeHoldingTypeField = parentTypeHoldingTypeField;
				}
			}
			return typeHoldingTypeField;
		}

		public String getTypeValue(final Class<?> type) {
			return typeValueMap_.get(type);
		}

		@Override
		public TypeRegistry add(final Class<?> type) {
			Validate.assertArgument(type.isInterface(), "Not an interface: %s", type.getName());
			//			Class<?> typeHoldingTypeField = findTypeHoldingTypeField(type);
			try {
				super.add(type);
				Replaces replaces = type.getAnnotation(Replaces.class);
				if (replaces != null) {
					//					System.out.println("TEMP DEBUG Registering replacement class. Replacing " + replaces.value().getName() + " with "
					//							+ type.getName());

					replacesMap_.put(replaces.value(), type);
				}
				localTypeMap_.put(type.getName(), type);
				Annotation valChk = type.getAnnotation(TypeValue.class);
				if (valChk != null) {
					String value = ((TypeValue) valChk).value();
					localTypeMap_.put(value, type);
					//					System.out.println("TEMP DEBUG Registering " + type.getName() + " to a value of " + value);
					typeValueMap_.put(type, value);
				}
				addProperties(type);
				//				System.out.println("TEMP DEBUG Adding type " + type.getName() + " to registry");
				for (Class<?> subtype : type.getClasses()) {
					Annotation annChk = subtype.getAnnotation(TypeValue.class);
					if (annChk != null && subtype.isInterface()) {
						add(subtype);
						//					System.out.println("TEMP DEBUG: TypeHoldingField from " + this.getTypeHoldingTypeField(subtype));
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}

			return this;
		}

		public TypeRegistry add(final Class<?> type, final DElementStore store) {
			Validate.assertArgument(type.isInterface(), "Not an interface: %s", type.getName());
			InternalTypeRegistry reg = storeTypeMap_.get(store);
			if (reg == null) {
				reg = new InternalTypeRegistry();
				storeTypeMap_.put(store, reg);
			}
			try {
				reg.add(type);
				localTypeMap_.put(type.getName(), type);
				Replaces replaces = type.getAnnotation(Replaces.class);
				if (replaces != null) {
					//					System.out.println("TEMP DEBUG Registering replacement class. Replacing " + replaces.value().getName() + " with "
					//							+ type.getName());
					replacesMap_.put(replaces.value(), type);
				}
				Annotation valChk = type.getAnnotation(TypeValue.class);
				if (valChk != null) {
					String value = ((TypeValue) valChk).value();
					localTypeMap_.put(value, type);
					typeValueMap_.put(type, value);
				}
				addProperties(type);
				//				System.out.println("TEMP DEBUG Adding type " + type.getName() + " to registry");
				for (Class<?> subtype : type.getClasses()) {
					Annotation annChk = subtype.getAnnotation(TypeValue.class);
					if (annChk != null && subtype.isInterface()) {
						add(subtype);
					}
				}
			} catch (IllegalArgumentException iae) {
				System.err.println("Unable to register frame type " + type.getName() + ": " + iae.getMessage());
			} catch (Throwable t) {
				t.printStackTrace();
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

		public List<CaseInsensitiveString> getProxied(final Class<?> type) {
			if (type == null) {
				System.out.println("ALERT! Type is null when attempting to get proxied list. This should not be possible!");
				Throwable t = new RuntimeException();
				t.printStackTrace();
				return null;
			}
			//			System.out.println("TEMP DEBUG Getting proxied properties for " + type.getName());
			List<CaseInsensitiveString> result = new ArrayList<CaseInsensitiveString>();
			List<CaseInsensitiveString> localProps = proxiedMap_.get(type);
			if (localProps != null) {
				result.addAll(localProps);
			}
			Class<?>[] inters = type.getInterfaces();
			for (Class<?> inter : inters) {
				localProps = proxiedMap_.get(inter);
				if (localProps != null) {
					result.addAll(localProps);
				}
			}
			return Collections.unmodifiableList(result);
		}

		public Map<CaseInsensitiveString, Method> getActions(final Class<?> type) {
			Map<CaseInsensitiveString, Method> result = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> crystals = actionMap_.get(type);
			if (crystals != null) {
				result.putAll(crystals);
			}
			Class<?>[] inters = type.getInterfaces();
			for (Class<?> inter : inters) {
				crystals = actionMap_.get(inter);
				if (crystals != null) {
					result.putAll(crystals);
				}
			}
			return Collections.unmodifiableMap(result);
		}

		public Map<CaseInsensitiveString, Method> getComputeds(final Class<?> type) {
			Map<CaseInsensitiveString, Method> result = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> crystals = computedMap_.get(type);
			if (crystals != null) {
				result.putAll(crystals);
			}
			Class<?>[] inters = type.getInterfaces();
			for (Class<?> inter : inters) {
				crystals = computedMap_.get(inter);
				if (crystals != null) {
					result.putAll(crystals);
				}
			}
			return Collections.unmodifiableMap(result);
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
			Map<CaseInsensitiveString, Method> actions = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> setters = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> counters = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> finders = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> adders = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> removers = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> incidences = new LinkedHashMap<CaseInsensitiveString, Method>();
			Map<CaseInsensitiveString, Method> computations = new LinkedHashMap<CaseInsensitiveString, Method>();
			List<CaseInsensitiveString> proxies = new ArrayList<CaseInsensitiveString>();
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
				boolean isComputed = false;
				Annotation typed = method.getAnnotation(TypedProperty.class);
				if (typed != null) {
					key = new CaseInsensitiveString(((TypedProperty) typed).value());
					derived = ((TypedProperty) typed).derived();
				} else {
					Annotation computed = method.getAnnotation(ComputedProperty.class);
					if (computed != null) {
						key = new CaseInsensitiveString(((ComputedProperty) computed).value());
						//						System.out.println("TEMP DEBUG adding a computed property for " + key);
						derived = true;
						isComputed = true;
					} else {
						Annotation property = method.getAnnotation(Property.class);
						if (property != null) {
							key = new CaseInsensitiveString(((Property) property).value());
						}
					}
				}
				Annotation proxied = method.getAnnotation(Proxied.class);
				Annotation action = method.getAnnotation(Action.class);
				if (action != null) {
					key = new CaseInsensitiveString(((Action) action).name());
					actions.put(key, method);
				}
				if (key != null) {
					if (ClassUtilities.isGetMethod(method)) {
						getters.put(key, method);
						if (isComputed) {
							computations.put(key, method);
						}
						if (proxied != null) {
							proxies.add(key);
							//							System.out.println("TEMP DEBUG Added a proxied method for " + key + " for type " + type.getName());
						}
					}
					if (ClassUtilities.isSetMethod(method)) {
						if (!derived) {
							setters.put(key, method);
						}
					}
				} else {
					Annotation incidenceUnique = method.getAnnotation(IncidenceUnique.class);
					if (incidenceUnique != null) {
						Direction direction = Direction.OUT;
						Object d = ((IncidenceUnique) incidenceUnique).direction();
						if (d instanceof Direction) {
							direction = (Direction) d;
						}
						key = new CaseInsensitiveString(
								((IncidenceUnique) incidenceUnique).label() + (direction == Direction.IN ? "In" : ""));
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
						key = new CaseInsensitiveString(
								((AdjacencyUnique) adjacencyUnique).label() + (direction == Direction.IN ? "In" : ""));
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
			actionMap_.put(type, actions);
			setterMap_.put(type, setters);
			counterMap_.put(type, counters);
			finderMap_.put(type, finders);
			adderMap_.put(type, adders);
			removerMap_.put(type, removers);
			computedMap_.put(type, computations);
			if (in != null) {
				inMap_.put(type, in);
				//				System.out.println("TEMP DEBUG: registering InVertex method " + in.getName() + " for class " + type.getName());
			}
			if (out != null) {
				outMap_.put(type, out);
			}
			incidenceMap_.put(type, incidences);
			proxiedMap_.put(type, proxies);
		}
	}

	public static class DTypeManager extends TypeManager {
		private DTypeRegistry typeRegistry_;

		public DTypeManager(final DTypeRegistry typeRegistry) {
			super(typeRegistry);
			typeRegistry_ = typeRegistry;
		}

		private Class<?> getDefaultType(final Vertex v) {
			if (v instanceof DCategoryVertex) {
				return CategoryVertex.class;
			}
			if (v instanceof DVertex) {
				Map map = ((DVertex) v).getDelegate();
				if (map instanceof Document) {
					if (DesignFactory.isView((Document) map)) {
						return ViewVertex.class;
					}
					if (DesignFactory.isIcon((Document) map) || DesignFactory.isACL((Document) map)) {
						//						System.out.println("TEMP DEBUG returning a DbInfoVertex");
						return DbInfoVertex.class;
					}
				}
			}
			DConfiguration config = typeRegistry_.config_;
			return config.getDefaultVertexFrameType();
		}

		private Class<?> getDefaultType(final Edge e) {
			if (e instanceof DEdge) {
				if (((DEdge) e).getDelegateType() == org.openntf.domino.ViewEntry.class) {
					return ViewVertex.Contains.class;
				}
			}
			DConfiguration config = typeRegistry_.config_;
			return config.getDefaultEdgeFrameType();
		}

		@Override
		public Class<?>[] resolveTypes(final Vertex v, final Class<?> defaultType) {
			Class<?>[] result = new Class<?>[] {
					resolve(v, defaultType == null || VertexFrame.class.equals(defaultType) ? getDefaultType(v) : defaultType) };
			return result;
		}

		@Override
		public Class<?>[] resolveTypes(final Edge e, final Class<?> defaultType) {
			return new Class<?>[] {
					resolve(e, defaultType == null || EdgeFrame.class.equals(defaultType) ? getDefaultType(e) : defaultType) };
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
					if (e instanceof Edge) {
						type = typeRegistry_.config_.getDefaultEdgeFrameType();
					} else {
						type = typeRegistry_.config_.getDefaultVertexFrameType();
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

		public Class<?> initElement(final Class<?> paramKind, final FramedGraph<?> framedGraph, final Element element,
				final boolean temporary) {
			if (element == null) {
				return null;
			}
			Class<?> kind = paramKind;
			boolean usingDefault = false;
			Class<?> defaultV = typeRegistry_.config_.getDefaultVertexFrameType();
			Class<?> defaultE = typeRegistry_.config_.getDefaultEdgeFrameType();
			if (paramKind == null) {
				//				usingDefault = true;
				if (element instanceof DCategoryVertex) {
					kind = CategoryVertex.class;
				} else if (element instanceof Edge) {
					kind = defaultE;
				} else if (element instanceof Vertex) {
					kind = defaultV;
				} else {
					throw new IllegalArgumentException(
							"element parameter is a " + (element == null ? "null" : element.getClass().getName()));
				}
			}
			usingDefault = (defaultV.equals(kind) || defaultE.equals(kind));
			//			if (element instanceof DProxyVertex) {
			//				System.out.print("TEMP DEBUG Wrapping a DProxyVertex");
			//			}
			//			System.out.println("TEMP DEBUG Wrapping a " + kind.getSimpleName() + " around a " + element.getClass().getSimpleName()
			//					+ " while paramKind was " + String.valueOf(paramKind));
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
					if (element instanceof DCategoryVertex) {
						update = false;
						kind = CategoryVertex.class;
					} else if (current != null) {
						currentVal = TypeUtils.toString(current);
						//						System.out.println("TEMP DEBUG currentVal is " + currentVal);
						if (currentVal.equals(typeValue.value())) {
							update = false;
							//							System.out.println("TEMP DEBUG value already applied: " + typeValue.value());
						} else {
							try {
								classChk = typeRegistry_.getType(typeHoldingTypeField, currentVal);
							} catch (IllegalArgumentException iae) {
								//no problem
							}
							if (classChk == null) {
								if (field.equalsIgnoreCase("form") && currentVal != null && currentVal.length() > 0
										&& !(element instanceof DProxyVertex)) {
									update = false;
								}
							} else if (usingDefault) {
								//								System.out.println("Using default vertex of type: " + defaultV.getName());
								update = false;
								if (classChk != null) {
									kind = classChk;
								}
							} else if (classChk.isAssignableFrom(kind)) {
								update = true;  //we're requesting a higher-level object type, so we should upgrade it.
							} else if (kind.isAssignableFrom(classChk)) {
								update = false; //we're requesting a superclass and should NEVER mess with the value.
							} else {
								update = false;
								//								System.out.println("TEMP DEBUG: existing type value " + classChk.getName()
								//										+ " extends requested type value " + kind.getName());
							}
						}
					} else {
						//						System.out.println("TEMP DEBUG: current value is null in field " + field);
					}
					if (update) {
						if (defaultV.equals(kind) || defaultE.equals(kind) /*|| kind == DEdgeFrame.class || kind == DVertexFrame.class*/
								|| kind == CategoryVertex.class || kind == ViewVertex.class || kind == ViewVertex.Contains.class
								|| kind == DbInfoVertex.class) {
							//							System.out.println("TEMP DEBUG not setting form value because kind is excluded");
						} else {
							//							element.setProperty(field, typeValue.value());
							try {
								boolean isReverseProxy = false;
								if (element instanceof DProxyVertex) {
									if (((DProxyVertex) element).isReverseProxy()) {
										isReverseProxy = true;
									}
								}
								if (element instanceof DElement && !(isReverseProxy)) {
									//									if (!(element instanceof DProxyVertex) || !((DProxyVertex) element).isReverseProxy()) {
									Document doc = ((DElement) element).asDocument();
									doc.replaceItemValue(field, typeValue.value());
									if (!temporary) {
										doc.save();
									}
									//									}
									element.setProperty(field, typeValue.value());
									if (currentVal != null && currentVal.length() > 0) {
										//										System.out.println("TEMP DEBUG Forcing type on document id " + doc.getMetaversalID() + " to "
										//												+ typeValue.value() + ". Was previously " + String.valueOf(currentVal) + " ("
										//												+ (classChk == null ? "null" : classChk.getName()) + ")");
									}
								}
							} catch (Throwable t) {
								throw new RuntimeException("Exception when trying to initialize a " + element.getClass().getName()
										+ " with id " + String.valueOf(element.getId()) + " when trying to frame as " + kind.getName(), t);
							}
							//							if (framedGraph instanceof TransactionalGraph) {
							//								((TransactionalGraph) framedGraph).commit();
							//							}
						}
					}
				} else {
					System.out.println("TEMP DEBUG: type value annotation is null");
				}
			} else {
				System.out.println("TEMP DEBUG: TypeHoldingTypeField is null for type " + kind.getName());
			}

			//			if (kind.getSimpleName().equals("Fact") || kind.getSimpleName().equals("User")) {
			//				System.out.println("TEMP DEBUG initElement() Returning " + kind.getName());
			//			}
			return kind;
		}

		@Override
		public void initElement(final Class<?> kind, final FramedGraph<?> framedGraph, final Element element) {
			initElement(kind, framedGraph, element, false);
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

	private boolean surpressSingleValueCategories_ = true;
	private Long defaultElementStoreKey_ = null;
	private DElementStore defaultElementStore_;
	private Long defaultProxyStoreKey_ = null;
	private DElementStore defaultProxyStore_;
	private Map<Long, DElementStore> elementStoreMap_;
	private Map<Class<?>, Long> typeMap_;
	private transient DGraph graph_;
	private transient Module module_;
	private DTypedGraphModuleBuilder builder_;

	private Class<? extends VertexFrame> defaultVertexFrameType_ = VertexFrame.class;
	private Class<? extends EdgeFrame> defaultEdgeFrameType_ = EdgeFrame.class;
	private Long defaultReverseProxyStoreKey_;
	private DElementStore defaultReverseProxyStore_;

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
	public void setDefaultProxyStore(final Long key) {
		defaultProxyStoreKey_ = key;
	}

	@Override
	public void setDefaultProxyStore(final DElementStore store) {
		defaultProxyStore_ = store;
	}

	@Override
	public void setDefaultReverseProxyStore(final Long key) {
		defaultReverseProxyStoreKey_ = key;
		//		((org.openntf.domino.graph2.impl.DElementStore) store).setReverseProxyStore(true);
	}

	@Override
	public void setDefaultReverseProxyStore(final DElementStore store) {
		defaultReverseProxyStore_ = store;
		((org.openntf.domino.graph2.impl.DElementStore) store).setReverseProxyStore(true);
	}

	@Override
	public DElementStore getDefaultElementStore() {
		if (defaultElementStore_ == null) {
			defaultElementStore_ = getElementStores().get(defaultElementStoreKey_);
		}
		return defaultElementStore_;
	}

	@Override
	public DElementStore getDefaultProxyStore() {
		if (defaultProxyStore_ == null && defaultProxyStoreKey_ != null) {
			defaultProxyStore_ = getElementStores().get(defaultProxyStoreKey_);
		}
		//		if (defaultProxyStore_ == null) {
		//			System.out.println("TEMP DEBUG Sorry there is no default proxy store in this configuration");
		//		}
		return defaultProxyStore_;
	}

	@Override
	public DElementStore getDefaultReverseProxyStore() {
		if (defaultReverseProxyStore_ == null && defaultReverseProxyStoreKey_ != null) {
			defaultReverseProxyStore_ = getElementStores().get(defaultReverseProxyStoreKey_);
		}
		//		if (defaultProxyStore_ == null) {
		//			System.out.println("TEMP DEBUG Sorry there is no default proxy store in this configuration");
		//		}
		return defaultReverseProxyStore_;
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
	public DElementStore addElementStore(final DElementStore store) throws IllegalStateException {
		store.setConfiguration(this);
		Long key = store.getStoreKey();
		DElementStore schk = getElementStores().get(key);
		DElementStore pchk = null;
		DElementStore rpchk = null;
		if (schk == null) {
			getElementStores().put(key, store);
		}
		Long pkey = store.getProxyStoreKey();
		if (pkey != null) {
			pchk = getElementStores().get(pkey);
			if (pchk == null) {
				getElementStores().put(pkey, store);
			}
		}
		Long rpkey = store.getReverseProxyStoreKey();
		if (rpkey != null) {
			rpchk = getElementStores().get(rpkey);
			if (rpchk == null) {
				getElementStores().put(rpkey, store);
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
		if (pchk != null) {
			types = pchk.getTypes();
			for (Class<?> type : types) {
				getTypeRegistry().add(type, pchk);
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
		}
		if (rpchk != null) {
			types = rpchk.getTypes();
			for (Class<?> type : types) {
				getTypeRegistry().add(type, rpchk);
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

	public String getTypeValue(final Class<?> type) {
		return getTypeRegistry().getTypeValue(type);
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
	@SuppressWarnings("unused")
	private DocumentScanner indexScanner_;

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

	@Override
	public boolean isSuppressSingleValueCategories() {
		return surpressSingleValueCategories_;
	}

	@Override
	public void setSuppressSingleValueCategories(final boolean value) {
		surpressSingleValueCategories_ = value;
	}

	@Override
	public Class<?> getDefaultVertexFrameType() {
		return defaultVertexFrameType_;
	}

	@Override
	public void setDefaultVertexFrameType(final Class<? extends VertexFrame> clazz) {
		defaultVertexFrameType_ = clazz;
	}

	@Override
	public Class<?> getDefaultEdgeFrameType() {
		return defaultEdgeFrameType_;
	}

	@Override
	public void setDefaultEdgeFrameType(final Class<? extends EdgeFrame> clazz) {
		defaultEdgeFrameType_ = clazz;
	}

	public Class<?> getReplacementType(final Class<?> requestedType) {
		Class<?> result = getTypeRegistry().replacesMap_.get(requestedType);
		if (result != null) {
			//			System.out.println("TEMP DEBUG Override type " + requestedType.getName() + " with " + result.getName());
		}
		return result;
	}

	//	public DocumentScanner getDocumentScanner() {
	//		return indexScanner_;
	//	}
	//
	//	public void setDocumentScanner(final DocumentScanner indexScanner) {
	//		indexScanner_ = indexScanner;
	//	}

}
